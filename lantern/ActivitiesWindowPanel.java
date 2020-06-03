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


class ActivitiesWindowPanel extends JPanel// implements InternalFrameListener
{
	JTable theEventsList;
	JList theSeeksList;
	JList theComputerSeeksList;
	JList theChannelList;
	JList theChannelList2;
	JList theChannelList3;


	listClass eventsList;
    listClass tournamentList;
	listClass seeksList;
	listClass computerSeeksList;
	listClass notifyList;
	channels sharedVariables;
	ConcurrentLinkedQueue<myoutput> queue;

    JLabel tournamentLabel;
    JLabel eventsLabel;
	JLabel seeksLabel;
	JLabel computerSeeksLabel;
	JLabel notifyLabel;
	JLabel channelLabel;
	Color defaultLabelColor = null;
	Color selectedLabelColor = null;
        int iconWidth = 42;
//JScrollPane seeklistScroller;
//JScrollPane computerseeklistScroller;
JScrollPane listScroller;
JScrollPane channelScroller;
JScrollPane channelScroller2;
JScrollPane channelScroller3;
EventsPanel listScrollerPanel;
notifyPanel notifylistScrollerPanel;

int currentChannel = -1;
int currentChannel2 = -1;
int currentChannel3 = -1;
int JOIN_COL = 0;
int WATCH_COL = 1;
int INFO_COL = 2;
int ENTRY_COL = 3;

JPanel channelPanel=new JPanel();
seekPanel myseeks1;
seekPanel myseeks2;

GroupLayout layout;

JFrame homeFrame;

