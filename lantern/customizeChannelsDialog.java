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

/**/
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import layout.TableLayout;

public class customizeChannelsDialog extends JDialog
  implements ActionListener {

  private List<Integer> qtellchannels;

  private int tab;
  private channels sVars;
  private subframe[] subs;

  private int width = 450;
  private int height;

  private JTextField name;
  private JCheckBox setname;
  private JCheckBox gamenotify;
  private JCheckBox shouts;
  private JCheckBox mainshouts;
  private JCheckBox sshouts;

  private JButton addbutton;

  private JButton save;
  private JButton cancel;
  private JButton apply;
  private JPanel buttons;

  private JLabel tells;
  private JLabel qtells;
  private JLabel maintab;
  private JLabel othertabs;
  
  private JList list;
  private DefaultListModel listmodel;
  private JScrollPane listpane;
  

  private List<JCheckBox> showtab;
  private List<JCheckBox> showqt;
  private List<JCheckBox> showmain;
  private List<Integer> tabchan;

  private TableLayout layout;

  public customizeChannelsDialog(JFrame frame, boolean mybool, int tab,
                                 channels sVars, subframe[] subs) {
    super(frame, "Customize Tab " + tab, mybool);

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    this.tab = tab;
    this.sVars = sVars;
    this.subs = subs;

    qtellchannels = qtellchannels();
    
    name = new JTextField(30);
    setname = new JCheckBox("Name tab");
    setname.setActionCommand("name");
    setname.addActionListener(this);
    
    String namestring = sVars.consoleTabCustomTitles[tab];
    if (!namestring.equals("")) {
      name.setText(namestring);
      setname.setSelected(true);
    } else {
      name.setEnabled(false);
    }

    gamenotify = new JCheckBox("Game notifications");
    if (sVars.gameNotifyConsole == tab)
      gamenotify.setSelected(true);
    
    mainshouts = new JCheckBox("(in main tab)");
    if (sVars.shoutsAlso)
      mainshouts.setSelected(true);

    shouts = new JCheckBox("Shouts");
    if (sVars.shoutRouter.shoutsConsole == tab)
      shouts.setSelected(true);
    else
      mainshouts.setEnabled(false);

    shouts.setActionCommand("shouts");
    shouts.addActionListener(this);
    
    sshouts = new JCheckBox("S-shouts");
    if (sVars.shoutRouter.sshoutsConsole == tab)
      sshouts.setSelected(true);

    listmodel = myChannels();
    list = new JList(listmodel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listpane = new JScrollPane(list);
    // always going to have at least ch. 1 in the list
    list.setSelectedIndex(0);
    
    addbutton = new JButton("Add");
    addbutton.setActionCommand("add");
    addbutton.addActionListener(this);

    save = new JButton("Save");
    save.setActionCommand("save");
    save.addActionListener(this);
    
    cancel = new JButton("Cancel");
    cancel.setActionCommand("cancel");
    cancel.addActionListener(this);

    apply = new JButton("Apply");
    apply.setActionCommand("apply");
    apply.addActionListener(this);

    buttons = new JPanel();
    buttons.add(save);
    buttons.add(cancel);
    buttons.add(apply);

    tells = new JLabel("This tab");
    qtells = new JLabel("(qtells)");
    maintab = new JLabel("Main tab");
    othertabs = new JLabel("Other tabs");

    buildLayout();

    setVisible(true);
  }

  private List<Integer> qtellchannels() {
    List<Integer> qtc = new ArrayList<Integer>();
    qtc.add(3);
    qtc.add(4);
    qtc.add(46);
    qtc.add(47);
    qtc.add(49);
    qtc.add(123);
    qtc.add(147);
    
    for (int i=221; i<229; i++)
      qtc.add(i);

    qtc.add(230);
    qtc.add(232);
    qtc.add(280);

    return qtc;
  }
  
  private void buildLayout() {
    height = 300;
    
    int border = 10;
    int space = 5;
    int ht = 20;
    double tf = TableLayout.FILL;

    double[][] size = {{border, 100, space, 60, space, 60, space,
                        60, space, tf, border},
                       {border, ht, space, ht, space, ht, space, ht, 55,
                        space, ht, space, tf, 30, border}};

    layout = new TableLayout(size);
    setLayout(layout);

    add(name, "3, 1, 7, 1");
    add(setname, "9, 1, l, f");
    add(listpane, "1, 1, 1, 8");
    add(gamenotify, "3, 3, 7, 3");
    add(shouts, "3, 5, 5, 5");
    add(mainshouts, "7, 5, 9, 5");
    add(sshouts, "3, 7, 5, 7");
    add(addbutton, "1, 10");
    add(tells, "3, 10, c, f");
    add(qtells, "5, 10, c, f");
    add(maintab, "7, 10, c, f");
    add(othertabs, "9, 10, l, f");
    add(buttons, "1, 13, 9, 13");

    showtab = new ArrayList<JCheckBox>();
    showqt = new ArrayList<JCheckBox>();
    showmain = new ArrayList<JCheckBox>();
    tabchan = new ArrayList<Integer>();

    setSize(width, height);

    getChannels();
  }
  
  private void getChannels() {
    // add all the channels in this tab
    for (int i=399; i>=0; i--) {
      if (sVars.console[tab][i] == 1) {
        addChannel(i, false);
      }
    }
  }

  private void addChannel(int chan, boolean containsCheck) {
    if (containsCheck) {
      // we can do more, but this will suffice for now
      if (tabchan.contains(chan))
        return;
    }

    // add the space for the layout
    layout.insertRow(12, 20);

    JCheckBox ctab = new JCheckBox();
    JCheckBox cqtell = new JCheckBox();

    if (qtellchannels.contains(chan)) {
      int qtellval = sVars.qtellController[tab][chan];
      ctab.setSelected((qtellval != 2));
      cqtell.setSelected((qtellval != 1));
    } else {
      ctab.setSelected(true);
      cqtell.setEnabled(false);
    }
    
    JCheckBox cmain = new JCheckBox();
    if (sVars.mainAlso[chan])
      cmain.setSelected(true);

    String others = "";
    boolean first = true;
    for (int i=1; i<sVars.maxConsoleTabs; i++) {
      if (sVars.console[i][chan] == 1) {
        if (i != tab) {
          if (first) first = false;
          else others += ", ";

          others += i;
        }
      }
    }
    
    if (others.equals("")) others = "None";

    tabchan.add(chan);
    showtab.add(ctab);
    showqt.add(cqtell);
    showmain.add(cmain);

    add(new JLabel("" + chan), "1, 12, r, f");
    add(ctab, "3, 12, c, f");
    add(cqtell, "5, 12, c, f");
    add(cmain, "7, 12, c, f");
    add(new JLabel(others), "9, 12, l, f");
    
    height += 20;

    setSize(width, height);
  }

  private DefaultListModel myChannels() {
    List<nameListClass> cnl = sVars.channelNamesList;
    List<Integer> sl = new ArrayList<Integer>();

    // we will add 1 for all people
    //sl.add(1); // not anymore

    for (int i=0; i<cnl.size(); i++)
      sl.add(Integer.valueOf(cnl.get(i).channel));

    Collections.sort(sl);
    DefaultListModel slm = new DefaultListModel();

    for (int i=0; i<sl.size(); i++)
      slm.addElement(sl.get(i));

    return slm;
  }

  private void saveChannels() {
    int len = tabchan.size();
      
    if (len == showtab.size() &&
        len == showmain.size() &&
        len == showqt.size()) {
      // these should always be equal and correlated
      for (int i=0; i<len; i++) {
        int chan = tabchan.get(i);
        boolean st = showtab.get(i).isSelected();
        boolean sq = showqt.get(i).isSelected();
        
        sVars.console[tab][chan] = (st || sq ? 1 : 0);
        if (qtellchannels.contains(chan))
          sVars.qtellController[tab][chan] =
            ((st && !sq) ? 1 :
             ((sq && !st) ? 2 : 0));

        sVars.mainAlso[chan] = showmain.get(i).isSelected();
      }
    }

    if (gamenotify.isSelected()) {
      sVars.gameNotifyConsole = tab;
    } else {
      if (sVars.gameNotifyConsole == tab)
        sVars.gameNotifyConsole = 0;
    }

    setConsoleTabTitles asetter = new setConsoleTabTitles();
    int oldshout;
      
    // not implementing shouts also in main for now
    if (shouts.isSelected()) {
      oldshout = sVars.shoutRouter.shoutsConsole;
      sVars.shoutRouter.shoutsConsole = tab;
      if (oldshout > 0)
        asetter.createConsoleTabTitle(sVars, oldshout, subs,
                                      sVars.consoleTabCustomTitles[oldshout]);
      sVars.shoutsAlso = (mainshouts.isSelected());

    } else {
      if (sVars.shoutRouter.shoutsConsole == tab)
        sVars.shoutRouter.shoutsConsole = 0;
    }

    if (sshouts.isSelected()) {
      oldshout = sVars.shoutRouter.sshoutsConsole;
      sVars.shoutRouter.sshoutsConsole = tab;
      if (oldshout > 0)
        asetter.createConsoleTabTitle(sVars, oldshout, subs,
                                      sVars.consoleTabCustomTitles[oldshout]);
    } else {
      if (sVars.shoutRouter.sshoutsConsole == tab)
        sVars.shoutRouter.sshoutsConsole = 0;
    }

    String tabname = "";
    if (setname.isSelected())
      tabname = name.getText();

    asetter.createConsoleTabTitle(sVars, tab, subs, tabname);

    if (sVars.consoleLayout == 3)
      for (int i=0; i<sVars.openConsoleCount; i++)
        subs[i].updateTabChooserCombo();
  }

  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();

    if (action.equals("add")) {
      int number = (Integer)list.getSelectedValue();
      addChannel(number, true);

    } else if (action.equals("save")) {
      saveChannels();
      dispose();

    } else if (action.equals("cancel")) {
      dispose();

    } else if (action.equals("apply")) {
      saveChannels();

    } else if (action.equals("name")) {
      name.setEnabled(setname.isSelected());

    } else if (action.equals("shouts")) {
      mainshouts.setEnabled(shouts.isSelected());
    }
  }
}

