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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA
 */

package free.util;

import java.io.*;


/**
 * An implementation of a virtual file, whose contents are kept in memory.
 */

public class MemoryFile{


  /**
   * The default buffer size.
   */

  private static final int DEFAULT_BUFFER_SIZE = 2048;



  /**
   * The initial buffer size.
   */

  private final int initBufSize;



  /**
   * The data byte array.
   */

  private byte [] data = null;



  /**
   * The amount of bytes that have been written into the file. 
   */

  private volatile int size = 0;



  /**
   * The currently open <code>OutputStream</code> writing into this
   * <code>MemoryFile</code>. <code>null</code> if none.
   */

  private OutputStream out = null;




  /**
   * Creates a new, empty, <code>MemoryFile</code>.
   */

  public MemoryFile(){
    this(null, DEFAULT_BUFFER_SIZE);
  }




  /**
   * Creates a new empty <code>MemoryFile</code> with the specified initial
   * buffer size.
   */

  public MemoryFile(int bufSize){
    this(null, bufSize);
  }




  /**
   * Creates a new <code>MemoryFile</code> initialized with the data from the
   * specified byte array. The specified byte array is copied.
   */

  public MemoryFile(byte [] data){
    this(data, DEFAULT_BUFFER_SIZE);
  }




  /**
   * Creates a new <code>MemoryFile</code> initialized with the data from the
   * specified byte array and the initial buffer size. Note that if the
   * specified initial buffer size is smaller than the length of the byte array,
   * it will only be user when/if the contents of the file are cleared.
   */

  public MemoryFile(byte [] data, int bufSize){
    if (bufSize <= 0)
      throw new IllegalArgumentException("The buffer size must be a positive integer");

    this.initBufSize = bufSize;

    if (data != null){
      this.data = new byte[bufSize > data.length ? bufSize : data.length];
      System.arraycopy(data, 0, this.data, 0, data.length);
      this.size = data.length;
    }
  }




  /**
   * Returns the size of the file.
   */

  public int getSize(){
    return size;
  }



  /**
   * Returns an <code>OutputStream</code> that will write into this
   * <code>MemoryFile</code>. Only a single open <code>OutputStream</code> is
   * allowed per <code>MemoryFile</code>. Note that invoking this method clears
   * the current contents of the file.
   *
   * @throws IOException if an open OutputStream writing into this
   * <code>MemoryFile</code> already exists.
   */

  public synchronized OutputStream getOutputStream() throws IOException{
    if (out != null)
      throw new IOException("MemoryFile already open for writing");

    size = 0;
    data = new byte[initBufSize];
    return out = new InternalOutputStream();
  }




  /**
   * Returns an <code>InputStream</code> that will read from this
   * <code>MemoryFile</code>. Note that the data read from the returned
   * <code>InputStream</code> will be the data in the file when this method is
   * invoked. If more data is written into the file or the contents of the file
   * are cleared, the returned <code>InputStream</code> will not be affected.
   */

  public InputStream getInputStream(){
    return new ByteArrayInputStream(data, 0, getSize());
  }




  /**
   * Clears this file, resetting its size to 0.
   *
   * @throws IOException if the file is currently open for writing.
   */

  public synchronized void clear() throws IOException{
    if (out != null)
      throw new IOException("MemoryFile open for writing");
    size = 0;
  }




  /**
   * Writes the contents of this <code>MemoryFile</code> into the specified
   * <code>OutputStream</code>.
   */

  public synchronized void writeTo(OutputStream out) throws IOException{
    out.write(data, 0, getSize());
  }




  /**
   * Increases the size of the internal buffer by at least the specified amount
   * of bytes. The caller must take care of proper synchronization.
   */

  private void growBuf(int minGrowSize){
    int growSize = minGrowSize < data.length ? data.length : minGrowSize;
    byte [] newData = new byte[data.length + growSize];
    System.arraycopy(data, 0, newData, 0, data.length);
    data = newData;
  }



  /**
   * Writes a single byte into the file.
   */

  private synchronized void write(int b){
    if (data.length - size == 0)
      growBuf(1);

    data[size++] = (byte)(b&0xff);
  }



  /**
   * Writes the specified amount of bytes from the specified array starting with
   * the specified index into the file.
   */

  private synchronized void write(byte [] arr, int offset, int length){
    if (data.length - size < arr.length)
      growBuf(arr.length + size - data.length);

    System.arraycopy(arr, offset, data, size, length);
    size += length;
  }



  /**
   * Closes the <code>OutputStream</code> writing into this file.
   *
   * @throws IOException if the <code>OutputStream</code> is already closed.
   */

  private synchronized void closeOutputStream() throws IOException{
    if (out == null)
      throw new IOException("OutputStream already closed");

    out = null;
  }



  /**
   * The <code>OutputStream</code> class that is responsible for writing into
   * this <code>MemoryFile</code>. This class simply forwards the calls to the
   * methods in its outer class.
   */

  private class InternalOutputStream extends OutputStream{

    public void write(int b){
      MemoryFile.this.write(b);
    }

    public void write(byte [] buf, int offset, int length){
      MemoryFile.this.write(buf, offset, length);
    }

    public void close() throws IOException{
      MemoryFile.this.closeOutputStream();
    }

  }


}