	//subframe [] consoleSubframes;

//subframe(JFrame frame, boolean mybool)
ActivitiesWindowPanel(JFrame master, channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1, listClass eventsList1, listClass seeksList1, listClass computerSeeksList1, listClass notifyList1, listClass tournamentList1, JFrame homeFrame1)
{

//super(frame, mybool);
/* super("Activities Window- double click to select",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable
  */


eventsList=eventsList1;
    tournamentList = tournamentList1;
seeksList =seeksList1;
computerSeeksList =computerSeeksList1;
notifyList = notifyList1;
sharedVariables=sharedVariables1;
queue=queue1;

myseeks1=new seekPanel(sharedVariables, queue, seekPanel.hSeeks);// 1 for  display type. show human seeks
myseeks2=new seekPanel(sharedVariables, queue, seekPanel.cSeeks);// 1 for  display type. show human seeks

//add(mypanel);
notifylistScrollerPanel = new notifyPanel(sharedVariables, queue,  notifyList);
//notifylistScrollerPanel.add(notifylistScrollerPanel.notifylistScroller);
initComponents();

homeFrame=homeFrame1;
//addInternalFrameListener(this);
}// end constructor

void setColors()
{
theEventsList.setBackground(sharedVariables.listColor);
theSeeksList.setBackground(sharedVariables.listColor);
theComputerSeeksList.setBackground(sharedVariables.listColor);
notifylistScrollerPanel.theNotifyList.setBackground(sharedVariables.listColor);
//theChannelList.setBackground(sharedVariables.listColor);

}

void setEventTournamentTableProperties()
    {
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

    }
    
void initComponents(){

/*try {
displayList = new listClass();
add(displayList.theList);
}catch(Exception d) { }
*/
eventsLabel = new JLabel("Events List", SwingConstants.CENTER);
tournamentLabel = new JLabel("Tournaments", SwingConstants.CENTER);
seeksLabel = new JLabel("Human Seeks", SwingConstants.CENTER);
computerSeeksLabel = new JLabel(" Computer Seeks", SwingConstants.CENTER);
notifyLabel = new JLabel(" Notify List", SwingConstants.CENTER);
channelLabel = new JLabel(" Channel List    ");

tournamentLabel.setOpaque(true);
eventsLabel.setOpaque(true);
seeksLabel.setOpaque(true);
computerSeeksLabel.setOpaque(true);
notifyLabel.setOpaque(true);

defaultLabelColor = this.getBackground();
selectedLabelColor = new Color(176,196,222);
//list = new JList(data); //data has type Object[]
    theEventsList = new JTable(eventsList.eventsTable)
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

    setEventTournamentTableProperties();
listScroller = new JScrollPane(theEventsList);
listScrollerPanel = new EventsPanel();
listScrollerPanel.add(listScroller);
/* grab font*/
/*	String aFont=theEventsList.getFont().getFontName();
	aFont=aFont.replace(" ", "*");


        JFrame fontFrame = new JFrame("" + aFont + " " +  theEventsList.getFont().getSize());
         fontFrame.setSize(500,100);
         fontFrame.setVisible(true);
*/
//listScroller.setPreferredSize(new Dimension(2500, 2500));

/********* now seeks list *****************/
theSeeksList = new JList(seeksList.model);
theSeeksList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
theSeeksList.setLayoutOrientation(JList.VERTICAL);
theSeeksList.setVisibleRowCount(-1);
/********* now computer seeks list *****************/
theComputerSeeksList = new JList(computerSeeksList.model);
theComputerSeeksList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
theComputerSeeksList.setLayoutOrientation(JList.VERTICAL);
theComputerSeeksList.setVisibleRowCount(-1);


/********* now channel list *****************/

nameListClass listclasstype = new nameListClass();
listclasstype.addToList("Click to Rotate Channels");
theChannelList = new JList(listclasstype.model);
theChannelList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
theChannelList.setLayoutOrientation(JList.VERTICAL);
theChannelList.setVisibleRowCount(-1);

theChannelList2 = new JList(listclasstype.model);
theChannelList2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
theChannelList2.setLayoutOrientation(JList.VERTICAL);
theChannelList2.setVisibleRowCount(-1);

theChannelList3 = new JList(listclasstype.model);
theChannelList3.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
theChannelList3.setLayoutOrientation(JList.VERTICAL);
theChannelList3.setVisibleRowCount(-1);


setColors();


/********************* channel mouse listeners (3) ************/
MouseListener mouseListenerChannel = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {
     try {

       if(sharedVariables.channelNamesList.size()==0)
       return;

      int mychan=0;
    if (e.getButton() == MouseEvent.BUTTON3)
{
      if(currentChannel > 0)
      mychan=currentChannel-1;
      else if(mychan == 0)
      mychan= sharedVariables.channelNamesList.size() - 1;

}
else
{    if(currentChannel > -1)
      mychan=currentChannel+1;
      if(mychan >= sharedVariables.channelNamesList.size())
      mychan=0;
}
     currentChannel=mychan;

   theChannelList.setModel(sharedVariables.channelNamesList.get(mychan).model2);
     }// end try
     catch(Exception dui){}
     }
};
theChannelList.addMouseListener(mouseListenerChannel);


/************** #2 ***************************/
MouseListener mouseListenerChannel2 = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {
     try {

       if(sharedVariables.channelNamesList.size()==0)
       return;

      int mychan=0;
  if (e.getButton() == MouseEvent.BUTTON3)
{
      if(currentChannel2 > 0)
      mychan=currentChannel2-1;
      else if(mychan == 0)
      mychan= sharedVariables.channelNamesList.size() - 1;

}
else
{
      if(currentChannel2 > -1)
      mychan=currentChannel2+1;
      if(mychan >= sharedVariables.channelNamesList.size())
      mychan=0;
}
     currentChannel2=mychan;

   theChannelList2.setModel(sharedVariables.channelNamesList.get(mychan).model2);
     }// end try
     catch(Exception dui){}
     }
};
theChannelList2.addMouseListener(mouseListenerChannel2);


/************* #3 **************************/
MouseListener mouseListenerChannel3 = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {
     try {

       if(sharedVariables.channelNamesList.size()==0)
       return;

      int mychan=0;

if (e.getButton() == MouseEvent.BUTTON3)
{
      if(currentChannel3 > 0)
      mychan=currentChannel3-1;
      else if(mychan == 0)
      mychan= sharedVariables.channelNamesList.size() - 1;

}
else
{
      if(currentChannel3 > -1)
      mychan=currentChannel3+1;
      if(mychan >= sharedVariables.channelNamesList.size())
      mychan=0;
}
     currentChannel3=mychan;

   theChannelList3.setModel(sharedVariables.channelNamesList.get(mychan).model2);
     }// end try
     catch(Exception dui){}
     }
};
theChannelList3.addMouseListener(mouseListenerChannel3);


/********************* end channel mouse listeners ****************/





