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

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;


/**
 * Implements a TCP/IP bounce utility (proxy). You run bounce specifying which
 * port to listen on, which host and on which port to connect to and it will
 * act as a proxy relaying information between anyone who connects to it and the
 * specified server.
 */

public class Bounce{


  
  /**
   * The main method.
   */

  public static void main(String [] args){
    if (args.length < 3){
      printUsage();
      System.exit(1);
    }

    int localPort;
    try{
      localPort = Integer.parseInt(args[0]);
    } catch (NumberFormatException e){
        System.err.println("Bad local port value: "+args[0]);
        printUsage();
        System.exit(2);
        return;
      }

    String hostname = args[1];

    int remotePort;
    try{
      remotePort = Integer.parseInt(args[2]);
    } catch (NumberFormatException e){
        System.err.println("Bad remote port value: "+args[2]);
        printUsage();
        System.exit(3);
        return;
      }

    boolean shouldLog = args.length > 3 ? Boolean.valueOf(args[3]).booleanValue() : false;
    int numConnections = 0;

    try{
      ServerSocket ssock = new ServerSocket(localPort);
      while (true){
        Socket incomingSock = ssock.accept();
        Socket outgoingSock = new Socket(hostname, remotePort);
        numConnections++;

        InputStream incomingIn = incomingSock.getInputStream();
        InputStream outgoingIn = outgoingSock.getInputStream();
        OutputStream incomingOut = incomingSock.getOutputStream();
        OutputStream outgoingOut = outgoingSock.getOutputStream();

        if (shouldLog){
          String incomingLogName = "in-log-"+incomingSock.getInetAddress().getHostName()+ "("+localPort+")-" + numConnections + ".dat";
          String outgoingLogName = "out-log-" + hostname + "("+remotePort + ")-" + numConnections+".dat";
          OutputStream incomingLog = new FileOutputStream(incomingLogName);
          incomingOut = new MultiOutputStream(incomingOut, incomingLog);
          OutputStream outgoingLog = new FileOutputStream(outgoingLogName);
          outgoingOut = new MultiOutputStream(outgoingOut, outgoingLog);
        }

        PumpThread t1 = new PumpThread(incomingIn, outgoingOut);
        PumpThread t2 = new PumpThread(outgoingIn, incomingOut);
        t1.start();
        t2.start();
      }
    } catch (IOException e){
        e.printStackTrace();
        System.exit(3);
      }
  }



  /**
   * Dumps usage information to the standard error stream.
   */

  private static void printUsage(){
    System.err.println("Bounce Utility");
    System.err.println("Copyright (C) 2002 Alexander Maryanovsky");
    System.err.println();
    System.err.println("Usage: java free.util.Bounce localPort hostname remotePort [shouldLog]");
    System.out.println();
    System.out.println("Version 1.01 - 31 Nov. 2002");
  }



}
