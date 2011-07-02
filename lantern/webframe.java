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

class webframe extends JInternalFrame  implements  ComponentListener// ActionListener,
{



    //private sharedVariables.ConsoleScrollPane[BoardIndex] sharedVariables.ConsoleScrollPane[BoardIndex];
	JScrollPane myscrollpane;
	int consoleNumber;
	JPopupMenu menu;
	JPopupMenu menu2;
	JPopupMenu menu3;
	JPanel mypanel;
	String lastcommand;
	int madeTextPane;
	JPaintedLabel [] channelTabs;
	JLabel tellLabel;
	JCheckBox tellCheckbox;
	JComboBox prefixHandler;

	channels sharedVariables;
	JEditorPane [] consoles;
	ConcurrentLinkedQueue<myoutput> queue;
	String incomingUrl;

	//subframe [] consoleSubframes;

//subframe(JFrame frame, boolean mybool)
webframe(channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1, String incomingUrl1)
{

//super(frame, mybool);
 super("Web",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable

//consoleSubframes=consoleSubframes1;
consoles= new JEditorPane[10];
sharedVariables=sharedVariables1;
queue=queue1;
incomingUrl=incomingUrl1;

setDefaultCloseOperation(DISPOSE_ON_CLOSE);

consoleNumber = 0;

menu=new JPopupMenu("Popup");
JMenuItem item = new JMenuItem("Copy");
item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            consoles[consoleNumber].copy();}
      });
      menu.add(item);



JMenuItem item2 = new JMenuItem("Copy&Paste");
item2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            consoles[consoleNumber].copy();
            Input.paste();}
      });
      menu.add(item2);
JMenuItem item3 = new JMenuItem("Hyperlink");
item3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
          try
                {


			String myurl ="";
			myurl=consoles[consoleNumber].getSelectedText();
			myurl=myurl.trim();
			String myurl2 = myurl.toLowerCase();
			int go=0;
			if(myurl2.startsWith("www."))
			go=1;
			if(myurl2.startsWith("http://"))
			go=1;
			if(myurl2.startsWith("https://"))
			go=1;
			if(go == 0)
			return;

			consoles[consoleNumber].setPage(myurl);


             }catch(Exception g)
                {}
           }
      });
      menu.add(item3);
      add(menu);

setupMenu();
 menu2=new JPopupMenu("Popup2");


 /*JMenuItem item3 = new JMenuItem("copy");
 item.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             consoles[consoleNumber].copy();}
       });
       menu.add(item3);
 */



 JMenuItem item4a = new JMenuItem("Copy");
 item4a.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             Input.copy();}
       });
       menu2.add(item4a);



 JMenuItem item4 = new JMenuItem("Paste");
 item4.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             Input.paste();}
       });
       menu2.add(item4);
      add(menu2);

scrollnow = 1; // we start off with auto scroll

//addMouseListener(this);

addComponentListener(this);
initComponents();
}

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
	                rt.exec( "open " + myurl);
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
   private void initComponents() {


        String[] prefixStrings = { ">"};

        prefixHandler = new JComboBox(prefixStrings);
        prefixHandler.setSelectedIndex(0);
        prefixHandler.setEditable(false);
        updateComboBox();
        Input = new JTextField();

		channelTabs = new JPaintedLabel[10];
		for(int a=0; a<10; a++)
		{if(a==0)
		channelTabs[a]=new JPaintedLabel("C" + a, sharedVariables);
	    else
		channelTabs[a]=new JPaintedLabel("C" + a, sharedVariables);
		}
	    tellLabel=new JLabel("tells");
	    tellCheckbox=new JCheckBox();
	    if(sharedVariables.openConsoleCount == 0)
	    tellCheckbox.setSelected(true);






        // Input.addActionListener (this);
Input.addKeyListener(new KeyListener() {public void keyPressed(KeyEvent e)
{
        int a=e.getKeyCode();
        if(a == 10)
 {  try {
	 consoles[consoleNumber].setPage(Input.getText());
	 Input.setText("... trying");
}
catch(Exception e1){}

        }
        if(a == 120)// f9
        {
                String s=Input.getText();
                if(s.length() == 0)
                if(sharedVariables.lasttell.length()>0)
                Input.setText("tell " + sharedVariables.lasttell + " ");
        }

        if(a == 38)// up
        {
                if(lastcommand.length() >0)
                Input.setText(lastcommand);
        }
// code here
    }

   public void keyTyped(KeyEvent e) {;

    }



    /** Handle the key-released event from the text field. */
    public void keyReleased(KeyEvent e) {;

    }




}

);

Input.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            if(e.isPopupTrigger())
               menu2.show(e.getComponent(),e.getX(),e.getY());


         }
         public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger())
               menu2.show(e.getComponent(),e.getX(),e.getY());
         }


public void mouseEntered (MouseEvent me) {}


public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {

	}


      });





        consoles[consoleNumber] = new JEditorPane();
        consoles[consoleNumber].setEditable(false);

		try {
			consoles[consoleNumber].setPage(incomingUrl);
		}
		catch(Exception e2){}

