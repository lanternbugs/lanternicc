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
//import java.awt.event.*;
import javax.swing.*;
//import javax.swing.JDialog;
import java.io.*;
import java.net.*;
import java.lang.Thread.*;
//import java.applet.*;
//import javax.swing.GroupLayout.*;
//import javax.swing.colorchooser.*;
//import javax.swing.event.*;
import java.lang.Integer;
import javax.swing.text.*;
//import java.awt.geom.*;
//import java.awt.image.BufferedImage;
//import java.applet.*;
//import java.awt.event.*;
//import java.awt.image.*;
//import javax.imageio.ImageIO;
import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.StringTokenizer;
//import java.util.concurrent.locks.*;
//import java.util.Timer;
//import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
//import javax.swing.event.ChangeEvent.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

class runningengine implements Runnable
{
  
  class PrincipalVariation {
 String line;
 String depth;
 String score;
 String multipv;
PrincipalVariation()
 {
   line = "";
   depth = "";
   score = "";
   multipv = "";
} // end constructor
 }// end inner class
 int cachedMultipleLines = 1;
ArrayList<PrincipalVariation> multiLines = new ArrayList();
ArrayList<String> scriptList = new ArrayList();
scriptLoader scripter = new scriptLoader();
double lastSendTime;
double priorSendTime;
double lastCheckTime;
int movesInTenSeconds;
String lastWinboardLine;
channels sharedVariables;
int BoardIndex;
JTextPane [] gameconsoles;
gamestuff gameData;
String pretext="";
runningengine(channels sharedVariables1, int board, JTextPane [] gameconsoles1, gamestuff gameData1)
{
	gameconsoles=gameconsoles1;
	sharedVariables=sharedVariables1;
	BoardIndex=board;
	gameData=gameData1;
}


void sendToEngine(String output)
{
	byte [] b2 = new byte[2500];
	try {
		for(int a=0; a< output.length(); a++)
	b2[a]=(byte) output.charAt(a);
	sharedVariables.engineOut.write(b2,0, output.length());
sharedVariables.engineOut.flush();

//engineOut.write(b2,0, output.length());
//engineOut.flush();
}
catch(Exception e) {}
}


Process engine;
void sendWildVariant()
{
/*
wildcastle	Shuffle chess where king can castle from d file
nocastle	Shuffle chess with no castling at all
fischerandom	Fischer Random
bughouse	Bughouse, ICC/FICS rules
crazyhouse	Crazyhouse, ICC/FICS rules
losers	Win by losing all pieces or getting mated (ICC)
suicide	Win by losing all pieces including king, or by having fewer pieces when one player has no legal moves (FICS)
giveaway	Win by losing all pieces including king, or by having no legal moves (ICC)
twokings	Weird ICC wild 9
kriegspiel	Kriegspiel (engines not supported)
atomic	Atomic
3check	Win by giving check 3 times
*/


if(sharedVariables.mygame[BoardIndex].wild == 28)
sendToEngine("variant shatranj\n");
else if(sharedVariables.mygame[BoardIndex].wild == 27)
sendToEngine("variant atomic\n");
else if(sharedVariables.mygame[BoardIndex].wild == 26)
sendToEngine("variant giveaway\n");
else if(sharedVariables.mygame[BoardIndex].wild == 25)
sendToEngine("variant 3check\n");
else if(sharedVariables.mygame[BoardIndex].wild == 23)
sendToEngine("variant crazyhouse\n");
else if(sharedVariables.mygame[BoardIndex].wild == 22)
sendToEngine("variant fischerandom\n");
else if(sharedVariables.mygame[BoardIndex].wild == 17)
sendToEngine("variant losers\n");

}
void intializeNewEngineGame()
{
	sendToEngine("new\n");
	sendToEngine("level 0 1 1\n");
	sendToEngine("post\n");
sendToEngine("hard\n");
sendWildVariant();
	sendToEngine("force\n");
	sendToEngine("analyze\n");

for(int a=0; a< sharedVariables.mygame[BoardIndex].engineTop; a++)// if they start analyzing in the middle of an examined game
sendToEngine(sharedVariables.mygame[BoardIndex].getEngineMove(a));
//sendToEngine(".\n");

//sendToEngine("e4\n");
}
OutputStream engineOut;

public void run()
	{


try {
//InputStream is= sharedVariables.engine.getInputStream();

			 		Runtime rt;
			 		rt = Runtime.getRuntime();
			 	//sharedVariables.engine = rt.exec(file.toString());
			 	if(sharedVariables.engineFile.toString().endsWith(".jar")) {
                                      String fileToOpen = "java -jar " + sharedVariables.engineFile.toString();
                                      engine = rt.exec(fileToOpen);
                                 }
                                 else {
                                   engine = rt.exec(sharedVariables.engineFile.toString());
                                 }


			 	sharedVariables.engineOn = true;
try {
 StyledDocument doc = sharedVariables.engineDoc;
doc.remove(0, doc.getLength());
}
catch(Exception ee) {
}

if(sharedVariables.uci == false)
{

  writeOut("Trying to run Winboard Engine. As a rule most engins like Rybka, Stockfish, Houdini and Komodo are UCI Engines. Crafty is an example of a Winboard Engine.\n");
  runWinboard();
}
else
runUci();


} // end try
catch(Exception e)
{ 
try {
if(sharedVariables.uci == false)
writeOut("There was an error starting the engine. Is the file a valid engine executable? Is it a Winboard Engine?\n");
else 
writeOut("There was an error starting the engine. Is the file a valid engine executable? Is it a UCI Engine?\n");
}
catch(Exception ee) {
}

}




}// end method


void runWinboard()
{
try {

int go=1;
InputStream is= engine.getInputStream();

byte [] b = new byte[15000];


 InputStreamReader converter = new InputStreamReader(is);
BufferedReader in = new BufferedReader(converter);
//sharedVariables.engineOut=sharedVariables.engine.getOutputStream();
sharedVariables.engineOut=engine.getOutputStream();
//engineOut=engine.getOutputStream();
sendToEngine("xboard\nprotover 2\n");




//sendToEngine("move e2e4\n");
//sendToEngine("move d7d5\n");
String text="";
lastWinboardLine = "";
do {

if(in.ready())
text=in.readLine();


if(text.contains("feature"))
{
	int i=0, k=-1;
	while(i>-1)
	{

	i=text.indexOf(" ", i);
	if(i>-1)
	{
		int j=text.indexOf("=", i);
		if(j>-1)
		{
				// accept feature
			String temp = "";
			temp=text.substring(i+1, j);
			//if(temp.contains("ping") || temp.contains("sans"))
			//sendToEngine("rejected " + temp + "\n");
			//else
			sendToEngine("accepted " + temp + "\n");
			i=j;

		}
	}

	}


}

	if(text.contains("done"))
	intializeNewEngineGame();

myoutput tosend = new myoutput();
try {
tosend=sharedVariables.engineQueue.poll();// we look for data from other areas of the program
if(tosend!=null)
{
	sendToEngine(tosend.data);
	lastWinboardLine = "";
	if(tosend.data.contains("quit"))
	go=0;
}
}
catch(Exception e) {

}
/*
if(text.length() > 0 && !text.startsWith("#") && !text.startsWith("stat"))
{

try {
//StyledDocument doc = sharedVariables.mygamedocs[BoardIndex];
 StyledDocument doc = sharedVariables.engineDoc;

doc.insertString(doc.getLength(), text + "\n", null);
for(int a=0; a<sharedVariables.openBoardCount; a++)
if(sharedVariables.gamelooking[a]==BoardIndex)
{

 if((sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_EXAMINING || sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_OBSERVING) && sharedVariables.engineOn == true)
 if(sharedVariables.mygame[sharedVariables.gamelooking[a]].clickCount %2 == 0)
setEngineDoc(doc, a);
}

}
catch(Exception e)
{}
}// end if
 */
 
 writeOutWinboard(text);







Thread.sleep(35);
}
while(go==1);
}// end try
catch(Exception e){}
}// end run winboard

void setEngineDoc(StyledDocument doc, int a)
{
gameconsoles[a].setStyledDocument(doc);
gameconsoles[a].setFont(sharedVariables.analysisFont);
gameconsoles[a].setForeground(sharedVariables.analysisForegroundColor);
gameconsoles[a].setBackground(sharedVariables.analysisBackgroundColor);

}
void runUci()
{
try {

int go=1;
lastSendTime = 0;
priorSendTime = 0;
lastCheckTime = 0;
movesInTenSeconds = 0;
InputStream is= engine.getInputStream();

byte [] b = new byte[15000];


 InputStreamReader converter = new InputStreamReader(is);
BufferedReader in = new BufferedReader(converter);
//sharedVariables.engineOut=sharedVariables.engine.getOutputStream();
sharedVariables.engineOut=engine.getOutputStream();
//engineOut=engine.getOutputStream();

pgnWriter pgnGetter = new pgnWriter();


int stage=0;

//sendToEngine("move e2e4\n");
//sendToEngine("move d7d5\n");
String text="";
do {
text = "";
if(in.ready())
text=in.readLine();


if(stage == 0)
{
	sendToEngine("uci\n");
	sendToEngine("setoption UCI_ShowCurrLine 1\n");
	stage++;
}

if(stage == 1 && text.contains("uciok"))
{
	sendToEngine("isready\n");
	stage++;

}
if(stage == 2 && text.contains("readyok"))
{


	sendToEngine("setoption name UCI_AnalyseMode value true\n");
	//sendToEngine("setoption name MultiPV value 3\n");
        scriptList.clear();
        scripter.loadScript(scriptList, "lantern_uci_script.txt");
        for(int scripts = 0; scripts < scriptList.size(); scripts++)
        {
        String scriptLine = scriptList.get(scripts).trim();
        if(scriptLine.contains("MultiPV") && (sharedVariables.uciMultipleLines == 2 || sharedVariables.uciMultipleLines == 3))
        ;
        else
	sendToEngine(scriptLine + "\n");

        }
        if(sharedVariables.uciMultipleLines == 2 || sharedVariables.uciMultipleLines == 3)
        {
          sendToEngine("setoption name MultiPV value " + sharedVariables.uciMultipleLines +  "\n");
        }
        cachedMultipleLines = sharedVariables.uciMultipleLines;

if(sharedVariables.mygame[BoardIndex].engineFen.length()>2)
{
    sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen = fixFenIfNeeded(sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen);
    sendToEngine("position fen " + sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen + "\n");
	writeOut("position fen " + sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen + "\n");

}
else
{
  sendToEngine("position startpos\n");
}
	sendToEngine("go infinite\n");

// if they start analyzing in the middle of an examined game
writeOut("Engine Top is " + sharedVariables.mygame[BoardIndex].engineTop);
if(sharedVariables.mygame[BoardIndex].engineTop > 0)
{
	sendToEngine("stop\n");
	writeOut("stop\n");

if(sharedVariables.mygame[BoardIndex].engineFen.length()>2)
{
   if(!sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen.startsWith("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"))
   sendToEngine("ucinewgame\n");
    sendToEngine("position fen " + sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen + sharedVariables.mygame[BoardIndex].getUciMoves());
	writeOut("position fen " + sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen + sharedVariables.mygame[BoardIndex].getUciMoves());

}
else
{
    sendToEngine("position startpos" + sharedVariables.mygame[BoardIndex].getUciMoves());
	writeOut("position startpos" + sharedVariables.mygame[BoardIndex].getUciMoves());
}

        multiLines.clear();
	sendToEngine("go infinite\n");
	writeOut("go infinite\n");
	writeOut("engine fen is " + sharedVariables.mygame[BoardIndex].engineFen + " and board index is " + BoardIndex + " \n");
}



	stage++;
}


if(stage == 3)
{
myoutput tosend = new myoutput();
String finalStuff="";

try {
tosend=sharedVariables.engineQueue.poll();// we look for data from other areas of the program
while(tosend!=null)
{
//	sendToEngine(tosend.data);
finalStuff=tosend.data;

	if(tosend.data.contains("quit"))
	{               if(sharedVariables.engineFile.toString().endsWith(".jar")) {
                        sendToEngine("stop\n");
                        }
			sendToEngine(finalStuff);
			finalStuff="";
			go=0;
			writeOut("sent quit");

		break;
	}
if(finalStuff.length() > 0)
{

	tosend=sharedVariables.engineQueue.poll();// we look for data from other areas of the program
	
        if(tosend != null && tosend.data.contains("stop") && tosend.data.contains("go infinite") &&
        finalStuff.contains("stop") && finalStuff.contains("go infinite"))
        {
          ;  // do nothing current data is redundant to next data i.e. stop start stop start
        }
        else 
        {
        	try {
		
                if(finalStuff.contains("stop") && finalStuff.contains("go infinite"))
                {
                  writeOut("sending new move to Engine\n");
                }
                else 
                {
                writeOut("final stuff lenght > 0 and sending" + finalStuff);
                }

	}
	catch(Exception badright){}

        try {
         
         if(System.currentTimeMillis()> lastSendTime + 3000 && System.currentTimeMillis()> priorSendTime + 7000 && tosend != null)
          Thread.sleep(1500);
          else if(tosend != null)
            Thread.sleep(3500);
            else
            Thread.sleep(375);


        
        if(System.currentTimeMillis()> lastCheckTime + 10000)
        {
          lastCheckTime = System.currentTimeMillis();
          movesInTenSeconds = 0;
        }
        
        movesInTenSeconds ++;
        if(movesInTenSeconds > 10)
        {
          lastCheckTime = System.currentTimeMillis();
          movesInTenSeconds = 0;
           Thread.sleep(3000);

        }
         } catch(Exception E5){}
        if(tosend == null)
        {
         tosend=sharedVariables.engineQueue.poll();// we look for data from other areas of the program
	
        if(tosend != null && tosend.data.contains("stop") && tosend.data.contains("go infinite") &&
        finalStuff.contains("stop") && finalStuff.contains("go infinite"))
        {
          continue;
        }

        }// to send null

        sendToEngine(finalStuff);
        priorSendTime = lastSendTime;
        lastSendTime = System.currentTimeMillis();
        }
}








}// end if tosent not null first time


}
catch(Exception e) {
writeOut("Excption in state 3\n");
}
}//end if stage 3

if(text.length() > 0 && ((text.contains(" pv") && stage ==3) || stage<3))
{
try {
if(text.startsWith("info") && (text.contains(" pv") && !text.contains("info currmove") && stage ==3))
{

        // routine for those who print pv twice
        int tryone=text.indexOf(" pv");
        int trytwo = text.indexOf(" pv", tryone + 1);

        String line1="";
        String line2="";
        if(trytwo != -1)
        {
       line1 = text.substring(0, trytwo);
       line2 = text.substring(trytwo + 4, text.length());


        }
        else
        {
        line1 = text.substring(0, text.indexOf(" pv"));
	line2 = text.substring(text.indexOf(" pv") + 4, text.length());
         }
        line2=pgnGetter.getPgn(line2, sharedVariables.mygame[gameData.BoardIndex].iflipped, sharedVariables.mygame[gameData.BoardIndex].board);
	if(text.contains("multipv"))
	{
          parseMultiPV(text, line2, true);
        }
         else
         {
           parseMultiPV(text, line2, false);
         }



}
else
writeOut(text);
}
catch(Exception badone){
writeOut("Excption bad one\n");
}
}// end if

try {
  if(text.length() == 0) {
      Thread.sleep(30);
  }

}
catch(Exception E5){}



}
while(go==1);
writeOut("go no longer 1\n");
}// end try
catch(Exception e){writeOut("exception terminated loop");}
}// end run uci


String fixFenIfNeeded(String fen)
{
  try {
  if(fen.contains(" KQkq")) {
     int [] board = sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].board;
     boolean go = false;
     if(board[0] != 4 && board[0] != 10)
         go = true;
     if(board[7] != 4 && board[7] != 10)
         go = true;
     if(board[56] != 4 && board[56] != 10)
         go = true;
     if(board[63] != 4 && board[63] != 10)
         go = true;
  if(go) {
    fen = fen.replace(" KQkq", "");
  }
  } // end try
  } catch(Exception dui) {}

