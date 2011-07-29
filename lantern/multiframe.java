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
import java.awt.Window.*;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.ArrayList;
import java.util.StringTokenizer;

//import javax.jnlp.*;


//public class multiframe  extends JApplet
public class multiframe
{

public static void main(String[] args)
{
//public void init()

//{
	try {

String os = System.getProperty("os.name").toLowerCase();
if (os.indexOf( "win" ) >= 0)
	UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
	// UIManager.setLookAndFeel( "com.sun.java.swing.plaf.motif.MotifLookAndFeel");

else
UIManager.setLookAndFeel( "com.sun.java.swing.plaf.motif.SystemLookAndFeel");

}
catch(Exception d){}


final mymultiframe frame = new mymultiframe();
frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//DO_NOTHING_ON_CLOSE



frame.setTitle("Lantern Chess");
frame.setVisible(true);
//frame.setDefaultLookAndFeelDecorated(false);

SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                             frame.repaintTabs();
                            } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });



try { frame.consoleSubframes[0].setSelected(true);} catch(Exception dd){}

    // warning dialouge

String swarning="This is a beta version of Mike's new Interface.  Game play is possible but it's highly recommended you play unrated.  I want more testing before rated play can happen.  Not all wilds are supported.";

//Popup pframe = new Popup((JFrame) frame, true, swarning);
//pframe.setVisible(true);

try {


	frame.myConnection = new connectionDialog(frame, frame.sharedVariables, frame.queue, false);
	frame.myConnection.setVisible(true);

}catch(Exception bfocus){}

}




}// end main class

class mymultiframe extends JFrame implements ActionListener, ChangeListener, WindowListener
                                            //WindowFocusListener,
                                           // WindowStateListene
{

connectionDialog myConnection;
seekGraphFrame seekGraph;
JToolBar toolBar;
docWriter myDocWriter;
listFrame myfirstlist;
gameFrame myGameList;
listClass eventsList;
listClass seeksList;
listClass computerSeeksList;

saveScores mineScores;

listClass notifyList;
tableClass gameList;
webframe mywebframe;
 channels sharedVariables;
 private JTextPane [] consoles;
 private JTextPane [] gameconsoles;
 protected JColorChooser tcc;
 int colortype;
 JCheckBoxMenuItem autonoidle;
 JCheckBoxMenuItem autobufferchat;
  JCheckBoxMenuItem autoHistoryPopup;
 JCheckBoxMenuItem makeObserveSounds;

 JCheckBoxMenuItem channelNumberLeft;
 JCheckBoxMenuItem tabbing;
 JCheckBoxMenuItem tellswitch;
 JCheckBoxMenuItem highlight;
 JCheckBoxMenuItem materialCount;
 JCheckBoxMenuItem showRatings;
 JCheckBoxMenuItem showFlags;
JCheckBoxMenuItem useLightBackground;
 JCheckBoxMenuItem boardconsole0;
 JCheckBoxMenuItem boardconsole1;
 JCheckBoxMenuItem boardconsole2;
 JCheckBoxMenuItem boardconsole3;
 JCheckBoxMenuItem sidewaysconsole;
JCheckBoxMenuItem userbuttons;
JCheckBoxMenuItem consolemenu;

JCheckBoxMenuItem toolbarvisible;
JCheckBoxMenuItem lineindent;

JCheckBoxMenuItem tabLayout1;
JCheckBoxMenuItem tabLayout2;
JCheckBoxMenuItem tabLayout3;

JCheckBoxMenuItem shoutTimestamp;
JCheckBoxMenuItem tellTimestamp;
JCheckBoxMenuItem channelTimestamp;
JCheckBoxMenuItem leftNameTimestamp;
JCheckBoxMenuItem reconnectTimestamp;
JCheckBoxMenuItem qtellTimestamp;
JCheckBoxMenuItem checkLegality;
JCheckBoxMenuItem useTopGame;



 JCheckBoxMenuItem aspect0;
 JCheckBoxMenuItem aspect1;
 JCheckBoxMenuItem aspect2;
 JCheckBoxMenuItem aspect3;
JCheckBoxMenuItem woodenboard1;
JCheckBoxMenuItem woodenboard2;
JCheckBoxMenuItem woodenboard3;
JCheckBoxMenuItem grayishboard;
JCheckBoxMenuItem solidboard;
JCheckBoxMenuItem oliveboard;
JCheckBoxMenuItem cherryboard;
JCheckBoxMenuItem purpleboard;


JCheckBoxMenuItem board5;
JCheckBoxMenuItem board6;
JCheckBoxMenuItem board7;

JCheckBoxMenuItem pieces1;
JCheckBoxMenuItem pieces2;
JCheckBoxMenuItem pieces3;
JCheckBoxMenuItem pieces4;
JCheckBoxMenuItem pieces5;
JCheckBoxMenuItem pieces6;
JCheckBoxMenuItem pieces7;
JCheckBoxMenuItem pieces8;
JCheckBoxMenuItem pieces9;
JCheckBoxMenuItem pieces10;
JCheckBoxMenuItem pieces11;
JCheckBoxMenuItem pieces12;
JCheckBoxMenuItem pieces13;
JCheckBoxMenuItem pieces14;
JCheckBoxMenuItem pieces15;
JCheckBoxMenuItem pieces16;
JCheckBoxMenuItem pieces17;
JCheckBoxMenuItem pieces18;
JCheckBoxMenuItem pieces19;
JCheckBoxMenuItem pieces20;
JCheckBoxMenuItem pieces21;
JCheckBoxMenuItem pieces22;
JCheckBoxMenuItem pieces23;



JCheckBoxMenuItem randomArmy;
JCheckBoxMenuItem randomTiles;

JCheckBoxMenuItem iloggedon;
JCheckBoxMenuItem rotateaways;
JCheckBoxMenuItem notifysound;
JCheckBoxMenuItem qsuggestPopup;
JCheckBoxMenuItem autopopup;

JCheckBoxMenuItem pgnlogging;
JCheckBoxMenuItem compactNameList;
  JMenuItem preset0;
  JMenuItem preset1;
  JMenuItem preset2;
  JMenuItem preset3;
  JMenuItem  reconnect2;
  createWindows mycreator;
  resourceClass graphics;
Runtime rt;

ConcurrentLinkedQueue<myoutput> queue;

chessbot4 client;
gameboard myboards[];
Image [] img;
ConcurrentLinkedQueue<newBoardData> gamequeue;
subframe [] consoleSubframes;
chatframe [] consoleChatframes;

settings mysettings;



 class MyFocusTraversalPolicy extends ContainerOrderFocusTraversalPolicy

{ protected boolean accept(Component aComp)

{ if (aComp instanceof subframe || aComp instanceof gameboard)

return super.accept(aComp);

return false; // JLabel and JPanel.

}
}
mymultiframe()
{

	graphics = new resourceClass();
	gamequeue = new ConcurrentLinkedQueue<newBoardData>();

	sharedVariables = new channels();
	sharedVariables.useTopGames = getOnTopSetting();
queue = new ConcurrentLinkedQueue<myoutput>();

	seekGraph = new seekGraphFrame(sharedVariables, queue);
	myboards = new gameboard[sharedVariables.maxGameTabs];
	for(int bbo=0; bbo<sharedVariables.maxGameTabs; bbo++)
		myboards[bbo]=null;

img = new Image[14];
mineScores = new saveScores();

mysettings = new settings(sharedVariables);


sharedVariables.img=img;

consoles = new JTextPane[sharedVariables.maxConsoleTabs];
gameconsoles = new JTextPane[sharedVariables.maxGameConsoles];
consoleSubframes=new subframe[sharedVariables.maxConsoles];;
consoleChatframes=new chatframe[sharedVariables.maxConsoles];;

colortype=1;
sharedVariables.desktop = new JDesktopPaneCustom(sharedVariables, myboards, consoleSubframes, this);
sharedVariables.desktop.add(seekGraph);




setFocusCycleRoot(true);
setFocusTraversalPolicy(new MyFocusTraversalPolicy());

String myweburl = "www.google.com";
mywebframe = new webframe(sharedVariables,  queue, myweburl);
sharedVariables.desktop.add(mywebframe);
		try {
			mywebframe.setSize(sharedVariables.webframeWidth, sharedVariables.webframeHeight);
}catch(Exception d){};

/*************** trying list code *****************/
eventsList = new listClass("Events");
seeksList = new listClass("Human Seeks");
computerSeeksList = new listClass("Computer Seeks");
notifyList = new listClass("Notify List");
myfirstlist = new listFrame(this, sharedVariables, queue, eventsList, seeksList, computerSeeksList, notifyList, this);
gameList = new tableClass();
myGameList = new gameFrame(sharedVariables, queue, gameList);
sharedVariables.myGameList=myGameList;
myGameList.setSize(600,425);
//sharedVariables.desktop.add(myGameList);

/*************** end list code******************/
docWriter myDocWriter = new docWriter(sharedVariables, consoleSubframes, consoles, gameconsoles, myboards, consoleChatframes);
mycreator = new createWindows(sharedVariables, consoleSubframes ,myboards, consoles, gameconsoles, queue, img, gamequeue, mywebframe, graphics, myfirstlist, myDocWriter, consoleChatframes);
mycreator.createConsoleFrame(); //Create first window
mycreator.createGameFrame();
//mycreator.createListFrame(eventsList, seeksList, computerSeeksList, notifyList, this);
//setContentPane(sharedVariables.desktop);
// add window listener so we can close an engine if open
addWindowListener(this);
getContentPane().add(sharedVariables.desktop, "Center");

 getSettings();
 boolean hasSettings=mysettings.readNow(myboards, consoleSubframes,  sharedVariables, consoles, gameconsoles); // read  for any saved settings  dont know what get settings  is doing MA 5-30-10
mineScores.readNow(sharedVariables);

client = new chessbot4(gameconsoles, gamequeue, queue, consoles, sharedVariables, myboards, consoleSubframes, mycreator, graphics, eventsList, seeksList, computerSeeksList, notifyList, gameList, myGameList, this, consoleChatframes, seekGraph, this, myConnection);
repaint();
client.enabletimestamp();

loadGraphicsStandAlone();
loadSoundsStandAlone();
//loadGraphicsApplet();
//loadSoundsApplet();

// we look if these files  exist and if we do load name pass then any commands into script array list for connect to run
// MA 1-1-11
try  {

scriptLoader loadScripts = new  scriptLoader();
loadScripts.loadScript(sharedVariables.iccLoginScript, "lantern_icc.ini");
loadScripts.loadScript(sharedVariables.ficsLoginScript, "lantern_fics.ini");
loadScripts.loadScript(sharedVariables.notifyControllerScript, sharedVariables.notifyControllerFile);

} catch(Exception scriptErrror){}
setUpChannelNotify();
setUpLanternNotify();
parseCountries();
Thread t = new Thread(client);
t.start();


	createMenu();
consoleSubframes[0].makeHappen(0);
    if(sharedVariables.highlightMoves == false)
    	highlight.setSelected(false);
    else
    	highlight.setSelected(true);



 if(sharedVariables.useTopGames == true)
 useTopGame.setSelected(true);
 else
 useTopGame.setSelected(false);

 if(sharedVariables.showMaterialCount == false)
    	materialCount.setSelected(false);
    else
    	materialCount.setSelected(true);

 if(sharedVariables.showRatings == false)
    	showRatings.setSelected(false);
    else
    	showRatings.setSelected(true);

 if(sharedVariables.showFlags == false)
    	showFlags.setSelected(false);
    else
    	showFlags.setSelected(true);

if(sharedVariables.useLightBackground == false)
    	useLightBackground.setSelected(false);
    else
    	useLightBackground.setSelected(true);

    setPieces(sharedVariables.pieceType);
    setBoard(sharedVariables.boardType);
	if(sharedVariables.pgnLogging == true)
		pgnlogging.setSelected(true);
	else
		pgnlogging.setSelected(false);

	if(sharedVariables.switchOnTell == true)
		tellswitch.setSelected(true);
	else
		tellswitch.setSelected(false);
if(sharedVariables.toolbarVisible == true)
		toolbarvisible.setSelected(true);
	else
		toolbarvisible.setSelected(false);

 if(sharedVariables.autoBufferChat == false)
    	autobufferchat.setSelected(false);
    else
    	autobufferchat.setSelected(true);
 if(sharedVariables.channelNumberLeft == false)
        channelNumberLeft.setSelected(false);
    else
    	channelNumberLeft.setSelected(true);


if(sharedVariables.channelTimestamp == true)
		channelTimestamp.setSelected(true);
	else
		channelTimestamp.setSelected(false);
if(sharedVariables.shoutTimestamp == true)
		shoutTimestamp.setSelected(true);
	else
		shoutTimestamp.setSelected(false);
if(sharedVariables.qtellTimestamp == true)
		qtellTimestamp.setSelected(true);
	else
		qtellTimestamp.setSelected(false);
if(sharedVariables.reconnectTimestamp == true)
		reconnectTimestamp.setSelected(true);
	else
		reconnectTimestamp.setSelected(false);

if(sharedVariables.tellTimestamp == true)
		tellTimestamp.setSelected(true);
	else
		tellTimestamp.setSelected(false);
if(sharedVariables.leftTimestamp == true)
		leftNameTimestamp.setSelected(true);
	else
		leftNameTimestamp.setSelected(false);


if(sharedVariables.checkLegality == true)
		checkLegality.setSelected(true);
	else
		checkLegality.setSelected(false);


if(sharedVariables.indent == true)
		lineindent.setSelected(true);
	else
		lineindent.setSelected(false);
if(sharedVariables.randomArmy == true)
		randomArmy.setSelected(true);
	else
		randomArmy.setSelected(false);
if(sharedVariables.randomBoardTiles == true)
		randomTiles.setSelected(true);
	else
		randomTiles.setSelected(false);


if(sharedVariables.specificSounds[4] == true)
		notifysound.setSelected(true);
	else
		notifysound.setSelected(false);
if(sharedVariables.tabsOnly == true)
	tabbing.setSelected(true);
else
	tabbing.setSelected(false);

if(sharedVariables.showQsuggest == true)
	qsuggestPopup.setSelected(true);
else
	qsuggestPopup.setSelected(false);


if(sharedVariables.rotateAways == true)
{
	rotateaways.setSelected(true);
try{
	scriptLoader loadScripts = new  scriptLoader();
	loadScripts.loadScript(sharedVariables.lanternAways, "lantern_away.txt");
}catch(Exception du){}
}
else
	rotateaways.setSelected(false);

if(sharedVariables.iloggedon == true)
	iloggedon.setSelected(true);
else
	iloggedon.setSelected(false);


if(sharedVariables.sideways == true)
	sidewaysconsole.setSelected(true);
else
	sidewaysconsole.setSelected(false);

if(sharedVariables.showButtonTitle == true)
	userbuttons.setSelected(true);
else
	userbuttons.setSelected(false);


if(sharedVariables.autopopup == true)
	autopopup.setSelected(true);
else
	autopopup.setSelected(false);

if(sharedVariables.autoHistoryPopup == true)
	autoHistoryPopup.setSelected(true);
else
	autoHistoryPopup.setSelected(false);
if(sharedVariables.makeObserveSounds == true)
	makeObserveSounds.setSelected(true);
else
	makeObserveSounds.setSelected(false);


if(sharedVariables.showConsoleMenu == true)
	consolemenu.setSelected(true);
else
	consolemenu.setSelected(false);

/*if(sharedVariables.consoleLayout == 1)
{
	tabLayout1.setSelected(true);
	tabLayout2.setSelected(false);
	tabLayout3.setSelected(false);

}
else if(sharedVariables.consoleLayout == 3)
{
	tabLayout1.setSelected(false);
	tabLayout2.setSelected(false);
	tabLayout3.setSelected(true);
	consoleSubframes[0].overall.recreate();


}
else
{
	tabLayout1.setSelected(false);
	tabLayout2.setSelected(true);
	tabLayout3.setSelected(false);

	consoleSubframes[0].overall.recreate();

}
*/

/* name list stuff */
myfirstlist.theChannelList.setForeground(sharedVariables.nameForegroundColor);
myfirstlist.theChannelList.setBackground(sharedVariables.nameBackgroundColor);
myfirstlist.theChannelList.setFont(sharedVariables.nameListFont);
myfirstlist.theChannelList2.setForeground(sharedVariables.nameForegroundColor);
myfirstlist.theChannelList2.setBackground(sharedVariables.nameBackgroundColor);
myfirstlist.theChannelList2.setFont(sharedVariables.nameListFont);
myfirstlist.theChannelList3.setForeground(sharedVariables.nameForegroundColor);
myfirstlist.theChannelList3.setBackground(sharedVariables.nameBackgroundColor);
myfirstlist.theChannelList3.setFont(sharedVariables.nameListFont);


for(int iii=0; iii<sharedVariables.maxConsoleTabs; iii++)
{
	if(consoleSubframes[iii]!=null)
	{

	if(sharedVariables.nameListFont == null)
		sharedVariables.nameListFont=consoleSubframes[iii].myNameList.getFont();
	if(sharedVariables.consolesNamesLayout[iii]==0)
	{

		consoleSubframes[iii].listChoice.setSelected(false);
	}
	consoleSubframes[iii].myNameList.setForeground(sharedVariables.nameForegroundColor);
	consoleSubframes[iii].myNameList.setBackground(sharedVariables.nameBackgroundColor);

	consoleSubframes[iii].overall.recreate(sharedVariables.consolesTabLayout[iii]);
final int iiii=iii;
SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                              	if(sharedVariables.tellconsole!=iiii)
							  		consoleSubframes[iiii].tellCheckbox.setSelected(false);// we set it to true in create console to match tell variable. undo the first one here if needed


                            } catch (Exception e1) {

                            }
                        }
                    });
	}
}

	for(int bam=0; bam<sharedVariables.openConsoleCount; bam++)
		consoleSubframes[bam].consoleMenu.setVisible(sharedVariables.showConsoleMenu);



