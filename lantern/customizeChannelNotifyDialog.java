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

/*
import java.awt.*;
import java.awt.Window.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JDialog;
*/

import javax.swing.JScrollPane;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import layout.TableLayout;

public class customizeChannelNotifyDialog extends JDialog
  implements ActionListener, ListSelectionListener {

  private channels sVars;
  private final String name;

  private JList list;
  private DefaultListModel listModel;

  private SpinnerNumberModel spinnerModel;
  
  private JButton removebutton;

  private int notIndex;
  
  private TableLayout layout;

  private double[] showrows;
  private double[] hiderows;
  
  public customizeChannelNotifyDialog(JFrame frame, boolean mybool,
                                      channels sVars, final String name) {
    super(frame, name + " Channel Notify", mybool);

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    this.sVars = sVars;
    this.name = name;

    listModel = new DefaultListModel();
    getChannels(name);

    list = new JList(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.addListSelectionListener(this);
    JScrollPane listpane = new JScrollPane(list);
    
    spinnerModel = new SpinnerNumberModel(2, 0, 399, 1);
    JSpinner spinner = new JSpinner(spinnerModel);

    notIndex = isOnGlobalNotify(name);
    JCheckBox globnot = new JCheckBox("Connect Notify", (notIndex != -1));
    globnot.setActionCommand("globnot");
    globnot.addActionListener(this);
    
    JButton addbutton = new JButton("Add");
    addbutton.setActionCommand("add");
    addbutton.addActionListener(this);
    
    removebutton = new JButton("Remove");
    removebutton.setActionCommand("remove");
    removebutton.addActionListener(this);
    removebutton.setEnabled((list.getSelectedIndex() != -1));

    int border = 10;
    int space = 5;
    int ht = 20;
    double tf = TableLayout.FILL;

    double[] sr = {border, ht, space, ht, space, tf, ht, tf, border};
    double[] hr = {border, ht, tf, 0, 0, 0, 0, 0, border};

    showrows = sr;
    hiderows = hr;

    setSize(250, (notIndex == -1 ? 200 : 100));
      
    double[][] size = {{border, tf, space, tf, border}, (notIndex == -1 ? showrows : hiderows)};
    layout = new TableLayout(size);
    setLayout(layout);

    add(globnot, "1, 1, 3, 1");
    add(spinner, "1, 3");
    add(addbutton, "3, 3");
    add(listpane, "1, 5, 1, 7");
    add(removebutton, "3, 6");
  }

  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting())
      removebutton.setEnabled((list.getSelectedIndex() != -1));
  }

  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    if (action.equals("globnot")) {
      if (notIndex == -1) {
        lanternNotifyClass ln = new lanternNotifyClass();
        ln.name = name;
        sVars.lanternNotifyList.add(ln);
        notIndex = sVars.lanternNotifyList.size() - 1;
        layout.setRow(hiderows);
        setSize(250, 100);
      } else {
        sVars.lanternNotifyList.remove(notIndex);
        notIndex = -1;
        layout.setRow(showrows);
        setSize(250, 200);
      }

      try {
        write2();
        
      } catch (Exception dui) {}
      
    } else if (action.equals("add") || action.equals("remove")) {
      int number;
      if (action.equals("add")) {
        number = spinnerModel.getNumber().intValue();
      } else {
        number = (Integer)list.getSelectedValue();
      }

      String text = String.valueOf(number);

      boolean haveChannel = false;
      for (int i=0; i<sVars.channelNotifyList.size(); i++) {
        channelNotifyClass cnc = sVars.channelNotifyList.get(i);
        if (cnc.channel.equals(text)) {
          boolean found = false;
          for (int j=0; j<cnc.nameList.size(); j++)
            if (cnc.nameList.get(j).toLowerCase().equals(name.toLowerCase())) {
              if (action.equals("remove")) {
                cnc.nameList.remove(j);
                int index = list.getSelectedIndex();
                listModel.remove(index);

                int size = listModel.getSize();

                if (size == 0) {
                  removebutton.setEnabled(false);

                } else {
                  if (index == size) {
                    index--;
                  }

                  list.setSelectedIndex(index);
                  list.ensureIndexIsVisible(index);
                }
              }
              found = true;
              break;
            }

          if (!found && action.equals("add")) {// found should always be true on remove
            // we have channel but he is not on list so we add him
            cnc.nameList.add(name);
            addlist(number);
          }

          haveChannel = true;
        }
      }  

      if (!haveChannel && action.equals("add")) {
        // haveChannel should always be true on remove
        channelNotifyClass temp = new channelNotifyClass();
        temp.channel = text;
        temp.nameList.add(name);
        sVars.channelNotifyList.add(temp);
        addlist(number);
      }

      try {
        write();

      } catch (Exception dummy) {}
    }
  }

  private void addlist(int number) {
    int size = listModel.getSize();
    for (int i=0; i<size; i++) {
      if (number < (Integer)listModel.getElementAt(i)) {
        listModel.insertElementAt(number, i);
        list.setSelectedIndex(i);
        list.ensureIndexIsVisible(i);
        return;
      }
    }
    
    listModel.insertElementAt(number, size);
    list.setSelectedIndex(size);
    list.ensureIndexIsVisible(size);
  }

  private void getChannels(String name) {
    for (int i=0; i<sVars.channelNotifyList.size(); i++) {
      channelNotifyClass cnc = sVars.channelNotifyList.get(i);
      if (cnc.nameList.size() > 0)
        for (int j=0; j<cnc.nameList.size(); j++)
          if (cnc.nameList.get(j).toLowerCase().equals(name.toLowerCase()))
            listModel.addElement(Integer.valueOf(cnc.channel));
    }
  }

  private int isOnGlobalNotify(String name) {
    for (int i=0; i<sVars.lanternNotifyList.size(); i++)
      if (sVars.lanternNotifyList.get(i).name.toLowerCase().equals(name.toLowerCase()))
        return i;

    return -1;
  }

  private void write() { // channel notify
    FileWrite writer = new FileWrite();
    String mess = "\r\n";
    for (int i=0; i<sVars.channelNotifyList.size(); i++) {
      channelNotifyClass cnc = sVars.channelNotifyList.get(i);
      if (cnc.nameList.size() > 0) {
        mess += "#" + cnc.channel + "\r\n";
        for (int j=0; j < cnc.nameList.size(); j++)
          mess += cnc.nameList.get(j) + "\r\n";
      }
    }

    writer.write(mess, "lantern_channel_notify.txt");
  }

  private void write2() { // global notify
    FileWrite writer = new FileWrite();
    String mess = "\r\n";
    for (int i=0; i<sVars.lanternNotifyList.size(); i++) {
      String name = sVars.lanternNotifyList.get(i).name;
      if (sVars.lanternNotifyList.get(i).sound)
        name = name + " 1\r\n";
      else
        name = name + " 0\r\n";
      mess += name;
    }// end for

    writer.write(mess, "lantern_global_notify.txt");
  }
}

