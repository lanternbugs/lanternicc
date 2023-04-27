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

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;


/**
 * A utility class which encodes certain common types into Strings.
 * 
 * @see StringParser
 */


public class StringEncoder{

  
  /**
   * Encodes the given Rectangle into a String in the following format:
   * "<x>;<y>;<width>;<height>" where <value> is replaced with the appropriate
   * value of the given Rectangle.
   *
   * @see StringParser#parseRectangle(String)
   */

  public static String encodeRectangle(Rectangle rect){
    StringBuffer buf = new StringBuffer();
    buf.append(rect.x);
    buf.append(";");
    buf.append(rect.y);
    buf.append(";");
    buf.append(rect.width);
    buf.append(";");
    buf.append(rect.height);

    return buf.toString();
  }



  /**
   * Encodes the given RectDouble into a String in the following format:
   * "<x>;<y>;<width>;<height>" where <value> is replaced with the appropriate
   * value of the given RectDouble.
   *
   * @see StringParser#parseRectDouble(String)
   */

  public static String encodeRectDouble(RectDouble rect){
    StringBuffer buf = new StringBuffer();
    buf.append(rect.getX());
    buf.append(";");
    buf.append(rect.getY());
    buf.append(";");
    buf.append(rect.getWidth());
    buf.append(";");
    buf.append(rect.getHeight());

    return buf.toString();
  }



  /**
   * Encodes the given Dimension into a String in the following format:
   * "<width>;<height>" where <value> is replaced with the appropriate
   * value of the given Dimension.
   *
   * @see StringParser#parseDimension(String)
   */

  public static String encodeDimension(Dimension dim){
    StringBuffer buf = new StringBuffer();
    buf.append(dim.width);
    buf.append(";");
    buf.append(dim.height);

    return buf.toString();
  }



  /**
   * Encodes the given Color into a String in RGB format where each component
   * is encoded into 2 characters as a hexadecimal string.
   */

  public static String encodeColor(Color color){
    return Integer.toHexString(color.getRGB()&0xffffff);
  }



  /**
   * Encodes the specified int list into a space delimited string.
   */

  public static String encodeIntList(int [] arr){
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < arr.length; i++){
      buf.append(String.valueOf(arr[i]));
      buf.append(" ");
    }

    if (arr.length != 0)
      buf.setLength(buf.length() - 1);

    return buf.toString();
  }



  /**
   * Escapes the specified string.
   */

  public static String encodeString(String s){
    StringBuffer buf = new StringBuffer();

    for (int i = 0; i < s.length(); i++){
      char c = s.charAt(i);
      switch (c){
        case '\n': buf.append("\\n"); break;
        case '\r': buf.append("\\r"); break;
        case '\t': buf.append("\\t"); break;
        case '\\': buf.append("\\\\"); break;
        default:
          buf.append(c);
      }
    }

    return buf.toString();
  }

  

}
