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

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertTrue;

/**
 * Behaviour based tests for Accent based on a mock connection.
 */
public class ConnectionBehaviourTest {
  @Test
  public void shouldKeepAttemptingToReconnect() throws Exception {
    // Create a connection factory that never returns a valid connection
    ConnectionFactory unconnectableFactory = createMock(ConnectionFactory.class);
    expect(unconnectableFactory.newConnection()).andStubThrow(new ConnectException("Boom"));
    replay(unconnectableFactory);

    // We want to see 4 attempts to connect
    final CountDownLatch attemptLatch = new CountDownLatch(4);
    ConnectionFailureListener eventingListener = new ConnectionFailureListener() {
      @Override public void connectionFailure(Exception e) {
        if (e instanceof ConnectException) {
          attemptLatch.countDown();
        } else {
          System.out.println("WARNING: Received unexpected exception - " + e);
        }
      }
    };

    // Create the connection, and wait for the countdown to reach 0
    AccentConnection conn = new AccentConnection(unconnectableFactory, eventingListener);
    assertTrue(attemptLatch.await(5000, TimeUnit.MILLISECONDS));
  }
}
