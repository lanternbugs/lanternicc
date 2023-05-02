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


class customizeChannelColorDialog extends JDialog {
    //JTextField field;
    JSpinner field;
    channels sharedVariables;
    JTextPane[] consoles;
    JLabel boldLabel;
    JLabel italicLabel;
    JCheckBox boldCheckBox;
    JCheckBox italicCheckBox;


    customizeChannelColorDialog(JFrame frame, boolean mybool, channels sharedVariables1, JTextPane[] consoles1) {
        super(frame, mybool);
        setTitle("Colorize/Apply Style");
        sharedVariables = sharedVariables1;
        consoles = consoles1;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel pane = new JPanel();
        add(pane);

        //JLabel greeting = new JLabel("Enter channel # and colorize/apply style");

        //field = new JTextField(4);
        final SpinnerNumberModel num = new SpinnerNumberModel(0, 0, sharedVariables.maxChannels - 1, 1);
        field = new JSpinner(num);
//	pane.add(field);


        boldCheckBox = new JCheckBox();
        italicCheckBox = new JCheckBox();
        boldLabel = new JLabel("Bold");
        italicLabel = new JLabel("Italic");


        JButton button = new JButton("colorize/apply styles");


        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //String mytext= field.getText();
                try {
                    //Integer num= new Integer(mytext);
                    //int num1=num.intValue();
                    int num1 = num.getNumber().intValue();

                    JDialog frame = new JDialog();
                    Color defaultCol;
                    if (sharedVariables.channelOn[num1] == 1)
                        defaultCol = sharedVariables.channelColor[num1];
                    else
                        defaultCol = consoles[0].getForeground();

                    Color newColor = JColorChooser.showDialog(frame, "Choose Channel Color", defaultCol);
                    if (newColor != null) {
                        sharedVariables.channelOn[num1] = 1; // channelOn accepts numbers up to 500 and indicates it is colorized, not a default color
                        sharedVariables.channelColor[num1] = newColor; // channelColor accepts channel numbers up to 500 and gives their color. you wouldnt need to look at this unless channelOn for that channel number was set to 1

                    }// end if new color not null

                    if (italicCheckBox.isSelected() == true && boldCheckBox.isSelected())
                        sharedVariables.style[num1] = 3;
                    else if (italicCheckBox.isSelected() == true)
                        sharedVariables.style[num1] = 1;
                    else if (boldCheckBox.isSelected() == true)
                        sharedVariables.style[num1] = 2;
                    else
                        sharedVariables.style[num1] = 0;


                } catch (Exception e) {

                }
            }
        });
        /*
JButton done = new JButton("Done");
done.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
					dispose();
				}});
pane.add(button);

pane.setLayout(new GridLayout(5,1));
pane.add(greeting);
pane.add(field);
JPanel checkboxes = new JPanel();
*/
        pane.add(field);
        pane.add(italicLabel);
        pane.add(italicCheckBox);
        pane.add(boldLabel);
        pane.add(boldCheckBox);
        pane.add(button);
//pane.add(checkboxes);
//pane.add(button);
//pane.add(done);
        if (sharedVariables.operatingSystem.equals("mac")) {
            setSize(230, 100);
        } else {
            setSize(200, 100);
        }


    }// end constructor
}// end class
