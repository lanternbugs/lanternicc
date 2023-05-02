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

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentLinkedQueue;

class AskAQuestionDialog extends JDialog {
    JTextArea field;
    String warning1;
    ConcurrentLinkedQueue<myoutput> queue;

    void setWarning(String s) {
        warning1 = s;
    }

    AskAQuestionDialog(JFrame frame, boolean mybool, ConcurrentLinkedQueue<myoutput> queue1) {
        super(frame, mybool);
        queue = queue1;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

//	JPanel pane = new JPanel();
//	add(pane, BorderLayout.NORTH);
//	JPanel pane2 = new JPanel();
//	add(pane2, BorderLayout.CENTER);
        field = new JTextArea();
        field.setColumns(50);
        field.setRows(8);
        field.setText("Type a question and hit send. Look in console for tab that Help(channel 1) is on for an answer or it can be answered in a tell\n");
        field.setEditable(false);
        Color backcol = new Color(0, 0, 0);
        Color forcol = new Color(255, 255, 255);
        field.setBackground(backcol);
        field.setForeground(forcol);

        field.setLineWrap(true);
        field.setWrapStyleWord(true);
        JScrollPane myscroller = new JScrollPane(field);
//pane2.add(field);
        final JTextField input = new JTextField();
        JButton button = new JButton("Send");


        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String actionmess = input.getText();
                actionmess = "`c0`" + "multi tell 1 " + actionmess + "\n";

                myoutput data = new myoutput();
                data.data = actionmess;
                queue.add(data);
                dispose();


            }
        });


//pane2.add(button);
//pack();


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup hGroup2 = layout.createSequentialGroup();


        hGroup.addComponent(myscroller, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE, Short.MAX_VALUE);
        hGroup2.addComponent(input, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE);
        hGroup2.addComponent(button, 60, 60, 60);
        hGroup.addGroup(hGroup2);
        //Create the horizontal group
        layout.setHorizontalGroup(hGroup);


        //Create a parallel group for the vertical axis
        ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading
        //Create a sequential group v1

        SequentialGroup v4 = layout.createSequentialGroup();
        ParallelGroup vGroup2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

        //Add the group v2 tp the group v1

        v4.addComponent(myscroller, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE);
        vGroup2.addComponent(button, GroupLayout.DEFAULT_SIZE, 30, 30);
        vGroup2.addComponent(input, GroupLayout.DEFAULT_SIZE, 30, 30);
        v4.addGroup(vGroup2);


        vGroup.addGroup(v4);
        //Create the vertical group
        layout.setVerticalGroup(vGroup);


        setSize(300, 260);// this may be set after the call to popup if they want a particular size


    }// end constructor
}// end class