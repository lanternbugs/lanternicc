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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import layout.TableLayout;
/**/
class userButtonsDialog extends JDialog implements ActionListener {

  channels sVars;
  JTextField[] mypanes;

  userButtonsDialog(JFrame myframe, channels sVars) {
    super(myframe, "Customize User Buttons", false);
    this.sVars = sVars;

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    mypanes = new JTextField[10];

    JButton ok = new JButton("OK");
    ok.setActionCommand("Submit");
    ok.addActionListener(this);

    JButton cancel = new JButton("Cancel");
    cancel.setActionCommand("Cancel");
    cancel.addActionListener(this);

    JPanel buttons = new JPanel();
    buttons.add(ok);
    buttons.add(cancel);

    int ht = 20;
    int border = 10;
    int space = 5;

    double[][] size = {{border, 10, TableLayout.FILL, border},
                       {border, 30, space, ht, space, ht, space, ht, space,
                        ht, space, ht, space, ht, space, ht, space, ht, space,
                        ht, space, ht, TableLayout.FILL, border}};

    setLayout(new TableLayout(size));

    JLabel mytext =
      new JLabel("<html>Enter custom commands and use Ctrl-# to activate.</html>");
    add(mytext, "1, 1, 2, 1");

    for (int i=0; i<10; i++) {
      int j = 2*(i==0 ? 10 : i) + 1;
      mypanes[i] = new JTextField(20);
      mypanes[i].setText(sVars.userButtonCommands[i]);
      mypanes[i].setActionCommand("Submit");
      mypanes[i].addActionListener(this);
      add(new JLabel(""+i), "1, "+j);
      add(mypanes[i], "2, "+j);
    }

    add(buttons, "1, 22, 2, 22");
  }

  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    if (action.equals("Cancel")) dispose();
    if (action.equals("Submit")) {
      for (int i=0; i<10; i++) {
        String bcommand = mypanes[i].getText();
        sVars.userButtonCommands[i] = bcommand;
        //String buttontitle = "" + i;
        /*if (!bcommand.equals("") && sVars.showButtonTitle) {
          buttontitle += " - ";
          if (bcommand.length() > 11)
            buttontitle += bcommand.substring(0, 11);
          else
            buttontitle += bcommand;
        }*/
        //sVars.mybuttons[i].setText(buttontitle);
      }
      dispose();
    }
  }
}

/*/
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
/**/