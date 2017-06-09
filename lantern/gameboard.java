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
import java.util.Calendar;
import java.math.BigInteger;

import layout.TableLayout;

//class gameboard extends JFrame implements ComponentListener,
//WindowListener
class gameboard extends JInternalFrame  implements InternalFrameListener, ComponentListener
{
  /*
  void setSelected(boolean home) {
    return;
  }
  boolean isSelected() {
    return false;
  }
  */
  Image [] img;
  int controlLength = 235;
  channels sharedVariables;
  overallpanel overall;

  //FileWriter fstream;
  //BufferedWriter out;
  long myspeed;
  gameboardPanel mypanel;
  gameboardConsolePanel myconsolepanel;
  gameboardControlsPanel mycontrolspanel;

  Timer timer;
  pgnWriter pgnGetter;

  Timer autotimer;

  Timer generalTimer;
  gameboard passboard=this;
  ConcurrentLinkedQueue<myoutput> queue;

  ConcurrentLinkedQueue<newBoardData> gamequeue;
  JTextPane [] gameconsoles;
  JTextPane [] consoles;
  subframe [] consoleSubframes;
  gamestuff gameData;
  resourceClass graphics;
  docWriter myDocWriter;
  gameboardTop topGame;
  int oldDif=0;

 public void timerSafeCancel()
 {
      if(timer != null)
      {
        try {
          timer.cancel();
        }
        catch(Exception e) { }
      }
 }

  public boolean superIsVisible() {
    return super.isVisible();
  }

  public void setSelected(boolean type) {
    try {
      if (sharedVariables == null || topGame == null) {
        super.setSelected(type);
        return;
      }
      if (sharedVariables.useTopGames == true)
        return;
      else
        super.setSelected(type);
    }
    catch(Exception dui){  }
  }
  
  public void setTitle(String type) {
    try {
    int d = gameData.BoardIndex + 1;
    type = "G" + d + ": " + type;

      if (sharedVariables == null || topGame == null) {
        super.setTitle(type);
        return;
      }
      if (sharedVariables.useTopGames == true)
        topGame.setTitle(type);
      else
        super.setTitle(type);
    }
    catch(Exception dui){}
  }

  public boolean isVisible() {
    try {
      if (sharedVariables == null || topGame == null) {
        return super.isVisible();
      }
      if (sharedVariables.useTopGames == true)
        return topGame.isVisible();

      return super.isVisible();
    }
    catch(Exception dummy){}
    return super.isVisible();
  }

  public void repaintCustom() {
    try {
      if (sharedVariables == null || topGame == null) {
        repaint();
        return;
      }
      if (sharedVariables.useTopGames == true)
        topGame.repaint();
      else
        repaint();
    }
    catch(Exception dummy){}
  }