consoles[consoleNumber].addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            if(e.isPopupTrigger())

               if(consoles[consoleNumber].getSelectedText().indexOf(" ") == -1)
               menu3.show(e.getComponent(),e.getX(),e.getY());
               else
               menu.show(e.getComponent(),e.getX(),e.getY());


         }
         public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger())
               if(consoles[consoleNumber].getSelectedText().indexOf(" ") == -1)
               menu3.show(e.getComponent(),e.getX(),e.getY());
               else
               menu.show(e.getComponent(),e.getX(),e.getY());
        }


public void mouseEntered (MouseEvent me) {}


public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {

	}


      });





consoles[consoleNumber].addHyperlinkListener(new HyperlinkListener()
        {
            public void hyperlinkUpdate(HyperlinkEvent r)
            {
                try
                {
             if(r.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
             {//finalpane.setPage(r.getURL());
				//String cmdLine = "start " + r.getURL();
				//Process p = Runtime.getRuntime().exec(cmdLine);
				String myurl="" + r.getURL();
				consoles[consoleNumber].setPage(myurl);
		 	}

             }catch(Exception e)
                {}
            }
        });


        scrollbutton = new JButton("no scroll");
		scrollbutton.setVisible(false);


       // newbox.setColumns(20);
       // newbox.setLineWrap(true);
       // newbox.setRows(5);
       // newbox.setWrapStyleWord(true);
        consoles[consoleNumber].setEditable(false);
        JScrollPane myscrollpane = new JScrollPane(consoles[consoleNumber]);



        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h1 = layout.createSequentialGroup();


	SequentialGroup middle = layout.createSequentialGroup();
	ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h3 = layout.createSequentialGroup();

	//Add a scroll pane and a label to the parallel group h2
	h2.addComponent(myscrollpane, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
	//h2.addComponent(status, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);// mike commented out

	//Create a sequential group h3

	//h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
	h3.addComponent(prefixHandler, GroupLayout.DEFAULT_SIZE, 60, 60);
	h3.addComponent(Input, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);
    h3.addComponent(scrollbutton);


	/*	for(int a=0; a<10; a++)
		{
			middle.addComponent(channelTabs[a],GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
			middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		}
			middle.addComponent(tellLabel);
			//middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
			middle.addComponent(tellCheckbox);
			//middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
*/



h2.addGroup(h3);
//h2.addGroup(middle);
h1.addGroup(h2);

//h1.addGroup(middle);
	//Add the group h1 to the hGroup
	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading
	//Create a sequential group v1

SequentialGroup v4 = layout.createSequentialGroup();

ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
	//Add a container gap to the sequential group v1
	//v1.addContainerGap();
	//Create a parallel group v2
	ParallelGroup vmiddle = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

//vmiddle.addContainerGap();
	ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);

	//Add the group v2 tp the group v1
	v2.addComponent(myscrollpane, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);

/*		for(int a=0; a<10; a++)
			vmiddle.addComponent(channelTabs[a]);
		vmiddle.addComponent(tellLabel);
		vmiddle.addComponent(tellCheckbox);
*/

		v1.addComponent(prefixHandler, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		v1.addComponent(scrollbutton);
		v1.addComponent(Input, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);


v4.addGroup(v2);
//v4.addGroup(vmiddle);
v4.addGroup(v1);

	vGroup.addGroup(v4);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);


	pack();



   Color lc=new Color(0,0,0);
/*	for(int a=0; a<10; a++)
		channelTabs[a].setForeground(lc);
*/

 setActiveTabForeground(sharedVariables.openConsoleCount);

	for(int a=0; a<10; a++)
		channelTabs[a].setOpaque(true);

	for(int a=0; a<10; a++)
		channelTabs[a].setBackground(sharedVariables.tabBackground);


    }

void setActiveTabForeground(int i)
{
	for(int a=0; a<10; a++)
	if(a==i)
	{	channelTabs[a].setForeground(sharedVariables.activeTabForeground);
		channelTabs[a].setBackground(sharedVariables.tabBackground);
	}
	else
		channelTabs[a].setForeground(sharedVariables.passiveTabForeground);


}

void dispatchCommand(String myurl)
{

	String mycommand="";
	mycommand=myurl; //.substring(1, myurl.length()-1);// need to figure out why this is -2 not -1, maybe i include the end space which adds a charaacter here when i cut it
	mycommand=mycommand + "\n";

	myoutput output = new myoutput();
	      output.data=mycommand;
	      output.consoleNumber=consoleNumber;
      queue.add(output);


}




/*

public void mousePressed(MouseEvent e) {
 if(e.isPopupTrigger())
               menu.show(e.getComponent(),e.getX(),e.getY());


}
public void mouseEntered (MouseEvent me) {}
public void mouseReleased (MouseEvent me) {
	if(me.isPopupTrigger())
               menu.show(me.getComponent(),me.getX(),me.getY());
          }

public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {

	}

*/

