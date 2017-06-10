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
import java.util.StringTokenizer;
import java.util.concurrent.locks.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.event.ChangeEvent.*;
import java.util.ArrayList;
import java.util.Calendar;

class gameboardConsolePanel extends JPanel
{
JTextPane [] gameconsoles;
ConcurrentLinkedQueue<newBoardData> gamequeue;
ConcurrentLinkedQueue<myoutput> queue;
    JScrollPane jScrollPane1;

	JPopupMenu menu;
	JPopupMenu menu2;
	JPopupMenu menu3;

	String lastcommand;
	int madeTextPane;
	JPaintedGameLabel [] channelTabs;
	JPaintedLabel mainConsoleTab;
        int mainConsoleIndex=0;// points main console tab to main to start
	JLabel tellLabel;
	JCheckBox tellCheckbox;
        boolean wheelIsScrolling=false;

	JTextField Input;
	int scrollnow;
	String myglobalinput;
arrowManagement arrowManager;
	JComboBox prefixHandler;
channels sharedVariables;
gamestuff gameData;
JTextPane [] consoles;
subframe [] consoleSubframes;
JDesktopPaneCustom myself;
docWriter myDocWriter;
int [] comboMemory;
int blockInc=23;
boolean tryOnce = true;
gameboardControlsPanel mycontrolspanel;
gameboardTop topGame;
gameboardPanel mypanel;
gameboardConsolePanel(gameboardTop topGame1, JTextPane consoles1[], subframe consoleSubframes1[], channels sharedVariables1, gamestuff gameData1, JTextPane gameconsoles1[], ConcurrentLinkedQueue<newBoardData> gamequeue1, ConcurrentLinkedQueue<myoutput> queue1, docWriter myDocWriter1, gameboardControlsPanel mycontrolspanel1, gameboardPanel mypanel1)
{
topGame=topGame1;
mypanel=mypanel1;
mycontrolspanel=mycontrolspanel1;
sharedVariables=sharedVariables1;
gameData = gameData1;
gameconsoles=gameconsoles1;
gamequeue=gamequeue1;
queue=queue1;
scrollnow=1;
consoles=consoles1;
consoleSubframes=consoleSubframes1;
myDocWriter=myDocWriter1;
 arrowManager=new arrowManagement();
initComponents();
}




// makehappen is called by every game tabs button  listener
// its even called  by the telnet class when new moves  come  in or moves are  made
//  to switch tabs to the next board for the simul giver
//  essentially it switches the game that is shown on the  board
public void makehappen(int i)
{
            gameconsoles[gameData.BoardIndex].getHighlighter().removeAllHighlights();
             int physicalTab=i;
             i=sharedVariables.tabLooking[i];// translate to tab its on
             int lastTabNumber=-1; // for erasing tabi'monbrackground from last tab make it active tab color since it was just active
             for(int twi=0; twi < sharedVariables.maxGameTabs; twi++)
             if(sharedVariables.tabLooking[twi] == gameData.LookingAt)
             {
              lastTabNumber=twi;
              break;
             }

             if(i == -1)
             return;
             

           //if(i>=sharedVariables.openBoardCount)
           // return;
          // if(channelTabs[physicalTab].isVisible()== false)
            //	channelTabs[physicalTab].setVisible(true);
            int oldLookingAt = gameData.LookingAt;
            gameData.LookingAt=i;
            sharedVariables.Looking[gameData.BoardIndex]=i;
            if (sharedVariables.mygame[gameData.LookingAt] != null && sharedVariables.mygame[gameData.LookingAt].state ==
            sharedVariables.STATE_EXAMINING)
            {
                double time=System.currentTimeMillis();
                sharedVariables.mygame[gameData.LookingAt].whitenow = (long) time;
                sharedVariables.mygame[gameData.LookingAt].blacknow = (long) time;

            }
             try {
            if(lastTabNumber > -1)
            channelTabs[lastTabNumber].setBackground(sharedVariables.tabBackground);
             } catch(Exception a12) { }
             try {
            setActiveTabForeground(physicalTab);
             }catch(Exception a13) { }
         //  mypanel.repaint();
         // mycontrolspanel.repaint();



 			//  need a way to set title from this class ma 5-29-10
 			//if(sharedVariables.mygame[gameData.LookingAt].title.length() >0)
 			//setTitle(sharedVariables.mygame[gameData.LookingAt].title);

                        mypanel.movingpiece=0;
                        mypanel.movingexaminepiece=0;
                        mypanel.inTheAir=false;
 			super.repaint();
 			if(sharedVariables.useTopGames == true)
 			 if(topGame != null);
                         topGame.repaint();

 			try {prefixHandler.setSelectedIndex(comboMemory[i]); }catch(Exception dummy){}
 			sharedVariables.lastButton=gameData.BoardIndex;
 			
 			try {
 			sharedVariables.moveSliders[gameData.BoardIndex].setMaximum(sharedVariables.mygame[gameData.LookingAt].turn);
 			sharedVariables.moveSliders[gameData.BoardIndex].setValue(sharedVariables.mygame[gameData.LookingAt].turn);
                        } catch(Exception a14) { }

 			sharedVariables.gamelooking[gameData.BoardIndex]=gameData.LookingAt;

                         if(gameData.LookingAt == sharedVariables.engineBoard && sharedVariables.engineOn == true )
                         {
                           if(gameData.LookingAt != oldLookingAt) {
                             sharedVariables.mygame[gameData.LookingAt].clickCount--;
                           }
                            try {
                           if(sharedVariables.mygame[gameData.LookingAt].clickCount %2 == 0)
                           setEngineDoc();
                           else {
                           setGameDoc();
                           }
                            }
                           catch(Exception a19) { }
                            if(gameData.LookingAt == oldLookingAt && tryOnce == true) {
                              tryOnce = false;
                            try {
                              String promptText = "Click tab to toggle analysis back on\n";
                              sharedVariables.mygamedocs[gameData.LookingAt].insertString(sharedVariables.mygamedocs[gameData.LookingAt].getLength(), promptText, null);
                            }catch(Exception ee){}

                           }
                            sharedVariables.mygame[gameData.LookingAt].clickCount++;
                         }
                         else
                         setGameDoc();


                         sharedVariables.pointedToMain[gameData.BoardIndex]=false;// this tells us if the tab is on a game but console is on main
 			 try {
                         sharedVariables.gametable[gameData.BoardIndex].setModel(sharedVariables.mygametable[gameData.LookingAt].gamedata);
			adjustMoveList();
			  } catch(Exception a111) { }
 								// after clicking a game tab the console is not pointed to main but when it is we dont change any other info like LookingAt just the console so we need to have a way of telling when chat is going to main like when you type somethi
}
void setEngineDoc()
{
  
  try {
gameconsoles[gameData.BoardIndex].setStyledDocument(sharedVariables.engineDoc);
if(sharedVariables.analysisFont != null) {
  gameconsoles[gameData.BoardIndex].setFont(sharedVariables.analysisFont);
}
gameconsoles[gameData.BoardIndex].setForeground(sharedVariables.analysisForegroundColor);
gameconsoles[gameData.BoardIndex].setBackground(sharedVariables.analysisBackgroundColor);
  } catch(Exception dui) { }
}
void  setGameDoc()
 { 
   try {
   gameconsoles[gameData.BoardIndex].setStyledDocument(sharedVariables.mygamedocs[gameData.LookingAt]);
   gameconsoles[gameData.BoardIndex].setForeground(sharedVariables.ForColor);
gameconsoles[gameData.BoardIndex].setBackground(sharedVariables.BackColor);
if(sharedVariables.myFont != null)
	gameconsoles[gameData.BoardIndex].setFont(sharedVariables.myFont);
   }catch(Exception dui2) { }
 }
void setMainConsoleDoc()
{   try {
gameconsoles[gameData.BoardIndex].setStyledDocument(sharedVariables.mydocs[mainConsoleIndex]);
     if(sharedVariables.tabStuff[mainConsoleIndex].tabFont!=null)
     gameconsoles[gameData.BoardIndex].setFont(sharedVariables.tabStuff[mainConsoleIndex].tabFont);
     else
    gameconsoles[gameData.BoardIndex].setFont(sharedVariables.myFont);

     if(sharedVariables.tabStuff[mainConsoleIndex].BackColor!=null)
    gameconsoles[gameData.BoardIndex].setBackground(sharedVariables.tabStuff[mainConsoleIndex].BackColor);
     else
     gameconsoles[gameData.BoardIndex].setBackground(sharedVariables.BackColor);

     if(sharedVariables.tabStuff[mainConsoleIndex].ForColor!=null)
     gameconsoles[gameData.BoardIndex].setForeground(sharedVariables.tabStuff[mainConsoleIndex].ForColor);
     else
     gameconsoles[gameData.BoardIndex].setForeground(sharedVariables.ForColor);
}catch(Exception dui2) { }
}


public void makerightclickmainhappen(MouseEvent e)
{
JPopupMenu menu2=new JPopupMenu("Popup2");
JMenuItem item1 = new JMenuItem("main");
 item1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           mainConsoleIndex=0;

setMainConsoleDoc();
sharedVariables.pointedToMain[gameData.BoardIndex]=true; // when you type our response text. what you type will go to main. LookingAt stays on game only console text is effected by sharedVariables.pointedToMain[gameData.BoardIndex]

            }
       });
       menu2.add(item1);
