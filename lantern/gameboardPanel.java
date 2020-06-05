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



/*********************

This class is your game board. the 64 squares, the drag move and the piece pallete, used in examine mode or crazyhouse.

The drawing of the board is arranged in 3 panels. This one, the gameboardConsolePanel, that draws your console and tabs

And the gameboardControlsPanel (found as nested class at bottom of game board now) which is the stuff to the right, the move slider, labels for names, arrows.

There is a gamestate array. each gamestate object holds the game. where the pieces are on the board, game info.

This panel typically looks at LookingAt, to tell which gamestate array index is the game to display.

If you convert this to a non tabbed game board, just use one gamestate array index, say the zero one, and set LookingAt to 0.

Also BoardIndex is the actual board we are on, for example first board created is BoardIndex 0, and if that board was looking at board 2's game

Then LookingAt would be 2 and BoardIndex 0.  For conversion to a non tabbed board, again just set both to 0 or some number where they are the same.

You can use the gameBoardControlsPanel for your slider and names etc and this class for your board, and of course, at least one gamestate object.

Within gameboard class there is an overallpanel.  This is the gameboards panel and the three panels talked about above are layed out on it.

If you convert this to a different type of board you will have to create some type of overall panel to group/layout the panels you want to use, say

This panel and the controls panel if you don't need a console, or tabs. Most of the rest of gameboard class is receiving info from icc and updating a gamestate.

There is a timer class in gameboard that is updating the clocks and keeping them ticking and you will need that if you want ticking clocks.

There is a queue object , a concurrentlyLinkedQueue, thread safe, that is used to send moves to the telnet class.

We have queue.add(amove); when a move is made.  The idea is the telnet class is on another thread, (chessbot4.java object), and it constantly loops and can check

The queue, for any data, and if there is data sent to queue, for example our move we added, it can send that data to icc or fics.

If you use this class and want a different way to indicate the player made a move on the board, or transmit that move to

Another part of your program,  feel free to implement one.


**********************/

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
import java.util.StringTokenizer;
import java.util.concurrent.locks.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.event.ChangeEvent.*;
import java.lang.Math;
class gameboardPanel extends JPanel implements MouseMotionListener, MouseListener
{

	int editable;
	Image [] img;
	gamestuff gameData;
	channels sharedVariables;
	ConcurrentLinkedQueue<myoutput> queue;
	resourceClass graphics;
	int bugging = -1;
        int xpiece = 99999; // piece value for examining x, can be anything i suppose just not in use

gameboardPanel(Image img1[], channels sharedVariables1, gamestuff gameData1, ConcurrentLinkedQueue<myoutput> queue1, resourceClass graphics1)
{
	editable = 1;
	mx=oldmx=my=oldmy=100;
	movingpiece=0;
	movingexaminepiece=0;
	arrowSquare = -1;

img=img1;
queue=queue1;
sharedVariables=sharedVariables1;
gameData=gameData1;
graphics=graphics1;
addMouseMotionListener(this);
addMouseListener(this);


}
		int examineSquareX;
		int examineSquareY;
		int examineOriginX;
		int examineOriginY;
		int bugExamineOriginY;
		int bugExamineOriginX;
                int boardx;
		int boardy;
		int bugboardx;
		int bugboardy;
                int coordinateX;
                int coordinateY;
	    int squarex;
	    int squarey;
		int width;
		int height;
		boolean inTheAir = false;


/* sets stuff like what width are we drawing the board. where do we start and end drawing. used both to draw the board
and to figure out where they clicked on our drawing. */
void setValues()
{

		        width = getWidth();
		        if(bugging > -1)
		        width = (int) width / 2;

				int trueWidth=width;
				height = getHeight();
                                if(bugging > -1)
                                boardy -= sharedVariables.myGameFont.getSize() + 5;
		if(bugging == -1)
		{
                if(width > height * 5/4 && sharedVariables.aspect == 1)
		width = (int) height * 5/4;
		if(width > height * 4/3 && sharedVariables.aspect == 2)
		width = (int) height * 4/3;
		if(width > height * 3/2 && sharedVariables.aspect == 3)
		width = (int) height * 3/2;
                }
				if(width < 150 && bugging == -1)
				width = 150;
				else if(bugging > -1 && width < 75)
				width = 75;

				if(height < 150)
				height = 150;

				boardx = 5;
		                boardy = 0;
		                if(bugging > -1)
		                boardy =sharedVariables.myGameFont.getSize() + 5;
                                bugboardx=width + 5;
                                bugboardy=boardy;
				squarex = 5;
				squarey=5;
				double width1 = (double) width - boardx;
				double height1= (double) height;
				squarex = (int) (width1 * 1/8);
				squarey = (int) (height1 * 1/8);


		if(sharedVariables.aspect == 0 || bugging > -1 ) // 1:1 and playingBug is the partners game
		{
			if(squarex > squarey)// preserve square aspect ratio // above this preserves just piece
			squarex=squarey;
			else
			squarey=squarex;
		}
                int eightHigh = squarey * 8 + 10;
                boardy = (height - eightHigh) / 2;
                if(boardy<0)
                boardy=0;
                if(bugging > -1)
                {
                 if(boardy < sharedVariables.myGameFont.getSize() + 5)
                 boardy = sharedVariables.myGameFont.getSize() + 5;
                 bugboardy=boardy;

                }
                int eightWide = squarex * 9 + 5;
                boardx = (width - eightWide) / 2;
                if(boardx < 5)
                boardx = 5;
		examineSquareX=0;
		examineSquareY=0;
		examineOriginX=0;
		examineOriginY=0;

		if(sharedVariables.mygame[gameData.LookingAt].piecePallette== true && (sharedVariables.showPallette == true || sharedVariables.mygame[gameData.LookingAt].state != sharedVariables.STATE_EXAMINING ||
                                           sharedVariables.mygame[gameData.LookingAt].wild == 24 || sharedVariables.mygame[gameData.LookingAt].wild == 23))
		{
			while((double) trueWidth - (8 * squarex) < (double) (squarex * 1.3))// we are not good if it hits here so we fix it in loop
			{
				width1-=8; // chop 8 from width which is 1 from each square
				squarex-=1;
				squarey-=1;
			}


			examineSquareX=squarex;
			examineSquareY=(int)((8 * squarey) / 13); // propportion y to fit board height
			if(examineSquareX > examineSquareY) // perspective
			examineSquareX=examineSquareY;

			examineOriginX= (int) (8 * squarex) + boardx + (int) (squarex * .2); // origin is upper left corner. where we start drawing this piece pallette
			examineOriginY= boardy;
			bugExamineOriginX=examineOriginX + width;
			bugExamineOriginY=examineOriginY;
			//g.drawString("sqx " + squarex + "sqy " + squarey + "esqx " + examineSquareX + "esqy "+ examineSquareY + "eOx " + examineOriginX + "eOy " + examineOriginY, 0, height - 20);
		}

      if(sharedVariables.drawCoordinates == true)
      {
        coordinateX = (int)( (double) squarex * .06);
        coordinateY = (int) ( (double) squarey * .06);
       squarex -=coordinateX;
       squarey -=coordinateY;
       coordinateX*=8;
       coordinateY*=8;
       boardx+=coordinateX;

      }

}
	int getArrowSquare()
	{
		setValues();

		for(int a = 0; a < 8; a++)
		{


			for(int b=0; b < 8; b++)
			{


				if(mx > boardx +  b * squarex && mx < boardx +  b * squarex + squarex)
				{
					if(my > boardy + a * squarey && my < boardy + a * squarey + squarey)
					{
						int aa=a;

						int gameslot = 63 - (aa * 8 + b);


							return gameslot;
					}
		    	}
		    }
		}
             return -100;
 }

/* returns i think -100 for not a click on square, and negative piece value if they click
on examine mode pallete, which you can easily turn on or off as you see fit, i think i turn it on if wild = 23 or state is STATE_EXAMINING
click on teh 64 squar4es it returns 0-63*/
	int getPiece()
	{
		setValues();

		for(int a = 0; a < 8; a++)
		{


			for(int b=0; b < 8; b++)
			{


				if(mx > boardx +  b * squarex && mx < boardx +  b * squarex + squarex)
				{
					if(my > boardy + a * squarey && my < boardy + a * squarey + squarey)
					{
						int aa=a;

						int gameslot = 63 - (aa * 8 + b);
						int piece=sharedVariables.mygame[gameData.LookingAt].board[gameslot];

							return gameslot;
					}
		    	}
		    }
		}


		if(sharedVariables.mygame[gameData.LookingAt].piecePallette== true)
		for(int a = 0; a < 13; a++)
		{

				if(mx > examineOriginX  && mx < examineOriginX  + examineSquareX)
				{
					if(my > examineOriginY + a * examineSquareY && my < examineOriginY + a * examineSquareY + examineSquareY)
					{

						int piece=0;


						piece = getExaminePieceNumber(a);

							if(piece != 0) // we will support 0
							return -piece;
					}
		    	}
		}

		return -100;

	}


/* draws our board.  can draw two possible boards. if the slider is below the maximum, scrolled back, it gets the slider board at the spot
the slider is on. otherwise it draws the curernt in play board*/

