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


class customizeConsolelColorsDialog extends JDialog
{
JPaintedLabel shoutLabel;
JPaintedLabel sshoutLabel;
JPaintedLabel tellLabel;
JPaintedLabel qtellLabel;
JPaintedLabel backgroundLabel;
JPaintedLabel defaultChannelLabel;
JPaintedLabel responseTextLabel;
JPaintedLabel nonResponseTextLabel;
JPaintedLabel kibLabel;
JPaintedLabel typedLabel;

JButton shoutButton;
JButton sshoutButton;
JButton tellButton;
JButton qtellButton;
JButton backgroundButton;
JButton defaultChannelButton;
JButton responseTextButton;
JButton nonResponseTextButton;
JButton okButton;
JButton cancelButton;
JButton kibButton;
JButton typedButton;

JCheckBox shoutItalic;
JCheckBox shoutBold;
JCheckBox sshoutItalic;
JCheckBox sshoutBold;
JCheckBox tellItalic;
JCheckBox tellBold;
JCheckBox qtellItalic;
JCheckBox qtellBold;
JCheckBox responseItalic;
JCheckBox responseBold;
JCheckBox nonResponseItalic;
JCheckBox nonResponseBold;
JCheckBox kibItalic;
JCheckBox kibBold;
JCheckBox typedItalic;
JCheckBox typedBold;




private JTextPane [] consoles;
private JTextPane [] gameconsoles;

channels sharedVariables;

customizeConsolelColorsDialog(JFrame frame, boolean mybool, channels sharedVariables1, JTextPane [] consoles1, JTextPane [] gameconsoles1)
{
super(frame, mybool);
sharedVariables=sharedVariables1;
consoles=consoles1;
gameconsoles=gameconsoles1;

setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	JPanel pane = new JPanel();
	add(pane);

JLabel boldLabel1 = new JLabel("Bold");
JLabel italicLabel1 = new JLabel("Italic");

JLabel boldLabel2 = new JLabel("Bold");
JLabel italicLabel2 = new JLabel("Italic");

JLabel boldLabel3 = new JLabel("Bold");
JLabel italicLabel3 = new JLabel("Italic");

JLabel boldLabel4 = new JLabel("Bold");
JLabel italicLabel4 = new JLabel("Italic");

JLabel boldLabel5 = new JLabel("Bold");
JLabel italicLabel5 = new JLabel("Italic");

JLabel boldLabel6 = new JLabel("Bold");
JLabel italicLabel6 = new JLabel("Italic");

JLabel boldLabel7 = new JLabel("Bold");
JLabel italicLabel7 = new JLabel("Italic");

JLabel boldLabel8 = new JLabel("Bold");
JLabel italicLabel8 = new JLabel("Italic");


shoutItalic = new JCheckBox();
if(sharedVariables.shoutStyle == 1 || sharedVariables.shoutStyle == 3)
shoutItalic.setSelected(true);

shoutBold = new JCheckBox();
if(sharedVariables.shoutStyle == 2 || sharedVariables.shoutStyle == 3)
shoutBold.setSelected(true);


sshoutItalic = new JCheckBox();
if(sharedVariables.sshoutStyle == 1 || sharedVariables.sshoutStyle == 3)
sshoutItalic.setSelected(true);


sshoutBold = new JCheckBox();
if(sharedVariables.sshoutStyle == 2 || sharedVariables.sshoutStyle == 3)
sshoutBold.setSelected(true);

;
tellItalic = new JCheckBox();
if(sharedVariables.tellStyle == 1 || sharedVariables.tellStyle == 3)
tellItalic.setSelected(true);

tellBold = new JCheckBox();
if(sharedVariables.tellStyle == 2 || sharedVariables.tellStyle == 3)
tellBold.setSelected(true);

qtellItalic = new JCheckBox();
if(sharedVariables.qtellStyle == 1 || sharedVariables.qtellStyle == 3)
qtellItalic.setSelected(true);

qtellBold = new JCheckBox();
if(sharedVariables.qtellStyle == 2 || sharedVariables.qtellStyle == 3)
qtellBold.setSelected(true);

responseItalic = new JCheckBox();
if(sharedVariables.responseStyle == 1 || sharedVariables.responseStyle == 3)
responseItalic.setSelected(true);

responseBold = new JCheckBox();
if(sharedVariables.responseStyle == 2 || sharedVariables.responseStyle == 3)
responseBold.setSelected(true);

nonResponseItalic = new JCheckBox();
if(sharedVariables.nonResponseStyle == 1 || sharedVariables.nonResponseStyle == 3)
nonResponseItalic.setSelected(true);

nonResponseBold = new JCheckBox();
if(sharedVariables.nonResponseStyle == 2 || sharedVariables.nonResponseStyle == 3)
nonResponseBold.setSelected(true);

kibItalic = new JCheckBox();
if(sharedVariables.kibStyle == 1 || sharedVariables.kibStyle == 3)
kibItalic.setSelected(true);

kibBold = new JCheckBox();
if(sharedVariables.kibStyle == 2 || sharedVariables.kibStyle == 3)
kibBold.setSelected(true);

typedItalic = new JCheckBox();
if(sharedVariables.typedStyle == 1 || sharedVariables.typedStyle == 3)
typedItalic.setSelected(true);

typedBold = new JCheckBox();
if(sharedVariables.typedStyle == 2 || sharedVariables.typedStyle == 3)
typedBold.setSelected(true);


	sshoutLabel = new JPaintedLabel("S-Shout Color", sharedVariables);
	sshoutLabel.fontType=0;
	sshoutLabel.setOpaque(true);
	sshoutLabel.setBackground(sharedVariables.BackColor);
	sshoutLabel.setForeground(sharedVariables.sshoutcolor);
	sshoutButton = new JButton("Colorize");
	sshoutButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.sshoutcolor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose S-Shout Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.sshoutcolor=newColor;
				sshoutLabel.setForeground(sharedVariables.sshoutcolor);
			}
		}// end try
			catch(Exception e)
			{}
		}
});


	shoutLabel = new JPaintedLabel("Shout Color", sharedVariables);
	shoutLabel.fontType=0;
	shoutLabel.setOpaque(true);
	shoutLabel.setBackground(sharedVariables.BackColor);
	shoutLabel.setForeground(sharedVariables.shoutcolor);
	shoutButton = new JButton("Colorize");
	shoutButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.shoutcolor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose Shout Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.shoutcolor=newColor;
				shoutLabel.setForeground(sharedVariables.shoutcolor);
			}
		}// end try
			catch(Exception e)
			{}
		}
});




