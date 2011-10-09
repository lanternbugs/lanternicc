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

class listFrame extends JDialog// implements InternalFrameListener
{
	JList theEventsList;
	JList theSeeksList;
	JList theComputerSeeksList;
	JList theNotifyList;
	JList theChannelList;
	JList theChannelList2;
	JList theChannelList3;


	listClass eventsList;
	listClass seeksList;
	listClass computerSeeksList;
	listClass notifyList;
	channels sharedVariables;
	ConcurrentLinkedQueue<myoutput> queue;

        JLabel eventsLabel;
	JLabel seeksLabel;
	JLabel computerSeeksLabel;
	JLabel notifyLabel;
	JLabel channelLabel;
	overallPanel overall;
JScrollPane notifylistScroller;
JScrollPane seeklistScroller;
JScrollPane computerseeklistScroller;
JScrollPane listScroller;
JScrollPane channelScroller;
JScrollPane channelScroller2;
JScrollPane channelScroller3;

int currentChannel = -1;
int currentChannel2 = -1;
int currentChannel3 = -1;

JPanel channelPanel=new JPanel();

GroupLayout layout;

JFrame homeFrame;

	//subframe [] consoleSubframes;

//subframe(JFrame frame, boolean mybool)
listFrame(JFrame master, channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1, listClass eventsList1, listClass seeksList1, listClass computerSeeksList1, listClass notifyList1, JFrame homeFrame1)
{

//super(frame, mybool);
/* super("Activities Window- double click to select",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable
  */
  super(master, false);
 setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
 addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent we) {
	if(isVisible() && getMaximumSize() != getSize() && getMinimumSize() != getSize())
	{
		setBoardSize();
	}
          dispose();
    }
});

eventsList=eventsList1;
seeksList =seeksList1;
computerSeeksList =computerSeeksList1;
notifyList = notifyList1;
sharedVariables=sharedVariables1;
queue=queue1;
setDefaultCloseOperation(DISPOSE_ON_CLOSE);
overall = new overallPanel();
initComponents();
homeFrame=homeFrame1;
//addInternalFrameListener(this);
}// end constructor

void setColors()
{
theEventsList.setBackground(sharedVariables.listColor);
theSeeksList.setBackground(sharedVariables.listColor);
theComputerSeeksList.setBackground(sharedVariables.listColor);
theNotifyList.setBackground(sharedVariables.listColor);
//theChannelList.setBackground(sharedVariables.listColor);

}


void initComponents(){

/*try {
displayList = new listClass();
add(displayList.theList);
}catch(Exception d) { }
*/
eventsLabel = new JLabel("Events List    ");

seeksLabel = new JLabel("Human Seeks    ");
computerSeeksLabel = new JLabel(" Computer Seeks    ");
notifyLabel = new JLabel(" Notify List    ");
channelLabel = new JLabel(" Channel List    ");

//list = new JList(data); //data has type Object[]
theEventsList = new JList(eventsList.model);
theEventsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
theEventsList.setLayoutOrientation(JList.VERTICAL);
theEventsList.setVisibleRowCount(-1);
listScroller = new JScrollPane(theEventsList);
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

/********* now notify list *****************/
theNotifyList = new JList(notifyList.model);
theNotifyList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
theNotifyList.setLayoutOrientation(JList.VERTICAL);
theNotifyList.setVisibleRowCount(-1);
theNotifyList.setCellRenderer(new DefaultListCellRenderer() {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
       Component c = super.getListCellRendererComponent(
            list,value,index,isSelected,cellHasFocus);
       if( value.toString().contains("Playing"))
        c.setForeground(Color.red);
        else
        c.setForeground(Color.black);
        
        return c;
    }
});

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
      if(currentChannel > -1)
      mychan=currentChannel+1;
      if(mychan >= sharedVariables.channelNamesList.size())
      mychan=0;

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
      if(currentChannel2 > -1)
      mychan=currentChannel2+1;
      if(mychan >= sharedVariables.channelNamesList.size())
      mychan=0;

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
      if(currentChannel3 > -1)
      mychan=currentChannel3+1;
      if(mychan >= sharedVariables.channelNamesList.size())
      mychan=0;

     currentChannel3=mychan;

   theChannelList3.setModel(sharedVariables.channelNamesList.get(mychan).model2);
     }// end try
     catch(Exception dui){}
     }
};
theChannelList3.addMouseListener(mouseListenerChannel3);


/********************* end channel mouse listeners ****************/