MouseListener mouseListenerSeeks = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {
         if (e.getClickCount() == 2) {
             int index = theSeeksList.locationToIndex(e.getPoint());

             String play = seeksList.getOfferNumber(index);
             if(!play.equals("-1"))
             {
				 myoutput output = new myoutput();
				 output.data="play " + play + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);
		 	 }
             //seekDialog aDialog;
			 //aDialog= new seekDialog(homeFrame, false,"Selected from " +  index );
			 //aDialog.setVisible(true);

          }
     }
 };
 theSeeksList.addMouseListener(mouseListenerSeeks);


MouseListener mouseListenerComputerSeeks = new MouseAdapter() {

     public void mouseClicked(MouseEvent e) {
         if (e.getClickCount() == 2) {
             int index = theComputerSeeksList.locationToIndex(e.getPoint());

             String play = computerSeeksList.getOfferNumber(index);
             if(!play.equals("-1"))
             {
				 myoutput output = new myoutput();
				 output.data="play " + play + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);
		 	 }
             //seekDialog aDialog;
			 //aDialog= new seekDialog(homeFrame, false,"Selected from " +  index );
			 //aDialog.setVisible(true);

          }
     }
 };
 theComputerSeeksList.addMouseListener(mouseListenerComputerSeeks);
    
    
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
         if(sharedVariables.activitiesTabNumber == 4) {
             listing = tournamentList.getEventListing(row);
             join = tournamentList.getJoinCommand(row);
             info = tournamentList.getInfoCommand(row);
             watch = tournamentList.getWatchCommand(row);
         }
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



//old list listener
/*MouseListener mouseListenerEvents = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {


         if (e.getClickCount() == 2) {
             int index = theEventsList.locationToIndex(e.getPoint());
			String listing = eventsList.getEventListing(index);
             String join = eventsList.getJoinCommand(index);
             String info = eventsList.getInfoCommand(index);
             String watch = eventsList.getWatchCommand(index);

             boolean go=false;
             if(listing.equals("-"))
             return;
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
                data.data=watch + "\n";
                queue.add(data);
                go=true;

             }
             if(watch.equals("!!!") && info.equals("!!!") && join.toLowerCase().startsWith("examine "))
             {
                myoutput data = new myoutput();
                data.consoleNumber = 0;
                data.data=join + "\n";
                queue.add(data);
                go=true;

             }
             if(go == false)
             {
               eventDialog aDialog;
			 aDialog= new  eventDialog(homeFrame, false, listing, join, info, watch);
			 aDialog.setSize(350,200);
			 aDialog.setVisible(true);
             }// if go==false


          }
     }
 };     */
 theEventsList.addMouseListener(mouseListenerEvents);
channelScroller = new JScrollPane(theChannelList);
channelScroller2 = new JScrollPane(theChannelList2);
channelScroller3 = new JScrollPane(theChannelList3);
//seeklistScroller = new JScrollPane(theSeeksList);
//computerseeklistScroller = new JScrollPane(theComputerSeeksList);
//listScroller.setPreferredSize(new Dimension(2500, 2500));
setLayout();


// set default visible
if(sharedVariables.activitiesTabNumber != 0 && sharedVariables.activitiesTabNumber != 4)
	listScrollerPanel.setVisible(false);
if(sharedVariables.activitiesTabNumber != 1)
	myseeks1.setVisible(false);
if(sharedVariables.activitiesTabNumber != 2)
	myseeks2.setVisible(false);
if(sharedVariables.activitiesTabNumber != 3)
	notifylistScrollerPanel.setVisible(false);
if(sharedVariables.activitiesTabNumber != 4)
	channelPanel.setVisible(false);
setLabelSelected(sharedVariables.activitiesTabNumber);

    tournamentLabel.addMouseListener(new MouseAdapter() {
             public void mousePressed(MouseEvent e) {
                 // turn on events and off seeks
               //  if(!listScroller.isVisible())


                 switchToTournaments();


                }


             public void mouseReleased(MouseEvent e) {
                // turn on events and off seeks
              /*  if(!listScroller.isVisible())
                 {
                     seeklistScroller.setVisible(false);
                     notifylistScroller.setVisible(false);
                     computerseeklistScroller.setVisible(false);
                     listScroller.setVisible(true);
                     paintComponents(getGraphics()); repaint();

                 }
              */
              }


    public void mouseEntered (MouseEvent me) {}
    public void mouseExited (MouseEvent me) {}
    public void mouseClicked (MouseEvent me) {}
    });

    
