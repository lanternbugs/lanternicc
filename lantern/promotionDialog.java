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
import java.awt.event.*;
import java.util.concurrent.ConcurrentLinkedQueue;


class promotionDialog extends JDialog {


    myoutput amove;
    resourceClass graphics;
    ConcurrentLinkedQueue<myoutput> queue;

    promotionDialog(JFrame frame, boolean mybool, myoutput amove1, resourceClass graphics1, ConcurrentLinkedQueue<myoutput> queue1) {
        super(frame, mybool);
        graphics = graphics1;
        amove = amove1;
        queue = queue1;
        promoPanel panel = new promoPanel();
        panel.addMouseMotionListener(panel);
        panel.addMouseListener(panel);

        this.add(panel);
        /* layout */
/*GroupLayout layout = new GroupLayout(getContentPane());
getContentPane().setLayout(layout);
ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
hGroup.addComponent(panel, Short.MAX_VALUE, Short.MAX_VALUE, Short.MAX_VALUE);
layout.setHorizontalGroup(hGroup);
//Create a parallel group for the vertical axis
ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading
vGroup.addComponent(panel, Short.MAX_VALUE, Short.MAX_VALUE, Short.MAX_VALUE);
layout.setVerticalGroup(vGroup);
*/
// end layout
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                dispose();

            }
        });
    }    // end constructore

    public void dispose() {
        amove.promotion = false;
        queue.add(amove);
        super.dispose();
    }

    class promoPanel extends JPanel implements MouseMotionListener, MouseListener {

        int mx;
        int my;
        int boardx;
        int boardy;
        int square;

        public void paintComponent(Graphics g) {

            try {

                super.paintComponent(g);

                setBackground(new Color(255, 255, 255));
                int wide = getWidth();
                int height = getHeight();
                Graphics2D g2 = (Graphics2D) g;
                int Plus = 0;
                if (amove.iAmWhite == false)
                    Plus = 6;

                boardx = 0;
                boardy = 0; // upper left
                square = wide / 4;
                if (amove.wildNumber == 26)
                    square = wide / 5;
                int dif = 2;
                int max = 5;
                if (amove.wildNumber == 26)
                    max = 6;
                for (int a = 1; a < max; a++)
                    g.drawImage(graphics.pieces[0][a + Plus], boardx + (a - 1) * square + dif, boardy, square - dif, height, this);

            } catch (Exception dui) {
            }

        }

        void eventOutput(String eventDescription, MouseEvent e) {


            mx = e.getX();
            my = e.getY();


        }


        public void mouseExited(MouseEvent e) {

        }

        public void mouseMoved(MouseEvent e) {
            eventOutput("Mouse moved", e);
        }

        public void mouseDragged(MouseEvent e) {
            eventOutput("Mouse dragged", e);
        }

        public void mouseEntered(MouseEvent me) {

        }

        public void mouseClicked(MouseEvent e) {
            eventOutput("Mouse Clicked", e);

        }

        public void mousePressed(MouseEvent e) {
            eventOutput("Mouse Pressed", e);
            String end = "";
            if (amove.wildNumber == 26) {
                if (mx < square)
                    end = "=N\n";
                else if (mx < 2 * square)
                    end = "=B\n";
                else if (mx < 3 * square)
                    end = "=R\n";
                else if (mx < 4 * square)
                    end = "=Q\n";
                else if (mx < 5 * square)
                    end = "=K\n";

            } else {
                if (mx < square)
                    end = "=N\n";
                else if (mx < 2 * square)
                    end = "=B\n";
                else if (mx < 3 * square)
                    end = "=R\n";
                else if (mx < 4 * square)
                    end = "=Q\n";
            }
            amove.data = amove.data.substring(0, amove.data.length() - 1);
            amove.data = amove.data + end;

            dispose();
        }


        public void mouseReleased(MouseEvent me) {


        }


    }// end sub class panel

}// end class