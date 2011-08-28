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


class createWindows {

channels sharedVariables;
subframe [] consoleSubframes;
gameboard [] myboards;
JTextPane [] consoles;
JTextPane [] gameconsoles;
ConcurrentLinkedQueue<myoutput> queue;
Image [] img;
ConcurrentLinkedQueue<newBoardData> gamequeue;
webframe mywebframe;
resourceClass graphics;
listFrame myfirstlist;
docWriter myDocWriter;
chatframe [] consoleChatframes;
mymultiframe masterFrame;
createWindows(channels sharedVariables1, subframe [] consoleSubframes1 ,gameboard [] myboards1, JTextPane [] consoles1, JTextPane [] gameconsoles1, ConcurrentLinkedQueue<myoutput> queue1, Image [] img1, ConcurrentLinkedQueue<newBoardData> gamequeue1, webframe mywebframe1, resourceClass graphics1, listFrame myfirstlist1, docWriter myDocWriter1, chatframe [] consoleChatframes1, mymultiframe masterFrame1)
{
consoleSubframes=consoleSubframes1;
sharedVariables=sharedVariables1;
myboards=myboards1;
consoles=consoles1;
gameconsoles=gameconsoles1;
queue=queue1;
masterFrame=masterFrame1;
gamequeue1=gamequeue;
img=img1;
mywebframe=mywebframe1;
graphics = graphics1;
myfirstlist=myfirstlist1;
myDocWriter=myDocWriter1;
consoleChatframes=consoleChatframes1;
}

protected void createConsoleFrame() {

	// our consoles, main to sub console ( for channels) are allways created here.  the start of the program does a call to this method and our main console is created. openConsoleCount is incremented from 0 to 1 and next console is indexed at 1.
   	if(sharedVariables.openConsoleCount >= sharedVariables.maxConsoleTabs-1) // openConsolecount initializes at 0 so main is always created at 0 and first sub console at 1 up to 9 for a total of 10
   	return; // cant create more than 10 consoles.// last console now detached




   	consoleSubframes[sharedVariables.openConsoleCount] = new subframe(sharedVariables, consoles, queue, myDocWriter, myboards);

	consoleSubframes[sharedVariables.openConsoleCount].setVisible(true);

    sharedVariables.desktop.add(consoleSubframes[sharedVariables.openConsoleCount]);

    sharedVariables.openConsoleCount++;
    try
    {
        consoleSubframes[sharedVariables.openConsoleCount-1].setSelected(true);

    }
    catch (Exception e)
    {}

    if(sharedVariables.openConsoleCount == 1)// new trick now going to at startup turn on all 10 consoles but not make the rest 2-10 visible
    {
		for(int z=sharedVariables.openConsoleCount; z < sharedVariables.maxConsoleTabs; z++)
		{
			consoleSubframes[sharedVariables.openConsoleCount] = new subframe(sharedVariables, consoles, queue, myDocWriter, myboards);


			sharedVariables.desktop.add(consoleSubframes[sharedVariables.openConsoleCount]);

			sharedVariables.openConsoleCount++;

		}
/*		sharedVariables.chatFrame=sharedVariables.maxConsoleTabs-2;
		consoleChatframes[sharedVariables.maxConsoleTabs-2] =  new chatframe(sharedVariables, consoles, queue, myDocWriter);
               // consoleChatframes[sharedVariables.maxConsoleTabs-1].setVisible(true);
                sharedVariables.openConsoleCount++;

		sharedVariables.chatFrame=sharedVariables.maxConsoleTabs-1;
		consoleChatframes[sharedVariables.maxConsoleTabs-1] =  new chatframe(sharedVariables, consoles, queue, myDocWriter);
               // consoleChatframes[sharedVariables.maxConsoleTabs-1].setVisible(true);
                sharedVariables.openConsoleCount++;

*/
	}
}

protected void createWebFrame(String url) {





   	/*webframe mywebframe = new webframe(sharedVariables,  queue, url);
	*/
	if(mywebframe.isVisible() == false)
	{
		mywebframe = new webframe(sharedVariables,  queue, url);
		sharedVariables.desktop.add(mywebframe);
		try {


			if(sharedVariables.webframeWidth + 80 > sharedVariables.screenW)
				sharedVariables.webframeWidth = sharedVariables.screenW - 80;
			if(sharedVariables.webframeHeight + 80 > sharedVariables.screenH)
				sharedVariables.webframeHeight = sharedVariables.screenH - 80;

			mywebframe.setSize(sharedVariables.webframeWidth, sharedVariables.webframeHeight);
			mywebframe.setLocation(sharedVariables.webframePoint.x, sharedVariables.webframePoint.y);
		}
		catch(Exception m){}
		mywebframe.setVisible(true);
		try {
			mywebframe.setSelected(true);
		}
		catch(Exception n){}
	}
	else
	{


    try
    {
        mywebframe.setSelected(true);
        mywebframe.consoles[0].setPage(url);

    }
    catch (Exception e)
    {}
	}
}



protected void restoreConsoleFrame() {


   // determine console to restore or return if none
   int num=-1;
   for(int a=0; a<sharedVariables.maxConsoleTabs-1; a++)// last console is detached
   if(consoleSubframes[a]!=null) // we look for the first console ( and next if we loop further) that is not null ( created)
   if(consoleSubframes[a].isVisible()==false) // we check if this console is visiable, if not we grab its number in num to work with and break. note restore happens in order of first console you can restore and only one gets restored at a time.
   {num=a;
   break;
	}

   if(num==-1)// there are no created but not visible consoles.
   return;

 int oldopenConsoleCount=sharedVariables.openConsoleCount;
 sharedVariables.openConsoleCount=num; // bit of a trick. openConsoleCount is now == to that num we found ( or invisiable console).  were going to restore openConsoleCount to its true number later.
 // the consoles increment open console count and take their number from it.  we are dubbing in a number and revering any changes ( incremening) they do


	consoleSubframes[sharedVariables.openConsoleCount].madeTextPane=1;
   consoleSubframes[sharedVariables.openConsoleCount] = new subframe(sharedVariables, consoles, queue, myDocWriter, myboards);


try {
// patch routine to restore board to same size if its first  board


if(sharedVariables.myConsoleSizes[num].con0x != -1)
{
consoleSubframes[num].setSize(sharedVariables.myConsoleSizes[num].con0x, sharedVariables.myConsoleSizes[num].con0y);
consoleSubframes[num].setLocation(sharedVariables.myConsoleSizes[num].point0.x, sharedVariables.myConsoleSizes[num].point0.y);
}
else
consoleSubframes[num].setSize(425,425);



}// end try
catch(Exception bad1){}





	consoleSubframes[sharedVariables.openConsoleCount].setVisible(true);

    sharedVariables.desktop.add(consoleSubframes[sharedVariables.openConsoleCount]);
    try
    {
        consoleSubframes[sharedVariables.openConsoleCount].setSelected(true);

    }
    catch (Exception e)
    {}
    sharedVariables.openConsoleCount=oldopenConsoleCount;// put openConsoleCount back to what it really is.
}


protected void createGameFrame() {
   // myboards[openBoardCount] = new gameboard();
 boolean newboard = false;

 int boardNumber = sharedVariables.openBoardCount;
 int t1;
 int tab;
/**************** quick check if we can just make a board visible *****************************/

for(t1=0; t1<sharedVariables.maxGameTabs; t1++)
{
if(myboards[t1] == null)
break;
else if(myboards[t1].isVisible() == false && (sharedVariables.mygame[t1].myGameNumber != -100 || sharedVariables.mygame[t1].imclosed == false))
{
  myboards[t1].setVisible(true);
  updateBoardsMenu(t1);
  //sharedVariables.mygame[t1]=new gamestate(sharedVariables.excludedPieces);
  myboards[t1].myconsolepanel.makehappen(t1);
  for(tab = 0; tab < sharedVariables.openBoardCount; tab++)
  { myboards[t1].myconsolepanel.channelTabs[tab].setVisible(true);
  myboards[t1].myconsolepanel.channelTabs[tab].setText(sharedVariables.tabTitle[sharedVariables.tabLooking[tab]], tab);

  }

  return;
}// end else
}// end for

/*************                                                   *****************************/


/****************************  look for first closed board ***********************************/
 for(t1=0; t1<sharedVariables.maxGameTabs; t1++)
{
if(myboards[t1] == null)
break;
else if(sharedVariables.mygame[t1].imclosed == true)
{
                                   sharedVariables.openBoardCount++;
                                    int mylast = sharedVariables.openBoardCount-1;
                                    sharedVariables.tabLooking[mylast] = t1;
                                    myboards[t1].setVisible(true);
                                    updateBoardsMenu(t1);
                                      sharedVariables.mygame[t1]=new gamestate(sharedVariables.excludedPiecesWhite, sharedVariables.excludedPiecesBlack);

                                    for(int cc=0; cc< sharedVariables.maxGameTabs; cc++)
                                    if(myboards[cc]!=null)
                                    myboards[cc].myconsolepanel.channelTabs[mylast].setVisible(true);
                                    else
                                    break;

                                    sharedVariables.tabTitle[sharedVariables.tabLooking[mylast]] = "G" + mylast;
                                    for(int draw=0; draw < sharedVariables.maxGameTabs; draw++)
                                    if(myboards[draw]!=null)
                                                myboards[draw].myconsolepanel.channelTabs[mylast].setText("" + sharedVariables.tabTitle[sharedVariables.tabLooking[mylast]], mylast);
                                    else
                                                break;
                                     sharedVariables.mygame[t1].imclosed = false;


 return;
}// end elseif

}// end for




/******************************** end first closed board *************************************/

 /*
 for(int d =sharedVariables.openBoardCount-1; d >=0; d--)
 if(myboards[d]!=null)
 if(!myboards[d].isVisible())
{
boardNumber=d;
break;
}
 if(myboards[boardNumber]!=null)    //  && boardNumber != sharedVariables.openBoardCount
 {
  if(sharedVariables.useTopGames == true)
    {  if(myboards[boardNumber].topGame != null)
    myboards[boardNumber].topGame.setVisible(true);
  }

  else
   myboards[boardNumber].setVisible(true);

                               boolean closed = true;

                                   closed = false;

                                   if(closed == true)
                                   {
                                    sharedVariables.openBoardCount++;
                                    sharedVariables.tabLooking[sharedVariables.openBoardCount - 1] = boardNumber;
                                    for(int cc=0; cc< sharedVariables.openBoardCount; cc++)
                                    if(myboards[cc]!=null)
                                    if(myboards[cc].isVisible() == true)
                                    myboards[cc].myconsolepanel.channelTabs[sharedVariables.openBoardCount-1].setVisible(true);
                                    int mylast = sharedVariables.openBoardCount-1;
                                    sharedVariables.tabTitle[sharedVariables.tabLooking[mylast]] = "G" + mylast;
                                    for(int draw=0; draw < sharedVariables.maxGameTabs; draw++)
                                    if(myboards[draw]!=null)
                                                myboards[draw].myconsolepanel.channelTabs[mylast].setText("" + sharedVariables.tabTitle[sharedVariables.tabLooking[mylast]], mylast);
                                    else
                                                break;

                                   }
                                   else
                                   {
                                        JFrame haha = new JFrame("haha");
                                        haha.setSize(200,200);
                                        haha.setVisible(true);

                                   }

 //  sharedVariables.tabLooking[sharedVariables.openBoardCount]=boardNumber;

 }
 else
 {
 
 
 */
 
    boardNumber=sharedVariables.openBoardCount;
   final int boardNumber1=boardNumber;


                            try {



   sharedVariables.tabLooking[boardNumber1]=boardNumber1;
   myboards[boardNumber1] = new gameboard(consoles, consoleSubframes, gameconsoles, gamequeue, boardNumber1, img, queue, sharedVariables, graphics, myDocWriter);



  int numb=boardNumber1+1;
  sharedVariables.tabTitle[sharedVariables.tabLooking[boardNumber1]] = "G" + numb;
  for(tab = 0; tab < sharedVariables.maxGameTabs; tab++)
  { if(myboards[tab]!=null)
  {
    myboards[tab].myconsolepanel.channelTabs[boardNumber1].setVisible(true);

  myboards[tab].myconsolepanel.channelTabs[boardNumber1].setText(sharedVariables.tabTitle[sharedVariables.tabLooking[boardNumber1]], boardNumber1);
  }// end if not null
  else
  break;
  }     // end for
  for(tab = 0; tab < sharedVariables.openBoardCount; tab++)
  {
 myboards[boardNumber].myconsolepanel.channelTabs[tab].setText(sharedVariables.tabTitle[sharedVariables.tabLooking[tab]], tab);
  }



  if(sharedVariables.useTopGames == true)
  { if(myboards[boardNumber1].topGame != null)
    myboards[boardNumber1].topGame.setVisible(true);
    updateBoardsMenu(boardNumber1);
  }
  else
 {  myboards[boardNumber1].setVisible(true);
    updateBoardsMenu(boardNumber1);
 }

   sharedVariables.desktop.add(myboards[boardNumber1] );
    // add desktop to consolesubframe so it can call its method of focus traversal between boards and consoles
    myboards[boardNumber1].myconsolepanel.myself=(JDesktopPaneCustom) sharedVariables.desktop;
try {
        if(boardNumber1 != 0)
        myboards[boardNumber1] .setSelected(true);
    } catch (Exception e) {}
		myboards[boardNumber1] .initializeGeneralTimer();


}// end try
catch (Exception e1) {
                                //ignore
                           }





 newboard=true;
//}


try {
// patch routine to restore board to same size if its first  board

 if(sharedVariables.useTopGames == false)
 {

if(sharedVariables.myBoardSizes[boardNumber].con0x != -1)
{
myboards[boardNumber].setSize(sharedVariables.myBoardSizes[boardNumber].con0x, sharedVariables.myBoardSizes[boardNumber].con0y);
myboards[boardNumber].setLocation(sharedVariables.myBoardSizes[boardNumber].point0.x, sharedVariables.myBoardSizes[boardNumber].point0.y);
}
else
myboards[boardNumber].setSize(sharedVariables.defaultBoardWide,sharedVariables.defaultBoardHigh);
 }// false
else
{
 //final int boardNumber1=boardNumber;
 SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {

if(sharedVariables.myBoardSizes[boardNumber1].con0x != -1)
{
  myboards[boardNumber1].topGame.setSize(sharedVariables.myBoardSizes[boardNumber1].con0x, sharedVariables.myBoardSizes[boardNumber1].con0y);
myboards[boardNumber1].topGame.setLocation(sharedVariables.myBoardSizes[boardNumber1].point0.x, sharedVariables.myBoardSizes[boardNumber1].point0.y);
}
else
myboards[boardNumber1].topGame.setSize(sharedVariables.defaultBoardWide,sharedVariables.defaultBoardHigh);
                          } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });



 }


}// end try
catch(Exception bad1){}