	public void drawCoordinates(Graphics g, int boardx, int boardy, int squarex, int squarey)
        {
		Graphics2D g2 = (Graphics2D) g;

              int fontX = boardx-coordinateX;
              int fontY = boardy * squarey * 8 + coordinateY;
              String [] boardFiles = {"a", "b", "c", "d", "e", "f", "g", "h"};
              String [] boardRows = {"1", "2", "3", "4", "5", "6", "7", "8"};
 	      g2.setColor(sharedVariables.boardForegroundColor);
              int fsize =(int) ((double) coordinateX * .8);
	      Font coordinateFont = new Font(sharedVariables.myGameFont.getFontName(), Font.BOLD, fsize); // "Times New Roman"
              g2.setFont(coordinateFont);
              int xoffset = (int) ((double) coordinateX/4);

              if(sharedVariables.mygame[gameData.LookingAt].iflipped == 0)
              {

                 for(int a=0; a< 8; a++)
                 {
                  g2.drawString(boardRows[7-a], boardx-coordinateX, boardy + a * squarey + (int) ((double) squarey/2));
                  g2.drawString(boardFiles[a], boardx + squarex * a + (int) ((double) squarex/2) - xoffset, boardy + squarey*8 +(int) ((double)coordinateY * .85));

                 }


              }
                else
                {

                 for(int a=0; a< 8; a++)
                 {
                  g2.drawString(boardRows[a], boardx-coordinateX, boardy + a * squarey + (int) ((double) squarey/2));
                  g2.drawString(boardFiles[7-a], boardx + squarex * a + (int) ((double) squarex/2) - xoffset, boardy + squarey*8 + (int) ((double)coordinateY * .85));

                 }


              }


        }


	public void paintShapes(Graphics g, int boardx, int boardy, int squarex, int squarey)
	{
		// will execute this function if sliding != 1
		Graphics2D g2 = (Graphics2D) g;
		int offsetX = (int) squarex / 10;
		int offsetY = (int ) squarey/10;


		for(int a = 0; a < sharedVariables.mygame[gameData.LookingAt].myShapes.size(); a++)
		{
				if(sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).type == sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).Arrow)
				{
					//g2.drawString(" a = " + a + " and from is " + sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).from, 500,500);
					int from = sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).from;
					int xfrom =(int) ((from%8) *squarex + squarex/2 + boardx ) ;
					int yfrom=(int) (from/8 * squarey + squarey/2 + boardy);

