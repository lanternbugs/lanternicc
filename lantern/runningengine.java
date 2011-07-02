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



class runningengine implements Runnable
{
channels sharedVariables;
int BoardIndex;
JTextPane [] gameconsoles;
gamestuff gameData;
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

void intializeNewEngineGame()
{
	sendToEngine("new\n");
	sendToEngine("level 0 1 1\n");
	sendToEngine("post\n");
sendToEngine("hard\n");
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
			 	engine = rt.exec(sharedVariables.engineFile.toString());

			 	sharedVariables.engineOn = true;
if(sharedVariables.uci == false)
runWinboard();
else
runUci();


} // end try
catch(Exception e)
{ }




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
	if(tosend.data.contains("quit"))
	go=0;
}
}
catch(Exception e) {

}
if(text.length() > 0 && !text.startsWith("#") && !text.startsWith("stat"))
{

try {
StyledDocument doc = sharedVariables.mygamedocs[BoardIndex];
doc.insertString(doc.getEndPosition().getOffset(), text + "\n", null);
for(int a=0; a<sharedVariables.openBoardCount; a++)
if(sharedVariables.gamelooking[a]==BoardIndex)
{
gameconsoles[a].setStyledDocument(doc);
}

}
catch(Exception e)
{}
}// end if








Thread.sleep(35);
}
while(go==1);
}// end try
catch(Exception e){}
}// end run winboard


void runUci()
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

pgnWriter pgnGetter = new pgnWriter();


int stage=0;

//sendToEngine("move e2e4\n");
//sendToEngine("move d7d5\n");
String text="";
do {

if(in.ready())
text=in.readLine();


if(stage == 0)
{
	sendToEngine("uci\n");
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
	sendToEngine("position startpos\n");
	sendToEngine("go infinite\n");

// if they start analyzing in the middle of an examined game
writeOut("Engine Top is " + sharedVariables.mygame[BoardIndex].engineTop);
if(sharedVariables.mygame[BoardIndex].engineTop > 0)
{
	sendToEngine("stop\n");
	writeOut("stop\n");
	sendToEngine("position startpos" + sharedVariables.mygame[BoardIndex].getUciMoves());
	writeOut("position startpos" + sharedVariables.mygame[BoardIndex].getUciMoves());
	sendToEngine("go infinite\n");
	writeOut("go infinite\n");
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
	{
			sendToEngine(finalStuff);
			finalStuff="";
			go=0;
			writeOut("sent quit");

		break;
	}

tosend=sharedVariables.engineQueue.poll();// we look for data from other areas of the program





/*if(!sharedVariables.engineQueue.isEmpty())

{
	tosend = new myoutput();
	tosend=sharedVariables.engineQueue.poll();// we look for data from other areas of the program
	String lastsent="";




	while(tosend != null) // check if we are forwarding a bunch of moves and get last one
	{


			if(tosend.data.contains("quit"))
			{sendToEngine(tosend.data);
			go=0;
			lastsent = tosend.data;
			break;
			}

	        lastsent=tosend.data.substring(0, tosend.data.length());
	        if(!sharedVariables.engineQueue.isEmpty())
	        {
				tosend = new myoutput();
	        	tosend=sharedVariables.engineQueue.poll();// we look for data from other areas of the program
			}
			else
			break;

			}// end while


sendToEngine(lastsent);
writeOut("last sent is " + lastsent);
}// end if not empty
*/

}// end if tosent not null first time
if(finalStuff.length() > 0)
{
	try {
		writeOut("final stuff lenght > 0 and sending " + finalStuff);

	}
	catch(Exception badright){}
	sendToEngine(finalStuff);
}

}
catch(Exception e) {}
}//end if stage 3

if(text.length() > 0 && ((text.contains("pv") && stage ==3) || stage<3))
{
try {
if(text.startsWith("info") && (text.contains("pv") && stage ==3))
{
	String line1 = text.substring(0, text.indexOf("pv"));
	String line2 = text.substring(text.indexOf("pv") + 3, text.length());
	line2=pgnGetter.getPgn(line2, sharedVariables.mygame[gameData.BoardIndex].iflipped, sharedVariables.mygame[gameData.BoardIndex].board);
	writeOut(line1);
	writeOut(line2);
}
else
writeOut(text);
}
catch(Exception badone){}
}// end if







try {
Thread.sleep(30);
}
catch(Exception E5){}



}
while(go==1);
}// end try
catch(Exception e){}
}// end run uci


void writeOut(String text)
{
try {
StyledDocument doc = sharedVariables.mygamedocs[BoardIndex];
doc.insertString(doc.getEndPosition().getOffset(), text + "\n", null);
for(int a=0; a<sharedVariables.openBoardCount; a++)
if(sharedVariables.gamelooking[a]==BoardIndex)
{
gameconsoles[a].setStyledDocument(doc);
}

}
catch(Exception e)
{}

}// end writeout method

}// end run time class


