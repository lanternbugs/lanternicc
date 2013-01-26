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
import java.applet.*;
import java.awt.event.*;
import java.util.StringTokenizer;

class settings {


	gameboard myboards[];

	subframe [] consoleSubframes;
	String aFile;
	String aFileLinux;
        String engineFile;


	settings(channels sharedVariables){
		aFile= "\\multiframe\\multi_settings.txt";

		if(sharedVariables.standAlone == true)
			aFile= "multi_settings.txt";

		aFileLinux= "/multiframe/multi_settings.txt";
		} // constructor

	void saveNow(gameboard boards[], subframe frames[], channels sharedVariables)
	{
		myboards=boards;
		consoleSubframes=frames;

		// sharedVariables.console[aChannelNumber 1-500] will tell me what console a channel number is in, if not setto a sub console number it goes in main, i.e its 0
		// note the only persistent feature really is the document associated with a console.  if it goes to main ( console 0 i think) that means it goes to teh document associated with console 0 originally.
		// any console if they change tabs can look at any document, but in a sense the consoles 1-10 enjoy persistence in their docuemnts 1-10
		// next two lines below an example of colorizing a channel to show how we store colors
		//	sharedVariables.channelOn[num1]=1; // channelOn accepts numbers up to 500 and indicates it is colorized, not a default color
		//    sharedVariables.channelColor[num1]=newColor; // channelColor accepts channel numbers up to 500 and gives their color. you wouldnt need to look at this unless channelOn for that channel number was set to 1


	// format for saving
	/*
	// \n between lines. we can make substrings by looking for the \n if we want. \n after last line.
	connum=#  // i.e. consnum=0
	visible=t or f // i.e. visible=t
	window=[window cordinates if visiable is true]
	channels=1 5 7 9 etc
	colored=0 1 1 0 etc
	colors=R G B, R G B
	// repeat for as many consoles as we have
	*/

	//  number = color.getRGB()


	// write engine out if possible
	if(sharedVariables.engineDirectory!= null)
	{
        try {
        engineFile = "lantern_engine_directory.ini";

        FileWriter efstream = new FileWriter(engineFile);
	FileWrite out = new FileWrite();

	out.write2(efstream, sharedVariables.engineDirectory.getPath());
        }
        catch(Exception easy){}

        }
	int zz;
	String aFont;
	String aFontStyle;
	String aFontSize;
	String set_string="";
	set_string = set_string + "[color] ";
	//  light square
	set_string = set_string + "light_color ";
	set_string = set_string + sharedVariables.lightcolor.getRGB() + " ";

	// dark square
	set_string = set_string + "dark_color ";
	set_string = set_string + sharedVariables.darkcolor.getRGB() + " ";


	// console foreground
	set_string = set_string + "ForColor ";
	set_string = set_string + sharedVariables.ForColor.getRGB() + " ";

	for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
	{
		if(sharedVariables.tabStuff[zz].ForColor != null)
		{
				set_string = set_string + "ForColor" + zz + " ";
				set_string = set_string + sharedVariables.tabStuff[zz].ForColor.getRGB() + " ";
		}
	}
for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
	{
		if(sharedVariables.tabStuff[zz].timestampColor != null)
		{
				set_string = set_string + "timestampColor" + zz + " ";
				set_string = set_string + sharedVariables.tabStuff[zz].timestampColor.getRGB() + " ";
		}
	}
	// console background
	set_string = set_string + "BackColor ";
	set_string = set_string + sharedVariables.BackColor.getRGB() + " ";

	for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
	{
		if(sharedVariables.tabStuff[zz].BackColor != null)
		{
				set_string = set_string + "BackColor" + zz + " ";
				set_string = set_string + sharedVariables.tabStuff[zz].BackColor.getRGB() + " ";
		}
	}


	//  application background
	set_string = set_string + "MainBackColor ";
	set_string = set_string + sharedVariables.MainBackColor.getRGB() + " ";

	// shout color
	set_string = set_string + "shoutcolor ";
	set_string = set_string + sharedVariables.shoutcolor.getRGB() + " ";

	// s-shout color
	set_string = set_string + "sshoutcolor ";
	set_string = set_string + sharedVariables.sshoutcolor.getRGB() + " ";

	// tell  color
	set_string = set_string + "tellcolor ";
	set_string = set_string + sharedVariables.tellcolor.getRGB() + " ";

// list  color
	set_string = set_string + "listcolor ";
	set_string = set_string + sharedVariables.listColor.getRGB() + " ";

//analysisForegroundColor
	set_string = set_string + "analysisForegroundColor ";
	set_string = set_string + sharedVariables.analysisForegroundColor.getRGB() + " ";
// analysisBackgroundColor
	set_string = set_string + "analysisBackgroundColor ";
	set_string = set_string + sharedVariables.analysisBackgroundColor.getRGB() + " ";

	for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
	{
		if(sharedVariables.tabStuff[zz].tellcolor != null)
		{
				set_string = set_string + "tellcolor" + zz + " ";
				set_string = set_string + sharedVariables.tabStuff[zz].tellcolor.getRGB() + " ";
		}
	}

	//hightlight moves color
	set_string = set_string + "highlightMovesColor ";
	set_string = set_string + sharedVariables.highlightcolor.getRGB() + " ";
	//scroll highlight moves color
	set_string = set_string + "scrollHighlightMovesColor ";
	set_string = set_string + sharedVariables.scrollhighlightcolor.getRGB() + " ";

	// board foreground  color
	set_string = set_string + "boardForegroundColor ";
	set_string = set_string + sharedVariables.boardForegroundColor.getRGB() + " ";

	// clock foreground  color
	set_string = set_string + "clockForegroundColor ";
	set_string = set_string + sharedVariables.clockForegroundColor.getRGB() + " ";


	// board background  color
	set_string = set_string + "boardBackgroundColor ";
	set_string = set_string + sharedVariables.boardBackgroundColor.getRGB() + " ";

	/* to add
	onMoveBoardBackgroundColor
	responseColor
	defaultChannelColor
	kibcolor
	qtellcolor

	*/

	// onMoveBoardBackgroundColor  color
	set_string = set_string + "onMoveBoardBackgroundColor ";
	set_string = set_string + sharedVariables.onMoveBoardBackgroundColor.getRGB() + " ";

	// responseColor  color
	set_string = set_string + "responseColor ";
	set_string = set_string + sharedVariables.responseColor.getRGB() + " ";

	for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
	{
		if(sharedVariables.tabStuff[zz].responseColor != null)
		{
				set_string = set_string + "responseColor" + zz + " ";
				set_string = set_string + sharedVariables.tabStuff[zz].responseColor.getRGB() + " ";
		}
	}


	// defaultChannelColor  color
	set_string = set_string + "defaultChannelColor ";
	set_string = set_string + sharedVariables.defaultChannelColor.getRGB() + " ";

	// name foreground   color
	set_string = set_string + "nameForegroundColor ";
	set_string = set_string + sharedVariables.nameForegroundColor.getRGB() + " ";

	// name background  color
	set_string = set_string + "nameBackgroundColor ";
	set_string = set_string + sharedVariables.nameBackgroundColor.getRGB() + " ";


	// inputChatColorColor  color
	set_string = set_string + "inputChatColor ";
	set_string = set_string + sharedVariables.inputChatColor.getRGB() + " ";

	// inputCommandColor  color
	set_string = set_string + "inputCommandColor ";
	set_string = set_string + sharedVariables.inputCommandColor.getRGB() + " ";

	// kibcolor  color
	set_string = set_string + "kibcolor ";
	set_string = set_string + sharedVariables.kibcolor.getRGB() + " ";


	// activeTabForeground  color
	set_string = set_string + "activeTabForeground ";
	set_string = set_string + sharedVariables.activeTabForeground.getRGB() + " ";

        	// passiveTabForeground  color
	set_string = set_string + "passiveTabForeground ";
	set_string = set_string + sharedVariables.passiveTabForeground.getRGB() + " ";


	// tabImOnBackground  color
	set_string = set_string + "tabImOnBackground ";
	set_string = set_string + sharedVariables.tabImOnBackground.getRGB() + " ";

	// tabBackground  color
	set_string = set_string + "tabBackground ";
	set_string = set_string + sharedVariables.tabBackground.getRGB() + " ";

	// tabBackground2  color
	set_string = set_string + "tabBackground2 ";
	set_string = set_string + sharedVariables.tabBackground2.getRGB() + " ";


       	// newInfoTabBackground  color
	set_string = set_string + "newInfoTabBackground ";
	set_string = set_string + sharedVariables.newInfoTabBackground.getRGB() + " ";

        // tabBorderColor  color
	set_string = set_string + "tabBorderColor ";
	set_string = set_string + sharedVariables.tabBorderColor.getRGB() + " ";

        // tellTabBorderColor  color
	set_string = set_string + "tellTabBorderColor ";
	set_string = set_string + sharedVariables.tellTabBorderColor.getRGB() + " ";

       // chatTimestampColor  color
	set_string = set_string + "chatTimestampColor ";
	set_string = set_string + sharedVariables.chatTimestampColor.getRGB() + " ";

       // qtellChannelNumberColor  color
	set_string = set_string + "qtellChannelNumberColor ";
	set_string = set_string + sharedVariables.qtellChannelNumberColor.getRGB() + " ";

      // channelTitlesColor  color
	set_string = set_string + "channelTitlesColor ";
	set_string = set_string + sharedVariables.channelTitlesColor.getRGB() + " ";

     // nameTellColor  color
	set_string = set_string + "nameTellColor ";
	set_string = set_string + sharedVariables.tellNameColor.getRGB() + " ";


/*******************styles *********************************/

        // shoutStyle
	set_string = set_string + "StyleShout ";
	set_string = set_string + sharedVariables.shoutStyle + " ";

        // sshoutStyle
	set_string = set_string + "StyleSShout ";
	set_string = set_string + sharedVariables.sshoutStyle + " ";

        // TellStyle
	set_string = set_string + "StyleTell ";
	set_string = set_string + sharedVariables.tellStyle + " ";

        // QTellStyle
	set_string = set_string + "StyleQTell ";
	set_string = set_string + sharedVariables.qtellStyle + " ";

        // ResponseStyle
	set_string = set_string + "StyleResponse ";
	set_string = set_string + sharedVariables.responseStyle + " ";

        // nonResponseStyle
	set_string = set_string + "StyleNonResponse ";
	set_string = set_string + sharedVariables.nonResponseStyle + " ";

        // kibStyle
	set_string = set_string + "StyleKib ";
	set_string = set_string + sharedVariables.kibStyle + " ";


/*************** end styles ********************************/

	// qtellcolor  color
	set_string = set_string + "qtellcolor ";
	set_string = set_string + sharedVariables.qtellcolor.getRGB() + " ";

	for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
	{
		if(sharedVariables.tabStuff[zz].qtellcolor != null)
		{
				set_string = set_string + "qtellcolor" + zz + " ";
				set_string = set_string + sharedVariables.tabStuff[zz].qtellcolor.getRGB() + " ";
		}
	}



	// tabs not implmented now


	// channels

	for(int a = 0;  a< sharedVariables.maxChannels; a++)
	if(sharedVariables.channelOn[a] == 1)
	{
		set_string = set_string + "cn" + a + " ";
		set_string = set_string + sharedVariables.channelColor[a].getRGB() + " ";
	}



// channels

	for(int a = 0;  a<sharedVariables.maxChannels; a++)
	if(sharedVariables.channelOn[a] == 1)
	{
		set_string = set_string + "cstyle" + a + " ";
		set_string = set_string + sharedVariables.style[a] + " ";
	}

	// now add closing signal
	set_string = set_string + "[donecolor] ";


	// tab layouts opening
	set_string = set_string + "[mytablayouts] ";
	for(int iii=0; iii<sharedVariables.maxConsoleTabs; iii++)
		set_string = set_string + sharedVariables.consolesTabLayout[iii] + " ";

	// closing
	set_string = set_string + "[donemytablayouts] ";

	// console names layouts opening
	set_string = set_string + "[mynameslayouts] ";
	for(int iii=0; iii<sharedVariables.maxConsoleTabs; iii++)
		set_string = set_string + sharedVariables.consolesNamesLayout[iii] + " ";

	// closing
	set_string = set_string + "[donemynameslayouts] ";


	//players in my games opening
	set_string = set_string + "[playersInMyGame] ";
	set_string = set_string + sharedVariables.playersInMyGame + " ";
	// closing
	set_string = set_string + "[donepieces] ";


	// pieces opening
	set_string = set_string + "[pieces] ";
	set_string = set_string + sharedVariables.pieceType + " ";
	// closing
	set_string = set_string + "[donepieces] ";

	// board opening
	set_string = set_string + "[boards] ";
	set_string = set_string + sharedVariables.boardType + " ";
	// closing
	set_string = set_string + "[doneboards] ";


	// activitiesTabNumber
		set_string = set_string + "[activitiesTabNumber] ";
		set_string = set_string + sharedVariables.activitiesTabNumber + " ";
		// closing
		set_string = set_string + "[doneactivitiesTabNumber] ";


	// show notifyMainAlso
	set_string = set_string + "[notifyMainAlso] ";
	if(sharedVariables.notifyMainAlso == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donenotifyMainAlso] ";

	// makeSounds
	set_string = set_string + "[makeSounds] ";
	if(sharedVariables.makeSounds == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donemakeSounds] ";


	// show qsuggest
	set_string = set_string + "[qsuggestShow] ";
	if(sharedVariables.showQsuggest == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneqsuggestShow] ";

	// Andreys layout

	set_string = set_string + "[AndreysLayout] ";
		set_string = set_string + sharedVariables.andreysLayout + " ";
	// closing
		set_string = set_string + "[doneAndreysLayout] ";

	// sideways console on board

	set_string = set_string + "[sidewaysConsole] ";
	if(sharedVariables.sideways == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donesidewaysConsole] ";

	// materialCount
	set_string = set_string + "[materialCount] ";
	if(sharedVariables.showMaterialCount == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donematerialCount] ";



	// saveNamePass
	set_string = set_string + "[saveNamePass] ";
	if(sharedVariables.saveNamePass == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donesaveNamePass] ";


	// unobserveGoExamine
	set_string = set_string + "[unobserveGoExamine] ";
	if(sharedVariables.unobserveGoExamine == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneunobserveGoExamine] ";


	// drawCoordinates
	set_string = set_string + "[drawCoordinates] ";
	if(sharedVariables.drawCoordinates == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donedrawCoordinates] ";

	// ActivitiesOnTop
	set_string = set_string + "[ActivitiesOnTop] ";
	if(sharedVariables.ActivitiesOnTop == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneActivitiesOnTop] ";

	// dontReuseGameTabs
	set_string = set_string + "[dontReuseGameTabs] ";
	if(sharedVariables.dontReuseGameTabs == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donedontReuseGameTabs] ";


	// newObserveGameSwitch
	set_string = set_string + "[newObserveGameSwitch] ";
	if(sharedVariables.newObserveGameSwitch == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donenewObserveGameSwitch] ";

	// lowTimeColors
	set_string = set_string + "[lowTimeColors] ";
	if(sharedVariables.lowTimeColors == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donelowTimeColors] ";

	// autochat
	set_string = set_string + "[autochat] ";
	if(sharedVariables.autoChat == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneautochat] ";


	// blocksays
	set_string = set_string + "[blocksays] ";
	if(sharedVariables.blockSays == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneblocksays] ";


	// showratings
	set_string = set_string + "[showratings] ";
	if(sharedVariables.showRatings == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneshowratings] ";

	// showFlags
	set_string = set_string + "[showflags] ";
	if(sharedVariables.showFlags == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneshowflags] ";

	// showFlags
	set_string = set_string + "[gameendmess] ";
	if(sharedVariables.gameend == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donegameendmess] ";


	// showPallette
	set_string = set_string + "[showPallette] ";
	if(sharedVariables.showPallette == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneshowPallette] ";


	// uselightbackground
	set_string = set_string + "[uselightbackground] ";
	if(sharedVariables.useLightBackground == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneuselightbackground] ";


	// showconsolemenu
	set_string = set_string + "[showconsolemenu] ";
	if(sharedVariables.showConsoleMenu == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneshowconsolemenu] ";



	// autopopup
	set_string = set_string + "[autopopup] ";
	if(sharedVariables.autopopup == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneautopopup] ";


	// autohistorypopup
	set_string = set_string + "[autohistorypopup] ";
	if(sharedVariables.autoHistoryPopup == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneautohistorypopup] ";

	// observesounds
	set_string = set_string + "[observesounds] ";
	if(sharedVariables.makeObserveSounds == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneobservesounds] ";



 	// tilesrandom
	set_string = set_string + "[tilesrandom] ";
	if(sharedVariables.randomBoardTiles == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donetilesrandom] ";


 	// armyrandom
	set_string = set_string + "[armyrandom] ";
	if(sharedVariables.randomArmy == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donearmyrandom] ";

	// basketball
	set_string = set_string + "[basketball] ";
	if(sharedVariables.basketballFlag == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donebasketball] ";



// shout timestamp
	set_string = set_string + "[time-shout] ";
	if(sharedVariables.shoutTimestamp == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donetime-shout] ";
// qtell timestamp
	set_string = set_string + "[time-qtell] ";
	if(sharedVariables.qtellTimestamp == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donetime-qtell] ";


// reconnect timestamp
	set_string = set_string + "[time-reconnect] ";
	if(sharedVariables.reconnectTimestamp == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donetime-reconnect] ";



// tell timestamp
	set_string = set_string + "[time-tell] ";
	if(sharedVariables.tellTimestamp == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donetime-tell] ";

// channel timestamp
	set_string = set_string + "[time-channel] ";
	if(sharedVariables.channelTimestamp == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donetime-channel] ";

// ecluded pieces
for(int excl = 0; excl < sharedVariables.excludedPiecesWhite.length; excl++)
{
 if(sharedVariables.excludedPiecesWhite[excl]==true)
 {

// channel timestamp
	set_string = set_string + "[excluded] ";

		set_string = set_string + excl + " ";

	// closing
		set_string = set_string + "[doneexcluded] ";



 }// end if

}// end for

// ecluded pieces
for(int excl = 0; excl < sharedVariables.excludedPiecesBlack.length; excl++)
{
 if(sharedVariables.excludedPiecesBlack[excl]==true)
 {

// channel timestamp
	set_string = set_string + "[bexcluded] ";

		set_string = set_string + excl + " ";

	// closing
		set_string = set_string + "[donebexcluded] ";



 }// end if

}// end for














	// rotate aways
	set_string = set_string + "[rotateaways] ";
	if(sharedVariables.rotateAways == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donerotateaways] ";
	// iloggedon
	set_string = set_string + "[iloggedon] ";
	if(sharedVariables.iloggedon == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneiloggedon] ";



	// autobuffer
	set_string = set_string + "[autobufferchat] ";
	if(sharedVariables.autoBufferChat == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[doneautobufferchat] ";

	// disable Name Lists
	set_string = set_string + "[disableNameLists] ";
	if(sharedVariables.disableNameLists == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donedisableNameLists] ";
	// channel Number left
	set_string = set_string + "[numberchannelleft] ";
	if(sharedVariables.channelNumberLeft == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
		set_string = set_string + "[donenumberchannelleft] ";

 	// italics behavior what ` ` does
	set_string = set_string + "[italicsbehavior] ";
		set_string = set_string + sharedVariables.italicsBehavior + " ";

	// closing
		set_string = set_string + "[doneitalicsbehavior] ";


	// last move highlight
	set_string = set_string + "[lastMoveHighlight] ";
	if(sharedVariables.highlightMoves == true)
		set_string = set_string + "1" + " ";
	else
		set_string = set_string + "0" + " ";
	// closing
	set_string = set_string + "[donelastMoveHighlight] ";

	// game console type opening
	set_string = set_string + "[boardconsoletype] ";
	set_string = set_string +  sharedVariables.boardConsoleType + " ";
	// closing game console type
	set_string = set_string + "[doneboardconsoletype] ";
	// subframe console type opening
	set_string = set_string + "[subconsoletype] ";
	set_string = set_string +  sharedVariables.consoleLayout + " ";
	// closing subframe console type
	set_string = set_string + "[donesubconsoletype] ";


// tabsonly
	set_string = set_string + "[onlytabs] ";
	if(sharedVariables.tabsOnly == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing onlytabs
	set_string = set_string + "[doneonlytabs] ";


// activities opening
	set_string = set_string + "[activitiesNeverOpen] ";
	if(sharedVariables.activitiesNeverOpen == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing activities
	set_string = set_string + "[doneactivitiesNeverOpen] ";


// activities opening
	set_string = set_string + "[activitiesOpen] ";
	if(sharedVariables.activitiesOpen == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing activities
	set_string = set_string + "[doneactivitiesOpen] ";

// seeks opening
	set_string = set_string + "[seeksOpen] ";
	if(sharedVariables.seeksOpen == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing seeks
	set_string = set_string + "[doneseeksOpen] ";


// shoutsAlso
	set_string = set_string + "[alsoshouts] ";
	if(sharedVariables.shoutsAlso == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing shoutsAlso
	set_string = set_string + "[donealsoshouts] ";


// indent opening
	set_string = set_string + "[indent] ";
	if(sharedVariables.indent == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing indent
	set_string = set_string + "[doneindent] ";

// show userbutton titles
	set_string = set_string + "[showuserbuttontitles] ";
	if(sharedVariables.showButtonTitle == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing user button
	set_string = set_string + "[doneshowuserbuttontitles] ";




// notifysound
	set_string = set_string + "[noti-sound] ";
	if(sharedVariables.specificSounds[4] == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing notify sound
	set_string = set_string + "[donenoti-sound] ";


	// tells  tab
	if(sharedVariables.tellsToTab == true)
	{
	set_string = set_string + "[telltab] ";

	set_string = set_string + sharedVariables.tellTab + " ";


	// closing tellstab
	set_string = set_string + "[donetelltab] ";
	}


	set_string = set_string + "[tellconsole] ";

	set_string = set_string + sharedVariables.tellconsole + " ";


	// closing tellstab
	set_string = set_string + "[donetellconsole] ";

	// tells to tab
	set_string = set_string + "[tellstotab] ";
	if(sharedVariables.tellsToTab == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing tellstotab
	set_string = set_string + "[donetellstotab] ";



	// pgn loggingopening
	set_string = set_string + "[pgnlogging] ";
	if(sharedVariables.pgnLogging == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing pgn logging
	set_string = set_string + "[donepgnlogging] ";

	// pgn observed loggingopening
	set_string = set_string + "[pgnobservedlogging] ";
	if(sharedVariables.pgnObservedLogging == true)
	set_string = set_string + "1" + " ";
	else
	set_string = set_string + "0" + " ";

	// closing pgn logging
	set_string = set_string + "[donepgnobservedlogging] ";



	for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
	{
		set_string = set_string + "[typed" + zz + "] ";
		if(sharedVariables.tabStuff[zz].typed == true)
		set_string = set_string + "1" + " ";
		else
		set_string = set_string + "0" + " ";


		set_string = set_string + "[donetyped" + zz+ "] ";
	}

	for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
	{
		set_string = set_string + "[told" + zz + "] ";
		if(sharedVariables.tabStuff[zz].told == true)
		set_string = set_string + "1" + " ";
		else
		set_string = set_string + "0" + " ";


		set_string = set_string + "[donetold" + zz+ "] ";
	}

for(int boarc=0; boarc< sharedVariables.maxConsoleTabs; boarc++)
{
	set_string = set_string + "[Con" + boarc +"] ";
			set_string = set_string + "" + sharedVariables.myConsoleSizes[boarc].point0.x + " " + sharedVariables.myConsoleSizes[boarc].point0.y + " ";
			set_string = set_string + "" + sharedVariables.myConsoleSizes[boarc].con0x + " " + sharedVariables.myConsoleSizes[boarc].con0y + " ";
			set_string = set_string + "[doneCon" + boarc + "] ";
}




try {
int boar2=0;
for(int boar=0; boar< sharedVariables.openBoardCount; boar++)
{
       if(myboards[boar]!=null)
       if(sharedVariables.myBoardSizes[boar].con0x > 10)
       {
	set_string = set_string + "[Gam" + boar2 +"] ";
			set_string = set_string + "" + sharedVariables.myBoardSizes[boar2].point0.x + " " + sharedVariables.myBoardSizes[boar2].point0.y + " ";
			set_string = set_string + "" + sharedVariables.myBoardSizes[boar2].con0x + " " + sharedVariables.myBoardSizes[boar2].con0y + " ";
			set_string = set_string + "[doneGam" + boar + "] ";

       boar2++;
       }
}

}// end try
catch(Exception badboard){}
	set_string = set_string + "[ActivitiesSizes] ";
			set_string = set_string + "" + sharedVariables.myActivitiesSizes.point0.x + " " + sharedVariables.myActivitiesSizes.point0.y + " ";
			set_string = set_string + "" + sharedVariables.myActivitiesSizes.con0x + " " + sharedVariables.myActivitiesSizes.con0y + " ";
			set_string = set_string + "[doneActivitiesSizes] ";

	set_string = set_string + "[SeekSizes] ";
			set_string = set_string + "" + sharedVariables.mySeekSizes.point0.x + " " + sharedVariables.mySeekSizes.point0.y + " ";
			set_string = set_string + "" + sharedVariables.mySeekSizes.con0x + " " + sharedVariables.mySeekSizes.con0y + " ";
			set_string = set_string + "[doneSeekSizes] ";



	set_string = set_string + "[Font] ";
	aFont= sharedVariables.myFont.getFontName();
	aFont=aFont.replace(" ", "*");
	set_string = set_string + aFont + " ";
	aFontStyle="" + sharedVariables.myFont.getStyle();
	set_string = set_string + aFontStyle + " ";
	aFontSize="" + sharedVariables.myFont.getSize();
	set_string = set_string + aFontSize + " ";
	set_string = set_string + "[doneFont] ";

	set_string = set_string + "[analysisFont] ";
	aFont= sharedVariables.analysisFont.getFontName();
	aFont=aFont.replace(" ", "*");
	set_string = set_string + aFont + " ";
	aFontStyle="" + sharedVariables.analysisFont.getStyle();
	set_string = set_string + aFontStyle + " ";
	aFontSize="" + sharedVariables.analysisFont.getSize();
	set_string = set_string + aFontSize + " ";
	set_string = set_string + "[doneanalysisFont] ";


	set_string = set_string + "[eventsFont] ";
	aFont= sharedVariables.eventsFont.getFontName();
	aFont=aFont.replace(" ", "*");
	set_string = set_string + aFont + " ";
	aFontStyle="" + sharedVariables.eventsFont.getStyle();
	set_string = set_string + aFontStyle + " ";
	aFontSize="" + sharedVariables.eventsFont.getSize();
	set_string = set_string + aFontSize + " ";
	set_string = set_string + "[doneeventsFont] ";



	set_string = set_string + "[FonGame] ";
	aFont= sharedVariables.myGameFont.getFontName();
	aFont=aFont.replace(" ", "*");
	set_string = set_string + aFont + " ";
	aFontStyle="" + sharedVariables.myGameFont.getStyle();
	set_string = set_string + aFontStyle + " ";
	aFontSize="" + sharedVariables.myGameFont.getSize();
	set_string = set_string + aFontSize + " ";
	set_string = set_string + "[doneFont] ";


	set_string = set_string + "[FonInput] ";
	aFont= sharedVariables.inputFont.getFontName();
	aFont=aFont.replace(" ", "*");
	set_string = set_string + aFont + " ";
	aFontStyle="" + sharedVariables.inputFont.getStyle();
	set_string = set_string + aFontStyle + " ";
	aFontSize="" + sharedVariables.inputFont.getSize();
	set_string = set_string + aFontSize + " ";
	set_string = set_string + "[doneFont] ";


	set_string = set_string + "[FonClock] ";
	aFont= sharedVariables.myGameClockFont.getFontName();
	aFont=aFont.replace(" ", "*");
	set_string = set_string + aFont + " ";
	aFontStyle="" + sharedVariables.myGameClockFont.getStyle();
	set_string = set_string + aFontStyle + " ";
	aFontSize="" + sharedVariables.myGameClockFont.getSize();
	set_string = set_string + aFontSize + " ";
	set_string = set_string + "[doneFont] ";





	set_string = set_string + "[tabFont] ";
	aFont= sharedVariables.myTabFont.getFontName();
	aFont=aFont.replace(" ", "*");
	set_string = set_string + aFont + " ";
	aFontStyle="" + sharedVariables.myTabFont.getStyle();
	set_string = set_string + aFontStyle + " ";
	aFontSize="" + sharedVariables.myTabFont.getSize();
	set_string = set_string + aFontSize + " ";
	set_string = set_string + "[doneTabFont] ";


	for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
	{
		if(sharedVariables.tabStuff[zz].tabFont != null)
		{
			set_string = set_string + "[Font" + zz + "] ";
			aFont= sharedVariables.tabStuff[zz].tabFont.getFontName();
			aFont=aFont.replace(" ", "*");
			set_string = set_string + aFont + " ";
			aFontStyle="" + sharedVariables.tabStuff[zz].tabFont.getStyle();
			set_string = set_string + aFontStyle + " ";
			aFontSize="" + sharedVariables.tabStuff[zz].tabFont.getSize();
			set_string = set_string + aFontSize + " ";
			set_string = set_string + "[doneFont] ";
		}
	}


	for(int tnum = 1; tnum<sharedVariables.maxConsoleTabs; tnum++)
	{
	set_string = set_string + "[Chan" + tnum + "] ";
		for(int cnum=0; cnum< sharedVariables.maxChannels; cnum++)
		{
			try{

			if(sharedVariables.console[tnum][cnum]==1)
			     set_string = set_string + cnum + " ";
			}catch(Exception badchannel){}
		}// done cnum for
	set_string = set_string + "[doneChan" + tnum + "] ";

	}// done tnum for



	for(int tnum = 0; tnum<sharedVariables.maxConsoleTabs; tnum++)
	{
	set_string = set_string + "[QChan" + tnum + "] ";
		for(int cnum=0; cnum< sharedVariables.maxChannels; cnum++)
		{
			try{

			if(sharedVariables.qtellController[tnum][cnum]> 0)
			     set_string = set_string + cnum + " " + sharedVariables.qtellController[tnum][cnum] + " ";
			}catch(Exception badchannel){}
		}// done cnum for
	set_string = set_string + "[doneQChan" + tnum + "] ";

	}// done tnum for




// does channel in tab go to main also?
	set_string = set_string + "[MainChan" + "] ";
		for(int cnum=0; cnum<500; cnum++)
		{
			try{

			if(sharedVariables.mainAlso[cnum]== true)
			     set_string = set_string + cnum + " ";
			}catch(Exception badchannel){}
		}// done cnum for
	set_string = set_string + "[doneMainChan" + "] ";

// wall papper
set_string = set_string + "[wallpaper] ";
if(!sharedVariables.wallpaperFileName.equals(""))
{
set_string = set_string + sharedVariables.wallpaperFileName + " ";
}
else
set_string = set_string + "none ";

set_string = set_string + "[doneWallpaper] ";


/************* save tab names ****************/

for(int ab=0; ab<sharedVariables.maxConsoleTabs; ab++)
{
set_string = set_string + "[consoletabname" + ab + "] ";
if(!sharedVariables.consoleTabCustomTitles[ab].equals(""))
{
set_string = set_string + sharedVariables.consoleTabCustomTitles[ab] + " ";
}
else
set_string = set_string + "none ";

set_string = set_string + "[doneConsoletabname] ";
} // done for
/************* end save tab names ***********/


/************* save userbuttons ****************/

for(int ab=0; ab<sharedVariables.maxUserButtons; ab++)
{
set_string = set_string + "[userbutton" + ab + "] ";
if(!sharedVariables.userButtonCommands[ab].equals(""))
{
set_string = set_string + sharedVariables.userButtonCommands[ab] + " ";
}
else
set_string = set_string + "none ";

set_string = set_string + "[doneuserbutton] ";
} // done for
/************* end save userbuttons ***********/

// moveInputType
		set_string = set_string + "[moveInputType] ";
		if(sharedVariables.moveInputType == 1)
		set_string = set_string + "1" + " ";
		else
		set_string = set_string + "0" + " ";


		set_string = set_string + "[donemoveInputType] ";


// checkLegality
		set_string = set_string + "[checkLegality] ";
		if(sharedVariables.checkLegality == true)
		set_string = set_string + "1" + " ";
		else
		set_string = set_string + "0" + " ";


		set_string = set_string + "[donecheckLegality] ";
// tool bar visible
		set_string = set_string + "[toolbar] ";
		if(sharedVariables.toolbarVisible == true)
		set_string = set_string + "1" + " ";
		else
		set_string = set_string + "0" + " ";


		set_string = set_string + "[donetoolbar] ";


// tell switch
		set_string = set_string + "[tellswitch] ";
		if(sharedVariables.switchOnTell == true)
		set_string = set_string + "1" + " ";
		else
		set_string = set_string + "0" + " ";


		set_string = set_string + "[doneTellswitch] ";


// shout console
		set_string = set_string + "[ShoutConsole] ";

		set_string = set_string + sharedVariables.shoutRouter.shoutsConsole + " ";


		set_string = set_string + "[doneShoutConsole] ";

// sshout console
		set_string = set_string + "[SShoutConsole] ";

		set_string = set_string + sharedVariables.shoutRouter.sshoutsConsole + " ";


		set_string = set_string + "[doneSShoutConsole] ";

int visibleConsoles=0;
for(int ab=0; ab<sharedVariables.maxConsoleTabs; ab++)
	if(consoleSubframes[ab]!=null)
		if(consoleSubframes[ab].isVisible())
			visibleConsoles++;

set_string = set_string + "[visibleConsoles] " + visibleConsoles + " [doneVisibleConsoles] ";



	FileWrite out = new FileWrite();
		out.write(set_string);

	}//  end  method


	boolean readNow(gameboard boards[], subframe frames[], channels sharedVariables, JTextPane consoles[], JTextPane gameconsoles[])
	{
				String fontStyle;
				String fontSize;
				sharedVariables.hasSettings = false;
				int zz;
				String mystring="";
				String entry = "";
	
        
                try {
        engineFile = "lantern_engine_directory.ini";
	FileRead in = new FileRead();

       String engineString = in.read2(engineFile);
       if(engineString.length() > 2)
       sharedVariables.engineDirectory = new File(engineString);
	//write2(efstream, sharedVariables.engineDirectory.getPath());
        }
        catch(Exception easy){}

        	try {
			FileRead in = new  FileRead();

			StringTokenizer tokens = new StringTokenizer(in.read(), " ");

			if (tokens.nextToken().equals("[color]"))
			{
				sharedVariables.hasSettings=true;
				String temp = "j";
				String temp2="";

				while(!temp.equals(""))
				{
					temp = tokens.nextToken();
					if(temp.equals("[donecolor]"))
					break;

					// we read temp now  read temp2
					temp2 = tokens.nextToken();

					if(temp.equals("light_color"))
					{
						sharedVariables.lightcolor=new Color(Integer.parseInt(temp2));
					}
					if(temp.equals("dark_color"))
					{
						sharedVariables.darkcolor=new Color(Integer.parseInt(temp2));

					}

					if(temp.equals("clockForegroundColor"))
					{
						sharedVariables.clockForegroundColor=new Color(Integer.parseInt(temp2));
					}


 					if(temp.equals("highlightMovesColor"))
					{
						sharedVariables.highlightcolor=new Color(Integer.parseInt(temp2));
					}
					if(temp.equals("scrollHighlightMovesColor"))
					{
						sharedVariables.scrollhighlightcolor=new Color(Integer.parseInt(temp2));
					}


					if(temp.equals("boardForegroundColor"))
					{
						sharedVariables.boardForegroundColor=new Color(Integer.parseInt(temp2));
					}
					if(temp.equals("boardBackgroundColor"))
					{
						sharedVariables.boardBackgroundColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("ForColor"))
					{
						sharedVariables.ForColor=new Color(Integer.parseInt(temp2));

					}

						for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
						{
							if(temp.equals("ForColor" + zz))
							sharedVariables.tabStuff[zz].ForColor=new Color(Integer.parseInt(temp2));
						}

						for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
						{
							if(temp.equals("timestampColor" + zz))
							sharedVariables.tabStuff[zz].timestampColor=new Color(Integer.parseInt(temp2));
						}

					if(temp.equals("onMoveBoardBackgroundColor"))
					{
						sharedVariables.onMoveBoardBackgroundColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("responseColor"))
					{
						sharedVariables.responseColor=new Color(Integer.parseInt(temp2));

					}


					if(temp.equals("listcolor"))
					{
						sharedVariables.listColor=new Color(Integer.parseInt(temp2));

					}

					if(temp.equals("analysisForegroundColor"))
					{
						sharedVariables.analysisForegroundColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("analysisBackgroundColor"))
					{
						sharedVariables.analysisBackgroundColor=new Color(Integer.parseInt(temp2));

					}

					for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
					{
						if(temp.equals("responseColor" + zz))
						sharedVariables.tabStuff[zz].responseColor=new Color(Integer.parseInt(temp2));
					}

					if(temp.equals("defaultChannelColor"))
					{
						sharedVariables.defaultChannelColor=new Color(Integer.parseInt(temp2));

					}

					if(temp.equals("nameForegroundColor"))
					{
						sharedVariables.nameForegroundColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("nameBackgroundColor"))
					{
						sharedVariables.nameBackgroundColor=new Color(Integer.parseInt(temp2));

					}

					if(temp.equals("inputChatColor"))
					{
						sharedVariables.inputChatColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("inputCommandColor"))
					{
						sharedVariables.inputCommandColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("kibcolor"))
					{
						sharedVariables.kibcolor=new Color(Integer.parseInt(temp2));

					}

					if(temp.equals("activeTabForeground"))
					{
						sharedVariables.activeTabForeground=new Color(Integer.parseInt(temp2));

					}

					if(temp.equals("passiveTabForeground"))
					{
						sharedVariables.passiveTabForeground=new Color(Integer.parseInt(temp2));

					}


					if(temp.equals("tabImOnBackground"))
					{
						sharedVariables.tabImOnBackground=new Color(Integer.parseInt(temp2));

					}

					if(temp.equals("tabBackground"))
					{
						sharedVariables.tabBackground=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("tabBackground2"))
					{
						sharedVariables.tabBackground2=new Color(Integer.parseInt(temp2));

					}


					if(temp.equals("newInfoTabBackground"))
					{
						sharedVariables.newInfoTabBackground=new Color(Integer.parseInt(temp2));

					}

					if(temp.equals("tabBorderColor"))
					{
						sharedVariables.tabBorderColor=new Color(Integer.parseInt(temp2));

					}

					if(temp.equals("tellTabBorderColor"))
					{
						sharedVariables.tellTabBorderColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("chatTimestampColor"))
					{
						sharedVariables.chatTimestampColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("qtellChannelNumberColor"))
					{
						sharedVariables.qtellChannelNumberColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("channelTitlesColor"))
					{
						sharedVariables.channelTitlesColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("nameTellColor"))
					{
						sharedVariables.tellNameColor=new Color(Integer.parseInt(temp2));

					}




					if(temp.equals("qtellcolor"))
					{
						sharedVariables.qtellcolor=new Color(Integer.parseInt(temp2));

					}


/********************************* styles *****************************/
					if(temp.equals("StyleShout"))
					{
						sharedVariables.shoutStyle=Integer.parseInt(temp2);

					}

					if(temp.equals("StyleSShout"))
					{
						sharedVariables.sshoutStyle=Integer.parseInt(temp2);

					}

					if(temp.equals("StyleTell"))
					{
						sharedVariables.tellStyle=Integer.parseInt(temp2);

					}

					if(temp.equals("StyleQTell"))
					{
						sharedVariables.qtellStyle=Integer.parseInt(temp2);

					}

					if(temp.equals("StyleResponse"))
					{
						sharedVariables.responseStyle=Integer.parseInt(temp2);

					}

					if(temp.equals("StyleNonResponse"))
					{
						sharedVariables.nonResponseStyle=Integer.parseInt(temp2);

					}

					if(temp.equals("StyleKib"))
					{
						sharedVariables.kibStyle=Integer.parseInt(temp2);

					}

/******************************* end styles ***************************/

					for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
					{
						if(temp.equals("qtellcolor" + zz))
						sharedVariables.tabStuff[zz].qtellcolor=new Color(Integer.parseInt(temp2));
					}

					if(temp.equals("BackColor"))
					{
						sharedVariables.BackColor=new Color(Integer.parseInt(temp2));
						 for(int i=0; i < sharedVariables.openConsoleCount; i++)
						  {
						 	 if(consoles[i]!= null)
							 {
						 consoles[i].setBackground(sharedVariables.BackColor);
							  }
						  }


 					// now game boards
 	        			for(int i=0; i < sharedVariables.openBoardCount; i++)
 			 			{
 				 			if(gameconsoles[i]!= null)
 	 						{
 	        				gameconsoles[i].setBackground(sharedVariables.BackColor);
 	 						}
 						}

					}

					for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
					{
						if(temp.equals("BackColor" + zz))
						sharedVariables.tabStuff[zz].BackColor=new Color(Integer.parseInt(temp2));
					}

					if(temp.equals("MainBackColor"))
					{
						sharedVariables.MainBackColor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("shoutcolor"))
					{
						sharedVariables.shoutcolor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("sshoutcolor"))
					{
						sharedVariables.sshoutcolor=new Color(Integer.parseInt(temp2));

					}
					if(temp.equals("tellcolor"))
					{
						sharedVariables.tellcolor=new Color(Integer.parseInt(temp2));

					}
					for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
					{
						if(temp.equals("tellcolor" + zz))
						sharedVariables.tabStuff[zz].tellcolor=new Color(Integer.parseInt(temp2));
					}

					if(temp.startsWith("cn"))
					{
						int numb=Integer.parseInt(temp.substring(2, temp.length()));
						sharedVariables.channelOn[numb]=1;
						sharedVariables.channelColor[numb]=new Color(Integer.parseInt(temp2));

					}

					if(temp.startsWith("cstyle"))
					{
						int numb=Integer.parseInt(temp.substring(6, temp.length()));

						sharedVariables.style[numb]=Integer.parseInt(temp2);

					}




                                }// end while
			}// end if color






				String temp = "j";
				while(!temp.equals(""))
				{
				temp = tokens.nextToken();

				if (temp.equals("[sidewaysConsole]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.sideways=true;
					else
						sharedVariables.sideways=false;
					}
					catch(Exception zzz){}
				}
				if (temp.equals("[AndreysLayout]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.andreysLayout=1;
					else if(truth == 0)
						sharedVariables.andreysLayout=0;
					else
                                        	sharedVariables.andreysLayout=2;

					}
					catch(Exception zzz){}
				}

			if (temp.startsWith("[Chan"))
			{

				try {
				int tnum =Integer.parseInt( "" + temp.charAt(5));// this will break if console numbers become two digits or if they mess with ini file it would fail try
				int twoDigit=tnum;

				try {
					twoDigit=Integer.parseInt( "" + temp.charAt(5) + temp.charAt(6));
					tnum=twoDigit;
				}catch(Exception digit){}

				String temp2="";

				while(!temp.equals(""))
				{
					temp2 = tokens.nextToken();
					if(temp2.startsWith("[doneChan"))
					break;
					try {
						int cnum = Integer.parseInt(temp2);
						sharedVariables.console[tnum][cnum]=1;
					}catch(Exception badNumber){}


				}// end while
			}catch(Exception badchan){}

			}// end if chan






			if (temp.startsWith("[QChan"))
			{

				try {
				int tnum =Integer.parseInt( "" + temp.charAt(6));// this will break if console numbers become two digits or if they mess with ini file it would fail try
				int twoDigit=tnum;

				try {
					twoDigit=Integer.parseInt( "" + temp.charAt(6) + temp.charAt(7));
					tnum=twoDigit;
				}catch(Exception digit){}

				String temp2="";
				String temp3 = "";

				while(!temp.equals(""))
				{
					temp2 = tokens.nextToken();
					if(temp2.startsWith("[doneQChan"))
					break;
 					temp3 = tokens.nextToken();
					if(temp3.startsWith("[doneQChan"))
					break;

					try {
						int cnum = Integer.parseInt(temp2);
						int cnum2 = Integer.parseInt(temp3);
                                                sharedVariables.qtellController[tnum][cnum]=cnum2;
					}catch(Exception badNumber){}


				}// end while
			}catch(Exception badchan){}

			}// end if chan














			if (temp.equals("[MainChan]"))
			{

				try {
				String temp2="";

				while(!temp.equals(""))
				{
					temp2 = tokens.nextToken();
					if(temp2.startsWith("[doneMainChan"))
					break;
					try {
						int cnum = Integer.parseInt(temp2);
						sharedVariables.mainAlso[cnum]=true;
					}catch(Exception badNumber){}


				}// end while
			}catch(Exception badchan){}

			}// end if mainchan


				if(temp.equals("[mytablayouts]"))
				{
					String tabtype = tokens.nextToken();
					int iii=0;
					try {

					while(!tabtype.equals("donemytablayouts") && iii < sharedVariables.maxConsoleTabs)
					{
						int truth = Integer.parseInt(tabtype);
						sharedVariables.consolesTabLayout[iii]=truth;
						tabtype = tokens.nextToken();
						iii++;


					}// end while

				}// end try
				catch(Exception duitalk){}

				}// end if tablayouts


				if(temp.equals("[mynameslayouts]"))
				{
					String tabtype = tokens.nextToken();
					int iii=0;
					try {

					while(!tabtype.equals("donemynameslayouts") && iii < sharedVariables.maxConsoleTabs)
					{
						int truth = Integer.parseInt(tabtype);
						sharedVariables.consolesNamesLayout[iii]=truth;
						tabtype = tokens.nextToken();
						iii++;


					}// end while

				}// end try
				catch(Exception duitalk){}

				}// end if consoleanmeslayouts




				if (temp.equals("[playersInMyGame]"))
				{
				try {
					sharedVariables.playersInMyGame = Integer.parseInt(tokens.nextToken());
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[pieces]"))
				{
				try {
					sharedVariables.pieceType = Integer.parseInt(tokens.nextToken());
					}
					catch(Exception zzz){}
				}
				if (temp.equals("[notifyMainAlso]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.notifyMainAlso=true;
					else
						sharedVariables.notifyMainAlso=false;
					}
					catch(Exception zzz){}
				}
				if (temp.equals("[makeSounds]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.makeSounds=true;
					else
						sharedVariables.makeSounds=false;
					}
					catch(Exception zzz){}
				}
				if (temp.equals("[qsuggestShow]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.showQsuggest=true;
					else
						sharedVariables.showQsuggest=false;
					}
					catch(Exception zzz){}
				}




				if (temp.equals("[materialCount]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.showMaterialCount=true;
					else
						sharedVariables.showMaterialCount=false;
					}
					catch(Exception zzz){}
				}


 				if (temp.equals("[unobserveGoExamine]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.unobserveGoExamine=true;
					else
						sharedVariables.unobserveGoExamine=false;
					}
					catch(Exception zzz){}
				}

 

				if (temp.equals("[drawCoordinates]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.drawCoordinates=true;
					else
						sharedVariables.drawCoordinates=false;
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[ActivitiesOnTop]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.ActivitiesOnTop=true;
					else
						sharedVariables.ActivitiesOnTop=false;
					}
					catch(Exception zzz){}
				}
				if (temp.equals("[dontReuseGameTabs]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.dontReuseGameTabs=true;
					else
						sharedVariables.dontReuseGameTabs=false;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[saveNamePass]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.saveNamePass=true;
					else
						sharedVariables.saveNamePass=false;
					}
					catch(Exception zzz){}
				}








  				if (temp.equals("[newObserveGameSwitch]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.newObserveGameSwitch=true;
					else
						sharedVariables.newObserveGameSwitch=false;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[lowTimeColors]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.lowTimeColors=true;
					else
						sharedVariables.lowTimeColors=false;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[autochat]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.autoChat=true;
					else
						sharedVariables.autoChat=false;
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[blocksays]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.blockSays=true;
					else
						sharedVariables.blockSays=false;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[showratings]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.showRatings=true;
					else
						sharedVariables.showRatings=false;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[showPallette]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.showPallette=true;
					else
						sharedVariables.showPallette=false;
					}
					catch(Exception zzz){}
				}



				if (temp.equals("[gameendmess]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.gameend=true;
					else
						sharedVariables.gameend=false;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[showflags]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.showFlags=true;
					else
						sharedVariables.showFlags=false;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[uselightbackground]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.useLightBackground=true;
					else
						sharedVariables.useLightBackground=false;
					}
					catch(Exception zzz){}
				}


					if (temp.equals("[rotateaways]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.rotateAways=true;
					else
						sharedVariables.rotateAways=false;
					}
					catch(Exception zzz){}
				}
					if (temp.equals("[iloggedon]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.iloggedon=true;
					else
						sharedVariables.iloggedon=false;
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[excluded]"))
				{
				try {
					int truth2 = Integer.parseInt(tokens.nextToken());
                                         sharedVariables.excludedPiecesWhite[truth2]=true;

                                }
                                catch(Exception exclusion){}

                                }// end excluded
				if (temp.equals("[bexcluded]"))
				{
				try {
					int truth2 = Integer.parseInt(tokens.nextToken());
                                         sharedVariables.excludedPiecesBlack[truth2]=true;

                                }
                                catch(Exception exclusion){}

                                }// end excluded


				if (temp.equals("[showconsolemenu]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.showConsoleMenu=true;
					else
						sharedVariables.showConsoleMenu=false;
					}
					catch(Exception zzz){}
				}


					if (temp.equals("[tilesrandom]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.randomBoardTiles=true;
					else
						sharedVariables.randomBoardTiles=false;
					}
					catch(Exception zzz){}
				}


					if (temp.equals("[armyrandom]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.randomArmy=true;
					else
						sharedVariables.randomArmy=false;
					}
					catch(Exception zzz){}
				}

 					if (temp.equals("[basketball]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.basketballFlag=true;
					else
						sharedVariables.basketballFlag=false;
					}
					catch(Exception zzz){}
				}

					if(temp.equals("[autopopup]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.autopopup=true;
					else
						sharedVariables.autopopup=false;
					}
					catch(Exception zzz){}
				}

					if(temp.equals("[autohistorypopup]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.autoHistoryPopup=true;
					else
						sharedVariables.autoHistoryPopup=false;
					}
					catch(Exception zzz){}
				}

					if(temp.equals("[observesounds]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.makeObserveSounds=true;
					else
						sharedVariables.makeObserveSounds=false;
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[time-shout]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.shoutTimestamp=true;
					else
						sharedVariables.shoutTimestamp=false;
					}
					catch(Exception zzz){}
				}
				if (temp.equals("[time-qtell]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.qtellTimestamp=true;
					else
						sharedVariables.qtellTimestamp=false;
					}
					catch(Exception zzz){}
				}



				if (temp.equals("[time-reconnect]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.reconnectTimestamp=true;
					else
						sharedVariables.reconnectTimestamp=false;
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[time-tell]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.tellTimestamp=true;
					else
						sharedVariables.tellTimestamp=false;
					}
					catch(Exception zzz){}
				}
				if (temp.equals("[time-channel]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.channelTimestamp=true;
					else
						sharedVariables.channelTimestamp=false;
					}
					catch(Exception zzz){}
				}




					if (temp.equals("[autobufferchat]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.autoBufferChat=true;
					else
						sharedVariables.autoBufferChat=false;
					}
					catch(Exception zzz){}
				}
				
                                	if (temp.equals("[disableNameLists]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.disableNameLists=true;
					else
						sharedVariables.disableNameLists=false;
					}
					catch(Exception zzz){}
				}


					if (temp.equals("[numberchannelleft]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.channelNumberLeft=true;
					else
						sharedVariables.channelNumberLeft=false;
					}
					catch(Exception zzz){}
				}


                                  if (temp.equals("[italicsbehavior]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());

						sharedVariables.italicsBehavior=truth;

					}
					catch(Exception zzz){}
				}





                        	if (temp.equals("[lastMoveHighlight]"))
				{
				try {
					int truth = Integer.parseInt(tokens.nextToken());
					if(truth == 1)
						sharedVariables.highlightMoves=true;
					else
						sharedVariables.highlightMoves=false;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[boards]"))
				{
				try {
					sharedVariables.boardType = Integer.parseInt(tokens.nextToken());
					}
					catch(Exception zzz){}
				}

				if(temp.equals("[activitiesTabNumber]"))
				{
				try {
					sharedVariables.activitiesTabNumber = Integer.parseInt(tokens.nextToken());
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[boardconsoletype]"))
				{
				try {
					sharedVariables.boardConsoleType = Integer.parseInt(tokens.nextToken());
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[subconsoletype]"))
				{
				try {
					sharedVariables.consoleLayout = Integer.parseInt(tokens.nextToken());
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[onlytabs]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.tabsOnly =  false;
					else
					sharedVariables.tabsOnly = true;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[activitiesNeverOpen]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.activitiesNeverOpen =  false;
					else
					sharedVariables.activitiesNeverOpen = true;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[activitiesOpen]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.activitiesOpen =  false;
					else
					sharedVariables.activitiesOpen = true;
					}
					catch(Exception zzz){}
				}
 				if (temp.equals("[seeksOpen]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.seeksOpen =  false;
					else
					sharedVariables.seeksOpen = true;
					}
					catch(Exception zzz){}
				}
               		if (temp.equals("[indent]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.indent =  false;
					else
					sharedVariables.indent = true;
					}
					catch(Exception zzz){}
				}
				if (temp.equals("[alsoshouts]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.shoutsAlso =  false;
					else
					sharedVariables.shoutsAlso = true;
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[showuserbuttontitles]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.showButtonTitle =  false;
					else
					sharedVariables.showButtonTitle = true;
					}
					catch(Exception zzz){}
				}

				if (temp.equals("[noti-sound]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.specificSounds[4] =  false;
					else
					sharedVariables.specificSounds[4] = true;
					}
					catch(Exception zzz){}
				}
				if (temp.equals("[tellconsole]"))
				{
				try {
					String tellconsole= tokens.nextToken();
					try { int telltabnum=Integer.parseInt(tellconsole); sharedVariables.tellconsole=telltabnum; } catch(Exception cant){}

					}
					catch(Exception zzz){}
				}


				if (temp.equals("[telltab]"))
				{
				try {
					String telltab= tokens.nextToken();
					try { int telltabnum=Integer.parseInt(telltab); sharedVariables.tellTab=telltabnum; } catch(Exception cant){}

					}
					catch(Exception zzz){}
				}




				if (temp.equals("[tellstotab]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.tellsToTab =  false;
					else
					sharedVariables.tellsToTab = true;
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[pgnobservedlogging]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.pgnObservedLogging =  false;
					else
					sharedVariables.pgnObservedLogging = true;
					}
					catch(Exception zzz){}
				}


				if (temp.equals("[pgnlogging]"))
				{
				try {
					if(tokens.nextToken().equals("0"))
					sharedVariables.pgnLogging =  false;
					else
					sharedVariables.pgnLogging = true;
					}
					catch(Exception zzz){}
				}







				for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
				{
					if (temp.equals("[typed" + zz + "]"))
					{
					try {
						if(tokens.nextToken().equals("0"))
						sharedVariables.tabStuff[zz].typed =  false;
						else
						sharedVariables.tabStuff[zz].typed = true;
						}
						catch(Exception zzz){}
					}
				}// end for

				for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
				{
					if (temp.equals("[told" + zz + "]"))
					{
					try {
						if(tokens.nextToken().equals("0"))
						sharedVariables.tabStuff[zz].told =  false;
						else
						sharedVariables.tabStuff[zz].told = true;
						}
						catch(Exception zzz){}
					}
				}// end for



				if(temp.equals("[wallpaper]"))
				{
					try {
					mystring="";
					entry = "go";
					while(!entry.equals("[doneWallpaper]"))
					{
				if(!entry.equals("go") && !entry.equals("[doneWallpaper]"))
					mystring+= " ";
						entry=tokens.nextToken();
					if(!entry.equals("[doneWallpaper]"))
					mystring+= entry;
					}// end while
				if(mystring.equals("none"))
				sharedVariables.wallpaperFileName = "";
				else
				sharedVariables.wallpaperFileName = mystring;

 						}catch(Exception badWAll){}


				}

				for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
				{

				if(temp.equals("[consoletabname" + zz + "]"))
				{
					try {
					mystring="";
					entry = "go";
					while(!entry.equals("[doneConsoletabname]"))
					{



					entry=tokens.nextToken();
					if(!entry.equals("[doneConsoletabname]"))
					mystring+= entry + " ";
					}// end while

				if(mystring.length() > 1)
				mystring = mystring.substring(0, mystring.length() -1);
				if(mystring.equals("none"))
				sharedVariables.consoleTabCustomTitles[zz] = "";
				else
				sharedVariables.consoleTabCustomTitles[zz] = mystring;

 						}catch(Exception badWAll){}


				}// end if
				}// end for



// ************************* user buttons ******************************************


				for(zz=0; zz< sharedVariables.maxUserButtons; zz++)
				{

				if(temp.equals("[userbutton" + zz + "]"))
				{
					try {
					mystring="";
					entry = "go";
					while(!entry.equals("[doneuserbutton]"))
					{



					entry=tokens.nextToken();
					if(!entry.equals("[doneuserbutton]"))
					mystring+= entry + " ";
					}// end while

				if(mystring.length() > 1)
				mystring = mystring.substring(0, mystring.length() -1);
				if(mystring.equals("none"))
				sharedVariables.userButtonCommands[zz] = "";
				else
				sharedVariables.userButtonCommands[zz] = mystring;

 						}catch(Exception badWAll){}


				}// end if
				}// end for


// **********************************************************************************


					if (temp.equals("[moveInputType]"))
					{
					try {
						if(tokens.nextToken().equals("0"))
						sharedVariables.moveInputType =  0;
						else
						sharedVariables.moveInputType = 1;
						}
						catch(Exception zzz){}
					}

					if (temp.equals("[checkLegality]"))
					{
					try {
						if(tokens.nextToken().equals("0"))
						sharedVariables.checkLegality =  false;
						else
						sharedVariables.checkLegality = true;
						}
						catch(Exception zzz){}
					}

					if (temp.equals("[toolbar]"))
					{
					try {
						if(tokens.nextToken().equals("0"))
						sharedVariables.toolbarVisible =  false;
						else
						sharedVariables.toolbarVisible = true;
						}
						catch(Exception zzz){}
					}


					if (temp.equals("[tellswitch]"))
					{
					try {
						if(tokens.nextToken().equals("0"))
						sharedVariables.switchOnTell =  false;
						else
						sharedVariables.switchOnTell = true;
						}
						catch(Exception zzz){}
					}


					if (temp.equals("[ShoutConsole]"))
					{
					try {
						sharedVariables.shoutRouter.shoutsConsole= Integer.parseInt(tokens.nextToken());

						}
						catch(Exception zzz){}
					}
					if (temp.equals("[SShoutConsole]"))
					{
					try {
						sharedVariables.shoutRouter.sshoutsConsole= Integer.parseInt(tokens.nextToken());

						}
						catch(Exception zzz){}
					}









			if(temp.startsWith("[Con"))
			{
				int boar=0;

				try{
						String dummyNumber = temp.substring(4, temp.length()-1);
						boar= Integer.parseInt(dummyNumber);

			String lookupboard = "[Con" + boar + "]";
			if (temp.equals(lookupboard))
			{
				try {
					int px = Integer.parseInt(tokens.nextToken());
					int py = Integer.parseInt(tokens.nextToken());
					int cw = Integer.parseInt(tokens.nextToken());
					int ch = Integer.parseInt(tokens.nextToken());
					if(boar == 0)
					{
					frames[boar].setLocation(px, py);
					frames[boar].setSize(cw, ch);

					}// if boar == 0
					// set sizes
					sharedVariables.myConsoleSizes[boar].con0x = cw;
					sharedVariables.myConsoleSizes[boar].con0y = ch;
					sharedVariables.myConsoleSizes[boar].point0.x = px;
					sharedVariables.myConsoleSizes[boar].point0.y = py;

					}
				catch(Exception zzz)
				{ }
			}

		}// end try
		catch(Exception dumber){}
		}// end if starts with


















			if(temp.startsWith("[Gam"))
			{
				int boar=0;

				try{
						String dummyNumber = temp.substring(4, temp.length()-1);
						boar= Integer.parseInt(dummyNumber);

			String lookupboard = "[Gam" + boar + "]";
			if (temp.equals(lookupboard))
			{
				try {
					final int px = Integer.parseInt(tokens.nextToken());
					final int py = Integer.parseInt(tokens.nextToken());
					final int cw = Integer.parseInt(tokens.nextToken());
					final int ch = Integer.parseInt(tokens.nextToken());
					if(boar == 0)
					{

                                       final gameboard finalboard = boards[boar];
                                       final boolean useTopGames = sharedVariables.useTopGames;
                SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                              if(finalboard.topGame != null && useTopGames == true)
                              {
                 finalboard.topGame.setLocation(px, py);
		finalboard.topGame.setSize(cw, ch);
                              }
                              else
                              {
                                	finalboard.setLocation(px, py);
                                	finalboard.setSize(cw, ch);
                                	finalboard.recreate();
                              }

                            } catch (Exception e1) {
                                //ignore
                            }
                        }
                    });
                       		}// if boar == 0
					// set sizes
					sharedVariables.myBoardSizes[boar].con0x = cw;
					sharedVariables.myBoardSizes[boar].con0y = ch;
					sharedVariables.myBoardSizes[boar].point0.x = px;
					sharedVariables.myBoardSizes[boar].point0.y = py;

					}
				catch(Exception zzz)
				{ }
			}

		}// end try
		catch(Exception dumber){}
		}// end if starts with


			if(temp.equals("[ActivitiesSizes]"))
			{

				try {
					int px = Integer.parseInt(tokens.nextToken());
					int py = Integer.parseInt(tokens.nextToken());
					int cw = Integer.parseInt(tokens.nextToken());
					int ch = Integer.parseInt(tokens.nextToken());

					// set sizes
					sharedVariables.myActivitiesSizes.con0x = cw;
					sharedVariables.myActivitiesSizes.con0y = ch;
					sharedVariables.myActivitiesSizes.point0.x = px;
					sharedVariables.myActivitiesSizes.point0.y = py;

					}
				catch(Exception zzz)
				{ }

		}// end if equals

			if(temp.equals("[SeekSizes]"))
			{

				try {
					int px = Integer.parseInt(tokens.nextToken());
					int py = Integer.parseInt(tokens.nextToken());
					int cw = Integer.parseInt(tokens.nextToken());
					int ch = Integer.parseInt(tokens.nextToken());

					// set sizes
					sharedVariables.mySeekSizes.con0x = cw;
					sharedVariables.mySeekSizes.con0y = ch;
					sharedVariables.mySeekSizes.point0.x = px;
					sharedVariables.mySeekSizes.point0.y = py;

					}
				catch(Exception zzz)
				{ }

		}// end if equals




			if(temp.equals("[Font]"))
			{
				temp=tokens.nextToken();
				temp=temp.replace("*", " "); // we searlize is with * for spaces
				fontStyle=tokens.nextToken();
				fontSize=tokens.nextToken();

				try {
				Font aFont = new Font(temp, Integer.parseInt(fontStyle), Integer.parseInt(fontSize));
				if(aFont != null)
				{
					sharedVariables.myFont=aFont;
				for(int i=0; i < sharedVariables.openConsoleCount; i++)
						  {
						 	 if(consoles[i]!= null)
							 {
						 consoles[i].setFont(sharedVariables.myFont);
							  }

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

				}// end if not null font
				catch(Exception dd)
				{ }


			}// end if font



			if(temp.equals("[FonInput]"))
			{
				String temp9=tokens.nextToken();
				temp9=temp9.replace("*", " "); // we searlize is with * for spaces
				fontStyle=tokens.nextToken();
				fontSize=tokens.nextToken();

				try {
				Font aFont = new Font(temp9, Integer.parseInt(fontStyle), Integer.parseInt(fontSize));
				if(aFont != null)
				{

						sharedVariables.inputFont=aFont;
					temp="[allDone]";

				// we set the font after settings is called with the myconsolepanel.setfont() methos outside this class/file

				}// end if not null font
			}
				catch(Exception dd)
				{ }


			}// end if font


			if(temp.equals("[eventsFont]"))
			{
				String temp9=tokens.nextToken();
				temp9=temp9.replace("*", " "); // we searlize is with * for spaces
				fontStyle=tokens.nextToken();
				fontSize=tokens.nextToken();

				try {
				Font aFont = new Font(temp9, Integer.parseInt(fontStyle), Integer.parseInt(fontSize));
				if(aFont != null)
				{

						sharedVariables.eventsFont=aFont;
					temp="[allDone]";

				// we set the font after settings is called with the myconsolepanel.setfont() methos outside this class/file

				}// end if not null font
			}
				catch(Exception dd)
				{ }


			}// end if font

			if(temp.equals("[analysisFont]"))
			{
				String temp9=tokens.nextToken();
				temp9=temp9.replace("*", " "); // we searlize is with * for spaces
				fontStyle=tokens.nextToken();
				fontSize=tokens.nextToken();

				try {
				Font aFont = new Font(temp9, Integer.parseInt(fontStyle), Integer.parseInt(fontSize));
				if(aFont != null)
				{

						sharedVariables.analysisFont=aFont;
					temp="[allDone]";

				// we set the font after settings is called with the myconsolepanel.setfont() methos outside this class/file

				}// end if not null font
			}
				catch(Exception dd)
				{ }


			}// end if analysisFont



			if(temp.equals("[FonGame]") || temp.equals("[FonClock]"))
			{
				String temp9=tokens.nextToken();
				temp9=temp9.replace("*", " "); // we searlize is with * for spaces
				fontStyle=tokens.nextToken();
				fontSize=tokens.nextToken();

				try {
				Font aFont = new Font(temp9, Integer.parseInt(fontStyle), Integer.parseInt(fontSize));
				if(aFont != null)
				{
					if(temp.equals("[FonGame]"))
						sharedVariables.myGameFont=aFont;
					if(temp.equals("[FonClock]"))
						sharedVariables.myGameClockFont=aFont;
					temp="[allDone]";

				// we set the font after settings is called with the myconsolepanel.setfont() methos outside this class/file

				}// end if not null font
			}
				catch(Exception dd)
				{ }


			}// end if font













			if(temp.equals("[visibleConsoles]"))
			{
				try {
					temp=tokens.nextToken();
					int num=Integer.parseInt(temp);
					sharedVariables.visibleConsoles=num;
				}
				catch(Exception visible){}
			}//end if visible consoles






			if(temp.equals("[tabFont]"))
			{
				temp=tokens.nextToken();
				temp=temp.replace("*", " "); // we searlize is with * for spaces
				fontStyle=tokens.nextToken();
				fontSize=tokens.nextToken();

				try {
				Font bFont = new Font(temp, Integer.parseInt(fontStyle), Integer.parseInt(fontSize));
				if(bFont != null)
				{
					sharedVariables.myTabFont=bFont;
				}
					}catch(Exception tabfont){}
			}





			for(zz=0; zz< sharedVariables.maxConsoleTabs; zz++)
			{
				if(temp.equals("[Font" + zz + "]"))
				{
					temp=tokens.nextToken();
					temp=temp.replace("*", " "); // we searlize is with * for spaces
					fontStyle=tokens.nextToken();
					fontSize=tokens.nextToken();

				try {
					Font aFont = new Font(temp, Integer.parseInt(fontStyle), Integer.parseInt(fontSize));
					if(aFont !=null)
					sharedVariables.tabStuff[zz].tabFont=aFont;
				}catch(Exception d9){}
				}// end if
			}




                }// end while

		}// end try
		catch(Exception e)
		{  System.err.println("settings exception: "
                        + e.getMessage());
}

		return sharedVariables.hasSettings;
	}

class FileWrite
{

   void write(String s)
   {

    		// Create file
    		try {
					FileWriter fstream = new FileWriter(aFile);
					write2(fstream, s);
				}
			catch(Exception e)
			{
				try {
						FileWriter fstream = new FileWriter(aFileLinux);
						write2(fstream, s);
					}
				catch(Exception ee)
					{
						//it really doesnt exist}
					}
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

class FileRead
{
   String read()
   {
    return read2(aFile);

   }
   String read2(String aFile)
   {String s = "";
		try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      BufferedReader input=null;

      try {
		  	input=  new BufferedReader(new FileReader(aFile));
		  }
      catch(Exception ee)
      {
		     try {
		  			input=  new BufferedReader(new FileReader(aFileLinux));
		  	     }
		  	catch(Exception eee)
		  	     {
					 input = null;
				 }
  	   }  // end outer catch



      try {
        String line = null; //not declared within while loop
        /*
        * readLine is a bit quirky :
        * it returns the content of a line MINUS the newline.
        * it returns null only for the END of the stream.
        * it returns an empty String if two newlines appear in a row.
        */
        while (( line = input.readLine()) != null){
         s+=line;

        }
      }
    catch (IOException ex){     }
    finally
    {
        input.close();
    }// end finally
}// overall try
catch(Exception eeee)
{ }// overall catch

    return s.toString();

  }// end method  read
}// end file read class


}