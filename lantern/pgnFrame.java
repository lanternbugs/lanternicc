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
import javax.swing.table.TableRowSorter;


class pgnFrame extends JInternalFrame
{
	JTable gametable;
tableClass mygametable;
	channels sharedVariables;
	ConcurrentLinkedQueue<myoutput> queue;
JScrollPane listScroller;
Color listColor;
TableRowSorter<TableModel> sorter;
pgnLoader myLoader;

	//subframe [] consoleSubframes;

//subframe(JFrame frame, boolean mybool)
pgnFrame(channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1, tableClass mygametable1, pgnLoader myLoader1)
{

//super(frame, mybool);
 super(myLoader1.title,
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable
listColor = new Color(255, 255, 255);
mygametable=mygametable1;
myLoader=myLoader1;
queue=queue1;
sharedVariables = sharedVariables1;
setDefaultCloseOperation(DISPOSE_ON_CLOSE);
initComponents();
}// end constructor


void initComponents(){


//list = new JList(data); //data has type Object[]

gametable = new JTable(mygametable.gamedata);
gametable.setBackground(listColor);

gametable.setShowVerticalLines(false);
gametable.setShowHorizontalLines(false);

listScroller = new JScrollPane(gametable);
overall mypane = new overall();
mypane.setLayout();
add(mypane);
/*try {
	listScroller.setPreferredSize(new Dimension(2500, 2500));
}
catch(Exception dd){}
*/


MouseListener mouseListenerEvents = new MouseAdapter() {



     void send(String s)
     {

		 myoutput x = new myoutput();
		 x.data=s;

		 queue.add(x);
	 }

         void showGameData(int row)
         {
          String text = myLoader.games.get(row).gameData;
           Color dataBackground = new Color(235,235,235);

          Popup dataPopup = new Popup((JFrame) getDesktopPane().getTopLevelAncestor()  , false, text);
          dataPopup.setSize(650,500);
          dataPopup.field.setFont(sharedVariables.myFont);
          dataPopup.field.setBackground(dataBackground);
          dataPopup.field.setForeground(Color.BLACK);
          dataPopup.setVisible(true);


         }

	void enterExamineMode(int row)
	{
         
         if(sharedVariables.myname != null && sharedVariables.myname.length() > 1 && !sharedVariables.myname.startsWith("guest"))
         {
            String event = myLoader.games.get(row).event;
            if(event.startsWith("ICC tourney") && event.contains("(w9 "))
             send("multi Match " + sharedVariables.myname + " w9\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w17 "))
             send("multi Match " + sharedVariables.myname + " w17\n");
             else if(event.startsWith("ICC tourney") && event.contains("(w22 "))
             send("multi Match " + sharedVariables.myname + " w22\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w23 "))
             send("multi Match " + sharedVariables.myname + " w23\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w25 "))
             send("multi Match " + sharedVariables.myname + " w25\n");
              else if(event.startsWith("ICC tourney") && event.contains("(w26 "))
             send("multi Match " + sharedVariables.myname + " w26\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w27 "))
             send("multi Match " + sharedVariables.myname + " w27\n");
             else if(event.startsWith("ICC tourney") && event.contains("(w28 "))
             send("multi Match " + sharedVariables.myname + " w28\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w30 "))
             send("multi Match " + sharedVariables.myname + " w30\n");

