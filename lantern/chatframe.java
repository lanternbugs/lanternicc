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
import java.util.ArrayList;
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
import java.awt.datatransfer.Clipboard;

class chatframe extends JFrame  implements  ComponentListener, ActionListener    // ActionListener,
{



    //private sharedVariables.ConsoleScrollPane[BoardIndex] sharedVariables.ConsoleScrollPane[BoardIndex];
	subPanel overall;
	int consoleNumber;
	JPopupMenu menu;
	JPopupMenu menu2;
	JPopupMenu menu3;
	JPanel mypanel;
	String lastcommand;
	String [] comboMemory;
	int madeTextPane;

	JPaintedLabel [] channelTabs;
	JLabel tellLabel;
	JCheckBox tellCheckbox;
	JComboBox prefixHandler;
	JComboBox tabChooser;
	Highlighter myHighlighter;

	channels sharedVariables;
	JTextPane [] consoles;
	ConcurrentLinkedQueue<myoutput> queue;
	String consoleTitle;
	JMenuBar consoleMenu;
	//subframe [] consoleSubframes;
docWriter myDocWriter;
//subframe(JFrame frame, boolean mybool)
chatframe(channels sharedVariables1, JTextPane consoles1[], ConcurrentLinkedQueue<myoutput> queue1, docWriter myDocWriter1)
{



//consoleSubframes=consoleSubframes1;
consoles=consoles1;
myDocWriter=myDocWriter1;
sharedVariables=sharedVariables1;
queue=queue1;
consoleTitle="Main Console";
setDefaultCloseOperation(DISPOSE_ON_CLOSE);
consoleNumber = sharedVariables.chatFrame;;
setTitle(consoleTitle);
setSize(600,600);
/*
JMenu consoleMenu = new JMenu("Colors/fonts per console");
JMenuItem setfont = new JMenuItem("Console's Font");
consoleMenu.add(setfont);
JCheckBoxMenuItem overridefont = new JCheckBoxMenuItem("Console Font Override Tab Font");
consoleMenu.add(overridefont);
setfont.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
			 setConsoleFont();
	}
      });
overridefont.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
           if(sharedVariables.useConsoleFont[consoleNumber]==true)
           sharedVariables.useConsoleFont[consoleNumber]=false;
           else
           sharedVariables.useConsoleFont[consoleNumber]=true;
		}

      });
JMenuBar myconsolemenu = new JMenuBar();

myconsolemenu.add(consoleMenu);
setJMenuBar(myconsolemenu);
*/

menu=new JPopupMenu("Popup");
JMenuItem item = new JMenuItem("Copy");
item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            consoles[consoleNumber].copy();
            giveFocus();}
      });
      menu.add(item);



JMenuItem item2 = new JMenuItem("Copy&Paste");
item2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            consoles[consoleNumber].copy();
            overall.Input.paste();
            giveFocus();}
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

			openUrl(myurl);

			giveFocus();
             }catch(Exception g)
                {}
           }
      });
      menu.add(item3);
      add(menu);


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
             overall.Input.copy();}
       });
       menu2.add(item4a);



 JMenuItem item4 = new JMenuItem("Paste");
 item4.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
             overall.Input.paste();}
       });
       menu2.add(item4);
      add(menu2);

scrollnow = 1; // we start off with auto scroll
wheelIsScrolling=true;
//addMouseListener(this);



consoleMenu = new JMenuBar();

 JMenu mywindows = new JMenu("Console Menu");

 JMenuItem  consoleLayout1 = new JMenuItem("Single Rows of Tabs");
 consoleLayout1.addActionListener(this);
 mywindows.add(consoleLayout1);

 JMenuItem  consoleLayout2 = new JMenuItem("Two Rows of Tabs");
  consoleLayout2.addActionListener(this);
  mywindows.add(consoleLayout2);

JMenuItem  consoleLayout3 = new JMenuItem("No Visible Tabs");
 consoleLayout3.addActionListener(this);
 mywindows.add(consoleLayout3);

JMenuItem  selectall = new JMenuItem("Select All");
 selectall.addActionListener(this);
 mywindows.add(selectall);

JMenuItem  copyit = new JMenuItem("Copy");
 copyit.addActionListener(this);
 mywindows.add(copyit);


 consoleMenu.add(mywindows);
setJMenuBar(consoleMenu);
consoleMenu.setVisible(sharedVariables.showConsoleMenu);
initComponents();

}


public void actionPerformed(ActionEvent event)
{
try
{
//Object source = event.getSource();
//handle action event here
if(event.getActionCommand().equals("Single Rows of Tabs"))
{
	recreate(1);
}

if(event.getActionCommand().equals("Two Rows of Tabs"))
{

	recreate(2);
}
if(event.getActionCommand().equals("No Visible Tabs"))
{
	recreate(3);
}


if(event.getActionCommand().equals("Select All"))
{

	consoles[consoleNumber].selectAll();
StyledDocument doc = consoles[consoleNumber].getStyledDocument();
myHighlighter.addHighlight(0, doc.getLength(), DefaultHighlighter.DefaultPainter);


}
if(event.getActionCommand().equals("Copy"))
{

Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
TransferHandler transferHandler = consoles[consoleNumber].getTransferHandler();
transferHandler.exportToClipboard(consoles[consoleNumber], clipboard, TransferHandler.COPY);
}// end copy

}// end try
catch(Exception badEvent){}


}// end method action performed
void recreate(int num)
 {

 getContentPane().removeAll();

 if(num == 1)
 	overall.setMyLayout1();
 else if(num == 2)
 	overall.setMyLayout2();
else
 	overall.setMyLayout3();


 }

void setTabFont(int con)
{

JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.tabStuff[con].tabFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
			{
				sharedVariables.tabStuff[con].tabFont=fnt;
				if(sharedVariables.looking[consoleNumber]==con)
					consoles[consoleNumber].setFont(fnt);
			}
}

void setConsoleFont()
{

JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.consoleFonts[consoleNumber]);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
			{
				sharedVariables.consoleFonts[consoleNumber]=fnt;
				if(sharedVariables.useConsoleFont[consoleNumber]!=false)
					consoles[consoleNumber].setFont(fnt);
			}
}




