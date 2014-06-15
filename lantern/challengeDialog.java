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
import java.util.concurrent.ConcurrentLinkedQueue;


class challengeDialog extends JDialog
{
JTextField timeField;
JTextField incField;
JLabel minSeekLabel;
JLabel maxSeekLabel;
JLabel timeLabel;
JLabel incLabel;
JLabel saveSettingsLabel;
JButton Okbutton;
JButton Cancelbutton;

JComboBox wildComboBox;
JComboBox colorComboBox;
JLabel colorLabel;

JLabel prompt;

JCheckBox ratedBox;
JCheckBox saveSettingsCheckBox;

JLabel ratedLabel;
channels sharedVariables;

int wildarray[]= {0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
String [] wildnames;
String colorarray[]={"auto", "white", "black"};
JLabel opponentLabel;
JTextField opponentfield;
ConcurrentLinkedQueue<myoutput> queue;
challengeDialog(final JFrame frame, boolean mybool, channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1, String opponent)
{
super(frame, mybool);
queue=queue1;
sharedVariables=sharedVariables1;

setDefaultCloseOperation(DISPOSE_ON_CLOSE);

         opponentLabel = new JLabel("Name");
         opponentfield  = new JTextField(15);
         opponentfield.setText(opponent);

        timeField = new JTextField(3);
        timeField.setText("" + sharedVariables.myseek.time);

         incField = new JTextField(3);
         incField.setText("" + sharedVariables.myseek.inc);

          saveSettingsLabel = new JLabel("Save Settings");
         saveSettingsCheckBox = new JCheckBox();
         if(sharedVariables.myseek.saveSettings == true)
                  saveSettingsCheckBox.setSelected(true);

         timeLabel = new JLabel("Time");
         incLabel = new JLabel("Increment");

// prompt text
prompt = new JLabel("");

// populate wilds in combo box
wildnames = new String[wildarray.length];
wildTypes mywilds = new wildTypes();
int wildIndex=0;
for(int a=0; a< wildarray.length; a++)
{
	if(a == 0)
         wildnames[a]=mywilds.getWildName(wildarray[a]);
        else
        wildnames[a]=mywilds.getWildName(wildarray[a]).substring(1, mywilds.getWildName(wildarray[a]).length());
	if(wildarray[a] == sharedVariables.myseek.wild)
		wildIndex=a;
}


wildComboBox = new JComboBox(wildnames);
wildComboBox.setSelectedIndex(wildIndex);
// end wilds and combo box

int colorIndex=0;
for(int a=0; a< 3; a++)
{

	if(colorarray[a].equals(sharedVariables.myseek.color))
		colorIndex=a;
}
colorComboBox = new JComboBox(colorarray);
colorComboBox.setSelectedIndex(colorIndex);
colorLabel = new JLabel("Color");
// end color and combo box

ratedBox = new JCheckBox();
if(sharedVariables.myseek.rated == true)
ratedBox.setSelected(true);


ratedLabel = new JLabel("rated");


	Okbutton = new JButton("Ok");
	Okbutton.addActionListener(new ActionListener() {
				 int BAD_VALUE = -10000;
                                  int time=0;
                                 int inc=0;
                                 int wild=0;
                                 String rated = "r";
                                 String opponent;
                                 String mytext;
                                 String color;
				public void actionPerformed(ActionEvent event)
				{
                                 opponent = opponentfield.getText();
                                 if(opponent.equals(""))
                                 {
                                   showErrorMessage("Error, need an opponent.");
				   return;
                                 }
                                 mytext= timeField.getText();
                                 time=processValue(mytext, "Time", 0, 600);
                                 if(time == BAD_VALUE)
                                            return;
                                 mytext= incField.getText();
                                 inc=processValue(mytext, "Increment", 0, 300);
                                 if(inc == BAD_VALUE)
                                            return;

                                if(time == 0 && inc == 0)
                                {
                                 showErrorMessage("time and inc cannot both be 0");
                                 return;
                                }

                                try {
									wild = wildarray[wildComboBox.getSelectedIndex()];
							}
							catch(Exception d)
							{
							showErrorMessage("Error parsing wild");
							return;
							}
                                try {
									int choice =colorComboBox.getSelectedIndex();
                                                                        color = colorarray[choice];
                                                                        if(color.equals("auto"))
                                                                        color = "";
                                                	}
							catch(Exception d)
							{
							showErrorMessage("Error parsing color");
							return;
							}

                            // now read check boxes.

                            if(ratedBox.isSelected() == true)
                            	rated="r";
                            else
                            	rated = "u";




							String matchString = "match " + opponent + " " + time + " " + inc + " w" + wild + " " + rated + " " + color;


                            // dialouge success
                                //showErrorMessage("success! " + seekString);
								sendToIcs("multi " + matchString + "\n");
                               // save values;
                               sharedVariables.myseek.time= time;
                               sharedVariables.myseek.inc=inc;
							   sharedVariables.myseek.wild=wild;
							   if(rated.equals("r"))
							   	sharedVariables.myseek.rated=true;
							   else
							    sharedVariables.myseek.rated=false;


                                  if(saveSettingsCheckBox.isSelected() == true)
                                    sharedVariables.myseek.saveSettings=true;
                                  else
                            	    sharedVariables.myseek.saveSettings = false;
                                   if(sharedVariables.myseek.saveSettings == true)
                                   saveToICC();


                               dispose();


                                } // end action performed

				int processValue(String mytext,  String question, int min, int max)
                                {
                                int newNumber=0;
                                 try
			 	{

                                 Integer num= new Integer(mytext);
			         newNumber=num.intValue();
                                 if(newNumber < 0 || newNumber > 9999)
                                 {
                                 showErrorMessage(question + " must be between " + min + " and " + max + ".");
                                 return BAD_VALUE;

                                 }//end if bad minseek
                                 } // end minseek try
			         catch(Exception e)
			         {
                                 showErrorMessage(question + " must be a number.");
                                 return BAD_VALUE;
			         }

			         return newNumber;
			         }// end process value method

			         void showErrorMessage(String mess)
			         {
			          Popup mywarning = new Popup(frame, true, mess);
			          mywarning.setVisible(true);
			         }

});

	Cancelbutton = new JButton("Cancel");
	Cancelbutton.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent event)
				{
					dispose();
				}
			});


	overallPanel pane = new overallPanel();
	add(pane);

}// end constructor