   return fen;
}
void writeOut(String text)
{
try {

// if(!sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen.equals(""))
// text+="\n" + sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen  + "\n";
//StyledDocument doc = sharedVariables.mygamedocs[BoardIndex];
 StyledDocument doc = sharedVariables.engineDoc;

doc.insertString(doc.getLength(), text + "\n", null);
for(int a=0; a<sharedVariables.openBoardCount; a++)
if(sharedVariables.gamelooking[a]==BoardIndex)
{
 if((sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_EXAMINING || sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_OBSERVING) && sharedVariables.engineOn == true)
 if(sharedVariables.mygame[sharedVariables.gamelooking[a]].clickCount %2 == 1)
setEngineDoc(doc, a);

//gameconsoles[a].setStyledDocument(doc);
}

}
catch(Exception e)
{}

}// end writeout method
void writeOutWinboard(String text)
{
text = text.trim();
text = text.replace("          ", " ");
text = text.replace("         ", " ");
text = text.replace("        ", " ");
text = text.replace("       ", " ");
text = text.replace("      ", " ");
text = text.replace("     ", " ");
text = text.replace("    ", " ");
text = text.replace("   ", " ");
text = text.replace("  ", " ");

String depth = "";
String score = "";
int i = 0;
try {

          StringTokenizer tokens = new StringTokenizer(text, " ");
          String temp;


             int num;
             depth = tokens.nextToken();
             i++;
             num = Integer.parseInt(depth);
             score = tokens.nextToken();
             i++;
             num = Integer.parseInt(score);
             score = formatScore(score, false);
             temp = tokens.nextToken();
             i++;
             num = Integer.parseInt(temp);

             temp = tokens.nextToken();
             i++;
             num = Integer.parseInt(temp);
             temp = tokens.nextToken();
             i++;
}
catch(Exception dui){}
try {
if(i == 5)
{

int space = text.indexOf(" ");
space = text.indexOf(" ", space+1);
space = text.indexOf(" ", space+1);
space = text.indexOf(" ", space+1);
String line = text.substring(space, text.length());
String scoreLine = "Depth: " + depth + " Score: " + score + "\n";
String winboardLine = scoreLine + line + "\n";


if(!winboardLine.equals(lastWinboardLine) && !winboardLine.equals(""))
{
lastWinboardLine = winboardLine;
StyledDocument doc = sharedVariables.engineDoc;

doc.remove(0, doc.getLength());
//doc.insertString(doc.getLength(), text + "\n", null);
doc.insertString(doc.getLength(), winboardLine, null);
for(int a=0; a<sharedVariables.openBoardCount; a++)
if(sharedVariables.gamelooking[a]==BoardIndex)
{
 if((sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_EXAMINING || sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_OBSERVING) && sharedVariables.engineOn == true)
 if(sharedVariables.mygame[sharedVariables.gamelooking[a]].clickCount %2 == 1)
setEngineDoc(doc, a);
}// if winboard line
//gameconsoles[a].setStyledDocument(doc);
}
}// end if i == 5
}
catch(Exception e)
{}

}// end writeout method
void writeOut2(String text)
{
try {

// if(!sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen.equals(""))
// text+="\n" + sharedVariables.mygame[sharedVariables.gamelooking[BoardIndex]].engineFen  + "\n";
//StyledDocument doc = sharedVariables.mygamedocs[BoardIndex];
 //JTextPane me = new JTextPane();
  StyledDocument doc = sharedVariables.engineDoc;
doc.remove(0, doc.getLength());
 // doc.remove(0,doc.toString().length());
doc.insertString(doc.getLength(), text + "\n", null);
for(int a=0; a<sharedVariables.openBoardCount; a++)
if(sharedVariables.gamelooking[a]==BoardIndex)
{
 if((sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_EXAMINING || sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_OBSERVING) && sharedVariables.engineOn == true)
 if(sharedVariables.mygame[sharedVariables.gamelooking[a]].clickCount %2 == 1)
setEngineDoc(doc, a);

//gameconsoles[a].setStyledDocument(doc);
}

}
catch(Exception e)
{}

}// end writeout2 method

