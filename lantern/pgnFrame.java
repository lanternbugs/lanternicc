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

          Popup dataPopup = new Popup((JFrame) getDesktopPane().getTopLevelAncestor()  , false, text, sharedVariables);
          dataPopup.setSize(650,500);
          dataPopup.field.setFont(sharedVariables.myFont);
          dataPopup.field.setBackground(dataBackground);
          dataPopup.field.setForeground(Color.BLACK);
          dataPopup.setVisible(true);


         }


         int getWildNumber(String variant) 
         {
          if(variant.toLowerCase().startsWith("three checks"))
            return 25;
            if(variant.toLowerCase().startsWith("3check"))
            return 25;
            if(variant.toLowerCase().startsWith("atomic"))
            return 27;
            if(variant.toLowerCase().startsWith("crazyhouse"))
            return 23;
            if(variant.toLowerCase().startsWith("loser"))
            return 17;
            if(variant.toLowerCase().startsWith("giveaway"))
            return 26;
            if(variant.toLowerCase().startsWith("suicide"))
            return 26;
            if(variant.toLowerCase().startsWith("shatranj"))
            return 28;
            if(variant.toLowerCase().startsWith("2king"))
            return 9;
            if(variant.toLowerCase().startsWith("twokings"))
            return 9;
            if(variant.toLowerCase().startsWith("wildcastle"))
            return 1;
            if(variant.toLowerCase().startsWith("fischerandom") || variant.toLowerCase().startsWith("chess960"))
            return 22;

          return 0;
         }


	void enterExamineMode(int row)
	{

         if(sharedVariables.myname != null && sharedVariables.myname.length() > 1 && !sharedVariables.isGuest() && !channels.fics)
         {


            String event = myLoader.games.get(row).event;
            String variant = myLoader.games.get(row).variant;
            int wild = 0;
            String timeControl = "";
            if(event != null)  {
                timeControl = findTime(event);
                if(sharedVariables.myname != null && sharedVariables.myname.length() > 1 && sharedVariables.isAnon()) {
                   timeControl = timeControl.replace("r", "u");
                }
            }
            if(variant != null) {
             wild = getWildNumber(variant);
            }

            if(event == null)
             send("multi Examine\n");
             else if(wild == 17)
             send("multi Match " + sharedVariables.myname + timeControl + " w17\n");
             else if(wild == 23)
             send("multi Match " + sharedVariables.myname + timeControl + " w23\n");
             else if(wild == 25)
             send("multi Match " + sharedVariables.myname + timeControl + " w25\n");
             else if(wild == 27)
             send("multi Match " + sharedVariables.myname + timeControl + " w27\n");
             else if(wild == 26)
             send("multi Match " + sharedVariables.myname + timeControl + " w26\n");
              else if(wild == 9)
             send("multi Match " + sharedVariables.myname + timeControl + " w9\n");
             else if(wild == 28)
             send("multi Match " + sharedVariables.myname + timeControl + " w28\n");
             else if(wild == 1)
             send("multi Match " + sharedVariables.myname + timeControl + " w1\n");
             else if(wild == 22)
             send("multi Match " + sharedVariables.myname + timeControl + " w22\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w9 "))
             send("multi Match " + sharedVariables.myname + timeControl + " w9\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w17 "))
             send("multi Match " + sharedVariables.myname + timeControl + " w17\n");
             else if(event.startsWith("ICC tourney") && event.contains("(w22 "))
             send("multi Match " + sharedVariables.myname + timeControl + " w22\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w23 "))
             send("multi Match " + sharedVariables.myname + timeControl + " w23\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w25 "))
             send("multi Match " + sharedVariables.myname + timeControl + " w25\n");
              else if(event.startsWith("ICC tourney") && event.contains("(w26 "))
             send("multi Match " + sharedVariables.myname + timeControl + " w26\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w27 "))
             send("multi Match " + sharedVariables.myname + timeControl + " w27\n");
             else if(event.startsWith("ICC tourney") && event.contains("(w28 "))
             send("multi Match " + sharedVariables.myname + timeControl + " w28\n");
            else if(event.startsWith("ICC tourney") && event.contains("(w30 "))
             send("multi Match " + sharedVariables.myname + timeControl + " w30\n");

            else if(myLoader.games.get(row).event.startsWith("ICC w9"))
             send("multi Match " + sharedVariables.myname + timeControl + " w9\n");
            else if(myLoader.games.get(row).event.startsWith("ICC w17"))
             send("multi Match " + sharedVariables.myname + timeControl + " w17\n");
            else if(myLoader.games.get(row).event.startsWith("ICC w22"))
             send("multi Match " + sharedVariables.myname + timeControl + " w22\n");
            else if(myLoader.games.get(row).event.startsWith("ICC w23"))
             send("multi Match " + sharedVariables.myname + timeControl + " w23\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w25"))
             send("multi Match " + sharedVariables.myname + timeControl + " w25\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w26"))
             send("multi Match " + sharedVariables.myname + timeControl + " w26\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w27"))
             send("multi Match " + sharedVariables.myname + timeControl + " w27\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w28"))
             send("multi Match " + sharedVariables.myname + timeControl + " w28\n");
           else if(myLoader.games.get(row).event.startsWith("ICC w30"))
             send("multi Match " + sharedVariables.myname + timeControl + " w30\n");
           else if(!timeControl.equals(""))
           {
            send("multi Match " + sharedVariables.myname + timeControl + "\n");
           }
           else
            send("multi Examine\n");
         }// if my name
         else
         {
           
          String variant = myLoader.games.get(row).variant;
            int wild = 0;
            if(variant != null) {
             wild = getWildNumber(variant);
            }
            if(channels.fics) {
                send("$unexamine\n");
                if(wild == 27) {
                   send("$examine b atomic\n");
                } else if(wild == 17){
                   send("$examine b losers\n");
                } else if(wild == 23) {
                    send("$examine b crazyhouse\n");
                } else if(wild == 26) {
                    send("$examine b suicide\n");
                }
                else {
                    send("$examine\n");
                }
            } else {
                if(wild != 0) {
                   send("set wild " + wild + "\n");
                }
                 
              send("Examine\n");
              if(wild != 0) {
               send("set wild 0" + "\n");
            }
            
          }
         }


        }
        String findTime(String event)
        {
         String time = "";
         String number1 = "";
         String number2 = "";
         boolean foundUnrated = false;
         try {
         if(event.startsWith("ICC tourney")) {
          // (w26 3 1) or //(3 1)
          int start = event.indexOf("(");
          if(start > 0)
          {
           int end = event.indexOf(")");
           if(end > start)
           {
            String sub = event.substring(start+1, end);
            if(sub.startsWith("w"))
            {
             if(sub.length() > 5)
             {
              sub = sub.substring(3, sub.length());
              // it can be w9<space> or w27<space> but we trim
              }
            }
              sub = sub.trim();

              int index3 = sub.indexOf(" u");
              if(index3 > 0) {
                foundUnrated = true;
               sub = sub.replace(" u", "");
              }

              int index = sub.indexOf(" ");
              if(index > 0 && index < sub.length() - 1)  {
                 number1 = sub.substring(0, index);
                 number2= sub.substring(index+1, sub.length());
              }

          }
          }

         }  else if(event.startsWith("ICC "))
         {
            if(event.length() > 6)
            {
             String sub = event.substring(4, event.length());
             if(sub.startsWith("w"))
            {
             if(sub.length() > 5)
             {
              sub = sub.substring(3, sub.length());
              // it can be w9<space> or w27<space> but we trim
              sub = sub.trim();
             }

            }


              int index2 = sub.indexOf(" u");
              if(index2 > 0) {
                foundUnrated = true;
               sub = sub.replace(" u", "");
              }

              int index = sub.indexOf(" ");
              if(index > 0 && index < sub.length() - 1)  {
                 number1 = sub.substring(0, index);
                 number2= sub.substring(index+1, sub.length());
              }
             }

            }



         if(!number1.equals("") && !number2.equals(""))
         {
            int num1 = Integer.parseInt(number1.trim());
            int num2 = Integer.parseInt(number2.trim());
            if(num1 >= 0 && num1 < 601 && num2  >=0 && num2 < 301)
            {
             if(!(num1 == 0 && num2 == 0))
             {
               time = " " + num1 + " " + num2;
               if(foundUnrated == true) {
                time = time + " u";
               } else {
                time = time + " r"; 
               }
             }
            }
         }
         }// end try
         catch(Exception dui) { }
         return time;
        }
         void examine(int row)
	 {

                         enterExamineMode(row);
         if(!channels.fics) {
             if(myLoader.games.get(row).iccFen != null)
                           send("multi loadfen " + myLoader.games.get(row).iccFen + "\n");
                           if(sharedVariables.myname != null && sharedVariables.myname.length() > 1 && !sharedVariables.isGuest()) {
                               send("Setwhitename " + myLoader.games.get(row).whiteName + "\n");
             send("Setblackname " + myLoader.games.get(row).blackName + "\n");
                           }
             
             if(myLoader.games.get(row).whiteElo != null)
                           send("Tag WhiteElo " + myLoader.games.get(row).whiteElo + "\n");
             if(myLoader.games.get(row).blackElo != null)
                           send("Tag BlackElo " + myLoader.games.get(row).blackElo + "\n");
             send("Tag Event " + myLoader.games.get(row).event + "\n");
             send("Tag Site " + myLoader.games.get(row).site + "\n");
             send("Tag Date " + myLoader.games.get(row).date + "\n");
         } else {
             send("$wname " + sanitizeName(myLoader.games.get(row).whiteName) + "\n");
             send("$bname " + sanitizeName(myLoader.games.get(row).blackName) + "\n");
         }
			

			





			for(int a=0; a<myLoader.games.get(row).moves.size() - 1; a++)// size - 1 since last thing is result we got there
			{
                String prefix = "multi chessmove ";
                if(channels.fics) {
                    prefix = "";
                }
                String theMoveSent = prefix + myLoader.games.get(row).moves.get(a) + "\n";
                          send(theMoveSent);
                        }
                        if(channels.fics) {
                            send("$commit\n");
                        } else {
                            if(myLoader.games.get(row).iccResult != null)
                                 send("Tag ICCResult " + myLoader.games.get(row).iccResult + "\n");
                               else
                                 send("Tag result " + myLoader.games.get(row).result + "\n");
                        }
                        
	 }
    
    String sanitizeName(String name)
    {
        if(name == null) {
            name = "";
        }
        name = name.replace(",", "");
        name = name.replace(" ", "");
        if(name.length() > 15) {
        name = name.substring(0, 15);
    }

        return name;
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