JMenuItem [] items = new JMenuItem[sharedVariables.maxConsoleTabs];

for(int b=1; b< sharedVariables.maxConsoleTabs; b++)
{
  final int a=b;
  String title = "" + a;
if(!sharedVariables.consoleTabCustomTitles[b].equals(""))
title = sharedVariables.consoleTabCustomTitles[b];
else if(!sharedVariables.consoleTabTitles[b].equals(""))
title = sharedVariables.consoleTabTitles[b];


items[a] = new JMenuItem(title);
 items[a].addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           mainConsoleIndex=a;

setMainConsoleDoc();
sharedVariables.pointedToMain[gameData.BoardIndex]=true; // when you type our response text. what you type will go to main. LookingAt stays on game only console text is effected by sharedVariables.pointedToMain[gameData.BoardIndex]

            }
       });
       menu2.add(items[a]);

}// end for

add(menu2);
menu2.show(e.getComponent(),e.getX(),e.getY());

} // end method
public void makerightclickhappen(MouseEvent e, final int n)
{
JPopupMenu menu2=new JPopupMenu("Popup2");
JMenuItem item1 = new JMenuItem("close game");
 item1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            myoutput data = new myoutput();
            data.closetab=n;
            queue.add(data);
            }
       });
       menu2.add(item1);

    JMenuItem saveitem = new JMenuItem("save to PGN");
    saveitem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {

                String myfile = gameFrame.getFile(new JFrame("framer"));
                if(myfile.equals(""))
                return;
                logPgn(sharedVariables.mygame[gameData.LookingAt].iccResultString, sharedVariables.mygame[gameData.LookingAt].iccResult, myfile);

            } catch(Exception dui) {

            }

        }
    });
    if(sharedVariables.mygame[gameData.LookingAt].state != -1 ||
    !sharedVariables.mygame[gameData.LookingAt].name1.equals("") ||
    !sharedVariables.mygame[gameData.LookingAt].name2.equals(""))
    {
       menu2.add(saveitem);
    }



JMenuItem item2 = new JMenuItem("clear tab chat");
 item2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            myoutput data = new myoutput();
            data.clearboard=n;
            queue.add(data);
            }
       });
       menu2.add(item2);

JMenuItem item22 = new JMenuItem("trim tab chat");
 item22.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            myoutput data = new myoutput();
            data.trimboard=n;
            queue.add(data);
            }
       });
       menu2.add(item22);





/*JMenuItem item3 = new JMenuItem("cancel");
menu2.add(item3);
*/
JMenuItem [] openItems = new JMenuItem[sharedVariables.openBoardCount];
for(int z=0; z < sharedVariables.openBoardCount; z++)
{

if(sharedVariables.mygame[z]!=null)
if(sharedVariables.mygame[z].myGameNumber > 0)
{
try {
  openItems[z] = new JMenuItem("" + sharedVariables.mygame[z].myGameNumber);
 final int num = z;
 openItems[z].addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            makehappen(num);
            }
       });
       menu2.add(openItems[z]);
 }
 catch(Exception dui){}
}


}



add(menu2);
menu2.show(e.getComponent(),e.getX(),e.getY());
}
    
    
    
    
    void logPgn(String iccresult, String iccresultstring, String fileName)
    {
        try {
            
            
            String game = "\r\n";
            /*
             [Event "ICC 5 0"]
             [Site "Internet Chess Club"]
             [Date "2011.10.09"]
             [Round "-"]
             [White "Mike"]
             [Black "Prophet-Daniel"]
             [Result "1-0"]
             [ICCResult "Black forfeits on time"]
             [WhiteElo "1046"]
             [BlackElo "943"]
             [Opening "Sicilian"]
             [ECO "B54"]
             [NIC "SI.01"]
             [Time "03:46:21"]
             [TimeControl "300+0"]
             */
            String date = "*";
            String theTime="*";
            try {
                
                Calendar Now=Calendar.getInstance();
                String hour= "" + Now.get(Now.HOUR_OF_DAY);// was HOUR for 12 hour time
                if(hour.equals("0"))
                    hour = "12";
                
                String minute="" + Now.get(Now.MINUTE);
                if(minute.length()==1)
                    minute="0"+ minute;
                
                String second="" + Now.get( Now.SECOND);
                if(second.length()==1)
                    second="0"+ second;
                
                theTime=hour + ":" + minute + ":" + second;
                
            }
            catch(Exception dumtime){}
            
            try {
                
                Calendar Now=Calendar.getInstance();
                // year.month.day
                String year = "" + Now.get(Now.YEAR);
                int m = Now.get(Now.MONTH) + 1;
                String month = "" + m;
                if(m < 10) {
                    month = "0" + month;
                }
                String day = "" + Now.get(Now.DAY_OF_MONTH);
                if(day.length() == 1)
                {
                    day = "0" + day;
                }
                date = year + "." + month + "." + day;
            }
            catch(Exception dumdate){}
            
            
            
            game += "[Event \"ICC " +  sharedVariables.mygame[gameData.LookingAt].time + " " + sharedVariables.mygame[gameData.LookingAt].inc + "\"]\r\n";
            game += "[Site \"Internet Chess Club\"]\r\n";
            game += "[Date \"" + date +  "\"]\r\n";
            game += "[Round \"-\"]\r\n";
            game += "[White \"" + sharedVariables.mygame[gameData.LookingAt].realname1 + "\"]\r\n";
            game += "[Black \"" + sharedVariables.mygame[gameData.LookingAt].realname2 + "\"]\r\n";
            if(iccresult.equals(""))
            {
               iccresult = "*";
            }
            game += "[Result \"" + iccresult + "\"]\r\n";
            if(!iccresultstring.equals(""))
            {
                game += "[ICCResult \"" + iccresultstring + "\"]\r\n";
            }
           if(!sharedVariables.mygame[gameData.BoardIndex].whiteRating.equals("0"))
           {
             game += "[WhiteElo \"" + sharedVariables.mygame[gameData.BoardIndex].whiteRating + "\"]\r\n";
           }
           if(!sharedVariables.mygame[gameData.BoardIndex].blackRating.equals("0"))
           {
              game += "[BlackElo \"" + sharedVariables.mygame[gameData.BoardIndex].blackRating + "\"]\r\n";
           }
            game += "[Opening \"*\"]\r\n";
            game += "[ECO \"*\"]\r\n";
            game += "[NIC \"*\"]\r\n";
            game += "[Time \"" + theTime + "\"]\r\n";
            int minutes = sharedVariables.mygame[gameData.LookingAt].time  * 60;
            game += "[TimeControl \"" +  minutes + "+" + sharedVariables.mygame[gameData.LookingAt].inc + "\"]\r\n";
            game += "\r\n";
            // now moves
            game += sharedVariables.mygametable[gameData.LookingAt].getMoves() + "\r\n";
            game += "{" + iccresultstring + "}\r\n";
            game += iccresult + "\r\n";
            FileWrite writer = new FileWrite();
            writer.writeAppend(game, fileName);
            
            
            
            
        }// end try
        catch(Exception logging){}
        
    }// end method log observed pgn
    
    
    