void setTabColors(int con)
{
//JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
	//customizeTabConsolelColorsDialog frame = new customizeTabConsolelColorsDialog( this, false, sharedVariables, consoles, con, this);


}


/*void switchWindows()
{

JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
myself.switchWindows();

}*/
void customizeTab(int num)
{
//JDesktopPaneCustom myself = (JDesktopPaneCustom) getDesktopPane();
//customizeChannelsDialog frame = new customizeChannelsDialog(this, false, num, sharedVariables, consoleSubframes);
//if(sharedVariables.looking[consoleNumber]==num)
//	makeHappen(num);

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
	             Runtime runtime = Runtime.getRuntime();
				   if(myurl.startsWith("www."))
				   myurl="http://" + myurl;
				   String[] args = { "osascript", "-e", "open location \"" + myurl + "\"" };
				   try
				   {
				     Process process = runtime.exec(args);
				   }
				   catch (IOException e)
				   {
				     // do what you want with this
				     // http://www.devdaily.com/java/mac-java-open-url-browser-osascript
				   }



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
void removeSelectionHighlight()
{
//	consoles[consoleNumber].getHighlighter().removeHighlights(consoles[consoleNumber]);//remove highlight if they click
try {

myHighlighter.removeAllHighlights();
}
catch(Exception d){}
}
void makeHappen(int con)
{
     sharedVariables.looking[consoleNumber]=con;
     consoles[consoleNumber].setStyledDocument(sharedVariables.mydocs[con]);
     Color my=new Color(193,153,153);
     setActiveTabForeground(con);

     //if(sharedVariables.consoleFonts[consoleNumber]!=null && sharedVariables.useConsoleFont[consoleNumber]== true)
	//	consoles[consoleNumber].setFont(sharedVariables.consoleFonts[consoleNumber]);
     //else
     if(sharedVariables.tabStuff[con].tabFont!=null)
     consoles[consoleNumber].setFont(sharedVariables.tabStuff[con].tabFont);
     else
     consoles[consoleNumber].setFont(sharedVariables.myFont);

     if(sharedVariables.tabStuff[con].BackColor!=null)
     consoles[consoleNumber].setBackground(sharedVariables.tabStuff[con].BackColor);
     else
     consoles[consoleNumber].setBackground(sharedVariables.BackColor);

     if(sharedVariables.tabStuff[con].ForColor!=null)
     consoles[consoleNumber].setForeground(sharedVariables.tabStuff[con].ForColor);
     else
     consoles[consoleNumber].setForeground(sharedVariables.ForColor);
	 setTitle(consoleTitle + sharedVariables.consoleTabTitles[con]);

     updateComboBox(con);

}

void initTabCombo()
{
	 		String [] tabNames = new String[sharedVariables.maxConsoleTabs];
	 		for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
	 			if(a==0)
	 				tabNames[a]="M0";
	 			else
	 			   tabNames[a]="C"+a;
	 		try {
	 		for(int ab=0; ab<sharedVariables.maxConsoleTabs; ab++)
	 			if(!sharedVariables.consoleTabCustomTitles[ab].equals(""))
	        			tabNames[ab]=sharedVariables.consoleTabCustomTitles[ab];
	        		else
	         		tabNames[ab]=sharedVariables.consoleTabTitles[ab];
			}
			catch(Exception d){}

	        tabChooser = new JComboBox(tabNames);
	         tabChooser.setEditable(false);

}
 void initTabChooser()
 {
			initTabCombo();

	         tabChooser.setSelectedIndex(sharedVariables.looking[consoleNumber]);
	         tabChooser.setEditable(false);
tabChooser.addActionListener(new ActionListener(){

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        try{

  		makeHappen(cb.getSelectedIndex());
  		giveFocus();
      //JFrame aframe = new JFrame();
      //aframe.setSize(200,200);
      //aframe.setTitle(comboMemory[sharedVariables.looking[consoleNumber]] + " " + sharedVariables.looking[consoleNumber] + " " + e.getActionCommand());
      //aframe.setVisible(true);
	}catch(Exception cant){}

    }

});
 }

 private void initComponents() {


        String[] prefixStrings = { ">"};

        prefixHandler = new JComboBox(prefixStrings);
        prefixHandler.setSelectedIndex(0);
        prefixHandler.setEditable(false);

		initTabCombo();

        // for 10 tabs we assume preselected index is ">"
        comboMemory = new String[sharedVariables.maxConsoleTabs];
        for(int cmem=0; cmem<sharedVariables.maxConsoleTabs; cmem++)
        comboMemory[cmem] = ">";
prefixHandler.addActionListener(new ActionListener(){

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        try{

        String mytext = (String)cb.getSelectedItem();
        if(mytext == null)
        return;
        comboMemory[sharedVariables.looking[consoleNumber]]=mytext;
       if(mytext.equals(">"))
        overall.Input.setForeground(sharedVariables.inputCommandColor);
        else
        overall.Input.setForeground(sharedVariables.inputChatColor);

        giveFocus();
      //JFrame aframe = new JFrame();
      //aframe.setSize(200,200);
      //aframe.setTitle(comboMemory[sharedVariables.looking[consoleNumber]] + " " + sharedVariables.looking[consoleNumber] + " " + e.getActionCommand());
      //aframe.setVisible(true);
	}catch(Exception cant){}

    }

});

        updateComboBox(sharedVariables.openConsoleCount);


		channelTabs = new JPaintedLabel[sharedVariables.maxConsoleTabs];
		for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
		{if(a==0)
		channelTabs[a]=new JPaintedLabel("M" + a, sharedVariables);
	    else
		{
			channelTabs[a]=new JPaintedLabel(sharedVariables.consoleTabTitles[a], sharedVariables);

			if(!sharedVariables.consoleTabCustomTitles[a].equals(""))
			channelTabs[a].setFullText(sharedVariables.consoleTabCustomTitles[a]);
			else
			channelTabs[a].setText(sharedVariables.consoleTabTitles[a]);
		}// end else

		}
	    tellLabel=new JLabel("tells");
	    tellCheckbox=new JCheckBox();
	    if(sharedVariables.openConsoleCount == 0)
	    tellCheckbox.setSelected(true);


		tellCheckbox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
					sharedVariables.tellconsole=consoleNumber;
					sharedVariables.updateTellConsole=1;
				}
});


