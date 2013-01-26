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


class wildTypes {

String getWildNameString(String wild)
{
	try {
	int w=Integer.parseInt(wild);
	return getWildName(w);
	}catch(Exception e){}

	return "w" + wild;
}


String getWildName(int wild)
{
if(wild == 0)
return "Chess";
if(wild == 1)
return "w1 Shuffle";
if(wild == 2)
return "w2 Shuffle";
if(wild == 3)
return "w3 Shuffle";
if(wild == 4)
return "w4 Shuffle";
if(wild == 5)
return "w5 Reversed";
if(wild == 6)
return "w6 Empty Board";
if(wild == 7)
return "w7 Three Pawns";
if(wild == 8)
return "w8 Advanced";
if(wild == 9)
return "w9 Two Kings";
if(wild == 10)
return "w10 Pawn Odds";
if(wild == 11)
return "w11 Knight Odds";
if(wild == 12)
return "w12 Rook Odds";
if(wild == 13)
return "w13 Queen Odds";
if(wild == 14)
return "w14 Rook Odds";
if(wild == 15)
return "w15 Bishop/Knight Mate";
if(wild == 16)
return "w16 Kriegspiel";
if(wild == 17)
return "w17 Loser's";
if(wild == 18)
return "w18 Power Chess";
if(wild == 19)
return "w19 KNNkp";
if(wild == 20)
return "w20 Loadgame";
if(wild == 21)
return "w21 Thematic";
if(wild == 22)
return "w22 Chess 960";
if(wild == 23)
return "w23 Crazyhouse";
if(wild == 24)
return "w24 Bughouse";
if(wild == 25)
return "w25 Three Checks";
if(wild == 26)
return "w26 Giveaway";
if(wild == 27)
return "w27 Atomic";
if(wild == 28)
return "w28 Shatranj";

return "w" + wild;

}// end get wild type method
}