// tell colors
	tellLabel = new JPaintedLabel("Tell Color", sharedVariables);
	tellLabel.setForeground(sharedVariables.tellcolor);
	tellLabel.fontType=0;
	tellLabel.setOpaque(true);
	tellLabel.setBackground(sharedVariables.BackColor);

	tellButton = new JButton("Colorize");
	tellButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.tellcolor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose Tell Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.tellcolor=newColor;
				tellLabel.setForeground(sharedVariables.tellcolor);
			}
		}// end try
			catch(Exception e)
			{}
		}
});


// qtell colors
	qtellLabel = new JPaintedLabel("Qtell Color", sharedVariables);
	qtellLabel.setForeground(sharedVariables.qtellcolor);
	qtellLabel.fontType=0;
	qtellLabel.setOpaque(true);
	qtellLabel.setBackground(sharedVariables.BackColor);
	qtellButton = new JButton("Colorize");
	qtellButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.qtellcolor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose Qtell Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.qtellcolor=newColor;
				qtellLabel.setForeground(sharedVariables.qtellcolor);
			}
		}// end try
			catch(Exception e)
			{}
		}
});

//  default channel color
	defaultChannelLabel = new JPaintedLabel("Default Channel Color", sharedVariables);
	defaultChannelLabel.setForeground(sharedVariables.defaultChannelColor);
	defaultChannelLabel.fontType=0;
	defaultChannelLabel.setOpaque(true);
	defaultChannelLabel.setBackground(sharedVariables.BackColor);
	defaultChannelButton = new JButton("Colorize");
	defaultChannelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.defaultChannelColor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose Default Channel Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.defaultChannelColor=newColor;
				defaultChannelLabel.setForeground(sharedVariables.defaultChannelColor);
			}
		}// end try
			catch(Exception e)
			{}
		}
});


//  kib color
	kibLabel = new JPaintedLabel("Kibitz Color", sharedVariables);
	kibLabel.setForeground(sharedVariables.kibcolor);
	kibLabel.fontType=0;
	kibLabel.setOpaque(true);
	kibLabel.setBackground(sharedVariables.BackColor);
	kibButton = new JButton("Colorize");
	kibButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.kibcolor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose Kibitz Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.kibcolor=newColor;
			    kibLabel.setForeground(sharedVariables.kibcolor);
			}
		}// end try
			catch(Exception e)
			{}
		}
});


//  response Text color
	responseTextLabel = new JPaintedLabel("Response Text Color", sharedVariables);
	responseTextLabel.setForeground(sharedVariables.responseColor);
	responseTextLabel.fontType=0;
	responseTextLabel.setOpaque(true);
	responseTextLabel.setBackground(sharedVariables.BackColor);
	responseTextButton = new JButton("Colorize");
	responseTextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.responseColor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose Response Text Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.responseColor=newColor;
			    responseTextLabel.setForeground(sharedVariables.responseColor);
			}
		}// end try
			catch(Exception e)
			{}
		}
});

