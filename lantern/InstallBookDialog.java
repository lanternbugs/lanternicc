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


class InstallBookDialog  extends JDialog
{


  String stockfish = "Stockfish 8";
  JTextPane textPane = new JTextPane();
  JButton installContinue = new JButton("Continue");
  JButton installCancel = new JButton("Cancel");
  JButton installOk = new JButton("Ok");
  static int openingBook18 = 1;
  static int mediocreChess5 = 2;
  static int cuckooChess112 = 3;
  static int stockfish8 = 4;
  boolean installEngine = false;
    int installType = 1;
    Font myfont;

    InstallBookDialog(JFrame frame, int type, Font thefont) {
      super(frame, "Extract Opening Book", true);
      myfont = thefont;
      setSize(350,300);
      if(channels.stockfishName.contains("10")) {
       stockfish = "Stockfish 10";
      }
      if(type == mediocreChess5) {
       installEngine = true;
       setTitle("Extract Mediocre Chess Engine");
      }
        if(type == mediocreChess5) {
            installEngine = true;
            setTitle("Extract Cuckoo Chess Engine");
        }
        if(type == stockfish8) {
            installEngine = true;
            setTitle("Extract " + stockfish + " Engine");
        }
        installType = type;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


      OverallForInstall mypane = new OverallForInstall();
      mypane.setLayout();
      add(mypane);

     installContinue.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            try
                {
                  installContinue.setEnabled(false);
                File file;
                if(installType == mediocreChess5) {
                    file = new File(channels.privateDirectory + channels.mediocreEngineName);
                  }else if(installType == cuckooChess112) {
                     file = new File(channels.privateDirectory + channels.cuckooEngineName);
                  } else if(installType == stockfish8) {
                      file = new File(channels.privateDirectory + channels.stockfishName);
                  }
                  else {
                    file = new File(channels.privateDirectory + channels.openingBookName);
                  }
                if(installType == mediocreChess5) {
                     setPaneText("Installing Mediocre Chess");
                } else if(installType == cuckooChess112) {
                    setPaneText("Installing Cuckoo Chess");
                }  else if(installType == stockfish8) {
                    setPaneText("Installing " + stockfish);
                }

                else {
                    setPaneText("Installing Book");
                  }
                if (!file.exists() && !file.isDirectory()) {
                    System.out.println("trying to copy");
                    InputStream link;
                    
                    if(installType == mediocreChess5) {
                        link = (getClass().getResourceAsStream("/engines/" + channels.mediocreEngineName));
                    } else if(installType == cuckooChess112) {
                        link = (getClass().getResourceAsStream("/engines/" + channels.cuckooEngineName));
                    } else if(installType == stockfish8) {
                        link = (getClass().getResourceAsStream("/engines/" + channels.stockfishName));
                    }else {
                        link = (getClass().getResourceAsStream("/" + channels.openingBookName));
                      }
                    //Files.copy(link, file.getAbsoluteFile().toPath());
                    copyInputStreamToFile( link,  file );
                    if(installType == stockfish8) {
                     file.setExecutable(true);
                    }
                    System.out.println("done copy");
                    if(installType == mediocreChess5) {
                      setPaneText("Mediocre Chess V0.5 succesfully installed.  Go to Options / Other Engines / Analyze With Mediocre Chess v0.5 again to open it.");
                    } else if(installType == cuckooChess112) {
                        setPaneText("Cuckoo Chess 1.12 succesfully installed.  Go to Options / Analyze With Cuckoo Chess 1.12 again to open it.");
                    }  else if(installType == stockfish8) {
                        setPaneText(stockfish + " succesfully installed.  Go to Options / Analyze With " + stockfish + " again to open it.");
                    }

                    else {
                      setPaneText("Opening book succesfully installed.  Go to Options / Opening Book again to open it.");
                    }

                    changeButtonsVisiblity();

                } else {
                    System.out.println("file exists");
                    if(installType == mediocreChess5) {
                    setPaneText("Medicore Chess allready exists");
                    } else if(installType == cuckooChess112) {
                        setPaneText("Cuckoo Chess allready exists");
                    } else if(installType == stockfish8) {
                        setPaneText(stockfish + " allready exists");
                    }

                     else {
                    setPaneText("Opening book file allready exists");
                    }
                    changeButtonsVisiblity();
                }
                } catch(Exception dui) {
                    setPaneText("There was an error installing. Does  Lantern have permission to write to this folder?");
                    changeButtonsVisiblity();
                }

          }
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}  });
          
     installOk.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
             myoutput output = new myoutput();
            dispose();

          }
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}  });
          
          installCancel.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
             myoutput output = new myoutput();
            dispose();

          }
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered (MouseEvent me) {}
          public void mouseExited (MouseEvent me) {}
          public void mouseClicked (MouseEvent me) {}  });


     textPane.setEditable(false);
     textPane.setFont(myfont);
     String text = "To use the Opening Book the book file must be extracted from the Lantern Jar. It will create a file called " + channels.openingBookName + "  in the lantern folder with a size of 3 megs.";
     if(installType == mediocreChess5) {
        text = "To use the Medicore Chess Engine it must be extracted from the Lantern Jar. It will create a file called " + channels.mediocreEngineName + "  in the lantern folder with a size of less than a meg.";
     }
        if(installType == cuckooChess112) {
            text = "To use the Cuckoo Chess Engine it must be extracted from the Lantern Jar. It will create a file called " + channels.cuckooEngineName + "  in the lantern folder with a size of less than a meg.";
        }
        if(installType == stockfish8) {
            text = "To use the " + stockfish + " Chess Engine it must be extracted from the Lantern Jar. It will create a file called " + channels.stockfishName + "  in the lantern folder with a size of about a meg.";
        }

     setPaneText(text);

      setVisible(true);
        // load the sqlite-JDBC driver using the current class loader



    }
    void setPaneText(String text) 
    {
      try {
        textPane.setText("");
      StyledDocument doc = textPane.getStyledDocument();
      doc.insertString(doc.getLength(), text, null);
     }
     catch(Exception pane) {

     }

    }
    
    void changeButtonsVisiblity()
    {
     installContinue.setVisible(false);
     installCancel.setVisible(false);
     installOk.setVisible(true);
    }
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
   class OverallForInstall extends JPanel
{

	void setLayout() {
		//mypane.add(listScroller);
 GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
	ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);



       	SequentialGroup hb = layout.createSequentialGroup();
       	hb.addComponent(installCancel,  0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
       	hb.addComponent(installContinue,  0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);



       h1.addComponent(installOk);
       h1.addComponent(textPane);
       h1.addGroup(hb);




	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);// was leading


SequentialGroup v1 = layout.createSequentialGroup();

		ParallelGroup vb = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
		vb.addComponent(installContinue);
		vb.addComponent(installOk);
		vb.addComponent(installCancel);
		 v1.addComponent(textPane);
		v1.addGroup(vb);


	vGroup.addGroup(v1);


	layout.setVerticalGroup(vGroup);

}// end set layout
}
}