if(sharedVariables.channelColor[0]!=null)
	sharedVariables.typedColor=sharedVariables.channelColor[0];
 
 
 
 /****************** we do these next few in gui thread **********************************/

 SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {

	if(sharedVariables.boardConsoleType != 2)
	redrawBoard(sharedVariables.boardConsoleType);
	// now apply foreground color to tabs
	for(int cona=0; cona< sharedVariables.maxConsoleTabs ; cona++)
		consoles[cona].setForeground(sharedVariables.ForColor);
	// now add in channel tab default names
	try{
		for(int mycons=1; mycons<sharedVariables.maxConsoleTabs; mycons++)
		{
	setConsoleTabTitles asetter = new setConsoleTabTitles();
    asetter.createConsoleTabTitle(sharedVariables, mycons, consoleSubframes, sharedVariables.consoleTabCustomTitles[mycons]);
	}
}catch(Exception badsetting){}

setInputFont();
try {

					// now game boards
 	        for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 {
 				 if(myboards[i] != null)
 	 			{
 	        		myboards[i].mycontrolspanel.setFont();
 	 			}
 			}

                        JFrame framer = new JFrame("open board count is ( later event hopefully)" + sharedVariables.openBoardCount);
                        framer.setSize(200,100);
                        framer.setVisible(true);

}
catch(Exception bdfont){}


                             } catch (Exception e1) {

                            }
                        }
                    });





// applet
//Image myIconImage = getImage(getDocumentBase(), "lantern.png");
//setIconImage(myIconImage);
// end applet
// stand alone
try{
		Image myIconImage=null;
		URL myurl = this.getClass().getResource("lantern.png");
		myIconImage =Toolkit.getDefaultToolkit().getImage(myurl);
		setIconImage(myIconImage);
}
catch(Exception e)
{

}
try {
	setMySize();
}
catch(Exception donthaveit){}

if(hasSettings == false) // this hasSettings is returned by readNow which reads settings. if false. they have no settings file and i try to position windows. MA 9-19-10
{
try {
		if(sharedVariables.screenW > 100 && sharedVariables.screenH > 100)
		{
		int px = 30;
		int py = 30;
		int cw = (int) (sharedVariables.screenW / 2 - px - 1/10 * sharedVariables.screenW/2);
		int ch = (int) (sharedVariables.screenH - py -   sharedVariables.screenH / 6);


		consoleSubframes[0].setLocation(px, py);
		consoleSubframes[0].setSize(cw, ch);
		px = px + px + cw;
		py = 30;
		cw = (int) (sharedVariables.screenW / 2 - 30 -   (sharedVariables.screenW/2) / 10);
		ch = (int) (sharedVariables.screenH - py - sharedVariables.screenH / 6);
		if(ch > cw + 100)
		ch=cw+100;
                if(sharedVariables.useTopGames == false)
                {
		myboards[0].setLocation(px, py);
		myboards[0].setSize(cw, ch);
                }
                else
                {
                  
                  final int px1 = px;
                  final int py1 = py;
                  final int cw1 = cw;
                  final int ch1 = ch;
                try {
                SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                              if(myboards[0].topGame != null)
                              {
                 myboards[0].topGame.setLocation(px1, py1);
		myboards[0].topGame.setSize(cw1, ch1);
                              }
                            } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });

                }catch(Exception badf){}
                }

                }

}catch(Exception dontknow){}// could not complete getting screen size and postioning windows
}// end if not have any settings read


// make as many consoles as we had last time
try {
	if(sharedVariables.visibleConsoles>1)
		for(int nummake=1; nummake<sharedVariables.visibleConsoles; nummake++)
			mycreator.restoreConsoleFrame();

}catch(Exception makingConsoles){}


try {if(sharedVariables.activitiesOpen == true)
openActivities();
}catch(Exception badopen){}

makeToolBar();
getContentPane().add(toolBar,  BorderLayout.NORTH);

if(sharedVariables.toolbarVisible == true)
toolBar.setVisible(true);
else
toolBar.setVisible(false);

              try {
                SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                            // myboards[0].recreate();
                                     } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });

                }catch(Exception badf){}






}




public void parseCountries()
{
/*
try {
  scriptLoader loadScripts = new  scriptLoader();
 ArrayList<String> country = new ArrayList();
loadScripts.loadScript(country, "flags3.txt");
String output = "";


for(int z=0; z<country.size(); z++)
{
  String line= country.get(z);
      String first="";
      String last="";
      String text="";
      String oldtext="";

  try {

	line=line.replace("\t", " ");
	if(line.contains("N/A"))
	continue;
        		StringTokenizer tokens = new StringTokenizer(line, " ");

      boolean lastToken=false;

      while(lastToken == false)
      {
      oldtext=text;
    try {

      text=tokens.nextToken();
      if(text==null)
      {
       last=oldtext;
       lastToken=true;
      }// end if null
      else
      {
       if(first.equals(""))
       first=oldtext;
       else
       first=first + "_" + oldtext;
      }

      }// end try
      catch(Exception dui){last=oldtext; lastToken=true;}
      } // end while
      output+=last.toUpperCase() + ";" + first + ";";
    //int i=line.indexOf(";");
   // String line1=line.substring(0, i);
   // String line2=line.substring(i+1, line.length());
  //  output+=line2 + ";" + line1 + ";";

  }
  catch(Exception dogeatdog){}
}// end for
FileWrite writer = new FileWrite();
writer.write(output, "new-countries.txt");
}
catch(Exception dumb){}
      */
  }

boolean getOnTopSetting()
{

scriptLoader loadScripts = new  scriptLoader();
 ArrayList<String> ontop = new ArrayList();
loadScripts.loadScript(ontop, "lantern_board_on_top.txt");
if(ontop.size() > 0)
{String top= ontop.get(0);
if(top.equals("true"))
return true;
}
return false;
}

public void setUpLanternNotify()
{
try {
  scriptLoader loadScripts = new  scriptLoader();
 ArrayList<String> notifyScript = new ArrayList();
loadScripts.loadScript(notifyScript, "lantern_global_notify.txt");
for(int z=0; z<notifyScript.size(); z++)
{
  String notString= notifyScript.get(z);
  try {
           int i=notString.indexOf(" ");
           if(i > 1) // first two spaces minimum needed for name
           {
                   lanternNotifyClass temp = new lanternNotifyClass();
                   temp.name=notString.substring(0, i);
                   try {int on=Integer.parseInt(notString.substring(i+1, notString.length()-2));// -2 for the \r\n
                   if(on==1)
                   temp.sound=true;
           } 
           catch(Exception nosound){}
                   sharedVariables.lanternNotifyList.add(temp);
           }
  }// end inner try
  catch(Exception duii){}
}// end for
}// end try
catch(Exception dui){}
}// end method

