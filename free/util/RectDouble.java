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


/**
 * A rectangle with coordinates represented by double values. No restrictions
 * are enforced on the different values. This class is not thread safe.
 */

public final class RectDouble{



  /**
   * The x coordinate.
   */

  private double x;



  /**
   * The y coordinate.
   */

  private double y;



  /**
   * The width of the rectangle.
   */

  private double width;



  /**
   * The height of the rectangle.
   */

  private double height;



  /**
   * Creates a new <code>RectDouble</code> with the specified value.
   */

  public RectDouble(double x, double y, double width, double height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }



  /**
   * Creates a copy of the specified <code>RectDouble</code>.
   */

  public RectDouble(RectDouble rect){
    this.x = rect.x;
    this.y = rect.y;
    this.width = rect.width;
    this.height = rect.height;
  }



  /**
   * Creates a RectDouble equivalent to the specified
   * <code>java.awt.Rectangle</code>.
   */

  public RectDouble(Rectangle rect){
    this.x = rect.x;
    this.y = rect.y;
    this.width = rect.width;
    this.height = rect.height;
  }



  /**
   * Returns the x coordinate.
   */

  public double getX(){
    return x;
  }



  /**
   * Returns the y coordinate.
   */

  public double getY(){
    return y;
  }



  /**
   * Returns the width.
   */

  public double getWidth(){
    return width;
  }



  /**
   * Returns the height.
   */

  public double getHeight(){
    return height;
  }



  /**
   * Sets the x coordinate.
   *
   * @return this
   */

  public RectDouble setX(double x){
    this.x = x;

    return this;
  }



  /**
   * Sets the y coordinate.
   *
   * @return this
   */

  public RectDouble setY(double y){
    this.y = y;

    return this;
  }



  /**
   * Sets the width.
   *
   * @return this
   */

  public RectDouble setWidth(double width){
    this.width = width;

    return this;
  }



  /**
   * Sets the height.
   *
   * @return this
   */

  public RectDouble setHeight(double height){
    this.height = height;

    return this;
  }



  /**
   * Scales this rectangle by the specified values.
   *
   * @return this
   */

  public RectDouble scale(double sx, double sy){
    this.x *= sx;
    this.y *= sy;
    this.width *= sx;
    this.height *= sy;

    return this;
  }



  /**
   * Creates a </code>java.awt.Rectangle</code> object from this one by casting
   * all the values to ints.
   */

  public Rectangle toRect(){
    return new Rectangle((int)x, (int)y, (int)width, (int)height);
  }



  /**
   * Returns whether this RectDouble is equal to the specified one.
   */

  public boolean equals(Object o){
    if (!(o instanceof RectDouble))
      return false;

    RectDouble rect = (RectDouble)o;

    return (x == rect.x) && (y == rect.y) && (rect.width == width) && (rect.height == height);
  }



  /**
   * Returns the hash code for this <code>RectDouble</code>.
   */

  public int hashCode(){
    int res = 37;
    res = 17*res + Utilities.hashCode(x);
    res = 17*res + Utilities.hashCode(y);
    res = 17*res + Utilities.hashCode(width);
    res = 17*res + Utilities.hashCode(height);

    return res;
  }



  /**
   * Returns a textual representation of this <code>RectDouble</code>.
   */

  public String toString(){
    return "[x=" + x + ", y=" + y + ", w=" + width + ", h=" + height + "]";
  }
   


}
