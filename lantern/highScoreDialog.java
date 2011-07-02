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


class highScoreDialog extends JDialog
{



highScoreDialog(JFrame frame, boolean mybool, highScoreManagement scores, String title)
{
super(frame, mybool);
setTitle(title);
JLabel [] times = new JLabel[10];
JLabel [] names = new JLabel[10];
JLabel [] dates = new JLabel[10];

JPanel panel = new JPanel();
add(panel);
panel.setLayout(new GridLayout(11,3)); // rows collums
JButton OKButton = new JButton("Ok");
JButton cancelButton = new JButton("Cancel");
JLabel  titleLabel = new JLabel("  " + title);

	cancelButton.addActionListener(new ActionListener() {
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

	OKButton.addActionListener(new ActionListener() {
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

panel.add(OKButton);
panel.add(titleLabel);
panel.add(cancelButton);


for(int a=0; a<10; a++)
{
times[a]=new JLabel("---");
names[a]=new JLabel("---");
dates[a]=new JLabel("---");
panel.add(times[a]);
panel.add(names[a]);
panel.add(dates[a]);
if(a < scores.top)
{
times[a].setText("" + scores.list.get(a).score + " Seconds");
names[a].setText(scores.list.get(a).name);
dates[a].setText(scores.list.get(a).date);
}// end if scores top < a
}// end for


setSize(500,300);
setVisible(true);

}// end constructor

}// end class