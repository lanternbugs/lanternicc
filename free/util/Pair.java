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
 * A wrapper for any two other given objects.
 */

public final class Pair{

  
  /**
   * The first object.
   */

  private final Object first;




  /**
   * The second object.
   */

  private final Object second;




  /**
   * Creates a new <code>Pair</code> with the two given objects. Either of the
   * objects may be <code>null</code>.
   */

  public Pair(Object first, Object second){
    this.first = first;
    this.second = second;
  }




  /**
   * Returns the first object.
   */

  public Object getFirst(){
    return first;
  }




  /**
   * Returns the second object.
   */

  public Object getSecond(){
    return second;
  }




  /**
   * Returns a hashcode combined from the hashcodes of the two target objects.
   */

  public int hashCode(){
    int hash1 = (first == null ? 0 : first.hashCode());
    int hash2 = (second == null ? 0 : second.hashCode());
    return hash1^hash2;
  }




  /**
   * Returns true iff the given Object is a Pair, and its two objects are the
   * same as this one's (comparison done via <code>Utilities.areEqual</code>)
   */

  public boolean equals(Object o){
    if (o == this)
      return true;

    if (!(o instanceof Pair))
      return false;

    Pair pair = (Pair)o;

    return Utilities.areEqual(pair.first, first) && Utilities.areEqual(pair.second, second);
  }

}
