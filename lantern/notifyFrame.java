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

class notifyFrame extends JDialog// implements InternalFrameListener
{

	//subframe [] consoleSubframes;
channels sharedVariables;
JCheckBoxMenuItem notontop;
 listClass notifyList;
 ConcurrentLinkedQueue queue;
JList theNotifyList;
JScrollPane notifylistScroller;
NotifyPanel notifylistScrollerPanel;


notifyFrame(JFrame master, channels sharedVariables1, ConcurrentLinkedQueue queue1,  listClass notifyList1)
{     super(master, false);
sharedVariables=sharedVariables1;
queue=queue1;
notifyList=notifyList1;

 setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
 addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent we) {

         setVisible(false);
    }
});

setTitle("Notify Window");
initializeComponents();

// make menu

/****************** of for now no menu
JMenuBar myMenu = new JMenuBar();

 JMenu mymenu1 = new JMenu("Menu");

   notontop = new JCheckBoxMenuItem("On Top Window");
   notontop.setSelected(true);
  notontop.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            myoutput data = new myoutput();
            data.swapActivities=1;
            queue.add(data);

            }
       });
  mymenu1.add(notontop);


myMenu.add(mymenu1);
setJMenuBar(myMenu);
myMenu.setVisible(true);
*/


}// end constructor

void initializeComponents()
{
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
        c.setForeground(sharedVariables.channelColor[400]);
        else
        c.setForeground(Color.black);
        
        return c;
    }
});



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
 theNotifyList.setBackground(sharedVariables.listColor);
 notifylistScroller = new JScrollPane(theNotifyList); 
 notifylistScrollerPanel = new NotifyPanel();
// notifylistScrollerPanel.add(notifylistScroller);
 add(notifylistScrollerPanel);
} // end init components

class NotifyPanel extends JPanel
 {
  NotifyPanel()
  {
   GroupLayout layout = new GroupLayout(this);
      setLayout(layout);
	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
	ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);







	h1.addComponent(notifylistScroller);
	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);// was leading




	vGroup.addComponent(notifylistScroller);

	layout.setVerticalGroup(vGroup);


  }

 }// end notify panel


}//end class