void adjustMoveList()
{
final int aaa = gameData.BoardIndex;
SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {

                             	int index = sharedVariables.moveSliders[gameData.BoardIndex].getValue();
                             	int row =(int) index/2;
                             	int column=0;
                             	if(index%2 == 1)
                             		row++;
                             	if(index%2 == 1)
                             		column = 1;
                             	else
                             		column = 2;
                             	if(index == 0)
                             		column=0;
                             	sharedVariables.gametable[aaa].scrollRectToVisible(sharedVariables.gametable[aaa].getCellRect(row, column, true));


                            } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });

			//readLock.lock();


		//readLock.unlock();

}// end method adjust move list
void setActiveTabForeground(int i)
{
	for(int a=0; a<sharedVariables.maxGameTabs; a++)
	if(a==i) // active tab
	{	channelTabs[a].setForeground(sharedVariables.activeTabForeground);
		channelTabs[a].setBackground(sharedVariables.tabImOnBackground);
	}
	else // every other  tab
		channelTabs[a].setForeground(sharedVariables.passiveTabForeground);


}
void dispatchCommand(String myurl)
{

	String mycommand="";
	mycommand=myurl; //.substring(1, myurl.length()-1);// need to figure out why this is -2 not -1, maybe i include the end space which adds a charaacter here when i cut it
	mycommand=mycommand + "\n";

	myoutput output = new myoutput();
      if(sharedVariables.myServer.equals("ICC") && sharedVariables.myname.length() > 0)
      {
			if(sharedVariables.pointedToMain[gameData.LookingAt] == false)
			    output.data="`g" + gameData.LookingAt + "`" + mycommand;
			else
				output.data="`c" + mainConsoleIndex + "`" + mycommand;

  	}
      else
      	output.data=mycommand;

	  output.consoleNumber=gameData.BoardIndex;
      queue.add(output);

	try {
		}
	catch(Exception E){ }

}
void removeSelectionHighlight()
{
//	consoles[consoleNumber].getHighlighter().removeHighlights(consoles[consoleNumber]);//remove highlight if they click
try {

gameconsoles[gameData.BoardIndex].getHighlighter().removeAllHighlights();
}
catch(Exception d){}
}

private void initComponents()
{
        String[] prefixStrings = { ">", "Kibitz", "Whisper", "Tell Opponent" };

        prefixHandler = new JComboBox(prefixStrings);
        prefixHandler.setSelectedIndex(0);
        prefixHandler.setEditable(false);

	comboMemory=new int[sharedVariables.maxGameTabs];
	for(int mem=0; mem<sharedVariables.maxGameTabs; mem++)
		comboMemory[mem]=0;

prefixHandler.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        try{

        String mytext =(String) cb.getSelectedItem();
        if(mytext != null)
       {
comboMemory[sharedVariables.Looking[gameData.BoardIndex]]=cb.getSelectedIndex();
		        if(mytext.equals(">") || Input.getText().startsWith("/"))
		        Input.setForeground(sharedVariables.inputCommandColor);
		        else
		        Input.setForeground(sharedVariables.inputChatColor);

		}
        giveFocus();
      //JFrame aframe = new JFrame();
      //aframe.setSize(200,200);
      //aframe.setTitle(comboMemory[sharedVariables.looking[consoleNumber]] + " " + sharedVariables.looking[consoleNumber] + " " + e.getActionCommand());
      //aframe.setVisible(true);
	}catch(Exception cant){}

    }

});

        Input = new JTextField();
        Input.setFont(sharedVariables.inputFont);
		mainConsoleTab = new JPaintedLabel("Main", sharedVariables);
		channelTabs = new JPaintedGameLabel[sharedVariables.maxGameTabs];
		for(int a=0; a< sharedVariables.maxGameTabs; a++)
		{channelTabs[a]=new JPaintedGameLabel(sharedVariables.tabTitle[a], sharedVariables);
		if(a <= gameData.BoardIndex)
		if(sharedVariables.mygame[a].tabtitle.length() > 0)
		channelTabs[a].setText(sharedVariables.mygame[a].tabtitle, a);
		//add(channelTabs[a]);
		}

		try{
			for(int a=sharedVariables.openBoardCount+1; a< sharedVariables.maxGameTabs; a++)
		channelTabs[a].setVisible(false);
		for(int a=1; a<sharedVariables.openBoardCount+1; a++)
			if(sharedVariables.tabTitle[a].startsWith("G"))
				channelTabs[a].setVisible(false);
	}catch(Exception invisibleerror){}

	   //tellLabel=new JLabel("tells");
	   //tellCheckbox=new JCheckBox();
		setActiveTabForeground(gameData.BoardIndex);


// listens for clicks on board, currently support 39 boards, thats how  many listeners we have
// each listener does any work when a tab is clicked in makehappen() method
mainConsoleTab.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
					

 			 if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2)
 			 makerightclickmainhappen(e);
 			 else
 			 {

                                        setMainConsoleDoc();
					sharedVariables.pointedToMain[gameData.BoardIndex]=true; // when you type our response text. what you type will go to main. LookingAt stays on game only console text is effected by sharedVariables.pointedToMain[gameData.BoardIndex]
                          }// end else
			 }
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });
for(int tabindex = 0; tabindex < 40; tabindex ++ )
{
final int tabnumber = tabindex;

channelTabs[tabnumber].addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
			 if (e.getButton() == MouseEvent.BUTTON3 || e.getClickCount() == 2)
			 makerightclickhappen(e, tabnumber);
			 else
			 makehappen(tabnumber);}
         public void mouseReleased(MouseEvent e) {}
         public void mouseEntered (MouseEvent me) {}
         public void mouseExited (MouseEvent me) {}
         public void mouseClicked (MouseEvent me) {}  });
}//end for

setupSmallMenu();

		// initialize the game console which is a text pane
       gameconsoles[gameData.BoardIndex] = new JTextPane();
	   // whenever we create a console we get its document and store it in a doc array
	   // telnet writes to documents and another board can be  looking at this  boards game and console and just needs its document which is now stored on the array
	   sharedVariables.mygamedocs[gameData.BoardIndex]=gameconsoles[gameData.BoardIndex].getStyledDocument();





gameconsoles[gameData.BoardIndex].setEditable(false); // cant edit or type in a game console
 gameconsoles[gameData.BoardIndex].addKeyListener(new KeyListener() {

		public void keyPressed(KeyEvent e)
		{
        int a=e.getKeyCode();
        if(a==33)
        {
			scrollnow=0;

		}
		}
 public void keyTyped(KeyEvent e) {;

    }



    /** Handle the key-released event from the text field.
    */
    public void keyReleased(KeyEvent e) {;

    }

	}
);


