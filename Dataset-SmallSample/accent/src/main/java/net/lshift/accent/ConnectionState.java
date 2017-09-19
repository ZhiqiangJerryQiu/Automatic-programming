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

/**
 * Enumeration of the possible states that an accent connection could be in.
 */
public enum ConnectionState {
  /**
   * The endpoint is connected to an AMQP server.
   */
  CONNECTED,

  /**
   * The endpoint is not connected to any AMQP server, but is attempting to reconnect.
   */
  RECONNECTING,

  /**
   * The endpoint is not connected to any AMQP server, and is currently waiting before the next reconnect.
   */
  WAITING,

  /**
   * The endpoint is not connected to any AMQP server, and is not going to attempt to reconnect.
   */
  DISCONNECTED
}
