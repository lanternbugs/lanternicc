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
import java.util.ArrayList;
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
import java.applet.*;
import java.awt.event.*;
import java.util.StringTokenizer;
import javax.swing.table.*;
import java.util.Vector;
import java.util.StringTokenizer;

class pgnLoader {
int STATE_NONE=0;
int STATE_TAGS=1;
int STATE_MOVES=2;
int state;
ArrayList<pgnData> games = new ArrayList<pgnData>();
String title;

pgnLoader(String fileName){
title=fileName;
state = STATE_NONE;
int count = -1;
pgnData mygame= new pgnData();

		try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      BufferedReader input=null;

      try {
		  	input=  new BufferedReader(new FileReader(fileName));
		  }
      catch(Exception ee)
      {return; }  // end outer catch



      try {
        String line = null; //not declared within while loop
        /*
        * readLine is a bit quirky :
        * it returns the content of a line MINUS the newline.
        * it returns null only for the END of the stream.
        * it returns an empty String if two newlines appear in a row.
        */
        while (( line = input.readLine()) != null){
         {
			 // read pgn here
			 if(line == "")
			 	continue;

			 if(line.startsWith("[") && state != STATE_TAGS)
			 {
				 if(state == STATE_MOVES)
				 games.add(mygame);

				 mygame= new pgnData();
				 state=STATE_TAGS;
			 }
			 if(line.startsWith("[") && state == STATE_TAGS)
			 {
				 if(line.startsWith("[White "))
				 	mygame.whiteName=getPgnArg(line);
				 if(line.startsWith("[Black "))
				 	mygame.blackName=getPgnArg(line);
				 if(line.startsWith("[Result"))
				 	mygame.result=getPgnArg(line);
				 if(line.startsWith("[Event "))
				 	mygame.event=getPgnArg(line);
				 if(line.startsWith("[Site "))
				 	mygame.site=getPgnArg(line);
				 if(line.startsWith("[Date"))
				 	mygame.date=getPgnArg(line);
				 if(line.startsWith("[WhiteElo "))
				 	mygame.whiteElo=getPgnArg(line);
				 if(line.startsWith("[BlackElo "))
				 	mygame.blackElo=getPgnArg(line);
				 if(line.startsWith("[ECO"))
				 	mygame.eco=getPgnArg(line);


			 }

			 if(line.startsWith("1."))
			 	state=STATE_MOVES;

			if(state == STATE_MOVES)
				addMoves(line, mygame);
		 }

        }
      }
    catch (IOException ex){     }
    finally
    {
		if(state == STATE_MOVES)// to catch the last game, since we normally add game in above loop when next game starts
		   games.add(mygame);
        input.close();
    }// end finally
}// overall try
catch(Exception eeee)
{ }// overall catch


}


String getPgnArg(String line)
{

try {

	int a=0, b=0;

	a=line.indexOf("\"");
	b=line.indexOf("\"", a+1);
	return line.substring(a+1, b);
	}
	catch(Exception d){}


	return "";
}


void addMoves(String line, pgnData mygame){

boolean go = true;
   do {
 	int a=line.indexOf("{");
	try {

		if(a >=0)
	{

		int b=line.indexOf("}");
		if(b>=0)
		{
			String temp = line.substring(a, b+1);
			line = line.replace(temp, "");
			go=true;
			continue;
		}
	}

}catch(Exception d){}
go=false;
}

while(go==true);

   StringTokenizer tokens = new StringTokenizer(line, " ");

    String item = "Start";
	try {
    while(!item.equals(""))
    {
	item = tokens.nextToken();
	if(!item.equals(""))
	{
		if(item.startsWith("{"))
			continue;
		if(item.contains("."))
			continue;
		mygame.moves.add(item);
	}// end if
	}// end while
}catch(Exception d){}



}





	class pgnData {
		ArrayList<String> moves = new ArrayList<String>();
		String whiteName;
		String blackName;
		String result;
		String whiteElo;
		String blackElo;
		String eco;
		String event;
		String site;
		String date;

}

void loadTable(tableClass myTable)
{

for(int a=0; a< games.size(); a++)
{
Vector<String> data = new Vector();
data.add("" + a);
data.add(games.get(a).whiteName);
data.add(games.get(a).whiteElo);
data.add(games.get(a).blackName);
data.add(games.get(a).blackElo);
data.add(games.get(a).result);
data.add(games.get(a).eco);
data.add(games.get(a).event);
data.add(games.get(a).site);
data.add(games.get(a).date);

myTable.gamedata.addTableRow(data);


}// end for
}


}