MouseListener mouseListenerNotify = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {



              int index = theNotifyList.locationToIndex(e.getPoint());
             final String watchName =(String) notifyList.modeldata.elementAt(index);



 // if right click
if (e.getButton() == MouseEvent.BUTTON3)
{
// determine their state
boolean supressLogins=sharedVariables.getNotifyControllerState(watchName);

JPopupMenu menu2=new JPopupMenu("Popup2");
JMenuItem item1;
if(supressLogins == false)
{

item1= new JMenuItem("Suppress Login Logout Messages");
 item1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            sharedVariables.notifyControllerScript.add(watchName);
            sharedVariables.setNotifyControllerState();
            }
       });
       menu2.add(item1);

}
else
{
item1= new JMenuItem("Enable Login Logout Messages");
 item1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            sharedVariables.notifyControllerScript.remove(watchName);
           sharedVariables.setNotifyControllerState();

            }
       });
       menu2.add(item1);


}
add(menu2);
menu2.show(e.getComponent(),e.getX(),e.getY());


}// end if right click
         else if (e.getClickCount() == 2) {





             String watch = "Observe " + watchName + "\n";

				 myoutput output = new myoutput();
				 output.data=watch;

				 output.consoleNumber=0;
      			 queue.add(output);

             //seekDialog aDialog;
			 //aDialog= new seekDialog(homeFrame, false,"Selected from " +  index );
			 //aDialog.setVisible(true);

          }
     }
 };
 theNotifyList.addMouseListener(mouseListenerNotify);




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
         if (e.getClickCount() == 2) {
             int index = theEventsList.locationToIndex(e.getPoint());
			String listing = eventsList.getEventListing(index);
             String join = eventsList.getJoinCommand(index);
             String info = eventsList.getInfoCommand(index);
             String watch = eventsList.getWatchCommand(index);
             /*if(!play.equals("-1"))
             {
				 myoutput output = new myoutput();
				 output.data="play " + play + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);
		 	 }*/
             
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
                 openUrl(info);
                }
                }
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
 };
 theEventsList.addMouseListener(mouseListenerEvents);
channelScroller = new JScrollPane(theChannelList);
channelScroller2 = new JScrollPane(theChannelList2);
channelScroller3 = new JScrollPane(theChannelList3);
seeklistScroller = new JScrollPane(theSeeksList);
computerseeklistScroller = new JScrollPane(theComputerSeeksList);
notifylistScroller = new JScrollPane(theNotifyList);
//listScroller.setPreferredSize(new Dimension(2500, 2500));
overall.setLayout();


// set default visible
if(sharedVariables.activitiesTabNumber != 0)
	listScroller.setVisible(false);
if(sharedVariables.activitiesTabNumber != 1)
	seeklistScroller.setVisible(false);
if(sharedVariables.activitiesTabNumber != 2)
	computerseeklistScroller.setVisible(false);
if(sharedVariables.activitiesTabNumber != 3)
	notifylistScroller.setVisible(false);
if(sharedVariables.activitiesTabNumber != 4)
	channelPanel.setVisible(false);


