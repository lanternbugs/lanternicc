package lantern;
/*    board2[a] = board[63-a];
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
/*****************
This class represents the data of the chess game.  the board, the 64 squares and what pieces are on what square is here.
1 = white pawn 6 = white king 7 is black pawn 8 is black knight etc and 12 is black king
we have board[64] or 0-63. you can call Make move to send moves and have teh board reflect the move and have the move added to move list.
If we ever have to do something other than send a new move,  the methods like getSliderBoard or replay, esssentially recreate the board
by starting at the start position and making all moves to whatever is now the current move. illegal move does this.
the slidermove method probably should be part of makemove but for some reason i wrote another method that is preety identical to
makeMove.  with slidermove, i.e. move us now to move 29 of our 40 move game, we essentially reload or make all moves from start to move 29
( if that is where the slider indicated they wanted to see.  Its possible to simply send fens to this class and make no moves.  in game play you will see
the board updating with the new position, but then the slider wont work unless you redesign it to save fens not moves and reload the right fen.
Normally white is on bottom of vissible board ( though its on top of board reprensentation board, i.e. spots 0-15 for whites initial pieces on our
board[63] but we flip for display on graphical board.  the iflipped variable indicates black should be on bottom of viewable board, ie.. we play black
When gamestate is changed there needs to be a repaint to draw the new board on the board.
****************/
import java.util.concurrent.locks.*;
import java.util.ArrayList;
import javax.swing.*;
import java.math.BigInteger;

class gamestate {
        static BigInteger currentHash = new BigInteger("-1");
        static int hashMoveTop = 0;
    ArrayList<String> style12Boards = new ArrayList<String>();
	int [] crazypieces = new int[13];
	int [] board = new int[64];
	int [] boardCopy = new int[64];// used for move list

	int lastfrom;
	int lastto;
	int currentLastfrom;
	int currentLastto;

	int [] moveListTo = new int[6000];
	int [] moveListFrom = new int[6000];
	String [] algabraicMoves;
	String [] engineMoves;
	int engineTop;
	char [] movePromote = new char[6000];
	int [] castleCaptures = new int[6000];
	int wild;
	int movetop;
	boolean rated = true;
int lastKingMoveWhite = -1;
int lastKingMoveBlack = -1;
double whiteClock;
double blackClock;
int whiteMinute;
int blackMinute;
int whiteSecond;
int blackSecond;
int whiteTenth;
int blackTenth;
int clickCount;
long whitenow;
long blacknow;
double wtime;
double btime;
String whiteShort = "";
String whiteLong = "";
String blackShort = "";
String blackLong = "";
int turn;
int state;
boolean iWasMadeExaminer;
int iflipped;
int myGameNumber;
int movelock;
int whiteMaterialCount;
int blackMaterialCount;
String sideToMove = "w";
int blank; // for an x in examine mode
int madeMove;
String eco;
boolean [] excludedPiecesWhite;
boolean [] excludedPiecesBlack;
boolean [] excludedBoards;

boolean lowTime=false;
boolean imclosed;
randomPieces randomObj;

boolean becameExamined;
boolean sentGameEnd;
boolean mugshot1=false;
boolean mugshot2=false;
String ficsResult = "*";
String whiteTimeDisplay;
String blackTimeDisplay;
String myColor;
String title;
String tabtitle;
String myFen;
String engineFen;
String initialPosition;
String iccResult = "";
String iccResultString = "";
boolean newBoard;
String name1;
String name2;
String realname1;
String realname2;
String country1;
String country2;
String realelo1;
String realelo2;
String gameListing;
String observedPgnFile = "";
String premove;
int premovefrom;
int premoveto;
int ficsSet;
String whiteRating="";
String blackRating="";
int time=0;
int inc=0;
int played_game;

boolean piecePallette;
ArrayList<Shapes> myShapes;

class Shapes {

	int type;
	int from;
	int to;
	int Circle =0;
	int Arrow = 1;