for(int cona=0; cona<sharedVariables.maxConsoleTabs; cona++)
{

	final int con=cona;

channelTabs[con].addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
             if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2)
			 	makerightclickhappen(e, con);
			else
			{  makeHappen(con);
			}
            }

         public void mouseReleased(MouseEvent e) {
			 if (e.getButton() == MouseEvent.BUTTON3)
			 	;
			 else
			 {
            sharedVariables.looking[consoleNumber]=con;
            consoles[consoleNumber].setStyledDocument(sharedVariables.mydocs[con]);
            Color my=new Color(193,153,153);
            setActiveTabForeground(con);
            updateComboBox(con);
			}
            }


public void mouseEntered (MouseEvent me) {}
public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {}
public void makerightclickhappen(MouseEvent e, final int n)
{
JPopupMenu menu2=new JPopupMenu("Popup2");
JMenuItem item1 = new JMenuItem("clear tab chat");
 item1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            myoutput data = new myoutput();
            data.clearconsole=n;
            queue.add(data);
            }
       });
       menu2.add(item1);
JMenuItem item2 = new JMenuItem("customize tab");
 item2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           customizeTab(n);
            }
       });
     if(n!=0)
     menu2.add(item2);


JMenuItem item3 = new JMenuItem("set tab font");
 item3.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           setTabFont(n);
            }
       });

     menu2.add(item3);


JMenuItem item4 = new JMenuItem("set tab colors");
 item4.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           setTabColors(n);
            }
       });

     menu2.add(item4);

final JCheckBoxMenuItem item5 = new JCheckBoxMenuItem("show typed text");
 item5.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
				if(sharedVariables.tabStuff[n].typed == true)
				{
					sharedVariables.tabStuff[n].typed=false;
					item5.setSelected(false);
				}
				else
				{
					sharedVariables.tabStuff[n].typed=true;
					item5.setSelected(true);
				}
            }
       });
if(sharedVariables.tabStuff[n].typed == true)
	item5.setSelected(true);
else
	item5.setSelected(false);
     menu2.add(item5);



final JCheckBoxMenuItem item6 = new JCheckBoxMenuItem("suppress (told...");
 item6.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
				if(sharedVariables.tabStuff[n].told == false)
				{
					sharedVariables.tabStuff[n].told=true;
					item6.setSelected(true);
				}
				else
				{
					sharedVariables.tabStuff[n].told=false;
					item6.setSelected(false);
				}
            }
       });
if(sharedVariables.tabStuff[n].told == false)
	item6.setSelected(true);
else
	item6.setSelected(false);
     menu2.add(item6);



JCheckBoxMenuItem item7 = new JCheckBoxMenuItem("tells to this tab");
item7.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
				if(sharedVariables.tellsToTab == true && sharedVariables.tellTab == n) // i am curerntly directing tells to this tab
				{
					sharedVariables.tellsToTab = false;

				}
				else
				{	sharedVariables.tellsToTab = true;

					sharedVariables.tellTab = n;
				}
            }
       });

if(sharedVariables.tellsToTab == true && sharedVariables.tellTab == n)
item7.setSelected(true);
if(sharedVariables.tellconsole == consoleNumber)
menu2.add(item7);


add(menu2);
menu2.show(e.getComponent(),e.getX(),e.getY());
}



});

}




        consoles[consoleNumber] = new JTextPane();
        myHighlighter=consoles[consoleNumber].getHighlighter();
        if(sharedVariables.mydocs[consoleNumber] == null) // new logic we don't want to erase the console on restore
        	sharedVariables.mydocs[consoleNumber]=consoles[consoleNumber].getStyledDocument();
		else
			consoles[consoleNumber].setStyledDocument(sharedVariables.mydocs[consoleNumber]);

consoles[consoleNumber].addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            //if(e.isPopupTrigger())
				try {
						if(e.getClickCount() == 1 && e.getButton() != MouseEvent.BUTTON3)
						removeSelectionHighlight();

					if (e.getButton() == MouseEvent.BUTTON3 || (e.getClickCount() == 2 && sharedVariables.autopopup == true))
				{
               if(consoles[consoleNumber].getSelectedText().indexOf(" ") == -1)
               {
				   setupMenu(consoles[consoleNumber].getSelectedText());
				   menu3.show(e.getComponent(),e.getX(),e.getY());
			   }
               else
               menu.show(e.getComponent(),e.getX(),e.getY());
				}



	 }// end try
	 catch(Exception mousebad){};
 }

     /*  public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger())
               if(consoles[consoleNumber].getSelectedText().indexOf(" ") == -1)
               menu3.show(e.getComponent(),e.getX(),e.getY());
               else
               menu.show(e.getComponent(),e.getX(),e.getY());

       }*/


public void mouseEntered (MouseEvent me) {}


public void mouseExited (MouseEvent me) {}
public void mouseClicked (MouseEvent me) {

	}


      });