  gameboard(JTextPane consoles1[], subframe consoleSubframes1[],
            JTextPane gameconsoles1[],
            ConcurrentLinkedQueue<newBoardData> gamequeue1,
            int boardNumber, Image img1[],
            ConcurrentLinkedQueue<myoutput> queue1,
            channels sharedVariables1, resourceClass graphics1,
            docWriter myDocWriter1)
  {
    super("Game Board" + (boardNumber),
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable

    try {
      // Create file
      // fstream = new FileWriter("\\multiframe\\out.txt");
      //  out = new BufferedWriter(fstream);

      myDocWriter=myDocWriter1;
      gameconsoles=gameconsoles1;
      //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      img=img1;
      pgnGetter = new pgnWriter();
      consoles=consoles1;
      consoleSubframes=consoleSubframes1;

      gamequeue=gamequeue1;
      graphics=graphics1;
      queue=queue1;

      sharedVariables=sharedVariables1;

      gameData= new gamestuff();
      gameData.BoardIndex = boardNumber;
      gameData.LookingAt=boardNumber;

      sharedVariables.gamelooking[gameData.BoardIndex]=gameData.LookingAt;


      if(sharedVariables.mygame[gameData.BoardIndex] == null)
        sharedVariables.mygame[gameData.BoardIndex] =
          new gamestate(sharedVariables.excludedPiecesWhite, sharedVariables.excludedPiecesBlack,sharedVariables.excludedBoards);

      //writeout("going to create overall\n");
      myconsolepanel =
        new gameboardConsolePanel(topGame, consoles, consoleSubframes,
                                  sharedVariables, gameData, gameconsoles,
                                  gamequeue, queue,myDocWriter, mycontrolspanel, mypanel);
      topGame= new gameboardTop(sharedVariables, myconsolepanel,
                                queue, gameData);
      myconsolepanel.topGame=topGame;

      if(sharedVariables.useTopGames == true) {
        overall = new overallpanel(true, passboard);
        myconsolepanel.mycontrolspanel=mycontrolspanel;
        myconsolepanel.mypanel=mypanel;
        topGame.add(overall);
        //topGame.setVisible(true);
        //topGame.setSize(300,300);
        //setVisible(true);
      } else {
        topGame.setVisible(false);
        overall = new overallpanel(true, passboard);
          myconsolepanel.mycontrolspanel=mycontrolspanel;
        myconsolepanel.mypanel=mypanel;

        add(overall);
        addComponentListener(this);
        //addWindowListener(this);

        addInternalFrameListener(this);
      }
      //setAlwaysOnTop(true);
      //out.close();
    }
    catch (Exception e){//Catch exception if any

    }
}

  // called when they want to resize the game console or make it hidden
  void recreate() {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
          public void run() {
          try {
            if(sharedVariables.useTopGames == true) {
              topGame.getContentPane().removeAll();

              overall = new overallpanel(false, passboard);

              topGame.add(overall);
              topGame.setVisible(true);
            } else {
              getContentPane().removeAll();

              overall = new overallpanel(false, passboard);
              add(overall);
              setVisible(true);
            }
          } catch (Exception e1) {
            //ignore
          }
        }
      });
    
  }

  void switchFrame(final boolean top) {
    
    SwingUtilities.invokeLater(new Runnable() {
        @Override
          public void run() {
          try {
            if(top == true) {
              setVisible(false);

              getContentPane().removeAll();
              //topGame.getContentPane().removeAll();

              try {
                // setSize(10,10);
                // setLocation(500, 5000);
              }
              catch(Exception badff){}

              overall= new overallpanel(passboard);
              myconsolepanel.removeAll();
              myconsolepanel =
                new gameboardConsolePanel(topGame, consoles,
                                          consoleSubframes, sharedVariables,
                                          gameData, gameconsoles, gamequeue,
                                          queue,myDocWriter, mycontrolspanel, mypanel);
              topGame.myconsolepanel=myconsolepanel;

              overall.overallSwitch();
              topGame.add(overall);

              topGame.setVisible(true);

              repaintCustom();
              topGame.setVisible(false);
              setVisible(false);
              topGame.setVisible(true);
            } else {
              topGame.getContentPane().removeAll();
              
              myconsolepanel.removeAll();

              myconsolepanel =
                new gameboardConsolePanel(topGame, consoles,
                                          consoleSubframes, sharedVariables,
                                          gameData, gameconsoles, gamequeue,
                                          queue,myDocWriter, mycontrolspanel, mypanel);
              topGame.myconsolepanel=myconsolepanel;

              overall = new overallpanel(passboard);
              overall.overallSwitch();
              add(overall);
              topGame.setVisible(false);
              setVisible(true);
              repaintCustom();
            }
          } catch (Exception e1) {
            //ignore
          }
        }
      });
  }
  
  int getBoardWidth() {
    return getWidth();
  }
  
  int getBoardHeight() {
    return getHeight();
  }
    int getControlHeight()
    {
     if(sharedVariables.sideways == true)
     return getBoardHeight();
     
     return getBoardHeight() - getConsoleHeight();
    }

      int getControlLength()
    {
      int width = getBoardWidth();
      int height = getBoardHeight();
      if (sharedVariables.sideways==false)
      height = height - getConsoleHeight();

      controlLength = 235;
     int dif = 0;
     if (sharedVariables.sideways==false)
      dif = width - controlLength;
     else
      dif = width - controlLength - getConsoleWidth();
      //JFrame framer = new JFrame(" width is " + width + " heigth is " +
      //                           height + " dif is " + dif +
      //                           " and controlLength is " + controlLength);
      //framer.setSize(200,100);
      //framer.setVisible(true);

      if (dif > height && mycontrolspanel.isAndreyLayout() == false)
      {  dif = (int) (dif - height) / 2;
         controlLength+=dif;
        }
   else if (dif > height && mycontrolspanel.isAndreyLayout() == true){
        dif = (int) (dif - height);
        controlLength+=dif;
        }

      return controlLength;
    }
    
    int getConsoleWidth()
    {
      
     return (int)(sharedVariables.boardConsoleSizes
              [sharedVariables.boardConsoleType] * 1.8);
    }

    int getConsoleHeight()
    {
     return sharedVariables.boardConsoleSizes[sharedVariables.boardConsoleType];

    }

  // class overall is  the overall  gameboard panel
  //  its strictly to provide a layout for the 3 panels that the
  // gameboard uses the 64 square board area, gameboardPanel, the
  // console and tabs, gameboardConsolePanel, and the controls like
  // clock , lables for names etc ratings. gameboardControlsPanel
  class overallpanel extends JPanel {

    public void paintComponent(Graphics g) {

      try {

        super.paintComponent(g);

        setBackground(sharedVariables.boardBackgroundColor);
      }// end try
      catch(Exception dui){}
    }//end paint components

    overallpanel(gameboard theBoard) {}

    overallpanel(boolean firstTime, gameboard theBoard) {
      if (firstTime == true) {
        mypanel= new gameboardPanel(img, sharedVariables, gameData,
                                    queue, graphics);

	// game board console panel moved up
	mycontrolspanel= new gameboardControlsPanel(theBoard, gameData, sharedVariables, queue);
      }	else {
        myconsolepanel.removeAll();
        if (sharedVariables.sideways==true)
          myconsolepanel.setVerticalLayout();
        else
          myconsolepanel.setHorizontalLayout();

            mycontrolspanel.removeAll();
          if(mycontrolspanel.isAndreyLayout() == true)
            mycontrolspanel.makeAndreysLayout();
         else
            mycontrolspanel.makeLayout();

      }
      if (sharedVariables.boardConsoleType == 0) {
        // make console components invisible
        myconsolepanel.mainConsoleTab.setVisible(false);
        myconsolepanel.prefixHandler.setVisible(false);
        myconsolepanel.Input.setVisible(false);
        myconsolepanel.jScrollPane1.setVisible(false);
      }	else {// make visible in case they were invisible
        myconsolepanel.mainConsoleTab.setVisible(true);
        myconsolepanel.prefixHandler.setVisible(true);
        myconsolepanel.Input.setVisible(true);
        myconsolepanel.jScrollPane1.setVisible(true);
      }

      if (sharedVariables.sideways==false)
	setOverallVertical();
      else
	setOverallHorizontal();

      return;
    }

    void overallSwitch() {

      if (sharedVariables.sideways==false)
	setOverallVertical();
      else
	setOverallHorizontal();

      return;
    }

    void setOverallVertical() {
      GroupLayout layout;
      if (sharedVariables.useTopGames == true) {
        layout = new GroupLayout(topGame.getContentPane());
        //layout = new GroupLayout(getContentPane());
        topGame.getContentPane().setLayout(layout);
        //getContentPane().setLayout(layout);
      } else {
        layout = new GroupLayout(getContentPane());
        //layout = new GroupLayout(this);
        getContentPane().setLayout(layout);
      }
      controlLength=getControlLength();

      //Create a parallel group for the horizontal axis
      ParallelGroup hGroup =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      ParallelGroup h1 =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);

      SequentialGroup h2 = layout.createSequentialGroup();
      SequentialGroup h3 = layout.createSequentialGroup();

      h2.addComponent(mypanel);

      h2.addComponent(mycontrolspanel, controlLength,
                      controlLength, controlLength);

      h3.addComponent(myconsolepanel);

      h1.addGroup(h2);
      
      h1.addGroup(h3);

      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
      //Create the horizontal group
      layout.setHorizontalGroup(hGroup);

      //Create a parallel group for the vertical axis
      ParallelGroup vGroup =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      // was leading

      ParallelGroup v4 =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);

      SequentialGroup v1 = layout.createSequentialGroup();

      SequentialGroup v2 = layout.createSequentialGroup();

      v1.addComponent(mypanel, 0, 300, Short.MAX_VALUE);
      int consolePanelDefault = getConsoleHeight();

      v1.addComponent(myconsolepanel, consolePanelDefault,
                      consolePanelDefault, consolePanelDefault);
      v2.addComponent(mycontrolspanel, 0, 300, Short.MAX_VALUE);

      v2.addComponent(myconsolepanel, consolePanelDefault,
                      consolePanelDefault, consolePanelDefault);

      v4.addGroup(v1);
      
      v4.addGroup(v2);

      vGroup.addGroup(v4);

      layout.setVerticalGroup(vGroup);
}


    void setOverallHorizontal() {

      GroupLayout layout;
      if (sharedVariables.useTopGames == true) {
        layout = new GroupLayout(topGame.getContentPane());
        //layout = new GroupLayout(getContentPane());
        topGame.getContentPane().setLayout(layout);
        //getContentPane().setLayout(layout);
      } else {
        layout = new GroupLayout(getContentPane());
        //layout = new GroupLayout(this);
        getContentPane().setLayout(layout);
      }
          controlLength=getControlLength();


      //Create a parallel group for the horizontal axis
      ParallelGroup hGroup =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      ParallelGroup h1 =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);

      SequentialGroup h2 = layout.createSequentialGroup();
      SequentialGroup h3 = layout.createSequentialGroup();

      int consolePanelDefault=getConsoleWidth();

      h2.addComponent(myconsolepanel, consolePanelDefault,
                      consolePanelDefault, consolePanelDefault);

      h2.addComponent(mypanel);
      h2.addComponent(mycontrolspanel, controlLength,  controlLength, controlLength);

      h1.addGroup(h2);

      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
      //Create the horizontal group
      layout.setHorizontalGroup(hGroup);

      //Create a parallel group for the vertical axis
      ParallelGroup vGroup =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      // was leading

      ParallelGroup v4 =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);

      SequentialGroup v1 = layout.createSequentialGroup();

      SequentialGroup v2 = layout.createSequentialGroup();

      v4.addComponent(myconsolepanel);

      v4.addComponent(mypanel);
      v4.addComponent(mycontrolspanel);
      vGroup.addGroup(v4);

      layout.setVerticalGroup(vGroup);

    }

  }// end class overall

  // lives as variable on frame now

  void startEngine() {

    runningengine engine1;
    engine1= new runningengine(sharedVariables, gameData.BoardIndex,
                               gameconsoles, gameData);
    Thread t = new Thread(engine1);
    t.start();
    sharedVariables.mygame[gameData.LookingAt].clickCount=0;
    myoutput data = new myoutput();
    data.startengine=1;
    queue.add(data);
  }

  int getGameNumber(String icsGameNumber) {
    try {
      return Integer.parseInt(icsGameNumber);
    } catch(Exception e) {}
    return sharedVariables.NOT_FOUND_NUMBER;
  }

  void initialPositionSent(String icsGameNumber, String fen) {
    int tempnumber=getGameNumber(icsGameNumber);
    if(tempnumber ==
       sharedVariables.mygame[gameData.BoardIndex].myGameNumber) {
      sharedVariables.mygame[gameData.BoardIndex].readInitialPosition(fen);

      try {
        sharedVariables.mygame[gameData.BoardIndex].clearShapes();
        sharedVariables.mygame[gameData.BoardIndex].movetop = 0;
        sharedVariables.mygame[gameData.BoardIndex].turn=0;
        for(int a=0; a<sharedVariables.maxGameTabs; a++) {
          if(sharedVariables.gamelooking[a]==gameData.BoardIndex &&
             sharedVariables.gamelooking[a]!=-1 &&
             sharedVariables.moveSliders[a]!=null) {
            sharedVariables.moveSliders[a].setMaximum
              (sharedVariables.mygame[gameData.BoardIndex].turn);
            sharedVariables.moveSliders[a].setValue
              (sharedVariables.moveSliders[a].getMaximum());
          }// end if
        }// end for
        resetMoveList();
        
       if ((sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_EXAMINING || sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_OBSERVING)&&
          sharedVariables.engineOn == true && sharedVariables.engineBoard == gameData.BoardIndex) {
            if (sharedVariables.uci)
              sendUciMoves();
        }// end if engine on true
       

      }// end try
      catch(Exception e) {}
    }
  }

  void refreshSent(String icsGameNumber) {

  }

  void flipSent(String icsGameNumber, String flip) {
    int tempnumber=getGameNumber(icsGameNumber);

    if (tempnumber ==
        sharedVariables.mygame[gameData.BoardIndex].myGameNumber) {

      if (flip.equals("1")) {// i'm black or black at bottom

        if(sharedVariables.mygame[gameData.BoardIndex].iflipped == 0) {

          sharedVariables.mygame[gameData.BoardIndex].iflipped = 1;
          redrawFlags();
          sharedVariables.mygame[gameData.BoardIndex].doFlip();
          sharedVariables.mygame[gameData.BoardIndex].flipMoves();
        }
      }	else {
        if (sharedVariables.mygame[gameData.BoardIndex].iflipped == 1) {
          sharedVariables.mygame[gameData.BoardIndex].iflipped = 0;
          redrawFlags();
          sharedVariables.mygame[gameData.BoardIndex].doFlip();
          sharedVariables.mygame[gameData.BoardIndex].flipMoves();
        }
      }
    }
  }

  void fenSent(String icsGameNumber, String fen) {
    int tempnumber=getGameNumber(icsGameNumber);
    //JFrame framer = new JFrame("fen: " + fen);
    //framer.setSize(500,100);
    //framer.setVisible(true);
      // check if black on the move
      try {
      int i = fen.indexOf(" ");
      if(i > -1)
      {

        if(fen.substring(i+1, i+2).equals("b") && sharedVariables.mygame[gameData.BoardIndex].wild == 20 && 
        sharedVariables.mygame[gameData.BoardIndex].movetop == 0)
        {  moveSent(icsGameNumber, "e2e2",
                "-", false);
        sharedVariables.mygame[gameData.BoardIndex].currentLastto = 0;
        sharedVariables.mygame[gameData.BoardIndex].currentLastfrom = 0;
    // JFrame framer = new JFrame("fen: " + fen);
    //framer.setSize(500,100);
    //framer.setVisible(true);

       }
        } // if i > -1
        if(sharedVariables.mygame[gameData.BoardIndex].state !=
          sharedVariables.STATE_OBSERVING && !sharedVariables.mygame[gameData.BoardIndex].engineFen.equals("*")) {
        sharedVariables.mygame[gameData.BoardIndex].engineFen = fen;
        }
       }
       catch(Exception e){}

    return; // currently parsing initial postions not fens
    /*
    if(tempnumber ==
       sharedVariables.mygame[gameData.BoardIndex].myGameNumber) {
      sharedVariables.mygame[gameData.BoardIndex].readFen(fen);
    }
    */
  }

  void gameStartedFics(String icsGameNumber) {
    ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    Lock readLock = rwl.readLock();
    readLock.lock();
    sharedVariables.mygame[gameData.BoardIndex] =
      new gamestate(sharedVariables.excludedPiecesWhite, sharedVariables.excludedPiecesBlack,sharedVariables.excludedBoards);
    readLock.unlock();

    sharedVariables.mygame[gameData.BoardIndex].myGameNumber =
      getGameNumber(icsGameNumber);
    mypanel.editable=0;
    sharedVariables.mygame[gameData.BoardIndex].iflipped=0;
    if (isVisible() == true)
      mypanel.repaint();
    sharedVariables.mygame[gameData.BoardIndex].turn=0;
    timer = new Timer (  ) ;
    timer.scheduleAtFixedRate( new ToDoTask (  ) , 100 ,100) ;
}

  void initialFicsInfo(String icsGameNumber, String WN, String BN,
                       String gameType, String played, String plies,
                       String myturn) {
    int tempnumber=getGameNumber(icsGameNumber);
    if (tempnumber ==
        sharedVariables.mygame[gameData.BoardIndex].myGameNumber) {
      // set ply always
      try {
        sharedVariables.mygame[gameData.BoardIndex].movetop =
          Integer.parseInt(plies);
        sharedVariables.mygame[gameData.BoardIndex].turn =
          Integer.parseInt(plies);
        for(int a=0; a<sharedVariables.maxGameTabs; a++) {
          if(sharedVariables.gamelooking[a]==gameData.BoardIndex &&
             sharedVariables.gamelooking[a]!=-1 &&
             sharedVariables.moveSliders[a]!=null) {
            sharedVariables.moveSliders[a].setMaximum
              (sharedVariables.mygame[gameData.BoardIndex].turn);
            sharedVariables.moveSliders[a].setValue
              (sharedVariables.moveSliders[a].getMaximum());
          }// end if
        }// end for
      }// end try
      catch(Exception e) {}

      if (sharedVariables.mygame[gameData.BoardIndex].ficsSet == 1)
        return;
      else
        sharedVariables.mygame[gameData.BoardIndex].ficsSet = 1;

      sharedVariables.mygame[gameData.BoardIndex].name1 = WN;
      // + " " + white_titles + " " + white_rating;
      sharedVariables.mygame[gameData.BoardIndex].name2 = BN;
      // + " " + black_titles + " " + black_rating;
      timer = new Timer (  ) ;
      timer.scheduleAtFixedRate( new ToDoTask (  ) , 300 ,300) ;
      if (gameType.equals("2"))
        sharedVariables.mygame[gameData.BoardIndex].state =
          sharedVariables.STATE_OBSERVING; // observed
      else if(gameType.equals("1") && played.equals("False"))
        sharedVariables.mygame[gameData.BoardIndex].state =
          sharedVariables.STATE_EXAMINING; // examined
      else {
        sharedVariables.mygame[gameData.BoardIndex].state =
          sharedVariables.STATE_PLAYING; // played
        if (myturn.equals("False")) {
          // on first move its not my turn so we flip
          // this breaks for resumed games by the way
          sharedVariables.mygame[gameData.BoardIndex].myColor = "B";
          //sharedVariables.mygame[gameData.BoardIndex].flip();
          sharedVariables.mygame[gameData.BoardIndex].iflipped=1;
        }
      }
      if (sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_EXAMINING)
        sharedVariables.mygame[gameData.BoardIndex].piecePallette=true;
    }// its a valid game
  }

  void newGameRelation(String icsGameNumber, String relation) {
    // O E X
    int tempnumber=getGameNumber(icsGameNumber);

    if (tempnumber ==
        sharedVariables.mygame[gameData.BoardIndex].myGameNumber) {
      if(relation.equals("E")) {
       if (sharedVariables.makeSounds == true  && sharedVariables.mygame[gameData.BoardIndex].state == sharedVariables.STATE_PLAYING)
       {
         makeASound(8);
       } else if(sharedVariables.mygame[gameData.BoardIndex].state == sharedVariables.STATE_OBSERVING) {
        sharedVariables.mygame[gameData.BoardIndex].iWasMadeExaminer = true;
       }
        sharedVariables.mygame[gameData.BoardIndex].state =
          sharedVariables.STATE_EXAMINING;
        sharedVariables.mygame[gameData.BoardIndex].piecePallette=true;
        sharedVariables.mygame[gameData.BoardIndex].title =
          icsGameNumber + " Examining " +
          sharedVariables.mygame[gameData.BoardIndex].realname1  +
          " vs " + sharedVariables.mygame[gameData.BoardIndex].realname2;
        sharedVariables.tabTitle[gameData.BoardIndex] = "E";
        sharedVariables.tabChanged = gameData.BoardIndex;
        repaintCustom();
      }
      if (relation.equals("O"))
        sharedVariables.mygame[gameData.BoardIndex].state =
          sharedVariables.STATE_OBSERVING;
      if(relation.equals("X")) {
        gameEnded(icsGameNumber);
      }
    }
  }
  
  void gameStarted(String icsGameNumber, String WN, String BN,
                   String wildNumber, String rating_type, String rated,
                   String white_initial, String white_inc, String type,
                   String white_rating, String black_rating,
                   String white_titles, String black_titles, int played) {

    sharedVariables.mygame[gameData.BoardIndex] =
      new gamestate(sharedVariables.excludedPiecesWhite, sharedVariables.excludedPiecesBlack, sharedVariables.excludedBoards);

    boolean lowTime=false;
    
    try {
    double timeCheck=Double.parseDouble(white_initial) + .666 * Double.parseDouble(white_inc);

    if(timeCheck < 2.9 && sharedVariables.lowTimeColors == true)
    lowTime=true;
    else
   lowTime=false;
   
   setObservedPgnFile(timeCheck, Integer.parseInt(wildNumber));// called automaticly but only logs if observed and pgnObservedLogging on
   sharedVariables.mygame[gameData.BoardIndex].time = Integer.parseInt(white_initial);
   sharedVariables.mygame[gameData.BoardIndex].inc = Integer.parseInt(white_inc);
   sharedVariables.mygame[gameData.BoardIndex].whiteRating = white_rating;
   sharedVariables.mygame[gameData.BoardIndex].blackRating = black_rating;


    }
    catch(Exception lowtime){
    }


    sharedVariables.mygame[gameData.BoardIndex].myGameNumber =
      getGameNumber(icsGameNumber);
    mypanel.editable=0;

    // set some game data
    try {
      sharedVariables.mygame[gameData.BoardIndex].wild =
        Integer.parseInt(wildNumber);
    }// end try
    catch(Exception e) {}

    sharedVariables.mygame[gameData.BoardIndex].realname1 = WN;
    sharedVariables.mygame[gameData.BoardIndex].realname2 = BN;
    sharedVariables.mygame[gameData.BoardIndex].realelo1 = white_rating;
    sharedVariables.mygame[gameData.BoardIndex].realelo2 = black_rating;
    sharedVariables.mygame[gameData.BoardIndex].newBoard=false;

    String ratedDisplay = "r";
    if (rated.equals("0"))
      ratedDisplay = "u";

    if (rating_type.equals("Wild") || rating_type.startsWith("Loser")) {
      wildTypes wt = new wildTypes();
      sharedVariables.mygame[gameData.BoardIndex].gameListing =
        "" + white_initial + " " + white_inc + " " + ratedDisplay +
        " " + wt.getWildNameString(wildNumber);
    } else
      sharedVariables.mygame[gameData.BoardIndex].gameListing =
        "" + white_initial + " " + white_inc + " " + ratedDisplay +
        " " + rating_type;

    int meplay=0;
    if (sharedVariables.myname.equals(WN))
      meplay=1;

    if (sharedVariables.myname.equals(BN))
      meplay=1;
    if (played == 1 && type.equals("1")) {
      if(!sharedVariables.myname.equals(WN))
        sharedVariables.myopponent=WN;

      if(!sharedVariables.myname.equals(BN))
        sharedVariables.myopponent=BN;
    }
    if (sharedVariables.showRatings == false &&
        type.equals("1") && played == 1) {
      sharedVariables.mygame[gameData.BoardIndex].name1 =
        WN + " " + white_titles;
      sharedVariables.mygame[gameData.BoardIndex].name2 =
        BN + " " + black_titles;
    } else {
      sharedVariables.mygame[gameData.BoardIndex].name1 =
        WN + " " + white_titles + " " + white_rating;
      sharedVariables.mygame[gameData.BoardIndex].name2 =
        BN + " " + black_titles + " " + black_rating;
    }
    if (played == 1) {
      if(type.equals("1"))
      {  sharedVariables.mygame[gameData.BoardIndex].state =
          sharedVariables.STATE_PLAYING;
          sharedVariables.mygame[gameData.BoardIndex].lowTime=lowTime;
          stopTheEngine();
      }

      else
        sharedVariables.mygame[gameData.BoardIndex].state =
          sharedVariables.STATE_EXAMINING;
		
      if (sharedVariables.mygame[gameData.BoardIndex].wild == 26 ||
          sharedVariables.mygame[gameData.BoardIndex].wild == 17) {
        myoutput amove = new myoutput();
        amove.game=1;
        amove.consoleNumber=0;
        if(sharedVariables.mygame[gameData.BoardIndex].wild == 26)
          amove.data="promote knight\n";
        else
          amove.data="promote rook\n";
        queue.add(amove);
      }    // end wild 26 or 17
    } // end if played
    else {
      sharedVariables.mygame[gameData.BoardIndex].state =
        sharedVariables.STATE_OBSERVING;
      if (sharedVariables.randomArmy == true)
        sharedVariables.mygame
          [gameData.BoardIndex].randomObj.randomizeGraphics();
    }
    if (sharedVariables.mygame[gameData.BoardIndex].state ==
        sharedVariables.STATE_EXAMINING)
      sharedVariables.mygame[gameData.BoardIndex].piecePallette=true;
    if (sharedVariables.mygame[gameData.BoardIndex].wild==23 ||
        sharedVariables.mygame[gameData.BoardIndex].wild==24)
      sharedVariables.mygame[gameData.BoardIndex].piecePallette=true;

    if (sharedVariables.mygame[gameData.BoardIndex].state ==
        sharedVariables.STATE_OBSERVING)
      sharedVariables.mygame[gameData.BoardIndex].title =
        icsGameNumber + " observing " + WN + " vs " + BN;
    if (sharedVariables.mygame[gameData.BoardIndex].state ==
        sharedVariables.STATE_PLAYING)
      sharedVariables.mygame[gameData.BoardIndex].title =
        icsGameNumber + " playing " + WN + " vs " + BN;
    if (sharedVariables.mygame[gameData.BoardIndex].state ==
        sharedVariables.STATE_EXAMINING)
      sharedVariables.mygame[gameData.BoardIndex].title =
        icsGameNumber + " Examining " + WN + " vs " + BN;

    setTitle(sharedVariables.mygame[gameData.BoardIndex].title);

    if (sharedVariables.mygame[gameData.BoardIndex].state ==
        sharedVariables.STATE_PLAYING) {
      if (sharedVariables.myname.equals(WN)) {
        sharedVariables.mygame[gameData.BoardIndex].myColor = "W";
        //sharedVariables.mygame[gameData.BoardIndex].iflipped=0;
      } else {
        sharedVariables.mygame[gameData.BoardIndex].myColor = "B";

        if (sharedVariables.mygame[gameData.BoardIndex].iflipped == 0)
          flipSent(icsGameNumber, "1");
      }
    }
    //else
    //sharedVariables.mygame[gameData.BoardIndex].iflipped=0;
    if(sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_EXAMINING) {
           sharedVariables.mygame[gameData.BoardIndex].computeHash();
          }
    if (isVisible() == true)
      mypanel.repaint();
    sharedVariables.mygame[gameData.BoardIndex].turn=0;

  try {
    timerSafeCancel();
    timer = new Timer (  ) ;
    timer.scheduleAtFixedRate( new ToDoTask (  ) , 155 ,155) ;
    resetMoveList();
  }
       catch(Exception duty){ 
 System.err.println("timer initialization exception: "
                        + duty.getMessage());
       }
    if (sharedVariables.mygame[gameData.BoardIndex].wild == 24 &&
        sharedVariables.mygame[gameData.BoardIndex].state ==
       sharedVariables.STATE_PLAYING) {
      myoutput output = new myoutput();
      output.data="Observe " + sharedVariables.myPartner + "\n";

      queue.add(output);
    }
    try {
      if (sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_PLAYING &&
          sharedVariables.makeSounds == true)
        makeASound(4);
    } catch(Exception dumsound) {}
  }

  void logpgn() {
    myoutput output = new myoutput();

    output.data= "`p" + "0" + "`" + "logpgn " +
      sharedVariables.myname + " -1"+ "\n";
    // having a name means level 1 is on if on icc and this
    // `phrase`mess will be used to direct output back to this console

    output.consoleNumber=0;
    queue.add(output);
  }
  void setObservedPgnFile(double time, int wild)
  {
   if(wild == 20)
   return; // we dont log these, string remains "" and will be ignored.
   if(wild > 0)
   sharedVariables.mygame[gameData.BoardIndex].observedPgnFile = "lantern_owild.pgn";
   else if(time < 3)
   sharedVariables.mygame[gameData.BoardIndex].observedPgnFile = "lantern_obullet.pgn";
   else if(time < 15)
   sharedVariables.mygame[gameData.BoardIndex].observedPgnFile = "lantern_oblitz.pgn";
   else
   sharedVariables.mygame[gameData.BoardIndex].observedPgnFile = "lantern_ostandard.pgn";
  } // end method set observed pgn file
