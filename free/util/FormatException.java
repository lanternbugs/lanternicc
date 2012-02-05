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


/**
 * Thrown when the format of something is wrong.
 */

public class FormatException extends RuntimeException{



  /**
   * The "reason" exception that caused this exception to be thrown.
   */

  private final Throwable realException;

  

  /**
   * Creates a new <code>FormatException</code> with the specified "reason"
   * <code>Throwable</code> and message.
   */

  public FormatException(Throwable realException, String message){
    super(message);
    
    this.realException = realException;
  }


  
  /**
   * Creates a new <code>FormatException</code> with the specified "reason"
   * <code>Throwable</code>.
   */

  public FormatException(Throwable realException){
    this(realException, null);
  }

  

  /**
   * Creates a new <code>FormatException</code> with the specified message.
   */

  public FormatException(String message){
    this(null, message);
  }


  
  /**
   * Creates a new <code>FormatException</code>.
   */

  public FormatException(){
    this(null, null);
  }



  /**
   * Returns the actual throwable that resulted in this exception. For example,
   * if an attempt to parse a string as an integer occurred, this should be
   * the NumberFormatException that was thrown by <code>Integer.parseInt</code>.
   */

  public Throwable getReason(){
    return realException;
  }



  /**
   * Prints the stack trace of this FormatException to the standard error stream.
   */

  public void printStackTrace(){ 
    printStackTrace(System.err);
  }



  /**
   * Prints the stack trace of this FormatException to the specified PrintStream.
   *
   * @param s <code>PrintStream</code> to use for output
   */

  public void printStackTrace(java.io.PrintStream s) { 
    synchronized(s){
      if (realException != null){
        s.println("++++");
        super.printStackTrace(s);
        realException.printStackTrace(s);
        s.println("----");
      }
      else
        super.printStackTrace(s);
    }
  }



  /**
   * Prints the stack trace of this FormatException to the specified PrintWriter.
   *
   * @param s <code>PrintWriter</code> to use for output
   */

  public void printStackTrace(java.io.PrintWriter s) { 
    synchronized(s){
      if (realException != null){
        s.println("++++");
        super.printStackTrace(s);
        realException.printStackTrace(s);
        s.println("----");
      }
      else
        super.printStackTrace(s);
    }
  }

  
  
}
