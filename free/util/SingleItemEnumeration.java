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
 * An implementation of the Enumeration interface which enumerates a single
 * item. Note: This class is not thread safe.
 */

public class SingleItemEnumeration implements Enumeration{


  /**
   * The sole item.
   */

  private Object item;




  /**
   * Becomes true when we've returned the sole item.
   */

  private boolean done = false;




  /**
   * Creates a new SingleItemEnumeration which enumerates the specified item.
   */

  public SingleItemEnumeration(Object item){
    this.item = item;
  }




  /**
   * Returns the sole item or throws a <code>NoSuchElementException</code>.
   */

  public Object nextElement(){
    if (!hasMoreElements())
      throw new NoSuchElementException();
      
    done = true;

    Object item = this.item;
    this.item = null; // We don't want to hold a reference to it any longer than we need.
    return item;
  }



  /**
   * Returns true if there are more elements in the enumeration.
   */

  public boolean hasMoreElements(){
    return !done;
  }



}
