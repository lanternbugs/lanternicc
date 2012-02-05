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

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * This class provides the functionality of storing and retrieving bean
 * properties while automatically notifying any listeners. It can be used to
 * delegate the bean storing, retrieving and notifying work to it from the
 * actual bean. It doesn't, however, support constrained properties.
 * <STRONG>Note:</STRONG>This class is not thread safe.
 */

public class BeanProperties{


  /**
   * The source bean.
   */

  private final Object source;



  /**
   * The hashtable where we keep the properties.
   */

  private final Hashtable props = new Hashtable();



  /**
   * The property change listeners.
   */

  private final EventListenerList listenerList = new EventListenerList();




  /**
   * Creates a new <code>BeanProperties</code> object with the specified source
   * bean.
   */

  public BeanProperties(Object source){
    this.source = source;
  }



  /**
   * Creates a new <code>BeanProperties</code> object with itself as the source.
   */

  public BeanProperties(){
    this.source = this;
  }



  /**
   * Registers the specified <code>PropertyChangeListener</code>.
   */

  public void addPropertyChangeListener(PropertyChangeListener listener){
    listenerList.add(PropertyChangeListener.class, listener);
  }



  /**
   * Unregisters the specified <code>PropertyChangeListener</code>.
   */

  public void removePropertyChangeListener(PropertyChangeListener listener){
    listenerList.remove(PropertyChangeListener.class, listener);
  }



  /**
   * Fires a <code>PropertyChangeEvent</code> to all registered property change
   * listeners.
   */

  private void firePropertyChanged(String propertyName, Object oldValue, Object newValue){
    PropertyChangeEvent evt = new PropertyChangeEvent(source, propertyName, oldValue, newValue);

    Object [] listeners = listenerList.getListenerList();
    for (int i = 0; i < listeners.length; i += 2){
      if (listeners[i] == PropertyChangeListener.class){
        PropertyChangeListener listener = (PropertyChangeListener)listeners[i+1];
        listener.propertyChange(evt);
      }
    }
  }



  /**
   * Returns an enumeration of all the property names.
   */

  public Enumeration getPropertyNames(){
    return props.keys();
  }



  /**
   * Sets the property with the specified name to the specified value.
   */

  public void setProperty(String propertyName, Object propertyValue){
    Object oldValue = propertyValue == null ? 
      props.remove(propertyName) : props.put(propertyName, propertyValue);
    if (!Utilities.areEqual(oldValue, propertyValue))
      firePropertyChanged(propertyName, oldValue, propertyValue);
  }



  /**
   * Returns the value of the property with the specified name. Throws a
   * <code>NoSuchElementException</code> if no property with the specified name
   * exists.
   */

  public Object getProperty(String propertyName){
    Object result = getProperty(propertyName, null);
    if (result == null)
      throw new NoSuchElementException(propertyName);

    return result;
  }



  /**
   * Returns the value of the property with the specified name or the specified
   * default value if there is no property with the specified name.
   */

  public Object getProperty(String propertyName, Object defaultValue){
    Object value = props.get(propertyName);
    return value == null ? defaultValue : value;
  }



  /**
   * Sets the property with the specified name to the specified
   * <code>boolean</code> value.
   */

  public void setBooleanProperty(String propertyName, boolean propertyValue){
    setProperty(propertyName, propertyValue ? Boolean.TRUE : Boolean.FALSE);
  }



  /**
   * Returns the value of the <code>boolean</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Boolean</code>. If there is
   * no property with the specified name, a <code>NoSuchElementException</code>
   * is thrown.
   */

  public boolean getBooleanProperty(String propertyName){
    return ((Boolean)getProperty(propertyName)).booleanValue();
  }



  /**
   * Returns the value of the <code>boolean</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Boolean</code>. If there is
   * no property with the specified name, the specified default value is
   * returned.
   */

  public boolean getBooleanProperty(String propertyName, boolean defaultValue){
    Boolean value = (Boolean)getProperty(propertyName, null);
    return value == null ? defaultValue : value.booleanValue();
  }



  /**
   * Sets the property with the specified name to the specified
   * <code>byte</code> value.
   */

  public void setByteProperty(String propertyName, byte propertyValue){
    setProperty(propertyName, new Byte(propertyValue));
  }



  /**
   * Returns the value of the <code>byte</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Byte</code>. If there is
   * no property with the specified name, a <code>NoSuchElementException</code>
   * is thrown.
   */

  public byte getByteProperty(String propertyName){
    return ((Byte)getProperty(propertyName)).byteValue();
  }



  /**
   * Returns the value of the <code>byte</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Byte</code>. If there is
   * no property with the specified name, the specified default value is
   * returned.
   */

  public byte getByteProperty(String propertyName, byte defaultValue){
    Byte value = (Byte)getProperty(propertyName, null);
    return value == null ? defaultValue : value.byteValue();
  }



  /**
   * Sets the property with the specified name to the specified
   * <code>short</code> value.
   */

  public void setShortProperty(String propertyName, short propertyValue){
    setProperty(propertyName, new Short(propertyValue));
  }



  /**
   * Returns the value of the <code>short</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Short</code>. If there is
   * no property with the specified name, a <code>NoSuchElementException</code>
   * is thrown.
   */

  public short getShortProperty(String propertyName){
    return ((Short)getProperty(propertyName)).shortValue();
  }



