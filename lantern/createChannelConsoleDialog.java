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


class createChannelConsoleDialog extends JDialog
{
JTextField field;
JFrame frame2;
channels sharedVariables;
createWindows mycreator;
subframe [] consoleSubframes;

createChannelConsoleDialog(JFrame frame, boolean mybool, channels sharedVariables1, createWindows mycreator1, subframe [] consoleSubframes1)
{
super(frame, mybool);

consoleSubframes=consoleSubframes1;
sharedVariables=sharedVariables1;
mycreator=mycreator1;

frame2=frame;
setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	JPanel pane = new JPanel();
	add(pane);
	 field = new JTextField(30);
	pane.add(field);

	JButton button = new JButton("Create Console");
	JButton button2 = new JButton("Cancel");
	button2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event)
					{
						dispose();
				}});
	JLabel label = new JLabel("edit space seperated list of channels for this console. i.e. 2 3 50");
	button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
				 String mytext= field.getText();
				 mytext=mytext.trim();
				 int make=0;
				 try
			 	{

				 while(mytext.length() > 0)
				 {
				int i= mytext.indexOf(" ");
				if(i == -1)// no more spaces last channel
				{

// sharedVariables.console[aChannelNumber 1-500] will tell me what console a channel number is in, if not setto a sub console number it goes in main, i.e its 0
				 Integer num= new Integer(mytext);
				 int num1=num.intValue();
				 if(num1 > 0 && num1 < 500)
				 sharedVariables.console[sharedVariables.openConsoleCount][num1]=1 ;
				 make=1;
				 break;
				}
				else
				{
					String temp=mytext.substring(0, i);
				 Integer num= new Integer(temp);
				 int num1=num.intValue();
				 if(num1 > 0 && num1 < 500)
				 {sharedVariables.console[sharedVariables.openConsoleCount][num1]=1 ;
				  make=1;}

				if(i+1 < mytext.length())
				mytext=mytext.substring(i+1, mytext.length());
				else
				{break;}

				}



			 	}// end else


			}// end try
			catch(Exception e)
			{
					String swarning = "Could not read your list of channels to create console. If any channel selection was valid a console will be made. Otherwise please try again. It needs to be a space seperated list with valid channel numbers. i.e. 2 3 100";

					Popup pop=new Popup(frame2, true, swarning);
					pop.setVisible(true);
			}
			finally{

				if(make==1)
				{
					setConsoleTabTitles asetter = new setConsoleTabTitles();
					asetter.createConsoleTabTitle(sharedVariables, sharedVariables.openConsoleCount, consoleSubframes, "");
					mycreator.createConsoleFrame();
					dispose();
				}
			}
				}
});
pane.add(button);
pane.add(button2);
pane.add(label);
}// end constructor





}// end class
