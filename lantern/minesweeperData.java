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
import java.util.Random;
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


class minesweeperData
{
int [][] grid;
int [][] numbers;
int [][] markedGrid;

int COVERED = 0;
int UNCOVERED =1;
int BOMB = 2;
int UNMARKED = 0;
int MARKED = 1;
int collumns;
int rows;
int bombs;
int state;
int ONGOING =1;
int DONE =2;
int NOTSTARTED =0;
int time=0;
long initialTime;

boolean win = false;
boolean started = false; // first click starts clocks

void setState(int n)
{
	state = n;

}
void setRows(int n)
{
 rows=n;

}

void setCollumns(int n)
{
  collumns=n;
}

void setBombs(int n)
{
 bombs=n;
}
void makeGrid()
{
win=false;
started = false;
time=0;

grid = new int[collumns][rows];
markedGrid = new int[collumns][rows];

numbers = new int[collumns][rows];
populateBombs(bombs, collumns, rows);

for(int a=0; a< collumns; a++)
for(int b=0; b<rows; b++)
{
	if(grid[a][b]!= BOMB)
		grid[a][b]=COVERED;
	markedGrid[a][b]=UNMARKED;
	countBombs(a, b);

}

}// end makegrid

void populateBombs(int bombs, int col, int row)
{
	int a=0;

Random generator = new Random( System.currentTimeMillis() );
	while(a<bombs)
	{
		// picke a random number less than col*rows (max)
		// check  if  bomb. if not place  bomb  and increment a
		// if bomb continue

		int randomIndex = generator.nextInt( col*row );
		// verify this math below but it doesnt really matter how exact i am at this stage if i'm legal
		int newCol = randomIndex%col;
		int newRow = (int) randomIndex/col;
		if(grid[newCol][newRow] == BOMB)
		continue;
		else
		{
		grid[newCol][newRow] = BOMB;
		a++;
		}
	}// end while
} // end populate bombs


void countBombs(int col, int row)
{
	int n=0;

	// top
	if(row > 0)
	{
		if(col > 0)
		if(grid[col -1][row -1] == BOMB)
		n++;

		if(grid[col][row -1] == BOMB)
		n++;

		if(col < collumns -1)
		if(grid[col +1][row -1] == BOMB)
		n++;

	}




	// middle
		if(col > 0)
		if(grid[col -1][row] == BOMB)
		n++;


		if(col < collumns -1)
		if(grid[col +1][row] == BOMB)
		n++;

	// bottom
		if(row < rows -1)
		{
		if(col > 0)
		if(grid[col -1][row +1] == BOMB)
		n++;

		if(grid[col][row +1] == BOMB)
		n++;

		if(col < collumns -1)
		if(grid[col +1][row +1] == BOMB)
		n++;
		}

if(grid[col][row] == BOMB)
	numbers[col][row]=-1;
else
	numbers[col][row]=n;


}// end count bombs

void removeSquares(int col, int row)
{
	// top
	if(row > 0)
	{
		if(col > 0)
		if(numbers[col -1][row -1] == 0 && grid[col-1][row-1] != UNCOVERED)
		{
			grid[col-1][row-1]=UNCOVERED;
			removeSquares(col-1, row-1);
		}
		else
		grid[col-1][row-1]=UNCOVERED;

		if(numbers[col][row -1] == 0 && grid[col][row-1] != UNCOVERED)
		{
			grid[col][row-1]=UNCOVERED;
			removeSquares(col, row-1);
		}
		else
		grid[col][row-1]=UNCOVERED;

		if(col < collumns -1)
		if(numbers[col +1][row -1] == 0 && grid[col+1][row-1]!=UNCOVERED)
		{
			grid[col+1][row-1]=UNCOVERED;
			removeSquares(col+1, row-1);
		}
		else
		grid[col+1][row-1]=UNCOVERED;

	}




	// middle
		if(col > 0)
		if(numbers[col -1][row] == 0 && grid[col-1][row] != UNCOVERED)
		{
			grid[col-1][row]=UNCOVERED;
			removeSquares(col-1, row);
		}
		else
		grid[col-1][row]=UNCOVERED;



		if(col < collumns -1)
		if(numbers[col +1][row] == 0 && grid[col+1][row] != UNCOVERED)
		{
			grid[col+1][row]=UNCOVERED;
			removeSquares(col+1, row);
		}
		else
		grid[col+1][row]=UNCOVERED;

	// bottom
		if(row < rows -1)
		{
		if(col > 0)
		if(numbers[col -1][row +1] == 0 && grid[col-1][row+1] !=UNCOVERED)
		{
			grid[col-1][row+1]=UNCOVERED;
			removeSquares(col-1, row+1);
		}
		else
		grid[col-1][row+1]=UNCOVERED;

		if(numbers[col][row +1] == 0 && grid[col][row+1] != UNCOVERED)
		{
			grid[col][row+1]=UNCOVERED;
			removeSquares(col, row+1);
		}
		else
		grid[col][row+1]=UNCOVERED;

		if(col < collumns -1)
		if(numbers[col +1][row +1] == 0 && grid[col+1][row+1] != UNCOVERED)
		{
			grid[col+1][row+1]=UNCOVERED;
			removeSquares(col+1, row+1);
		}
		else
		grid[col+1][row+1]=UNCOVERED;

		}

}// end removesquares

boolean checkForWin()
{

	for(int a=0; a< collumns; a++)
	for(int b=0; b<rows; b++)
	if(grid[a][b] == COVERED)
	return false;

	return true;

}
}