					int to = sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).to;
					int xto =(int) ((to%8) *squarex + squarex/2 + boardx ) ;
					int yto=(int)(to/8 * squarey + squarey/2 + boardy);
					g.setColor(new Color(255,0,0));

					if(sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).from /8 != sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).to/8)
					{
						for(int b=0; b< offsetX; b++)
						g.drawLine(xfrom + b,yfrom,xto + b,yto);
					}
					else
					{
						for(int b=0; b< offsetY; b++)
						g.drawLine(xfrom ,yfrom+ b,xto ,yto+ b);

					}

				}

				else  if(sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).type == sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).Circle)
				{
					int from = sharedVariables.mygame[gameData.LookingAt].myShapes.get(a).from;
					int xfrom =(int) ((from%8) *squarex + squarex/10 + boardx ) ;
					int yfrom=(int) (from/8 * squarey + squarey/10 + boardy);

					g.setColor(new Color(0,0,255));
                                         offsetX = (int) squarex / 16;
                                         if(offsetX < 2)
                                         offsetX = 2;
					for(int b =0; b < offsetX; b++)
				//	g.drawArc(xfrom, yfrom, squarex *2/3 + b, squarey *2/ 3 + b, 0 , 360);
			{
                          g.drawOval(xfrom + b - (offsetX/2), yfrom, squarex * 4/5, squarey * 4/5 );
                          g.drawOval(xfrom, yfrom + b - (offsetX/2), squarex * 4/5, squarey * 4/5 );
				//g.drawOval(xfrom, yfrom, squarex *2/3, squarey *2/ 3 );

                         }// end for b=0
				}

		}
	}
           // mx - ((int) (squarex -4 ) /2), my - ((int) (squarey -4 ) /2)
        public void  drawCheckersPiece(Graphics g, int a, int b, int piece)
        {
          
          int yOff = squarey/12;
          int xOff = squarex / 12;
          double fxOff = (double) squarex/12;
        if(piece == 1)
        {
                 if(sharedVariables.checkersPieceType == 1)
                  g.setColor(Color.RED);
                  else
                   g.setColor(new Color(240, 240, 240));
                  g.fillOval( a + xOff  ,  b + yOff , squarex - (2*xOff), squarey - (2*yOff));
                  // g.setColor(Color.BLACK);
                 /*
                 circle indented in a bit
                  drawCheckersCircles(g, a + xOff * 2 , b + yOff * 2 ,  squarex - (xOff * 4), squarey - (yOff * 4) );
                  drawCheckersCircles(g, a + xOff * 2  + 1, b + yOff * 2  + 1,  squarex - (xOff * 4) - 2, squarey - (yOff * 4) - 2 );
                  drawCheckersCircles(g, a + xOff * 2  + 2, b + yOff * 2  + 2,  squarex - (xOff * 4) - 4, squarey - (yOff * 4) - 4 );
               */ 
              // g.setColor(new Color(35,0,0));
                 if(sharedVariables.checkersPieceType == 1)
                g.setColor(Color.RED.darker().darker());
                else
                g.setColor((new Color(240, 240, 240)).darker().darker());
                  drawCheckersCircles(g, a + xOff , b + yOff ,  squarex - (xOff * 2), squarey - (yOff * 2) );
                  drawCheckersCircles(g, a + xOff  + 1, b + yOff  + 1,  squarex - (xOff * 2) - 2, squarey - (yOff * 2) - 2 );
                      drawCheckersCircles(g, a + xOff * 3 , b + yOff * 3 ,  squarex - (xOff * 6), squarey - (yOff * 6) );
                  drawCheckersCircles(g, a + xOff * 3 + 1, b + yOff * 3  + 1,  squarex - (xOff * 6) - 2, squarey - (yOff * 6) - 2 );

         }
         else if(piece == 7)
         {
                  g.setColor(new Color(35,0,0));
                 g.fillOval( a + xOff  ,  b + yOff , squarex - (2*xOff), squarey - (2*yOff));
                  g.setColor((new Color(60,0,0)).brighter().brighter().brighter());
                 /*
                 circle indented in a bit
                 drawCheckersCircles(g, a + xOff * 2 , b + yOff * 2 ,  squarex - (xOff * 4), squarey - (yOff * 4) );
                  drawCheckersCircles(g, a + xOff * 2  + 1, b + yOff * 2  + 1,  squarex - (xOff * 4) - 2, squarey - (yOff * 4) - 2 );
                  drawCheckersCircles(g, a + xOff * 2  + 2, b + yOff * 2  + 2,  squarex - (xOff * 4) - 4, squarey - (yOff * 4) - 4 );
                   */
                 // drawCheckersCircles(g, a + xOff * 4, b + yOff * 4,  squarex - (xOff * 8), squarey - (yOff * 8) );
                  drawCheckersCircles(g, a + xOff , b + yOff ,  squarex - (xOff * 2), squarey - (yOff * 2) );
                  drawCheckersCircles(g, a + xOff  + 1, b + yOff  + 1,  squarex - (xOff * 2) - 2, squarey - (yOff * 2) - 2 );
                    drawCheckersCircles(g, a + xOff * 3 , b + yOff * 3 ,  squarex - (xOff * 6), squarey - (yOff * 6) );
                  drawCheckersCircles(g, a + xOff * 3 + 1, b + yOff * 3  + 1,  squarex - (xOff * 6) - 2, squarey - (yOff * 6) - 2 );

          }
          if(piece == 6)
        {
                 if(sharedVariables.checkersPieceType == 1)
                  g.setColor(Color.RED);
                  else
                   g.setColor(new Color(240, 240, 240));

                 g.fillOval( a + ((int)fxOff)  ,  b + ((int)fxOff) , squarex - ((int)(2*fxOff)), squarey - ((int)(2*fxOff)));

                /*  int fsize =(int) ((double) squarey * .55);
	          Font checkersFont = new Font("Arial", Font.BOLD, fsize); // "Times New Roman"
                 g.setFont(checkersFont);
                g.drawString("K",a +squarex/3 , b +  (14 * squarey)/20);
                 */
                if(sharedVariables.checkersPieceType == 1)
                 g.setColor(Color.RED.darker().darker());
                else
                g.setColor((new Color(240, 240, 240)).darker().darker());

                /* for(int i =  xOff; i <=  xOff * 3 + 1; i++)
                 {
                   drawCheckersCircles(g, a+i , b + i ,  squarex - i * 2, squarey - i * 2 );
                 }
                  */

                  drawCheckersCircles(g, a + ((int)fxOff) , b + ((int) fxOff) ,  squarex - ((int)(fxOff * 2)), squarey - ((int)(fxOff * 2)) );
                  drawCheckersCircles(g, a + ((int) (xOff  + 1)), b + ((int) (fxOff  + 1)),  squarex - ((int)(xOff * 2)) - 2, squarey - ((int)(fxOff * 2)) - 2 );


                   // drawCheckersCircles(g, a + ((int) (fxOff * 3)) , b + ((int) (fxOff * 3)) ,  squarex - ((int) (fxOff * 6)), squarey - ((int) (fxOff * 6)) );
                  //drawCheckersCircles(g, a +((int) (fxOff * 3)) + 1, b + ((int) (fxOff * 3))  + 1,  squarex - ((int) (fxOff * 6)) - 2, squarey - ((int) (fxOff  * 6)) - 2 );
               //  drawCheckersCircles(g, a + xOff * 5 , b + yOff *  5 ,  squarex - (xOff * 10), squarey - (yOff * 10) );
                //  drawCheckersCircles(g, a + xOff *  5 + 1, b + yOff *  5  + 1,  squarex - (xOff * 10) - 2, squarey - (yOff * 10) - 2 );
             /*  for(double i =  fxOff * 3; i <=  squarex/2 ; i++)
                 {
                   drawCheckersCircles(g, ((int)(a+i)) ,((int)( b + i)) ,  squarex - ((int)(i * 2)), squarey - ((int)(i * 2)) );
                 }
               */
               drawCheckersCircles(g, a + ((int) (fxOff * 3)) , b + ((int) (fxOff * 3)) ,  squarex - ((int) (fxOff * 6)), squarey - ((int) (fxOff * 6)) );
               g.fillOval( a + ((int)(fxOff * 3))  ,  b + ((int)(fxOff * 3)) , squarex - ((int)(6*fxOff)), squarey - ((int)(6*fxOff)));


        }

       if(piece == 12)
        {
                  g.setColor(new Color(35,0,0));
                 g.fillOval( a + ((int)fxOff)  ,  b + ((int)fxOff) , squarex - ((int)(2*fxOff)), squarey - ((int)(2*fxOff)));

                 /* int fsize =(int) ((double) squarey * .55);
	          Font checkersFont = new Font("Arial", Font.BOLD, fsize); // "Times New Roman"
                 g.setFont(checkersFont);
                g.drawString("K",a +squarex/3 , b +  (14 * squarey)/20);
                   */
                  g.setColor((new Color(60,0,0)).brighter().brighter().brighter());

                /* for(int i =  xOff; i <=  xOff * 3 + 1; i++)
                 {
                   drawCheckersCircles(g, a+i , b + i ,  squarex - i * 2, squarey - i * 2 );
                 }
                  */

                 drawCheckersCircles(g, a + ((int)fxOff) , b + ((int) fxOff) ,  squarex - ((int)(fxOff * 2)), squarey - ((int)(fxOff * 2)) );
                  drawCheckersCircles(g, a + ((int) (xOff  + 1)), b + ((int) (fxOff  + 1)),  squarex - ((int)(xOff * 2)) - 2, squarey - ((int)(fxOff * 2)) - 2 );

                   // drawCheckersCircles(g, a + ((int) (fxOff * 3)) , b + ((int) (fxOff * 3)) ,  squarex - ((int) (fxOff * 6)), squarey - ((int) (fxOff * 6)) );
                  //drawCheckersCircles(g, a +((int) (fxOff * 3)) + 1, b + ((int) (fxOff * 3))  + 1,  squarex - ((int) (fxOff * 6)) - 2, squarey - ((int) (fxOff  * 6)) - 2 );
               //  drawCheckersCircles(g, a + xOff * 5 , b + yOff *  5 ,  squarex - (xOff * 10), squarey - (yOff * 10) );
                //  drawCheckersCircles(g, a + xOff *  5 + 1, b + yOff *  5  + 1,  squarex - (xOff * 10) - 2, squarey - (yOff * 10) - 2 );
              /*  for(double i =  fxOff * 3; i <=  squarex/2 ; i++)
                 {
                   drawCheckersCircles(g, ((int)(a+i)) ,((int)( b + i)) ,  squarex - ((int)(i * 2)), squarey - ((int)(i * 2)) );
                 }
               */
               drawCheckersCircles(g, a + ((int) (fxOff * 3)) , b + ((int) (fxOff * 3)) ,  squarex - ((int) (fxOff * 6)), squarey - ((int) (fxOff * 6)) );
               g.fillOval( a + ((int)(fxOff * 3))  ,  b + ((int)(fxOff * 3)) , squarex - ((int)(6*fxOff)), squarey - ((int)(6*fxOff)));

        }
        }// end function


        public void drawCheckersCircles(Graphics g, int x, int y,  int width, int height )
        {
           Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    Ellipse2D.Double hole = new Ellipse2D.Double();
    hole.width = width;
    hole.height = height;
    hole.x = x;
    hole.y = y;
    g2d.draw(hole);
        }
	public void paintComponent(Graphics g)
		{

		try {

		super.paintComponent(g);
                 bugging = amPlayingBug();
		setBackground(sharedVariables.boardBackgroundColor);
		int [] slidingBoard = new int[64];
		int sliding = 0;
		boolean startPosition =false;
		if(sharedVariables.moveSliders[gameData.BoardIndex].getValue() < sharedVariables.moveSliders[gameData.BoardIndex].getMaximum())
		{
			sliding=1;
			sharedVariables.mygame[gameData.LookingAt].getSliderBoard(sharedVariables.moveSliders[gameData.BoardIndex].getValue(), slidingBoard);
		        if(sharedVariables.moveSliders[gameData.BoardIndex].getValue() == 0 ||
                        (sharedVariables.moveSliders[gameData.BoardIndex].getValue() == 1 &&
                        (sharedVariables.mygame[gameData.LookingAt].moveListTo[0] == sharedVariables.mygame[gameData.LookingAt].moveListFrom[0]
                        && sharedVariables.mygame[gameData.LookingAt].moveListFrom[0] > 0)))
		        startPosition =true;
                }
		else if(sharedVariables.mygame[gameData.LookingAt].currentLastto >= 0)
		{
                   sharedVariables.mygame[gameData.LookingAt].lastfrom=sharedVariables.mygame[gameData.LookingAt].currentLastfrom;
                   sharedVariables.mygame[gameData.LookingAt].lastto = sharedVariables.mygame[gameData.LookingAt].currentLastto;
                    if(sharedVariables.mygame[gameData.LookingAt].movetop == 0 ||
                    (sharedVariables.mygame[gameData.LookingAt].currentLastto == sharedVariables.mygame[gameData.LookingAt].currentLastfrom
                    && sharedVariables.mygame[gameData.LookingAt].movetop == 1))
                    startPosition = true;
                 sharedVariables.mygame[gameData.LookingAt].setMaterialCount(sharedVariables.mygame[gameData.LookingAt].board);
                }

		//g.drawString("in here",  50,  50);
		Graphics2D g2 = (Graphics2D) g;

		setValues();


		int piecexy = 5, difx=0, dify=0;
		if(sharedVariables.aspect > 0) // greater than 0 squares are not 1:1
		{
		if(squarex > squarey) // pieces are same width and height in original drawing 60x60. we use the smaller of width or height for piece with and height to preserve = scaling
		{
			piecexy=squarey;
			difx = (int) (squarex - squarey )/2;
		}
		else
		{
			piecexy=squarex;
			dify = (int) (squarey - squarex ) /2;
		}
		}// end aspect > 0



		int timex = (int) (width * 9/10);
		int otimey = (int) (height * 1/4);
		int ptimey = (int) (height * 3/4);
		int c=0;
		//g.drawString("" + blackClock,  timex,  otimey);
		//g.drawString("" + whiteClock,  timex,  ptimey);
		int choice=0;
		int blackChoice=0;


		if(difx == 0 && dify == 0 && graphics.resizable[getPieceType(1)] == false)
		{
                  choice=findPieceMatch(1);


                 }

		if(difx == 0 && dify == 0 && graphics.resizable[getPieceType(7)] == false)
		{

                  blackChoice=findPieceMatch(7);

                 }

		// relative line thickness for highlight
		int lineThick=1;
		if(boardx <boardy)
		{
			lineThick=(int ) squarex / 25;
			if(lineThick < 2)
				lineThick=2;

		}
		else
		{
			lineThick=(int ) squarey / 25;
			if(lineThick < 2)
				lineThick=2;

		}


		int mpa=-1;
		int mpb=-1;

		for(int a = 0; a < 8; a++)
		{
			if(c== 0) // for determining light and dark squares
				c=1;
			else
				c=0;

			for(int b=0; b < 8; b++)
			{
				if((b+1)%2==c)
				g2.setColor(sharedVariables.lightcolor);
				else
				g2.setColor(sharedVariables.darkcolor);
				int aa=a;
				//if(myColor.equals("B"))// we flip board representation now instead
				//aa=7-aa;
				if(getBoardType() == 0)
					g2.fill(new Rectangle2D.Double((double)boardx +  b * squarex, (double) boardy + aa * squarey, (double) squarex, (double)squarey));
				else
				{
					if((b+1)%2==c)
					g.drawImage(graphics.boards[getBoardType()][graphics.light], boardx +  b * squarex,  boardy + aa * squarey,  squarex, squarey, this);
					else
					g.drawImage(graphics.boards[getBoardType()][graphics.dark], boardx +  b * squarex,  boardy + aa * squarey, squarex, squarey, this);

				}

                                if(bugging > -1)
                                {

				if(getBoardType() == 0)
					g2.fill(new Rectangle2D.Double((double)bugboardx +  b * squarex, (double) bugboardy + aa * squarey, (double) squarex, (double)squarey));
				else
				{
					if((b+1)%2==c)
					g.drawImage(graphics.boards[getBoardType()][graphics.light], bugboardx +  b * squarex,  bugboardy + aa * squarey,  squarex, squarey, this);
					else
					g.drawImage(graphics.boards[getBoardType()][graphics.dark], bugboardx +  b * squarex,  bugboardy + aa * squarey, squarex, squarey, this);

				}


                                }// drawing partners board squares if bugging


				int gameslot = 63 - (a * 8 + b);

				if(sharedVariables.highlightMoves == true && startPosition == false)
				if(gameslot == sharedVariables.mygame[gameData.LookingAt].lastfrom || gameslot == sharedVariables.mygame[gameData.LookingAt].lastto)
				{
                                if(sliding == 0)
                                g2.setColor(sharedVariables.highlightcolor);
                                else
                                g2.setColor(sharedVariables.scrollhighlightcolor);
    				// horizontal
				for(int thick = 0; thick<lineThick ; thick++) // we draw multiple times at different offsents to create say 3 thick
				{
					g2.draw(new Line2D.Double((double)boardx +  b * squarex  , (double) boardy + aa * squarey + thick , (double)boardx +  b * squarex + squarex - 1 ,(double) boardy + aa * squarey + (thick ) ));
					g2.draw(new Line2D.Double((double)boardx +  b * squarex, (double) boardy + aa * squarey + squarey - 1 - thick , (double)boardx +  b * squarex + squarex -1 ,(double) boardy + aa * squarey+squarey - 1 - (thick )));
					// vertical
					g2.draw(new Line2D.Double((double)boardx +  b * squarex + thick , (double) boardy + aa * squarey , (double)boardx +  b * squarex + (thick) ,(double) boardy + aa * squarey + squarey -1));
					g2.draw(new Line2D.Double((double)boardx +  b * squarex + squarex -1 - thick, (double) boardy + aa * squarey , (double)boardx +  b * squarex + squarex - 1 - (thick),(double) boardy + aa * squarey + squarey - 1 - (thick )));
				}// end for

			    }// end highlight moves
 				if(sliding == 0)
				{

				if(sharedVariables.highlightMoves == true && bugging > -1)// hightlight bug partners move
				if(gameslot == sharedVariables.mygame[bugging].lastfrom || gameslot == sharedVariables.mygame[bugging].lastto)
				{ g2.setColor(sharedVariables.highlightcolor);


    				// horizontal
				for(int thick = 0; thick<lineThick; thick++) // we draw multiple times at different offsents to create say 3 thick
				{
					g2.draw(new Line2D.Double((double)bugboardx +  b * squarex  , (double) bugboardy + aa * squarey + thick , (double)bugboardx +  b * squarex + squarex - 1 ,(double) bugboardy + aa * squarey + (thick ) ));
					g2.draw(new Line2D.Double((double)bugboardx +  b * squarex, (double) bugboardy + aa * squarey + squarey - 1 - thick , (double)bugboardx +  b * squarex + squarex -1 ,(double) bugboardy + aa * squarey+squarey - 1 - (thick )));
					// vertical
					g2.draw(new Line2D.Double((double)bugboardx +  b * squarex + thick , (double) bugboardy + aa * squarey , (double)bugboardx +  b * squarex + (thick) ,(double) bugboardy + aa * squarey + squarey -1));
					g2.draw(new Line2D.Double((double)bugboardx +  b * squarex + squarex -1 - thick, (double) bugboardy + aa * squarey , (double)bugboardx +  b * squarex + squarex - 1 - (thick),(double) bugboardy + aa * squarey + squarey - 1 - (thick )));
				}// end for

			    }// end highlight moves



				// draw premove highlight
				if(!sharedVariables.mygame[gameData.LookingAt].premove.equals("") && sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_PLAYING)// state 1 i.e. playing
				if(gameslot == sharedVariables.mygame[gameData.LookingAt].premovefrom || gameslot == sharedVariables.mygame[gameData.LookingAt].premoveto)
				{ g2.setColor(sharedVariables.premovehighlightcolor);

				for(int thick = 0; thick<lineThick; thick++) // we draw multiple times at different offsents to create say 3 thick
				{

    				// horizontal
				g2.draw(new Line2D.Double((double)boardx +  b * squarex , (double) boardy + aa * squarey + thick, (double)boardx +  b * squarex + squarex - 1,(double) boardy + aa * squarey + thick ));
				g2.draw(new Line2D.Double((double)boardx +  b * squarex , (double) boardy + aa * squarey + squarey - 1 - thick, (double)boardx +  b * squarex + squarex -1 ,(double) boardy + aa * squarey+squarey - 1 - thick));
					// vertical
				g2.draw(new Line2D.Double((double)boardx +  b * squarex + thick, (double) boardy + aa * squarey, (double)boardx +  b * squarex + thick ,(double) boardy + aa * squarey + squarey -1));
				g2.draw(new Line2D.Double((double)boardx +  b * squarex + squarex -1 - thick, (double) boardy + aa * squarey, (double)boardx +  b * squarex + squarex - 1 - thick,(double) boardy + aa * squarey + squarey - 1));
				}//end for

			    }

				}// end if not sliding


				int piece;
				if(sliding == 0)
				piece=sharedVariables.mygame[gameData.LookingAt].board[gameslot];
				else
				piece=slidingBoard[gameslot];
				try
      			{
		 		 if(piece > 0 && (gameslot != piecemoving || movingpiece == 0))
				//g.drawImage(img[piece-1], boardx +  b * squarex + 2 , boardy + aa * squarey + 2 , squarex - 4, squarey-4, this);
				// above assumes x goes from the squares x + 2 to the squares x -2 or width is x-4 same with y
				// we now use an additional value for decreasing x and y of piece ( one or other) based on if we truncated the x or y width or height to match the opposing parameter ( with or height)
			{
                          if(sharedVariables.mygame[gameData.LookingAt].wild == 30)
                          {
                             drawCheckersPiece(g,  boardx + b * squarex,  boardy + aa * squarey,  piece);

                          }
                          else
                          {
                          if(piece > 6)
                                    drawMyPiece(g, boardx, boardy, difx, dify, b, aa, piece, blackChoice);
                               else
                                    drawMyPiece(g, boardx, boardy, difx, dify, b, aa, piece, choice);
			   }
                          }
                        	else if(piece > 0)
				{
					mpa=a;
					mpb=b;
				}
			}
			catch(Exception e)
			{ }

                         if(bugging > -1)
                         {
				piece=sharedVariables.mygame[bugging].board[gameslot];
				try
      			{
		 		 if(piece > 0 )
				//g.drawImage(img[piece-1], boardx +  b * squarex + 2 , boardy + aa * squarey + 2 , squarex - 4, squarey-4, this);
				// above assumes x goes from the squares x + 2 to the squares x -2 or width is x-4 same with y
				// we now use an additional value for decreasing x and y of piece ( one or other) based on if we truncated the x or y width or height to match the opposing parameter ( with or height)
				if(piece > 6)
                                    drawMyPiece(g, bugboardx, bugboardy, difx, dify, b, aa, piece, blackChoice);
                               else
                                    drawMyPiece(g, bugboardx, bugboardy, difx, dify, b, aa, piece, choice);
                                    }
                                    catch(Exception bugy){}

                         }// end if bugging > -1 i'm playing bughosue and drawing partners pieces

		}
		}

			if(movingpiece == 1 && mpa > -1 && mpb > -1)// mpa and mpb are set to show that we didnt draw that piece3 on the board in its normal spot
			{
			int gameslot = 63 - (mpa * 8 + mpb);
				int piece=sharedVariables.mygame[gameData.LookingAt].board[gameslot];
				if(piece > 0) // piece moving//((int) (squarex -4 ) /2) this centers it
					drawMySlidingPiece(g, squarex, squarey, difx, dify, mx, my, piece, choice);
			}




// draw examine pallete if needed
// if you import this code up to you if you want it drawing an examine mode pallete
if(sharedVariables.mygame[gameData.LookingAt].piecePallette== true &&
                                    (sharedVariables.showPallette == true || sharedVariables.mygame[gameData.LookingAt].state != sharedVariables.STATE_EXAMINING || sharedVariables.mygame[gameData.LookingAt].wild == 24 || sharedVariables.mygame[gameData.LookingAt].wild == 23))
{
	int piece=0;

         drawPiecePallete(piece, examineOriginX, examineOriginY, gameData.LookingAt, g, g2);

         if(bugging > -1)
         drawPiecePallete(piece, bugExamineOriginX, bugExamineOriginY, bugging, g, g2);

			if(movingexaminepiece == 1 )
			{

				 piece=examinepiecemoving;
				if(piece > 0) // piece moving//((int) (squarex -4 ) /2) this centers it
				{
                                  if(piece == xpiece)
                                  g.drawImage(graphics.xpiece, mx - ((int) (squarex -4 ) /2), my - ((int) (squarey -4 ) /2), squarex - 4, squarey-4, this);
                                  else
                                  g.drawImage(graphics.pieces[getPieceType(piece-1)][piece-1], mx - ((int) (squarex -4 ) /2), my - ((int) (squarey -4 ) /2), squarex - 4, squarey-4, this);
                                  }
                        }

}// end if

if(sliding == 0)
paintShapes(g, boardx, boardy, squarex, squarey);
 if(bugging > -1)
 {
	g2.setColor(sharedVariables.boardForegroundColor);
	g2.setFont(sharedVariables.myGameFont);
         TimeDisplayClass timeGetter = new TimeDisplayClass(sharedVariables);
        sharedVariables.mygame[bugging].whiteTimeDisplay = timeGetter.getWhiteTimeDisplay(bugging);
        sharedVariables.mygame[bugging].blackTimeDisplay = timeGetter.getBlackTimeDisplay(bugging);
         timeGetter=null;

	g2.drawString("" + sharedVariables.mygame[bugging].realname1  + " vs. " + sharedVariables.mygame[bugging].realname2 + " " + sharedVariables.mygame[bugging].whiteTimeDisplay + " " + sharedVariables.mygame[bugging].blackTimeDisplay
        , bugboardx + 10, sharedVariables.myGameFont.getSize() + 2);


 }// end if bugging > -1

 if(sharedVariables.drawCoordinates == true)
 drawCoordinates(g, boardx, boardy, squarex, squarey);
}
catch(Exception e)
{}
}// end method