void parseMultiPV(String text, String pvLine, boolean multi)
{
  if(!multi && multiLines.size() > 1)
     return;  // extraneous pv

   PrincipalVariation p = new PrincipalVariation();
   int i =0;
   int max = 2;
   try {


          StringTokenizer tokens = new StringTokenizer(text, " ");
          String temp = "j";
          if(multi)
          {
            max = 3;   // we search for three arguments
          }
          while(!temp.equals(""))
         {
             temp = tokens.nextToken();
             if(temp.equals("multipv"))
             {
                p.multipv = tokens.nextToken();
                i++;
             }
             if(temp.equals("depth"))
             {
                p.depth = tokens.nextToken();
                i++;
             }
             if(temp.equals("score"))
             {  temp = tokens.nextToken();
                if(temp.equals("cp"))
                {
                p.score = tokens.nextToken();
                p.score = formatScore(p.score, true);
                i++;
                }
                if(temp.equals("mate")) {
                    String tempo = tokens.nextToken();
                    if(tempo.startsWith("-")) {  
                      tempo = tempo.replace('-', ' ');
                        p.score = "Mated in " + tempo;
                    } else { 
                      p.score = "Mate in " + tempo; 
                    } 
                      i++;
                }
             }
             if(i == max)// we got all components
             {
               break;
             }
         } // end while
         }// end try
         catch(Exception e)
         {
         }
       p.line = pvLine;
       if(i == max)
       {
         if(addSwapLine(p))// if its a duplicate line we would not reprint
         printMultiPv();
       }

        }            // end function
