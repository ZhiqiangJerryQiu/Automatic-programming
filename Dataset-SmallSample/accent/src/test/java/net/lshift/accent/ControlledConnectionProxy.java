//  The contents of this file are subject to the Mozilla Public License
//  Version 1.1 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License
//  at http://www.mozilla.org/MPL/
//
//  Software distributed under the License is distributed on an "AS IS"
//  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
//  the License for the specific language governing rights and
//  limitations under the License.
//
//  The Original Code is RabbitMQ.
//  Significantly modified code as part of Accent.
//
//  The Initial Developer of the Original Code is VMware, Inc.
//  Copyright (c) 2007-2011 VMware, Inc.  All rights reserved.
//  Copyright (c) 2011 LShift Ltd. All rights reserved.
//

package net.lshift.accent;

import com.rabbitmq.client.impl.AMQCommand;
import com.rabbitmq.client.impl.Frame;
import com.rabbitmq.utility.BlockingCell;
import com.rabbitmq.utility.Utility;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AMQP connection proxy that can be started and stopped to simulate Broker failures.
 */
public class ControlledConnectionProxy implements Runnable, Closeable {
  private ServerSocket inServer;
  private Socket inSock;
  private Socket outSock;
  private final int listenPort;
  private final String host;
  private final int port;
  private final String idLabel;
  private final BlockingCell<Exception> reportEnd;
  private final AtomicBoolean started;

  public ControlledConnectionProxy(int listenPort, String id, String host, int port, BlockingCell<Exception> reportEnd)
    throws IOException {
    this.listenPort = listenPort;
    this.host = host;
    this.port = port;
    this.idLabel = ": <" + id + "> ";
    this.reportEnd = reportEnd;
    this.started = new AtomicBoolean(false);
  }

  public void start() {
    if (this.started.compareAndSet(false, true)) {
      new Thread(this).start();
    }
  }

  public void close() {
    if (this.started.compareAndSet(true, false)) {
      try {
        this.inServer.close();
      } catch (IOException e) { /* Ignore */ }
      try {
        if (this.inSock != null) this.inSock.close();
      } catch (IOException e) { /* Ignore */ }
      try {
        if (this.outSock != null) this.outSock.close();
      } catch (IOException e) { /* Ignore */ }

      this.inServer = null;
      this.inSock = null;
      this.outSock = null;
    }
  }

  public void run() {
    try {
      this.inServer = new ServerSocket(listenPort);
      inServer.setReuseAddress(true);

      this.inSock = inServer.accept();
      this.outSock = new Socket(host, port);

      DataInputStream iis = new DataInputStream(this.inSock.getInputStream());
      DataOutputStream ios = new DataOutputStream(this.inSock.getOutputStream());
      DataInputStream ois = new DataInputStream(this.outSock.getInputStream());
      DataOutputStream oos = new DataOutputStream(this.outSock.getOutputStream());
      
      byte[] handshake = new byte[8];
      iis.readFully(handshake);
      oos.write(handshake);

      BlockingCell<Exception> wio = new BlockingCell<Exception>();

      new Thread(new DirectionHandler(wio, true, iis, oos))
        .start();
      new Thread(new DirectionHandler(wio, false, ois, ios))
        .start();

      waitAndLogException(wio); // either stops => we stop
    } catch (Exception e) {
      reportAndLogNonNullException(e);
    } finally {
      try {
        if (this.inSock != null) this.inSock.close();
      } catch (Exception e) {
        logException(e);
      }
      try {
        if (this.outSock != null) this.outSock.close();
      } catch (Exception e) {
        logException(e);
      }
      this.reportEnd.setIfUnset(null);
    }
  }

  private void waitAndLogException(BlockingCell<Exception> bc) {
    reportAndLogNonNullException(bc.uninterruptibleGet());
  }

  private void reportAndLogNonNullException(Exception e) {
    if (e != null) {
      this.reportEnd.setIfUnset(e);
      logException(e);
    }
  }

  public void log(String message) {
    StringBuilder sb = new StringBuilder();
    System.err.println(sb.append(System.currentTimeMillis())
      .append(this.idLabel)
      .append(message)
      .toString());
  }

  public void logException(Exception e) {
    log("uncaught " + Utility.makeStackTrace(e));
  }

  private class DirectionHandler implements Runnable {
    private final BlockingCell<Exception> waitCell;
    private final DataInputStream inStream;
    private final DataOutputStream outStream;
    private final Map<Integer, AMQCommand.Assembler> assemblers;

    public DirectionHandler(BlockingCell<Exception> waitCell, boolean inBound,
                            DataInputStream inStream, DataOutputStream outStream) {
      this.waitCell = waitCell;
      this.inStream = inStream;
      this.outStream = outStream;
      this.assemblers = new HashMap<Integer, AMQCommand.Assembler>();
    }

    private Frame readFrame() throws IOException {
      return Frame.readFrom(this.inStream);
    }

    private void doFrame() throws IOException {
      Frame frame = readFrame();

      if (frame != null) {
        frame.writeTo(this.outStream);
      }
    }

    public void run() {
      try {
        while (true) {
          doFrame();
        }
      } catch (Exception e) {
        this.waitCell.setIfUnset(e);
      } finally {
        this.waitCell.setIfUnset(null);
      }
    }
  }
}
