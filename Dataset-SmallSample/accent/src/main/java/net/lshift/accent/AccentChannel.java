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
 * Wrapper for a RabbitMQ channel providing the auto-reattempt logic.
 */
public class AccentChannel implements ConnectionListener, Closeable {
  private final AccentConnection connection;
  private final List<ChannelListener> setupListeners = new ArrayList<ChannelListener>();
  private Channel channel = null;
  private final Object channelStateLock = new Object();

  public AccentChannel(AccentConnection connection) {
    this.connection = connection;
    this.connection.addConnectionListener(this);
  }

  /**
   * Closes the given accent channel. Any persist consumers will be terminated.
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    this.connection.removeConnectionListener(this);

    // Close our underlying connection
    executeIfChannelValid(new ChannelCallback() {
      public void runWithChannel(Channel c) throws IOException {
        c.close();

        // Invalidate the stored channel
        channel = null;
        
        // Inform all listeners that the channel is lost
        for (ChannelListener cb : setupListeners) {
          cb.channelLost();
        }
      }
    });
  }

  /**
   * Acquire a channel, and perform the given operation on it. If a channel is lost and reconnected, this operation will
   * not be repeated.
   *
   * @param cb the callback that will be provided the channel.
   */
  public void doOnceWithChannel(final ChannelCallback cb) {
    // Attempt the operation once with the current channel. We need to run this in the context of attemptOnce to ensure
    // the channel is recreated if the user operation fails.
    executeWhenChannelValid(cb);
  }

  /**
   * Add a listener that will be invoked each time the underlying channel is created or destroyed. Used mainly by
   * the various messaging components that need to do work on channel setup, and cleanup on channel loss.
   *
   * @param listener the callback that will be provided the channel.
   */
  public void addChannelSetupListener(final ChannelListener listener) {
    synchronized (channelStateLock) {
      setupListeners.add(listener);
    }
    
    executeIfChannelValid(new ChannelCallback() {
      public void runWithChannel(Channel c) throws IOException {
        listener.channelCreated(c);
      }
    });
  }

  /**
   * Removes a listener from this list of listeners informed of channel creation. For completeness, also notifies the
   * provided listener that the channel has been lost (to ensure that it performs any cleanup operations).
   * @param listener the listener to remove.
   */
  public void removeChannelSetupListener(ChannelListener listener) {
    synchronized (channelStateLock) {
      setupListeners.remove(listener);
    }
    
    listener.channelLost();
  }

  /**
   * Callback method from ConnectionListener that handles a connection becoming available.
   * @param connection the connection that is available.
   * @throws IOException if an exception occurs setting up the channel within the connection.
   */
  public void onConnected(final Connection connection) throws IOException {
    ShutdownListener shutdownListener = new ShutdownListener() {
        @Override
        public void shutdownCompleted(ShutdownSignalException cause) {
          synchronized (channelStateLock) {
            // Release the channel
            channel = null;

            // Inform all listeners that the channel is lost
            for (ChannelListener cb : setupListeners) {
              cb.channelLost();
            }

            // Request that the connection thread rebuild us when it is ready
            AccentChannel.this.connection.rebuild(AccentChannel.this);
          }
        }
      };

    // Attach a shutdown listener to the connection so if it goes completely, we'll be able to tear down
    // our channel.
    connection.addShutdownListener(shutdownListener);

    synchronized (channelStateLock) {
      channel = connection.createChannel();
      channel.addShutdownListener(shutdownListener);

      // Invoke the setup listeners
      for (ChannelListener cb : setupListeners) {
        cb.channelCreated(channel);
      }

      channelStateLock.notifyAll();
    }
  }

  /**
   * Executes a given operation only if the channel is currently valid.
   * @param t  the operation to execute.
   * @return an exception that was thrown, or null if the operation completed successfully.
   */
  public Exception executeIfChannelValid(final ChannelCallback t) {
    synchronized (channelStateLock) {
      if (channel != null) {
        return ExceptionUtils.attempt(new Thunk() {
          @Override public void run() throws IOException { t.runWithChannel(channel); }
        });
      } else {
        return null;
      }
    }
  }

  /**
   * Blocks until the channel becomes available, and then execute the given operation.
   * @param t the operation to execute.
   * @return any exception generated.
   */
  public Exception executeWhenChannelValid(final ChannelCallback t) {
    synchronized (channelStateLock) {
      while (channel == null) {
        try { channelStateLock.wait(); } catch (InterruptedException e) { /* Ignore */ }
      }

      return ExceptionUtils.attempt(new Thunk() {
        @Override
        public void run() throws IOException {
          t.runWithChannel(channel);
        }
      });
    }
  }
}