/*
import java.awt.*;
import java.awt.Window.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JDialog;
import java.util.ArrayList;

class customizeChannelsDialog extends JDialog {
  JTextField field;
  JFrame frame2;
  int conNumber;
  channels sharedVariables;
  JCheckBox gamenotify;
  JCheckBox shouts;
  JCheckBox shoutsAlso;
  JCheckBox sshouts;
  JCheckBox names;
  JCheckBox announcements;
  JCheckBox name;
  JLabel nameLabel;
  JCheckBox deleteCheckBox;
  JLabel deleteLabel;
  JTextField nameField;
  boolean deleteOn;
  
  subframe[] consoleSubframes;
  JCheckBox mybox;

  String myTabName = "";

  String getChannels(int num) {
    String mychannelnums="";

    boolean mainAlso=false;
    boolean also=false;
    int a;

    for (a=0; a<500; a++) {
      if (sharedVariables.console[num][a] == 1) {
        if (sharedVariables.mainAlso[a]) {
          mainAlso = true;
        } else {
          mainAlso = false;
          break;
	}
      }
    }

    for (a=0; a<500; a++) {
      if (sharedVariables.console[num][a]==1) {
        int i=0;

        for (int b=1; b<sharedVariables.maxConsoleTabs; b++)
          if (sharedVariables.console[b][a]==1)
            i++;

        if (i>1) {
          also=true;
        } else {
          also=false;
          break;
        }
      }
    }// end outer if before for

    if (mainAlso)
      mybox.setSelected(true);

    if (also)
      deleteCheckBox.setSelected(true);

    for (a=0; a<500; a++) {
      if (sharedVariables.console[num][a]==1) {
	mychannelnums=mychannelnums + a;

	if (sharedVariables.mainAlso[a] && !mainAlso)
          mychannelnums = mychannelnums + "m";
	if (!also) {
          int i=0;
          for (int b=1; b<sharedVariables.maxConsoleTabs; b++)
            if (sharedVariables.console[b][a]==1)
              i++;

          if (i>1) {
            mychannelnums = mychannelnums + "a";
          }
	}// if also == false

        mychannelnums = mychannelnums + " ";
      }
    }

    return mychannelnums;
  }

  void resetChannels(int num) {
    for (int a=0; a<500; a++)
      sharedVariables.console[num][a]=0;
  }

  customizeChannelsDialog(JFrame frame, boolean mybool, int conNumberInputed,
                          channels sharedVariables1, subframe[] consoleSubframes1) {
    super(frame, mybool);
    sharedVariables=sharedVariables1;
    consoleSubframes=consoleSubframes1;
    deleteOn=true;

    conNumber=conNumberInputed;
    frame2=frame;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setTitle("Customize channels for Tab" + conNumber);
    JPanel pane = new JPanel();
    Color newCol = new Color(255, 255, 240);
    pane.setBackground(newCol);
    add(pane);
    field = new JTextField(30);

    name = new JCheckBox();
    nameLabel = new JLabel("let me name tab");
    nameField=new JTextField(30);
    if(!sharedVariables.consoleTabCustomTitles[conNumber].equals(""))
      nameField.setText(sharedVariables.consoleTabCustomTitles[conNumber]);

    JPanel warningpanel = new JPanel();
    warningpanel.setBackground(newCol);
    warningpanel.setLayout(new GridLayout(2,1));

    JLabel label =
      new JLabel("<html>Type a space separated list of channels for this tab. " +
                 "i.e. 2 3 50<br>50m can be used as a shortcut to keep that " +
                 "channel on main also.<br>Options below for if all these " +
                 "channels should continue to print to main.</html>");
    float mysizefont = 16;
    label.setFont(label.getFont().deriveFont(mysizefont));
    //JLabel labelm = new JLabel("50m can be used as a shortcut to
    //  keep that channel on main also.");
    //JLabel labelOptions = new JLabel("Options below for if all these
    //	channels should continue to print to main.");
    warningpanel.add(label);
    JPanel fieldpane = new JPanel();
    fieldpane.setBackground(newCol);
    fieldpane.add(field);
    JButton button = new JButton("Customize Tab");
    fieldpane.add(button);
    pane.add(warningpanel, BorderLayout.NORTH);
    pane.add(fieldpane);

    // we set fields text below after dialog set up because if we find
    // all main or all also we need to check checkboxes

    gamenotify = new JCheckBox();
    gamenotify.setBackground(newCol);
    JLabel gamenotifyLabel = new JLabel("Check for gnotify to this tab");
    if (sharedVariables.gameNotifyConsole == conNumber)
      gamenotify.setSelected(true);

    JButton button2 = new JButton("Cancel");
    JButton button3 = new JButton("Erase all");

    button2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          dispose();
        }
      });

    button3.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          resetChannels(conNumber);
          sharedVariables.comboNames[conNumber].clear();
          setConsoleTabTitles asetter = new setConsoleTabTitles();
          asetter.createConsoleTabTitle(sharedVariables, conNumber,
                                        consoleSubframes, myTabName);

          dispose();
        }
      });

    button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          boolean mainAlso = false;
          if (mybox.isSelected())
            mainAlso = true;

          if (deleteCheckBox.isSelected())
            deleteOn=false;

          if (gamenotify.isSelected())
            sharedVariables.gameNotifyConsole=conNumber;

          if (!gamenotify.isSelected() &&
              sharedVariables.gameNotifyConsole == conNumber)
            sharedVariables.gameNotifyConsole=0;

          if (shouts.isSelected()) {

            // as we move shoutsinto a console we have to rename any
            // console tabs that were using that allready
            setConsoleTabTitles asetter = new setConsoleTabTitles();
            int oldshout=sharedVariables.shoutRouter.shoutsConsole;
            sharedVariables.shoutRouter.shoutsConsole=conNumber;
            if (oldshout > 0)
              asetter.createConsoleTabTitle(sharedVariables, oldshout,
                                            consoleSubframes, myTabName);

            if (shoutsAlso.isSelected())
              sharedVariables.shoutsAlso=true;
            else
              sharedVariables.shoutsAlso=false;
          }

          if (!shouts.isSelected() &&
              sharedVariables.shoutRouter.shoutsConsole == conNumber) {
            sharedVariables.shoutRouter.shoutsConsole=0;
            sharedVariables.shoutsAlso=false;
          }

          if (names.isSelected()) {
            sharedVariables.comboNames[conNumber].clear();
          }

          if (sshouts.isSelected()) {

            setConsoleTabTitles asetter = new setConsoleTabTitles();
            int oldshout = sharedVariables.shoutRouter.sshoutsConsole;

            sharedVariables.shoutRouter.sshoutsConsole=conNumber;

            if (oldshout > 0)
              asetter.createConsoleTabTitle(sharedVariables, oldshout,
                                            consoleSubframes, myTabName);
          }

          if (!sshouts.isSelected() &&
              sharedVariables.shoutRouter.sshoutsConsole == conNumber)
            sharedVariables.shoutRouter.sshoutsConsole=0;

          //if (announcements.isSelected())
          //  sharedVariables.shoutRouter.announcementsConsole=conNumber;
          //if (!sshouts.isSelected() &&
          //    sharedVariables.shoutRouter.announcementsConsole == conNumber)
          //  sharedVariables.shoutRouter.announcementsConsole=0;

          myTabName = nameField.getText();
          int foundChannel=0;
          // if we find one channel we zero out all channels in
          // console first before adding channels
          String mytext= field.getText();
          mytext=mytext.trim();
          int make=0;
          try {
            resetChannels(conNumber);
            while (mytext.length() > 0) {
              int i = mytext.indexOf(" ");
              if (i == -1) {// no more spaces last channel
                // sharedVariables.console[aChannelNumber 1-500] will
                // tell me what console a channel number is in, if not
                // setto a sub console number it goes in main, i.e its
                // 0
                flagClass args = new flagClass();
                mytext = args.parseFlags(mytext, 2);

                Integer num = new Integer(mytext);
                int num1=num.intValue();
                if(num1 >= 0 && num1 < 500) {
                  //int oldConNum = sharedVariables.console[num1];
                  sharedVariables.console[conNumber][num1]= 1;

                  // if mainAlso is true they selected in checkbox to
                  // route this tabs channels to main as well
                  if (mainAlso || args.mainAlso)
                    sharedVariables.mainAlso[num1] = true;
                  else
                    sharedVariables.mainAlso[num1] = false;

                  if (deleteOn && args.deleteOn)
                    removeFromOtherTabs(num1, conNumber);
                }
                make=1;
                break;

              } else {
                String temp=mytext.substring(0, i);
                flagClass args = new flagClass();
                temp = args.parseFlags(temp, 2);

                Integer num = new Integer(temp);
                int num1=num.intValue();
                if (num1 > 0 && num1 < 500) {
                  sharedVariables.console[conNumber][num1]=1;

                  // if mainAlso is true they selected in checkbox to
                  // route this tabs channels to main as well
                  if (mainAlso || args.mainAlso)
                    sharedVariables.mainAlso[num1] = true;
                  else
                    sharedVariables.mainAlso[num1] = false;

                  if (deleteOn && args.deleteOn)
                    removeFromOtherTabs(num1, conNumber);

                  make=1;
                }

                if (i+1 < mytext.length()) {
                  mytext=mytext.substring(i+1, mytext.length());
                } else {
                  break;
                }
              }
            }// end else

            setConsoleTabTitles asetter = new setConsoleTabTitles();

            for (int z=1; z<sharedVariables.openConsoleCount; z++)
              if (z==conNumber || deleteOn) {
                String tempName=sharedVariables.consoleTabCustomTitles[z];
                if (z==conNumber)
                  asetter.createConsoleTabTitle(sharedVariables, z,
                                                consoleSubframes, myTabName);
                else
                  asetter.createConsoleTabTitle(sharedVariables, z,
                                                consoleSubframes, tempName);
              }

            if (sharedVariables.consoleLayout == 3)
              for (int z=0; z<sharedVariables.openConsoleCount; z++)
                consoleSubframes[z].updateTabChooserCombo();
            // end try
          } catch (Exception e) {
            String swarning =
              "Could not read your list of channels to customize console.  " +
              "If channels are valid they will be put in this console.  " +
              "Otherwise please try again, or select customize console again " +
              "to see what channels you got and hit cancel. It needs to be a space " +
              "seperated list with valid channel numbers. i.e. 2 3 100";
            setConsoleTabTitles asetter = new setConsoleTabTitles();

            asetter.createConsoleTabTitle(sharedVariables, conNumber,
                                          consoleSubframes, myTabName);
            Popup pop=new Popup(frame2, true, swarning);
            pop.setVisible(true);

          } finally {
            dispose();
          }
        }
      });

    mybox = new JCheckBox();
    mybox.setSelected(false);
    mybox.setBackground(newCol);
    JLabel myboxLabel = new JLabel("Show these channels on main tab as well?");

    shouts = new JCheckBox();
    shouts.setSelected(false);
    shouts.setBackground(newCol);
    if (sharedVariables.shoutRouter.shoutsConsole == conNumber)
      shouts.setSelected(true);

    shoutsAlso=new JCheckBox();
    shoutsAlso.setSelected(false);
    shoutsAlso.setBackground(newCol);
    if (sharedVariables.shoutsAlso)
      shoutsAlso.setSelected(true);
    JLabel shoutsAlsoLabel = new JLabel("Any shouts moved go to main also.");

    JLabel myshoutLabel = new JLabel("Direct Shouts to this tab?");
    sshouts = new JCheckBox();
    sshouts.setBackground(newCol);
    if (sharedVariables.shoutRouter.sshoutsConsole == conNumber)
      sshouts.setSelected(true);

    JLabel mynamesLabel = new JLabel("Remove any tell names?");
    names = new JCheckBox();
    names.setBackground(newCol);

    sshouts.setSelected(false);
    if (sharedVariables.shoutRouter.sshoutsConsole == conNumber)
      sshouts.setSelected(true);

    JLabel mysshoutLabel = new JLabel("Direct S-Shouts to this tab?");
    deleteLabel = new JLabel("Don't delete these channels from other tabs?");
    deleteCheckBox = new JCheckBox();
    deleteCheckBox.setBackground(newCol);

    String channelList = getChannels(conNumber);
    field.setText(channelList);

    JPanel checkboxpanel = new JPanel();
    checkboxpanel.setBackground(newCol);
    checkboxpanel.setLayout(new GridLayout(9,2));
    checkboxpanel.add(button3);
    checkboxpanel.add(button2);

    checkboxpanel.add(myshoutLabel);
    checkboxpanel.add(shouts);
    checkboxpanel.add(shoutsAlsoLabel);
    checkboxpanel.add(shoutsAlso);
    checkboxpanel.add(mysshoutLabel);
    checkboxpanel.add(sshouts);

    checkboxpanel.add(myboxLabel);
    checkboxpanel.add(mybox);
    checkboxpanel.add(deleteLabel);
    checkboxpanel.add(deleteCheckBox);

    checkboxpanel.add(mynamesLabel);
    checkboxpanel.add(names);
    checkboxpanel.add(gamenotifyLabel);

    checkboxpanel.add(gamenotify);

    pane.add(checkboxpanel);

    JPanel naming = new JPanel();
    naming.setBackground(newCol);
    naming.add(nameLabel);
    naming.add(name);
    naming.add(nameField);
    pane.add(naming);
    //pane.add(myboxLabel);
    //pane.add(mybox);

    setSize(650,550);
    setVisible(true);
  }// end constructor

  void removeFromOtherTabs(int num, int myTab) {
    for (int a=1; a<sharedVariables.maxConsoleTabs; a++)
      if (a!=myTab)
        sharedVariables.console[a][num]=0;
  }

  class flagClass {

    boolean mainAlso;
    boolean deleteOn;

    flagClass() {
      mainAlso = false;
      deleteOn = true;
    }

    String parseFlags(String text, int depth) {

      if (text.length() < 2)
        return text;

      String newText = text.substring(0, text.length() - 1);
      String flag = text.substring(text.length() - 1, text.length());

      if (flag.equals("m") || flag.equals("M")) {
        mainAlso = true;
        if (depth > 1) {
          return parseFlags(newText, depth-1);

        } else {
          return newText;
        }

      } else if (flag.equals("a") || flag.equals("A")) {
        deleteOn = false;
        if (depth > 1) {
          return parseFlags(newText, depth-1);

        } else {
          return newText;
        }
      }

      return text;
    }
  }// end flag class
}// end class
/**/