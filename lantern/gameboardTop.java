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
import java.util.StringTokenizer;
import java.util.concurrent.locks.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.event.ChangeEvent.*;
import java.util.concurrent.locks.*;
import java.util.Random;



class gameboardTop extends JFrame  implements  WindowListener
{



void setSelected(boolean home)
{
 return;
}
boolean isSelected()
{

 return false;
}

// myconsolepanel queue sharedVariables
 channels sharedVariables;
 gameboardConsolePanel myconsolepanel;
 ConcurrentLinkedQueue<myoutput> queue;
gamestuff gameData;

gameboardTop( channels sharedVariables1, gameboardConsolePanel myconsolepanel1, ConcurrentLinkedQueue<myoutput> queue1, gamestuff gameData1)
{

addWindowListener(this);
queue=queue1;
myconsolepanel=myconsolepanel1;
sharedVariables=sharedVariables1;
gameData=gameData1;
setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

if(sharedVariables.useTopGames == true)
setAlwaysOnTop(true);
}
     void setBoardSize()
     {
       if(sharedVariables.useTopGames == false)
       return;


	try {	sharedVariables.myBoardSizes[gameData.BoardIndex].point0=getLocation();
		//set_string = set_string + "" + point0.x + " " + point0.y + " ";
		sharedVariables.myBoardSizes[gameData.BoardIndex].con0x=getWidth();
		sharedVariables.myBoardSizes[gameData.BoardIndex].con0y=getHeight();
		//set_string = set_string + "" + con0x + " " + con0y + " ";
     }
     catch(Exception darn){}
	 }



    public void windowClosing(WindowEvent e) {
 	// we want to serialize the window dimensions
        if(sharedVariables.useTopGames == false)
       return;

	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
		setBoardSize();
	}



		if(myconsolepanel.Input.hasFocus() && myconsolepanel.myself!=null)
			myconsolepanel.myself.switchConsoleWindows();

		setVisible(false);
		if(sharedVariables.mygame[gameData.LookingAt].state != sharedVariables.STATE_PLAYING)
		{
            myoutput data = new myoutput();
            data.closetab=gameData.LookingAt;
            queue.add(data);

		}

    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {
        if(sharedVariables.useTopGames == false)
       return;
   	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
	//	setBoardSize();
	}

    }

    public void windowActivated(WindowEvent e) {
        if(sharedVariables.useTopGames == false)
       return;
 	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
	//	setBoardSize();
	}
    giveFocus();

    }

    public void windowDeactivated(WindowEvent e) {
       if(sharedVariables.useTopGames == false)
       return;
   myconsolepanel.Input.setFocusable(false);
    }

    public void windowGainedFocus(WindowEvent e) {

    }

    public void windowLostFocus(WindowEvent e) {

    }

    public void windowStateChanged(WindowEvent e) {

    }



  boolean isMaximum()
  {
   return false;
  }

  boolean isIcon()
  {
   return false;
  }

  void setMaximum(boolean home)
{
  return;
  }












void giveFocus()
{          if(sharedVariables.useTopGames == false)
       return;

 SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {



                           myconsolepanel.Input.setFocusable(true);
                               myconsolepanel.Input.setRequestFocusEnabled(true);

                           myconsolepanel.Input.requestFocusInWindow();

                            } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });

}






}