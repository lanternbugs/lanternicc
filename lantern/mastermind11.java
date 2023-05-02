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
import java.awt.geom.Rectangle2D;
//import javax.jnlp.*;


//public class mastermind11  extends JApplet
/*public class mastermind11 extends JApplet
{
public void init()
{
//public static void main(String[] args)
//{


//public void init()

//{
mastermindframe frame = new mastermindframe();

frame.setSize(600,600);
//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
frame.setTitle("Mastermind");
frame.setVisible(true);
frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);


}
}// end main class

*/

class mastermind11 extends JInternalFrame {

    mastermind11() {

        super("Mastermind",
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable

        setSize(600, 600);
//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Mastermind");
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        mastermindpanel panel = new mastermindpanel();
        add(panel);

    }// end constructor


    class mastermindpanel extends JPanel implements MouseMotionListener, MouseListener {

        double width = 30;
        double height = 34;
        double originX = 50;
        double originY = 50;
        double masterOriginY = 10;
        double gap = 2;
        double gapY = 6;
        int OFFBOARD = -100;
        mastermindData mydata;
        double[] colorTabsX;
        double[] colorTabsY;
        int maxColors = 6;
        int oldmx;
        int oldmy;
        int mx;
        int my;
        int[] currentGuess;
        int currentGuessTop;
        JButton undoButton;
        JButton guessButton;
        JButton newButton;
        boolean winner;


        mastermindpanel() {
            mydata = new mastermindData();
            addMouseMotionListener(this);
            addMouseListener(this);
            winner = false;

            undoButton = new JButton("Undo");
            guessButton = new JButton("Score");
            newButton = new JButton("New Game");
            add(undoButton);
            add(guessButton);
            add(newButton);
            newButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        mydata.resetGame();
                        winner = false;
                        mydata.state = mydata.ONGOING;
                        repaint();

                    }// end try
                    catch (Exception e) {
                    }
                }
            });

            undoButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        if (currentGuessTop == 0)
                            return;

                        currentGuess[--currentGuessTop] = 0;
                        boolean rowDone = mydata.makeGuess(currentGuess);
                        repaint();

                    }// end try
                    catch (Exception e) {
                    }
                }
            });

            guessButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    try {
                        if (currentGuessTop != mydata.guessLimit)
                            return;


                        winner = mydata.scoreGuess(currentGuess);

                        resetCurrentGuess();

                        repaint();

                    }// end try
                    catch (Exception e) {
                    }
                }
            });


            colorTabsX = new double[maxColors];
            colorTabsY = new double[maxColors];
            currentGuess = new int[mydata.guessLimit];
            resetCurrentGuess();
