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


class autoExamDialog extends JDialog
{
JTextField field;
channels sharedVariables;
autoExamDialog(JFrame frame, boolean mybool, channels sharedVariables1)
{
super(frame, mybool);
sharedVariables=sharedVariables1;

setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	JPanel pane = new JPanel();
	add(pane);
	 field = new JTextField(20);
	pane.add(field);

	JButton button = new JButton("Set speed");

	JLabel label = new JLabel("Enter AutoExam speed in seconds, now " + sharedVariables.autoexamspeed/1000 + "s");
	JCheckBox abox = new JCheckBox();
	if(sharedVariables.autoexamnoshow==1)
	abox.setSelected(true);
	else
	abox.setSelected(false);
	button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
				 String mytext= field.getText();
				 mytext=mytext.trim();
				 try
			 	{



				 Integer num= new Integer(mytext);
				 int num1=num.intValue();
				 if(num1 > 0 && num1 < 500)
				 sharedVariables.autoexamspeed=num1 * 1000;


				}
				catch(Exception e)
				{

				}
				finally
				{
				dispose();
				}

				}
});



abox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
				 sharedVariables.autoexamnoshow=(sharedVariables.autoexamnoshow+1)%2;
				}


});

JLabel label2 = new JLabel("Check box to not show Forward 1 message when autoexamining.");


pane.add(button);
pane.add(label);
pane.add(abox);
pane.add(label2);
}// end constructor
}// end class