void logObservedPgn(String iccresult, String iccresultstring)
{
try {
if(sharedVariables.mygame[gameData.BoardIndex].observedPgnFile.equals(""))
return;
if(sharedVariables.mygame[gameData.BoardIndex].observedPgnFile.equals("lantern_owild.pgn"))
return;
if(sharedVariables.mygame[gameData.BoardIndex].movetop < 1)
return;
if(sharedVariables.mygame[gameData.BoardIndex].state == sharedVariables.STATE_EXAMINING &&
sharedVariables.mygame[gameData.BoardIndex].iWasMadeExaminer == false)
return;

String game = "\r\n";
/*
[Event "ICC 5 0"]
[Site "Internet Chess Club"]
[Date "2011.10.09"]
[Round "-"]
[White "Mike"]
[Black "Prophet-Daniel"]
[Result "1-0"]
[ICCResult "Black forfeits on time"]
[WhiteElo "1046"]
[BlackElo "943"]
[Opening "Sicilian"]
[ECO "B54"]
[NIC "SI.01"]
[Time "03:46:21"]
[TimeControl "300+0"]
*/
String date = "*";
String theTime="*";
try {

Calendar Now=Calendar.getInstance();
String hour= "" + Now.get(Now.HOUR_OF_DAY);// was HOUR for 12 hour time
if(hour.equals("0"))
hour = "12";

String minute="" + Now.get(Now.MINUTE);
if(minute.length()==1)
minute="0"+ minute;

String second="" + Now.get( Now.SECOND);
if(second.length()==1)
second="0"+ second;

theTime=hour + ":" + minute + ":" + second;

}
catch(Exception dumtime){}

try {

Calendar Now=Calendar.getInstance();
// year.month.day
String year = "" + Now.get(Now.YEAR);
int m = Now.get(Now.MONTH) + 1;
String month = "" + m;
if(m < 10) {
 month = "0" + month;
}
String day = "" + Now.get(Now.DAY_OF_MONTH);
if(day.length() == 1)
{
  day = "0" + day;
}
date = year + "." + month + "." + day;
}
catch(Exception dumdate){}



game += "[Event \"ICC " +  sharedVariables.mygame[gameData.BoardIndex].time + " " + sharedVariables.mygame[gameData.BoardIndex].inc + "\"]\r\n";
game += "[Site \"Internet Chess Club\"]\r\n";
game += "[Date \"" + date +  "\"]\r\n";
game += "[Round \"-\"]\r\n";
game += "[White \"" + sharedVariables.mygame[gameData.BoardIndex].realname1 + "\"]\r\n";
game += "[Black \"" + sharedVariables.mygame[gameData.BoardIndex].realname2 + "\"]\r\n";
game += "[Result \"" + iccresult + "\"]\r\n";
game += "[ICCResult \"" + iccresultstring + "\"]\r\n";
game += "[WhiteElo \"" + sharedVariables.mygame[gameData.BoardIndex].whiteRating + "\"]\r\n";
game += "[BlackElo \"" + sharedVariables.mygame[gameData.BoardIndex].blackRating + "\"]\r\n";
game += "[Opening \"*\"]\r\n";
game += "[ECO \"*\"]\r\n";
game += "[NIC \"*\"]\r\n";
game += "[Time \"" + theTime + "\"]\r\n";
int minutes = sharedVariables.mygame[gameData.BoardIndex].time  * 60;
game += "[TimeControl \"" +  minutes + "+" + sharedVariables.mygame[gameData.BoardIndex].inc + "\"]\r\n";
game += "\r\n";
// now moves
game += sharedVariables.mygametable[gameData.BoardIndex].getMoves() + "\r\n";
game += "{" + iccresultstring + "}\r\n";
game += iccresult + "\r\n";
FileWrite writer = new FileWrite();
writer.writeAppend(game, sharedVariables.mygame[gameData.BoardIndex].observedPgnFile);




}// end try
catch(Exception logging){}

}// end method log observed pgn

  void gameEnded(String icsGameNumber) {

    int tempnumber=getGameNumber(icsGameNumber);

    if (tempnumber ==
        sharedVariables.mygame[gameData.BoardIndex].myGameNumber) {

      // add to quue to change tab
      /*
      myoutput output = new myoutput();
      output.tab=gameData.BoardIndex;
      output.tabTitle =
        "W" + sharedVariables.mygame[gameData.BoardIndex].myGameNumber;
      queue.add(output);
      */
      if(sharedVariables.mygame[gameData.BoardIndex].state == sharedVariables.STATE_EXAMINING) {
       gamestate.currentHash = new BigInteger("-1");
      }
       if (sharedVariables.makeSounds == true  && sharedVariables.mygame[gameData.BoardIndex].state == sharedVariables.STATE_PLAYING)
       makeASound(8);

      sharedVariables.mygame[gameData.BoardIndex].title =
        "game over - " +
        sharedVariables.mygame[gameData.BoardIndex].myGameNumber;
      if (sharedVariables.mygame[gameData.BoardIndex].myGameNumber ==
          sharedVariables.ISOLATED_NUMBER)
        sharedVariables.tabTitle[gameData.BoardIndex] = "SP";
      else
        sharedVariables.tabTitle[gameData.BoardIndex] = channels.gameOverTitle;

      sharedVariables.tabChanged = gameData.BoardIndex;

      if (isVisible() == true)
        setTitle(sharedVariables.mygame[gameData.BoardIndex].title);
      mypanel.editable=1;
      sharedVariables.mygame[gameData.BoardIndex].myGameNumber =
        sharedVariables.STATE_OVER;
      //turn=0;
      try {
        if (isVisible() == false)
          // if this board is not visible it's not being used to look
          // at other games and we can terminate it, we also simply
          // check in timer class if there is a game going on before
          // doing work
          timer.cancel() ; //Terminate the thread
      } catch(Exception bad) {}
      sharedVariables.mygame[gameData.BoardIndex].piecePallette=false;
      if ((sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_EXAMINING || sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_OBSERVING) &&
          sharedVariables.engineOn == true && sharedVariables.engineBoard == gameData.BoardIndex) {

          stopTheEngine();
      }

      if (sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_PLAYING &&
          sharedVariables.pgnLogging == true)
        logpgn();

      // this stops moves from being sent in game if its plaing or examining
      sharedVariables.mygame[gameData.BoardIndex].state =
        sharedVariables.STATE_OVER;
    }
  }

