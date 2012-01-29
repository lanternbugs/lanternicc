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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JDialog;
import java.io.*;
import java.net.*;
import java.lang.Thread.*;
import java.applet.*;
import javax.swing.GroupLayout.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import java.lang.Integer;
import javax.swing.text.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.applet.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.StringTokenizer;
import java.util.concurrent.locks.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.event.ChangeEvent.*;
import java.util.concurrent.locks.*;
import java.util.Random;

import layout.TableLayout;





  class gameboardControlsPanel extends JPanel  {

    JLabel topClockDisplay;
    JLabel botClockDisplay;
    //JPanel topClock;
    //JPanel bottomClock;
    JPanel actionPanel;
    JPanel actionPanelFlow;
    JPanel buttonPanelFlow;
    JPanel andreyNavig;
    JPanel andreyAct;
    float andreysFontSize=16;
    //JPanel sliderArrows;
    JLabel topNameDisplay;
    JLabel botNameDisplay;
    JLabel gameListingDisplay;

    JLabel lastMove;
    JButton forward;
    JButton backward;
    JButton backwardEnd;
    JButton forwardEnd;
    JPanel buttonPanel;

    JButton resignButton;
    JButton drawButton;
    JButton abortButton;

    Color mycolor;
    int oldLooking = -1;
    String oldcountry1="";
    String oldcountry2="";
    JLabel flagTop = new JLabel("");
    JLabel flagBottom = new JLabel("");

    JScrollPane listScroller;

    public void setAndreyFontSize(float n)
    {
     sharedVariables.andreysFonts = true;
     andreysFontSize=n;
     setFont();
    }

    public float getAndreyFontSize()
    {

     return andreysFontSize;
    }


    public void setFont() {
      try {
        topClockDisplay.setFont(sharedVariables.myGameClockFont);
        botClockDisplay.setFont(sharedVariables.myGameClockFont);
        if(sharedVariables.andreysFonts == false)
        {
        topNameDisplay.setFont(sharedVariables.myGameFont);
        botNameDisplay.setFont(sharedVariables.myGameFont);
        gameListingDisplay.setFont(sharedVariables.myGameFont);

        lastMove.setFont(sharedVariables.myGameFont);
        }
       else // andreys fonts on
        {

         Font newFont =sharedVariables.myGameFont.deriveFont(andreysFontSize);
        topNameDisplay.setFont(newFont);
        botNameDisplay.setFont(newFont);
        gameListingDisplay.setFont(newFont);

        lastMove.setFont(newFont);


        }
        //forward.setFont(sharedVariables.myGameFont);
        //backward.setFont(sharedVariables.myGameFont);
        //backwardEnd.setFont(sharedVariables.myGameFont);
        //forwardEnd.setFont(sharedVariables.myGameFont);
      }	catch(Exception e) {}
    }

    int getBoardType() {
 
      if (sharedVariables.mygame[gameData.LookingAt].state ==
          sharedVariables.STATE_OBSERVING &&
          sharedVariables.randomBoardTiles == true)
        return sharedVariables.mygame[gameData.LookingAt].randomObj.boardNum;

      return sharedVariables.boardType;
    }


    public void paintComponent(Graphics g) {

      try {

        super.paintComponent(g);

        Color highlightcolor;
        highlightcolor = new Color(230,0,10);
        setBackground(sharedVariables.boardBackgroundColor);

        /*
        int Width = getWidth();
        int Height = getHeight();
        if (sharedVariables.useLightBackground == true)
          g.drawImage(graphics.boards[sharedVariables.boardType][graphics.light],
                      0, 0,  Width, Height, this);
        else
          setBackground(sharedVariables.boardBackgroundColor);
        */
        if (isAndreyLayout() == false) {
          if(actionPanel != null)
          actionPanel.setBackground(sharedVariables.boardBackgroundColor);
          if(actionPanelFlow != null)
          actionPanelFlow.setBackground(sharedVariables.boardBackgroundColor);
          if(buttonPanelFlow != null)
          buttonPanelFlow.setBackground(sharedVariables.boardBackgroundColor);
        }
        if(isAndreyLayout() == true)
        {
         if(andreyNavig!=null)
         andreyNavig.setBackground(sharedVariables.boardBackgroundColor);
         if(andreyAct != null)
         andreyAct.setBackground(sharedVariables.boardBackgroundColor);

        }
        flagTop.setBackground(sharedVariables.boardBackgroundColor);
        flagBottom.setBackground(sharedVariables.boardBackgroundColor);


        if (oldLooking != gameData.LookingAt ||
            oldcountry1 != sharedVariables.mygame[gameData.LookingAt].country1 ||
            oldcountry2 != sharedVariables.mygame[gameData.LookingAt].country2) {
          myboard.redrawFlags();
          oldLooking = gameData.LookingAt;
          oldcountry1 = sharedVariables.mygame[gameData.LookingAt].country1;
          oldcountry2 = sharedVariables.mygame[gameData.LookingAt].country2;
        }
        if (sharedVariables.mygame[gameData.LookingAt].country1.equals("") &&
            sharedVariables.mygame[gameData.LookingAt].country2.equals("")) {

          flagTop.setVisible(false);
          flagBottom.setVisible(false);

        }// end no game

        //sliderArrows.setBackground(sharedVariables.boardBackgroundColor);
        //topClock.setBackground(sharedVariables.boardBackgroundColor);
        //bottomClock.setBackground(sharedVariables.boardBackgroundColor);

        if (isAndreyLayout() == false)
          buttonPanel.setBackground(sharedVariables.boardBackgroundColor);

        setClockBackgrounds();

        topNameDisplay.setForeground(sharedVariables.boardForegroundColor);
        botNameDisplay.setForeground(sharedVariables.boardForegroundColor);
        gameListingDisplay.setForeground(sharedVariables.boardForegroundColor);

        //lastMove.setForeground(sharedVariables.boardForegroundColor);
        /*
        forward.setForeground(sharedVariables.boardForegroundColor);
        backward.setForeground(sharedVariables.boardForegroundColor);
        backwardEnd.setForeground(sharedVariables.boardForegroundColor);
        forwardEnd.setForeground(sharedVariables.boardForegroundColor);
        */

        sharedVariables.moveSliders[gameData.BoardIndex].setBackground
          (sharedVariables.boardBackgroundColor);

        //g.drawString("in here",  50,  50);
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        if (width<10)
          width=10;
        if (height<10)
          height=10;
        //g.drawString("width " + width + " height " + height,  75,  50);
        //g.drawString("movingpiece " + movingpiece + " piecemoving "
        //+ piecemoving + " mx " + mx + " my " + my, 20, (int) height
        //* 9/10);

        double width1 = (double) width;
        double height1= (double) height;
        int timex = (int) (width * 1/20);
        int otimey = (int) (height * 1/8);
        int ptimey = (int) (height * 7/8);

        int name2y = (int) (height * 1/4);
        int name1y= (int) (height * 3/4);
        int gameListingy= (int) (height * 3/8);


        int slidery=(int) height / 2;
        if (timex < 1)
          timex=1;
        if (otimey<1)
          otimey=1;
        if (ptimey<1)
          ptimey=1;

        int c=0;
        //g.drawString("" + blackClock,  timex,  otimey);
        //g.drawString("" + whiteClock,  timex,  ptimey);
       TimeDisplayClass timeGetter = new TimeDisplayClass(sharedVariables);
        String whiteTimeDisplay = timeGetter.getWhiteTimeDisplay(gameData.LookingAt);
        String blackTimeDisplay = timeGetter.getBlackTimeDisplay(gameData.LookingAt);
         timeGetter=null;
        String whiteCount="";
        String blackCount="";


        // material count doesnt currently go here
        try {
          if (sharedVariables.showMaterialCount==true &&
              !sharedVariables.mygame[gameData.LookingAt].name1.equals(""))
            if (sharedVariables.mygame[gameData.LookingAt].wild != 0 &&
                sharedVariables.mygame[gameData.LookingAt].wild != 20 && isAndreyLayout() == false) {
              whiteCount=" (" + sharedVariables.mygame
                [gameData.LookingAt].whiteMaterialCount+")";
              blackCount=" (" + sharedVariables.mygame
                [gameData.LookingAt].blackMaterialCount+")";
            }
        } catch(Exception darn) {}


        if (sharedVariables.mygame[gameData.LookingAt].iflipped == 1) {

          topNameDisplay.setText(sharedVariables.mygame[gameData.LookingAt].name1 +
                                 sharedVariables.mygame[gameData.LookingAt].country1 +
                                 whiteCount);
          botNameDisplay.setText(sharedVariables.mygame[gameData.LookingAt].name2 +
                                 sharedVariables.mygame[gameData.LookingAt].country2 +
                                 blackCount);
          topClockDisplay.setText(whiteTimeDisplay);
          botClockDisplay.setText(blackTimeDisplay);

        } else {

          topNameDisplay.setText(sharedVariables.mygame[gameData.LookingAt].name2 +
                                 sharedVariables.mygame[gameData.LookingAt].country2 +
                                 blackCount);
          botNameDisplay.setText(sharedVariables.mygame[gameData.LookingAt].name1 +
                                 sharedVariables.mygame[gameData.LookingAt].country1 +
                                 whiteCount);
          topClockDisplay.setText(blackTimeDisplay);
          botClockDisplay.setText(whiteTimeDisplay);
        }
        String listing = sharedVariables.mygame[gameData.LookingAt].gameListing;
        if (sharedVariables.showMaterialCount==true &&
            !sharedVariables.mygame[gameData.LookingAt].name1.equals(""))
          if (sharedVariables.mygame[gameData.LookingAt].wild == 0 ||
              sharedVariables.mygame[gameData.LookingAt].wild == 20 || isAndreyLayout() == true)
            listing += " " + sharedVariables.mygame
              [gameData.LookingAt].whiteMaterialCount + " - " +
              sharedVariables.mygame[gameData.LookingAt].blackMaterialCount;
        gameListingDisplay.setText(listing);
        setLastMove();

        if (sharedVariables.mygame[gameData.LookingAt].state ==
            sharedVariables.STATE_PLAYING) {// make draw abort resign buttons visible

          resignButton.setVisible(true);
          drawButton.setVisible(true);
          abortButton.setVisible(true);

          if (isAndreyLayout() == false)
            actionPanel.setVisible(true);
        } else {

          resignButton.setVisible(false);
          drawButton.setVisible(false);
          abortButton.setVisible(false);

          if (isAndreyLayout() == false)
            actionPanel.setVisible(false);
        }
        //sharedVariables.moveSliders[gameData.BoardIndex].setLocation(3, slidery);

        //g2.drawString("at " + sharedVariables.moveSliders
        //              [gameData.BoardIndex].getValue() + " of " +
        //              sharedVariables.moveSliders[gameData.BoardIndex].getMaximum(),
        //              5,15);

      } catch(Exception e) {}
    }
     gameboard myboard;
     channels sharedVariables;
     gamestuff gameData;
     ConcurrentLinkedQueue<myoutput> queue;
    gameboardControlsPanel(gameboard myboard1, gamestuff gameData1, channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1) {

    queue=queue1;
    myboard=myboard1;
    gameData=gameData1;
    sharedVariables=sharedVariables1;
      topClockDisplay = new JLabel("0");
      botClockDisplay = new JLabel("0");
      topClockDisplay.setBackground(sharedVariables.boardBackgroundColor);
      botClockDisplay.setBackground(sharedVariables.boardBackgroundColor);

      topClockDisplay.setOpaque(true);
      botClockDisplay.setOpaque(true);

      resignButton = new JButton("Resign");
      abortButton = new JButton("Abort");
      drawButton = new JButton("Draw");

      resignButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            try {
              sendAction("Resign");
            }// end try
            catch(Exception e) {}
          }
        });
      abortButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            try {
              sendAction("Abort");
            }// end try
            catch(Exception e) {}
          }
        });
      drawButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            try {
              sendAction("Draw");
            }// end try
            catch(Exception e) {}
          }
        });

      topNameDisplay = new JLabel(" ");
      botNameDisplay = new JLabel(" ");

      topNameDisplay.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2)
              makerightclickhappen(e);

          }

          public void mouseReleased(MouseEvent e) {

          }


          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}
          public void makerightclickhappen(MouseEvent e) {

            String name = topNameDisplay.getText();
            int n = name.indexOf(" ");
            if (n > -1)
              name = name.substring(0, n);

            final String nameF=name;

            JPopupMenu menu2=new JPopupMenu("Popup2");
            JMenuItem item11 = new JMenuItem("Observe " + nameF);
            item11.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  sendCommand("Observe " + nameF + "\n");
                }
              });

            menu2.add(item11);
            
            JMenuItem item2 = new JMenuItem("Follow " + nameF);
            item2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  sendCommand("Follow " + nameF + "\n");
                }
              });

            menu2.add(item2);
     
            JMenuItem item3 = new JMenuItem("Unfollow ");
            item3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  sendCommand("Unfollow\n");
                }
              });

            menu2.add(item3);

            JMenuItem item4 = new JMenuItem("Finger  " + nameF);
            item4.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  sendCommand("Finger " + nameF + "\n");
                }
              });

            menu2.add(item4);

            JMenuItem item5 = new JMenuItem("Move Board Up ");
            item5.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  try {
                    myboard.moveBoardDown();
                  } catch(Exception dui) {}
                }
              });

            menu2.add(item5);

            JMenuItem item6 = new JMenuItem("Move Board Down ");
            item6.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  try {
                    myboard.moveBoardUp();
                  } catch(Exception dui) {}
                }
              });

            menu2.add(item6);

            add(menu2);
            menu2.show(e.getComponent(),e.getX(),e.getY());
          }
        });


      botNameDisplay.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2)
              makerightclickhappen(e);
          }

          public void mouseReleased(MouseEvent e) {

          }


          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}
          public void makerightclickhappen(MouseEvent e) {

            String name = botNameDisplay.getText();
            int n = name.indexOf(" ");
            if(n > -1)
              name = name.substring(0, n);

            final String nameF=name;

            JPopupMenu menu2=new JPopupMenu("Popup2");
            JMenuItem item11 = new JMenuItem("Observe " + nameF);
            item11.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  sendCommand("Observe " + nameF + "\n");
                }
              });

            menu2.add(item11);
            
            JMenuItem item2 = new JMenuItem("Follow " + nameF);
            item2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  sendCommand("Follow " + nameF + "\n");
                }
              });

            menu2.add(item2);
     
            JMenuItem item3 = new JMenuItem("Unfollow ");
            item3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  sendCommand("Unfollow\n");
                }
              });

            menu2.add(item3);

            JMenuItem item4 = new JMenuItem("Finger  " + nameF);
            item4.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  sendCommand("Finger " + nameF + "\n");
                }
              });

            menu2.add(item4);

            JMenuItem item5 = new JMenuItem("Move Board Up ");
            item5.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  try {
                    myboard.moveBoardDown();
                  } catch(Exception dui) {}
                }
              });

            menu2.add(item5);

            JMenuItem item6 = new JMenuItem("Move Board Down ");
            item6.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  try {
                    myboard.moveBoardUp();
                  } catch(Exception dui) {}
                }
              });

            menu2.add(item6);

            add(menu2);
            menu2.show(e.getComponent(),e.getX(),e.getY());
          }
        });

      lastMove = new JLabel("");
      forward = new JButton(">");
      forwardEnd = new JButton(">>");
      backwardEnd = new JButton("<<");
      backward = new JButton("<");

      forward.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {

            if (sharedVariables.mygame[gameData.LookingAt].state ==
                sharedVariables.STATE_EXAMINING) {
              sendCommand("multi forward\n");
              myboard.giveFocus();

            } else {
              int loc = sharedVariables.moveSliders[gameData.BoardIndex].getValue();
              int max = sharedVariables.moveSliders[gameData.BoardIndex].getMaximum();
              if (loc < max) {
                loc++;
                sharedVariables.moveSliders[gameData.BoardIndex].setValue(loc);
                adjustMoveList();

                // to garantee last move updates last square highlight
               /* if(loc == max)
                {
		int [] slidingBoard = new int[64];
                sharedVariables.mygame[gameData.BoardIndex].getSliderBoard(sharedVariables.moveSliders[gameData.BoardIndex].getValue(), slidingBoard);
                }
                */
                myboard.mypanel.repaint();
              }
              myboard.giveFocus();
            }
          }
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}  });
      backward.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {

            if (sharedVariables.mygame[gameData.LookingAt].state ==
                sharedVariables.STATE_EXAMINING) {
              sendCommand("multi backward\n");
              myboard.giveFocus();

            } else {
              int loc = sharedVariables.moveSliders[gameData.BoardIndex].getValue();

              if (loc > 0) {
                loc--;
                sharedVariables.moveSliders[gameData.BoardIndex].setValue(loc);
                adjustMoveList();
                myboard.mypanel.repaint();
              }
              myboard.giveFocus();
            }
          }
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}  });
      forwardEnd.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if (sharedVariables.mygame[gameData.LookingAt].state ==
                sharedVariables.STATE_EXAMINING) {
              sendCommand("multi forward 999\n");

            } else {

              int loc = sharedVariables.moveSliders[gameData.BoardIndex].getValue();
              int max = sharedVariables.moveSliders[gameData.BoardIndex].getMaximum();
              if (loc < max) {

                sharedVariables.moveSliders[gameData.BoardIndex].setValue(max);
                adjustMoveList();
                /* if(loc == max)
                {
		int [] slidingBoard = new int[64];
                sharedVariables.mygame[gameData.BoardIndex].getSliderBoard(sharedVariables.moveSliders[gameData.BoardIndex].getValue(), slidingBoard);
                }
                */

                myboard.mypanel.repaint();
              }
            }

            myboard.giveFocus();
          }
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}  });
      backwardEnd.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {

            if (sharedVariables.mygame[gameData.LookingAt].state ==
                sharedVariables.STATE_EXAMINING) {
              sendCommand("multi backward 999\n");
            } else {
              int loc = sharedVariables.moveSliders[gameData.BoardIndex].getValue();

              if (loc > 0) {

                sharedVariables.moveSliders[gameData.BoardIndex].setValue(0);
                adjustMoveList();
                myboard.mypanel.repaint();

              }
            }
            myboard.giveFocus();

          }
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}  });

      //buttonPanel.setLayout(new GridLayout(1,4));
      /*buttonPanel.add(backwardEnd);
        buttonPanel.add(backward);
        buttonPanel.add(forward);
        buttonPanel.add(forwardEnd);
      */

      gameListingDisplay = new JLabel(" ");
      if (sharedVariables.moveSliders[gameData.BoardIndex] == null) {

        sharedVariables.moveSliders[gameData.BoardIndex] = new JSlider(0,0);
        sharedVariables.moveSliders[gameData.BoardIndex] .setPreferredSize
          ( new Dimension( 25, 75 ) );  // was 75 25 for horizontal oritntation
        sharedVariables.moveSliders[gameData.BoardIndex] .setOrientation
          (JSlider.VERTICAL);
        sharedVariables.moveSliders[gameData.BoardIndex] .setInverted(true);
        sharedVariables.moveSliders[gameData.BoardIndex].addMouseListener
          (new MouseAdapter() {
              public void mousePressed(MouseEvent e) {}
              public void mouseReleased(MouseEvent e) {
                myboard.mypanel.repaint();
                adjustMoveList();
              }
              public void mouseEntered (MouseEvent me) {}
              public void mouseExited (MouseEvent me) {}
              public void mouseClicked (MouseEvent me) {}  });
      }
      if (sharedVariables.mygametable[gameData.BoardIndex] == null) {
	sharedVariables.mygametable[gameData.BoardIndex] = new tableClass();
        sharedVariables.mygametable[gameData.BoardIndex].createMoveListColumns();
        sharedVariables.gametable[gameData.BoardIndex] =
          new JTable(sharedVariables.mygametable[gameData.BoardIndex].gamedata);
      }
      //gametable.setBackground(listColor);
      listScroller = new JScrollPane(sharedVariables.gametable[gameData.BoardIndex]);
      MouseListener mouseListenerEvents = new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {

            JTable target = (JTable)e.getSource();
            int row = target.getSelectedRow();
            int column = target.getSelectedColumn();
            int index = row * 2 + 1;
            if (column == 2)
              index++;
            if (column == 0)
              index--;
            if (index>=0) {
              ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
              Lock readLock = rwl.readLock();

              readLock.lock();

              sharedVariables.moveSliders[gameData.BoardIndex].setValue(index);

              readLock.unlock();
              
              myboard.mypanel.repaint();
            }
          }
        };
      sharedVariables.gametable[gameData.BoardIndex].addMouseListener
        (mouseListenerEvents);


      if (isAndreyLayout() == true)
        makeAndreysLayout();
      else
        makeLayout();

      setFont();

      //handleLayout();
    }
   boolean isAndreyLayout(){

    if(myboard.isMaximum() == true && sharedVariables.andreysLayout == 2)
    return true;

    if(sharedVariables.andreysLayout == 1)
    return true;
    
    return false;

   }
    void makeAndreysLayout() {

      /* int myOverallWidth = getControlLength();  //width of this panel
      int myOverallHeight = getControlHeight();
      setAndreyFontSize(n);  pass in int n with font size ( effects game board font now not clock

     if(myOverallWidth < 250) // default minimum width is 235
      setAndreyFontSize(12);
     else
      setAndreyFontSize(28);
      */

      int andreySpace = 5;
      double[][] andreySize = {{150, TableLayout.FILL, 80},
                               {20,  40, andreySpace, 20, TableLayout.FILL,
                                30, 30, andreySpace, 40, 20}};
     
     //JFrame framer = new JFrame("" + getBoardWidth() + " height " + getBoardHeight() + "  control length " + getControlLength() + " control height " + getControlHeight());
     //framer.setVisible(true);
     //framer.setSize(300,100);
      setLayout(new TableLayout(andreySize));
      // our 4 move buttons
      andreyNavig = new JPanel();
     double[][] buttonSizes = {{TableLayout.FILL, 55, 3, 55, 3,  55, 3,  55, TableLayout.FILL}, {25}};
     andreyNavig.setLayout(new TableLayout(buttonSizes));
     add(andreyNavig, " 0,5,2,5");
     andreyNavig.add(backwardEnd, "1,0, f, b");
     andreyNavig.add(backward, "3,0, f, b");
     andreyNavig.add(forward, "5,0, f, b");
     andreyNavig.add(forwardEnd, "7,0, f, b");



      /*
      add(andreyNavig, "0, 5, 2, 5");
      andreyNavig.add(backwardEnd);
      andreyNavig.add(backward);
      andreyNavig.add(forward);
      andreyNavig.add(forwardEnd);
     */


      // these are visible when playing
      andreyAct = new JPanel();
      double[][] actionSizes = {{TableLayout.FILL, 75, 3, 75, 3,  75, TableLayout.FILL}, {25}};
      andreyAct.setLayout(new TableLayout(actionSizes));
      add(andreyAct, "0, 6, 2, 6");
      andreyAct.add(abortButton, "1,0, f, b");
      andreyAct.add(drawButton, "3,0, f, b");
      andreyAct.add(resignButton, "5,0, f, b");
      // end action buttons

      add(topClockDisplay, "0, 1, 1, 1"); // a Jlabel, clock at top of board
      add(flagTop, "2, 0, 2, 1");  // a JLabel flag at top of board

      add(topNameDisplay, "0, 0, 2, 0");  // a JLabel, name at top of board

      add(gameListingDisplay, "0, 3, 2, 3"); // a JLabel "3 0 Blitz " for example
      add(sharedVariables.moveSliders[gameData.BoardIndex], "2, 4, r, f");// the move slider

      add(botNameDisplay, "0, 9, 2, 9"); // a JLabel name at bottm
      add(botClockDisplay, "0, 8, 1, 8");// a JLabel bottom clock
      add(flagBottom, "2, 8, 2, 9"); // JLable the flag at bottom
      add(listScroller, "0, 4");     // the move list

    }

    void makeLayout() {
      actionPanel= new JPanel();
        buttonPanel = new JPanel();
      buttonPanel.setBackground(sharedVariables.boardBackgroundColor);
     buttonPanelFlow = new JPanel();

      if (isAndreyLayout() == false) {

        buttonPanel.add(buttonPanelFlow);

        GroupLayout buttonLayout = new GroupLayout(buttonPanelFlow);
        //GroupLayout layout = new GroupLayout(this);
        buttonPanelFlow.setLayout(buttonLayout);
        ParallelGroup hGroup = buttonLayout.createParallelGroup();

        SequentialGroup h1 = buttonLayout.createSequentialGroup();
	h1.addComponent( backwardEnd, 20, GroupLayout.DEFAULT_SIZE,60);
        h1.addComponent(backward, 20, GroupLayout.DEFAULT_SIZE,60);
  	h1.addComponent(forward,  20, GroupLayout.DEFAULT_SIZE,60);
        h1.addComponent(forwardEnd,  20, GroupLayout.DEFAULT_SIZE,60);

	hGroup.addGroup(GroupLayout.Alignment.LEADING, h1);// was trailing
	//Create the horizontal group
	buttonLayout.setHorizontalGroup(hGroup);
	ParallelGroup v1 =
          buttonLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        // was leading
        v1.addComponent(backwardEnd);
        v1.addComponent(backward);
        v1.addComponent(forward);
        v1.addComponent(forwardEnd);

	buttonLayout.setVerticalGroup(v1);
      }// if not andreys layout
     actionPanelFlow = new JPanel();

      actionPanel.add(actionPanelFlow);

      //JPanel moveListPanel = new JPanel();
      //moveListPanel.add(listScroller);
      //moveListPanel.add(sharedVariables.moveSliders[gameData.BoardIndex]);


      GroupLayout buttonLayout = new GroupLayout(actionPanelFlow);
      //GroupLayout layout = new GroupLayout(this);
      actionPanelFlow.setLayout(buttonLayout);
      ParallelGroup hbGroup = buttonLayout.createParallelGroup();

      SequentialGroup h1b = buttonLayout.createSequentialGroup();
      h1b.addComponent( abortButton, 20, GroupLayout.DEFAULT_SIZE,80);
      h1b.addComponent(drawButton, 20, GroupLayout.DEFAULT_SIZE,80);
      h1b.addComponent(resignButton, 20, GroupLayout.DEFAULT_SIZE,80);

      hbGroup.addGroup(GroupLayout.Alignment.LEADING, h1b);// was trailing
      //Create the horizontal group
      buttonLayout.setHorizontalGroup(hbGroup);
      ParallelGroup vb1 =
        buttonLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
      // was leading
      vb1.addComponent(abortButton);
      vb1.addComponent(drawButton);
      vb1.addComponent(resignButton);

      buttonLayout.setVerticalGroup(vb1);

      /*topClock = new JPanel();
        topClock.setLayout(new GridLayout(2,1));
        bottomClock = new JPanel();
        bottomClock.setLayout(new GridLayout(2,1));

        sliderArrows = new JPanel();
        sliderArrows.setLayout(new GridLayout(3,1));
        sliderArrows.add(sharedVariables.moveSliders[gameData.BoardIndex]);
        sliderArrows.add(buttonPanel);
        sliderArrows.add(actionPanel);
      */
      //bottomClock.add(botNameDisplay);
      //bottomClock.add(botClockDisplay);
      //topClock.add(topClockDisplay);
      //topClock.add(topNameDisplay);

      //setLayout(new GridLayout(5,1));

      /*add(topClock);
        add(gameListingDisplay);

        add(listScroller);

        add(sliderArrows);

        add(bottomClock);
      */

      GroupLayout layout = new GroupLayout(this);
      setLayout(layout);

      //Create a parallel group for the horizontal axis
      ParallelGroup hGroup =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      ParallelGroup h1 =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      SequentialGroup hflagtop = layout.createSequentialGroup();
      SequentialGroup hflagbottom = layout.createSequentialGroup();
      ParallelGroup vMoveGroup = layout.createParallelGroup();

      SequentialGroup hMoveGroup = layout.createSequentialGroup();


      int num= Short.MAX_VALUE;
      int flagnum=105;

      hflagtop.addComponent(topClockDisplay, 0, GroupLayout.DEFAULT_SIZE, num);
      hflagtop.addComponent(flagTop, flagnum, GroupLayout.DEFAULT_SIZE, flagnum);
      h1.addGroup(hflagtop);
      h1.addComponent(topNameDisplay, 0, GroupLayout.DEFAULT_SIZE, num);

      h1.addComponent(gameListingDisplay, 0, GroupLayout.DEFAULT_SIZE, num);
      //h1.addComponent(moveListPanel, 0, GroupLayout.DEFAULT_SIZE, num);
      hMoveGroup.addComponent(listScroller, 0, GroupLayout.DEFAULT_SIZE, num);

      hMoveGroup.addComponent(sharedVariables.moveSliders[gameData.BoardIndex],
                              25, GroupLayout.DEFAULT_SIZE,num);

      h1.addGroup(hMoveGroup);
      ParallelGroup buttonGroup =
        layout.createParallelGroup(GroupLayout.Alignment.CENTER, true);
      buttonGroup.addComponent(buttonPanel, 0, GroupLayout.DEFAULT_SIZE, num);
      h1.addGroup(GroupLayout.Alignment.CENTER, buttonGroup);
      h1.addComponent(actionPanel, 0, GroupLayout.DEFAULT_SIZE,  num);

      h1.addComponent(botNameDisplay, 0, GroupLayout.DEFAULT_SIZE, num);
      hflagbottom.addComponent(botClockDisplay, 0, GroupLayout.DEFAULT_SIZE, num);
      hflagbottom.addComponent(flagBottom, 0, GroupLayout.DEFAULT_SIZE, flagnum);
      h1.addGroup(hflagbottom);

      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
      //Create the horizontal group
      layout.setHorizontalGroup(hGroup);

      //Create a parallel group for the vertical axis
      ParallelGroup vGroup =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      // was leading

      ParallelGroup v4 =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);

      ParallelGroup vflagtop =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      ParallelGroup vflagbottom =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);

      SequentialGroup v1 = layout.createSequentialGroup();

      num=175;
      int num2=20;
      int num3=60;
      int num4 = 60;
      int num5 = 45;
      int num6 = 24;
      vflagtop.addComponent(topClockDisplay, num3, GroupLayout.DEFAULT_SIZE, num);
      vflagtop.addComponent(flagTop, num4, GroupLayout.DEFAULT_SIZE, num);
      v1.addGroup(vflagtop);

      v1.addComponent(topNameDisplay, num2, GroupLayout.DEFAULT_SIZE, num);
      v1.addComponent(gameListingDisplay, num6, GroupLayout.DEFAULT_SIZE, num);

      //v1.addComponent(moveListPanel, 0, GroupLayout.DEFAULT_SIZE, num);

      vMoveGroup.addComponent(listScroller, 0, GroupLayout.DEFAULT_SIZE, num);

      vMoveGroup.addComponent(sharedVariables.moveSliders[gameData.BoardIndex],
                              num2, GroupLayout.DEFAULT_SIZE,  num);
      v1.addGroup(vMoveGroup);
      v1.addComponent(buttonPanel, num5, GroupLayout.DEFAULT_SIZE, num);
      v1.addComponent(actionPanel, num5, GroupLayout.DEFAULT_SIZE,  num);

      v1.addComponent(botNameDisplay, num2, GroupLayout.DEFAULT_SIZE, num);

      vflagbottom.addComponent(botClockDisplay, num3, GroupLayout.DEFAULT_SIZE, num);
      vflagbottom.addComponent(flagBottom, num4, GroupLayout.DEFAULT_SIZE, num);
      v1.addGroup(vflagbottom);

      v4.addGroup(v1);

      vGroup.addGroup(v4);

      layout.setVerticalGroup(vGroup);
    }

    
    void sendAction(String action) {
      String primary  = "primary " + sharedVariables.mygame
        [sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber + "\n";
      if (sharedVariables.mygame
          [sharedVariables.gamelooking[gameData.BoardIndex]].state ==
          sharedVariables.STATE_OVER)
        primary = "";

      sendCommand(primary + action + "\n");
    }


    void adjustMoveList() {
      final int aaa = gameData.BoardIndex;
      SwingUtilities.invokeLater(new Runnable() {
          @Override
            public void run() {
            try {

              int index = sharedVariables.moveSliders[gameData.BoardIndex].getValue();
              int row =(int) index/2;
              int column=0;
              if (index%2 == 1)
                row++;
              if (index%2 == 1)
                column = 1;
              else
                column = 2;
              if (index == 0)
                column=0;
              sharedVariables.gametable[aaa].scrollRectToVisible
                (sharedVariables.gametable[aaa].getCellRect(row, column, true));

            } catch (Exception e1) {
              //ignore
            }
          }
        });

      //readLock.lock();


      //readLock.unlock();

    }// end method adjust move list

    void sendCommand(String command) {
      myoutput output = new myoutput();
      output.data=command;
      output.consoleNumber=0;
      output.game=1;
      queue.add(output);
    }

    void setClockBackgrounds() {// set clock background depending on turn
      int go=0;
      if (sharedVariables.mygame[gameData.LookingAt].state ==
          sharedVariables.STATE_PLAYING)
        go=1;
      if (sharedVariables.mygame[gameData.LookingAt].state ==
          sharedVariables.STATE_EXAMINING)
        go=1;
      if (sharedVariables.mygame[gameData.LookingAt].state ==
          sharedVariables.STATE_OBSERVING)
        go=1;

      if (go == 1) {
        
        Color boardForegroundColor = sharedVariables.boardForegroundColor;
        Color clockForegroundColor = sharedVariables.clockForegroundColor;
   if(sharedVariables.mygame[gameData.LookingAt].lowTime == true) {

        if(sharedVariables.mygame[gameData.LookingAt].iflipped == 0) { // white at top
         if(sharedVariables.mygame[gameData.LookingAt].wtime < sharedVariables.mygame[gameData.LookingAt].btime) {
          topClockDisplay.setBackground(sharedVariables.clockHigh);
           botClockDisplay.setBackground(sharedVariables.clockLow);
         } else {
          topClockDisplay.setBackground(sharedVariables.clockLow);
           botClockDisplay.setBackground(sharedVariables.clockHigh);
         }
      } else {
         if(sharedVariables.mygame[gameData.LookingAt].wtime < sharedVariables.mygame[gameData.LookingAt].btime) {
           topClockDisplay.setBackground(sharedVariables.clockLow);
           botClockDisplay.setBackground(sharedVariables.clockHigh);
         } else {
           topClockDisplay.setBackground(sharedVariables.clockHigh);
           botClockDisplay.setBackground(sharedVariables.clockLow);
         }
      }

        boardForegroundColor = sharedVariables.timeForeground;
        clockForegroundColor = sharedVariables.onmoveTimeForeground;
      }



        if (sharedVariables.mygame[gameData.LookingAt].iflipped==0) {
          if (sharedVariables.mygame[gameData.LookingAt].movetop%2==0) {
                   if(sharedVariables.mygame[gameData.LookingAt].lowTime == false) {
            topClockDisplay.setBackground(sharedVariables.boardBackgroundColor);
            botClockDisplay.setBackground(sharedVariables.onMoveBoardBackgroundColor);
           
           }
            topClockDisplay.setForeground(boardForegroundColor);
            botClockDisplay.setForeground(clockForegroundColor);

          } else {
                  if(sharedVariables.mygame[gameData.LookingAt].lowTime == false) {
            topClockDisplay.setBackground(sharedVariables.onMoveBoardBackgroundColor);
            botClockDisplay.setBackground(sharedVariables.boardBackgroundColor);
                  }
            topClockDisplay.setForeground(clockForegroundColor);
            botClockDisplay.setForeground(boardForegroundColor);
          }
        } else {// i flipped
          if (sharedVariables.mygame[gameData.LookingAt].movetop%2==0) {
                  if(sharedVariables.mygame[gameData.LookingAt].lowTime == false)
                  {
            topClockDisplay.setBackground(sharedVariables.onMoveBoardBackgroundColor);
            botClockDisplay.setBackground(sharedVariables.boardBackgroundColor);
                  }
            topClockDisplay.setForeground(clockForegroundColor);
            botClockDisplay.setForeground(boardForegroundColor);

          } else {
                  if(sharedVariables.mygame[gameData.LookingAt].lowTime == false) {

            topClockDisplay.setBackground(sharedVariables.boardBackgroundColor);
            botClockDisplay.setBackground(sharedVariables.onMoveBoardBackgroundColor);
                  }

            topClockDisplay.setForeground(boardForegroundColor);
            botClockDisplay.setForeground(clockForegroundColor);

          }
        }// end else iflipped condition

      }// end if playing
      else {
        topClockDisplay.setBackground(sharedVariables.boardBackgroundColor);
        botClockDisplay.setBackground(sharedVariables.boardBackgroundColor);
        topClockDisplay.setForeground(sharedVariables.boardForegroundColor);
        botClockDisplay.setForeground(sharedVariables.boardForegroundColor);
      }

    }// end method

    /*************** set last move code *********************************/
    void setLastMove() {
      if (sharedVariables.mygame[gameData.LookingAt].movetop < 1) {
        lastMove.setText(" ");
        return;
      }

      try {

	int moveNumber = sharedVariables.mygame[gameData.LookingAt].movetop;
	if (sharedVariables.moveSliders[gameData.BoardIndex].getValue() <
            sharedVariables.moveSliders[gameData.BoardIndex].getMaximum())
          moveNumber=sharedVariables.moveSliders[gameData.BoardIndex].getValue();

	int from =
          sharedVariables.mygame[gameData.LookingAt].moveListFrom[moveNumber-1];
	int to = sharedVariables.mygame[gameData.LookingAt].moveListTo[moveNumber-1];
	String text = "";
	if (moveNumber %2 == 1) {// whites move
          text += "" + (int) (moveNumber/2 + 1) + ") ";
	} else {// blacks move
          text += ".." + (int) (moveNumber/2 ) + ") ";
	}
	text+=getMoveInNotation(from);
	text+=getMoveInNotation(to);
	lastMove.setText(text);

      } catch(Exception z) { lastMove.setText("???");}

    }// end method setLastMove

    String getMoveInNotation(int from) {// works for from and to

      try {
	if (from == -1)
          return "P@";
	if (from == -2)
          return "N@";
	if (from == -3)
          return "B@";
	if (from == -4)
          return "R@";
	if (from == -5)
          return "Q@";
	if (from == -7)
          return "p@";
	if (from == -8)
          return "n@";
	if (from == -9)
          return "b@";
	if (from == -10)
          return "r@";
	if (from == -11)
          return "q@";
        if (sharedVariables.mygame[gameData.LookingAt].iflipped == 1)
          from = 63-from;

        String col = "";
        int row = (int) (from / 8);
        row++;

        if (from%8 == 7)
          col = "a";
        if (from%8 == 6)
          col = "b";
        if (from%8 == 5)
          col = "c";
        if (from%8 == 4)
          col = "d";
        if (from%8 == 3)
          col = "e";
        if (from%8 == 2)
          col = "f";
        if (from%8 == 1)
          col = "g";
        if (from%8 == 0)
          col = "h";

        return col + row;

      } catch(Exception d) {}

      return "??";
    }

    /*************************** end set last move code ******************/
  }// end controls panel class

