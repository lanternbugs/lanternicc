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

import java.util.Vector;


class gameItem {

    void addHistoryRow(String index, String whiteName, String blackName, String whiteRating, String blackRating, String date, String time, String whitetime, String whiteinc,
                       String rated, String ratedType, String wild, String eco, String status, String color, String mode, tableClass myTable) {
        Vector<String> data = new Vector();
        if (index.length() == 1)
            data.add("0" + index);
        else
            data.add(index);

        String myName = myTable.type2;

/*Status is 0=win 1=draw 2=adjourned 3=abort.
	Color is 0=black 1=white, and is the side that lost (for status 0) or maybe acted.
*/

// my rating my color vs opponent and opponetns rating.
        if (myName.equals(whiteName)) {
            String res = "";
            if (status.equals("0")) {
                if (color.equals("0"))// white wins
                    res = "+";
                else
                    res = "-";
            }// end status 0
            if (status.equals("1"))
                res = "=";
            if (status.equals("3"))
                res = "a";

            if (channels.fics) {
                res = status;
            }
            data.add(res); // result

            data.add(whiteRating);
            data.add("W");
            data.add(blackName);
            data.add(blackRating);

        }// i'm white
        else {
            String res = "";
            if (status.equals("0")) {
                if (color.equals("1"))// black wins
                    res = "+";
                else
                    res = "-";
            }// end status 0
            if (status.equals("1"))
                res = "=";
            if (status.equals("3"))
                res = "a";
            if (channels.fics) {
                res = status;
            }
            data.add(res); // result

            data.add(blackRating);
            data.add("B");
            data.add(whiteName);
            data.add(whiteRating);

        }// i'm black
        if (rated.equals("1"))
            rated = "r";
        else
            rated = "u";

/*0=wild, 1=blitz, 2=standard,
	3=bullet, 4=bughouse*/
        ratedType = getRatedType(ratedType, wild);


        data.add("" + whitetime + " " + whiteinc + " [" + ratedType + " " + rated + "]");
        data.add(eco);
        if (channels.fics) {
            data.add(mode);
        } else {
            data.add(getGameEndCode(status, mode, color));
        }

        data.add(date + " " + time);
        myTable.gamedata.addTableRow(data);
    }// end add history


    void addSearchLiblistRow(String index, String whiteName, String blackName, String whiteRating, String blackRating, String date, String time, String whitetime, String whiteinc,
                             String rated, String ratedType, String wild, String eco, String status, String color, String mode, String libnote, tableClass myTable) {
        Vector<String> data = new Vector();
        if (index.length() == 1)
            data.add("0" + index);
        else
            data.add(index);

        data.add(whiteName);
        data.add(whiteRating);
        data.add(blackName);
        data.add(blackRating);
        String myName = myTable.type2;

/*Status is 0=win 1=draw 2=adjourned 3=abort.
	Color is 0=black 1=white, and is the side that lost (for status 0) or maybe acted.
*/

// my rating my color vs opponent and opponetns rating.
/*if(myName.equals(whiteName))
{
String res = "";

if(status.equals("0"))
{if(color.equals("0"))// white wins
res="1-0";
else
res="0-1";
}// end status 0

if(status.equals("1"))
res="=";
if(status.equals("3"))
res="a";
data.add(res + " " + getGameEndCode(status, mode, color)); // result
}// i'm white
else
{
*/
        String res = "";

        if (status.equals("0")) {
            if (color.equals("1"))// black wins
                res = "0-1";
            else
                res = "1-0";
        }// end status 0

        if (status.equals("1"))
            res = "=";
        if (status.equals("3"))
            res = "a";
        if (channels.fics) {
            data.add(status);
            data.add(mode);
        } else {
            data.add(res + " "/* + getGameEndCode(status, mode, color)*/); // result
        }


//}// i'm black


        if (rated.equals("1"))
            rated = "r";
        else
            rated = "u";

/*0=wild, 1=blitz, 2=standard,
	3=bullet, 4=bughouse*/
        ratedType = getRatedType(ratedType, wild);
        if (channels.fics) {
            data.add(time);
        } else {
            data.add("" + whitetime + " " + whiteinc + " [" + ratedType + " " + rated + "]");
        }
        if (channels.fics) {
            data.add(color);
        } else {
            data.add(eco);
        }


        if (!channels.fics) {
            data.add(date + " " + time);
            if (myTable.type1.equals("liblist"))
                data.add(libnote);
        }


        myTable.gamedata.addTableRow(data);
    }// end add history