consoles[consoleNumber].addMouseListener(new MouseListener()
{
	public void mouseClicked(MouseEvent e)
	{    JTextPane editor = (JTextPane) e.getSource();
	if (! editor.isEditable())
	{      Point pt = new Point(e.getX(), e.getY());
	int pos = editor.viewToModel(pt);
	if (pos >= 0)
	{        // get the element at the pos
	// check if the elemnt has the HREF
	// attribute defined
	// if so notify the HyperLinkListeners
	//Style mine=getLogicalStyle(pos);
	Element e2=editor.getStyledDocument().getCharacterElement(pos);
	AttributeSet at = e2.getAttributes();
//String underline="false";
SimpleAttributeSet attrs = new SimpleAttributeSet();
StyleConstants.setUnderline(attrs, true);
String myurl = "";
if(at.containsAttributes(attrs))
{//underline = "true";
 //at has atributes
myurl += at.getAttribute(javax.swing.text.html.HTML.Attribute.HREF).toString();
String myurl2=myurl;
myurl2=myurl2.toLowerCase();
if(myurl2.startsWith("observe"))
{
	dispatchCommand(myurl);
}
else if(myurl2.startsWith("finger"))
{
	dispatchCommand(myurl);
}
else if(myurl2.startsWith("help"))
{
	dispatchCommand(myurl);
}
else if(myurl2.startsWith("accept"))
{
	dispatchCommand(myurl);
}
else if(myurl2.startsWith("decline"))
{
	dispatchCommand(myurl);
}
else if(myurl2.startsWith("match"))
{
	dispatchCommand(myurl);
}else if(myurl2.startsWith("examine"))
{
	dispatchCommand(myurl);
}else if(myurl2.startsWith("follow"))
{
	dispatchCommand(myurl);
}
else if(myurl2.startsWith("play"))
{
	dispatchCommand(myurl);
}
else if(myurl2.startsWith("games"))
{
	dispatchCommand(myurl);
}
else
openUrl(myurl);
}

/*	Element elm=editor.getStyledDocument().getParagraphElement(pos);
	Document doc=elm.getDocument();
	String doctext="";
	try {
		doctext=doc.getText(0, doc.getLength());
}
catch(Exception ee)
{}
	JFrame aframe = new JFrame();
	JPanel panel = new JPanel();
	JTextArea TA = new JTextArea();
	TA.setText(underline);
	TA.setColumns(40);
	TA.setRows(20);

	panel.add(TA);
	aframe.add(panel);
	aframe.pack();

	//aframe.setSize(100,100);
	aframe.setVisible(true);

*/
	}
	}
	}// end click event



		 public void mousePressed(MouseEvent e) {}
		 public void mouseEntered (MouseEvent me) {}
		 public void mouseReleased (MouseEvent me) {}
		 public void mouseExited (MouseEvent me) {}


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
				openUrl("www.adam16mr.org");
		 	}

             }catch(Exception e)
                {}
            }
        });


        scrollbutton = new JButton("no scroll");
		scrollbutton.setVisible(false);
		scrollbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				scrollnow=(scrollnow+1)%2;
				if(scrollnow == 1)
				scrollbutton.setText("no scroll");
				else
				scrollbutton.setText("autoscroll");

			/*	JFrame aframe = new JFrame();
				int d =consoles[consoleNumber].getScrollableBlockIncrement(consoles[consoleNumber].getVisibleRect(), SwingConstants.VERTICAL, -1 );
				int f=sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue();
				int g= sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum();
				aframe.setTitle(" curernt value" + f + " and maximum " + g + " and block inc is "+ d);
				aframe.setVisible(true);
			*/
			}
});


       // newbox.setColumns(20);
       // newbox.setLineWrap(true);
       // newbox.setRows(5);
       // newbox.setWrapStyleWord(true);
        consoles[consoleNumber].setEditable(false);
        sharedVariables.ConsoleScrollPane[consoleNumber] = new JScrollPane(consoles[consoleNumber]);



    overall = new subPanel();
    add(overall);
consoles[consoleNumber].addMouseWheelListener(new MouseWheelListener()
//sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().addMouseWheelListener(new MouseWheelListener()
{
    public void mouseWheelMoved(MouseWheelEvent e) {
       String message;
       int notches = e.getWheelRotation();

			int mult = 27;


  if (notches < 0) {
           message = "Mouse wheel moved UP "
                        + -notches + " notch(es)";
       } else {
           message = "Mouse wheel moved DOWN "
                        + notches + " notch(es)" ;
       }
       if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
          /* message += "    Scroll type: WHEEL_UNIT_SCROLL" + newline;
           message += "    Scroll amount: " + e.getScrollAmount()
                   + " unit increments per notch" + newline;
           message += "    Units to scroll: " + e.getUnitsToScroll()
                   + " unit increments" + newline;
           message += "    Vertical unit increment: "
               + sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getUnitIncrement(1)
               + " pixels" + newline;
		*/

               if (notches < 0)
               {		sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() - (e.getScrollAmount() * mult));
		   	   			scrollnow=0;
   						wheelIsScrolling=true;

		   		}
               else
               	{

							int d =(e.getScrollAmount() * mult);
							int myvalue =100 + d;
							if(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + myvalue > sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum())

							{wheelIsScrolling = false;
							scrollnow=1;
				   			sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum());

							}
							else
							{
				   			scrollnow=0;
				   			wheelIsScrolling=false;
				   			sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + (e.getScrollAmount() * mult));

							}
				}

 } else { //scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL
          /* message += "    Scroll type: WHEEL_BLOCK_SCROLL" + newline;
           message += "    Vertical block increment: "
               + sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getBlockIncrement(1)
               + " pixels" + newline;
       */

          			scrollnow=0;
	      			wheelIsScrolling=true;

       					int block=sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getBlockIncrement(1) * mult;
                      if (notches < 0)
	                  {
						  sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() - block);

				  		}
	                  else
	                  {

						  int myvalue =100 + block;
							if(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + myvalue > sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum())

							{wheelIsScrolling = false;
						  	scrollnow=1;
						}

						  sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + block);

					}
       }

	}
});

	sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
	{
	public void adjustmentValueChanged(AdjustmentEvent e) {

		if(wheelIsScrolling)
		{
			wheelIsScrolling=false;
			return;

		}
	if(scrollnow == 1 && false == sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValueIsAdjusting())
	{
e.getAdjustable().setValue(e.getAdjustable().getMaximum());




	}// end if not adjusting
	else
	{
		if(true == sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValueIsAdjusting())
		scrollnow = 0;

			int d =consoles[consoleNumber].getScrollableBlockIncrement(consoles[consoleNumber].getVisibleRect(), SwingConstants.VERTICAL, -1 );
int myvalue =60 + d;
	try{
		if(sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValue() + myvalue > sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getMaximum())
		scrollnow = 1;

				if(scrollnow == 1 && sharedVariables.ConsoleScrollPane[consoleNumber].getVerticalScrollBar().getValueIsAdjusting() == false)
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());

	}// end try
	catch(Exception e1)
	{}
	}// end else
	}// end  is adjustment value changed
	}// end adjustment class
);

	consoles[consoleNumber].setForeground(sharedVariables.ForColor);
		consoles[consoleNumber].setBackground(sharedVariables.BackColor);
		if(sharedVariables.myFont != null)
		consoles[consoleNumber].setFont(sharedVariables.myFont);
	makeHappen(consoleNumber);

   Color lc=new Color(0,0,0);
