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
JFrame mymultiframe;
seekPanel mypanel;
ConcurrentLinkedQueue<myoutput> queue;
final JCheckBoxMenuItem allSeeks;
final JCheckBoxMenuItem humanSeeks;
final JCheckBoxMenuItem computerSeeks;

seekGraphFrame(channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1, JFrame mymultiframe1)
{
 super("Seek Graph",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable

sharedVariables=sharedVariables1;
queue=queue1;
mymultiframe = mymultiframe1;

addInternalFrameListener(this);
setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
mypanel=new seekPanel(sharedVariables, queue, 0);// 0 for  display type. show all seeks


// make menu
JMenuBar seekMenu = new JMenuBar();

 JMenu mymenu1 = new JMenu("Menu");

  allSeeks = new JCheckBoxMenuItem("All Seeks");

  allSeeks.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             mypanel.setDisplayType(0);
            SelectMenu(0);}
       });
  mymenu1.add(allSeeks);
 humanSeeks = new JCheckBoxMenuItem("Human Seeks");
  humanSeeks.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             mypanel.setDisplayType(1);
             SelectMenu(1);}
       });
  mymenu1.add(humanSeeks);

  computerSeeks = new JCheckBoxMenuItem("Computer Seeks");
  computerSeeks.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             mypanel.setDisplayType(2);
            SelectMenu(2);}
       });
  mymenu1.add(computerSeeks);

 JMenuItem showSeekDialog = new JMenuItem("Place a Seek");
  showSeekDialog.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          showSeekDialog();}
       });
  mymenu1.add(showSeekDialog);




seekMenu.add(mymenu1);
setJMenuBar(seekMenu);
seekMenu.setVisible(true);

add(mypanel);
}// end constructor


 void SelectMenu(int n)
 {
  if(n !=0)
  allSeeks.setSelected(false);
  else
  allSeeks.setSelected(true);

  if(n !=1)
  humanSeeks.setSelected(false);
  else
  humanSeeks.setSelected(true);

  if(n !=2)
  computerSeeks.setSelected(false);
  else
  computerSeeks.setSelected(true);


 }

void showSeekDialog()
{
//JFrame mytempframe = new JFrame();
seekGameDialog myseeker = new seekGameDialog(mymultiframe, false, sharedVariables, queue);
int defaultWidth = 425;
int defaultHeight = 220;
myseeker.setSize(defaultWidth,defaultHeight);
    myseeker.setLocation(mymultiframe.getLocation().x + mymultiframe.getSize().width / 2  - defaultWidth / 2, mymultiframe.getLocation().y + mymultiframe.getSize().height / 2 - defaultHeight / 2);

/*try {
	Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        int screenW = dim.width;
        int screenH = dim.height;
      int px = (int) ((screenW - defaultWidth) / 2);
      if(px < 50)
       px=50;
      int py = (int) ((screenH - defaultHeight) / 2);
      if(py < 50)
       py=50;


      myseeker.setLocation(px, py);
}
catch(Exception centerError){}
 */

myseeker.setTitle("Seek a Game");

myseeker.setVisible(true);

}




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
