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
 * An alternative implementation of PipedOutputStream. Note that this is just
 * a wrapper, the real action happens in PipedStreams.
 */

class PipedOutputStream extends OutputStream{


  /**
   * The PipedStreams object we write the data to.
   */

  private final PipedStreams sink;



  /**
   * Creates a new PipedOutputStream which writes data to the given
   * PipedStreams object.
   */

  public PipedOutputStream(PipedStreams sink){
    this.sink = sink;
  }




  /**
   * Writes a single byte into the sink.
   */

  public void write(int b) throws IOException{
    sink.write(b);
  }



  /**
   * Writes the given amount of bytes from the specified byte array starting at
   * the given offset to the sink.
   */

  public void write(byte [] arr, int offset, int length) throws IOException{
    sink.write(arr,offset,length);
  }




  /**
   * Closes this PipedOutputStream.
   */

  public void close() throws IOException{
    sink.closeWriter();
  }

}