String formatScore(String score, boolean uci)
{
 try {
     int n = Integer.parseInt(score);
     if(sharedVariables.mygame[BoardIndex].engineTop %2 == 1 && uci)
     {
       n *= -1;
     }
     double num =  ((double) n * .01);
     return String.format("%.2f", num);
 }
 catch(Exception dui){}


 return score;
}
boolean addSwapLine(PrincipalVariation p)
{
  boolean found = false;
  if(!p.score.toLowerCase().contains("mate"))
  {
    int spaces = 0;
    int index = p.line.indexOf(" ");
    while(index != -1) {
     spaces++;
     index = p.line.indexOf(" ", index + 1);
    }
    if(spaces < 3) {
     return false; 
    }
  }
  for(int i=0; i< multiLines.size(); i++)
  {
    PrincipalVariation temp = multiLines.get(i);
    if(temp.multipv.equals(p.multipv))
    {
      found = true;
      if(compareLine(temp,p))// lines are equal we dont need to do anything redundancy
       return false;
      multiLines.set(i,p);
      break;
    }
  }  // end for

  if(!found)
  {
    multiLines.add(p);
    sortMultiLines();
  }
  return true;
} // end function
 boolean compareLine(PrincipalVariation i, PrincipalVariation j)
 {
 if(i.depth.equals(j.depth))
 {
     if(i.score.equals(j.score))
     {
       if(i.multipv.equals(j.multipv))
       {
         if(i.line.equals(j.line))
         {
           return true;
         }// line ==
       }// multipv equal
     }// score =-
 }// depth ==
 return false;
 }