/*
class customizeChannelNotifyDialog extends JDialog {
  JTextField field;

  JLabel messageLabel;
  JLabel addLabel;
  JLabel removeLabel;
  JButton okButton;
  JButton cancelButton;
  JButton globalButton;
  channels sharedVariables;
  int notIndex;

  customizeChannelNotifyDialog(JFrame frame, boolean mybool,
                               channels sharedVariables1, final String name) {
    super(frame, mybool);
    sharedVariables = sharedVariables1;

    String hisChannels = getChannels(name);
    if (hisChannels == null)
      hisChannels = " null";
    setTitle("Set Channel Notify for " + name + hisChannels);
    setSize(500,300);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    okButton = new JButton("Ok");
    cancelButton = new JButton("Cancel");
    addLabel = new JLabel("Add channel to channel notify " + name +
                          " or remove " + name + " from notify");
    field = new JTextField(3);
    JPanel panel = new JPanel();
    notIndex = isOnGlobalNotify(name);
    String text;
    if (notIndex == -1)
      text = "Connect Notify " + name;
    else
      text = "Un-connect Notify " + name;
    globalButton = new JButton(text);

    panel.add(field, BorderLayout.SOUTH);
    panel.add(addLabel, BorderLayout.NORTH);
    panel.add(okButton, BorderLayout.CENTER);
    panel.add(cancelButton, BorderLayout.CENTER);
    panel.add(globalButton,  BorderLayout.CENTER);
    add(panel);

    globalButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          //String mytext= field.getText();
          try {
            if (notIndex == -1) {
              lanternNotifyClass ln = new lanternNotifyClass();
              ln.name = name;
              sharedVariables.lanternNotifyList.add(ln);
            } else
              sharedVariables.lanternNotifyList.remove(notIndex);
            write2();
            dispose();

          } catch (Exception dui) {
            dispose();
          }
        }// end action performed
      });

    okButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          //String mytext= field.getText();
          try {
            String text = field.getText().trim();
            int number = Integer.parseInt(text);

            if (number < 400 && number >= 0) {
              boolean haveChannel = false;
              for (int z=0; z<sharedVariables.channelNotifyList.size(); z++)
                if (sharedVariables.channelNotifyList.get(z).channel.equals(text)) {
                  boolean found = false;
                  for (int b=0; b<sharedVariables.channelNotifyList.get(z).nameList.size(); b++)
                    if (sharedVariables.channelNotifyList.get(z).nameList.get(b).toLowerCase().equals(name.toLowerCase())) {
                      sharedVariables.channelNotifyList.get(z).nameList.remove(b);
                      found = true;
                    }

                  if (found == false) {// we have channel but he is not on list so we add him
                    sharedVariables.channelNotifyList.get(z).nameList.add(name);
                  }

                  haveChannel = true;
                }

              if (haveChannel == false) {
                channelNotifyClass temp = new channelNotifyClass();
                temp.channel = text;
                temp.nameList.add(name);
                sharedVariables.channelNotifyList.add(temp);
              }
              write();
            }// if valid number
            dispose();

          } catch (Exception dummy) {
            dispose();
          }
        }// end event
      });

    cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          //String mytext= field.getText();
          try {
            dispose();
          } catch (Exception dummy) {}
        }// end event
      });
  }// end constructor

  String getChannels(String name) {
    String mess=" ";
    String channel="";
    for (int z=0; z<sharedVariables.channelNotifyList.size(); z++)
      if (sharedVariables.channelNotifyList.get(z).nameList.size() > 0) {
        channel = sharedVariables.channelNotifyList.get(z).channel;
        for (int x=0; x<sharedVariables.channelNotifyList.get(z).nameList.size(); x++)
          if (sharedVariables.channelNotifyList.get(z).nameList.get(x).toLowerCase().equals(name.toLowerCase()))
            mess += " " + channel;
      }
    return mess;
  }

  void write() { // channel notify
    FileWrite writer = new FileWrite();
    String mess = "\r\n";
    for (int z=0; z<sharedVariables.channelNotifyList.size(); z++)
      if (sharedVariables.channelNotifyList.get(z).nameList.size() > 0) {
        mess +="#" + sharedVariables.channelNotifyList.get(z).channel + "\r\n";
        for (int x=0; x < sharedVariables.channelNotifyList.get(z).nameList.size(); x++)
          mess += sharedVariables.channelNotifyList.get(z).nameList.get(x) + "\r\n";
      }

    writer.write(mess, "lantern_channel_notify.txt");
  }

  void write2() { // global notify
    FileWrite writer = new FileWrite();
    String mess = "\r\n";
    for (int z=0; z<sharedVariables.lanternNotifyList.size(); z++) {
      String name=sharedVariables.lanternNotifyList.get(z).name;
      if (sharedVariables.lanternNotifyList.get(z).sound == true)
        name = name + " 1\r\n";
      else
        name = name+ " 0\r\n";
      mess+=name;
    }// end for

    writer.write(mess, "lantern_global_notify.txt");
  }

  int isOnGlobalNotify(String name) {
    for (int z=0; z<sharedVariables.lanternNotifyList.size(); z++)
      if (sharedVariables.lanternNotifyList.get(z).name.toLowerCase().equals(name.toLowerCase()))
        return z;

    return -1;
  }
}// end class
*/