public void setUpChannelNotify()
{
try {
  scriptLoader loadScripts = new  scriptLoader();
 ArrayList<String> notifyScript = new ArrayList();
loadScripts.loadScript(notifyScript, "lantern_channel_notify.txt");
String channel="";
channelNotifyClass temp=null;
for(int z=0; z<notifyScript.size(); z++)
{
if(notifyScript.get(z).startsWith("#"))
{
 channel=notifyScript.get(z).substring(1, notifyScript.get(z).length());
 // add node
 if(temp!=null)// add last channel on new channel
 sharedVariables.channelNotifyList.add(temp);

 temp = new channelNotifyClass();
 temp.channel=channel;
}
else if(!channel.equals(""))
{
 temp.nameList.add(notifyScript.get(z));
}// end else if
}  // end for
 if(temp!=null && !channel.equals(""))
 if(temp.nameList.size()> 0)
 sharedVariables.channelNotifyList.add(temp);// to get last one or even first one since prior loads happen on next item
}// end try
catch(Exception dui){}
}// end method setupchannelnotify
public void createMenu()
{

JMenuBar menu = new JMenuBar();
 JMenu mywindows = new JMenu("File");
  JMenuItem  reconnect1 = new JMenuItem("Reconnect to ICC");
  mywindows.add(reconnect1);
  JMenuItem  reconnect3 = new JMenuItem("Reconnect to Queen");
  mywindows.add(reconnect3);

  reconnect2 = new JMenuItem("Reconnect to FICS");// off now
  mywindows.add(reconnect2);
  reconnect2.setVisible(false);
  JMenuItem  wallpaper1 = new JMenuItem("Set Wallpaper");
  mywindows.add(wallpaper1);
    JMenuItem  settings2 = new JMenuItem("Save Settings");
    mywindows.add(settings2);

 menu.add(mywindows);


JMenu mywindowscolors = new JMenu("Colors");

  JMenuItem fontchange = new JMenuItem("Change Font");
  mywindowscolors.add(fontchange);
   fontchange.addActionListener(this);






JMenuItem channelcol = new JMenuItem("Channel Colors");
  mywindowscolors.add(channelcol);

 JMenuItem consoleColors = new JMenuItem("Console Colors");
  mywindowscolors.add(consoleColors);

JMenuItem listColor = new JMenuItem("Activities Window Color");
  mywindowscolors.add(listColor);
listColor.addActionListener(this);




JMenuItem tellNameColor = new JMenuItem("PTell Name Color");
mywindowscolors.add(tellNameColor);
tellNameColor.addActionListener(this);


 JMenu typingarea = new JMenu("Typing Field");

 JMenuItem inputfontchange = new JMenuItem("Change Input Font");
  typingarea.add(inputfontchange);
inputfontchange.addActionListener(this);

JMenuItem inputcommand = new JMenuItem("Input Command Color");
  typingarea.add(inputcommand);
inputcommand.addActionListener(this);

JMenuItem inputchat = new JMenuItem("Input Chat Color");
  typingarea.add(inputchat);
inputchat.addActionListener(this);

mywindowscolors.add(typingarea);



/****************** names list *****************/
JMenu inChannelNamesMenu = new JMenu("In Channel Names Menu");

JMenuItem nameForegroundColor = new JMenuItem("Names List Foreground Color");
inChannelNamesMenu.add(nameForegroundColor);
nameForegroundColor.addActionListener(this);

JMenuItem nameBackgroundColor = new JMenuItem("Names List Background Color");
inChannelNamesMenu.add(nameBackgroundColor);
nameBackgroundColor.addActionListener(this);

JMenuItem namelistFont = new JMenuItem("Names List Font");
inChannelNamesMenu.add(namelistFont);
namelistFont.addActionListener(this);
mywindowscolors.add(inChannelNamesMenu);
/*********** end names list ******************/

JMenuItem colortimestamp = new JMenuItem("Chat Timestamp Color");
colortimestamp.addActionListener(this);
mywindowscolors.add(colortimestamp);


/*JMenuItem channelTitles = new JMenuItem("Titles In Channel Color");
mywindowscolors.add(channelTitles);
channelTitles.addActionListener(this);
  */
//duplicate
// JMenuItem mainback = new JMenuItem("Main Background");
//  mywindows.add(mainback);

JMenu tabsColorsMenu = new JMenu("Tabs Colors Menu");


JMenu tabback = new JMenu("Tab Background");
tabsColorsMenu.add(tabback);
JMenuItem tabback1 = new JMenuItem("Visited");
JMenuItem tabback2 = new JMenuItem("Unvisited");
JMenuItem tabback5 = new JMenuItem("Unvisited/Visited");

tabback.add(tabback1);
tabback.add(tabback2);
tabback.add(tabback5);
JMenuItem tabimon = new JMenuItem("Tab I'm On Background");
tabback.add(tabimon);


JMenu tabfore = new JMenu("Tab Foreground");
tabsColorsMenu.add(tabfore);
JMenuItem tabback3 = new JMenuItem("Active");
JMenuItem tabback4 = new JMenuItem("Non Active");
tabfore.add(tabback3);
tabfore.add(tabback4);

tabback1.addActionListener(this);
tabback2.addActionListener(this);
tabback3.addActionListener(this);
tabback4.addActionListener(this);
tabback5.addActionListener(this);
tabimon.addActionListener(this);


JMenuItem tabborder1 = new JMenuItem("Tab Border");
tabborder1.addActionListener(this);
tabsColorsMenu.add(tabborder1);

JMenuItem tabfontchange = new JMenuItem("Change Tab Font");
  tabsColorsMenu.add(tabfontchange);
tabfontchange.addActionListener(this);


mywindowscolors.add(tabsColorsMenu);


 JMenuItem  wallpaper2 = new JMenuItem("Set Application Background Color");
    mywindowscolors.add(wallpaper2);
   wallpaper2.addActionListener(this);






menu.add(mywindowscolors);















setJMenuBar(menu);
//mainback.addActionListener(this);

settings2.addActionListener(this);
reconnect1.addActionListener(this);
reconnect2.addActionListener(this);
reconnect3.addActionListener(this);

wallpaper1.addActionListener(this);



channelcol.addActionListener(this);
consoleColors.addActionListener(this);

/*************************  options window now ************/

JMenu optionsmenu = new JMenu("Options");
 JMenu soundmenu = new JMenu("Sound");
 JCheckBoxMenuItem hearsound = new JCheckBoxMenuItem("Sounds");
hearsound.setSelected(true);
makeObserveSounds= new JCheckBoxMenuItem("Sounds for Observed Games");
makeObserveSounds.addActionListener(this);


notifysound=new JCheckBoxMenuItem("Sounds for Notifications");
hearsound.addActionListener(this);
notifysound.addActionListener(this);

soundmenu.add(hearsound);
soundmenu.add(makeObserveSounds);
soundmenu.add(notifysound);

optionsmenu.add(soundmenu);


 optionsmenu.addSeparator();
 JMenuItem winanalysis = new JMenuItem("Load Winboard Engine");
winanalysis.addActionListener(this);
  optionsmenu.add(winanalysis);

JMenuItem ucianalysis = new JMenuItem("Load UCI Engine");
  optionsmenu.add(ucianalysis);
ucianalysis.addActionListener(this);

JMenuItem enginerestart = new JMenuItem("Restart Engine");
  optionsmenu.add(enginerestart);
enginerestart.addActionListener(this);


JMenuItem enginestop = new JMenuItem("Stop Engine");
  optionsmenu.add(enginestop);
enginestop.addActionListener(this);

optionsmenu.addSeparator();
JMenuItem customizetools = new JMenuItem("Customize Toolbar");
customizetools.addActionListener(this);
optionsmenu.add(customizetools);

optionsmenu.addSeparator();
/************** advanced ****************/

JMenu advancedOptions = new JMenu("Advanced");
qsuggestPopup = new JCheckBoxMenuItem("Qsuggest Popups");
qsuggestPopup.addActionListener(this);
advancedOptions.add(qsuggestPopup);

userbuttons = new JCheckBoxMenuItem("Show User Button Titles");
userbuttons.addActionListener(this);
advancedOptions.add(userbuttons);

consolemenu = new JCheckBoxMenuItem("Show Console Menu");
consolemenu.setSelected(true);
consolemenu.addActionListener(this);
advancedOptions.add(consolemenu);

channelNumberLeft = new JCheckBoxMenuItem("Channel Number On Left");
channelNumberLeft.setSelected(true);
channelNumberLeft.addActionListener(this);
advancedOptions.add(channelNumberLeft);


compactNameList = new JCheckBoxMenuItem("Compact Channel Name List");
compactNameList.setSelected(false);
compactNameList.addActionListener(this);
advancedOptions.add(compactNameList);


autobufferchat = new JCheckBoxMenuItem("Auto Buffer Chat Length");
advancedOptions.add(autobufferchat);

useTopGame = new JCheckBoxMenuItem("Make Boards Always On Top");
advancedOptions.add(useTopGame);
useTopGame.addActionListener(this);


  autopopup = new JCheckBoxMenuItem("Auto Name Popup");
  advancedOptions.add(autopopup);
   autoHistoryPopup = new JCheckBoxMenuItem("Auto History Popup");
  advancedOptions.add(autoHistoryPopup);

  autobufferchat.addActionListener(this);
autopopup.addActionListener(this);
autoHistoryPopup.addActionListener(this);

lineindent = new JCheckBoxMenuItem("Indent Multi Line Tells");
lineindent.addActionListener(this);
advancedOptions.add(lineindent);

/**************************** end advanced ***********************/
optionsmenu.add(advancedOptions);


JMenu featuresMenu = new JMenu("Features");

 tellswitch = new JCheckBoxMenuItem("Switch Tab On Tell");
  featuresMenu.add(tellswitch);
autonoidle = new JCheckBoxMenuItem("No Idle");
featuresMenu.add(autonoidle);
tellswitch.addActionListener(this);
autonoidle.addActionListener(this);
 rotateaways = new JCheckBoxMenuItem("Rotate Away Message");
  featuresMenu.add(rotateaways);
iloggedon = new JCheckBoxMenuItem("Send iloggedon");
featuresMenu.add(iloggedon);
iloggedon.addActionListener(this);
rotateaways.addActionListener(this);


 JMenu examReplay = new JMenu("Examine Game Replay");
 JMenuItem autostart = new JMenuItem("Start AutoExam");
 examReplay.add(autostart);
 JMenuItem autostop = new JMenuItem("Stop AutoExam");
 examReplay.add(autostop);
 JMenuItem autoset = new JMenuItem("Set AutoExam Speed");
  examReplay.add(autoset);
 autostart.addActionListener(this);
autoset.addActionListener(this);
autostop.addActionListener(this);
 featuresMenu.add(examReplay);
optionsmenu.add(featuresMenu);


JMenu observeOptions = new JMenu("Observing Options");
JMenu tournieFollow = new JMenu("Follow Tomato Tournament Games");
JCheckBoxMenuItem autoflash = new JCheckBoxMenuItem("Auto Observe Flash");
  tournieFollow.add(autoflash);
JCheckBoxMenuItem autocooly = new JCheckBoxMenuItem("Auto Observe Cooly");
  tournieFollow.add(autocooly);
JCheckBoxMenuItem autotomato = new JCheckBoxMenuItem("Auto Observe Tomato");
  tournieFollow.add(autotomato);
JCheckBoxMenuItem autowildone = new JCheckBoxMenuItem("Auto Observe WildOne");
  tournieFollow.add(autowildone);
JCheckBoxMenuItem autoslomato = new JCheckBoxMenuItem("Auto Observe Slomato");
  tournieFollow.add(autoslomato);
JCheckBoxMenuItem autoketchup = new JCheckBoxMenuItem("Auto Observe Ketchup");
  tournieFollow.add(autoketchup);
JCheckBoxMenuItem autoolive = new JCheckBoxMenuItem("Auto Observe Olive");
  tournieFollow.add(autoolive);
JCheckBoxMenuItem autolittleper = new JCheckBoxMenuItem("Auto Observe LittlePer");
  tournieFollow.add(autolittleper);



  autoflash.addActionListener(this);
autocooly.addActionListener(this);
autotomato.addActionListener(this);
autowildone.addActionListener(this);
autoslomato.addActionListener(this);
autoketchup.addActionListener(this);
autoolive.addActionListener(this);
autolittleper.addActionListener(this);


 observeOptions.add(tournieFollow);
  JMenu randomGraphics = new JMenu("Random Pieces Board when Observing");
  

 randomArmy = new JCheckBoxMenuItem("Random Piece Set Observe Only");
randomArmy.addActionListener(this);
randomGraphics.add(randomArmy);

JMenuItem configureRand = new JMenuItem("Configure Random Pieces");
configureRand.addActionListener(this);
randomGraphics.add(configureRand);

 randomTiles = new JCheckBoxMenuItem("Random Square Tiles Observe Only");
randomTiles.addActionListener(this);
randomGraphics.add(randomTiles);

 observeOptions.add(randomGraphics);

 optionsmenu.add(observeOptions);



 JMenu chattimestamp=new JMenu("Chat Timestamp");

channelTimestamp=new JCheckBoxMenuItem("Timestamp Channels");
channelTimestamp.addActionListener(this);
chattimestamp.add(channelTimestamp);

shoutTimestamp=new JCheckBoxMenuItem("Timestamp Shouts");
shoutTimestamp.addActionListener(this);
chattimestamp.add(shoutTimestamp);





tellTimestamp=new JCheckBoxMenuItem("Timestamp Tells");
tellTimestamp.addActionListener(this);
chattimestamp.add(tellTimestamp);

leftNameTimestamp=new JCheckBoxMenuItem("Timestamp To Left Of Name");
leftNameTimestamp.addActionListener(this);
//chattimestamp.add(leftNameTimestamp); disabling the option

qtellTimestamp=new JCheckBoxMenuItem("Timestamp Channel Qtells");
qtellTimestamp.addActionListener(this);
chattimestamp.add(qtellTimestamp);


reconnectTimestamp=new JCheckBoxMenuItem("Timestamp Connecting");
reconnectTimestamp.addActionListener(this);
chattimestamp.add(reconnectTimestamp);



optionsmenu.add(chattimestamp);


 menu.add(optionsmenu);


/******************************************* end of options window *****************/







JMenu myconsolesmenu = new JMenu("Windows");
// JMenuItem nconsole = new JMenuItem("New Console");
// myconsolesmenu.add(nconsole);
 JMenuItem nboard = new JMenuItem("New Board");
 myconsolesmenu.add(nboard);
nboard.addActionListener(this);


 JMenuItem rconsole = new JMenuItem("New Chat Console");
 myconsolesmenu.add(rconsole);

  JMenuItem detachedconsole = new JMenuItem("New Detached Chat Console");
  myconsolesmenu.add(detachedconsole);


  JMenuItem rconsole2 = new JMenuItem("Customize Tab");
  myconsolesmenu.add(rconsole2);
 JMenuItem  webopener = new JMenuItem("Open Web");
    myconsolesmenu.add(webopener);

 JMenuItem  seekingGraph = new JMenuItem("Seek Graph");
    myconsolesmenu.add(seekingGraph);

JMenuItem eventlist = new JMenuItem("Activities Window");
 myconsolesmenu.add(eventlist);

toolbarvisible= new JCheckBoxMenuItem("Toolbar");
myconsolesmenu.add(toolbarvisible);

JMenuItem channelmap = new JMenuItem("Channel Map");
myconsolesmenu.add(channelmap);
channelmap.addActionListener(this);

JMenuItem channelnotifymap = new JMenuItem("Channel Notify Map");
myconsolesmenu.add(channelnotifymap);
channelnotifymap.addActionListener(this);

JMenuItem channelnotifyonline = new JMenuItem("Channel Notify Online");
myconsolesmenu.add(channelnotifyonline);
channelnotifyonline.addActionListener(this);



JMenuItem toolbox = new JMenuItem("ToolBox");
myconsolesmenu.add(toolbox);

JMenuItem cascading = new JMenuItem("Cascade");
myconsolesmenu.add(cascading);
cascading.addActionListener(this);

 menu.add(myconsolesmenu);
 seekingGraph.addActionListener(this);
 detachedconsole.addActionListener(this);
webopener.addActionListener(this);
eventlist.addActionListener(this);
toolbarvisible.addActionListener(this);
toolbox.addActionListener(this);

JMenu myboardmenu = new JMenu("Game");

 JMenuItem nseek = new JMenuItem("Get a Game");
 myboardmenu.add(nseek);
 nseek.addActionListener(this);

JMenuItem openpgn = new JMenuItem("Open Pgn");
 myboardmenu.add(openpgn);
 openpgn.addActionListener(this);

checkLegality=new JCheckBoxMenuItem("Check Move Legality");
checkLegality.addActionListener(this);
myboardmenu.add(checkLegality);


  JMenu selectboards = new JMenu("Boards");
solidboard = new JCheckBoxMenuItem("Solid Color Board");

woodenboard1 = new JCheckBoxMenuItem("Pale Wood");
woodenboard2 = new JCheckBoxMenuItem("Light Wood");
woodenboard3 = new JCheckBoxMenuItem("Dark Wood");
grayishboard = new JCheckBoxMenuItem("Gray Marble");
board5 = new JCheckBoxMenuItem("Red Marble");
board6 = new JCheckBoxMenuItem("Crampled Paper");
board7 = new JCheckBoxMenuItem("Winter");
oliveboard = new JCheckBoxMenuItem("Olive Board");
cherryboard = new JCheckBoxMenuItem("Cherry Board");
purpleboard = new JCheckBoxMenuItem("Purple Board");



selectboards.add(solidboard);
selectboards.add(woodenboard1);
selectboards.add(woodenboard2);
selectboards.add(woodenboard3);
selectboards.add(grayishboard);
selectboards.add(board5);
selectboards.add(board6);
selectboards.add(board7);
selectboards.add(oliveboard);
selectboards.add(cherryboard);
selectboards.add(purpleboard);


myboardmenu.add(selectboards);
solidboard.addActionListener(this);
woodenboard1.addActionListener(this);
woodenboard2.addActionListener(this);
woodenboard3.addActionListener(this);
grayishboard.addActionListener(this);
board5.addActionListener(this);
board6.addActionListener(this);
board7.addActionListener(this);
oliveboard.addActionListener(this);
cherryboard.addActionListener(this);
purpleboard.addActionListener(this);

woodenboard2.setSelected(true);


// pieces

  JMenu selectpieces = new JMenu("Pieces");
pieces1 = new JCheckBoxMenuItem("Dyche1");
pieces2 = new JCheckBoxMenuItem("Dyche2");
pieces3 = new JCheckBoxMenuItem("Dyche3");
pieces4 = new JCheckBoxMenuItem("Bookup");
pieces5 = new JCheckBoxMenuItem("Xboard");
pieces6 = new JCheckBoxMenuItem("Alpha");
pieces7 = new JCheckBoxMenuItem("Spatial");
pieces8 = new JCheckBoxMenuItem("Harlequin");
pieces9 = new JCheckBoxMenuItem("Berlin");
pieces10 = new JCheckBoxMenuItem("Eboard Classic");
pieces11 = new JCheckBoxMenuItem("Molten Good");
pieces12 = new JCheckBoxMenuItem("Molten Evil");
pieces13 = new JCheckBoxMenuItem("Liebeskind");
pieces14 = new JCheckBoxMenuItem("Eyes");
pieces15 = new JCheckBoxMenuItem("Fantasy");

pieces16 = new JCheckBoxMenuItem("Line");
pieces17 = new JCheckBoxMenuItem("Motif");
pieces18 = new JCheckBoxMenuItem("Utrecht");

pieces19 = new JCheckBoxMenuItem("Adventure");
pieces20 = new JCheckBoxMenuItem("Maya");
pieces21 = new JCheckBoxMenuItem("Medieval");



pieces22 = new JCheckBoxMenuItem("Monge Mix");
pieces23 = new JCheckBoxMenuItem("Random Pieces");

JMenuItem aboutmonge = new JMenuItem ("About Monge Pieces");

selectpieces.add(pieces1);
selectpieces.add(pieces2);
selectpieces.add(pieces3);
selectpieces.add(pieces4);
selectpieces.add(pieces5);
selectpieces.add(pieces6);
//selectpieces.add(pieces7);
JMenu mongeMenu = new JMenu("Monge");
mongeMenu.add(pieces7);
mongeMenu.add(pieces14);
mongeMenu.add(pieces15);
mongeMenu.add(pieces22);

mongeMenu.add(aboutmonge);

selectpieces.add(pieces19);
selectpieces.add(pieces20);
selectpieces.add(pieces21);

selectpieces.add(mongeMenu);

selectpieces.add(pieces8);
selectpieces.add(pieces9);
selectpieces.add(pieces10);

selectpieces.add(pieces16);
selectpieces.add(pieces17);
selectpieces.add(pieces18);


JMenu moltenmenu=new JMenu("Molten");
moltenmenu.add(pieces11);
moltenmenu.add(pieces12);
moltenmenu.add(pieces13);
selectpieces.add(moltenmenu);

selectpieces.add(pieces23);






myboardmenu.add(selectpieces);
pieces1.addActionListener(this);
pieces2.addActionListener(this);
pieces3.addActionListener(this);
pieces4.addActionListener(this);
pieces5.addActionListener(this);
pieces6.addActionListener(this);
pieces7.addActionListener(this);
pieces8.addActionListener(this);
pieces9.addActionListener(this);
pieces10.addActionListener(this);
pieces11.addActionListener(this);
pieces12.addActionListener(this);
pieces13.addActionListener(this);
pieces14.addActionListener(this);
pieces15.addActionListener(this);
pieces16.addActionListener(this);
pieces17.addActionListener(this);
pieces18.addActionListener(this);
pieces19.addActionListener(this);
pieces20.addActionListener(this);

pieces21.addActionListener(this);
pieces22.addActionListener(this);
pieces23.addActionListener(this);


aboutmonge.addActionListener(this);

pieces1.setSelected(true);

/********** end pieces **************/



  JMenu preset = new JMenu("Preset Color Boards");

preset0 = new JMenuItem("Default Board");
preset.add(preset0);


preset1 = new JMenuItem("Tan Board");
preset.add(preset1);

preset2 = new JMenuItem("Gray Color Board");
preset.add(preset2);


preset3 = new JMenuItem("Blitzin Green Board");
preset.add(preset3);

selectboards.add(preset);

preset0.addActionListener(this);
preset1.addActionListener(this);
preset2.addActionListener(this);
preset3.addActionListener(this);
/******************* board colors ********************/

JMenu boardSquareColors = new JMenu("Board Squares Colors");
JMenuItem lcolor = new JMenuItem("Light Square Color");
 boardSquareColors.add(lcolor);
 JMenuItem dcolor = new JMenuItem("Dark Square Color");
  boardSquareColors.add(dcolor);
lcolor.addActionListener(this);
dcolor.addActionListener(this);
myboardmenu.add(boardSquareColors);


JMenu boardColors = new JMenu("Board Colors");

 JMenuItem bbackcolor = new JMenuItem("Board Background Color");
 boardColors.add(bbackcolor);
 JMenuItem bforcolor = new JMenuItem("Board Foreground Color");
  boardColors.add(bforcolor);

  JMenuItem cforcolor = new JMenuItem("Clock Foreground Color");
  boardColors.add(cforcolor);


JMenuItem bcbackcolor = new JMenuItem("Board Clock Background Color");
  boardColors.add(bcbackcolor);
 bbackcolor.addActionListener(this);
bcbackcolor.addActionListener(this);
bforcolor.addActionListener(this);
cforcolor.addActionListener(this);

 myboardmenu.add(boardColors);

 JMenu boardFonts9 = new JMenu("Board Fonts");

 JMenuItem gamefont = new JMenuItem("Game Board Font");
  boardFonts9.add(gamefont);
  gamefont.addActionListener(this);
JMenuItem gameclockfont = new JMenuItem("Game Clock Font");
  boardFonts9.add(gameclockfont);
 gameclockfont.addActionListener(this);
 myboardmenu.add(boardFonts9);





/*************** end board colors *****************/



 tabbing = new JCheckBoxMenuItem("Tabs Only");
  myboardmenu.add(tabbing);

   highlight = new JCheckBoxMenuItem("Highlight Moves");
  myboardmenu.add(highlight);
  materialCount = new JCheckBoxMenuItem("Material Count");
  myboardmenu.add(materialCount);
materialCount.addActionListener(this);

 showFlags = new JCheckBoxMenuItem("Show Flags");
  myboardmenu.add(showFlags);
showFlags.addActionListener(this);

 useLightBackground = new JCheckBoxMenuItem("Use Light Square as Board Background");
 // myboardmenu.add(useLightBackground);   // disabled
useLightBackground.addActionListener(this);

 showRatings = new JCheckBoxMenuItem("Show Ratings on Board When Playing");
  myboardmenu.add(showRatings);
showRatings.addActionListener(this);


JMenu aspect = new JMenu("Board Aspect Ratio");
aspect0 = new JCheckBoxMenuItem("1:1");
aspect.add(aspect0);
aspect0.setSelected(true);

aspect1 = new JCheckBoxMenuItem("5:4");
aspect.add(aspect1);
aspect1.setSelected(false);

aspect2 = new JCheckBoxMenuItem("4:3");
aspect.add(aspect2);
aspect2.setSelected(false);

aspect3 = new JCheckBoxMenuItem("3:2");
aspect.add(aspect3);
aspect3.setSelected(false);

pgnlogging = new JCheckBoxMenuItem("Log Pgn");
myboardmenu.add(pgnlogging);
pgnlogging.addActionListener(this);
pgnlogging.setSelected(true);


myboardmenu.add(aspect);
aspect0.addActionListener(this);
aspect1.addActionListener(this);
aspect2.addActionListener(this);
aspect3.addActionListener(this);


/*************** menu for board console *****************/
JMenu consoleaspect = new JMenu("Board Console");

boardconsole0 = new JCheckBoxMenuItem("Hide Board Console");
consoleaspect.add(boardconsole0);
boardconsole0.setSelected(false);

boardconsole1 = new JCheckBoxMenuItem("Compact Board Console");
consoleaspect.add(boardconsole1);
boardconsole1.setSelected(false);

boardconsole2 = new JCheckBoxMenuItem("Normal Board Console");
consoleaspect.add(boardconsole2);
boardconsole2.setSelected(true);

boardconsole3 = new JCheckBoxMenuItem("Larger Board Console");
consoleaspect.add(boardconsole3);
boardconsole3.setSelected(false);
consoleaspect.addSeparator();
sidewaysconsole = new JCheckBoxMenuItem("Console On Side");
consoleaspect.add(sidewaysconsole);
sidewaysconsole.setSelected(false);



myboardmenu.add(consoleaspect);
boardconsole0.addActionListener(this);
boardconsole1.addActionListener(this);
boardconsole2.addActionListener(this);
boardconsole3.addActionListener(this);
sidewaysconsole.addActionListener(this);

/***************** end board console menu **************/


highlight.addActionListener(this);





highlight.setSelected(true);

menu.add(myboardmenu);


tabbing.addActionListener(this);

//nconsole.addActionListener(this);
rconsole.addActionListener(this);
rconsole2.addActionListener(this);

JMenu actionsmenu = new JMenu("Actions");
 JMenuItem showhistory = new JMenuItem("Show My Recent Games");
 actionsmenu.add(showhistory);
 showhistory.addActionListener(this);

 JMenuItem showlib = new JMenuItem("Show My Game Library");
 actionsmenu.add(showlib);
 showlib.addActionListener(this);

 JMenuItem showstored = new JMenuItem("Show My Adjourned Games");
 actionsmenu.add(showstored);
 showstored.addActionListener(this);

actionsmenu.addSeparator();
JMenuItem showfinger = new JMenuItem("Show My Profile and Ratings");
 actionsmenu.add(showfinger);
 showfinger.addActionListener(this);



JMenuItem showexam = new JMenuItem("Enter Examination Mode");
 actionsmenu.add(showexam);
 showexam.addActionListener(this);

JMenuItem showexamlast = new JMenuItem("Examine My Last Game");
 actionsmenu.add(showexamlast);
 showexamlast.addActionListener(this);



actionsmenu.addSeparator();

 JMenuItem showobs= new JMenuItem("Observe High Rated Game");
 actionsmenu.add(showobs);
 showobs.addActionListener(this);

 JMenuItem showobs5 = new JMenuItem("Observe High Rated 5-Minute Game");
 actionsmenu.add(showobs5);
 showobs5.addActionListener(this);

 JMenuItem showobs15 = new JMenuItem("Observe High Rated 15-Minute Game");
 actionsmenu.add(showobs15);
 showobs15.addActionListener(this);

 actionsmenu.addSeparator();
  JMenuItem showrelay = new JMenuItem("Show Relay Schedule");
 actionsmenu.add(showrelay);
 showrelay.addActionListener(this);

   JMenuItem showweek = new JMenuItem("Show Game of the Week Index");
 actionsmenu.add(showweek);
 showweek.addActionListener(this);

   JMenuItem showfm = new JMenuItem("Open ChessFM");
 actionsmenu.add(showfm);
 showfm.addActionListener(this);

menu.add(actionsmenu);

JMenu helpmenu = new JMenu("Help");
 JMenuItem lanternmanual = new JMenuItem("Lantern Manual");
 helpmenu.add(lanternmanual);
 lanternmanual.addActionListener(this);

  JMenuItem changelog = new JMenuItem("Change Log");
 helpmenu.add(changelog);
 changelog.addActionListener(this);


 JMenuItem infohelp = new JMenuItem("ICC Information Help Files");
 helpmenu.add(infohelp);
 infohelp.addActionListener(this);

 JMenuItem commandhelp = new JMenuItem("ICC Command Help Files");
 helpmenu.add(commandhelp);
 commandhelp.addActionListener(this);
 JMenu poweroutmenu = new JMenu("Extra-games");
 JMenuItem power = new JMenuItem("Start Powerout");
 poweroutmenu.add(power);
 power.addActionListener(this);
 JMenuItem mines = new JMenuItem("Start MineSweeper");
 poweroutmenu.add(mines);
 mines.addActionListener(this);


 JMenuItem mastermind = new JMenuItem("Start Mastermind");
 poweroutmenu.add(mastermind);
 mastermind.addActionListener(this);

 JMenuItem startfour = new JMenuItem("Start Connect Four");
 poweroutmenu.add(startfour);
 startfour.addActionListener(this);




helpmenu.add(poweroutmenu);


menu.add(helpmenu);


}


