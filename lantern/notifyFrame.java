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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

class notifyFrame extends JDialog// implements InternalFrameListener
{

    //subframe [] consoleSubframes;
    channels sharedVariables;
    JCheckBoxMenuItem notontop;
    listClass notifyList;
    ConcurrentLinkedQueue queue;
    notifyPanel notifylistScrollerPanel;


    notifyFrame(JFrame master, channels sharedVariables1, ConcurrentLinkedQueue queue1, listClass notifyList1) {
        super(master, false);
        sharedVariables = sharedVariables1;
        queue = queue1;
        notifyList = notifyList1;

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {

                setVisible(false);
            }
        });

        setTitle("Notify");
        setSize(130, 240);
        notifylistScrollerPanel = new notifyPanel(sharedVariables, queue, notifyList);

        notifylistScrollerPanel.notifylistScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        add(notifylistScrollerPanel);


    }// end constructor


    void saveSize() {
        int width = getWidth();
        int height = getHeight();
        if (width > 40 && width < 600) {
            sharedVariables.notifyWindowWidth = width;
        }

        if (height > 40 && height < 600) {
            sharedVariables.notifyWindowHeight = height;
        }
    }


}//end class