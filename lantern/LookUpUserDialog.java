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


class LookupUserDialog extends JDialog
{
  JTextField field;

JButton okButton;
JButton cancelButton;
channels sharedVariables;
 ConcurrentLinkedQueue<myoutput> queue;
LookupUserDialog(JFrame frame, boolean mybool, ConcurrentLinkedQueue<myoutput> queue1)
{
super(frame, mybool);
queue = queue1;

JPanel panel = new JPanel();
panel.setLayout(new GridLayout(2,1));

JPanel buttonPanel = new JPanel();
buttonPanel.setLayout(new GridLayout(1,2));
setSize(200,100);
setTitle("Lookup User");
okButton = new JButton("Lookup");
cancelButton = new JButton("Cancel");
field=new JTextField(15);
setKeyInputListener();
buttonPanel.add(okButton);
buttonPanel.add(cancelButton);
panel.add(field);
panel.add(buttonPanel);
add(panel);
okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
                    lookupUser();                //String mytext= field.getText();
				

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

setVisible(true);
}// end method

    void setKeyInputListener()
    {
        field.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
              int a = e.getKeyCode();
              int gme = e.getModifiersEx();
            
              //if (a == 27) {
              if (a == KeyEvent.VK_ESCAPE) {
                field.setText("");
              }

              //if (a == 10) {
              if (a == KeyEvent.VK_ENTER) {
                lookupUser();
              }// end enter
                }// end key pressed

            public void keyTyped(KeyEvent e) {

            }

            /** Handle the key-released event from the text field. */
            public void keyReleased(KeyEvent e) {

            }
          });
    }
    
    void lookupUser()
    {
        try
        {
           if(field.getText().length()>0)
           {
            String mess = "`f1`Finger " + field.getText() + "\n";
            if(channels.fics) {
                mess = "Finger " + field.getText() + "\n";
            }
            myoutput data = new myoutput();
            data.data=mess;
            data.consoleNumber=0;
            queue.add(data);
           }
                               dispose();
       }
       catch(Exception dummy){}
    }

}// end class
