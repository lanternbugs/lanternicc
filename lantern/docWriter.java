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
import javax.swing.text.html.HTML.Attribute.*;
import java.util.StringTokenizer;
import java.lang.reflect.Constructor;
import java.util.Vector;
import free.freechess.*;
import free.util.*;
import java.util.concurrent.locks.*;

class docWriter {
channels sharedVariables;
subframe [] consoleSubframes;
chatframe [] consoleChatframes;
JTextPane consoles[];
gameboard myboards[];
JTextPane [] gameconsoles;
int SUBFRAME_CONSOLES=0;
int GAME_CONSOLES=1;
int maxLinks=75;
int [] allTabs;

docWriter(channels sharedVariables1, subframe [] consoleSubframes1, JTextPane [] consoles1, JTextPane [] gameconsoles1, gameboard [] myboards1, chatframe [] consoleChatframes1)
{
consoles=consoles1;
sharedVariables=sharedVariables1;
consoleSubframes=consoleSubframes1;
consoleChatframes=consoleChatframes1;
myboards=myboards1;
gameconsoles= gameconsoles1;
allTabs=null;

}
void processLink2(StyledDocument doc, String thetell, Color col, int index, int attempt, int game, SimpleAttributeSet attrs, int [] allTabs2, messageStyles myStyles)
{
	allTabs = allTabs2;
	processLink(doc, thetell, col, index, attempt, game, attrs, myStyles);

}

void processLink(StyledDocument doc, String thetell, Color col, int index, int attempt, int game, SimpleAttributeSet attrs, messageStyles myStyles)
{
	processLink3(doc, thetell, 0, thetell.length(), col, index, attempt, game, attrs, myStyles);

}
void processLink3(StyledDocument doc, String thetell, int start, int stop, Color col, int index, int attempt, int game, SimpleAttributeSet attrs, messageStyles myStyles)
{

try{
	if(game == GAME_CONSOLES && sharedVariables.boardConsoleType == 0)// they hid game console
{	game=SUBFRAME_CONSOLES;
	doc = sharedVariables.mydocs[0];
}// end if
}catch(Exception cant){}


try {
/*Font font;
if(game==SUBFRAME_CONSOLES)
{
	font= sharedVariables.tabStuff[index].tabFont;
	if(font == null)
	font=sharedVariables.myFont;
}
else
font=sharedVariables.myFont;

int x = consoleSubframes[0].getFontMetrics(font).stringWidth("asdf");
*/
if(attrs == null)
	attrs = new SimpleAttributeSet();
SimpleAttributeSet  style = new SimpleAttributeSet();// = doc.addStyle(null, null);
if(sharedVariables.indent == true)
{
StyleConstants.setLeftIndent(style, 12);
StyleConstants.setFirstLineIndent(style, -11);
}
else
{
StyleConstants.setLeftIndent(style, 1);
StyleConstants.setFirstLineIndent(style, 0);

}

StyleConstants.setForeground(attrs, col);
//StyleConstants.setLeftIndent(attrs, 30);
//StyleConstants.setBold(attrs, true);
//StyleConstants.setFirstLineIndent(attrs, -20);
//StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);

parseString mine;

if(myStyles == null)
mine = new parseString(0, thetell.length());
else
mine = new parseString(start, stop);
mine.parse(thetell);


//Style styleQ = doc.addStyle(null, null);

//StyleConstants.setForeground(styleQ, col );



if(mine.itsLink == 0)
{

			doc.setParagraphAttributes(doc.getLength(), 1, style, false);

if(myStyles!=null)
	insertWithStyles(doc, thetell, attrs, myStyles, start, stop, false);

else if(sharedVariables.leftTimestamp == true && hasTimestamp(thetell))
	insertWithTimeColor(doc, thetell, attrs, col);
else
	doc.insertString(doc.getEndPosition().getOffset(), thetell, attrs);

}
else
{
if(mine.text1.length() !=0)
{
if(attempt == 1)
{
if(myStyles!=null)
	insertWithStyles(doc, mine.text1, attrs, myStyles,mine.start1, mine.stop1, false);
else
doc.insertString(doc.getEndPosition().getOffset(), mine.text1, attrs);


			doc.setParagraphAttributes(doc.getLength(), 1, style, false);

}
else
processLink3(doc, mine.text1, mine.start1, mine.stop1, col, index, attempt -1 , game, attrs, myStyles);
}
StyleConstants.setUnderline(attrs, true);
StyleConstants.setForeground(attrs, col.brighter());
//attrs.addAttribute(ID, new Integer(++linkID));
attrs.addAttribute(javax.swing.text.html.HTML.Attribute.HREF, mine.text2);
if(myStyles!=null)
	insertWithStyles(doc, mine.text2, attrs, myStyles,mine.start2, mine.stop2, true);
else
doc.insertString(doc.getEndPosition().getOffset(), mine.text2, attrs);


			doc.setParagraphAttributes(doc.getLength(), 1, style, false);

StyleConstants.setUnderline(attrs, false);
StyleConstants.setForeground(attrs, col);
if(mine.text3.length() !=0)
{


if(attempt == 1)
{
if(myStyles!=null)
	insertWithStyles(doc, mine.text3, attrs, myStyles,mine.start3, mine.stop3, false);
else

	doc.insertString(doc.getEndPosition().getOffset(), mine.text3, attrs);


			doc.setParagraphAttributes(doc.getLength(), 1, style, false);

}
else
processLink3(doc, mine.text3, mine.start3, mine.stop3, col, index, attempt -1,  game, attrs, myStyles);


}




}// end else
if(attempt == maxLinks && game == 0)
{
	if(sharedVariables.autoBufferChat == true && doc.getLength() > sharedVariables.chatBufferSize)
		autoBuffer(doc);
	writeToConsole(doc, index);
}
else if(attempt == maxLinks && game == 1)
{
	if(sharedVariables.autoBufferChat == true && doc.getLength() > sharedVariables.chatBufferSize)
		autoBuffer(doc);
	writeToGameConsole(doc, index);
}
allTabs = null; // could be on if they came in on process link 2 now its always off after business is done


}  // end try
catch(Exception e){}


}
/*
void writeToConsole(StyledDocument doc, int i)
{
	return;
}
void writeToGameConsole(StyledDocument doc, int i)
{
	return;
}

*/
	void writeToConsole(StyledDocument doc, int i)
	{

		boolean anyoneLooking=false;// another console looking
		boolean seen=false;// if true going to multiple tabs on this console and i'm looking at one.
		int a=0;


		for(a=0; a< sharedVariables.openConsoleCount; a++)
			if(consoleSubframes[a] != null)
		     {
			if(consoleSubframes[a].isVisible())
		     {
				 if(sharedVariables.looking[a]==i)
			 	{
					anyoneLooking=true;
				}
			}
		}


		for(a=0; a< sharedVariables.openConsoleCount; a++)
		 {
			 if(consoleSubframes[a] != null)
		     {
			if(consoleSubframes[a].isVisible())
		     {
				 if(sharedVariables.looking[a]==i)
			 	{
					try {
					consoles[a].setStyledDocument(doc);
					;


					}catch(Exception e){ }
				}
			 	else
			 	{


					if(allTabs !=null)
					{

					try {

						if(allTabs[sharedVariables.looking[a]] == 1)
							seen=true;
						}
					catch(Exception dum){}
					}


				if(seen == false)
					{
						if(anyoneLooking == true)
							consoleSubframes[a].channelTabs[i].setBackground(sharedVariables.tabBackground2);
						else
							consoleSubframes[a].channelTabs[i].setBackground(sharedVariables.newInfoTabBackground);
					
                                        }// end seen

					seen = false; // must reset and check again for each console

				}// end else
			  }
		  }// end outer if
	  }// end for



	  // now chat frames

		for(a=0; a< sharedVariables.openConsoleCount; a++)
		 {
			 if(consoleChatframes[a] != null)
		     {
			if(consoleChatframes[a].isVisible())
		     {
				 if(sharedVariables.looking[a]==i)
			 	{
					try {
					consoles[a].setStyledDocument(doc);
					;


					}catch(Exception e){ }
				}
			 	else
			 	{


					if(allTabs !=null)
					{

					try {

						if(allTabs[sharedVariables.looking[a]] == 1)
							seen=true;
						}
					catch(Exception dum){}
					}


				if(seen == false)
					{

							consoleChatframes[a].channelTabs[i].setBackground(sharedVariables.newInfoTabBackground);
					}// end seen

					seen = false; // must reset and check again for each console

				}// end else
			  }
		  }// end outer if
	  }// end for
	}
	void writeToGameConsole(StyledDocument doc, int i)
	{
		for(int a=0; a< sharedVariables.openBoardCount; a++)
		     if(myboards[a]!=null)
		     if(myboards[a].isVisible())
		     {
				 if(sharedVariables.gamelooking[a]==i && sharedVariables.pointedToMain[a] == false)

			{
                          
                          boolean go=true;
                           if(sharedVariables.mygame[i].state == sharedVariables.STATE_EXAMINING && sharedVariables.engineOn == true)
                           if(sharedVariables.mygame[i].clickCount %2 == 1)
                           go=false;
                           
                           if(go==true)
                          gameconsoles[a].setStyledDocument(doc);
                          }
			else
			myboards[a].myconsolepanel.channelTabs[i].setBackground(sharedVariables.newInfoTabBackground);
			}
	}



void autoBuffer(StyledDocument doc)
{
try {
	if(doc.getLength() - (sharedVariables.chatBufferSize - sharedVariables.chatBufferExtra) > 0)
	doc.remove(0, doc.getLength() - (sharedVariables.chatBufferSize - sharedVariables.chatBufferExtra));
}
catch(Exception badbuffer){}
}



boolean hasTimestamp(String thetell)
{

	if(thetell.length() < 12)
	return false;

	String thetell2 = thetell.substring(0, 7);


	if(thetell2.matches("[0-9][0-9]:[0-9][0-9]:[0-9]"))
	return true;
	if(thetell2.matches("[0-9]:[0-9][0-9]:[0-9][0-9]"))
	return true;

	return false;
}

void insertWithTimeColor(StyledDocument doc, String thetell, SimpleAttributeSet attrs, Color col)
{

try {
int index = thetell.indexOf(" ");
if(index==-1)
{
doc.insertString(doc.getEndPosition().getOffset(), thetell, attrs);
}
else
{

StyleConstants.setForeground(attrs, sharedVariables.chatTimestampColor);
String subtell1=thetell.substring(0, index);
String subtell2=thetell.substring(index,thetell.length());

	doc.insertString(doc.getEndPosition().getOffset(), subtell1, attrs);
StyleConstants.setForeground(attrs, col);
	doc.insertString(doc.getEndPosition().getOffset(), subtell2, attrs);

}
}
catch(Exception d){}

}

// 	insertWithStyles(doc, thetell, attrs, myStyles, start, stop, false);

void insertWithStyles(StyledDocument doc, String thetell, SimpleAttributeSet attrs, messageStyles myStyles, int start, int stop, boolean underline)
{
try {
if(underline == true)
StyleConstants.setUnderline(attrs, true);
// loop
String [] myStrings = new String[100];

boolean end=false;

for(int a=0; a<myStyles.top && end != true; a++)
{
if(start >= myStyles.blocks[a])
continue;

if(start < myStyles.blocks[a])
{
int a1;
if(a == 0)
a1=0;
else
{a1= myStyles.blocks[a-1] - start;
if(a1 < 0)
a1=0;
}


int a2=myStyles.blocks[a];
if(a2 > stop)
{
	a2=stop;
	end=true;
}
a2=a2-start;
if(a2>thetell.length())
a2=thetell.length();

myStrings[a]=thetell.substring(a1, a2);
if(underline == false)
	StyleConstants.setForeground(attrs, myStyles.colors[a]);
else
	StyleConstants.setForeground(attrs, myStyles.colors[a].brighter());

int italic1=-1;
int italic2=-1;

// dasher feature make stuff between ` ` italic
italic1 = myStrings[a].indexOf("`");
if(italic1 > -1 && italic1 < myStrings[a].length()-2)
italic2 = myStrings[a].indexOf("`", italic1 + 1);


if(italic1 > -1 && italic2 > -1 && !attrs.containsAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE))
{
    int dd = 0;
    while(dd != -1)
    {
italic1 = myStrings[a].indexOf("`", dd);
if(italic1 > -1 && italic1 < myStrings[a].length()-2)
italic2 = myStrings[a].indexOf("`", italic1  + 1);
else 
italic2=-1;
if(italic1 > -1 && italic2 > -1)
{




doc.insertString(doc.getEndPosition().getOffset(), myStrings[a].substring(dd, italic1), attrs);

if(sharedVariables.italicsBehavior == 2)
	StyleConstants.setForeground(attrs, myStyles.colors[a].brighter());
if(sharedVariables.italicsBehavior == 1)
StyleConstants.setItalic(attrs, true);
doc.insertString(doc.getEndPosition().getOffset(), myStrings[a].substring(italic1, italic2+1), attrs);
if(sharedVariables.italicsBehavior == 1)
StyleConstants.setItalic(attrs, false);
if(sharedVariables.italicsBehavior == 2)
	StyleConstants.setForeground(attrs, myStyles.colors[a]);

       dd=italic2 + 1;
/*JFrame fram = new JFrame("italic1 " + italic1 +  " and italic2 " + italic2 + " " + myStrings[a]);
fram.setVisible(true);
fram.setSize(300,300);
  */
}
else
{
 // insert end
doc.insertString(doc.getEndPosition().getOffset(), myStrings[a].substring(dd, myStrings[a].length()), attrs);

 dd=-1;
}
      
    } // end while


}  // end if
else
doc.insertString(doc.getEndPosition().getOffset(), myStrings[a], attrs);
}// if start > blocks[a]
}//end for



if(underline == true)
{


	StyleConstants.setUnderline(attrs, false);
}// end underline
}
catch(Exception dui){}

}// end method
}// end class