void sortMultiLines()
{

  for(int a = 0; a<multiLines.size() - 1; a++)
  {

    PrincipalVariation i = multiLines.get(a);
    for(int b = a+1; b < multiLines.size(); b++)
    {
      PrincipalVariation j = multiLines.get(b);
      try  {
        int m = Integer.parseInt(i.multipv);
        int n = Integer.parseInt( j.multipv);

        if(n < m)
      {
        multiLines.set(a,j);
        multiLines.set(b,i);
      }
      }
      catch(Exception dui){}
    }
  }
}

String parseChessFont(String line)
{
  line = line.replace("        ", " ");
  line = line.replace("       ", " ");
  line = line.replace("      ", " ");
  line = line.replace("     ", " ");
  line = line.replace("    ", " ");
  line = line.replace("   ", " ");
  line = line.replace("  ", " ");

 String newLine = "";
 int whiteMove = 1;
 int blackMove  = 2;
 int newMove = 0;
 if(line == null || line.equals(""))
 {
  return line;

 }

 int i = 0;
 try {
 StringTokenizer tokens = null;

 tokens = new StringTokenizer(line, " ");
  if(!tokens.hasMoreElements())
  {
    return line;
  }

   boolean initialized = false;
   while(tokens.hasMoreElements())
   {
      String temp = tokens.nextToken();
      if(!initialized) {
        initialized = true;
       newLine = newLine + temp + " ";
       if(temp.contains("..")) {
          if(tokens.hasMoreElements()) {
               String temp2 = tokens.nextToken();
               temp2 = parseBlackFont(temp2);
               newLine = newLine + temp2 + " ";
          }
       } else {
         i++;
       }
       continue;
      } // if initializes
      if(i %3 == newMove) {
        newLine = newLine + temp + " ";
      }
       else if(i %3 == blackMove) {
           newLine = newLine + parseBlackFont(temp) + " ";
       } else if(i %3 == whiteMove) {
          newLine = newLine + parseWhiteFont(temp) + " ";
       }
      i++;
   }
      }// end try 
      catch(Exception dui) {
      }
   return newLine;
}