eventsLabel.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
             // turn on events and off seeks
           //  if(!listScroller.isVisible())


				 notifylistScrollerPanel.setVisible(false);
				 myseeks2.setVisible(false);
				 myseeks1.setVisible(false);
				  channelPanel.setVisible(false);
                                   listScrollerPanel.setVisible(true);
                                   listScroller.setVisible(true);
				 sharedVariables.activitiesTabNumber=0;
				 setLabelSelected(sharedVariables.activitiesTabNumber);
             theEventsList.setModel(eventsList.eventsTable);
             setEventTournamentTableProperties();



				 paintComponents(getGraphics()); repaint();


            }


         public void mouseReleased(MouseEvent e) {
            // turn on events and off seeks
          /*  if(!listScroller.isVisible())
             {
				 seeklistScroller.setVisible(false);
				 notifylistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(false);
				 listScroller.setVisible(true);
				 paintComponents(getGraphics()); repaint();

			 }
          */
          }


public void mouseEntered (MouseEvent me) {}
public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {}
});

seeksLabel.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
             // turn on events and off seeks
             //if(!seeklistScroller.isVisible())

				 listScrollerPanel.setVisible(false);
				 notifylistScrollerPanel.setVisible(false);
				 myseeks2.setVisible(false);

				 channelPanel.setVisible(false);
				  myseeks1.setVisible(true);
				 sharedVariables.activitiesTabNumber=1;
				 setLabelSelected(sharedVariables.activitiesTabNumber);
				 paintComponents(getGraphics()); repaint();


            }


         public void mouseReleased(MouseEvent e) {
            // turn on events and off seeks
         /*  if(!listScroller.isVisible())
             {
				 listScroller.setVisible(false);
				 notifylistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(false);
				 seeklistScroller.setVisible(true);
				 paintComponents(getGraphics()); repaint();

			 }
         */
         }


public void mouseEntered (MouseEvent me) {}
public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {}
});

notifyLabel.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
             // turn on events and off seeks
             //if(!seeklistScroller.isVisible())
                                   // JFrame tempo1 = new JFrame("1");
                                   // tempo1.setVisible(true);
                                   // tempo1.setSize(100,100);
				 listScrollerPanel.setVisible(false);
				 myseeks1.setVisible(false);
				 myseeks2.setVisible(false);

				 channelPanel.setVisible(false);
				  notifylistScrollerPanel.setVisible(true);
				  notifylistScrollerPanel.notifylistScroller.setVisible(true);
				 sharedVariables.activitiesTabNumber=3;
				 setLabelSelected(sharedVariables.activitiesTabNumber);
				   notifylistScrollerPanel.repaint();
                                   paintComponents(getGraphics());
                                    repaint();
                                  //  JFrame tempo2 = new JFrame("2");
                                   // tempo2.setVisible(true);
                                   // tempo2.setSize(100,100);

            }


         public void mouseReleased(MouseEvent e) {
            // turn on events and off seeks
           /* if(!listScroller.isVisible())
             {
				 listScroller.setVisible(false);
				 seeklistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(false);
				 notifylistScroller.setVisible(true);
				 paintComponents(getGraphics()); repaint();

			 }

         */
         }


public void mouseEntered (MouseEvent me) {}
public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {}
});



computerSeeksLabel.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
             // turn on events and off seeks
             //if(!seeklistScroller.isVisible())

				 listScrollerPanel.setVisible(false);
				 myseeks1.setVisible(false);
				 notifylistScrollerPanel.setVisible(false);
				 channelPanel.setVisible(false);
				 myseeks2.setVisible(true);
				 sharedVariables.activitiesTabNumber=2;
				 setLabelSelected(sharedVariables.activitiesTabNumber);
				 paintComponents(getGraphics()); repaint();

            }


         public void mouseReleased(MouseEvent e) {
            // turn on events and off seeks
           /* if(!listScroller.isVisible())
             {
				 listScroller.setVisible(false);
				 seeklistScroller.setVisible(false);
				  notifylistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(true);
				paintComponents(getGraphics());

			 }
         */
         }


