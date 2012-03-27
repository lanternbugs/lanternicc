package lantern;
/*
*  Copyright (C) 2012 Michael Ronald Adams, Andrey Gorlin.
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
import java.awt.*;
import java.awt.Window.*;
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
import java.util.Queue; // added by Andrey
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.ArrayList;
import java.util.StringTokenizer;

//import javax.jnlp.*;


//public class multiframe  extends JApplet
public class multiframe {
  
  public static void createFrame() {
    Frame frame = new Frame();
    frame.setBounds(100, 100, 300, 300);
    frame.show();

    double size[][] =
      {{0.25, 0.25, 0.25, 0.25},
       {50, TableLayout.FILL, 40, 40, 40}};

    frame.setLayout (new TableLayout(size));
  }

  public static void main(String[] args) {
    //public void init()

    //{
    try {

      String os = System.getProperty("os.name").toLowerCase();
      if (os.indexOf( "win" ) >= 0)
	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	// UIManager.setLookAndFeel( "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
      else
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.SystemLookAndFeel");
      
    } catch(Exception d) {}

    final mymultiframe frame = new mymultiframe();
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    //DO_NOTHING_ON_CLOSE

    frame.setTitle("Lantern Chess " + frame.sharedVariables.version);
    frame.setVisible(true);

    // uncomment below line to test name and pass saving
    //passTest tester = new passTest();


    //frame.setDefaultLookAndFeelDecorated(false);

    SwingUtilities.invokeLater(new Runnable() {
        @Override
          public void run() {
          try {
            frame.repaintTabs();
          } catch (Exception e1) {
            //ignore
          }
        }
      });


    try {
      frame.consoleSubframes[0].setSelected(true);
    } catch(Exception dd) {}

    // warning dialogue

    String swarning="This is a beta version of Mike's new Interface.  Game play is possible but it's highly recommended you play unrated.  I want more testing before rated play can happen.  Not all wilds are supported.";

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
    } catch(Exception badopen) {}

    try {
      if (frame.sharedVariables.seeksOpen &&
          !frame.sharedVariables.activitiesNeverOpen)
        frame.openSeekGraph();
    } catch(Exception badopen) {}

    try {
      frame.myConnection =
        new connectionDialog(frame, frame.sharedVariables, frame.queue, false);
      frame.myConnection.setVisible(true);

    } catch(Exception bfocus) {}
  }
}// end main class

class mymultiframe extends JFrame
  implements ActionListener, ChangeListener, WindowListener {
                                            //WindowFocusListener,
                                           // WindowStateListene
  connectionDialog myConnection;
  seekGraphFrame seekGraph;
  JToolBar toolBar;
  docWriter myDocWriter;
  listFrame myfirstlist;
  listInternalFrame mysecondlist;
  notifyFrame myNotifyFrame;
  gameFrame myGameList;
  listClass eventsList;
  listClass seeksList;
  listClass computerSeeksList;

  saveScores mineScores;

  listClass notifyList;
  tableClass gameList;
  webframe mywebframe;
  channels sharedVariables;
  private JTextPane[] consoles;
  private JTextPane[] gameconsoles;
  protected JColorChooser tcc;
  int colortype;

  // Andrey says:
  // organize these in an intuitive manner
  JCheckBoxMenuItem autonoidle;
  JCheckBoxMenuItem autobufferchat;
  JCheckBoxMenuItem autoHistoryPopup;
  JCheckBoxMenuItem makeObserveSounds;
  JCheckBoxMenuItem hearsound;
  JCheckBoxMenuItem gameend;
  JCheckBoxMenuItem channelNumberLeft;
  JCheckBoxMenuItem tabbing;
  JCheckBoxMenuItem BoardDesign1;
  JCheckBoxMenuItem BoardDesign2;
  JCheckBoxMenuItem BoardDesign3;
  JCheckBoxMenuItem tellswitch;
  JCheckBoxMenuItem highlight;
  JCheckBoxMenuItem materialCount;
  JCheckBoxMenuItem drawCoordinates;
  JCheckBoxMenuItem showRatings;
  JCheckBoxMenuItem showFlags;
  JCheckBoxMenuItem showPallette;
  JCheckBoxMenuItem autoChat;
  JCheckBoxMenuItem lowTimeColors;
  JCheckBoxMenuItem newObserveGameSwitch;
  JCheckBoxMenuItem blockSays;
  JCheckBoxMenuItem useLightBackground;
  JCheckBoxMenuItem boardconsole0;
  JCheckBoxMenuItem boardconsole1;
  JCheckBoxMenuItem boardconsole2;
  JCheckBoxMenuItem boardconsole3;
  JCheckBoxMenuItem sidewaysconsole;
  JCheckBoxMenuItem playersInMyGame;
  JCheckBoxMenuItem unobserveGoExamine;

  JCheckBoxMenuItem userbuttons;
  JCheckBoxMenuItem consolemenu;

  JCheckBoxMenuItem toolbarvisible;
  JCheckBoxMenuItem lineindent;

  JCheckBoxMenuItem tabLayout1;
  JCheckBoxMenuItem tabLayout2;
  JCheckBoxMenuItem tabLayout3;

  JCheckBoxMenuItem shoutTimestamp;
  JCheckBoxMenuItem tellTimestamp;
  JCheckBoxMenuItem channelTimestamp;
  JCheckBoxMenuItem leftNameTimestamp;
  JCheckBoxMenuItem reconnectTimestamp;
  JCheckBoxMenuItem qtellTimestamp;
  JCheckBoxMenuItem checkLegality;
  JCheckBoxMenuItem useTopGame;
  JCheckBoxMenuItem notifyMainAlso;

  JCheckBoxMenuItem aspect0;
  JCheckBoxMenuItem aspect1;
  JCheckBoxMenuItem aspect2;
  JCheckBoxMenuItem aspect3;
  JCheckBoxMenuItem woodenboard1;
  JCheckBoxMenuItem woodenboard2;
  JCheckBoxMenuItem woodenboard3;
  JCheckBoxMenuItem grayishboard;
  JCheckBoxMenuItem solidboard;
  JCheckBoxMenuItem oliveboard;
  JCheckBoxMenuItem cherryboard;
  JCheckBoxMenuItem purpleboard;

  JCheckBoxMenuItem board5;
  JCheckBoxMenuItem board6;
  JCheckBoxMenuItem board7;

  JCheckBoxMenuItem pieces1;
  JCheckBoxMenuItem pieces2;
  JCheckBoxMenuItem pieces3;
  JCheckBoxMenuItem pieces4;
  JCheckBoxMenuItem pieces5;
  JCheckBoxMenuItem pieces6;
  JCheckBoxMenuItem pieces7;
  JCheckBoxMenuItem pieces8;
  JCheckBoxMenuItem pieces9;
  JCheckBoxMenuItem pieces10;
  JCheckBoxMenuItem pieces11;
  JCheckBoxMenuItem pieces12;
  JCheckBoxMenuItem pieces13;
  JCheckBoxMenuItem pieces14;
  JCheckBoxMenuItem pieces15;
  JCheckBoxMenuItem pieces16;
  JCheckBoxMenuItem pieces17;
  JCheckBoxMenuItem pieces18;
  JCheckBoxMenuItem pieces19;
  JCheckBoxMenuItem pieces20;
  JCheckBoxMenuItem pieces21;
  JCheckBoxMenuItem pieces22;
  JCheckBoxMenuItem pieces23;
  JCheckBoxMenuItem pieces24;

  JCheckBoxMenuItem[] italicsBehavior = new JCheckBoxMenuItem[3];

  JCheckBoxMenuItem randomArmy;
  JCheckBoxMenuItem randomTiles;

  JCheckBoxMenuItem iloggedon;
  JCheckBoxMenuItem rotateaways;
  JCheckBoxMenuItem notifysound;
  JCheckBoxMenuItem qsuggestPopup;
  JCheckBoxMenuItem autopopup;
  JCheckBoxMenuItem basketballFlag;

  JCheckBoxMenuItem pgnlogging;
  JCheckBoxMenuItem pgnObservedLogging;
  JCheckBoxMenuItem compactNameList;
  
  JMenuItem preset0;
  JMenuItem preset1;
  JMenuItem preset2;
  JMenuItem preset3;
  JMenuItem reconnect2;
  
  createWindows mycreator;
  resourceClass graphics;
  Runtime rt;

  ConcurrentLinkedQueue<myoutput> queue;
  // Andrey says:
  // want to be able to change this to:
  //Queue<myoutput> queue;

  chessbot4 client;
  gameboard[] myboards;
  Image[] img;
  ConcurrentLinkedQueue<newBoardData> gamequeue;
  // Andrey says:
  // want to be able to change this to:
  //Queue<newBoardData> gamequeue;
  subframe[] consoleSubframes;
  chatframe[] consoleChatframes;

  settings mysettings;


  class MyFocusTraversalPolicy extends ContainerOrderFocusTraversalPolicy {

    protected boolean accept(Component aComp) {
      if (aComp instanceof subframe || aComp instanceof gameboard)
        return super.accept(aComp);

      return false; // JLabel and JPanel.
    }
  }
  
  mymultiframe() {

    graphics = new resourceClass();
    gamequeue = new ConcurrentLinkedQueue<newBoardData>();

    sharedVariables = new channels();
    sharedVariables.useTopGames = getOnTopSetting();
    queue = new ConcurrentLinkedQueue<myoutput>();

    seekGraph = new seekGraphFrame(sharedVariables, queue);
    
    try {
      seekGraph.setSize(sharedVariables.mySeekSizes.con0x,
                        sharedVariables.mySeekSizes.con0y);
      seekGraph.setLocation(sharedVariables.mySeekSizes.point0.x,
                            sharedVariables.mySeekSizes.point0.y);
    } catch (Exception duiseeek) {}

    myboards = new gameboard[sharedVariables.maxGameTabs];
    for (int bbo=0; bbo<sharedVariables.maxGameTabs; bbo++)
      myboards[bbo] = null;

    img = new Image[14];
    mineScores = new saveScores();

    mysettings = new settings(sharedVariables);


    sharedVariables.img=img;

    consoles = new JTextPane[sharedVariables.maxConsoleTabs];
    gameconsoles = new JTextPane[sharedVariables.maxGameConsoles];
    consoleSubframes = new subframe[sharedVariables.maxConsoles];
    consoleChatframes = new chatframe[sharedVariables.maxConsoles];

    colortype = 1;
    sharedVariables.desktop = new JDesktopPaneCustom(sharedVariables, myboards,
                                                     consoleSubframes, this);
    sharedVariables.desktop.add(seekGraph);


    setFocusCycleRoot(true);
    setFocusTraversalPolicy(new MyFocusTraversalPolicy());

    String myweburl = "www.google.com";
    mywebframe = new webframe(sharedVariables,  queue, myweburl);
    sharedVariables.desktop.add(mywebframe);
    try {
      mywebframe.setSize(sharedVariables.webframeWidth, sharedVariables.webframeHeight);
    } catch(Exception d) {};

    /*************** trying list code *****************/
    notifyList = new listClass("Notify List");
    eventsList = new listClass("Events");
    seeksList = new listClass("Human Seeks");
    computerSeeksList = new listClass("Computer Seeks");
    sharedVariables.activitiesPanel =
      new ActivitiesWindowPanel(this, sharedVariables, queue, eventsList,
                                seeksList, computerSeeksList, notifyList, this);
    myfirstlist = new listFrame(this, sharedVariables, queue);
    mysecondlist = new listInternalFrame(this, sharedVariables, queue);
    sharedVariables.desktop.add(mysecondlist);
    myNotifyFrame = new notifyFrame(this, sharedVariables, queue,  notifyList);
    gameList = new tableClass();
    myGameList = new gameFrame(sharedVariables, queue, gameList);
    sharedVariables.myGameList=myGameList;
    myGameList.setSize(600,425);
    //sharedVariables.desktop.add(myGameList);

    /*************** end list code******************/
    docWriter myDocWriter = new docWriter(sharedVariables, consoleSubframes, consoles,
                                          gameconsoles, myboards, consoleChatframes);
    mycreator =
      new createWindows(sharedVariables, consoleSubframes ,myboards, consoles, gameconsoles,
                        queue, img, gamequeue, mywebframe, graphics, myfirstlist,
                        mysecondlist, myDocWriter, consoleChatframes, this);
    mycreator.createConsoleFrame(); //Create first window
    mycreator.createGameFrame();
    //mycreator.createListFrame(eventsList, seeksList, computerSeeksList, notifyList, this);
    //setContentPane(sharedVariables.desktop);
    // add window listener so we can close an engine if open
    addWindowListener(this);
    getContentPane().add(sharedVariables.desktop, "Center");

    getSettings();
    sharedVariables.hasSettings =
      mysettings.readNow(myboards, consoleSubframes,
                         sharedVariables, consoles, gameconsoles);
    // read  for any saved settings don't know what get settings is doing MA 5-30-10
    mineScores.readNow(sharedVariables);

    client = new chessbot4(gameconsoles, gamequeue, queue, consoles, sharedVariables,
                           myboards, consoleSubframes, mycreator, graphics, eventsList,
                           seeksList, computerSeeksList, notifyList, gameList,
                           myGameList, this, consoleChatframes, seekGraph, this,
                           myConnection, myfirstlist, mysecondlist);
    repaint();
    client.enabletimestamp();

    loadGraphicsStandAlone();
    loadSoundsStandAlone();
    //loadGraphicsApplet();
    //loadSoundsApplet();

    // we look if these files exist and if we do load name pass then
    // any commands into script array list for connect to run MA
    // 1-1-11
    try {

      scriptLoader loadScripts = new  scriptLoader();
      loadScripts.loadScript(sharedVariables.iccLoginScript, "lantern_icc.ini");
      loadScripts.loadScript(sharedVariables.ficsLoginScript, "lantern_fics.ini");
      loadScripts.loadScript(sharedVariables.notifyControllerScript,
                             sharedVariables.notifyControllerFile);

    } catch (Exception scriptErrror) {}
    
    setUpChannelNotify();
    setUpLanternNotify();
    parseCountries();
    Thread t = new Thread(client);
    t.start();


    createMenu();
    consoleSubframes[0].makeHappen(0);
    /*
    if (sharedVariables.highlightMoves == false)
      highlight.setSelected(false);
    else
      highlight.setSelected(true);

    if (sharedVariables.gameend == false)
      gameend.setSelected(false);
    else
      gameend.setSelected(true);

    if (sharedVariables.notifyMainAlso == true)
      notifyMainAlso.setSelected(true);
    else
      notifyMainAlso.setSelected(false);

    if (sharedVariables.useTopGames == true)
      useTopGame.setSelected(true);
    else
      useTopGame.setSelected(false);

    if (sharedVariables.showMaterialCount == false)
      materialCount.setSelected(false);
    else
      materialCount.setSelected(true);

    if (sharedVariables.drawCoordinates == false)
      drawCoordinates.setSelected(false);
    else
      drawCoordinates.setSelected(true);

    if (sharedVariables.showRatings == false)
      showRatings.setSelected(false);
    else
      showRatings.setSelected(true);

    if (sharedVariables.showFlags == false)
      showFlags.setSelected(false);
    else
      showFlags.setSelected(true);

    if (sharedVariables.showPallette == false)
      showPallette.setSelected(false);
    else
      showPallette.setSelected(true);

    if (sharedVariables.autoChat == false)
      autoChat.setSelected(false);
    else
      autoChat.setSelected(true);
    
    if (sharedVariables.lowTimeColors == false)
      lowTimeColors.setSelected(false);
    else
      lowTimeColors.setSelected(true);
    
    if (sharedVariables.newObserveGameSwitch == false)
      newObserveGameSwitch.setSelected(false);
    else
      newObserveGameSwitch.setSelected(true);

    if (sharedVariables.blockSays == false)
      blockSays.setSelected(false);
    else
      blockSays.setSelected(true);

    if (sharedVariables.useLightBackground == false)
      useLightBackground.setSelected(false);
    else
      useLightBackground.setSelected(true);
    */

    highlight.           setSelected(sharedVariables.highlightMoves);
    gameend.             setSelected(sharedVariables.gameend);
    notifyMainAlso.      setSelected(sharedVariables.notifyMainAlso);
    useTopGame.          setSelected(sharedVariables.useTopGames);
    materialCount.       setSelected(sharedVariables.showMaterialCount);
    drawCoordinates.     setSelected(sharedVariables.drawCoordinates);
    showRatings.         setSelected(sharedVariables.showRatings);
    showFlags.           setSelected(sharedVariables.showFlags);
    showPallette.        setSelected(sharedVariables.showPallette);
    autoChat.            setSelected(sharedVariables.autoChat);
    lowTimeColors.       setSelected(sharedVariables.lowTimeColors);
    newObserveGameSwitch.setSelected(sharedVariables.newObserveGameSwitch);
    blockSays.           setSelected(sharedVariables.blockSays);
    useLightBackground.  setSelected(sharedVariables.useLightBackground);
    
    setPieces(sharedVariables.pieceType);
    setBoard(sharedVariables.boardType);
    /*
    if (sharedVariables.pgnObservedLogging == true)
      pgnObservedLogging.setSelected(true);
    else
      pgnObservedLogging.setSelected(false);

    if (sharedVariables.pgnLogging == true)
      pgnlogging.setSelected(true);
    else
      pgnlogging.setSelected(false);

    if (sharedVariables.switchOnTell == true)
      tellswitch.setSelected(true);
    else
      tellswitch.setSelected(false);
    
    if (sharedVariables.toolbarVisible == true)
      toolbarvisible.setSelected(true);
    else
      toolbarvisible.setSelected(false);

    if (sharedVariables.autoBufferChat == false)
      autobufferchat.setSelected(false);
    else
      autobufferchat.setSelected(true);
    
    if (sharedVariables.channelNumberLeft == false)
      channelNumberLeft.setSelected(false);
    else
      channelNumberLeft.setSelected(true);
    
    if (sharedVariables.channelTimestamp == true)
      channelTimestamp.setSelected(true);
    else
      channelTimestamp.setSelected(false);
    
    if (sharedVariables.shoutTimestamp == true)
      shoutTimestamp.setSelected(true);
    else
      shoutTimestamp.setSelected(false);
    
    if (sharedVariables.qtellTimestamp == true)
      qtellTimestamp.setSelected(true);
    else
      qtellTimestamp.setSelected(false);
    
    if (sharedVariables.reconnectTimestamp == true)
      reconnectTimestamp.setSelected(true);
    else
      reconnectTimestamp.setSelected(false);

    if(sharedVariables.andreysLayout == 0)
      BoardDesign1.setSelected(true);
    else
      BoardDesign1.setSelected(false);

    if (sharedVariables.andreysLayout == 1)
      BoardDesign2.setSelected(true);
    else
      BoardDesign2.setSelected(false);

    if (sharedVariables.andreysLayout == 2)
      BoardDesign3.setSelected(true);
    else
      BoardDesign3.setSelected(false);

    if (sharedVariables.playersInMyGame == 2)
      playersInMyGame.setSelected(true);
    else
      playersInMyGame.setSelected(false);

    if(sharedVariables.unobserveGoExamine == true)
      unobserveGoExamine.setSelected(true);
    else
      unobserveGoExamine.setSelected(false);

    if (sharedVariables.tellTimestamp == true)
      tellTimestamp.setSelected(true);
    else
      tellTimestamp.setSelected(false);
    
    if (sharedVariables.leftTimestamp == true)
      leftNameTimestamp.setSelected(true);
    else
      leftNameTimestamp.setSelected(false);

    if (sharedVariables.checkLegality == true)
      checkLegality.setSelected(true);
    else
      checkLegality.setSelected(false);

    if (sharedVariables.indent == true)
      lineindent.setSelected(true);
    else
      lineindent.setSelected(false);
    */
      
    pgnObservedLogging.  setSelected(sharedVariables.pgnObservedLogging);
    pgnlogging.          setSelected(sharedVariables.pgnLogging);
    tellswitch.          setSelected(sharedVariables.switchOnTell);
    toolbarvisible.      setSelected(sharedVariables.toolbarVisible);
    autobufferchat.      setSelected(sharedVariables.autoBufferChat);
    channelNumberLeft.   setSelected(sharedVariables.channelNumberLeft);
    channelTimestamp.    setSelected(sharedVariables.channelTimestamp);
    shoutTimestamp.      setSelected(sharedVariables.shoutTimestamp);
    qtellTimestamp.      setSelected(sharedVariables.qtellTimestamp);
    reconnectTimestamp.  setSelected(sharedVariables.reconnectTimestamp);
    BoardDesign1.        setSelected((sharedVariables.andreysLayout == 0));
    BoardDesign2.        setSelected((sharedVariables.andreysLayout == 1));
    BoardDesign3.        setSelected((sharedVariables.andreysLayout == 2));
    playersInMyGame.     setSelected((sharedVariables.playersInMyGame == 2));
    unobserveGoExamine.  setSelected(sharedVariables.unobserveGoExamine);
    tellTimestamp.       setSelected(sharedVariables.tellTimestamp);
    leftNameTimestamp.   setSelected(sharedVariables.leftTimestamp);
    checkLegality.       setSelected(sharedVariables.checkLegality);
    lineindent.          setSelected(sharedVariables.indent);
    
    checkItalicsBehavior(sharedVariables.italicsBehavior);

    /*
    if (sharedVariables.randomArmy == true)
      randomArmy.setSelected(true);
    else
      randomArmy.setSelected(false);
    
    if (sharedVariables.randomBoardTiles == true)
      randomTiles.setSelected(true);
    else
      randomTiles.setSelected(false);

    if (sharedVariables.specificSounds[4] == true)
      notifysound.setSelected(true);
    else
      notifysound.setSelected(false);
    
    if (sharedVariables.tabsOnly == true)
      tabbing.setSelected(true);
    else
      tabbing.setSelected(false);

    if (sharedVariables.showQsuggest == true)
      qsuggestPopup.setSelected(true);
    else
      qsuggestPopup.setSelected(false);
    */

    randomArmy.          setSelected(sharedVariables.randomArmy);
    randomTiles.         setSelected(sharedVariables.randomBoardTiles);
    notifysound.         setSelected(sharedVariables.specificSounds[4]);
    tabbing.             setSelected(sharedVariables.tabsOnly);
    qsuggestPopup.       setSelected(sharedVariables.showQsuggest);
    rotateaways.         setSelected(sharedVariables.rotateAways);
    
    if (sharedVariables.rotateAways) {
      //rotateaways.setSelected(true);
      try {
	scriptLoader loadScripts = new  scriptLoader();
	loadScripts.loadScript(sharedVariables.lanternAways, "lantern_away.txt");
      } catch(Exception du) {}
    }
    /*
    else
      rotateaways.setSelected(false);
    */

    iloggedon.           setSelected(sharedVariables.iloggedon);
    sidewaysconsole.     setSelected(sharedVariables.sideways);
    userbuttons.         setSelected(sharedVariables.showButtonTitle);
    autopopup.           setSelected(sharedVariables.autopopup);
    basketballFlag.      setSelected(sharedVariables.basketballFlag);
    autoHistoryPopup.    setSelected(sharedVariables.autoHistoryPopup);
    makeObserveSounds.   setSelected(sharedVariables.makeObserveSounds);
    hearsound.           setSelected(sharedVariables.makeSounds);
    consolemenu.         setSelected(sharedVariables.showConsoleMenu);

    /*
    if (sharedVariables.iloggedon == true)
      iloggedon.setSelected(true);
    else
      iloggedon.setSelected(false);

    if (sharedVariables.sideways == true)
      sidewaysconsole.setSelected(true);
    else
      sidewaysconsole.setSelected(false);

    if (sharedVariables.showButtonTitle == true)
      userbuttons.setSelected(true);
    else
      userbuttons.setSelected(false);

    if (sharedVariables.autopopup == true)
      autopopup.setSelected(true);
    else
      autopopup.setSelected(false);

    if (sharedVariables.basketballFlag == true)
      basketballFlag.setSelected(true);
    else
      basketballFlag.setSelected(false);

    if (sharedVariables.autoHistoryPopup == true)
      autoHistoryPopup.setSelected(true);
    else
      autoHistoryPopup.setSelected(false);
    
    if (sharedVariables.makeObserveSounds == true)
      makeObserveSounds.setSelected(true);
    else
      makeObserveSounds.setSelected(false);

    if (sharedVariables.makeSounds == true)
      hearsound.setSelected(true);
    else
      hearsound.setSelected(false);

    if (sharedVariables.showConsoleMenu == true)
      consolemenu.setSelected(true);
    else
      consolemenu.setSelected(false);
    */
      
    /*
    if(sharedVariables.consoleLayout == 1) {
      tabLayout1.setSelected(true);
      tabLayout2.setSelected(false);
      tabLayout3.setSelected(false);
    } else if(sharedVariables.consoleLayout == 3) {
      tabLayout1.setSelected(false);
      tabLayout2.setSelected(false);
      tabLayout3.setSelected(true);
      consoleSubframes[0].overall.recreate();
    } else {
      tabLayout1.setSelected(false);
      tabLayout2.setSelected(true);
      tabLayout3.setSelected(false);
      
      consoleSubframes[0].overall.recreate();
    }
    */

    /* name list stuff */
    sharedVariables.activitiesPanel.theChannelList.setForeground(sharedVariables.nameForegroundColor);
    sharedVariables.activitiesPanel.theChannelList.setBackground(sharedVariables.nameBackgroundColor);
    sharedVariables.activitiesPanel.theChannelList.setFont(sharedVariables.nameListFont);
    sharedVariables.activitiesPanel.theChannelList2.setForeground(sharedVariables.nameForegroundColor);
    sharedVariables.activitiesPanel.theChannelList2.setBackground(sharedVariables.nameBackgroundColor);
    sharedVariables.activitiesPanel.theChannelList2.setFont(sharedVariables.nameListFont);
    sharedVariables.activitiesPanel.theChannelList3.setForeground(sharedVariables.nameForegroundColor);
    sharedVariables.activitiesPanel.theChannelList3.setBackground(sharedVariables.nameBackgroundColor);
    sharedVariables.activitiesPanel.theChannelList3.setFont(sharedVariables.nameListFont);


    for (int iii=0; iii<sharedVariables.maxConsoleTabs; iii++) {
      if (consoleSubframes[iii]!=null) {

	if (sharedVariables.nameListFont == null)
          sharedVariables.nameListFont=consoleSubframes[iii].myNameList.getFont();
	if (sharedVariables.consolesNamesLayout[iii]==0) {
          
          consoleSubframes[iii].listChoice.setSelected(false);
	}
	consoleSubframes[iii].myNameList.setForeground(sharedVariables.nameForegroundColor);
	consoleSubframes[iii].myNameList.setBackground(sharedVariables.nameBackgroundColor);

	consoleSubframes[iii].overall.recreate(sharedVariables.consolesTabLayout[iii]);
        final int iiii=iii;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
              public void run() {
              try {
                if (sharedVariables.tellconsole!=iiii)
                  consoleSubframes[iiii].tellCheckbox.setSelected(false);
                // we set it to true in create console to match tell
                // variable. undo the first one here if needed
              } catch (Exception e1) {             

              }
            }
          });
      }
    }
    
    for (int bam=0; bam<sharedVariables.openConsoleCount; bam++)
      consoleSubframes[bam].consoleMenu.setVisible(sharedVariables.showConsoleMenu);


    if (sharedVariables.channelColor[0]!=null)
      sharedVariables.typedColor=sharedVariables.channelColor[0];

    
    /****************** we do these next few in gui thread *****************************/
    SwingUtilities.invokeLater(new Runnable() {
        @Override
          public void run() {
          try {

            if (sharedVariables.boardConsoleType != 2)
              redrawBoard(sharedVariables.boardConsoleType);
            // now apply foreground color to tabs
            for (int cona=0; cona<sharedVariables.maxConsoleTabs; cona++)
              consoles[cona].setForeground(sharedVariables.ForColor);
            // now add in channel tab default names
            try {
              for (int mycons=1; mycons<sharedVariables.maxConsoleTabs; mycons++) {
                setConsoleTabTitles asetter = new setConsoleTabTitles();
                asetter.createConsoleTabTitle(sharedVariables, mycons, consoleSubframes,
                                              sharedVariables.consoleTabCustomTitles[mycons]);
              }
            } catch(Exception badsetting) {}

            setInputFont();
            try {
              // now game boards
              for (int i=0; i < sharedVariables.maxGameTabs; i++) {
                if (myboards[i] != null) {
                  myboards[i].mycontrolspanel.setFont();
                }
              }
              
              //  JFrame framer = new JFrame("open board count is (later event hopefully)" +
              //                             sharedVariables.openBoardCount);
              //  framer.setSize(200,100);
              //  framer.setVisible(true);
            } catch (Exception bdfont) {}

          } catch (Exception e1) {
            
          }
        }
      });


    // applet
    //Image myIconImage = getImage(getDocumentBase(), "lantern.png");
    //setIconImage(myIconImage);
    // end applet
    // stand alone
    try {
      Image myIconImage = null;
      URL myurl = this.getClass().getResource("lantern.png");
      myIconImage = Toolkit.getDefaultToolkit().getImage(myurl);
      setIconImage(myIconImage);
      // need a package for this
      /*
      if (sharedVariables.operatingSystem.equals("mac")) {
        Application app = new Application();
        app.setDockIconImage(myIconImage);
      }
      */
    } catch (Exception e) {

    }
    try {
      setMySize();
    } catch(Exception donthaveit) {}
 
    try {
      sharedVariables.activitiesPanel.theEventsList.setFont(sharedVariables.eventsFont);
    }  catch(Exception badfontsetting) {}


    if (!sharedVariables.hasSettings) {
      // this hasSettings is returned by readNow which reads
      // settings. if false. they have no settings file and i try to
      // position windows. MA 9-19-10
      try {
        if (sharedVariables.screenW > 100 && sharedVariables.screenH > 100) {
          int px = 30;
          int py = 30;
          int cw = (int) (sharedVariables.screenW/2 - px - 1/10*sharedVariables.screenW/2);
          int ch = (int) (sharedVariables.screenH - py - sharedVariables.screenH/6);


          consoleSubframes[0].setLocation(px, py);
          consoleSubframes[0].setSize(cw, ch);
          px = px + px + cw;
          py = 30;
          cw = (int) (sharedVariables.screenW/2 - 30 - (sharedVariables.screenW/2) / 10);
          ch = (int) (sharedVariables.screenH - py - sharedVariables.screenH/6);
          if (ch > cw + 100)
            ch=cw+100;
          
          if (!sharedVariables.useTopGames) {
            myboards[0].setLocation(px, py);
            myboards[0].setSize(cw, ch);
          } else {
            final int px1 = px;
            final int py1 = py;
            final int cw1 = cw;
            final int ch1 = ch;
            try {
              SwingUtilities.invokeLater(new Runnable() {
                  @Override
                    public void run() {
                    try {
                      if (myboards[0].topGame != null) {
                        myboards[0].topGame.setLocation(px1, py1);
                        myboards[0].topGame.setSize(cw1, ch1);
                      }
                    } catch (Exception e1) {
                      //ignore
                    }
                  }
                });
              
            } catch (Exception badf) {}
          }
        }
      } catch (Exception dontknow) {}
      // could not complete getting screen size and postioning windows
    }// end if not have any settings read

    // make as many consoles as we had last time
    try {
	if (sharedVariables.visibleConsoles>1)
          for (int nummake=1; nummake<sharedVariables.visibleConsoles; nummake++)
            mycreator.restoreConsoleFrame();

    } catch(Exception makingConsoles) {}


    makeToolBar();
    getContentPane().add(toolBar,  BorderLayout.NORTH);

    toolBar.setVisible(sharedVariables.toolbarVisible);
    /*
    if (sharedVariables.toolbarVisible == true)
      toolBar.setVisible(true);
    else
      toolBar.setVisible(false);
    */

    try {
      SwingUtilities.invokeLater(new Runnable() {
          @Override
            public void run() {
            try {
              // myboards[0].recreate();
            } catch (Exception e1) {
              //ignore
            }
          }
        });
      
    } catch(Exception badf) {}


    mycreator.updateBoardsMenu(0);  // first board
  }


  public void parseCountries() {
    /*
    try {
      scriptLoader loadScripts = new  scriptLoader();
      ArrayList<String> country = new ArrayList();
      loadScripts.loadScript(country, "flags3.txt");
      String output = "";


      for (int z=0; z<country.size(); z++) {
        String line= country.get(z);
        String first="";
        String last="";
        String text="";
        String oldtext="";

        try {
          
          line=line.replace("\t", " ");
          if (line.contains("N/A"))
            continue;
          StringTokenizer tokens = new StringTokenizer(line, " ");

          boolean lastToken=false;

          while (!lastToken) {
            oldtext=text;
            try {

              text=tokens.nextToken();
              if (text==null) {
                last=oldtext;
                lastToken=true;
                // end if null
              } else {
                if (first.equals(""))
                  first=oldtext;
                else
                  first=first + "_" + oldtext;
              }

            }// end try
            catch (Exception dui) {
              last=oldtext;
              lastToken=true;
            }
          } // end while
          output+=last.toUpperCase() + ";" + first + ";";
          //int i=line.indexOf(";");
          //String line1=line.substring(0, i);
          //String line2=line.substring(i+1, line.length());
          //output+=line2 + ";" + line1 + ";";
          
        } catch (Exception dogeatdog) {}
      }// end for
      FileWrite writer = new FileWrite();
      writer.write(output, "new-countries.txt");
    } catch(Exception dumb) {}
    */
  }

  boolean getOnTopSetting() {

    scriptLoader loadScripts = new  scriptLoader();
    ArrayList<String> ontop = new ArrayList();
    // Andrey says:
    // want to be able to change this to:
    //List<String> ontop = new ArrayList<String>();
    loadScripts.loadScript(ontop, "lantern_board_on_top.txt");
    if (ontop.size() > 0) {
      String top = ontop.get(0);
      if (top.equals("true"))
        return true;
    }
    return false;
  }

  public void setUpLanternNotify() {
    try {
      scriptLoader loadScripts = new  scriptLoader();
      ArrayList<String> notifyScript = new ArrayList();
      // Andrey says:
      // want to be able to change this to:
      //List<String> notifyScript = new ArrayList<String>();
      loadScripts.loadScript(notifyScript, "lantern_global_notify.txt");
      for (int z=0; z<notifyScript.size(); z++) {
        String notString = notifyScript.get(z);
        try {
          int i = notString.indexOf(" ");
          if (i > 1) {// first two spaces minimum needed for name

            lanternNotifyClass temp = new lanternNotifyClass();
            temp.name = notString.substring(0, i);
            try {
              int on = Integer.parseInt(notString.substring(i+1, notString.length()-2));
              // -2 for the \r\n
              if (on==1)
                temp.sound = true;
            } catch (Exception nosound) {}
            sharedVariables.lanternNotifyList.add(temp);
          }
          // end inner try
        } catch (Exception duii) {}
      }// end for
      // end try
    } catch (Exception dui) {}
  }// end method

  public void setUpChannelNotify() {
    try {
      scriptLoader loadScripts = new  scriptLoader();
      ArrayList<String> notifyScript = new ArrayList();
      // Andrey says:
      // want to be able to change this to:
      //List<String> notifyScript = new ArrayList<String>();
      loadScripts.loadScript(notifyScript, "lantern_channel_notify.txt");
      String channel = "";
      channelNotifyClass temp = null;
      for (int z=0; z<notifyScript.size(); z++) {
        if (notifyScript.get(z).startsWith("#")) {
          channel = notifyScript.get(z).substring(1, notifyScript.get(z).length());
          // add node
          if (temp != null) // add last channel on new channel
            sharedVariables.channelNotifyList.add(temp);

          temp = new channelNotifyClass();
          temp.channel = channel;
        } else if (!channel.equals("")) {
          temp.nameList.add(notifyScript.get(z));
        }// end else if
      }// end for
      if (temp != null && !channel.equals(""))
        if (temp.nameList.size() > 0)
          // to get last one or even first one since prior loads happen on next item
          sharedVariables.channelNotifyList.add(temp);
      // end try
    } catch (Exception dui) {}
  }// end method setupchannelnotify
  
  public void createMenu() {

    JMenuBar menu = new JMenuBar();
    JMenu myfiles = new JMenu("File");
    myfiles.setMnemonic(KeyEvent.VK_F);

    JMenuItem reconnect1 = new JMenuItem("Reconnect to ICC");
    myfiles.add(reconnect1);
    reconnect1.setMnemonic(KeyEvent.VK_R);

    JMenuItem reconnect3 = new JMenuItem("Reconnect to Queen");
    myfiles.add(reconnect3);

    reconnect2 = new JMenuItem("Reconnect to FICS");// off now
    myfiles.add(reconnect2);
    reconnect2.setVisible(false);
    JMenuItem wallpaper1 = new JMenuItem("Set Wallpaper");
    myfiles.add(wallpaper1);
    JMenuItem settings2 = new JMenuItem("Save Settings");
    myfiles.add(settings2);

    menu.add(myfiles);


    JMenu mywindowscolors = new JMenu("Colors");

    JMenuItem fontchange = new JMenuItem("Change Font");
    mywindowscolors.add(fontchange);
    fontchange.addActionListener(this);


    JMenuItem channelcol = new JMenuItem("Channel Colors");
    mywindowscolors.add(channelcol);

    JMenuItem consoleColors = new JMenuItem("Console Colors");
    mywindowscolors.add(consoleColors);

    JMenuItem listColor = new JMenuItem("Notify and Events Background Color");
    mywindowscolors.add(listColor);
    listColor.addActionListener(this);


    JMenuItem tellNameColor = new JMenuItem("PTell Name Color");
    mywindowscolors.add(tellNameColor);
    tellNameColor.addActionListener(this);


    JMenu typingarea = new JMenu("Typing Field");

    JMenuItem inputfontchange = new JMenuItem("Change Input Font");
    typingarea.add(inputfontchange);
    inputfontchange.addActionListener(this);

    JMenuItem inputcommand = new JMenuItem("Input Command Color");
    typingarea.add(inputcommand);
    inputcommand.addActionListener(this);

    JMenuItem inputchat = new JMenuItem("Input Chat Color");
    typingarea.add(inputchat);
    inputchat.addActionListener(this);

    mywindowscolors.add(typingarea);


    /****************** names list *****************/
    JMenu inChannelNamesMenu = new JMenu("In Channel Names Menu");

    JMenuItem nameForegroundColor = new JMenuItem("Names List Foreground Color");
    inChannelNamesMenu.add(nameForegroundColor);
    nameForegroundColor.addActionListener(this);

    JMenuItem nameBackgroundColor = new JMenuItem("Names List Background Color");
    inChannelNamesMenu.add(nameBackgroundColor);
    nameBackgroundColor.addActionListener(this);

    JMenuItem namelistFont = new JMenuItem("Names List Font");
    inChannelNamesMenu.add(namelistFont);
    namelistFont.addActionListener(this);
    mywindowscolors.add(inChannelNamesMenu);
    /*********** end names list ******************/
    
    JMenuItem eventsFont = new JMenuItem("Events List Font");
    eventsFont.addActionListener(this);
    mywindowscolors.add(eventsFont);

    JMenuItem colortimestamp = new JMenuItem("Chat Timestamp Color");
    colortimestamp.addActionListener(this);
    mywindowscolors.add(colortimestamp);

    /*
    JMenuItem channelTitles = new JMenuItem("Titles In Channel Color");
    mywindowscolors.add(channelTitles);
    channelTitles.addActionListener(this);
    */
    //duplicate
    //JMenuItem mainback = new JMenuItem("Main Background");
    //myfiles.add(mainback);

    JMenu tabsColorsMenu = new JMenu("Tabs Colors Menu");


    JMenu tabback = new JMenu("Tab Background");
    tabsColorsMenu.add(tabback);
    JMenuItem tabback1 = new JMenuItem("Visited");
    JMenuItem tabback2 = new JMenuItem("Unvisited");
    JMenuItem tabback5 = new JMenuItem("Unvisited/Visited");

    tabback.add(tabback1);
    tabback.add(tabback2);
    tabback.add(tabback5);
    JMenuItem tabimon = new JMenuItem("Tab I'm On Background");
    tabback.add(tabimon);


    JMenu tabfore = new JMenu("Tab Foreground");
    tabsColorsMenu.add(tabfore);
    JMenuItem tabback3 = new JMenuItem("Active");
    JMenuItem tabback4 = new JMenuItem("Non Active");
    tabfore.add(tabback3);
    tabfore.add(tabback4);

    tabback1.addActionListener(this);
    tabback2.addActionListener(this);
    tabback3.addActionListener(this);
    tabback4.addActionListener(this);
    tabback5.addActionListener(this);
    tabimon.addActionListener(this);


    JMenuItem tabborder1 = new JMenuItem("Tab Border");
    tabborder1.addActionListener(this);
    tabsColorsMenu.add(tabborder1);

    JMenuItem tabborder2 = new JMenuItem("Tell Tab Border");
    tabborder2.addActionListener(this);
    tabsColorsMenu.add(tabborder2);


    JMenuItem tabfontchange = new JMenuItem("Change Tab Font");
    tabsColorsMenu.add(tabfontchange);
    tabfontchange.addActionListener(this);


    mywindowscolors.add(tabsColorsMenu);


    JMenuItem wallpaper2 = new JMenuItem("Set Application Background Color");
    mywindowscolors.add(wallpaper2);
    wallpaper2.addActionListener(this);


    menu.add(mywindowscolors);


    setJMenuBar(menu);
    //mainback.addActionListener(this);

    settings2.addActionListener(this);
    reconnect1.addActionListener(this);
    reconnect2.addActionListener(this);
    reconnect3.addActionListener(this);

    wallpaper1.addActionListener(this);


    channelcol.addActionListener(this);
    consoleColors.addActionListener(this);

    /*************************  options window now ************/
    JMenu optionsmenu = new JMenu("Options");
    
    // Andreys adds:
    optionsmenu.setMnemonic(KeyEvent.VK_O);
    
    JMenu soundmenu = new JMenu("Sound");
    hearsound = new JCheckBoxMenuItem("Sounds");
    makeObserveSounds = new JCheckBoxMenuItem("Sounds for Observed Games");
    makeObserveSounds.addActionListener(this);


    notifysound=new JCheckBoxMenuItem("Sounds for Notifications");
    hearsound.addActionListener(this);
    notifysound.addActionListener(this);

    soundmenu.add(hearsound);
    soundmenu.add(makeObserveSounds);
    soundmenu.add(notifysound);

    optionsmenu.add(soundmenu);


    optionsmenu.addSeparator();
    JMenuItem winanalysis = new JMenuItem("Load Winboard Engine");
    winanalysis.addActionListener(this);
    optionsmenu.add(winanalysis);

    JMenuItem ucianalysis = new JMenuItem("Load UCI Engine");
    optionsmenu.add(ucianalysis);
    ucianalysis.addActionListener(this);

    JMenuItem enginerestart = new JMenuItem("Restart Engine");
    optionsmenu.add(enginerestart);
    enginerestart.addActionListener(this);


    JMenuItem enginestop = new JMenuItem("Stop Engine");
    optionsmenu.add(enginestop);
    enginestop.addActionListener(this);

    JMenu engineMenu = new JMenu("Analysis Display");

    JMenuItem ananfont = new JMenuItem("Analysis Font");
    engineMenu.add(ananfont);
    ananfont.addActionListener(this);

    JMenuItem ananfore = new JMenuItem("Analysis Foreground Color");
    engineMenu.add(ananfore);
    ananfore.addActionListener(this);

    JMenuItem ananback = new JMenuItem("Analysis Background Color");
    engineMenu.add(ananback);
    ananback.addActionListener(this);
    optionsmenu.add(engineMenu);

    optionsmenu.addSeparator();
    JMenuItem customizetools = new JMenuItem("Customize User Buttons");
    customizetools.addActionListener(this);
    optionsmenu.add(customizetools);

    optionsmenu.addSeparator();
    
    /************** advanced ****************/
    JMenu advancedOptions = new JMenu("Advanced");
    qsuggestPopup = new JCheckBoxMenuItem("Qsuggest Popups");
    qsuggestPopup.addActionListener(this);
    advancedOptions.add(qsuggestPopup);

    userbuttons = new JCheckBoxMenuItem("Show User Button Titles");
    userbuttons.addActionListener(this);
    advancedOptions.add(userbuttons);

    consolemenu = new JCheckBoxMenuItem("Show Console Menu");
    consolemenu.setSelected(true);
    consolemenu.addActionListener(this);
    advancedOptions.add(consolemenu);

    channelNumberLeft = new JCheckBoxMenuItem("Channel Number On Left");
    channelNumberLeft.setSelected(true);
    channelNumberLeft.addActionListener(this);
    advancedOptions.add(channelNumberLeft);


    compactNameList = new JCheckBoxMenuItem("Compact Channel Name List");
    compactNameList.setSelected(false);
    compactNameList.addActionListener(this);
    advancedOptions.add(compactNameList);


    autobufferchat = new JCheckBoxMenuItem("Auto Buffer Chat Length");
    advancedOptions.add(autobufferchat);

    useTopGame = new JCheckBoxMenuItem("Make Boards Always On Top");
    advancedOptions.add(useTopGame);
    useTopGame.addActionListener(this);

    notifyMainAlso = new JCheckBoxMenuItem("Print Channel Notify for Main Also");
    advancedOptions.add(notifyMainAlso);
    notifyMainAlso.addActionListener(this);


    autopopup = new JCheckBoxMenuItem("Auto Name Popup");
    advancedOptions.add(autopopup);
    autoHistoryPopup = new JCheckBoxMenuItem("Auto History Popup");
    advancedOptions.add(autoHistoryPopup);

    autobufferchat.addActionListener(this);
    autopopup.addActionListener(this);
    autoHistoryPopup.addActionListener(this);

    basketballFlag = new JCheckBoxMenuItem("Use Basketball Logo ICC Flag");
    advancedOptions.add(basketballFlag);
    basketballFlag.addActionListener(this);

    lineindent = new JCheckBoxMenuItem("Indent Multi Line Tells");
    lineindent.addActionListener(this);
    advancedOptions.add(lineindent);


    JMenu italicsBehaviorMenu = new JMenu("` ` Behavior");
    italicsBehavior[0] = new JCheckBoxMenuItem("` ` Do Nothing");
    italicsBehavior[0].addActionListener(this);
    italicsBehaviorMenu.add(italicsBehavior[0]);

    italicsBehavior[1] = new JCheckBoxMenuItem("` ` Italics");
    italicsBehavior[1].addActionListener(this);
    italicsBehaviorMenu.add(italicsBehavior[1]);

    italicsBehavior[2] = new JCheckBoxMenuItem("` ` Brighter Color");
    italicsBehavior[2].addActionListener(this);
    italicsBehaviorMenu.add(italicsBehavior[2]);

    advancedOptions.add(italicsBehaviorMenu);
/**************************** end advanced ***********************/
    
    optionsmenu.add(advancedOptions);


    JMenu featuresMenu = new JMenu("Features");


    tellswitch = new JCheckBoxMenuItem("Switch Tab On Tell");
    tellswitch.addActionListener(this);
    featuresMenu.add(tellswitch);

    autonoidle = new JCheckBoxMenuItem("No Idle");
    featuresMenu.add(autonoidle);
    autonoidle.addActionListener(this);

    rotateaways = new JCheckBoxMenuItem("Rotate Away Message");
    featuresMenu.add(rotateaways);
    iloggedon = new JCheckBoxMenuItem("Send iloggedon");
    featuresMenu.add(iloggedon);
    iloggedon.addActionListener(this);
    rotateaways.addActionListener(this);


    optionsmenu.add(featuresMenu);


    JMenu observeOptions = new JMenu("Observing Options");

    // Andrey adds:
    observeOptions.setMnemonic(KeyEvent.VK_O);
    
    JMenu tournieFollow = new JMenu("Follow Tomato Tournament Games");

    // Andrey adds:
    tournieFollow.setMnemonic(KeyEvent.VK_F);

    // Andrey edits:
    // remove "Auto Observe"
    JCheckBoxMenuItem autoflash = new JCheckBoxMenuItem("Flash");
    JCheckBoxMenuItem autocooly = new JCheckBoxMenuItem("Cooly");
    JCheckBoxMenuItem autotomato = new JCheckBoxMenuItem("Tomato");
    JCheckBoxMenuItem autowildone = new JCheckBoxMenuItem("WildOne");
    JCheckBoxMenuItem autoslomato = new JCheckBoxMenuItem("Slomato");
    JCheckBoxMenuItem autoketchup = new JCheckBoxMenuItem("Ketchup");
    JCheckBoxMenuItem autoolive = new JCheckBoxMenuItem("Olive");
    JCheckBoxMenuItem autolittleper = new JCheckBoxMenuItem("LittlePer");

    tournieFollow.add(autoflash);
    tournieFollow.add(autocooly);
    tournieFollow.add(autotomato);
    tournieFollow.add(autowildone);
    tournieFollow.add(autoslomato);
    tournieFollow.add(autoketchup);
    tournieFollow.add(autoolive);
    tournieFollow.add(autolittleper);

    autoflash.addActionListener(this);
    autocooly.addActionListener(this);
    autotomato.addActionListener(this);
    autowildone.addActionListener(this);
    autoslomato.addActionListener(this);
    autoketchup.addActionListener(this);
    autoolive.addActionListener(this);
    autolittleper.addActionListener(this);
    
    // Andrey adds:
    autoflash.setMnemonic(KeyEvent.VK_F);
    autocooly.setMnemonic(KeyEvent.VK_C);
    autotomato.setMnemonic(KeyEvent.VK_T);
    autowildone.setMnemonic(KeyEvent.VK_W);
    autoslomato.setMnemonic(KeyEvent.VK_S);
    autoketchup.setMnemonic(KeyEvent.VK_K);
    autoolive.setMnemonic(KeyEvent.VK_O);
    autolittleper.setMnemonic(KeyEvent.VK_L);

    observeOptions.add(tournieFollow);
    JMenu randomGraphics = new JMenu("Random Pieces Board when Observing");
  

    randomArmy = new JCheckBoxMenuItem("Random Piece Set Observe Only");
    randomArmy.addActionListener(this);
    randomGraphics.add(randomArmy);

    JMenuItem configureRand = new JMenuItem("Configure Random Pieces For White");
    configureRand.addActionListener(this);
    randomGraphics.add(configureRand);

    JMenuItem configureRandBlack = new JMenuItem("Configure Random Pieces For Black");
    configureRandBlack.addActionListener(this);
    randomGraphics.add(configureRandBlack);


    randomTiles = new JCheckBoxMenuItem("Random Square Tiles Observe Only");
    randomTiles.addActionListener(this);
    randomGraphics.add(randomTiles);

    observeOptions.add(randomGraphics);

    optionsmenu.add(observeOptions);


    JMenu chattimestamp = new JMenu("Chat Timestamp");

    channelTimestamp = new JCheckBoxMenuItem("Timestamp Channels and Kibs");
    channelTimestamp.addActionListener(this);
    chattimestamp.add(channelTimestamp);

    shoutTimestamp = new JCheckBoxMenuItem("Timestamp Shouts");
    shoutTimestamp.addActionListener(this);
    chattimestamp.add(shoutTimestamp);


    tellTimestamp = new JCheckBoxMenuItem("Timestamp Tells");
    tellTimestamp.addActionListener(this);
    chattimestamp.add(tellTimestamp);

    leftNameTimestamp=new JCheckBoxMenuItem("Timestamp To Left Of Name");
    leftNameTimestamp.addActionListener(this);
    //chattimestamp.add(leftNameTimestamp); disabling the option

    qtellTimestamp=new JCheckBoxMenuItem("Timestamp Channel Qtells");
    qtellTimestamp.addActionListener(this);
    chattimestamp.add(qtellTimestamp);


    reconnectTimestamp=new JCheckBoxMenuItem("Timestamp Connecting");
    reconnectTimestamp.addActionListener(this);
    chattimestamp.add(reconnectTimestamp);


    optionsmenu.add(chattimestamp);


    menu.add(optionsmenu);
    /******************************************* end of options window *****************/

    sharedVariables.myWindows = new JMenu("Windows");
    sharedVariables.myWindows.setMnemonic(KeyEvent.VK_W);

    // JMenuItem nconsole = new JMenuItem("New Console");
    // sharedVariables.myWindows.add(nconsole);
    JMenuItem eventlist = new JMenuItem("Activities Window");
    sharedVariables.myWindows.add(eventlist);
    eventlist.setMnemonic(KeyEvent.VK_A);
    eventlist.addActionListener(this);
    JMenuItem  seekingGraph = new JMenuItem("Seek Graph");
    sharedVariables.myWindows.add(seekingGraph);
    seekingGraph.setMnemonic(KeyEvent.VK_S);
    seekingGraph.addActionListener(this);

    JMenuItem mynotify = new JMenuItem("Notify Window");
    sharedVariables.myWindows.add(mynotify);
    mynotify.addActionListener(this);


    sharedVariables.myWindows.addSeparator();


    JMenuItem nboard = new JMenuItem("New Board");
    sharedVariables.myWindows.add(nboard);
    nboard.addActionListener(this);


    JMenuItem rconsole = new JMenuItem("New Chat Console");
    sharedVariables.myWindows.add(rconsole);
    rconsole.addActionListener(this);

    JMenuItem detachedconsole = new JMenuItem("New Detached Chat Console");
    sharedVariables.myWindows.add(detachedconsole);
    detachedconsole.addActionListener(this);

    sharedVariables.myWindows.addSeparator();


    JMenuItem rconsole2 = new JMenuItem("Customize Tab");
    sharedVariables.myWindows.add(rconsole2);
    rconsole2.setMnemonic(KeyEvent.VK_C);
    rconsole2.addActionListener(this);


    JMenuItem  webopener = new JMenuItem("Open Web");
    //  sharedVariables.myWindows.add(webopener);


    toolbarvisible = new JCheckBoxMenuItem("Toolbar");
    sharedVariables.myWindows.add(toolbarvisible);
    toolbarvisible.setMnemonic(KeyEvent.VK_T);


    JMenuItem channelmap = new JMenuItem("Channel Map");
    sharedVariables.myWindows.add(channelmap);
    channelmap.addActionListener(this);
    channelmap.setMnemonic(KeyEvent.VK_M);


    JMenuItem channelnotifymap = new JMenuItem("Channel Notify Map");
    sharedVariables.myWindows.add(channelnotifymap);
    channelnotifymap.setMnemonic(KeyEvent.VK_N);
    channelnotifymap.addActionListener(this);

    JMenuItem channelnotifyonline = new JMenuItem("Channel Notify Online");
    sharedVariables.myWindows.add(channelnotifyonline);
    channelnotifyonline.setMnemonic(KeyEvent.VK_O);
    channelnotifyonline.addActionListener(this);


    JMenuItem toolbox = new JMenuItem("ToolBox");
    sharedVariables.myWindows.add(toolbox);

    JMenuItem cascading = new JMenuItem("Cascade");
    sharedVariables.myWindows.add(cascading);
    cascading.addActionListener(this);

    sharedVariables.myWindows.addSeparator();

    menu.add(sharedVariables.myWindows);


    webopener.addActionListener(this);
    toolbarvisible.addActionListener(this);
    toolbox.addActionListener(this);

    JMenu myboardmenu = new JMenu("Game");

    JMenuItem nseek = new JMenuItem("Get a Game");
    myboardmenu.add(nseek);
    nseek.addActionListener(this);
  
    JMenuItem flipSent = new JMenuItem("Flip");
    myboardmenu.add(flipSent);
    flipSent.addActionListener(this);

    JMenuItem withdrawSent = new JMenuItem("Withdraw Challenges");
    myboardmenu.add(withdrawSent);
    withdrawSent.addActionListener(this);


    JMenu boardDesign = new JMenu("Board Design");
    BoardDesign1 = new JCheckBoxMenuItem("Original");
    BoardDesign2 = new JCheckBoxMenuItem("Modern");
    BoardDesign3 = new JCheckBoxMenuItem("Mixed");

    boardDesign.add(BoardDesign1);
    boardDesign.add(BoardDesign2);
    boardDesign.add(BoardDesign3);
    BoardDesign1.addActionListener(this);
    BoardDesign2.addActionListener(this);
    BoardDesign3.addActionListener(this);

    myboardmenu.add(boardDesign);

    tabbing = new JCheckBoxMenuItem("Tabs Only");
    myboardmenu.add(tabbing);
    tabbing.addActionListener(this);


    JMenu selectboards = new JMenu("Boards");
    solidboard = new JCheckBoxMenuItem("Solid Color Board");

    woodenboard1 = new JCheckBoxMenuItem("Pale Wood");
    woodenboard2 = new JCheckBoxMenuItem("Light Wood");
    woodenboard3 = new JCheckBoxMenuItem("Dark Wood");
    grayishboard = new JCheckBoxMenuItem("Gray Marble");
    board5 = new JCheckBoxMenuItem("Red Marble");
    board6 = new JCheckBoxMenuItem("Crampled Paper");
    board7 = new JCheckBoxMenuItem("Winter");
    oliveboard = new JCheckBoxMenuItem("Olive Board");
    cherryboard = new JCheckBoxMenuItem("Cherry Board");
    purpleboard = new JCheckBoxMenuItem("Purple Board");


    selectboards.add(solidboard);
    selectboards.add(woodenboard1);
    selectboards.add(woodenboard2);
    selectboards.add(woodenboard3);
    selectboards.add(grayishboard);
    selectboards.add(board5);
    selectboards.add(board6);
    selectboards.add(board7);
    selectboards.add(oliveboard);
    selectboards.add(cherryboard);
    selectboards.add(purpleboard);


    myboardmenu.add(selectboards);
    solidboard.addActionListener(this);
    woodenboard1.addActionListener(this);
    woodenboard2.addActionListener(this);
    woodenboard3.addActionListener(this);
    grayishboard.addActionListener(this);
    board5.addActionListener(this);
    board6.addActionListener(this);
    board7.addActionListener(this);
    oliveboard.addActionListener(this);
    cherryboard.addActionListener(this);
    purpleboard.addActionListener(this);

    woodenboard2.setSelected(true);

    // pieces
    JMenu selectpieces = new JMenu("Pieces");
    pieces1 = new JCheckBoxMenuItem("Dyche1");
    pieces2 = new JCheckBoxMenuItem("Dyche2");
    pieces3 = new JCheckBoxMenuItem("Dyche3");
    pieces4 = new JCheckBoxMenuItem("Bookup");
    pieces5 = new JCheckBoxMenuItem("Xboard");
    pieces6 = new JCheckBoxMenuItem("Alpha");
    pieces7 = new JCheckBoxMenuItem("Spatial");
    pieces8 = new JCheckBoxMenuItem("Harlequin");
    pieces9 = new JCheckBoxMenuItem("Berlin");
    pieces10 = new JCheckBoxMenuItem("Eboard Classic");
    pieces11 = new JCheckBoxMenuItem("Molten Good");
    pieces12 = new JCheckBoxMenuItem("Molten Evil");
    pieces13 = new JCheckBoxMenuItem("Liebeskind");
    pieces14 = new JCheckBoxMenuItem("Eyes");
    pieces15 = new JCheckBoxMenuItem("Fantasy");

    pieces16 = new JCheckBoxMenuItem("Line");
    pieces17 = new JCheckBoxMenuItem("Motif");
    pieces18 = new JCheckBoxMenuItem("Utrecht");

    pieces19 = new JCheckBoxMenuItem("Adventure");
    pieces20 = new JCheckBoxMenuItem("Maya");
    pieces21 = new JCheckBoxMenuItem("Medieval");

    pieces22 = new JCheckBoxMenuItem("CCube");

    pieces23 = new JCheckBoxMenuItem("Monge Mix");
    pieces24 = new JCheckBoxMenuItem("Random Pieces");

    JMenuItem aboutmonge = new JMenuItem ("About Monge Pieces");

    selectpieces.add(pieces1);
    selectpieces.add(pieces2);
    selectpieces.add(pieces3);
    selectpieces.add(pieces4);
    selectpieces.add(pieces5);
    selectpieces.add(pieces6);
    //selectpieces.add(pieces7);
    JMenu mongeMenu = new JMenu("Monge");
    mongeMenu.add(pieces7);
    mongeMenu.add(pieces14);
    mongeMenu.add(pieces15);
    mongeMenu.add(pieces23);

    mongeMenu.add(aboutmonge);

    selectpieces.add(pieces19);
    selectpieces.add(pieces20);
    selectpieces.add(pieces21);

    selectpieces.add(mongeMenu);

    selectpieces.add(pieces8);
    selectpieces.add(pieces9);
    selectpieces.add(pieces10);

    selectpieces.add(pieces16);
    selectpieces.add(pieces17);
    selectpieces.add(pieces18);

    JMenu moltenmenu=new JMenu("Molten");
    moltenmenu.add(pieces11);
    moltenmenu.add(pieces12);
    moltenmenu.add(pieces13);
    selectpieces.add(moltenmenu);
    selectpieces.add(pieces22);
    selectpieces.add(pieces24);


    myboardmenu.add(selectpieces);
    pieces1.addActionListener(this);
    pieces2.addActionListener(this);
    pieces3.addActionListener(this);
    pieces4.addActionListener(this);
    pieces5.addActionListener(this);
    pieces6.addActionListener(this);
    pieces7.addActionListener(this);
    pieces8.addActionListener(this);
    pieces9.addActionListener(this);
    pieces10.addActionListener(this);
    pieces11.addActionListener(this);
    pieces12.addActionListener(this);
    pieces13.addActionListener(this);
    pieces14.addActionListener(this);
    pieces15.addActionListener(this);
    pieces16.addActionListener(this);
    pieces17.addActionListener(this);
    pieces18.addActionListener(this);
    pieces19.addActionListener(this);
    pieces20.addActionListener(this);

    pieces21.addActionListener(this);
    pieces22.addActionListener(this);
    pieces23.addActionListener(this);
    pieces24.addActionListener(this);


    aboutmonge.addActionListener(this);

    pieces1.setSelected(true);
    /********** end pieces **************/

    JMenu preset = new JMenu("Preset Color Boards");

    preset0 = new JMenuItem("Default Board");
    preset.add(preset0);

    preset1 = new JMenuItem("Tan Board");
    preset.add(preset1);

    preset2 = new JMenuItem("Gray Color Board");
    preset.add(preset2);

    preset3 = new JMenuItem("Blitzin Green Board");
    preset.add(preset3);

    selectboards.add(preset);

    preset0.addActionListener(this);
    preset1.addActionListener(this);
    preset2.addActionListener(this);
    preset3.addActionListener(this);
    
    /******************* board colors ********************/
    JMenu boardSquareColors = new JMenu("Board Squares Colors");
    JMenuItem lcolor = new JMenuItem("Light Square Color");
    boardSquareColors.add(lcolor);
    JMenuItem dcolor = new JMenuItem("Dark Square Color");
    boardSquareColors.add(dcolor);
    lcolor.addActionListener(this);
    dcolor.addActionListener(this);
    myboardmenu.add(boardSquareColors);


    JMenu boardColors = new JMenu("Board Colors");

    JMenuItem bbackcolor = new JMenuItem("Board Background Color");
    boardColors.add(bbackcolor);
    JMenuItem bforcolor = new JMenuItem("Board Foreground Color");
    boardColors.add(bforcolor);

    JMenuItem cforcolor = new JMenuItem("Clock Foreground Color");
    boardColors.add(cforcolor);


    JMenuItem bcbackcolor = new JMenuItem("Board Clock Background Color");
    boardColors.add(bcbackcolor);
    bbackcolor.addActionListener(this);
    bcbackcolor.addActionListener(this);
    bforcolor.addActionListener(this);
    cforcolor.addActionListener(this);

    JMenuItem highlightcolor = new JMenuItem("Highlight Moves Color");
    highlightcolor.addActionListener(this);
    boardColors.add(highlightcolor);

    JMenuItem scrollhighlightcolor = new JMenuItem("Scroll Back Highlight Color");
    scrollhighlightcolor.addActionListener(this);
    boardColors.add(scrollhighlightcolor);


    myboardmenu.add(boardColors);

    JMenu boardFonts9 = new JMenu("Board Fonts");

    JMenuItem gamefont = new JMenuItem("Game Board Font");
    boardFonts9.add(gamefont);
    gamefont.addActionListener(this);
    JMenuItem gameclockfont = new JMenuItem("Game Clock Font");
    boardFonts9.add(gameclockfont);
    gameclockfont.addActionListener(this);
    myboardmenu.add(boardFonts9);
    /*************** end board colors *****************/

    JMenu theHideMenu = new JMenu("Things to Hide or Show");

    highlight = new JCheckBoxMenuItem("Highlight Moves");
    theHideMenu.add(highlight);
    materialCount = new JCheckBoxMenuItem("Material Count");
    theHideMenu.add(materialCount);
    materialCount.addActionListener(this);

    drawCoordinates = new JCheckBoxMenuItem("Draw Coordinates");
    theHideMenu.add(drawCoordinates);
    drawCoordinates.addActionListener(this);


    showPallette = new JCheckBoxMenuItem("Show Examine Mode Palette");
    theHideMenu.add(showPallette);
    showPallette.addActionListener(this);


    showFlags = new JCheckBoxMenuItem("Show Flags");
    theHideMenu.add(showFlags);
    showFlags.addActionListener(this);

    showRatings = new JCheckBoxMenuItem("Show Ratings on Board When Playing");
    theHideMenu.add(showRatings);
    showRatings.addActionListener(this);

    playersInMyGame = new JCheckBoxMenuItem("Show Observers In Games");
    playersInMyGame.addActionListener(this);
    theHideMenu.add(playersInMyGame);

    myboardmenu.add(theHideMenu);

    useLightBackground = new JCheckBoxMenuItem("Use Light Square as Board Background");
    // myboardmenu.add(useLightBackground);   // disabled
    useLightBackground.addActionListener(this);


    /* examine game replay */
    JMenu examReplay = new JMenu("Examine Game Replay");

    /*
    JMenuItem autostart = new JMenuItem("Start AutoExam");
    examReplay.add(autostart);
    JMenuItem autostop = new JMenuItem("Stop AutoExam");
    examReplay.add(autostop);
    JMenuItem autoset = new JMenuItem("Set AutoExam Speed");
    */
    JMenuItem autoset = new JMenuItem("AutoExam Dialog");
    examReplay.add(autoset);

 
    JMenuItem whatexaminereplay = new JMenuItem("What's Examine Game Replay Quick Help");
    examReplay.add(whatexaminereplay);
    whatexaminereplay.addActionListener(this);

    //autostart.addActionListener(this);
    autoset.addActionListener(this);
    //autostop.addActionListener(this);
    myboardmenu.add(examReplay);

    /* Pgn      */
    JMenu PgnMenu = new JMenu("PGN");
    pgnlogging = new JCheckBoxMenuItem("Log Pgn");
    PgnMenu.add(pgnlogging);
    pgnlogging.addActionListener(this);
    pgnlogging.setSelected(true);

    pgnObservedLogging = new JCheckBoxMenuItem("Log Observed Games To Pgn");
    PgnMenu.add(pgnObservedLogging);
    pgnObservedLogging.addActionListener(this);
    pgnObservedLogging.setSelected(true);

    JMenuItem openpgn = new JMenuItem("Open Pgn");
    PgnMenu.add(openpgn);
    openpgn.addActionListener(this);

    myboardmenu.add(PgnMenu);
    /* communications */
    JMenu Communications = new JMenu("Communications");

    blockSays = new JCheckBoxMenuItem("Block Opponents Says When Not Playing");
    Communications.add(blockSays);
    blockSays.addActionListener(this);
    gameend = new JCheckBoxMenuItem("Send Game End Messages");
    Communications.add(gameend);
    gameend.addActionListener(this);
    autoChat = new JCheckBoxMenuItem("AutoChat");
    Communications.add(autoChat);
    autoChat.addActionListener(this);

    myboardmenu.add(Communications);
    
    /* Advanced */
    JMenu AdvancedGameMenu = new JMenu("Advanced");
    lowTimeColors = new JCheckBoxMenuItem("Low Time Clock Colors (Bullet Only)");
    AdvancedGameMenu.add(lowTimeColors);
    lowTimeColors.addActionListener(this);

    checkLegality = new JCheckBoxMenuItem("Check Move Legality");
    checkLegality.addActionListener(this);
    AdvancedGameMenu.add(checkLegality);

    unobserveGoExamine = new JCheckBoxMenuItem("Unobserve Games Gone Examine");
    unobserveGoExamine.addActionListener(this);
    AdvancedGameMenu.add(unobserveGoExamine);

    newObserveGameSwitch = new JCheckBoxMenuItem("Switch To New Game Tab On Observe");
    AdvancedGameMenu.add(newObserveGameSwitch);
    newObserveGameSwitch.addActionListener(this);

    JMenu aspect = new JMenu("Board Aspect Ratio");
    aspect0 = new JCheckBoxMenuItem("1:1");
    aspect.add(aspect0);
    aspect0.setSelected(true);

    aspect1 = new JCheckBoxMenuItem("5:4");
    aspect.add(aspect1);
    aspect1.setSelected(false);

    aspect2 = new JCheckBoxMenuItem("4:3");
    aspect.add(aspect2);
    aspect2.setSelected(false);

    aspect3 = new JCheckBoxMenuItem("3:2");
    aspect.add(aspect3);
    aspect3.setSelected(false);
    
    aspect0.addActionListener(this);
    aspect1.addActionListener(this);
    aspect2.addActionListener(this);
    aspect3.addActionListener(this);
    
    AdvancedGameMenu.add(aspect);
    myboardmenu.add(AdvancedGameMenu);
    
    /*************** menu for board console *****************/
    JMenu consoleaspect = new JMenu("Board Console");

    boardconsole0 = new JCheckBoxMenuItem("Hide Board Console");
    consoleaspect.add(boardconsole0);
    boardconsole0.setSelected(false);

    boardconsole1 = new JCheckBoxMenuItem("Compact Board Console");
    consoleaspect.add(boardconsole1);
    boardconsole1.setSelected(false);

    boardconsole2 = new JCheckBoxMenuItem("Normal Board Console");
    consoleaspect.add(boardconsole2);
    boardconsole2.setSelected(true);

    boardconsole3 = new JCheckBoxMenuItem("Larger Board Console");
    consoleaspect.add(boardconsole3);
    boardconsole3.setSelected(false);
    
    consoleaspect.addSeparator();
    
    sidewaysconsole = new JCheckBoxMenuItem("Console On Side");
    consoleaspect.add(sidewaysconsole);
    sidewaysconsole.setSelected(false);

    myboardmenu.add(consoleaspect);
    
    boardconsole0.addActionListener(this);
    boardconsole1.addActionListener(this);
    boardconsole2.addActionListener(this);
    boardconsole3.addActionListener(this);
    sidewaysconsole.addActionListener(this);
    /***************** end board console menu **************/

    highlight.addActionListener(this);

    highlight.setSelected(true);

    menu.add(myboardmenu);

    //nconsole.addActionListener(this);

    JMenu actionsmenu = new JMenu("Actions");
    JMenuItem showhistory = new JMenuItem("Show My Recent Games");
    actionsmenu.add(showhistory);
    showhistory.addActionListener(this);

    JMenuItem showlib = new JMenuItem("Show My Game Library");
    actionsmenu.add(showlib);
    showlib.addActionListener(this);

    JMenuItem showstored = new JMenuItem("Show My Adjourned Games");
    actionsmenu.add(showstored);
    showstored.addActionListener(this);

    actionsmenu.addSeparator();
    JMenuItem showfinger = new JMenuItem("Show My Profile and Ratings");
    actionsmenu.add(showfinger);
    showfinger.addActionListener(this);


    JMenuItem showexam = new JMenuItem("Enter Examination Mode");
    actionsmenu.add(showexam);
    showexam.addActionListener(this);

    JMenuItem showexamlast = new JMenuItem("Examine My Last Game");
    actionsmenu.add(showexamlast);
    showexamlast.addActionListener(this);


    actionsmenu.addSeparator();

    JMenuItem showobs = new JMenuItem("Observe High Rated Game");
    actionsmenu.add(showobs);
    showobs.addActionListener(this);

    JMenuItem showobs5 = new JMenuItem("Observe High Rated 5-Minute Game");
    actionsmenu.add(showobs5);
    showobs5.addActionListener(this);

    JMenuItem showobs15 = new JMenuItem("Observe High Rated 15-Minute Game");
    actionsmenu.add(showobs15);
    showobs15.addActionListener(this);

    actionsmenu.addSeparator();
    
    JMenuItem showrelay = new JMenuItem("Show Relay Schedule");
    actionsmenu.add(showrelay);
    showrelay.addActionListener(this);
 
    JMenuItem ratinggraph = new JMenuItem("Show Rating Graphs");
    actionsmenu.add(ratinggraph);
    ratinggraph.addActionListener(this);

    JMenuItem addfriend = new JMenuItem("Add a Friend");
    actionsmenu.add(addfriend);
    addfriend.addActionListener(this);

    actionsmenu.addSeparator();

    JMenuItem followBroadcast = new JMenuItem("Follow Broadcast- When On");
    actionsmenu.add(followBroadcast);
    followBroadcast.addActionListener(this);

    JMenuItem unfollowBroadcast = new JMenuItem("Stop Following");
    actionsmenu.add(unfollowBroadcast);
    unfollowBroadcast.addActionListener(this);

    JMenuItem showfm = new JMenuItem("Open ChessFM");
    actionsmenu.add(showfm);
    showfm.addActionListener(this);

    menu.add(actionsmenu);

    JMenu helpmenu = new JMenu("Help");
    JMenuItem lanternmanual = new JMenuItem("Lantern Manual");
    helpmenu.add(lanternmanual);
    lanternmanual.addActionListener(this);

    JMenuItem changelog = new JMenuItem("Change Log");
    helpmenu.add(changelog);
    changelog.addActionListener(this);

    JMenuItem infohelp = new JMenuItem("ICC Information Help Files");
    helpmenu.add(infohelp);
    infohelp.addActionListener(this);

    JMenuItem commandhelp = new JMenuItem("ICC Command Help Files");
    helpmenu.add(commandhelp);
    commandhelp.addActionListener(this);

    JMenuItem joinrenewhelp = new JMenuItem("Join/Renew");
    helpmenu.add(joinrenewhelp);
    joinrenewhelp.addActionListener(this);

    JMenuItem passwordhelp = new JMenuItem("Lost Password");
    helpmenu.add(passwordhelp);
    passwordhelp.addActionListener(this);

    JMenu poweroutmenu = new JMenu("Extra-games");
    JMenuItem power = new JMenuItem("Start Powerout");
    poweroutmenu.add(power);
    power.addActionListener(this);
    
    JMenuItem mines = new JMenuItem("Start MineSweeper");
    poweroutmenu.add(mines);
    mines.addActionListener(this);

    JMenuItem mastermind = new JMenuItem("Start Mastermind");
    poweroutmenu.add(mastermind);
    mastermind.addActionListener(this);

    JMenuItem startfour = new JMenuItem("Start Connect Four");
    poweroutmenu.add(startfour);
    startfour.addActionListener(this);

    helpmenu.add(poweroutmenu);

    menu.add(helpmenu);
  }

  public void stateChanged(ChangeEvent e) {

    for (int i=0; i < sharedVariables.openConsoleCount; i++) {
      if (consoles[i]!= null) {
        if (colortype == 1) {
          Color newColor = tcc.getColor();
          consoles[i].setForeground(newColor);
        } else if (colortype == 2) {
          Color newColor = tcc.getColor();
          consoles[i].setBackground(newColor);
        }
      } // end if not null
    } // end for
  }// end method

  public void actionPerformed(ActionEvent event) {
    //Object source = event.getSource();
    //handle action event here
    // Andrey edits:
    String action = event.getActionCommand();
    // and replaces "event.getActionCommand()" with "action" below
    
    if (action.equals("Single Rows of Tabs")) {
      sharedVariables.consoleLayout=1;
      resetConsoleLayout();
      
    } else if (action.equals("No Visible Tabs")) {
      sharedVariables.consoleLayout=3;
      resetConsoleLayout();
      
    } else if (action.equals("Two Rows of Tabs")) {
      sharedVariables.consoleLayout=2;
      resetConsoleLayout();
      
    } else if (action.equals("Indent Multi Line Tells")) {
      /*
      if(sharedVariables.indent == true) {
	sharedVariables.indent=false;
	lineindent.setSelected(false);
      } else {
	sharedVariables.indent=true;
	lineindent.setSelected(true);
      }
      */
      sharedVariables.indent = !sharedVariables.indent;
      lineindent.setSelected(sharedVariables.indent);
      
    } else if (action.equals("Check Move Legality")) {
      /*
      if (sharedVariables.checkLegality == true) {
	sharedVariables.checkLegality=false;
	checkLegality.setSelected(false);
      } else {
	sharedVariables.checkLegality=true;
	checkLegality.setSelected(true);
      }
      */
      sharedVariables.checkLegality = !sharedVariables.checkLegality;
      checkLegality.setSelected(sharedVariables.checkLegality);
      
    } else if (action.equals("Unobserve Games Gone Examine")) {
      /*
      if (sharedVariables.unobserveGoExamine == true) {
	sharedVariables.unobserveGoExamine=false;
	unobserveGoExamine.setSelected(false);
      } else {
	sharedVariables.unobserveGoExamine=true;
	unobserveGoExamine.setSelected(true);
      }
      */
      sharedVariables.unobserveGoExamine = !sharedVariables.unobserveGoExamine;
      unobserveGoExamine.setSelected(sharedVariables.unobserveGoExamine);
      
    } else if (action.equals("Compact Channel Name List")) {
      sharedVariables.compactNameList = !sharedVariables.compactNameList;
      sharedVariables.nameListSize =
        (sharedVariables.compactNameList ? 65 : 90);
      compactNameList.setSelected(sharedVariables.compactNameList);
      
      try {
        /*
	if (sharedVariables.compactNameList == true) {
          sharedVariables.compactNameList = false;
          sharedVariables.nameListSize=90;
          compactNameList.setSelected(false);
	} else {
          sharedVariables.compactNameList = true;
          sharedVariables.nameListSize=65;
          compactNameList.setSelected(true);
	}
        */
        for (int iii=0; iii<sharedVariables.maxConsoleTabs; iii++) {
          if (consoleSubframes[iii]!=null) {
            consoleSubframes[iii].overall.recreate(sharedVariables.consolesTabLayout[iii]);
          }
        }//end for
      // end try
      } catch(Exception namebad) {}
      
    } else if (action.equals("Channel Number On Left")) {
      /*
      if (sharedVariables.channelNumberLeft == true) {
	sharedVariables.channelNumberLeft=false;
	channelNumberLeft.setSelected(false);
      } else {
	sharedVariables.channelNumberLeft=true;
	channelNumberLeft.setSelected(true);
      }
      */
      sharedVariables.channelNumberLeft = !sharedVariables.channelNumberLeft;
      channelNumberLeft.setSelected(sharedVariables.channelNumberLeft);
      
    } else if (action.equals("Show Console Menu")) {
      /*
      if (sharedVariables.showConsoleMenu == true) {
        sharedVariables.showConsoleMenu = false;
        consolemenu.setSelected(false);
      } else {
        sharedVariables.showConsoleMenu = true;
        consolemenu.setSelected(true);
      }
      */
      sharedVariables.showConsoleMenu = !sharedVariables.showConsoleMenu;
      consolemenu.setSelected(sharedVariables.showConsoleMenu);
      
      try {
	for (int bam=0; bam<sharedVariables.openConsoleCount; bam++)
          consoleSubframes[bam].consoleMenu.setVisible(sharedVariables.showConsoleMenu);
      }	catch(Exception bal) {}
      
    } else if (action.equals("Show User Button Titles")) {
      /*
      if (sharedVariables.showButtonTitle == true) {
	sharedVariables.showButtonTitle=false;
	userbuttons.setSelected(false);
      } else {
	sharedVariables.showButtonTitle=true;
	userbuttons.setSelected(true);
      }
      */
      sharedVariables.showButtonTitle = !sharedVariables.showButtonTitle;
      userbuttons.setSelected(sharedVariables.showButtonTitle);
      
      for (int a=0; a<sharedVariables.maxUserButtons; a++)
        setButtonTitle(a);

    } else if (action.equals("Qsuggest Popups")) {
      /*
      if (sharedVariables.showQsuggest == true) {
	qsuggestPopup.setSelected(false);
	sharedVariables.showQsuggest=false;
      } else {
	qsuggestPopup.setSelected(true);
	sharedVariables.showQsuggest=true;
      }
      */
      sharedVariables.showQsuggest = !sharedVariables.showQsuggest;
      qsuggestPopup.setSelected(sharedVariables.showQsuggest);

    } else if (action.equals("Lantern Manual")) {
      mycreator.createWebFrame("http://www.lanternchess.com/lanternhelp/lantern-help.html");
      
    } else if (action.equals("Change Log")) {
      mycreator.createWebFrame("http://www.lanternchess.com/changelog.htm");
      
    } else if (action.equals("ICC Information Help Files")) {
      mycreator.createWebFrame("http://www.chessclub.com/help/info-list");
      
    } else if (action.equals("ICC Command Help Files")) {
      mycreator.createWebFrame("http://www.chessclub.com/help/help-list");

    } else if (action.equals("Join/Renew")) {
      openUrl("http://www.chessclub.com/tryicc/purchase.html");

    } else if (action.equals("Lost Password")) {
      openUrl("http://www.chessclub.com/helpcenter/mailpassword.html");
      
      /*
    } else if (action.equals("Start AutoExam")) {
      for (int a=0; a<sharedVariables.maxGameTabs; a++) {
	if (myboards[a]!=null) {
          if (sharedVariables.mygame[a].state == 2)
            myboards[a].setautoexamon();
	}
      }
      */

    } else if (action.equals("Load Winboard Engine") ||
               // Andrey edits:
               // merge the responses for loading engines
               action.equals("Load UCI Engine")) {
      boolean go = false;
      if (!sharedVariables.engineOn) {
        go=true;
        try {
          JFileChooser fc = new JFileChooser();
          fc.setCurrentDirectory(new File("."));

          int returnVal = fc.showOpenDialog(this);

          if (returnVal == JFileChooser.APPROVE_OPTION) {
            sharedVariables.engineFile = fc.getSelectedFile();
            //sharedVariables.uci = false;
            sharedVariables.uci = (action.equals("Load UCI Engine"));

            startTheEngine();
          }
        } catch (Exception e) {}
      }

      if (!go && !sharedVariables.engineOn)
        makeEngineWarning();
      
      /*    
    } else if (action.equals("Load UCI Engine")) {
      boolean go = false;
      if (!sharedVariables.engineOn) {
        go=true;
	try {
          JFileChooser fc = new JFileChooser();
          fc.setCurrentDirectory(new File("."));;

          int returnVal = fc.showOpenDialog(this);

          if (returnVal == JFileChooser.APPROVE_OPTION) {
            sharedVariables.engineFile = fc.getSelectedFile();
            sharedVariables.uci=true;
            startTheEngine();
          }
        } catch (Exception e) {}
      }

      if (!go && !sharedVariables.engineOn)
        makeEngineWarning();
      */
      
    } else if (action.equals("Set Application Background Color")) {
      try {
	JDialog frame = new JDialog();
        Color newColor = JColorChooser.showDialog(frame, "Application Color",
                                                  sharedVariables.MainBackColor);
        if (newColor != null) {
          sharedVariables.MainBackColor=newColor;
          sharedVariables.wallpaperImage=null;
          repaint();
        }
      } catch (Exception e) {}

    } else if (action.equals("Open Web")) {
      mycreator.createWebFrame("http://www.google.com");

    } else if (action.equals("Open Pgn")) {
      try {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));;

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String myfile = fc.getSelectedFile().toString();
          pgnLoader myLoader = new pgnLoader(myfile);
          tableClass myTableClass = new tableClass();
          myTableClass.createPgnListColumns();
          myLoader.loadTable(myTableClass);
          pgnFrame myPgnFrame = new pgnFrame(sharedVariables, queue,
                                             myTableClass, myLoader);
          sharedVariables.desktop.add(myPgnFrame);
          myPgnFrame.setSize(600,400);
          myPgnFrame.setVisible(true);
        }
      } catch (Exception nine) {}
      
    } else if (action.equals("Set Wallpaper")) {
      try {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          sharedVariables.wallpaperFile = fc.getSelectedFile();
          URL wallpaperURL = sharedVariables.wallpaperFile.toURL();
          //applet
          //sharedVariables.wallpaperImage=getImage(wallpaperURL);
          // end applet
          // stand alone
          sharedVariables.wallpaperImage =
            Toolkit.getDefaultToolkit().getImage(wallpaperURL);
          // end stand alone
        }

        repaint();
      // end try
      } catch (Exception e) {}

      // Andrey edits:
      // merging the various analysis settings into one response
    } else if (action.equals("Analysis Font") ||
               action.equals("Analysis Foreground Color") ||
               action.equals("Analysis Background Color")) {
      // Andrey edits:
      // checking if the new setting was made
      boolean newSetting = false;

      if (action.equals("Analysis Font")) {
        JFrame f = new JFrame("FontChooser Startup");
        FontChooser2 fc = new FontChooser2(f, sharedVariables.analysisFont);
        fc.setVisible(true);
        Font fnt = fc.getSelectedFont();
        
        if (fnt != null) {
          sharedVariables.analysisFont=fnt;
          newSetting = true;
        }
        
      } else {
        boolean foregroundColor = action.equals("Analysis Foreground Color");
        JDialog frame = new JDialog();
        Color analysisColor = (foregroundColor ?
                               sharedVariables.analysisForegroundColor :
                               sharedVariables.analysisBackgroundColor);
        
        Color newColor = JColorChooser.showDialog(frame, action, analysisColor);

        if (newColor != null) {
          if (foregroundColor)
            sharedVariables.analysisForegroundColor = newColor;
          else
            sharedVariables.analysisBackgroundColor = newColor;
          newSetting = true;
        }
      }

      if (newSetting) {
        for (int a=0; a<sharedVariables.maxGameTabs; a++)
          if (myboards[a] != null)
            if (sharedVariables.gamelooking[a] == sharedVariables.engineBoard) {
              if ((sharedVariables.mygame[sharedVariables.gamelooking[a]].state ==
                   sharedVariables.STATE_EXAMINING ||
                   sharedVariables.mygame[sharedVariables.gamelooking[a]].state ==
                   sharedVariables.STATE_OBSERVING) &&
                  sharedVariables.engineOn)
                if (sharedVariables.mygame[sharedVariables.gamelooking[a]].clickCount%2 == 1)
                  myboards[a].myconsolepanel.setEngineDoc();
            }
      }
      
      /*
    } else if (action.equals("Analysis Foreground Color")) {
      JDialog frame = new JDialog();
      Color newColor = JColorChooser.showDialog(frame, "Analysis Foreground Color",
                                                sharedVariables.analysisForegroundColor);
      if (newColor != null) {
        sharedVariables.analysisForegroundColor=newColor;

        // Andrey says:
        // marked for future edits
        for (int a=0; a<sharedVariables.maxGameTabs; a++)
          if (myboards[a] != null)
            if (sharedVariables.gamelooking[a] == sharedVariables.engineBoard) {
              if ((sharedVariables.mygame[sharedVariables.gamelooking[a]].state ==
                   sharedVariables.STATE_EXAMINING ||
                   sharedVariables.mygame[sharedVariables.gamelooking[a]].state ==
                   sharedVariables.STATE_OBSERVING) &&
                  sharedVariables.engineOn)
                if (sharedVariables.mygame[sharedVariables.gamelooking[a]].clickCount%2 == 1)
                  myboards[a].myconsolepanel.setEngineDoc();
            }
      }

    } else if (action.equals("Analysis Background Color")) {
      JDialog frame = new JDialog();
      Color newColor = JColorChooser.showDialog(frame, "Analysis Background Color",
                                                sharedVariables.analysisBackgroundColor);
      if (newColor != null) {
        sharedVariables.analysisBackgroundColor=newColor;

        // Andrey says:
        // marked for future edits
        for (int a=0; a<sharedVariables.maxGameTabs; a++)
          if (myboards[a] != null)
            if (sharedVariables.gamelooking[a] == sharedVariables.engineBoard) {
              if ((sharedVariables.mygame[sharedVariables.gamelooking[a]].state ==
                   sharedVariables.STATE_EXAMINING ||
                   sharedVariables.mygame[sharedVariables.gamelooking[a]].state ==
                   sharedVariables.STATE_OBSERVING) &&
                  sharedVariables.engineOn)
                if (sharedVariables.mygame[sharedVariables.gamelooking[a]].clickCount%2 == 1)
                  myboards[a].myconsolepanel.setEngineDoc();
            }
      }
      */
  
    } else if (action.equals("Stop Engine")) {

      if (sharedVariables.engineOn) {

        myoutput outgoing = new myoutput();
        outgoing.data = "exit\n";

        sharedVariables.engineQueue.add(outgoing);

        myoutput outgoing2 = new myoutput();
        outgoing2.data = "quit\n";

        sharedVariables.engineQueue.add(outgoing2);
        sharedVariables.engineOn=false;
      }

    } else if (action.equals("Restart Engine")) {
      if (!sharedVariables.engineOn)
	startTheEngine();

    } else if (action.equals("Activities Window")) {
      openActivities();

    } else if (action.equals("Notify Window")) {

      myNotifyFrame.notifylistScrollerPanel.theNotifyList.setBackground(sharedVariables.listColor);
      myNotifyFrame.setVisible(true);

    } else if (action.equals("Seek Graph")) {
      openSeekGraph();

    } else if (action.equals("Send iloggedon")) {
      /*
      if (sharedVariables.iloggedon== false) {
        sharedVariables.iloggedon=true;
        iloggedon.setSelected(true);
      } else {
        sharedVariables.iloggedon=false;
        iloggedon.setSelected(false);
      }
      */
      sharedVariables.iloggedon = !sharedVariables.iloggedon;
      iloggedon.setSelected(sharedVariables.iloggedon);
      
    } else if (action.equals("Channel Notify Map")) {
      String mess="Map of people on channel notify.\n\n";
      
      for (int z=0; z<sharedVariables.channelNotifyList.size(); z++)
        if (sharedVariables.channelNotifyList.get(z).nameList.size()>0) {
          mess+="\n#" + sharedVariables.channelNotifyList.get(z).channel + " ";
          for (int x=0; x < sharedVariables.channelNotifyList.get(z).nameList.size(); x++)
            mess+=sharedVariables.channelNotifyList.get(z).nameList.get(x) + " ";
        }
      
      Popup mypopper = new Popup(this, false, mess);
      mypopper.setSize(600,500);
      mypopper.setVisible(true);

    } else if(action.equals("Channel Notify Online")) {
  
      String mess = sharedVariables.getChannelNotifyOnline();
      mess += sharedVariables.getConnectNotifyOnline();
      Popup mypopper = new Popup(this, false, mess);
      mypopper.setSize(600,500);
      mypopper.setVisible(true);

    } else if (action.equals("Channel Map")) {

      String mymap="Map of channels, shouts and sshouts moved to tabs.\n\n";

      for (int a=1; a<sharedVariables.maxConsoleTabs; a++) {
        mymap+="C" + a +  ": ";
        for (int aa=0; aa<500; aa++)
          if (sharedVariables.console[a][aa] == 1) {
            mymap+=aa;

            if (sharedVariables.mainAlso[aa])
              mymap+="m";

            mymap+= " ";
          }

        if (sharedVariables.shoutRouter.shoutsConsole == a)
          mymap += "Shouts ";
        if (sharedVariables.shoutRouter.sshoutsConsole == a)
          mymap += "S-Shouts ";
        mymap+="\n";
      }

      Popup mypopper = new Popup(this, false, mymap);
      mypopper.setSize(600,500);
      mypopper.setVisible(true);

    } else if (action.equals("Send Game End Messages")) {
      /*  
      if (sharedVariables.gameend == false) {// activate
        gameend.setSelected(true);
        sharedVariables.gameend=true;
      */
      sharedVariables.gameend = !sharedVariables.gameend;
      gameend.setSelected(sharedVariables.gameend);

      if (sharedVariables.gameend) {
        String mes1 = "Lantern will send to the server the commands: " +
          "gameendwin, gameendloss, gameenddraw.  These will produce " +
          "'command not found: gameendwin' for example untill you alias" +
          " them to do 'says' which speak to last opponent.\n\n";
        String mes2 = "type for example: +alias gameendloss say darnit lost again\n\n";
        String mes3 = "Create 3 gameend alias's, one for each type, " +
          "gameendwin, gameenddraw, gameendloss.  format is type in main" +
          " console +alias   then the alias name, then the command which" +
          " should start with 'say' to speak to your opponent\n";
        Popup mypopper = new Popup(this, false, mes1 + mes2 + mes3);
        mypopper.setSize(600,500);
        mypopper.setVisible(true);
      }
      /*
      } else {// deactivate
        gameend.setSelected(false);
        sharedVariables.gameend=false;
      }
      */
      // end gameend menu item
      
  } else if (action.equals("Rotate Away Message")) {
      if (!sharedVariables.rotateAways) {
        scriptLoader loadScripts = new scriptLoader();
        sharedVariables.lanternAways.clear();
        loadScripts.loadScript(sharedVariables.lanternAways, "lantern_away.txt");
        if (sharedVariables.lanternAways.size() > 0) {
          rotateaways.setSelected(true);
          sharedVariables.rotateAways=true;
          // size > 0
        } else {
          String mes="lantern_away.txt not found or has nothing in it.  " +
            "Create a file called lantern_away.txt and put away messages " +
            "in it till you run out of ideas, then reselect this option";
          Popup mypopper = new Popup(this, false, mes);
          mypopper.setVisible(true);
          rotateaways.setSelected(false);
        }// else size not > 0

        // if rotateAways == false
      } else {
        rotateaways.setSelected(false);
        sharedVariables.rotateAways=false;
      }// if rotate aways true

    } else if (action.equals("What's Examine Game Replay Quick Help")) {
      String mes = "If Examining a game from a history (including your own)," +
        " library or search list, you can have Lantern issue the command " +
        "forward 1, at a set interval with delay set by the user between " +
        "moves.\n\nFor example go to the Actions menu and choose Examine My " +
        "Last game, then to to Start Examine Game Replay.";
      Popup mypopper = new Popup(this, false, mes);
      mypopper.setSize(300,350);
      mypopper.setVisible(true);
      /*
    } else if (action.equals("Stop AutoExam")) {
      sharedVariables.autoexam=0;
      */

    } else if (action.equals("AutoExam Dialog")) {
    //} else if (action.equals("Set AutoExam Speed")) {
      autoExamDialog frame = new autoExamDialog((JFrame) this, false,
                                                sharedVariables, myboards);
      frame.pack();
      frame.setVisible(true);


    } else if (action.equals("Reconnect to Queen") ||
      // Andrey edits:
      // merging queen and main reconnect
               action.equals("Reconnect to ICC")) {
      try {
        sharedVariables.myServer = "ICC";
        sharedVariables.chessclubIP = (action.equals("Reconnect to Queen") ?
                                       "207.99.83.231" :
                                       "207.99.83.228");
        sharedVariables.doreconnect=true;
        //if (myConnection == null)
        if (myConnection == null || !myConnection.isVisible())
          myConnection = new connectionDialog(this, sharedVariables, queue, false);
        //else if (!myConnection.isVisible())
        //  myConnection = new connectionDialog(this, sharedVariables, queue, false);

        myConnection.setVisible(true);
      } catch(Exception conn) {}

      /*
    } else if (action.equals("Reconnect to ICC")) {

      try {
	sharedVariables.myServer="ICC";
        sharedVariables.chessclubIP = "207.99.83.228";
        sharedVariables.doreconnect=true;
        if(myConnection == null)
          myConnection = new connectionDialog(this, sharedVariables, queue, false);
        else if(!myConnection.isVisible())
          myConnection = new connectionDialog(this, sharedVariables, queue, false);
        myConnection.setVisible(true);
      } catch(Exception conn) {}
      */

    } else if (action.equals("Reconnect to FICS")) {

      sharedVariables.myServer="FICS";
      sharedVariables.doreconnect=true;

    } else if (action.equals("Save Settings")) {
      /*
      sharedVariables.activitiesOpen = false;
      if (myfirstlist != null)
        if (myfirstlist.isVisible())
          sharedVariables.activitiesOpen = true;
      if (mysecondlist != null)
        if (mysecondlist.isVisible())
          sharedVariables.activitiesOpen = true;

      sharedVariables.seeksOpen = false;
      if (seekGraph != null)
        if (seekGraph.isVisible())
          sharedVariables.seeksOpen = true;
      */
      sharedVariables.activitiesOpen =
        (myfirstlist != null && myfirstlist.isVisible() ||
         mysecondlist != null && mysecondlist.isVisible());

      sharedVariables.seeksOpen = (seekGraph != null &&
                                   seekGraph.isVisible());

      mysettings.saveNow(myboards, consoleSubframes, sharedVariables);
      mineScores.saveNow(sharedVariables);
      sharedVariables.hasSettings = true;
      sharedVariables.activitiesOpen = false;
      // it gets set to true on close and here, needs to be false so
      // it's checked on close
      sharedVariables.seeksOpen = false;

    } else if (action.equals("ToolBox")) {
      toolboxDialog mybox = new toolboxDialog(this, false, queue,
                                              sharedVariables);
      mybox.setSize(500,450);
      mybox.setLocation(200,250);
      mybox.setVisible(true);

    } else if (action.equals("Customize User Buttons")) {
      userButtonsDialog mydialog = new userButtonsDialog((JFrame) this,
                                                         sharedVariables);
      mydialog.setSize(400,400);
      mydialog.setVisible(true);

    } else if (action.equals("Toolbar")) {
      /*
      if (sharedVariables.toolbarVisible == true) {
        toolbarvisible.setSelected(false);
        sharedVariables.toolbarVisible = false;
        toolBar.setVisible(false);
      } else {
        toolbarvisible.setSelected(true);
        sharedVariables.toolbarVisible = true;
        toolBar.setVisible(true);
      }
      */
      sharedVariables.toolbarVisible = !sharedVariables.toolbarVisible;
      toolbarvisible.setSelected(sharedVariables.toolbarVisible);
      toolBar.setVisible(sharedVariables.toolbarVisible);
      
    } else if (action.equals("New Console")) {
      createChannelConsoleDialog frame =
        new createChannelConsoleDialog((JFrame) this, true, sharedVariables,
                                       mycreator, consoleSubframes);
      frame.setSize(550,120);
      frame.setVisible(true);

    } else if (action.equals("Customize Tab")) {
      int hasfocus=-1;
      for (int nn=0; nn<sharedVariables.openConsoleCount; nn++)
	if (consoleSubframes[nn] != null &&
            consoleSubframes[nn].isSelected())
          hasfocus=nn;

      if (hasfocus == -1) {
        String swarning = "First click or select a console window, " +
          "and change tab to one to customize.";
        Popup pframe = new Popup((JFrame) this, true, swarning);
        pframe.setVisible(true);
        return;
      }

      // new idea we know the console lets find the tab
      hasfocus = sharedVariables.looking[hasfocus];

      String consoleWithFocus = "No console has focus";
      if (hasfocus > 0) {
	customizeChannelsDialog frame =
          new customizeChannelsDialog((JFrame) this, false, hasfocus,
                                      sharedVariables, consoleSubframes);
      } else {
        String swarning = "The currently selected window is looking at the " +
          "main console tab, this can't be customized, click C1, C2, etc., first.";
        Popup pframe = new Popup((JFrame) this, true, swarning);
        pframe.setVisible(true);
        return;
      }

    } else if (action.equals("New Detached Chat Console")) {
      /*
      boolean makeChatter = false;
      if (consoleChatframes[11] == null)
        makeChatter=true;
      else if (!consoleChatframes[11].isVisible())
        makeChatter=true;

      if (makeChatter == true) {
        sharedVariables.chatFrame=11;
        consoleChatframes[11] =
          new chatframe(sharedVariables, consoles, queue, mycreator.myDocWriter);
        consoleChatframes[11].setVisible(true);
      } else {
        if (consoleChatframes[10] == null)
          makeChatter=true;
        else if (!consoleChatframes[10].isVisible())
          makeChatter=true;
        if (makeChatter == true) {
          sharedVariables.chatFrame=10;
          consoleChatframes[10] =
            new chatframe(sharedVariables, consoles, queue, mycreator.myDocWriter);
          consoleChatframes[10].setVisible(true);
        } else {
          Popup mypopper =
            new Popup(this, false, "Can only have two detached chat frames open now");
          mypopper.setVisible(true);
        }
      }
      */

      // Andrey says:
      // worth looking at further
      int detachedIndex = 11;
      while (detachedIndex > 9 &&
             consoleChatframes[detachedIndex] != null &&
             consoleChatframes[detachedIndex].isVisible())
        detachedIndex--;

      if (detachedIndex > 9) {
        sharedVariables.chatFrame = detachedIndex;
        consoleChatframes[detachedIndex] =
          new chatframe(sharedVariables, consoles, queue, mycreator.myDocWriter);
        consoleChatframes[detachedIndex].setVisible(true);
      } else {
        Popup mypopper =
          new Popup(this, false, "Can only have two detached chat frames open now.");
        mypopper.setVisible(true);
      }

    } else if (action.equals("New Chat Console")) {
      mycreator.restoreConsoleFrame();

    } else if (action.equals("New Board")) {
      mycreator.createGameFrame();

    } else if (action.equals("Cascade")) {
      int x=160;
      int y=120;
      int width=400;
      int height=300;
      int dif=30;
      int count=0;
      try {
	for (int a=0; a<sharedVariables.openConsoleCount; a++)
          if (consoleSubframes[a] != null &&
              consoleSubframes[a].isVisible()) {
            consoleSubframes[a].setSize(width,height);
            consoleSubframes[a].setLocation(x + count * dif, y + count * dif);
            consoleSubframes[a].setSelected(true);
            count++;
          }
        
	for (int a=0; a<sharedVariables.openBoardCount; a++)
          if (myboards[a] != null && myboards[a].isVisible()) {
            if (!sharedVariables.useTopGames) {
              myboards[a].setSize(width,height);
              myboards[a].setLocation(x + count * dif, y + count * dif);
              myboards[a].setSelected(true);
            } else {
              if (myboards[a].topGame != null) {
                myboards[a].topGame.setSize(width,height);
                myboards[a].topGame.setLocation(x + count * dif, y + count * dif);
              }
            }
            count++;
          }

	if (myfirstlist != null && myfirstlist.isVisible()) {
          myfirstlist.setSize(width,height);
          myfirstlist.setLocation(x + count * dif, y + count * dif);
          //myfirstlist.setSelected(true);
        }
        
	if (mysecondlist != null && mysecondlist.isVisible()) {
          mysecondlist.setSize(width,height);
          mysecondlist.setLocation(x + count * dif, y + count * dif);
          mysecondlist.setSelected(true);
        }
      } catch(Exception d) {}

    } else if (action.equals("Make Boards Always On Top")) {
      /*
      SwingUtilities.invokeLater(new Runnable() {
          @Override
            public void run() {
            try {
              int x=160;
              int y=120;
              int width=400;
              int height=300;
              int dif=30;
              int count=0;

              try {

                for (int a=0; a<myboards.length; a++)
                  if (myboards[a]!=null) {
                    if (sharedVariables.useTopGames == true) {
                      myboards[a].switchFrame(false);

                      myboards[a].setSize(width,height);
                      myboards[a].setLocation(x + count * dif, y + count * dif);
                      myboards[a].setSelected(true);

                      myboards[a].topGame.setAlwaysOnTop(false);
                    } else {
                      if (myboards[a].topGame != null) {
                        myboards[a].switchFrame(true);
                        
                        myboards[a].topGame.setSize(width,height);
                        myboards[a].topGame.setLocation(x + count * dif, y + count * dif);
                        
                        myboards[a].topGame.setAlwaysOnTop(true);
                        myboards[a].setVisible(false);
                      }
                    }// end else
                    count++;
                    // if not null
                  } else
                    break;
              } catch(Exception d) {}
              if (sharedVariables.useTopGames == true)
                sharedVariables.useTopGames = false;
              else
                sharedVariables.useTopGames = true;
            } catch (Exception e1) {
              //ignore
            }
          }
        });

      SwingUtilities.invokeLater(new Runnable() {
          @Override
            public void run() {
            try {
              for (int b=0; b< myboards.length; b++)
                if (myboards[b]!=null && sharedVariables.useTopGames == true)
                  myboards[b].setVisible(false);
            } catch (Exception e1) {
              //ignore
            }
          }
        });
      */

      //lantern_board_on_top.txt

      boolean ontop = getOnTopSetting();

      FileWrite mywriter = new FileWrite();
      /*
      if (ontop == false) {
        String mess = "Next time you start the program, boards will be on top windows.";
        Popup mypopper = new Popup(this, true, mess);
        mypopper.setVisible(true);
        mywriter.write("true\r\n", "lantern_board_on_top.txt");
      } else {
        String mess = "Next time you start the program, boards will NOT be on top windows.";
        Popup mypopper = new Popup(this, true, mess);
        mypopper.setVisible(true);
        mywriter.write("false\r\n", "lantern_board_on_top.txt");
      }
      */
      String mess = "Next time you start the program, boards " +
        (ontop ? "will NOT" : "will") + " be on top windows.";
      Popup mypopper = new Popup(this, true, mess);
      mypopper.setVisible(true);
      mywriter.write((ontop ? "false" : "true") + "\r\n", "lantern_board_on_top.txt");

    } else if (action.equals("Get a Game")) {

      seekGameDialog myseeker = new seekGameDialog(this, false, sharedVariables, queue);
      int defaultWidth = 425;
      int defaultHeight = 260;
      myseeker.setSize(defaultWidth,defaultHeight);

      try {
	Toolkit toolkit =  Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        int screenW = dim.width;
        int screenH = dim.height;
        int px = (int) ((screenW - defaultWidth) / 2);
        if (px < 50)
          px = 50;
        int py = (int) ((screenH - defaultHeight) / 2);
        if (py < 50)
          py=50;

        myseeker.setLocation(px, py);
      } catch (Exception centerError) {}

      myseeker.setTitle("Get a Game");

      myseeker.setVisible(true);

    } else if (action.equals("Log Pgn")) {
      /*
      if(sharedVariables.pgnLogging == true) {
        sharedVariables.pgnLogging = false;
        pgnlogging.setSelected(false);
      } else {
        sharedVariables.pgnLogging = true;
        pgnlogging.setSelected(true);
      }
      */
      sharedVariables.pgnLogging = !sharedVariables.pgnLogging;
      pgnlogging.setSelected(sharedVariables.pgnLogging);

    } else if (action.equals("Log Observed Games To Pgn")) {
      /*
      if (sharedVariables.pgnObservedLogging == true) {
        sharedVariables.pgnObservedLogging = false;
        pgnObservedLogging.setSelected(false);
      } else {
        sharedVariables.pgnObservedLogging = true;
        pgnObservedLogging.setSelected(true);
      */
      sharedVariables.pgnObservedLogging = !sharedVariables.pgnObservedLogging;
      pgnObservedLogging.setSelected(sharedVariables.pgnObservedLogging);
      if (sharedVariables.pgnObservedLogging) {
        String s = "Lantern will log bullet, blitz and standard games " +
          "you observe to lantern_obullet.pgn, lantern_oblitz.pgn, and " +
          "lantern_ostandard.pgn.\n\n  Not currently configured to log wild games.";
        Popup temp = new Popup(this, false, s); 
        temp.setVisible(true);
      }

      // Andrey edits:
      // merging the aspect actions
    } else if (action.equals("1:1") ||
               action.equals("5:4") ||
               action.equals("4:3") ||
               action.equals("3:2")) {
      sharedVariables.aspect = (action.equals("1:1") ? 0 :
                                (action.equals("5:4") ? 1 :
                                 (action.equals("4:3") ? 2 : 3)));
      // Andrey says:
      // I believe this can be done with a button group
      aspect0.setSelected((sharedVariables.aspect == 0));
      aspect1.setSelected((sharedVariables.aspect == 1));
      aspect2.setSelected((sharedVariables.aspect == 2));
      aspect3.setSelected((sharedVariables.aspect == 3));
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a]!=null && myboards[a].isVisible())
          myboards[a].mypanel.repaint();
      
      /*
    } else if (action.equals("5:4")) {
      sharedVariables.aspect=1;
      aspect0.setSelected(false);
      aspect1.setSelected(true);
      aspect2.setSelected(false);
      aspect3.setSelected(false);
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a]!=null)
          if (myboards[a].isVisible() == true)
            myboards[a].mypanel.repaint();

    } else if (action.equals("4:3")) {
      sharedVariables.aspect=2;
      aspect0.setSelected(false);
      aspect1.setSelected(false);
      aspect2.setSelected(true);
      aspect3.setSelected(false);
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a]!=null)
          if (myboards[a].isVisible() == true)
            myboards[a].mypanel.repaint();

    } else if (action.equals("3:2")) {
      sharedVariables.aspect=3;
      aspect0.setSelected(false);
      aspect1.setSelected(false);
      aspect2.setSelected(false);
      aspect3.setSelected(true);
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a]!=null)
          if (myboards[a].isVisible() == true)
            myboards[a].mypanel.repaint();
      */
      
    } else if (action.equals("Hide Board Console")) {
      sharedVariables.boardConsoleType = 0;
      sharedVariables.sideways = false;
      sidewaysconsole.setSelected(false);
	
      redrawBoard(sharedVariables.boardConsoleType);

    } else if (action.equals("Compact Board Console")) {
      compactConsole();

    } else if (action.equals("Normal Board Console")) {
      normalConsole();

    } else if (action.equals("Larger Board Console")) {
      largerConsole();

    } else if (action.equals("Console On Side")) {
      sideConsole();

      // Andrey edits:
      // merge the board actions
    } else if (action.equals("Default Board") ||
               action.equals("Tan Board") ||
               action.equals("Gray Color Board") ||
               action.equals("Blitzin Green Board")) {
      int boardTypeIndex = (action.equals("Default Board") ? 0 :
                            (action.equals("Tan Board") ? 1 :
                             (action.equals("Gray Color Board") ? 2 : 3)));
      sharedVariables.boardType = 0;
      sharedVariables.lightcolor=sharedVariables.preselectBoards.light[boardTypeIndex];
      sharedVariables.darkcolor=sharedVariables.preselectBoards.dark[boardTypeIndex];

      setBoard(0);

      /*
    } else if (action.equals("Tan Board")) {
      sharedVariables.boardType = 0;
      sharedVariables.lightcolor=sharedVariables.preselectBoards.light[1];
      sharedVariables.darkcolor=sharedVariables.preselectBoards.dark[1];

      setBoard(0);

    } else if (action.equals("Gray Color Board")) {
      sharedVariables.boardType = 0;
      sharedVariables.lightcolor=sharedVariables.preselectBoards.light[2];
      sharedVariables.darkcolor=sharedVariables.preselectBoards.dark[2];

      setBoard(0);

    } else if (action.equals("Blitzin Green Board")) {
      sharedVariables.boardType = 0;
      sharedVariables.lightcolor=sharedVariables.preselectBoards.light[3];
      sharedVariables.darkcolor=sharedVariables.preselectBoards.dark[3];

      setBoard(0);
      */

      // Andrey edits:
      // merge the board color settings
    } else if (action.equals("Board Clock Background Color") ||
               action.equals("Board Background Color") ||
               action.equals("Highlight Moves Color") ||
               action.equals("Scroll Back Highlight Color") ||
               action.equals("Board Foreground Color") ||
               action.equals("Clock Foreground Color") ||
               action.equals("Light Square Color") ||
               action.equals("Dark Square Color")) {

      JDialog frame = new JDialog();

      int boardSetting =
        (action.equals("Board Clock Background Color") ? 0 :
         (action.equals("Board Background Color") ? 1 :
          (action.equals("Highlight Moves Color") ? 2 :
           (action.equals("Scroll Back Highlight Color") ? 3 :
            (action.equals("Board Foreground Color") ? 4 :
             (action.equals("Clock Foreground Color") ? 5 :
              (action.equals("Light Square Color") ? 6 : 7)))))));

      Color boardSettingColor =
        (boardSetting == 0 ? sharedVariables.onMoveBoardBackgroundColor :
         (boardSetting == 1 ? sharedVariables.boardBackgroundColor :
          (boardSetting == 2 ? sharedVariables.highlightcolor :
           (boardSetting == 3 ? sharedVariables.scrollhighlightcolor :
            (boardSetting == 4 ? sharedVariables.boardForegroundColor :
             (boardSetting == 5 ? sharedVariables.clockForegroundColor :
              (boardSetting == 6 ? sharedVariables.lightcolor :
               sharedVariables.darkcolor)))))));

      Color newColor =
        JColorChooser.showDialog(frame, action, boardSettingColor);

      if (newColor != null) {
        if (boardSetting == 0)
          sharedVariables.onMoveBoardBackgroundColor = newColor;
        else if (boardSetting == 1)
          sharedVariables.boardBackgroundColor = newColor;
        else if (boardSetting == 2)
          sharedVariables.highlightcolor = newColor;
        else if (boardSetting == 3)
          sharedVariables.scrollhighlightcolor = newColor;
        else if (boardSetting == 4)
          sharedVariables.boardForegroundColor = newColor;
        else if (boardSetting == 5)
          sharedVariables.clockForegroundColor = newColor;
        else if (boardSetting == 6)
          sharedVariables.lightcolor = newColor;
        else // if (boardSetting == 7)
          sharedVariables.darkcolor = newColor;
      }
      
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a] != null)
          //if(myboards[a].isVisible() == true)
          myboards[a].repaint();

      /*
    } else if (action.equals("Board Background Color")) {

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Board Background Color",
                                 sharedVariables.boardBackgroundColor);
      if (newColor != null)
        sharedVariables.boardBackgroundColor=newColor;
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a] != null)
          //if(myboards[a].isVisible() == true)
          myboards[a].repaint();

    } else if (action.equals("Highlight Moves Color")) {

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Highlight Moves Color",
                                 sharedVariables.highlightcolor);
      if (newColor != null)
        sharedVariables.highlightcolor=newColor;
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a]!=null)
          //if(myboards[a].isVisible() == true)
          myboards[a].repaint();

    } if (action.equals("Scroll Back Highlight Color")) {

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Scroll Back Highlight Color",
                                 sharedVariables.scrollhighlightcolor);
      if (newColor != null)
        sharedVariables.scrollhighlightcolor=newColor;
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a] != null)
          //if(myboards[a].isVisible() == true)
          myboards[a].repaint();

    } else if (action.equals("Board Foreground Color")) {

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Board Foreground Color",
                                 sharedVariables.boardForegroundColor);
      if (newColor != null)
        sharedVariables.boardForegroundColor=newColor;
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a] != null)
          //if(myboards[a].isVisible() == true)
          myboards[a].repaint();

    } else if (action.equals("Clock Foreground Color")) {

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Clock Foreground Color",
                                 sharedVariables.clockForegroundColor);
      if (newColor != null)
        sharedVariables.clockForegroundColor=newColor;
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a]!=null)
          //if(myboards[a].isVisible() == true)
          myboards[a].repaint();

    } else if (action.equals("Light Square Color")) {

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Light Square Color",
                                 sharedVariables.lightcolor);
      if (newColor != null)
        sharedVariables.lightcolor=newColor;
      for (int a=0; a< sharedVariables.maxGameTabs; a++)
        if (myboards[a]!=null)
          //if(myboards[a].isVisible() == true)
          myboards[a].mypanel.repaint();

    } else if (action.equals("Dark Square Color")) {

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Dark Square Color",
                                 sharedVariables.darkcolor);
      if (newColor != null)
        sharedVariables.darkcolor=newColor;
      for (int a=0; a<sharedVariables.maxGameTabs; a++)
        if (myboards[a]!=null)
          //if(myboards[a].isVisible() == true)
          myboards[a].mypanel.repaint();
      */

      // Andrey says:
      // the next few actions appear to be no longer called
      /*
    } else if (action.equals("Titles In Channel Color")) {
      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Set Titles In Channel Color",
                                 sharedVariables.channelTitlesColor);
      if (newColor != null)
        sharedVariables.channelTitlesColor=newColor;

    } else if (action.equals("Channel Name Color")) {
      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Set Channel Name Color",
                                 sharedVariables.qtellChannelNumberColor);
      if (newColor != null)
        sharedVariables.qtellChannelNumberColor=newColor;

      // Andrey edits:
      // merge brighter channel name color with darker
    } else if (action.equals("Brighter Channel Name Color") ||
               action.equals("Darker Channel Name Color")) {
      String mycolstring;
      float[] hsbValues = new float[3];
      Color col2 = sharedVariables.qtellChannelNumberColor;
      hsbValues = Color.RGBtoHSB(col2.getRed(), col2.getGreen(),
                                 col2.getBlue(), hsbValues);
      float hue, saturation, brightness;
      hue = hsbValues[0];
      saturation = hsbValues[1];
      brightness = hsbValues[2];
      mycolstring = "color values were hue= " + hue + " and saturation= " +
        saturation + " and brightness=" + brightness + " and red=" +
        col2.getRed() + " and blue =" + col2.getGreen() + " and green=" +
        col2.getBlue() + " ";

      sharedVariables.qtellChannelNumberColor =
        (action.equals("Brighter Channel Name Color") ?
         col2.brighter() : col2.darker());

      col2 = sharedVariables.qtellChannelNumberColor;
      hsbValues = Color.RGBtoHSB(col2.getRed(), col2.getGreen(),
                                 col2.getBlue(), hsbValues);

      hue = hsbValues[0];
      saturation = hsbValues[1];
      brightness = hsbValues[2];
      mycolstring = mycolstring + " color values now hue= " + hue +
        " and saturation= " + saturation + " and brightness=" + brightness +
        " and red=" + col2.getRed() + " and blue =" + col2.getGreen() +
        " and green=" + col2.getBlue() + " ";
      Popup mypopper= new Popup(this, false, mycolstring);
      mypopper.setVisible(true);

    } else if (action.equals("Darker Channel Name Color")) {

      String mycolstring = "";
      float[] hsbValues = new float[3];
      Color col2 = sharedVariables.qtellChannelNumberColor;
      hsbValues = Color.RGBtoHSB(col2.getRed(), col2.getGreen(),col2.getBlue(), hsbValues);
      float hue, saturation, brightness;
      hue = hsbValues[0];
      saturation = hsbValues[1];
      brightness = hsbValues[2];
      mycolstring = "color values were hue= " + hue + " and saturation= " +
        saturation + " and brightness=" + brightness + " and red=" +
        col2.getRed() + " and blue =" + col2.getGreen() + " and green=" +
        col2.getBlue() + " ";

      sharedVariables.qtellChannelNumberColor =
        sharedVariables.qtellChannelNumberColor.darker();

      col2 = sharedVariables.qtellChannelNumberColor;
      hsbValues = Color.RGBtoHSB(col2.getRed(), col2.getGreen(),col2.getBlue(), hsbValues);

      hue = hsbValues[0];
      saturation = hsbValues[1];
      brightness = hsbValues[2];
      mycolstring = mycolstring + " color values now hue= " + hue +
        " and saturation= " + saturation + " and brightness=" + brightness +
        " and red=" + col2.getRed() + " and blue =" + col2.getGreen() +
        " and green=" + col2.getBlue() + " ";

      Popup mypopper= new Popup(this, false, mycolstring);
      mypopper.setVisible(true);
      */
      
    } else if (action.equals("PTell Name Color")) {
      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Tell Name Color",
                                 sharedVariables.tellNameColor);
      if (newColor != null)
        sharedVariables.tellNameColor = newColor;

    } else if (action.equals("Names List Foreground Color")) {
      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Names List Foreground Color",
                                 sharedVariables.nameForegroundColor);
      if (newColor != null) {
        sharedVariables.nameForegroundColor = newColor;
        sharedVariables.activitiesPanel.theChannelList.setForeground(sharedVariables.nameForegroundColor);
        sharedVariables.activitiesPanel.theChannelList2.setForeground(sharedVariables.nameForegroundColor);
        sharedVariables.activitiesPanel.theChannelList3.setForeground(sharedVariables.nameForegroundColor);
        for (int c=0; c<sharedVariables.maxConsoleTabs; c++) {
          if (consoleSubframes[c] != null) {
            consoleSubframes[c].myNameList.setForeground(newColor);
          }
        }
      }

    } else if (action.equals("Names List Background Color")) {
      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Names List Background Color",
                                 sharedVariables.nameBackgroundColor);
      if (newColor != null) {
        sharedVariables.nameBackgroundColor = newColor;

        sharedVariables.activitiesPanel.theChannelList.setBackground(sharedVariables.nameBackgroundColor);
        sharedVariables.activitiesPanel.theChannelList2.setBackground(sharedVariables.nameBackgroundColor);
        sharedVariables.activitiesPanel.theChannelList3.setBackground(sharedVariables.nameBackgroundColor);

        for (int c=0; c<sharedVariables.maxConsoleTabs; c++) {
          if (consoleSubframes[c] != null) {
            consoleSubframes[c].myNameList.setBackground(newColor);
          }
        }// end for
      }//end if not null
      // end if name list background

      // Andrey edits:
      // merge all the commands sent from the action menu
    } else if (action.equals("Show My Recent Games") ||
               action.equals("Show My Game Library") ||
               action.equals("Show My Adjourned Games") ||
               action.equals("Show My Profile and Ratings") ||
               action.equals("Enter Examination Mode") ||
               action.equals("Examine My Last Game") ||
               action.equals("Observe High Rated Game") ||
               action.equals("Observe High Rated 5-Minute Game") ||
               action.equals("Observe High Rated 15-Minute Game") ||
               action.equals("Stop Following") ||
               action.equals("Follow Broadcast- When On") ||
               action.equals("Withdraw Challenges")) {
      if (action.equals("Follow Broadcast- When On"))
        client.writeToSubConsole("Be sure to turn on the radio by opening ChessFM, " +
                                 "Actions - Open ChessFM in the menu.\n", 0);
        
      //String actionmess = "History\n";
      String actionmess =
        (action.equals("Show My Recent Games") ? "History" :
         (action.equals("Show My Game Library") ? "Liblist" :
          (action.equals("Show My Adjourned Games") ? "Stored" :
           (action.equals("Show My Profile and Ratings") ? "Finger" :
            (action.equals("Enter Examination Mode") ? "Examine" :
             (action.equals("Examine My Last Game") ? "Examine -1" :
              (action.equals("Observe High Rated Game") ? "Observe *" :
               (action.equals("Observe High Rated 5-Minute Game") ? "Observe *f" :
                (action.equals("Observe High Rated 15-Minute Game") ? "Observe *P" :
                 (action.equals("Stop Following") ? "Unfollow" :
                  (action.equals("Follow Broadcast- When On") ? "Follow Broadcast" :
                                 "Match"))))))))))) + "\n";

      
      if (sharedVariables.myServer.equals("ICC"))
        actionmess = "`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);
      
      /*
    } else if (action.equals("Show My Game Library")) {
      String actionmess = "Liblist\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data = actionmess;
      queue.add(data);

    } else if (action.equals("Show My Adjourned Games")) {
      String actionmess = "Stored\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data = actionmess;
      queue.add(data);

    } else if (action.equals("Show My Profile and Ratings")) {
      String actionmess="Finger\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);

    } else if(action.equals("Enter Examination Mode")) {
      String actionmess="Examine\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);

    } else if(action.equals("Examine My Last Game")) {
      String actionmess="Examine -1\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);
      
    } else if (action.equals("Observe High Rated Game")) {
      String actionmess="Observe *\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);

    } else if (action.equals("Observe High Rated 5-Minute Game")) {
      String actionmess="Observe *f\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);

    } else if (action.equals("Observe High Rated 15-Minute Game")) {
      String actionmess="Observe *P\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);
      */
      
    } else if (action.equals("Show Relay Schedule")) {
      openUrl("http://www.chessclub.com/activities/relays.html");

    } else if (action.equals("Add a Friend")) {
      addFriendDialog frame = new addFriendDialog(this, false, sharedVariables, queue);

      /*
    } else if (action.equals("Stop Following")) {

      String actionmess="Unfollow\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);

    } else if (action.equals("Follow Broadcast- When On")) {

      client.writeToSubConsole("Be sure to turn on the radio by opening ChessFm, " +
                               "Actions - Open ChessFm in the menu.\n", 0);

      String actionmess="Follow Broadcast\n";
      if (sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);
      */
      
    } else if (action.equals("Open ChessFM")) {

      // ?user=me&pass=pass

      /*
      if (sharedVariables.myname.length() > 0 &&
          sharedVariables.mypassword.length() > 0)
        openUrl("http://www.chessclub.com/chessfm/?user=" + sharedVariables.myname +
                "&pass=" + sharedVariables.mypassword);
      else
      */
      openUrl("http://www.chessclub.com/chessfm/");

    } else if(action.equals("Show Rating Graphs")) {
      openUrl("https://www.chessclub.com/cgi-auth/web_dev_perl/graph-rating.pl"); 

    } else if(action.equals("Events List Font")) {
      JFrame f = new JFrame("Events List Font");
      FontChooser2 fc = new FontChooser2(f, sharedVariables.eventsFont);
      fc.setVisible(true);
      Font fnt = fc.getSelectedFont();
      if (fnt != null) {
        sharedVariables.eventsFont=fnt;
        sharedVariables.activitiesPanel.theEventsList.setFont(sharedVariables.eventsFont);
      }
      // end events font

    } else if (action.equals("Names List Font")) {
      JFrame f = new JFrame("FontChooser Startup");
      FontChooser2 fc = new FontChooser2(f, sharedVariables.nameListFont);
      fc.setVisible(true);
      Font fnt = fc.getSelectedFont();
      if (fnt != null) {
        sharedVariables.nameListFont = fnt;
        sharedVariables.activitiesPanel.theChannelList.setFont(sharedVariables.nameListFont);
        sharedVariables.activitiesPanel.theChannelList2.setFont(sharedVariables.nameListFont);
        sharedVariables.activitiesPanel.theChannelList3.setFont(sharedVariables.nameListFont);

        for (int c=0; c<sharedVariables.maxConsoleTabs; c++) {
          if (consoleSubframes[c] != null) {
            consoleSubframes[c].myNameList.setFont(fnt);
          }
        }// end for
      }

    } else if (action.equals("Chat Timestamp Color")) {
      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Set Timestamp Color",
                                 sharedVariables.chatTimestampColor);
      if (newColor != null)
        sharedVariables.chatTimestampColor=newColor;

    } else if (action.equals("Solid Color Board")) {
      sharedVariables.boardType=0;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Pale Wood")) {
      sharedVariables.boardType=1;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Light Wood")) {
      sharedVariables.boardType=2;
      setBoard(sharedVariables.boardType);

    } else if(action.equals("Dark Wood")) {
      sharedVariables.boardType=3;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Gray Marble")) {
      sharedVariables.boardType=4;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Red Marble")) {
      sharedVariables.boardType=5;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Crampled Paper")) {
      sharedVariables.boardType=6;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Winter")) {
      sharedVariables.boardType=7;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Olive Board")) {
      sharedVariables.boardType=8;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Cherry Board")) {
      sharedVariables.boardType=9;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Purple Board")) {
      sharedVariables.boardType=10;
      setBoard(sharedVariables.boardType);

    } else if (action.equals("Dyche1")) {
      sharedVariables.pieceType=0;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Dyche2")) {
      sharedVariables.pieceType=1;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Dyche3")) {
      sharedVariables.pieceType=2;
      setPieces(sharedVariables.pieceType);

    } else if(action.equals("Bookup")) {
      sharedVariables.pieceType=3;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Xboard")) {
      sharedVariables.pieceType=4;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Alpha")) {
      sharedVariables.pieceType=5;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Spatial")) {
      sharedVariables.pieceType=6;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Harlequin")) {
      sharedVariables.pieceType=7;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Berlin")) {
      sharedVariables.pieceType=8;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Eboard Classic")) {
      sharedVariables.pieceType=9;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Molten Good")) {
      sharedVariables.pieceType=10;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Molten Evil")) {
      sharedVariables.pieceType=11;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Liebeskind")) {
      sharedVariables.pieceType=12;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Eyes")) {
      sharedVariables.pieceType=13;
      setPieces(sharedVariables.pieceType);

    } if (action.equals("Fantasy")) {
      sharedVariables.pieceType=14;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Adventure")) {
      sharedVariables.pieceType=18;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Maya")) {
      sharedVariables.pieceType=19;
      setPieces(sharedVariables.pieceType);

    } if (action.equals("Medieval")) {
      sharedVariables.pieceType=20;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("CCube")) {
      sharedVariables.pieceType=21;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Monge Mix")) {
      sharedVariables.pieceType=22;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("About Monge Pieces")) {

      String warning = "The Monge chess pieces are authored by Maurizio Monge, " +
        "at this time, three of the six sets are currently in Lantern and they " +
        "are under the LGPL (library GPL) license at the time of this writing. " +
        "\n\n Virtually all the piece sets in Lantern come from the Jin Chess, " +
        "and except in cases like the Monge pieces, where I know the license, " +
        "I offer no more rights than Jin does.  LGPL allows you to reuse the " +
        "pieces in your own application if you're a developer. So unzip the " +
        "lantern.jar to get at the pieces.  A warning though, the \\setName\\64\\" +
        "folder is a general folder for when I want pieces I can resize, and the " +
        "monge pieces don't actually come in the 64 size.\n\n  The monge mix is a " +
        "mix of pieces from the Fantasy and Spatial set I've put together.";
      Popup mypopup = new Popup(this, false, warning);
      mypopup.setSize(600,500);
      mypopup.setVisible(true);

    } else if (action.equals("Random Pieces")) {
      sharedVariables.pieceType=23;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Line")) {
      sharedVariables.pieceType=15;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Motif")) {
      sharedVariables.pieceType=16;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("Utrecht")) {
      sharedVariables.pieceType=17;
      setPieces(sharedVariables.pieceType);

    } else if (action.equals("` ` Do Nothing")) {
      sharedVariables.italicsBehavior = 0;
      checkItalicsBehavior(0);

    } else if (action.equals("` ` Italics")) {
      sharedVariables.italicsBehavior = 1;
      checkItalicsBehavior(1);

    } else if (action.equals("` ` Brighter Color")) {
      sharedVariables.italicsBehavior = 2;
      checkItalicsBehavior(2);

    } else if (action.equals("Unvisited/Visited")) {// active tab

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Unvisited/Visited Color",
                                 sharedVariables.tabBackground2);
      if (newColor != null)
        sharedVariables.tabBackground2 = newColor;

    } else if (action.equals("Unvisited")) {// active tab

      JDialog frame = new JDialog();
      Color newColor = JColorChooser.showDialog(frame, "Unvisited Color",
                                                sharedVariables.newInfoTabBackground);
      if (newColor != null)
        sharedVariables.newInfoTabBackground = newColor;

      for (int a=0; a<sharedVariables.openBoardCount; a++)
        if (myboards[a] != null &&
            myboards[a].isVisible())
          for (int aa=0; aa<sharedVariables.openBoardCount; aa++) {
            newColor = myboards[a].myconsolepanel.channelTabs[aa].getBackground();

            if (newColor.getRGB() != sharedVariables.tabBackground.getRGB())
              myboards[a].myconsolepanel.channelTabs[aa].setBackground(sharedVariables.newInfoTabBackground);
          }
      // now update consoles
      for (int a=0; a<sharedVariables.openConsoleCount; a++)
        if (consoleSubframes[a] != null &&
            consoleSubframes[a].isVisible())
          for (int aa=0; aa<sharedVariables.openConsoleCount; aa++) {
            newColor = consoleSubframes[a].channelTabs[aa].getBackground();
            if (newColor.getRGB() != sharedVariables.tabBackground.getRGB())
              consoleSubframes[a].channelTabs[aa].setBackground(sharedVariables.newInfoTabBackground);
          }

    } else if (action.equals("Tab I'm On Background")) {// active tab

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Tab I'm On Color",
                                 sharedVariables.tabImOnBackground);
      if (newColor != null)
        sharedVariables.tabImOnBackground = newColor;

      for (int a=0; a<sharedVariables.openConsoleCount; a++)
        if (consoleSubframes[a] != null &&
            consoleSubframes[a].isVisible())
          consoleSubframes[a].channelTabs[sharedVariables.looking[a]].setBackground(sharedVariables.tabImOnBackground);

    } else if (action.equals("Visited")) {// active tab

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Visited Color",
                                 sharedVariables.tabBackground);
      if (newColor != null)
        sharedVariables.tabBackground = newColor;

      for (int a=0; a<sharedVariables.openBoardCount; a++)
        if(myboards[a] != null &&
           myboards[a].isVisible())
          for (int aa=0; aa<sharedVariables.openBoardCount; aa++) {
            newColor = myboards[a].myconsolepanel.channelTabs[aa].getBackground();
            if (!newColor.equals(sharedVariables.newInfoTabBackground))
              myboards[a].myconsolepanel.channelTabs[aa].setBackground(sharedVariables.tabBackground);
          }
      // now update consoles
      for (int a=0; a<sharedVariables.openConsoleCount; a++)
        if (consoleSubframes[a] != null &&
            consoleSubframes[a].isVisible())
          for (int aa=0; aa<sharedVariables.maxConsoleTabs; aa++) {
            newColor = consoleSubframes[a].channelTabs[aa].getBackground();
            if (!newColor.equals(sharedVariables.newInfoTabBackground))
              consoleSubframes[a].channelTabs[aa].setBackground(sharedVariables.tabBackground);
          }

      // Andrey edits:
      // merge the tab border actions
    } else if (action.equals("Tab Border") ||
               action.equals("Tell Tab Border")) {// active tab

      JDialog frame = new JDialog();
      
      Color tabSetting = (action.equals("Tab Border") ?
                          sharedVariables.tabBorderColor :
                          sharedVariables.tellTabBorderColor);
      
      Color newColor =
        JColorChooser.showDialog(frame, action + " Color", tabSetting);

      if (newColor != null) {
        if (action.equals("Tab Border"))
          sharedVariables.tabBorderColor = newColor;
        else
          sharedVariables.tellTabBorderColor = newColor;
      }
      repaintTabBorders();
      
      /*
    } else if (action.equals("Tell Tab Border")) {// active tab

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Tell Tab Border Color",
                                 sharedVariables.tellTabBorderColor);
      if (newColor != null)
        sharedVariables.tellTabBorderColor = newColor;
      repaintTabBorders();
      */

      // Andrey edits:
      // merge the input settings
    } else if (action.equals("Input Command Color") ||
               action.equals("Input Chat Color")) {//Input Colors

      JDialog frame = new JDialog();

      Color inputSetting = (action.equals("Input Command Color") ?
                            sharedVariables.inputCommandColor :
                            sharedVariables.inputChatColor);
                            
      Color newColor =
        JColorChooser.showDialog(frame, action, inputSetting);

      if (newColor != null) {
        if (action.equals("Input Command Color"))
          sharedVariables.inputCommandColor = newColor;
        else
          sharedVariables.inputChatColor = newColor;
      }

      /*
    } else if (action.equals("Input Chat Color")) {//Input Chat Color

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Input Chat Color",
                                 sharedVariables.activeTabForeground);
      if (newColor != null)
        sharedVariables.inputChatColor = newColor;
      */
      
      // Andrey edits:
      // merge active with non-active
    } else if (action.equals("Active") ||// active tab
               action.equals("Non Active")) {
               
      JDialog frame = new JDialog();

      Color activeSetting = (action.equals("Active") ?
                             sharedVariables.activeTabForeground :
                             sharedVariables.passiveTabForeground);
      
      Color newColor =
        JColorChooser.showDialog(frame, action + " Foreground Color",
                                 activeSetting);
      if (newColor != null) {
        if (action.equals("Active"))
          sharedVariables.activeTabForeground = newColor;
        else // if (action.equals("Non Active")
          sharedVariables.passiveTabForeground = newColor;
      }
        
      for (int a=0; a<sharedVariables.openBoardCount; a++)
        if (myboards[a] != null &&
            myboards[a].isVisible())
          myboards[a].myconsolepanel.setActiveTabForeground(sharedVariables.gamelooking[a]);

      // now update consoles
      for (int a=0; a<sharedVariables.openConsoleCount; a++)
        if (consoleSubframes[a] != null &&
            consoleSubframes[a].isVisible())
          consoleSubframes[a].setActiveTabForeground(sharedVariables.looking[a]);

      /*
    } else if (action.equals("Non Active")) {// active tab

      JDialog frame = new JDialog();
      Color newColor =
        JColorChooser.showDialog(frame, "Non Active Foreground Color",
                                 sharedVariables.passiveTabForeground);
      if (newColor != null)
        sharedVariables.passiveTabForeground=newColor;
      for (int a=0; a<sharedVariables.openBoardCount; a++)
        if (myboards[a] != null &&
            myboards[a].isVisible())
          myboards[a].myconsolepanel.setActiveTabForeground(sharedVariables.gamelooking[a]);

      // now update consoles
      for (int a=0; a<sharedVariables.openConsoleCount; a++)
        if (consoleSubframes[a] != null &&
            consoleSubframes[a].isVisible())
          consoleSubframes[a].setActiveTabForeground(sharedVariables.looking[a]);
      */
      
    } else if (action.equals("Highlight Moves")) {
      /*
      if (sharedVariables.highlightMoves == false) {
        highlight.setSelected(true);
        sharedVariables.highlightMoves = true;
      } else {
        highlight.setSelected(false);
        sharedVariables.highlightMoves = false;
      }
      */
      sharedVariables.highlightMoves = !sharedVariables.highlightMoves;
      highlight.setSelected(sharedVariables.highlightMoves);
      
    } else if (action.equals("Material Count")) {
      /*
      if (sharedVariables.showMaterialCount == false) {
        materialCount.setSelected(true);
        sharedVariables.showMaterialCount = true;
      } else {
        materialCount.setSelected(false);
        sharedVariables.showMaterialCount = false;
      }
      */
      sharedVariables.showMaterialCount = !sharedVariables.showMaterialCount;
      materialCount.setSelected(sharedVariables.showMaterialCount);
      
    } else if (action.equals("Draw Coordinates")) {
      /*
      if (sharedVariables.drawCoordinates == false) {
        drawCoordinates.setSelected(true);
        sharedVariables.drawCoordinates = true;
      } else {
        drawCoordinates.setSelected(false);
        sharedVariables.drawCoordinates = false;
      }
      */
      sharedVariables.drawCoordinates = !sharedVariables.drawCoordinates;
      drawCoordinates.setSelected(sharedVariables.drawCoordinates);
      
      myoutput output = new myoutput();
      output.repaint64=1;
      queue.add(output);

    } else if (action.equals("Show Observers In Games")) {
      /*
      if (sharedVariables.playersInMyGame == 0) {
        playersInMyGame.setSelected(true);
        sharedVariables.playersInMyGame = 2;
      } else {
        playersInMyGame.setSelected(false);
        sharedVariables.playersInMyGame = 0;
      }
      */
      sharedVariables.playersInMyGame =
        (sharedVariables.playersInMyGame == 0 ? 2 : 0);
      playersInMyGame.setSelected(sharedVariables.playersInMyGame != 0);
                                         
    } else if (action.equals("Show Ratings on Board When Playing")) {
      /*
      if (sharedVariables.showRatings == false) {
        showRatings.setSelected(true);
        sharedVariables.showRatings = true;
      } else {
        showRatings.setSelected(false);
        sharedVariables.showRatings = false;
      }
      */
      sharedVariables.showRatings = !sharedVariables.showRatings;
      showRatings.setSelected(sharedVariables.showRatings);
      
    } else if (action.equals("Switch To New Game Tab On Observe")) {
      /*
      if (sharedVariables.newObserveGameSwitch == false) {
        newObserveGameSwitch.setSelected(true);
        sharedVariables.newObserveGameSwitch = true;
      } else {
        newObserveGameSwitch.setSelected(false);
        sharedVariables.newObserveGameSwitch = false;
      }
      */
      sharedVariables.newObserveGameSwitch = !sharedVariables.newObserveGameSwitch;
      newObserveGameSwitch.setSelected(sharedVariables.newObserveGameSwitch);
  
    } else if (action.equals("Low Time Clock Colors (Bullet Only)")) {
      /*
      if (sharedVariables.lowTimeColors == false) {
        lowTimeColors.setSelected(true);
        sharedVariables.lowTimeColors = true;
      } else {
        lowTimeColors.setSelected(false);
        sharedVariables.lowTimeColors = false;
      }
      */
      sharedVariables.lowTimeColors = !sharedVariables.lowTimeColors;
      lowTimeColors.setSelected(sharedVariables.lowTimeColors);
  
    } else if (action.equals("AutoChat")) {
      /*
      if (sharedVariables.autoChat == false) {
        autoChat.setSelected(true);
        sharedVariables.autoChat = true;
      } else {
        autoChat.setSelected(false);
        sharedVariables.autoChat = false;
      }
      */
      sharedVariables.autoChat = !sharedVariables.autoChat;
      autoChat.setSelected(sharedVariables.autoChat);
      
    } else if (action.equals("Block Opponents Says When Not Playing")) {
      /*
      if (sharedVariables.blockSays == false) {
        blockSays.setSelected(true);
        sharedVariables.blockSays = true;
      } else {
        blockSays.setSelected(false);
        sharedVariables.blockSays = false;
      }
      */
      sharedVariables.blockSays = !sharedVariables.blockSays;
      blockSays.setSelected(sharedVariables.blockSays);

    } else if (action.equals("Show Examine Mode Palette")) {
      /*
      if (sharedVariables.showPallette == false) {
        showPallette.setSelected(true);
        sharedVariables.showPallette = true;
      } else {
        showPallette.setSelected(false);
        sharedVariables.showPallette = false;
      }
      */
      sharedVariables.showPallette = !sharedVariables.showPallette;
      showPallette.setSelected(sharedVariables.showPallette);
	
      for (int bn=0; bn < sharedVariables.maxGameTabs; bn++) {
	if (myboards[bn]!=null)
          myboards[bn].mypanel.repaint();
        else break;
      }

    } else if (action.equals("Show Flags")) {
      /*
      if (sharedVariables.showFlags == false) {
        showFlags.setSelected(true);
        sharedVariables.showFlags = true;
      } else {
        showFlags.setSelected(false);
        sharedVariables.showFlags = false;
      }
      */
      sharedVariables.showFlags = !sharedVariables.showFlags;
      showFlags.setSelected(sharedVariables.showFlags);

      String swarning =
        "This setting will update on board as soon as the next game starts.";
      Popup pframe = new Popup((JFrame) this, true, swarning);
      pframe.setVisible(true);

    } else if (action.equals("Use Light Square as Board Background")) {
      /*
      if (sharedVariables.useLightBackground == false) {
        useLightBackground.setSelected(true);
        sharedVariables.useLightBackground = true;
      } else {
        useLightBackground.setSelected(false);
        sharedVariables.useLightBackground = false;
      }
      */
      sharedVariables.useLightBackground = !sharedVariables.useLightBackground;
      useLightBackground.setSelected(sharedVariables.useLightBackground);
      
      // Andrey edits:
      // merge the following 3 actions
    } else if (action.equals("Original") ||
               action.equals("Modern") ||
               action.equals("Mixed")) {
      /*
      BoardDesign1.setSelected(true);
      BoardDesign2.setSelected(false);
      BoardDesign3.setSelected(false);
      sharedVariables.andreysLayout = 0;
      */
      sharedVariables.andreysLayout = (action.equals("Original") ? 0 :
                                       (action.equals("Modern") ? 1 :
                                        2));
      BoardDesign1.setSelected(sharedVariables.andreysLayout == 0);
      BoardDesign2.setSelected(sharedVariables.andreysLayout == 1);
      BoardDesign3.setSelected(sharedVariables.andreysLayout == 2);
                                       
      redrawBoard(sharedVariables.boardConsoleType);

      /*
    } else if (action.equals("Modern")) {

      BoardDesign1.setSelected(false);
      BoardDesign2.setSelected(true);
      BoardDesign3.setSelected(false);
      sharedVariables.andreysLayout = 1;
      redrawBoard(sharedVariables.boardConsoleType);

    } else if (action.equals("Mixed")) {

      BoardDesign1.setSelected(false);
      BoardDesign2.setSelected(false);
      BoardDesign3.setSelected(true);
      sharedVariables.andreysLayout = 2;
      redrawBoard(sharedVariables.boardConsoleType);
      */

    } else if (action.equals("Flip")) {
      for (int a=0; a<sharedVariables.maxGameTabs; a++) {
        if (myboards[a] != null &&
            myboards[a].isVisible() &&
            myboards[a].isSelected()) { 
          int flipPlus = (sharedVariables.mygame[myboards[a].gameData.LookingAt].iflipped + 1) % 2;
          String flip= "" + flipPlus;
          String icsGameNumber =  "" +
            sharedVariables.mygame[myboards[a].gameData.LookingAt].myGameNumber;
          myboards[myboards[a].gameData.LookingAt].flipSent(icsGameNumber, flip);
          myboards[a].mypanel.repaint();
          myboards[a].mycontrolspanel.repaint();
          break;
        }// end selected
      }

      /*
    } else if (action.equals("Withdraw Challenges")) {
      String actionmess="multi match\n";
      if(sharedVariables.myServer.equals("ICC"))
        actionmess="`c0`" + actionmess;

      myoutput data = new myoutput();
      data.data=actionmess;
      queue.add(data);
      */

    } else if (action.equals("Tabs Only")) {
      /*
      if (sharedVariables.tabsOnly == false) {
        tabbing.setSelected(true);
        sharedVariables.tabsOnly = true;
      } else {
        tabbing.setSelected(false);
        sharedVariables.tabsOnly = false;
      }
      */
      sharedVariables.tabsOnly = !sharedVariables.tabsOnly;
      tabbing.setSelected(sharedVariables.tabsOnly);

    } else if (action.equals("Auto Buffer Chat Length")) {
      /*
      if (sharedVariables.autoBufferChat == false) {
        sharedVariables.autoBufferChat=true;
        autobufferchat.setSelected(true);
      } else {
        sharedVariables.autoBufferChat=false;
        autobufferchat.setSelected(false);
      }
      */
      sharedVariables.autoBufferChat = !sharedVariables.autoBufferChat;
      autobufferchat.setSelected(sharedVariables.autoBufferChat);

    } else if (action.equals("No Idle")) {
      /*
      if (sharedVariables.noidle == false) {
        sharedVariables.noidle=true;
        autonoidle.setSelected(true);
      } else {
        sharedVariables.noidle=false;
        autonoidle.setSelected(false);
      }
      */
      sharedVariables.noidle = !sharedVariables.noidle;
      autonoidle.setSelected(sharedVariables.noidle);

    } else if (action.equals("Switch Tab On Tell")) {
      /*
      if (sharedVariables.switchOnTell == false) {
        sharedVariables.switchOnTell=true;
        tellswitch.setSelected(true);
      } else {
        sharedVariables.switchOnTell=false;
        tellswitch.setSelected(false);
      }
      */
      sharedVariables.switchOnTell = !sharedVariables.switchOnTell;
      tellswitch.setSelected(sharedVariables.switchOnTell);

    } else if (action.equals("Timestamp To Left Of Name")) {
      /*
      if (sharedVariables.leftTimestamp == false) {
        sharedVariables.leftTimestamp = true;
        leftNameTimestamp.setSelected(true);
      } else {
        sharedVariables.leftTimestamp = false;
        leftNameTimestamp.setSelected(false);
      }
      */
      sharedVariables.leftTimestamp = !sharedVariables.leftTimestamp;
      leftNameTimestamp.setSelected(sharedVariables.leftTimestamp);


    } else if (action.equals("Timestamp Connecting")) {
      /*
      if (sharedVariables.reconnectTimestamp == false) {
        sharedVariables.reconnectTimestamp = true;
        reconnectTimestamp.setSelected(true);
      } else {
        sharedVariables.reconnectTimestamp = false;
        reconnectTimestamp.setSelected(false);
      }
      */
      sharedVariables.reconnectTimestamp = !sharedVariables.reconnectTimestamp;
      reconnectTimestamp.setSelected(sharedVariables.reconnectTimestamp);

    } else if (action.equals("Timestamp Shouts")) {
      /*
      if (sharedVariables.shoutTimestamp == false) {
        sharedVariables.shoutTimestamp = true;
        shoutTimestamp.setSelected(true);
      } else {
        sharedVariables.shoutTimestamp = false;
        shoutTimestamp.setSelected(false);
      }
      */
      sharedVariables.shoutTimestamp = !sharedVariables.shoutTimestamp;
      shoutTimestamp.setSelected(sharedVariables.shoutTimestamp);

    } else if (action.equals("Timestamp Channel Qtells")) {
      /*
      if (sharedVariables.qtellTimestamp == false) {
        sharedVariables.qtellTimestamp = true;
        qtellTimestamp.setSelected(true);
      } else {
        sharedVariables.qtellTimestamp = false;
        qtellTimestamp.setSelected(false);
      }
      */
      sharedVariables.qtellTimestamp = !sharedVariables.qtellTimestamp;
      qtellTimestamp.setSelected(sharedVariables.qtellTimestamp);

    } else if (action.equals("Timestamp Tells")) {
      /*
      if (sharedVariables.tellTimestamp == false) {
        sharedVariables.tellTimestamp = true;
        tellTimestamp.setSelected(true);
      } else {
        sharedVariables.tellTimestamp = false;
        tellTimestamp.setSelected(false);
      }
      */
      sharedVariables.tellTimestamp = !sharedVariables.tellTimestamp;
      tellTimestamp.setSelected(sharedVariables.tellTimestamp);

    } else if (action.equals("Timestamp Channels and Kibs")) {
      /*
      if (sharedVariables.channelTimestamp == false) {
        sharedVariables.channelTimestamp = true;
        channelTimestamp.setSelected(true);
      } else {
        sharedVariables.channelTimestamp = false;
        channelTimestamp.setSelected(false);
      }
      */
      sharedVariables.channelTimestamp = !sharedVariables.channelTimestamp;
      channelTimestamp.setSelected(sharedVariables.channelTimestamp);

    } else if (action.equals("Use Basketball Logo ICC Flag")) {
      /*
      if (sharedVariables.basketballFlag == false) {
        sharedVariables.basketballFlag = true;
        basketballFlag.setSelected(true);
      } else {
        sharedVariables.basketballFlag = false;
        basketballFlag.setSelected(false);
      }
      */
      sharedVariables.basketballFlag = !sharedVariables.basketballFlag;
      basketballFlag.setSelected(sharedVariables.basketballFlag);

    } else if (action.equals("Print Channel Notify for Main Also")) {
      /*
      if (sharedVariables.notifyMainAlso == false) {
        sharedVariables.notifyMainAlso = true;
        notifyMainAlso.setSelected(true);
      } else {
        sharedVariables.notifyMainAlso = false;
        notifyMainAlso.setSelected(false);
      }
      */
      sharedVariables.notifyMainAlso = !sharedVariables.notifyMainAlso;
      notifyMainAlso.setSelected(sharedVariables.notifyMainAlso);

    } else if (action.equals("Auto Name Popup")) {
      /*
      if (sharedVariables.autopopup == false) {
        sharedVariables.autopopup = true;
        autopopup.setSelected(true);
      } else {
        sharedVariables.autopopup = false;
        autopopup.setSelected(false);
      }
      */
      sharedVariables.autopopup = !sharedVariables.autopopup;
      autopopup.setSelected(sharedVariables.autopopup);

    } else if (action.equals("Auto History Popup")) {
      /*
      if (sharedVariables.autoHistoryPopup == false) {
        sharedVariables.autoHistoryPopup = true;
        autoHistoryPopup.setSelected(true);
      } else {
        sharedVariables.autoHistoryPopup = false;
        autoHistoryPopup.setSelected(false);
      }
      */
      sharedVariables.autoHistoryPopup = !sharedVariables.autoHistoryPopup;
      autoHistoryPopup.setSelected(sharedVariables.autoHistoryPopup);

// Andrey edits:
// remove "Auto Observe"
    } else if (action.equals("Tomato")) {
      /*
      if (sharedVariables.autoTomato == false)
	sharedVariables.autoTomato = true;
      else
	sharedVariables.autoTomato = false;
      */
      sharedVariables.autoTomato = !sharedVariables.autoTomato;

    } else if (action.equals("Cooly")) {
      /*
      if (sharedVariables.autoCooly == false)
	sharedVariables.autoCooly = true;
      else
	sharedVariables.autoCooly = false;
      */
      sharedVariables.autoCooly = !sharedVariables.autoCooly;

    } else if (action.equals("WildOne")) {
      /*
      if (sharedVariables.autoWildOne == false)
	sharedVariables.autoWildOne = true;
      else
	sharedVariables.autoWildOne = false;
      //reconnect2.setVisible(true);
      */
      sharedVariables.autoWildOne = !sharedVariables.autoWildOne;

    } else if (action.equals("Flash")) {
      /*
      if (sharedVariables.autoFlash == false)
	sharedVariables.autoFlash = true;
      else
	sharedVariables.autoFlash = false;
      */
      sharedVariables.autoFlash = !sharedVariables.autoFlash;

    } if (action.equals("Olive")) {
      /*
      if (sharedVariables.autoOlive == false)
	sharedVariables.autoOlive = true;
      else
	sharedVariables.autoOlive = false;
      */
      sharedVariables.autoOlive = !sharedVariables.autoOlive;

    } else if (action.equals("Ketchup")) {
      /*
      if (sharedVariables.autoKetchup == false)
	sharedVariables.autoKetchup = true;
      else
	sharedVariables.autoKetchup = false;
      //reconnect2.setVisible(true);
      */
      sharedVariables.autoKetchup = !sharedVariables.autoKetchup;

    } else if (action.equals("LittlePer")) {
      /*
      if (sharedVariables.autoLittlePer == false)
	sharedVariables.autoLittlePer = true;
      else
	sharedVariables.autoLittlePer = false;
      */
      sharedVariables.autoLittlePer = !sharedVariables.autoLittlePer;

    } else if (action.equals("Slomato")) {
      /*
      if (sharedVariables.autoSlomato == false)
	sharedVariables.autoSlomato = true;
      else
	sharedVariables.autoSlomato = false;
      */
      sharedVariables.autoSlomato = !sharedVariables.autoSlomato;

    } else if (action.equals("Random Piece Set Observe Only")) {
      /*
      if (sharedVariables.randomArmy == false) {
        sharedVariables.randomArmy = true;
        randomArmy.setSelected(true);
      } else {
	sharedVariables.randomArmy = false;
        randomArmy.setSelected(false);
      }
      */
      sharedVariables.randomArmy = !sharedVariables.randomArmy;
      randomArmy.setSelected(sharedVariables.randomArmy);

      // Andrey edits:
      // merge the next 2 actions
    } else if (action.equals("Configure Random Pieces For White") ||
               action.equals("Configure Random Pieces For Black")) {
      boolean whiteSetting = action.equals("Configure Random Pieces For White");
      boolean[] excludedSetting = (whiteSetting ?
                                   sharedVariables.excludedPiecesWhite :
                                   sharedVariables.excludedPiecesBlack);
      
      customizeExcludedPiecesDialog goConfigure =
        new customizeExcludedPiecesDialog(this, false, sharedVariables, graphics,
                                          excludedSetting, whiteSetting);
                                          //sharedVariables.excludedPiecesWhite, true);
      goConfigure.setVisible(true);

      /*
    } else if (action.equals("Configure Random Pieces For Black")) {
      customizeExcludedPiecesDialog goConfigure =
        new customizeExcludedPiecesDialog(this, false, sharedVariables, graphics,
                                          sharedVariables.excludedPiecesBlack, false);
      goConfigure.setVisible(true);
      */
      
    } else if (action.equals("Random Square Tiles Observe Only")) {
      /*
      if (sharedVariables.randomBoardTiles == false) {
        sharedVariables.randomBoardTiles = true;
        randomTiles.setSelected(true);
      } else {
	sharedVariables.randomBoardTiles = false;
        randomTiles.setSelected(false);
      }
      */
      sharedVariables.randomBoardTiles = !sharedVariables.randomBoardTiles;
      randomTiles.setSelected(sharedVariables.randomBoardTiles);

    } else if (action.equals("Sounds for Notifications")) {
      /*
      if (sharedVariables.specificSounds[4] == false) {
        notifysound.setSelected(true);
        sharedVariables.specificSounds[4] = true;
      } else {
        notifysound.setSelected(false);
        sharedVariables.specificSounds[4] = false;
      }
      */
      sharedVariables.specificSounds[4] = !sharedVariables.specificSounds[4];
      notifysound.setSelected(sharedVariables.specificSounds[4]);

    } else if (action.equals("Sounds for Observed Games")) {
      /*
      if (sharedVariables.makeObserveSounds == false) {
        makeObserveSounds.setSelected(true);
        sharedVariables.makeObserveSounds = true;
      } else {
        makeObserveSounds.setSelected(false);
        sharedVariables.makeObserveSounds = false;
      }
      */
      sharedVariables.makeObserveSounds = !sharedVariables.makeObserveSounds;
      makeObserveSounds.setSelected(sharedVariables.makeObserveSounds);

    } else if (action.equals("Sounds")) {
      /*
      if (sharedVariables.makeSounds == false)
	sharedVariables.makeSounds = true;
      else
	sharedVariables.makeSounds = false;
      */
      sharedVariables.makeSounds = !sharedVariables.makeSounds;

    } else if (action.equals("Start Powerout")) {

      //JFrame aframe = new JFrame();
      //aframe.setVisible(true);
      poweroutframe frame = new poweroutframe(sharedVariables.poweroutSounds);
      //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //frame.pack();
      sharedVariables.desktop.add(frame);
      frame.setVisible(true);
      /*
      JComponent newContentPane = new poweroutpanel();
      newContentPane.setOpaque(true); //content panes must be opaque
      frame.setContentPane(newContentPane);
      */
      try {
        frame.setSelected(true);
        //aa.Input.setFocusable(true);
      } catch (Exception e) {}
      frame.setSize(700,550);
      //frame.setExtendedState(Frame.MAXIMIZED_BOTH);

    } else if (action.equals("Start MineSweeper")) {

      minesweeper10 frame = new minesweeper10(sharedVariables, this);

      sharedVariables.desktop.add(frame);

      try {
        frame.setSelected(true);
        //aa.Input.setFocusable(true);
      } catch (Exception e) {}

    } else if (action.equals("Start Connect Four")) {

      connectFour frame = new connectFour();

      sharedVariables.desktop.add(frame);

      try {
        frame.setSelected(true);
        //aa.Input.setFocusable(true);
      } catch (Exception e) {}

    } else if (action.equals("Start Mastermind")) {

      mastermind11 frame = new mastermind11();

      sharedVariables.desktop.add(frame);

      try {
        frame.setSelected(true);
        //aa.Input.setFocusable(true);
      } catch (Exception e) {}

    } else if (action.equals("Console Colors")) {

      //JDialog frame = new JDialog();
      //sharedVariables.shoutcolor =
      //  JColorChooser.showDialog(frame, "Choose Shout Color",
      //                           sharedVariables.shoutcolor);
      customizeConsolelColorsDialog frame =
        new customizeConsolelColorsDialog((JFrame) this, false, sharedVariables,
                                          consoles, gameconsoles);

    } else if (action.equals("Notify and Events Background Color")) {
      // BackColor
      JDialog frame = new JDialog();
      Color newColor = JColorChooser.showDialog(frame, "Choose " + action,
                                                sharedVariables.listColor);
      if (newColor != null)
        sharedVariables.listColor = newColor;
      if (sharedVariables.activitiesPanel != null)
        sharedVariables.activitiesPanel.setColors();
      if (myNotifyFrame != null)
        myNotifyFrame.notifylistScrollerPanel.theNotifyList.setBackground(sharedVariables.listColor);

}

