/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2003 Alexander Maryanovsky.
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
 * An implementation of the <code>Enumeration</code> interface which delegates
 * to another <code>Enumeration</code>, but only returns elements which pass
 * the {@link #accept(Object)} method.
 */

public abstract class FilteringEnumeration implements Enumeration{



  /**
   * The delegate enumeration.
   */

  private final Enumeration delegate;



  /**
   * The next element we'll return. This is set by the <code>findNext</code>
   * method.
   */

  private Object next = null;



  /**
   * Creates a new <code>FilteringEnumeration</code> object with the specified
   * delegate.
   */

  public FilteringEnumeration(Enumeration delegate){
    this.delegate = delegate;
  }



  /**
   * Finds the next element in the delegate enumeration which passes
   * <code>accept</code> and puts it in <code>next</code>.
   */

  private void findNext(){
    if (next != null)
      return;

    while (delegate.hasMoreElements()){
      Object element = delegate.nextElement();
      if (accept(element)){
        next = element;
        break;
      }
    }
  }



  /**
   * Returns whether there are more elements in this <code>Enumeration</code>.
   */

  public boolean hasMoreElements(){
    findNext();

    return next != null;
  }



  /**
   * Returns the next element in the delegate enumeration which passes the
   * <code>accept</code> method.
   */

  public Object nextElement() throws NoSuchElementException{
    findNext();

    if (next == null)
      throw new NoSuchElementException();

    Object result = next;
    next = null;
    return result;
  }



  /**
   * Returns whether the specified object passes the filter.
   */

  public abstract boolean accept(Object element);



}