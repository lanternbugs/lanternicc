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
boardSizes mySeekSizes;
ActivitiesWindowPanel activitiesPanel;
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
JMenu myWindows;
JMenuItem [] openBoards;
boolean hasSettings=false;
boolean randomArmy=false;
boolean randomBoardTiles=false;
boolean gameend=false;
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
boolean [] excludedPiecesBlack;
boolean [] excludedPiecesWhite;
boolean highlightMoves;
boolean makeSounds;
boolean makeObserveSounds;
boolean engineOn;
boolean tabsOnly;
boolean standAlone;
boolean pgnLogging;
boolean pgnObservedLogging=false;
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
boolean autoHistoryPopup;
boolean activitiesOpen;
boolean seeksOpen;
boolean activitiesNeverOpen=false;
boolean showMaterialCount;
boolean showRatings;
boolean showFlags;
boolean showPallette;
boolean notifyMainAlso = false;

  boolean andreysFonts = false;
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
boolean useTopGames=false;
boolean useLightBackground=false;
boolean basketballFlag=false;
boolean autoChat = true;
boolean blockSays = false;
boolean lowTimeColors = false;
boolean newObserveGameSwitch = true;
boolean saveNamePass = false;
boolean drawCoordinates = true;
boolean ActivitiesOnTop=true;
boolean unobserveGoExamine=false;
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
ArrayList<told> pingNames = new ArrayList();

ArrayList<String> [] comboNames;
ArrayList<Image> flagImages = new ArrayList();
ArrayList<String> flagImageNames = new ArrayList();


F9Management F9Manager;

ImageIcon observeIcon;
ImageIcon wasIcon;
ImageIcon playingIcon;
ImageIcon examiningIcon;
ImageIcon sposIcon;
ImageIcon gameIcon;
ImageIcon pure1;
ImageIcon pure3;
ImageIcon pure5;
ImageIcon pure15;
ImageIcon pure45;
ImageIcon pure960;
ImageIcon seekIcon;
ImageIcon activitiesIcon;

String [] userButtonCommands = new String[maxUserButtons];
String [] tabTitle;
String [] consoleTabTitles;
String [] consoleTabCustomTitles;
String wallpaperFileName;
String lasttell;
String myname;// my login on the server
String mypassword; // used if we need to open a secure web page
String myPartner; // my bughouse partner
String myopponent;
String myServer; // FICS or ICC
String version; // current version of this build i.e. v1.48
String chessclubIP;
String operatingSystem;
String notifyControllerFile;
String defaultpgn;

