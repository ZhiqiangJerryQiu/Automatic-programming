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
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for a RabbitMQ connection factory/connection providing automated reconnect logic.
 */
public class AccentConnection implements Closeable {
  protected int pause = 1000; // ms
  
  protected ConnectionState state = ConnectionState.DISCONNECTED;
  protected ConnectionFactory factory;
  private final Object connectionMutex = new Object();
  protected Connection connection;
  private final ConnectionManager connectThread = new ConnectionManager();
  protected final Object closeMutex = new Object();
  protected boolean isClosing = false;
  private List<ConnectionStateListener> stateListeners = new ArrayList<ConnectionStateListener>();
  private final List<ConnectionListener> listeners = new ArrayList<ConnectionListener>();
  private final ConnectionFailureListener failureListener;


  public AccentConnection(ConnectionFactory factory, ConnectionFailureListener failureListener) {
    this.factory = factory;
    this.failureListener = failureListener;

    // Start the connection thread to maintain the connection
    connectThread.start();
  }

  public int getPause() {
    return pause;
  }

  public void setPause(int pause) {
    this.pause = pause;
  }

  public ConnectionFactory getFactory() {
    return factory;
  }

  public ConnectionState getState() {
    return state;
  }

  public void addConnectionStateListener(ConnectionStateListener listener) {
    stateListeners.add(listener);
  }

  public void removeConnectionStateListener(ConnectionStateListener listener) {
    stateListeners.remove(listener);
  }

  public AccentChannel createChannel() {
    return new AccentChannel(this);
  }

  public void close() {
    synchronized (closeMutex) {
      isClosing = true;

      closeMutex.notifyAll();
    }
    synchronized (connectionMutex) {
      if (connection != null) connection.abort(100);
    }

    try { connectThread.join(10000); } catch (InterruptedException ex) { /* Ignore */ }
  }

  /**
   * If a listener determines that it has become invalid, and would like to be re-initialised at a safe time, it can
   * submit itself to the rebuild mechanism. This method will either directly rebuild the listener, or do it as part
   * of a complete connection rebuild.
   * @param listener the listener to rebuild.
   */
  public void rebuild(ConnectionListener listener) {
    connectThread.scheduleRebuild(listener);
  }

  private class ConnectionManager extends Thread implements ShutdownListener {
    private final Object actionMutex = new Object();
    private final List<ConnectionListener> rebuildQueue = new ArrayList<ConnectionListener>();

    @Override public void run() {
      while (true) {
        synchronized (closeMutex) {
          if (isClosing) {
            onStateChange(ConnectionState.DISCONNECTED);
            return;
          }
        }

        try {
          if (connection == null) {
            connect();
          } else {
            executeRebuilds();
          }

          // Wait for an event that requires us to do something
          synchronized (actionMutex) {
            // Only wait if there is nothing to rebuild, and the connection is still valid
            if (rebuildQueue.size() == 0 && connection.getCloseReason() == null) {
              actionMutex.wait();
            }
          }
          if (connection.getCloseReason() != null) {
            handleFailure(connection.getCloseReason());
          }
        } catch (Exception e) {
          handleFailure(e);
        }
      }
    }

    public void scheduleRebuild(ConnectionListener listener) {
      synchronized (actionMutex) {
        rebuildQueue.add(listener);
        actionMutex.notifyAll();
      }
    }

    public void shutdownCompleted(ShutdownSignalException cause) {
      synchronized (actionMutex) {
        actionMutex.notifyAll();
      }
    }

    private void connect() throws IOException {
      onStateChange(ConnectionState.RECONNECTING);

      synchronized (connectionMutex) {
        connection = factory.newConnection();
        connection.addShutdownListener(this);
      }

      // Flush the rebuild queue. We're just about to process everything.
      synchronized (actionMutex) {
        rebuildQueue.clear();
      }
      

      // Notify all of the attached listeners that we're now connected
      synchronized (listeners) {
        for (ConnectionListener listener : listeners) {
          // If a listener fails, then we'll let it propagate right up and rebuild everything. Yes, it is destructive.
          // But presumably if a listener doesn't complete, then we're not in a good state anyway.
          listener.onConnected(connection);
        }
      }

      onStateChange(ConnectionState.CONNECTED);
    }

    private void executeRebuilds() throws IOException {
      ConnectionListener[] toRebuild;
      synchronized (actionMutex) {
        toRebuild = rebuildQueue.toArray(new ConnectionListener[rebuildQueue.size()]);
        rebuildQueue.clear();
      }

      for (ConnectionListener r : toRebuild) {
        r.onConnected(connection);
      }
    }

    private void handleFailure(Exception e) {
      // Ensure that the connection is closed
      synchronized (connectionMutex) {
        try {
          if (connection.isOpen()) {
            connection.abort(100);
          }
        } catch (Exception ce) {
          // Ignore
        }

        // Invalidate the connection
        connection = null;
      }

      if (!(e instanceof ShutdownSignalException && ((ShutdownSignalException) e).isInitiatedByApplication())) {
        onStateChange(ConnectionState.WAITING);

        // Issue an event about this exception.
        failureListener.connectionFailure(e);

        // Wait before the next reconnect
        doPause();
      }
    }

    private void doPause() {
      synchronized (closeMutex) {
        try {
          closeMutex.wait(pause);
        } catch (InterruptedException ie) { /* Ignore */ }
      }
    }
  }

  protected void onStateChange(ConnectionState newState) {
    if (state == newState) return;

    state = newState;

    for (ConnectionStateListener listener : stateListeners) {
      listener.connectionStateChanged(this, state);
    }
  }

  /**
   * Attaches a connection listener to this connection. It will be notified whenever a new connection is created. It will
   * also be invoked once with the current connection details before this method returns.
   *
   * @param connectionListener the connection listener to register.
   */
  public void addConnectionListener(ConnectionListener connectionListener) {
    synchronized (listeners) {
      listeners.add(connectionListener);
    }

    rebuild(connectionListener);
  }

  public void removeConnectionListener(ConnectionListener connectionListener) {
    synchronized (listeners) {
      listeners.remove(connectionListener);
    }
  }
}
