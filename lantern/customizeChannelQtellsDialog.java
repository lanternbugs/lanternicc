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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


class customizeChannelQtellsDialog extends JDialog
{
  //JTextField field;
  JSpinner field;
channels sharedVariables;


JRadioButton allCheckBox;
JRadioButton qtellCheckBox;
JRadioButton normalCheckBox;

JLabel update;
customizeChannelQtellsDialog(JFrame frame, boolean mybool, channels sharedVariables1, final int tabNum)
{
super(frame, mybool);
setTitle("Customize Channel Qtells on Tab");
sharedVariables=sharedVariables1;


setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	JPanel pane = new JPanel();
	add(pane);
        pane.setLayout(new GridLayout(6, 1));
	//JLabel greeting = new JLabel("Enter channel # and colorize/apply style");

	//field = new JTextField(4);

         final SpinnerNumberModel num = new SpinnerNumberModel(0,0,399,1);
        field = new JSpinner(num);
        JPanel fieldPanel = new JPanel();
        fieldPanel.add(field);
        field.addChangeListener( new ChangeListener() {
          public void stateChanged(ChangeEvent e) {

            try {
            int num1 =num.getNumber().intValue();
            num1=sharedVariables.qtellController[tabNum][num1];
            if(num1 == 0)
            allCheckBox.setSelected(true);
            else
            allCheckBox.setSelected(false);


            if(num1 == 1)
            normalCheckBox.setSelected(true);
            else
            normalCheckBox.setSelected(false);

            if(num1 == 2)
            qtellCheckBox.setSelected(true);
            else
            qtellCheckBox.setSelected(false);

            }
            catch(Exception dui){}
        }// end method
        }// end class
        );


	JButton button = new JButton("Apply Qtell Choice To Channel");
        allCheckBox = new JRadioButton("Show qtells and channel text on this tab for this channel");
        normalCheckBox = new JRadioButton("Show channel text from this channel not qtells on this tab");
        qtellCheckBox = new JRadioButton("Show qtells from this channel on this tab");
        ButtonGroup group = new ButtonGroup();
        group.add(allCheckBox);
        group.add(normalCheckBox);
        group.add(qtellCheckBox);

        update = new JLabel("No setting Changed Yet");
        pane.add(fieldPanel);
        pane.add(allCheckBox);
        pane.add(normalCheckBox);
        pane.add(qtellCheckBox);
        pane.add(update);
        pane.add(button);




	button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
                                  //String mytext= field.getText();
				 try
			 	{
                                  //Integer num= new Integer(mytext);
                                  //int num1=num.intValue();
                                  int num1=num.getNumber().intValue();

				if(allCheckBox.isSelected() == true)
					sharedVariables.qtellController[tabNum][num1]=0;
				if(normalCheckBox.isSelected() == true)
					sharedVariables.qtellController[tabNum][num1]=1;
 				if(qtellCheckBox.isSelected() == true)
					sharedVariables.qtellController[tabNum][num1]=2;
                                 update.setText("channel " + num1 + " adjusted");

			}
			catch(Exception e)
			{

			}
				}
});


setSize(400,400);

}// end constructor
}// end class