JTextField Input;
int scrollnow;
String myglobalinput;
JButton scrollbutton;

// Show text when user presses ENTER.
/*public void actionPerformed(ActionEvent ae) {
      //client.sendMessage(Input.getText());
      String mes = Input.getText() + "\n";
      myoutput output = new myoutput();
      output.data=mes;
      output.consoleNumber=consoleNumber;
      queue.add(output);
      Input.setText("");

	  			try {
				StyledDocument doc=consoles[consoleNumber].getStyledDocument();
	  			if(sharedVariables.password == 0)
	  			doc.insertString(doc.getEndPosition().getOffset(), mes, null);
	  			else
	  			{
				doc.insertString(doc.getEndPosition().getOffset(), "*******\n", null);
				sharedVariables.password=0;
				}
	  			consoles[consoleNumber].setStyledDocument(doc);
				}
				catch(Exception E){ }

    }

*/
void setupMenu()
{

menu3=new JPopupMenu("Popup");
JMenuItem item = new JMenuItem("Finger");
item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
          String name =  consoles[consoleNumber].getSelectedText();
          doCommand("finger " + name + "\n");
          }
      });
      menu3.add(item);



JMenuItem item2 = new JMenuItem("Vars");
item2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             String name =  consoles[consoleNumber].getSelectedText();
             doCommand("Vars " + name + "\n");
             }
      });
      menu3.add(item2);
JMenuItem item3 = new JMenuItem("Ping");
item3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         String name =  consoles[consoleNumber].getSelectedText();
         doCommand("ping " + name + "\n");
           }
      });
      menu3.add(item3);
      add(menu3);

menu3.addSeparator();

JMenuItem item4 = new JMenuItem("Match");
item4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
          String name =  consoles[consoleNumber].getSelectedText();
          doCommand("match " + name + "\n");
          }
      });
      menu3.add(item4);



JMenuItem item5 = new JMenuItem("Assess");
item5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             String name =  consoles[consoleNumber].getSelectedText();
             doCommand("assess " + name + "\n");
             }
      });
      menu3.add(item5);

JMenuItem item6 = new JMenuItem("Pstat");
item6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         String name =  consoles[consoleNumber].getSelectedText();
         doCommand("pstat " + name + "\n");
           }
      });
      menu3.add(item6);


menu3.addSeparator();


 JMenuItem item7 = new JMenuItem("Observe");
 item7.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           String name =  consoles[consoleNumber].getSelectedText();
           doCommand("observe " + name + "\n");
           }
       });
       menu3.add(item7);



 JMenuItem item8 = new JMenuItem("Follow");
 item8.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              String name =  consoles[consoleNumber].getSelectedText();
              doCommand("follow " + name + "\n");
              }
       });
       menu3.add(item8);
menu3.addSeparator();

 JMenuItem item9 = new JMenuItem("History");
 item9.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          String name =  consoles[consoleNumber].getSelectedText();
          doCommand("history " + name + "\n");
            }
       });
       menu3.add(item9);



 JMenuItem item10 = new JMenuItem("Liblist");
 item10.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           String name =  consoles[consoleNumber].getSelectedText();
           doCommand("liblist " + name + "\n");
           }
       });
       menu3.add(item10);



 JMenuItem item11 = new JMenuItem("Stored");
 item11.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              String name =  consoles[consoleNumber].getSelectedText();
              doCommand("stored " + name + "\n");
              }
       });
       menu3.add(item11);

menu3.addSeparator();
 JMenuItem item12 = new JMenuItem("Copy");
 item12.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          consoles[consoleNumber].copy();

            }
       });
       menu3.add(item12);

 JMenuItem item13 = new JMenuItem("Copy&Paste");
  item13.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
           consoles[consoleNumber].copy();
           Input.paste();

             }
        });
        menu3.add(item13);





 add(menu3);

}// end menu setup




void doCommand(String mycommand)
{
	myoutput output = new myoutput();
	output.data=mycommand;
	output.consoleNumber=sharedVariables.looking[consoleNumber];
    queue.add(output);

}



void updateComboBox()
{

	// int cindex=sharedVariables.console[Integer.parseInt(dg.getArg(1))];
	prefixHandler.removeAllItems();
	prefixHandler.addItem(">");
	for(int a=0; a<400; a++)
	if(sharedVariables.console[consoleNumber][a] == sharedVariables.looking[consoleNumber] && sharedVariables.looking[consoleNumber] != 0)
	prefixHandler.addItem("Tell " + a + " ");
}














public void componentHidden(ComponentEvent e) {

    }

    public void componentMoved(ComponentEvent e) {
sharedVariables.webframePoint=getLocation();
sharedVariables.webframeWidth=getWidth();
sharedVariables.webframeHeight=getHeight();

    }

    public void componentResized(ComponentEvent e) {
    sharedVariables.webframePoint=getLocation();
	sharedVariables.webframeWidth=getWidth();
	sharedVariables.webframeHeight=getHeight();

    }

    public void componentShown(ComponentEvent e) {


    }

























}// end subframe