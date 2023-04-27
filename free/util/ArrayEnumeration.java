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

import java.util.Enumeration;
import java.util.NoSuchElementException;


/**
 * An implementation of the Enumeration interface which enumerates the items of
 * an array. Note: This class is not thread safe.
 */

public class ArrayEnumeration implements Enumeration{


  /**
   * The array.
   */

  private Object [] arr;



  /**
   * The index of the first enumerated item.
   */

  private final int offset;



  /**
   * The amount of the enumerated items.
   */

  private final int count;



  /**
   * The index of the next returned item.
   */

  private int curIndex;



  /**
   * Creates a new ArrayEnumeration which enumerates the items of the given
   * array. The first <code>count</code> items starting at index 
   * <code>offset</code> are enumerated.
   *
   * @throws IllegalArgumentException if the offset and/or count parameters are
   * invalid.
   */

  public ArrayEnumeration(Object [] arr, int offset, int count){
    if ((offset < 0) || (offset + count > arr.length) || (count < 0))
      throw new IllegalArgumentException("Invalid enumeration range");

    this.arr = new Object[arr.length];
    System.arraycopy(arr, 0, this.arr, 0, arr.length);
    this.offset = offset;
    this.count = count;

    curIndex = offset;
  }



  /**
   * Creates a new ArrayEnumeration which enumerates all the items of the given
   * array.
   */

  public ArrayEnumeration(Object [] arr){
    this(arr, 0, arr.length);
  }



  /**
   * Returns the next element in the enumeration.
   */

  public Object nextElement(){
    if (!hasMoreElements())
      throw new NoSuchElementException();
      
    Object item = arr[curIndex];
    arr[curIndex++] = null; // We don't want to keep a reference to it any longer than we have to.
    if (!hasMoreElements())
      arr = null; // Neither do we need this any more.
    return item;
  }



  /**
   * Returns true if there are more elements in the enumeration.
   */

  public boolean hasMoreElements(){
    return curIndex < offset + count;
  }



}
