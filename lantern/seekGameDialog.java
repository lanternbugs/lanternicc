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


class seekGameDialog extends JDialog
{
JTextField minSeekField;
JTextField maxSeekField;
JTextField timeField;
JTextField incField;
JLabel minSeekLabel;
JLabel maxSeekLabel;
JLabel timeLabel;
JLabel incLabel;
JLabel saveSettingsLabel;
JButton Okbutton;
JButton Cancelbutton;
JButton button5;
JButton button3;
JButton button1;
JButton button45;
JButton button960;
JButton button15;

JComboBox wildComboBox;
JLabel prompt;

JCheckBox ratedBox;
JCheckBox formulaBox;
JCheckBox manualBox;
JCheckBox saveSettingsCheckBox;

JLabel ratedLabel;
JLabel formulaLabel;
JLabel manualLabel;
channels sharedVariables;

int wildarray[]= {0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 16,17, 18, 19, 20, 21, 22, 23, 25, 26, 27, 28, 29};
String [] wildnames;
ConcurrentLinkedQueue<myoutput> queue;
seekGameDialog(final JFrame frame, boolean mybool, channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1)
{
super(frame, mybool);
queue=queue1;
sharedVariables=sharedVariables1;

setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	 minSeekField = new JTextField(4);
	 minSeekField.setText("" + sharedVariables.myseek.minseek);

        maxSeekField = new JTextField(4);
        maxSeekField.setText("" + sharedVariables.myseek.maxseek);

        timeField = new JTextField(3);
        timeField.setText("" + sharedVariables.myseek.time);

         incField = new JTextField(3);
         incField.setText("" + sharedVariables.myseek.inc);

         minSeekLabel = new JLabel("Min Rating");
         maxSeekLabel = new JLabel("Max Rating");
         saveSettingsLabel = new JLabel("Save Settings");
         saveSettingsCheckBox = new JCheckBox();
         if(sharedVariables.myseek.saveSettings == true)
                  saveSettingsCheckBox.setSelected(true);

         timeLabel = new JLabel("Time");
         incLabel = new JLabel("Increment");

// prompt text
prompt = new JLabel("Enter above, then ok to seek, or click below for a pure game");

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

ratedBox = new JCheckBox();
if(sharedVariables.myseek.rated == true)
ratedBox.setSelected(true);

manualBox = new JCheckBox();
if(sharedVariables.myseek.manual == true)
manualBox.setSelected(true);

formulaBox = new JCheckBox();
if(sharedVariables.myseek.formula == true)
formulaBox.setSelected(true);

ratedLabel = new JLabel("rated");
manualLabel = new JLabel("manual");
formulaLabel = new JLabel("formula");


	Okbutton = new JButton("Ok");
	Okbutton.addActionListener(new ActionListener() {
				 int BAD_VALUE = -10000;
                                 int minseek=0;
                                 int maxseek=0;
                                 int time=0;
                                 int inc=0;
                                 int wild=0;
                                 String rated = "r";
                                 String manual = "";
                                 String formula = "";


				public void actionPerformed(ActionEvent event)
				{
                                 String mytext= minSeekField.getText();
                                 minseek=processValue(mytext, "Min Rating", 0, 9999);
                                 if(minseek == BAD_VALUE)
                                            return;
                                 mytext= maxSeekField.getText();
                                 maxseek=processValue(mytext, "Max Rating", 0, 9999);
                                 if(maxseek == BAD_VALUE)
                                            return;

                                if(maxseek < minseek)
                                {
                                 showErrorMessage("Max Seek should not be less than Min Seek");
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

                            // now read check boxes.

                            if(ratedBox.isSelected() == true)
                            	rated="r";
                            else
                            	rated = "u";

                            if(formulaBox.isSelected() == true)
                            	formula="f";
                            else
                            	formula = "n";

                            if(manualBox.isSelected() == true)
                            	manual="m";
                            else
                            	manual = "a";



							String seekString = "seek " + time + " " + inc + " w" + wild + " " + rated + " " + minseek + "-" + maxseek + " " + formula + " " + manual;


                            // dialouge success
                                //showErrorMessage("success! " + seekString);
								sendToIcs("multi " + seekString + "\n");
                               // save values;
                               sharedVariables.myseek.minseek=minseek;
                               sharedVariables.myseek.maxseek=maxseek;
                               sharedVariables.myseek.time= time;
                               sharedVariables.myseek.inc=inc;
							   sharedVariables.myseek.wild=wild;
							   if(rated.equals("r"))
							   	sharedVariables.myseek.rated=true;
							   else
							    sharedVariables.myseek.rated=false;

							   if(manual.equals("m"))
							   	sharedVariables.myseek.manual=true;
							   else
							    sharedVariables.myseek.manual=false;

							   if(formula.equals("f"))
							   	sharedVariables.myseek.formula=true;
							   else
							    sharedVariables.myseek.formula=false;
                                 
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

	button1 = new JButton("1-minute");
	button1.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent event)
				{
					sendToIcs("multi 1-minute\n");
					dispose();
				}
			});

	button3 = new JButton("3-minute");
	button3.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent event)
				{
					sendToIcs("multi 3-minute\n");
					dispose();
				}
			});
	button5 = new JButton("5-minute");
	button5.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent event)
				{
					sendToIcs("multi 5-minute\n");
					dispose();
				}
			});
	button15 = new JButton("15-minute");
	button15.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent event)
				{
					sendToIcs("multi 15-minute\n");
					dispose();
				}
			});
	button45 = new JButton("45 45");
	button45.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent event)
				{
					sendToIcs("multi 45-minute\n");
					dispose();
				}
			});
	button960 = new JButton("Chess 960");
	button960.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent event)
				{
					sendToIcs("multi 960\n");
					dispose();
				}
			});


	overallPanel pane = new overallPanel();
	add(pane);

}// end constructor

