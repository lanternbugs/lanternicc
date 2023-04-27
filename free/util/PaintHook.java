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

import java.awt.Graphics;
import java.awt.Component;


/**
 * An interface suitable for classes which want to allow extending their
 * (complex) painting behaviour. The interface has one method - 
 * <code>paint(Component, Graphics)</code> which should be called by the
 * service component at some point during its painting.
 */

public interface PaintHook{



  /**
   * This method should be called by the service component sometime during its
   * painting.
   *
   * @param component The Component which is painting.
   * @param g The Graphics object which is doing the painting.
   */

  void paint(Component component, Graphics g);


}