gameconsoles[gameData.BoardIndex].addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
         


if(e.getClickCount() == 1 && e.getButton() != MouseEvent.BUTTON3 )
removeSelectionHighlight();

            if (e.getButton() == MouseEvent.BUTTON3){

               if(gameconsoles[gameData.BoardIndex].getSelectedText().indexOf(" ") == -1)
               {
				  setupLargeMenu(gameconsoles[gameData.BoardIndex].getSelectedText());
				  menu3.show(e.getComponent(),e.getX(),e.getY());
		   		}
               else
               menu.show(e.getComponent(),e.getX(),e.getY());
            }

         }  });

gameconsoles[gameData.BoardIndex].addMouseListener(new MouseListener()
{
	public void mouseClicked(MouseEvent e)
	{  
          if(e.getButton() == MouseEvent.BUTTON3)
          return; // right click
          
          
          JTextPane editor = (JTextPane) e.getSource();
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
if(myurl2.startsWith("/"))
myurl2=myurl2.substring(1, myurl2.length());
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
else if(myurl2.startsWith("liblist"))
{
	dispatchCommand(myurl);
}
else
sharedVariables.openUrl(myurl);
}


	}
	}
	}// end click event



		 public void mousePressed(MouseEvent e) {}
		 public void mouseEntered (MouseEvent me) {}
		 public void mouseReleased (MouseEvent me) {}
		 public void mouseExited (MouseEvent me) {}


});

/************************** input key listener ***************************************/
// Input is the text box they type in below the board
// this listens for them hitting enter