//String countryNames = "AF;AFGHANISTAN;AX;ÅLAND ISLANDS;AL;ALBANIA;DZ;ALGERIA;AS;AMERICAN SAMOA;AD;ANDORRA;AO;ANGOLA;AI;ANGUILLA;AQ;ANTARCTICA;AG;ANTIGUA AND BARBUDA;AR;ARGENTINA;AM;ARMENIA;AW;ARUBA;AU;AUSTRALIA;AT;AUSTRIA;AZ;AZERBAIJAN;BS;BAHAMAS;BH;BAHRAIN;BD;BANGLADESH;BB;BARBADOS;BY;BELARUS;BE;BELGIUM;BZ;BELIZE;BJ;BENIN;BM;BERMUDA;BT;BHUTAN;BO;BOLIVIA, PLURINATIONAL STATE OF;BQ;BONAIRE, SINT EUSTATIUS AND SABA;BA;BOSNIA AND HERZEGOVINA;BW;BOTSWANA;BV;BOUVET ISLAND;BR;BRAZIL;IO;BRITISH INDIAN OCEAN TERRITORY;BN;BRUNEI DARUSSALAM;BG;BULGARIA;BF;BURKINA FASO;BI;BURUNDI;KH;CAMBODIA;CM;CAMEROON;CA;CANADA;CV;CAPE VERDE;KY;CAYMAN ISLANDS;CF;CENTRAL AFRICAN REPUBLIC;TD;CHAD;CL;CHILE;CN;CHINA;CX;CHRISTMAS ISLAND;CC;COCOS (KEELING) ISLANDS;CO;COLOMBIA;KM;COMOROS;CG;CONGO;CD;CONGO, THE DEMOCRATIC REPUBLIC OF THE;CK;COOK ISLANDS;CR;COSTA RICA;CI;CÔTE D'IVOIRE;HR;CROATIA;CU;CUBA;CW;CURAÇAO;CY;CYPRUS;CZ;CZECH REPUBLIC;DK;DENMARK;DJ;DJIBOUTI;DM;DOMINICA;DO;DOMINICAN REPUBLIC;EC;ECUADOR;EG;EGYPT;SV;EL SALVADOR;GQ;EQUATORIAL GUINEA;ER;ERITREA;EE;ESTONIA;ET;ETHIOPIA;FK;FALKLAND ISLANDS (MALVINAS);FO;FAROE ISLANDS;FJ;FIJI;FI;FINLAND;FR;FRANCE;GF;FRENCH GUIANA;PF;FRENCH POLYNESIA;TF;FRENCH SOUTHERN TERRITORIES;GA;GABON;GM;GAMBIA;GE;GEORGIA;DE;GERMANY;GH;GHANA;GI;GIBRALTAR;GR;GREECE;GL;GREENLAND;GD;GRENADA;GP;GUADELOUPE;GU;GUAM;GT;GUATEMALA;GG;GUERNSEY;GN;GUINEA;GW;GUINEA-BISSAU;GY;GUYANA;HT;HAITI;HM;HEARD ISLAND AND MCDONALD ISLANDS;VA;HOLY SEE (VATICAN CITY STATE);HN;HONDURAS;HK;HONG KONG;HU;HUNGARY;IS;ICELAND;IN;INDIA;ID;INDONESIA;IR;IRAN, ISLAMIC REPUBLIC OF;IQ;IRAQ;IE;IRELAND;IM;ISLE OF MAN;IL;ISRAEL;IT;ITALY;JM;JAMAICA;JP;JAPAN;JE;JERSEY;JO;JORDAN;KZ;KAZAKHSTAN;KE;KENYA;KI;KIRIBATI;KP;KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF;KR;KOREA, REPUBLIC OF;KW;KUWAIT;KG;KYRGYZSTAN;LA;LAO PEOPLE'S DEMOCRATIC REPUBLIC;LV;LATVIA;LB;LEBANON;LS;LESOTHO;LR;LIBERIA;LY;LIBYAN ARAB JAMAHIRIYA;LI;LIECHTENSTEIN;LT;LITHUANIA;LU;LUXEMBOURG;MO;MACAO;MK;MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF;MG;MADAGASCAR;MW;MALAWI;MY;MALAYSIA;MV;MALDIVES;ML;MALI;MT;MALTA;MH;MARSHALL ISLANDS;MQ;MARTINIQUE;MR;MAURITANIA;MU;MAURITIUS;YT;MAYOTTE;MX;MEXICO;FM;MICRONESIA, FEDERATED STATES OF;MD;MOLDOVA, REPUBLIC OF;MC;MONACO;MN;MONGOLIA;ME;MONTENEGRO;MS;MONTSERRAT;MA;MOROCCO;MZ;MOZAMBIQUE;MM;MYANMAR;NA;NAMIBIA;NR;NAURU;NP;NEPAL;NL;NETHERLANDS;NC;NEW CALEDONIA;NZ;NEW ZEALAND;NI;NICARAGUA;NE;NIGER;NG;NIGERIA;NU;NIUE;NF;NORFOLK ISLAND;MP;NORTHERN MARIANA ISLANDS;NO;NORWAY;OM;OMAN;PK;PAKISTAN;PW;PALAU;PS;PALESTINIAN TERRITORY, OCCUPIED;PA;PANAMA;PG;PAPUA NEW GUINEA;PY;PARAGUAY;PE;PERU;PH;PHILIPPINES;PN;PITCAIRN;PL;POLAND;PT;PORTUGAL;PR;PUERTO RICO;QA;QATAR;RE;RÉUNION;RO;ROMANIA;RU;RUSSIAN FEDERATION;RW;RWANDA;BL;SAINT BARTHÉLEMY;SH;SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA;KN;SAINT KITTS AND NEVIS;LC;SAINT LUCIA;MF;SAINT MARTIN (FRENCH PART);PM;SAINT PIERRE AND MIQUELON;VC;SAINT VINCENT AND THE GRENADINES;WS;SAMOA;SM;SAN MARINO;ST;SAO TOME AND PRINCIPE;SA;SAUDI ARABIA;SN;SENEGAL;RS;SERBIA;SC;SEYCHELLES;SL;SIERRA LEONE;SG;SINGAPORE;SX;SINT MAARTEN (DUTCH PART);SK;SLOVAKIA;SI;SLOVENIA;SB;SOLOMON ISLANDS;SO;SOMALIA;ZA;SOUTH AFRICA;GS;SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS;ES;SPAIN;LK;SRI LANKA;SD;SUDAN;SR;SURINAME;SJ;SVALBARD AND JAN MAYEN;SZ;SWAZILAND;SE;SWEDEN;CH;SWITZERLAND;SY;SYRIAN ARAB REPUBLIC;TW;TAIWAN, PROVINCE OF CHINA;TJ;TAJIKISTAN;TZ;TANZANIA, UNITED REPUBLIC OF;TH;THAILAND;TL;TIMOR-LESTE;TG;TOGO;TK;TOKELAU;TO;TONGA;TT;TRINIDAD AND TOBAGO;TN;TUNISIA;TR;TURKEY;TM;TURKMENISTAN;TC;TURKS AND CAICOS ISLANDS;TV;TUVALU;UG;UGANDA;UA;UKRAINE;AE;UNITED ARAB EMIRATES;GB;UNITED KINGDOM;US;UNITED STATES;UM;UNITED STATES MINOR OUTLYING ISLANDS;UY;URUGUAY;UZ;UZBEKISTAN;VU;VANUATU;VE;VENEZUELA, BOLIVARIAN REPUBLIC OF;VN;VIET NAM;VG;VIRGIN ISLANDS, BRITISH;VI;VIRGIN ISLANDS, U.S.;WF;WALLIS AND FUTUNA;EH;WESTERN SAHARA;YE;YEMEN;ZM;ZAMBIA;ZW;ZIMBABWE;";
String countryNames = "AE;UAE;AF;Afghanistan;AL;Albania;DZ;Algeria;AS;American_Samoa;AD;Andorra;AO;Angola;AI;Anguilla;AG;Antigua_and_Barbuda;AR;Argentina;AM;Armenia;AW;Aruba;AU;Australia;AT;Austria;AZ;Azerbaijan;BS;Bahamas;BH;Bahrain;BD;Bangladesh;BB;Barbados;BY;Belarus;BE;Belgium;BZ;Belize;BJ;Benin;BM;Bermuda;BT;Bhutan;BO;Bolivia;BW;Botswana;BR;Brazil;BG;Bulgaria;BF;Burkina_Faso;BI;Burundi;KH;Cambodia;CM;Cameroon;CA;Canada;CV;Cape_Verde;KY;Cayman_Islands;CF;Central_African_Republic;TD;Chad;CL;Chile;CN;China;CX;Christmas_Island;CO;Colombia;KM;Comoros;CK;Cook_Islands;CR;Costa_Rica;HR;Croatia;CU;Cuba;CY;Cyprus;DK;Denmark;DJ;Djibouti;DM;Dominica;DO;Dominican_Republic;EC;Ecuador;EG;Egypt;SV;El_Salvador;GQ;Equatorial_Guinea;ER;Eritrea;EE;Estonia;ET;Ethiopia;FK;Falkland_Islands;FO;Faroe_Islands;FJ;Fiji;FI;Finland;FR;France;GA;Gabon;GM;Gambia;GE;Georgia;DE;Germany;GH;Ghana;GI;Gibraltar;GB;United_Kingdom;GR;Greece;GL;Greenland;GD;Grenada;GT;Guatemala;GN;Guinea;GW;Guinea_Bissau;GY;Guyana;HT;Haiti;HN;Honduras;HK;Hong_Kong;HU;Hungary;icc;icc;icc;icc1;IS;Iceland;IN;India;ID;Indonesia;IR;Iran;IQ;Iraq;IE;Ireland;IL;Israel;IT;Italy;JM;Jamaica;JP;Japan;JO;Jordan;KZ;Kazakhstan;KE;Kenya;KI;Kiribati;KR;South_Korea;KW;Kuwait;KG;Kyrgyzstan;LA;Laos;LV;Latvia;LB;Lebanon;LS;Lesotho;LR;Liberia;LY;Libya;LI;Liechtenstein;LT;Lithuania;LU;Luxembourg;MK;Macedonia;MG;Madagascar;MW;Malawi;MY;Malaysia;MV;Maldives;ML;Mali;MT;Malta;MH;Marshall_Islands;MR;Mauritania;MU;Mauritius;MX;Mexico;FM;Micronesia;MD;Moldova;MC;Monaco;MN;Mongolia;MS;Montserrat;MA;Morocco;MZ;Mozambique;MM;Myanmar;NA;Namibia;NR;Nauru;NP;Nepal;NL;Netherlands;AN;Netherlands_Antilles;NZ;New_Zealand;NI;Nicaragua;NE;Niger;NG;Nigeria;NU;Niue;NF;Norfolk_Island;NO;Norway;OM;Oman;PK;Pakistan;PW;Palau;PA;Panama;PG;Papua_New_Guinea;PY;Paraguay;PE;Peru;PH;Philippines;PL;Poland;PR;Puerto_Rico;PT;Portugal;PR;Puerto_Rico;QA;Qatar;RO;Romania;RS;Serbia_and_Montenegro;RU;Russian_Federation;RW;Rwanda;LC;Saint_Lucia;WS;Samoa;SM;San_Marino;SA;Saudi_Arabia;SN;Senegal;SC;Seychelles;SL;Sierra_Leone;SG;Singapore;SK;Slovakia;SI;Slovenia;SO;Somalia;ZA;South_Africa;ES;Spain;LK;Sri_Lanka;SD;Sudan;SR;Suriname;SZ;Swaziland;SE;Sweden;CH;Switzerland;SY;Syria;TW;Taiwan;TJ;Tajikistan;TZ;Tanzania;TH;Thailand;TG;Togo;TO;Tonga;TT;Trinidad_and_Tobago;TN;Tunisia;TR;Turkey;TM;Turkmenistan;TC;Turks_and_Caicos_Islands;TV;Tuvalu;UG;Uganda;UA;Ukraine;UK;United_Kingdom;US;United_States_of_America;UY;Uruguay;UZ;Uzbekistan;VU;Vanuatu;VE;Venezuela;VI;US_Virgin_Islands;VN;Vietnam;YE;Yemen;ZA;South_Africa;ZM;Zambia;ZW;Zimbabwe;";
String newUserMessage;
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
Font eventsFont;
Font analysisFont;
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
Color scrollhighlightcolor;
Color premovehighlightcolor;
Color activeTabForeground;
Color passiveTabForeground;
Color tabBackground;
Color tabBackground2;// the background if tab is unvisted on one window but not another
Color newInfoTabBackground;
Color tabImOnBackground;
Color tabBorderColor;
Color tellTabBorderColor;
Color boardBackgroundColor;
Color boardForegroundColor;
Color clockForegroundColor;
Color clockLow;
Color clockHigh;
Color timeForeground;
Color onmoveTimeForeground;

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
Color analysisForegroundColor;
Color analysisBackgroundColor;

