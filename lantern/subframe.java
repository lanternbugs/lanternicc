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

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
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
import java.awt.datatransfer.Clipboard;
import java.lang.reflect.Method;


class subframe extends JInternalFrame
  implements  ComponentListener, InternalFrameListener, ActionListener {
              // ActionListener,

  //private sharedVariables.ConsoleScrollPane[BoardIndex]
  //sharedVariables.ConsoleScrollPane[BoardIndex];
  subPanel overall;
  int myChannelNumber=-1;
  int consoleNumber;
  JPopupMenu menu;
  JPopupMenu menu2;
  JPopupMenu menu3;
  JPanel mypanel;
  String lastcommand;
  String[] comboMemory;
  String[] namesMemory;
  int madeTextPane;

  JPaintedLabel[] channelTabs;
  JLabel tellLabel;
  JCheckBox tellCheckbox;
  JComboBox prefixHandler;
  JComboBox tabChooser;
  Highlighter myHighlighter;

  channels sharedVariables;
  JTextPane[] consoles;
  
  // Andrey says: want to change this to
  // Queue<myoutput> queue;
  ConcurrentLinkedQueue<myoutput> queue;
  
  String consoleTitle;
  JMenuBar consoleMenu;
  JList myNameList;
  JScrollPane listScroller;
  JCheckBoxMenuItem listChoice;
  gameboard[] myboards;
  int blockInc=23;
  //subframe [] consoleSubframes;
  docWriter myDocWriter;
  //subframe(JFrame frame, boolean mybool)

  subframe(channels sharedVariables1, JTextPane[] consoles1,
           ConcurrentLinkedQueue<myoutput> queue1, docWriter myDocWriter1,
           gameboard[] myboards1) {

    //super(frame, mybool);
    super("Main Console " + (sharedVariables1.openConsoleCount),
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable

    //consoleSubframes=consoleSubframes1;
    consoles=consoles1;
    myDocWriter=myDocWriter1;
    sharedVariables=sharedVariables1;
    queue=queue1;
    myboards=myboards1;
    consoleTitle="Main Console " + (sharedVariables1.openConsoleCount) + ": ";
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    consoleNumber = sharedVariables.openConsoleCount;
    
    /*
    JMenu consoleMenu = new JMenu("Colors/fonts per console");
    JMenuItem setfont = new JMenuItem("Console's Font");
    consoleMenu.add(setfont);
    JCheckBoxMenuItem overridefont =
      new JCheckBoxMenuItem("Console Font Override Tab Font");
    consoleMenu.add(overridefont);
    setfont.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          setConsoleFont();
	}
      });
    overridefont.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (sharedVariables.useConsoleFont[consoleNumber]==true)
            sharedVariables.useConsoleFont[consoleNumber]=false;
          else
            sharedVariables.useConsoleFont[consoleNumber]=true;
        }
      });
    JMenuBar myconsolemenu = new JMenuBar();

    myconsolemenu.add(consoleMenu);
    setJMenuBar(myconsolemenu);
    */

    menu=new JPopupMenu("Popup");
    JMenuItem item = new JMenuItem("Copy");
    item.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          consoles[consoleNumber].copy();
          giveFocus();
        }
      });
    menu.add(item);

    JMenuItem item2 = new JMenuItem("Copy&Paste");
    item2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          consoles[consoleNumber].copy();
          overall.Input.paste();
          if (sharedVariables.operatingSystem.equals("mac"))
            giveFocusTell();
          else
            giveFocus();
        }
      });
    menu.add(item2);
    
    /*
    JMenuItem item3 = new JMenuItem("Hyperlink");
    item3.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            String myurl ="";
            myurl=consoles[consoleNumber].getSelectedText();
            myurl=myurl.trim();
            String myurl2 = myurl.toLowerCase();
            int go=0;
            if (myurl2.startsWith("www."))
              go=1;
            if (myurl2.startsWith("http://"))
              go=1;
            if (myurl2.startsWith("https://"))
              go=1;
            if (go == 0)
              return;
            sharedVariables.openUrl(myurl);

            giveFocus();
          } catch (Exception g) {}
        }
      });
    menu.add(item3);
    */
    
    JMenuItem item3 = new JMenuItem("Google");
    item3.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            String myurl ="";
            myurl=consoles[consoleNumber].getSelectedText();
            myurl=myurl.trim();
            myurl=myurl.replace(" ", "+");

            sharedVariables.openUrl("http://www.google.com/search?q=" + myurl);

            giveFocus();
          } catch (Exception g) {}
        }
      });
    menu.add(item3);
    add(menu);

    menu2 = new JPopupMenu("Popup2");

    /*
    JMenuItem item3 = new JMenuItem("copy");
    item.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          consoles[consoleNumber].copy();}
      });
    menu.add(item3);
    */

    JMenuItem item4a = new JMenuItem("Copy");
    item4a.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          overall.Input.copy();
        }
      });
    menu2.add(item4a);

    JMenuItem item4 = new JMenuItem("Paste");
    item4.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          overall.Input.paste();
        }
      });
    menu2.add(item4);
    add(menu2);

    scrollnow = 1; // we start off with auto scroll
    wheelIsScrolling = true;
    //addMouseListener(this);

    addInternalFrameListener(this);

    consoleMenu = new JMenuBar();
    setJMenuBar(consoleMenu);

    /******************** Layout ********************/
    JMenu mywindows = new JMenu("Layout");
    // Layout / 
    JMenuItem consoleLayout1 = new JMenuItem("Single Rows of Tabs");
    JMenuItem consoleLayout2 = new JMenuItem("Two Rows of Tabs");
    JMenuItem consoleLayout3 = new JMenuItem("No Visible Tabs");
    JMenuItem consoleLayout4 = new JMenuItem("Tabs on Top");
    
    // add to menu bar
    consoleMenu.add(mywindows);
    // Layout /
    mywindows.add(consoleLayout1);
    mywindows.add(consoleLayout2);
    mywindows.add(consoleLayout3);
    mywindows.add(consoleLayout4);


    // add listeners
    consoleLayout1.addActionListener(this);
    consoleLayout2.addActionListener(this);
    consoleLayout3.addActionListener(this);
    consoleLayout4.addActionListener(this);

    /******************** Edit ********************/
    JMenu editmenu = new JMenu("Edit");
    // Edit /
    JMenuItem selectall = new JMenuItem("Select All");
    JMenuItem copyit = new JMenuItem("Copy");
    // .. / (separator)
    JMenuItem telltab = new JMenuItem("Make tell tab");

    // add accelerators
    telltab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
                                                  ActionEvent.CTRL_MASK));

    // add to menu bar
    consoleMenu.add(editmenu);
    // Edit /
    editmenu.add(selectall);
    editmenu.add(copyit);
    editmenu.addSeparator();
    editmenu.add(telltab);

    // add listeners
    selectall.addActionListener(this);
    copyit.addActionListener(this);
    telltab.addActionListener(this);

    /******************** View ********************/
    JMenu viewmenu = new JMenu("View");
    // View /
    listChoice = new JCheckBoxMenuItem("Show Channel Names List");
   JMenuItem chnotify = new JMenuItem("Show online players on channel notify");
    // .. / (separator)
    JMenuItem incfont = new JMenuItem("Increase font size");
    JMenuItem decfont = new JMenuItem("Decrease font size");

    // add accelerators
    chnotify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                                                   ActionEvent.CTRL_MASK));
    incfont.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
                                                  ActionEvent.ALT_MASK));
    decfont.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
                                                  ActionEvent.ALT_MASK));

    // special settings
    listChoice.setSelected((sharedVariables.consolesNamesLayout[consoleNumber] == 1));

    // add to menu bar
    consoleMenu.add(viewmenu);
    // View
    viewmenu.add(listChoice);
    viewmenu.add(chnotify);
    viewmenu.addSeparator();
    viewmenu.add(incfont);
    viewmenu.add(decfont);

    // add listeners
    listChoice.addActionListener(this);
    chnotify.addActionListener(this);
    incfont.addActionListener(this);
    decfont.addActionListener(this);
    
    /******************** User Buttons ********************/
    JMenu buttonmenu = new JMenu("User Buttons");
    // User Buttons /
    JMenuItem[] buttonlist = new JMenuItem[10];
    for (int i=0; i<buttonlist.length; i++)
      buttonlist[i] = new JMenuItem("Button "+i);

    // add accelerators
    buttonlist[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
                                                        ActionEvent.CTRL_MASK));
    buttonlist[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
                                                        ActionEvent.CTRL_MASK));
    buttonlist[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
                                                        ActionEvent.CTRL_MASK));
    buttonlist[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
                                                        ActionEvent.CTRL_MASK));
    buttonlist[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5,
                                                        ActionEvent.CTRL_MASK));
    buttonlist[6].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6,
                                                        ActionEvent.CTRL_MASK));
    buttonlist[7].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7,
                                                        ActionEvent.CTRL_MASK));
    buttonlist[8].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8,
                                                        ActionEvent.CTRL_MASK));
    buttonlist[9].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9,
                                                        ActionEvent.CTRL_MASK));
    buttonlist[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0,
                                                        ActionEvent.CTRL_MASK));

    // add to menu bar
    consoleMenu.add(buttonmenu);
    // User Buttons /
    for (int i=1; i<=10; i++)
      buttonmenu.add(buttonlist[i%10]);

    // add listeners
    for (int i=0; i<10; i++)
      buttonlist[i].addActionListener(this);

    /******************** Console Navigation ********************/
    JMenu consolenav = new JMenu("Navigation");
    // Navigation /
    JMenuItem nextconsole = new JMenuItem("Next console");
    JMenuItem nextchat = new JMenuItem("Next chat console");
    // .. / (separator)
    JMenuItem nexttab = new JMenuItem("Next tab");
    JMenuItem prevtab = new JMenuItem("Previous tab");
    JMenu selecttab = new JMenu("Select tab");
    // .. / Select tab /
    JMenuItem[] tabarray = new JMenuItem[12];
    for (int i=1; i<=tabarray.length; i++)
      tabarray[i-1] = new JMenuItem("Tab " + i);
    // .. / (separator)
    JMenuItem showboard = new JMenuItem("Show board");
    JMenuItem nextbtab = new JMenuItem("Next board tab");
    JMenuItem prevbtab = new JMenuItem("Previous board tab");
    JMenuItem closebtab = new JMenuItem("Close board tab");
    // .. / (separator)
    JMenuItem prevmoves = new JMenuItem("Move to start of game");
    JMenuItem prevmove = new JMenuItem("Previous move");
    JMenuItem nextmove = new JMenuItem("Next move");
    JMenuItem nextmoves = new JMenuItem("Move to end of game");

    // add accelerators
    nextconsole.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
                                                      ActionEvent.CTRL_MASK));
    nextchat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                                                   ActionEvent.CTRL_MASK));
    nexttab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,
                                                  ActionEvent.ALT_MASK));
    prevtab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,
                                                  ActionEvent.ALT_MASK));
    tabarray[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
                                                      ActionEvent.ALT_MASK));
    tabarray[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
                                                      ActionEvent.ALT_MASK));
    tabarray[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
                                                      ActionEvent.ALT_MASK));
    tabarray[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
                                                      ActionEvent.ALT_MASK));
    tabarray[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5,
                                                      ActionEvent.ALT_MASK));
    tabarray[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6,
                                                      ActionEvent.ALT_MASK));
    tabarray[6].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7,
                                                      ActionEvent.ALT_MASK));
    tabarray[7].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8,
                                                      ActionEvent.ALT_MASK));
    tabarray[8].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9,
                                                      ActionEvent.ALT_MASK));
    tabarray[9].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0,
                                                      ActionEvent.ALT_MASK));
    tabarray[10].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
                                                      ActionEvent.ALT_MASK));
    tabarray[11].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,
                                                      ActionEvent.ALT_MASK));
    showboard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
                                                    ActionEvent.ALT_MASK));
    nextbtab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                                                   ActionEvent.ALT_MASK));
    prevbtab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                                                   ActionEvent.ALT_MASK));
    closebtab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                                                    ActionEvent.ALT_MASK));
    prevmoves.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
                                                    ActionEvent.CTRL_MASK |
                                                    ActionEvent.SHIFT_MASK));
    prevmove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
                                                   ActionEvent.CTRL_MASK));
    nextmove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,
                                                   ActionEvent.CTRL_MASK));
    nextmoves.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,
                                                    ActionEvent.CTRL_MASK |
                                                    ActionEvent.SHIFT_MASK));

    // add to menu bar
    consoleMenu.add(consolenav);
    // Navigation /
    consolenav.add(nextconsole);
    consolenav.add(nextchat);
    consolenav.addSeparator();
    consolenav.add(nexttab);
    consolenav.add(prevtab);
    consolenav.add(selecttab);
    // .. / Select tab
    for (int i=0; i<tabarray.length; i++)
      selecttab.add(tabarray[i]);
    // .. /
    consolenav.addSeparator();
    consolenav.add(showboard);
    consolenav.add(nextbtab);
    consolenav.add(prevbtab);
    consolenav.add(closebtab);
    consolenav.addSeparator();
    consolenav.add(prevmoves);
    consolenav.add(prevmove);
    consolenav.add(nextmove);
    consolenav.add(nextmoves);

    // add listeners
    nextconsole.addActionListener(this);
    nextchat.addActionListener(this);
    nexttab.addActionListener(this);
    prevtab.addActionListener(this);
    for (int i=0; i<tabarray.length; i++)
      tabarray[i].addActionListener(this);
    showboard.addActionListener(this);
    nextbtab.addActionListener(this);
    prevbtab.addActionListener(this);
    closebtab.addActionListener(this);
    prevmoves.addActionListener(this);
    prevmove.addActionListener(this);
    nextmove.addActionListener(this);
    nextmoves.addActionListener(this);

    /******************** end of menus ********************/
    
    consoleMenu.setVisible(sharedVariables.showConsoleMenu);
    initComponents();
  }

  public void actionPerformed(ActionEvent event) {
  
    try {
      //Object source = event.getSource();
      //handle action event here
      String action = event.getActionCommand();

      if (action.equals("Show Channel Names List")) {
        if (sharedVariables.consolesNamesLayout[consoleNumber] == 1) {
          sharedVariables.consolesNamesLayout[consoleNumber] = 0;
          listScroller.setVisible(false);
          recreate(sharedVariables.consolesTabLayout[consoleNumber]);
          listChoice.setSelected(false);
        
        } else {
          sharedVariables.consolesNamesLayout[consoleNumber] = 1;
          if (sharedVariables.looking[consoleNumber] != 0) {
            listScroller.setVisible(true);
            recreate(sharedVariables.consolesTabLayout[consoleNumber]);
          }
          listChoice.setSelected(true);
        }
        
      } else if (action.equals("Single Rows of Tabs")) {
        sharedVariables.consolesTabLayout[consoleNumber] = 1;
        recreate(1);

      } else if (action.equals("Two Rows of Tabs")) {
        sharedVariables.consolesTabLayout[consoleNumber] = 2;
        recreate(2);

      } else if (action.equals("No Visible Tabs")) {
        sharedVariables.consolesTabLayout[consoleNumber] = 3;
        recreate(3);

      } else if (action.equals("Tabs on Top")) {
        sharedVariables.consolesTabLayout[consoleNumber] = 4;
        recreate(4);

      } else if (action.equals("Select All")) {
        consoles[consoleNumber].selectAll();
        StyledDocument doc = consoles[consoleNumber].getStyledDocument();
        myHighlighter.addHighlight(0, doc.getLength(),
                                   DefaultHighlighter.DefaultPainter);

      } else if (action.equals("Copy")) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        TransferHandler transferHandler =
          consoles[consoleNumber].getTransferHandler();
        transferHandler.exportToClipboard(consoles[consoleNumber],
                                          clipboard, TransferHandler.COPY);

      } else if (action.equals("Make tell tab")) {
        sharedVariables.tellsToTab = true;
        sharedVariables.tellTab = sharedVariables.looking[consoleNumber];
        myoutput data = new myoutput();
        data.repaintTabBorders = 1;
        queue.add(data);

      } else if (action.equals("Show online players on channel notify")) {
        String mess2 = sharedVariables.getChannelNotifyOnline()
          + sharedVariables.getConnectNotifyOnline();
        writeToConsole(mess2, sharedVariables.responseColor, false);

      } else if (action.equals("Increase font size") ||
                 action.equals("Decrease font size")) {
        boolean incdec = action.equals("Increase font size");
        int con = sharedVariables.looking[consoleNumber];

        if (sharedVariables.tabStuff[con].tabFont != null) {
          float fontsize =
            (float) sharedVariables.tabStuff[con].tabFont.getSize();
          fontsize += (incdec ? 1 : -1);
          sharedVariables.tabStuff[con].tabFont =
            sharedVariables.tabStuff[con].tabFont.deriveFont(fontsize);
        } else {
          float fontsize = (float) sharedVariables.myFont.getSize();
          fontsize += (incdec ? 1 : -1);
          sharedVariables.myFont =
            sharedVariables.myFont.deriveFont(fontsize);
        }
        makeHappen(con);

      } else if (action.equals("Button 1")) {
        doToolBarCommand(1);

      } else if (action.equals("Button 2")) {
        doToolBarCommand(2);

      } else if (action.equals("Button 3")) {
        doToolBarCommand(3);

      } else if (action.equals("Button 4")) {
        doToolBarCommand(4);

      } else if (action.equals("Button 5")) {
        doToolBarCommand(5);

      } else if (action.equals("Button 6")) {
        doToolBarCommand(6);

      } else if (action.equals("Button 7")) {
        doToolBarCommand(7);

      } else if (action.equals("Button 8")) {
        doToolBarCommand(8);

      } else if (action.equals("Button 9")) {
        doToolBarCommand(9);

      } else if (action.equals("Button 0")) {
        doToolBarCommand(0);

      } else if (action.equals("Next console")) {
        switchWindows();
        
      } else if (action.equals("Next chat console")) {
        switchConsoleWindows();

      } else if (action.equals("Next tab") ||
                 action.equals("Previous tab")) {
        boolean nextprev = action.equals("Next tab");
        int mct = sharedVariables.maxConsoleTabs;
        int con = (sharedVariables.looking[consoleNumber] +
                   (nextprev ? 1 : mct - 1))%mct;
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3) {
          tabChooser.setSelectedIndex(con);
        }
        makeHappen(con);

      } else if (action.equals("Tab 1")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(0);
        makeHappen(0);

      } else if (action.equals("Tab 2")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(1);
        makeHappen(1);

      } else if (action.equals("Tab 3")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(2);
        makeHappen(2);

      } else if (action.equals("Tab 4")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(3);
        makeHappen(3);

      } else if (action.equals("Tab 5")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(4);
        makeHappen(4);

      } else if (action.equals("Tab 6")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(5);
        makeHappen(5);

      } else if (action.equals("Tab 7")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(6);
        makeHappen(6);

      } else if (action.equals("Tab 8")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(7);
        makeHappen(7);

      } else if (action.equals("Tab 9")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(8);
        makeHappen(8);

      } else if (action.equals("Tab 10")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(9);
        makeHappen(9);

      } else if (action.equals("Tab 11")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(10);
        makeHappen(10);

      } else if (action.equals("Tab 12")) {
        if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
          tabChooser.setSelectedIndex(11);
        makeHappen(11);

      } else if (action.equals("Show board")) {
        int games = getActiveGame();
                
        if (games > -1) {
          myboards[games].setSelected(true);
          giveFocus();
        }

      } else if (action.equals("Next board tab") ||
                 action.equals("Previous board tab")) {
        boolean nextprev = action.equals("Next board tab");
        int games = getActiveGame();
        if (games > -1) {
          myboards[games].myconsolepanel.makehappen
            (myboards[games].myconsolepanel.getNextGame(nextprev));
          giveFocus();
        }

      } else if (action.equals("Close board tab")) {
        int games = getActiveGame();
                
        if (games > -1) {
          myoutput data = new myoutput();
          data.closetab = myboards[games].myconsolepanel.getPhysicalTab
            (myboards[games].gameData.LookingAt);
          data.focusConsole = consoleNumber;

          queue.add(data);
        }

      } else if (action.equals("Move to start of game") ||
                 action.equals("Previous move") ||
                 action.equals("Next move") ||
                 action.equals("Move to end of game")) {
        int games = getActiveGame();
        if (games > -1) {
          JSlider js = sharedVariables.moveSliders[games];
          int loc = js.getValue();
          int max = js.getMaximum();
          if (action.equals("Move to start of game"))
            loc = 0;
          else if (action.equals("Previous move"))
            loc -= (loc == 0 ? 0 : 1);
          else if (action.equals("Next move"))
            loc += (loc == max ? 0 : 1);
          else // if (action.equals("Move to end of game"))
            loc = max;
          
          js.setValue(loc);
          myboards[games].mycontrolspanel.adjustMoveList();
          myboards[games].repaint();
        }
        
        giveFocus();

      }
    } catch (Exception badEvent) {}
  }// end method action performed

  void recreate(int num) {

    getContentPane().removeAll();

    if (num == 1 || num==4)  // 4 is same as one but tabs on top
      overall.setMyLayout1();
    else if (num == 2)
      overall.setMyLayout2();
    else
      overall.setMyLayout3();
  }

  void setTabFont(int con) {

    JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.tabStuff[con].tabFont);
    fc.setVisible(true);
    Font fnt = fc.getSelectedFont();
    if (fnt != null) {
      sharedVariables.tabStuff[con].tabFont=fnt;
      if (sharedVariables.looking[consoleNumber]==con)
        consoles[consoleNumber].setFont(fnt);
    }
  }

  void setConsoleFont() {

    JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc =
      new FontChooser2(f, sharedVariables.consoleFonts[consoleNumber]);
    fc.setVisible(true);
    Font fnt = fc.getSelectedFont();
    if (fnt != null) {
      sharedVariables.consoleFonts[consoleNumber]=fnt;
      if (sharedVariables.useConsoleFont[consoleNumber]!=false)
        consoles[consoleNumber].setFont(fnt);
    }
  }

  void setTabColors(int con) {
    JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
    customizeTabConsolelColorsDialog frame =
      new customizeTabConsolelColorsDialog((JFrame) myself.myframe,
                                           false, sharedVariables,
                                           consoles, con, this);
  }
  
  void changeTellTab(boolean forward) {
    JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
    if (forward == true)
      myself.changeTellTabForward();
    else
      myself.changeTellTabBackward();
  }

  void switchWindows() {
    JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
    myself.switchWindows();
  }

  void switchConsoleWindows() {
    JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
    myself.switchConsoleWindows();
  }

  void customizeTabQtells(int num) {
    JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
    customizeChannelQtellsDialog frame =
      new customizeChannelQtellsDialog((JFrame) myself.myframe,
                                       false,  sharedVariables, num);
    frame.setVisible(true);
  }

  void customizeTab(int num) {
    JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
    customizeChannelsDialog frame =
      new customizeChannelsDialog((JFrame) myself.myframe, false,
                                  num, sharedVariables,
                                  myself.consoleSubframes);
    if (sharedVariables.looking[consoleNumber]==num)
      makeHappen(num);
  }
  
  void removeSelectionHighlight() {
    //consoles[consoleNumber].getHighlighter().removeHighlights(consoles[consoleNumber]);
    //remove highlight if they click
    try {
      myHighlighter.removeAllHighlights();

    } catch (Exception d) {}
  }

  boolean restoreNamesList(int con) {
    if (con==0)
      return false;

    try {
      if (sharedVariables.consolesNamesLayout[consoleNumber]==1)
	listScroller.setVisible(true);

      for (int z=0; z< sharedVariables.channelNamesList.size(); z++) {
        int mychan=Integer.parseInt(sharedVariables.channelNamesList.get(z).channel);

        if (sharedVariables.console[con][mychan] == 1)
          if (sharedVariables.channelNamesList.get(z).channel.equals(namesMemory[con])) {
            if (sharedVariables.channelNamesList.get(z).model.size()>0) {
              myNameList.setModel(sharedVariables.channelNamesList.get(z).model);
              myChannelNumber=mychan;
              return true;
            }
          }
      }// end for

    } catch (Exception badmodel) {}
    return false;
  }// end method

  void setNameList(int con) {
    int mychan=-1;
    try {
      if (con==0) {
        nameListClass listclasstype = new nameListClass();
        listclasstype.addToList("Default");
        myNameList.setModel(listclasstype.model);

        listScroller.setVisible(false);

      } else {
	int number=-1;
	if (sharedVariables.consolesNamesLayout[consoleNumber]==1)
          listScroller.setVisible(true);

        int z=0;
        for (z=0; z<sharedVariables.channelNamesList.size(); z++) {
          mychan=Integer.parseInt(sharedVariables.channelNamesList.get(z).channel);

          if (sharedVariables.console[con][mychan] == 1) {
            number=z;
            break;
          }
        }// end for

        if (number > -1) {
          myNameList.setModel(sharedVariables.channelNamesList.get(z).model);
          myChannelNumber=mychan;
          namesMemory[con]="" + mychan;
        // end if number > -1
        } else {
          nameListClass listclasstype = new nameListClass();
          listclasstype.addToList("Default");
          myNameList.setModel(listclasstype.model);
        }
      }// end else
    // end try
    } catch (Exception nochan) {}
  }// end method

  void changeTellTab(int n) {
    if (sharedVariables.tellsToTab == true && sharedVariables.tellTab == n) {
      // i am currently directing tells to this tab
      sharedVariables.tellsToTab = false;
    } else {
      sharedVariables.tellsToTab = true;
      sharedVariables.tellTab = n;
      myoutput data = new myoutput();
      data.repaintTabBorders=1;
      queue.add(data);
    }
  }
  
  void makeHappen(int con) {
    myHighlighter.removeAllHighlights();
    channelTabs[sharedVariables.looking[consoleNumber]].setBackground(sharedVariables.tabBackground);
    sharedVariables.looking[consoleNumber] = con;
    consoles[consoleNumber].setStyledDocument(sharedVariables.mydocs[con]);
    Color my = new Color(193,153,153);
    setActiveTabForeground(con);
    if (con > 0 && namesMemory[con]!=null) {
      boolean nametry = false;
      try {
        if (namesMemory[con].length() > 0)
          nametry = restoreNamesList(con);

      } catch (Exception duey) {}
      if (!nametry)
        setNameList(con);
    } else {
      setNameList(con);
    }
    //if (sharedVariables.consoleFonts[consoleNumber]!=null &&
    //    sharedVariables.useConsoleFont[consoleNumber]== true)
    //	consoles[consoleNumber].setFont(sharedVariables.consoleFonts[consoleNumber]);
    //else
    if (sharedVariables.tabStuff[con].tabFont != null)
      consoles[consoleNumber].setFont(sharedVariables.tabStuff[con].tabFont);
    else
      consoles[consoleNumber].setFont(sharedVariables.myFont);

    if (sharedVariables.tabStuff[con].BackColor != null)
      consoles[consoleNumber].setBackground(sharedVariables.tabStuff[con].BackColor);
    else
      consoles[consoleNumber].setBackground(sharedVariables.BackColor);

    if (sharedVariables.tabStuff[con].ForColor != null)
      consoles[consoleNumber].setForeground(sharedVariables.tabStuff[con].ForColor);
    else
      consoles[consoleNumber].setForeground(sharedVariables.ForColor);
    setTitle(consoleTitle + sharedVariables.consoleTabTitles[con]);
    wheelIsScrolling = false;
    scrollnow = 1;
    updateComboBox(con);
  }

  void initTabCombo() {
    String[] tabNames = new String[sharedVariables.maxConsoleTabs];
    for (int a=0; a<sharedVariables.maxConsoleTabs; a++)
      if (a == 0)
        tabNames[a] = "M0";
      else
        tabNames[a] = "C"+a;
    try {
      for (int ab=0; ab<sharedVariables.maxConsoleTabs; ab++)
        if (!sharedVariables.consoleTabCustomTitles[ab].equals(""))
          tabNames[ab] = sharedVariables.consoleTabCustomTitles[ab];
        else
          tabNames[ab] = sharedVariables.consoleTabTitles[ab];

    } catch (Exception d) {}

    tabChooser = new JComboBox(tabNames);
    tabChooser.setEditable(false);
  }
  
  void initTabChooser() {
    initTabCombo();

    tabChooser.setSelectedIndex(sharedVariables.looking[consoleNumber]);
    tabChooser.setEditable(false);
    tabChooser.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JComboBox cb = (JComboBox)e.getSource();
          try {
            makeHappen(cb.getSelectedIndex());
            giveFocus();
            //JFrame aframe = new JFrame();
            //aframe.setSize(200,200);
            //aframe.setTitle(comboMemory[sharedVariables.looking[consoleNumber]] +
            //                " " + sharedVariables.looking[consoleNumber] +
            //                " " + e.getActionCommand());
            //aframe.setVisible(true);
          } catch (Exception cant) {}
        }
      });
  }

  private void initComponents() {
    String[] prefixStrings = {">"};

    prefixHandler = new JComboBox(prefixStrings);
    prefixHandler.setSelectedIndex(0);
    prefixHandler.setEditable(false);

    initTabCombo();

    // for 10 tabs we assume preselected index is ">"
    comboMemory = new String[sharedVariables.maxConsoleTabs];
    namesMemory = new String[sharedVariables.maxConsoleTabs];
    for (int cmem=0; cmem<sharedVariables.maxConsoleTabs; cmem++)
      comboMemory[cmem] = ">";
    prefixHandler.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JComboBox cb = (JComboBox)e.getSource();
          try {
            String mytext = (String)cb.getSelectedItem();
            if (mytext != null) {
              comboMemory[sharedVariables.looking[consoleNumber]] = mytext;
              if (mytext.equals(">") || overall.Input.getText().startsWith("/"))
                overall.Input.setForeground(sharedVariables.inputCommandColor);
              else
                overall.Input.setForeground(sharedVariables.inputChatColor);
            }
            giveFocus();
            //JFrame aframe = new JFrame();
            //aframe.setSize(200,200);
            //aframe.setTitle(comboMemory[sharedVariables.looking[consoleNumber]] +
            //                " " + sharedVariables.looking[consoleNumber] +
            //                " " + e.getActionCommand());
            //aframe.setVisible(true);
          } catch (Exception cant) {}
        }
      });

    updateComboBox(sharedVariables.openConsoleCount);

    channelTabs = new JPaintedLabel[sharedVariables.maxConsoleTabs];
    for (int a=0; a<sharedVariables.maxConsoleTabs; a++) {
      if (a==0) {
        channelTabs[a] = new JPaintedLabel("M" + a, sharedVariables, a);
      } else {
        channelTabs[a] = new JPaintedLabel(sharedVariables.consoleTabTitles[a],
                                           sharedVariables, a);

        if (!sharedVariables.consoleTabCustomTitles[a].equals(""))
          channelTabs[a].setFullText(sharedVariables.consoleTabCustomTitles[a]);
        else
          channelTabs[a].setText(sharedVariables.consoleTabTitles[a]);
      }// end else
    }

    tellLabel = new JLabel("tells");
    tellCheckbox = new JCheckBox();
    if (sharedVariables.tellconsole == consoleNumber)
      tellCheckbox.setSelected(true);

    tellCheckbox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          sharedVariables.tellconsole=consoleNumber;
          sharedVariables.updateTellConsole=1;
        }
      });

    for (int cona=0; cona<sharedVariables.maxConsoleTabs; cona++) {
      final int con=cona;

      channelTabs[con].addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2) {
              makerightclickhappen(e, con);
            } else {
              makeHappen(con);
            }
          }

          public void mouseReleased(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON3) {
              sharedVariables.looking[consoleNumber] = con;
              consoles[consoleNumber].setStyledDocument(sharedVariables.mydocs[con]);
              Color my = new Color(193,153,153);
              setActiveTabForeground(con);
              updateComboBox(con);
            }
          }

          public void mouseEntered(MouseEvent me) {}
          public void mouseExited(MouseEvent me) {}
          public void mouseClicked(MouseEvent me) {}
          
          public void makerightclickhappen(MouseEvent e, final int n) {
            JPopupMenu menu2 = new JPopupMenu("Popup2");
            JMenuItem item11 = new JMenuItem("trim tab chat");
            item11.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  myoutput data = new myoutput();
                  data.trimconsole = n;
                  queue.add(data);
                }
              });
            
            JMenu submenu  = new JMenu("Advanced");

            submenu.add(item11);
            JMenuItem item2 = new JMenuItem("set tab channels and name");
            item2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  customizeTab(n);
                }
              });
            if (n != 0)
              menu2.add(item2);

            JMenuItem item3 = new JMenuItem("set tab font");
            item3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  setTabFont(n);
                }
              });
            menu2.add(item3);

            JMenuItem item4 = new JMenuItem("set tab colors");
            item4.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  setTabColors(n);
                }
              });
            menu2.add(item4);

            JMenuItem item1 = new JMenuItem("clear tab chat");
            item1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  myoutput data = new myoutput();
                  data.clearconsole = n;
                  queue.add(data);
                }
              });
            menu2.add(item1);

            final JCheckBoxMenuItem item5 = new JCheckBoxMenuItem("show typed text");
            item5.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  /*
                  if (sharedVariables.tabStuff[n].typed) {
                    sharedVariables.tabStuff[n].typed = false;
                    item5.setSelected(false);
                  } else {
                    sharedVariables.tabStuff[n].typed = true;
                    item5.setSelected(true);
                  }
                  */
                  sharedVariables.tabStuff[n].typed = !sharedVariables.tabStuff[n].typed;
                  item5.setSelected(sharedVariables.tabStuff[n].typed);
                }
              });
            /*
            if (sharedVariables.tabStuff[n].typed)
              item5.setSelected(true);
            else
              item5.setSelected(false);
            */
            item5.setSelected(sharedVariables.tabStuff[n].typed);
            
            submenu.add(item5);

            final JCheckBoxMenuItem item6 = new JCheckBoxMenuItem("suppress (told ...)");
            item6.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  /*
                  if (!sharedVariables.tabStuff[n].told) {
                    sharedVariables.tabStuff[n].told = true;
                    item6.setSelected(true);
                  } else {
                    sharedVariables.tabStuff[n].told = false;
                    item6.setSelected(false);
                  }
                  */
                  sharedVariables.tabStuff[n].told = !sharedVariables.tabStuff[n].told;
                  item6.setSelected(!sharedVariables.tabStuff[n].told);
                }
              });
            /*
            if (!sharedVariables.tabStuff[n].told)
              item6.setSelected(true);
            else
              item6.setSelected(false);
            */
            item6.setSelected(!sharedVariables.tabStuff[n].told);
            submenu.add(item6);

            JCheckBoxMenuItem item7 = new JCheckBoxMenuItem("make tell tab");
            item7.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  changeTellTab(n);
                }
              });

            if (sharedVariables.tellsToTab && sharedVariables.tellTab == n)
              item7.setSelected(true);
            //if (sharedVariables.tellconsole == consoleNumber)
            menu2.add(item7);

            JMenuItem itemQ = new JMenuItem("Manage Qtells For Channels on Tab");
            itemQ.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  customizeTabQtells(n);
                }
              });

            menu2.add(itemQ);

            menu2.add(submenu);

            add(menu2);
            menu2.show(e.getComponent(),e.getX(),e.getY());
          }
        });
    }

    nameListClass listclasstype = new nameListClass();
    listclasstype.addToList("Default");
    myNameList = new JList(listclasstype.model);
    myNameList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    myNameList.setLayoutOrientation(JList.VERTICAL);
    myNameList.setVisibleRowCount(-1);
    myNameList.setForeground(sharedVariables.nameForegroundColor);
    myNameList.setBackground(sharedVariables.nameBackgroundColor);
    listScroller = new JScrollPane(myNameList);

    MouseListener mouseListenerNotify = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {

          if (e.getButton() == MouseEvent.BUTTON3) {// right click
            int index = myNameList.locationToIndex(e.getPoint());
            String Name = (String)myNameList.getModel().getElementAt(index);

            setupMenu(Name);
            menu3.show(e.getComponent(),e.getX(),e.getY());
            return;
          }
          //rotate through
          int z = 0;
          int point = -1;
          int number = -1;
          int mychan = -1;
          for (z=0; z<sharedVariables.channelNamesList.size(); z++) {
            try {
              mychan = Integer.parseInt(sharedVariables.channelNamesList.get(z).channel);

              if (mychan == myChannelNumber) {
                point=z;
                break;
              }
            } catch (Exception dd) {}
          }// end for

          for (z=point+1; z<sharedVariables.channelNamesList.size(); z++) {
            mychan = Integer.parseInt(sharedVariables.channelNamesList.get(z).channel);

            if (sharedVariables.console[sharedVariables.looking[consoleNumber]][mychan] == 1) {
              number = z;
              break;
            }
          }// end for


          if (number == -1) {
            for (z=0; z<point; z++) {
              mychan = Integer.parseInt(sharedVariables.channelNamesList.get(z).channel);

              if (sharedVariables.console[sharedVariables.looking[consoleNumber]][mychan] == 1) {
                number = z;
                break;
              }
            }// end for
          }

          if (number > -1) {
            myNameList.setModel(sharedVariables.channelNamesList.get(number).model);
            myChannelNumber = mychan;
            namesMemory[sharedVariables.looking[consoleNumber]] = "" + mychan;
            myNameList.repaint();
          }// end if number > -1
        }
      };

    myNameList.addMouseListener(mouseListenerNotify);

    //listScroller.setVisible(false);
    //myNameList.setVisible(false);

    consoles[consoleNumber] = new JTextPane();
    myHighlighter = consoles[consoleNumber].getHighlighter();
    if (sharedVariables.mydocs[consoleNumber] == null)
      // new logic we don't want to erase the console on restore
      sharedVariables.mydocs[consoleNumber] = consoles[consoleNumber].getStyledDocument();
    else
      consoles[consoleNumber].setStyledDocument(sharedVariables.mydocs[consoleNumber]);

    consoles[consoleNumber].addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
          //if(e.isPopupTrigger())
          try {
            if (e.getClickCount() == 1 && e.getButton() != MouseEvent.BUTTON3)
              removeSelectionHighlight();

            if (e.getButton() == MouseEvent.BUTTON3 ||
                (e.getClickCount() == 2 && sharedVariables.autopopup)) {
              if (consoles[consoleNumber].getSelectedText().indexOf(" ") == -1) {
                setupMenu(consoles[consoleNumber].getSelectedText());
                menu3.show(e.getComponent(),e.getX(),e.getY());
              } else {
                menu.show(e.getComponent(),e.getX(),e.getY());
              }
            }
          // end try
          } catch (Exception mousebad) {}
        }

        /*
        public void mouseReleased(MouseEvent e) {
          if (e.isPopupTrigger())
            if (consoles[consoleNumber].getSelectedText().indexOf(" ") == -1)
              menu3.show(e.getComponent(),e.getX(),e.getY());
            else
              menu.show(e.getComponent(),e.getX(),e.getY());
        }
        */

        public void mouseEntered(MouseEvent me) {}
        public void mouseExited(MouseEvent me) {}
        public void mouseClicked(MouseEvent me) {

	}
      });

    consoles[consoleNumber].addMouseListener(new MouseListener() {
        public void mouseClicked(MouseEvent e) {   
          if (e.getButton() == MouseEvent.BUTTON3)
            return; // right click

          JTextPane editor = (JTextPane) e.getSource();
          if (!editor.isEditable()) {
            Point pt = new Point(e.getX(), e.getY());
            int pos = editor.viewToModel(pt);
            if (pos >= 0) {
              // get the element at the pos
              // check if the elemnt has the HREF
              // attribute defined
              // if so notify the HyperLinkListeners
              //Style mine = getLogicalStyle(pos);
              Element e2 = editor.getStyledDocument().getCharacterElement(pos);
              AttributeSet at = e2.getAttributes();
              //String underline = "false";
              SimpleAttributeSet attrs = new SimpleAttributeSet();
              StyleConstants.setUnderline(attrs, true);
              String myurl = "";
              if (at.containsAttributes(attrs)) {
                //e2.setVisible(false);
                //underline = "true";
                //at has atributes
                myurl += at.getAttribute(javax.swing.text.html.HTML.Attribute.HREF).toString();
                String myurl2 = myurl;
                myurl2 = myurl2.toLowerCase();
                if (myurl2.startsWith("/"))
                  myurl2 = myurl2.substring(1, myurl2.length());
                /*
                if (myurl2.startsWith("observe")) {
                  dispatchCommand(myurl);

                } else if (myurl2.startsWith("finger")) {
                  dispatchCommand(myurl);

                } else if (myurl2.startsWith("help")) {
                  dispatchCommand(myurl);

                } else if(myurl2.startsWith("accept")) {
                  dispatchCommand(myurl);

                } else if (myurl2.startsWith("decline")) {
                  dispatchCommand(myurl);

                } else if (myurl2.startsWith("match")) {
                  dispatchCommand(myurl);

                } else if (myurl2.startsWith("examine")) {
                  dispatchCommand(myurl);
                  
                } else if (myurl2.startsWith("follow")) {
                  dispatchCommand(myurl);

                } else if (myurl2.startsWith("play")) {
                  dispatchCommand(myurl);

                } else if (myurl2.startsWith("games")) {
                  dispatchCommand(myurl);

                } else if (myurl2.startsWith("liblist")) {
                  dispatchCommand(myurl);
                */
                if (myurl2.startsWith("observe") ||
                    myurl2.startsWith("finger") ||
                    myurl2.startsWith("help") ||
                    myurl2.startsWith("accept") ||
                    myurl2.startsWith("decline") ||
                    myurl2.startsWith("match") ||
                    myurl2.startsWith("examine") ||
                    myurl2.startsWith("follow") ||
                    myurl2.startsWith("play") ||
                    myurl2.startsWith("games") ||
                    myurl2.startsWith("liblist")) {
                  dispatchCommand(myurl);
                  
                } else {
                  sharedVariables.openUrl(myurl);
                }
              }
            }
          }
        }// end click event

        public void mousePressed(MouseEvent e) {}
        public void mouseEntered(MouseEvent me) {}
        public void mouseReleased(MouseEvent me) {}
        public void mouseExited(MouseEvent me) {}
      });

    consoles[consoleNumber].addHyperlinkListener(new HyperlinkListener() {
        public void hyperlinkUpdate(HyperlinkEvent r) {
          try {
            if (r.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
              //finalpane.setPage(r.getURL());
              //String cmdLine = "start " + r.getURL();
              //Process p = Runtime.getRuntime().exec(cmdLine);
              String myurl = "" + r.getURL();
              sharedVariables.openUrl("www.adam16mr.org");
            }
             
          } catch (Exception e) {}
        }
      });

    scrollbutton = new JButton("no scroll");
    scrollbutton.setVisible(false);
    scrollbutton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          scrollnow = (scrollnow+1)%2;
          if (scrollnow == 1)
            scrollbutton.setText("no scroll");
          else
            scrollbutton.setText("autoscroll");
          /*
            JFrame aframe = new JFrame();
            int d = consoles[consoleNumber].getScrollableBlockIncrement(consoles[consoleNumber].getVisibleRect(),
                                                                        SwingConstants.VERTICAL, -1);
            int f = sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue();
            int g = sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum();
            aframe.setTitle(" current value" + f + " and maximum " + g + " and block inc is "+ d);
            aframe.setVisible(true);
          */
        }
      });

    //newbox.setColumns(20);
    //newbox.setLineWrap(true);
    //newbox.setRows(5);
    //newbox.setWrapStyleWord(true);
    consoles[consoleNumber].setEditable(false);
    sharedVariables.ConsoleScrollPane[consoleNumber] =
      new JScrollPane(consoles[consoleNumber], JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    overall = new subPanel();
    add(overall);

    consoles[consoleNumber].addKeyListener(new KeyListener() {
        public void keyPressed(KeyEvent e) {
          int a = e.getKeyCode();
          if (a==33) {
            scrollnow = 0;
          }
        }
          
        public void keyTyped(KeyEvent e) {

        }

        /** Handle the key-released event from the text field.
         */
        public void keyReleased(KeyEvent e) {

        }
      });

    consoles[consoleNumber].addMouseWheelListener(new MouseWheelListener() {
        //sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().addMouseWheelListener(new MouseWheelListener()
        public void mouseWheelMoved(MouseWheelEvent e) {
          String message;
          int notches = e.getWheelRotation();

          int mult = 27;

          if (notches < 0) {
            message = "Mouse wheel moved UP " + -notches + " notch(es)";
          } else {
            message = "Mouse wheel moved DOWN " + notches + " notch(es)" ;
          }
          if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            /*
              message += "    Scroll type: WHEEL_UNIT_SCROLL" + newline;
              message += "    Scroll amount: " + e.getScrollAmount()
                + " unit increments per notch" + newline;
              message += "    Units to scroll: " + e.getUnitsToScroll()
                + " unit increments" + newline;
              message += "    Vertical unit increment: "
                + sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getUnitIncrement(1)
                + " pixels" + newline;
            */

            if (notches < 0) {
              sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() - (e.getScrollAmount() * mult));
              scrollnow = 0;
              wheelIsScrolling = true;

            } else {
              int d = (e.getScrollAmount() * mult);
              int myvalue = 100 + d;
              if (sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + myvalue >
                  sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum()) {
                wheelIsScrolling = false;
                scrollnow = 1;
                sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum());

              } else {
                scrollnow = 0;
                wheelIsScrolling = false;
                sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + (e.getScrollAmount() * mult));
              }
            }

          } else {
            //scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL
            /*
              message += "    Scroll type: WHEEL_BLOCK_SCROLL" + newline;
              message += "    Vertical block increment: "
                + sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getBlockIncrement(1)
                + " pixels" + newline;
            */
            scrollnow = 0;
            wheelIsScrolling = true;

            int block = sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getBlockIncrement(1) * mult;
            if (notches < 0) {
              sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() - block);

            } else {
              int myvalue = 100 + block;
              if (sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + myvalue >
                  sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum()) {
                wheelIsScrolling = false;
                scrollnow = 1;
              }

              sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + block);
            }
          }
        }
      });

    sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setUnitIncrement(blockInc);
    sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          if (wheelIsScrolling) {
            wheelIsScrolling = false;
            return;
          }
          // below code tells if they click the up line button and
          // i've over riden its value in blockInc and when
          // adjustment =block inc i dont scroll
          /*
            int z = sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() -
              sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum();

            int dd = consoles[consoleNumber].getScrollableBlockIncrement(consoles[consoleNumber].getVisibleRect(),
                                                                         SwingConstants.VERTICAL, -1);
            z = z+dd;
            z *= -1;

            if (z == blockInc) {
              //JFrame fri = new JFrame("z is " + z + " and dd is " + dd);
              //fri.setVisible(true);
              wheelIsScrolling = true;
              scrollnow = 0;
              return;
            }
          */

          if (scrollnow == 1 &&
              !sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValueIsAdjusting()) {
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            // end if not adjusting
          } else {
            if (sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValueIsAdjusting())
              scrollnow = 0;
            
            int d = consoles[consoleNumber].getScrollableBlockIncrement(consoles[consoleNumber].getVisibleRect(),
                                                                        SwingConstants.VERTICAL, -1);
            int myvalue = 60 + d;
            try {
              if (sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + myvalue >
                  sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum())
                scrollnow = 1;

              if (scrollnow == 1 &&
                  !sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValueIsAdjusting())
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
              // end try
            } catch (Exception e1) {}
          }// end else
        }// end  is adjustment value changed
      });// end adjustment class

    consoles[consoleNumber].setForeground(sharedVariables.ForColor);
    consoles[consoleNumber].setBackground(sharedVariables.BackColor);
    if (sharedVariables.myFont != null)
      consoles[consoleNumber].setFont(sharedVariables.myFont);

    makeHappen(consoleNumber);

    Color lc=new Color(0,0,0);
    /*
      for (int a=0; a<10; a++)
        channelTabs[a].setForeground(lc);
    */

    setActiveTabForeground(sharedVariables.openConsoleCount);
    
    for (int a=0; a<sharedVariables.maxConsoleTabs; a++)
      channelTabs[a].setOpaque(true);
    
    for (int a=0; a<sharedVariables.maxConsoleTabs; a++)
      channelTabs[a].setBackground(sharedVariables.tabBackground);

    channelTabs[sharedVariables.looking[consoleNumber]].setBackground(sharedVariables.tabImOnBackground);
  }

  void setActiveTabForeground(int i) {
    for (int a=0; a<sharedVariables.maxConsoleTabs; a++) {
      if (a == i) {
	channelTabs[a].setForeground(sharedVariables.activeTabForeground);
        channelTabs[a].setBackground(sharedVariables.tabImOnBackground);
      } else {
        channelTabs[a].setForeground(sharedVariables.passiveTabForeground);
      }
    }
  }

  void dispatchCommand(String myurl) {

    String mycommand = "";
    mycommand = myurl;
    //.substring(1, myurl.length()-1);
    // need to figure out why this is -2 not -1, maybe i include the
    // end space which adds a charaacter here when i cut it
    mycommand = mycommand + "\n";

    myoutput output = new myoutput();
    if (sharedVariables.myServer.equals("ICC") &&
        sharedVariables.myname.length() > 0)
      output.data = "`c" + sharedVariables.looking[consoleNumber] + "`" + mycommand;
    else
      output.data = mycommand;
    
    output.consoleNumber = consoleNumber;
    queue.add(output);

    try {
      StyledDocument doc =
        sharedVariables.mydocs[sharedVariables.looking[consoleNumber]];

      doc.insertString(doc.getEndPosition().getOffset(), mycommand, null);


      for (int a=0; a<sharedVariables.maxConsoleTabs; a++)
	if (sharedVariables.looking[consoleNumber] == sharedVariables.looking[a])
          consoles[a].setStyledDocument(doc);

    } catch (Exception E) {}
  }

  public void componentResized(ComponentEvent e) {
    //updateSize();
  }
  
  public void componentHidden(ComponentEvent e) {

  }
  
  public void componentShown(ComponentEvent e) {

  }
  
  public void componentMoved(ComponentEvent e) {
    //updateSize();
  }

  /*
  public void mousePressed(MouseEvent e) {
  if (e.isPopupTrigger())
    menu.show(e.getComponent(),e.getX(),e.getY());
  }
  
  public void mouseEntered(MouseEvent me) {}
  
  public void mouseReleased(MouseEvent me) {
    if (me.isPopupTrigger())
      menu.show(me.getComponent(),me.getX(),me.getY());
  }

  public void mouseExited(MouseEvent me) {}
  public void mouseClicked(MouseEvent me) {

  }
  */

  int scrollnow;
  boolean wheelIsScrolling;

  String myglobalinput;
  JButton scrollbutton;


  void setupMenu(final String handle) {

    menu3 = new JPopupMenu("Popup");

    JMenuItem[] items;
    if (sharedVariables.rightClickMenu.size() > 0) {
      int removal = 0;
      if (sharedVariables.looking[consoleNumber] == 0)
	removal = 1;

      items = new JMenuItem[sharedVariables.rightClickMenu.size()-removal];

      for (int m=0; m<sharedVariables.rightClickMenu.size()-removal; m++) {

	final int mfinal = m;
        items[m] = new JMenuItem("" + sharedVariables.rightClickMenu.get(m) +
                                 " " + handle);
        
        items[m].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              // String name =  consoles[consoleNumber].getSelectedText();
              String name = handle;
              if (sharedVariables.rightClickMenu.get(mfinal).equals("Tell")) {
                if (sharedVariables.looking[consoleNumber] != 0)
                  addNameToCombo(name);

              } else if (sharedVariables.rightClickMenu.get(mfinal).equals("Hyperlink")) {
                sharedVariables.openUrl(name);

              } else if (sharedVariables.rightClickMenu.get(mfinal).equals("Google")) {
                sharedVariables.openUrl("http://www.google.com/search?q=" + name);

              } else if (sharedVariables.rightClickMenu.get(mfinal).equals("Channel Notify")) {
                JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
                customizeChannelNotifyDialog frame =
                  new customizeChannelNotifyDialog((JFrame) myself.myframe, false,
                                                   sharedVariables, handle);
                frame.setVisible(true);

              } else if (sharedVariables.rightClickMenu.get(mfinal).equals("Direct tells of")) {
                JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
                boolean soundof = true;

                for (int i=0; i<sharedVariables.toldTabNames.size(); i++) {
                  if (sharedVariables.toldTabNames.get(i).name.equals(handle))
                    soundof = sharedVariables.toldTabNames.get(i).sound;
                }

                tellMasterDialog frame =
                  new tellMasterDialog((JFrame) myself.myframe, false,
                                       sharedVariables, handle, soundof);

                /*
                boolean found = false;
                for (int i=0; i< sharedVariables.toldTabNames.size(); i++) {
                  if (sharedVariables.toldTabNames.get(i).name.equals(handle)) {
                    sharedVariables.toldTabNames.get(i).tab =
                      sharedVariables.looking[consoleNumber];

                    found = true;
                    break;
                  }
                }// end for

                if(!found) {
                  told him = new told();
                  him.name = handle;
                  him.tab = sharedVariables.looking[consoleNumber];
                  sharedVariables.toldTabNames.add(him);
                }
                */
              } else {
          	doCommand(sharedVariables.rightClickMenu.get(mfinal) + " " + name + "\n");
              }
            }
          });
        menu3.add(items[m]);

        if (m == 3 || m == 7 || m == 9 || (removal==0 && m==12))
          menu3.addSeparator();

        if (m < sharedVariables.rightClickMenu.size()) {
          String menuEntry = sharedVariables.rightClickMenu.get(m);
          if (menuEntry.equals("Stored")) {// now add edit list sub menu
            JMenu LMenu = new JMenu("Edit List");
            sharedVariables.setUpListMenu(LMenu, handle, queue, "`c" + sharedVariables.looking[consoleNumber] + "`");
            menu3.addSeparator();
            menu3.add(LMenu);
            menu3.addSeparator();
          }
        }
      }// end for
    }// end if any items

    menu3.addSeparator();
    JMenuItem item12 = new JMenuItem("Copy");
    item12.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          consoles[consoleNumber].copy();
          giveFocus();
        }
      });
    menu3.add(item12);

    JMenuItem item13 = new JMenuItem("Copy&Paste");
    item13.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          consoles[consoleNumber].copy();
          overall.Input.paste();
          if (sharedVariables.operatingSystem.equals("mac"))
            giveFocusTell();
          else
            giveFocus();
        }
      });
    menu3.add(item13);

    add(menu3);
  }// end menu setup

  void doCommand(String mycommand) {
    myoutput output = new myoutput();
    output.data = "`c" + sharedVariables.looking[consoleNumber] +
      "`" + mycommand;
    
    if (!sharedVariables.myServer.equals("ICC"))
      output.data = mycommand;
    
    output.consoleNumber = sharedVariables.looking[consoleNumber];
    queue.add(output);
    giveFocus();
  }

  void doToolBarCommand(int n) {
    toolbarCommands commander = new toolbarCommands(myboards);
    commander.dispatchCommand(n, sharedVariables.looking[consoleNumber],
                              false, sharedVariables,  queue);
    String mes = sharedVariables.userButtonCommands[n] + "\n";
    StyledDocument doc =
      sharedVariables.mydocs[sharedVariables.looking[consoleNumber]];

    Style styleQ = doc.addStyle(null, null);
    
    StyleConstants.setForeground(styleQ, sharedVariables.typedColor);
    //StyleConstants.setUnderline(attrs, true);
    SimpleAttributeSet attrs = new SimpleAttributeSet();

    StyleConstants.setItalic(attrs, true);
    StyleConstants.setForeground(attrs, sharedVariables.typedColor);
    try {
      doc.insertString(doc.getEndPosition().getOffset(), mes, attrs);
    } catch (Exception mydoc) {}
  }

  void updateComboBox(int n) {
    //int cindex = sharedVariables.console[Integer.parseInt(dg.getArg(1))];
    //JFrame aaframe = new JFrame();
    //aaframe.setSize(200,200);
    //aaframe.setTitle(comboMemory[n] + " is combo memory, in update combo box and n is " + n);
    //aaframe.setVisible(true);
    int count = 0;
    int foundIndex = -1;
    int a = 0;

    // first loop is to check items we would add against combo memory
    // BEFORE we remove the items triggering updates on combo memory
    for (a=0; a<400; a++) {
      if (sharedVariables.console[sharedVariables.looking[consoleNumber]][a] == 1 &&
          sharedVariables.looking[consoleNumber] != 0) {
        count++;
        String aItem = "Tell " + a + " ";

        if (aItem.equals(comboMemory[n])) {
          foundIndex = count;
          // the idea is we want the index that we added the item that
          // should be selected ( its in comboMemory)
        }
      }
    }

    // now check names
    for (a=0; a<sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].size(); a++) {
      count++;
      String aItem = "Tell " +
        sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].get(a) + " ";

      if (aItem.equals(comboMemory[n])) {
        foundIndex = count;
        // the idea is we want the index that we added the item that
        // should be selected ( its in comboMemory)
      }
    }

    prefixHandler.removeAllItems();
    prefixHandler.addItem(">");
    for (a=0; a<400; a++) {
      if (sharedVariables.console[sharedVariables.looking[consoleNumber]][a] == 1 &&
          sharedVariables.looking[consoleNumber] != 0) {
        String aItem = "Tell " + a + " ";
        prefixHandler.addItem(aItem);
      }
    }

    // now add back names
    for (a=0; a<sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].size(); a++) {
      String aItem = "Tell " +
        sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].get(a) + " ";

      prefixHandler.addItem(aItem);
    }

    try {
      if (foundIndex > -1)
        prefixHandler.setSelectedIndex(foundIndex);
    } catch (Exception badcomboupdate) {}
  }

  void updateTabChooserCombo() {
    int oldNumber = sharedVariables.looking[consoleNumber];
    tabChooser.removeAllItems();
    for (int ab=0; ab<sharedVariables.maxConsoleTabs; ab++) {
      if (!sharedVariables.consoleTabCustomTitles[ab].equals(""))
        tabChooser.addItem(sharedVariables.consoleTabCustomTitles[ab]);
      else
        tabChooser.addItem(sharedVariables.consoleTabTitles[ab]);
    }

    tabChooser.setSelectedIndex(oldNumber);
    //makeHappen(sharedVariables.looking[consoleNumber]);
  }
  
  void addNameToCombo(String name) {
    try {
      for (int z=0; z<sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].size(); z++)
        if (sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].get(z).equals(name))
          return;

      String aItem = "Tell " + name + " ";
      prefixHandler.addItem(aItem);
      sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].add(name);
      prefixHandler.setSelectedIndex(prefixHandler.getItemCount()-1);

    } catch (Exception d) {}
  }

  /************** jinternal frame listener ******************************/
  void setBoardSize() {
    sharedVariables.myConsoleSizes[consoleNumber].point0 = getLocation();
    //set_string = set_string + "" + point0.x + " " + point0.y + " ";
    sharedVariables.myConsoleSizes[consoleNumber].con0x = getWidth();
    sharedVariables.myConsoleSizes[consoleNumber].con0y = getHeight();
    //set_string = set_string + "" + con0x + " " + con0y + " ";
  }

  public void internalFrameClosing(InternalFrameEvent e) {
    if (isVisible() && !isMaximum() && !isIcon()) {
      setBoardSize();
    }
  }

  public void internalFrameClosed(InternalFrameEvent e) {

  }

  public void internalFrameOpened(InternalFrameEvent e) {

  }

  public void internalFrameIconified(InternalFrameEvent e) {

  }

  public void internalFrameDeiconified(InternalFrameEvent e) {
    if (isVisible() && !isMaximum() && !isIcon()) {
      setBoardSize();
    }
  }

  public void internalFrameActivated(final InternalFrameEvent e) {
    // System.out.println("fame activate");
    if (isVisible() && !isMaximum() && !isIcon()) {
      setBoardSize();
    }

    giveFocus();
  }

  public void internalFrameDeactivated(InternalFrameEvent e) {
    overall.Input.setFocusable(false);
  }

  void giveFocus() {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
          public void run() {
          try {
            //JComponent comp = DataViewer.getSubcomponentByName(e.getInternalFrame(),
            //                                                   SearchModel.SEARCHTEXT);

            overall.Input.setFocusable(true);
            overall.Input.setRequestFocusEnabled(true);
            //Input.requestFocus();
            overall.Input.requestFocusInWindow();
            //if (sharedVariables.operatingSystem.equals("mac")) {
            //  overall.Input.setCaretPosition(overall.Input.getDocument().getLength()-1);
            //}
          } catch (Exception e1) {
            //ignore
          }
        }
      });
  }
  
  void giveFocusTell() {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
          public void run() {
          try {
            //JComponent comp = DataViewer.getSubcomponentByName(e.getInternalFrame(),
            //                                                   SearchModel.SEARCHTEXT);

            overall.Input.setFocusable(true);
            overall.Input.setRequestFocusEnabled(true);
            //Input.requestFocus();
            overall.Input.requestFocusInWindow();
            if (sharedVariables.operatingSystem.equals("mac")) {
              String current = overall.Input.getText();
              if (!current.equals("") &&
                  current.charAt(current.length() - 1) != ' ')
                overall.Input.setText(current + " ");

              if (!current.equals(""))
                overall.Input.setCaretPosition(overall.Input.getDocument().getLength()-1);
            }
          } catch (Exception e1) {
            //ignore
          }
        }
      });
  }

  int getActiveGame() {
    for (int d=0; d<sharedVariables.maxGameTabs; d++) {
      if (myboards[d] == null)
        break;
      if (myboards[d].isVisible()) {
        return d;
      }
    }
    return -1;
  }

  void writeToConsole(String mes, Color col, boolean italic) {
    try {
      StyledDocument doc =
        sharedVariables.mydocs[sharedVariables.looking[consoleNumber]];

      Style styleQ = doc.addStyle(null, null);
      
      StyleConstants.setForeground(styleQ, col);
      //StyleConstants.setUnderline(attrs, true);
      SimpleAttributeSet attrs = new SimpleAttributeSet();

      if (italic)
        StyleConstants.setItalic(attrs, true);

      StyleConstants.setForeground(attrs, col);
      int SUBFRAME_CONSOLES = 0;
      int maxLinks = 75;
      myoutput printOut = new myoutput();
      printOut.processLink(doc, mes, col, sharedVariables.looking[consoleNumber],
                           maxLinks, SUBFRAME_CONSOLES, attrs, null);
      queue.add(printOut);
      //doc.insertString(doc.getEndPosition().getOffset(), mes, attrs);
      
      //for (int aa=0; aa<sharedVariables.maxConsoleTabs; aa++)
      //  if (sharedVariables.looking[consoleNumber]==sharedVariables.looking[aa])
      //    consoles[aa].setStyledDocument(doc);

    } catch (Exception E) {}
  }// end write to console

  /****************************************************************************************/
  class subPanel extends JPanel {
    JTextField Input;
    arrowManagement arrowManager;

    subPanel() {
      Input = new JTextField();
      Input.setFont(sharedVariables.inputFont);
      arrowManager = new arrowManagement();

      //Input.addActionListener (this);
      Input.addKeyListener(new KeyListener() {
          public void keyPressed(KeyEvent e) {
            int a = e.getKeyCode();
            int gme = e.getModifiersEx();

            //if (a == 33)
            if (a == KeyEvent.VK_PAGE_UP)
              scrollnow = 0;
            
            String mytext = (String) prefixHandler.getSelectedItem();
            if (mytext != null) {
              if (mytext.equals(">") || Input.getText().startsWith("/"))
                overall.Input.setForeground(sharedVariables.inputCommandColor);
              else
                overall.Input.setForeground(sharedVariables.inputChatColor);
            }

            /*
            //if (e.getModifiersEx() == 128 || e.getModifiersEx() == 192)
            if ((gme & InputEvent.CTRL_DOWN_MASK) != 0) {
              //int moveKeyType = gme;
              //if (a == 61) {
              if (a == KeyEvent.VK_EQUALS) {
                int games = getActiveGame();
                if (games > -1) {
                  int loc = sharedVariables.moveSliders[games].getValue();
                  int max = sharedVariables.moveSliders[games].getMaximum();
                  //if (loc < max || moveKeyType == 192) {
                  if (loc < max || (gme & InputEvent.SHIFT_DOWN_MASK) != 0) {
                    //if (moveKeyType == 192)
                    if ((gme & InputEvent.SHIFT_DOWN_MASK) != 0)
                      loc = max;
                    else
                      loc++;

                    sharedVariables.moveSliders[games].setValue(loc);
                    myboards[games].mycontrolspanel.adjustMoveList();
                    myboards[games].repaint();
                  }
                  giveFocus();
                }

                return;
              }

              //if (a == 45) {
              if (a == KeyEvent.VK_MINUS) {
                int games = getActiveGame();
                if (games > -1) {
                  int loc = sharedVariables.moveSliders[games].getValue();

                  //if (loc > 0 || moveKeyType == 192) {
                  if (loc > 0 || (gme & InputEvent.SHIFT_DOWN_MASK) != 0) {
                    //if (moveKeyType == 192)
                    if ((gme & InputEvent.SHIFT_DOWN_MASK) != 0)
                      loc = 0;
                    else
                      loc--;

                    sharedVariables.moveSliders[games].setValue(loc);
                    myboards[games].mycontrolspanel.adjustMoveList();
                    myboards[games].repaint();
                  }
                  giveFocus();
                }

                return;
              }
            }// if control or control + shift
            */

            //if (e.getModifiersEx() == 128)// ctrl + t
            if (gme == InputEvent.CTRL_DOWN_MASK) {

              //if (a == 70) {
              if (a == KeyEvent.VK_F) {

                textSearcher ts = new textSearcher();
                ts.find(Input.getText(), consoles[consoleNumber]);
              }

              //if (a == 71) {
              if (a == KeyEvent.VK_G) {
                String myurl = Input.getText();
                Input.setText("");

                myurl = myurl.trim();
                myurl = myurl.replace(" ", "+");

                sharedVariables.openUrl("http://www.google.com/search?q=" + myurl);
              }
              
              //if (a == 72) {
              if (a == KeyEvent.VK_H) {
                // bring history to front not needed now that its a top window
                if (sharedVariables.myGameList != null &&
                    sharedVariables.myGameList.isVisible()) {
                  try {
                    sharedVariables.myGameList.setSelected(true);
                  } catch (Exception gamedui) {}
                }
              }
              
              /*
              //if (a == 77) {
              if (a == KeyEvent.VK_M) {
                sharedVariables.tellsToTab = true;
                sharedVariables.tellTab = sharedVariables.looking[consoleNumber];
                myoutput data = new myoutput();
                data.repaintTabBorders = 1;
                queue.add(data);

                return;
              }

              //if (a == 90) {
              if (a == KeyEvent.VK_Z) {
                switchConsoleWindows();
                return;
              }

              //if (a == 84) {
              if (a == KeyEvent.VK_T) {
                switchWindows();
                return;
              }

              //if (a == 49) {
              if (a == KeyEvent.VK_1) {
                doToolBarCommand(1);
                return;
              }
              //if (a == 50) {
              if (a == KeyEvent.VK_2) {
                doToolBarCommand(2);
                return;
              }
              //if (a == 51) {
              if (a == KeyEvent.VK_3) {
                doToolBarCommand(3);
                return;
              }
              //if (a == 52) {
              if (a == KeyEvent.VK_4) {
                doToolBarCommand(4);
                return;
              }
              //if (a == 53) {
              if (a == KeyEvent.VK_5) {
                doToolBarCommand(5);
                return;
              }
              //if (a == 54) {
              if (a == KeyEvent.VK_6) {
                doToolBarCommand(6);
                return;
              }
              //if (a == 55) {
              if (a == KeyEvent.VK_7) {
                doToolBarCommand(7);
                return;
              }
              //if (a == 56) {
              if (a == KeyEvent.VK_8) {
                doToolBarCommand(8);
                return;
              }
              //if (a == 57) {
              if (a == KeyEvent.VK_9) {
                doToolBarCommand(9);
                return;
              }
              //if (a == 48) {
              if (a == KeyEvent.VK_0) {
                doToolBarCommand(0);
                return;
              }

              //if (a == 68) {
              if (a == KeyEvent.VK_D) {
                String mess2 = sharedVariables.getChannelNotifyOnline();
                mess2 += sharedVariables.getConnectNotifyOnline();
                writeToConsole(mess2, sharedVariables.responseColor, false);
                // false for not italic
                return;
              }
              */
            }

            /*
            //if (e.getModifiersEx() == 512) {
            if (gme == InputEvent.ALT_DOWN_MASK &&
              //if (a == 84) {
                a == KeyEvent.VK_T) {
              changeTellTab(true); // true for forward
            }

            //if (e.getModifiersEx() == 576) {
            if (gme == InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK &&
              //if (a == 84) {
                a == KeyEvent.VK_T) {
              changeTellTab(false);// false for backward
            }
            */
            if ((gme & InputEvent.ALT_DOWN_MASK) != 0 &&
              //if (a == 84) {
                a == KeyEvent.VK_T) {
              changeTellTab((gme & InputEvent.SHIFT_DOWN_MASK) == 0);
              // true for forward
            }
            
            
            //if (e.getModifiersEx() == 512) {
            if (gme == InputEvent.ALT_DOWN_MASK) {
              //if (a == 192) {
              if (a == KeyEvent.VK_BACK_QUOTE) {
                /*
                if (sharedVariables.activitiesNeverOpen == true) {
                  sharedVariables.activitiesNeverOpen = false;
                  String mess2 = "Activities window enabled to open at login if " +
                    "you leave it open on close and save settings. " +
                    "Do 'save settings' to save this change.\n";
                  writeToConsole(mess2, sharedVariables.responseColor, false);
                } else {
                  sharedVariables.activitiesNeverOpen = true;
                  String mess2 = "Activities window disabled to open at login if " +
                    "you leave it open on close and save settings. " +
                    "Do 'save settings' to save this change.\n";
                  writeToConsole(mess2, sharedVariables.responseColor, false);
                }
                */
                sharedVariables.activitiesNeverOpen =
                  !sharedVariables.activitiesNeverOpen;
                String mess2 = "Activities window " +
                  (sharedVariables.activitiesNeverOpen ? "disabled" : "enabled") +
                  " to open at login if you leave it open on close and save settings." +
                  "  Do 'save settings' to save this change.\n";
                writeToConsole(mess2, sharedVariables.responseColor, false);
                return;
              }
              /*
              //if (a == 82) {// switch to right board tab
              if (a == KeyEvent.VK_R) {
                int games = getActiveGame();
                if (games > -1) {
                  myboards[games].myconsolepanel.makehappen(myboards[games].myconsolepanel.getNextGame(true));
                  giveFocus();
                }
                return;
              }
              
              //if (a == 76) {// switch to left board tab
              if (a == KeyEvent.VK_L) {
                int games = getActiveGame();
                if (games > -1) {
                  myboards[games].myconsolepanel.makehappen(myboards[games].myconsolepanel.getNextGame(false));
                  giveFocus();
                }
                return;
              }
              
              //if (a == 66) {// bring up board
              if (a == KeyEvent.VK_B) {

                int games = getActiveGame();
                
                if (games > -1) {
                  myboards[games].setSelected(true);
                  giveFocus();
                }
                return;
              }
              
              //if (a == 88) {// x close active game tab (first board)
              if (a == KeyEvent.VK_X) {
                int games = getActiveGame();
                
                if (games > -1) {
                  myoutput data = new myoutput();
                  data.closetab =
                    myboards[games].myconsolepanel.getPhysicalTab(myboards[games].gameData.LookingAt);

                  data.focusConsole = consoleNumber;

                  queue.add(data);
                }
                return;
              }
              */
            }   // end this alt section

            //if (e.getModifiersEx() == 512) {
            if (gme == InputEvent.ALT_DOWN_MASK) {
              /*
              //if (a == 49) {
              if (a == KeyEvent.VK_1) {
                //sharedVariables.looking[consoleNumber]=0;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(0);
                makeHappen(0);
              }
              //if (a == 50) {
              if (a == KeyEvent.VK_2) {
                //sharedVariables.looking[consoleNumber]=1;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(1);
                makeHappen(1);
              }
              //if (a == 51) {
              if (a == KeyEvent.VK_3) {
                //sharedVariables.looking[consoleNumber]=2;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(2);
                makeHappen(2);
              }
              //if (a == 52) {
              if (a == KeyEvent.VK_4) {
                //sharedVariables.looking[consoleNumber]=3;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(3);
                makeHappen(3);
              }
              //if (a == 53) {
              if (a == KeyEvent.VK_5) {
                //sharedVariables.looking[consoleNumber]=4;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(4);
                makeHappen(4);
              }
              //if (a == 54) {
              if (a == KeyEvent.VK_6) {
                //sharedVariables.looking[consoleNumber]=5;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(5);
                makeHappen(5);
              }
              //if (a == 55) {
              if (a == KeyEvent.VK_7) {
                //sharedVariables.looking[consoleNumber]=6;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(6);
                makeHappen(6);
              }
              //if (a == 56) {
              if (a == KeyEvent.VK_8) {
                //sharedVariables.looking[consoleNumber]=7;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(7);
                makeHappen(7);
              }
              //if (a == 57) {
              if (a == KeyEvent.VK_9) {
                //sharedVariables.looking[consoleNumber]=8;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(8);
                makeHappen(8);
              }

              //if (a == 48) {
              if (a == KeyEvent.VK_0) {
                //sharedVariables.looking[consoleNumber]=9;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(9);
                makeHappen(9);
              }
              //if (a == 45) {
              if (a == KeyEvent.VK_MINUS) {
                //sharedVariables.looking[consoleNumber]=10;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(10);
                makeHappen(10);
              }
              //if (a == 61) {
              if (a == KeyEvent.VK_EQUALS) {
                //sharedVariables.looking[consoleNumber]=11;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3)
                  tabChooser.setSelectedIndex(11);
                makeHappen(11);
              }

              //if (a == 38) {
              if (a == KeyEvent.VK_UP) {
                int con = sharedVariables.looking[consoleNumber];

                if (sharedVariables.tabStuff[con].tabFont != null) {
                  float fontsize =
                    (float) sharedVariables.tabStuff[con].tabFont.getSize();
                  fontsize++;
                  sharedVariables.tabStuff[con].tabFont =
                    sharedVariables.tabStuff[con].tabFont.deriveFont(fontsize);
                  makeHappen(con);
                  return;
		} else {
                  float fontsize = (float) sharedVariables.myFont.getSize();
                  fontsize++;
                  sharedVariables.myFont =
                    sharedVariables.myFont.deriveFont(fontsize);
                  makeHappen(con);
                  return;
		}
              }
              
              //if (a == 40) {
              if (a == KeyEvent.VK_DOWN) {

                int con = sharedVariables.looking[consoleNumber];

                if (sharedVariables.tabStuff[con].tabFont != null) {
                  float fontsize =
                    (float) sharedVariables.tabStuff[con].tabFont.getSize();
                  fontsize--;
                  sharedVariables.tabStuff[con].tabFont =
                    sharedVariables.tabStuff[con].tabFont.deriveFont(fontsize);
                  makeHappen(con);
                  return;
		} else {
                  float fontsize = (float) sharedVariables.myFont.getSize();
                  fontsize--;
                  sharedVariables.myFont=sharedVariables.myFont.deriveFont(fontsize);
                  makeHappen(con);
                  return;
                }
              }
              */
            }
            
            //if (e.getModifiersEx() == 512) {
            if (gme == InputEvent.ALT_DOWN_MASK) {
              /*
              //if (a == 39) {
              if (a == KeyEvent.VK_RIGHT) {
                int con = sharedVariables.looking[consoleNumber] + 1;
                if (con == sharedVariables.maxConsoleTabs)
                  con = 0;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3) {
                  tabChooser.setSelectedIndex(con);
                }
                makeHappen(con);
              }
              
              //if (a == 37) {
              if (a == KeyEvent.VK_LEFT) {
                int con = sharedVariables.looking[consoleNumber] - 1;
                if (con == -1)
                  con = sharedVariables.maxConsoleTabs - 1;
                if (sharedVariables.consolesTabLayout[consoleNumber] == 3) {
                  tabChooser.setSelectedIndex(con);
                }
                makeHappen(con);
              }
              */
            }

            //if (a == 27) {
            if (a == KeyEvent.VK_ESCAPE) {
              Input.setText("");
            }

            //if (a == 10) {
            if (a == KeyEvent.VK_ENTER) {
              lastcommand = Input.getText();
              arrowManager.add(lastcommand);

              String mes = lastcommand + "\n";

              int index = prefixHandler.getSelectedIndex();
              String pre = "";
              pre = prefixHandler.getItemAt(index).toString();

              if (index > 0 && !Input.getText().startsWith("/"))
                // we don't use tell channel prefix if starts with /
                mes = pre + mes;

              //if (e.getModifiersEx() == 128) {
              if (gme == InputEvent.CTRL_DOWN_MASK) {
                int changeTo =
                  ctrlEnterSwitch(sharedVariables.looking[consoleNumber]);
                if (changeTo != -1)
                  makeHappen(changeTo);
              }
            
              if (sharedVariables.tabStuff[sharedVariables.looking[consoleNumber]].typed) {
                writeToConsole(mes, sharedVariables.typedColor , true);
                // true for italic
              }
            
              myoutput output = new myoutput();
              if (sharedVariables.myServer.equals("ICC") &&
                  sharedVariables.myname.length() > 0)
                output.data = "`c" + sharedVariables.looking[consoleNumber] +
                  "`" + mes;
              // having a name means level 1 is on if on icc and this
              // `phrase`mess will be used to direct output back to this
              // console
              else
                output.data = mes;

              output.consoleNumber = consoleNumber;
              queue.add(output);
              Input.setText("");
            }// end enter
          
            /*
            //if ((a == 120 || a == 119)  && e.getModifiersEx() != 64) {
            if ((a == KeyEvent.VK_F9 || a == KeyEvent.VK_F8) &&
                (gme & InputEvent.SHIFT_DOWN_MASK) == 0) {
              String s = Input.getText();
              String person;
              if (s.length() == 0)
                person = sharedVariables.F9Manager.getName(true);
              else
                person = sharedVariables.F9Manager.getName(false);
              
              if (person.length() > 0) {
                Input.setText("/Tell " + person + "! ");
                Input.setForeground(sharedVariables.inputCommandColor);
              }
            }

            //if ((a == 120 || a == 119) && e.getModifiersEx() == 64) {
            if ((a == KeyEvent.VK_F9 || a == KeyEvent.VK_F8) &&
                (gme & InputEvent.SHIFT_DOWN_MASK) != 0) { 
              String s = Input.getText();
              String person;
              if (s.length() == 0)
                person = sharedVariables.F9Manager.getNameReverse(true);
              else
                person = sharedVariables.F9Manager.getNameReverse(false);

              if (person.length() > 0) {
                Input.setText("/Tell " + person + "! ");
                Input.setForeground(sharedVariables.inputCommandColor);
              }
            }
            */
            if (a == KeyEvent.VK_F9 || a == KeyEvent.VK_F8) {
              String s = Input.getText();
              String person;
              boolean resetcycle = !s.startsWith("/Tell ");
              person =
                ((gme & InputEvent.SHIFT_DOWN_MASK) == 0 ?
                 sharedVariables.F9Manager.getName(resetcycle) :
                 sharedVariables.F9Manager.getNameReverse(resetcycle));

              if (person.length() > 0) {
                Input.setText("/Tell " + person + "! ");
                Input.setForeground(sharedVariables.inputCommandColor);
              }
            }

            //if (a == 40) {
            if (a == KeyEvent.VK_DOWN) {
              arrowManager.down();
            }
              
            //if (e.getModifiersEx() == 128  && a == 38 && !Input.getText().equals("")) {
            if ((gme & InputEvent.CTRL_DOWN_MASK) != 0 &&
                a == KeyEvent.VK_UP && !Input.getText().equals("")) {
              arrowManager.add(Input.getText());
              Input.setText("");

              //} else if (a == 38) {
            } else if (a == KeyEvent.VK_UP) {
              arrowManager.up();
              //if (lastcommand.length() > 0)
              //  Input.setText(lastcommand);
            }
            // code here
          }

          public void keyTyped(KeyEvent e) {

          }

          /** Handle the key-released event from the text field. */
          public void keyReleased(KeyEvent e) {

          }
        });

      Input.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
              menu2.show(e.getComponent(),e.getX(),e.getY());
          }
          
          public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
              menu2.show(e.getComponent(),e.getX(),e.getY());
          }

          public void mouseEntered(MouseEvent me) {}
          public void mouseExited(MouseEvent me) {}
          public void mouseClicked(MouseEvent me) {

          }
        });


      /*
      if (sharedVariables.consoleLayout==1)
	setMyLayout1();
      else if (sharedVariables.consoleLayout==3)
        setMyLayout3();
      else
	setMyLayout2();
      */
      recreate(sharedVariables.consolesTabLayout[consoleNumber]);
    }
    /*
    // Andrey says: copied outside the class
    int getActiveGame() {
      int games = -1;
      for (int d=0; d<sharedVariables.maxGameTabs; d++) {
        if (myboards[d] == null)
          break;
        if (myboards[d].isVisible()) {
          games = d;
          break;
        }
      }
      return games;
    }
    
    void writeToConsole(String mes, Color col, boolean italic) {
      try {
        StyledDocument doc =
          sharedVariables.mydocs[sharedVariables.looking[consoleNumber]];

        Style styleQ = doc.addStyle(null, null);

        StyleConstants.setForeground(styleQ, col);
        //StyleConstants.setUnderline(attrs, true);
        SimpleAttributeSet attrs = new SimpleAttributeSet();

        if (italic)
          StyleConstants.setItalic(attrs, true);

        StyleConstants.setForeground(attrs, col);
        int SUBFRAME_CONSOLES = 0;
        int maxLinks = 75;
        myoutput printOut = new myoutput();
        printOut.processLink(doc, mes, col, sharedVariables.looking[consoleNumber],
                             maxLinks, SUBFRAME_CONSOLES, attrs, null);
        queue.add(printOut);
        //doc.insertString(doc.getEndPosition().getOffset(), mes, attrs);

        //for (int aa=0; aa<sharedVariables.maxConsoleTabs; aa++)
        //  if (sharedVariables.looking[consoleNumber]==sharedVariables.looking[aa])
        //    consoles[aa].setStyledDocument(doc);

      } catch (Exception E) {}
    }// end write to console
    */

    int ctrlEnterSwitch(int console) {
      int a;
      int aa;

      for (a=console+1; a<sharedVariables.maxConsoleTabs; a++) {
        boolean go = true;
	for (aa=0; aa<400; aa++) {
          if (sharedVariables.console[a][aa] == 1) {
            go = false;
          }
        }

        if (sharedVariables.tellTab == a)
          go = false;

        if (go)
          return a;
      }

      for (a=1; a<console; a++) {
        boolean go = true;
	for (aa=0; aa<400; aa++) {
          if (sharedVariables.console[a][aa] == 1) {
            go = false;
          }
        }

        if (sharedVariables.tellTab == a)
          go = false;

        if (go)
          return a;
      }

      return -1;
    }
    
    void setMyLayout1() {
      GroupLayout layout = new GroupLayout(getContentPane());
      //GroupLayout layout = new GroupLayout(this);
      //setLayout(layout);
      getContentPane().setLayout(layout);
      int inputHeight = 17;
      try {
	inputHeight = sharedVariables.inputFont.getSize();

      } catch (Exception inputing) {}

      ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
      ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

      SequentialGroup middle = layout.createSequentialGroup();
      SequentialGroup h2 = layout.createSequentialGroup();
      SequentialGroup h3 = layout.createSequentialGroup();

      //Add a scroll pane and a label to the parallel group h2
      h2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber],
                      GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
      h2.addComponent(listScroller, sharedVariables.nameListSize,
                      sharedVariables.nameListSize, sharedVariables.nameListSize);
      h3.addComponent(prefixHandler, GroupLayout.DEFAULT_SIZE, 105, 105);
      h3.addComponent(Input, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);
      h3.addComponent(scrollbutton);

      for (int a=0; a<sharedVariables.maxConsoleTabs; a++) {
        middle.addComponent(channelTabs[a],GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
        middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
      }
      h3.addComponent(tellLabel);

      h3.addComponent(tellCheckbox);

      h1.addGroup(h2);
      h1.addGroup(h3);
      h1.addGroup(middle);

      //Add the group h1 to the hGroup
      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
      //Create the horizontal group
      layout.setHorizontalGroup(hGroup);

      ParallelGroup vGroup =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading

      SequentialGroup v4 = layout.createSequentialGroup();

      ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

      ParallelGroup vmiddle = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

      ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

      v2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber],
                      GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
      v2.addComponent(listScroller);
      for (int a=0; a<sharedVariables.maxConsoleTabs; a++)
        vmiddle.addComponent(channelTabs[a]);

      v1.addComponent(prefixHandler, GroupLayout.PREFERRED_SIZE,
                      GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
      v1.addComponent(scrollbutton);
      v1.addComponent(Input, inputHeight, GroupLayout.DEFAULT_SIZE,
                      GroupLayout.PREFERRED_SIZE);
      v1.addComponent(tellLabel);
      v1.addComponent(tellCheckbox);

      if (sharedVariables.consolesTabLayout[consoleNumber] == 4)
        v4.addGroup(vmiddle);

      v4.addGroup(v2);
      
      if (sharedVariables.consolesTabLayout[consoleNumber] != 4)
        v4.addGroup(vmiddle);

      v4.addGroup(v1);

      vGroup.addGroup(v4);
      //Create the vertical group
      layout.setVerticalGroup(vGroup);
      //pack();
    }
    
    void setMyLayout2() {
      GroupLayout layout = new GroupLayout(getContentPane());
      //GroupLayout layout = new GroupLayout(this);
      getContentPane().setLayout(layout);
      //setLayout(layout);
      int inputHeight = 17;
      try {
	inputHeight = sharedVariables.inputFont.getSize();

      } catch (Exception inputing) {}

      ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
      ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

      SequentialGroup middle = layout.createSequentialGroup();
      SequentialGroup middle2 = layout.createSequentialGroup();
      SequentialGroup h2 = layout.createSequentialGroup();

      SequentialGroup h3 = layout.createSequentialGroup();

      //Add a scroll pane and a label to the parallel group h2
      h2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber],
                      GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
      h2.addComponent(listScroller, sharedVariables.nameListSize,
                      sharedVariables.nameListSize, sharedVariables.nameListSize);

      h3.addComponent(prefixHandler, GroupLayout.DEFAULT_SIZE, 105, 105);
      h3.addComponent(Input, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);
      h3.addComponent(scrollbutton);

      int half = (int) sharedVariables.maxConsoleTabs/2;
      ParallelGroup[] middles = new ParallelGroup[half];

      for (int a=0; a<half; a++)
        middles[a]=layout.createParallelGroup(GroupLayout.Alignment.LEADING);

      int a;
      for (a=0; a<half; a++) {
        middles[a].addComponent(channelTabs[a*2],GroupLayout.DEFAULT_SIZE,
                                15, Short.MAX_VALUE);
        middles[a].addComponent(channelTabs[a*2+1],GroupLayout.DEFAULT_SIZE,
                                15, Short.MAX_VALUE);
        middle.addGroup(middles[a]);
        middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
      }
      //middle.addGroup(middles[a]);
      h3.addComponent(tellLabel);

      h3.addComponent(tellCheckbox);

      /*
      for (int a=half; a<sharedVariables.maxConsoleTabs; a++) {
        middle2.addComponent(channelTabs[a],GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
        middle2.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
      }
      */

      //h2.addGroup(middle2);
      h1.addGroup(h2);
      h1.addGroup(h3);
      h1.addGroup(middle);

      //Add the group h1 to the hGroup
      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
      //Create the horizontal group
      layout.setHorizontalGroup(hGroup);

      ParallelGroup vGroup =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading

      SequentialGroup v4 = layout.createSequentialGroup();

      ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

      ParallelGroup vmiddle = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
      ParallelGroup vmiddle2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

      ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

      v2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber],
                      GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
      v2.addComponent(listScroller);

      for (a=0; a<half; a++)
        vmiddle.addComponent(channelTabs[a*2]);
      //vmiddle.addComponent(tellLabel);
      for (a=0; a<half; a++)
        vmiddle2.addComponent(channelTabs[a*2+1]);

      v1.addComponent(prefixHandler, GroupLayout.PREFERRED_SIZE,
                      GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
      v1.addComponent(scrollbutton);
      v1.addComponent(Input, inputHeight, GroupLayout.DEFAULT_SIZE,
                      GroupLayout.PREFERRED_SIZE);
      v1.addComponent(tellLabel);

      v1.addComponent(tellCheckbox);

      v4.addGroup(v2);
      v4.addGroup(vmiddle);
      v4.addGroup(vmiddle2);
      v4.addGroup(v1);
      
      vGroup.addGroup(v4);
      //Create the vertical group
      layout.setVerticalGroup(vGroup);
      //pack();
    }

    void setMyLayout3() {
      GroupLayout layout = new GroupLayout(getContentPane());
      //GroupLayout layout = new GroupLayout(this);
      getContentPane().setLayout(layout);
      int inputHeight = 17;
      try {
	inputHeight = sharedVariables.inputFont.getSize();

      } catch (Exception inputing) {}

      initTabChooser();
      ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
      SequentialGroup h1 = layout.createSequentialGroup();
      ParallelGroup h4 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

      //SequentialGroup middle = layout.createSequentialGroup();
      SequentialGroup h2 = layout.createSequentialGroup();
      SequentialGroup h3 = layout.createSequentialGroup();

      //Add a scroll pane and a label to the parallel group h2
      h2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber],
                      GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);
      h2.addComponent(listScroller, sharedVariables.nameListSize,
                      sharedVariables.nameListSize, sharedVariables.nameListSize);

      h3.addComponent(prefixHandler, GroupLayout.DEFAULT_SIZE, 95, 95);

      h3.addComponent(Input, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);
      h3.addComponent(tellCheckbox,21,21,21);
      h3.addComponent(tabChooser, GroupLayout.DEFAULT_SIZE, 75, 75);
      //h3.addComponent(scrollbutton);

      /*
      for (int a=0; a<sharedVariables.maxConsoleTabs; a++) {
        middle.addComponent(channelTabs[a],GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
        middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
      }
      middle.addComponent(tellLabel);

      middle.addComponent(tellCheckbox);
      */

      h4.addGroup(h2);
      h4.addGroup(h3);
      //h2.addGroup(middle);
      h1.addGroup(h4);

      //Add the group h1 to the hGroup
      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
      //Create the horizontal group
      layout.setHorizontalGroup(hGroup);

      ParallelGroup vGroup =
        layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading

      SequentialGroup v4 = layout.createSequentialGroup();

      ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

      //ParallelGroup vmiddle = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

      ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

      v2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber],
                      GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
      v2.addComponent(listScroller);

      /*
      for (int a=0; a<sharedVariables.maxConsoleTabs; a++)
        vmiddle.addComponent(channelTabs[a]);
      vmiddle.addComponent(tellLabel);
      vmiddle.addComponent(tellCheckbox);
      */
      v1.addComponent(tellCheckbox, GroupLayout.PREFERRED_SIZE,
                      GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
      v1.addComponent(tabChooser, GroupLayout.PREFERRED_SIZE,
                      GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
      v1.addComponent(prefixHandler, GroupLayout.PREFERRED_SIZE,
                      GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
      //v1.addComponent(scrollbutton);
      v1.addComponent(Input, inputHeight, GroupLayout.DEFAULT_SIZE,
                      GroupLayout.PREFERRED_SIZE);

      v4.addGroup(v2);
      //v4.addGroup(vmiddle);
      v4.addGroup(v1);

      vGroup.addGroup(v4);
      //Create the vertical group
      layout.setVerticalGroup(vGroup);
      //pack();
    }

    void recreate(int n) {
      getContentPane().removeAll();
      if (sharedVariables.consolesNamesLayout[consoleNumber] == 0)
	listScroller.setVisible(false);

      if(n==1 || n == 4)
 	setMyLayout1();
      else if (n==2)
 	setMyLayout2();
      else
 	setMyLayout3();

      // this.add(overall);
      // this.setVisible(true);
    }

    class arrowManagement {
      //ArrayList list;
      List<String> list;
      //int head;
      int index;
      int max;

      arrowManagement() {
        //list = new ArrayList();
        list = new ArrayList<String>();
        list.add("");
        //head = 0;
        index = 0;
        max = 20;
      }

      void down() {
        int tail = list.size() - 1;
        String curtext = Input.getText();
        if (!list.contains(curtext)) {
          list.set(tail, curtext);
          index = 0;
        } else {
          index++;
          if (index > tail)
            index = 0;
        }
          //Input.setText((String)list.get(index));
        Input.setText(list.get(index));
        // if  input is empty do nothing
        // otherwise if iterator is not at top iterate and grab command
      }// end down
      
      void up() {
        // if input is empty reset iterator and return tail
        // otherwise iterate one and return item
        int tail = list.size() - 1;
        String curtext = Input.getText();
        if (!list.contains(curtext)) {
          list.set(tail, curtext);
          index = (tail == 0 ? 0 : tail - 1);
        } else {
          // index is set to 1 more than tail when at initial position
          index--;
          if (index < 0)
            index = tail;
        }
        //Input.setText((String)list.get(index));
        Input.setText(list.get(index));
      }// end up
      
      void add(String mes) {
        int tail = list.size() - 1;
        // add to queue, delete if more than 10 last of commands, reset iterator
        list.set(tail, "");
        if (mes.equals("")) return;

        if (list.remove(mes)) tail--;
        list.set(tail, mes);

        list.add("");
        if (tail >= max) {
          list.remove(0);
        }
        index = list.size() - 1;
        // add to queue, delete if more than 10 last of commands, reset iterator
      }// end add
    } // end arrow manager
  }// end panel
}// end subframe