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
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.table.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;


class CorrespondenceViewPanel extends JPanel// implements InternalFrameListener
{
	JTable corrTable;
    JScrollPane scrollPane;
	channels sharedVariables;
	ConcurrentLinkedQueue<myoutput> queue;
    JFrame homeFrame;
    JLabel dummyLabel;
    JLabel openToGamesLabel = new JLabel("Open to Random Games");
    JButton openToGamesButton;
    JButton helpCorrespondenceButton;
    JLabel statusLabel = new JLabel("Status: ");
    JLabel doubleClickHintLabel = new JLabel("Double click on a game entry for options like making a move or viewing.");
    int doubleClickHintLabelFontSizeToUse = 16;
    JButton refreshGamesButton;
    JButton startGameButton;

	
CorrespondenceViewPanel(JFrame master, channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1)
{
sharedVariables=sharedVariables1;
queue=queue1;
sharedVariables.corrPanel = this;
homeFrame=master;
    initComponents();
}// end constructor


void initComponents(){
    corrTable = new JTable(sharedVariables.ccListData, sharedVariables.ccListColumnNames);
    corrTable.setDefaultRenderer(Object.class, new CorrTableCellRenderer());
    //corrTable.removeColumn(corrTable.getColumnModel().getColumn(0)); will work to remove game numbers
    dummyLabel = new JLabel("Correspondence");
    scrollPane = new JScrollPane();
    scrollPane = new JScrollPane(corrTable);
   // scrollPane.setViewportView(corrTable);
  corrTable.setFillsViewportHeight(true);
    scrollPane.setColumnHeaderView(corrTable.getTableHeader());
    corrTable.setDefaultEditor(Object.class, null);
    refreshGamesButton = new JButton();
    startGameButton = new JButton();
    openToGamesButton = new JButton();
    helpCorrespondenceButton = new JButton();
    refreshGamesButton.setText("Refresh Games");
    refreshGamesButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        refreshGames();
      }
    } );
    startGameButton.setText("Start Game");
    startGameButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        startGame();
      }
    } );
    openToGamesButton.setText("Make Open");
    openToGamesButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        openToGames();
      }
    } );
    helpCorrespondenceButton.setText("Help Correspondence");
    helpCorrespondenceButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          myoutput output = new myoutput();
            output.data = "multi help correspondence\n";
          output.consoleNumber = 0;
          queue.add(output);
      }
    } );
    setBackground(Color.white);
    addDoubleClickListener();
    Font doubleClickHintLabelFont = doubleClickHintLabel.getFont();
    doubleClickHintLabel.setFont(new Font(doubleClickHintLabelFont.getName(), Font.PLAIN, doubleClickHintLabelFontSizeToUse));
    //System.out.println("font size is " + doubleClickHintLabel.getFont().getSize());
setLayout();

}// end inti components

    public class CorrTableCellRenderer extends DefaultTableCellRenderer {


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row,int col) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        c.setBackground(corrTable.getBackground());
        c.setForeground(corrTable.getForeground());
        
        return c;
    }
    }
    
