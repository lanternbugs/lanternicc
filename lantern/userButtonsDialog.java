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
/*
class userButtonsDialog extends JDialog implements ActionListener {

}
*/
class userButtonsDialog extends JDialog {
  JTextField [] mypanes;
  JLabel [] mylabels;
  JPanel [] mypanels;
  JButton okbutton;
  JButton cancelbutton;

  channels sharedVariables;
  userButtonsDialog(JFrame myframe, channels sharedVariables1) {
    super(myframe, false);
    sharedVariables = sharedVariables1;
    mypanes  = new JTextField[10];
    mylabels = new JLabel[10];
    mypanels = new JPanel[12];
    JPanel overall = new JPanel();
    add(overall);
    overall.setLayout(new GridLayout(12,1));

    JLabel mytext = new JLabel("<html>Enter custom commands and use control #<br> to activate in chat console.</html>");

    mypanels[10]= new JPanel();
    overall.add(mytext);
    for(int a = 0; a<10; a++) {
      mypanes[a] = new JTextField(20);
      mypanes[a].setText(sharedVariables.userButtonCommands[a]);
      mylabels[a] = new JLabel("user button " + a);
      mypanels[a]= new JPanel();
      mypanels[a].add(mylabels[a]);
      mypanels[a].add(mypanes[a]);
      overall.add(mypanels[a]);
    }// end for

    okbutton = new JButton("Ok");
    cancelbutton = new JButton("Cancel");
    mypanels[10].add(okbutton);
    mypanels[10].add(cancelbutton);
    overall.add(mypanels[10]);
    okbutton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          for(int a=0; a<10; a++) {
            sharedVariables.userButtonCommands[a] = mypanes[a].getText();
            String buttonTitle="" + a;
            if(!sharedVariables.userButtonCommands[a].equals("") &&
               sharedVariables.showButtonTitle==true) {
              buttonTitle="" + a + " - ";
              if(sharedVariables.userButtonCommands[a].length() > 11)
                buttonTitle+=sharedVariables.userButtonCommands[a].substring(0,11);
              else
                buttonTitle+=sharedVariables.userButtonCommands[a];
            }
            sharedVariables.mybuttons[a].setText(buttonTitle);
          }
          dispose();
        }
      }
      );
    cancelbutton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          dispose();
        }
      }
      );
  }// end constructor
}