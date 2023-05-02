package lantern;
/*
 *  Copyright (C) 2012-2022 Michael Ronald Adams, Andrey Gorlin.
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

//http://java.sun.com/products/jfc/tsc/articles/tablelayout/

import layout.*;

import javax.swing.*;
import java.awt.*;

//import javax.jnlp.*;


//make this Pearl for a pearl build and rename file Pearl.java. or Lantern for an icc build and rename file Lantern.java if you see it other way around
// more instructons on fics vs icc build in channels.java were the static variable fics is declared.
public class Lantern {

    public static void createFrame() {
        Frame frame = new Frame();
        frame.setBounds(100, 100, 300, 300);
        frame.show();

        double size[][] =
                {{0.25, 0.25, 0.25, 0.25},
                        {50, TableLayout.FILL, 40, 40, 40}};

        frame.setLayout(new TableLayout(size));
    }

    public static void main(String[] args) {
        //public void init()

        //{
        try {

            String os = System.getProperty("os.name").toLowerCase();
            if (os.indexOf("mac") >= 0) {
                System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
            }

            if (os.indexOf("win") >= 0)
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                // UIManager.setLookAndFeel( "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            else
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.SystemLookAndFeel");

        } catch (Exception d) {
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final Multiframe frame = new Multiframe();
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    //DO_NOTHING_ON_CLOSE
                    if (channels.fics) {
                        frame.setTitle("Pearl Chess on FICS " + frame.sharedVariables.version);
                    } else {
                        frame.setTitle("Lantern Chess " + frame.sharedVariables.version);
                    }

                    frame.setVisible(true);

                    // uncomment below line to test name and pass saving
                    //passTest tester = new passTest();


                    //frame.setDefaultLookAndFeelDecorated(false);


                    frame.repaintTabs();


                    try {
                        frame.consoleSubframes[0].setSelected(true);
                    } catch (Exception dd) {
                    }

                    // warning dialogue

                    String swarning = "This is a beta version of Mike's new Interface.  Game " +
                            "play is possible but it's highly recommended you play unrated.  I want" +
                            " more testing before rated play can happen.  Not all wilds are supported.";

                    if (frame.sharedVariables.ActivitiesOnTop) {
                        frame.myfirstlist.add(frame.sharedVariables.activitiesPanel);
                        frame.myfirstlist.notontop.setSelected(true);
                    } else {
                        frame.mysecondlist.add(frame.sharedVariables.activitiesPanel);
                        frame.mysecondlist.notontop.setSelected(false);
                    }
                    //Popup pframe = new Popup((JFrame) frame, true, swarning);
                    //pframe.setVisible(true);
                    try {
                        if (frame.sharedVariables.activitiesOpen &&
                                !frame.sharedVariables.activitiesNeverOpen)
                            frame.openActivities();
                    } catch (Exception badopen) {
                    }

                    try {
                        if (frame.sharedVariables.seeksOpen &&
                                !frame.sharedVariables.activitiesNeverOpen)
                            frame.openSeekGraph();
                    } catch (Exception badopen) {
                    }

                    try {
                        frame.myConnection =
                                new connectionDialog(frame, frame.sharedVariables, frame.queue, false);
                        frame.myConnection.setLocation(frame.getLocation().x, frame.getLocation().y);
                        frame.myConnection.setVisible(true);

                    } catch (Exception bfocus) {
                    }

                    try {
                        frame.sharedVariables.setDefaultWebBoardSize();
                    } catch (Exception duiii) {
                    }
                } catch (Exception e1) {
                    //ignore
                }
            }
        });
    }

}// end main class

