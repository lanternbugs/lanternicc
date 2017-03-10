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
import java.util.*;
import java.awt.*;
import java.awt.Window.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JDialog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.table.*;
import javax.swing.table.TableRowSorter;
import javax.swing.GroupLayout.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.text.*;


class OpeningBookView  extends JDialog
{
  Connection connection = null;
  static String openingEco = "";
  ArrayList<DatabaseAMove> moveListData;
  JTable moveTable;
  tableClass mymovetabledata  = new tableClass();
  TableRowSorter<TableModel> sorter;
  JScrollPane listScroller;
  ConcurrentLinkedQueue<myoutput> queue;
  JTextPane textPane = new JTextPane();
  JButton backward = new JButton("<");
  JButton forward = new JButton(">");
  boolean oldBook = false;

  public static int choice = 0;
   private void copyInputStreamToFile( InputStream in, File file ) {
    try {
        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while((len=in.read(buf))>0){
            out.write(buf,0,len);
        }
        out.close();
        in.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    OpeningBookView(JFrame frame, ConcurrentLinkedQueue<myoutput> queue1, boolean old) {
      super(frame, "Opening Book", false);
      setSize(275,200);
       setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
       queue = queue1;
       oldBook = old;
       addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent we) {

         setVisible(false);
    }
});

