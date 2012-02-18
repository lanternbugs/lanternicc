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

class notifyFrame extends JDialog// implements InternalFrameListener
{

	//subframe [] consoleSubframes;
channels sharedVariables;
JCheckBoxMenuItem notontop;
 listClass notifyList;
 ConcurrentLinkedQueue queue;
notifyPanel notifylistScrollerPanel;


notifyFrame(JFrame master, channels sharedVariables1, ConcurrentLinkedQueue queue1,  listClass notifyList1)
{     super(master, false);
sharedVariables=sharedVariables1;
queue=queue1;
notifyList=notifyList1;

 setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
 addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent we) {

         setVisible(false);
    }
});

setTitle("Notify Window");

 notifylistScrollerPanel = new notifyPanel(sharedVariables, queue,  notifyList);

 notifylistScrollerPanel.notifylistScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


 add(notifylistScrollerPanel);


}// end constructor






}//end class