// populate their values
            for (int a = 0; a < maxColors; a++) {
                colorTabsX[a] = (double) (originX + a * width);
                colorTabsY[a] = (double) (originY + (mydata.guessMax + 1) * height);
            }

        }

        void resetCurrentGuess() {
            for (int a = 0; a < mydata.guessLimit; a++)
                currentGuess[a] = 0;
            currentGuessTop = 0;

        }

        public void paintComponent(Graphics g) {
            Color backColor, red, green, blue, orange, yellow, purple, white, black, colorEmpty;
            backColor = new Color(75, 75, 75);
            red = new Color(255, 0, 0);
            green = new Color(0, 255, 0);
            blue = new Color(0, 0, 255);
            yellow = new Color(255, 255, 0);
            purple = new Color(160, 32, 240);
            orange = new Color(255, 165, 0);
            white = new Color(255, 255, 255);
            black = new Color(0, 0, 0);

            colorEmpty = new Color(200, 200, 200);


            try {

                super.paintComponent(g);

                setBackground(backColor);

                Graphics2D g2 = (Graphics2D) g;

                for (int a = mydata.guessMax - 1; a >= 0; a--) {
                    int totalScores = 0;
                    for (int b = 0; b < mydata.guessLimit; b++) {
                        // set colors
                        if (mydata.guessGrid[a][b] == mydata.RED)
                            g2.setColor(red);
                        else if (mydata.guessGrid[a][b] == mydata.BLUE)
                            g2.setPaint(blue);
                        else if (mydata.guessGrid[a][b] == mydata.GREEN)
                            g2.setPaint(green);
                        else if (mydata.guessGrid[a][b] == mydata.YELLOW)
                            g2.setPaint(yellow);
                        else if (mydata.guessGrid[a][b] == mydata.ORANGE)
                            g2.setPaint(orange);
                        else if (mydata.guessGrid[a][b] == mydata.PURPLE)
                            g2.setPaint(purple);
                        else
                            g2.setPaint(colorEmpty);

                        g2.fill(new Rectangle2D.Double((double) originX + b * width, (double) originY + (mydata.guessMax - a - 1) * height, width - gap, height - gapY));
                        if (mydata.scoreGrid[a][b] == mydata.BLACK) {
                            g2.setPaint(black);
                            totalScores++;
                        } else if (mydata.scoreGrid[a][b] == mydata.WHITE) {
                            g2.setPaint(white);
                            totalScores++;
                        } else
                            g2.setPaint(backColor);
                        g2.fill(new Rectangle2D.Double((double) (originX * 1.3) + mydata.guessLimit * width + b * width / 2, (double) originY + (mydata.guessMax - a - 1) * height, (width / 2) - gap, height - gapY));

                    }

                    g2.setColor(black);
                    if (totalScores == 0 && a < mydata.guessTop)
                        g2.drawString("Nothing Scored", (int) (originX * 1.3 + mydata.guessLimit * width), (int) (originY + (mydata.guessMax - a - 1) * height + height / 2));
                }// end outer for
                if (mydata.state == mydata.DONE) {


                    for (int b = 0; b < mydata.guessLimit; b++) {
                        // set colors
                        if (mydata.masterBoard[b] == mydata.RED)
                            g2.setColor(red);
                        else if (mydata.masterBoard[b] == mydata.BLUE)
                            g2.setPaint(blue);
                        else if (mydata.masterBoard[b] == mydata.GREEN)
                            g2.setPaint(green);
                        else if (mydata.masterBoard[b] == mydata.YELLOW)
                            g2.setPaint(yellow);
                        else if (mydata.masterBoard[b] == mydata.ORANGE)
                            g2.setPaint(orange);
                        else if (mydata.masterBoard[b] == mydata.PURPLE)
                            g2.setPaint(purple);
                        else
                            g2.setPaint(colorEmpty);

                        g2.fill(new Rectangle2D.Double((double) originX + b * width, (double) masterOriginY, width - gap, height - gapY));
                    }// end for
                    g2.setPaint(black);
                    if (winner == true)
                        g2.drawString("You win", (int) (originX + mydata.guessLimit * width + 20), (int) masterOriginY + 10);
                    else
                        g2.drawString("You lose", (int) (originX + mydata.guessLimit * width + 20), (int) masterOriginY + 10);


                }// end if done

                // now color tabs
                g2.setColor(red);
                g2.fill(new Rectangle2D.Double((double) colorTabsX[0], (double) colorTabsY[0], width - gap, height - gapY));

                g2.setPaint(blue);
                g2.fill(new Rectangle2D.Double((double) colorTabsX[1], (double) colorTabsY[1], width - gap, height - gapY));

                g2.setPaint(green);
                g2.fill(new Rectangle2D.Double((double) colorTabsX[2], (double) colorTabsY[2], width - gap, height - gapY));

                g2.setPaint(yellow);
                g2.fill(new Rectangle2D.Double((double) colorTabsX[3], (double) colorTabsY[3], width - gap, height - gapY));

                g2.setPaint(purple);
                g2.fill(new Rectangle2D.Double((double) colorTabsX[4], (double) colorTabsY[4], width - gap, height - gapY));

                g2.setPaint(orange);
                g2.fill(new Rectangle2D.Double((double) colorTabsX[5], (double) colorTabsY[5], width - gap, height - gapY));
                newButton.setLocation((int) (originX), (int) (colorTabsY[5] + height));
                undoButton.setLocation((int) (originX + 150), (int) (colorTabsY[5] + height));
                guessButton.setLocation((int) (originX + 250), (int) (colorTabsY[5] + height));


// paint 10 guesses and scores
// paint row of 6 colors to click for guesses


            } // end try
            catch (Exception e) {
            }
        } // end paint components


        /*****************************    mouse events *****************************************/

        void eventOutput(String eventDescription, MouseEvent e) {
            oldmx = mx;
            oldmy = my;

            mx = e.getX();
            my = e.getY();

        }

        public void mouseMoved(MouseEvent e) {
            eventOutput("Mouse moved", e);
        }

        public void mouseDragged(MouseEvent e) {
            eventOutput("Mouse dragged", e);
        }


        public void mousePressed(MouseEvent e) {

            int num = getColorbutton();
            if (num == OFFBOARD)
                return;
            if (mydata.state != mydata.ONGOING)
                return;

               /* JFrame myframe = new JFrame();
                myframe.setSize(100,100);
                myframe.setTitle("" + num);
                myframe.setVisible(true);
                 */

            if (currentGuessTop >= mydata.guessLimit)
                return;
            currentGuess[currentGuessTop++] = num + 1;
            boolean rowDone = mydata.makeGuess(currentGuess);

            repaint();


        }


        int getColorbutton() {


            int x = mx;
            int y = my;


            for (int a = 0; a < maxColors; a++) {
                if (x > colorTabsX[a] && x < colorTabsX[a] + width)
                    if (y > colorTabsY[a] && y < colorTabsY[a] + height)
                        return a;
            }
            return OFFBOARD;


        }


        public void mouseEntered(MouseEvent me) {
        }


        public void mouseReleased(MouseEvent me) {
        }

        public void mouseExited(MouseEvent me) {
        }

        public void mouseClicked(MouseEvent me) {
        }


/********************************* end mouse events **************************************/


    }// end mastermind panel


}// end mastermind frame