      mymovetabledata.createOpeningBookColumns();
      moveListData = new ArrayList<DatabaseAMove>();
      moveTable = new JTable();
      moveTable = new JTable(mymovetabledata.gamedata);
      moveTable.setShowVerticalLines(true);
      moveTable.setShowHorizontalLines(true);
      listScroller = new JScrollPane(moveTable);
      OverallForBook mypane = new OverallForBook();
      mypane.setLayout();
      add(mypane);
      addSorter();
      MouseListener mouseListenerEvents = new MouseAdapter() {
     public void mouseClicked(MouseEvent e) {
         if (e.getClickCount() == 2 && e.getButton() != MouseEvent.BUTTON3) {

             JTable target = (JTable)e.getSource();
             int row = target.getSelectedRow();
             row = sorter.convertRowIndexToModel(row);
             //String move = (String)moveTable.getModel().getValueAt(row,0);
             String move = getSubMove(moveListData.get(row).movefrom) + getSubMove(moveListData.get(row).moveto);
             myoutput output = new myoutput();
             output.data="`c0`" +move + "\n";
             output.consoleNumber=0;
             queue.add(output);
         }
     }
     };
     backward.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
             myoutput output = new myoutput();
             output.data="`c0`" + "multi backward" + "\n";
             output.consoleNumber=0;
             queue.add(output);

          }
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}  });
          
     forward.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
             myoutput output = new myoutput();
             output.data="`c0`" + "multi forward" + "\n";
             output.consoleNumber=0;
             queue.add(output);

          }
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}  });
     moveTable.addMouseListener(mouseListenerEvents);
     textPane.setEditable(false);
     try {
      StyledDocument doc = textPane.getStyledDocument();
      String text = "Opening moves show when examining and in book.  Try Game menu / Examine or Examine my Last Game";
      doc.insertString(doc.getEndPosition().getOffset(), text, null);
     }
     catch(Exception pane) {

     }
     setItemsVisiblity();
      setVisible(true);
        // load the sqlite-JDBC driver using the current class loader

        try {
            Class.forName("org.sqlite.JDBC");


            try
            {
                // create a database connection
                if(oldBook) {
                  connection = DriverManager.getConnection("jdbc:sqlite:" + channels.oldOpeningBookName);
                } else {
                connection = DriverManager.getConnection("jdbc:sqlite:" + channels.openingBookName);
                }

            }
            catch(SQLException e)
            {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println(e.getMessage());
            }



        } catch(Exception nameerror) {

        }

    }
    void closeDatabase()
    {

                try
                {
                    if(connection != null)
                        connection.close();
                }
                catch(SQLException e)
                {
                    // connection close failed.
                    System.err.println(e);
                }
    }
    void setItemsVisiblity() {
          if(gamestate.currentHash.toString().equals("-1")) {
      backward.setVisible(false);
      forward.setVisible(false);
      listScroller.setVisible(false);
      textPane.setVisible(true);
     } else {
      textPane.setVisible(false);
      listScroller.setVisible(true);
      backward.setVisible(true);
      forward.setVisible(true);
     }
    }


    void update()
    {          for(int s = moveListData.size() -1; s >=0; s--) {
                  mymovetabledata.gamedata.removeRow(s);
               }
               moveListData.clear();
               setItemsVisiblity();
                if(gamestate.currentHash.toString().equals("-1")) {
                  setTitle("Opening Book");
                 return;
                }
                try {
                   Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);  // set timeout to 30 sec.

               /* statement.executeUpdate("drop table if exists person");
                statement.executeUpdate("create table person (id integer, name string)");
                statement.executeUpdate("insert into person values(1, 'leo')");
                statement.executeUpdate("insert into person values(2, 'yui')");
               */

               /*(3 keys) 1421291105980716732 1421291105980716732 2628344263795677394
               */
               String key;
               if(choice %3 == 0) {
                  key = "1421291105980716732";
               } else if(choice %3 == 1) {
                  key = "1421291105980716732";
               } else {
                  key = "2628344263795677394";
               }
               choice++;
               // (MOVEFROM int, MOVETO int, MOVE TEXT, WIN int, LOSS int, DRAW int)
                ResultSet rs;
                if(oldBook) 
                {
                if(gamestate.hashMoveTop %2 == 0) {
                   rs = statement.executeQuery("select * from MOVE" + gamestate.currentHash.toString() + " ORDER BY CAST(WIN AS INTEGER) DESC, CAST(DRAW AS INTEGER) DESC" );
                } else {
                  rs = statement.executeQuery("select * from MOVE" + gamestate.currentHash.toString() + " ORDER BY CAST(LOSS AS INTEGER) DESC, CAST(DRAW AS INTEGER) DESC" );
                }
                
                } // if oldbook
                else {
                  if(gamestate.hashMoveTop %2 == 0) {
                     rs = statement.executeQuery("select * from bookmoves where hash = 'MOVE" +  gamestate.currentHash.toString()+ "'  ORDER BY CAST(WIN AS INTEGER) DESC, CAST(DRAW AS INTEGER) DESC" );
                     } else {
                     rs = statement.executeQuery("select * from bookmoves where hash = 'MOVE" +  gamestate.currentHash.toString()+ "'  ORDER BY CAST(LOSS AS INTEGER) DESC, CAST(DRAW AS INTEGER) DESC" );
                     }
                }
                if(gamestate.hashMoveTop > 6 && openingEco.length() > 0)
                    ;
                else setTitle("Opening Book");

                while(rs.next())
                {
                    // read the result set
                    //System.out.println("name = " + rs.getString("name"));
                   // System.out.println("move is " + rs.getString("move"));
                   // title = title + " " +  rs.getString("move");
                   // System.out.println("id = " + rs.getInt("id"));
                   Vector<String> data = new Vector();
                   DatabaseAMove move = new DatabaseAMove();
                   move.move = rs.getString("move");
                   data.add(move.move);
                   move.movefrom = rs.getInt( "movefrom");
                   move.moveto = rs.getInt("moveto");
                   if(gamestate.hashMoveTop %2 == 0) {
                       move.win = rs.getInt("win");
                   data.add("" + move.win);
                   move.draw = rs.getInt("draw");
                   data.add("" + move.draw);
                   move.loss = rs.getInt("loss");
                   data.add("" + move.loss);
                   } else {
                       move.win = rs.getInt("loss");
                   data.add("" + move.win);
                   move.draw = rs.getInt("draw");
                   data.add("" + move.draw);
                   move.loss = rs.getInt("win");
                   data.add("" + move.loss);
                   }
                   float percentWin = 0;
                    try {
                        float denominator =  Float.parseFloat("" + move.win) +  Float.parseFloat("" + move.draw)  + Float.parseFloat("" + move.loss);
                        float numerator = Float.parseFloat("" + move.win) + (Float.parseFloat("" + move.draw) * (float) .5);
                        percentWin = numerator / denominator;
                        
                    } catch(Exception floating) {
                        
                    }
                    
                    data.add(String.format("%.2f", percentWin));
                   mymovetabledata.gamedata.addRow(data);
                   moveListData.add(move);
                }

                }
                catch(SQLException e) {
                  // System.err.println(e);
                }
               myoutput output = new myoutput();
               output.data="`e0`" + "multi eco" + "\n";
               output.consoleNumber=0;
               queue.add(output);


    }
    
    void addSorter()
    {

      sorter =  new TableRowSorter<TableModel>(mymovetabledata.gamedata);

     Comparator intComparator = new Comparator<String>()
  {
     @Override
    public int compare(String arg0, String arg1)
    {
      try {
           Integer val = Integer.parseInt(arg0) - Integer.parseInt(arg1);
           return val.intValue();

      } catch(Exception dui) {
      }
      return 0;
    }
  };
  Comparator floatComparator = new Comparator<String>()
        {
            @Override
            public int compare(String arg0, String arg1)
            {
                try {
                    float val = Float.parseFloat(arg0) - Float.parseFloat(arg1);
                    if(val == 0) {
                        
                        return 0;
                    } else if(val > 0)
                    {
                        return 1;
                    }
                    else
                        return -1;
                    
                } catch(Exception dui) {
                }
                return 0;
            }
        };


  sorter.setComparator(1, intComparator);
  sorter.setComparator(2, intComparator);
  sorter.setComparator(3, intComparator);
  sorter.setComparator(4, floatComparator);

  moveTable.setRowSorter(sorter);
    }
   class DatabaseAMove {
    // (MOVEFROM int, MOVETO int, MOVE TEXT, WIN int, LOSS int, DRAW int)
     int movefrom;
     int moveto;
     String move;
     int win;
     int loss;
     int draw;

     DatabaseAMove()
     {
         movefrom = 0;
         moveto = 0;
         move = "";
         win = 0;
         loss = 0;
         draw = 0;
     }
   }

