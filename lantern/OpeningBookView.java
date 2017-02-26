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


class OpeningBookView  extends JDialog
{
  Connection connection = null;
  ArrayList<DatabaseAMove> moveListData;
  JTable moveTable;
  tableClass mymovetabledata  = new tableClass();
  TableRowSorter<TableModel> sorter;
  JScrollPane listScroller;

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

    OpeningBookView(JFrame frame) {
      super(frame, "none", false);
      setSize(500,200);
       setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
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


      setVisible(true);
        // load the sqlite-JDBC driver using the current class loader
        try
                {
                File file = new File("iosopeningbook18.db");
                if (!file.exists()) {
                    System.out.println("trying to copy");
                    InputStream link = (getClass().getResourceAsStream("/iosopeningbook18.db"));
                    //Files.copy(link, file.getAbsoluteFile().toPath());
                    copyInputStreamToFile( link,  file );
                    System.out.println("done copy");

                } else {
                    System.out.println("file exists");
                }
                } catch(Exception dui) {
                    System.out.println("exception on copy");
                }
        try {
            Class.forName("org.sqlite.JDBC");


            try
            {
                // create a database connection
                connection = DriverManager.getConnection("jdbc:sqlite:iosopeningbook18.db");

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
   void setWindowState()
   {
     
   }
    void update()
    {          for(int s = moveListData.size() -1; s >=0; s--) {
                  mymovetabledata.gamedata.removeRow(s);
               }
               moveListData.clear();
               String title = "none";
                if(gamestate.currentHash.toString().equals("-1")) {
                 setWindowState();
                 return;
                } else {
                 System.out.println("hash is " + gamestate.currentHash.toString());
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
                ResultSet rs = statement.executeQuery("select * from MOVE" + gamestate.currentHash.toString());
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
                   move.draw = rs.getInt("draw");
                   data.add("" + move.draw);
                   move.win = rs.getInt("win");
                   data.add("" + move.win);
                   move.loss = rs.getInt("loss");
                   data.add("" + move.loss);
                   mymovetabledata.gamedata.addRow(data);
                   moveListData.add(move);
                }
                }
                catch(SQLException e) {
                   System.err.println(e);
                }

                setWindowState();
                setTitle(title);

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
   
   
   
   class OverallForBook extends JPanel
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
}
}