if(action.equals("Main Background"))
{
// BackColor
 JDialog frame = new JDialog();
 Color newColor = JColorChooser.showDialog(frame, "Choose Main Background Color", sharedVariables.MainBackColor);
 if(newColor != null)
 sharedVariables.MainBackColor=newColor;
 sharedVariables.desktop.setBackground(sharedVariables.MainBackColor);


}


 if(action.equals("Change Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.myFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {
				sharedVariables.myFont=fnt;
	        for(int i=0; i < sharedVariables.openConsoleCount; i++)
			 {
				 if(consoles[i]!= null)
				 if(sharedVariables.tabStuff[i].tabFont == null)
	              {
	              consoles[i].setFont(sharedVariables.myFont);
	              }
 			}

 // now game boards
 	        for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 {
 				 if(gameconsoles[i]!= null)
 	 			{
 	        		gameconsoles[i].setFont(sharedVariables.myFont);
 	 			}
 			}
	}//end if font not null

}

if(action.equals("Change Tab Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.myTabFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {

				sharedVariables.myTabFont=fnt;
				repaintTabs();
			}// end if font not null
}
if(action.equals("Change Input Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.inputFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {

				sharedVariables.inputFont=fnt;
				setInputFont();
			}// end if font not null
}




if(action.equals("Game Board Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.myGameFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {
				sharedVariables.myGameFont=fnt;

 // now game boards
 	        for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 {
 				 if(myboards[i]!= null)
 	 			{

 	        		myboards[i].mycontrolspanel.setFont();
 	 			}
 			}
		}// not null font

}

