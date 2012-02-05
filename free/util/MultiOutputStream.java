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

import java.io.OutputStream;
import java.io.IOException;


/**
 * An <code>OutputStream</code> which relays all data written into it into a
 * list of given <code>OutputStreams</code>.
 */

public class MultiOutputStream extends OutputStream{



  /**
   * An array containing the OutputStreams we're relaying data to.
   */

  private final OutputStream [] streams;



  /**
   * Creates a new <code>MultiOutputStream</code> which relays data to the
   * specified two <code>OutputStreams</code>. Any <code>null</code> values
   * will be silently ignored.
   */

  public MultiOutputStream(OutputStream out1, OutputStream out2){
    this(new OutputStream[]{out1, out2});
  }



  /**
   * Creates a new <code>MultiOutputStream</code> which relays data to the
   * specified <code>OutputStreams</code>. Any <code>null</code> items in the
   * array will be silently ignored.
   */

  public MultiOutputStream(OutputStream [] streams){
    if (streams == null)
      throw new IllegalArgumentException("Specified array may not be null");

    int count = 0;
    for (int i = 0; i < streams.length; i++)
      if (streams[i] != null)
        count++;

    this.streams = new OutputStream[count];
    count = 0;
    for (int i = 0; i < streams.length; i++){
      OutputStream stream = streams[i];
      if (stream != null)
        this.streams[count++] = stream;
    }
  }



  /**
   * Closes all the underlying <code>OutputStreams</code>.
   */

  public void close() throws IOException{
    for (int i = 0; i < streams.length; i++)
      streams[i].close();
  }



  /**
   * Flushes all the underlying <code>OutputStreams</code>.
   */

  public void flush() throws IOException{
    for (int i = 0; i < streams.length; i++)
      streams[i].flush();
  }




  /**
   * Writes the specified <code>byte</code> into the underlying
   * <code>OutputStreams</code>.
   */
  
  public void write(int b) throws IOException{
    for (int i = 0; i < streams.length; i++)
      streams[i].write(b);
  }




  /**
   * Writes the specified amount of bytes from the given byte array starting
   * at the specified offset to the underlying <code>OutputStreams</code>.
   */

  public void write(byte [] arr, int offset, int length) throws IOException{
    for (int i = 0; i < streams.length; i++)
      streams[i].write(arr, offset, length);
  }

}
