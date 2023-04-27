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
 * A LayoutManager which lays out a single component making sure its height is
 * always the same as its width. If the parent container's width differs from
 * it height, the child will be laid out according to its x and y alignment.
 */

public class SquareLayout implements LayoutManager{
  

  
  /**
   * Creates a new SquareLayout.
   */

  public SquareLayout(){

  }
  
  
  
  /**
   * Creates a new Container with SquareLayout which will contain the specified
   * component.
   */
   
  public static Container createSquareContainer(Component child){
    Container container = new ConcreteContainer();
    container.setLayout(new SquareLayout());
    container.add(child);
    
    return container;
  }



  /**
   * Adds the specified component with the specified name to the layout. 
   */

  public void addLayoutComponent(String name, Component component){
    
  }
  
  

  /**
   * Removes the specified component from the layout. 
   */

  public void removeLayoutComponent(Component component){
    
  }
  
  
  
  /**
   * Returns the sole child of the specified container, or <code>null</code> if
   * none. Throws an <code>IllegalStateException</code> if there is more than
   * one child.
   */
   
  private Component getChild(Container c){
    int childCount = c.getComponentCount();
    if (childCount > 1)
      throw new IllegalStateException("May not layout more than one component");
    else if (childCount == 0)
      return null;
    else
      return c.getComponent(childCount - 1);
  }



  /**
   * Lays out the container in the specified panel. 
   */

  public void layoutContainer(Container container){
    Component child = getChild(container);
    if (child == null)
      return;
    
    Dimension parentSize = container.getSize();
    Insets insets = container.getInsets();
    
    // A rectangle specifying the actual area available for layout. 
    Rectangle rect = new Rectangle(insets.left, insets.top,
      parentSize.width - insets.left - insets.right, parentSize.height - insets.top - insets.bottom);

    int minSize = rect.width < rect.height ? rect.width : rect.height;
    int widthSpace = rect.width - minSize;
    int heightSpace = rect.height - minSize;
    child.setBounds(rect.x + (int)(widthSpace*child.getAlignmentX()),
                    rect.y + (int)(heightSpace*child.getAlignmentY()), minSize, minSize);
  }

  

  /**
   * Calculates the minimum size dimensions for the specified panel given the 
   * components in the specified parent container. 
   */

  public Dimension minimumLayoutSize(Container container){
    Component child = getChild(container);
    if (child == null)
      return new Dimension(0, 0);
    
    Dimension childMinSize = child.getMinimumSize();
    Insets insets = container.getInsets();
    
    int width = childMinSize.width + insets.left + insets.right;
    int height = childMinSize.height + insets.top + insets.bottom;
    int maxSize = Math.max(width, height);
    return new Dimension(maxSize, maxSize);
  }




  /**
   * Calculates the preferred size dimensions for the specified panel given the
   * components in the specified parent container. 
   */

  public Dimension preferredLayoutSize(Container container){
    Component child = getChild(container);
    if (child == null)
      return new Dimension(0, 0);
    
    Dimension childPrefSize = child.getPreferredSize();
    Insets insets = container.getInsets();
    
    int width = childPrefSize.width + insets.left + insets.right;
    int height = childPrefSize.height + insets.top + insets.bottom;
    int maxSize = Math.max(width, height);
    return new Dimension(maxSize, maxSize);
  }


  
}