	Shapes(){
		from=to=type=0;
	}
}
gamestate(boolean [] excludedPieces1, boolean [] excludedPieces2, boolean [] excludedBoards1)
{

       excludedPiecesWhite=excludedPieces1;
       excludedPiecesBlack=excludedPieces2;
       excludedBoards = excludedBoards1;
	myShapes = new ArrayList();
	randomObj = new randomPieces(excludedPiecesWhite, excludedPiecesBlack, excludedBoards);
	randomObj.randomizeGraphics();
	ficsSet=0;
	wild=0;
	eco = "";
	tabtitle = "";
	becameExamined=false;
        name1="";
	name2="";
	realname1="";
	realname2="";
	country1="";
	country2="";
	realelo1="";
	realelo2="";
	whiteTimeDisplay="";
	blackTimeDisplay="";
	gameListing="";
	title = "new board";
	movetop=0;
	  myFen="";
	  engineFen="";
	  initialPosition="*";
	  initialize(board);
         sentGameEnd=false;
	lastfrom=-1;
	lastto=-1;
	blank=50;
	madeMove = 0;
        clickCount=0;
	wtime=0;
	btime=0;
        played_game = -1; // 0 not played 1 played -1 not set, used in auto unob on examine
	newBoard = true;
	whiteMaterialCount=blackMaterialCount=39;
	myGameNumber = -100;
	imclosed=false;
	movelock=0;
	whiteMinute=blackMinute=whiteSecond=blackSecond=whiteTenth=blackTenth=0;
	state=-1;
	iWasMadeExaminer = false;
	myColor = "W";
    iflipped=0;
    turn=0;
    engineTop=0;
     engineMoves = new String[2000];
     algabraicMoves = new String[6000];
	premove="";
	premovefrom=0;
	premoveto=0;
	piecePallette=false;
	for(int z=0; z<13; z++)
		crazypieces[z]=0;

}

void initialize(int board[])
{


for(int a=0; a<64; a++)
	board[a]=0;

	board[0]=board[7]=4;
	board[1]=board[6]=2;
	board[2]=board[5]=3;

	board[56]=board[63]=10;
	board[57]=board[62]=8;
	board[58]=board[61]=9;

	board[4]=5;
	board[3]=6;
	board[60]=11;
	board[59]=12;
	for(int a=0; a<8; a++)
	{
		board[8+a]=1;
		board[48+a]=7;

	}
if(!initialPosition.equals("*"))
readInitialPosition2(initialPosition, board);
else if(iflipped == 1)
	{
		flipSent(board);
	}

if(!myFen.equals(""))
readFen2(myFen, board);
copyBoard();
}

void readFen(String fen)
{

myFen= new String(fen);
readFen2(fen, board);
}

void readInitialPosition(String initial)
{

initialPosition= new String(initial);
readInitialPosition2(initialPosition, board);
generateFen(board, initial);
}

void generateFen(int board[], String initial)
{
  engineFen = "";
  engineTop = 0;
  if(initial.equals("*")) {
    // this is enough to make gameboard.java send startpos for fen.
   engineFen = "*";
   return;
  }

int spaces = 0;
for(int a=7; a>=0; a--)
{
for(int b=7; b>=0; b--)
{
  int n=a*8+b;
if(board[n]==0)
spaces++;
else
{
if(spaces > 0)
{
 engineFen+="" + spaces;
 spaces=0;
}
if(board[n]==12)
engineFen+="k";
if(board[n]==11)
engineFen+="q";
if(board[n]==10)
engineFen+="r";
if(board[n]==9)
engineFen+="b";
if(board[n]==8)
engineFen+="n";
if(board[n]==7)
engineFen+="p";
if(board[n]==6)
engineFen+="K";
if(board[n]==5)
engineFen+="Q";
if(board[n]==4)
engineFen+="R";
if(board[n]==3)
engineFen+="B";
if(board[n]==2)
engineFen+="N";
if(board[n]==1)
engineFen+="P";



}// end else
} // end b loop

if(spaces > 0)
{
 engineFen+="" + spaces;
 spaces=0;
}
if(a>0)
engineFen+="/";
}// end a loop
if(engineFen.equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"))
//engineFen+=" w KQkq - 0";
engineFen = "";
else
engineFen+=" w - - 0";
} // end method

void readFen2(String fen, int board[])
{

	// this applies an existing fen, myFen, to any board.
	int a, n;
	if(state != 2)
	return; // we want to use the move list for  now. the fen on observing in middle seems to be fen at middle of game
	for(a=0; a<64; a++)
		board[a]=0;

	a=0;
	n=0;
	while(a<64)
	{
	if(fen.charAt(n) == 'K')
	{ board[63 - a]=6; a++; n++;}
	else if(fen.charAt(n) == 'Q' )
	{ board[63 - a]=5; a++; n++;}
	else if(fen.charAt(n) == 'R')
	{ board[63 - a]=4; a++; n++;}
	else if(fen.charAt(n) == 'B')
	{ board[63 - a]=3; a++; n++;}
	else if(fen.charAt(n) == 'N')
	{ board[63 - a]=2; a++; n++;}
	else if(fen.charAt(n) == 'P')
	{ board[63 - a]=1; a++; n++;}
	else if(fen.charAt(n) == 'k')
	{ board[63 - a]=12; a++; n++;}
	else if(fen.charAt(n) == 'q')
	{ board[63 - a]=11; a++; n++;}
	else if(fen.charAt(n) == 'r')
	{ board[63 - a]=10; a++; n++;}
	else if(fen.charAt(n) == 'b')
	{ board[63 - a]=9; a++; n++;}
	else if(fen.charAt(n) == 'n')
	{ board[63 - a]=8; a++; n++;}
	else if(fen.charAt(n) == 'p')
	{ board[63 - a]=7; a++; n++;}
	else
	{
	try {
		String b=fen.substring(n, n + 1);

		int c = Integer.parseInt(b);
		if(c > 0)
		for(int d=0; d<c; d++)
		{
			board[63 - a]=0;
			a++;
		}// end for
		n++;
	}// end try
	catch(Exception e)
	{n++;}
	}// end final else




	}// end while



	// now reverse
/*	int [] board9;
	board9=new int[64];

	for(a=0; a<64; a++)
	board9[a]=board[a];

for(a=0; a<8; a++)
for(int z=0; z<8; z++)
{
	board[a*8 + z] = board9[a*8 + 7 -z];
}
*/
}


void readInitialPosition2(String initial, int board[])
{

	// this applies an existing fen, myFen, to any board.
	int a, b, n;
	if(initial.equals("*"))
	return; // * is sent by server for standard chess
	for(a=0; a<64; a++)
		board[a]=0;

	a=0;
	n=0;

	// initial is a8 b8 ... h1 with - pnbrqkPNBRQK
	for(a=63; a>=0; a--)
	{
		b=(int)(a/8);
		n = a - b*8;
		n=7-n;
		n=(7-b)*8+n;

		if(initial.charAt(a) == '-')
		continue;
		if(initial.charAt(a) == 'p')
		board[n]=7;
		if(initial.charAt(a) == 'n')
		board[n]=8;
		if(initial.charAt(a) == 'b')
		board[n]=9;
		if(initial.charAt(a) == 'r')
		board[n]=10;
		if(initial.charAt(a) == 'q')
		board[n]=11;
		if(initial.charAt(a) == 'k')
		board[n]=12;
		if(initial.charAt(a) == 'P')
		board[n]=1;
		if(initial.charAt(a) == 'N')
		board[n]=2;
		if(initial.charAt(a) == 'B')
		board[n]=3;
		if(initial.charAt(a) == 'R')
		board[n]=4;
		if(initial.charAt(a) == 'Q')
		board[n]=5;
		if(initial.charAt(a) == 'K')
		board[n]=6;







	}// end loop
        setMaterialCount(board);
	if(iflipped == 1)
	{
		flipSent(board);

	}
}// end method

void doFlip()// gameboard can call this and access the standard board
{
	flipSent(board);

	// flip initial postiion
	String tempinitial = "";
	copyBoard();
}

void flipMoves()
{
	for(int a=0; a<movetop; a++)
	{
		if(moveListFrom[a] >=0)// in crazyhouse would be -piecevalue for from i.e. -7 63 black pawn at spot 63
			moveListFrom[a] = 63 - moveListFrom[a];
		moveListTo[a] = 63 - moveListTo[a];
	}

	if(lastfrom>=0)
	{	lastfrom = 63-lastfrom;
                currentLastfrom =lastfrom;
        }
        lastto = 63 - lastto;
        currentLastto=lastto;


}
void flipSent(int [] board)// this is a dynamic flip method, can handle flip at any time with any board
{
		int flippedboard[] = new int[64];
		for(int x=0; x<64; x++)
		flippedboard[x]=board[63-x];

		for(int x=0; x<64; x++)
		board[x]=flippedboard[x];

}
void flip() // this is a static flip method for standard chess at beginning at game
{
	for(int a=0; a<64; a++)
	board[a]=0;

	board[0]=board[7]=10;
	board[1]=board[6]=8;
	board[2]=board[5]=9;

	board[56]=board[63]=4;
	board[57]=board[62]=2;
	board[58]=board[61]=3;

	board[4]=12;
	board[3]=11;
	board[60]=6;
	board[59]=5;
	for(int a=0; a<8; a++)
	{
		board[8+a]=7;
		board[48+a]=1;

	}


}
void sliderflip(int board[])
{
	for(int a=0; a<64; a++)
	board[a]=0;

	board[0]=board[7]=10;
	board[1]=board[6]=8;
	board[2]=board[5]=9;

	board[56]=board[63]=4;
	board[57]=board[62]=2;
	board[58]=board[61]=3;

	board[4]=12;
	board[3]=11;
	board[60]=6;
	board[59]=5;
	for(int a=0; a<8; a++)
	{
		board[8+a]=7;
		board[48+a]=1;

	}
if(!initialPosition.equals("*"))
readInitialPosition2(initialPosition, board);

}


void getSliderBoard(int num, int sliderboard[])
{
	initialize(sliderboard);
	if(iflipped == 1)
	sliderflip(sliderboard);
	for(int a=1; a<=num; a++)
	if(wild != 16 || moveListFrom[a-1] != -1)
	makeslidermove(moveListFrom[a-1], moveListTo[a-1], movePromote[a-1], 1, castleCaptures[a-1], a-1, sliderboard);
		else
		{
			if(moveListTo[a] == -1)
			kriegSliderMove(1, sliderboard);
			else
			kriegSliderCapture(moveListTo[a], 1, sliderboard);
		}
setMaterialCount(sliderboard);
}

void replay()
{
	initialize(board);
ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
Lock writeLock = rwl.writeLock();
writeLock.lock();
	int oldmovetop=movetop;
	for(int a=0; a<oldmovetop; a++)
	{
		movetop=a;
		if(wild != 16 || moveListFrom[a] != -1)
		makemove(moveListFrom[a], moveListTo[a], movePromote[a], 1, castleCaptures[a], algabraicMoves[a]);
		else
		{
			if(moveListTo[a] == -1)
			kriegMove(1);
			else
			kriegCapture(moveListTo[a], 1);
		}
	}
	movetop=oldmovetop;
	writeLock.unlock();

}

void addCircle(int from)
{

	Shapes temp=new Shapes();
	temp.from=from;
	temp.type=temp.Circle;
	myShapes.add(temp);
}
void addArrow(int from, int to)
{

	Shapes temp=new Shapes();
	temp.from=from;
	temp.to=to;
	temp.type=temp.Arrow;
	myShapes.add(temp);
}


void clearShapes()
{
	myShapes.clear();
}
boolean myturn()
{
	boolean value = false;
        int check =0;
	if(myColor.equals("W"))
	check=1;

	if((turn+madeMove)%2 != check)
	value =  true;
        else
	value = false;

        if(wild == 30)
        return !value;

        return value;
}

void kriegSliderCapture(int square, int reload, int [] board)
{
try {
	board[square]=0;
if(reload == 0)
{
		moveListTo[movetop]=square;
		moveListFrom[movetop]=-1;
		movePromote[movetop]=0;
		castleCaptures[movetop]=0;
		movetop++;


}
}catch(Exception d){}

}
void kriegSliderMove(int reload, int [] board)
{

	if(reload == 0)
	{
		moveListTo[movetop]=-1;
		moveListFrom[movetop]=-1;
		movePromote[movetop]=0;
		castleCaptures[movetop]=0;
		movetop++;
	}




}

void kriegCapture(int square, int reload)
{
try {
	board[square]=0;
if(reload == 0)
{
		moveListTo[movetop]=square;
		moveListFrom[movetop]=-1;
		movePromote[movetop]=0;
		castleCaptures[movetop]=0;
		movetop++;


}
}catch(Exception d){}

}
void kriegMove(int reload)
{

	if(reload == 0)
	{
		moveListTo[movetop]=-1;
		moveListFrom[movetop]=-1;
		movePromote[movetop]=0;
		castleCaptures[movetop]=0;
		movetop++;
	}




}

// from and to are the basic what move do i make on board
// reload is for example dont make sound, we are reloading moves allready made
// castle capture is used in enpassant and fr castles i think.  set to "" normally
// normal castle is detected as king move 2 but fr castle of course that doesnt work but icc sends i think a "c" and it looks like i'm feeding
// numbers in like if castleCapture == 3 for enpassant.
// prom is any promotion character, i.e 'Q'. i think type that is returnerd is for sound.
int makemove(int from, int to, char prom, int reload, int castleCapture, String algabraicMove)// h8 = 1 in checkers
{

        if(reload == 0)
       { algabraicMoves[movetop] = algabraicMove;
        
       }
       if(from > -1 && to > -1 && (board[from] == 6 || board[to] == 6) && lastKingMoveWhite == -1) {
        lastKingMoveWhite = movetop + 1;
       }  else if(from > -1 && to > -1 && (board[from] == 12 || board[to] == 12) && lastKingMoveBlack == -1) {
        lastKingMoveBlack = movetop + 1;
       }

	if((castleCapture == 1 || castleCapture == 2) && wild==22)
	{

		makecastleCapture(castleCapture, movetop, board);
			if(reload == 0)
			{
			moveListTo[movetop]=to;
			moveListFrom[movetop]=from;
			movePromote[movetop]=prom;
			castleCaptures[movetop]=castleCapture;
			movetop++;
			}

		lastfrom=from;
	    lastto=to;
             currentLastfrom=lastfrom;
             currentLastto=lastto;
		return 0;  // type for sound
	}


	// enpassant.
	if(castleCapture == 3)
	{
		if(from - to == 7)
		board[from + 1] =0;
		if(from - to == 9)
		board[from -1] =0;
		if(from - to  == -7)
		board[from -1 ] =0;
		if(from - to == -9)
		board[from + 1] =0;

		if(wild == 27)
		{	makeAtomicCaptures(board, from, to);
		        board[to]=0;
	        }
	}
        if(wild == 30)
         captureCheckers(from, to, algabraicMove ,board);
	int type =-1; // no sound
	if(from >=0)// not crazyhouse drop
	{
		if(( state != 1 || (state == 1 && board[from]!=0)) && (state != 2 || board[from]!=0)) // state 0=observing 1=playing 2=examining
	{
		if(board[to] != 0)// for sound
		type=1;// capture
		else
		type=0;
		if(wild == 27 && board[to]!=0)
			makeAtomicCaptures(board, from, to);

	    board[to]=board[from];
	    if(from != to)// so we can send dummy moves like black first
            board[from]=0;

	}
	else if(reload == 1)
	{
		if(board[to] != 0) //for sound
		type=1;// capture
		else
		type=0;

	board[to]=board[from];
        if(from != to)// so we can send dummy moves like black first
	board[from]=0;

	}
	}// from >=0, not crazyhouse drop
	else
	{
		if(from==blank)
			board[to]=0;
		else
			board[to]=-from;
		type=1;
		copyBoard();// this should hit at end but for some reason its not getting there with crazyhouse move. need to investigate
	}
	if(reload == 0)
	{
	moveListTo[movetop]=to;
	moveListFrom[movetop]=from;
	movePromote[movetop]=prom;
	castleCaptures[movetop]=castleCapture;
	movetop++;
	}
	if(from >=0)
		lastfrom=from;
	else
		lastfrom=to;//crazyhouse
	lastto=to;
        currentLastfrom=lastfrom;
        currentLastto=lastto;

	if(from<0)
	 return type;

	// castling
	if(board[to] == 6 || board[to] == 12)
	{
		if(iflipped == 0)
		{if(from - to == 2)// king side castle
		{board[to+1]=board[to-1];
		board[to-1]=0;}


		if(from - to == -2)// queen side castle.
		{board[to-1]=board[to+2];
		board[to+2]=0;}
 	}else
 	{// black
	if(from - to == -2)// king side castle k 60-62 r 63-61
	{board[to-1]=board[to+1];
	board[to+1]=0;}


	if(from - to == 2)// queen side castle.
	{board[to+1]=board[to-2];
	board[to-2]=0;}
 	}


	 }
           if(wild == 30)
           {
              if(board[to] == 1 && to > 55)
              board[to]=6;
             if(board[to] == 1 && to < 8)
              board[to]=6;
             if(board[to] == 7 && to < 8)
              board[to]=12;
             if(board[to] == 7 && to > 55)
              board[to]=12;
           }
	 if(prom == 'K')
	 {
		 if(board[to]<7)
		 board[to]=6;
		 else
		 board[to]=12;
	 }


	 if(prom == 'Q')
	 {
		 if(board[to]<7)
		 board[to]=5;
		 else
		 board[to]=11;
		 if(wild == 27 && (from%8 != to%8)) // indicates capture in atomic
		    board[to]=0;

	 }
	 if(prom == 'R')
	 {
	 	if(board[to]<7)
	 	board[to]=4;
	 	else
	 	board[to]=10;
		 if(wild == 27 && (from%8 != to%8)) // indicates capture in atomic
		    board[to]=0;

	 }
	 if(prom == 'B')
	 {
		 if(board[to]<7)
		 board[to]=3;
		 else
		 board[to]=9;
		 if(wild == 27 && (from%8 != to%8)) // indicates capture in atomic
		    board[to]=0;

	 }
	 if(prom == 'N')
	 {
	 	if(board[to]<7)
	 	board[to]=2;
	 	else
	 	board[to]=8;
		 if(wild == 27 && (from%8 != to%8)) // indicates capture in atomic
		    board[to]=0;

	 }

copyBoard();// to have current icc board for move list

setMaterialCount(board);
  if(state == 2) {
    computeHash();
  }

return type;

}

void makeEngineMove(String move)
{
        if(move.contains("e2e2")) {
          engineFen = engineFen.replace(" w - - 0", " b - - 0");
          return;
        }

	engineMoves[engineTop]=move;
	engineTop++;
}

String getEngineMove(int t)
{
	if(t >= 0 && t < engineTop)
	return engineMoves[t];

	return "\n";
}

String getUciMoves()
{
	String s="";
	// moves e2e4 e7e5
	for(int a=0; a<engineTop; a++)
	{
	if(a==0)
	s+= " moves ";
	else
	s+= " ";

	String m = engineMoves[a];
	int z= m.length();
	if(z > 0)
	{
		if(m.charAt(z-1) == '\n')
		s+=m.substring(0,z-1);
		else
		s+=m;
	}
	}
	return s + "\n";

}
void makeCheckerCapture(int from, int to, int board2[])
{
      // h8 = 1 in  checkers and 63 on board
      

      

      from = 63 - (from -1) * 2;
      to = 63 - (to-1) * 2;
      int fromCol = from / 8 + 1;
      if(from %8 == 0)
      fromCol --;
      int toCol = to / 8 + 1;
      if(to %8 == 0)
      toCol --;
    /*  
    // works with me white on bottom not black
    if((fromCol %2 == 0 && iflipped == 0) ||(fromCol %2 == 1 && iflipped == 1) )
      from--;
      if((toCol %2 == 0 && iflipped == 0) ||(toCol %2 == 1 && iflipped == 1))
      to--;
      */
       if(fromCol %2 == 0 )
      from--;
      if(toCol %2 == 0)
      to--;
      if(iflipped == 1)
      {
       from = 63 - from;
       to = 63 - to;
      }
      if(from < 0 || to < 0 || from > 63 || to > 63)
      return;
      
  if(from - to == 18)
  board2[from - 9] = 0;
  else if(from - to == 14)
  board2[from - 7] = 0;
  else if(to - from  ==  18)
  board2[to  - 9] = 0;
  else if(to - from  == 14)
  board2[to - 7] = 0;

}
void captureCheckers(int from, int to, String move, int board2[])
{

          boolean go=true;
  if(!move.equals(""))
  {
      String algabraicMove = move;
 try {
   while(go)
   {
     int a = algabraicMove.indexOf("-");
     if(a == -1)
     go=false;
     else
     {
      from = Integer.parseInt(algabraicMove.substring(0, a));
     algabraicMove  = algabraicMove.substring(a+1, algabraicMove.length());
      a = algabraicMove.indexOf("-");
      if(a > -1)
      {
        to  = Integer.parseInt(algabraicMove.substring(0, a));
      }
      else
      {
         to  = Integer.parseInt(algabraicMove.substring(0, algabraicMove.length()));
      }  // end else

      makeCheckerCapture(from, to, board2);
     }// end else
   }// end while
     }// end try
     catch(Exception dui){}
  }  // end initial if





}
void makeslidermove(int from, int to, char prom, int reload, int castleCapture, int movetop, int board[])
{


	if(from == to && from == 0 && wild != 22)
	return; // fics move list could have empty moves since we dont get moves before we start observing.

	if((castleCapture == 1 || castleCapture == 2) && wild==22)
	{

		makecastleCapture(castleCapture, movetop, board);
			lastfrom=from;
	       lastto=to;
			return;

	}
	// enpassant.
	if(castleCapture == 3)
	{
		if(from - to == 7)
		board[from + 1] =0;
		if(from - to == 9)
		board[from -1] =0;
		if(from - to  == -7)
		board[from -1 ] =0;
		if(from - to == -9)
		board[from + 1] =0;

		if(wild == 27)
		{	makeAtomicCaptures(board, from, to);
		        board[to]=0;
	        }

	}
        if(wild == 30)
        { captureCheckers(from, to, algabraicMoves[movetop], board);
         
        }
	int check =0;
	if(myColor.equals("W"))
	check=1;
	if(from>=0) // not crazyhouse drop
	{

	if((turn%2 != check || state != 1 || (state == 1 && board[from]!=0)) && (state != 2 || board[from]!=0))
	{
		if(wild == 27 && board[to]!=0)
			makeAtomicCaptures(board, from, to);
	board[to]=board[from];
        if(from != to)// so we can send dummy moves like black first
	board[from]=0;

	}
	else if(reload == 1)
	{
	board[to]=board[from];
        if(from != to)// so we can send dummy moves like black first
	board[from]=0;

	}

	if(reload == 0)
	{
	moveListTo[movetop]=to;
	moveListFrom[movetop]=from;
	movePromote[movetop]=prom;
	movetop++;
	}
	}// end not crazyhouse drop @
	else
	{
		board[to]=-from;

	}

	if(from >=0)
		lastfrom=from;
	else
		lastfrom=to;// drop
	lastto=to;

	if(from<0)
		return;

	// castling
	if(board[to] == 6 || board[to] == 12)
	{
		if(iflipped == 0)
		{if(from - to == 2)// king side castle
		{board[to+1]=board[to-1];
		board[to-1]=0;}


		if(from - to == -2)// queen side castle.
		{board[to-1]=board[to+2];
		board[to+2]=0;}
 	}else
 	{// black
	if(from - to == -2)// king side castle k 60-62 r 63-61
	{board[to-1]=board[to+1];
	board[to+1]=0;}


	if(from - to == 2)// queen side castle.
	{board[to+1]=board[to-2];
	board[to-2]=0;}
 	}


	 }
            if(wild == 30)
           {
             if(board[to] == 1 && to > 55)
              board[to]=6;
             if(board[to] == 1 && to < 8)
              board[to]=6;
             if(board[to] == 7 && to < 8)
              board[to]=12;
             if(board[to] == 7 && to > 55)
              board[to]=12;
           }

	 if(prom == 'K')
	 {
		 if(board[to]<7)
		 board[to]=6;
		 else
		 board[to]=12;

		 if(wild == 27 && (from%8 != to%8)) // indicates capture in atomic
		 	board[to]=0;
	 }

	 if(prom == 'Q')
	 {
		 if(board[to]<7)
		 board[to]=5;
		 else
		 board[to]=11;
		 if(wild == 27 && (from%8 != to%8)) // indicates capture in atomic
		 	board[to]=0;

	 }
	 if(prom == 'R')
	 {
	 	if(board[to]<7)
	 	board[to]=4;
	 	else
	 	board[to]=10;
		 if(wild == 27 && (from%8 != to%8)) // indicates capture in atomic
		    board[to]=0;

	 }
	 if(prom == 'B')
	 {
		 if(board[to]<7)
		 board[to]=3;
		 else
		 board[to]=9;
		 if(wild == 27 && (from%8 != to%8)) // indicates capture in atomic
		    board[to]=0;

	 }
	 if(prom == 'N')
	 {
	 	if(board[to]<7)
	 	board[to]=2;
	 	else
	 	board[to]=8;
		 if(wild == 27 && (from%8 != to%8)) // indicates capture in atomic
		    board[to]=0;

	 }


}
/************************ FR Chess 960 Castle Functions **************************************/

void makecastleCapture(int castleCapture, int movetop, int board[])
{
		int check =0;
		if(myColor.equals("W"))
		check=1;
		// need to know color and turn to determine if white or black is castling.

		int k=-1;
		int r=-1;
		// castleCapture 1 oo 2 ooo
		if(movetop%2== 0)// whites move
		{
			if(castleCapture == 1)
			{
				k=getKingWhiteFR(board);
				r=getKingRookWhiteFR(board);

				// erase
				board[k]=0;
				board[r]=0;

				// move

				if(iflipped == 1)
				{
					board[62]=6;
					board[61]=4;
				}
				else
				{
					board[1]=6;
					board[2]=4;

				}

			}
			if(castleCapture == 2)
			{
				k=getKingWhiteFR(board);
				r=getQueenRookWhiteFR(board);

				// erase
				board[k]=0;
				board[r]=0;

				// move

				if(iflipped == 1)
				{
					board[58]=6;
					board[59]=4;
				}
				else
				{
					board[5]=6;
					board[4]=4;

				}

			}





		}
		else // blacks move
		{
			if(castleCapture == 1)
			{
				k=getKingBlackFR(board);
				r=getKingRookBlackFR(board);

				// erase
				board[k]=0;
				board[r]=0;

				// move

				if(iflipped == 1)
				{
					board[6]=12;
					board[5]=10;
				}
				else
				{
					board[57]=12;
					board[58]=10;

				}

			}


			if(castleCapture == 2)
			{
				k=getKingBlackFR(board);
				r=getQueenRookBlackFR(board);

				// erase
				board[k]=0;
				board[r]=0;

				// move

				if(iflipped == 1)
				{
					board[2]=12;
					board[3]=10;
				}
				else
				{
					board[61]=12;
					board[60]=10;

				}

			}




		}
}
int getKingWhiteFR(int board[])
{
	int a=0;
	if(iflipped == 1)// white on bottom 56-63
	{
		for(a=56; a < 64; a++)
		if(board[a]==6)
		return a;

		return -1;
	}
	else // white on top 0-7
	{
		for(a=0; a <= 7; a++)
		if(board[a]==6)
		return a;

		return -1;

	}
}

int getKingBlackFR(int board[])
{
	int a=0;
	if(iflipped == 0)// black on bottom 56-63
	{
		for(a=56; a < 64; a++)
		if(board[a]==12)
		return a;

		return -1;
	}
	else // black on top 0-7
	{
		for(a=0; a <= 7 ; a++)
		if(board[a]==12)
		return a;

		return -1;

	}

}

int getKingRookWhiteFR(int board[])
{
	int a=0;
	if(iflipped == 1)// white on top 56-63
	{
		for(a=63; a>=56; a--)
		if(board[a]==4)
		return a;

		return -1;
	}
	else // white on bottom 0-7
	{
		for(a=0; a <=7; a++)
		if(board[a]==4)
		return a;

		return -1;

	}

}

int getQueenRookWhiteFR(int board[])
{
	int a=0;
	if(iflipped == 1)// white on bottom 56-63
	{
		for(a=56; a<=63; a++)
		if(board[a]==4)
		return a;

		return -1;
	}
	else // white on top 0-7
	{
		for(a=7; a >=0; a--)
		if(board[a]==4)
		return a;

		return -1;

	}

}

int getKingRookBlackFR(int board[])
{
	int a=0;
	if(iflipped == 1)// white on bottom 56-63
	{
		for(a=7; a >=0; a--)
		if(board[a]==10)
		return a;

		return -1;
	}
	else // white on top 0-7
	{
		for(a=56; a <=63; a++)
		if(board[a]==10)
		return a;

		return -1;

	}

}

int getQueenRookBlackFR(int board[])
{
	int a=0;
	if(iflipped == 1)// white on bottom 56-63
	{
		for(a=0; a<=7; a++)
		if(board[a]==10)
		return a;

		return -1;
	}
	else // white on top 0-7
	{
		for(a=63; a >=56; a--)
		if(board[a]==10)
		return a;

		return -1;

	}

}
/************************ End FR Chess 960 Castle Funct **************************************/

/********************** atomic capture function **********************************************/

void makeAtomicCaptures(int board[], int from, int to)
{

// this method is allways called before a move is made by any other means
// the squares around to that are not pawns are zeroed out
// and board from is zeroed out before it moves to space to

	// based on board 64 0-63

	int v = to +8;
	if(v < 64) // capture up
	{
		if(board[v] != 7 && board[v]!= 1)
			board[v]=0;
	}
	 v = to - 8;
	if(v >=0) // capture down
	{
		if(board[v] != 7 && board[v]!= 1)
			board[v]=0;
	}
	 v = to + 1;
	if(v < 64 && v % 8 !=0) // capture right
	{
		if(board[v] != 7 && board[v]!= 1)
			board[v]=0;
	}

	 v = to - 1;
	if(v >= 0 && v % 8 !=7) // capture left
	{
		if(board[v] != 7 && board[v]!= 1)
			board[v]=0;
	}





// diagonal

	 v = to +9;
	if(v < 64 && v%8 != 0) // capture up
	{
		if(board[v] != 7 && board[v]!= 1)
			board[v]=0;
	}
	 v = to - 9;
	if(v >=0 && v%8 !=7) // capture down
	{
		if(board[v] != 7 && board[v]!= 1)
			board[v]=0;
	}
	 v = to + 7;
	if(v < 64 && v%8 !=7) // capture up
	{
		if(board[v] != 7 && board[v]!= 1)
			board[v]=0;
	}

	 v = to - 7;
	if(v >= 0 && v%8 !=0) // capture down
	{
		if(board[v] != 7 && board[v]!= 1)
			board[v]=0;
	}

	// now erase my own piece
	board[from]=0;

}// end method atomic capture

void copyBoard()
{

	for(int a=0; a<64; a++)
	boardCopy[a]=board[a];
}


void setMaterialCount(int [] board)
{
	whiteMaterialCount=blackMaterialCount=0;

	for(int a=0; a<64; a++)
	{

		if(board[a] == 1)
			whiteMaterialCount+=1;
		if(board[a] == 2)
			whiteMaterialCount+=3;
		if(board[a] == 3)
			whiteMaterialCount+=3;
		if(board[a] == 4)
			whiteMaterialCount+=5;
		if(board[a] == 5)
			whiteMaterialCount+=9;
		if(board[a] == 6 && wild == 30)
			whiteMaterialCount+=2;

		if(board[a] == 7)
			blackMaterialCount+=1;
		if(board[a] == 8)
			blackMaterialCount+=3;
		if(board[a] == 9)
			blackMaterialCount+=3;
		if(board[a] == 10)
			blackMaterialCount+=5;
		if(board[a] == 11)
			blackMaterialCount+=9;
		if(board[a] == 12 && wild == 30)
			blackMaterialCount+=2;





	}

}

void computeHash()
{        try {
        hashMoveTop = movetop;
        currentHash = new BigInteger( HashKeysClass.globalinitialhash.toString());
        int [] board2 = new int[64];
	for(int a=0; a<64; a++)
	{  if(iflipped == 0) {
              board2[a] = board[63-a];
          } else {
              board2[a] = board[a];
            }
          if(board2[a]>0)
          {
              BigInteger newHash = HashKeysClass.hashboard[a][board2[a]].xor(currentHash);
              currentHash = new BigInteger(newHash.toString());

          }
        }
         if(movetop  %2 == 1) {
            BigInteger newHash = HashKeysClass.hashtoggle.xor(currentHash);
              currentHash = new BigInteger(newHash.toString());
         }
         if(lastKingMoveWhite == -1) {
              // white king
          BigInteger newHash1 = HashKeysClass.hashwk.xor(currentHash);
              currentHash = new BigInteger(newHash1.toString());
          // white king
          BigInteger newHash2 = HashKeysClass.hashwq.xor(currentHash);
              currentHash = new BigInteger(newHash2.toString());
         }

        if(lastKingMoveBlack == -1)
        {
          // black king
          BigInteger newHash3 = HashKeysClass.hashbk.xor(currentHash);
              currentHash = new BigInteger(newHash3.toString());
          // black king
          BigInteger newHash4 = HashKeysClass.hashbq.xor(currentHash);
              currentHash = new BigInteger(newHash4.toString());
        }

         /*
           if(self->rights.wk == 1)
        key = key ^ hashwk;
    if(self->rights.wq == 1)
        key = key ^ hashwq;
    if(self->rights.bk == 1)
        key = key ^ hashbk;
    if(self->rights.bq == 1)
        key = key ^ hashbq;
         */

     }
     catch(Exception e)  {System.out.println("error in game state hash generator "); }

}
 // MARK: FICS Engine Fen Code
    String getStockfishFen()
    {
        String side = sideToMove.equals("W") ? "b" : "w";
        String fen =  getPGNPartialFen() + " " + side + " ";
        // need support for following below:
        // whiteShort, whiteLong, blackShort, blackLong, sideToMove, enpassantSquare
        
        // KQkq or kq or -
        
        
        //+ " - - 0";
        // castle rights
        boolean haveWhiteRights = false;
        boolean haveBlackRights = false;
        if(whiteShort.equals("") && whiteLong.equals("")) {
        haveWhiteRights = false;
    } else {
        fen = fen + whiteShort;
        fen = fen + whiteLong;
        haveWhiteRights = true;
    }
        if(blackShort.equals("") && blackLong.equals("")) {
        haveBlackRights = false;
    } else {
        haveBlackRights = true;
        fen = fen + blackShort;
        fen = fen + blackLong;
    }
        if(!haveBlackRights && !haveWhiteRights) {
            fen = fen + "-";
        }
        return fen + " - 0 \n";
        /*
        if(enpassantSquare.equals("")) {
        fen = fen + " - 0";
    } else {
        fen = fen + " ";
        fen = fen + enpassantSquare;
        fen = fen + " 0";
    }
        return fen;
         */
    }
    String getPGNPartialFen()
   {
       
       int [] board = new int[64];
       for(int a = 0; a < 64; a++) {
           board[a] = this.board[63 - a];
       }
       if(iflipped == 1) {
           flipSent(board);
       }

       String fen = "";
       for(int a = 0; a < 8; a++) {
           int spaces = 0;
           for(int b = 0; b < 8; b++) {
               if(board[a * 8 + b] == 0) {
                   spaces++;
               } else {
                   if(spaces > 0) {
                       fen = fen + spaces;
                       spaces = 0;
                   }
                   if(board[a * 8 + b] == 1) {
                       fen = fen + "P";
                   } else if(board[a * 8 + b] == 2) {
                       fen = fen + "N";
                   } else if(board[a * 8 + b] == 3) {
                       fen = fen + "B";
                   } else if(board[a * 8 + b] == 4) {
                       fen = fen + "R";
                   } else if(board[a * 8 + b] == 5) {
                       fen = fen + "Q";
                   } else if(board[a * 8 + b] == 6) {
                       fen = fen + "K";
                   } else if(board[a * 8 + b] == 7) {
                       fen = fen + "p";
                   } else if(board[a * 8 + b] == 8) {
                       fen = fen + "n";
                   } else if(board[a * 8 + b] == 9) {
                       fen = fen + "b";
                   } else if(board[a * 8 + b] == 10) {
                       fen = fen + "r";
                   } else if(board[a * 8 + b] == 11) {
                       fen = fen + "q";
                   } else if(board[a * 8 + b] == 12) {
                       fen = fen + "k";
                   }

               }
           }
           if(spaces > 0) {
               fen = fen +  spaces;
               spaces = 0;
           }
           if(a < 7) {
               fen = fen + "/";
           }
       } // for a
       return fen;
   }

}// end class game state