if(action.equals("Game Clock Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.myGameClockFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {
				sharedVariables.myGameClockFont=fnt;

 // now game boards
 	        for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 {
 				 if(myboards[i]!= null)
 	 			{

 	        		myboards[i].mycontrolspanel.setFont();
 	 			}
 			}
		}// not null font

}






if(action.equals("Channel Colors"))
{
	customizeChannelColorDialog frame = new customizeChannelColorDialog((JFrame) this, false, sharedVariables, consoles);
	//frame.setSize(300,250);
	frame.setVisible(true);
}
    // Andrey says:
    // moved the for loop to the end of the action performed method
    for (int openBoardMenu=0; openBoardMenu < sharedVariables.maxGameTabs; openBoardMenu++) {
      if (myboards[openBoardMenu] == null)
        break;
      if (sharedVariables.openBoards[openBoardMenu] != null &&
          action.equals(sharedVariables.openBoards[openBoardMenu].getText())) {
        try {
          myboards[openBoardMenu].setSelected(true);
          break;
        } catch(Exception duiii) {}
      }// end if
      // end for
    }


}// end action performed method
void openActivities()
{
try {
//	if(myfirstlist == null)
//	mycreator.createListFrame(eventsList, seeksList, computerSeeksList, notifyList, this);
	if(!myfirstlist.isVisible() && !mysecondlist.isVisible())
	mycreator.createListFrame(eventsList, seeksList, computerSeeksList, notifyList, this);
        else if(mysecondlist.isVisible() && mysecondlist.isSelected() == false)
        mysecondlist.setSelected(true);
        else if(mysecondlist.isVisible())
        mysecondlist.setVisible(false);
        else if(myfirstlist.isVisible())
        { myfirstlist.setBoardSize();
          myfirstlist.setVisible(false);
        }
        sharedVariables.activitiesPanel.setColors();
//	myfirstlist.setSelected(true);
}catch(Exception dui){}
}


