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


class customizeTabConsolelColorsDialog extends JDialog {
    JPaintedLabel shoutLabel;
    JPaintedLabel tellLabel;
    JPaintedLabel qtellLabel;
    JPaintedLabel backgroundLabel;
    JPaintedLabel defaultChannelLabel;
    JPaintedLabel responseTextLabel;
    JPaintedLabel nonResponseTextLabel;
    JPaintedLabel kibLabel;
    JPaintedLabel timestampLabel;
    JPaintedLabel typedLabel;
    JPaintedLabel resetLabel;


    JButton shoutButton;
    JButton tellButton;
    JButton qtellButton;
    JButton backgroundButton;
    JButton defaultChannelButton;
    JButton responseTextButton;
    JButton nonResponseTextButton;
    JButton okButton;
    JButton cancelButton;
    JButton kibButton;
    JButton timestampButton;
    JButton typedButton;
    JButton resetButton;

    Color tellcolor;
    Color BackColor;
    Color qtellcolor;
    Color responseColor;
    Color ForColor;
    Color timestampColor;
    Color typedColor;
    subframe me;

    private JTextPane[] consoles;

    channels sharedVariables;

    customizeTabConsolelColorsDialog(JFrame frame, boolean mybool, channels sharedVariables1, JTextPane[] consoles1, final int consoleNumber, subframe me1) {
        super(frame, mybool);
        sharedVariables = sharedVariables1;
        consoles = consoles1;
        me = me1;

        if (sharedVariables.tabStuff[consoleNumber].tellcolor == null)
            tellcolor = sharedVariables.tellcolor;
        else
            tellcolor = sharedVariables.tabStuff[consoleNumber].tellcolor;


        if (sharedVariables.tabStuff[consoleNumber].timestampColor == null)
            timestampColor = sharedVariables.chatTimestampColor;
        else
            timestampColor = sharedVariables.tabStuff[consoleNumber].timestampColor;

        if (sharedVariables.tabStuff[consoleNumber].typedColor == null)
            typedColor = sharedVariables.typedColor;
        else
            typedColor = sharedVariables.tabStuff[consoleNumber].typedColor;


        if (sharedVariables.tabStuff[consoleNumber].BackColor == null)
            BackColor = sharedVariables.BackColor;
        else
            BackColor = sharedVariables.tabStuff[consoleNumber].BackColor;

        if (sharedVariables.tabStuff[consoleNumber].qtellcolor == null)
            qtellcolor = sharedVariables.qtellcolor;
        else
            qtellcolor = sharedVariables.tabStuff[consoleNumber].qtellcolor;

        if (sharedVariables.tabStuff[consoleNumber].responseColor == null)
            responseColor = sharedVariables.responseColor;
        else
            responseColor = sharedVariables.tabStuff[consoleNumber].responseColor;

        if (sharedVariables.tabStuff[consoleNumber].ForColor == null)
            ForColor = sharedVariables.ForColor;
        else
            ForColor = sharedVariables.tabStuff[consoleNumber].ForColor;


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel pane = new JPanel();
        add(pane);


// tell colors
        tellLabel = new JPaintedLabel("Tell Color", sharedVariables);
        tellLabel.setForeground(tellcolor);
        tellLabel.fontType = 0;
        tellLabel.setOpaque(true);
        tellLabel.setBackground(BackColor);

        tellButton = new JButton("Colorize");
        tellButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    JDialog frame = new JDialog();
                    Color defaultCol = tellcolor;
                    Color newColor = JColorChooser.showDialog(frame, "Choose Tell Color", defaultCol);
                    if (newColor != null) {
                        sharedVariables.tabStuff[consoleNumber].tellcolor = newColor;
                        tellLabel.setForeground(sharedVariables.tabStuff[consoleNumber].tellcolor);
                    }
                }// end try
                catch (Exception e) {
                }
            }
        });


