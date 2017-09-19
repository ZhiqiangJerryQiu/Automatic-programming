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

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

/**
 * Reliable Consumer component for ensuring that a consumer survives connection restarts.
 */
public class AccentConsumer implements ChannelListener, Closeable {

  /**
   * Due to the fact that we have to call basicConsume/4, we want to turn auto-acks off.
   */
  private final static boolean AUTO_ACK = false;

  private final AccentChannel owner;
  private final String queue;
  private final Consumer consumer;
  private String consumerTag = null;
  private ConsumerDelegate delegate;
  private boolean hasUnderlyingConsume = false;

  // Not thread safe
  private long deliveryTagCounter = 0L;
  // Not thread safe
  private Map<Long, Long> outstandingAcks = new HashMap<Long, Long>();

  public AccentConsumer(AccentChannel owner, String queue, Consumer consumer) {
    this.owner = owner;
    this.queue = queue;
    this.consumer = consumer;

    this.delegate = new ConsumerDelegate(consumer);
    this.consumerTag = UUID.randomUUID().toString();

    this.owner.addChannelSetupListener(this);
  }

  /**
   * Retrieves the consumer tag that will be used by this consumer. This tag is allocated upon creation of the AccentConsumer,
   * and will remain constant throughout reconnections.
   * @return the consumer tag.
   */
  public String getConsumerTag() {
    return consumerTag;
  }

  @Override public void channelCreated(Channel c) throws IOException {
    c.basicConsume(queue, AUTO_ACK, consumerTag, delegate);
    hasUnderlyingConsume = true;
  }

  @Override public void channelLost() {
    // Clear the outstanding acks, since they are now meaningless
    outstandingAcks.clear();

    // We also don't have a consume anymore, so we don't need to cancel anything if we get closed before we
    // re-establish.
    hasUnderlyingConsume = false;
  }

  @Override public void close() throws IOException {
    this.owner.removeChannelSetupListener(this);

    // Attempt to cancel once. Failures will result in the channel being recreated, so it isn't necessary to retry.
    this.owner.executeIfChannelValid(new ChannelCallback() {
      public void runWithChannel(Channel c) throws IOException {
        if (hasUnderlyingConsume) {
          c.basicCancel(consumerTag);
          hasUnderlyingConsume = false;
        }
      }
    });
  }

  /**
   * Acknowledge a message with the given id.
   *
   * @param deliveryTag
   * @param batch
   * @throws StaleMessageException if the message could not be acked (for example, if the channel had been reset in the meantime).
   */
  public void reliableAck(final long deliveryTag, final boolean batch) {
    final Long channelDeliveryTag = outstandingAcks.get(deliveryTag);
    if (channelDeliveryTag != null) {
      owner.executeIfChannelValid(new ChannelCallback() {
        public void runWithChannel(Channel c) throws IOException {
          c.basicAck(channelDeliveryTag, batch);
          outstandingAcks.remove(deliveryTag);
        }
      });
    } else {
      throw new StaleMessageException(deliveryTag);
    }
  }

  /**
   * Reject a message with the given id.
   *
   * @param deliveryTag
   * @param requeue
   * @throws StaleMessageException if the message could not be rejected (for example, if the channel had been reset in the meantime).
   */
  public void reliableReject(final long deliveryTag, final boolean requeue) {
    final Long channelDeliveryTag = outstandingAcks.get(deliveryTag);
    if (channelDeliveryTag != null) {
      owner.executeIfChannelValid(new ChannelCallback() {
        public void runWithChannel(Channel c) throws IOException {
          c.basicReject(channelDeliveryTag, requeue);
          outstandingAcks.remove(deliveryTag);
        }
      });
    } else {
      throw new StaleMessageException(deliveryTag);
    }
  }

  /**
   * Wrapping delegate that is used internally to re-write delivery tags across channel re-starts.
   */
  private class ConsumerDelegate implements Consumer {

    Consumer underlyingConsumer;

    ConsumerDelegate(Consumer c) {
      underlyingConsumer = c;
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
      // TODO: Do we need to propogate an error to start re-consuming?
      underlyingConsumer.handleCancel(consumerTag);
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
      underlyingConsumer.handleConsumeOk(consumerTag);
    }

    @Override
    public void handleCancelOk(String consumerTag) {
      underlyingConsumer.handleCancelOk(consumerTag);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
      // Don't proxy shutdown signals to the underlying consumer, since we don't want our connection failures
      // to cause the consumer implementation to shut down.
    }

    @Override
    public void handleRecoverOk() {
      underlyingConsumer.handleRecoverOk();
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
      // Allocate our internal delivery tag, and then map it to the original delivery tag
      long internalDeliveryTag = (deliveryTagCounter++);
      outstandingAcks.put(internalDeliveryTag, envelope.getDeliveryTag());

      // Pass on the delivery with an updated envelope containing our mapped delivery tag
      underlyingConsumer.handleDelivery(consumerTag,
        new Envelope(internalDeliveryTag, envelope.isRedeliver(), envelope.getExchange(), envelope.getRoutingKey()),
        properties, body);
    }
  }
}