public void mouseEntered (MouseEvent me) {}
public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {}
});

channelLabel.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
             // turn on events and off seeks
             //if(!seeklistScroller.isVisible())

				 listScrollerPanel.setVisible(false);
				 myseeks1.setVisible(false);
				 notifylistScrollerPanel.setVisible(false);

				 myseeks2.setVisible(false);
				 channelPanel.setVisible(true);
				 sharedVariables.activitiesTabNumber=4;
				 setLabelSelected(sharedVariables.activitiesTabNumber);
				 paintComponents(getGraphics()); repaint();

            }


         public void mouseReleased(MouseEvent e) {
            // turn on events and off seeks
           /* if(!listScroller.isVisible())
             {
				 listScroller.setVisible(false);
				 seeklistScroller.setVisible(false);
				  notifylistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(true);
				paintComponents(getGraphics());

			 }
         */
         }


public void mouseEntered (MouseEvent me) {}
public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {}
});


}// end inti components

    void switchToTournaments()
    {
        notifylistScrollerPanel.setVisible(false);
                            myseeks2.setVisible(false);
                            myseeks1.setVisible(false);
                             channelPanel.setVisible(false);
                                              listScrollerPanel.setVisible(true);
                                              listScroller.setVisible(true);
                            sharedVariables.activitiesTabNumber=4;
                            setLabelSelected(sharedVariables.activitiesTabNumber);
                        theEventsList.setModel(tournamentList.eventsTable);
                        setEventTournamentTableProperties();



                            paintComponents(getGraphics()); repaint();
    }
    