void stopTheEngine()
{
        myoutput outgoing = new myoutput();
        outgoing.data = "exit\n";

        sharedVariables.engineQueue.add(outgoing);

        myoutput outgoing2 = new myoutput();
        outgoing2.data = "quit\n";

        sharedVariables.engineQueue.add(outgoing2);
        sharedVariables.engineOn=false;
}


  void resetMoveList() {

    try {
      for (int d=0; d<sharedVariables.openBoardCount; d++)
        if (sharedVariables.mygametable[sharedVariables.gamelooking[d]] != null)
          if (sharedVariables.gamelooking[d] == gameData.BoardIndex) {
            sharedVariables.mygametable[gameData.BoardIndex] = new tableClass();
            sharedVariables.mygametable[gameData.BoardIndex].setChessFontForMoveList(sharedVariables.chessFontForMoveList);
            sharedVariables.mygametable
              [gameData.BoardIndex].createMoveListColumns(

               sharedVariables.mygame[gameData.BoardIndex].wild
              );
            sharedVariables.gametable[gameData.BoardIndex].setModel
              (sharedVariables.mygametable[gameData.BoardIndex].gamedata);
          }
    }// end try
    catch(Exception reset) {}
  }

  void gameEndedExamined(String icsGameNumber) {

    int tempnumber=getGameNumber(icsGameNumber);
    // remove this

    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{

       if (sharedVariables.makeSounds == true  && sharedVariables.mygame[gameData.BoardIndex].state == sharedVariables.STATE_PLAYING)
       makeASound(8);


      sharedVariables.mygame[gameData.BoardIndex].title =
        "game over now examined- " +
        sharedVariables.mygame[gameData.BoardIndex].myGameNumber;
      sharedVariables.tabTitle[gameData.BoardIndex] = "WE";
      sharedVariables.tabChanged = gameData.BoardIndex;

      if (isVisible() == true)
        setTitle(sharedVariables.mygame[gameData.BoardIndex].title);
      mypanel.editable=1;
      if (sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_OBSERVING)
        sharedVariables.mygame[gameData.BoardIndex].becameExamined=true;
      // game we were observing is over so set this to stop clock


      if (sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_PLAYING &&
          sharedVariables.pgnLogging == true)
        logpgn();


      if (sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_PLAYING)
        // we do this when playing ( state =1) so in a simul this game
        // stops being a played game
        sharedVariables.mygame[gameData.BoardIndex].state =
          sharedVariables.STATE_OBSERVING;
      //turn=0;
      try {
        /*
        if(isVisible() == false)
          timer.cancel() ;
        //Terminate the thread
        // clocks were stopping with this code on new games. don't know why
        
        // MA 12-10-10
        */
      } catch(Exception bad) {}
    }
  }

  void updateWhiteName(String icsGameNumber, String value) {
    int tempnumber=getGameNumber(icsGameNumber);
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{
      sharedVariables.mygame[gameData.BoardIndex].name1 =
        value + " " + sharedVariables.mygame[gameData.BoardIndex].realelo1 ;
      sharedVariables.mygame[gameData.BoardIndex].realname1 = value;
    }
  }
  
  void updateBlackName(String icsGameNumber, String value) {
    int tempnumber=getGameNumber(icsGameNumber);
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{
      sharedVariables.mygame[gameData.BoardIndex].name2 =
        value + " " + sharedVariables.mygame[gameData.BoardIndex].realelo2 ;
      sharedVariables.mygame[gameData.BoardIndex].realname2 = value;
    }
  }
  
  void updateWhiteElo(String icsGameNumber, String value) {
    int tempnumber=getGameNumber(icsGameNumber);
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{
      sharedVariables.mygame[gameData.BoardIndex].name1 =
        sharedVariables.mygame[gameData.BoardIndex].realname1 + " " + value  ;
      sharedVariables.mygame[gameData.BoardIndex].realelo1=value;
    }
  }
  
  void updateBlackElo(String icsGameNumber, String value) {
    int tempnumber=getGameNumber(icsGameNumber);
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{
      sharedVariables.mygame[gameData.BoardIndex].name2 =
        sharedVariables.mygame[gameData.BoardIndex].realname2 + " " + value  ;
      sharedVariables.mygame[gameData.BoardIndex].realelo2=value;
    }
  }

  void moveBoardDown() {

    try {
      Point P= getLocation();
      setLocation(P.x, P.y - 75);
    } catch(Exception dui) {}
  }

  void moveBoardUp() {
    try {
      Point P= getLocation();
      setLocation(P.x, P.y + 75);
    } catch(Exception dui) {}
  }

  void updateClock(String icsGameNumber, String colort, String time) {
    //int tempnumber=getGameNumber(icsGameNumber);
    //if(tempnumber == myGameNumber)
    //{

    try {
      //double whiteClockd, blackClockd;
      if(colort.equals("W")) {
        sharedVariables.mygame[gameData.BoardIndex].whiteClock =
          Double.parseDouble(time)/1000;
        sharedVariables.mygame[gameData.BoardIndex].wtime =
          sharedVariables.mygame[gameData.BoardIndex].whiteClock;
        sharedVariables.mygame[gameData.BoardIndex].whiteMinute =
          getMinutes(sharedVariables.mygame[gameData.BoardIndex].whiteClock);
        sharedVariables.mygame[gameData.BoardIndex].whiteSecond =
          getSeconds(sharedVariables.mygame[gameData.BoardIndex].whiteMinute,
                     sharedVariables.mygame[gameData.BoardIndex].whiteClock);
        sharedVariables.mygame[gameData.BoardIndex].whiteTenth =
          getTenths(Double.parseDouble(time));

        sharedVariables.mygame[gameData.BoardIndex].whitenow =
          System.currentTimeMillis();
      } else {
        sharedVariables.mygame[gameData.BoardIndex].blackClock =
          Double.parseDouble(time)/1000;
        sharedVariables.mygame[gameData.BoardIndex].btime =
          sharedVariables.mygame[gameData.BoardIndex].blackClock;
        sharedVariables.mygame[gameData.BoardIndex].blackMinute =
          getMinutes(sharedVariables.mygame[gameData.BoardIndex].blackClock);
        sharedVariables.mygame[gameData.BoardIndex].blackSecond =
          getSeconds(sharedVariables.mygame[gameData.BoardIndex].blackMinute,
                     sharedVariables.mygame[gameData.BoardIndex].blackClock);
        sharedVariables.mygame[gameData.BoardIndex].blackTenth =
          getTenths(Double.parseDouble(time));
        sharedVariables.mygame[gameData.BoardIndex].blacknow =
          System.currentTimeMillis();
      }
    } catch(Exception e) {}

    //}
  }

  void updateFicsClock(String icsGameNumber, String whiteTime, String blackTime) {

    //int tempnumber=getGameNumber(icsGameNumber);
    //if(tempnumber == myGameNumber)
    //{
    ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    Lock writeLock = rwl.writeLock();

    writeLock.lock();
    try {
      //double whiteClockd, blackClockd;

      sharedVariables.mygame[gameData.BoardIndex].whiteClock =
        Double.parseDouble(whiteTime)/1000;
      sharedVariables.mygame[gameData.BoardIndex].whiteMinute =
        getMinutes(sharedVariables.mygame[gameData.BoardIndex].whiteClock);
      sharedVariables.mygame[gameData.BoardIndex].whiteSecond =
        getSeconds(sharedVariables.mygame[gameData.BoardIndex].whiteMinute,
                   sharedVariables.mygame[gameData.BoardIndex].whiteClock);
      sharedVariables.mygame[gameData.BoardIndex].whitenow =
        System.currentTimeMillis();

      sharedVariables.mygame[gameData.BoardIndex].blackClock =
        Double.parseDouble(blackTime)/1000;
      sharedVariables.mygame[gameData.BoardIndex].blackMinute =
        getMinutes(sharedVariables.mygame[gameData.BoardIndex].blackClock);
      sharedVariables.mygame[gameData.BoardIndex].blackSecond =
        getSeconds(sharedVariables.mygame[gameData.BoardIndex].blackMinute,
                   sharedVariables.mygame[gameData.BoardIndex].blackClock);
      sharedVariables.mygame[gameData.BoardIndex].blacknow =
        System.currentTimeMillis();
    } catch(Exception e) {}
    finally { writeLock.unlock();}
    //}
  }

  int getTenths(double ms) {
    /*
    int min = getMinutes(ms/1000);
    int sec = getSeconds(min, ms/1000);
    double dif = (min * 60 + sec) * 1000;
    dif = ms - dif; //milliseconds under a second
    int dif2 = (int) dif * 10;
    if(dif2 < 10 && dif2 > 0)
      return dif2;
    */
    try {
      int sec = (int) (ms / 1000);
      double secHigh= (double) (ms / 1000);
      double fractionOfSec= (double) (secHigh - sec);
      if (fractionOfSec < 0) // negative tenths
        fractionOfSec= (double) (sec - secHigh );
      int tenth = (int) (fractionOfSec * 10);
      tenth = Math.abs(tenth);
      if(tenth < 10)
	return tenth;
    } catch(Exception badTenth) {}
    return 0;
  }

  int getMinutes(double s) {

    int min=0;

    s=s/60;
    min = (int) s;

    return min;

  }

  int getSeconds(int min, double s) {

    int sec=0;

    sec =  (int) ( s - ((double) min * 60));
    return sec;
  }

  void repaintClocks() {
   /* Point topPoint = mycontrolspanel.topClockDisplay.getLocation();
    Point botPoint = mycontrolspanel.botClockDisplay.getLocation();
    Dimension topSize=mycontrolspanel.topClockDisplay.getSize();
    Dimension botSize=mycontrolspanel.botClockDisplay.getSize();

    mycontrolspanel.repaint(topPoint.x, topPoint.y,
                            topSize.width, topSize.height);
    mycontrolspanel.repaint(botPoint.x, botPoint.y,
                            botSize.width, botSize.height);
   */
     mycontrolspanel.repaint();
   if(sharedVariables.mygame[gameData.LookingAt].wild==24)// paint bug partners clock on gameboardpanel
   mypanel.repaint((int) mypanel.getWidth()/ 2, 0, (int)mypanel.getWidth()/ 2, sharedVariables.myGameFont.getSize() + 5);
  }

  class ToDoTask extends TimerTask {

    void paintClocks() {

      //mycontrolspanel.repaint();
      try {
        if (isVisible() == true)
          if (sharedVariables.mygame[gameData.LookingAt].myGameNumber !=
              sharedVariables.NOT_FOUND_NUMBER && sharedVariables.mygame[gameData.LookingAt].myGameNumber !=
              sharedVariables.STATE_OVER)
            if(sharedVariables.mygame[gameData.LookingAt].becameExamined ==
               false) {

              repaintClocks();
            }
      } catch(Exception painting) {}

    }// ened paint clocks




    public void run() {

      if (sharedVariables.mygame[gameData.BoardIndex] == null)
        // not equal null remove
	return;
      // mike remove

      if (sharedVariables.mygame[gameData.BoardIndex].myGameNumber ==
          sharedVariables.NOT_FOUND_NUMBER || sharedVariables.mygame[gameData.LookingAt].myGameNumber ==
              sharedVariables.STATE_OVER) {
        // we dont want to do updates to time if the game is over
        paintClocks();
        return;
      }
       SwingUtilities.invokeLater(new Runnable() {
    public void run() {
      

      int newminute=0;
      int newsecond=0;
      int newtenth=0;
      if (((sharedVariables.mygame[gameData.BoardIndex].turn + 1)%2 == 1 &&  sharedVariables.mygame[gameData.BoardIndex].wild != 30)
      || ((sharedVariables.mygame[gameData.BoardIndex].turn + 1)%2 == 0 && sharedVariables.mygame[gameData.BoardIndex].wild == 30)) {
        // white on the move

        try {



          double time=System.currentTimeMillis();
          if (sharedVariables.mygame[gameData.LookingAt].state ==
              sharedVariables.STATE_EXAMINING)
              {
                sharedVariables.mygame[gameData.LookingAt].whitenow = (long) time;
                sharedVariables.mygame[gameData.LookingAt].blacknow = (long) time;

               }

          time =
            (double)(time-sharedVariables.mygame[gameData.BoardIndex].whitenow);
          sharedVariables.mygame[gameData.BoardIndex].wtime =
            sharedVariables.mygame[gameData.BoardIndex].whiteClock;
          sharedVariables.mygame[gameData.BoardIndex].wtime =
            (double) sharedVariables.mygame[gameData.BoardIndex].wtime -
            time/1000;

          newminute =
            getMinutes(sharedVariables.mygame[gameData.BoardIndex].wtime);
          newsecond =
            getSeconds(newminute,
                       sharedVariables.mygame[gameData.BoardIndex].wtime);
          newtenth =
            getTenths(1000 *
                      (sharedVariables.mygame[gameData.BoardIndex].wtime ));

        } catch(Exception duy) {}


        if (sharedVariables.mygame[gameData.BoardIndex].whiteMinute != newminute ||
            sharedVariables.mygame[gameData.BoardIndex].whiteSecond != newsecond ||
            sharedVariables.mygame[gameData.BoardIndex].whiteTenth != newtenth) {

          if (sharedVariables.mygame[gameData.BoardIndex].state !=
              sharedVariables.STATE_EXAMINING) {

            try {


              sharedVariables.mygame[gameData.BoardIndex].whiteMinute=newminute;

              sharedVariables.mygame[gameData.BoardIndex].whiteSecond=newsecond;
              sharedVariables.mygame[gameData.BoardIndex].whiteTenth=newtenth;
            } catch(Exception duyi) {}

	  		}
          //if(isVisible() == true)
          //repaint();
          paintClocks();

        }
      }// end if white
      else {


        try {


          double time=System.currentTimeMillis();
          time=(double)(time-sharedVariables.mygame[gameData.BoardIndex].blacknow);
          sharedVariables.mygame[gameData.BoardIndex].btime =
            sharedVariables.mygame[gameData.BoardIndex].blackClock;
          sharedVariables.mygame[gameData.BoardIndex].btime =
            (double) sharedVariables.mygame[gameData.BoardIndex].btime - time/1000;

          newminute=getMinutes(sharedVariables.mygame[gameData.BoardIndex].btime);
          newsecond =
            getSeconds(newminute, sharedVariables.mygame[gameData.BoardIndex].btime);
          newtenth =
            getTenths(1000 * (sharedVariables.mygame[gameData.BoardIndex].btime));
        } catch(Exception duy){}


        if (sharedVariables.mygame[gameData.BoardIndex].blackMinute != newminute ||
            sharedVariables.mygame[gameData.BoardIndex].blackSecond != newsecond ||
            sharedVariables.mygame[gameData.BoardIndex].blackTenth != newtenth) {
          if (sharedVariables.mygame[gameData.BoardIndex].state !=
              sharedVariables.STATE_EXAMINING) {

            try {


              sharedVariables.mygame[gameData.BoardIndex].blackMinute=newminute;

              sharedVariables.mygame[gameData.BoardIndex].blackSecond=newsecond;
              sharedVariables.mygame[gameData.BoardIndex].blackTenth=newtenth;
            } catch(Exception duyi) {}


          }
          //if(isVisible() == true)
          //  repaint();

          paintClocks();

        }
      }// end if black
      


         }
         });// end invoke later
    }

  }// end todo class

  class AutoExamTask extends TimerTask  {

    public void run (  )   {

      if (sharedVariables.autoexam==0 ||
          sharedVariables.mygame[gameData.BoardIndex].state !=
          sharedVariables.STATE_EXAMINING) {
	//autotimer.cancel();
	return;
      }

      myoutput amove = new myoutput();
      amove.game=1;
      amove.consoleNumber=0;
      amove.data="forward\n";
      queue.add(amove);

      autotimer = new Timer ();
      autotimer.schedule( new AutoExamTask (  ) ,sharedVariables.autoexamspeed);

      /*
      if(sharedVariables.autoexamspeed != myspeed) {
        autotimer.cancel();
        autotimer = new Timer ();
        autotimer.scheduleAtFixedRate
          ( new AutoExamTask (  ) , sharedVariables.autoexamspeed ,
            sharedVariables.autoexamspeed);

        myspeed=sharedVariables.autoexamspeed;
      }
      */
    }

  }// end todo class

  public void initializeGeneralTimer() {
    generalTimer = new Timer ();
    generalTimer.scheduleAtFixedRate( new GeneralTask (  ) , 1000, 1000);
  }

  class GeneralTask extends TimerTask  {

    public void run (  )   {

      //generalTimer = new Timer ();
      //generalTimer.schedule( new GeneralTask (  ) ,500);
      if (sharedVariables.mygame[gameData.BoardIndex] == null)
        return;

      if (sharedVariables.mygame[gameData.BoardIndex].myGameNumber >
          sharedVariables.NOT_FOUND_NUMBER)
        // i got a game on my board, i dont need a repaint for my clocks
	return;

      if (sharedVariables.mygame[gameData.LookingAt].myGameNumber ==
          sharedVariables.NOT_FOUND_NUMBER)
        // the game i'm looking at doesnt have a clock
	return;
      if (isVisible() == true)
        repaintClocks();
    }
  }// end todo class

  void makeASound(int type) {
    try {
      if (type == 0)
	SwingUtilities.invokeLater(new Runnable() {
            @Override
              public void run() {
              try {

                Sound movesound=new Sound(sharedVariables.songs[1]);

              } catch (Exception e1) {
                //ignore
              }
            }
          });
      if (type == 1)
	SwingUtilities.invokeLater(new Runnable() {
            @Override
              public void run() {
              try {
                Sound movesound=new Sound(sharedVariables.songs[2]);

              } catch (Exception e1) {
                //ignore
              }
            }
          });
      if (type == 4)
	SwingUtilities.invokeLater(new Runnable() {
            @Override
              public void run() {
              try {
                Sound movesound=new Sound(sharedVariables.songs[5]);

              } catch (Exception e1) {
                //ignore
              }
            }
          });

      if (type == 8)
	SwingUtilities.invokeLater(new Runnable() {
            @Override
              public void run() {
              try {
                
                Sound movesound=new Sound(sharedVariables.songs[5]);

              } catch (Exception e1) {
                //ignore
              }
            }
          });


    } catch(Exception dumb1) {}

  }// end make sound method

  void loadMoveList(String icsGameNumber, String moves) {
    StringTokenizer st = new StringTokenizer(moves);
    while (st.hasMoreTokens()) {
      String amove=st.nextToken();
      moveSent(icsGameNumber, amove, amove, false);// arg false for no sound
    }
  }
  
  void makePremove() {

    myoutput amove = new myoutput();
    amove.game=1;
    amove.consoleNumber=0;
    amove.data=sharedVariables.mygame[gameData.BoardIndex].premove;
    queue.add(amove);
    sharedVariables.mygame[gameData.BoardIndex].premove="";
  }

  void moveSent(String icsGameNumber, String amove,
                String algabraicMove, boolean makeSound) {

    int tempnumber=getGameNumber(icsGameNumber);
    boolean iLocked = false;
    if(!sharedVariables.makeMoveSounds) {
     makeSound = false;
    }
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{
      // get pgn move before we make move on board
      String newMove = algabraicMove;
      /*
      try {
	if (!newMove.contains("@") &&
            sharedVariables.mygame[gameData.BoardIndex].wild!=20)
          newMove =
            pgnGetter.getPgn(amove, sharedVariables.mygame[gameData.BoardIndex].
                             iflipped,
                             sharedVariables.mygame[gameData.BoardIndex].boardCopy);
      }	catch(Exception dy) {}
      */
      ReentrantReadWriteLock rwl2 = new ReentrantReadWriteLock();
      Lock readLock2 = rwl2.readLock();
      if (sharedVariables.mygame[gameData.LookingAt].state ==
          sharedVariables.STATE_PLAYING) {
        readLock2.lock();
        iLocked=true;
      }

      //setTitle(icsGameNumber + ":" + amove + ":");
      if (!sharedVariables.mygame[gameData.BoardIndex].premove.equals("")) {
        int movetop=sharedVariables.mygame[gameData.BoardIndex].movetop;
        if ((sharedVariables.mygame[gameData.BoardIndex].realname1.equals
             (sharedVariables.myname) && movetop%2==1) ||
            (sharedVariables.mygame[gameData.BoardIndex].realname2.equals
             (sharedVariables.myname) && movetop%2==0)) {
          makePremove();
          if(sharedVariables.mygame[gameData.BoardIndex].wild !=30)
          sharedVariables.mygame[gameData.BoardIndex].madeMove=1;
        } else
          sharedVariables.mygame[gameData.BoardIndex].madeMove=0;
      } else
        sharedVariables.mygame[gameData.BoardIndex].madeMove=0;

      int castleCapture =0;
      int dummy1=amove.length();
      if (dummy1 >= 5) {
	if(amove.charAt(4) == 'c')
          castleCapture=1;
	if(amove.charAt(4) == 'C')
          castleCapture=2;
	if(amove.charAt(4) == 'E')
          castleCapture=3;
      }

      int xfrom=0, yfrom=0, xto=0, yto=0;

      if (!(amove.contains("?") &&
            sharedVariables.mygame[gameData.BoardIndex].wild == 16)) {
        xfrom=getxmove(amove, 0);
        if (xfrom >=0)
          yfrom=getymove(amove, 0);
        else
          yfrom=0;
        xto=getxmove(amove, 2);
        yto=getymove(amove, 2);
        if (sharedVariables.mygame[gameData.BoardIndex].iflipped == 1) {
          yfrom = 7-yfrom;
          yto=7-yto;
        }
      }// end if not dummy kried move

      // current time
      if (sharedVariables.mygame[gameData.BoardIndex].turn %2 == 1)
        sharedVariables.mygame[gameData.BoardIndex].whitenow =
          System.currentTimeMillis();
      else
        sharedVariables.mygame[gameData.BoardIndex].blacknow =
          System.currentTimeMillis();

      sharedVariables.mygame[gameData.BoardIndex].clearShapes();

      // we change slider before turn some bug on some computers to
      // repaint move just before move or when slider set back 1
      ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
      Lock readLock = rwl.readLock();

      for (int a=0; a<sharedVariables.maxGameTabs; a++) {
        if (sharedVariables.gamelooking[a]==gameData.BoardIndex &&
            sharedVariables.gamelooking[a]!=-1 &&
            sharedVariables.moveSliders[a]!=null) {

          if(iLocked == false)
            readLock.lock();

          sharedVariables.moveSliders[a].setMaximum
            (sharedVariables.mygame[gameData.BoardIndex].turn+1 );
          // turn + 1 because turn is not +1 yet
          if (sharedVariables.moveSliders[a].getValue() ==
              sharedVariables.moveSliders[a].getMaximum() -1)
            sharedVariables.moveSliders[a].setValue
              (sharedVariables.moveSliders[a].getMaximum());

          if(iLocked == false)
            readLock.unlock();
        }// end if
      }// end for

      // increment turn
      sharedVariables.mygame[gameData.BoardIndex].turn++;

      char prom='*';
      if (amove.length() == 5 && castleCapture == 0)// look for promotion
        prom=amove.charAt(4);
      if(amove.length() == 6)// look for promotion
        prom=amove.charAt(5);

      int type=0;
      if (!(amove.contains("?") &&
            sharedVariables.mygame[gameData.BoardIndex].wild == 16)) {

        if (xfrom >= 0) // not @ drop
          type=sharedVariables.mygame[gameData.BoardIndex].makemove
            (xfrom + yfrom * 8, xto + yto * 8, prom, 0, castleCapture, algabraicMove);
        // second to last field is reload
        else
          type=sharedVariables.mygame[gameData.BoardIndex].makemove
            (xfrom, xto + yto * 8, prom, 0, castleCapture, algabraicMove);
        // second to last field is reload
      } else {
        type=0;
        sharedVariables.mygame[gameData.BoardIndex].kriegMove(0);
        // check for move in form of ?xa3 that means we have to erase a piece in krieg
        if (amove.length()==4) {
          String aCaptureSquare = amove.substring(2, 4);
          xfrom=getxmove(aCaptureSquare, 0);
          yfrom=getymove(aCaptureSquare, 0);

          if (sharedVariables.mygame[gameData.BoardIndex].iflipped == 1)
            yfrom = 7-yfrom;

          sharedVariables.mygame[gameData.BoardIndex].kriegCapture
            (xfrom + yfrom * 8, 0);

        }
      }
      if (iLocked == true)
        readLock2.unlock();

      //String newtitle = "from " + xfrom + yfrom * 8 + " to " +  xto + yto * 8;
      //setTitle(newtitle);

      // send to engine if we are anlyzing
    if ((sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_EXAMINING || sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_OBSERVING || sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_PLAYING)) {
	//sendToEngine("time 1000000");
        //sendToEngine("otime 1000000");
        myoutput outgoing = new myoutput();

        if (amove.length() > 4) {
          if (prom != 'N' && prom != 'B' && prom != 'R' &&
              prom != 'Q' && prom != 'K')
            outgoing.data = "" + amove.substring(0, 4)+ "\n";
          else {
            String promString = "" + prom;
            promString=promString.toLowerCase();
            outgoing.data = "" + amove.substring(0, 4) + promString + "\n";
          }
        } else
          outgoing.data= "" + amove + "\n";

        sharedVariables.mygame[gameData.BoardIndex].makeEngineMove(outgoing.data);
        //engine move hanldes incrementing top
         if ((sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_EXAMINING || sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_OBSERVING)&&
          sharedVariables.engineOn == true && sharedVariables.engineBoard == gameData.BoardIndex) {
          // we allways make the engine moves for later but dont send
          // unless of course the engine is on
            if (sharedVariables.uci == false)
              sharedVariables.engineQueue.add(outgoing);
            else
              sendUciMoves();
        }// end if engine on true
      }// end if examining

        // add to move list
        try {
          /*
          if (amove.length() > 4)
            sharedVariables.mygametable[gameData.BoardIndex].addMove
              (sharedVariables.mygame[gameData.BoardIndex].movetop,
               amove.substring(0,4));
          else
            sharedVariables.mygametable[gameData.BoardIndex].addMove
              (sharedVariables.mygame[gameData.BoardIndex].movetop, amove);
          */
          /*
          if (amove.contains("?") || amove.contains("@") ||
              sharedVariables.mygame[gameData.BoardIndex].wild == 16 ||
              sharedVariables.mygame[gameData.BoardIndex].wild == 20 ) {
            if (amove.length() > 4)
              sharedVariables.mygametable[gameData.BoardIndex].addMove
                (sharedVariables.mygame[gameData.BoardIndex].movetop,
                 amove.substring(0,4));
            else
              sharedVariables.mygametable[gameData.BoardIndex].addMove
                (sharedVariables.mygame[gameData.BoardIndex].movetop, amove);

          } else
            */
          sharedVariables.mygametable[gameData.BoardIndex].addMove
            (sharedVariables.mygame[gameData.BoardIndex].movetop, newMove);

          try {
            for (int aa=0; aa<sharedVariables.maxGameTabs; aa++) {
              // move this to not hard coded

              if(sharedVariables.gamelooking[aa]==gameData.BoardIndex &&
                 sharedVariables.gamelooking[aa]!=-1 &&
                 sharedVariables.gametable[aa]!=null) {
                final int aaa=aa;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                      public void run() {
                      try {
                        if(sharedVariables.moveSliders[aaa].getValue() ==
                           sharedVariables.moveSliders[aaa].getMaximum())
                          sharedVariables.gametable[aaa].scrollRectToVisible
                            (sharedVariables.gametable[aaa].getCellRect
                             (sharedVariables.gametable[aaa].getRowCount()-1,0,true));

                      } catch (Exception e1) {
                        //ignore
                      }
                    }
                  });

                //readLock.lock();

		//readLock.unlock();
              }// end if
            }// end for

          } catch(Exception dumb) {}
        } catch(Exception dd) {}
        if (isVisible() == true) {
          if (sharedVariables.useTopGames == true)
            mypanel.repaint();
          else
            repaint();
        }

        try {// sound
          if (sharedVariables.makeSounds == true && makeSound == true)
            if (sharedVariables.mygame[gameData.BoardIndex].state ==
                sharedVariables.STATE_PLAYING ||
                sharedVariables.mygame[gameData.BoardIndex].state ==
                sharedVariables.STATE_EXAMINING ||
                (sharedVariables.mygame[gameData.BoardIndex].state ==
                 sharedVariables.STATE_OBSERVING &&
                 sharedVariables.makeObserveSounds == true &&
                 sharedVariables.soundGame == gameData.BoardIndex)) {
              if (sharedVariables.lastSoundTime + 200 >
                  System.currentTimeMillis() && sharedVariables.lastSoundCount> 1)
                ; // dont make a sound do nothing
              else {
                if (sharedVariables.lastSoundTime + 200 > System.currentTimeMillis())
                  sharedVariables.lastSoundCount++;
                else
                  sharedVariables.lastSoundCount=0;
                // limited to 2 sounds every 1200 ms
               if(sharedVariables.mygame[gameData.BoardIndex].state !=
                sharedVariables.STATE_PLAYING )
                sharedVariables.lastSoundTime = System.currentTimeMillis();
                makeASound(type);
              }
            }

        } catch(Exception tomanysounds) {}

    }// end if my game
  }// end method


  void illegalMove(String icsGameNumber, String moveString, String reason) {
    int tempnumber=getGameNumber(icsGameNumber);
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber) {

     
    if( sharedVariables.mygame[gameData.BoardIndex].wild == 30 && reason.equals("11")) // checkers illegal move means they have another move to jump
    return;

      ReentrantReadWriteLock rwl2 = new ReentrantReadWriteLock();
      Lock readLock2 = rwl2.readLock();

      readLock2.lock();

      sharedVariables.mygame[gameData.BoardIndex].premove="";
      sharedVariables.mygame[gameData.BoardIndex].madeMove=0;
      readLock2.unlock();

      sharedVariables.mygame[gameData.BoardIndex].replay();
      if(sharedVariables.mygame[gameData.BoardIndex].state == sharedVariables.STATE_EXAMINING)
      {
          sharedVariables.mygame[gameData.BoardIndex].computeHash();
      }

      Sound s;

      if (sharedVariables.makeSounds == true)
        s = new Sound(sharedVariables.songs[3]);
      if (isVisible() == true)
        repaintCustom();
    }
  }


  void writeCountry(String icsGameNumber, String name, String country) {

    int tempnumber=getGameNumber(icsGameNumber);
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{
      if (sharedVariables.mygame[gameData.BoardIndex].realname1.equals(name)) {
        sharedVariables.mygame[gameData.BoardIndex].country1 = " " + country + " ";
        mycontrolspanel.oldLooking=-1;
      } else {
        sharedVariables.mygame[gameData.BoardIndex].country2 = " " + country + " ";
        mycontrolspanel.oldLooking=-1;
      }
    }
    if (isVisible() == true)
      repaintCustom();
  }
  
  void redrawFlags() {
    if (sharedVariables.mygame[gameData.LookingAt].iflipped == 0) {
      createFlag(sharedVariables.mygame[gameData.LookingAt].country1.trim(), false);
      createFlag(sharedVariables.mygame[gameData.LookingAt].country2.trim(), true);
    } else {
      createFlag(sharedVariables.mygame[gameData.LookingAt].country1.trim(), true);
      createFlag(sharedVariables.mygame[gameData.LookingAt].country2.trim(), false);
    }
  }

  void createFlag(String country, boolean top) {

    if (!country.equals("icc")) country = country.toUpperCase();

    try {
    
      String uniqueName="";
      int bb=sharedVariables.countryNames.indexOf(";" + country + ";");

      if(bb > -1) {
        int bbb=sharedVariables.countryNames.indexOf( ";", bb + 4);

        if (bbb > -1 && !country.equals("icc"))
          uniqueName =
            sharedVariables.countryNames.substring(bb+ country.length() + 2, bbb);
        if (country.equals("icc"))
          uniqueName = "icc";
      }

      if (uniqueName.equals("") || sharedVariables.showFlags == false) {
        if (top == true)
          mycontrolspanel.flagTop.setVisible(false);
        else
          mycontrolspanel.flagBottom.setVisible(false);
        return;
      } else {
        int n=-1;
        if (uniqueName.equals("icc") && sharedVariables.basketballFlag == true) {
          for (int m=0; m < sharedVariables.flagImageNames.size(); m++)
            if (sharedVariables.flagImageNames.get(m).equals("icc1")) {
              n=m;
              break;
            }
        } else {
          for (int m=0; m < sharedVariables.flagImageNames.size(); m++)
            if (sharedVariables.flagImageNames.get(m).equals(uniqueName)) {
              n=m;
              break;
            }
        }
        if (n == -1) {
    
          if (top == true)
            mycontrolspanel.flagTop.setVisible(false);
          else
            mycontrolspanel.flagBottom.setVisible(false);

          return;
        }
        Icon flagIcon = new ImageIcon(sharedVariables.flagImages.get(n));
        if (top == true) {
          mycontrolspanel.flagTop.setIcon(flagIcon);
          mycontrolspanel.flagTop.setVisible(true);
          mycontrolspanel.flagTop.setToolTipText
            (sharedVariables.flagImageNames.get(n));
        }// end if top == true
        else {
          mycontrolspanel.flagBottom.setIcon(flagIcon);
          mycontrolspanel.flagBottom.setVisible(true);
          mycontrolspanel.flagBottom.setToolTipText
            (sharedVariables.flagImageNames.get(n));

        }// top not true
      }// end else there is a unique name

    }// end try
    catch(Exception dui) {}
  }
  
  void newCircle(String icsGameNumber, String examiner, String from) {
    int tempnumber=getGameNumber(icsGameNumber);
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{
      int xfrom=getxmove(from, 0);
      int yfrom=getymove(from, 0);
      if (sharedVariables.mygame[gameData.BoardIndex].iflipped == 1) {
        yfrom = 7-yfrom;
      }

      sharedVariables.mygame[gameData.BoardIndex].addCircle
        (63 - (xfrom + yfrom * 8));
      if(isVisible() == true)
        repaintCustom();
    }
  }


  void newArrow(String icsGameNumber, String examiner, String from, String to) {
    int tempnumber=getGameNumber(icsGameNumber);
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{
      int xfrom=getxmove(from, 0);
      int yfrom=getymove(from, 0);
      int xto=getxmove(to, 0);
      int yto=getymove(to, 0);
      if (sharedVariables.mygame[gameData.BoardIndex].iflipped == 1) {
        yfrom = 7-yfrom;
        yto=7-yto;
      }

      sharedVariables.mygame[gameData.BoardIndex].addArrow
        (63 - (xfrom + yfrom * 8), 63 - (xto + yto * 8));
      if (isVisible() == true)
        repaintCustom();
    }
  }

  void sendUciMoves() {


      

    myoutput outgoing = new myoutput();
    outgoing.data="stop\n";
    //sharedVariables.engineQueue.add(outgoing);

    //myoutput outgoing2 = new myoutput();
    String moves;
    if (sharedVariables.mygame[gameData.BoardIndex].engineFen.length()>2) {

      moves="";
      if (!sharedVariables.mygame[gameData.BoardIndex].engineFen.startsWith
          ("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"))
        moves="ucinewgame\n";
      moves+="position fen " +
        sharedVariables.mygame[gameData.BoardIndex].engineFen +
        sharedVariables.mygame[gameData.BoardIndex].getUciMoves();
      
    } else {
      moves ="stop\nposition startpos";
      moves+=sharedVariables.mygame[gameData.BoardIndex].getUciMoves();
    }
    outgoing.data+= moves;
    //sharedVariables.engineQueue.add(outgoing2);

    //myoutput outgoing3 = new myoutput();
    outgoing.data+= "go infinite\n";
    sharedVariables.engineQueue.add(outgoing);
  }

  void Backward(String icsGameNumber, String count) {
    int tempnumber=getGameNumber(icsGameNumber);
    final int num=getGameNumber(count);
    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber)	{
      sharedVariables.mygame[gameData.BoardIndex].movetop-=num;

      if(sharedVariables.mygame[gameData.BoardIndex].lastKingMoveWhite > sharedVariables.mygame[gameData.BoardIndex].movetop) {
        sharedVariables.mygame[gameData.BoardIndex].lastKingMoveWhite = -1;
      }
      if(sharedVariables.mygame[gameData.BoardIndex].lastKingMoveBlack > sharedVariables.mygame[gameData.BoardIndex].movetop) {
        sharedVariables.mygame[gameData.BoardIndex].lastKingMoveBlack = -1;
      }
      sharedVariables.mygame[gameData.BoardIndex].turn =
        sharedVariables.mygame[gameData.BoardIndex].turn-num;
      for (int a=0; a<sharedVariables.maxGameTabs; a++) {
        if (sharedVariables.Looking[a]==gameData.BoardIndex &&
            sharedVariables.Looking[a]!=-1 && sharedVariables.moveSliders[a]!=null) {
          sharedVariables.moveSliders[a].setMaximum
            (sharedVariables.mygame[gameData.BoardIndex].turn);

          sharedVariables.moveSliders[a].setValue
            (sharedVariables.moveSliders[a].getMaximum());
        }// end if
      }// end for

      // remove from move list.  we dont have to call all boards
      // because all boards share same data object
      if (sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_EXAMINING) {
            sharedVariables.mygame[gameData.BoardIndex].clearShapes();
            sharedVariables.mygame[gameData.BoardIndex].computeHash();
      }
      if (sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_EXAMINING || sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_OBSERVING || sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_PLAYING) {
	sharedVariables.mygame[gameData.BoardIndex].engineTop -=num;
        if (sharedVariables.mygame[gameData.BoardIndex].engineTop < 0)
          sharedVariables.mygame[gameData.BoardIndex].engineTop = 0;

      }

       if ((sharedVariables.mygame[gameData.BoardIndex].state ==
          sharedVariables.STATE_EXAMINING || sharedVariables.mygame[gameData.BoardIndex].state ==                
          sharedVariables.STATE_OBSERVING) &&
          sharedVariables.engineOn == true && sharedVariables.engineBoard == gameData.BoardIndex) {
        if (sharedVariables.uci == false) {
          for (int z=0; z < num; z++) {
            myoutput outgoing = new myoutput();
            outgoing.data = "undo\n";
            sharedVariables.engineQueue.add(outgoing);
          }
          // reduce engineTop ( the number of moves we have for this
          // game stored for the engine)
        }// end uci false
        else
          sendUciMoves();
      }

      if (sharedVariables.mygame[gameData.BoardIndex].movetop < 0)
        sharedVariables.mygame[gameData.BoardIndex].movetop=0;

     try {

                sharedVariables.mygametable[gameData.BoardIndex].removeMoves
                  (sharedVariables.mygame[gameData.BoardIndex].movetop + num, num);

              } catch (Exception e1) {
                //ignore
              }

     /* try {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
              public void run() {
              try {

                sharedVariables.mygametable[gameData.BoardIndex].removeMoves
                  (sharedVariables.mygame[gameData.BoardIndex].movetop + num, num);

              } catch (Exception e1) {
                //ignore
              }
            }
          });
      }//end try
      catch(Exception dumb) {}
       */
      sharedVariables.mygame[gameData.BoardIndex].replay();
      if(sharedVariables.mygame[gameData.BoardIndex].state == sharedVariables.STATE_EXAMINING)
      {
          sharedVariables.mygame[gameData.BoardIndex].computeHash();
      }
      if (isVisible() == true)
        repaintCustom();
    }
  }
  
  void parseCrazyHoldings(String icsGameNumber, String whiteHoldings,
                          String blackHoldings) {
    int tempnumber=getGameNumber(icsGameNumber);

    if (tempnumber == sharedVariables.mygame[gameData.BoardIndex].myGameNumber) {
      sharedVariables.mygame[gameData.BoardIndex].crazypieces[1] =
        getCrazyPieceCount(whiteHoldings, 'P');
      sharedVariables.mygame[gameData.BoardIndex].crazypieces[2] =
        getCrazyPieceCount(whiteHoldings, 'N');
      sharedVariables.mygame[gameData.BoardIndex].crazypieces[3] =
        getCrazyPieceCount(whiteHoldings, 'B');
      sharedVariables.mygame[gameData.BoardIndex].crazypieces[4] =
        getCrazyPieceCount(whiteHoldings, 'R');
      sharedVariables.mygame[gameData.BoardIndex].crazypieces[5] =
        getCrazyPieceCount(whiteHoldings, 'Q');

      sharedVariables.mygame[gameData.BoardIndex].crazypieces[7] =
        getCrazyPieceCount(blackHoldings, 'P');
      sharedVariables.mygame[gameData.BoardIndex].crazypieces[8] =
        getCrazyPieceCount(blackHoldings, 'N');
      sharedVariables.mygame[gameData.BoardIndex].crazypieces[9] =
        getCrazyPieceCount(blackHoldings, 'B');
      sharedVariables.mygame[gameData.BoardIndex].crazypieces[10] =
        getCrazyPieceCount(blackHoldings, 'R');
      sharedVariables.mygame[gameData.BoardIndex].crazypieces[11] =
        getCrazyPieceCount(blackHoldings, 'Q');

    }
  }

  int getCrazyPieceCount(String holdings, char mychar) {
    int n=0;

    for (int z=0; z<holdings.length(); z++)
      if (holdings.charAt(z)==mychar)
        n++;

    return n;
  }
  
  int getxmove(String amove, int toggle) {
    int x=0;
    //crazyhouse moves
    if (toggle == 0 && amove.charAt(1) == '@') {

      if (amove.charAt(0 + toggle) == 'P')
        x=-1;
      if (amove.charAt(0 + toggle) == 'N')
        x=-2;
      if (amove.charAt(0 + toggle) == 'B')
        x=-3;
      if (amove.charAt(0 + toggle) == 'R')
        x=-4;
      if (amove.charAt(0 + toggle) == 'Q')
        x=-5;
      if (sharedVariables.mygame[gameData.BoardIndex].turn%2==1)
        x=x-6;
      if (amove.charAt(0 + toggle) == 'x' || amove.charAt(0 + toggle) == 'X')
        x=sharedVariables.mygame[gameData.BoardIndex].blank;
      return x;
    }


    if (amove.charAt(0 + toggle) == 'a')
      x=7;
    if (amove.charAt(0 + toggle) == 'b')
      x=6;
    if (amove.charAt(0 + toggle) == 'c')
      x=5;
    if (amove.charAt(0 + toggle) == 'd')
      x=4;
    if (amove.charAt(0 + toggle) == 'e')
      x=3;
    if (amove.charAt(0 + toggle) == 'f')
      x=2;
    if (amove.charAt(0 + toggle) == 'g')
      x=1;
    if (amove.charAt(0 + toggle) == 'h')
      x=0;

    if (sharedVariables.mygame[gameData.BoardIndex].iflipped == 1)
      return 7-x;

    return x;

  }


  int getymove(String amove, int toggle) {
    if (amove.charAt(1 + toggle) == '1')
      return 0;
    if (amove.charAt(1 + toggle) == '2')
      return 1;
    if (amove.charAt(1 + toggle) == '3')
      return 2;
    if (amove.charAt(1 + toggle) == '4')
      return 3;
    if (amove.charAt(1 + toggle) == '5')
      return 4;
    if (amove.charAt(1 + toggle) == '6')
      return 5;
    if (amove.charAt(1 + toggle) == '7')
      return 6;
    if (amove.charAt(1 + toggle) == '8')
      return 7;

    return 0;
  }

  void setautoexamon() {
    sharedVariables.autoexam = 1;
    //if(autotimer != null)
    //timer.cancel();
    autotimer = new Timer (  ) ;
    //autotimer.scheduleAtFixedRate( new AutoExamTask ( ) ,
    //sharedVariables.autoexamspeed ,sharedVariables.autoexamspeed) ;
    autotimer.schedule( new AutoExamTask (  ) ,sharedVariables.autoexamspeed) ;

    myspeed=sharedVariables.autoexamspeed;
  }

  void setautoexamoff() {
    sharedVariables.autoexam=0;
  }

  

  /* component listener */
  public void componentHidden(ComponentEvent e) {

  }

  public void componentMoved(ComponentEvent e) {

  }

  public void componentResized(ComponentEvent e) {
  
    if (isVisible() == true)
      recreate();
    if (!isMaximum())
      setBoardSize();
    //JFrame framer = new JFrame("hi");
    //framer.setSize(200,100);
    //framer.setVisible(true);

  }

  public void componentShown(ComponentEvent e) {

  }


  /************** jinternal frame listener ******************************/

  void setBoardSize() {
       
    if (sharedVariables.useTopGames == true) {
      //topGame.setBoardSize();
      return;
    }
	
    if (getWidth() < 50)
      return;

    sharedVariables.myBoardSizes[gameData.BoardIndex].point0=getLocation();
    //set_string = set_string + "" + point0.x + " " + point0.y + " ";
    sharedVariables.myBoardSizes[gameData.BoardIndex].con0x=getWidth();
    sharedVariables.myBoardSizes[gameData.BoardIndex].con0y=getHeight();
    //set_string = set_string + "" + con0x + " " + con0y + " ";
  }
  
  public void internalFrameClosing(InternalFrameEvent e) {
    // we want to serialize the window dimensions

    if (sharedVariables.useTopGames == true)
      return;

    if (isVisible() && isMaximum() == false && isIcon() == false) {
      setBoardSize();
    }

    if (myconsolepanel.Input.hasFocus() && myconsolepanel.myself!=null)
      myconsolepanel.myself.switchConsoleWindows();
    
    if(isVisible() && sharedVariables.engineOn) {
        stopTheEngine();
      }

    setVisible(false);
    if (sharedVariables.mygame[gameData.LookingAt].state !=
        sharedVariables.STATE_PLAYING) {
      myoutput data = new myoutput();
      data.closetab=getPhysicalTab(gameData.LookingAt);
      queue.add(data);

    }
      timerSafeCancel();
      myoutput data2 = new myoutput();
      data2.boardClosing= gameData.BoardIndex;
      queue.add(data2);

  }

  public void internalFrameClosed(InternalFrameEvent e) {

  }

  public void internalFrameOpened(InternalFrameEvent e) {

  }

  public void internalFrameIconified(InternalFrameEvent e) {

  }

  public void internalFrameDeiconified(InternalFrameEvent e) {
    if (sharedVariables.useTopGames == true)
      return;

    if (isVisible() && isMaximum() == false && isIcon() == false) {
      setBoardSize();
    }
  }

  public void internalFrameActivated(final InternalFrameEvent e) {
    // System.out.println("fame activate");
    if (sharedVariables.useTopGames == true)
      return;
    if (isVisible() == true) {
      // let this be the sound board. whatever tab its on is the game with sound
      myoutput output = new myoutput();
      output.soundBoard=gameData.BoardIndex;
      queue.add(output);
    }

    if (isVisible() && isMaximum() == false && isIcon() == false) {
      setBoardSize();
    }
    giveFocus();
  }

  public void internalFrameDeactivated(InternalFrameEvent e) {
    if (sharedVariables.useTopGames == true)
      return;

    myconsolepanel.Input.setFocusable(false);

  }


  /*
  public void windowClosing(WindowEvent e) {
    // we want to serialize the window dimensions

    if (isVisible() && isMaximum() == false && isIcon() == false) {
      setBoardSize();
    }

    if (myconsolepanel.Input.hasFocus() && myconsolepanel.myself!=null)
      myconsolepanel.myself.switchConsoleWindows();

    setVisible(false);
    if (sharedVariables.mygame[gameData.LookingAt].state !=
        sharedVariables.STATE_PLAYING) {
      myoutput data = new myoutput();
      data.closetab=gameData.LookingAt;
      queue.add(data);
    }
  }

  public void windowClosed(WindowEvent e) {

  }

  public void windowOpened(WindowEvent e) {

  }

  public void windowIconified(WindowEvent e) {

  }

  public void windowDeiconified(WindowEvent e) {
    if(isVisible() && isMaximum() == false && isIcon() == false) {
      setBoardSize();
    }
  }

  public void windowActivated(WindowEvent e) {
    if (isVisible() && isMaximum() == false && isIcon() == false) {
      setBoardSize();
    }
    giveFocus();
  }

  public void windowDeactivated(WindowEvent e) {
    myconsolepanel.Input.setFocusable(false);
  }

  public void windowGainedFocus(WindowEvent e) {

  }

  public void windowLostFocus(WindowEvent e) {

  }

  public void windowStateChanged(WindowEvent e) {

  }

  boolean isMaximum() {
    return false;
  }

  boolean isIcon() {
    return false;
  }

  void setMaximum(boolean home) {
    return;
  }
  */

  void giveFocus() {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
          public void run() {
          try {
            //JComponent comp = DataViewer.getSubcomponentByName(e.getInternalFrame(),
            //SearchModel.SEARCHTEXT);

            myconsolepanel.Input.setFocusable(true);
            myconsolepanel.Input.setRequestFocusEnabled(true);
            //Input.requestFocus();
            myconsolepanel.Input.requestFocusInWindow();

          } catch (Exception e1) {
            //ignore
          }
        }
      });
  }

  int getPhysicalTab(int look) {
    for (int a=0; a<sharedVariables.openBoardCount; a++)
      if (sharedVariables.tabLooking[a]==look)
        return a;
 
    return look;
  }

  /****************************************************************************************/

}// end class

