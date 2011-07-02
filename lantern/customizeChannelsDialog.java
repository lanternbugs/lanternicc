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
import java.util.ArrayList;;


class customizeChannelsDialog extends JDialog
{
JTextField field;
JFrame frame2;
int conNumber;
channels sharedVariables;
JCheckBox gamenotify;
JCheckBox shouts;
JCheckBox shoutsAlso;
JCheckBox sshouts;
JCheckBox names;
JCheckBox announcements;
JCheckBox name;
JLabel nameLabel;
JCheckBox deleteCheckBox;
JLabel deleteLabel;
JTextField nameField;
boolean deleteOn;

subframe [] consoleSubframes;
JCheckBox mybox;


String myTabName = "";

String getChannels(int num)
{
String mychannelnums="";

boolean mainAlso=false;
boolean also=false;
int a;


for(a=0; a<500; a++)
if(sharedVariables.console[num][a]==1)
{
	if(sharedVariables.mainAlso[a] == true)
	{
		mainAlso=true;
	}
	else
	{
		mainAlso=false;
		break;
	}
}

for(a=0; a<500; a++)
if(sharedVariables.console[num][a]==1)
{
int i=0;

for(int b=1; b<sharedVariables.maxConsoleTabs; b++)
	if(sharedVariables.console[b][a]==1)
		i++;
if(i>1)
{
	also=true;
}
else
{
	also=false;
	break;
}

}// end outer if before for

if(mainAlso == true)
	mybox.setSelected(true);
if(also == true)
	deleteCheckBox.setSelected(true);

for(a=0; a<500; a++)
if(sharedVariables.console[num][a]==1)
{
	mychannelnums=mychannelnums + a;


	if(sharedVariables.mainAlso[a] == true && mainAlso==false)
	mychannelnums = mychannelnums + "m";
	if(also == false)
	{
		int i=0;
		for(int b=1; b<sharedVariables.maxConsoleTabs; b++)
			if(sharedVariables.console[b][a]==1)
			i++;
		if(i>1)
		{
			mychannelnums = mychannelnums + "a";

		}
	}// if also == false

mychannelnums = mychannelnums + " ";


}


return mychannelnums;

}




void resetChannels(int num)
{
for(int a=0; a<500; a++)
sharedVariables.console[num][a]=0;
}


customizeChannelsDialog(JFrame frame, boolean mybool, int conNumberInputed, channels sharedVariables1, subframe [] consoleSubframes1)
{
super(frame, mybool);
sharedVariables=sharedVariables1;
consoleSubframes=consoleSubframes1;
deleteOn=true;

conNumber=conNumberInputed;
frame2=frame;
setDefaultCloseOperation(DISPOSE_ON_CLOSE);
setTitle("Customize channels for Tab" + conNumber);
	JPanel pane = new JPanel();
	Color newCol = new Color(255, 255, 240);
	pane.setBackground(newCol);
	add(pane);
	 field = new JTextField(30);

	 name= new JCheckBox();
	 nameLabel = new JLabel("let me name tab");
	 nameField=new JTextField(30);
if(!sharedVariables.consoleTabCustomTitles[conNumber].equals(""))
nameField.setText(sharedVariables.consoleTabCustomTitles[conNumber]);

	JPanel warningpanel = new JPanel();
	warningpanel.setLayout(new GridLayout(2,1));


	JLabel label = new JLabel("Type a space seperated list of channels for this tab. i.e. 2 3 50   50m is a shortcut to keep that channel on main also.");
	JLabel labelOptions = new JLabel("Options below for if all these channels should continue to print to main.");
	warningpanel.add(label);
	warningpanel.add(labelOptions);
	pane.add(warningpanel);


	pane.add(field);
	// we set fields text below after dialog set up because if we find all main or all also we need to check checkboxes
	JButton button = new JButton("Customize Tab");
	gamenotify = new JCheckBox();
	JLabel gamenotifyLabel = new JLabel("Check for gnotify to this tab");
	if(sharedVariables.gameNotifyConsole == conNumber)
		gamenotify.setSelected(true);

	JButton button2 = new JButton("Cancel");
	JButton button3 = new JButton("Erase all");

	button2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event)
				{
					dispose();
				}});
