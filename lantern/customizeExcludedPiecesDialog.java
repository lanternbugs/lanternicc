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
resourceClass graphics;
boolean [] excludedPieces;
JTextField field;
JImagePanel myImagePanel=new JImagePanel();
JButton okButton;
JButton cancelButton;
resourceClass dummyUse = new resourceClass();
JCheckBox [] mypieces;
int Pieces = 0;
JPanel checkPanel = new JPanel();
channels sharedVariables;
int notIndex;
boolean amWhite;
customizeExcludedPiecesDialog(JFrame frame, boolean mybool, channels sharedVariables1, 	resourceClass graphics1, boolean [] excludedPieces1, boolean amWhite1)
{
super(frame, mybool);
sharedVariables=sharedVariables1;
graphics=graphics1;
excludedPieces =excludedPieces1;
amWhite=amWhite1;
String excluded=getExcluded();
if(excluded== null)
excluded = "";
setTitle(" Pieces Checked show in random pieces on observe.");
setSize(500,500);
setDefaultCloseOperation(DISPOSE_ON_CLOSE);

checkPanel.setLayout(new GridLayout(0, 3));
mypieces = new JCheckBox[dummyUse.maxPieces - 1];



for(int z=0; z< dummyUse.maxPieces - 1; z++)
{
  mypieces[z] = new JCheckBox(dummyUse.piecePaths[z]);
  if(excludedPieces[z]==false)
  mypieces[z].setSelected(true);
  checkPanel.add(mypieces[z]);
  final int num=z;
  mypieces[z].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
                                  //String mytext= field.getText();
				 try
			 	{
                                         Pieces=num;
                                         myImagePanel.repaint();
                                  }
                                  catch(Exception dummy){}

    }// end method
    }// end class
    );

} // end for

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
			         excludedPieces[z]=false;
			         else
			         excludedPieces[z]=true;

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
field2.setFont(sharedVariables.myFont);
field2.setText(getText());



 	  GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
         SequentialGroup h4 = layout.createSequentialGroup();



	hGroup.addComponent(myImagePanel);
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


        v4.addComponent(myImagePanel, 50, 50, 50);
          v4.addGroup(v3);


	vGroup.addGroup(v4);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);





}// end constructor

String getExcluded()
{

String mess=" ";

for(int z=0; z < dummyUse.maxPieces - 1; z++)
if(excludedPieces[z] == true)
{

mess+= " " + z;
}
return mess;
}

String getText()
{
String mess="";
if(amWhite == true)
mess = "Check the pieces you wish for random pieces on observe to choose from for white and uncheck those you dont. Hit ok to save your changes.";
else
mess = "Check the pieces you wish for random pieces on observe to choose from for black and uncheck those you dont. Hit ok to save your changes.";


 return mess;
}

class JImagePanel extends JPanel
{
	public void paintComponent(Graphics g)
		{

		try {

		super.paintComponent(g);

		setBackground(sharedVariables.boardBackgroundColor);

		Graphics2D g2 = (Graphics2D) g;
		int Plus =0;
		if(amWhite == false)
		Plus=6;

                int boardx=5;
		int boardy=5; // upper left
		int square=50;
		int dif=2;
                for(int a=0; a<6; a++)
                g.drawImage(graphics.pieces[Pieces][a+Plus], boardx +  a * square + dif , boardy + dif, square - dif, square-4, this);

  }
  catch(Exception dui){}

}
}// end image class
}// end class
