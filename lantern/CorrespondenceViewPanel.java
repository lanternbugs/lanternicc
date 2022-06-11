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
import javax.swing.table.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


class CorrespondenceViewPanel extends JPanel// implements InternalFrameListener
{
	JTable corrTable;
    JScrollPane scrollPane;
	channels sharedVariables;
	ConcurrentLinkedQueue<myoutput> queue;
    JFrame homeFrame;
    JLabel dummyLabel;
    JLabel statusLabel = new JLabel("status: ");
    JLabel doubleClickHintLabel = new JLabel("Double click on a game entry for options like making a move or viewing.");
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
    setBackground(Color.white);
    addDoubleClickListener();
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
    hRow1.addComponent(refreshGamesButton);
    hRow1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    hRow1.addComponent(doubleClickHintLabel);
    hRow2.addComponent(startGameButton);
    hRow2.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    hRow2.addComponent(statusLabel);
    







    h1.addComponent(dummyLabel, GroupLayout.PREFERRED_SIZE,
                    GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
    h1.addGroup(hRow1);
    h1.addGroup(hRow2);
    h1.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE,
                      GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
      //Create the horizontal group
      layout.setHorizontalGroup(hGroup);


      //Create a parallel group for the vertical axis
      SequentialGroup vGroup = layout.createSequentialGroup();
    ParallelGroup vRow1 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
    ParallelGroup vRow2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
    vRow1.addComponent(refreshGamesButton);
    vRow1.addComponent(doubleClickHintLabel);
    vRow2.addComponent(statusLabel);
    vRow2.addComponent(startGameButton);




    vGroup.addComponent(dummyLabel, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
    vGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    vGroup.addGroup(vRow1);
    vGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    vGroup.addGroup(vRow2);
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
                     String whiteName = (String)corrTable.getModel().getValueAt(row,1);
                     String blackName = (String)corrTable.getModel().getValueAt(row,3);
                     
                    

                         // Get the ListSelectionModel of the JTable
                         ListSelectionModel model = target.getSelectionModel();

                         // set the selected interval of rows. Using the "rowNumber"
                         // variable for the beginning and end selects only that one row.
                         model.setSelectionInterval( row, row );


                  //row = sorter.convertRowIndexToModel(row);
                     JPopupMenu menu2=new JPopupMenu("Popup2");
                                     JMenuItem item1 = new JMenuItem("Examine");
                                      item1.addActionListener(new ActionListener() {
                                       public void actionPerformed(ActionEvent e) {
                                         String examineString = "multi examine #" + gameIndex;
                                           myoutput output = new myoutput();
                                           output.data="`c0`" + examineString + "\n";

                                           output.consoleNumber=0;
                                             queue.add(output);

                                     }
                                          

                            });
                    
                     menu2.add(item1);
                     
                     JMenuItem item2 = new JMenuItem("Move in Game");
                      item2.addActionListener(new ActionListener() {
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
    
     menu2.add(item2);
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

    
    void startGame()
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
            System.out.println(textBuilder.toString());
            
        } catch(Exception e) {
            
        }
        
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
    
   
}//end class