button3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event)
				{
					resetChannels(conNumber);
					sharedVariables.comboNames[conNumber].clear();
					setConsoleTabTitles asetter = new setConsoleTabTitles();
					asetter.createConsoleTabTitle(sharedVariables, conNumber, consoleSubframes, myTabName);

					dispose();
				}});
	button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
				boolean mainAlso = false;
				if(mybox.isSelected() == true)
					mainAlso = true;

				if(deleteCheckBox.isSelected())
					deleteOn=false;

				if(gamenotify.isSelected() == true)
				 	sharedVariables.gameNotifyConsole=conNumber;
				 if(gamenotify.isSelected() == false && sharedVariables.gameNotifyConsole == conNumber)
				 	sharedVariables.gameNotifyConsole=0;

				if(shouts.isSelected() == true)
				 {

				// as we move shoutsinto a console we have to rename any console tabs that were using that  allready
				setConsoleTabTitles asetter = new setConsoleTabTitles();
				int oldshout=sharedVariables.shoutRouter.shoutsConsole;
				sharedVariables.shoutRouter.shoutsConsole=conNumber;
				if(oldshout> 0)
					asetter.createConsoleTabTitle(sharedVariables, oldshout, consoleSubframes, myTabName);

				if(shoutsAlso.isSelected() == true)
					sharedVariables.shoutsAlso=true;
				else
					sharedVariables.shoutsAlso=false;

				}

				if(shouts.isSelected() == false && sharedVariables.shoutRouter.shoutsConsole == conNumber)
				{
					sharedVariables.shoutRouter.shoutsConsole=0;
					sharedVariables.shoutsAlso=false;
				}
			if(names.isSelected() == true)
			{
			sharedVariables.comboNames[conNumber].clear();
			}
			if(sshouts.isSelected() == true)
			{

				setConsoleTabTitles asetter = new setConsoleTabTitles();
				int oldshout = sharedVariables.shoutRouter.sshoutsConsole;

				sharedVariables.shoutRouter.sshoutsConsole=conNumber;

				if(oldshout > 0)
				asetter.createConsoleTabTitle(sharedVariables, oldshout, consoleSubframes, myTabName);


			}
			if(sshouts.isSelected() == false && sharedVariables.shoutRouter.sshoutsConsole == conNumber)
				 	sharedVariables.shoutRouter.sshoutsConsole=0;
 				/*if(announcements.isSelected() == true)
				 	sharedVariables.shoutRouter.announcementsConsole=conNumber;
				 if(sshouts.isSelected() == false && sharedVariables.shoutRouter.announcementsConsole == conNumber)
				 	sharedVariables.shoutRouter.announcementsConsole=0;
				*/


























                                 myTabName = nameField.getText();
				 int foundChannel=0; // if we find one channel we zero out all channels in console first before adding channels
				 String mytext= field.getText();
				 mytext=mytext.trim();
				 int make=0;
				 try
			 	{
 				resetChannels(conNumber);
				 while(mytext.length() > 0)
				 {
				int i= mytext.indexOf(" ");
				if(i == -1)// no more spaces last channel
				{

// sharedVariables.console[aChannelNumber 1-500] will tell me what console a channel number is in, if not setto a sub console number it goes in main, i.e its 0
				flagClass args = new flagClass();
				mytext = args.parseFlags(mytext, 2);

				Integer num= new Integer(mytext);
				 int num1=num.intValue();
				 if(num1 >= 0 && num1 < 500)
				 {
					//int oldConNum = sharedVariables.console[num1];
					sharedVariables.console[conNumber][num1]= 1;

					// if mainAlso is true they selected in checkbox to route this tabs channels to main as well
					if(mainAlso == true || args.mainAlso == true)
						sharedVariables.mainAlso[num1] = true;
					else
						sharedVariables.mainAlso[num1] = false;

					if(deleteOn == true && args.deleteOn == true)
						removeFromOtherTabs(num1, conNumber);





			 	 }
				 make=1;
				 break;
				}
				else
				{
					String temp=mytext.substring(0, i);
				flagClass args = new flagClass();
				temp = args.parseFlags(temp, 2);

				 Integer num= new Integer(temp);
				 int num1=num.intValue();
				 if(num1 > 0 && num1 < 500)
				 {sharedVariables.console[conNumber][num1]=1  ;

					// if mainAlso is true they selected in checkbox to route this tabs channels to main as well
					if(mainAlso == true || args.mainAlso == true)
						sharedVariables.mainAlso[num1] = true;
					else
						sharedVariables.mainAlso[num1] = false;
					if(deleteOn == true && args.deleteOn == true)
						removeFromOtherTabs(num1, conNumber);

				  make=1;
				  }

				if(i+1 < mytext.length())
				mytext=mytext.substring(i+1, mytext.length());
				else
				{break;}

				}



			 	}// end else

					setConsoleTabTitles asetter = new setConsoleTabTitles();

					for(int z=1; z<sharedVariables.openConsoleCount; z++)
						if(z==conNumber || deleteOn== true)
						{
							String tempName=sharedVariables.consoleTabCustomTitles[z];
							if(z==conNumber)
								asetter.createConsoleTabTitle(sharedVariables, z, consoleSubframes, myTabName);
							else
								asetter.createConsoleTabTitle(sharedVariables, z, consoleSubframes, tempName);





						}

						if(sharedVariables.consoleLayout == 3)
						for(int z=0; z<sharedVariables.openConsoleCount; z++)
							consoleSubframes[z].updateTabChooserCombo();
				}// end try
			catch(Exception e)
			{
					String swarning = "Could not read your list of channels to customize console. If channels are valid they will be put in this console. Otherwise please try again, or select customize console again to see what channels you got and hit cancel. It needs to be a space seperated list with valid channel numbers. i.e. 2 3 100";
					setConsoleTabTitles asetter = new setConsoleTabTitles();



					asetter.createConsoleTabTitle(sharedVariables, conNumber, consoleSubframes, myTabName);
					Popup pop=new Popup(frame2, true, swarning);
					pop.setVisible(true);
			}
			finally{



					dispose();

			}
				}
});