public void stateChanged(ChangeEvent e) {

 for(int i=0; i < sharedVariables.openConsoleCount; i++)
 {
	 if(consoles[i]!= null)
	 {
 if(colortype == 1)
 {
	 Color newColor = tcc.getColor();
        consoles[i].setForeground(newColor);
}
else if(colortype == 2)
{
	 Color newColor = tcc.getColor();
        consoles[i].setBackground(newColor);
}
} // end if not null
} // end for
}// end method

public void actionPerformed(ActionEvent event)
{
//Object source = event.getSource();
//handle action event here
if(event.getActionCommand().equals("Single Rows of Tabs"))
{
	sharedVariables.consoleLayout=1;
	resetConsoleLayout();
}

if(event.getActionCommand().equals("No Visible Tabs"))
{
	sharedVariables.consoleLayout=3;
	resetConsoleLayout();
}

if(event.getActionCommand().equals("Two Rows of Tabs"))
{

	sharedVariables.consoleLayout=2;
	resetConsoleLayout();
}


if(event.getActionCommand().equals("Indent Multi Line Tells"))
{

if(sharedVariables.indent == true)
{
	sharedVariables.indent=false;
	lineindent.setSelected(false);

}
else
{
	sharedVariables.indent=true;
	lineindent.setSelected(true);

}
}

if(event.getActionCommand().equals("Check Move Legality"))
{

if(sharedVariables.checkLegality == true)
{
	sharedVariables.checkLegality=false;
	checkLegality.setSelected(false);

}
else
{
	sharedVariables.checkLegality=true;
	checkLegality.setSelected(true);

}
}



if(event.getActionCommand().equals("Compact Channel Name List"))
{
try{
	if(sharedVariables.compactNameList == true)
	{
		sharedVariables.compactNameList = false;
		sharedVariables.nameListSize=90;
		compactNameList.setSelected(false);
	}
	else
	{

		sharedVariables.compactNameList = true;
		sharedVariables.nameListSize=65;
		compactNameList.setSelected(true);
	}

for(int iii=0; iii<sharedVariables.maxConsoleTabs; iii++)
{
	if(consoleSubframes[iii]!=null)
	{

	consoleSubframes[iii].overall.recreate(sharedVariables.consolesTabLayout[iii]);
	}
}//end for
}// end try
catch(Exception namebad){}
}
if(event.getActionCommand().equals("Channel Number On Left"))
{

if(sharedVariables.channelNumberLeft == true)
{
	sharedVariables.channelNumberLeft=false;
	channelNumberLeft.setSelected(false);

}
else
{
	sharedVariables.channelNumberLeft=true;
	channelNumberLeft.setSelected(true);

}
}


if(event.getActionCommand().equals("Show Console Menu"))
{
	if(sharedVariables.showConsoleMenu == true)
	{
		sharedVariables.showConsoleMenu = false;
		consolemenu.setSelected(false);
	}
	else
	{
		sharedVariables.showConsoleMenu = true;
		consolemenu.setSelected(true);

	}
	try {
	for(int bam=0; bam<sharedVariables.openConsoleCount; bam++)
		consoleSubframes[bam].consoleMenu.setVisible(sharedVariables.showConsoleMenu);
	}
	catch(Exception bal){}
}

if(event.getActionCommand().equals("Show User Button Titles"))
{
if(sharedVariables.showButtonTitle == true)
{
	sharedVariables.showButtonTitle=false;
	userbuttons.setSelected(false);

}
else
{
	sharedVariables.showButtonTitle=true;
	userbuttons.setSelected(true);

}

for(int a=0; a<sharedVariables.maxUserButtons; a++)
setButtonTitle(a);

}// end qsuggest popups






if(event.getActionCommand().equals("Qsuggest Popups"))
{
if(sharedVariables.showQsuggest == true)
{
	qsuggestPopup.setSelected(false);
	sharedVariables.showQsuggest=false;

}
else
{
	qsuggestPopup.setSelected(true);
	sharedVariables.showQsuggest=true;

}

}// end qsuggest popups




if(event.getActionCommand().equals("Lantern Manual"))
{
	mycreator.createWebFrame("http://www.adammr.com/lanternhelp/lantern-help.html");
}

if(event.getActionCommand().equals("Change Log"))
{
	mycreator.createWebFrame("http://www.adammr.com/changelog.htm");
}


if(event.getActionCommand().equals("ICC Information Help Files"))
{
	mycreator.createWebFrame("http://www.chessclub.com/help/info-list");
}

if(event.getActionCommand().equals("ICC Command Help Files"))
{
	mycreator.createWebFrame("http://www.chessclub.com/help/help-list");
}

if(event.getActionCommand().equals("Start AutoExam"))
{
for(int a=0; a<sharedVariables.maxGameTabs; a++)
{
	if(myboards[a]!=null)
	{
		if(sharedVariables.mygame[a].state == 2)
		myboards[a].setautoexamon();

	}
}


}
if(event.getActionCommand().equals("Load Winboard Engine"))
{
	boolean go = false;
	if(sharedVariables.engineOn == false)
	for(int a=0; a<sharedVariables.openBoardCount; a++)
	if(sharedVariables.mygame[a].state==sharedVariables.STATE_EXAMINING)
	{
		go=true;
		try {
			JFileChooser fc = new JFileChooser();
			 fc.setCurrentDirectory(new File("."));;

			 int returnVal = fc.showOpenDialog(this);

			 if (returnVal == JFileChooser.APPROVE_OPTION) {
			 sharedVariables.engineFile = fc.getSelectedFile();
			 	sharedVariables.uci=false;
			 	myboards[a].startEngine();

		    }



		}
		catch(Exception e){}
		break;
	}

	if(go== false && sharedVariables.engineOn == false)
		makeEngineWarning();

}


if(event.getActionCommand().equals("Set Application Background Color"))
{

try {
	JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Application Color", sharedVariables.MainBackColor);
 if(newColor != null)
 {
	 sharedVariables.MainBackColor=newColor;
	 sharedVariables.wallpaperImage=null;
	 repaint();
 }
}
catch(Exception e)
{}

}


if(event.getActionCommand().equals("Open Web"))
{
	mycreator.createWebFrame("http://www.google.com");
}




if(event.getActionCommand().equals("Open Pgn"))
{
		try {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));;

			 int returnVal = fc.showOpenDialog(this);

			 if (returnVal == JFileChooser.APPROVE_OPTION)
			 {
			 String myfile = fc.getSelectedFile().toString();
			 pgnLoader myLoader = new pgnLoader(myfile);
			 tableClass myTableClass = new tableClass();
			 myTableClass.createPgnListColumns();
			 myLoader.loadTable(myTableClass);
			 pgnFrame myPgnFrame = new pgnFrame(sharedVariables, queue, myTableClass, myLoader);
			 sharedVariables.desktop.add(myPgnFrame);
			 myPgnFrame.setSize(600,400);
			 myPgnFrame.setVisible(true);

		 }
	 }catch(Exception nine){}
}













if(event.getActionCommand().equals("Set Wallpaper"))
{
		try {
			JFileChooser fc = new JFileChooser();
			 int returnVal = fc.showOpenDialog(this);

			 if (returnVal == JFileChooser.APPROVE_OPTION)
			 {
			 sharedVariables.wallpaperFile = fc.getSelectedFile();
			 URL wallpaperURL = sharedVariables.wallpaperFile.toURL();
//applet
//sharedVariables.wallpaperImage=getImage(wallpaperURL);
// end applet
// stand alone
sharedVariables.wallpaperImage=Toolkit.getDefaultToolkit().getImage(wallpaperURL);
// end stand alone

}


			repaint();


			}// end try
		catch(Exception e){}

}

if(event.getActionCommand().equals("Stop Engine"))
{


	if(sharedVariables.engineOn == true)
	{




			myoutput outgoing = new myoutput();
			outgoing.data = "exit\n";

			sharedVariables.engineQueue.add(outgoing);

			myoutput outgoing2 = new myoutput();
			outgoing2.data = "quit\n";

			sharedVariables.engineQueue.add(outgoing2);
			sharedVariables.engineOn=false;
		}

}


if(event.getActionCommand().equals("Restart Engine"))
{
	if(sharedVariables.engineOn == false)
	for(int a=0; a<sharedVariables.openBoardCount; a++)
	if(sharedVariables.mygame[a].state==sharedVariables.STATE_EXAMINING)
	{
		try {

			 	myboards[a].startEngine();

		    }
		catch(Exception e){}
		break;
	}
}
if(event.getActionCommand().equals("Load UCI Engine"))
{
	boolean go = false;
	if(sharedVariables.engineOn == false)
	for(int a=0; a<sharedVariables.openBoardCount; a++)
	if(sharedVariables.mygame[a].state==sharedVariables.STATE_EXAMINING)
	{
	go=true;
	try {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));;

			 int returnVal = fc.showOpenDialog(this);

			 if (returnVal == JFileChooser.APPROVE_OPTION) {
			 sharedVariables.engineFile = fc.getSelectedFile();
			 	sharedVariables.uci=true;
			 	myboards[a].startEngine();

		    }



		}
		catch(Exception e){}
		break;
	}

	if(go== false && sharedVariables.engineOn == false)
		makeEngineWarning();

}

if(event.getActionCommand().equals("Activities Window"))
{
openActivities();
}

if(event.getActionCommand().equals("Seek Graph"))
{
try {
	if(seekGraph.isVisible() == false)
{seekGraph.setVisible(true);
seekGraph.setSize(600,500);
}

seekGraph.setSelected(true);
}catch(Exception dummyseek){}
}

if(event.getActionCommand().equals("Send iloggedon"))
{
	if(sharedVariables.iloggedon== false)
	{
		sharedVariables.iloggedon=true;
		iloggedon.setSelected(true);

	}
	else
	{
		sharedVariables.iloggedon=false;
		iloggedon.setSelected(false);
	}
}
if(event.getActionCommand().equals("Channel Notify Map"))
{
 String mess="Map of people on channel notify.\n\n";
for(int z=0; z<sharedVariables.channelNotifyList.size(); z++)
if(sharedVariables.channelNotifyList.get(z).nameList.size()>0)
{
mess+="\n#" + sharedVariables.channelNotifyList.get(z).channel + " ";
for(int x=0; x < sharedVariables.channelNotifyList.get(z).nameList.size(); x++)
mess+=sharedVariables.channelNotifyList.get(z).nameList.get(x) + " ";
}
Popup mypopper = new Popup(this, false, mess);
mypopper.setSize(600,500);
mypopper.setVisible(true);


}

if(event.getActionCommand().equals("Channel Notify Online"))
{
  
String mess = sharedVariables.getChannelNotifyOnline();
mess += sharedVariables.getConnectNotifyOnline();
Popup mypopper = new Popup(this, false, mess);
mypopper.setSize(600,500);
mypopper.setVisible(true);

}

if(event.getActionCommand().equals("Channel Map"))
{

String mymap="Map of channels, shouts and sshouts moved to tabs.\n\n";

for(int a=1; a< sharedVariables.maxConsoleTabs; a++)
{
mymap+="C" + a +  ": ";
for(int aa=0; aa<500; aa++)
if(sharedVariables.console[a][aa]==1)
{
	mymap+=aa;

	if(sharedVariables.mainAlso[aa] == true)
	mymap+="m";

	mymap+= " ";
}

if(sharedVariables.shoutRouter.shoutsConsole == a)
	mymap += "Shouts ";
if(sharedVariables.shoutRouter.sshoutsConsole == a)
	mymap += "S-Shouts ";
mymap+="\n";
}

Popup mypopper = new Popup(this, false, mymap);
mypopper.setSize(600,500);
mypopper.setVisible(true);


}

if(event.getActionCommand().equals("Rotate Away Message"))
{
if(sharedVariables.rotateAways == false)
{scriptLoader loadScripts = new  scriptLoader();
sharedVariables.lanternAways.clear();
loadScripts.loadScript(sharedVariables.lanternAways, "lantern_away.txt");
if(sharedVariables.lanternAways.size()>0)
{
rotateaways.setSelected(true);
sharedVariables.rotateAways=true;
}// size > 0
else
{
String mes="lantern_away.txt not found or has nothing in it.  Create a file called lantern_away.txt and put away messages in it till you run out of ideas, then reselect this option";
Popup mypopper = new Popup(this, false, mes);
mypopper.setVisible(true);
rotateaways.setSelected(false);
}// else size not > 0

}// if rotateAways == false
else
{
rotateaways.setSelected(false);
sharedVariables.rotateAways=false;

}// if rotate aways true

}

if(event.getActionCommand().equals("Stop AutoExam"))
{

 sharedVariables.autoexam=0;

}

if(event.getActionCommand().equals("Set AutoExam Speed"))
{

autoExamDialog frame = new autoExamDialog((JFrame) this, true, sharedVariables);
	frame.setSize(550,120);
	frame.setVisible(true);


}
if(event.getActionCommand().equals("Reconnect to Queen"))
{

 try {
	 sharedVariables.myServer="ICC";
 sharedVariables.chessclubIP="207.99.83.231";
 sharedVariables.doreconnect=true;
 if(myConnection == null)
 myConnection = new connectionDialog(this, sharedVariables, queue, false);
 else if(!myConnection.isVisible())
 myConnection = new connectionDialog(this, sharedVariables, queue, false);

myConnection.setVisible(true);
}catch(Exception conn){}

}

if(event.getActionCommand().equals("Reconnect to ICC"))
{

try {
	sharedVariables.myServer="ICC";
 sharedVariables.chessclubIP = "207.99.83.228";
 sharedVariables.doreconnect=true;
if(myConnection == null)
 myConnection = new connectionDialog(this, sharedVariables, queue, false);
 else if(!myConnection.isVisible())
 myConnection = new connectionDialog(this, sharedVariables, queue, false);
myConnection.setVisible(true);
}catch(Exception conn){}
}

if(event.getActionCommand().equals("Reconnect to FICS"))
{

 sharedVariables.myServer="FICS";
 sharedVariables.doreconnect=true;

}

