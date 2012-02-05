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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package free.util;

import java.awt.*;
import java.util.Vector;


/**
 * A LayoutManager which lays out the components in a table-like structure.
 * Unlike <code>GridLayout</code>, the sizes of the rows and columns
 * are dynamic, although properly aligned. The cell sizes are determined
 * according to the preferred sizes of the components and each component is
 * sized to either its maximum size or the cell size. Components are positioned
 * within their cells according to their X and Y alignments.
 * When a new component is added, it is placed in the first empty cell, in
 * lexigraphic order. A new row is created if necessary.
 * To create an empty cell, simply add blank component.
 */

public class TableLayout implements LayoutManager2{


  /**
   * The amount of columns in the table.
   */

  private final int columnCount;


  /**
   * The gap between columns, in pixels.
   */

  private final int xGap;


  /**
   * The gap between rows, in pixels.
   */

  private final int yGap;



  /**
   * A Vector of rows where each row is a Component array holding the components
   * in that row.
   */

  private final Vector rows = new Vector();



  /**
   * Creates a new TableLayout with the specified amount of columns,
   * horizontal/vertical gaps between columns/cells.
   */

  public TableLayout(int columnCount, int xGap, int yGap){
    if (columnCount <= 0)
      throw new IllegalArgumentException("The amount of columns must be positive");
    if (xGap < 0)
      throw new IllegalArgumentException("The horizontal gap may not be negative: "+xGap);
    if (yGap < 0)
      throw new IllegalArgumentException("The vertical gap may not be negative: "+yGap);

    this.columnCount = columnCount;
    this.xGap = xGap;
    this.yGap = yGap;
  }



  /**
   * Creates a new TableLayout with the specified amount of columns.
   */

  public TableLayout(int columnCount){
    this(columnCount, 0, 0);
  }




  /**
   * Adds the specified component to the layout.
   */

  public void addLayoutComponent(Component component, Object constraints){
    synchronized(component.getTreeLock()){
      int rowCount = rows.size();
      for (int i = 0; i < rowCount; i++){
        Component [] row = (Component [])rows.elementAt(i);
        for (int j = 0; j < row.length; j++){
          if (row[j] == null){
            row[j] = component;
            return;
          }
        }
      }

      Component [] newRow = new Component[columnCount];
      newRow[0] = component;
      rows.addElement(newRow);
    }
  }



  /**
   * Throws an exception.
   */

  public void addLayoutComponent(String name, Component component){
    throw new UnsupportedOperationException("deprecated addLayoutComponent(String, Component)");
  }




  /**
   * Removes the specified component from the layout.
   */

  public void removeLayoutComponent(Component component){
    synchronized(component.getTreeLock()){
      int rowCount = rows.size();
      outer: for (int i = 0; i < rowCount; i++){
        Component [] row = (Component [])rows.elementAt(i);
        for (int j = 0; j < row.length; j++){
          if (row[j] == component){
            row[j] = null;
            break outer;
          }
        }
      }

      // Remove any empty rows at the end.
      for (int i = rowCount - 1; i >= 0; i--){
        Component [] row = (Component [])rows.elementAt(i);
        boolean isEmpty = true;
        for (int j = 0; j < row.length; j++){
          if (row[j] != null){
            isEmpty = false;
            break;
          }
        }
        if (isEmpty)
          rows.removeElementAt(i);
        else
          break;
      }
    }
  }




  /**
   * Returns a matrix of Dimension objects specifying the preferred sizes of the
   * components we are going to layout.
   */

  private Dimension [][] getPreferredSizes(Container parent){
    int rowCount = rows.size();
    Dimension [][] prefSizes = new Dimension[rowCount][columnCount];

    for (int i = 0; i < rowCount; i++){
      Component [] row = (Component [])rows.elementAt(i);
      for (int j = 0; j < columnCount; j++){
        Component component = row[j];

        // Can only happen on the last line when all the remaining components are null as well
        if (component == null) 
          break;

        if (component.getParent() != parent)
          throw new IllegalStateException("Bad parent specified");

        prefSizes[i][j] = component.getPreferredSize();
      }
    }

    return prefSizes;
  }



  /**
   * Calculates and returns a Pair where the first object is an array holding
   * the column widths of our layout and the second is the rowHeights.
   */

  private Pair calculateLayout(Dimension [][] prefSizes){
    int rowCount = rows.size();

    int [] columnWidths = new int[columnCount];
    int [] rowHeights = new int[rowCount];

    // Find the maximum preferred row heights and column widths.
    for (int i = 0; i < rowCount; i++){
      for (int j = 0; j < columnCount; j++){
        Dimension prefSize = prefSizes[i][j];

        // Can only happen on the last line when all the remaining components are null as well
        if (prefSize == null)
          break;

        columnWidths[j] = Math.max(columnWidths[j], prefSize.width);
        rowHeights[i] = Math.max(rowHeights[i], prefSize.height);
      }
    }

    return new Pair(columnWidths, rowHeights);
  }