void setLabelSelected(int num)
{
   if(num != 0)
     eventsLabel.setBackground(defaultLabelColor);
   else 
     eventsLabel.setBackground(selectedLabelColor);
     
   if(num != 1 )
     seeksLabel.setBackground(defaultLabelColor);
   else 
     seeksLabel.setBackground(selectedLabelColor);

   if(num != 2 )
     computerSeeksLabel.setBackground(defaultLabelColor);
   else 
     computerSeeksLabel.setBackground(selectedLabelColor);

   if(num != 3 )
     notifyLabel.setBackground(defaultLabelColor);
   else 
     notifyLabel.setBackground(selectedLabelColor);
    if(num != 4)
      tournamentLabel.setBackground(defaultLabelColor);
    else
      tournamentLabel.setBackground(selectedLabelColor);

}
void setLayout()
{
//add(listScroller);

channelPanel.setLayout(new GridLayout(1,3));
channelPanel.add(channelScroller);
channelPanel.add(channelScroller2);
channelPanel.add(channelScroller3);
 GroupLayout layout = new GroupLayout(this);
      setLayout(layout);
	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
	ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);



	SequentialGroup h2 = layout.createSequentialGroup();
	SequentialGroup h3 = layout.createSequentialGroup();
	SequentialGroup h4 = layout.createSequentialGroup();
	SequentialGroup h5 = layout.createSequentialGroup();
	SequentialGroup h6 = layout.createSequentialGroup();
 	SequentialGroup h7 = layout.createSequentialGroup();



	h2.addComponent(seeksLabel,GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
	h2.addComponent(computerSeeksLabel,GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
	h2.addComponent(eventsLabel,GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
	h2.addComponent(notifyLabel,GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
    h2.addComponent(tournamentLabel,GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
	//h2.addComponent(channelLabel);

	h3.addComponent(myseeks1);
	h4.addComponent(listScrollerPanel);
	h5.addComponent(notifylistScrollerPanel);
	h6.addComponent(myseeks2);
	//h7.addComponent(channelPanel);



h1.addGroup(h2);

h1.addGroup(h3);
h1.addGroup(h4);
h1.addGroup(h5);
h1.addGroup(h6);
//h1.addGroup(h7);

	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);// was leading

	ParallelGroup v4 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);

SequentialGroup v1 = layout.createSequentialGroup();

SequentialGroup v2 = layout.createSequentialGroup();
SequentialGroup v3 = layout.createSequentialGroup();
SequentialGroup v44 = layout.createSequentialGroup();

SequentialGroup v33 = layout.createSequentialGroup();
SequentialGroup v7 = layout.createSequentialGroup();
SequentialGroup v8 = layout.createSequentialGroup();
ParallelGroup v9 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);


v9.addComponent(eventsLabel);
v9.addComponent(notifyLabel);
v9.addComponent(seeksLabel);
v9.addComponent(computerSeeksLabel);
    v9.addComponent(tournamentLabel);
//v9.addComponent(channelLabel);
		v1.addGroup(v9);
		v1.addComponent(listScrollerPanel);

		v2.addGroup(v9);

		v2.addComponent(myseeks1);


v3.addGroup(v9);
v3.addComponent(notifylistScrollerPanel);
//v33.addGroup(v9);
//v33.addComponent(channelPanel);
v44.addGroup(v9);
v44.addComponent(myseeks2);

v4.addGroup(v1);

v4.addGroup(v2);
v4.addGroup(v3);
v4.addGroup(v44);
//v4.addGroup(v33);

	vGroup.addGroup(v4);

	layout.setVerticalGroup(vGroup);

}// end set layout






// seek display dialog

class seekDialog extends JDialog
{

seekDialog(JFrame frame, boolean mybool, String text)
{
super((JFrame)frame, mybool);

setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	JPanel pane = new JPanel();
	add(pane);

	JButton button = new JButton(text);
	pane.add(button);
}//end dialog constructor
}// end dialog class


class eventDialog extends JDialog
{
String join1;
String join2;

eventDialog(JFrame frame, boolean mybool, String event, final String join, final String info, final String watch)
{
super((JFrame)frame, mybool);

setDefaultCloseOperation(DISPOSE_ON_CLOSE);

join1=join;
join2="";

if(join.indexOf(" & ")!=-1)
{
	int spot = join.indexOf(" & ");
	try {
	join1=join.substring(0, spot);
	join2=join.substring(spot + 3, join.length() );
	}catch(Exception f){}
}
	JPanel pane = new JPanel();
	add(pane);
	pane.setLayout(new GridLayout(2,1));
	JPanel labelPane = new JPanel();
	labelPane.setLayout(new GridLayout(2,1));
	JLabel eventlabel = new JLabel(event);
	labelPane.add(eventlabel);
	JLabel instructions = new JLabel("Click available buttons below for actions.");
	labelPane.add(instructions);
	pane.add(labelPane);

	JPanel buttonPane = new JPanel();
	JButton buttonjoin = new JButton("Join with: " + join);
	if(!join.equals("!!!"))
	buttonPane.add(buttonjoin);
	JButton buttoninfo = new JButton("Info with: " + info);
	if(!info.equals("!!!"))
	buttonPane.add(buttoninfo);
	JButton buttonwatch = new JButton("Watch with: " + watch);
	if(!watch.equals("!!!"))
	buttonPane.add(buttonwatch);
	JButton cancelButton = new JButton("Cancel");
	buttonPane.add(cancelButton);

	// action listeners

	buttonjoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
                              /* if(join1.startsWith("https:") && join1.contains("webcast"))
                               if(sharedVariables.myname.length() > 0 && sharedVariables.mypassword.length() > 0)
                                {
                                 sharedVariables.openUrl(join1 + "?user=" + sharedVariables.myname + "&pass=" + sharedVariables.mypassword);
                                 return;
                                }
                                */

                                joinMethod(join1, join2);

						dispose();
			}});

	buttoninfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{


                                         infoMethod(info);
						dispose();
			}});
	buttonwatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{

                         watchMethod(watch);

						dispose();
			}});

	cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event)
					{

						dispose();
				}});


	pane.add(buttonPane);

}//end dialog constructor
}// end dialog class
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
/*void sharedVariables.openUrl(String myurl)
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
 */
 class EventsPanel extends JPanel
 {
  EventsPanel()
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


  }

 }




 
 public void paintComponent(Graphics g)
{

try
{
	super.paintComponent(g);
	//if(seeklistScroller.isVisible())
	//seeklistScroller.setBackground(sharedVariables.listColor);
//	if(computerseeklistScroller.isVisible())
//	computerseeklistScroller.setBackground(sharedVariables.listColor);

	 if(listScroller.isVisible())
	listScroller.setBackground(sharedVariables.listColor);
	else if(notifylistScrollerPanel.notifylistScroller.isVisible())
	notifylistScrollerPanel.notifylistScroller.setBackground(sharedVariables.listColor);
}
catch(Exception e){}
}//end paint components


}//end class