if(event.getActionCommand().equals("Save Settings"))
{
		sharedVariables.activitiesOpen = false;
		if(myfirstlist!=null)
		if(myfirstlist.isVisible())
			sharedVariables.activitiesOpen = true;

mysettings.saveNow(myboards, consoleSubframes, sharedVariables);
	mineScores.saveNow(sharedVariables);

		sharedVariables.activitiesOpen = false;// it gets set to true on close and here, needs to be false so its checked on close

}

if(event.getActionCommand().equals("ToolBox"))
{
toolboxDialog mybox = new toolboxDialog(this, false, queue, sharedVariables);
mybox.setSize(500,450);
mybox.setLocation(200,250);
mybox.setVisible(true);
}
if(event.getActionCommand().equals("Customize Toolbar"))
{
userButtonsDialog mydialog = new userButtonsDialog((JFrame) this, sharedVariables);
mydialog.setSize(400,400);
mydialog.setVisible(true);
}

if(event.getActionCommand().equals("Toolbar"))
{
 if(sharedVariables.toolbarVisible == true)
 {
  toolbarvisible.setSelected(false);
  sharedVariables.toolbarVisible = false;
  toolBar.setVisible(false);
 }
 else
 {
  toolbarvisible.setSelected(true);
    sharedVariables.toolbarVisible = true;
  toolBar.setVisible(true);
 }
}








if(event.getActionCommand().equals("New Console"))
{
createChannelConsoleDialog frame = new createChannelConsoleDialog((JFrame) this, true, sharedVariables, mycreator, consoleSubframes);
	frame.setSize(550,120);
	frame.setVisible(true);


}
if(event.getActionCommand().equals("Customize Tab"))
{
int hasfocus=-1;
for(int nn=0; nn<sharedVariables.openConsoleCount; nn++)
	if(consoleSubframes[nn]!=null)
	if(consoleSubframes[nn].isSelected())
		hasfocus=nn;

if(hasfocus == -1)
{
String swarning = "First click or select a console window, and change tab to one to customize.";
Popup pframe = new Popup((JFrame) this, true, swarning);
pframe.setVisible(true);
return;
}

// new idea we know the console lets find the tab
hasfocus=sharedVariables.looking[hasfocus];

String consoleWithFocus = "No console has focus";
if(hasfocus> 0)
{
	customizeChannelsDialog frame = new customizeChannelsDialog((JFrame) this, false, hasfocus, sharedVariables, consoleSubframes);

}
else
{
String swarning = "The currently selected window is looking at the main console tab, this cant be custimized, click C1, C2, etc first.";
Popup pframe = new Popup((JFrame) this, true, swarning);
pframe.setVisible(true);
return;
}
}
if(event.getActionCommand().equals("New Detached Chat Console"))
{
boolean makeChatter = false;
if(consoleChatframes[11]== null)
makeChatter=true;
else if(!consoleChatframes[11].isVisible())
makeChatter=true;
if(makeChatter == true)
{sharedVariables.chatFrame=11;
consoleChatframes[11] =  new chatframe(sharedVariables, consoles, queue, mycreator.myDocWriter);
consoleChatframes[11].setVisible(true);
}
else
{
if(consoleChatframes[10]== null)
makeChatter=true;
else if(!consoleChatframes[10].isVisible())
makeChatter=true;
if(makeChatter == true)
{
	sharedVariables.chatFrame=10;
consoleChatframes[10] =  new chatframe(sharedVariables, consoles, queue, mycreator.myDocWriter);
consoleChatframes[10].setVisible(true);
}
else
{Popup mypopper= new Popup(this, false, "Can only have two detached chat frames open now");
mypopper.setVisible(true);
}
}
}

if(event.getActionCommand().equals("New Chat Console"))
{
mycreator.restoreConsoleFrame();
}
if(event.getActionCommand().equals("New Board"))
{

mycreator.createGameFrame();

}
if(event.getActionCommand().equals("Cascade"))
{
	int x=160;
	int y=120;
	int width=400;
	int height=300;
	int dif=30;
	int count=0;
try {

	for(int a=0; a<sharedVariables.openConsoleCount; a++)
	if(consoleSubframes[a]!=null)
	if(consoleSubframes[a].isVisible())
	{
		consoleSubframes[a].setSize(width,height);
		consoleSubframes[a].setLocation(x + count * dif, y + count * dif);
		consoleSubframes[a].setSelected(true);
		count++;
	}
	for(int a=0; a<sharedVariables.openBoardCount; a++)
	if(myboards[a]!=null)
	if(myboards[a].isVisible())
	{
if(sharedVariables.useTopGames == false)
{		myboards[a].setSize(width,height);
		myboards[a].setLocation(x + count * dif, y + count * dif);
		myboards[a].setSelected(true);
}
else
{
 if(myboards[a].topGame != null)
 {
  		myboards[a].topGame.setSize(width,height);
		myboards[a].topGame.setLocation(x + count * dif, y + count * dif);


 }
}
		 count++;
	}

	if(myfirstlist!=null)
	if(myfirstlist.isVisible())
	{

		myfirstlist.setSize(width,height);
		myfirstlist.setLocation(x + count * dif, y + count * dif);
	//	myfirstlist.setSelected(true);

	}
}
catch(Exception d){}
}




if(event.getActionCommand().equals("Make Boards Always On Top"))
{
/* SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
	int x=160;
	int y=120;
	int width=400;
	int height=300;
	int dif=30;
	int count=0;


try {

	for(int a=0; a<myboards.length; a++)
	if(myboards[a]!=null)
{

            if(sharedVariables.useTopGames == true)
            {
               myboards[a].switchFrame(false);

              myboards[a].setSize(width,height);
            		myboards[a].setLocation(x + count * dif, y + count * dif);
            		myboards[a].setSelected(true);

             myboards[a].topGame.setAlwaysOnTop(false);
            }
            else
            {
             if(myboards[a].topGame != null)
             {
                          myboards[a].switchFrame(true);

               		myboards[a].topGame.setSize(width,height);
            		myboards[a].topGame.setLocation(x + count * dif, y + count * dif);

             myboards[a].topGame.setAlwaysOnTop(true);
              myboards[a].setVisible(false);
             }
             }// end else
		 count++;

}// if not null
else
break;



}
catch(Exception d){}
 if(sharedVariables.useTopGames == true)
sharedVariables.useTopGames = false;
else
sharedVariables.useTopGames = true;
                           } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });

  SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                            for(int b=0; b< myboards.length; b++)
                            if(myboards[b]!=null && sharedVariables.useTopGames == true)
                            myboards[b].setVisible(false);
                           } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });
*/

//lantern_board_on_top.txt

boolean ontop = getOnTopSetting();

FileWrite mywriter = new FileWrite();
if(ontop == false)
{
    String mess = "Next time you start the program, boards will be on top windows.";
  Popup mypopper = new Popup(this, true, mess);
  mypopper.setVisible(true);
  mywriter.write("true\r\n", "lantern_board_on_top.txt");
}
else
{
    String mess = "Next time you start the program, boards will NOT be on top windows.";
   Popup mypopper = new Popup(this, true, mess);
   mypopper.setVisible(true);
    mywriter.write("false\r\n", "lantern_board_on_top.txt");
}

}






if(event.getActionCommand().equals("Get a Game"))
{

seekGameDialog myseeker = new seekGameDialog(this, false, sharedVariables, queue);
int defaultWidth = 425;
int defaultHeight = 260;
myseeker.setSize(defaultWidth,defaultHeight);

try {
	Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        int screenW = dim.width;
        int screenH = dim.height;
      int px = (int) ((screenW - defaultWidth) / 2);
      if(px < 50)
       px=50;
      int py = (int) ((screenH - defaultHeight) / 2);
      if(py < 50)
       py=50;


      myseeker.setLocation(px, py);
}
catch(Exception centerError){}

myseeker.setTitle("Get a Game");

myseeker.setVisible(true);

}

if(event.getActionCommand().equals("Log Pgn"))
{
	if(sharedVariables.pgnLogging == true)
	 {
		 sharedVariables.pgnLogging = false;
	 	pgnlogging.setSelected(false);
	}
	 else
	 {
		 sharedVariables.pgnLogging = true;
		 pgnlogging.setSelected(true);
 	}


}

if(event.getActionCommand().equals("1:1"))
{
	sharedVariables.aspect=0;
	aspect0.setSelected(true);
	aspect1.setSelected(false);
	aspect2.setSelected(false);
	aspect3.setSelected(false);
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();

}

if(event.getActionCommand().equals("5:4"))
{
	sharedVariables.aspect=1;
	aspect0.setSelected(false);
	aspect1.setSelected(true);
	aspect2.setSelected(false);
	aspect3.setSelected(false);
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();

}
if(event.getActionCommand().equals("4:3"))
{
	sharedVariables.aspect=2;
	aspect0.setSelected(false);
	aspect1.setSelected(false);
	aspect2.setSelected(true);
	aspect3.setSelected(false);
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();

}
if(event.getActionCommand().equals("3:2"))
{
	sharedVariables.aspect=3;
	aspect0.setSelected(false);
	aspect1.setSelected(false);
	aspect2.setSelected(false);
	aspect3.setSelected(true);
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();

}

if(event.getActionCommand().equals("Hide Board Console"))
{
	sharedVariables.boardConsoleType=0;
	redrawBoard(sharedVariables.boardConsoleType);
}
if(event.getActionCommand().equals("Compact Board Console"))
{
	sharedVariables.boardConsoleType=1;
	redrawBoard(sharedVariables.boardConsoleType);
}
if(event.getActionCommand().equals("Normal Board Console"))
{
	sharedVariables.boardConsoleType=2;
	redrawBoard(sharedVariables.boardConsoleType);
}
if(event.getActionCommand().equals("Larger Board Console"))
{
	sharedVariables.boardConsoleType=3;
	redrawBoard(sharedVariables.boardConsoleType);
}
if(event.getActionCommand().equals("Console On Side"))
{
	if(sharedVariables.sideways == true)
	{
		sharedVariables.sideways=false;
		sidewaysconsole.setSelected(false);
	}
	else
	{
		sharedVariables.sideways=true;
		sidewaysconsole.setSelected(true);
	}
	redrawBoard(sharedVariables.boardConsoleType);
}



if(event.getActionCommand().equals("Default Board"))
{
sharedVariables.boardType = 0;
sharedVariables.lightcolor=sharedVariables.preselectBoards.light[0];
sharedVariables.darkcolor=sharedVariables.preselectBoards.dark[0];

setBoard(0);

}

if(event.getActionCommand().equals("Tan Board"))
{
sharedVariables.boardType = 0;
sharedVariables.lightcolor=sharedVariables.preselectBoards.light[1];
sharedVariables.darkcolor=sharedVariables.preselectBoards.dark[1];

setBoard(0);

}

if(event.getActionCommand().equals("Gray Color Board"))
{
sharedVariables.boardType = 0;
sharedVariables.lightcolor=sharedVariables.preselectBoards.light[2];
sharedVariables.darkcolor=sharedVariables.preselectBoards.dark[2];

setBoard(0);

}

if(event.getActionCommand().equals("Blitzin Green Board"))
{
sharedVariables.boardType = 0;
sharedVariables.lightcolor=sharedVariables.preselectBoards.light[3];
sharedVariables.darkcolor=sharedVariables.preselectBoards.dark[3];

 setBoard(0);

}

if(event.getActionCommand().equals("Board Clock Background Color"))
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Board Clock Background Color", sharedVariables.onMoveBoardBackgroundColor);
 if(newColor != null)
 sharedVariables.onMoveBoardBackgroundColor=newColor;
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].repaint();


}


if(event.getActionCommand().equals("Board Background Color"))
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Board Background Color", sharedVariables.boardBackgroundColor);
 if(newColor != null)
 sharedVariables.boardBackgroundColor=newColor;
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].repaint();


}
if(event.getActionCommand().equals("Titles In Channel Color"))
{
JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Set Titles In Channel Color", sharedVariables.channelTitlesColor);
 if(newColor != null)
 sharedVariables.channelTitlesColor=newColor;

}

if(event.getActionCommand().equals("Channel Name Color"))
{
JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Set Channel Name Color", sharedVariables.qtellChannelNumberColor);
 if(newColor != null)
 sharedVariables.qtellChannelNumberColor=newColor;

}

if(event.getActionCommand().equals("Brighter Channel Name Color"))
{
String mycolstring="";
float[] hsbValues = new float[3];
Color col2=sharedVariables.qtellChannelNumberColor;
hsbValues = Color.RGBtoHSB(col2.getRed(), col2.getGreen(),col2.getBlue(), hsbValues);
float hue, saturation, brightness;
hue = hsbValues[0];
saturation = hsbValues[1];
brightness = hsbValues[2];
mycolstring="color values were hue= " + hue + " and saturation= " + saturation + " and brightness=" + brightness + " and red=" + col2.getRed() + " and blue =" + col2.getGreen() + " and green=" + col2.getBlue() + " ";


	sharedVariables.qtellChannelNumberColor=sharedVariables.qtellChannelNumberColor.brighter();

col2=sharedVariables.qtellChannelNumberColor;
hsbValues = Color.RGBtoHSB(col2.getRed(), col2.getGreen(),col2.getBlue(), hsbValues);

hue = hsbValues[0];
saturation = hsbValues[1];
brightness = hsbValues[2];
mycolstring=mycolstring + " color values now hue= " + hue + " and saturation= " + saturation + " and brightness=" + brightness +  " and red=" + col2.getRed() + " and blue =" + col2.getGreen() + " and green=" + col2.getBlue() + " ";
Popup mypopper= new Popup(this, false, mycolstring);
mypopper.setVisible(true);


}

	if(event.getActionCommand().equals("Darker Channel Name Color"))
{

String mycolstring="";
float[] hsbValues = new float[3];
Color col2=sharedVariables.qtellChannelNumberColor;
hsbValues = Color.RGBtoHSB(col2.getRed(), col2.getGreen(),col2.getBlue(), hsbValues);
float hue, saturation, brightness;
hue = hsbValues[0];
saturation = hsbValues[1];
brightness = hsbValues[2];
mycolstring="color values were hue= " + hue + " and saturation= " + saturation + " and brightness=" + brightness + " and red=" + col2.getRed() + " and blue =" + col2.getGreen() + " and green=" + col2.getBlue() + " ";


	sharedVariables.qtellChannelNumberColor=sharedVariables.qtellChannelNumberColor.darker();

col2=sharedVariables.qtellChannelNumberColor;
hsbValues = Color.RGBtoHSB(col2.getRed(), col2.getGreen(),col2.getBlue(), hsbValues);

hue = hsbValues[0];
saturation = hsbValues[1];
brightness = hsbValues[2];
mycolstring=mycolstring + " color values now hue= " + hue + " and saturation= " + saturation + " and brightness=" + brightness +  " and red=" + col2.getRed() + " and blue =" + col2.getGreen() + " and green=" + col2.getBlue() + " ";

Popup mypopper= new Popup(this, false, mycolstring);
mypopper.setVisible(true);

}