int findPieceMatch(int pieceCol) // would feed it say 1 and 7 for white and black
{
				int idealSize = (squarex -6);
				int choice=0;
				int m=0;
				for(m=0; m< graphics.numberPiecePaths[getPieceType(pieceCol)]; m++)
					if(graphics.multiPiecePaths[getPieceType(pieceCol)][m] <= idealSize)
						choice=m;
					else
					    break;
	if(graphics.multiPiecePaths[getPieceType(pieceCol)][choice] + 30 < idealSize && getPieceType(pieceCol) < 4) {
        choice = -1;// this will force resizing
        //System.out.println("forcing resizing and piece type is " + getPieceType(pieceCol));
    }
    return choice;
}
void drawPiecePallete(int piece, int OriginX, int OriginY, int Looking, Graphics g, Graphics2D g2)
{

	for( int a =0; a<13; a++)
	{

		piece = getExaminePieceNumber(a);
		// alternate colors
		if(a%2==0)
		g2.setColor(sharedVariables.lightcolor);
			else
		g2.setColor(sharedVariables.darkcolor);
		if(getBoardType() == 0)
		g2.fill(new Rectangle2D.Double((double)OriginX , (double)OriginY + a * examineSquareY , (double)examineSquareX  , (double)examineSquareY));
		else
		{
			if(a%2==0)
				g.drawImage(graphics.boards[getBoardType()][graphics.light], OriginX , OriginY + a * examineSquareY , examineSquareX  , examineSquareY, this);
			else
				g.drawImage(graphics.boards[getBoardType()][graphics.dark], OriginX , OriginY + a * examineSquareY , examineSquareX  , examineSquareY, this);
		}
		if(piece > 0)
		{
			if(sharedVariables.mygame[Looking].wild==23 || sharedVariables.mygame[Looking].wild==24)
			{
				if(piece != xpiece) // the examine x we dont draw in crazyhouse
                                if(sharedVariables.mygame[Looking].crazypieces[piece] > 0)
				{
					g.drawImage(graphics.pieces[getPieceType(piece-1)][piece-1], OriginX+2 , OriginY + a * examineSquareY + 2, examineSquareX - 4 , examineSquareY-4, this);
				}
			}
			else
			{
                          if(piece == xpiece)
                          g.drawImage(graphics.xpiece, OriginX+2 , OriginY + a * examineSquareY + 2, examineSquareX - 4 , examineSquareY-4, this);
                          else
                          g.drawImage(graphics.pieces[getPieceType(piece-1)][piece-1], OriginX+2 , OriginY + a * examineSquareY + 2, examineSquareX - 4 , examineSquareY-4, this);
                          }
			if((sharedVariables.mygame[Looking].wild==23 || sharedVariables.mygame[Looking].wild==24) && piece != xpiece)
			{
				if(sharedVariables.mygame[Looking].crazypieces[piece] > 1)
				{
					g2.setColor(new Color(0,0,0));
					g2.setFont(sharedVariables.crazyFont);
					g2.drawString("" + sharedVariables.mygame[Looking].crazypieces[piece], (int)(OriginX), (int)OriginY + a * examineSquareY + 16);

				}
			}
		}
	}// end for

 }// end method