void sendToIcs(String mess)
{
	myoutput data = new myoutput();
	data.data= "`c0`" + mess;
	queue.add(data);

}
class overallPanel extends JPanel {
    overallPanel()
    {
     // layout here
JPanel row1 = new JPanel();
JPanel row2 = new JPanel();
JPanel row3 = new JPanel();
JPanel row4 = new JPanel();
JPanel row5 = new JPanel();
JPanel row6 = new JPanel();
JPanel row7 = new JPanel();

row1.add(timeLabel);
row1.add(timeField);
row1.add(incLabel);
row1.add(incField);

row2.add(opponentLabel);
row2.add(opponentfield);
row2.add(saveSettingsLabel);
row2.add(saveSettingsCheckBox);


row3.add(wildComboBox);
row3.add(colorLabel);
row3.add(colorComboBox);
row3.add(ratedLabel);
row3.add(ratedBox);

row4.add(Okbutton);
row4.add(Cancelbutton);



setLayout(new GridLayout(4,1));
add(row1);
add(row2);
add(row3);
add(row4);

    }
}



void saveToICC()
{

                              	sendToIcs("multi set-quietly time " + sharedVariables.myseek.time + "\n");
                              	sendToIcs("multi set-quietly inc " + sharedVariables.myseek.inc + "\n");
                              	sendToIcs("multi set-quietly wild " + sharedVariables.myseek.wild + "\n");
                                sendToIcs("multi set-quietly color " + sharedVariables.myseek.color + "\n");
                                if(sharedVariables.myseek.rated==true)
                             	  sendToIcs("multi set-quietly rated 1" +  "\n");
                                else
         	                  sendToIcs("multi set-quietly rated 0" +  "\n");


}
}// end class
