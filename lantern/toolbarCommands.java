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
import java.awt.Window.*;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.Collections;


class toolbarCommands
{

 void dispatchCommand(int button, int con, boolean game, channels sharedVariables,  ConcurrentLinkedQueue<myoutput> queue)
 {

	if(sharedVariables.userButtonCommands[button].equals(""))
		return;

	myoutput output = new myoutput();


     String mes = sharedVariables.userButtonCommands[button] + "\n";
      mes=mes.replace("%opp", sharedVariables.myopponent);
      if(sharedVariables.myServer.equals("ICC") && sharedVariables.myname.length() > 0)
      {
      if(game == false)
      output.data="`c" + con + "`" + mes;
      else
     output.data="`g" + con + "`" + mes;


      }
      else
      output.data=mes;

      output.consoleNumber=con;
      queue.add(output);

 }

}