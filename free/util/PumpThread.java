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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


/**
 * A thread which pumps information read from a given input stream into the
 * given output stream.
 */

public class PumpThread extends Thread{


  /**
   * The InputStream.
   */

  private final InputStream in;



  /**
   * The OutputStream.
   */

  private final OutputStream out;




  /**
   * The buffer we're using.
   */

  private final byte [] buffer;




  /**
   * The IOException thrown while reading or writing information, or null if
   * none.
   */

  private IOException exception;




  /**
   * Creates a new PumpThread which will pump information from the given
   * InputStream into the given OutputStream.
   */

  public PumpThread(InputStream in, OutputStream out){
    this(in, out, 2048);
  }




  /**
   * Creates a new PumpThread which will pump information from the given
   * InputStream into the given OutputStream and will use a buffer of the given
   * size.
   */

  public PumpThread(InputStream in, OutputStream out, int bufSize){
    this(in, out, new byte[bufSize]);
  }




  /**
   * Creates a new PumpThread which will pump information from the given
   * InputStream into the given OutputStream and will use the given buffer.
   */

  public PumpThread(InputStream in, OutputStream out, byte [] buffer){
    this.in = in;
    this.out = out;
    this.buffer = buffer;
  }




  /**
   * Does the actual pumping.
   */

  public void run(){
    try{
      while (true){
        int count = in.read(buffer);
        if (count <= 0)
          return;
        out.write(buffer, 0, count);
      }
    } catch (IOException e){
        exception = e;
      }
  }




  /**
   * Returns the exception thrown while reading or writing, or <code>null</code>
   * if it finished normally, without throwing an exception (read returned -1).
   *
   * @throws IllegalStateException if the thread is still alive.
   */

  public IOException getException(){
    if (isAlive())
      throw new IllegalStateException("The thread is still alive");

    return exception;
  }

}
