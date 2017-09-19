/**
 * Copyright (C) 2011 LShift Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.lshift.accent;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Component for reliable message publishing. Utilises the publisher acknowledgements feature made available
 * in RabbitMQ 2.3 and higher.
 */
public class AccentConfirmPublisher implements ChannelListener {
  private final Object channelIdLock = new Object();
  private final ChannelFeedbackListener feedbackListener = new ChannelFeedbackListener();
  private final AccentChannel owner;

  public AccentConfirmPublisher(AccentChannel owner) {
    this.owner = owner;
    this.owner.addChannelSetupListener(this);
  }

  @Override public void channelCreated(Channel c) throws IOException {
    // Enable publisher confirmations on channel
    c.confirmSelect();

    // Add listeners to allow us to process publisher acks
    c.addConfirmListener(feedbackListener);
  }

  @Override public void channelLost() {
    // Indicate that all publish operations have failed
    feedbackListener.failAll();
  }

  /**
   * Attempt to reliably publish the given message to the given exchange. If this method returns, it is guaranteed
   * that the message is either completely delivered or that responsibility has been transferred to the broker. If an
   * exception is thrown, the message is not guaranteed to have been sent (though it is possible that it may have been
   * due to a possible communication failure just after delivery completed, but before acknowledgement was received).
   *
   * @param exchangeName
   * @param routingKey
   * @param props
   * @param data
   */
  public void reliablePublish(final String exchangeName, final String routingKey, final AMQP.BasicProperties props, final byte[] data) {
    while (true) {
      Exception res = owner.executeWhenChannelValid(new ChannelCallback() {
        @Override
        public void runWithChannel(Channel channel) throws IOException {
          while (true) {
            FeedbackHandle feedback;

            synchronized (channelIdLock) {
              // Create a handle to receive feedback, then publish the message. Lock this to the channel so that we can
              // ensure the seqNo and the publish are the same ones.
              feedback = feedbackListener.createFeedbackHandle(channel.getNextPublishSeqNo());
              channel.basicPublish(exchangeName, routingKey, props, data);
            }

            // Once the feedback is positive, we can stop resending
            if (feedback.await()) break;
          }
        }
      });

      if (res == null) break;
    }
  }

  private class ChannelFeedbackListener implements ConfirmListener {
    private final List<FeedbackHandle> waiting = new ArrayList<FeedbackHandle>();

    /**
     * Makes the given seqNo a candidate for receiving feedback.
     * @param seqNo the sequence number.
     */
    public FeedbackHandle createFeedbackHandle(long seqNo) {
      synchronized (waiting) {
        FeedbackHandle handle = new FeedbackHandle(seqNo);
        waiting.add(handle);

        return handle;
      }
    }

    @Override
    public void handleAck(long seqNo, boolean multiple) throws IOException {
      applyResult(FeedbackResult.SUCCESS, seqNo, multiple);
    }

    @Override
    public void handleNack(long seqNo, boolean multiple) throws IOException {
      applyResult(FeedbackResult.FAILURE, seqNo, multiple);
    }

    public void failAll() {
      synchronized (waiting) {
        for (FeedbackHandle handle : waiting) {
          handle.setResult(FeedbackResult.FAILURE);
        }
        waiting.clear();
      }
    }

    private void applyResult(FeedbackResult result, long seqNo, boolean multiple) {
      synchronized (waiting) {
        List<FeedbackHandle> toRemove = new ArrayList<FeedbackHandle>();

        for (FeedbackHandle handle : waiting) {
          if (handle.ackApplies(seqNo, multiple)) {
            handle.setResult(result);
            toRemove.add(handle);
          }
        }
        waiting.removeAll(toRemove);
      }
    }
  }

  private class FeedbackHandle {
    public final long seqNo;
    private final Object lock = new Object();
    private FeedbackResult result = FeedbackResult.NONE;

    private FeedbackHandle(long seqNo) {
      this.seqNo = seqNo;
    }

    public boolean await() {
      synchronized (lock) {
        while (result == FeedbackResult.NONE) {
          try {
            lock.wait();
          } catch (InterruptedException e) {
            throw new UnrecoverableMessagingException(e);
          }
        }

        return result == FeedbackResult.SUCCESS;
      }
    }

    public void setResult(FeedbackResult result) {
      synchronized (lock) {
        this.result = result;
        lock.notifyAll();
      }
    }

    public boolean ackApplies(long ackSeqNo, boolean ackMultiple) {
      return (ackSeqNo == seqNo || (ackMultiple && ackSeqNo > seqNo));
    }
  }

  private enum FeedbackResult {
    NONE, SUCCESS, FAILURE
  }
}