void makeEngineWarning()
{
String swarning = "You must be in examine or observe mode o load an engine and you need to click on the board and game tab that is in this mode as well first.";
Popup pframe = new Popup((JFrame) this, true, swarning);
pframe.setVisible(true);

}
void makeEngineWarning2()
{
String swarning = "You must not be playing to load an engine.";
Popup pframe = new Popup((JFrame) this, true, swarning);
pframe.setVisible(true);

}
void setBoard(int type)
{


if(type == 0)
solidboard.setSelected(true);
else
solidboard.setSelected(false);

if(type == 1)
woodenboard1.setSelected(true);
else
woodenboard1.setSelected(false);

if(type == 2)
woodenboard2.setSelected(true);
else
woodenboard2.setSelected(false);

if(type == 3)
woodenboard3.setSelected(true);
else
woodenboard3.setSelected(false);

if(type == 4)
grayishboard.setSelected(true);
else
grayishboard.setSelected(false);


if(type == 5)
board5.setSelected(true);
else
board5.setSelected(false);

if(type == 6)
board6.setSelected(true);
else
board6.setSelected(false);

if(type == 7)
board7.setSelected(true);
else
board7.setSelected(false);

if(type == 8)
oliveboard.setSelected(true);
else
oliveboard.setSelected(false);

if(type == 9)
cherryboard.setSelected(true);
else
cherryboard.setSelected(false);

if(type == 10)
purpleboard.setSelected(true);
else
purpleboard.setSelected(false);


 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();

}