    String getGameEndCode(String s, String m, String c) {
        int status;
        int mode;
        int color;

        try {

            status = Integer.parseInt(s);
            mode = Integer.parseInt(m);
            color = Integer.parseInt(c);
        } catch (Exception badnumbers) {
            return "??";
        }

        String disconnect = "BQ";
        if (color == 1)
            disconnect = "WQ";

        if (status == 0) // status 0 clause
        {
            if (mode == 0)
                return "Res";
            if (mode == 1)
                return "Mat";
            if (mode == 2)
                return "Fla";
            if (mode == 3)
                return "Adj";
            if (mode == 4)
                return disconnect;
            if (mode == 5)
                return disconnect;
            if (mode == 6)
                return disconnect;
            if (mode == 7)
                return "Res";
            if (mode == 8)
                return "Mat";
            if (mode == 9)
                return "Fla";
            if (mode == 10)
                return disconnect;
            if (mode == 11)
                return disconnect;
            if (mode == 12)
                return "1-0";


        }

        if (status == 1) // status 1 clause
        {
            if (mode == 0)
                return "Agr";
            if (mode == 1)
                return "Sta";
            if (mode == 2)
                return "Rep";
            if (mode == 3)
                return "50";
            if (mode == 4)
                return "TM";
            if (mode == 5)
                return "NM";
            if (mode == 6)
                return "NT";
            if (mode == 7)
                return "Adj";
            if (mode == 8)
                return "Agr";
            if (mode == 9)
                return "NT";
            if (mode == 10)
                return "1/2";
        }

        if (status == 2) // status 2 clause
        {
                /*
                mode 0:  (?)   Game adjourned by mutual agreement
		mode 1:  (?)   Game adjourned when Black disconnected
		mode 2:  (?)   Game adjourned by system shutdown
		mode 3:  (?)   Game courtesyadjourned by Black
		mode 4:  (?)   Game adjourned by an administrator
		mode 5:  (?)   Game adjourned when Black got disconnected

                  */
            String dqcolor = "W";
            if (color == 0)
                dqcolor = "B";

            if (mode == 0)
                return "Mu-Ag";
            if (mode == 1)
                return dqcolor + "-Dis";
            if (mode == 2)
                return "Shutdown";
            if (mode == 3)
                return "Curtesy - " + dqcolor;
            if (mode == 4)
                return "Admin- Adjourn";
            if (mode == 5)
                return dqcolor + "-Dis";
        }


        if (status == 3) // status 3 clause
        {
            if (mode == 0)
                return "Agr";
            if (mode == 1 && color == 0)
                return "BA";
            if (mode == 1 && color == 1)
                return "WA";

            if (mode == 2)
                return "SD";
            if (mode == 3 && color == 0)
                return "BA";
            if (mode == 3 && color == 1)
                return "WA";
            if (mode == 4)
                return "Adj";
            if (mode == 5)
                return "Sho";
            if (mode == 6)
                return disconnect;
            if (mode == 7)
                return "Sho";
            if (mode == 8)
                return "Sho";
            if (mode == 9)
                return "Sho";
            if (mode == 10)
                return "Adj";
            if (mode == 11)
                return disconnect;
            if (mode == 12)
                return "?";


        }


        return "??";
    }


    String getRatedType(String ratedType, String wild) {
        if (ratedType.equals("0"))
            return "w" + wild;
        else if (ratedType.equals("1"))
            return "blitz";
        else if (ratedType.equals("2"))
            return "standard";
        else if (ratedType.equals("3"))
            return "Bullet";
        else if (ratedType.equals("4"))
            return "bughouse";
        else if (ratedType.equals("5"))// loser's added feb 21 2011
            return "w" + wild;

        else if (ratedType.equals("6"))
            return "w23";
        else if (ratedType.equals("7"))// modified to 5-min feb 21 2011
            return "5-min";
        else if (ratedType.equals("8"))// added feb 21 2011
            return "1-min";
        else if (ratedType.equals("10"))// added feb 21 2011
            return "15-min";
        else if (ratedType.equals("11"))// added feb 21 2011
            return "3-min";
        else if (ratedType.equals("12"))// added feb 21 2011 45 45
            return "w0";

        else if (ratedType.equals("13"))// added feb 21 2011
            return "960";

        return ratedType;

    }


}