String parseBlackFont(String line)
{       try {
        line = line.replace("N", "Z");
        line = line.replace("B", "J");
        line = line.replace("Q", "M");
        line = line.replace("K", "N");
        line = line.replace("R", "L");
        line = line.replace("Z", "K");
 } catch(Exception dui) {
  System.out.println("color black exception");
   }
        return line;
}

String parseWhiteFont(String line)
{
 try {
        line = line.replace("N", "k");
        line = line.replace("B", "j");
        line = line.replace("Q", "m");
        line = line.replace("K", "n");
        line = line.replace("R", "l");
 } catch(Exception dui) {
   System.out.println("color white exception");
 }
        return line;
}

void printMultiPv()
{

  try {


  StyledDocument doc = sharedVariables.engineDoc;
doc.remove(0, doc.getLength());
for(int i=0; i < multiLines.size(); i++)
{
PrincipalVariation p = multiLines.get(i);

String line1 = "Depth: " + p.depth + " Score: " + p.score + " Multi-" + p.multipv + ": ";
    if(sharedVariables.analysisFont != null && sharedVariables.analysisFont.getFontName().toLowerCase().equals("chess alpha 2")) {
        line1 = "d: " + p.depth + " : " + p.score + " --" + p.multipv + ": ";
    }
if(multiLines.size() == 1) //  no multipv
{
  line1 = "Depth: " + p.depth + " Score: " + p.score + "\n";
    if(sharedVariables.analysisFont != null && sharedVariables.analysisFont.getFontName().toLowerCase().equals("chess alpha 2")) {
        line1 = "d: " + p.depth + " : " + p.score + "\n";
    }
}
String line2 = p.line + "\n";
line2 = addMoveNumbers(line2);

 if(sharedVariables.analysisFont != null && sharedVariables.analysisFont.getFontName().toLowerCase().equals("chess alpha 2")) {
        line2 = parseChessFont(line2);
    }
doc.insertString(doc.getLength(), line1 + line2, null);
}
for(int a=0; a<sharedVariables.openBoardCount; a++)
if(sharedVariables.gamelooking[a]==BoardIndex)
{
 if((sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_EXAMINING || sharedVariables.mygame[sharedVariables.gamelooking[a]].state == sharedVariables.STATE_OBSERVING) && sharedVariables.engineOn == true)
 if(sharedVariables.mygame[sharedVariables.gamelooking[a]].clickCount %2 == 1)
setEngineDoc(doc, a);

//gameconsoles[a].setStyledDocument(doc);
}

}
catch(Exception e)
{}
}

String addMoveNumbers(String inputLine)
{
  int moveNumber =(int) (sharedVariables.mygame[BoardIndex].engineTop / 2);
  moveNumber++;
  boolean whiteMoving = true;
  if(sharedVariables.mygame[BoardIndex].engineTop %2 == 1) {
    whiteMoving = false;
  }
  String temp = "" +  moveNumber;
  if(whiteMoving) {
    temp = temp + ". ";
  } else {
    temp = temp + ".. ";
  }
  // now add this one at end later
  moveNumber++;
  int counter = 0;

  for(int a = 1; a< inputLine.length(); a++) {
    if(inputLine.charAt(a) == ' ')
    counter++;
      if(inputLine.charAt(a) == ' ' && ((counter %2 == 0 && whiteMoving ) || (counter % 2 == 1 && !whiteMoving)) && a != inputLine.length() - 1) {
          String temp2 = " " +  moveNumber++ + ". ";
          inputLine = inputLine.substring(0, a) + temp2  + inputLine.substring(a+1, inputLine.length());
          a = a + 1 + temp2.length();
      }
  }
    return temp + inputLine;
}

}// end run time class


