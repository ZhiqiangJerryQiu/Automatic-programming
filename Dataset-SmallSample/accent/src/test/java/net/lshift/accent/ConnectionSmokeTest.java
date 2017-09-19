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
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.utility.BlockingCell;
import org.junit.Before;
import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Smoke tests executed against real RabbitMQ instances.
 */
public class ConnectionSmokeTest {
  @Before
  public void ensureServer() throws IOException {
    AccentTestUtils.ensureAMQPServer();
  }

  @Test
  public void shouldConnectPublishAndConsume() throws IOException, InterruptedException {
    AccentConnection conn = new AccentConnection(directFactory(), AccentTestUtils.printingFailureLogger());
    try {
      AccentChannel ch = conn.createChannel();

      // Add a setup listener to create the queue
      ch.addChannelSetupListener(new ChannelListenerAdapter() {
        @Override public void channelCreated(Channel c) throws IOException {
          c.queueDeclare("accent.testq", false, false, true, null);
        }
      });

      QueueingConsumer consumer = new QueueingConsumer(null);
      AccentConsumer aConsumer = new AccentConsumer(ch, "accent.testq", consumer);

      AccentConfirmPublisher publisher = new AccentConfirmPublisher(ch);
      publisher.reliablePublish("", "accent.testq", new AMQP.BasicProperties(), new byte[0]);

      QueueingConsumer.Delivery d = consumer.nextDelivery(10000);

      assertNotNull(d);
      assertEquals(0, d.getBody().length);

      aConsumer.reliableAck(d.getEnvelope().getDeliveryTag(), false);

      ch.close();
    } finally {
      closeQuietly(conn);
    }
  }

  @Test
  public void shouldConsumeEvenIfConnectionIsntCreatedUntilLater() throws IOException, InterruptedException {
    AccentConnection directConn = new AccentConnection(directFactory(), AccentTestUtils.printingFailureLogger());
    AccentConnection managedConn = new AccentConnection(controlledFactory(5673), AccentTestUtils.smallPrintingFailureLogger());
    try {
      AccentChannel directCh = directConn.createChannel();
      AccentChannel managedCh = managedConn.createChannel();

      // Add a setup listener to create the queue
      directCh.addChannelSetupListener(new ChannelListenerAdapter() {
        @Override
        public void channelCreated(Channel c) throws IOException {
          c.queueDeclare("accent.testq", false, false, true, null);
        }
      });

      // Consumer on the managed channel which we'll allow to work later
      QueueingConsumer consumer = new QueueingConsumer(null);
      AccentConsumer aConsumer = new AccentConsumer(managedCh, "accent.testq", consumer);

      // Publish the message on a direct channel
      AccentConfirmPublisher publisher = new AccentConfirmPublisher(directCh);
      publisher.reliablePublish("", "accent.testq", new AMQP.BasicProperties(), new byte[0]);

      // Try to consume. We shouldn't get anything because we haven't allowed the connection through
      QueueingConsumer.Delivery d = consumer.nextDelivery(2000);
      assertNull(d);

      // Start the tracer to give us a valid connection
      ControlledConnectionProxy proxy = createProxy(5673);
      try {
        QueueingConsumer.Delivery d2 = consumer.nextDelivery(2000);
        assertNotNull(d2);
        assertEquals(0, d2.getBody().length);

        aConsumer.reliableAck(d2.getEnvelope().getDeliveryTag(), false);

        closeQuietly(aConsumer);
      } finally {
        closeQuietly(proxy);
      }
    } finally {
      closeQuietly(managedConn);
      closeQuietly(directConn);
    }
  }

  @Test
  public void shouldRecoverConsumeIfConnectionIsLost() throws IOException, InterruptedException {
    AccentConnection conn = new AccentConnection(controlledFactory(5675), AccentTestUtils.printingFailureLogger());
    ControlledConnectionProxy proxy = createProxy(5675);
    try {
      AccentChannel ch = conn.createChannel();

      // Add a setup listener to create the queue
      ch.addChannelSetupListener(new ChannelListenerAdapter() {
        @Override public void channelCreated(Channel c) throws IOException {
          c.queueDeclare("accent.testq", false, false, true, null);
        }
      });

      QueueingConsumer consumer = new QueueingConsumer(null);
      AccentConsumer aConsumer = new AccentConsumer(ch, "accent.testq", consumer);

      // Ensure that our consumer works
      AccentConfirmPublisher publisher = new AccentConfirmPublisher(ch);
      publisher.reliablePublish("", "accent.testq", new AMQP.BasicProperties(), new byte[0]);
      QueueingConsumer.Delivery d = consumer.nextDelivery(10000);
      assertNotNull(d);
      aConsumer.reliableAck(d.getEnvelope().getDeliveryTag(), false);

      // Terminate the connection and recreate it
      proxy.close();
      proxy.start();

      // Send another message through, and ensure that we see it
      publisher.reliablePublish("", "accent.testq", new AMQP.BasicProperties(), new byte[0]);
      QueueingConsumer.Delivery d2 = consumer.nextDelivery(10000);
      assertNotNull(d);
      aConsumer.reliableAck(d2.getEnvelope().getDeliveryTag(), false);

      closeQuietly(aConsumer);
    } finally {
      closeQuietly(proxy);
      closeQuietly(conn);
    }
  }

  private static ConnectionFactory directFactory() {
    return new ConnectionFactory();
  }
  private static ConnectionFactory controlledFactory(int port) {
    ConnectionFactory controlledFactory = new ConnectionFactory();
    controlledFactory.setPort(port);
    return controlledFactory;
  }
  private static void closeQuietly(Closeable c) {
    try {
      c.close();
    } catch (IOException e) {
      // Ignore
    }
  }

  private static ControlledConnectionProxy createProxy(int port) throws IOException {
    ControlledConnectionProxy proxy = new ControlledConnectionProxy(
      port, "proxy", "localhost", ConnectionFactory.DEFAULT_AMQP_PORT, new BlockingCell<Exception>());
    proxy.start();

    return proxy;
  }
}
