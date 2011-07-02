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
import javax.swing.GroupLayout.*;


class customizeExcludedPiecesDialog extends JDialog
{
JTextField field;
JButton okButton;
JButton cancelButton;
resourceClass dummyUse = new resourceClass();
JCheckBox [] mypieces;
JPanel checkPanel = new JPanel();
channels sharedVariables;
int notIndex;
customizeExcludedPiecesDialog(JFrame frame, boolean mybool, channels sharedVariables1)
{
super(frame, mybool);
sharedVariables=sharedVariables1;

String excluded=getExcluded();
if(excluded== null)
excluded = "";
setTitle(" Pieces Checked show in random pieces on observe.");
setSize(500,500);
setDefaultCloseOperation(DISPOSE_ON_CLOSE);

checkPanel.setLayout(new GridLayout(11, 2));
mypieces = new JCheckBox[dummyUse.maxPieces - 1];
for(int z=0; z< dummyUse.maxPieces - 1; z++)
{
  mypieces[z] = new JCheckBox(dummyUse.piecePaths[z]);
  if(sharedVariables.excludedPieces[z]==false)
  mypieces[z].setSelected(true);
  checkPanel.add(mypieces[z]);

}

okButton = new JButton("Ok");
cancelButton = new JButton("Cancel");
field= new JTextField(3);
JPanel panel = new JPanel();



okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
                                  //String mytext= field.getText();
				 try
			 	{
			         for(int z=0; z<dummyUse.maxPieces-1; z++)
			         if(mypieces[z].isSelected()==true)
			         sharedVariables.excludedPieces[z]=false;
			         else
			         sharedVariables.excludedPieces[z]=true;

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


JTextArea field2 = new JTextArea();
field2.setEditable(false);
Color backcol= new Color(0,0,0);
Color forcol = new Color(255,255,255);
field2.setBackground(backcol);
field2.setForeground(forcol);

field2.setLineWrap(true);
field2.setWrapStyleWord(true);
JScrollPane myscroller = new JScrollPane(field2);
field2.setText(getText());



 	  GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
         SequentialGroup h4 = layout.createSequentialGroup();



	hGroup.addComponent(myscroller);
        hGroup.addComponent(checkPanel);
	h4.addComponent(okButton);
	h4.addComponent(cancelButton);
        hGroup.addGroup(h4);


	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading
	//Create a sequential group v1

SequentialGroup v4 = layout.createSequentialGroup();

	//Add the group v2 tp the group v1

	v4.addComponent(checkPanel);
        	v4.addComponent(myscroller);

   ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

	v3.addComponent(okButton);
	v3.addComponent(cancelButton);



          v4.addGroup(v3);


	vGroup.addGroup(v4);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);





}// end constructor

String getExcluded()
{

String mess=" ";

for(int z=0; z < dummyUse.maxPieces - 1; z++)
if(sharedVariables.excludedPieces[z] == true)
{

mess+= " " + z;
}
return mess;
}

String getText()
{
String mess="";
mess = "Check the pieces you wish for random pieces on observe to choose from and uncheck those you dont. Hit ok to save your changes.";
 return mess;
}
}// end class
