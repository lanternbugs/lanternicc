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

class qsuggest extends JDialog
{


JTextPane myTextPane;
JScrollPane myScrollPane;
JButton OkButton;
JButton CancelButton;
String command;
String id;
    String suggestor;
ConcurrentLinkedQueue<myoutput> queue;

qsuggest(JFrame frame, boolean mybool,  ConcurrentLinkedQueue<myoutput> queue1)
{
super(frame, mybool);
queue=queue1;
setTitle("Suggestion");
setSize(250, 150);
myTextPane = new JTextPane();
myTextPane.setEditable(false);
myScrollPane = new JScrollPane(myTextPane);
OkButton = new JButton("Ok");
CancelButton = new JButton("Cancel");
	OkButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 myoutput output = new myoutput();
				 output.data="`c0`" + command + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);
				dispose();
		}// end try
			catch(Exception e)
			{}
		}
});
CancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				dispose();
		}// end try
			catch(Exception e)
			{}
		}
});


JSuggestPanel mypanel = new JSuggestPanel();
add(mypanel);


}
void suggestion(String text, String mycommand, String myid, String suggestor1)
{
	command=mycommand;
    suggestor = suggestor1;
    if(myid.equals(suggestor)) {
        myTextPane.setText(text + "\n\n");
    } else {
        myTextPane.setText(suggestor + " suggests: " + command + "\n\n");
    }
	
	id=myid;

}

class JSuggestPanel extends JPanel
{
  JSuggestPanel()
  {


	  GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
	//setLayout(layout);
	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h1 = layout.createSequentialGroup();


	SequentialGroup middle = layout.createSequentialGroup();
	ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

	//Add a scroll pane and a label to the parallel group h2
	h2.addComponent(myScrollPane, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE);
	middle.addComponent(OkButton, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);
	middle.addComponent(CancelButton, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);







h2.addGroup(middle);
h1.addGroup(h2);


	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading
	//Create a sequential group v1

SequentialGroup v4 = layout.createSequentialGroup();

ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

	ParallelGroup vbot = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

//vmiddle.addContainerGap();
	ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

	//Add the group v2 tp the group v1

		vbot.addComponent(OkButton, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE);
		vbot.addComponent(CancelButton, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE);


		//v1.addComponent(myScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		v1.addComponent(myScrollPane);


v4.addGroup(v1);
v4.addGroup(vbot);


	vGroup.addGroup(v4);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);


}// end constructor
}// end class panel

}// end dialoge class