//  non response Text color
    String responeType = channels.fics ? "Server Text " : "Non Response Text ";
	nonResponseTextLabel = new JPaintedLabel(responeType + "Color", sharedVariables);
	nonResponseTextLabel.setForeground(sharedVariables.ForColor);
	nonResponseTextLabel.fontType=0;
	nonResponseTextLabel.setOpaque(true);
	nonResponseTextLabel.setBackground(sharedVariables.BackColor);
	nonResponseTextButton = new JButton("Colorize");
	nonResponseTextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.ForColor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose " + responeType + "Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.ForColor =newColor;
			    nonResponseTextLabel.setForeground(sharedVariables.ForColor);
			    for(int a = 0; a< sharedVariables.maxConsoles; a++)
			    	if(consoles[a]!=null)
			    	if(sharedVariables.tabStuff[a].ForColor == null)
			    		consoles[a].setForeground(sharedVariables.ForColor);
			    for(int a = 0; a< sharedVariables.maxGameConsoles; a++)
			    	if(gameconsoles[a]!=null)
			    		gameconsoles[a].setForeground(sharedVariables.ForColor);

			}
		}// end try
			catch(Exception e)
			{}
		}
});

// typed color
	typedLabel = new JPaintedLabel("Typed Color", sharedVariables);
	typedLabel.setForeground(sharedVariables.typedColor);
	typedLabel.fontType=0;
	typedLabel.setOpaque(true);
	typedLabel.setBackground(sharedVariables.BackColor);

	typedButton = new JButton("Colorize");
	typedButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.typedColor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose Typed Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.typedColor=newColor;
				typedLabel.setForeground(sharedVariables.typedColor);
			}
		}// end try
			catch(Exception e)
			{}
		}
});




//  background color
	backgroundLabel = new JPaintedLabel("Background Color", sharedVariables);
	backgroundLabel.setForeground(sharedVariables.ForColor);
	backgroundLabel.fontType=0;


	backgroundLabel.setOpaque(true);
	backgroundLabel.setBackground(sharedVariables.BackColor);
	backgroundButton = new JButton("Colorize");
	backgroundButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				 JDialog frame = new JDialog();
 				Color defaultCol=sharedVariables.BackColor;
 				Color newColor = JColorChooser.showDialog(frame, "Choose Background Color", defaultCol);
		    if(newColor != null)
		    {
				sharedVariables.BackColor =newColor;
			    updateLabelBackgrounds(sharedVariables.BackColor);
			    for(int a = 0; a< sharedVariables.maxConsoles; a++)
			    	if(consoles[a]!=null)
			    	if(sharedVariables.tabStuff[a].BackColor == null)
			    		consoles[a].setBackground(sharedVariables.BackColor);
			    for(int a = 0; a< sharedVariables.maxGameConsoles; a++)
			    	if(gameconsoles[a]!=null)
			    		gameconsoles[a].setBackground(sharedVariables.BackColor);

			}
		}// end try
			catch(Exception e)
			{}
		}
});






okButton = new JButton("Ok");
cancelButton = new JButton("Cancel");
okButton.setBackground(new Color(230, 220, 220));
cancelButton.setBackground(new Color(230, 220, 220));

	okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{



				if(shoutItalic.isSelected() == true && shoutBold.isSelected() == true)
					sharedVariables.shoutStyle=3;
				else if(shoutItalic.isSelected() == true)
					sharedVariables.shoutStyle=1;
				else if(shoutBold.isSelected() == true)
					sharedVariables.shoutStyle=2;
				else
					sharedVariables.shoutStyle=0;


				if(sshoutItalic.isSelected() == true && sshoutBold.isSelected() == true)
					sharedVariables.sshoutStyle=3;
				else if(sshoutItalic.isSelected() == true)
					sharedVariables.sshoutStyle=1;
				else if(sshoutBold.isSelected() == true)
					sharedVariables.sshoutStyle=2;
				else
					sharedVariables.sshoutStyle=0;

				if(tellItalic.isSelected() == true && tellBold.isSelected() == true)
					sharedVariables.tellStyle=3;
				else if(tellItalic.isSelected() == true)
					sharedVariables.tellStyle=1;
				else if(tellBold.isSelected() == true)
					sharedVariables.tellStyle=2;
				else
					sharedVariables.tellStyle=0;


				if(qtellItalic.isSelected() == true && qtellBold.isSelected() == true)
					sharedVariables.qtellStyle=3;
				else if(qtellItalic.isSelected() == true)
					sharedVariables.qtellStyle=1;
				else if(qtellBold.isSelected() == true)
					sharedVariables.qtellStyle=2;
				else
					sharedVariables.qtellStyle=0;


				if(responseItalic.isSelected() == true && responseBold.isSelected() == true)
					sharedVariables.responseStyle=3;
				else if(responseItalic.isSelected() == true)
					sharedVariables.responseStyle=1;
				else if(responseBold.isSelected() == true)
					sharedVariables.responseStyle=2;
				else
					sharedVariables.responseStyle=0;


				if(nonResponseItalic.isSelected() == true && nonResponseBold.isSelected() == true)
					sharedVariables.nonResponseStyle=3;
				else if(nonResponseItalic.isSelected() == true)
					sharedVariables.nonResponseStyle=1;
				else if(nonResponseBold.isSelected() == true)
					sharedVariables.nonResponseStyle=2;
				else
					sharedVariables.nonResponseStyle=0;

				if(kibItalic.isSelected() == true && kibBold.isSelected() == true)
					sharedVariables.kibStyle=3;
				else if(kibItalic.isSelected() == true)
					sharedVariables.kibStyle=1;
				else if(kibBold.isSelected() == true)
					sharedVariables.kibStyle=2;
				else
					sharedVariables.kibStyle=0;

				if(typedItalic.isSelected() == true && typedBold.isSelected() == true)
					sharedVariables.typedStyle=3;
				else if(typedItalic.isSelected() == true)
					sharedVariables.typedStyle=1;
				else if(typedBold.isSelected() == true)
					sharedVariables.typedStyle=2;
				else
					sharedVariables.typedStyle=0;



					dispose();

		}// end try
			catch(Exception e)
			{}
		}
});
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
int rows = 11;
    if(channels.fics) {
        rows = 10;
    }