Input.addKeyListener(new KeyListener() {


	public void keyPressed(KeyEvent e)
{
         String mytext =(String) prefixHandler.getSelectedItem();
        if(mytext != null)
        {

        if(mytext.equals(">") || Input.getText().startsWith("/"))
        Input.setForeground(sharedVariables.inputCommandColor);
        else
        Input.setForeground(sharedVariables.inputChatColor);
		}



       int aa=e.getKeyCode();
        if( e.getModifiersEx() == 512 || (sharedVariables.operatingSystem.equals("mac") && e.getModifiersEx() == 128)) // alt
       {
         if(aa == 39  || aa == 82) //  right arrow
          {
           makehappen(getNextGame(true));
           return;
          }
         if(aa == 37 || aa == 76) // left arrow
         {
           makehappen(getNextGame(false));
           return;
         }

        if(aa == 83)// s
        {
         myoutput output = new myoutput();
         output.gameConsoleSide =1; // function later togles it
         output.gameFocusConsole = gameData.BoardIndex;
          queue.add(output);
         return;

        }
        if(aa == 67)// c
        {
         myoutput output = new myoutput();
         output.gameConsoleSize =(sharedVariables.boardConsoleType + 1 )%4;
         if(output.gameConsoleSize == 0)
         output.gameConsoleSize = 1;
         output.gameFocusConsole = gameData.BoardIndex;
         queue.add(output);
         return;

        }

         if(aa == 88)
         {   myoutput data = new myoutput();
            data.closetab=getPhysicalTab(gameData.LookingAt);
            queue.add(data);
            return;
          }
       }
                   if( aa == 71 &&  e.getModifiersEx() == 128) // ctrl + g
                {
  			String myurl =Input.getText();
                        Input.setText("");

			myurl=myurl.trim();
                        myurl=myurl.replace(" ", "+");

			sharedVariables.openUrl("http://www.google.com/search?q=" + myurl);
                }
          if( aa == 72 &&  e.getModifiersEx() == 128 )
             {
              if(sharedVariables.myGameList != null)
              if(sharedVariables.myGameList.isVisible())
              {
               try { sharedVariables.myGameList.setSelected(true); } catch(Exception gamedui){}

              }
             }

       if( e.getModifiersEx() == 128 && aa == 90)// ctrl + t
	   {

	   		if(myself!=null)
	   			myself.switchConsoleWindows();

	   		return;
	    }

        if( e.getModifiersEx() == 128 && aa == 84)// ctrl + t
        {

		if(myself!=null)
		myself.switchWindows();
		else
		{

JFrame newframe = new JFrame();
newframe.setSize(100,100);
newframe.setTitle(" null");
newframe.setVisible(true);

		}

		}






if( e.getModifiersEx() == 128 || e.getModifiersEx() == 192) // 128 control 192 control + shift
{
int moveKeyType=e.getModifiersEx();
            if(aa == 61) // - 45 = 61
            {
              int games = gameData.LookingAt;
          if(games > -1)
           {
           int loc = sharedVariables.moveSliders[games].getValue();
              int max = sharedVariables.moveSliders[games].getMaximum();
              if (loc < max || moveKeyType == 192) {

                if(moveKeyType == 192)
                loc=max;
                else
                loc++;

                sharedVariables.moveSliders[games].setValue(loc);
                mycontrolspanel.adjustMoveList();
                mypanel.repaint();
              }
              giveFocus();

              }

              return;


            }


            if(aa == 45) // - 45 = 61
            {
            int games = gameData.LookingAt;
           if(games > -1)
           {
             int loc = sharedVariables.moveSliders[games].getValue();

              if (loc > 0 || moveKeyType == 192) {

                if(moveKeyType == 192)
                loc=0;
                else
                loc--;
                sharedVariables.moveSliders[games].setValue(loc);
                 mycontrolspanel.adjustMoveList();
                mypanel.repaint();
              }
              giveFocus();

              }

              return;
            }
        }// if control or control + shift

          if( e.getModifiersEx() == 128  && aa == 76)// control 0
          {
                sharedVariables.consoleDebug = !sharedVariables.consoleDebug;
             /*   JFrame framer;
          if(sharedVariables.consoleDebug == true)
          framer = new JFrame("" + 1);
           else
           framer = new JFrame("" + 0);
           framer.setSize(100,100);
           framer.setVisible(true);
          */
          }

        if( e.getModifiersEx() == 128 )// ctrl + t
        {
             if(aa == 70)// F
            {

             textSearcher ts = new textSearcher();
             ts.find(Input.getText(), gameconsoles[gameData.BoardIndex]);
            }


			if(aa == 49)
			{
				doToolBarCommand(1);
				return;
			}
			if(aa == 50)
			{
				doToolBarCommand(2);
				return;
			}
			if(aa == 51)
			{
				doToolBarCommand(3);
				return;
			}
			if(aa == 52)
			{
				doToolBarCommand(4);
				return;
			}
			if(aa == 53)
			{
				doToolBarCommand(5);
				return;
			}
			if(aa == 54)
			{
				doToolBarCommand(6);
				return;
			}
			if(aa == 55)
			{
				doToolBarCommand(7);
				return;
			}
			if(aa == 56)
			{
				doToolBarCommand(8);
				return;
			}
			if(aa == 57)
			{
				doToolBarCommand(9);
				return;
			}
			if(aa == 48)
			{
				doToolBarCommand(0);
				return;
			}
                        if(aa == 79) // 'o' ctrl + 0 -- debug info
                        {
                         String mess = "Open Board Count is: " + sharedVariables.openBoardCount + " ";
                         int index = 0;
                         for(index = 0; index < sharedVariables.openBoardCount; index++)
                         {
                           try {
                               mess = mess + "Tab " + index + " Pointing to " + sharedVariables.tabLooking[index] + " ";
                           }
                           catch(Exception dui){

                           mess+=" Exception finding tab were pointing to on index " + index + " ";  }// end catch

                           } // end for
                           
                           mess+="\n";
                           writeTypedText(mess);
                           return;




                        }

		}



      if( e.getModifiersEx() == 512 && sharedVariables.pointedToMain[gameData.BoardIndex] == true)// alt
      {

                 if(aa == 49  )
		 {

			  mainConsoleIndex=0;
			  setMainConsoleDoc();
			  return;
		 }
		 if(aa == 50)
		 {


			mainConsoleIndex=1;
			setMainConsoleDoc();
			return;
		}
		 if(aa == 51  )
		 {

			 mainConsoleIndex=2;
			 setMainConsoleDoc();
			 return;
		 }
		 if(aa == 52)
		 {


			mainConsoleIndex=3;
			setMainConsoleDoc();
			return;
		 }
		 if(aa == 53  )
		 {

			 mainConsoleIndex=4;
			 setMainConsoleDoc();
			 return;
		 }
		 if(aa == 54)
		 {

		mainConsoleIndex=5;
		setMainConsoleDoc();
		return;
		 }
		 if(aa == 55  )
		 {
			mainConsoleIndex=6;
			setMainConsoleDoc();
			return;

		 }
		 if(aa == 56)
		 {
			 mainConsoleIndex=7;
			 setMainConsoleDoc();
			 return;

		 }
		 if(aa == 57  )
		 {
			mainConsoleIndex=8;
			setMainConsoleDoc();
			return;
		}
		if(aa == 48)
		{
			mainConsoleIndex=9;
			setMainConsoleDoc();
			return;
		}

		 if(aa == 45  )
		 {

			 mainConsoleIndex=10;
			 setMainConsoleDoc();
			 return;
		}
		if(aa == 61)
		{

		mainConsoleIndex=11;
		setMainConsoleDoc();
		return;
		}

                }// if alt and pointed to main






          if(aa == 27) // esc
         {
			 Input.setText("");
		 }

        if(aa == 10)
 {   lastcommand=Input.getText();
 arrowManager.add(lastcommand);
	 String mes = lastcommand + "\n";


	int index = prefixHandler.getSelectedIndex();
	String pre="";
	String primary  = "primary " + sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber + "\n";
	if(sharedVariables.isGuest()) {
          primary = "";
        }
	if(sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].state == sharedVariables.STATE_OVER)
		primary = "";

	//if(index == 0)
	//sendToEngine(mes);
	if(!Input.getText().startsWith("/"))
	{
	// dont put any prefix commands in if they escape with /
	if(index == 1)
	{
		if(sharedVariables.myServer.equals("ICC"))
		pre=pre + "kibitzto ";
		else
		pre=pre + "xkib ";

		if(sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber != -1)
			pre = pre + sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber + " ";
		else
		pre = "kibitz ";

		mes = pre + mes;

	}

		if(index == 2)
		{
			if(sharedVariables.myServer.equals("ICC"))
			pre=pre + "whisperto ";
			else
			pre=pre + "xwhisper ";

			if(sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber != -1)
			pre = pre + sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber + " ";
			else
			pre = "whisper ";

			mes = pre + mes;

		}
		if(index == 3)
		{
			pre = "say ";
			mes = pre + mes;
		}
}// end if not "/"
      Input.setText("");

         writeTypedText(mes);
                     try {
					// we make this game number primary
					myoutput output;
					if(sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber != -1 && sharedVariables.myServer.equals("ICC"))// primary doesnt work on fics when playing so for now we dont send it there MA 5-31-10
					{
							  output = new myoutput();
							  output.data=primary;
						// code here to prefix our command with `g#`command if on icc and we have a name defined i.e. recieved whoami
						 // this will get any text back from server to go to right console
						 /*if(sharedVariables.myServer.equals("ICC") && sharedVariables.myname.length() > 0)
						 {
							 if(sharedVariables.pointedToMain[gameData.LookingAt] == false)
						 		output.data="`g" + gameData.LookingAt + "`" + primary;
						 	 else
						 		output.data="`c0" + "`" + primary;
					   	 }*/

							  output.consoleNumber=0;
							  output.game=1;
							  queue.add(output);
					}
					// we send message
					          output = new myoutput();
						      output.data=mes;
						// code here to prefix our command with `g#`command if on icc and we have a name defined i.e. recieved whoami
						 // this will get any text back from server to go to right console
						 if(sharedVariables.myServer.equals("ICC") && sharedVariables.myname.length() > 0)
						 {
							 if(sharedVariables.pointedToMain[gameData.LookingAt] == false)
						 		output.data="`g" + gameData.LookingAt + "`" + mes;
						 	 else
						 		output.data="`c" + mainConsoleIndex + "`" + mes;
					   	 }
						      output.consoleNumber=0;
						      output.game=1;
					          if(sharedVariables.pointedToMain[gameData.LookingAt] == true)
					          	output.game=0;

					          queue.add(output);

				}
				catch(Exception E){ }
        }
        if((aa == 120 || aa == 119) && e.getModifiersEx() != 64)// f9
        {
               String s=Input.getText();
                String person;
                if(s.length() == 0)
                	person = sharedVariables.F9Manager.getName(true);
                else
                	person = sharedVariables.F9Manager.getName(false);

                if(person.length()>0)
                {
					Input.setText("/Tell " + person + " ");
					Input.setForeground(sharedVariables.inputCommandColor);
				}
        }
        if((aa == 120 || aa == 119) && e.getModifiersEx() == 64)// shift f9
        {
               String s=Input.getText();
                String person;
                if(s.length() == 0)
                	person = sharedVariables.F9Manager.getNameReverse(true);
                else
                	person = sharedVariables.F9Manager.getNameReverse(false);

                if(person.length()>0)
                {
					Input.setText("/Tell " + person + " ");
					Input.setForeground(sharedVariables.inputCommandColor);
				}
        }


        if(aa == 40)// down
        {
          arrowManager.down();
        }
        if( e.getModifiersEx() == 128  && aa == 38 && !Input.getText().equals(""))
        {
          arrowManager.add(Input.getText());
          Input.setText("");
        }
        else if(aa == 38)// up
        {
                 arrowManager.up();

               // if(lastcommand.length() >0)
               // Input.setText(lastcommand);
        }


    }



 	void writeToConsole(StyledDocument doc, int i)
 	{
 		for(int a=0; a< sharedVariables.openConsoleCount; a++)
 		 {
 			 if(consoleSubframes[a] != null)
 		     {
 			if(consoleSubframes[a].isVisible())
 		     {
 				 if(sharedVariables.looking[a]==i)
 			 	{
 					try {
 					consoles[a].setStyledDocument(doc);

 					/*if(false == sharedVariables.ConsoleScrollPane[a].getVerticalScrollBar().getValueIsAdjusting())
 					{

 					// lets check if the scroll bar is raised up passeed a bottom window that we'll auto max it if its in
 					if(sharedVariables.ConsoleScrollPane[a].getVerticalScrollBar().getValue() + 500 > sharedVariables.ConsoleScrollPane[a].getVerticalScrollBar().getMaximum())// scrolled more than 10 lines up dont auto scroll
 					{

 						sharedVariables.ConsoleScrollPane[a].getVerticalScrollBar().setValue(sharedVariables.ConsoleScrollPane[a].getVerticalScrollBar().getMaximum());// scrolled mor

 					}// end if
 					}// end if
 					*/



 					}catch(Exception e){ }
 				}
 			 	else
 			 	{
 					consoleSubframes[a].channelTabs[i].setBackground(sharedVariables.newInfoTabBackground);

 				}
 			  }
 		  }// end outer if
 	  }// end for
	}


void doToolBarCommand(int n)
{
					toolbarCommands commander = new toolbarCommands();
				commander.dispatchCommand(n, sharedVariables.gamelooking[gameData.BoardIndex], true, sharedVariables,  queue);

}





 public void keyTyped(KeyEvent e) {;

    }




    public void keyReleased(KeyEvent e) {;

    }




}

);