mybox = new JCheckBox();
mybox.setSelected(false);
JLabel myboxLabel = new JLabel("Show these channels on main tab as well?");

shouts = new JCheckBox();
shouts.setSelected(false);
if(sharedVariables.shoutRouter.shoutsConsole == conNumber)
shouts.setSelected(true);

shoutsAlso=new JCheckBox();
shoutsAlso.setSelected(false);
if(sharedVariables.shoutsAlso == true)
shoutsAlso.setSelected(true);
JLabel shoutsAlsoLabel = new JLabel("Any shouts moved go to main also.");

JLabel myshoutLabel = new JLabel("Direct Shouts to this tab?");
sshouts = new JCheckBox();
if(sharedVariables.shoutRouter.sshoutsConsole == conNumber)
sshouts.setSelected(true);

JLabel mynamesLabel = new JLabel("Remove any tell names?");
names = new JCheckBox();


sshouts.setSelected(false);
if(sharedVariables.shoutRouter.sshoutsConsole == conNumber)
sshouts.setSelected(true);

JLabel mysshoutLabel = new JLabel("Direct S-Shouts to this tab?");
deleteLabel = new JLabel("Don't delete these channels from other tabs?");
deleteCheckBox = new JCheckBox();

	String channelList = getChannels(conNumber);
	field.setText(channelList);

JPanel checkboxpanel = new JPanel();
checkboxpanel.setLayout(new GridLayout(9,2));
checkboxpanel.add(button3);
checkboxpanel.add(button2);

checkboxpanel.add(myshoutLabel);
checkboxpanel.add(shouts);
checkboxpanel.add(shoutsAlsoLabel);
checkboxpanel.add(shoutsAlso);
checkboxpanel.add(mysshoutLabel);
checkboxpanel.add(sshouts);

checkboxpanel.add(myboxLabel);
checkboxpanel.add(mybox);
checkboxpanel.add(deleteLabel);
checkboxpanel.add(deleteCheckBox);

checkboxpanel.add(mynamesLabel);
checkboxpanel.add(names);
checkboxpanel.add(gamenotifyLabel);

checkboxpanel.add(gamenotify);

pane.add(button);
pane.add(checkboxpanel);

 JPanel naming = new JPanel();
 naming.add(nameLabel);
 naming.add(name);
 naming.add(nameField);
 pane.add(naming);
//pane.add(myboxLabel);
//pane.add(mybox);

setSize(650,430);
setVisible(true);

}// end constructor

void removeFromOtherTabs(int num, int myTab)
{
	for(int a=1; a<sharedVariables.maxConsoleTabs; a++)
		if(a!=myTab)
			sharedVariables.console[a][num]=0;

}

class flagClass
{

boolean mainAlso;
boolean deleteOn;

flagClass() {

	mainAlso = false;
	deleteOn = true;
}

String parseFlags(String text, int depth)
{

	if(text.length() < 2)
		return text;

	String newText = text.substring(0, text.length() - 1);
	String flag = text.substring(text.length() -1 , text.length());

	if(flag.equals("m") || flag.equals("M"))
	{
		mainAlso = true;
		if(depth > 1)
		{
			return parseFlags(newText, depth-1);

		}
		else
		return newText;
	}
	else 	if(flag.equals("a") || flag.equals("A"))
	{
		deleteOn = false;
		if(depth > 1)
		{
			return parseFlags(newText, depth-1);
		}
		else
		return newText;
	}

	return text;

}

}// end flag class



}// end class





