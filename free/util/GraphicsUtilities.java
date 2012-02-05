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

import java.awt.*;


/**
 * Provides graphics related utilities.
 */

public class GraphicsUtilities{


  /**
   * Calculates the largest size of the given font for which the given string 
   * will fit into the given size. This method uses the default toolkit.
   */

  public static int getMaxFittingFontSize(Graphics g, Font font, String string, Dimension size){
    return getMaxFittingFontSize(g, font, string, size.width, size.height);
  }

  

  /**
   * Calculates the largest size of the given font for which the given string 
   * will fit into the given size.
   */

  public static int getMaxFittingFontSize(Graphics g, Font font, String string, int width, int height){
    int minSize = 0;
    int maxSize = 288;
    int curSize = font.getSize();

    while (maxSize - minSize > 2){
      FontMetrics fm = g.getFontMetrics(new Font(font.getName(), font.getStyle(), curSize));
      int fontWidth = fm.stringWidth(string);
      int fontHeight = fm.getLeading() + fm.getMaxAscent() + fm.getMaxDescent();

      if ((fontWidth > width) || (fontHeight > height)){
        maxSize = curSize;
        curSize = (maxSize + minSize) / 2;
      }
      else{
        minSize = curSize;
        curSize = (minSize + maxSize) / 2;
      }
    }

    return curSize;
  }
  
  
  
  
  /**
   * Returns the <code>FontMetrics</code> for the specified <code>Font</code>.  
   */
  
  public static FontMetrics getFontMetrics(Font font){
    return Toolkit.getDefaultToolkit().getFontMetrics(font); // DEPRECATED    
  }





  /**
   * Returns <code>true</code> if the first rectangle completely contains the
   * second one. Returns <code>false</code> otherwise. This method is needed
   * because there is no Rectangle.contains(Rectangle) method in JDK 1.1
   * (unlike claimed in the 1.2 documentation).
   */

  public static boolean contains(Rectangle rect1, Rectangle rect2){
    return (rect2.x >= rect1.x) &&
           (rect2.y >= rect1.y) &&
           (rect2.x+rect2.width <= rect1.x+rect1.width) &&
           (rect2.y+rect2.height <= rect1.y+rect1.height);
  }




  /**
   * Returns true if the two specified rectangles intersect. Two rectangles
   * intersect if their intersection is nonempty. This method is not really
   * needed because Rectangle.intersects(Rectangle) exists in JDK1.1, but I
   * still like having it here for symmetry.
   */

  public static boolean intersect(Rectangle rect1, Rectangle rect2){
    return rect1.intersects(rect2);
  }



}