StyledDocument [] mydocs;
StyledDocument [] mygamedocs = new StyledDocument[100];
StyledDocument engineDoc;
JButton [] mybuttons;

URL [] songs;
URL [] poweroutSounds;
int andreysLayout = 2; // Andrey's layout variable
int chatBufferSize=100000;
int chatBufferExtra=1000;
int showTenths;
int maxChannels = 500;
int chatFrame;
int visibleConsoles=0;
int maxSongs=100;
int maxConsoleTabs=12;
int soundBoard=0;
int soundGame=0;
int consoleLayout;
long autoexamspeed;
long lastSoundTime;// used to not send multiple sounds in say one second when forwarding through games
int lastSoundCount;
int [] channelOn= new int[maxChannels];
int [][] console = new int[maxConsoleTabs][maxChannels];
int [][] qtellController  = new int[maxConsoleTabs][maxChannels];
int [] style = new int[maxChannels];
int [] looking = new int[maxConsoleTabs];
int [] gamelooking = new int[100];
int [] tabLooking = new int[100];
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
int italicsBehavior=1; // for what ` ` does
int playersInMyGame=2; // playing and examining
channelTabInfo [] tabStuff = new channelTabInfo[maxConsoleTabs];
ConcurrentLinkedQueue<myoutput> engineQueue;
OutputStream engineOut;
File engineFile;
File wallpaperFile;
preselectedBoards preselectBoards;