if(event.getActionCommand().equals("PTell Name Color"))
{
JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Tell Name Color", sharedVariables.tellNameColor);
 if(newColor != null)
 sharedVariables.tellNameColor=newColor;

}
if(event.getActionCommand().equals("Names List Foreground Color"))
{
JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Names List Foreground Color", sharedVariables.nameForegroundColor);
 if(newColor != null)
 {
	 sharedVariables.nameForegroundColor=newColor;
	  myfirstlist.theChannelList.setForeground(sharedVariables.nameForegroundColor);
	   myfirstlist.theChannelList2.setForeground(sharedVariables.nameForegroundColor);
	    myfirstlist.theChannelList3.setForeground(sharedVariables.nameForegroundColor);
	 for(int c=0; c<sharedVariables.maxConsoleTabs; c++)
	 {
		 if(consoleSubframes[c]!=null)
		 {
			 consoleSubframes[c].myNameList.setForeground(newColor);

		 }
	 }

 }

}
if(event.getActionCommand().equals("Names List Background Color"))
{
JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Names List Background Color", sharedVariables.nameBackgroundColor);
 if(newColor != null)
 {
	 sharedVariables.nameBackgroundColor=newColor;

myfirstlist.theChannelList.setBackground(sharedVariables.nameBackgroundColor);
myfirstlist.theChannelList2.setBackground(sharedVariables.nameBackgroundColor);
myfirstlist.theChannelList3.setBackground(sharedVariables.nameBackgroundColor);

	 for(int c=0; c<sharedVariables.maxConsoleTabs; c++)
	 {
		 if(consoleSubframes[c]!=null)
		 {
			 consoleSubframes[c].myNameList.setBackground(newColor);

		 }
	 }// end for

 }//end if not null

}// end if name list background

 if(event.getActionCommand().equals("Show My Recent Games"))
 {
     String actionmess="History\n";
     if(sharedVariables.myServer.equals("ICC"))
     actionmess="`c0`" + actionmess;

     myoutput data = new myoutput();
     data.data=actionmess;
    queue.add(data);

 }
  if(event.getActionCommand().equals("Show My Game Library"))
 {
      String actionmess="Liblist\n";
     if(sharedVariables.myServer.equals("ICC"))
     actionmess="`c0`" + actionmess;

     myoutput data = new myoutput();
     data.data=actionmess;
    queue.add(data);

 }

  if(event.getActionCommand().equals("Show My Adjourned Games"))
 {
      String actionmess="Stored\n";
     if(sharedVariables.myServer.equals("ICC"))
     actionmess="`c0`" + actionmess;

     myoutput data = new myoutput();
     data.data=actionmess;
    queue.add(data);

 }

  if(event.getActionCommand().equals("Show My Profile and Ratings"))
 {
      String actionmess="Finger\n";
     if(sharedVariables.myServer.equals("ICC"))
     actionmess="`c0`" + actionmess;

     myoutput data = new myoutput();
     data.data=actionmess;
    queue.add(data);

 }

  if(event.getActionCommand().equals("Enter Examination Mode"))
 {
      String actionmess="Examine\n";
     if(sharedVariables.myServer.equals("ICC"))
     actionmess="`c0`" + actionmess;

     myoutput data = new myoutput();
     data.data=actionmess;
    queue.add(data);

 }

   if(event.getActionCommand().equals("Examine My Last Game"))
 {
      String actionmess="Examine -1\n";
     if(sharedVariables.myServer.equals("ICC"))
     actionmess="`c0`" + actionmess;

     myoutput data = new myoutput();
     data.data=actionmess;
    queue.add(data);

 }


  if(event.getActionCommand().equals("Observe High Rated Game"))
 {
      String actionmess="Observe *\n";
     if(sharedVariables.myServer.equals("ICC"))
     actionmess="`c0`" + actionmess;

     myoutput data = new myoutput();
     data.data=actionmess;
    queue.add(data);

 }

  if(event.getActionCommand().equals("Observe High Rated 5-Minute Game"))
 {
      String actionmess="Observe *f\n";
     if(sharedVariables.myServer.equals("ICC"))
     actionmess="`c0`" + actionmess;

     myoutput data = new myoutput();
     data.data=actionmess;
    queue.add(data);

 }

  if(event.getActionCommand().equals("Observe High Rated 15-Minute Game"))
 {
      String actionmess="Observe *P\n";
     if(sharedVariables.myServer.equals("ICC"))
     actionmess="`c0`" + actionmess;

     myoutput data = new myoutput();
     data.data=actionmess;
    queue.add(data);

 }
  if(event.getActionCommand().equals("Show Relay Schedule"))
 {

  openUrl("http://www.chessclub.com/activities/relays.html");
 }

  if(event.getActionCommand().equals("Show Game of the Week Index"))
 {

  openUrl("http://www.chessclub.com/chessfm/index/gotw/index.html");
 }
  if(event.getActionCommand().equals("Open ChessFM"))
 {

  openUrl("http://www.chessclub.com/chessfm/");
 }


 if(event.getActionCommand().equals("Names List Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.nameListFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {
				sharedVariables.nameListFont=fnt;
				myfirstlist.theChannelList.setFont(sharedVariables.nameListFont);
				myfirstlist.theChannelList2.setFont(sharedVariables.nameListFont);
				myfirstlist.theChannelList3.setFont(sharedVariables.nameListFont);

	 for(int c=0; c<sharedVariables.maxConsoleTabs; c++)
	 {
		 if(consoleSubframes[c]!=null)
		 {
			 consoleSubframes[c].myNameList.setFont(fnt);

		 }
	 }// end for



		}

}

if(event.getActionCommand().equals("Chat Timestamp Color"))
{
JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Set Timestamp Color", sharedVariables.chatTimestampColor);
 if(newColor != null)
 sharedVariables.chatTimestampColor=newColor;

}
if(event.getActionCommand().equals("Board Foreground Color"))
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Board Foreground Color", sharedVariables.boardForegroundColor);
 if(newColor != null)
 sharedVariables.boardForegroundColor=newColor;
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].repaint();


}

if(event.getActionCommand().equals("Clock Foreground Color"))
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Clock Foreground Color", sharedVariables.clockForegroundColor);
 if(newColor != null)
 sharedVariables.clockForegroundColor=newColor;
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].repaint();


}





if(event.getActionCommand().equals("Solid Color Board"))
{
sharedVariables.boardType=0;
setBoard(sharedVariables.boardType);
}

if(event.getActionCommand().equals("Pale Wood"))
{
sharedVariables.boardType=1;
setBoard(sharedVariables.boardType);

}

if(event.getActionCommand().equals("Light Wood"))
{
sharedVariables.boardType=2;
setBoard(sharedVariables.boardType);

}

if(event.getActionCommand().equals("Dark Wood"))
{
sharedVariables.boardType=3;
setBoard(sharedVariables.boardType);

}

if(event.getActionCommand().equals("Gray Marble"))
{
sharedVariables.boardType=4;
setBoard(sharedVariables.boardType);

}

if(event.getActionCommand().equals("Red Marble"))
{
sharedVariables.boardType=5;
setBoard(sharedVariables.boardType);

}

if(event.getActionCommand().equals("Crampled Paper"))
{
sharedVariables.boardType=6;
setBoard(sharedVariables.boardType);

}

if(event.getActionCommand().equals("Winter"))
{
sharedVariables.boardType=7;
setBoard(sharedVariables.boardType);

}
if(event.getActionCommand().equals("Olive Board"))
{
sharedVariables.boardType=8;
setBoard(sharedVariables.boardType);

}
if(event.getActionCommand().equals("Cherry Board"))
{
sharedVariables.boardType=9;
setBoard(sharedVariables.boardType);

}
if(event.getActionCommand().equals("Purple Board"))
{
sharedVariables.boardType=10;
setBoard(sharedVariables.boardType);

}