/*	for(int a=0; a<10; a++)
		channelTabs[a].setForeground(lc);
*/

 setActiveTabForeground(sharedVariables.openConsoleCount);

	for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
		channelTabs[a].setOpaque(true);

	for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
		channelTabs[a].setBackground(sharedVariables.tabBackground);


    }

void setActiveTabForeground(int i)
{
	for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
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
      if(sharedVariables.myServer.equals("ICC") && sharedVariables.myname.length() > 0)
      	output.data="`c" + sharedVariables.looking[consoleNumber] + "`" + mycommand;
      else
      	output.data=mycommand;
	  output.consoleNumber=consoleNumber;
      queue.add(output);

	try {
	StyledDocument doc=sharedVariables.mydocs[sharedVariables.looking[consoleNumber]];

	doc.insertString(doc.getEndPosition().getOffset(), mycommand, null);


	for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
	if(sharedVariables.looking[consoleNumber]==sharedVariables.looking[a])
	consoles[a].setStyledDocument(doc);
		}
	catch(Exception E){ }

}


public void componentResized( ComponentEvent e)
{
//updateSize();

}
public void componentHidden( ComponentEvent e)
{

}
public void componentShown( ComponentEvent e)
{

}
public void componentMoved( ComponentEvent e)
{
//updateSize();
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


int scrollnow;
boolean wheelIsScrolling;

String myglobalinput;
JButton scrollbutton;


void setupMenu(String handle)
{

menu3=new JPopupMenu("Popup");

JMenuItem [] items;
if(sharedVariables.rightClickMenu.size() > 0)
{
int removal=0;
if(sharedVariables.looking[consoleNumber]==0)
	removal=1;


items = new JMenuItem[sharedVariables.rightClickMenu.size()-removal];

for(int m=0; m< sharedVariables.rightClickMenu.size()-removal; m++)
{

	final int mfinal=m;
items[m] = new JMenuItem("" + sharedVariables.rightClickMenu.get(m) + " " + handle);
items[m].addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
          String name =  consoles[consoleNumber].getSelectedText();
          if( sharedVariables.rightClickMenu.get(mfinal).equals("Tell"))
         {
			 if(sharedVariables.looking[consoleNumber]!=0)
			 addNameToCombo(name);
	 	}
         else
          	doCommand(sharedVariables.rightClickMenu.get(mfinal) + " " + name + "\n");
          }
      });
      menu3.add(items[m]);
      if(m == 3 || m == 7 || m == 9 || (removal==0 && m==12))
      menu3.addSeparator();

}// end for
}// end if any items

menu3.addSeparator();
 JMenuItem item12 = new JMenuItem("Copy");
 item12.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          consoles[consoleNumber].copy();
			giveFocus();
            }
       });
       menu3.add(item12);

 JMenuItem item13 = new JMenuItem("Copy&Paste");
  item13.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
           consoles[consoleNumber].copy();
           overall.Input.paste();
           giveFocus();

             }
        });
        menu3.add(item13);





 add(menu3);

}// end menu setup




void doCommand(String mycommand)
{
	myoutput output = new myoutput();
	output.data="`c" + sharedVariables.looking[consoleNumber] + "`" + mycommand;
	output.consoleNumber=sharedVariables.looking[consoleNumber];
    queue.add(output);
    giveFocus();

}

void doToolBarCommand(int n)
{
					toolbarCommands commander = new toolbarCommands();
				commander.dispatchCommand(n, sharedVariables.looking[consoleNumber], false, sharedVariables,  queue);
				String mes = sharedVariables.userButtonCommands[n] + "\n";
				StyledDocument doc=sharedVariables.mydocs[sharedVariables.looking[consoleNumber]];

						Style styleQ = doc.addStyle(null, null);

						StyleConstants.setForeground(styleQ, sharedVariables.typedColor );
						//StyleConstants.setUnderline(attrs, true);
						SimpleAttributeSet attrs = new SimpleAttributeSet();

						StyleConstants.setItalic(attrs, true);
						StyleConstants.setForeground(attrs, sharedVariables.typedColor);
	try {
		doc.insertString(doc.getEndPosition().getOffset(), mes, attrs);
		}catch(Exception mydoc){}

}

void updateComboBox(int n)
{
	try {

	// int cindex=sharedVariables.console[Integer.parseInt(dg.getArg(1))];
    //  JFrame aaframe = new JFrame();
    //   aaframe.setSize(200,200);
    //   aaframe.setTitle(comboMemory[n] + " is combo memory, in update combo box and n is " + n);
    //   aaframe.setVisible(true);
	int count=0;
	int foundIndex = -1;
	int a=0;

	// first loop is to check items we would add against combo memory BEFORE we remove the items triggering updates on combo memory
	for(a=0; a<400; a++)
	if(sharedVariables.console[sharedVariables.looking[consoleNumber]][a] == 1 && sharedVariables.looking[consoleNumber] != 0)
	{
		count++;
		String aItem = "Tell " + a + " ";

		if(aItem.equals(comboMemory[n]))
		{

		foundIndex=count;// the idea is we want the index that we added the item that should be selected ( its in comboMemory)


		}
	}

// now check names
	for(a=0; a<sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].size(); a++)
	{
		count++;
		String aItem = "Tell " + sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].get(a) + " ";

		if(aItem.equals(comboMemory[n]))
		{

		foundIndex=count;// the idea is we want the index that we added the item that should be selected ( its in comboMemory)


		}
	}



		prefixHandler.removeAllItems();
		prefixHandler.addItem(">");
	for(a=0; a<400; a++)
	if(sharedVariables.console[sharedVariables.looking[consoleNumber]][a] == 1 && sharedVariables.looking[consoleNumber] != 0)
	{

		String aItem = "Tell " + a + " ";
		prefixHandler.addItem(aItem);

	}

	// now add back names
	for(a=0; a<sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].size(); a++)
	{

		String aItem = "Tell " + sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].get(a) + " ";
		prefixHandler.addItem(aItem);

	}






		if(foundIndex > -1)
			prefixHandler.setSelectedIndex(foundIndex);
	}catch(Exception badcomboupdate){}
}