// qtell colors
        qtellLabel = new JPaintedLabel("Qtell Color", sharedVariables);
        qtellLabel.setForeground(qtellcolor);
        qtellLabel.fontType = 0;
        qtellLabel.setOpaque(true);
        qtellLabel.setBackground(BackColor);
        qtellButton = new JButton("Colorize");
        qtellButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    JDialog frame = new JDialog();
                    Color defaultCol = qtellcolor;
                    Color newColor = JColorChooser.showDialog(frame, "Choose Qtell Color", defaultCol);
                    if (newColor != null) {
                        sharedVariables.tabStuff[consoleNumber].qtellcolor = newColor;
                        qtellLabel.setForeground(sharedVariables.tabStuff[consoleNumber].qtellcolor);
                    }
                }// end try
                catch (Exception e) {
                }
            }
        });


//  response Text color
        responseTextLabel = new JPaintedLabel("Response Text Color", sharedVariables);
        responseTextLabel.setForeground(responseColor);
        responseTextLabel.fontType = 0;
        responseTextLabel.setOpaque(true);
        responseTextLabel.setBackground(BackColor);
        responseTextButton = new JButton("Colorize");
        responseTextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    JDialog frame = new JDialog();
                    Color defaultCol = responseColor;
                    Color newColor = JColorChooser.showDialog(frame, "Choose Response Text Color", defaultCol);
                    if (newColor != null) {
                        sharedVariables.tabStuff[consoleNumber].responseColor = newColor;
                        responseTextLabel.setForeground(sharedVariables.tabStuff[consoleNumber].responseColor);
                    }
                }// end try
                catch (Exception e) {
                }
            }
        });

//  non response Text color
        String responseColorType = !channels.fics ? "Non Response Text Color" : "Server Text Color";
        nonResponseTextLabel = new JPaintedLabel(responseColorType, sharedVariables);
        nonResponseTextLabel.setForeground(ForColor);
        nonResponseTextLabel.fontType = 0;
        nonResponseTextLabel.setOpaque(true);
        nonResponseTextLabel.setBackground(BackColor);
        nonResponseTextButton = new JButton("Colorize");
        nonResponseTextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    JDialog frame = new JDialog();
                    Color defaultCol = ForColor;
                    Color newColor = JColorChooser.showDialog(frame, "Choose " + responseColorType, defaultCol);
                    if (newColor != null) {
                        sharedVariables.tabStuff[consoleNumber].ForColor = newColor;
                        nonResponseTextLabel.setForeground(sharedVariables.tabStuff[consoleNumber].ForColor);
                        backgroundLabel.setForeground(sharedVariables.tabStuff[consoleNumber].ForColor);

                    }
                }// end try
                catch (Exception e) {
                }
            }
        });


//  timestamp Text color
        timestampLabel = new JPaintedLabel("Timestamp Color", sharedVariables);
        timestampLabel.setForeground(timestampColor);
        timestampLabel.fontType = 0;
        timestampLabel.setOpaque(true);
        timestampLabel.setBackground(BackColor);
        timestampButton = new JButton("Colorize");
        timestampButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    JDialog frame = new JDialog();
                    Color defaultCol = timestampColor;
                    Color newColor = JColorChooser.showDialog(frame, "Choose Timestamp Color", defaultCol);
                    if (newColor != null) {
                        sharedVariables.tabStuff[consoleNumber].timestampColor = newColor;
                        timestampLabel.setForeground(sharedVariables.tabStuff[consoleNumber].timestampColor);

                    }
                }// end try
                catch (Exception e) {
                }
            }
        });

//  typed Text color
        typedLabel = new JPaintedLabel("Typed Color", sharedVariables);
        typedLabel.setForeground(typedColor);
        typedLabel.fontType = 0;
        typedLabel.setOpaque(true);
        typedLabel.setBackground(BackColor);
        typedButton = new JButton("Colorize");
        typedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    JDialog frame = new JDialog();
                    Color defaultCol = typedColor;
                    Color newColor = JColorChooser.showDialog(frame, "Choose Typed Color", defaultCol);
                    if (newColor != null) {
                        sharedVariables.tabStuff[consoleNumber].typedColor = newColor;
                        typedLabel.setForeground(sharedVariables.tabStuff[consoleNumber].typedColor);

                    }
                }// end try
                catch (Exception e) {
                }
            }
        });

