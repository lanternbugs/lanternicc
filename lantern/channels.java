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
import javax.swing.text.*;
import java.io.*;
import java.net.*;
import java.applet.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class channels {
boardSizes [] myBoardSizes;
boardSizes [] myConsoleSizes;
boardSizes myActivitiesSizes;

seekGraphData graphData;
mineScoresGroup mineScores;
gameFrame myGameList;

JScrollPane [] ConsoleScrollPane;
gamestate [] mygame;
JSlider [] moveSliders;
seekData myseek;
shoutRouting shoutRouter;
listClass toolboxListData;
JDesktopPane desktop;
//Container desktop;
Image [] img;
Image wallpaperImage;
tableClass [] mygametable;
JTable [] gametable;

boolean randomArmy=false;
boolean randomBoardTiles=false;

boolean indent;
boolean doreconnect;
boolean uci;
boolean autoTomato;
boolean autoFlash;
boolean autoCooly;
boolean autoWildOne;
boolean autoKetchup;
boolean autoSlomato;
boolean autoOlive;
boolean autoLittlePer;
boolean [] pointedToMain = new boolean[100];
boolean [] excludedPieces;
boolean highlightMoves;
boolean makeSounds;
boolean engineOn;
boolean tabsOnly;
boolean standAlone;
boolean pgnLogging;
boolean [] mainAlso = new boolean[500]; // determines if a channel on a seperate tab also prints to main or M0
boolean shoutsAlso;
boolean [] specificSounds;
boolean tellsToTab;
boolean switchOnTell;
boolean [] useConsoleFont;
boolean toolbarVisible;
boolean noidle;
boolean showQsuggest;
boolean autopopup;
boolean activitiesOpen;
boolean showMaterialCount;
boolean sideways;
boolean showButtonTitle;
boolean loadSizes;
boolean showConsoleMenu;
boolean autoBufferChat;
boolean rotateAways;
boolean iloggedon;
boolean channelTimestamp;
boolean shoutTimestamp;
boolean tellTimestamp;
boolean leftTimestamp;
boolean reconnectTimestamp;
boolean qtellTimestamp;
boolean channelNumberLeft;
boolean checkLegality;
boolean compactNameList=false;

int maxUserButtons=10;
ArrayList<String> rightClickMenu = new ArrayList();
ArrayList<String> iccLoginScript = new ArrayList();
ArrayList<String> ficsLoginScript = new ArrayList();
ArrayList<String> notifyControllerScript = new ArrayList();
ArrayList<String> iccChannelList = new ArrayList();
ArrayList<channelNotifyClass> channelNotifyList = new ArrayList();
ArrayList<lanternNotifyClass> lanternNotifyList = new ArrayList();

ArrayList<nameListClass> channelNamesList = new ArrayList();

ArrayList<String> lanternAways = new ArrayList();
ArrayList<told> toldNames = new ArrayList();
ArrayList<told> toldTabNames = new ArrayList();

ArrayList<String> [] comboNames;
F9Management F9Manager;

ImageIcon observeIcon;
ImageIcon wasIcon;
ImageIcon playingIcon;
ImageIcon examiningIcon;
ImageIcon sposIcon;
ImageIcon gameIcon;

String [] userButtonCommands = new String[maxUserButtons];
String [] tabTitle;
String [] consoleTabTitles;
String [] consoleTabCustomTitles;
String wallpaperFileName;
String lasttell;
String myname;// my login on the server
String myPartner; // my bughouse partner
String myopponent;
String myServer; // FICS or ICC
String version; // current version of this build i.e. v1.48
String chessclubIP;
String operatingSystem;
String notifyControllerFile;
Process timestamp;
Process engine;

Font myFont;
Font myGameFont;
Font myGameClockFont;
Font crazyFont;
Font myTabFont;
Font inputFont;
Font nameListFont;
Font [] consoleFonts;
Color [] channelColor = new Color[500];
Color lightcolor;
Color darkcolor;
Color shoutcolor;
Color sshoutcolor;
Color tellcolor;
Color qtellcolor;
Color kibcolor;
Color ForColor;
Color BackColor;
Color typedColor;
Color MainBackColor;
Color highlightcolor;
Color premovehighlightcolor;
Color activeTabForeground;
Color passiveTabForeground;
Color tabBackground;
Color tabBackground2;// the background if tab is unvisted on one window but not another
Color newInfoTabBackground;
Color tabImOnBackground;
Color tabBorderColor;
Color boardBackgroundColor;
Color boardForegroundColor;
Color clockForegroundColor;
Color onMoveBoardBackgroundColor;
Color responseColor;
Color defaultChannelColor;
Color listColor;
Color inputChatColor;
Color inputCommandColor;
Color chatTimestampColor;
Color qtellChannelNumberColor;
Color channelTitlesColor;
Color tellNameColor;
Color nameForegroundColor;
Color nameBackgroundColor;

StyledDocument [] mydocs;
StyledDocument [] mygamedocs = new StyledDocument[100];
JButton [] mybuttons;

URL [] songs;
URL [] poweroutSounds;
int chatBufferSize=100000;
int chatBufferExtra=1000;
int showTenths;
int chatFrame;
int visibleConsoles=0;
int maxSongs=100;
int maxConsoleTabs=12;
int consoleLayout;
long autoexamspeed;
long lastSoundTime;// used to not send multiple sounds in say one second when forwarding through games
int lastSoundCount;
int [] channelOn= new int[500];
int [][] console = new int[maxConsoleTabs][500];
int [] style = new int[500];
int [] looking = new int[maxConsoleTabs];
int [] gamelooking = new int[100];
int [] boardConsoleSizes = new int[4];
int [] consolesTabLayout;
int [] consolesNamesLayout;
int boardConsoleType;
int openConsoleCount;
int tellconsole;
int updateTellConsole;
int lastButton;
int tabChanged;
int maxBoardTabs;
int [] Looking;
int engineBoard;
int autoexam;
int autoexamnoshow;
int password;
int openBoardCount;
int aspect;
int NOT_FOUND_NUMBER;
int STATE_EXAMINING;
int STATE_PLAYING;
int STATE_OBSERVING;
int STATE_OVER;
int ISOLATED_NUMBER;
int webframeWidth;
int webframeHeight;
int boardType;
int pieceType;
int maxGameTabs;
int maxGameConsoles;
int maxConsoles;
int gameNotifyConsole;
int tellTab;
int tellStyle;
int qtellStyle;
int nonResponseStyle;
int BackStyle;
int responseStyle;
int shoutStyle;
int sshoutStyle;
int kibStyle;
int screenW;
int screenH;
int activitiesTabNumber=0;
int defaultBoardWide;
int defaultBoardHigh;
int nameListSize=90;

channelTabInfo [] tabStuff = new channelTabInfo[maxConsoleTabs];
ConcurrentLinkedQueue<myoutput> engineQueue;
OutputStream engineOut;
File engineFile;
File wallpaperFile;
preselectedBoards preselectBoards;

Point webframePoint;

channels()
{
myServer = "ICC";
version = "v4.00";
F9Manager = new F9Management();
mineScores = new mineScoresGroup();
Looking = new int[100];

mydocs = new StyledDocument[maxConsoleTabs];
String os = System.getProperty("os.name").toLowerCase();
if (os.indexOf( "win" ) >= 0)
	operatingSystem = "win";
else if(os.indexOf( "mac" ) >= 0)
	operatingSystem = "mac";
else
	operatingSystem = "unix";
setupMenu();
toolboxListData = new listClass("Scripts");

shoutRouter = new shoutRouting();
try {
/*	observeIcon = new ImageIcon("images/observing.gif", "Observing");
wasIcon = new ImageIcon("images/was.gif", "Was");
playingIcon = new ImageIcon("images/playing.gif", "Playing");
examiningIcon = new ImageIcon("images/examining.gif", "Examining");
sposIcon = new ImageIcon("images/sposition.gif", "Sposition");
*/

}


catch(Exception d){}
myseek = new seekData();
resourceClass dummyUse = new resourceClass();
excludedPieces = new boolean[dummyUse.maxPieces - 1];
for(int excl = 0; excl < dummyUse.maxPieces - 1; excl++)
excludedPieces[excl]=false;


noidle=false;
standAlone = true;
chessclubIP = "207.99.83.228";
notifyControllerFile = "lantern_notify_controler.ini";
boardType=2;
pieceType=14;
NOT_FOUND_NUMBER = -100;

STATE_EXAMINING = 2;
STATE_PLAYING = 1;
STATE_OBSERVING = 0;
STATE_OVER = -1;
ISOLATED_NUMBER = -1;

// default
webframeWidth=700;
webframeHeight=500;
defaultBoardWide=650;
defaultBoardHigh=550;

// now see if they got a bigger screen
try {
	Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        int screenW = dim.width;
        int screenH = dim.height;
if(screenW > 1100 && screenH > 650)
{
webframeWidth=1000;
webframeHeight=700;

}
if(screenW > 900 && screenH > 700)
{
	defaultBoardWide=850;
	defaultBoardHigh=700;

}

}
catch(Exception sizing1){}
maxGameTabs = maxGameConsoles=maxBoardTabs=40;
myBoardSizes = new boardSizes[maxGameTabs];
moveSliders = new JSlider[maxGameTabs];
mygametable = new tableClass[maxGameTabs];
gametable = new JTable[maxGameTabs];

maxConsoles=maxConsoleTabs;

graphData = new seekGraphData();

tellStyle=0;
qtellStyle=0;
nonResponseStyle=0;
BackStyle=0;
responseStyle=0;
shoutStyle=0;
sshoutStyle=0;
kibStyle=0;
consoleLayout=1;// different layouts of subframe console. 1 is all tabs in row. 2 is two rows. can make  more

preselectBoards = new preselectedBoards();
switchOnTell = true;
wallpaperImage=null;
aspect=0;
highlightMoves=true;
uci=false;
engineQueue = new ConcurrentLinkedQueue<myoutput>();
doreconnect=false;
engineBoard=-1;
engineOn=false;
makeSounds=true;
pgnLogging=true;
indent=false;
tellTimestamp=true;
leftTimestamp=true;
reconnectTimestamp=true;
shoutTimestamp=true;
channelTimestamp=true;
qtellTimestamp=true;
channelNumberLeft=true;
checkLegality=true;

if(operatingSystem.equals("mac"))
	indent=true;
autopopup=true;
activitiesOpen=false;
showMaterialCount=true;
sideways=false;
showButtonTitle=true;
showConsoleMenu=true;
autoBufferChat=true;
rotateAways=false;
iloggedon=false;
comboNames = new ArrayList[maxConsoleTabs];
for(int combo=0; combo<maxConsoleTabs; combo++)
	comboNames[combo]=new ArrayList();
myConsoleSizes = new boardSizes[maxConsoleTabs];
myActivitiesSizes = new boardSizes();
myActivitiesSizes.point0.x=50;
myActivitiesSizes.point0.y=50;
myActivitiesSizes.con0x=600;
myActivitiesSizes.con0y=500;

for(int a=0; a<100; a++)
{
	Looking[a]=-1;
	pointedToMain[a]=false;
}

ConsoleScrollPane = new JScrollPane[maxConsoleTabs];
autoTomato = false;
autoCooly = false;
autoFlash = false;
autoWildOne = false;
autoKetchup = false;
autoOlive = false;
autoSlomato = false;
autoLittlePer =false;

toolbarVisible=false;
showQsuggest=true;
shoutsAlso=false;
loadSizes=true;

tabChanged = -1;
tabTitle = new String[200];
for(int a=0; a<200; a++)
tabTitle[a] = "G" + a;
consoleTabTitles = new String[maxConsoleTabs];
consoleTabCustomTitles = new String[maxConsoleTabs];
useConsoleFont = new boolean[maxConsoleTabs];
consoleFonts = new Font[maxConsoleTabs];
for(int a=0; a<maxConsoleTabs; a++)
{if(a==0)
consoleTabTitles[a]= "M" + a;
  else
consoleTabTitles[a]= "C" + a;
if(a==1)
consoleTabCustomTitles[a]="doubleclick to name tab";
else
consoleTabCustomTitles[a]="";
consoleFonts[a]=null;
useConsoleFont[a]=false;
}
for(int a=0; a<maxUserButtons; a++)
userButtonCommands[a]="";

wallpaperFileName="";
tabsOnly=false;
int lastButton=-1;
songs = new URL[maxSongs];
poweroutSounds= new URL[maxSongs];

specificSounds = new boolean[maxSongs];
// for use in muting specific sounds

for(int songcount=0; songcount<maxSongs; songcount++)
	specificSounds[songcount]=true;

mygame = new gamestate[300];

boardConsoleType=2;
boardConsoleSizes[0]=30;
boardConsoleSizes[1]=100;
boardConsoleSizes[2]=160;
boardConsoleSizes[3]=300;
lastSoundTime=0;
lastSoundCount=0;

showTenths=1; // 0 no 1 when low 2 allways
openBoardCount=0;
openConsoleCount=0;
lasttell="";
tellconsole=0;
tellTab=0;
tellsToTab=false;
updateTellConsole=0;
tabBorderColor = new Color(235,0,0);
gameNotifyConsole=0;
consolesTabLayout = new int[maxConsoleTabs];
consolesNamesLayout = new int[maxConsoleTabs];

for(int a=0; a<maxConsoleTabs; a++)
{looking[a]=a;
tabStuff[a]=new channelTabInfo();
consolesTabLayout[a]=4;
consolesNamesLayout[a]=1;
}


	passiveTabForeground=new Color(0,0,0);
	activeTabForeground=new Color(30,30,200);
    tabBackground=new Color(204,255,255);// was 204 204 204 a gray
    tabBackground2=new Color(204,255,255);
   highlightcolor = new Color(230,0,10);
premovehighlightcolor = new Color(10,0,230);

    //newInfoTabBackground=new Color(150,145,130);
    //newInfoTabBackground=tabBackground.brighter();
	newInfoTabBackground=new Color(200,145,130);
	tabImOnBackground = new Color(255, 255, 0);
//myFont = new Font("Comic Sans MS", Font.PLAIN, 18);

if(operatingSystem.equals("unix"))
	myFont = new Font("Andale Mono", Font.PLAIN, 18);
else if(operatingSystem.equals("mac"))
	myFont = new Font("Andale Mono", Font.PLAIN, 18);
else
	myFont = new Font("Lucida Console", Font.PLAIN, 18);
try {
	myGameFont = new Font("Times New Roman", Font.PLAIN, 20);
	myGameClockFont = new Font("Times New Roman", Font.PLAIN, 40);

}
catch(Exception badfont1)
{
try {
myGameFont = new Font("Lucida Console", Font.PLAIN, 18);
myGameClockFont = new Font("Lucida Console", Font.PLAIN, 28);

}
catch(Exception badfont2)
{

}


}
inputFont= new Font("Tahoma", Font.PLAIN, 14);

myTabFont = new Font("TimesRoman", Font.PLAIN, 16);

crazyFont = new Font("TimesRoman", Font.PLAIN, 20);
ForColor = new Color(204,204, 255);
typedColor = new Color(235, 235, 255);
MainBackColor = new Color(171,171,171);
BackColor = new Color(0,0,0);
//boardForegroundColor = new Color(0,0,0);
//boardBackgroundColor = new Color(235,223,236);
boardForegroundColor = new Color(60,60,60);
clockForegroundColor = new Color(60,60,60);
boardBackgroundColor = new Color(234,255,255);
onMoveBoardBackgroundColor = new Color(100, 203, 203);
inputCommandColor = new Color(0,0,0);
inputChatColor= new Color(130,57,0);
chatTimestampColor = new Color(40,200,40);
listColor = new Color(245, 245, 245);
lightcolor= new Color(255, 255, 255);
darkcolor = new Color(71,203,211);
tellcolor = new Color(255,255,0);
qtellcolor= new Color(220, 110, 0);
shoutcolor= new Color(240,0,0);
sshoutcolor= new Color(255,255,255);
responseColor = new Color(169, 174, 214);
defaultChannelColor = new Color(180, 128, 95);
kibcolor = new Color(240, 10, 10);
qtellChannelNumberColor=new Color(204,204,255);
channelTitlesColor = new Color(204, 255, 204);
tellNameColor=new Color(0,153,153);
nameForegroundColor = new Color(51, 51, 0);
nameBackgroundColor = new Color(255,255,204);
// my original tan board
//lightcolor= new Color(255, 204, 204);
//darkcolor = new Color(193,153,153);
// current white/ blue board
for(int b=0; b<maxConsoleTabs; b++)
for(int a=0; a<500; a++)
console[b][a]=0;

for(int a=0; a<500; a++)
	{
		channelOn[a]=0;
		style[a]=0;
		mainAlso[a] = false;
		if(a<maxGameTabs)
		myBoardSizes[a]= new boardSizes();
		if(a<maxConsoleTabs)
		myConsoleSizes[a]= new boardSizes();
	}
	Color col=new Color(0,0,0);

	channelOn[0]=1;
	channelColor[0]= new Color(235, 235, 255);

	channelOn[1]=1;
	channelColor[1]= new Color(228,135,133);

	channelOn[2]=1;
	channelColor[2]= new Color(15,188,118);

	channelOn[6]=1;
	channelColor[6]= new Color(153,153,255);


	channelOn[35]=1;
	channelColor[35]= new Color(255,204,0);


	channelOn[36]=1;
	channelColor[36]= new Color(0,164,164);

	channelOn[40]=1;
	channelColor[40]= new Color(0,135,120);


	channelOn[50]=1;
	channelColor[50]= new Color(255,102,102);


	channelOn[53]=1;
	channelColor[53]= new Color(102,255,102);


	channelOn[43]=1;
	channelColor[43]= new Color(204,0,151);

	channelOn[97]=1;
	channelColor[97]= new Color(200,65,71);

	channelOn[103]=1;
	channelColor[103]= new Color(223,190,128);

	channelOn[107]=1;
	channelColor[107]= new Color(234,234,186);


	channelOn[203]=1;
	channelColor[203]= new Color(234,234,186);

	channelOn[212]=1;
	channelColor[212]= new Color(234,234,186);


	channelOn[221]=1;
	channelColor[221]= new Color(102,0,204);

	channelOn[250]=1;
	channelColor[250]= new Color(187,75,61);

	myname="";
	myPartner="";
	autoexamspeed=6000;
	autoexam=0;
	autoexamnoshow=1;
	password=0;
}



void setupMenu()
{

	rightClickMenu.add("Finger");
	rightClickMenu.add("Finger -n");
	rightClickMenu.add("Vars");
	rightClickMenu.add("Google");
	rightClickMenu.add("Match");
	rightClickMenu.add("Assess");
	rightClickMenu.add("Pstat");
	rightClickMenu.add("Games");
	rightClickMenu.add("Observe");
	rightClickMenu.add("Follow");
	rightClickMenu.add("History");
	rightClickMenu.add("Liblist");
	rightClickMenu.add("Stored");
	rightClickMenu.add("Hyperlink");
	rightClickMenu.add("Direct tells of");
	rightClickMenu.add("Channel Notify");
	rightClickMenu.add("Tell");

}

boolean getNotifyControllerState(String watchName)
{
	for(int a=0; a<notifyControllerScript.size(); a++)
	if(notifyControllerScript.get(a).equals(watchName))
		return true;

	return false;
}
void setNotifyControllerState()
{
	FileWrite writer = new FileWrite();

	String s="";
	for(int a=0; a<notifyControllerScript.size(); a++)
		s= s + notifyControllerScript.get(a) + "\r\n";

	writer.write(s, notifyControllerFile);

}




class seekData {
int minseek;
int maxseek;
int time;
int inc;
boolean rated;
boolean manual;
boolean formula;
int wild;
int color;
boolean saveSettings;

	seekData()
	{
		minseek =0;
		maxseek =9999;
		time=10;
		inc=0;
		rated = true;
		manual = false;
		formula = false;
		wild=0;
                color=0;
                saveSettings=true;
	}

}

class preselectedBoards {
	Color [] light;
	Color [] dark;
	int colorTop;
	preselectedBoards()
	{
		colorTop=4;
		light = new Color[colorTop];
		dark = new Color[colorTop];

		// default
		light[0]=new Color(255, 255, 255);
		dark[0]= new Color(71,203,211);

		// tan
		light[1]=new Color(204, 204, 128);
		dark[1]= new Color(204,139,61);

		// gray
		light[2]=new Color(255, 255, 255);
		dark[2]= new Color(150,150,150);

		// green blitzin
		light[3]=new Color(240, 240, 224);
		dark[3]= new Color(0,160,128);





	}
}// end class presleect5ed boards

class shoutRouting {
 int shoutsConsole;
 int sshoutsConsole;
 int announcementsConsole;

 boolean shoutsOnMain;
 boolean sshoutsOnMain;
 boolean announcementsOnMain;

 shoutRouting()
 {
  shoutsConsole=0;
  sshoutsConsole=0;
  announcementsConsole=0;
  shoutsOnMain=false;
  sshoutsOnMain=false;
  announcementsOnMain=false;

 }

}

class channelTabInfo
{
Font tabFont;
Color tellcolor;
Color qtellcolor;
Color ForColor;
Color BackColor;
Color responseColor;
Color shoutcolor;
Color sshoutcolor;
Color timestampColor;
int tellStyle;
int qtellStyle;
int ForStyle;
int BackStyle;
int responseStyle;
int shoutStyle;
int sshoutStyle;

boolean typed;
boolean told;

channelTabInfo()
{
tabFont=null;
tellcolor=null;
qtellcolor=null;
ForColor=null;
BackColor=null;
responseColor=null;
shoutcolor=null;
sshoutcolor=null;
timestampColor=null;
typed=true;
told=true;
tellStyle=0;
qtellStyle=0;
ForStyle=0;
BackStyle=0;
responseStyle=0;
shoutStyle=0;
sshoutStyle=0;

}// channel tab info constructor
}// end class channel tab info


class boardSizes
{
	Point point0;

	int con0x;
	int con0y;

	boardSizes()
	{
		point0= new Point();
		 point0.x = -1;
		 point0.y = -1;
		con0x = -1;
		con0y= -1;

	}
}// end class boardSizes;

class mineScoresGroup
{

	highScoreManagement score9by9;
	highScoreManagement score16by16;
	highScoreManagement score16by30;
	highScoreManagement scoreCustom;

	mineScoresGroup()
	{
		score9by9 = new highScoreManagement();
		score16by16 = new highScoreManagement();
		score16by30 = new highScoreManagement();
		scoreCustom = new highScoreManagement();


	}
}





}// end class channels

class channelNotifyClass
{
	ArrayList<String> nameList=new ArrayList();
	String channel;
}
class FileWrite
{

   void write(String s, String aFile)
   {

    		// Create file
    		try {
					FileWriter fstream = new FileWriter(aFile);
					write2(fstream, s);
				}
			catch(Exception e)
			{
				}// end outer catch

  }// end method

void write2(FileWriter fstream, String s)
{
	       try {
			   BufferedWriter out = new BufferedWriter(fstream);
	    		out.write(s);
	    		//Close the output stream
	    		out.close();
			}
			catch(Exception e)
			{  }

}

}// end class

class lanternNotifyClass {
 
 String name="";
 boolean sound = false;


}

