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


class customizeChannelNotifyDialog extends JDialog
{
  JTextField field;

JLabel messageLabel;
JLabel addLabel;
JLabel removeLabel;
JButton okButton;
JButton cancelButton;
JButton globalButton;
channels sharedVariables;
int notIndex;
customizeChannelNotifyDialog(JFrame frame, boolean mybool, channels sharedVariables1, final String name)
{
super(frame, mybool);
sharedVariables=sharedVariables1;

String hisChannels=getChannels(name);
if(hisChannels == null)
hisChannels = " null";
setTitle("Set Channel Notify for " + name + hisChannels );
setSize(500,300);
setDefaultCloseOperation(DISPOSE_ON_CLOSE);


okButton = new JButton("Ok");
cancelButton = new JButton("Cancel");
addLabel = new JLabel("Add channel to channel notify " + name + " or remove " + name + " from notify");
field= new JTextField(3);
JPanel panel = new JPanel();
notIndex=isOnGlobalNotify(name);
String text;
if(notIndex==-1)
text = "Connect Notify " + name;
else
text= "Un-connect Notify " + name;
globalButton = new JButton(text);

panel.add(field, BorderLayout.SOUTH);
panel.add(addLabel, BorderLayout.NORTH);
panel.add(okButton, BorderLayout.CENTER);
panel.add(cancelButton, BorderLayout.CENTER);
panel.add(globalButton,  BorderLayout.CENTER);
add(panel);


globalButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
                                  //String mytext= field.getText();
				 try
			 	{
                                        if(notIndex==-1)
                                        {
                                         lanternNotifyClass ln = new lanternNotifyClass();
                                         ln.name=name;
                                          sharedVariables.lanternNotifyList.add(ln);
                                        }
                                        else
                                         sharedVariables.lanternNotifyList.remove(notIndex);
                                         write2();
                                         dispose();
                                  }
                                  catch(Exception dui){dispose();}

                                  }// end action performed
});
okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
                                  //String mytext= field.getText();
				 try
			 	{
				String text = field.getText().trim();
				int number = Integer.parseInt(text);

				if(number <400 && number >=0)
				{



					boolean haveChannel=false;
						for(int z=0; z<sharedVariables.channelNotifyList.size(); z++)
						if(sharedVariables.channelNotifyList.get(z).channel.equals(text))
						{
							boolean found=false;
							for(int b=0; b<sharedVariables.channelNotifyList.get(z).nameList.size(); b++)
							if(sharedVariables.channelNotifyList.get(z).nameList.get(b).toLowerCase().equals(name.toLowerCase()))
							{
								sharedVariables.channelNotifyList.get(z).nameList.remove(b);
								found=true;
							}

							if(found==false)// we have channel but he is not on list so we add him
							{
								sharedVariables.channelNotifyList.get(z).nameList.add(name);
							}

							haveChannel=true;

						}

						if(haveChannel==false)
						{
							channelNotifyClass temp = new channelNotifyClass();
							temp.channel=text;
							temp.nameList.add(name);
							sharedVariables.channelNotifyList.add(temp);
						}
						write();



			}// if valid number
				dispose();
				}
				catch(Exception dummy){dispose();}

			}// end event

		});

cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
                                  //String mytext= field.getText();
				 try
			 	{
					dispose();
				}
				catch(Exception dummy){}

			}// end event

		});



}// end constructor

String getChannels(String name)
{
  
String mess=" ";
String channel="";
for(int z=0; z<sharedVariables.channelNotifyList.size(); z++)
if(sharedVariables.channelNotifyList.get(z).nameList.size()>0)
{
channel=sharedVariables.channelNotifyList.get(z).channel ;
for(int x=0; x < sharedVariables.channelNotifyList.get(z).nameList.size(); x++)
 if(sharedVariables.channelNotifyList.get(z).nameList.get(x).toLowerCase().equals(name.toLowerCase()))
mess+= " " + channel;
}
return mess;
}


void write()  // channel notify
{

FileWrite writer = new FileWrite();
String mess="\r\n";
for(int z=0; z<sharedVariables.channelNotifyList.size(); z++)
if(sharedVariables.channelNotifyList.get(z).nameList.size()>0)
{
mess+="#" + sharedVariables.channelNotifyList.get(z).channel + "\r\n";
for(int x=0; x < sharedVariables.channelNotifyList.get(z).nameList.size(); x++)
mess+=sharedVariables.channelNotifyList.get(z).nameList.get(x) + "\r\n";
}

writer.write(mess, "lantern_channel_notify.txt");
}

void write2()  // global notify
{

FileWrite writer = new FileWrite();
String mess="\r\n";
for(int z=0; z<sharedVariables.lanternNotifyList.size(); z++)
{ String name=sharedVariables.lanternNotifyList.get(z).name;
if(sharedVariables.lanternNotifyList.get(z).sound == true)
name=name + " 1\r\n";
else
name=name+ " 0\r\n";
mess+=name;
}// end for


writer.write(mess, "lantern_global_notify.txt");
}

int isOnGlobalNotify(String name)
{

for(int z=0; z<sharedVariables.lanternNotifyList.size(); z++)
if(sharedVariables.lanternNotifyList.get(z).name.toLowerCase().equals(name.toLowerCase()))
return z;

return -1;
}

}// end class