class randomPieces {
  Random randomGenerator = new Random();
  int blackPieceNum;
  int whitePieceNum;
  int boardNum;
  channels SharedVariables;
  boolean [] excludedPiecesWhite;
   boolean [] excludedPiecesBlack;
    boolean [] excludedBoards;
  randomPieces(boolean [] excludedPieces1, boolean [] excludedPieces2, boolean [] excludedBoards1) {
    excludedPiecesWhite=excludedPieces1;
    excludedPiecesBlack=excludedPieces2;
    excludedBoards = excludedBoards1;
    blackPieceNum=0;
    whitePieceNum=0;
    boardNum=0;
  }

 void randomizeGraphics() {
   resourceClass temp = new resourceClass();

   int maxDepth=5;
   whitePieceNum = getChoiceWhite(randomGenerator.nextInt(getMaxPieceChoiceWhite()), -1, maxDepth);
   blackPieceNum = getChoiceBlack(randomGenerator.nextInt(getMaxPieceChoiceBlack() - 1),
                             whitePieceNum, maxDepth);

  // boardNum = randomGenerator.nextInt(temp.maxBoards);
  boardNum =   getChoiceBoard(randomGenerator.nextInt(getMaxBoardChoice()),
                             boardNum, maxDepth);
 }

 int getMaxPieceChoiceWhite() {
   int x=0;
   for (int y=0; y<excludedPiecesWhite.length; y++)
     if (excludedPiecesWhite[y]==false)
       x++;

   if (x > 2)
     return x;

   return 2;
 }
 
