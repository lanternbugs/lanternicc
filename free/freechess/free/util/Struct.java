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

import java.util.Hashtable;


/**
 * This is a convenient superclass for classes whose job is to be containers
 * of final (constant) data. It's especially convenient for holding a large 
 * amount of fields, since it alleviates the need for instance variables,
 * instead holding the data in a Hashtable.
 */

public class Struct{


  /**
   * A Hashtable where we hold all the properties.
   */

  private final Hashtable properties;



  /**
   * Creates a new Struct.
   */

  public Struct(){
    this(10);
  }



  /**
   * Creates a new struct with the specified estimated amount of properties it
   * will contain.
   */

  public Struct(int propertiesCount){
    properties = new Hashtable(propertiesCount);
  }



  /**
   * Sets the value of the given property. A property may not have its value
   * changed after it's been set.
   * <P><B>Known bug:</B> Since Hashtables don't allow <code>null</code> values,
   * you may change the value from <code>null</code> to any value.
   */

  protected final void setProperty(String propertyName, Object propertyValue){
    Object oldValue;
    if (propertyValue != null)
      oldValue = properties.put(propertyName, propertyValue);
    else
      oldValue = properties.get(propertyName);
    if (oldValue != null){
      properties.put(propertyName, oldValue);
      throw new IllegalArgumentException("A property's value may not be reset - attempted to change the value of "+propertyName);
    }
  }




  /**
   * Sets an integer property.
   */

  protected final void setIntegerProperty(String propertyName, int propertyValue){
    setProperty(propertyName, new Integer(propertyValue));
  }




  /**
   * Sets a character property.
   */

  protected final void setCharProperty(String propertyName, char propertyValue){
    setProperty(propertyName, new Character(propertyValue));
  }




  /**
   * Sets a boolean property.
   */

  protected final void setBooleanProperty(String propertyName, boolean propertyValue){
    setProperty(propertyName, propertyValue ? Boolean.TRUE : Boolean.FALSE);
  }




  /**
   * Sets a String property.
   */

  protected final void setStringProperty(String propertyName, String propertyValue){
    setProperty(propertyName, propertyValue);
  }




  /**
   * Returns the value of the given property.
   */

  protected final Object getProperty(Object property){
    return properties.get(property);
  }



  /**
   * Returns the value of the given integer property.
   */

  protected final int getIntegerProperty(Object property){
    return ((Integer)getProperty(property)).intValue();
  }




  /**
   * Returns the value of the given character property.
   */

  protected final char getCharProperty(Object property){
    return ((Character)getProperty(property)).charValue();
  }




  /**
   * Returns the value of the given string property.
   */

  protected final String getStringProperty(Object property){
    return (String)getProperty(property);
  }



  /**
   * Returns the value of the given boolean property.
   */

  protected final boolean getBooleanProperty(Object property){
    return ((Boolean)getProperty(property)).booleanValue();
  }


}