/************************* end input key listener ********************************/






/**************************** a scroll pane and adjustment listener ******************/
// we want new text to scroll to the bottom. i.e. text comes in console scrolls
// there is code here also to turn off scroll to bottom if they scroll up a little bit, beyond a predetermined bottom area  that if they are in it scrolls
//  or they are dragging the scroll bar

jScrollPane1 = new JScrollPane(gameconsoles[gameData.BoardIndex]);
jScrollPane1.getVerticalScrollBar().setUnitIncrement(blockInc);
//jScrollPane1.getVerticalScrollBar().setBlockIncrement(300);


jScrollPane1.addMouseWheelListener(new MouseWheelListener()
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
               {		jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getValue() - (e.getScrollAmount() * mult));
		   	   			scrollnow=0;
   						wheelIsScrolling=true;

		   		}
               else
               	{

							int d =(e.getScrollAmount() * mult);
							int myvalue =100 + d;
							if(jScrollPane1.getVerticalScrollBar().getValue() + myvalue > jScrollPane1.getVerticalScrollBar().getMaximum())

							{wheelIsScrolling = false;
							scrollnow=1;
				   			jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());

							}
							else
							{
				   			scrollnow=0;
				   			wheelIsScrolling=false;
				   			jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getValue() + (e.getScrollAmount() * mult));

							}
				}

 } else { //scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL
          /* message += "    Scroll type: WHEEL_BLOCK_SCROLL" + newline;
           message += "    Vertical block increment: "
               + jScrollPane1.getVerticalScrollBar().getBlockIncrement(1)
               + " pixels" + newline;
       */

          			scrollnow=0;
	      			wheelIsScrolling=true;

       					int block=jScrollPane1.getVerticalScrollBar().getBlockIncrement(1) * mult;
                      if (notches < 0)
	                  {
						  jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getValue() - block);

				  		}
	                  else
	                  {

						  int myvalue =100 + block;
							if(jScrollPane1.getVerticalScrollBar().getValue() + myvalue > jScrollPane1.getVerticalScrollBar().getMaximum())

							{wheelIsScrolling = false;
						  	scrollnow=1;
						}

						  jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getValue() + block);

					}
       }

	}
});


jScrollPane1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
		{
public void adjustmentValueChanged(AdjustmentEvent e)
	{

 		if(wheelIsScrolling)
		{
			wheelIsScrolling=false;
			return;

		}


                 // below code tells if they click the up line button and i've over riden its value in blockInc and when adjustment =block inc i dont scroll
                int z = jScrollPane1.getVerticalScrollBar().getValue() - jScrollPane1.getVerticalScrollBar().getMaximum();


                 int dd =gameconsoles[gameData.BoardIndex].getScrollableBlockIncrement(gameconsoles[gameData.BoardIndex].getVisibleRect(), SwingConstants.VERTICAL, -1 );
                 z=z+dd;
                 z*=-1;

                 if(z==blockInc)
               {

                    // JFrame fri = new JFrame("z is " + z + " and dd is " + dd);
		//	fri.setVisible(true);
             scrollnow =0;
             wheelIsScrolling=true;
                return;
               }





	if(scrollnow == 1 && false == jScrollPane1.getVerticalScrollBar().getValueIsAdjusting())
	{
e.getAdjustable().setValue(e.getAdjustable().getMaximum());



	}// end if not adjusting
	else
	{
		if(true == jScrollPane1.getVerticalScrollBar().getValueIsAdjusting())
		scrollnow = 0;

			int d =gameconsoles[gameData.BoardIndex].getScrollableBlockIncrement(gameconsoles[gameData.BoardIndex].getVisibleRect(), SwingConstants.VERTICAL, -1 );

int myvalue =40 + d;
	try{
		if(jScrollPane1.getVerticalScrollBar().getValue() + myvalue > jScrollPane1.getVerticalScrollBar().getMaximum())
		scrollnow = 1;

				if(scrollnow == 1 && jScrollPane1.getVerticalScrollBar().getValueIsAdjusting() == false)
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());

	}// end try
	catch(Exception e1)
	{}
	}// end else
	}// end  is adjustment value changed		}
}// end class
);

/******************************* End scroll pane and adjustment listener ****************/






// we initialize the foreground, background and font of this boards game console
// this boards game console always specified by the  array index BoardIndex

gameconsoles[gameData.BoardIndex].setForeground(sharedVariables.ForColor);
gameconsoles[gameData.BoardIndex].setBackground(sharedVariables.BackColor);
if(sharedVariables.myFont != null)
	gameconsoles[gameData.BoardIndex].setFont(sharedVariables.myFont);








	//pack(); // had to comment out pack wheni moved to sub class dont  know  why

/******************* End of creating our group layout for this panel ******************/


   Color lc=new Color(0,0,0);



	for(int a=0; a< sharedVariables.maxGameTabs; a++)
		channelTabs[a].setOpaque(true);

	for(int a=0; a< sharedVariables.maxGameTabs; a++)
		channelTabs[a].setBackground(sharedVariables.tabBackground);
if(sharedVariables.sideways==false)
	setHorizontalLayout();
else
	setVerticalLayout();


    } // end initialize components


  int getNextGame(boolean right)
    {
     try {
     int i = 0;
     for(int a=0; a<sharedVariables.openBoardCount; a++)
     if(sharedVariables.tabLooking[a] == gameData.LookingAt)
     {
      i=a;
      break;
     }


     if(right == true)
     {
      if(i+1 < sharedVariables.openBoardCount && sharedVariables.tabLooking[i+1] >=0)
      return i+1;

      return 0;
     }
     else
     {
        if(i-1 >= 0 && sharedVariables.tabLooking[i-1] >=0)
        return i-1;

        return sharedVariables.openBoardCount -1;

     }


     }
     catch(Exception dui){return gameData.LookingAt;}
    }



 void writeTypedText(String mes)
 {
   
 	  			try {
				StyledDocument doc;

				if(sharedVariables.pointedToMain[gameData.BoardIndex] == false)
					doc=sharedVariables.mygamedocs[gameData.LookingAt];
				else
					doc=sharedVariables.mydocs[mainConsoleIndex]; // LookingAt always stays on game even if they click main tab so we use this variable , pointedatmain to see if we really want main document

	  			//doc.insertString(doc.getLength(), mes, null);



					/*	if(sharedVariables.pointedToMain[gameData.BoardIndex] == false)
						{
							for(int a=0; a<sharedVariables.maxGameTabs && a < sharedVariables.openBoardCount; a++)
							if(sharedVariables.gamelooking[gameData.BoardIndex]==sharedVariables.gamelooking[a])
					*/
					if(sharedVariables.pointedToMain[gameData.LookingAt] ==false)
							{


						SimpleAttributeSet attrs = new SimpleAttributeSet();
						
     	                                          if(sharedVariables.typedStyle == 1 || sharedVariables.typedStyle == 3)
		                                     StyleConstants.setItalic(attrs, true);
                                               	if(sharedVariables.typedStyle == 2 || sharedVariables.typedStyle == 3)
	                                        	StyleConstants.setBold(attrs, true);


						StyleConstants.setForeground(attrs, sharedVariables.typedColor);
					int GAME_CONSOLES=1;// game
					int maxLinks =75;
					myDocWriter.processLink(doc, mes, sharedVariables.typedColor, gameData.LookingAt, maxLinks, GAME_CONSOLES, attrs, null);


								//gameconsoles[a].setStyledDocument(doc);


						//	}
						}
						else
						{
							//we need to update every subframe console nad game console pointed to main


							//writeToConsole(doc, 0); // we need to move this function for general use out of chessbot 4 and out of this file to one place for general use
							//for(int a=0; a<sharedVariables.maxGameTabs && a < sharedVariables.openBoardCount; a++)
							/*if(sharedVariables.pointedToMain[a] ==true)
								//gameconsoles[a].setStyledDocument(doc);
							{*/

						SimpleAttributeSet attrs = new SimpleAttributeSet();
						StyleConstants.setItalic(attrs, true);
						StyleConstants.setForeground(attrs, sharedVariables.typedColor);
					int SUBFRAME_CONSOLES=0;// game
					int maxLinks =75;
					myDocWriter.processLink(doc, mes, sharedVariables.typedColor, /*gameData.LookingAt*/ mainConsoleIndex, maxLinks, SUBFRAME_CONSOLES, attrs, null);


							//}
						}
	}
        catch(Exception dui){}


 }