void updateTabChooserCombo()
{
int oldNumber = sharedVariables.looking[consoleNumber];
tabChooser.removeAllItems();
	 		for(int ab=0; ab<sharedVariables.maxConsoleTabs; ab++)
	 			if(!sharedVariables.consoleTabCustomTitles[ab].equals(""))
	        			tabChooser.addItem(sharedVariables.consoleTabCustomTitles[ab]);
	        		else
	         		tabChooser.addItem(sharedVariables.consoleTabTitles[ab]);

		tabChooser.setSelectedIndex(oldNumber);
//	makeHappen(sharedVariables.looking[consoleNumber]);
}
void addNameToCombo(String name)
{
	try {

		for(int z=0; z<sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].size(); z++)
			if(sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].get(z).equals(name))
				return;

			String aItem = "Tell " + name + " ";
			prefixHandler.addItem(aItem);
			sharedVariables.comboNames[sharedVariables.looking[consoleNumber]].add(name);
			prefixHandler.setSelectedIndex(prefixHandler.getItemCount()-1);
}
catch(Exception d){}
}

















void giveFocus()
{
 SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                              //  JComponent comp = DataViewer.getSubcomponentByName(e.getInternalFrame(),
                                //SearchModel.SEARCHTEXT);

                             overall.Input.setFocusable(true);
                               overall.Input.setRequestFocusEnabled(true);
                                //Input.requestFocus();
                           overall.Input.requestFocusInWindow();
                            } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });

}

/****************************************************************************************/





