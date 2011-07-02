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

/*
requires:
move string in algabraic
current board
iflipped
returns:
move string as pgn
*/


class pgnWriter
{

void makeFrame(String text)
{
		JFrame myframe = new JFrame();
		myframe.setTitle("" + text);
		myframe.setSize(100,100);
		myframe.setVisible(true);

}
String getPgn(String moves, int iflipped, int [] board2)
{

	int [] board = new int[64];
	for(int z=0; z<64; z++)
	board[z]=board2[z];
	String newMoves="";
	moveToCordinates cordinator = new moveToCordinates();
	moveGenerator generator = new moveGenerator();


	// tokenize move string into array
	// loop
	//       call for cordinates, derive From To
	//       for now write pgn as piece from to. i.e. Nb1c3, move generators need to check uniqueness for shorter moves.  exception pawn and king moves can be simplified
	//       make move on board so board updates

	String [] theMoveArray = moves.split(" ");

	int i=0;


	for(i=0; i<theMoveArray.length; i++)
	{


	try{

		boolean enpassant = false;
		int castle = 0;  // 1 = king side 2 = queen side
		String promotion = "";
		String tryMove =theMoveArray[i];
		if(tryMove.length()>4)
			tryMove=tryMove.substring(0,4);

		int xfrom=cordinator.getxmove(tryMove, 0, iflipped, 0); // move toggle iflipped turn with turn ok to always be 0 if not crazyhouse
		int yfrom=cordinator.getymove(tryMove, 0);
		if(iflipped == 1)
			yfrom=7-yfrom;

		int from= yfrom * 8 + xfrom;

		int xto=cordinator.getxmove(tryMove, 2, iflipped, 0); // move toggle iflipped turn with turn ok to always be 0 if not crazyhouse
		int yto=cordinator.getymove(tryMove, 2);
		if(iflipped == 1)
			yto=7-yto;

		int to= yto * 8 + xto;
		String fromString=tryMove.substring(0,2);
		String toString=tryMove.substring(2,4);



		if(board[from] == 1 || board[from]== 7)// a pawn
		{

			// handle enpassant
			if(fromString.charAt(0) == toString.charAt(0)) // e2 e4 e=e
				newMoves= newMoves + toString + " ";
			else if(board[to]==0) // e5d6
			{
				enpassant=true;
				newMoves= newMoves + fromString.charAt(0) + "x" + toString + " ";
			}
			else
			{
				newMoves= newMoves + fromString.charAt(0) + "x" + toString + " ";

			}

			// promotion
			if(theMoveArray[i].length() == 5 && (toString.charAt(1)== '1' || toString.charAt(1) == '8'))
			{
				newMoves = newMoves.substring(0, newMoves.length()-1);
				newMoves = newMoves + theMoveArray[i].substring(4, 5) + " ";
				promotion=theMoveArray[i].substring(4, 5);
			}

		}
		else if(board[from]==6 || board[from]==12)// a king
		{

			// handle castling
			if(from - to == 2 || to - from == 2)// castle
			{
				if(xto == 2 || xto == 5)// queen side
				{
					newMoves= newMoves + "O-O-O" + " ";
					castle=2;
				}
				else
				{
					newMoves= newMoves + "O-O" + " ";
					castle=1;
				}
			}
			else
			{
				newMoves= newMoves + "K" + toString + " ";
			}


		}
		else // a knight bishop or queen
		{

		//	if(board[from] == 2 || board[from]==8)
		//			newMoves= newMoves + "N" + fromString + toString + " ";


/***************** A knight ***********************************************/
			if(board[from] == 2 || board[from]==8)
			{

				int [] fromList = new int[150];
				int [] toList = new int[150];
				int top=0;
				boolean unique = false;
				int count=0; // we count moves to to square, if == 1 unique becomes true
				int color=0;

				if(board[from] == 2)
					color=1;

				top=generator.generateKnightMoves(fromList, toList, board, top, color, board[from]);// the 0 is top , this is first call

				for(int z=0; z<top; z++)
					if(toList[z]==to)
						count++;
				if(count == 1)
					unique= true;

				if(unique == true)
				{
					if(board[to] > 0)
					newMoves= newMoves + "Nx" + toString + " ";
					else
						newMoves= newMoves + "N" + toString + " ";

				}
				else
					newMoves= newMoves + "N" + fromString + toString + " ";
			}// end if Knight
/***************** End bishop ***********************************************/




/***************** A bishop ***********************************************/
			if(board[from] == 3 || board[from]==9)
			{

				int [] fromList = new int[150];
				int [] toList = new int[150];
				int top=0;
				boolean unique = false;
				int count=0; // we count moves to to square, if == 1 unique becomes true
				int color=0;

				if(board[from] == 3)
					color=1;

				top=generator.generateBishopMoves(fromList, toList, board, top, color, board[from]);// the 0 is top , this is first call

				for(int z=0; z<top; z++)
					if(toList[z]==to)
						count++;
				if(count == 1)
					unique= true;

				if(unique == true)
				{
					if(board[to] > 0)
					newMoves= newMoves + "Bx" + toString + " ";
					else
						newMoves= newMoves + "B" + toString + " ";

				}
				else
					newMoves= newMoves + "B" + fromString + toString + " ";
			}// end if bishop
/***************** End bishop ***********************************************/



/***************** A rook ***********************************************/
			if(board[from] == 4 || board[from]==10)
			{

				int [] fromList = new int[150];
				int [] toList = new int[150];
				int top=0;
				boolean unique = false;
				int count=0; // we count moves to to square, if == 1 unique becomes true
				int color=0;

				if(board[from] == 4)
					color=1;

				top=generator.generateRookMoves(fromList, toList, board, top, color, board[from]);// the 0 is top , this is first call

				for(int z=0; z<top; z++)
					if(toList[z]==to)
						count++;
				if(count == 1)
					unique= true;

				if(unique == true)
				{
					if(board[to] > 0)
					newMoves= newMoves + "Rx" + toString + " ";
					else
						newMoves= newMoves + "R" + toString + " ";

				}
				else
					newMoves= newMoves + "R" + fromString + toString + " ";
			}// end if rook
/***************** End rook ***********************************************/



/***************** A Queen ***********************************************/
			if(board[from] == 5 || board[from]==11)
			{

				int [] fromList = new int[400];
				int [] toList = new int[400];
				int top=0;
				boolean unique = false;
				int count=0; // we count moves to to square, if == 1 unique becomes true
				int color=0;

				if(board[from] == 5)
					color=1;

				top=generator.generateRookMoves(fromList, toList, board, top, color, board[from]);// the 0 is top , this is first call
				top=generator.generateBishopMoves(fromList, toList, board, top, color, board[from]);// the 0 is top , this is first call

				for(int z=0; z<top; z++)
					if(toList[z]==to)
						count++;


				if(count == 1)
					unique= true;

				if(unique == true)
				{
					if(board[to] > 0)
					newMoves= newMoves + "Qx" + toString + " ";
					else
						newMoves= newMoves + "Q" + toString + " ";

				}
				else
					newMoves= newMoves + "Q" + fromString + toString + " ";
			}// end if queen
/***************** End queen ***********************************************/



		}

		// make move
		if(enpassant == true)
		{
			board[to]=board[from];
			board[from]=0;
			if(to > 32)
				board[to-8]=0;
			else
				board[to+8]=0;

		}
		else if(castle > 0)
		{
			board[to]=board[from];
			board[from]=0;
			if(xto > 4)
				board[yto * 8 + 7]=0;
			else
				board[yto * 8]=0;
		}
		else if(!promotion.equals("") && (board[from] == 1 || board[from]==7))
		{
			int toggle=0;
			if(board[from]==7)
				toggle=6;

			promotion = promotion.toLowerCase();
			if(promotion.equals("q"))
				board[to]=5 + toggle;
			if(promotion.equals("r"))
				board[to]=4 + toggle;
			if(promotion.equals("b"))
				board[to]=3 + toggle;
			if(promotion.equals("n"))
				board[to]=2 + toggle;
			if(promotion.equals("k"))
				board[to]=6 + toggle;



			board[from]=0;
		}
		else
		{
			board[to]=board[from];
			board[from]=0;
		}

		}// end try
		catch(Exception e){ return moves;}


	}// end for


	if(newMoves.equals(""))
		return moves;

	return newMoves;



}






}