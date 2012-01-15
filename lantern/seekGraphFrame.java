package lantern;
/*
*  Copyright (C) 2010 Michael Ronald Adams.
*  All rights reserved.
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
*  This code is distributed in the hope that it will
*  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  General Public License for more details.
*/

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JDialog;
import java.io.*;
import java.net.*;
import java.lang.Thread.*;
import java.applet.*;
import javax.swing.GroupLayout.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import java.lang.Integer;
import javax.swing.text.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.applet.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.datatransfer.Clipboard;
import java.lang.reflect.Method;


class seekGraphFrame extends JInternalFrame implements InternalFrameListener
{
channels sharedVariables;
seekPanel mypanel;
ConcurrentLinkedQueue<myoutput> queue;


seekGraphFrame(channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1)
{
 super("Seek Graph",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable

sharedVariables=sharedVariables1;
queue=queue1;

addInternalFrameListener(this);
setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
mypanel=new seekPanel(sharedVariables, queue, 0);// 0 for  display type. show all seeks
add(mypanel);
}// end constructor









/************** jinternal frame listener ******************************/

     void setBoardSize()
     {
		if(isVisible() && isMaximum() == false && isIcon() == false)
	{
          
          sharedVariables.mySeekSizes.point0=getLocation();
		//set_string = set_string + "" + point0.x + " " + point0.y + " ";
	sharedVariables.mySeekSizes.con0x=getWidth();
		sharedVariables.mySeekSizes.con0y=getHeight();
		//set_string = set_string + "" + con0x + " " + con0y + " ";
         }

	}
      public void internalFrameClosing(InternalFrameEvent e) {
	// we want to serialize the window dimensions

	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
		setBoardSize();
	}
		setVisible(false);

    }

    public void internalFrameClosed(InternalFrameEvent e) {

    }

    public void internalFrameOpened(InternalFrameEvent e) {

    }

    public void internalFrameIconified(InternalFrameEvent e) {

    }

    public void internalFrameDeiconified(InternalFrameEvent e) {

	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
		setBoardSize();
	}


	   }

    public void internalFrameActivated(final InternalFrameEvent e) {
     // System.out.println("fame activate");


	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
		setBoardSize();
	}
//    giveFocus();


    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
//myconsolepanel.Input.setFocusable(false);

    }

/*void giveFocus()
{
 SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                              //  JComponent comp = DataViewer.getSubcomponentByName(e.getInternalFrame(),
                                //SearchModel.SEARCHTEXT);


                           myconsolepanel.Input.setFocusable(true);
                               myconsolepanel.Input.setRequestFocusEnabled(true);
                                //Input.requestFocus();
                           myconsolepanel.Input.requestFocusInWindow();

                            } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });

}
*/
/****************************************************************************************/





}// end class