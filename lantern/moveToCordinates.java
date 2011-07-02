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

class moveToCordinates
{
/*
int getxmove(String amove, int toggle, int iflipped,  int turn)
amove: the 4 digit algabraic move
toggle: 0 or 2 is it reading the first 2 chars or second two chars
iflipped: 1 or 0. if 1 returns 7- value
turn: used in crazyhouse to detect if looking for upper or lower case feed it 0 if not crazyhouse it wont matter
returns: collumn number 0-7 and as mentinoed inverted if iflipped 1

returns: the row 1-8 or 0 if an error
*/

int getxmove(String amove, int toggle, int iflipped,  int turn)
{
			int x=0;
			//crazyhouse moves
			if(toggle == 0 && amove.charAt(1) == '@')
			{

			if(amove.charAt(0 + toggle) == 'P')
				x=-1;
			if(amove.charAt(0 + toggle) == 'N')
				x=-2;
			if(amove.charAt(0 + toggle) == 'B')
				x=-3;
			if(amove.charAt(0 + toggle) == 'R')
				x=-4;
			if(amove.charAt(0 + toggle) == 'Q')
				x=-5;
			if(turn%2==1)
				x=x-6;

			return x;
			}


			if(amove.charAt(0 + toggle) == 'a')
			x=7;
			if(amove.charAt(0 + toggle) == 'b')
			x=6;
			if(amove.charAt(0 + toggle) == 'c')
			x=5;
			if(amove.charAt(0 + toggle) == 'd')
			x=4;
			if(amove.charAt(0 + toggle) == 'e')
			x=3;
			if(amove.charAt(0 + toggle) == 'f')
			x=2;
			if(amove.charAt(0 + toggle) == 'g')
			x=1;
			if(amove.charAt(0 + toggle) == 'h')
			x=0;

			if(iflipped == 1)
			return 7-x;

			return x;

}

/*
int getymove(String amove, int toggle)
amove: the 4 digit algabraic move
toggle: 0 or 2 is it reading the first 2 chars or second two chars
returns: the row 0-7 or 0 if an error
*/

int getymove(String amove, int toggle)
{
			if(amove.charAt(1 + toggle) == '1')
			return 0;
			if(amove.charAt(1 + toggle) == '2')
			return 1;
			if(amove.charAt(1 + toggle) == '3')
			return 2;
			if(amove.charAt(1 + toggle) == '4')
			return 3;
			if(amove.charAt(1 + toggle) == '5')
			return 4;
			if(amove.charAt(1 + toggle) == '6')
			return 5;
			if(amove.charAt(1 + toggle) == '7')
			return 6;
			if(amove.charAt(1 + toggle) == '8')
			return 7;

			return 0;

}




}