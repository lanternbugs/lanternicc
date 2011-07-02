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

class Popup extends JDialog
{
JTextArea field;
String warning1;

void setWarning(String s)
{
	warning1=s;
}

Popup(JFrame frame, boolean mybool, String warning)
{
super(frame, mybool);

setDefaultCloseOperation(DISPOSE_ON_CLOSE);

//	JPanel pane = new JPanel();
//	add(pane, BorderLayout.NORTH);
//	JPanel pane2 = new JPanel();
//	add(pane2, BorderLayout.CENTER);
	 field = new JTextArea();
	 field.setColumns(50);
	 field.setRows(8);
field.setText(warning);
field.setEditable(false);
Color backcol= new Color(0,0,0);
Color forcol = new Color(255,255,255);
field.setBackground(backcol);
field.setForeground(forcol);

field.setLineWrap(true);
field.setWrapStyleWord(true);
JScrollPane myscroller = new JScrollPane(field);
//pane2.add(field);
JButton button = new JButton("Ok");



	button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{

				dispose();


				}
});






//pane2.add(button);
//pack();




	  GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);




	hGroup.addComponent(myscroller, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE, Short.MAX_VALUE);
	hGroup.addComponent(button, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE, Short.MAX_VALUE);


	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading
	//Create a sequential group v1

SequentialGroup v4 = layout.createSequentialGroup();

	//Add the group v2 tp the group v1

		v4.addComponent(myscroller, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE);
		v4.addComponent(button, 60, 60, 60);







	vGroup.addGroup(v4);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);










setSize(300,260);// this may be set after the call to popup if they want a particular size







}// end constructor
}// end class