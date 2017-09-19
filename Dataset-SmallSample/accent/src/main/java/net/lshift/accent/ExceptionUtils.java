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

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.impl.AMQImpl;

import java.io.EOFException;
import java.io.IOException;

/**
 * Utilities for diagnosing and working with exceptions.
 */
public class ExceptionUtils {
  /**
   * Attempts to extract a reply code from the given ShutdownSignalException. If a reply code is available, it is
   * returned - otherwise, the method returns 0.
   * @param s the signal to inspect.
   * @return the reply code if available, or 0.
   */
  public static int getReplyCode(ShutdownSignalException s) {
    if (s.getReason() instanceof AMQImpl.Connection.Close) {
      return ((AMQImpl.Connection.Close) s.getReason()).getReplyCode();
    } else {
      return 0;
    }
  }

  /**
   * Determines whether a given shutdown reason is recoverable.
   * @param s the shutdown signal to inspect.
   * @return true - the shutdown is recoverable; false - it isn't.
   */
  public static boolean isShutdownRecoverable(ShutdownSignalException s) {
    if (s != null) {
      int replyCode = getReplyCode(s);

      return s.isInitiatedByApplication() &&
          ((replyCode == AMQP.CONNECTION_FORCED) ||
              (replyCode == AMQP.INTERNAL_ERROR) ||
              s instanceof AlreadyClosedException ||
              (s.getCause() instanceof EOFException));
    }
    return false;
  }

  /**
   * Attempts to execute the given Thunk. If the operation unrecoverably failed, then the exception will be propogated.
   * If the exception can be recovered, then the exception will be simply returned. If the operation succeeds, null
   * will be returned.
   * @param t the operation to attempt.
   * @return the result exception, or null if the operation succeeds.
   * @throws UnrecoverableMessagingException if the operation fails, and cannot be recovered.
   */
  public static Exception attempt(Thunk t) {
    try {
      t.run();
      return null;
    } catch (AlreadyClosedException e) {
      if (isShutdownRecoverable(e)) {
        return e;
      } else {
        throw new UnrecoverableMessagingException(e);
      }
    } catch (IOException e) {
      if (e.getCause() instanceof ShutdownSignalException &&
          !isShutdownRecoverable((ShutdownSignalException) e.getCause())) {
        throw new UnrecoverableMessagingException(e);
      }

      return e;
    }
  }
}