if(boardNumber == sharedVariables.openBoardCount)
sharedVariables.openBoardCount++;

                     //   JFrame framer = new JFrame("open board count is " + sharedVariables.openBoardCount + " and board number is " + boardNumber);
                      //  framer.setSize(200,100);
                      //  framer.setVisible(true);

}

protected void createListFrame(listClass eventsList, listClass seeksList, listClass computerSeeksList, listClass notifyList, JFrame homeFrame)
{
if(myfirstlist == null)
{
 JFrame master = new JFrame();
  myfirstlist = new listFrame(master, sharedVariables, queue, eventsList, seeksList, computerSeeksList, notifyList, homeFrame);
}
try {
	myfirstlist.setSize(sharedVariables.myActivitiesSizes.con0x, sharedVariables.myActivitiesSizes.con0y);
myfirstlist.setLocation(sharedVariables.myActivitiesSizes.point0.x, sharedVariables.myActivitiesSizes.point0.y);
}catch(Exception activitiesFailure){}

myfirstlist.setVisible(true);
//sharedVariables.desktop.add(myfirstlist);
try
    { //myfirstlist.setSelected(true);
if(sharedVariables.activitiesTabNumber != 0)
	myfirstlist.listScroller.setVisible(false);
else
	myfirstlist.listScroller.setVisible(true);

if(sharedVariables.activitiesTabNumber != 1)
	myfirstlist.seeklistScroller.setVisible(false);
else
	myfirstlist.seeklistScroller.setVisible(true);

if(sharedVariables.activitiesTabNumber != 2)
	myfirstlist.computerseeklistScroller.setVisible(false);
else
	myfirstlist.computerseeklistScroller.setVisible(true);

if(sharedVariables.activitiesTabNumber != 3)
myfirstlist.notifylistScroller.setVisible(false);
else
myfirstlist.notifylistScroller.setVisible(true);



}
catch(Exception z){}
}

void updateBoardsMenuClosing(int num)
{
  
//  JFrame framer = new JFrame(" num is " + num);
//  framer.setSize(300, 100);
//  framer.setVisible(true);

  if(sharedVariables.myWindows == null)// shouldnt happen
  return;
  sharedVariables.myWindows.remove(sharedVariables.openBoards[num]);
  sharedVariables.openBoards[num]=null;



}
void updateBoardsMenu(int num)
{
  
  if(sharedVariables.myWindows == null)// happens for first board before menu is created
  return;
  int d=num+1;
  sharedVariables.openBoards[num]= new JMenuItem("Board " + d + ":");
  sharedVariables.myWindows.add(sharedVariables.openBoards[num]);
  sharedVariables.openBoards[num].addActionListener(masterFrame);

}
} // end class