class subPanel extends JPanel
{
JTextField Input;
arrowManagement arrowManager;
subPanel()
{


Input = new JTextField();
Input.setFont(sharedVariables.inputFont);
arrowManager = new arrowManagement();


        // Input.addActionListener (this);
Input.addKeyListener(new KeyListener() {public void keyPressed(KeyEvent e)
{
        int a=e.getKeyCode();

        if( e.getModifiersEx() == 128 )// ctrl + t
        {
			if(a == 84)
			{
				//switchWindows();
				return;
			}
			if(a == 49)
			{
				doToolBarCommand(1);
				return;
			}
			if(a == 50)
			{
				doToolBarCommand(2);
				return;
			}
			if(a == 51)
			{
				doToolBarCommand(3);
				return;
			}
			if(a == 52)
			{
				doToolBarCommand(4);
				return;
			}
			if(a == 53)
			{
				doToolBarCommand(5);
				return;
			}
			if(a == 54)
			{
				doToolBarCommand(6);
				return;
			}
			if(a == 55)
			{
				doToolBarCommand(7);
				return;
			}
			if(a == 56)
			{
				doToolBarCommand(8);
				return;
			}
			if(a == 57)
			{
				doToolBarCommand(9);
				return;
			}
			if(a == 48)
			{
				doToolBarCommand(0);
				return;
			}


		}



      if( e.getModifiersEx() == 512)// alt
      {
		 if(a == 49  )
		 {

			 sharedVariables.looking[consoleNumber]=0;
			  makeHappen(0);
		 }
		 if(a == 50)
		 {

			 sharedVariables.looking[consoleNumber]=1;
			makeHappen(1);
		}
		 if(a == 51  )
		 {
			 ;
			 sharedVariables.looking[consoleNumber]=2;
			 makeHappen(2);
		 }
		 if(a == 52)
		 {

			 sharedVariables.looking[consoleNumber]=3;
			 makeHappen(3);
		 }
		 if(a == 53  )
		 {

			 sharedVariables.looking[consoleNumber]=4;
			 makeHappen(4);
		 }
		 if(a == 54)
		 {
			sharedVariables.looking[consoleNumber]=5;
			makeHappen(5);
		 }
		 if(a == 55  )
		 {
			 makeHappen(6);
			 sharedVariables.looking[consoleNumber]=6;
		 }
		 if(a == 56)
		 {
			 sharedVariables.looking[consoleNumber]=7;
			 makeHappen(7);

		 }
		 if(a == 57  )
		 {
			 sharedVariables.looking[consoleNumber]=8;
			 makeHappen(8);
		}
		if(a == 48)
		{
			sharedVariables.looking[consoleNumber]=9;
			makeHappen(9);
		}

		 if(a == 45  )
		 {
			 sharedVariables.looking[consoleNumber]=10;
			 makeHappen(10);
		}
		if(a == 61)
		{
			sharedVariables.looking[consoleNumber]=11;
			makeHappen(11);
		}

         if(a == 38  ) // alt + up arrow
         {    int con = sharedVariables.looking[consoleNumber] ;

         if(sharedVariables.tabStuff[con].tabFont != null)
         {
         float fontsize =(float) sharedVariables.tabStuff[con].tabFont.getSize();
         fontsize++;
         sharedVariables.tabStuff[con].tabFont=sharedVariables.tabStuff[con].tabFont.deriveFont(fontsize);
         makeHappen(con);
         return;
		}else
         {
         float fontsize =(float) sharedVariables.myFont.getSize();
         fontsize++;
         sharedVariables.myFont=sharedVariables.myFont.deriveFont(fontsize);
         makeHappen(con);
         return;
		}

         }
         if(a == 40  ) // alt + down arrow
         {    int con = sharedVariables.looking[consoleNumber] ;

         if(sharedVariables.tabStuff[con].tabFont != null)
         {
         float fontsize =(float) sharedVariables.tabStuff[con].tabFont.getSize();
         fontsize --;
         sharedVariables.tabStuff[con].tabFont=sharedVariables.tabStuff[con].tabFont.deriveFont(fontsize);
         makeHappen(con);
         return;
		}else

		         {
		         float fontsize =(float) sharedVariables.myFont.getSize();
		         fontsize --;
		         sharedVariables.myFont=sharedVariables.myFont.deriveFont(fontsize);
		         makeHappen(con);
		         return;
				}

         }


	  }
      if( e.getModifiersEx() == 64)
       {
         if(a == 39  ) // shift + right arrow
         {    int con = sharedVariables.looking[consoleNumber] + 1;
         if(con == sharedVariables.maxConsoleTabs)
         con=0;
         if(sharedVariables.consoleLayout == 3)
         	tabChooser.setSelectedIndex(con);
         else
         	makeHappen(con);


         }
         if(a == 37  ) // shift + left arrow
         {    int con = sharedVariables.looking[consoleNumber] - 1;
         if(con == -1)
         con=sharedVariables.maxConsoleTabs-1;
         if(sharedVariables.consoleLayout == 3)
         	tabChooser.setSelectedIndex(con);
         else
         	makeHappen(con);

         }


  }

         if(a == 27) // esc
         {
			 Input.setText("");
		 }


         if(a == 10)
 {   lastcommand=Input.getText();
     arrowManager.add(lastcommand);

	 String mes = lastcommand + "\n";

	 	int index = prefixHandler.getSelectedIndex();
	 	String pre="";
	 	pre = prefixHandler.getItemAt(index).toString();

	 	if(index > 0 && !Input.getText().startsWith("/")) // we done use tell channel prefix if starts with /
	 	mes = pre + mes;


      myoutput output = new myoutput();
      if(sharedVariables.myServer.equals("ICC") && sharedVariables.myname.length() > 0)
      output.data="`c" + sharedVariables.looking[consoleNumber] + "`" + mes;// having a name means level 1 is on if on icc and this `phrase`mess will be used to direct output back to this console
      else
      output.data=mes;

      output.consoleNumber=consoleNumber;
      queue.add(output);
      Input.setText("");

	  			try {
				StyledDocument doc=sharedVariables.mydocs[sharedVariables.looking[consoleNumber]];
	  			if(sharedVariables.password == 0)
	  			{




					if(sharedVariables.tabStuff[sharedVariables.looking[consoleNumber]].typed == true)
					{
						Style styleQ = doc.addStyle(null, null);

						StyleConstants.setForeground(styleQ, sharedVariables.typedColor );
						//StyleConstants.setUnderline(attrs, true);
						SimpleAttributeSet attrs = new SimpleAttributeSet();

						StyleConstants.setItalic(attrs, true);
						StyleConstants.setForeground(attrs, sharedVariables.typedColor);
					int SUBFRAME_CONSOLES=0;
					int maxLinks =75;
					myDocWriter.processLink(doc, mes, sharedVariables.typedColor, sharedVariables.looking[consoleNumber], maxLinks, SUBFRAME_CONSOLES, attrs, null);
					//doc.insertString(doc.getEndPosition().getOffset(), mes, attrs);

					}

			}
	  			else
	  			{
					if(sharedVariables.tabStuff[sharedVariables.looking[consoleNumber]].typed == true)
						doc.insertString(doc.getEndPosition().getOffset(), "*******\n", null);
					sharedVariables.password=0;
				}

					for(int aa=0; aa<sharedVariables.maxConsoleTabs; aa++)
					if(sharedVariables.looking[consoleNumber]==sharedVariables.looking[aa])
					consoles[aa].setStyledDocument(doc);
				}
				catch(Exception E){ }
        }
        if((a == 120 || a == 119) && e.getModifiersEx() != 64)// f9
        {
                String s=Input.getText();
                String person;
                if(s.length() == 0)
                	person = sharedVariables.F9Manager.getName(true);
                else
                	person = sharedVariables.F9Manager.getName(false);

                if(person.length()>0)
                Input.setText("/Tell " + person + " ");
        }

       if((a == 120 || a== 119) && e.getModifiersEx() == 64)// shift f9
        {
                String s=Input.getText();
                String person;
                if(s.length() == 0)
                	person = sharedVariables.F9Manager.getNameReverse(true);
                else
                	person = sharedVariables.F9Manager.getNameReverse(false);

                if(person.length()>0)
                Input.setText("/Tell " + person + " ");

	 	}

        if(a == 40)// down
        {
          arrowManager.down();
        }
        if(a == 38)// up
        {
                 arrowManager.up();

               // if(lastcommand.length() >0)
               // Input.setText(lastcommand);
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


if(sharedVariables.consoleLayout==1)
	setMyLayout1();
else if(sharedVariables.consoleLayout==3)
setMyLayout3();
else
	setMyLayout2();

}

void setMyLayout1()
{

   GroupLayout layout = new GroupLayout(getContentPane());
   // GroupLayout layout = new GroupLayout(this);
   //    setLayout(layout);
       getContentPane().setLayout(layout);
	int inputHeight=17;
	try {
	inputHeight=sharedVariables.inputFont.getSize();

	}catch(Exception inputing){}


	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h1 = layout.createSequentialGroup();


	SequentialGroup middle = layout.createSequentialGroup();
	ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h3 = layout.createSequentialGroup();

	//Add a scroll pane and a label to the parallel group h2
	h2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber], GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);

	h3.addComponent(prefixHandler, GroupLayout.DEFAULT_SIZE, 105, 105);
	h3.addComponent(Input, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);
    h3.addComponent(scrollbutton);


		for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
		{
			middle.addComponent(channelTabs[a],GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
			middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		}
			middle.addComponent(tellLabel);

			middle.addComponent(tellCheckbox);





h2.addGroup(h3);
h2.addGroup(middle);
h1.addGroup(h2);

	//Add the group h1 to the hGroup
	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);



	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading


SequentialGroup v4 = layout.createSequentialGroup();

ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

	ParallelGroup vmiddle = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);


	ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);


	v2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber], GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);

		for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
			vmiddle.addComponent(channelTabs[a]);
		vmiddle.addComponent(tellLabel);
		vmiddle.addComponent(tellCheckbox);


		v1.addComponent(prefixHandler, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		v1.addComponent(scrollbutton);
		v1.addComponent(Input, inputHeight, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);


v4.addGroup(v2);
v4.addGroup(vmiddle);
v4.addGroup(v1);

	vGroup.addGroup(v4);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);