Point webframePoint;
JTextPane engineField = new JTextPane();

channels()
{
myServer = "ICC";
version = "v4.80";
newUserMessage="Welcome to Lantern Chess! You will stop seeing this message when you go to file/ save settings or save settings on exit. Be sure to check out the Windows Menu for items like Activities Window ( has the Lantern event list), and also in the Windows Menu, the Seek Graph. Check out the Help Menu for the Lantern Manual menu item to learn more about this program. Inputing from the game console Alt + C, toggles the game console size.\n";

F9Manager = new F9Management();
mineScores = new mineScoresGroup();
Looking = new int[100];
try { engineDoc=engineField.getStyledDocument(); } catch(Exception enginestuff){}
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
excludedPiecesWhite = new boolean[dummyUse.maxPieces - 1];
excludedPiecesBlack = new boolean[dummyUse.maxPieces - 1];

for(int excl = 0; excl < dummyUse.maxPieces - 1; excl++)
{
  excludedPiecesWhite[excl]=false;
  excludedPiecesBlack[excl]=false;
}

noidle=false;
standAlone = true;
chessclubIP = "207.99.83.228";
notifyControllerFile = "lantern_notify_controler.ini";
boardType=2;
pieceType=1;
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
makeObserveSounds=true;
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

if(operatingSystem.equals("mac"))
autoHistoryPopup=true;
else
autoHistoryPopup=false;

activitiesOpen=false;
seeksOpen = false;
showMaterialCount=true;
showRatings=true;
showFlags=true;
showPallette=true;

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

mySeekSizes = new boardSizes();
mySeekSizes.point0.x=50;
mySeekSizes.point0.y=50;
mySeekSizes.con0x=600;
mySeekSizes.con0y=600;



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

toolbarVisible=true;
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
tabsOnly=true;
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
defaultpgn="";
tellconsole=0;
tellTab=0;
tellsToTab=false;
updateTellConsole=0;
tabBorderColor = new Color(235,0,0);
tellTabBorderColor = new Color(0, 235, 0);
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
    tabBackground=new Color(255,255,255);//204,255,255);// was 204 204 204 a gray
    tabBackground2=new Color(204,255,255);
   highlightcolor = new Color(230,0,10);
   scrollhighlightcolor = new Color(255,102,102);
premovehighlightcolor = new Color(10,0,230);

    //newInfoTabBackground=new Color(150,145,130);
    //newInfoTabBackground=tabBackground.brighter();
	newInfoTabBackground=new Color(0, 204, 255);//200,145,130);
	tabImOnBackground = new Color(51, 133, 255); //255, 255, 0);
//myFont = new Font("Comic Sans MS", Font.PLAIN, 18);
try {

    eventsFont = new Font("Tahoma", Font.PLAIN, 14);
}
catch(Exception badEventsFont){}
try {
analysisFont = new Font("Times New Roman", Font.PLAIN, 12);

}
catch(Exception badanalysisfont){}

if(operatingSystem.equals("unix"))
	myFont = new Font("Andale Mono", Font.PLAIN, 18);
else if(operatingSystem.equals("mac"))
	myFont = new Font("Andale Mono", Font.BOLD, 14);
else
	myFont = new Font("Lucida Console", Font.PLAIN, 18);
try {
	myGameFont = new Font("Times New Roman", Font.PLAIN, 20);
	myGameClockFont = new Font("Arial", Font.PLAIN, 40);

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
MainBackColor = new Color(204,255,255);
BackColor = new Color(0,0,0);
//boardForegroundColor = new Color(0,0,0);
//boardBackgroundColor = new Color(235,223,236);
boardForegroundColor = new Color(60,60,60);
boardBackgroundColor = new Color(255, 255, 255);
clockLow = new Color(255,0,0);
clockHigh = new Color(0,255,0);
timeForeground = new Color(0,0,0);

onmoveTimeForeground = new Color(255,255,255);
/*onMoveBoardBackgroundColor = new Color(100, 203, 203);
*/

clockForegroundColor = new Color(0,0,255);

onMoveBoardBackgroundColor = new Color(205, 205, 205);

inputCommandColor = new Color(0,0,0);
inputChatColor= new Color(130,57,0);
chatTimestampColor = new Color(40,200,40);
listColor = new Color(204, 204, 204);
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
tellNameColor=new Color(255,255,153);
nameForegroundColor = new Color(51, 51, 0);
nameBackgroundColor = new Color(255,255,204);

analysisForegroundColor =  new Color(51, 51, 0);
analysisBackgroundColor =  new Color(255,255,204);

// my original tan board
//lightcolor= new Color(255, 204, 204);
//darkcolor = new Color(193,153,153);
// current white/ blue board

openBoards = new JMenuItem[maxGameTabs];

for(int b=0; b<maxConsoleTabs; b++)
for(int a=0; a<maxChannels; a++)
{console[b][a]=0;
 qtellController[b][a]=0;
 }
for(int a=0; a<maxChannels; a++)
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

 	channelOn[300]=1;
	channelColor[300]= new Color(255,0,0);

 	channelOn[400]=1;
	channelColor[400]= new Color(255,0,0);


	myname="";
	mypassword="";
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


void openUrl(String myurl)
{
// mac fix replace %0D at end with empty
if(myurl.endsWith("\r")) 
myurl=myurl.trim();
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

String getConnectNotifyOnline()
{
String mess2 = "People on connect notify online:\n   ";
String mess = "";
ArrayList<String> realnames = new ArrayList();

for(int z=0; z<lanternNotifyList.size(); z++)
{

for(int x=0; x < channelNamesList.size(); x++)
for(int xx=0; xx < channelNamesList.get(x).model2.size(); xx++)

{
try {
 String temp = (String) channelNamesList.get(x).model2.elementAt(xx);

if( lanternNotifyList.get(z).name.toLowerCase().equals(temp.toLowerCase()))
{
    String realName= temp;
    boolean go=true;
    for(int m=0; m < realnames.size(); m++)
    if(realnames.get(m).equals(temp))
    go=false;
    if(go==true)
    {
    mess+=realName + " ";;
    realnames.add(realName);
    }

}// end if match
}catch(Exception dui){}

}// end for
} // end outer for
if(!mess.equals(""))
return mess2 + mess + "\n";

return "";


}// end get connect notify online


String getChannelNotifyOnline()
{
 String mess="People on channel notify online:\n   ";
ArrayList<String> realnames = new ArrayList();
for(int z=0; z < channelNotifyList.size(); z++)
if(channelNotifyList.get(z).nameList.size()>0)
{

String channel=channelNotifyList.get(z).channel;

for(int x=0; x < channelNotifyList.get(z).nameList.size(); x++)
{
try {


for(int yy =0 ; yy < channelNamesList.size(); yy++)
{ if(Integer.parseInt(channelNamesList.get(yy).channel) == Integer.parseInt(channel))
  {
    int chan=yy;
for(int y=0; y < channelNamesList.get(chan).model2.size(); y++)
{
  String temp = (String) channelNamesList.get(chan).model2.elementAt(y);
if(channelNotifyList.get(z).nameList.get(x).toLowerCase().equals(temp.toLowerCase()))
  {
    String realName= temp;
    boolean go=true;
    for(int m=0; m < realnames.size(); m++)
    if(realnames.get(m).equals(temp))
    go=false;
    if(go==true)
    {
    mess+=realName + " ";;
    realnames.add(realName);
    }

  }// end if a match
} // end y for
}// end if
}// end yy loop
}catch(Exception dui){}

}// end for
} // end outer for


return mess + "\n";

} // end channel notify online


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
  void writeAppend(String s, String aFile)
   {

    		// Create file
    		try {
					FileWriter fstream = new FileWriter(aFile, true);
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

