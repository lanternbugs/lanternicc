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
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Queue;


class passTest extends JDialog
{
JTextField input1;
JTextField input2;
JButton button1;
JButton button2;
JButton button3;
JLabel label1;
JLabel label2;
credentials util;
passTest()
{
input1 = new JTextField(20);
input2 = new JTextField(20);
button1 = new JButton("ok");
button2= new JButton("get");
button3= new JButton("reset");

label1 = new JLabel("name");
label2 = new JLabel("pass");
util = new credentials();

button1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event)
				{          String name = input1.getText();
                                           String pass = input2.getText();
					util.saveNamePass(name, pass);
				}});

button2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event)
				{
                                             String name = util.getName();
                                             String pass = util.getPass();
                                             label1.setText(name);
                                             label2.setText(pass);
				}});

button3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event)
				{
					util.resetNamePass();
				}});


JPanel panel = new JPanel();
panel.add(input1);
panel.add(input2);
panel.add(button1);
panel.add(button2);
panel.add(button3);
panel.add(label1);
panel.add(label2);
add(panel);
setSize(500,500);
setVisible(true);

}// end contstrutor

} // end class