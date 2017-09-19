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

import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * Utilities for use within test cases.
 */
public class AccentTestUtils {
  /**
   * Given the reconnection logic in Accent, a missing AMQP server during testing can cause tests to hang. This method
   * tries a direct AMQP connection first, so we can ensure that an AMQP host is available.
   */
  public static void ensureAMQPServer() throws IOException {
    ConnectionFactory cf = new ConnectionFactory();
    cf.newConnection().close();
  }

  /**
   * Generates a ConnectionFailureListener that prints out failures to ensure tests don't silently have problems.
   * @return the listener.
   */
  public static ConnectionFailureListener printingFailureLogger() {
    return new ConnectionFailureListener() {
      @Override public void connectionFailure(Exception e) {
        System.err.println("Connection Failure Occurred:");
        e.printStackTrace();
      }
    };
  }

  /**
   * Generates a ConnectionFailureListener that prints out failures to ensure tests don't silently have problems.
   * @return the listener.
   */
  public static ConnectionFailureListener smallPrintingFailureLogger() {
    return new ConnectionFailureListener() {
      @Override public void connectionFailure(Exception e) {
        System.err.println("Connection Failure Occurred:" + e.getMessage());
      }
    };
  }
}
