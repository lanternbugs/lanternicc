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

class listInternalFrame extends JInternalFrame implements InternalFrameListener
{

	//subframe [] consoleSubframes;
channels sharedVariables;
JCheckBoxMenuItem notontop;
ConcurrentLinkedQueue queue;
//subframe(JFrame frame, boolean mybool)
listInternalFrame(Multiframe master, channels sharedVariables1, ConcurrentLinkedQueue queue1)
{
//super(frame, mybool);
 super("Activities Window- double click to select",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable

sharedVariables=sharedVariables1;
queue=queue1;

 setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

setTitle("Activities Window");
// make menu
JMenuBar myMenu = new JMenuBar();

 JMenu mymenu1 = new JMenu("Window");

   notontop = new JCheckBoxMenuItem("On Top Window");
   notontop.setSelected(true);
  notontop.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            myoutput data = new myoutput();
            data.swapActivities=1;
            queue.add(data);


            }
       });
  mymenu1.add(notontop);


myMenu.add(mymenu1);
    
    JMenu mymenu2 = new JMenu("Font");

    JMenuItem fontchange = new JMenuItem("Set Event List/Tournaments Font");
    fontchange.setSelected(true);
    fontchange.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
              master.setEventListFont();


               }
          });
     mymenu2.add(fontchange);


   myMenu.add(mymenu2);
    
    
setJMenuBar(myMenu);
myMenu.setVisible(true);

addInternalFrameListener(this);
}// end constructor


/************** jinternal frame listener ******************************/

    void setBoardSize()
     {
	if(isVisible() == false || isIcon() == true || isMaximum() == true)
        return;
        sharedVariables.myActivitiesSizes.point0=getLocation();
		//set_string = set_string + "" + point0.x + " " + point0.y + " ";
		sharedVariables.myActivitiesSizes.con0x=getWidth();
		sharedVariables.myActivitiesSizes.con0y=getHeight();
		//set_string = set_string + "" + con0x + " " + con0y + " ";

	 }


      public void internalFrameClosing(InternalFrameEvent e) {
	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
		setBoardSize();
		setVisible(false);
	}

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
	//	setBoardSize();
	}



    }

    public void internalFrameDeactivated(InternalFrameEvent e) {


    }



/****************************************************************************************/
/*void sharedVariables.openUrl(String myurl)
{

				try {

				String os = System.getProperty("os.name").toLowerCase();

					//Process p = Runtime.getRuntime().exec(cmdLine);
				Runtime rt = Runtime.getRuntime();
				if (os.indexOf( "win" ) >= 0)
	            {
				 String[] cmd = new String[4];
	              cmd[0] = "cmd.exe";
	              cmd[1] = "/C";
	              cmd[2] = "start";
	              cmd[3] = myurl;

	              rt.exec(cmd);
			  }
			 else if (os.indexOf( "mac" ) >= 0)
	           {

	             Runtime runtime = Runtime.getRuntime();
				   if(myurl.startsWith("www."))
				   myurl="http://" + myurl;
				   String[] args = { "osascript", "-e", "open location \"" + myurl + "\"" };
				   try
				   {
				     Process process = runtime.exec(args);
				   }
				   catch (IOException e)
				   {
				     // do what you want with this
				     // http://www.devdaily.com/java/mac-java-open-url-browser-osascript
				   }






	             // rt.exec( "open " + myurl);

			//String[] commandLine = { "safari", "http://www.javaworld.com/" };
			//  Process process = Runtime.getRuntime().exec(commandLine);


	          }
				else
				{             //prioritized 'guess' of users' preference
	              String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
	                  "netscape","opera","links","lynx"};

	              StringBuffer cmd = new StringBuffer();
	              for (int i=0; i<browsers.length; i++)
	                cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + myurl + "\" ");

	              rt.exec(new String[] { "sh", "-c", cmd.toString() });
	              //rt.exec("firefox http://www.google.com");
	              //System.out.println(cmd.toString());


				}// end else
			}// end try
			catch(Exception e)
			{}


}
 */

}//end class
