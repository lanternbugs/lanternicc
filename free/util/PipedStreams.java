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

import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * This class is responsible for creating and managing pairs of PipedStreams.
 * A single instance of this class consists of one PipedInputStream and one
 * PipedOutputStream connected to each other.
 */

public class PipedStreams{


  /**
   * The default buffer size.
   */

  private static final int DEFAULT_BUFFER_SIZE = 2048;



  /**
   * The PipedInputStream.
   */

  private final PipedInputStream in;



  /**
   * The PipedOutputStream.
   */

  private final PipedOutputStream out;



  /**
   * The value of the soTimeout.
   */

  private volatile int soTimeout = 0;



  /**
   * The buffer.
   */

  private byte [] buf;



  /**
   * Whether the buffer is allowed to grow.
   */

  private final boolean growBuf;




  /**
   * The index of the byte that will be read next.
   */

  private int readIndex = 0;



  /**
   * The index of the byte that that will be written next.
   */

  private int writeIndex = 0;



  /**
   * Becomes true when the PipedOutputStream gets closed.
   */

  private boolean writerClosed = false;



  /**
   * Becomes true when the PipedInputStream gets closed.
   */

  private boolean readerClosed = false;



  /**
   * The lock protecting writing.
   */

  private Object writeLock = new String("Write Lock for PipedStreams");



  /**
   * The lock protecting reading.
   */

  private Object readLock = new String("Read Lock for PipedStream");



  /**
   * Creates new <code>PipedStreams</code>.
   */

  public PipedStreams(){
    this(2048, false);
  }



  /**
   * Creates new <code>PipedStreams</code> with the specified buffer size. Once
   * the specified amount of bytes have been written into the,
   * <code>OutputStream</code> attempting to write more data will block until
   * enough data has been read to allow writing into the buffer again.
   */

  public PipedStreams(int bufSize){
    this(bufSize, false);
  }




  /**
   * Creates new <code>PipedStreams</code>. If <code>growBuf</code> is
   * <code>true</code>, the internal buffer will be grown indefinitely when more
   * space is required for the written data. This means that writing into the
   * <code>OutputStream</code> will never block. Note that there is currently no
   * mechanism to cause the internal buffer to shrink.
   */

  public PipedStreams(boolean growBuf){
    this(DEFAULT_BUFFER_SIZE, growBuf);
  }




  /**
   * Creates new <code>PipedStreams</code> with the specified initial buffer
   * size, potentially allowing the buffer to grow indefinitely.
   */

  public PipedStreams(int bufSize, boolean growBuf){
    if (bufSize <= 0)
      throw new IllegalArgumentException("The buffer size must be a positive integer");

    in = new PipedInputStream(this);
    out = new PipedOutputStream(this);

    this.growBuf = growBuf;
    this.buf = new byte[bufSize];
  }




  /**
   * Sets the SO_TIMEOUT for the PipedInputStream. A read operation on the
   * PipedInputStream will only block for the given amount of milliseconds, after
   * that, an InterruptedIOException will be thrown, but the streams will remain
   * valid. A value of 0 implies this option is off (read() can block indefinitely).
   * NOTE: This method should not be called while a read() operation is in progress
   * (it will block until the read() is done, which may be a long time, or never).
   */

  public void setSoTimeout(int timeout){
    synchronized(readLock){ // Don't modify this while a read is in progress.
      soTimeout = timeout;
    }
  }




  /**
   * Returns the value of SO_TIMEOUT. A value of 0 implies this option is off
   * (read() can block indefinitely).
   */

  public int getSoTimeout(){
    return soTimeout;
  }





  /**
   * Returns the PipedInputStream.
   */

  public PipedInputStream getInputStream(){
    return in;
  }


  /**
   * Returns the PipedOutputStream.
   */

  public PipedOutputStream getOutputStream(){
    return out;
  }



  /**
   * Returns the amount of bytes available to be read immediately (without
   * blocking) by the PipedInputStream.
   */

  synchronized int available(){
    if (readerClosed)
      return 0;

    return availableImpl();
  }




  /**
   * Returns the amount of bytes available to be read immediately (without
   * blocking) by the PipedInputStream.
   */

  private int availableImpl(){
    if (writeIndex >= readIndex) // On the same lap.
      return writeIndex - readIndex;
    else // On different laps.
      return writeIndex + buf.length - readIndex;
  }




  /**
   * Returns the amount of bytes that can be written into the buffer without
   * blocking.
   */

  private int availableSpace(){
    return buf.length - availableImpl() - 1;
  }




  /**
   * Increases the size of the internal buffer by at least the specified amount
   * of bytes. The caller must take care of proper synchronization.
   */

  private void growBuf(int minGrowSize){
    int growSize = minGrowSize < buf.length ? buf.length : minGrowSize;
    byte [] newBuf = new byte[buf.length + growSize];
    System.arraycopy(buf, 0, newBuf, 0, buf.length);
    buf = newBuf;
  }




