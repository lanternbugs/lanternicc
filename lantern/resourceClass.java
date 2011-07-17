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
/* this is for storing the graphics.  i.e. boards pieces*/

import java.awt.*;
import java.awt.Window.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JDialog;
import java.awt.image.*;



class resourceClass {
Image [][] boards;
Image [][] pieces;
Image [][][] multiPieces;

 int light;
 int dark;
 int maxBoards;
 int maxPieces;
 int maxPiecePaths;

 String [] boardPaths;
 String [] piecePaths;
int [][] multiPiecePaths;
boolean [] resizable;
int [] numberPiecePaths;
 String [] pieceExt;
resourceClass()
{


maxBoards=11;
maxPieces=23;
maxPiecePaths=20;

 //squares
 light=0;
 dark=1;

boardPaths = new String[maxBoards];
boardPaths[1]= "pale-wood";
boardPaths[2]= "wooden-light";
boardPaths[3]= "wooden-dark";
boardPaths[4]= "dark-marble";
boardPaths[5]= "red-marble";
boardPaths[6]= "crampled-paper";
boardPaths[7]= "winter";
boardPaths[8]= "olive";
boardPaths[9]= "cherry";
boardPaths[10]= "purple";

int num=0;
//for(num=0; num < 8; num++)
//boardPaths[num]="/" + boardPaths[num];
boards = new Image[maxBoards][2];

piecePaths = new String[maxPieces];
piecePaths[0]="dyche1";
piecePaths[1]="dyche2";
piecePaths[2]="dyche3";
piecePaths[3]="bookup";
piecePaths[4]="xboard";
piecePaths[5]="alpha";
piecePaths[6]="spatial";
piecePaths[7]="harlequin";
piecePaths[8]="berlin";
piecePaths[9]="eboard-classic";
piecePaths[10]="moltengood";
piecePaths[11]="moltenevil";
piecePaths[12]="liebeskind";
piecePaths[13]="eyes";
piecePaths[14]="fantasy";

piecePaths[15]="line";
piecePaths[16]="motif";
 piecePaths[17]="utrecht";

 piecePaths[18]="adventure";
piecePaths[19]="maya";
 piecePaths[20]="medieval";



piecePaths[21]="mongemix";


piecePaths[22]="random";
//for(num=0; num < 16; num++)
//piecePaths[num]="/" + piecePaths[num];

pieceExt = new String[maxPieces];
pieceExt[0]="gif";
pieceExt[1]="gif";
pieceExt[2]="gif";
pieceExt[3]="gif";
pieceExt[4]="gif";
pieceExt[5]="png";
pieceExt[6]="png";
pieceExt[7]="png";
pieceExt[8]="png";
pieceExt[9]="png";
pieceExt[10]="png";
pieceExt[11]="png";
pieceExt[12]="png";
pieceExt[13]="png";
pieceExt[14]="png";
pieceExt[15]="png";
pieceExt[16]="png";
pieceExt[17]="png";
pieceExt[18]="png";
pieceExt[19]="png";
pieceExt[20]="png";

pieceExt[21]="mix";
pieceExt[22]="ran";

resizable=new boolean[maxPieces];
resizable[0]=false;
resizable[1]=false;
resizable[2]=false;
resizable[3]=false;
resizable[4]=false;
resizable[5]=false;
resizable[6]=false;
resizable[7]=false;
resizable[8]=false;
resizable[9]=true;
resizable[10]=true;
resizable[11]=true;
resizable[12]=false;
resizable[13]=false;
resizable[14]=false;
resizable[15]=false;
resizable[16]=false;
resizable[17]=false;
resizable[18]=false;
resizable[19]=false;
resizable[20]=false;
resizable[21]=false;

resizable[22]=true;


pieces = new Image[maxPieces][12];
multiPieces = new Image[maxPieces][maxPiecePaths][12];

numberPiecePaths = new int[maxPieces];
for(int b=0; b<maxPieces; b++)
	if(b==4 || b==5 || b==8)
		numberPiecePaths[b]=17;
	else if(b == 0 || b == 1 || b == 2 || b == 3)
		numberPiecePaths[b]=8;
	else if(b==12)
		numberPiecePaths[b]=6;
	else if(b==6 || b==7 || b == 13 || b== 14 || b==15 || b == 16 || b == 17 || b == 18 || b == 19 || b == 20 || b == 21)
		numberPiecePaths[b]=15;

	else
		numberPiecePaths[b]=0;
multiPiecePaths=new int[maxPieces][maxPiecePaths];

for(int b=0; b<maxPieces; b++)
{
if(b==4)
{
	multiPiecePaths[b][0]=21;
	multiPiecePaths[b][1]=25;
	multiPiecePaths[b][2]=29;
	multiPiecePaths[b][3]=33;
	multiPiecePaths[b][4]=37;
	multiPiecePaths[b][5]=40;
	multiPiecePaths[b][6]=45;
	multiPiecePaths[b][7]=49;
	multiPiecePaths[b][8]=54;
	multiPiecePaths[b][9]=58;
	multiPiecePaths[b][10]=64;
	multiPiecePaths[b][11]=72;
	multiPiecePaths[b][12]=87;
	multiPiecePaths[b][13]=95;
	multiPiecePaths[b][14]=108;
	multiPiecePaths[b][15]=116;
	multiPiecePaths[b][16]=129;


}
if(b==5 || b==8)// alpha
{
	multiPiecePaths[b][0]=20;
	multiPiecePaths[b][1]=24;
	multiPiecePaths[b][2]=28;
	multiPiecePaths[b][3]=32;
	multiPiecePaths[b][4]=36;
	multiPiecePaths[b][5]=42;
	multiPiecePaths[b][6]=48;
	multiPiecePaths[b][7]=54;
	multiPiecePaths[b][8]=60;
	multiPiecePaths[b][9]=68;
	multiPiecePaths[b][10]=76;
	multiPiecePaths[b][11]=84;
	multiPiecePaths[b][12]=92;
	multiPiecePaths[b][13]=102;
	multiPiecePaths[b][14]=112;
	multiPiecePaths[b][15]=122;
	multiPiecePaths[b][16]=132;


}

if(b==6 || b==7 || b==13 || b==14 || b == 15 || b == 16 || b == 17 || b == 18 || b == 19 || b == 20 || b == 21)// monge harlequin
{
	multiPiecePaths[b][0]=20;
	multiPiecePaths[b][1]=24;
	multiPiecePaths[b][2]=28;
	multiPiecePaths[b][3]=32;
	multiPiecePaths[b][4]=36;
	multiPiecePaths[b][5]=42;
	multiPiecePaths[b][6]=48;
	multiPiecePaths[b][7]=54;
	multiPiecePaths[b][8]=60;
	multiPiecePaths[b][9]=68;
	multiPiecePaths[b][10]=76;
	multiPiecePaths[b][11]=84;
	multiPiecePaths[b][12]=92;
	multiPiecePaths[b][13]=102;
	multiPiecePaths[b][14]=112;
	// we didnt load two largest sizes to conserve disk space


}

if(b == 0 || b == 1 || b == 2 || b == 3)
{
	multiPiecePaths[b][0]=24;
	multiPiecePaths[b][1]=28;
	multiPiecePaths[b][2]=32;
	multiPiecePaths[b][3]=38;
	multiPiecePaths[b][4]=46;
	multiPiecePaths[b][5]=54;
	multiPiecePaths[b][6]=64;
	multiPiecePaths[b][7]=76;

}
if(b == 12)
{
	multiPiecePaths[b][0]=68;
	multiPiecePaths[b][1]=76;
	multiPiecePaths[b][2]=84;
	multiPiecePaths[b][3]=92;
	multiPiecePaths[b][4]=102;
	multiPiecePaths[b][5]=112;

}
}//end for



}//end method

}