eventsLabel.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
             // turn on events and off seeks
           //  if(!listScroller.isVisible())

				 listScroller.setVisible(true);
				 notifylistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(false);
				 seeklistScroller.setVisible(false);
				  channelPanel.setVisible(false);

				 sharedVariables.activitiesTabNumber=0;



				 paintComponents(getGraphics());


            }


         public void mouseReleased(MouseEvent e) {
            // turn on events and off seeks
          /*  if(!listScroller.isVisible())
             {
				 seeklistScroller.setVisible(false);
				 notifylistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(false);
				 listScroller.setVisible(true);
				 paintComponents(getGraphics());

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

				 listScroller.setVisible(false);
				 notifylistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(false);
				 seeklistScroller.setVisible(true);
				 channelPanel.setVisible(false);
				 sharedVariables.activitiesTabNumber=1;
				 paintComponents(getGraphics());


            }


         public void mouseReleased(MouseEvent e) {
            // turn on events and off seeks
         /*  if(!listScroller.isVisible())
             {
				 listScroller.setVisible(false);
				 notifylistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(false);
				 seeklistScroller.setVisible(true);
				 paintComponents(getGraphics());

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

				 listScroller.setVisible(false);
				 seeklistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(false);
				 notifylistScroller.setVisible(true);
				 channelPanel.setVisible(false);
				 sharedVariables.activitiesTabNumber=3;
				 paintComponents(getGraphics());

            }


         public void mouseReleased(MouseEvent e) {
            // turn on events and off seeks
           /* if(!listScroller.isVisible())
             {
				 listScroller.setVisible(false);
				 seeklistScroller.setVisible(false);
				 computerseeklistScroller.setVisible(false);
				 notifylistScroller.setVisible(true);
				 paintComponents(getGraphics());

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

				 listScroller.setVisible(false);
				 seeklistScroller.setVisible(false);
				 notifylistScroller.setVisible(false);
				 channelPanel.setVisible(false);
				 computerseeklistScroller.setVisible(true);
				 sharedVariables.activitiesTabNumber=2;
				 paintComponents(getGraphics());

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

				 listScroller.setVisible(false);
				 seeklistScroller.setVisible(false);
				 notifylistScroller.setVisible(false);
				 channelPanel.setVisible(true);
				 computerseeklistScroller.setVisible(false);
				 sharedVariables.activitiesTabNumber=4;
				 paintComponents(getGraphics());

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

add(overall);

}// end inti components

class overallPanel extends JPanel
{
void setLayout()
{
//add(listScroller);

channelPanel.setLayout(new GridLayout(1,3));
channelPanel.add(channelScroller);
channelPanel.add(channelScroller2);
channelPanel.add(channelScroller3);

 layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
	ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);



	SequentialGroup h2 = layout.createSequentialGroup();
	SequentialGroup h3 = layout.createSequentialGroup();
	SequentialGroup h4 = layout.createSequentialGroup();
	SequentialGroup h5 = layout.createSequentialGroup();
	SequentialGroup h6 = layout.createSequentialGroup();
 	SequentialGroup h7 = layout.createSequentialGroup();



	h2.addComponent(seeksLabel);
	h2.addComponent(computerSeeksLabel);
	h2.addComponent(eventsLabel);
	h2.addComponent(notifyLabel);
	h2.addComponent(channelLabel);

	h3.addComponent(seeklistScroller);
	h4.addComponent(listScroller);
	h5.addComponent(notifylistScroller);
	h6.addComponent(computerseeklistScroller);
	h7.addComponent(channelPanel);



h1.addGroup(h2);

h1.addGroup(h3);
h1.addGroup(h4);
h1.addGroup(h5);
h1.addGroup(h6);
h1.addGroup(h7);

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
v9.addComponent(channelLabel);
		v1.addGroup(v9);
		v1.addComponent(listScroller);

		v2.addGroup(v9);

		v2.addComponent(seeklistScroller);


v3.addGroup(v9);
v3.addComponent(notifylistScroller);
v33.addGroup(v9);
v33.addComponent(channelPanel);
v44.addGroup(v9);
v44.addComponent(computerseeklistScroller);

v4.addGroup(v1);

v4.addGroup(v2);
v4.addGroup(v3);
v4.addGroup(v44);
v4.addGroup(v33);

	vGroup.addGroup(v4);

	layout.setVerticalGroup(vGroup);

}// end set layout
public void paintComponent(Graphics g)
{

try
{
	super.paintComponent(g);
	if(seeklistScroller.isVisible())
	seeklistScroller.setBackground(sharedVariables.listColor);
	else if(computerseeklistScroller.isVisible())
	computerseeklistScroller.setBackground(sharedVariables.listColor);

	else if(listScroller.isVisible())
	listScroller.setBackground(sharedVariables.listColor);
	else if(notifylistScroller.isVisible())
	notifylistScroller.setBackground(sharedVariables.listColor);
}
catch(Exception e){}
}//end paint components



}//end overall class


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
                                if(join1.startsWith("http"))
                                {
                                 openUrl(join1);
                                 return;
                                }


				 myoutput output = new myoutput();
				 output.data=join1 + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);

      			 if(!join2.equals(""))
      			 {
				 output = new myoutput();
				 output.data=join2 + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);

				 }

						dispose();
			}});

	buttoninfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				
                                
                                if(info.startsWith("http"))
                                {
                                 openUrl(info);
                                 return;
                                }
                                 myoutput output = new myoutput();
				 output.data=info + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);

						dispose();
			}});
	buttonwatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				 myoutput output = new myoutput();
				 output.data=watch + "\n";

				 output.consoleNumber=0;
      			 queue.add(output);

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

/************** jinternal frame listener ******************************/

    void setBoardSize()
     {
		sharedVariables.myActivitiesSizes.point0=getLocation();
		//set_string = set_string + "" + point0.x + " " + point0.y + " ";
		sharedVariables.myActivitiesSizes.con0x=getWidth();
		sharedVariables.myActivitiesSizes.con0y=getHeight();
		//set_string = set_string + "" + con0x + " " + con0y + " ";

	 }
/*

      public void internalFrameClosing(InternalFrameEvent e) {
	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
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
	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
		setBoardSize();
	}
	   }

    public void internalFrameActivated(final InternalFrameEvent e) {
     // System.out.println("fame activate");
	if(isVisible() && isMaximum() == false && isIcon() == false)
	{
		setBoardSize();
	}



    }

    public void internalFrameDeactivated(InternalFrameEvent e) {


    }

*/

/****************************************************************************************/
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


}//end class