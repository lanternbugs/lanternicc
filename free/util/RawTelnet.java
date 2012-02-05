/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2002 Alexander Maryanovsky.
 * All rights reserved.
 *
 * The utillib library is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * The utillib library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with utillib library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package free.util;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Implements a raw connection to a server. The output from the server is
 * written to the standard output stream and input from the standard input
 * stream is sent to the server.
 */

public class RawTelnet{


  
  /**
   * The main method.
   */

  public static void main(String [] args){
    if (args.length < 2){
      printUsage();
      System.exit(1);
    }

    String hostname = args[0];
    int port;
    try{
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e){
        System.err.println("Bad port value: "+args[1]);
        printUsage();
        System.exit(2);
        return;
      }

    try{
      Socket sock = new Socket(hostname, port);
      InputStream in = sock.getInputStream();
      OutputStream out = sock.getOutputStream();
      PumpThread t1 = new PumpThread(in, System.out);
      PumpThread t2 = new PumpThread(System.in, out);
      t1.start();
      t2.start();
      new ProcessKillerThread(t1).start();
      new ProcessKillerThread(t2).start();
    } catch (IOException e){
        e.printStackTrace();
        System.exit(3);
      }
  }



  /**
   * Dumps usage information to the standard error stream.
   */

  private static void printUsage(){
    System.err.println("RawTelnet Utility");
    System.err.println("Copyright (C) 2002 Alexander Maryanovsky");
    System.err.println();
    System.err.println("Usage: java free.util.RawTelnet hostname port");
    System.out.println();
    System.out.println("Version 1.00 - 20 Jul. 2002");
  }




  /**
   * A thread which waits for the given thread to die and then calls System.exit(0);
   */

  private static class ProcessKillerThread extends Thread{



    /**
     * The thread we're to wait on.
     */

    private final Thread target;




    /**
     * Creates a new ProcessKillerThread with the given Thread to wait on.
     */

    public ProcessKillerThread(Thread target){
      super("ProcessKillerThread("+target.getName()+")");

      this.target = target;

      setDaemon(true);
    }




    /**
     * Waits for the target thread to die, prints a message and calls
     * <code>System.exit(0)</code>
     */

    public void run(){
      try{
        target.join();
      } catch (InterruptedException e){
          e.printStackTrace();
        }

      System.err.println("Connection died");
      System.exit(0);
    }


  }


}