void setVerticalLayout()
{
	/****************************** Layout of game console area is handled ****************/
	        GroupLayout layout = new GroupLayout(this);
	        setLayout(layout);

		//Create a parallel group for the horizontal axis
		ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

	ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

		SequentialGroup [] middlehor = new SequentialGroup[5];
               int bb=0;
                int cc=0;

                 for(cc=0; cc<5; cc++)
                middlehor[cc] = layout.createSequentialGroup();

                  ParallelGroup [] middles = new ParallelGroup[5];
                for(bb=0; bb<5; bb++)
                middles[bb] = layout.createParallelGroup(GroupLayout.Alignment.LEADING);


		SequentialGroup h2 = layout.createSequentialGroup();

		SequentialGroup h3 = layout.createSequentialGroup();
		//Add a scroll pane and a label to the parallel group h2

	h3.addComponent(prefixHandler, GroupLayout.DEFAULT_SIZE, 40 , 60);
		h3.addComponent(Input);


			middlehor[0].addComponent(mainConsoleTab, 40,40,40);


			for(int a=0; a<4; a++)
			{

				try {
					middlehor[0].addComponent(channelTabs[a], 45,45,45);
						middlehor[0].addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
			}catch(Exception e) {}
			}
			for(int a=4; a<10; a++)
			{

				try {
					middlehor[1].addComponent(channelTabs[a], 42,42,42);
						middlehor[1].addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
			}catch(Exception e) {}
			}
			for(int a=10; a<16; a++)
			{

				try {
					middlehor[2].addComponent(channelTabs[a], 42,42,42);
						middlehor[2].addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
			}catch(Exception e) {}
			}
			for(int a=16; a<23; a++)
			{

				try {
					middlehor[3].addComponent(channelTabs[a], 30,30,30);
						middlehor[3].addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
			}catch(Exception e) {}
			}
			for(int a=23; a<30; a++)
			{

				try {
					middlehor[4].addComponent(channelTabs[a], 30,30,30);
						middlehor[4].addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
			}catch(Exception e) {}
			}




                  middles[0].addGroup(middlehor[0]);
                  middles[1].addGroup(middlehor[1]);
                  middles[2].addGroup(middlehor[2]);
                  middles[3].addGroup(middlehor[3]);
                  middles[4].addGroup(middlehor[4]);




	//	h2.addGroup(middle);
		h2.addComponent(jScrollPane1);

	h1.addGroup(middles[0]);
	h1.addGroup(middles[1]);
	h1.addGroup(middles[2]);
	h1.addGroup(middles[3]);
	h1.addGroup(middles[4]);

        h1.addGroup(h2);

	h1.addGroup(h3);

		hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
		//Create the horizontal group
		layout.setHorizontalGroup(hGroup);


		//Create a parallel group for the vertical axis
		ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading

	SequentialGroup v4 = layout.createSequentialGroup();

	ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);


		ParallelGroup [] vmiddlehor = new ParallelGroup[5];

                 for(cc=0; cc<5; cc++)
                vmiddlehor[cc] = layout.createParallelGroup();
  		SequentialGroup [] vmiddles = new SequentialGroup[5];
                for(bb=0; bb<5; bb++)
                vmiddles[bb] = layout.createSequentialGroup();


		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);


		v2.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);

			vmiddlehor[0].addComponent(mainConsoleTab);


			for(int a=0; a<4; a++)
			{

				try {
					vmiddlehor[0].addComponent(channelTabs[a]);

			}catch(Exception e) {}
			}
			for(int a=4; a<10; a++)
			{

				try {
					vmiddlehor[1].addComponent(channelTabs[a]);

			}catch(Exception e) {}
			}
			for(int a=10; a<16; a++)
			{

				try {
					vmiddlehor[2].addComponent(channelTabs[a]);

			}catch(Exception e) {}
			}
			for(int a=16; a<23; a++)
			{

				try {
					vmiddlehor[3].addComponent(channelTabs[a]);

			}catch(Exception e) {}
			}
			for(int a=23; a<30; a++)
			{

				try {
					vmiddlehor[4].addComponent(channelTabs[a]);

			}catch(Exception e) {}
			}


                  vmiddles[0].addGroup(vmiddlehor[0]);
                  	vmiddles[0].addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
                  vmiddles[1].addGroup(vmiddlehor[1]);
                  	vmiddles[1].addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
                   vmiddles[2].addGroup(vmiddlehor[2]);
                   	vmiddles[2].addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
                   vmiddles[3].addGroup(vmiddlehor[3]);
                  	vmiddles[3].addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
                   vmiddles[4].addGroup(vmiddlehor[4]);


			v1.addComponent(prefixHandler,30,30,30);

			v1.addComponent(Input,30,30,30);
	ParallelGroup bulk = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	bulk.addGroup(v2);

         v4.addGroup(vmiddles[0]);
         v4.addGroup(vmiddles[1]);
         v4.addGroup(vmiddles[2]);
          v4.addGroup(vmiddles[3]);
         v4.addGroup(vmiddles[4]);

  	v4.addGroup(bulk);
	v4.addGroup(v1);

		vGroup.addGroup(v4);
		//Create the vertical group
		layout.setVerticalGroup(vGroup);


}

void setHorizontalLayout()
{
/****************************** Layout of game console area is handled ****************/
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

	//Create a parallel group for the horizontal axis
	ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	//SequentialGroup h1 = layout.createSequentialGroup();
ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

	SequentialGroup middle = layout.createSequentialGroup();
	//ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	SequentialGroup h2 = layout.createSequentialGroup();

	SequentialGroup h3 = layout.createSequentialGroup();

	//Add a scroll pane and a label to the parallel group h2
	h2.addComponent(jScrollPane1);
	//h2.addComponent(status, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);// mike commented out

	//Create a sequential group h3

	//h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
	h3.addComponent(prefixHandler, GroupLayout.DEFAULT_SIZE, 40 , 60);
	h3.addComponent(Input);


		middle.addComponent(mainConsoleTab);
		middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);


		for(int a=0; a<sharedVariables.maxGameTabs; a++)
		{


			try {
				middle.addComponent(channelTabs[a], 25, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE);
			middle.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		}catch(Exception e) {}

		}

h1.addGroup(h2);
h1.addGroup(middle);
h1.addGroup(h3);

	hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
	//Create the horizontal group
	layout.setHorizontalGroup(hGroup);


	//Create a parallel group for the vertical axis
	ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);// was leading

SequentialGroup v4 = layout.createSequentialGroup();

ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

	ParallelGroup vmiddle = layout.createParallelGroup(GroupLayout.Alignment.LEADING);


	ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);


	v2.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);

		for(int a=0; a< sharedVariables.maxGameTabs; a++)
			vmiddle.addComponent(channelTabs[a]);

	vmiddle.addComponent(mainConsoleTab);

		v1.addComponent(prefixHandler);

		v1.addComponent(Input);


