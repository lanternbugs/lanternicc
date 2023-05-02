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

class tellMasterDialog extends JDialog {
    channels sharedVariables;
    JButton OkButton;
    JButton cancelButton;
    JComboBox tabChoices;
    JCheckBox sound;
    JCheckBox qchannels;
    JLabel soundLabel;
    JLabel channelLabel;
    JLabel preamble;
    JLabel preamble2;
    JLabel preamble3;

    tellMasterDialog(JFrame frame, boolean mybool, channels sharedVariables1, final String handle, boolean soundOn, boolean channelsOn) {
        super(frame, mybool);
        sharedVariables = sharedVariables1;

        JPanel mypanel = new JPanel();

        preamble = new JLabel("For person " + handle + " select the tab you want to direct their tells");
        preamble2 = new JLabel("and check if you want sound for their tells or not.");
        preamble3 = new JLabel("Then hit ok.  cancel to back out.  This resets to default tell scheme at reconnect.");


        sound = new JCheckBox();

        if (soundOn)
            sound.setSelected(true);

        soundLabel = new JLabel("Check if you want to hear sound for this person's tells.");

        qchannels = new JCheckBox();

        if (channelsOn)
            qchannels.setSelected(true);

        channelLabel = new JLabel("Check if you want to quarantine channel tells as well to this tab.");


//String [] stuff = { "M0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11"};
        String[] tabNames = new String[sharedVariables.maxConsoleTabs];
        for (int a = 0; a < sharedVariables.maxConsoleTabs; a++)
            if (a == 0)
                tabNames[a] = "M0";
            else
                tabNames[a] = "C" + a;
        try {
            for (int ab = 0; ab < sharedVariables.maxConsoleTabs; ab++)
                if (!sharedVariables.consoleTabCustomTitles[ab].equals(""))
                    tabNames[ab] = sharedVariables.consoleTabCustomTitles[ab];
                else
                    tabNames[ab] = sharedVariables.consoleTabTitles[ab];

        } catch (Exception d) {
        }
        tabChoices = new JComboBox(tabNames);


        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    dispose();
                }// end try
                catch (Exception e) {
                }
            }
        });

        OkButton = new JButton("Ok");
        OkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {

                    boolean found = false;
                    for (int i = 0; i < sharedVariables.toldTabNames.size(); i++) {
                        if (sharedVariables.toldTabNames.get(i).name.equals(handle)) {
                            sharedVariables.toldTabNames.get(i).tab = tabChoices.getSelectedIndex();
                            if (sound.isSelected() == true)
                                sharedVariables.toldTabNames.get(i).sound = true;
                            else
                                sharedVariables.toldTabNames.get(i).sound = false;
                            if (qchannels.isSelected() == true)
                                sharedVariables.toldTabNames.get(i).blockChannels = true;
                            else
                                sharedVariables.toldTabNames.get(i).blockChannels = false;

                            found = true;
                            break;
                        }
                    }// end for

                    if (found == false) {
                        told him = new told();
                        him.name = handle;
                        him.tab = tabChoices.getSelectedIndex();
                        if (sound.isSelected() == true)
                            him.sound = true;
                        else
                            him.sound = false;
                        if (qchannels.isSelected() == true)
                            him.blockChannels = true;
                        else
                            him.blockChannels = false;

                        sharedVariables.toldTabNames.add(him);
                    }


                    dispose();
                }// end try
                catch (Exception e) {
                }
            }
        });
        mypanel.setLayout(new GridLayout(5, 1)); // rows collums

        JPanel preamblePanel = new JPanel();
        preamblePanel.setLayout(new GridLayout(3, 1));
        preamblePanel.add(preamble);
        preamblePanel.add(preamble2);
        preamblePanel.add(preamble3);
        mypanel.add(preamblePanel);

        JPanel SoundPanel = new JPanel();
        SoundPanel.add(sound);
        SoundPanel.add(soundLabel);
        mypanel.add(SoundPanel);

        JPanel ChannelPanel = new JPanel();
        ChannelPanel.add(qchannels);
        ChannelPanel.add(channelLabel);
        mypanel.add(ChannelPanel);

        JPanel tabPanel = new JPanel();
        JLabel choiceLabel = new JLabel("Select tab tells of " + handle + " go.");
        tabPanel.add(choiceLabel);
        tabPanel.add(tabChoices);
        mypanel.add(tabPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(OkButton);
        buttonPanel.add(cancelButton);
        mypanel.add(buttonPanel);


        add(mypanel);
        setSize(500, 250);
        setVisible(true);


    }// end constructor method

}// end class