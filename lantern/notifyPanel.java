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

class notifyPanel extends JPanel// implements InternalFrameListener
{

	//subframe [] consoleSubframes;
channels sharedVariables;
 listClass notifyList;
 ConcurrentLinkedQueue queue;
JList theNotifyList;
JScrollPane notifylistScroller;



notifyPanel(channels sharedVariables1, ConcurrentLinkedQueue queue1,  listClass notifyList1)
{
sharedVariables=sharedVariables1;
queue=queue1;
notifyList=notifyList1;


initializeComponents();


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
        if (isSelected) {
           c.setBackground(new Color(135,206,250)); 
        }
        
        return c;
    }
});



MouseListener mouseListenerNotify = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {



              int index = theNotifyList.locationToIndex(e.getPoint());
             final String watchName =(String) notifyList.modeldata.elementAt(index);



 // if right click
if (e.getButton() == MouseEvent.BUTTON3 || (sharedVariables.autoHistoryPopup == true && e.getClickCount() == 2))
{
// determine their state
boolean supressLogins=sharedVariables.getNotifyControllerState(watchName);
final notifyOnTabs tabsNotify = sharedVariables.getNotifyOnTabs(watchName);

JPopupMenu menu2=new JPopupMenu("Popup2");
JMenuItem itemobserve= new JMenuItem("Observe " + watchName);
 itemobserve.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {

         String action = "`c0`" + "Observe " + watchName + "\n";
         myoutput output = new myoutput();
         output.data=action;
         output.consoleNumber=0;
         queue.add(output);
            }
       });
       menu2.add(itemobserve);


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

JMenu tabsnot = new  JMenu("Tabs To Show Notify On If Channels On Tab");
menu2.add(tabsnot);
for(int z=0; z< tabsNotify.notifyControllerTabs.size(); z++)
{
  final JCheckBoxMenuItem tempo = new JCheckBoxMenuItem("Show Notifications on Tab " + z);
  if(tabsNotify.notifyControllerTabs.get(z).equals("T"))
  tempo.setSelected(true);
  else
  tempo.setSelected(false);
  final int num = z;
  tempo.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if(tabsNotify.notifyControllerTabs.get(num).equals("T"))
            {
                    tempo.setSelected(false);
                    tabsNotify.notifyControllerTabs.set(num, "F");
                    sharedVariables.setNotifyOnTabsState();
            }
            else
            {
                    tempo.setSelected(true);
                    tabsNotify.notifyControllerTabs.set(num, "T");
                    sharedVariables.setNotifyOnTabsState();
            }
        }
       });
  tabsnot.add(tempo);
}// end for






  final JMenuItem tempo2 = new JMenuItem("Deselect All");


  tempo2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {

                    for(int c=0; c< tabsNotify.notifyControllerTabs.size(); c++)
                    tabsNotify.notifyControllerTabs.set(c, "F");
                    sharedVariables.setNotifyOnTabsState();

        }
       });
  tabsnot.add(tempo2);

 final JMenuItem tempo3 = new JMenuItem("Select All");


  tempo3.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {

                    for(int c=0; c< tabsNotify.notifyControllerTabs.size(); c++)
                    tabsNotify.notifyControllerTabs.set(c, "T");
                    sharedVariables.setNotifyOnTabsState();

        }
       });
  tabsnot.add(tempo3);

} // end if supress logins false
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

JMenuItem item2= new JMenuItem("History " + watchName);
 item2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {

         String action = "`c0`" + "History " + watchName + "\n";
         myoutput output = new myoutput();
         output.data=action;
         output.consoleNumber=0;
         queue.add(output);
            }
       });
       menu2.add(item2);
JMenuItem item3= new JMenuItem("Finger " + watchName);
 item3.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {

         String action = "`c0`" +  "Finger " + watchName + "\n";
         myoutput output = new myoutput();
         output.data=action;
         output.consoleNumber=0;
         queue.add(output);
            }
       });
       menu2.add(item3);


JMenuItem item4= new JMenuItem("Games " + watchName);
 item4.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {

         String action = "`c0`" +  "Games " + watchName + "\n";
         myoutput output = new myoutput();
         output.data=action;
         output.consoleNumber=0;
         queue.add(output);
            }
       });
       menu2.add(item4);

JMenuItem item5= new JMenuItem("Ping " + watchName);
 item5.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {

         String action = "`c0`" +  "Ping " + watchName + "\n";
         myoutput output = new myoutput();
         output.data=action;
         output.consoleNumber=0;
         queue.add(output);
            }
       });
       menu2.add(item5);


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
  makeLayout();
} // end init components



void  makeLayout()
  {
  GroupLayout layout = new GroupLayout(this);
      setLayout(layout);
	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
	ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);







	h1.addComponent(notifylistScroller, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);// was leading




	vGroup.addComponent(notifylistScroller,GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);

	layout.setVerticalGroup(vGroup);


  }




}//end class