if(event.getActionCommand().equals("Dyche1"))
{
sharedVariables.pieceType=0;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Dyche2"))
{
sharedVariables.pieceType=1;
setPieces(sharedVariables.pieceType);

}if(event.getActionCommand().equals("Dyche3"))
{
sharedVariables.pieceType=2;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Bookup"))
{
sharedVariables.pieceType=3;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Xboard"))
{
sharedVariables.pieceType=4;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Alpha"))
{
sharedVariables.pieceType=5;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Spatial"))
{
sharedVariables.pieceType=6;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Harlequin"))
{
sharedVariables.pieceType=7;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Berlin"))
{
sharedVariables.pieceType=8;
setPieces(sharedVariables.pieceType);

}


if(event.getActionCommand().equals("Eboard Classic"))
{
sharedVariables.pieceType=9;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Molten Good"))
{
sharedVariables.pieceType=10;
setPieces(sharedVariables.pieceType);

}

if(event.getActionCommand().equals("Molten Evil"))
{
sharedVariables.pieceType=11;
setPieces(sharedVariables.pieceType);

}

if(event.getActionCommand().equals("Liebeskind"))
{
sharedVariables.pieceType=12;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Eyes"))
{
sharedVariables.pieceType=13;
setPieces(sharedVariables.pieceType);

}if(event.getActionCommand().equals("Fantasy"))
{
sharedVariables.pieceType=14;
setPieces(sharedVariables.pieceType);

}

if(event.getActionCommand().equals("Adventure"))
{
sharedVariables.pieceType=18;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Maya"))
{
sharedVariables.pieceType=19;
setPieces(sharedVariables.pieceType);

}if(event.getActionCommand().equals("Medieval"))
{
sharedVariables.pieceType=20;
setPieces(sharedVariables.pieceType);

}


if(event.getActionCommand().equals("Monge Mix"))
{
sharedVariables.pieceType=21;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("About Monge Pieces"))
{

  String warning = "The Monge chess pieces are authored by Maurizio Monge, at this time, three of the six sets are currently in Lantern and they are under the LGPL (library GPL) license at the time of this writing. \n\n Virtually all the piece sets in Lantern come from the Jin Chess, and except in case's like the Monge pieces, where i know the license, I offer no more rights than Jin does.  LGPL allows you to resuse the pieces in your own application if your a developer. So unzip the lantern.jar to get at the pieces.  A warning though, the \\setName\\64\\ folder is a general folder for when i want pieces i can resize, and the monge pieces dont actually come in the 64 size.\n\n  The monge mix is a mix of pieces from the Fantasy and Spatial set i've put together.";
 Popup mypopup = new Popup(this, false, warning);
 mypopup.setSize(600,500);
 mypopup.setVisible(true);
}
if(event.getActionCommand().equals("Random Pieces"))
{
sharedVariables.pieceType=22;
setPieces(sharedVariables.pieceType);

}



if(event.getActionCommand().equals("Line"))
{
sharedVariables.pieceType=15;
setPieces(sharedVariables.pieceType);

}if(event.getActionCommand().equals("Motif"))
{
sharedVariables.pieceType=16;
setPieces(sharedVariables.pieceType);

}
if(event.getActionCommand().equals("Utrecht"))
{
sharedVariables.pieceType=17;
setPieces(sharedVariables.pieceType);

}

if(event.getActionCommand().equals("Light Square Color"))
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Light Square Color", sharedVariables.lightcolor);
 if(newColor != null)
 sharedVariables.lightcolor=newColor;
 for(int a=0; a< sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();


}
if(event.getActionCommand().equals("Dark Square Color"))
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Dark Square Color", sharedVariables.darkcolor);
 if(newColor != null)
 sharedVariables.darkcolor=newColor;
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();

}


if(event.getActionCommand().equals("Unvisited/Visited"))// active tab
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Unvisited/Visited Color", sharedVariables.tabBackground2);
 if(newColor != null)
 sharedVariables.tabBackground2=newColor;


}


if(event.getActionCommand().equals("Unvisited"))// active tab
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Unvisited Color", sharedVariables.newInfoTabBackground);
 if(newColor != null)
 sharedVariables.newInfoTabBackground=newColor;


 for(int a=0; a<sharedVariables.openBoardCount; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 for(int aa=0; aa<sharedVariables.openBoardCount; aa++)
 {
	 newColor = myboards[a].myconsolepanel.channelTabs[aa].getBackground();

	if( newColor.getRGB() != sharedVariables.tabBackground.getRGB())
	myboards[a].myconsolepanel.channelTabs[aa].setBackground(sharedVariables.newInfoTabBackground);
 }
// now update consoles
 for(int a=0; a<sharedVariables.openConsoleCount; a++)
 if(consoleSubframes[a]!=null)
 if(consoleSubframes[a].isVisible() == true)
 for(int aa=0; aa<sharedVariables.openConsoleCount; aa++)
 {
	 newColor = consoleSubframes[a].channelTabs[aa].getBackground();
	 if( newColor.getRGB() != sharedVariables.tabBackground.getRGB())
	consoleSubframes[a].channelTabs[aa].setBackground(sharedVariables.newInfoTabBackground);
 }

}



if(event.getActionCommand().equals("Tab I'm On Background"))// active tab
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Tab I'm On Color", sharedVariables.tabImOnBackground);
 if(newColor != null)
 sharedVariables.tabImOnBackground=newColor;



 for(int a=0; a<sharedVariables.openConsoleCount; a++)
 if(consoleSubframes[a]!=null)
 if(consoleSubframes[a].isVisible() == true)
	consoleSubframes[a].channelTabs[sharedVariables.looking[a]].setBackground(sharedVariables.tabImOnBackground);

}






if(event.getActionCommand().equals("Visited"))// active tab
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Visited Color", sharedVariables.tabBackground);
 if(newColor != null)
 sharedVariables.tabBackground=newColor;


 for(int a=0; a<sharedVariables.openBoardCount; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 for(int aa=0; aa<sharedVariables.openBoardCount; aa++)
 {
	 newColor = myboards[a].myconsolepanel.channelTabs[aa].getBackground();
	 if(!newColor.equals(sharedVariables.newInfoTabBackground))
	myboards[a].myconsolepanel.channelTabs[aa].setBackground(sharedVariables.tabBackground);
 }
// now update consoles
 for(int a=0; a<sharedVariables.openConsoleCount; a++)
 if(consoleSubframes[a]!=null)
 if(consoleSubframes[a].isVisible() == true)
 for(int aa=0; aa<sharedVariables.maxConsoleTabs; aa++)
 {
	 newColor = consoleSubframes[a].channelTabs[aa].getBackground();
	 if(!newColor.equals(sharedVariables.newInfoTabBackground))
	consoleSubframes[a].channelTabs[aa].setBackground(sharedVariables.tabBackground);
 }

}



if(event.getActionCommand().equals("Tab Border"))// active tab
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Tab Border Color", sharedVariables.tabBorderColor);
 if(newColor != null)
 sharedVariables.tabBorderColor=newColor;
 for(int a=0; a<sharedVariables.openBoardCount; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 myboards[a].myconsolepanel.channelTabs[a].repaint();


// now update consoles
 for(int a=0; a<sharedVariables.openConsoleCount; a++)
 if(consoleSubframes[a]!=null)
 if(consoleSubframes[a].isVisible() == true)
 for(int aa=0; aa<sharedVariables.maxConsoleTabs; aa++)
 {
 consoleSubframes[a].channelTabs[aa].repaint();
}

}

if(event.getActionCommand().equals("Input Command Color"))//Input Command Color
{
JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Input Command Color", sharedVariables.activeTabForeground);
 if(newColor != null)
 sharedVariables.inputCommandColor=newColor;
}
if(event.getActionCommand().equals("Input Chat Color"))//Input Chat Color
{
JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Input Chat Color", sharedVariables.activeTabForeground);
 if(newColor != null)
 sharedVariables.inputChatColor=newColor;
}

if(event.getActionCommand().equals("Active"))// active tab
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Active Foreground Color", sharedVariables.activeTabForeground);
 if(newColor != null)
 sharedVariables.activeTabForeground=newColor;
 for(int a=0; a<sharedVariables.openBoardCount; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 myboards[a].myconsolepanel.setActiveTabForeground(sharedVariables.gamelooking[a]);


// now update consoles
 for(int a=0; a<sharedVariables.openConsoleCount; a++)
 if(consoleSubframes[a]!=null)
 if(consoleSubframes[a].isVisible() == true)
 consoleSubframes[a].setActiveTabForeground(sharedVariables.looking[a]);

}

if(event.getActionCommand().equals("Non Active"))// active tab
{

JDialog frame = new JDialog();
Color newColor = JColorChooser.showDialog(frame, "Non Active Foreground Color", sharedVariables.passiveTabForeground);
 if(newColor != null)
 sharedVariables.passiveTabForeground=newColor;
 for(int a=0; a<sharedVariables.openBoardCount; a++)
 if(myboards[a]!=null)
 if(myboards[a].isVisible() == true)
 myboards[a].myconsolepanel.setActiveTabForeground(sharedVariables.gamelooking[a]);


// now update consoles
 for(int a=0; a<sharedVariables.openConsoleCount; a++)
 if(consoleSubframes[a]!=null)
 if(consoleSubframes[a].isVisible() == true)
 consoleSubframes[a].setActiveTabForeground(sharedVariables.looking[a]);

}

if(event.getActionCommand().equals("Highlight Moves"))
{
	if(sharedVariables.highlightMoves == false)
	{
		highlight.setSelected(true);
		sharedVariables.highlightMoves = true;
	}
	else
	{
		highlight.setSelected(false);
		sharedVariables.highlightMoves = false;
	}
}

if(event.getActionCommand().equals("Material Count"))
{
	if(sharedVariables.showMaterialCount == false)
	{
		materialCount.setSelected(true);
		sharedVariables.showMaterialCount = true;
	}
	else
	{
		materialCount.setSelected(false);
		sharedVariables.showMaterialCount = false;
	}
}

if(event.getActionCommand().equals("Show Ratings on Board When Playing"))
{
	if(sharedVariables.showRatings == false)
	{
		showRatings.setSelected(true);
		sharedVariables.showRatings = true;
	}
	else
	{
		showRatings.setSelected(false);
		sharedVariables.showRatings = false;
	}
}

if(event.getActionCommand().equals("Show Flags"))
{
	if(sharedVariables.showFlags == false)
	{
		showFlags.setSelected(true);
		sharedVariables.showFlags = true;
	}
	else
	{
		showFlags.setSelected(false);
		sharedVariables.showFlags = false;
	}

String swarning = "This setting will update on board as soon as the next game starts.";
Popup pframe = new Popup((JFrame) this, true, swarning);
pframe.setVisible(true);

}

if(event.getActionCommand().equals("Use Light Square as Board Background"))
{
	if(sharedVariables.useLightBackground == false)
	{
		useLightBackground.setSelected(true);
		sharedVariables.useLightBackground = true;
	}
	else
	{
		useLightBackground.setSelected(false);
		sharedVariables.useLightBackground = false;
	}

}


if(event.getActionCommand().equals("Tabs Only"))
{
	if(sharedVariables.tabsOnly == false)
	{
		tabbing.setSelected(true);
		sharedVariables.tabsOnly = true;
	}
	else
	{
		tabbing.setSelected(false);
		sharedVariables.tabsOnly = false;
	}
}


if(event.getActionCommand().equals("Auto Buffer Chat Length"))
{

	if(sharedVariables.autoBufferChat == false)
	{
		sharedVariables.autoBufferChat=true;
		autobufferchat.setSelected(true);
	}
	else
	{
		sharedVariables.autoBufferChat=false;
		autobufferchat.setSelected(false);
	}
}


if(event.getActionCommand().equals("No Idle"))
{

	if(sharedVariables.noidle == false)
	{
		sharedVariables.noidle=true;
		autonoidle.setSelected(true);
	}
	else
	{
		sharedVariables.noidle=false;
		autonoidle.setSelected(false);
	}
}


if(event.getActionCommand().equals("Switch Tab On Tell"))
{

	if(sharedVariables.switchOnTell == false)
	{
		sharedVariables.switchOnTell=true;
		tellswitch.setSelected(true);
	}
	else
	{
		sharedVariables.switchOnTell=false;
		tellswitch.setSelected(false);
	}
}

if(event.getActionCommand().equals("Timestamp To Left Of Name"))
{
	if(sharedVariables.leftTimestamp == false)
	{
		sharedVariables.leftTimestamp = true;
		leftNameTimestamp.setSelected(true);
	}
	else
	{
		sharedVariables.leftTimestamp = false;
		leftNameTimestamp.setSelected(false);
	}

}
if(event.getActionCommand().equals("Timestamp Connecting"))
{
	if(sharedVariables.reconnectTimestamp == false)
	{
		sharedVariables.reconnectTimestamp = true;
		reconnectTimestamp.setSelected(true);
	}
	else
	{
		sharedVariables.reconnectTimestamp = false;
		reconnectTimestamp.setSelected(false);
	}

}

if(event.getActionCommand().equals("Timestamp Shouts"))
{
	if(sharedVariables.shoutTimestamp == false)
	{
		sharedVariables.shoutTimestamp = true;
		shoutTimestamp.setSelected(true);
	}
	else
	{
		sharedVariables.shoutTimestamp = false;
		shoutTimestamp.setSelected(false);
	}
}

if(event.getActionCommand().equals("Timestamp Channel Qtells"))
{
	if(sharedVariables.qtellTimestamp == false)
	{
		sharedVariables.qtellTimestamp = true;
		qtellTimestamp.setSelected(true);
	}
	else
	{
		sharedVariables.qtellTimestamp = false;
		qtellTimestamp.setSelected(false);
	}
}


if(event.getActionCommand().equals("Timestamp Tells"))
{
	if(sharedVariables.tellTimestamp == false)
	{
		sharedVariables.tellTimestamp = true;
		tellTimestamp.setSelected(true);
	}
	else
	{
		sharedVariables.tellTimestamp = false;
		tellTimestamp.setSelected(false);
	}
}
if(event.getActionCommand().equals("Timestamp Channels"))
{
	if(sharedVariables.channelTimestamp == false)
	{
		sharedVariables.channelTimestamp = true;
		channelTimestamp.setSelected(true);
	}
	else
	{
		sharedVariables.channelTimestamp = false;
		channelTimestamp.setSelected(false);
	}
}


if(event.getActionCommand().equals("Auto Name Popup"))
{
	if(sharedVariables.autopopup == false)
	{
		sharedVariables.autopopup = true;
		autopopup.setSelected(true);
	}
	else
	{
		sharedVariables.autopopup = false;
		autopopup.setSelected(false);
	}
}
if(event.getActionCommand().equals("Auto History Popup"))
{
	if(sharedVariables.autoHistoryPopup == false)
	{
		sharedVariables.autoHistoryPopup = true;
		autoHistoryPopup.setSelected(true);
	}
	else
	{
		sharedVariables.autoHistoryPopup = false;
		autoHistoryPopup.setSelected(false);
	}
}
if(event.getActionCommand().equals("Auto Observe Tomato"))
{
	if(sharedVariables.autoTomato == false)
	sharedVariables.autoTomato = true;
	else
	sharedVariables.autoTomato = false;
}

if(event.getActionCommand().equals("Auto Observe Cooly"))
{
	if(sharedVariables.autoCooly == false)
	sharedVariables.autoCooly = true;
	else
	sharedVariables.autoCooly = false;
}
if(event.getActionCommand().equals("Auto Observe WildOne"))
{
	if(sharedVariables.autoWildOne == false)
	sharedVariables.autoWildOne = true;
	else
	sharedVariables.autoWildOne = false;

//	reconnect2.setVisible(true);
}


if(event.getActionCommand().equals("Auto Observe Flash"))
{
	if(sharedVariables.autoFlash == false)
	sharedVariables.autoFlash = true;
	else
	sharedVariables.autoFlash = false;
}

if(event.getActionCommand().equals("Auto Observe Olive"))
{
	if(sharedVariables.autoOlive == false)
	sharedVariables.autoOlive = true;
	else
	sharedVariables.autoOlive = false;
}
if(event.getActionCommand().equals("Auto Observe Ketchup"))
{
	if(sharedVariables.autoKetchup == false)
	sharedVariables.autoKetchup = true;
	else
	sharedVariables.autoKetchup = false;

//	reconnect2.setVisible(true);
}


if(event.getActionCommand().equals("Auto Observe LittlePer"))
{
	if(sharedVariables.autoLittlePer == false)
	sharedVariables.autoLittlePer = true;
	else
	sharedVariables.autoLittlePer = false;
}



if(event.getActionCommand().equals("Auto Observe Slomato"))
{
	if(sharedVariables.autoSlomato == false)
	sharedVariables.autoSlomato = true;
	else
	sharedVariables.autoSlomato = false;
}



if(event.getActionCommand().equals("Random Piece Set Observe Only"))
{
	if(sharedVariables.randomArmy == false)
	{
          sharedVariables.randomArmy = true;
          randomArmy.setSelected(true);
	}
        else
        {
	sharedVariables.randomArmy = false;
        randomArmy.setSelected(false);
        }
}
if(event.getActionCommand().equals("Configure Random Pieces"))
{
 customizeExcludedPiecesDialog goConfigure = new customizeExcludedPiecesDialog(this, false, sharedVariables);
 goConfigure.setVisible(true);
}

if(event.getActionCommand().equals("Random Square Tiles Observe Only"))
{
	if(sharedVariables.randomBoardTiles == false)
	{
          sharedVariables.randomBoardTiles = true;
          randomTiles.setSelected(true);
	}
        else
        {
	sharedVariables.randomBoardTiles = false;
        randomTiles.setSelected(false);
        }
}
if(event.getActionCommand().equals("Sounds for Notifications"))
{
	if(sharedVariables.specificSounds[4] == false)
	{
		notifysound.setSelected(true);
		sharedVariables.specificSounds[4] = true;
	}
	else
	{
		notifysound.setSelected(false);
		sharedVariables.specificSounds[4] = false;
	}
}

if(event.getActionCommand().equals("Sounds for Observed Games"))
{
	if(sharedVariables.makeObserveSounds == false)
	{
		makeObserveSounds.setSelected(true);
		sharedVariables.makeObserveSounds = true;
	}
	else
	{
		makeObserveSounds.setSelected(false);
		sharedVariables.makeObserveSounds = false;
	}
}
if(event.getActionCommand().equals("Sounds"))
{
	if(sharedVariables.makeSounds == false)
	sharedVariables.makeSounds = true;
	else
	sharedVariables.makeSounds = false;
}



if(event.getActionCommand().equals("Start Powerout"))
{

//JFrame aframe = new JFrame();
//aframe.setVisible(true);
poweroutframe frame = new poweroutframe(sharedVariables.poweroutSounds);
//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//frame.pack();
sharedVariables.desktop.add(frame);
frame.setVisible(true);
/*JComponent newContentPane = new poweroutpanel();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
*/   try {
        frame.setSelected(true);
        //aa.Input.setFocusable(true);
    } catch (Exception e) {}
frame.setSize(700,550);
//frame.setExtendedState(Frame.MAXIMIZED_BOTH);

}
if(event.getActionCommand().equals("Start MineSweeper"))
{


minesweeper10 frame = new minesweeper10(sharedVariables, this);

sharedVariables.desktop.add(frame);

   try {
        frame.setSelected(true);
        //aa.Input.setFocusable(true);
    } catch (Exception e) {}

}



if(event.getActionCommand().equals("Start Connect Four"))
{


connectFour frame = new connectFour();

sharedVariables.desktop.add(frame);

   try {
        frame.setSelected(true);
        //aa.Input.setFocusable(true);
    } catch (Exception e) {}

}



if(event.getActionCommand().equals("Start Mastermind"))
{


mastermind11 frame = new mastermind11();

sharedVariables.desktop.add(frame);

   try {
        frame.setSelected(true);
        //aa.Input.setFocusable(true);
    } catch (Exception e) {}

}





if(event.getActionCommand().equals("Console Colors"))
{

//JDialog frame = new JDialog();
//sharedVariables.shoutcolor = JColorChooser.showDialog(frame, "Choose Shout Color", sharedVariables.shoutcolor);
	customizeConsolelColorsDialog frame = new customizeConsolelColorsDialog((JFrame) this, false, sharedVariables, consoles, gameconsoles);





}
if(event.getActionCommand().equals("Activities Window Color"))
{
// BackColor
 JDialog frame = new JDialog();
 Color newColor = JColorChooser.showDialog(frame, "Choose Activites Window Background Color", sharedVariables.listColor);
 if(newColor != null)
 sharedVariables.listColor=newColor;
 if(myfirstlist != null)
 myfirstlist.setColors();

}

if(event.getActionCommand().equals("Main Background"))
{
// BackColor
 JDialog frame = new JDialog();
 Color newColor = JColorChooser.showDialog(frame, "Choose Main Background Color", sharedVariables.MainBackColor);
 if(newColor != null)
 sharedVariables.MainBackColor=newColor;
 sharedVariables.desktop.setBackground(sharedVariables.MainBackColor);


}


 if(event.getActionCommand().equals("Change Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.myFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {
				sharedVariables.myFont=fnt;
	        for(int i=0; i < sharedVariables.openConsoleCount; i++)
			 {
				 if(consoles[i]!= null)
				 if(sharedVariables.tabStuff[i].tabFont == null)
	              {
	              consoles[i].setFont(sharedVariables.myFont);
	              }
 			}

 // now game boards
 	        for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 {
 				 if(gameconsoles[i]!= null)
 	 			{
 	        		gameconsoles[i].setFont(sharedVariables.myFont);
 	 			}
 			}
	}//end if font not null

}

if(event.getActionCommand().equals("Change Tab Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.myTabFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {

				sharedVariables.myTabFont=fnt;
				repaintTabs();
			}// end if font not null
}
if(event.getActionCommand().equals("Change Input Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.inputFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {

				sharedVariables.inputFont=fnt;
				setInputFont();
			}// end if font not null
}




if(event.getActionCommand().equals("Game Board Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.myGameFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {
				sharedVariables.myGameFont=fnt;

 // now game boards
 	        for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 {
 				 if(myboards[i]!= null)
 	 			{

 	        		myboards[i].mycontrolspanel.setFont();
 	 			}
 			}
		}// not null font

}

if(event.getActionCommand().equals("Game Clock Font"))
{JFrame f = new JFrame("FontChooser Startup");
    FontChooser2 fc = new FontChooser2(f, sharedVariables.myGameClockFont);
    fc.setVisible(true);
	         Font fnt = fc.getSelectedFont();
	        if(fnt != null)
	        {
				sharedVariables.myGameClockFont=fnt;

 // now game boards
 	        for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 {
 				 if(myboards[i]!= null)
 	 			{

 	        		myboards[i].mycontrolspanel.setFont();
 	 			}
 			}
		}// not null font

}






if(event.getActionCommand().equals("Channel Colors"))
{
	customizeChannelColorDialog frame = new customizeChannelColorDialog((JFrame) this, false, sharedVariables, consoles);
	//frame.setSize(300,250);
	frame.setVisible(true);
}

}// end action performed method
void openActivities()
{
try {
	if(myfirstlist == null)
	mycreator.createListFrame(eventsList, seeksList, computerSeeksList, notifyList, this);
	else if(!myfirstlist.isVisible())
	mycreator.createListFrame(eventsList, seeksList, computerSeeksList, notifyList, this);

	myfirstlist.setColors();
//	myfirstlist.setSelected(true);
}catch(Exception dui){}
}


void makeEngineWarning()
{
String swarning = "You must be in examine mode to load an engine.";
Popup pframe = new Popup((JFrame) this, true, swarning);
pframe.setVisible(true);

}
void setBoard(int type)
{


if(type == 0)
solidboard.setSelected(true);
else
solidboard.setSelected(false);

if(type == 1)
woodenboard1.setSelected(true);
else
woodenboard1.setSelected(false);

if(type == 2)
woodenboard2.setSelected(true);
else
woodenboard2.setSelected(false);

if(type == 3)
woodenboard3.setSelected(true);
else
woodenboard3.setSelected(false);

if(type == 4)
grayishboard.setSelected(true);
else
grayishboard.setSelected(false);


if(type == 5)
board5.setSelected(true);
else
board5.setSelected(false);

if(type == 6)
board6.setSelected(true);
else
board6.setSelected(false);

if(type == 7)
board7.setSelected(true);
else
board7.setSelected(false);

if(type == 8)
oliveboard.setSelected(true);
else
oliveboard.setSelected(false);

if(type == 9)
cherryboard.setSelected(true);
else
cherryboard.setSelected(false);

if(type == 10)
purpleboard.setSelected(true);
else
purpleboard.setSelected(false);


 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();

}
void resetConsoleLayout()
{
		if(sharedVariables.consoleLayout == 1)
		{
			tabLayout1.setSelected(true);
			tabLayout2.setSelected(false);
			tabLayout3.setSelected(false);

		}
		else if(sharedVariables.consoleLayout == 2)
		{
			tabLayout1.setSelected(false);
			tabLayout2.setSelected(true);
			tabLayout3.setSelected(false);

		}
		else
		{
			tabLayout1.setSelected(false);
			tabLayout2.setSelected(false);
			tabLayout3.setSelected(true);

		}

		for(int a=0; a< sharedVariables.maxConsoleTabs; a++)
			if(consoleSubframes[a]!=null)
			consoleSubframes[a].overall.recreate(sharedVariables.consolesTabLayout[a]);
}
void redrawBoard(int type)
{
if(type == 0)
boardconsole0.setSelected(true);
else
boardconsole0.setSelected(false);

if(type == 1)
boardconsole1.setSelected(true);
else
boardconsole1.setSelected(false);

if(type == 2)
boardconsole2.setSelected(true);
else
boardconsole2.setSelected(false);

if(type == 3)
boardconsole3.setSelected(true);
else
boardconsole3.setSelected(false);


for(int a = 0; a<sharedVariables.maxGameTabs; a++)
if(myboards[a] != null)
if(myboards[a].isVisible())
myboards[a].recreate();
}
void setPieces(int type)
{


if(type == 0)
pieces1.setSelected(true);
else
pieces1.setSelected(false);

if(type == 1)
pieces2.setSelected(true);
else
pieces2.setSelected(false);

if(type == 2)
pieces3.setSelected(true);
else
pieces3.setSelected(false);

if(type == 3)
pieces4.setSelected(true);
else
pieces4.setSelected(false);

if(type == 4)
pieces5.setSelected(true);
else
pieces5.setSelected(false);

if(type == 5)
pieces6.setSelected(true);
else
pieces6.setSelected(false);


if(type == 6)
pieces7.setSelected(true);
else
pieces7.setSelected(false);

if(type == 7)
pieces8.setSelected(true);
else
pieces8.setSelected(false);

if(type == 8)
pieces9.setSelected(true);
else
pieces9.setSelected(false);

if(type == 9)
pieces10.setSelected(true);
else
pieces10.setSelected(false);


if(type == 10)
pieces11.setSelected(true);
else
pieces11.setSelected(false);

if(type == 11)
pieces12.setSelected(true);
else
pieces12.setSelected(false);

if(type == 12)
pieces13.setSelected(true);
else
pieces13.setSelected(false);
if(type == 13)
pieces14.setSelected(true);
else
pieces14.setSelected(false);

if(type == 14)
pieces15.setSelected(true);
else
pieces15.setSelected(false);

if(type == 15)
pieces16.setSelected(true);
else
pieces16.setSelected(false);

if(type == 16)
pieces17.setSelected(true);
else
pieces17.setSelected(false);

if(type == 17)
pieces18.setSelected(true);
else
pieces18.setSelected(false);

if(type == 18)
pieces19.setSelected(true);
else
pieces19.setSelected(false);


if(type == 19)
pieces19.setSelected(true);
else
pieces19.setSelected(false);
if(type == 20)
pieces19.setSelected(true);
else
pieces19.setSelected(false);
if(type == 21)
pieces19.setSelected(true);
else
pieces19.setSelected(false);



if(type == 22)
{
	pieces20.setSelected(true);
	generateRandomPieces(type);
}
else
pieces20.setSelected(false);


 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(myboards[a]!=null)
 //if(myboards[a].isVisible() == true)
 myboards[a].mypanel.repaint();

}

void generateRandomPieces(int type)
{
	Random randomGenerator = new Random();

	for(int a = 0; a<12; a++)
	{
	      int randomInt = randomGenerator.nextInt(type - 1);
		graphics.pieces[type][a] = graphics.pieces[randomInt][a];
	}
}

void setMySize()
{
	/* check if we need to resize differently and not maximize */
	scriptLoader myloader = new scriptLoader();
	boolean valid = false;
	int width=800;
	int height=800;
try {

	Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        sharedVariables.screenW = dim.width;
        sharedVariables.screenH = dim.height;
}
catch(Exception badtool){ return;}


try {
ArrayList<String> myArray = new ArrayList();

myloader.loadScript(myArray, "lantern_sizing.ini");
try {
	if(myArray.size() > 1)
	{
		width=Integer.parseInt(myArray.get(0));
		height=Integer.parseInt(myArray.get(1));
        if(width > 200 && height > 200 && width < sharedVariables.screenW - 100 && height < sharedVariables.screenH - 50)
        {
			valid=true;
			sharedVariables.screenW=width;
			sharedVariables.screenH=height;
		}
        else
        {
			width=800;
			height=800;

		}

	}// end size of array
}// end try
catch(Exception wrongsize) {

	width=800;
	height=800;

}
setSize(width,height);
if(valid == false)
setExtendedState(JFrame.MAXIMIZED_BOTH);


}// end outer try
catch(Exception d)
{
setSize(width,height);
if(valid == false)
setExtendedState(JFrame.MAXIMIZED_BOTH);

}// end catch


}// end method set size

public void windowClosed(WindowEvent e)
{

//if(sharedVariables.engineOn == true)
sendToEngine("exit\n");
sendToEngine("quit\n");

}

public void windowDeactivated(WindowEvent e)
{

}
public void windowDeiconified(WindowEvent e)
{

}
public void windowClosing(WindowEvent e)
{
//if(sharedVariables.engineOn == true)
sendToEngine("exit\n");
sendToEngine("quit\n");
if(sharedVariables.standAlone == false)
	System.exit(0);
else
{
	JSettingsDialog frame = new JSettingsDialog((JFrame) this, false, sharedVariables);
}
}

class JSettingsDialog extends JFrame
{

channels sharedVariables;

JSettingsDialog(JFrame frame, boolean mybool, channels sharedVariables1)
{
//	super(frame, false);

	sharedVariables=sharedVariables1;
	JPanel pane =  new JPanel();
	JLabel tosave = new JLabel("Save Settings?");
	JButton yes =  new JButton("Yes");
	JButton no = new JButton("No");
	yes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
						try {
							mysettings.saveNow(myboards, consoleSubframes, sharedVariables);
							mineScores.saveNow(sharedVariables);
				}
				catch(Exception d){}
					System.exit(0);
						dispose();

		}// end try
			catch(Exception e)
			{}
		}
});
	no.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
			 try
			 	{
				System.exit(0);
				dispose();
		}// end try
			catch(Exception e)
			{}
		}
});
	pane.setLayout(new GridLayout(3,1)); // rows collums
	pane.add(tosave);
	pane.add(yes);
	pane.add(no);
	add(pane);
	setLocation(400,300);
	setSize(200, 200);

	for(int b=0; b<sharedVariables.maxGameTabs; b++)
	if(myboards[b]!=null)
	{
          if(myboards[b].isVisible())
	  {
            if(myboards[b].isMaximum()==false)
	    {
              myboards[b].setBoardSize();
              }
          }
       }
       else
       {
        sharedVariables.openBoardCount=b;
        break;
       }


		for(int b=0; b<sharedVariables.maxConsoleTabs; b++)
		if(consoleSubframes[b]!=null)
		if(consoleSubframes[b].isVisible())
		if(consoleSubframes[b].isMaximum()==false)
	consoleSubframes[b].setBoardSize();

			sharedVariables.activitiesOpen = false;
		if(myfirstlist!=null)
		if(myfirstlist.isVisible())
		{
			sharedVariables.activitiesOpen = true;

				myfirstlist.setBoardSize();

		}

	setVisible(true);
        setAlwaysOnTop(true);
}// end constructor


}//end class