void openSeekGraph()
{
try {
	if(seekGraph.isVisible() == false)
{
seekGraph.setSize(sharedVariables.mySeekSizes.con0x, sharedVariables.mySeekSizes.con0y);
seekGraph.setLocation(sharedVariables.mySeekSizes.point0.x, sharedVariables.mySeekSizes.point0.y);
seekGraph.setVisible(true);
seekGraph.setSelected(true);
//seekGraph.setSize(600,600);
}
else if(seekGraph.isSelected() == false)
seekGraph.setSelected(true);
else
{seekGraph.setBoardSize();
seekGraph.setVisible(false);
}
}catch(Exception dummyseek){}

}


void resetConsoleLayout()
{
		if(sharedVariables.consoleLayout == 1)
		{
			tabLayout1.setSelected(true);
			tabLayout2.setSelected(false);
			tabLayout3.setSelected(false);

		}
		else if(sharedVariables.consoleLayout == 2)
		{
			tabLayout1.setSelected(false);
			tabLayout2.setSelected(true);
			tabLayout3.setSelected(false);

		}
		else
		{
			tabLayout1.setSelected(false);
			tabLayout2.setSelected(false);
			tabLayout3.setSelected(true);

		}

		for(int a=0; a< sharedVariables.maxConsoleTabs; a++)
			if(consoleSubframes[a]!=null)
			consoleSubframes[a].overall.recreate(sharedVariables.consolesTabLayout[a]);
}
void startTheEngine()
{        boolean go = false;

         for(int aa=0; aa< sharedVariables.openBoardCount; aa++)
         if(sharedVariables.mygame[aa].state == sharedVariables.STATE_PLAYING)
         {
           sharedVariables.engineOn = false;
           makeEngineWarning2();
           return; 

         }

        for(int a=0; a< sharedVariables.openBoardCount; a++)
	if(myboards[a].isSelected())
	{
	if(sharedVariables.mygame[myboards[a].gameData.LookingAt] != null)
        if(sharedVariables.mygame[myboards[a].gameData.LookingAt].state == sharedVariables.STATE_EXAMINING ||  sharedVariables.mygame[myboards[a].gameData.LookingAt].state == sharedVariables.STATE_OBSERVING)
        {	try {

                              go=true;
                              myoutput tosend = new myoutput();


                              try {
                              tosend=sharedVariables.engineQueue.poll();// we look for data from other areas of the program
                              while(tosend!=null)
                              tosend=sharedVariables.engineQueue.poll();
                              }
                              catch(Exception duiii){}

                              sharedVariables.engineBoard = myboards[a].gameData.LookingAt;
			 	myboards[myboards[a].gameData.LookingAt].startEngine();


		    }
		catch(Exception e){}
		break;
        }
        }
if(go== false)
{
  sharedVariables.engineOn = false;
  makeEngineWarning();
}// if go false
}