void drawMySlidingPiece(Graphics g, int squarex, int squarey, int difx, int dify, int mx, int my, int piece, int choice)
{
		 if(sharedVariables.mygame[gameData.LookingAt].wild == 30){

                     drawCheckersPiece( g, mx - ((int) (squarex -4 ) /2), my - ((int) (squarey -4 ) /2),  piece);
                   return;
                   }
                	if(choice != -1 && difx == 0 && dify == 0 && graphics.resizable[sharedVariables.pieceType] == false && graphics.multiPieces[sharedVariables.pieceType][choice][piece-1]!=null)
			{
				// piece size goes from squarex + 2 to 2*squarex-4
				// and squarey + 2 to 2*squarey -4;
				// we need to find closest match

				int realSize = graphics.multiPiecePaths[sharedVariables.pieceType][choice];

				int idealSize = (squarex -6);

				 if(realSize<=idealSize)
			g.drawImage(graphics.multiPieces[sharedVariables.pieceType][choice][piece-1], mx - ((int) (squarex -4 ) /2), my - ((int) (squarey -4 ) /2), realSize, realSize, this);
				else
			g.drawImage(graphics.pieces[getPieceType(piece-1)][piece-1], mx - ((int) (squarex -4 ) /2), my - ((int) (squarey -4 ) /2), squarex - 4, squarey-4, this);

			}
			else
							g.drawImage(graphics.pieces[getPieceType(piece-1)][piece-1], mx - ((int) (squarex -4 ) /2), my - ((int) (squarey -4 ) /2), squarex - 4, squarey-4, this);

}
void drawMyPiece(Graphics g, int boardx, int boardy, int difx, int dify, int b, int aa, int piece, int choice)
{
			// squarex squarey is square size
			// difx dify is 0 if squarex = squarey
			// boardx board y is overall board offset
			if(choice != -1 && difx == 0 && dify == 0 && graphics.resizable[getPieceType(piece-1)] == false && graphics.multiPieces[getPieceType(piece-1)][choice][piece-1]!=null)
			{
				// piece size goes from squarex + 2 to 2*squarex-4
				// and squarey + 2 to 2*squarey -4;
				// we need to find closest match

				int realSize = graphics.multiPiecePaths[getPieceType(piece-1)][choice];
				int offset = (int) ((squarex - realSize)/2);
				int idealSize = (squarex -6);

				if(realSize<=idealSize)
			g.drawImage(graphics.multiPieces[getPieceType(piece-1)][choice][piece-1], boardx +  b * squarex + offset , boardy + aa * squarey + offset, realSize, realSize, this);
			else
				g.drawImage(graphics.pieces[getPieceType(piece-1)][piece-1], boardx +  b * squarex + 2 + difx , boardy + aa * squarey + 2 + dify, squarex - 4 - (difx * 2), squarey-4 - (dify * 2), this);

			}
			else
				g.drawImage(graphics.pieces[getPieceType(piece-1)][piece-1], boardx +  b * squarex + 2 + difx , boardy + aa * squarey + 2 + dify, squarex - 4 - (difx * 2), squarey-4 - (dify * 2), this);

}
int getExaminePieceNumber(int a)
{
		int piece=0;
if(sharedVariables.mygame[gameData.LookingAt].iflipped == 0)
{
	    if(a < 6) // blacks pieces
			piece = a + 7;
		else if( a == 6)
		{if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_EXAMINING)
                  piece =xpiece;
                  else
                  piece=0;
		}
                else // whites pieces for white on bottom
		piece = a-6;

}
else
{
	    if(a < 6) // whites pieces
			piece = a+1;
		else if( a == 6)
		{if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_EXAMINING)
                  piece =xpiece;
                  else
                  piece=0;
		}
		else // whites pieces for white on bottom
		piece = a ;

}

		return piece;
}

		int mx;
		int my;
		int oldmx;
		int oldmy;
		int movingpiece;

		int piecemoving;
		int movingexaminepiece;
		int examinepiecemoving;
		int arrowSquare;