void setLayout()
{

    GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
      //Create a parallel group for the horizontal axis
      ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
    SequentialGroup hRow1 = layout.createSequentialGroup();
    SequentialGroup hRow2 = layout.createSequentialGroup();
    SequentialGroup hRow3 = layout.createSequentialGroup();
    hRow1.addComponent(refreshGamesButton);
    hRow1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    hRow1.addComponent(doubleClickHintLabel);
    hRow2.addComponent(startGameButton);
    hRow2.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    hRow2.addComponent(statusLabel);
    hRow3.addComponent(helpCorrespondenceButton);
    hRow3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    hRow3.addComponent(openToGamesLabel);
    hRow3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    hRow3.addComponent(openToGamesButton);
    







    h1.addComponent(dummyLabel, GroupLayout.PREFERRED_SIZE,
                    GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
    h1.addGroup(hRow1);
    h1.addGroup(hRow2);
    h1.addGroup(hRow3);
    h1.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE,
                      GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
      //Create the horizontal group
      layout.setHorizontalGroup(hGroup);


      //Create a parallel group for the vertical axis
      SequentialGroup vGroup = layout.createSequentialGroup();
    ParallelGroup vRow1 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
    ParallelGroup vRow2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
    ParallelGroup vRow3 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
    
    vRow1.addComponent(refreshGamesButton);
    vRow1.addComponent(refreshGamesButton);
    vRow1.addComponent(doubleClickHintLabel);
    vRow2.addComponent(helpCorrespondenceButton);
    vRow2.addComponent(openToGamesLabel);
    vRow2.addComponent(openToGamesButton);
    vRow3.addComponent(statusLabel);
    vRow3.addComponent(startGameButton);
    



    vGroup.addComponent(dummyLabel, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
    vGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    vGroup.addGroup(vRow1);
    vGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    vGroup.addGroup(vRow2);
    vGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    vGroup.addGroup(vRow3);
    vGroup.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE,
                          GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

      layout.setVerticalGroup(vGroup);


}// end set layout
    
    void addDoubleClickListener()
    {
        MouseListener mouseListenerEvents = new MouseAdapter() {
             public void mousePressed(MouseEvent e) {
                 if (e.getClickCount() == 2 && e.getButton() != MouseEvent.BUTTON3) {

                     JTable target = (JTable)e.getSource();
              //int row = target.getSelectedRow();
              //row = sorter.convertRowIndexToModel(row);
              /*int index = gametable.rowAtPoint(e.getPoint());*/
                     Point p = e.getPoint();

                         // get the row index that contains that coordinate
                         int row = target.rowAtPoint( p );
                    String gameIndex = (String)corrTable.getModel().getValueAt(row,0);
                     String whiteName = (String)corrTable.getModel().getValueAt(row,2);
                     String blackName = (String)corrTable.getModel().getValueAt(row,4);
                     String opponent = whiteName.toLowerCase().trim().equals(sharedVariables.whoAmI.toLowerCase().trim()) ? blackName : whiteName;
                     
                    

                         // Get the ListSelectionModel of the JTable
                         ListSelectionModel model = target.getSelectionModel();

                         // set the selected interval of rows. Using the "rowNumber"
                         // variable for the beginning and end selects only that one row.
                         model.setSelectionInterval( row, row );


                  //row = sorter.convertRowIndexToModel(row);
                     JPopupMenu menu2=new JPopupMenu("Correspondence Game");
                    JMenuItem item1 = new JMenuItem("Examine #" + gameIndex);
                    item1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            sendICSCommand("examine #" + gameIndex);
                        }
                    });
                     menu2.add(item1);
                     
                     JMenuItem itemMove = new JMenuItem("Move in Game");
                     itemMove.addActionListener(new ActionListener() {
                       public void actionPerformed(ActionEvent e) {
                           CorrespondenceMoveDialog dialog = new CorrespondenceMoveDialog(homeFrame, sharedVariables, queue, gameIndex, whiteName + " vs " + blackName);
                           dialog.setSize(800,300);
                           if(homeFrame.getSize().width > 850) {
                               int x = homeFrame.getLocation().x + (homeFrame.getSize().width - 800) / 2;
                               int y = 0;
                               if(homeFrame.getSize().height > 350) {
                                   y  = homeFrame.getLocation().y + (homeFrame.getSize().height - 300) / 2;
                               }
                               dialog.setLocation(x, y);
                           }
                           dialog.setVisible(true);
                           dialog.input.requestFocus(true);
                          

                     }
                          

            });
    
     menu2.add(itemMove);
                     
                     JMenuItem item2 = new JMenuItem("Draw Offer");
                     item2.addActionListener(new ActionListener() {
                         public void actionPerformed(ActionEvent e) {
                             sendICSYesNoCommand("draw #" + gameIndex);
                             updateStatusBar("Draw offer sent. Check Console M0 Tab for server feedback.");
                         }
                     });
                      menu2.add(item2);
                     
                     JMenuItem item3 = new JMenuItem("Abort Offer");
                     item3.addActionListener(new ActionListener() {
                         public void actionPerformed(ActionEvent e) {
                             sendICSYesNoCommand("abort #" + gameIndex);
                             updateStatusBar("Aort offer sent or automatic on move one. Check Console M0 Tab for server feedback.");
                         }
                     });
                      menu2.add(item3);
                     
                     JMenuItem item4 = new JMenuItem("Resign Game #" + gameIndex);
                     item4.addActionListener(new ActionListener() {
                         public void actionPerformed(ActionEvent e) {
                             sendICSYesNoCommand("resign #" + gameIndex);
                             updateStatusBar("Resign sent. Check Console M0 Tab for server feedback.");
                         }
                     });
                      menu2.add(item4);
                     
                     
                    /* JMenuItem item5 = new JMenuItem("Delete Game");
                     item5.addActionListener(new ActionListener() {
                         public void actionPerformed(ActionEvent e) {
                             sendICSCommand("cc-delete #" + gameIndex);
                             // need to clear data first
                     Command("cc-list");
                             updateStatusBar("Delete sent. Check list or Console M0 Tab for feedback.");
                         }
                     });
                      menu2.add(item5);*/
                     
                     JMenuItem item6 = new JMenuItem("Copy " + gameIndex);
                     item6.addActionListener(new ActionListener() {
                         public void actionPerformed(ActionEvent e) {
                             String myString = gameIndex;
                             StringSelection stringSelection = new StringSelection(myString);
                             Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                             clipboard.setContents(stringSelection, null);
                         }
                     });
                      menu2.add(item6);
                     
                     JMenuItem item7 = new JMenuItem("Lookup " + opponent);
                     item7.addActionListener(new ActionListener() {
                         public void actionPerformed(ActionEvent e) {
                             sendICSLookupCommand("Finger " + opponent);
                             updateStatusBar("Resign sent. Check Console M0 Tab for server feedback.");
                         }
                     });
                      menu2.add(item7);
                     menu2.show(e.getComponent(),e.getX(),e.getY());


                  }// end click count two
               
                       
         }
            public void mouseClicked(MouseEvent e) {
                //System.out.println("in clicked");

               }


            public void mouseReleased(MouseEvent e) {
                //System.out.println("in released");
            }


   public void mouseEntered (MouseEvent me) {}
   public void mouseExited (MouseEvent me) {}
            
            
            
        };
        corrTable.addMouseListener(mouseListenerEvents);
    }

    class GameStarterClass implements Runnable
    {
        @Override
        public void run()
        {
            try {
                URL url = new URL("http://pool.cloud.chessclub.com/");
                URLConnection con = url.openConnection();
                InputStream inputStream = con.getInputStream();
                StringBuilder textBuilder = new StringBuilder();
                    try (Reader reader = new BufferedReader(new InputStreamReader
                      (inputStream, StandardCharsets.UTF_8))) {
                        int c = 0;
                        while ((c = reader.read()) != -1) {
                            textBuilder.append((char) c);
                        }
                    }
                myoutput output = new myoutput();
                output.data ="`y7`" + textBuilder.toString().trim() + "\n";
                queue.add(output);
                System.out.println(textBuilder.toString());
                
            } catch(Exception e) {
                
            }
        }
    }
    void startGame()
    {
        startGameButton.setEnabled(false);
        Timer timer = new Timer();
        timer.schedule(new enableStartGameTimer(),1000);
        GameStarterClass enabler = new GameStarterClass();
         Thread t_start = new Thread(enabler);
         t_start.start();
        
        
    }
    
    void refreshGames()
    {
        try {
            myoutput output = new myoutput();
            output.consoleNumber = 0;
            output.data = "multi cc-list\n";
            queue.add(output);
            sharedVariables.ccListData.clear();
            corrTable.repaint();
            
        } catch(Exception e) {
            
        }
        
    }
    
    void openToGames()
    {
        
    }
    
    public class enableStartGameTimer extends TimerTask
    {
        @Override
        public void run() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                  public void run() {
                  try {
                      if(startGameButton != null) {
                          startGameButton.setEnabled(true);
                      }
                  } catch (Exception e1) {

                  }
                }
              });
            
        }
    }
    
    void sendICSCommand(String command) {
        sendCommand(command, "`c0`");
    }
    
    void sendICSLookupCommand(String command)
    {
        sendCommand(command, "`fl`");
    }
    
    void sendICSYesNoCommand(String command)
    {
        sendCommand(command, "`r1`");
    }
    
    void sendCommand(String command, String prefix)
    {
        String examineString = "multi " + command;
        myoutput output = new myoutput();
        output.data= prefix + examineString + "\n";
        output.consoleNumber=0;
        queue.add(output);
    }
    
    void updateStatusBar(String text)
    {
        statusLabel.setText("Status: " + text);
        statusLabel.repaint();
    }
    
   
}//end class