void repaintTabBorders()
{
 for(int a=0; a<sharedVariables.openBoardCount; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 myboards[a].myconsolepanel.channelTabs[a].repaint();


// now update consoles
 for(int a=0; a<sharedVariables.openConsoleCount; a++)
 if(consoleSubframes[a]!=null)
 if(consoleSubframes[a].isVisible() == true)
 for(int aa=0; aa<sharedVariables.maxConsoleTabs; aa++)
 {
 consoleSubframes[a].channelTabs[aa].repaint();
}
  


}
void checkItalicsBehavior(int n)
{
 if(n == 0)
italicsBehavior[0].setSelected(true);
else
italicsBehavior[0].setSelected(false);

 if(n == 1)
italicsBehavior[1].setSelected(true);
else
italicsBehavior[1].setSelected(false);

 if(n == 2)
italicsBehavior[2].setSelected(true);
else
italicsBehavior[2].setSelected(false);
}

void redrawBoard(int type)
{
if(type == 0)
boardconsole0.setSelected(true);
else
boardconsole0.setSelected(false);

if(type == 1)
boardconsole1.setSelected(true);
else
boardconsole1.setSelected(false);

if(type == 2)
boardconsole2.setSelected(true);
else
boardconsole2.setSelected(false);

if(type == 3)
boardconsole3.setSelected(true);
else
boardconsole3.setSelected(false);


for(int a = 0; a<sharedVariables.maxGameTabs; a++)
if(myboards[a] != null)
if(myboards[a].isVisible())
myboards[a].recreate();
}
void setPieces(int type)
{


if(type == 0)
pieces1.setSelected(true);
else
pieces1.setSelected(false);

if(type == 1)
pieces2.setSelected(true);
else
pieces2.setSelected(false);

if(type == 2)
pieces3.setSelected(true);
else
pieces3.setSelected(false);

if(type == 3)
pieces4.setSelected(true);
else
pieces4.setSelected(false);

if(type == 4)
pieces5.setSelected(true);
else
pieces5.setSelected(false);

if(type == 5)
pieces6.setSelected(true);
else
pieces6.setSelected(false);


if(type == 6)
pieces7.setSelected(true);
else
pieces7.setSelected(false);

if(type == 7)
pieces8.setSelected(true);
else
pieces8.setSelected(false);

if(type == 8)
pieces9.setSelected(true);
else
pieces9.setSelected(false);

if(type == 9)
pieces10.setSelected(true);
else
pieces10.setSelected(false);


if(type == 10)
pieces11.setSelected(true);
else
pieces11.setSelected(false);

if(type == 11)
pieces12.setSelected(true);
else
pieces12.setSelected(false);

if(type == 12)
pieces13.setSelected(true);
else
pieces13.setSelected(false);
if(type == 13)
pieces14.setSelected(true);
else
pieces14.setSelected(false);

if(type == 14)
pieces15.setSelected(true);
else
pieces15.setSelected(false);

if(type == 15)
pieces16.setSelected(true);
else
pieces16.setSelected(false);

if(type == 16)
pieces17.setSelected(true);
else
pieces17.setSelected(false);

if(type == 17)
pieces18.setSelected(true);
else
pieces18.setSelected(false);

if(type == 18)
pieces19.setSelected(true);
else
pieces19.setSelected(false);


if(type == 19)
pieces20.setSelected(true);
else
pieces20.setSelected(false);
if(type == 20)
pieces21.setSelected(true);
else
pieces21.setSelected(false);
if(type == 21)
pieces22.setSelected(true);
else
pieces22.setSelected(false);
if(type == 22)
pieces23.setSelected(true);
else
pieces23.setSelected(false);



if(type == 23)
{
	pieces24.setSelected(true);
	generateRandomPieces(type);
}
else
pieces24.setSelected(false);


 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();

}

void generateRandomPieces(int type)
{
	Random randomGenerator = new Random();

	for(int a = 0; a<12; a++)
	{
	      int randomInt = randomGenerator.nextInt(type - 1);
		graphics.pieces[type][a] = graphics.pieces[randomInt][a];
	}
}

void setMySize()
{
	/* check if we need to resize differently and not maximize */
	scriptLoader myloader = new scriptLoader();
	boolean valid = false;
	int width=800;
	int height=800;
try {

	Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        sharedVariables.screenW = dim.width;
        sharedVariables.screenH = dim.height;
}
catch(Exception badtool){ return;}


try {
ArrayList<String> myArray = new ArrayList();

myloader.loadScript(myArray, "lantern_sizing.ini");
try {
	if(myArray.size() > 1)
	{
		width=Integer.parseInt(myArray.get(0));
		height=Integer.parseInt(myArray.get(1));
        if(width > 200 && height > 200 && width < sharedVariables.screenW - 100 && height < sharedVariables.screenH - 50)
        {
			valid=true;
			sharedVariables.screenW=width;
			sharedVariables.screenH=height;
		}
        else
        {
			width=800;
			height=800;

		}

	}// end size of array
}// end try
catch(Exception wrongsize) {

	width=800;
	height=800;

}
setSize(width,height);
if(valid == false)
{
 if(sharedVariables.operatingSystem.equals("unix"))
 {
   setVisible(true); // unix needs the window to be visible before maximized
   setLocation(0,0);// put it in top corner to hopefully fix a bug on some linux that mouse and menu got out of snych
 } 
  setExtendedState(JFrame.MAXIMIZED_BOTH);
}

}// end outer try
catch(Exception d)
{
setSize(width,height);
if(valid == false)
setExtendedState(JFrame.MAXIMIZED_BOTH);

}// end catch


}// end method set size

public void windowClosed(WindowEvent e)
{

//if(sharedVariables.engineOn == true)
sendToEngine("exit\n");
sendToEngine("quit\n");

}

public void windowDeactivated(WindowEvent e)
{

}
public void windowDeiconified(WindowEvent e)
{

}
public void windowClosing(WindowEvent e)
{
//if(sharedVariables.engineOn == true)
sendToEngine("exit\n");
sendToEngine("quit\n");
if(sharedVariables.standAlone == false)
	System.exit(0);
else
{
	JSettingsDialog frame = new JSettingsDialog((JFrame) this, false, sharedVariables);
}
}



/********************* Console Events *******************************************************************************/

void compactConsole()
{
	sharedVariables.boardConsoleType=1;
	redrawBoard(sharedVariables.boardConsoleType);
}
void normalConsole()
{
	sharedVariables.boardConsoleType=2;
	redrawBoard(sharedVariables.boardConsoleType);
}
void largerConsole()
{
	sharedVariables.boardConsoleType=3;
	redrawBoard(sharedVariables.boardConsoleType);
}
void sideConsole()
{
	if(sharedVariables.sideways == true)
	{
		sharedVariables.sideways=false;
		sidewaysconsole.setSelected(false);
	}
	else
	{
		sharedVariables.sideways=true;
		sidewaysconsole.setSelected(true);
	}
	redrawBoard(sharedVariables.boardConsoleType);
}


/********************************************************** end console events ******************************************/

































class JSettingsDialog extends JFrame
{

channels sharedVariables;

JSettingsDialog(JFrame frame, boolean mybool, channels sharedVariables1)
{
//	super(frame, false);

	sharedVariables=sharedVariables1;
	JPanel pane =  new JPanel();
	JLabel tosave = new JLabel("Save Settings?");
	JButton yes =  new JButton("Yes");
	JButton no = new JButton("No");
	yes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
						try {
							mysettings.saveNow(myboards, consoleSubframes, sharedVariables);
							mineScores.saveNow(sharedVariables);
				}
				catch(Exception d){}
					System.exit(0);
						dispose();

		}// end try
			catch(Exception e)
			{}
		}
});
	no.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				System.exit(0);
				dispose();
		}// end try
			catch(Exception e)
			{}
		}
});
	pane.setLayout(new GridLayout(3,1)); // rows collums
	pane.add(tosave);
	pane.add(yes);
	pane.add(no);
	add(pane);
	setLocation(400,300);
	setSize(200, 200);

	for(int b=0; b<sharedVariables.maxGameTabs; b++)
	if(myboards[b]!=null)
	{
          if(myboards[b].isVisible())
	  {
            if(myboards[b].isMaximum()==false)
	    {
              myboards[b].setBoardSize();
              }
          }
       }
       else
       {
        sharedVariables.openBoardCount=b;
        break;
       }


		for(int b=0; b<sharedVariables.maxConsoleTabs; b++)
		if(consoleSubframes[b]!=null)
		if(consoleSubframes[b].isVisible())
		if(consoleSubframes[b].isMaximum()==false)
	consoleSubframes[b].setBoardSize();

			sharedVariables.activitiesOpen = false;
		if(myfirstlist!=null)
		if(myfirstlist.isVisible())
		{
			sharedVariables.activitiesOpen = true;

				myfirstlist.setBoardSize();

		}

		if(mysecondlist!=null)
		if(mysecondlist.isVisible())
		{
			sharedVariables.activitiesOpen = true;

				mysecondlist.setBoardSize();

		}



			sharedVariables.seeksOpen = false;
		if(seekGraph!=null)
		if(seekGraph.isVisible())
		{
			sharedVariables.seeksOpen = true;
                        seekGraph.setBoardSize();


		}


	setVisible(true);
        setAlwaysOnTop(true);
}// end constructor


}//end class