 int getMaxBoardChoice() {
   int x=0;
   for (int y=0; y<excludedBoards.length; y++)
     if (excludedBoards[y]==false)
       x++;

   if (x > 2)
     return x;

   return 2;
 }

 int getMaxPieceChoiceBlack() {
   int x=0;
   for (int y=0; y<excludedPiecesBlack.length; y++)
     if (excludedPiecesBlack[y]==false)
       x++;

   if (x > 2)
     return x;

   return 2;
 }


 int getChoiceWhite(int num, int otherset, int depth) {
   int i=0;
   int y;
   for (y=0; y<excludedPiecesWhite.length; y++) {
     if (excludedPiecesWhite[y] == false && y != otherset)
     {  if (i==num)
         return y;


       i++;
   }
   }     // end for

         if(i==num && depth > 0)
       return getChoiceWhite(randomGenerator.nextInt(getMaxPieceChoiceWhite()), -1, depth-1);
     // end for

   return 0;
 }     // end function

  int getChoiceBlack(int num, int otherset, int depth) {
   int i=0;
   int y;

   for (y=0; y<excludedPiecesBlack.length; y++) {
     if (excludedPiecesBlack[y] == false && y != otherset)
    {   if (i==num)
         return y;


       i++;
   }
   }     // end for

            if( depth > 0)
       return getChoiceBlack(randomGenerator.nextInt(getMaxPieceChoiceBlack() - 1),
                      otherset, depth-1);





   return 0;
 }     // end function