pane.setLayout(new GridLayout(rows,2)); // rows collums
JPanel shoutPanel = new JPanel();
shoutPanel.add(shoutButton);
shoutPanel.add(italicLabel1);
shoutPanel.add(shoutItalic);
shoutPanel.add(boldLabel1);
shoutPanel.add(shoutBold);

pane.add(shoutPanel);
pane.add(shoutLabel);

JPanel sshoutPanel = new JPanel();
sshoutPanel.add(sshoutButton);
sshoutPanel.add(italicLabel2);
sshoutPanel.add(sshoutItalic);
sshoutPanel.add(boldLabel2);
sshoutPanel.add(sshoutBold);

pane.add(sshoutPanel);
pane.add(sshoutLabel);

JPanel tellPanel = new JPanel();
tellPanel.add(tellButton);
tellPanel.add(italicLabel3);
tellPanel.add(tellItalic);
tellPanel.add(boldLabel3);
tellPanel.add(tellBold);


pane.add(tellPanel);
pane.add(tellLabel);

JPanel qtellPanel = new JPanel();
qtellPanel.add(qtellButton);
qtellPanel.add(italicLabel4);
qtellPanel.add(qtellItalic);
qtellPanel.add(boldLabel4);
qtellPanel.add(qtellBold);


pane.add(qtellPanel);
pane.add(qtellLabel);

pane.add(defaultChannelButton);
pane.add(defaultChannelLabel);

JPanel kibPanel = new JPanel();
kibPanel.add(kibButton);
kibPanel.add(italicLabel5);
kibPanel.add(kibItalic);
kibPanel.add(boldLabel5);
kibPanel.add(kibBold);


pane.add(kibPanel);
pane.add(kibLabel);

JPanel typedPanel = new JPanel();
typedPanel.add(typedButton);
typedPanel.add(italicLabel8);
typedPanel.add(typedItalic);
typedPanel.add(boldLabel8);
typedPanel.add(typedBold);


pane.add(typedPanel);
pane.add(typedLabel);

if(!channels.fics) {
    JPanel responsePanel = new JPanel();
    responsePanel.add(responseTextButton);
    responsePanel.add(italicLabel6);
    responsePanel.add(responseItalic);
    responsePanel.add(boldLabel6);
    responsePanel.add(responseBold);


    pane.add(responsePanel);
    pane.add(responseTextLabel);
}


JPanel nonResponsePanel = new JPanel();
nonResponsePanel.add(nonResponseTextButton);
nonResponsePanel.add(italicLabel7);
nonResponsePanel.add(nonResponseItalic);
nonResponsePanel.add(boldLabel7);
nonResponsePanel.add(nonResponseBold);


pane.add(nonResponsePanel);
pane.add(nonResponseTextLabel);

pane.add(backgroundButton);
pane.add(backgroundLabel);


pane.add(okButton);
pane.add(cancelButton);


setSize(470,400);
setTitle("Console Color Chooser");
setLocation(75,70);
setVisible(true);
}// end constructor


void updateLabelBackgrounds(Color c)
{
shoutLabel.setBackground(c);
tellLabel.setBackground(c);
qtellLabel.setBackground(c);
backgroundLabel.setBackground(c);
defaultChannelLabel.setBackground(c);
responseTextLabel.setBackground(c);
nonResponseTextLabel.setBackground(c);
kibLabel.setBackground(c);
typedLabel.setBackground(c);
}
}// end class
