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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ConcurrentLinkedQueue;


class addFriendDialog extends JDialog {
    JTextField field;

    JButton okButton;
    JButton cancelButton;
    channels sharedVariables;
    ConcurrentLinkedQueue<myoutput> queue;

    addFriendDialog(JFrame frame, boolean mybool, channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1) {
        super(frame, mybool);
        sharedVariables = sharedVariables1;
        queue = queue1;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        setSize(200, 100);
        setTitle("Add Friend");
        okButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        field = new JTextField(15);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel.add(field);
        panel.add(buttonPanel);
        add(panel);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //String mytext= field.getText();
                addFriendAction();

            }// end event

        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //String mytext= field.getText();
                try {
                    dispose();
                } catch (Exception dummy) {
                }

            }// end event

        });
        setKeyListener();
        setVisible(true);
    }// end method


    void setKeyListener() {
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
                    addFriendAction();
                }// end enter
            }// end key pressed

            public void keyTyped(KeyEvent e) {

            }

            /** Handle the key-released event from the text field. */
            public void keyReleased(KeyEvent e) {

            }
        });
    }


    void addFriendAction() {
        try {
            if (field.getText().length() > 0) {
                String mess = "+Notify " + field.getText() + "\n";
                myoutput data = new myoutput();
                data.data = mess;
                data.consoleNumber = 0;
                queue.add(data);
            }
            dispose();
        } catch (Exception dummy) {
        }
    }

}// end class
