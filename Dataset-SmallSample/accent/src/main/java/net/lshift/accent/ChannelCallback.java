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

import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * Callback invoked when an operation needs to be performed with a valid RabbitMQ channel.
 */
public interface ChannelCallback {
  /**
   * Invoked to allow an operation to perform with a channel.
   * @param c the channel. Should not be kept beyond the scope of this call, as it may later become invalid.
   */
  void runWithChannel(Channel c) throws IOException;
}
