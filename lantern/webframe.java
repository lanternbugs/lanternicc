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
import javax.swing.text.html.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;

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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


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
 super("Web View",
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
			if(incomingUrl.startsWith("<"))
			{
                         consoles[consoleNumber].setContentType("text/html");
                         consoles[consoleNumber].setText(incomingUrl);
			}
                        else
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

consoles[consoleNumber].addPropertyChangeListener("page", new MyPropertyChangedClass());


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



      
        consoles[consoleNumber].setEditable(false);
        JScrollPane myscrollpane = new JScrollPane(consoles[consoleNumber]);



        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	hGroup.addComponent(myscrollpane);
	layout.setHorizontalGroup(hGroup);


	
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading

	vGroup.addComponent(myscrollpane);

	//Create the vertical group
	layout.setVerticalGroup(vGroup);


	pack();

}

class MyPropertyChangedClass implements PropertyChangeListener {
 public void propertyChange(PropertyChangeEvent evt) {
            if (!evt.getPropertyName().equals("page")) return;
             try {
        HTMLDocument doc = (HTMLDocument) consoles[consoleNumber].getDocument();
        String title = (String) doc.getProperty(Document.TitleProperty);
        //System.out.println("change update and title  is " + title);
        if(title.length() > 0) {
         setTitle(title);
        } else {
         setTitle("Web View");
        }

        } catch(Exception dui) {
          try {
            setTitle("Web View");

          } catch(Exception duio) { };
        }
 }
}
void setDocumentListenerUp()
{

  
  consoles[consoleNumber].getDocument().addDocumentListener(new DocumentListener()
{
   public void insertUpdate(DocumentEvent e) {

    }
    public void removeUpdate(DocumentEvent e) {

    }
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
        //String text = "";//e.getDocument().getWholeText();

    }

});
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

 JMenuItem item12 = new JMenuItem("Copy");
 item12.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          consoles[consoleNumber].copy();

            }
       });
       menu3.add(item12);






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