void sendToIcs(String mess)
{
	myoutput data = new myoutput();
	data.data=mess;
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

row2.add(minSeekLabel);
row2.add(minSeekField);
row2.add(maxSeekLabel);
row2.add(maxSeekField);
row2.add(saveSettingsLabel);
row2.add(saveSettingsCheckBox);


row3.add(wildComboBox);
row3.add(ratedLabel);
row3.add(ratedBox);
row3.add(formulaLabel);
row3.add(formulaBox);
row3.add(manualLabel);
row3.add(manualBox);

row4.add(Okbutton);
row4.add(Cancelbutton);

row5.add(prompt);

row6.add(button1);
row6.add(button3);
row6.add(button5);

row7.add(button15);
row7.add(button45);
row7.add(button960);

setLayout(new GridLayout(7,1));
add(row1);
add(row2);
add(row3);
add(row4);
add(row5);
add(row6);
add(row7);
    }
}



void saveToICC()
{

                              	sendToIcs("multi set-quietly time " + sharedVariables.myseek.time + "\n");
                              	sendToIcs("multi set-quietly inc " + sharedVariables.myseek.inc + "\n");
                              	sendToIcs("multi set-quietly minseek " + sharedVariables.myseek.minseek + "\n");
                              	sendToIcs("multi set-quietly maxseek " + sharedVariables.myseek.maxseek + "\n");
                              	sendToIcs("multi set-quietly wild " + sharedVariables.myseek.wild + "\n");
                                if(sharedVariables.myseek.rated==true)
                             	  sendToIcs("multi set-quietly rated 1" +  "\n");
                                else
         	                  sendToIcs("multi set-quietly rated 0" +  "\n");

                                if(sharedVariables.myseek.manual==true)
                             	  sendToIcs("multi set-quietly manual 1" +  "\n");
                                else
         	                  sendToIcs("multi set-quietly manual 0" +  "\n");

                                if(sharedVariables.myseek.formula==true)
                             	  sendToIcs("multi set-quietly useformula 1" +  "\n");
                                else
         	                  sendToIcs("multi set-quietly useformula 0" +  "\n");

}
}// end class