void repaintPiece()
{
			int w=Math.abs(oldmx-mx);
			int h=Math.abs(oldmy-my);
			int xx;
			int yy;

			if(oldmx < mx)
			xx=oldmx;
			else
			xx=mx;

			if(oldmy<my)
			yy=oldmy;
			else
			yy=my;

			int wide=getWidth();
			int high=getWidth();
			repaint(xx-(int)wide/8, yy-(int)high/8, w + 2 * (int)wide/8, h + 2 * (int)high/8);

}

	    void eventOutput(String eventDescription, MouseEvent e) {
	      oldmx=mx;
	      oldmy=my;

	      mx=e.getX();
	      my=e.getY();


		if(movingpiece == 1 || movingexaminepiece ==1)
		{
		if(isVisible() == true)
		{
			repaintPiece();
		}

		}




	    }


	    public void mouseMoved(MouseEvent e) {
	        eventOutput("Mouse moved", e);
	    }

	    public void mouseDragged(MouseEvent e) {
	        eventOutput("Mouse dragged", e);
	    }
	    public void mousePressed(MouseEvent e) {


		if(sharedVariables.moveSliders[gameData.BoardIndex].getValue() < sharedVariables.moveSliders[gameData.BoardIndex].getMaximum())
		return;
		
		if(inTheAir == true && sharedVariables.moveInputType == channels.CLICK_CLICK)
		{
                   inTheAir = false;
                                       if(e.getButton() == MouseEvent.BUTTON3)
                                       {
						sharedVariables.mygame[gameData.LookingAt].premove="";
						movingpiece = 0;
                                                movingexaminepiece=0;
                                                repaint();
						return;
					} 
                   mouseReleasedMoveMade (e);
                   return;
                }
		if(movingpiece == 0 && movingexaminepiece==0)
				{
                                      // first check for right click to ignore as move and cancel a premove
                                       if(e.getButton() == MouseEvent.BUTTON3)
                                       {
						sharedVariables.mygame[gameData.LookingAt].premove="";
						repaint();
						if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_EXAMINING)
						{
                                                   arrowSquare = getArrowSquare();
                                                }
						return;
					}



					int piece = getPiece();
					if(piece > -1)
					{
						if(sharedVariables.mygame[gameData.LookingAt].board[piece] > 0)
						{
						movingpiece=1;
						piecemoving=piece;
						inTheAir = true;
						}
					}
					else if(piece != -100)
					{

						movingexaminepiece=1;
						examinepiecemoving=-piece;
						inTheAir= true;

					}

				}
                                else if(e.getButton() == MouseEvent.BUTTON3)
                                       {
						sharedVariables.mygame[gameData.LookingAt].premove="";
						//movingpiece = 0;
                                                //movingexaminepiece=0;
                                                repaint();
						return;
					}


	    }
	 public void mouseEntered (MouseEvent me) {}






	 public void mouseReleased (MouseEvent me) {

          if(sharedVariables.moveInputType == channels.CLICK_CLICK && inTheAir == true)
          return;
           if(me.getButton() == MouseEvent.BUTTON3)
           {
             if(arrowSquare > -1 && sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_EXAMINING)
             {
               mouseReleasedCircleArrowDrawn(me);
             }
           arrowSquare = -1;
           return;
           }
          mouseReleasedMoveMade (me);

  }

       public void mouseReleasedCircleArrowDrawn(MouseEvent me) {

            try {
              int arrowToSquare = getArrowSquare();
              if(arrowToSquare < 0)
                return;

              String arrowFrom = getSubMove(arrowSquare);
              String arrowTo = getSubMove(arrowToSquare);
              String primary  = "primary " + sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber + "\n";
              if(sharedVariables.isGuest()) {
               primary = "";
              }
              String prefixcommand="";
             // code here to prefix our command with `g#`command if on icc and we have a name defined i.e. recieved whoami
            // this will get any text back from server to go to right console
            if(sharedVariables.myServer.equals("ICC") && sharedVariables.myname.length() > 0)
               prefixcommand="`g" + gameData.LookingAt + "`";

            String themove = primary  + prefixcommand;
            if(arrowFrom.equals(arrowTo))// circle
            themove += "Circle " + arrowTo + "\n";
            else
            themove += "Arrow " + arrowFrom + " " + arrowTo + "\n";
            myoutput amove = new myoutput();
            amove.game=1;
            amove.consoleNumber=0;
            amove.data=themove;
             queue.add(amove);

            }
            catch(Exception badArrow){}

       }

	 public void mouseReleasedMoveMade (MouseEvent me) {

     boolean iLocked=false;
     int ipremoving;
	 int capture=0;

		 if(movingpiece == 1)
		 {
			 int piece=getPiece();
                         int wildtype = sharedVariables.mygame[gameData.LookingAt].wild;

		if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_PLAYING)// if playing
			{

			int pieceToMove=sharedVariables.mygame[gameData.LookingAt].board[piecemoving];

			 if((wildtype != 16 && wildtype != 23 && wildtype != 24 && wildtype != 28 && wildtype != 30) && sharedVariables.checkLegality == true && (sharedVariables.mygame[gameData.LookingAt].myturn() || sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_EXAMINING))
			 {
			 if(pieceToMove==1 || pieceToMove==2 || pieceToMove==3 || pieceToMove==4 || pieceToMove==5 || pieceToMove==7 || pieceToMove==8 || pieceToMove==9 || pieceToMove==10 || pieceToMove==11) // a biship rook or queen
			 {
				 boolean legalmove=checkLegality(piecemoving, piece);
				 if(legalmove==false)
				 {movingpiece=0;
				 repaint();

				Sound movesound;
				if(sharedVariables.makeSounds == true && sharedVariables.makeMoveSounds && piecemoving != piece)
				if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_PLAYING || sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_EXAMINING)
				{

						movesound=new Sound(sharedVariables.songs[6]);

				}


				return;
				}// if legal move = false


			 }// if a piece we check
			}// if wild we check
		 }


			 if(piece > -1 && sharedVariables.mygame[gameData.LookingAt].movelock==0)
			 {
						 int prom=0;// shouldnt this below be movingpiece
						 if((sharedVariables.mygame[gameData.LookingAt].board[piecemoving] == 1  || sharedVariables.mygame[gameData.LookingAt].board[piecemoving] == 7) && piece < 8) //  promotion
						 prom=1;
						 if((sharedVariables.mygame[gameData.LookingAt].board[piecemoving] == 1  || sharedVariables.mygame[gameData.LookingAt].board[piecemoving] == 7) && piece > 55) //  promotion
						 prom=1;

						 if(piece != piecemoving && sharedVariables.mygame[gameData.LookingAt].state != sharedVariables.STATE_OBSERVING)
						 {
							 int type=-1;
							// need readlock
								ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
								Lock readLock = rwl.readLock();
								if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_PLAYING)
								{
									readLock.lock();
									iLocked=true;
								}

							 // determine if premoving
							 // state 1 is playing.  on move 1 movetop is 0 i.e. as you go to make move 1
							 ipremoving=0;
							if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_PLAYING && !sharedVariables.mygame[gameData.LookingAt].myturn() )
							 {
								 ipremoving=1;
							 sharedVariables.mygame[gameData.LookingAt].premovefrom=piecemoving;
							 sharedVariables.mygame[gameData.LookingAt].premoveto=piece;
							 movingpiece=0;
							 repaint();
							 }

							 if(ipremoving == 0)
							 {

						if(sharedVariables.mygame[gameData.LookingAt].wild == 27 && sharedVariables.mygame[gameData.LookingAt].board[piece]!=0)
								sharedVariables.mygame[gameData.LookingAt].makeAtomicCaptures(sharedVariables.mygame[gameData.LookingAt].board, piecemoving, piece);

							 if(sharedVariables.mygame[gameData.LookingAt].board[piece]>0)
							 	capture=1;
							 sharedVariables.mygame[gameData.LookingAt].board[piece]=sharedVariables.mygame[gameData.LookingAt].board[piecemoving];
							 sharedVariables.mygame[gameData.LookingAt].board[piecemoving]=0;
							 sharedVariables.mygame[gameData.LookingAt].lastto=piece;
							 sharedVariables.mygame[gameData.LookingAt].lastfrom=piecemoving;
							 sharedVariables.mygame[gameData.LookingAt].currentLastto=piece;
							 sharedVariables.mygame[gameData.LookingAt].currentLastfrom=piecemoving;
							 if( wildtype == 30)
							    deleteCheckersJumpPieceAsNeeded(piece, piecemoving);
                                                         movingpiece=0;
							 repaint();



							}// not premoving, we made move on board if went through above code and any sound

						 // we need to make the board we are moving on primary first in case of a simul
						 /************************** primary code ***********************************/
						 String primary  = "primary " + sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber + "\n";
                                                 if(sharedVariables.isGuest()) {
                                                  primary = "";
                                                  }
						String prefixcommand="";
						 // code here to prefix our command with `g#`command if on icc and we have a name defined i.e. recieved whoami
						 // this will get any text back from server to go to right console
						 if(sharedVariables.myServer.equals("ICC") && sharedVariables.myname.length() > 0)
						 	prefixcommand="`g" + gameData.LookingAt + "`";

						 String themove = primary  + prefixcommand + "multi chessmove " +  getMove(piecemoving, piece);
                                                 myoutput amove = new myoutput();
						 amove.game=1;
						 amove.consoleNumber=0;
						 amove.data=themove;

						 // we set these next two in case we are in a simul, chessbot class will switch our board
						 if(ipremoving == 0)
						 {
							 amove.gameboard=gameData.BoardIndex;
							amove.gamelooking=gameData.LookingAt;
						 }


						/* if(prom == 1)
						 {
							if(sharedVariables.mygame[gameData.LookingAt].wild == 17)
							amove.data= amove.data + "=R";// this will autoqueen we need a choice
							else if(sharedVariables.mygame[gameData.LookingAt].wild == 26)
							amove.data= amove.data + "=K";// this will autoqueen we need a choice
							else
							amove.data= amove.data + "=Q";// this will autoqueen we need a choice


							if(sharedVariables.mygame[gameData.LookingAt].board[piece] == 7)
							{if(sharedVariables.mygame[gameData.LookingAt].wild == 17)
								sharedVariables.mygame[gameData.LookingAt].board[piece]=10;
							else if(sharedVariables.mygame[gameData.LookingAt].wild == 26)
								sharedVariables.mygame[gameData.LookingAt].board[piece]=12;
							else
								sharedVariables.mygame[gameData.LookingAt].board[piece]=11;

							}
							if(sharedVariables.mygame[gameData.LookingAt].board[piece]==1)
							{
								if(sharedVariables.mygame[gameData.LookingAt].wild == 17)
								sharedVariables.mygame[gameData.LookingAt].board[piece]=4;

								else if(sharedVariables.mygame[gameData.LookingAt].wild == 26)
								sharedVariables.mygame[gameData.LookingAt].board[piece]=6;
								else
								sharedVariables.mygame[gameData.LookingAt].board[piece]=5;
							}

						}// end if prom 1
						*/
                                                 amove.data=amove.data + "\n";

						 if( sharedVariables.mygame[gameData.LookingAt].state != sharedVariables.STATE_OVER)
						 {
							if(ipremoving == 0)
							{

							if(wildtype != 30)
                                                        sharedVariables.mygame[gameData.LookingAt].madeMove=1;
							if(prom == 1 && sharedVariables.autoPromote == false && wildtype != 30)
							{amove.promotion = true;
                                                         amove.wildNumber = sharedVariables.mygame[gameData.LookingAt].wild;
                                                         if(sharedVariables.mygame[gameData.LookingAt].board[piece] > 6)
                                                         amove.iAmWhite = false;

                                                         }

                                                        queue.add(amove);

							}
							// by addint to queue another class can be polling to see if they moved and what move they made.
							//this is how I communicate the move to telnet so it can send it
							// interestingly i dont add the move to the gamestate move list. i wait for chessclub to send me back the move i made.
							// if its illegal it was never added, and i reload the baord from start position plus all moves that were made on move list
							else
							sharedVariables.mygame[gameData.LookingAt].premove=amove.data;// when we get a move the get move from icc can send a premove (its now our turn)
						 }
						 if(iLocked==true)
						 	readLock.unlock();

							 // sound
							if(ipremoving==0)
							{
								Sound movesound;
							 if(sharedVariables.makeSounds == true && sharedVariables.makeMoveSounds)
								 if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_PLAYING || sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_EXAMINING)
								 {
									 if(capture ==0)
                                            if(sharedVariables.softerMoveSounds) {
                                                movesound=new Sound(sharedVariables.songs[10]);
                                            } else {
                                                movesound=new Sound(sharedVariables.songs[1]);
                                            }
                                     else
                                            if(sharedVariables.softerMoveSounds) {
                                                movesound=new Sound(sharedVariables.songs[11]);
                                            } else {
                                                movesound=new Sound(sharedVariables.songs[2]);
                                            }
								 }

							 }

				}// end if piece not = same square
					 else
			 		{
						sharedVariables.mygame[gameData.LookingAt].premove="";
						movingpiece=0;
						repaint();
					}
			 }// end on board
			 else
				 {movingpiece=0;
				 repaint();// off board move cant drop so it goes back to original postion
				 }

		 }// end if moviing piece > -1

		 // this section below was added on when i added an examine mode pallete piece that can be moved with mouse.
		 if(sharedVariables.mygame[gameData.LookingAt].piecePallette == true)
		 {
			 if(movingexaminepiece == 1)
		     {
				 int piece=getPiece();
				 if(piece > -1 && sharedVariables.mygame[gameData.LookingAt].movelock==0)
				 {

							 ipremoving=0;
							if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_PLAYING && !sharedVariables.mygame[gameData.LookingAt].myturn())
							 {
								 ipremoving=1;
							 sharedVariables.mygame[gameData.LookingAt].premovefrom=-examinepiecemoving;
							 sharedVariables.mygame[gameData.LookingAt].premoveto=piece;
							 movingexaminepiece=0;
							 repaint();
							 }


                                          if(ipremoving == 0)
                                          {
                                	 // sound
					 Sound movesound;
					 if(sharedVariables.makeSounds == true && sharedVariables.makeMoveSounds)
					 if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_PLAYING || sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_EXAMINING)
								movesound=new Sound(sharedVariables.songs[1]);


					 if(examinepiecemoving == xpiece)
                                         sharedVariables.mygame[gameData.LookingAt].board[piece]=0;
                                         else
                                         sharedVariables.mygame[gameData.LookingAt].board[piece]=examinepiecemoving;
					 //String atPiece=getExamPieceMoving(exampiecemoving);


                                          }// if not premoving
					 String primary  = "primary " + sharedVariables.mygame[sharedVariables.gamelooking[gameData.BoardIndex]].myGameNumber + "\n";
					 if(sharedVariables.isGuest()) {
                                         primary = "";
                                         }
                                         String chessMove = "chessmove ";
                                         if(sharedVariables.mygame[gameData.LookingAt].wild != 23) {
                                          chessMove = "";
                                         }
					 String themove = primary  + "multi " + chessMove + getMove(-examinepiecemoving, piece);
					 myoutput amove = new myoutput();
					 amove.game=1;
					 amove.consoleNumber=0;
					 amove.data=themove + "\n";
					
                                        if(ipremoving == 0)
                                         queue.add(amove);
					else
					sharedVariables.mygame[gameData.LookingAt].premove=amove.data;// when we get a move the get move from icc can send a premove (its now our turn)


				movingexaminepiece =0;
				 }// end if we found the board i.e. piece > -1
			 else
			 {movingexaminepiece =0;// they dropped off the board. no longer a piece moving
			 sharedVariables.mygame[gameData.LookingAt].premove="";
                          }
                         repaint();

	 	 }// end if movingexaminepiece==1
		 }// end if examine pallete true



}// end method

       void deleteCheckersJumpPieceAsNeeded(int to, int from)
       {
            if(from - to == 14)
               sharedVariables.mygame[gameData.LookingAt].board[from - 7]=0;
            if(from - to == 18)
               sharedVariables.mygame[gameData.LookingAt].board[from - 9]=0;
            if(from - to == -14)
               sharedVariables.mygame[gameData.LookingAt].board[from + 7]=0;
            if(from - to == -18)
               sharedVariables.mygame[gameData.LookingAt].board[from + 9]=0;
       }

	String getMove(int from, int to)
	{
		String temp1="a1";
		String temp2="a1";
		 if(from>=0)
		 temp1=getSubMove(from);
		 else
		 temp1=getSubPlop(-from);// we pass a negative material scored piece in if its comming from examine drop but we now makeit positive for next method
		 temp2=getSubMove(to);


	 	return temp1 + temp2;

	}


	String getSubPlop(int piece)
	{

		if(piece == 1)
			return "P@";
		else if(piece == 2)
			return "N@";
		else if(piece == 3)
			return "B@";
		else if(piece == 4)
			return "R@";
		else if(piece == 5)
			return "Q@";
		else if(piece == 6)
			return "K@";

		else if(piece == 7)
			return "p@";
		else if(piece == 8)
			return "n@";
		else if(piece == 9)
			return "b@";
		else if(piece == 10)
			return "r@";
		else if(piece == 11)
			return "q@";
		else if(piece == 12)
			return "k@";
		else if(piece == xpiece)
		       return "x@";
	return "";

	}
	String getSubMove(int square)
	{

		int col=0;
		int row=0;
		String value="";
		square = 63 - square;
		square++; // 1-64
		int square2=65-square;

		row=square%8;
		if(sharedVariables.mygame[gameData.LookingAt].iflipped == 1)
		row=square2%8;

		if(row == 1)
		value = value + "a";
		else if(row == 2)
		value = value + "b";
		else if(row == 3)
		value = value + "c";
		else if(row == 4)
		value = value + "d";
		else if(row == 5)
		value = value + "e";
		else if(row == 6)
		value = value + "f";
		else if(row == 7)
		value = value + "g";
		else if(row == 0)
		value = value + "h";

		col=(int) square/8;
		if(square %8 != 0)
		col++;

		if(sharedVariables.mygame[gameData.LookingAt].iflipped == 0)// white on bottom doesnt work. black on bottom does
		col=9-col;

		value = value + col;





		return value;
	}
	 public void mouseExited (MouseEvent me) {}
	public void mouseClicked (MouseEvent me) {



		}


