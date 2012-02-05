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

import java.util.StringTokenizer;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;


/**
 * A utility class which parses certain common types from Strings.
 *
 * @see StringEncoder
 */

public class StringParser{


  
  /**
   * Parses the given String as a Rectangle object. The expected format is:
   * "<x>;<y>;<width>;<height>" where <value> is replaced by an integer value.
   *
   * @throws FormatException if the given String is not in the expected wrong
   * format.
   *
   * @see StringEncoder#encodeRectangle(Rectangle)
   */

  public static Rectangle parseRectangle(String rectString){
    StringTokenizer tokenizer = new StringTokenizer(rectString, ";");
    if (tokenizer.countTokens() != 4)
      throw new FormatException("Wrong Rectangle format: " + rectString);
    
    try{
      int x = Integer.parseInt(tokenizer.nextToken());
      int y = Integer.parseInt(tokenizer.nextToken());
      int width = Integer.parseInt(tokenizer.nextToken());
      int height = Integer.parseInt(tokenizer.nextToken());
      return new Rectangle(x, y, width, height);
    } catch (NumberFormatException e){
        throw new FormatException(e,"Wrong Rectangle format: " + rectString);
      }
  }



  /**
   * Parses the given String as a RectDouble object. The expected format is:
   * "<x>;<y>;<width>;<height>" where <value> is replaced by a double value,
   * parseable by {@link Double#valueOf(String)}.
   *
   * @throws FormatException if the given String is not in the expected wrong
   * format.
   *
   * @see StringEncoder#encodeRectDouble(RectDouble)
   */

  public static RectDouble parseRectDouble(String rectString){
    StringTokenizer tokenizer = new StringTokenizer(rectString, ";");
    if (tokenizer.countTokens() != 4)
      throw new FormatException("Wrong Rectangle format: " + rectString);
    
    try{
      double x = Double.valueOf(tokenizer.nextToken()).doubleValue();
      double y = Double.valueOf(tokenizer.nextToken()).doubleValue();
      double width = Double.valueOf(tokenizer.nextToken()).doubleValue();
      double height = Double.valueOf(tokenizer.nextToken()).doubleValue();
      return new RectDouble(x, y, width, height);
    } catch (NumberFormatException e){
        throw new FormatException(e,"Wrong Rectangle format: " + rectString);
      }
  }



  /**
   * Parses the given String as a Dimension object. The expected format is:
   * "<width>;<height>" where <value> is replaced by an integer value.
   *
   * @throws FormatException if the given String is not in the expected wrong
   * format.
   *
   * @see StringEncoder#encodeDimension(Dimension)
   */

  public static Dimension parseDimension(String dimString){
    StringTokenizer tokenizer = new StringTokenizer(dimString, ";");
    if (tokenizer.countTokens() != 2)
      throw new FormatException("Wrong Dimension format: " + dimString);
    
    try{
      int width = Integer.parseInt(tokenizer.nextToken());
      int height = Integer.parseInt(tokenizer.nextToken());
      return new Dimension(width, height);
    } catch (NumberFormatException e){
        throw new FormatException(e,"Wrong Dimension format: " + dimString);
      }
  }



  /**
   * Parses the given string as a color. The expected format is a hexadecimal
   * value in the range [0..0xffffff] in RGB format.
   *
   * @throws FormatException if the given String is not in the correct format.
   */

  public static Color parseColor(String colorString){
    try{
      int colorInt = Integer.parseInt(colorString, 16);
      if ((colorInt < 0) || (colorInt > 0xffffffL))
        throw new FormatException("Wrong Color format: " + colorString);

      return new Color(colorInt);
    } catch (NumberFormatException e){
        throw new FormatException(e, "Wrong Color format: " + colorString);
      }
  }



  /**
   * Parses the given string as a list of integers.
   */

  public static int [] parseIntList(String text){
    return TextUtilities.parseIntList(text, " ");
  }



  /**
   * Unescapes the specified string.
   */

  public static String parseString(String s){
    StringBuffer buf = null;
    for (int i = 0; i < s.length(); i++){
      char c = s.charAt(i);
      if (c == '\\'){
        if (buf == null)
          buf = new StringBuffer(s.substring(0, i)); // Initialize lazily.
        c = s.charAt(++i);
        switch (c){
          case 'n': buf.append('\n'); break;
          case 'r': buf.append('\r'); break;
          case 't': buf.append('\t'); break;
          case '\\': buf.append('\\'); break;
          default:
            buf.append('\'');
            buf.append(c);
        }
      }
      else if (buf != null)
        buf.append(c);
    }

    return buf == null ? s : buf.toString();
  }

}