//  background color
        backgroundLabel = new JPaintedLabel("Background Color", sharedVariables);
        backgroundLabel.setForeground(ForColor);
        backgroundLabel.fontType = 0;


        backgroundLabel.setOpaque(true);
        backgroundLabel.setBackground(BackColor);
        backgroundButton = new JButton("Colorize");
        backgroundButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    JDialog frame = new JDialog();
                    Color defaultCol = BackColor;
                    Color newColor = JColorChooser.showDialog(frame, "Choose Background Color", defaultCol);
                    if (newColor != null) {
                        sharedVariables.tabStuff[consoleNumber].BackColor = newColor;
                        updateLabelBackgrounds(sharedVariables.tabStuff[consoleNumber].BackColor);

                    }
                }// end try
                catch (Exception e) {
                }
            }
        });


//  reset colors
        resetLabel = new JPaintedLabel("Reset to Global", sharedVariables);
        resetLabel.setForeground(ForColor);
        resetLabel.fontType = 0;


        resetLabel.setOpaque(true);
        resetLabel.setBackground(BackColor);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    sharedVariables.tabStuff[consoleNumber].typedColor = null;
                    sharedVariables.tabStuff[consoleNumber].BackColor = null;
                    sharedVariables.tabStuff[consoleNumber].timestampColor = null;
                    sharedVariables.tabStuff[consoleNumber].ForColor = null;
                    sharedVariables.tabStuff[consoleNumber].responseColor = null;
                    sharedVariables.tabStuff[consoleNumber].qtellcolor = null;
                    sharedVariables.tabStuff[consoleNumber].tellcolor = null;
                    if (sharedVariables.looking[me.consoleNumber] == consoleNumber)
                        me.makeHappen(consoleNumber);
                    dispose();


                }// end try
                catch (Exception e) {
                }
            }
        });

        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");
        okButton.setBackground(new Color(230, 220, 220));
        cancelButton.setBackground(new Color(230, 220, 220));

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    if (sharedVariables.looking[me.consoleNumber] == consoleNumber)
                        me.makeHappen(consoleNumber);

                    dispose();

                }// end try
                catch (Exception e) {
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    dispose();
                }// end try
                catch (Exception e) {
                }
            }
        });
        int numberOfRows = 9;
        if (channels.fics) {
            numberOfRows = 8;
        }
        pane.setLayout(new GridLayout(numberOfRows, 2)); // rows collums


        pane.add(tellButton);
        pane.add(tellLabel);

        pane.add(qtellButton);
        pane.add(qtellLabel);


        if (!channels.fics) {
            pane.add(responseTextButton);
            pane.add(responseTextLabel);
        }


        pane.add(nonResponseTextButton);
        pane.add(nonResponseTextLabel);

        pane.add(backgroundButton);
        pane.add(backgroundLabel);

        pane.add(timestampButton);
        pane.add(timestampLabel);

        pane.add(typedButton);
        pane.add(typedLabel);

        pane.add(resetButton);
        pane.add(resetLabel);


        pane.add(okButton);
        pane.add(cancelButton);


        setSize(470, 320);
        setTitle("Tab " + consoleNumber + " Color Chooser");
    }// end constructor


    void updateLabelBackgrounds(Color c) {
        tellLabel.setBackground(c);
        qtellLabel.setBackground(c);
        backgroundLabel.setBackground(c);
        responseTextLabel.setBackground(c);
        nonResponseTextLabel.setBackground(c);
        timestampLabel.setBackground(c);
        typedLabel.setBackground(c);
    }
}// end class