  /**
   * Lays out the specified container. Throws an
   * <code>IllegalStateException</code> if any of the components added via the
   * <code>addLayoutComponent</code> method have a different parent than the
   * specified Container.
   */

  public void layoutContainer(Container parent){
    synchronized(parent.getTreeLock()){
      int rowCount = rows.size();

      Insets parentInsets = parent.getInsets();

      // Collect the preferred sizes.
      Dimension [][] prefSizes = getPreferredSizes(parent);
      Pair layout = calculateLayout(prefSizes);
      int [] columnWidths = (int [])layout.getFirst();
      int [] rowHeights = (int [])layout.getSecond();

      Dimension prefParentSize = calculatePreferredLayoutSize(parent, columnWidths, rowHeights);
      Dimension parentSize = parent.getSize();
      Dimension layoutSize = 
        new Dimension(parentSize.width - xGap*(rowCount - 1) - parentInsets.left - parentInsets.right,
                      parentSize.height - yGap*(columnCount - 1) - parentInsets.top - parentInsets.bottom);
      Dimension prefLayoutSize =
        new Dimension(prefParentSize.width - xGap*(rowCount - 1) - parentInsets.left - parentInsets.right,
                      prefParentSize.height - yGap*(columnCount - 1) - parentInsets.top - parentInsets.bottom);

      // Layout the components.
      int y = parentInsets.top;
      for (int i = 0; i < rowCount; i++){
        int x = parentInsets.left;
        int cellHeight = (rowHeights[i]*layoutSize.height)/prefLayoutSize.height;
        Component [] row = (Component [])rows.elementAt(i);
        for (int j = 0; j < row.length; j++){
          int cellWidth = (columnWidths[j]*layoutSize.width)/prefLayoutSize.width;
          Component component = row[j];

          // Can only happen on the last line when all the remaining components are null as well
          if (component == null)
            break;

          Dimension maxSize = component.getMaximumSize();

          int compWidth = Math.min(maxSize.width, cellWidth);
          int compHeight = Math.min(maxSize.height, cellHeight);

          int compX = x + (int)((cellWidth - compWidth)*component.getAlignmentX());
          int compY = y + (int)((cellHeight - compHeight)*component.getAlignmentY());

          component.setBounds(compX, compY, compWidth, compHeight);

          x += cellWidth + xGap;
        }

        y += cellHeight + yGap;
      }
    }
  }



  /**
   * We're not caching anything yet, so this call is ignored.
   */

  public void invalidateLayout(Container parent){

  }



  /**
   * Returns the preferred layout for the specified parent container.
   */

  public Dimension preferredLayoutSize(Container parent){
    synchronized(parent.getTreeLock()){
      Dimension [][] prefSizes = getPreferredSizes(parent);
      Pair layout = calculateLayout(prefSizes);
      int [] columnWidths = (int [])layout.getFirst();
      int [] rowHeights = (int [])layout.getSecond();

      return calculatePreferredLayoutSize(parent, columnWidths, rowHeights);
    }
  }



  /**
   * Calculates the preferred layout size for the specified preferred column
   * widths and row heights.
   */

  private Dimension calculatePreferredLayoutSize(Container parent, int [] columnWidths, int [] rowHeights){
    int prefWidth = 0;
    int prefHeight = 0;

    for (int i = 0; i < columnWidths.length; i++)
      prefWidth += columnWidths[i];
    for (int i = 0; i < rowHeights.length; i++)
      prefHeight += rowHeights[i];

    // Add the gaps
    prefWidth += xGap*(columnWidths.length - 1);
    prefHeight += yGap*(rowHeights.length - 1);

    // Add parent insets
    Insets parentInsets = parent.getInsets();
    prefWidth += parentInsets.left + parentInsets.right;
    prefHeight += parentInsets.top + parentInsets.bottom;

    
    return new Dimension(prefWidth, prefHeight);
  }



  /**
   * Returns the same as <code>preferredLayoutSize</code>.
   */

  public Dimension minimumLayoutSize(Container parent){
    return preferredLayoutSize(parent);
  }




  /**
   * Returns a dimension with maximum possible values.
   */

  public Dimension maximumLayoutSize(Container parent){
    return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
  }



  /**
   * Returns <code>CENTER_ALIGNMENT</code>;
   */

  public float getLayoutAlignmentX(Container parent) {
    return Component.CENTER_ALIGNMENT;
  }



  /**
   * Returns <code>CENTER_ALIGNMENT</code>;
   */

  public float getLayoutAlignmentY(Container parent) {
    return Component.CENTER_ALIGNMENT;
  }


}