  /**
   * Returns the value of the <code>short</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Short</code>. If there is
   * no property with the specified name, the specified default value is
   * returned.
   */

  public short getShortProperty(String propertyName, short defaultValue){
    Short value = (Short)getProperty(propertyName, null);
    return value == null ? defaultValue : value.shortValue();
  }



  /**
   * Sets the property with the specified name to the specified
   * <code>int</code> value.
   */

  public void setIntegerProperty(String propertyName, int propertyValue){
    setProperty(propertyName, new Integer(propertyValue));
  }



  /**
   * Returns the value of the <code>int</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Integer</code>. If there is
   * no property with the specified name, a <code>NoSuchElementException</code>
   * is thrown.
   */

  public int getIntegerProperty(String propertyName){
    return ((Integer)getProperty(propertyName)).intValue();
  }



  /**
   * Returns the value of the <code>int</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Integer</code>. If there is
   * no property with the specified name, the specified default value is
   * returned.
   */

  public int getIntegerProperty(String propertyName, int defaultValue){
    Integer value = (Integer)getProperty(propertyName, null);
    return value == null ? defaultValue : value.intValue();
  }



  /**
   * Sets the property with the specified name to the specified
   * <code>long</code> value.
   */

  public void setLongProperty(String propertyName, int propertyValue){
    setProperty(propertyName, new Long(propertyValue));
  }



  /**
   * Returns the value of the <code>long</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Long</code>. If there is
   * no property with the specified name, a <code>NoSuchElementException</code>
   * is thrown.
   */

  public long getLongProperty(String propertyName){
    return ((Long)getProperty(propertyName)).longValue();
  }



  /**
   * Returns the value of the <code>long</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Long</code>. If there is
   * no property with the specified name, the specified default value is
   * returned.
   */

  public long getLongProperty(String propertyName, long defaultValue){
    Long value = (Long)getProperty(propertyName, null);
    return value == null ? defaultValue : value.longValue();
  }



  /**
   * Sets the property with the specified name to the specified
   * <code>float</code> value.
   */

  public void setFloatProperty(String propertyName, float propertyValue){
    setProperty(propertyName, new Float(propertyValue));
  }



  /**
   * Returns the value of the <code>float</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Float</code>. If there is
   * no property with the specified name, a <code>NoSuchElementException</code>
   * is thrown.
   */

  public float getFloatProperty(String propertyName){
    return ((Float)getProperty(propertyName)).floatValue();
  }



  /**
   * Returns the value of the <code>float</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Float</code>. If there is
   * no property with the specified name, the specified default value is
   * returned.
   */

  public float getFloatProperty(String propertyName, float defaultValue){
    Float value = (Float)getProperty(propertyName, null);
    return value == null ? defaultValue : value.floatValue();
  }



  /**
   * Sets the property with the specified name to the specified
   * <code>double</code> value.
   */

  public void setDoubleProperty(String propertyName, double propertyValue){
    setProperty(propertyName, new Double(propertyValue));
  }



  /**
   * Returns the value of the <code>double</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Double</code>. If there is
   * no property with the specified name, a <code>NoSuchElementException</code>
   * is thrown.
   */

  public double getDoubleProperty(String propertyName){
    return ((Double)getProperty(propertyName)).doubleValue();
  }



  /**
   * Returns the value of the <code>double</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Double</code>. If there is
   * no property with the specified name, the specified default value is
   * returned.
   */

  public double getDoubleProperty(String propertyName, double defaultValue){
    Double value = (Double)getProperty(propertyName, null);
    return value == null ? defaultValue : value.doubleValue();
  }



  /**
   * Sets the property with the specified name to the specified
   * <code>char</code> value.
   */

  public void setCharacterProperty(String propertyName, char propertyValue){
    setProperty(propertyName, new Character(propertyValue));
  }



  /**
   * Returns the value of the <code>char</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Character</code>. If there
   * is no property with the specified name, a
   * <code>NoSuchElementException</code> is thrown.
   */

  public char getCharacterProperty(String propertyName){
    return ((Character)getProperty(propertyName)).charValue();
  }



  /**
   * Returns the value of the <code>char</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.Character</code>. If there
   * is no property with the specified name, the specified default value is
   * returned.
   */

  public char getCharacterProperty(String propertyName, char defaultValue){
    Character value = (Character)getProperty(propertyName, null);
    return value == null ? defaultValue : value.charValue();
  }



  /**
   * Sets the property with the specified name to the specified
   * <code>String</code>.
   */

  public void setStringProperty(String propertyName, String propertyValue){
    setProperty(propertyName, propertyValue);
  }



  /**
   * Returns the value of the <code>String</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.String</code>. If there is
   * no property with the specified name,  a
   * <code>NoSuchElementException</code> is thrown.
   */

  public String getStringProperty(String propertyName){
    return (String)getProperty(propertyName);
  }



  /**
   * Returns the value of the <code>String</code> property with the specified
   * name. Throws a <code>ClassCastException</code> if current value of the
   * property is not an instance of <code>java.lang.String</code>. If there is
   * no property with the specified name, the specified default value is
   * returned.
   */

  public String getStringProperty(String propertyName, String defaultValue){
    String value = (String)getProperty(propertyName, null);
    return value == null ? defaultValue : value;
  }



}