  /**
   * Writes a single byte to the buffer. Note that this method can block if the
   * buffer is full.
   */

  synchronized void write(int b) throws IOException{
    synchronized(writeLock){
      if (readerClosed || writerClosed)
        throw new IOException("Stream closed");

      while (availableSpace() == 0){
        if (growBuf)
          growBuf(1);
        else try{
          wait();
        } catch (InterruptedException e){
            throw new InterruptedIOException();
          }
      }

      if (readerClosed || writerClosed)
        throw new IOException("Stream closed");

      buf[writeIndex++] = (byte)(b&0xff);
      if (writeIndex == buf.length)
        writeIndex = 0;

      notifyAll();
    }
  }



  /**
   * Writes bytes according to the contract of OutputStream.write(byte [], int, int)
   * with the only difference that this might block if the buffer is full.
   */

  synchronized void write(byte [] arr, int offset, int length) throws IOException{
    synchronized(writeLock){
      if (readerClosed||writerClosed)
        throw new IOException("Stream closed");

      if (growBuf && (length > availableSpace()))
        growBuf(length - availableSpace());

      while(length > 0){
        while (availableSpace() == 0){
          try{
            wait();
          } catch (InterruptedException e){
              throw new InterruptedIOException();
            }
        }

        int availableSpace = availableSpace();

        int amountToWrite = length > availableSpace ? availableSpace : length;
        int part1Size = buf.length-writeIndex >= amountToWrite ? amountToWrite : buf.length-writeIndex;
        int part2Size = amountToWrite-part1Size > 0 ? amountToWrite - part1Size : 0;

        System.arraycopy(arr, offset, buf, writeIndex, part1Size);
        System.arraycopy(arr, offset + part1Size, buf, 0, part2Size);

        offset += amountToWrite;
        length -= amountToWrite;

        writeIndex = (writeIndex + amountToWrite) % buf.length;

        notifyAll();
      }
    }
  }



  /**
   * Reads a single byte from the buffer according to the contract of
   * InputStream.read().
   */

  synchronized int read() throws IOException{
    synchronized(readLock){
      if (readerClosed)
        throw new IOException("Stream closed");

      final long startedWaitingTS = System.currentTimeMillis();
      while (available() == 0){
        if (writerClosed)
          return -1;

        long curTime = System.currentTimeMillis();
        if ((soTimeout != 0) && (curTime - startedWaitingTS >= soTimeout))
          throw new InterruptedIOException();

        try{
          if (soTimeout == 0)
            wait();
          else{
            wait(soTimeout + curTime - startedWaitingTS);
          }
        } catch (InterruptedException e){
            throw new InterruptedIOException();
          }
        if (readerClosed)
          throw new IOException("Stream closed");

      }

      int b = buf[readIndex++];
      if (readIndex == buf.length)
        readIndex = 0;

      notifyAll();

      return b < 0 ? b+256 : b;
    }
  }




  /**
   * Reads bytes from the buffer into the given array according to the contract
   * of InputStream.read(byte [], int, int)
   */

  synchronized int read(byte [] arr, int offset, int length) throws IOException{
    synchronized(readLock){
      if (readerClosed)
        throw new IOException("Stream closed");

      final long startedWaitingTS = System.currentTimeMillis();
      while (available() == 0){
        if (writerClosed)
          return -1;

        long curTime = System.currentTimeMillis();
        if ((soTimeout != 0) && (curTime - startedWaitingTS >= soTimeout))
          throw new InterruptedIOException();

        try{
          if (soTimeout == 0)
            wait();
          else{
            wait(soTimeout + curTime - startedWaitingTS);
          }
        } catch (InterruptedException e){
            throw new InterruptedIOException();
          }
        if (readerClosed)
          throw new IOException("Stream closed");
      }

      int available = available();
      int amountToRead = length > available ? available : length;
      int part1Size = buf.length-readIndex > amountToRead ? amountToRead : buf.length-readIndex;
      int part2Size = amountToRead-part1Size > 0 ? amountToRead-part1Size : 0;

      System.arraycopy(buf, readIndex, arr, offset, part1Size);
      System.arraycopy(buf, 0, arr, offset + part1Size, part2Size);

      readIndex = (readIndex + amountToRead) % buf.length;

      notifyAll();

      return amountToRead;
    }
  }



  /**
   * Closes down the streams as far as the PipedOutputStream is concerned.
   */

  synchronized void closeWriter(){
    if (writerClosed)
      throw new IllegalStateException("Already closed");
    writerClosed = true;
    notifyAll();
  }



  /**
   * Closes down the streams as far as the PipedInputStream is concerned
   */

  synchronized void closeReader(){
    if (readerClosed)
      throw new IllegalStateException("Already closed");

    readerClosed = true;
    notifyAll();
  }

}
