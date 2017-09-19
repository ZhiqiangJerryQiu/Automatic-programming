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
 * Listener notified about connection failures. Whilst connection failures will result in a retry, some may require
 * user intervention to correct (for example, invalid credentials). To ensure that application failures don't go
 * undiagnosed, this listener should be installed and events logged.
 */
public interface ConnectionFailureListener {
  /**
   * Indicates that a connection failure has occurred.
   * @param e the failure exception.
   */
  void connectionFailure(Exception e);
}
