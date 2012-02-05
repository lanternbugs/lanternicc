/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2002, 2003 Alexander Maryanovsky.
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

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;


/**
 * A collection of general utility methods.
 */

public class Utilities{
  
  
  
  /**
   * A 0-length Object array.
   */
   
  public static final Object [] EMPTY_ARRAY = new Object[0];



  /**
   * A 0-length long array.
   */
   
  public static final long [] EMPTY_LONG_ARRAY = new long[0];



  /**
   * A 0-length int array.
   */
   
  public static final int [] EMPTY_INT_ARRAY = new int[0];
  

  
  /**
   * A 0-length short array.
   */
   
  public static final short [] EMPTY_SHORT_ARRAY = new short[0];

  

  /**
   * A 0-length byte array.
   */
   
  public static final byte [] EMPTY_BYTE_ARRAY = new byte[0];
  
  
  
  /**
   * A 0-length char array.
   */
   
  public static final char [] EMPTY_CHAR_ARRAY = new char[0];



  /**
   * A 0-length double array.
   */
   
  public static final double [] EMPTY_DOUBLE_ARRAY = new double[0];
  


  /**
   * A 0-length float array.
   */
   
  public static final float [] EMPTY_FLOAT_ARRAY = new float[0];



  /**
   * A 0-length String array.
   */
   
  public static final String [] EMPTY_STRING_ARRAY = new String[0];
  
  
  
  /**
   * An empty enumeration.
   */
  
  public static final Enumeration EMPTY_ENUM = new Enumeration(){
    public boolean hasMoreElements(){return false;}
    public Object nextElement(){throw new NoSuchElementException();}
  };
  
  

  /**
   * Returns <code>true</code> if the two specified objects are the same.
   * Returns <code>false</code> otherwise. To be considered the same, the two
   * references must either both be null or invoking <code>equals</code> on one
   * of them with the other must return <code>true</code>.
   */

  public static boolean areEqual(Object obj1, Object obj2){
    return (obj1 == obj2) || (obj1 == null ? false : obj1.equals(obj2));
  }




  /**
   * Maps the specified key to the specified value in the specified
   * <code>Hashtable</code>. If the specified value is <code>null</code> any
   * existing mapping of the specified key is removed from the
   * <code>Hashtable</code>. The old value mapped to the specified key
   * is returned, or <code>null</code> if no value was mapped to the key.
   */

  public static Object put(Hashtable table, Object key, Object value){
    return value == null ? table.remove(key) : table.put(key, value);
  }



  /**
   * Returns <code>true</code> if the specified object is an element of the
   * specified array. The specified array may not be <code>null</code>. The
   * specified object may be <code>null</code>, in which case this method will
   * return <code>true</code> iff one of the indices in the array is empty 
   * (contains <code>null</code>).
   */

  public static boolean contains(Object [] array, Object item){
    return (indexOf(array, item) != -1);
  }



  /**
   * Returns the index of the first occurrance of specified object in the
   * specified array, or -1 if the specified object is not an element of the
   * specified array. The specified object may be <code>null</code> in which
   * case the returned index will be the index of the first <code>null</code>
   * in the array.
   */

  public static int indexOf(Object [] array, Object item){
    if (array == null)
      throw new IllegalArgumentException("The specified array may not be null");

    for (int i = 0; i < array.length; i++)
      if (areEqual(item, array[i]))
        return i;

    return -1;
  }




  /**
   * Returns the index of the first occurrance of specified integer in the
   * specified array, or -1 if the specified integer is not an element of the
   * specified array.
   */

  public static int indexOf(int [] arr, int val){
    if (arr == null)
      throw new IllegalArgumentException("The specified array may not be null");

    for (int i = 0; i < arr.length; i++)
      if (arr[i] == val)
        return i;

    return -1;
  }

  
  
  /**
   * Converts the specified array into a string by appending all its elements
   * separated by a semicolon.
   */

  public static String arrayToString(Object [] arr){
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < arr.length; i++){
      buf.append(arr[i]);
      buf.append("; ");
    }
    if (arr.length > 0)
      buf.setLength(buf.length() - 2); // get rid of the extra "; "

    return buf.toString();
  }




  /**
   * Converts the specified <code>Hashtable</code> into a string by putting
   * each key and value on a separate line (separated by '\n') and an arrow
   * (" -> ") between them.
   */

  public static String hashtableToString(Hashtable hashtable){
    StringBuffer buf = new StringBuffer();
    Enumeration keys = hashtable.keys();
    while (keys.hasMoreElements()){
      Object key = keys.nextElement();
      Object value = hashtable.get(key);
      buf.append(key.toString());
      buf.append(" -> ");
      buf.append(value.toString());
      buf.append("\n");
    }

    return buf.toString();
  }




  /**
   * Returns the maximum element in the specified integer array.
   */

  public static int max(int [] arr){
    if (arr == null)
      throw new IllegalArgumentException("The specified array may not be null");
    if (arr.length == 0)
      throw new IllegalArgumentException("The specified array must have at least one element");

    int n = arr[0];
    for (int i = 1; i < arr.length; i++)
      if (arr[i] > n)
        n = arr[i];

    return n;
  }



  /**
   * Returns a hash code for the specified double value.
   */

  public static int hashCode(double val){
    return hashCode(Double.doubleToLongBits(val));
  }



  /**
   * Returns a hash code for the specified long value.
   */

  public static int hashCode(long val){
    return (int)(val ^ (val >>> 32));
  }
  
  
  
  /**
   * Returns the name of the package of the specified class.
   */
  
  public static String getPackageName(Class c){
    return getPackageName(c.getName());
  }
  
  
  
  /**
   * Returns the name of the package of the class with the specified (full) name.
   */
  
  public static String getPackageName(String className){
    int lastDotIndex = className.lastIndexOf(".");
    return lastDotIndex == -1 ? "" : className.substring(0, lastDotIndex);
  }
  
  
  
  /**
   * Returns the short name (excluding the package name) of the specified class. 
   */
  
  public static String getClassName(Class c){
    return getClassName(c.getName());
  }
  
  
  
  /**
   * Returns the short name (excluding the package name) of the class with the
   * specified fully qualified name.
   */
  
  public static String getClassName(String className){
    int lastDotIndex = className.lastIndexOf(".");
    return lastDotIndex == -1 ? className : className.substring(lastDotIndex + 1);
  }
  
  
  
}
