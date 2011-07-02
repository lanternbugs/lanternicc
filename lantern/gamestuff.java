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
/* for a single game board just set these both to 0 and use gamestate array index 0 and forget about it. the idea is board 1 or boardindex 0 can be looking at any game in the game state array not just one to one.
*/
class gamestuff {
int BoardIndex;// our board index starts at 0 for game board 1
int LookingAt;// the  board this  board is looking at

gamestuff()
{
BoardIndex=-1;
LookingAt=-1;

}

}