//pack();
}
void setMyLayout2()
{

    GroupLayout layout = new GroupLayout(getContentPane());
    //GroupLayout layout = new GroupLayout(this);
     getContentPane().setLayout(layout);
		//	setLayout(layout);
	int inputHeight=17;
	try {
	inputHeight=sharedVariables.inputFont.getSize();

	}catch(Exception inputing){}

	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h1 = layout.createSequentialGroup();


	SequentialGroup middle = layout.createSequentialGroup();
	SequentialGroup middle2 = layout.createSequentialGroup();
	ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h3 = layout.createSequentialGroup();

	//Add a scroll pane and a label to the parallel group h2
	h2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber], GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);

	h3.addComponent(prefixHandler, GroupLayout.DEFAULT_SIZE, 105, 105);
	h3.addComponent(Input, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);
    h3.addComponent(scrollbutton);

int half = (int) sharedVariables.maxConsoleTabs/2;
ParallelGroup [] middles = new ParallelGroup[half];

for(int a=0; a<half; a++)
middles[a]=layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		for(int a=0; a<half; a++)
		{
			middles[a].addComponent(channelTabs[a*2],GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
			middles[a].addComponent(channelTabs[a*2+1],GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
			middle.addGroup(middles[a]);
			middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		}
			middle.addComponent(tellLabel);

			middle.addComponent(tellCheckbox);

		/*for(int a=half; a<sharedVariables.maxConsoleTabs; a++)
		{
			middle2.addComponent(channelTabs[a],GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
			middle2.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		}
*/



h2.addGroup(h3);
h2.addGroup(middle);
//h2.addGroup(middle2);
h1.addGroup(h2);

	//Add the group h1 to the hGroup
	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);



	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading


SequentialGroup v4 = layout.createSequentialGroup();

ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

	ParallelGroup vmiddle = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
	ParallelGroup vmiddle2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);


	ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);


	v2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber], GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);

		for(int a=0; a<half; a++)
			vmiddle.addComponent(channelTabs[a*2]);
		vmiddle.addComponent(tellLabel);
		vmiddle.addComponent(tellCheckbox);
		for(int a=0; a<half; a++)
			vmiddle2.addComponent(channelTabs[a*2+1]);


		v1.addComponent(prefixHandler, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		v1.addComponent(scrollbutton);
		v1.addComponent(Input, inputHeight, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);


v4.addGroup(v2);
v4.addGroup(vmiddle);
v4.addGroup(vmiddle2);
v4.addGroup(v1);

	vGroup.addGroup(v4);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);
//pack();
}


void setMyLayout3()
{

    GroupLayout layout = new GroupLayout(getContentPane());
   //GroupLayout layout = new GroupLayout(this);
   getContentPane().setLayout(layout);
	int inputHeight=17;
	try {
	inputHeight=sharedVariables.inputFont.getSize();

	}catch(Exception inputing){}

	initTabChooser();
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h1 = layout.createSequentialGroup();


	//SequentialGroup middle = layout.createSequentialGroup();
	ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h3 = layout.createSequentialGroup();

	//Add a scroll pane and a label to the parallel group h2
	h2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber], GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
	h3.addComponent(prefixHandler, GroupLayout.DEFAULT_SIZE, 95, 95);

	h3.addComponent(Input, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE);
	h3.addComponent(tellCheckbox,21,21,21);
	h3.addComponent(tabChooser, GroupLayout.DEFAULT_SIZE, 75, 75);
    //h3.addComponent(scrollbutton);


	/*	for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
		{
			middle.addComponent(channelTabs[a],GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE);
			middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		}
			middle.addComponent(tellLabel);

			middle.addComponent(tellCheckbox);

*/



h2.addGroup(h3);
//h2.addGroup(middle);
h1.addGroup(h2);

	//Add the group h1 to the hGroup
	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);



	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading


SequentialGroup v4 = layout.createSequentialGroup();

ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

//	ParallelGroup vmiddle = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);


	ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);


	v2.addComponent(sharedVariables.ConsoleScrollPane[consoleNumber], GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);

/*		for(int a=0; a<sharedVariables.maxConsoleTabs; a++)
			vmiddle.addComponent(channelTabs[a]);
		vmiddle.addComponent(tellLabel);
		vmiddle.addComponent(tellCheckbox);
*/
		v1.addComponent(tellCheckbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		v1.addComponent(tabChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		v1.addComponent(prefixHandler, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		//v1.addComponent(scrollbutton);
		v1.addComponent(Input, inputHeight, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);


v4.addGroup(v2);
//v4.addGroup(vmiddle);
v4.addGroup(v1);

	vGroup.addGroup(v4);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);
//pack();
}


void recreate()
 {

 getContentPane().removeAll();

 if(sharedVariables.consoleLayout==1)
 	setMyLayout1();
 else if(sharedVariables.consoleLayout==2)
 	setMyLayout2();
else
 	setMyLayout3();


 // this.add(overall);
 // this.setVisible(true);
 }


class arrowManagement
{
 ArrayList list;
 int head;
 int tail;
 int index;
 int max;


 arrowManagement()
 {
  list = new ArrayList();
 head=tail=index-1;
 max=10;
 }



 void down()
 {
    if(Input.getText().equals(""))
    return;
    if(head == -1)
    return;
  if(index < tail)
  {
   index++;
   Input.setText((String)list.get(index));

  }
    // if  input is empty do nothing
    // otherwise if iterator is not at top iterate and grab command
 }// end down
 void up()
 {
  // if input is empty reset iterator and return tail
  // otherwise iterate one and return item
  if(head == -1)
  return;
  if(Input.getText().equals(""))
  index = tail;
  else
  {
   // index is set to 1 more than tail when at initial position
   index --;
   if(index < 0)
   index=tail;
  }
   Input.setText((String)list.get(index));
 }// end up
 void add(String mes)
 {
   // add to queue, delete if more than 10 last of commands, reset iterator
  if(mes.equals(""))
  return;

  list.add(mes);
  if(head == -1)
  {
   head=tail=0;
   index=1;
   return;
  }
  else if(tail < max -1)
 {
   tail++;
   index=tail+1;
 }
 else
 {
  list.remove(0);
  index=tail+1;
 }


   // add to queue, delete if more than 10 last of commands, reset iterator
 }// end add

} // end arrow manager










 }// end panel


}// end subframe