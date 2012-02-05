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

import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.util.EventObject;


/**
 * An <code>ActionListener</code> and a <code>WindowListener</code> which
 * disposes of the given Window when one of <code>actionPerformed</code> or
 * <code>windowClosing</code> methods are called.
 */

public class WindowDisposingListener implements ActionListener, WindowListener{



  /**
   * The target Window.
   */

  private final Window targetWindow;



  /**
   * Creates a new <code>WindowDisposingListener</code> with the given target
   * <code>Window</code>.
   */

  public WindowDisposingListener(Window targetWindow){
    this.targetWindow = targetWindow;
  }



  /**
   * This method is called just before the target <code>Window</code> is
   * disposed. It is meant to be overridden to allow subclasses run their own
   * code when the window is being closed. The default implementation does
   * nothing.
   */

  protected void beforeDisposing(EventObject evt){}


  
  /**
   * Calls <code>dispose()</code> on the target <code>Window</code>.
   */

  public void actionPerformed(ActionEvent evt){
    beforeDisposing(evt);
    targetWindow.dispose();
  }



  /**
   * Calls <code>dispose()</code> on the target <code>Window</code>.
   */

  public void windowClosing(WindowEvent evt){
    beforeDisposing(evt);
    targetWindow.dispose();
  }


  public void windowOpened(WindowEvent evt){}
  public void windowClosed(WindowEvent evt){}
  public void windowIconified(WindowEvent evt){}
  public void windowDeiconified(WindowEvent evt){}
  public void windowActivated(WindowEvent evt){}
  public void windowDeactivated(WindowEvent evt){}

}
