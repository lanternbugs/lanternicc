package lantern;
/*
*  Copyright (C) 2010 Michael Ronald Adams.
*  All rights reserved.
*
* this file is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.

*  This code is distributed in the hope that it will
*  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  General Public License for more details.

note - 4-21-19. i've relicenced this file From GPL to LGPL.  Since it is just a utility file used in
a chessclub connectoin I want to make it more easily availble for anyone doing a parser to ICC
*/
import java.io.*;

public class Datagram1
{


Datagram1()
{

}


Datagram1(String s)
{
    makeDatagram(s);
}


void makeDatagram(String s)
{
try {
	if(s.length() == 0)// this is fics dummy datagram
		return;

		type = -1;

        int len = s.length();
        if (len >= 100000)
        {
               // Datagram to long!!
                len = 100000 - 1;
        }


       String p = s.substring(2, len-1);

		// newbox
		//newbox.append("trying to parse 2, p.length =" + p.length() + " p is: " + p + "\n");
/*StyledDocument doc=consoles[0].getStyledDocument();
						try {
							doc.insertString(doc.getLength(), "trying to parse 2, p.length =" + p.length() + " p is: " + p + "\n", null);


						consoles[0].setStyledDocument(doc);
						}
						catch(Exception e)
						{
						}

*/
        argc=0;
		int a=1; // allways on
        while (a==1)
        {
                if (p.charAt(0)=='{')
                {
                        int end = p.indexOf("}");
                        String p2;
                        if(end != 1)
                        p2=p.substring(1, end);
                        else
                        p2="";
                        arg[argc++] = p2;
                        try {
							p=p.substring(end+1, p.length());
							if(p.length() < 3)
								return;
													}
						catch(Exception dd){ return;}

                }
                else if(p.charAt(0)=='\031' && p.charAt(1)=='{'){
					int counter=0;
					while (p.charAt(0)=='\031' && p.charAt(1)=='{')
                {
					counter++;

                       int end = p.indexOf("\031}") ;
                         String p2;
                        if(end != 2)
                        p2=p.substring(2, end);
                        else
                        p2="";
                        arg[argc++] = p2;
                        try {
							p=p.substring(end+2, p.length());
							if(p.length() < 3)
							  return;
						}
						catch(Exception dd){ return;}

                }// end while
                }// end if
                else if(p.charAt(0) != ' ' && p.charAt(0) != ')')
                {
				int end = p.indexOf(" ");
				//writedg("p remains start :" + p + ": and lenght is " + p.length());
				if(end == -1)
				{end = p.indexOf("\031");
				if(end == -1)
					return;
				}
					//writedg("final else " + argc);
                        String p2=p.substring(0, end);
                        arg[argc++] = p2;
                        p=p.substring(end, p.length());
                    //    writedg("final else2 " + argc + " and arg is :" + arg[argc-1] + ":");
                    //    writedg("p remains end :" + p + ": and lenght is " + p.length());

				}

				//if(p.charAt(0) == '\031' && p.charAt(1) == ')')
               // return;

                while (a==1)
                {
                        if(p.length() <= 1) // " )'\n''031'" in no particular order
                        return;
                        else
                        {
                   		if(p.charAt(0) == '{')
                   		break;
                   		else if(p.charAt(0) == '\031' && p.charAt(1) == '{')
                   		break;
                   		else
                   		p=p.substring(1, p.length());
				   		}
                       if(p.length() == 1) // " )'\n''031'" in no particular order
                        return;
                        if (p.charAt(0) != ' ')          // Look for a non-space.
                                break;
                }
        }
}// end try
catch(Exception dui){//writeToSubConsole(" datagram exception \n", sharedVariables.openConsoleCount-1);
}

}

/*void writedg(String mydg)
{

	StyledDocument doc=consoles[0].getStyledDocument();
							try {
								doc.insertString(doc.getLength(), mydg + "\n", null);


							consoles[0].setStyledDocument(doc);
							}
							catch(Exception e)
							{
							}

}
*/
public String getArg(int i)
{
        if (i>=argc || i<0)
                return "";

        return arg[i];
}


public String [] arg = new String[5000];
public int argc;
public int type;


static String DG_WHO_AM_I = "" +                    0;
static String DG_PLAYER_ARRIVED = "" +              1;
static String DG_PLAYER_LEFT = "" +                 2;
static String DG_BULLET = "" +                      3;
static String DG_BLITZ = "" +                       4;
static String DG_STANDARD = "" +                    5;
static String DG_WILD = "" +                        6;
static String DG_BUGHOUSE = "" +                    7;
static String DG_TIMESTAMP = "" +                   8;
static String DG_TITLES = "" +                      9;
static String DG_OPEN = "" +                        10;
static String DG_STATE = "" +                       11;
static String DG_GAME_STARTED = "" +                12;
static String DG_GAME_RESULT = "" +                 13;
static String DG_EXAMINED_GAME_IS_GONE = "" +       14;
static String DG_MY_GAME_STARTED = "" +             15;
static String DG_MY_GAME_RESULT = "" +              16;
static String DG_MY_GAME_ENDED = "" +               17;
static String DG_STARTED_OBSERVING = "" +           18;
static String DG_STOP_OBSERVING = "" +              19;
static String DG_PLAYERS_IN_MY_GAME = "" +          20;
static String DG_OFFERS_IN_MY_GAME = "" +           21;
static String DG_TAKEBACK = "" +                    22;
static String DG_BACKWARD = "" +                    23;
static String DG_SEND_MOVES = "" +                  24;
static String DG_MOVE_LIST = "" +                   25;
static String DG_KIBITZ = "" +                      26;
static String DG_PEOPLE_IN_MY_CHANNEL = "" +        27;
static String DG_CHANNEL_TELL = "" +                28;
static String DG_MATCH = "" +                       29;
static String DG_MATCH_REMOVED = "" +               30;
static String DG_PERSONAL_TELL = "" +               31;
static String DG_SHOUT = "" +                       32;
static String DG_MOVE_ALGEBRAIC = "" +              33;
static String DG_MOVE_SMITH = "" +                  34;
static String DG_MOVE_TIME = "" +                   35;
static String DG_MOVE_CLOCK = "" +                  36;
static String DG_BUGHOUSE_HOLDINGS = "" +           37;
static String DG_SET_CLOCK = "" +                   38;
static String DG_FLIP = "" +                        39;
static String DG_ISOLATED_BOARD = "" +              40;
static String DG_REFRESH = "" +                     41;
static String DG_ILLEGAL_MOVE = "" +                42;
static String DG_MY_RELATION_TO_GAME = "" +         43;
static String DG_PARTNERSHIP = "" +                 44;
static String DG_SEES_SHOUTS = "" +                 45;
static String DG_CHANNELS_SHARED = "" +             46;
static String DG_MY_VARIABLE = "" +                 47;
static String DG_MY_STRING_VARIABLE = "" +          48;
static String DG_JBOARD = "" +                      49;
static String DG_SEEK = "" +                        50;
static String DG_SEEK_REMOVED = "" +                51;
static String DG_MY_RATING = "" +                   52;
static String DG_SOUND = "" +                       53;
static String DG_PLAYER_ARRIVED_SIMPLE = "" +       55;
static String DG_MSEC = "" +                        56;
static String DG_BUGHOUSE_PASS = "" +               57;
static String DG_IP = "" +                          58;
static String DG_CIRCLE = "" +                      59;
static String DG_ARROW = "" +                       60;
static String DG_MORETIME = "" +                    61;
static String DG_PERSONAL_TELL_ECHO = "" +          62;
static String DG_SUGGESTION = "" +                  63;
static String DG_NOTIFY_ARRIVED = "" +              64;
static String DG_NOTIFY_LEFT = "" +                 65;
static String DG_NOTIFY_OPEN = "" +                 66;
static String DG_NOTIFY_STATE = "" +                67;
static String DG_MY_NOTIFY_LIST = "" +              68;
static String DG_LOGIN_FAILED = "" +                69;
static String DG_FEN = "" +                         70;
static String DG_TOURNEY_MATCH = "" +               71;
static String DG_GAMELIST_BEGIN = "" +              72;
static String DG_GAMELIST_ITEM = "" +               73;
static String DG_IDLE = "" +                        74;
static String DG_ACK_PING = "" +                    75;
static String DG_RATING_TYPE_KEY = "" +             76;
static String DG_GAME_MESSAGE = "" +                77;
static String DG_UNACCENTED = "" +                  78;
static String DG_STRINGLIST_BEGIN = "" +            79;
static String DG_STRINGLIST_ITEM = "" +             80;
static String DG_DUMMY_RESPONSE = "" +              81;
static String DG_CHANNEL_QTELL = "" +               82;
static String DG_PERSONAL_QTELL = "" +              83;
static String DG_SET_BOARD = "" +                   84;
static String DG_MATCH_ASSESSMENT = "" +            85;
static String DG_LOG_PGN = "" +                     86;
static String DG_NEW_MY_RATING = "" +               87;
static String DG_LOSERS = "" +                      88;
static String DG_UNCIRCLE = "" +                    89;
static String DG_UNARROW = "" +                     90;
static String DG_WSUGGEST = "" +                    91;
static String DG_TEMPORARY_PASSWORD = "" +          93;
static String DG_MESSAGELIST_BEGIN = "" +           94;
static String DG_MESSAGELIST_ITEM = "" +            95;
static String DG_LIST = "" +                        96;
static String DG_SJI_AD = "" +                      97;
static String DG_RETRACT = "" +                     99;
static String DG_MY_GAME_CHANGE = "" +              100;
static String DG_POSITION_BEGIN = "" +              101;
static String DG_TOURNEY = "" +                     103;
static String DG_REMOVE_TOURNEY = "" +              104;
static String DG_DIALOG_START = "" +                105;
static String DG_DIALOG_DATA = "" +                 106;
static String DG_DIALOG_DEFAULT = "" +              107;
static String DG_DIALOG_END = "" +                  108;
static String DG_DIALOG_RELEASE = "" +              109;
static String DG_POSITION_BEGIN2 = "" +             110;
static String DG_PAST_MOVE = "" +                   111;
static String DG_PGN_TAG = "" +                     112;
static String DG_IS_VARIATION = "" +                113;
static String DG_PASSWORD = "" +                    114;
static String DG_WILD_KEY = "" +                    116;
static String DG_SWITCH_SERVERS = "" +              120;
static String DG_SET2 = "" +                        124;
static String DG_FIVEMINUTE = "" +                  125;
static String DG_ONEMINUTE = "" +                   126;
static String DG_TRANSLATIONOKAY = "" +             129;
static String DG_UID = "" +                         131;
static String DG_KNOWS_FISCHER_RANDOM = "" +        132;
static String DG_COMMAND = "" +                     136;
static String DG_TOURNEY_GAME_STARTED = "" +        137;
static String DG_TOURNEY_GAME_ENDED = "" +          138;
static String DG_MY_TURN = "" +                     139;
static String DG_CORRESPONDENCE_RATING = "" +       140;
static String DG_DISABLE_PREMOVE = "" +             141;
static String DG_PSTAT = "" +                       142;
static String DG_BOARDINFO = "" +                   143;
static String DG_MOVE_LAG = "" +                    144;
static String DG_FIFTEENMINUTE = "" +               145;
static String DG_PHRASELIST_UPDATE = "" +           146;
static String DG_PHRASELIST_ITEM = "" +             147;
static String DG_MENU_SPEAK = "" +                  148;
static String DG_THREEMINUTE = "" +                 149;
static String DG_FORTYFIVEMINUTE = "" +		    150;
static String DG_CHESS960 = "" +		    151;
static String DG_COUNTRY_CODE = "" +                152;// we named this one not documented


}// end class