  int getChoiceBoard(int num, int otherset, int depth) {
   int i=0;
   int y;
   for (y=0; y<excludedBoards.length; y++) {
     if (excludedBoards[y] == false)
     {  if (i==num)
         return y;


       i++;
   }
   }     // end for

         if(i==num && depth > 0)
       return getChoiceBoard(randomGenerator.nextInt(getMaxBoardChoice()), -1, depth-1);
     // end for

   return 0;
 }     // end function






} // end grpahics sub class
class TimeDisplayClass
{
channels sharedVariables;
TimeDisplayClass(channels sharedVariables1)
{
 sharedVariables=sharedVariables1;
}


   String getWhiteTimeDisplay(int Looking)
   {
   String text="";
        int wsecI = sharedVariables.mygame[Looking].whiteSecond;
        int wminI = sharedVariables.mygame[Looking].whiteMinute;

        String	wsec="" + Math.abs(wsecI);
        String	wmin="" + Math.abs(wminI);


           if (sharedVariables.showTenths == 1) {
          boolean goTenth=false;
          if (sharedVariables.mygame[Looking].whiteMinute==0 &&
              wsecI < 15 &&
              sharedVariables.mygame[Looking].whiteSecond > -1)
            wsec=wsec + "." + sharedVariables.mygame[Looking].whiteTenth;


        } else if(sharedVariables.showTenths == 2) {// always
          wsec=wsec + "." + sharedVariables.mygame[Looking].whiteTenth;

        }

        if (sharedVariables.mygame[Looking].whiteSecond >=0 &&
            sharedVariables.mygame[Looking].whiteMinute >= 0 &&
            sharedVariables.mygame[Looking].wtime>=0 ) {
          if (sharedVariables.mygame[Looking].whiteSecond < 10 &&
              sharedVariables.mygame[Looking].whiteSecond > -10)
            text =  " " + wmin + ":0" + wsec;
          else
            text = " " + wmin + ":" + wsec;
        } else {
          if (sharedVariables.mygame[Looking].whiteSecond < 10 &&
              sharedVariables.mygame[Looking].whiteSecond > -10)
            text = "-" + wmin + ":0" + wsec;
          else
            text = "-" + wmin + ":" + wsec;
        }





   return text;
   }
String getBlackTimeDisplay(int Looking)
 {

String text="";
        int bsecI = sharedVariables.mygame[Looking].blackSecond;
       int bminI = sharedVariables.mygame[Looking].blackMinute;
         String	bsec="" + Math.abs(bsecI);
        String  bmin="" + Math.abs(bminI);


            if (sharedVariables.showTenths == 1) {
          boolean goTenth=false;

          if (sharedVariables.mygame[Looking].blackMinute==0 &&
              bsecI < 15 &&
              sharedVariables.mygame[Looking].blackSecond > -1)
            bsec=bsec + "." + sharedVariables.mygame[Looking].blackTenth;

        } else if(sharedVariables.showTenths == 2) {// always

          bsec=bsec + "." + sharedVariables.mygame[Looking].blackTenth;
        }



           if (sharedVariables.mygame[Looking].blackSecond >=0 &&
            sharedVariables.mygame[Looking].blackMinute >= 0 &&
            sharedVariables.mygame[Looking].btime >=0) {

          if (sharedVariables.mygame[Looking].blackSecond<10 &&
              sharedVariables.mygame[Looking].blackSecond > -10)
            text = " " + bmin+ ":0" + bsec;
          else
            text = " " + bmin + ":" + bsec;

        } else {

          if (sharedVariables.mygame[Looking].blackSecond<10 &&
              sharedVariables.mygame[Looking].blackSecond > -10)
            text = "-" + bmin+ ":0" + bsec;
          else
            text = "-" + bmin + ":" + bsec;

        }

        return text;
        }           // end method get black time


}