String getSubMove(int square)
	{

		int col=0;
		int row=0;
		String value="";
		square = 63 - square;
		square++; // 1-64
		int square2=65-square;

		row=square%8;
		//if(flipIt)
		row=square2%8;

		if(row == 1)
		value = value + "a";
		else if(row == 2)
		value = value + "b";
		else if(row == 3)
		value = value + "c";
		else if(row == 4)
		value = value + "d";
		else if(row == 5)
		value = value + "e";
		else if(row == 6)
		value = value + "f";
		else if(row == 7)
		value = value + "g";
		else if(row == 0)
		value = value + "h";

		col=(int) square/8;
		if(square %8 != 0)
		col++;

	//	if(flipIt)// white on bottom doesnt work. black on bottom does
	//	col=9-col;

		value = value + col;





		return value;
	}
   
   class OverallForBook extends JPanel
{

	void setLayout() {
		//mypane.add(listScroller);
 GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
	ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);



       	SequentialGroup hb = layout.createSequentialGroup();
       	hb.addComponent(backward,  0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
       	hb.addComponent(forward,  0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);



       h1.addComponent(listScroller);
       h1.addComponent(textPane);
       h1.addGroup(hb);




	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);// was leading


SequentialGroup v1 = layout.createSequentialGroup();



		v1.addComponent(listScroller);
		ParallelGroup vb = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
		vb.addComponent(backward);
		vb.addComponent(forward);
		v1.addGroup(vb);


	vGroup.addGroup(v1);
	vGroup.addComponent(textPane);

	layout.setVerticalGroup(vGroup);

}// end set layout
}
}


/*
 
 Script used to populate new openingbook (lanternopeninbook19.db) from old(18).
 
 System.out.println("finding tables");
 rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
 System.out.println("found ");
 while(rs.next())
 {
 
 System.out.println("table is " +  rs.getString("TABLE_NAME"));
 }
 */
/*
 Statement statement2 = connection2.createStatement();
 statement2.setQueryTimeout(30);  // set timeout to 30 sec.
 
 statement2.executeUpdate("drop table if exists bookmoves");
 statement2.executeUpdate("CREATE TABLE IF NOT EXISTS bookmoves (HASH TEXT, MOVEFROM int, MOVETO int, MOVE TEXT, WIN int, LOSS int, DRAW int)");
 
 
 
 DatabaseMetaData md = connection.getMetaData();
 ResultSet rsM = md.getTables(null, null, "%", null);
 while (rsM.next()) {
 System.out.printf("table name: %s%n", rsM.getString("TABLE_NAME"));
 ResultSet rs = statement.executeQuery("select * from " + rsM.getString("TABLE_NAME") + " ORDER BY CAST(WIN AS INTEGER) DESC, CAST(DRAW AS INTEGER) DESC" );
 while(rs.next())
 {
 
 
 DatabaseAMove move = new DatabaseAMove();
 move.move = rs.getString("move");
 
 move.movefrom = rs.getInt( "movefrom");
 move.moveto = rs.getInt("moveto");
 
 move.win = rs.getInt("win");
 
 move.draw = rs.getInt("draw");
 
 move.loss = rs.getInt("loss");
 System.out.println(move.move + " " + move.movefrom + " " + move.moveto +  " " + move.win + " " + move.draw + " " + move.loss);
 statement2.executeUpdate("insert into bookmoves values('" + rsM.getString("TABLE_NAME") + "', " + move.movefrom + ", " + move.moveto + ", '" + move.move + "', " +  move.win + ", " + move.loss + ", " + move.draw + ")");
 }
 
 
 }
 System.out.println("done");
 */