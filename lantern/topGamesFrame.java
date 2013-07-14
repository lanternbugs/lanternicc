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

class topGamesFrame extends JDialog// implements InternalFrameListener
{

	//subframe [] consoleSubframes;
channels sharedVariables;
JCheckBoxMenuItem notontop;
 listClass eventsList;
 ConcurrentLinkedQueue queue;
topGamesPanel topGamesListPanel;
JTable theEventsList;
JScrollPane listScroller;
int JOIN_COL = 0;
int WATCH_COL = 1;
int INFO_COL = 2;
int ENTRY_COL = 3;
  int iconWidth = 42;

topGamesFrame(JFrame master, channels sharedVariables1, ConcurrentLinkedQueue queue1,  listClass eventsList1)
{     super(master, false);
sharedVariables=sharedVariables1;
queue=queue1;
eventsList=eventsList1;

 setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
 addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent we) {

         setVisible(false);
    }
});

setTitle("Top Games Window");
setSize(500,180);
setLocation(200,250);

theEventsList = new JTable(eventsList.topGamesTable)
{
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
};
/*theEventsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
theEventsList.setLayoutOrientation(JList.VERTICAL);
theEventsList.setVisibleRowCount(-1);
*/

theEventsList.setShowVerticalLines(false);
theEventsList.setShowHorizontalLines(true);
TableColumn col0 = theEventsList.getColumnModel().getColumn(JOIN_COL);
col0.setPreferredWidth(iconWidth);
col0.setMaxWidth(iconWidth);
TableColumn col1 = theEventsList.getColumnModel().getColumn(WATCH_COL);
col1.setPreferredWidth(iconWidth);
col1.setMaxWidth(iconWidth);

TableColumn col2 = theEventsList.getColumnModel().getColumn(INFO_COL);
col2.setPreferredWidth(iconWidth);
col2.setMaxWidth(iconWidth);

TableColumn col3 = theEventsList.getColumnModel().getColumn(ENTRY_COL);
col3.setPreferredWidth(200);
col3.setMaxWidth(10000);

listScroller = new JScrollPane(theEventsList);
//listScrollerPanel = new EventsPanel();
//listScrollerPanel.add(listScroller);

 MouseListener mouseListenerEvents = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {
  JTable target = (JTable)e.getSource();
      int row = target.getSelectedRow();
 TableColumnModel colModel = target.getColumnModel();
 // get column index
 int  col = colModel.getColumnIndexAtX(e.getX());

 if(row == 0)
 return;
if(col == ENTRY_COL)
return;
String join1;
String join2;

String listing = eventsList.getEventListing(row);
String join = eventsList.getJoinCommand(row);
String info = eventsList.getInfoCommand(row);
String watch = eventsList.getWatchCommand(row);
/*JFrame framer = new JFrame("join is: " + join + " and watch is: " + watch + " and info is: " + info + " and col is: " + col);
framer.setSize(1000,100);
framer.setVisible(true);
*/
if(col == JOIN_COL && join.equals("!!!"))
return;
if(col == WATCH_COL && watch.equals("!!!"))
return;
if(col == INFO_COL && info.equals("!!!"))
return;


             if(listing.equals("-"))
             return;
             boolean go=false;

             if(listing.contains("[VIDEO]"))
             {
                if(!info.equals(""))
                if(info.startsWith("http://"))
                {

                if(!join.equals(""))
                if(join.toLowerCase().contains(" webcast"))
                {
                 go=true;
                 sharedVariables.openUrl(info);
                }
                }
              if(!join.equals(""))
                if(join.startsWith("https://"))
                {

                if(!join.equals(""))
                if(join.toLowerCase().contains("gotd"))
                {
                 go=true;
                 sharedVariables.openUrl(join);
                }
                }
             }
              if(join.equals("!!!") && info.equals("!!!") && (watch.toLowerCase().startsWith("observe ") && !listing.startsWith("LIVE")))
             {
                myoutput data = new myoutput();
                data.consoleNumber = 0;
                data.data= "`c0`" + watch + "\n";
                queue.add(data);
                go=true;

             }
             if(watch.equals("!!!") && info.equals("!!!") && join.toLowerCase().startsWith("examine "))
             {
                myoutput data = new myoutput();
                data.consoleNumber = 0;
                data.data= "`c0`" + join + "\n";
                queue.add(data);
                go=true;

             }
             if(go == false)
             {
join1=join;
join2="";

if(join.indexOf(" & ")!=-1)
{
        int spot = join.indexOf(" & ");
	try {
	join1=join.substring(0, spot);
	join2=join.substring(spot + 3, join.length() );
	}catch(Exception f){}
}// if join has &

if(col == JOIN_COL)
{
joinMethod(join1, join2);
} // if join
else if(col == WATCH_COL)
{
  watchMethod(watch);
}// if watch
else if(col == INFO_COL)
{
infoMethod(info);
}// if info



             } // if go equals false

/* JFrame framer = new JFrame("row is " + row + " and collumn is " + col);
 framer.setVisible(true);
 framer.setSize(500,100);
 */
 }// end method
 };// end class
theEventsList.addMouseListener(mouseListenerEvents);


 topGamesListPanel = new topGamesPanel();



 add(topGamesListPanel);


}// end constructor
void setColors()
{
theEventsList.setBackground(sharedVariables.listColor);

}
void joinMethod(String join1, String join2)
{
                                if(join1.startsWith("http"))
                                {
                                 sharedVariables.openUrl(join1);
                                 return;
                                }


				 myoutput output = new myoutput();
				 output.data= "`c0`" + join1 + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);

      			 if(!join2.equals(""))
      			 {
				 output = new myoutput();
				 output.data= "`c0`" + join2 + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);

				 }

}// end join method

void infoMethod(String info)
{
                                if(info.startsWith("http"))
                                {
                                 sharedVariables.openUrl(info);
                                 return;
                                }
                                 myoutput output = new myoutput();
				 output.data= "`c0`" + info + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);

}// end info method



void watchMethod(String watch)
{
				 myoutput output = new myoutput();
				 output.data= "`c0`" + watch + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);

}//end watch method


class topGamesPanel extends JPanel
{
topGamesPanel()
{
   GroupLayout layout = new GroupLayout(this);
      setLayout(layout);
	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
	ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);







	h1.addComponent(listScroller);
	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);// was leading




	vGroup.addComponent(listScroller);

	layout.setVerticalGroup(vGroup);

}// end constructor

}



}//end class