public void windowOpened(WindowEvent e)
{

}

public void windowIconified(WindowEvent e)
{

}


public void windowActivated(WindowEvent e)
{



}

void setButtonTitle(int a)
{
	 String buttonTitle="" + a;
   if(!sharedVariables.userButtonCommands[a].equals("") && sharedVariables.showButtonTitle==true)
    {
		buttonTitle="" + a + " - ";
		if(sharedVariables.userButtonCommands[a].length() > 11)
			buttonTitle+=sharedVariables.userButtonCommands[a].substring(0,11);
		else
			buttonTitle+=sharedVariables.userButtonCommands[a];


	}
sharedVariables.mybuttons[a].setText(buttonTitle);

}
void makeToolBar()
{
  toolBar = new JToolBar("Still draggable");
 sharedVariables.mybuttons = new JButton[10];
 toolBar.setLayout(new GridLayout(1,10));
  for(int a=0; a<10; a++)
  {





    sharedVariables.mybuttons[a] = new JButton("" + a);
    setButtonTitle(a);
     sharedVariables.mybuttons[a].setFont(sharedVariables.myFont);
   final int con = a;

    sharedVariables.mybuttons[a].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event)
				{
					toolbarCommands commander = new toolbarCommands();
				commander.dispatchCommand(con, 0, false, sharedVariables,  queue);


				}});

   toolBar.add(sharedVariables.mybuttons[a]);

  }
}
void sendToEngine(String output)
{
	byte [] b2 = new byte[2500];
	try {
		for(int a=0; a< output.length(); a++)
	b2[a]=(byte) output.charAt(a);


//sharedVariables.engineQueue.add(output);

//Thread.sleep(1000);
//for(int a=0; a<10; a++)
//;
	sharedVariables.engineOut.write(b2,0, output.length());
sharedVariables.engineOut.flush();
}
catch(Exception e){}
}



private static final String CHARSET = "UTF-8";



void repaintTabs()
{

	        for(int i=0; i < sharedVariables.openConsoleCount; i++)
			 {
				 if(consoleSubframes[i]!= null)
	              {

					  for(int a=0; a< sharedVariables.maxConsoleTabs; a++)
					  {
						 try {
							 	if(a==sharedVariables.looking[consoleSubframes[i].consoleNumber]) // tab = console number
							 	consoleSubframes[i].channelTabs[a].setBackground(sharedVariables.tabImOnBackground);
							 	else
						 		consoleSubframes[i].channelTabs[a].setBackground(sharedVariables.tabBackground);
							 consoleSubframes[i].channelTabs[a].setFont(sharedVariables.myTabFont);
						  consoleSubframes[i].channelTabs[a].repaint(); } catch(Exception e){}
					  }
	              }

 			  }

 // now game boards
 	        for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 {
				 if(myboards[i]!= null)
	              {
	              myboards[i].myconsolepanel.repaint();
	              }
 			}

}

void setInputFont()
{


try {
	        for(int i=0; i < sharedVariables.openConsoleCount; i++)
			 {
				 if(consoleSubframes[i]!= null)
	              {



							 consoleSubframes[i].overall.Input.setFont(sharedVariables.inputFont);

	              }

 			  }

 // now game boards
 	        for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 {
				 if(myboards[i]!= null)
	              {
	              myboards[i].myconsolepanel.Input.setFont(sharedVariables.inputFont);
	              }
 			}


}catch(Exception badfont){}
}




void loadSoundsStandAlone()
{

// load sounds
try {
	URL songPath = this.getClass().getResource("tell.wav"); // Geturl of sound
	sharedVariables.songs[0]=songPath;
	songPath = this.getClass().getResource("click18a.wav"); // Geturl of sound
	sharedVariables.songs[1]=songPath;
	songPath = this.getClass().getResource("click10b.wav"); // Geturl of sound
	sharedVariables.songs[2]=songPath;
	songPath = this.getClass().getResource("serv1a.wav"); // Geturl of sound
	sharedVariables.songs[3]=songPath;
	songPath = this.getClass().getResource("beeppure.wav"); // Geturl of sound
	sharedVariables.songs[4]=songPath;
	songPath = this.getClass().getResource("fitebell.au"); // Geturl of sound
	sharedVariables.songs[5]=songPath;
	songPath = this.getClass().getResource("buzzer.wav"); // Geturl of sound
	sharedVariables.songs[6]=songPath;


	songPath = this.getClass().getResource("BEEP_FM.wav"); // Geturl of sound
	sharedVariables.poweroutSounds[0]=songPath;
	songPath = this.getClass().getResource("BEEPPURE.wav"); // Geturl of sound
	sharedVariables.poweroutSounds[1]=songPath;
	songPath = this.getClass().getResource("BEEPSPAC.wav"); // Geturl of sound
	sharedVariables.poweroutSounds[2]=songPath;

//song1 = new Sound("DING.WAV");
//song2 = new Sound("BEEPPURE.wav");
//song1 = new Sound("BEEP_FM.wav");
//song3 = new Sound("BEEPSPAC.wav");


}
catch(Exception e){}


}


void loadGraphicsStandAlone()
{
try {
	URL myiconurl = this.getClass().getResource( "images/game.gif");
sharedVariables.gameIcon = new ImageIcon(myiconurl, "Game");


 myiconurl = this.getClass().getResource( "images/observing.gif");
sharedVariables.observeIcon = new ImageIcon(myiconurl, "observing");

 myiconurl = this.getClass().getResource( "images/playing.gif");
sharedVariables.playingIcon = new ImageIcon(myiconurl, "Playing");

myiconurl = this.getClass().getResource( "images/examining.gif");
sharedVariables.examiningIcon = new ImageIcon(myiconurl, "Examining");


 myiconurl = this.getClass().getResource( "images/sposition.gif");
sharedVariables.sposIcon = new ImageIcon(myiconurl, "Sposition");

 myiconurl = this.getClass().getResource( "images/was.gif");
sharedVariables.wasIcon = new ImageIcon(myiconurl, "was");
}catch(Exception dd){}

for( int a = 1 ; a < graphics.maxBoards; a++)
{
	if( a != 6)
	{
		URL myurl = this.getClass().getResource(graphics.boardPaths[a] + "/light.gif");
		graphics.boards[a][0] =Toolkit.getDefaultToolkit().getImage(myurl);
		myurl = this.getClass().getResource(graphics.boardPaths[a] + "/dark.gif");
		graphics.boards[a][1] =Toolkit.getDefaultToolkit().getImage(myurl);

	}
	else
	{
		URL myurl = this.getClass().getResource(graphics.boardPaths[a] + "/light.png");
		graphics.boards[a][0] =Toolkit.getDefaultToolkit().getImage(myurl);
		myurl = this.getClass().getResource(graphics.boardPaths[a] + "/dark.png");
		graphics.boards[a][1] =Toolkit.getDefaultToolkit().getImage(myurl);
	}

}

for( int a = 0 ; a < graphics.maxPieces; a++)
{
		String ext = "gif";
		ext = graphics.pieceExt[a];
		if(ext.equals("mix"))// was rand
			break;   // allways last. not real pieces , randomly generated
		URL myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wp." + ext);
		graphics.pieces[a][0] =Toolkit.getDefaultToolkit().getImage(myurl);
		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wn." + ext);
		graphics.pieces[a][1] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wb." + ext);
		graphics.pieces[a][2] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wr." + ext);
		graphics.pieces[a][3] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wq." + ext);
		graphics.pieces[a][4] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/wk." + ext);
		graphics.pieces[a][5] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bp." + ext);
		graphics.pieces[a][6] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bn." + ext);
		graphics.pieces[a][7] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bb." + ext);
		graphics.pieces[a][8] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/br." + ext);
		graphics.pieces[a][9] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bq." + ext);
		graphics.pieces[a][10] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/64/bk." + ext);
		graphics.pieces[a][11] =Toolkit.getDefaultToolkit().getImage(myurl);

// now load multi pieces
if(graphics.resizable[a]==false)
{for(int aa=0; aa < graphics.numberPiecePaths[a]; aa++)
{

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wp." + ext);
		graphics.multiPieces[a][aa][0] =Toolkit.getDefaultToolkit().getImage(myurl);
		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wn." + ext);
		graphics.multiPieces[a][aa][1] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wb." + ext);
		graphics.multiPieces[a][aa][2] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wr." + ext);
		graphics.multiPieces[a][aa][3] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wq." + ext);
		graphics.multiPieces[a][aa][4] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/wk." + ext);
		graphics.multiPieces[a][aa][5] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bp." + ext);
		graphics.multiPieces[a][aa][6] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bn." + ext);
		graphics.multiPieces[a][aa][7] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bb." + ext);
		graphics.multiPieces[a][aa][8] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/br." + ext);
		graphics.multiPieces[a][aa][9] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bq." + ext);
		graphics.multiPieces[a][aa][10] =Toolkit.getDefaultToolkit().getImage(myurl);

		myurl = this.getClass().getResource( graphics.piecePaths[a] + "/" + graphics.multiPiecePaths[a][aa] + "/bk." + ext);
		graphics.multiPieces[a][aa][11] =Toolkit.getDefaultToolkit().getImage(myurl);



}// end aa loop

}// end multi piece if
	}// end for
  // make monge mix pieces

  int fantasy=0;
  int spatial=0;

  for(int e=0; e<graphics.maxPieces; e++)
  { if(graphics.piecePaths[e].equals("fantasy"))
      fantasy=e;
  if(graphics.piecePaths[e].equals("spatial"))
      spatial=e;

  }
 for(int aa=0; aa < graphics.numberPiecePaths[graphics.maxPieces-2]; aa++)
  {
    graphics.multiPieces[graphics.maxPieces-2][aa][0] = graphics.multiPieces[fantasy][aa][0];
          graphics.multiPieces[graphics.maxPieces-2][aa][1] = graphics.multiPieces[spatial][aa][1];
        graphics.multiPieces[graphics.maxPieces-2][aa][2] = graphics.multiPieces[fantasy][aa][2];
        graphics.multiPieces[graphics.maxPieces-2][aa][3] = graphics.multiPieces[fantasy][aa][3];
        graphics.multiPieces[graphics.maxPieces-2][aa][4] = graphics.multiPieces[spatial][aa][4];
        graphics.multiPieces[graphics.maxPieces-2][aa][5] = graphics.multiPieces[spatial][aa][5];
        graphics.multiPieces[graphics.maxPieces-2][aa][6] = graphics.multiPieces[fantasy][aa][6];
        graphics.multiPieces[graphics.maxPieces-2][aa][7] = graphics.multiPieces[spatial][aa][7];
        graphics.multiPieces[graphics.maxPieces-2][aa][8] = graphics.multiPieces[fantasy][aa][8];
        graphics.multiPieces[graphics.maxPieces-2][aa][9] = graphics.multiPieces[fantasy][aa][9];
        graphics.multiPieces[graphics.maxPieces-2][aa][10] = graphics.multiPieces[spatial][aa][10];
        graphics.multiPieces[graphics.maxPieces-2][aa][11] = graphics.multiPieces[spatial][aa][11];
  }


boolean flagger=true;
int index11=-1;
int index22=-1;
int place=0;
while(flagger== true)
{
 index11=sharedVariables.countryNames.indexOf(";", index11+1);
 index22=sharedVariables.countryNames.indexOf(";", index11+1);
 if(index11 > -1 && index22 > -1 && index22 > index11)
 {
  String lookup =sharedVariables.countryNames.substring(index11 + 1, index22);
  sharedVariables.flagImageNames.add(lookup);
  lookup="flags-small/" + lookup + ".png";

		URL myurl = this.getClass().getResource( lookup);
		sharedVariables.flagImages.add(Toolkit.getDefaultToolkit().getImage(myurl));

 }
 else
 break;

 index11=index22;






}
}// end method


/*
void loadGraphicsApplet()
{
for( int a = 1 ; a < graphics.maxBoards; a++)
{
	if( a != 6)
	{
		graphics.boards[a][0]=getImage(getDocumentBase(), graphics.boardPaths[a] + "/light.gif");
		graphics.boards[a][1]=getImage(getDocumentBase(), graphics.boardPaths[a] + "/dark.gif");
	}
	else
	{
		graphics.boards[a][0]=getImage(getDocumentBase(), graphics.boardPaths[a] + "/light.png");
		graphics.boards[a][1]=getImage(getDocumentBase(), graphics.boardPaths[a] + "/dark.png");
	}

}
for( int a = 0 ; a < graphics.maxPieces; a++)
{


graphics.pieces[a][0] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wp.gif");
graphics.pieces[a][1] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wn.gif");
graphics.pieces[a][2] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wb.gif");
graphics.pieces[a][3] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wr.gif");
graphics.pieces[a][4] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wq.gif");
graphics.pieces[a][5] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/wk.gif");
graphics.pieces[a][6] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bp.gif");
graphics.pieces[a][7] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bn.gif");
graphics.pieces[a][8] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bb.gif");
graphics.pieces[a][9] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/br.gif");
graphics.pieces[a][10] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bq.gif");
graphics.pieces[a][11] = getImage(getDocumentBase(), graphics.piecePaths[a] + "/64/bk.gif");



}
}//end method
void loadSoundsApplet()
{// load sounds
try {
	URL songPath = new URL(getCodeBase(), "tell.wav"); // Geturl of sound
	sharedVariables.songs[0]=songPath;
	songPath = new URL(getCodeBase(), "click18a.wav"); // Geturl of sound
	sharedVariables.songs[1]=songPath;
	songPath = new URL(getCodeBase(), "click10b.wav"); // Geturl of sound
	sharedVariables.songs[2]=songPath;
	songPath = new URL(getCodeBase(), "serv1a.wav"); // Geturl of sound
	sharedVariables.songs[3]=songPath;

}
catch(Exception e){}
}//end method
*/
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






	             // rt.exec( "open " + myurl);
          /*Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL",
               new Class[] {String.class});
            openURL.invoke(null, new Object[] {myurl});

			http://www.java2s.com/Code/Java/Development-Class/LaunchBrowserinMacLinuxUnix.htm
			*/
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
public void saveSettings() {


          }

// not really used. i am saving to a file on a hard drive now
// this was an attempt to write to the web but any settings are deleted if they clear temp internet files

 public void getSettings() {

          }
} // end multi frame class