int amPlayingBug()
{
  
 if(sharedVariables.mygame[gameData.LookingAt].wild != 24)
 return -1;
 if(sharedVariables.mygame[gameData.LookingAt].state != sharedVariables.STATE_PLAYING)
 return -1;
 
 for(int a=0; a<sharedVariables.maxGameTabs; a++)
 if(sharedVariables.mygame[a]== null)
 return -1;
 else if((sharedVariables.mygame[a].realname1.equals(sharedVariables.myPartner) ||  sharedVariables.mygame[a].realname2.equals(sharedVariables.myPartner)) && sharedVariables.mygame[a].state == sharedVariables.STATE_OBSERVING)
 return a;
 
 return -1;
}

boolean checkLegality(int from, int to)
{

try {
        moveGenerator generator = new moveGenerator();
	int piece=sharedVariables.mygame[gameData.LookingAt].board[from];
	int top=0;
	int [] fromList = new int[256];
	int [] toList = new int[256];


	int color=0;
	if(sharedVariables.mygame[gameData.LookingAt].board[from]<7)
		color=1;


   if(piece == 1 || piece == 2 || piece == 3 || piece == 4 || piece == 5 || piece == 7 || piece == 8 || piece == 9 || piece == 10 || piece == 11) // bishop rooke or queen
 {
	  if(piece == 1 || piece == 7)
	   	top=generator.generatePawnMoves(fromList, toList, sharedVariables.mygame[gameData.LookingAt].board, top, color, piece, sharedVariables.mygame[gameData.LookingAt].iflipped);// the 0 is top , this is first call
	  else if(piece == 3 || piece == 9)
	   	top=generator.generateBishopMoves(fromList, toList, sharedVariables.mygame[gameData.LookingAt].board, top, color, piece );// the 0 is top , this is first call
	  else if(piece == 4 || piece == 10)
	   	top=generator.generateRookMoves(fromList, toList, sharedVariables.mygame[gameData.LookingAt].board, top, color, piece );// the 0 is top , this is first call
	  else if(piece == 2 || piece == 8)
	   	top=generator.generateKnightMoves(fromList, toList, sharedVariables.mygame[gameData.LookingAt].board, top, color, piece );// the 0 is top , this is first call
	  else
	  {
	   	top=generator.generateBishopMoves(fromList, toList, sharedVariables.mygame[gameData.LookingAt].board, top, color, piece );// the 0 is top , this is first call
	   	top=generator.generateRookMoves(fromList, toList, sharedVariables.mygame[gameData.LookingAt].board, top, color, piece );// the 0 is top , this is first call

	  }
		int count=0;

		for(int z=0; z<top; z++)
				if(toList[z]==to && fromList[z]== from)
					count++;

	if(count == 0)
		return false;


	}

	return true;
}// end try
catch(Exception dui){ }
return true;
}

int getPieceType(int piece)
{
if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_OBSERVING && sharedVariables.randomArmy == true)
{
   if(piece < 6)
   return sharedVariables.mygame[gameData.LookingAt].randomObj.whitePieceNum;
   else
   return sharedVariables.mygame[gameData.LookingAt].randomObj.blackPieceNum;

}



 return sharedVariables.pieceType;
}


int getBoardType()
{
 
 if(sharedVariables.mygame[gameData.LookingAt].state == sharedVariables.STATE_OBSERVING && sharedVariables.randomBoardTiles == true)
 return sharedVariables.mygame[gameData.LookingAt].randomObj.boardNum;
 
 return sharedVariables.boardType;
}
}
