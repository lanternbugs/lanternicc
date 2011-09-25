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
/* the program uses concurrently linked queus to send data to other parts of the program. for example gameboardpanel will add the move , like to String data,  and another class, my telnet class will poll the queue and if there is data , then send it to icc.  for use in another program this class can preety much be modified to send whatever data you want to send through the queue. for example if you just have a game board, maybe you just want to send two ints, from and two. Its even possible to not use the queueu at all if you just wanted, for example if just using gameboard, to update a variable shared by the class waiting for the move, i..e moveMade=1 and update a from and to for that class and it simply waits for moveMade to = 1. but this is thread safe the queue.
*/
class myoutput {

	String data;
	int consoleNumber;
	int game;
	int tab;
	int closetab;
	int clearconsole;
	int trimconsole;
	int clearboard;
	int trimboard;
	int startengine;
	int focusConsole;
	int gameFocusConsole;
	int gameConsoleSide;
	int gameConsoleSize;
	String tabTitle;
	// these next two (game board and game looking) are ignored generally and not set or used
	// but during a game if sent from the mouse release function after a move
	// they are set to see if you are in a simul and switch you to the low time board
	int gameboard;
	int gamelooking;
	 int reconnectTry;
	 int soundBoard;
	 int boardClosing;
	 int repaintTabBorders;

myoutput(){

	data = "";
	boardClosing=-1;
	repaintTabBorders=-1;
	gameFocusConsole=-1;
	gameConsoleSide = -1;
	gameConsoleSize = -1;
	consoleNumber = -1;
	focusConsole = -1;
	soundBoard = -1;
	reconnectTry=-1;
	game=0; // set to 1 if message comes from gameboard
	tab=-1;
	tabTitle="";
	gameboard=-1;
	gamelooking=-1;
	closetab=-1;
	clearconsole=-1;
	clearboard=-1;
	trimconsole=-1;
	trimboard=-1;
	startengine=-1;
	}
}