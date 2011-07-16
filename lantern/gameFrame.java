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
import javax.swing.filechooser.FileFilter;


class gameFrame extends JFrame
{
	JTable gametable;
tableClass mygametable;
	channels sharedVariables;
	ConcurrentLinkedQueue<myoutput> queue;
JScrollPane listScroller;
Color listColor;
TableRowSorter<TableModel> sorter;

void setSelected(boolean home)
{
 return;
  
}
	//subframe [] consoleSubframes;

//subframe(JFrame frame, boolean mybool)
gameFrame(channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1, tableClass mygametable1)
{

//super(frame, mybool);
/* super("Game List",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable
*/
setAlwaysOnTop(true);
listColor = new Color(240, 240, 240);
mygametable=mygametable1;

queue=queue1;
sharedVariables = sharedVariables1;
setDefaultCloseOperation(DISPOSE_ON_CLOSE);
initComponents();
}// end constructor


void initComponents(){       


//list = new JList(data); //data has type Object[]

gametable = new JTable(mygametable.gamedata);
gametable.setShowVerticalLines(false);
gametable.setShowHorizontalLines(true);


gametable.setBackground(listColor);

listScroller = new JScrollPane(gametable);
overall mypane = new overall();
mypane.setLayout();
add(mypane);
/*try {
	listScroller.setPreferredSize(new Dimension(2500, 2500));
}
catch(Exception dd){}
*/

if(mygametable.type1.equals("history") || mygametable.type1.equals("stored"))
{
TableColumn col = gametable.getColumnModel().getColumn(0);
TableColumn col1 = gametable.getColumnModel().getColumn(1);
TableColumn col3 = gametable.getColumnModel().getColumn(3);
int width = 25;
col.setPreferredWidth(width);
col1.setPreferredWidth(width);
col3.setPreferredWidth(width);
width=35;
TableColumn col7 = gametable.getColumnModel().getColumn(7);
col7.setPreferredWidth(width);
TableColumn col8 = gametable.getColumnModel().getColumn(8);
col8.setPreferredWidth(width);
TableColumn col9 = gametable.getColumnModel().getColumn(9);
width=125;
col9.setPreferredWidth(width);
}
else // library search
{

}

MouseListener mouseListenerEvents = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {
         if (e.getClickCount() == 2 && sharedVariables.autoHistoryPopup == false) {

             JTable target = (JTable)e.getSource();
      int row = target.getSelectedRow();
      row = sorter.convertRowIndexToModel(row);
      /*int index = gametable.rowAtPoint(e.getPoint());*/
			String gameIndex = (String)gametable.getModel().getValueAt(row,0);
			String StoredOpponent ="";
			if(mygametable.type1.equals("stored"))
			StoredOpponent = (String)gametable.getModel().getValueAt(row,4);

             if(!gameIndex.equals("-1"))
             {
				 String examineString="";
				 String type1 = mygametable.type1;
				 String type2=mygametable.type2;
				if(type1.equals("history") || type1.equals("liblist") || type1.equals("search") || type1.equals("stored"))
				 {
				 	if(type1.equals("history"))
				 	examineString = "Spos " + type2 + " " + gameIndex;
				 	else if(type1.equals("liblist"))
				 	examineString = "Spos " + type2 + " %" + gameIndex;
				 	else if(type1.equals("search"))
				 	examineString = "Spos " + gameIndex;
				 	else if(type1.equals("stored"))
				 	examineString = "Spos " + StoredOpponent + " " + type2;

				 	myoutput output = new myoutput();
				 	output.data=examineString + "\n";

				 	output.consoleNumber=0;
      			 	queue.add(output);
				}
		 	}


          }// end click count two
          else if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2) // right click event
          {
             JTable target = (JTable)e.getSource();
     // int row = target.getSelectedRow();
		Point p = e.getPoint();

			// get the row index that contains that coordinate
			int row = target.rowAtPoint( p );

			// Get the ListSelectionModel of the JTable
			ListSelectionModel model = target.getSelectionModel();

			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one row.
			model.setSelectionInterval( row, row );


     row = sorter.convertRowIndexToModel(row);
      /*int index = gametable.rowAtPoint(e.getPoint());*/
			final String gameIndex = (String)gametable.getModel().getValueAt(row,0);

             if(!gameIndex.equals("-1"))
             {
				 String examineString="";
				 final String type1 = mygametable.type1;
				 final String type2=mygametable.type2;
				if(type1.equals("history") || type1.equals("liblist") || type1.equals("search"))
				{

				JPopupMenu menu2=new JPopupMenu("Popup2");
				JMenuItem item1 = new JMenuItem("examine");
				 item1.addActionListener(new ActionListener() {
          		public void actionPerformed(ActionEvent e) {
					String examineString = "";


				 	if(type1.equals("history"))
				 	examineString = "Examine " + type2 + " " + gameIndex;
				 	else if(type1.equals("liblist"))
				 	examineString = "Examine " + type2 + " %" + gameIndex;
				 	else if(type1.equals("search"))
				 	examineString = "Examine " + gameIndex;
				 	myoutput output = new myoutput();
				 	output.data=examineString + "\n";

				 	output.consoleNumber=0;
      			 	queue.add(output);

				}

       });
			    menu2.add(item1);
				JMenuItem item2 = new JMenuItem("libappend");
				 item2.addActionListener(new ActionListener() {
          		public void actionPerformed(ActionEvent e) {
						String examineString = "";


				 	if(type1.equals("history"))
				 	examineString = "Libappend " + type2 + " " + gameIndex;
				 	else if(type1.equals("search"))
				 	examineString = "Libappend " + gameIndex;
				 	myoutput output = new myoutput();
				 	output.data=examineString + "\n";

				 	output.consoleNumber=0;
      			 	queue.add(output);

				}

       });


				if(!type1.equals("liblist"))
				menu2.add(item2);
				JMenuItem item3 = new JMenuItem("libdelete");
				 item3.addActionListener(new ActionListener() {
          		public void actionPerformed(ActionEvent e) {
					String examineString = "";

				 	if(type1.equals("liblist"))
				 	examineString = "Libdelete" + " %" + gameIndex;

				 	myoutput output = new myoutput();
				 	output.data=examineString + "\n";

				 	output.consoleNumber=0;
      			 	queue.add(output);

				}

       });


				if(type1.equals("liblist"))
				menu2.add(item3);


				JMenuItem item4 = new JMenuItem("savpgn");
				 item4.addActionListener(new ActionListener() {
          		public void actionPerformed(ActionEvent e) {
                                 savePgn(type1, type2, gameIndex);

				}

       });



				menu2.add(item4);


				add(menu2);
				menu2.show(e.getComponent(),e.getX(),e.getY());

}// if history liblist or search

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


void savePgn(String type1, String type2, String gameIndex)
{                       String fileName = getFile();
                        if(fileName.equals(""))
                        return;
 			else
                         sharedVariables.defaultpgn=fileName;
                         	  String examineString = "";
                                  	if(type1.equals("history"))
				 	examineString = "`s" + "0" + "`" + "logpgn " + type2 + " " + gameIndex;
				 	else if(type1.equals("liblist"))
				 	examineString = "`s" + "0" + "`" + "logpgn " + type2 + " %" + gameIndex;
				 	else if(type1.equals("search"))
				 	examineString = "`s" + "0" + "`" + "logpgn " + gameIndex;
				 	myoutput output = new myoutput();
				 	output.data=examineString + "\n";

				 	output.consoleNumber=0;
      			 	       if(!examineString.equals(""))
                                       queue.add(output);
}


String getFile()
{

		try {
JFileChooser fc = new JFileChooser();
fc.setCurrentDirectory(new File("."));
fc.setFileFilter(new FileFilter() {
        public boolean accept(File f) {
          if(f.getName().toLowerCase().endsWith(".pgn"))
          return true;

            if( f.isDirectory())
            return true;
            return false;
        }

        public String getDescription() {


return "Pgn";

        }
      });






			int returnVal = fc.showDialog((JFrame) this, "Save");

			 if (returnVal == JFileChooser.APPROVE_OPTION)
			 {
			  File scriptFile = fc.getSelectedFile();


			  String myFilename=scriptFile.getAbsolutePath();
			{
                          
                          if(!myFilename.toLowerCase().endsWith(".pgn"))
                          return myFilename + ".pgn";
                          return myFilename;
                          }
		  	}// end if
		}// end try
		catch(Exception d){}

		return "";
}// end method

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
	setBackground(listColor);

}// end set layout
}

}// end class