            else if(myLoader.games.get(row).event.startsWith("ICC w9"))
             send("multi Match " + sharedVariables.myname + " w9\n");
            else if(myLoader.games.get(row).event.startsWith("ICC w17"))
             send("multi Match " + sharedVariables.myname + " w17\n");
            else if(myLoader.games.get(row).event.startsWith("ICC w22"))
             send("multi Match " + sharedVariables.myname + " w22\n");
            else if(myLoader.games.get(row).event.startsWith("ICC w23"))
             send("multi Match " + sharedVariables.myname + " w23\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w25"))
             send("multi Match " + sharedVariables.myname + " w25\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w26"))
             send("multi Match " + sharedVariables.myname + " w26\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w27"))
             send("multi Match " + sharedVariables.myname + " w27\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w28"))
             send("multi Match " + sharedVariables.myname + " w28\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w30"))
             send("multi Match " + sharedVariables.myname + " w30\n");
           else
            send("multi Examine\n");
         }// if my name
         else
         {
          send("Examine\n");
         }


        }

         void examine(int row)
	 {

                         enterExamineMode(row);
			if(myLoader.games.get(row).iccFen != null)
                          send("multi loadfen " + myLoader.games.get(row).iccFen + "\n");
			send("Setwhitename " + myLoader.games.get(row).whiteName + "\n");
			send("Setblackname " + myLoader.games.get(row).blackName + "\n");
			if(myLoader.games.get(row).whiteElo != null)
                          send("Tag WhiteElo " + myLoader.games.get(row).whiteElo + "\n");
			if(myLoader.games.get(row).blackElo != null)
                          send("Tag BlackElo " + myLoader.games.get(row).blackElo + "\n");
			send("Tag Event " + myLoader.games.get(row).event + "\n");
			send("Tag Site " + myLoader.games.get(row).site + "\n");
			send("Tag Date " + myLoader.games.get(row).date + "\n");





			for(int a=0; a<myLoader.games.get(row).moves.size() - 1; a++)// size - 1 since last thing is result we got there
				send(myLoader.games.get(row).moves.get(a) + "\n");

                        if(myLoader.games.get(row).iccResult != null)
                             send("Tag ICCResult " + myLoader.games.get(row).iccResult + "\n");
                       	else
                             send("Tag result " + myLoader.games.get(row).result + "\n");
	 }


     public void mouseClicked(MouseEvent e) {
         if (e.getClickCount() == 2) {

             JTable target = (JTable)e.getSource();
      int row = target.getSelectedRow();
      row = sorter.convertRowIndexToModel(row);
      /*int index = gametable.rowAtPoint(e.getPoint());*/
			String gameIndex = (String)gametable.getModel().getValueAt(row,0);

             if(!gameIndex.equals("-1"))
             {
			// to do add double click functionality
				examine(row);

	     }


          }// end click count two
          else if (e.getButton() == MouseEvent.BUTTON3) // right click event
          {
             JTable target = (JTable)e.getSource();
     // int row = target.getSelectedRow();
		Point p = e.getPoint();

			// get the row index that contains that coordinate
			 int rowStarting = target.rowAtPoint( p );

			// Get the ListSelectionModel of the JTable
			ListSelectionModel model = target.getSelectionModel();

			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one row.
			model.setSelectionInterval( rowStarting, rowStarting );


     final int row = sorter.convertRowIndexToModel(rowStarting);
      /*int index = gametable.rowAtPoint(e.getPoint());*/
			final String gameIndex = (String)gametable.getModel().getValueAt(row,0);

             if(!gameIndex.equals("-1"))
             {
				 String examineString="";
				 final String type1 = mygametable.type1;
				 final String type2=mygametable.type2;

				JPopupMenu menu2=new JPopupMenu("Popup2");
				JMenuItem item1 = new JMenuItem("Examine");
				 item1.addActionListener(new ActionListener() {
          		public void actionPerformed(ActionEvent e) {
          		// to do add right click examine
					examine(row);
				}

       });
			    menu2.add(item1);
				JMenuItem item2 = new JMenuItem("Save to Library");
				 item2.addActionListener(new ActionListener() {
          		public void actionPerformed(ActionEvent e) {
						// to do add libappend
						try {
                                                  examine(row);
                                                } catch(Exception dui) {
                                                  
                                                }  
						send("LibKeepExam\n");
				}

       });

				menu2.add(item2);



	JMenuItem item3 = new JMenuItem("Show Game Data");
				 item3.addActionListener(new ActionListener() {
          		public void actionPerformed(ActionEvent e) {
						// to do add libappend
					showGameData(row);
				}

       });

				menu2.add(item3);


				add(menu2);
				menu2.show(e.getComponent(),e.getX(),e.getY());



			 }// end if valid index

		  }// end right click event
     }
 };
gametable.addMouseListener(mouseListenerEvents);


/****************** add row sorter ***********************/
 sorter =  new TableRowSorter<TableModel>(mygametable.gamedata);
        gametable.setRowSorter(sorter);



/******************* end row sorter **********************/

}// end init components

class overall extends JPanel
{

	void setLayout() {
		//mypane.add(listScroller);
 GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
	ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);



	SequentialGroup h2 = layout.createSequentialGroup();



	h2.addComponent(listScroller);




h1.addGroup(h2);



	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);// was leading


SequentialGroup v1 = layout.createSequentialGroup();



		v1.addComponent(listScroller);


	vGroup.addGroup(v1);

	layout.setVerticalGroup(vGroup);

}// end set layout

public void paintComponent(Graphics g)
{

try
{
  setBackground(listColor);
}
catch(Exception dui){}
}//end paint

}


public void paintComponent(Graphics g)
{

try
{
  setBackground(listColor);
}
catch(Exception dui){}
}//end paint
}// end c