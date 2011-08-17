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

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import layout.TableLayout;

class autoExamDialog extends JDialog implements ChangeListener, ActionListener {

  channels sVars;
  SpinnerNumberModel model;
  JCheckBox showf;
  long aes;
  boolean aen;

  autoExamDialog(JFrame frame, boolean mybool, channels sVars) {
    super(frame, mybool);
    this.sVars = sVars;

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    aes = sVars.autoexamspeed;


    model = new SpinnerNumberModel(aes*.001, .25, 60, .25);
    JSpinner speed = new JSpinner(model);
    speed.addChangeListener(this);

    showf = new JCheckBox();
    aen = (sVars.autoexamnoshow==0);
    showf.setSelected(aen);
    showf.setActionCommand("showf");
    showf.addActionListener(this);

    JButton ok = new JButton("OK");
    ok.setActionCommand("OK");
    ok.addActionListener(this);

    JButton cancel = new JButton("Cancel");
    cancel.setActionCommand("Cancel");
    cancel.addActionListener(this);

    JPanel buttons = new JPanel();
    buttons.add(ok);
    buttons.add(cancel);
    
    int border = 10;

    int space = 5;

    int ht = 20;

    double[][] size = {{border, 20, 40, space, TableLayout.FILL, border},
                       {border, ht, ht, ht, TableLayout.FILL, border}};

    setLayout(new TableLayout(size));

    JLabel label = new JLabel("Select AutoExam speed in seconds");
    add(label, "1, 1, 4, 1");
    add(speed, "1, 2, 2, 2");
    add(new JLabel("seconds"), "4, 2");
    add(showf, "1, 3");
    add(new JLabel("Show 'forward 1' message."), "2, 3, 4, 3");
    add(buttons, "1, 4, 4, 4");
  }

  public void stateChanged(ChangeEvent e) {
    sVars.autoexamspeed = (long) (model.getNumber().doubleValue()*1000);
  }

  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    if (action.equals("OK")) dispose();
    if (action.equals("Cancel")) {
      sVars.autoexamspeed = aes;
      sVars.autoexamnoshow = (aen ? 0 : 1);
      dispose();
    }
    if (action.equals("showf"))
      sVars.autoexamnoshow = (showf.isSelected() ? 0 : 1);
  }
}

// class autoExamDialog extends JDialog {
  
//   JTextField field;
//   channels sharedVariables;
  
//   autoExamDialog(JFrame frame, boolean mybool, channels sharedVariables1) {
    
//     super(frame, mybool);
//     sharedVariables=sharedVariables1;

//     setDefaultCloseOperation(DISPOSE_ON_CLOSE);

//     JPanel pane = new JPanel();
//     add(pane);
//     field = new JTextField(20);
//     pane.add(field);

//     JButton button = new JButton("Set speed");

//     JLabel label = new JLabel("Enter AutoExam speed in seconds, now " +
//                               sharedVariables.autoexamspeed/1000 + "s");
//     JCheckBox abox = new JCheckBox();
//     if (sharedVariables.autoexamnoshow==1)
//       abox.setSelected(true);
//     else
//       abox.setSelected(false);
//     button.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent event) {
//           String mytext= field.getText();
//           mytext=mytext.trim();
//           try {

//             Integer num= new Integer(mytext);
//             int num1=num.intValue();
//             if (num1 > 0 && num1 < 500)
//               sharedVariables.autoexamspeed=num1 * 1000;
//           } catch(Exception e) {

//           } finally {
//             dispose();
//           }
//         }
//       });

//     abox.addActionListener(new ActionListener() {
//         public void actionPerformed(ActionEvent event) {
//           sharedVariables.autoexamnoshow=(sharedVariables.autoexamnoshow+1)%2;
//         }
//       });

//     JLabel label2 =
//       new JLabel("Check box to not show Forward 1 message when autoexamining.");

//     pane.add(button);
//     pane.add(label);
//     pane.add(abox);
//     pane.add(label2);
//   }// end constructor
// }// end class