public void windowOpened(WindowEvent e)
{

}

public void windowIconified(WindowEvent e)
{

}


public void windowActivated(WindowEvent e)
{
 myoutput data = new myoutput();
 data.data="\n";
 data.reconnectTry=1;
 queue.add(data);



}

void setButtonTitle(int a)
{
	 String buttonTitle="" + a;
   if(!sharedVariables.userButtonCommands[a].equals("") && sharedVariables.showButtonTitle==true)
    {
		buttonTitle="" + a + " - ";
		if(sharedVariables.userButtonCommands[a].length() > 11)
			buttonTitle+=sharedVariables.userButtonCommands[a].substring(0,11);
		else
			buttonTitle+=sharedVariables.userButtonCommands[a];


	}
sharedVariables.mybuttons[a].setText(buttonTitle);

}
void makeToolBar()
{
  toolBar = new JToolBar("Still draggable");
 sharedVariables.mybuttons = new JButton[10];
 toolBar.setLayout(new GridLayout(1,18));

JButton pure1 = new JButton("1-min");
JButton pure3 = new JButton("3-min");
JButton pure5 = new JButton("5-min");
JButton pure15 = new JButton("15-min");
JButton pure45 = new JButton("45 45");
JButton pure960 = new JButton("Chess960");
JLabel seeksLabel = new JLabel();
JLabel activitesLabel = new JLabel();
/*pure1.setIcon(sharedVariables.pure1);
pure3.setIcon(sharedVariables.pure3);
pure5.setIcon(sharedVariables.pure5);
pure15.setIcon(sharedVariables.pure15);
pure45.setIcon(sharedVariables.pure45);
pure960.setIcon(sharedVariables.pure960);
*/
pure1.setBackground(new Color(255,255,255));
pure3.setBackground(new Color(255,255,255));
pure5.setBackground(new Color(255,255,255));
pure15.setBackground(new Color(255,255,255));
pure45.setBackground(new Color(255,255,255));
pure960.setBackground(new Color(255,255,255));

seeksLabel.setIcon(sharedVariables.seekIcon);
seeksLabel.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {


 			 if (e.getButton() == MouseEvent.BUTTON3/* || e.getClickCount() == 2*/)
 			;
 			 else
 			 {
			openSeekGraph();
                          }// end else
			 }
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });

//activitesLabel.setIcon(sharedVariables.activitiesIcon);
  activitesLabel.setText("<html><Center><B>A</b></center></html>");
activitesLabel.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {


 			 if (e.getButton() == MouseEvent.BUTTON3/* || e.getClickCount() == 2*/)
 			;
 			 else
 			 {
			openActivities();
                          }// end else
			 }
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });

pure1.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {


 			 if (e.getButton() == MouseEvent.BUTTON3/* || e.getClickCount() == 2*/)
 			;
 			 else
 			 {
				myoutput data = new myoutput();
				data.data="1-Minute\n";
				data.consoleNumber=0;
				queue.add(data);
                          }// end else
			 }
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });

pure3.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {


 			 if (e.getButton() == MouseEvent.BUTTON3/* || e.getClickCount() == 2*/)
 			;
 			 else
 			 {
				myoutput data = new myoutput();
				data.data="3-Minute\n";
				data.consoleNumber=0;
				queue.add(data);
                          }// end else
			 }
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });


pure5.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {


 			 if (e.getButton() == MouseEvent.BUTTON3/* || e.getClickCount() == 2*/)
 			;
 			 else
 			 {
				myoutput data = new myoutput();
				data.data="5-Minute\n";
				data.consoleNumber=0;
				queue.add(data);
                          }// end else
			 }
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });

pure15.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {


 			 if (e.getButton() == MouseEvent.BUTTON3/* || e.getClickCount() == 2*/)
 			;
 			 else
 			 {
				myoutput data = new myoutput();
				data.data="15-Minute\n";
				data.consoleNumber=0;
				queue.add(data);
                          }// end else
			 }
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });

pure45.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {


 			 if (e.getButton() == MouseEvent.BUTTON3/* || e.getClickCount() == 2*/)
 			;
 			 else
 			 {
				myoutput data = new myoutput();
				data.data="45\n";
				data.consoleNumber=0;
				queue.add(data);
                          }// end else
			 }
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });

pure960.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {


 			 if (e.getButton() == MouseEvent.BUTTON3/* || e.getClickCount() == 2*/)
 			;
 			 else
 			 {
				myoutput data = new myoutput();
				data.data="960\n";
				data.consoleNumber=0;
				queue.add(data);
                          }// end else
			 }
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });



  for(int a=0; a<10; a++)
  {





    sharedVariables.mybuttons[a] = new JButton("" + a);
    setButtonTitle(a);
     sharedVariables.mybuttons[a].setFont(sharedVariables.myFont);
   final int con = a;

    sharedVariables.mybuttons[a].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
					toolbarCommands commander = new toolbarCommands(myboards);
				commander.dispatchCommand(con, 0, false, sharedVariables,  queue);


				}});

   if(a != 0)
   toolBar.add(sharedVariables.mybuttons[a]);

  }
   toolBar.add(sharedVariables.mybuttons[0]);
 toolBar.add(seeksLabel);
 toolBar.add(activitesLabel);
 toolBar.add(pure1);
toolBar.add(pure3);
toolBar.add(pure5);
toolBar.add(pure15);
toolBar.add(pure45);
toolBar.add(pure960);
JLabel spacer = new JLabel("");
//toolBar.add(spacer);


}
void sendToEngine(String output)
{
	byte [] b2 = new byte[2500];
	try {
		for(int a=0; a< output.length(); a++)
	b2[a]=(byte) output.charAt(a);


//sharedVariables.engineQueue.add(output);

//Thread.sleep(1000);
//for(int a=0; a<10; a++)
//;
	sharedVariables.engineOut.write(b2,0, output.length());
sharedVariables.engineOut.flush();
}
catch(Exception e){}
}



private static final String CHARSET = "UTF-8";



void repaintTabs()
{

	        for(int i=0; i < sharedVariables.openConsoleCount; i++)
			 {
				 if(consoleSubframes[i]!= null)
	              {

					  for(int a=0; a< sharedVariables.maxConsoleTabs; a++)
					  {
						 try {
							 	if(a==sharedVariables.looking[consoleSubframes[i].consoleNumber]) // tab = console number
							 	consoleSubframes[i].channelTabs[a].setBackground(sharedVariables.tabImOnBackground);
							 	else
						 		consoleSubframes[i].channelTabs[a].setBackground(sharedVariables.tabBackground);
							 consoleSubframes[i].channelTabs[a].setFont(sharedVariables.myTabFont);
						  consoleSubframes[i].channelTabs[a].repaint(); } catch(Exception e){}
					  }
	              }

 			  }

 // now game boards
 	        for(int i=0; i < sharedVariables.maxGameTabs; i++)
 			 {
				 if(myboards[i]!= null)
	              {
	              myboards[i].myconsolepanel.repaint();
	              }
 			}

}

void setInputFont()
{


try {
	        for(int i=0; i < sharedVariables.openConsoleCount; i++)
			 {
				 if(consoleSubframes[i]!= null)
	              {



							 consoleSubframes[i].overall.Input.setFont(sharedVariables.inputFont);

	              }

 			  }

 // now game boards
 	        for(int i=0; i < sharedVariables.maxGameTabs; i++)
 			 {
				 if(myboards[i]!= null)
	              {
	              myboards[i].myconsolepanel.Input.setFont(sharedVariables.inputFont);
	              }
 			}


}catch(Exception badfont){}
}




void loadSoundsStandAlone()
{

// load sounds
try {
	URL songPath;

        if(sharedVariables.operatingSystem.equals("unix"))
        {
        songPath = this.getClass().getResource("whistle.au"); // Geturl of sound
	sharedVariables.songs[0]=songPath;
	songPath = this.getClass().getResource("move-icc.au"); // Geturl of sound
	sharedVariables.songs[1]=songPath;
       songPath = this.getClass().getResource("capture-icc.au"); // Geturl of sound
	sharedVariables.songs[2]=songPath;
 	songPath = this.getClass().getResource("ding.au"); // Geturl of sound
	sharedVariables.songs[4]=songPath;

        }
        else
        {
        songPath = this.getClass().getResource("tell.wav"); // Geturl of sound
	sharedVariables.songs[0]=songPath;
	songPath = this.getClass().getResource("click18a.wav"); // Geturl of sound
  	sharedVariables.songs[1]=songPath;
       songPath = this.getClass().getResource("click10b.wav"); // Geturl of sound
	sharedVariables.songs[2]=songPath;
	songPath = this.getClass().getResource("beeppure.wav"); // Geturl of sound
	sharedVariables.songs[4]=songPath;
       }

	songPath = this.getClass().getResource("serv1a.wav"); // Geturl of sound
	sharedVariables.songs[3]=songPath;
	songPath = this.getClass().getResource("fitebell.au"); // Geturl of sound
	sharedVariables.songs[5]=songPath;
	songPath = this.getClass().getResource("buzzer.wav"); // Geturl of sound
	sharedVariables.songs[6]=songPath;
	songPath = this.getClass().getResource("fitbell.wav"); // Geturl of sound
	sharedVariables.songs[7]=songPath;


	songPath = this.getClass().getResource("BEEP_FM.wav"); // Geturl of sound
	sharedVariables.poweroutSounds[0]=songPath;
	songPath = this.getClass().getResource("BEEPPURE.wav"); // Geturl of sound
	sharedVariables.poweroutSounds[1]=songPath;
	songPath = this.getClass().getResource("BEEPSPAC.wav"); // Geturl of sound
	sharedVariables.poweroutSounds[2]=songPath;

//song1 = new Sound("DING.WAV");
//song2 = new Sound("BEEPPURE.wav");
//song1 = new Sound("BEEP_FM.wav");
//song3 = new Sound("BEEPSPAC.wav");


}
catch(Exception e){}


}


void loadGraphicsStandAlone()
{
try {
	URL myiconurl = this.getClass().getResource( "images/game.gif");
sharedVariables.gameIcon = new ImageIcon(myiconurl, "Game");
 myiconurl = this.getClass().getResource( "images/oval1.png");
sharedVariables.pure1 = new ImageIcon(myiconurl, "1");
 myiconurl = this.getClass().getResource( "images/oval3.png");
sharedVariables.pure3 = new ImageIcon(myiconurl, "3");
 myiconurl = this.getClass().getResource( "images/oval5.png");
sharedVariables.pure5 = new ImageIcon(myiconurl, "5");
 myiconurl = this.getClass().getResource( "images/oval15.png");
sharedVariables.pure15 = new ImageIcon(myiconurl, "15");
 myiconurl = this.getClass().getResource( "images/oval45.png");
sharedVariables.pure45 = new ImageIcon(myiconurl, "45");
 myiconurl = this.getClass().getResource( "images/oval960.png");
sharedVariables.pure960 = new ImageIcon(myiconurl, "960");
 myiconurl = this.getClass().getResource( "images/seekIcon.png");
sharedVariables.seekIcon = new ImageIcon(myiconurl, "seekIcon");

 myiconurl = this.getClass().getResource( "images/activitiesIcon.png");
sharedVariables.activitiesIcon = new ImageIcon(myiconurl, "activitiesIcon");

 myiconurl = this.getClass().getResource( "images/observing.gif");
sharedVariables.observeIcon = new ImageIcon(myiconurl, "observing");

 myiconurl = this.getClass().getResource( "images/playing.gif");
sharedVariables.playingIcon = new ImageIcon(myiconurl, "Playing");

myiconurl = this.getClass().getResource( "images/examining.gif");
sharedVariables.examiningIcon = new ImageIcon(myiconurl, "Examining");


 myiconurl = this.getClass().getResource( "images/sposition.gif");
sharedVariables.sposIcon = new ImageIcon(myiconurl, "Sposition");

 myiconurl = this.getClass().getResource( "images/was.gif");
sharedVariables.wasIcon = new ImageIcon(myiconurl, "was");
}catch(Exception dd){}

URL myurl = this.getClass().getResource("images/x.gif");
graphics.xpiece =Toolkit.getDefaultToolkit().getImage(myurl);

for( int a = 1 ; a < graphics.maxBoards; a++)
{
	if( a != 6)
	{
		myurl = this.getClass().getResource(graphics.boardPaths[a] + "/light.gif");
		graphics.boards[a][0] =Toolkit.getDefaultToolkit().getImage(myurl);
		myurl = this.getClass().getResource(graphics.boardPaths[a] + "/dark.gif");
		graphics.boards[a][1] =Toolkit.getDefaultToolkit().getImage(myurl);

	}
	else
	{
		 myurl = this.getClass().getResource(graphics.boardPaths[a] + "/light.png");
		graphics.boards[a][0] =Toolkit.getDefaultToolkit().getImage(myurl);
		myurl = this.getClass().getResource(graphics.boardPaths[a] + "/dark.png");
		graphics.boards[a][1] =Toolkit.getDefaultToolkit().getImage(myurl);
	}

}

for( int a = 0 ; a < graphics.maxPieces; a++)
{
		String ext = "gif";
		ext = graphics.pieceExt[a];
		if(ext.equals("mix"))// was rand
			break;   // allways last. not real pieces , randomly generated
		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wp." + ext);
		graphics.pieces[a][0] =Toolkit.getDefaultToolkit().getImage(myurl);
		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wn." + ext);
		graphics.pieces[a][1] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wb." + ext);
		graphics.pieces[a][2] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wr." + ext);
		graphics.pieces[a][3] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wq." + ext);
		graphics.pieces[a][4] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wk." + ext);
		graphics.pieces[a][5] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bp." + ext);
		graphics.pieces[a][6] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bn." + ext);
		graphics.pieces[a][7] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bb." + ext);
		graphics.pieces[a][8] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/br." + ext);
		graphics.pieces[a][9] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bq." + ext);
		graphics.pieces[a][10] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bk." + ext);
		graphics.pieces[a][11] =Toolkit.getDefaultToolkit().getImage(myurl);

// now load multi pieces
if(graphics.resizable[a]==false)
{for(int aa=0; aa < graphics.numberPiecePaths[a]; aa++)
{

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wp." + ext);
		graphics.multiPieces[a][aa][0] =Toolkit.getDefaultToolkit().getImage(myurl);
		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wn." + ext);
		graphics.multiPieces[a][aa][1] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wb." + ext);
		graphics.multiPieces[a][aa][2] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wr." + ext);
		graphics.multiPieces[a][aa][3] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wq." + ext);
		graphics.multiPieces[a][aa][4] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wk." + ext);
		graphics.multiPieces[a][aa][5] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bp." + ext);
		graphics.multiPieces[a][aa][6] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bn." + ext);
		graphics.multiPieces[a][aa][7] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bb." + ext);
		graphics.multiPieces[a][aa][8] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/br." + ext);
		graphics.multiPieces[a][aa][9] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bq." + ext);
		graphics.multiPieces[a][aa][10] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bk." + ext);
		graphics.multiPieces[a][aa][11] =Toolkit.getDefaultToolkit().getImage(myurl);



}// end aa loop

}// end multi piece if
	}// end for
  // make monge mix pieces

  int fantasy=0;
  int spatial=0;

  for(int e=0; e<graphics.maxPieces; e++)
  { if(graphics.piecePaths[e].equals("fantasy"))
      fantasy=e;
  if(graphics.piecePaths[e].equals("spatial"))
      spatial=e;

  }
 for(int aa=0; aa < graphics.numberPiecePaths[graphics.maxPieces-2]; aa++)
  {
    graphics.multiPieces[graphics.maxPieces-2][aa][0] = graphics.multiPieces[fantasy][aa][0];
          graphics.multiPieces[graphics.maxPieces-2][aa][1] = graphics.multiPieces[spatial][aa][1];
        graphics.multiPieces[graphics.maxPieces-2][aa][2] = graphics.multiPieces[fantasy][aa][2];
        graphics.multiPieces[graphics.maxPieces-2][aa][3] = graphics.multiPieces[fantasy][aa][3];
        graphics.multiPieces[graphics.maxPieces-2][aa][4] = graphics.multiPieces[spatial][aa][4];
        graphics.multiPieces[graphics.maxPieces-2][aa][5] = graphics.multiPieces[spatial][aa][5];
        graphics.multiPieces[graphics.maxPieces-2][aa][6] = graphics.multiPieces[fantasy][aa][6];
        graphics.multiPieces[graphics.maxPieces-2][aa][7] = graphics.multiPieces[spatial][aa][7];
        graphics.multiPieces[graphics.maxPieces-2][aa][8] = graphics.multiPieces[fantasy][aa][8];
        graphics.multiPieces[graphics.maxPieces-2][aa][9] = graphics.multiPieces[fantasy][aa][9];
        graphics.multiPieces[graphics.maxPieces-2][aa][10] = graphics.multiPieces[spatial][aa][10];
        graphics.multiPieces[graphics.maxPieces-2][aa][11] = graphics.multiPieces[spatial][aa][11];
  }


boolean flagger=true;
int index11=-1;
int index22=-1;
int place=0;
while(flagger== true)
{
 index11=sharedVariables.countryNames.indexOf(";", index11+1);
 index22=sharedVariables.countryNames.indexOf(";", index11+1);
 if(index11 > -1 && index22 > -1 && index22 > index11)
 {
  String lookup =sharedVariables.countryNames.substring(index11 + 1, index22);
  sharedVariables.flagImageNames.add(lookup);
  lookup="flags-small/" + lookup + ".png";

		myurl = this.getClass().getResource( lookup);
		sharedVariables.flagImages.add(Toolkit.getDefaultToolkit().getImage(myurl));

 }
 else
 break;

 index11=index22;






}
}// end method


/*
void loadGraphicsApplet()
{
for( int a = 1 ; a < graphics.maxBoards; a++)
{
	if( a != 6)
	{
		graphics.boards[a][0]=getImage(getDocumentBase(), graphics.boardPaths[a] + "/light.gif");
		graphics.boards[a][1]=getImage(getDocumentBase(), graphics.boardPaths[a] + "/dark.gif");
	}
	else
	{
		graphics.boards[a][0]=getImage(getDocumentBase(), graphics.boardPaths[a] + "/light.png");
		graphics.boards[a][1]=getImage(getDocumentBase(), graphics.boardPaths[a] + "/dark.png");
	}

}
for( int a = 0 ; a < graphics.maxPieces; a++)
{


graphics.pieces[a][0] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wp.gif");
graphics.pieces[a][1] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wn.gif");
graphics.pieces[a][2] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wb.gif");
graphics.pieces[a][3] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wr.gif");
graphics.pieces[a][4] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wq.gif");
graphics.pieces[a][5] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wk.gif");
graphics.pieces[a][6] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bp.gif");
graphics.pieces[a][7] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bn.gif");
graphics.pieces[a][8] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bb.gif");
graphics.pieces[a][9] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/br.gif");
graphics.pieces[a][10] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bq.gif");
graphics.pieces[a][11] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bk.gif");



}
}//end method
void loadSoundsApplet()
{// load sounds
try {
	URL songPath = new URL(getCodeBase(), "tell.wav"); // Geturl of sound
	sharedVariables.songs[0]=songPath;
	songPath = new URL(getCodeBase(), "click18a.wav"); // Geturl of sound
	sharedVariables.songs[1]=songPath;
	songPath = new URL(getCodeBase(), "click10b.wav"); // Geturl of sound
	sharedVariables.songs[2]=songPath;
	songPath = new URL(getCodeBase(), "serv1a.wav"); // Geturl of sound
	sharedVariables.songs[3]=songPath;

}
catch(Exception e){}
}//end method
*/
void openUrl(String myurl)
{

				try {

				String os = System.getProperty("os.name").toLowerCase();

					//Process p = Runtime.getRuntime().exec(cmdLine);
				Runtime rt = Runtime.getRuntime();
				if (os.indexOf( "win" ) >= 0)
	            {
				 String[] cmd = new String[4];
	              cmd[0] = "cmd.exe";
	              cmd[1] = "/C";
	              cmd[2] = "start";
	              cmd[3] = myurl;

	              rt.exec(cmd);
			  }
			 else if (os.indexOf( "mac" ) >= 0)
	           {

	             Runtime runtime = Runtime.getRuntime();
				   if(myurl.startsWith("www."))
				   myurl="http://" + myurl;
				   String[] args = { "osascript", "-e", "open location \"" + myurl + "\"" };
				   try
				   {
				     Process process = runtime.exec(args);
				   }
				   catch (IOException e)
				   {
				     // do what you want with this
				     // http://www.devdaily.com/java/mac-java-open-url-browser-osascript
				   }






	             // rt.exec( "open " + myurl);
          /*Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL",
               new Class[] {String.class});
            openURL.invoke(null, new Object[] {myurl});

			http://www.java2s.com/Code/Java/Development-Class/LaunchBrowserinMacLinuxUnix.htm
			*/
			//String[] commandLine = { "safari", "http://www.javaworld.com/" };
			//  Process process = Runtime.getRuntime().exec(commandLine);


	          }
				else
				{             //prioritized 'guess' of users' preference
	              String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
	                  "netscape","opera","links","lynx"};

	              StringBuffer cmd = new StringBuffer();
	              for (int i=0; i<browsers.length; i++)
	                cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + myurl + "\" ");

	              rt.exec(new String[] { "sh", "-c", cmd.toString() });
	              //rt.exec("firefox http://www.google.com");
	              //System.out.println(cmd.toString());


				}// end else
			}// end try
			catch(Exception e)
			{}


}
public void saveSettings() {


          }

// not really used. i am saving to a file on a hard drive now
// this was an attempt to write to the web but any settings are deleted if they clear temp internet files

 public void getSettings() {

          }




void repaintboards()
{
try {
for(int coo=0; coo<sharedVariables.openBoardCount; coo++)
if(myboards[coo]!=null)
if(myboards[coo].isVisible())
{

myboards[coo].mypanel.repaint(0,0, 5000,5000);

} 
if(myboards[0]!=null)
if(myboards[0].isVisible())
myboards[0].repaint();                          }
catch(Exception dui){}

}// end method repaint boards




} // end multi frame class