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
import java.io.IOException;


/**
 * An alternative implementation of PipedInputStream. Note that this is just
 * a wrapper, the real action happens in PipedStreams.
 */

class PipedInputStream extends InputStream{


  /**
   * The PipedStreams object we get the data from.
   */

  private final PipedStreams source;



  /**
   * Creates a new PipedInputStream which reads data from the given
   * PipedStreams object.
   */

  public PipedInputStream(PipedStreams source){
    this.source = source;
  }



  /**
   * Returns the amount of bytes it's possible to read from this PipedInputStream
   * without blocking.
   */

  public int available(){
    return source.available();
  }



  /**
   * Reads the next byte of data from this input stream. The value byte is 
   * returned as an int in the range 0 to 255. If no byte is available because
   * the end of the stream has been reached, the value -1 is returned. This 
   * method blocks until input data is available, the end of the stream is 
   * detected, or an exception is thrown. 
   */

  public int read() throws IOException{
    return source.read();
  }




  /**
   * Reads up to length bytes of data from this input stream into an array of bytes.
   * This method blocks until some input is available. If the argument b is null,
   * a NullPointerException is thrown. 
   */

  public int read(byte [] buf, int offset, int length) throws IOException{
    return source.read(buf,offset,length);
  }




  /**
   * Closes this PipedInputStream.
   */

  public void close() throws IOException{
    source.closeReader();
  }

  


}