v4.addGroup(v2);
v4.addGroup(vmiddle);
v4.addGroup(v1);

	vGroup.addGroup(v4);
	//Create the vertical group
	layout.setVerticalGroup(vGroup);



}



void setupLargeMenu(final String handle)
{

menu3=new JPopupMenu("Popup");
JMenuItem [] items;
if(sharedVariables.rightClickMenu.size() > 0)
{

items = new JMenuItem[sharedVariables.rightClickMenu.size()-1];

for(int m=0; m< sharedVariables.rightClickMenu.size()-1; m++) // size()-1 caue last item is just used in console now the Tell name
{

	final int mfinal=m;
items[m] = new JMenuItem("" + sharedVariables.rightClickMenu.get(m) + " " + handle);
items[m].addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
          String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
 if( sharedVariables.rightClickMenu.get(mfinal).equals("Hyperlink"))
         {
			sharedVariables.openUrl(name);
       }
                    else if (sharedVariables.rightClickMenu.get(mfinal).equals("Lookup")) {

                 doCommand("`f1`Finger" + " " + name + "\n");
              }
       else if (sharedVariables.rightClickMenu.get(mfinal).equals("Challenge")) {
               JFrame framer =  new JFrame();
                sharedVariables.challengeCreator(name, framer, queue);

              }
else if (sharedVariables.rightClickMenu.get(mfinal).equals("Quarantine")) {
               JFrame tripper = new JFrame();
               boolean soundof = true;
                boolean channelof = false;
               String theName =  handle;

                for (int i=0; i<sharedVariables.toldTabNames.size(); i++) {
                  if (sharedVariables.toldTabNames.get(i).name.toLowerCase().equals(handle.toLowerCase()))
                  { 
                    soundof = sharedVariables.toldTabNames.get(i).sound;
                    channelof = sharedVariables.toldTabNames.get(i).blockChannels;
                    theName = sharedVariables.toldTabNames.get(i).name;
                  }
                }

                tellMasterDialog frame =
                  new tellMasterDialog(tripper, false,
                                       sharedVariables, theName, soundof, channelof);


              }   else if( sharedVariables.rightClickMenu.get(mfinal).equals("Google"))
   {
			sharedVariables.openUrl("http://www.google.com//search?q=" + name);
   }

 else
 doCommand( sharedVariables.rightClickMenu.get(mfinal) + " " + name + "\n");
          }
      });
      menu3.add(items[m]);
      if(m == 3  || m == 10 || m == 14)
      menu3.addSeparator();

      if(m < sharedVariables.rightClickMenu.size())
      {String menuEntry = sharedVariables.rightClickMenu.get(m);
      if(menuEntry.equals("Stored"))// now add edit list sub menu
      {
       JMenu LMenu = new JMenu("Edit List");
       sharedVariables.setUpListMenu(LMenu, handle, queue, "`g" + gameData.LookingAt +  "`");
       menu3.addSeparator();
       menu3.add(LMenu);
       menu3.addSeparator();
      }
      }
}// end for
}// end if any items
/*
JMenuItem item = new JMenuItem("Finger");
item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
          String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
          doCommand("finger " + name + "\n");
          }
      });
      menu3.add(item);



JMenuItem item2 = new JMenuItem("Vars");
item2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
             doCommand("Vars " + name + "\n");
             }
      });
      menu3.add(item2);
JMenuItem item3 = new JMenuItem("Ping");
item3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
         doCommand("ping " + name + "\n");
           }
      });
      menu3.add(item3);
      add(menu3);

menu3.addSeparator();

JMenuItem item4 = new JMenuItem("Match");
item4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
          String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
          doCommand("match " + name + "\n");
          }
      });
      menu3.add(item4);



JMenuItem item5 = new JMenuItem("Assess");
item5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
             doCommand("assess " + name + "\n");
             }
      });
      menu3.add(item5);

JMenuItem item6 = new JMenuItem("Pstat");
item6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
         doCommand("pstat " + name + "\n");
           }
      });
      menu3.add(item6);


menu3.addSeparator();


 JMenuItem item7 = new JMenuItem("Observe");
 item7.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
           doCommand("observe " + name + "\n");
           }
       });
       menu3.add(item7);



 JMenuItem item8 = new JMenuItem("Follow");
 item8.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
              doCommand("follow " + name + "\n");
              }
       });
       menu3.add(item8);
menu3.addSeparator();

 JMenuItem item9 = new JMenuItem("History");
 item9.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
          doCommand("history " + name + "\n");
            }
       });
       menu3.add(item9);



 JMenuItem item10 = new JMenuItem("Liblist");
 item10.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
           doCommand("liblist " + name + "\n");
           }
       });
       menu3.add(item10);



 JMenuItem item11 = new JMenuItem("Stored");
 item11.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              String name =  gameconsoles[gameData.BoardIndex].getSelectedText();
              doCommand("stored " + name + "\n");
              }
       });
       menu3.add(item11);

  */


menu3.addSeparator();
 JMenuItem item12 = new JMenuItem("Copy");
 item12.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          gameconsoles[gameData.BoardIndex].copy();
			giveFocus();
            }
       });
       menu3.add(item12);

 JMenuItem item13 = new JMenuItem("Copy&Paste");
  item13.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
           gameconsoles[gameData.BoardIndex].copy();
           Input.paste();
           giveFocus();

             }
        });
        menu3.add(item13);





 add(menu3);


  }

  void setupSmallMenu()
 {

 menu=new JPopupMenu("Popup");
JMenuItem item = new JMenuItem("Copy");
item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            gameconsoles[gameData.BoardIndex].copy();
            giveFocus();}
      });
      menu.add(item);



JMenuItem item2 = new JMenuItem("Copy&Paste");
item2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            gameconsoles[gameData.BoardIndex].copy();
            Input.paste();
            giveFocus();}
      });
      menu.add(item2);
 JMenuItem item3 = new JMenuItem("Google");
item3.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try
                {


			String myurl ="";
			myurl=gameconsoles[gameData.BoardIndex].getSelectedText();
			myurl=myurl.trim();
			myurl=myurl.replace(" ", "+");


			sharedVariables.openUrl("http://www.google.com/search?q=" + myurl);

			giveFocus();
             }catch(Exception g)
                {}
           }}
           );
      menu.add(item3);


      add(menu);

}// end menu setup


void giveFocus()
{
 SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                              //  JComponent comp = DataViewer.getSubcomponentByName(e.getInternalFrame(),
                                //SearchModel.SEARCHTEXT);

                             Input.setFocusable(true);
                               Input.setRequestFocusEnabled(true);
                                //Input.requestFocus();
                           Input.requestFocusInWindow();
                           if(sharedVariables.operatingSystem.equals("mac"))
                           { Input.setCaretPosition(Input.getDocument().getLength() - 1); }

                            
                            } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });

}
int getPhysicalTab(int look)
{
 for(int a=0; a<sharedVariables.openBoardCount; a++)
 if(sharedVariables.tabLooking[a]==look)
 return a;
 
 return look;


}

void doCommand(String mycommand)
{
	myoutput output = new myoutput();
   if(mycommand.startsWith("`"))
    output.data = mycommand;
    else
	output.data="`g" + gameData.LookingAt +  "`" + mycommand;
	//output.data=mycommand;
	output.game=1;
	output.consoleNumber=0;
    queue.add(output);
    giveFocus();
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

/*
void sharedVariables.openUrl(String myurl)
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






	             // rt.exec( "open " + myurl);

			//String[] commandLine = { "safari", "http://www.javaworld.com/" };
			//  Process process = Runtime.getRuntime().exec(commandLine);


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
*/

}// end gameconsole class
