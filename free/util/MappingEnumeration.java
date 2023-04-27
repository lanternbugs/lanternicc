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
 * to another <code>Enumeration</code>, but modifies the values via the
 * {@link #map(Object)} method, which can be overridden to specify different
 * mappings.
 */

public abstract class MappingEnumeration implements Enumeration{



  /**
   * The delegate enumeration.
   */

  private final Enumeration delegate;



  /**
   * Creates a new <code>MappingEnumeration</code> object with the specified
   * delegate.
   */

  public MappingEnumeration(Enumeration delegate){
    this.delegate = delegate;
  }



  /**
   * Returns whether there are more elements in this <code>Enumeration</code>.
   */

  public boolean hasMoreElements(){
    return delegate.hasMoreElements();
  }



  /**
   * Returns the result of invoking {@link #map(Object)} on the next element in
   * the delegate enumeration.
   */

  public Object nextElement() throws NoSuchElementException{
    return map(delegate.nextElement());
  }



  /**
   * Maps the specified element to some other element.
   */

  public abstract Object map(Object element);



}