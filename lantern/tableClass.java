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
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Vector;

class tableClass {



public myDefaultTableModel gamedata;
Vector<String> data;
Vector<String> collumns;
String type1;
String type2;
    
    boolean chessFontForMoveList;
    boolean getChessFontForMoveList() { return chessFontForMoveList;}
    void setChessFontForMoveList(boolean val) { chessFontForMoveList = val;}

tableClass()
{
data = new Vector();
collumns = new Vector();

collumns.add("index");
collumns.add("white name");
collumns.add("white rating");
collumns.add("black name");
collumns.add("black rating");
collumns.add("Result");
collumns.add("Time Controls");
collumns.add("ECO");
collumns.add("Date Game Started");
gamedata = new myDefaultTableModel(data, collumns);
type1="none";
type2="none";
chessFontForMoveList = true;
}// end constructor
void createLiblistColumns()
{
data = new Vector();
collumns = new Vector();

collumns.add("index");
collumns.add("white name");
collumns.add("white rating");
collumns.add("black name");
collumns.add("black rating");
collumns.add("Result");
collumns.add("Time Controls");
collumns.add("ECO");
collumns.add("Date Game Started");
collumns.add("Note");
gamedata = new myDefaultTableModel(data, collumns);

}// end constructor

void createOpeningBookColumns()
{
data = new Vector();
collumns = new Vector();

collumns.add("Move");
collumns.add("Wins");
collumns.add("Draws");
collumns.add("Losses");
collumns.add("Percent");
gamedata = new myDefaultTableModel(data, collumns);

}// end constructor


void createHistoryListColumns()
{
Vector<String> data = new Vector();
Vector<String> collumns = new Vector();

collumns.add("Index");
collumns.add("Result");
collumns.add("Rating");
collumns.add("Color");
collumns.add("Opponents Name");
collumns.add("Opponents Rating");
collumns.add("Time Controls");
collumns.add("ECO");
collumns.add("End");
collumns.add("Date Game Started");


gamedata = new myDefaultTableModel(data, collumns);
}

void createPgnListColumns()
{
Vector<String> data = new Vector();
Vector<String> collumns = new Vector();

collumns.add("Index");
collumns.add("White Name");
collumns.add("White Rating");

collumns.add("Black Name");
collumns.add("Black Rating");

collumns.add("Result");
collumns.add("Eco");
collumns.add("Event");
collumns.add("Site");
collumns.add("Date");




gamedata = new myDefaultTableModel(data, collumns);
}




void createMoveListColumns(int wild)
{
Vector<String> data = new Vector();
Vector<String> collumns = new Vector();

collumns.add("No.");
if(wild == 30)
{collumns.add("Black");
collumns.add("Red");
}
else
{
  collumns.add("White");
collumns.add("Black");
}




gamedata = new myDefaultTableModel(data, collumns);
}

void addMove(int moveNum, String move)
{
	// moveNum 0 no moves.  move 1 white move 2 black
    if(chessFontForMoveList) {
        if(moveNum %2 == 0 ) {
            move = move.replace("N", "Z");
            move = move.replace("B", "J");
            move = move.replace("Q", "M");
            move = move.replace("K", "N");
            move = move.replace("R", "L");
            move = move.replace("P", "I");
            move = move.replace("p", "I");
            move = move.replace("Z", "K");
            
        } else {
            move = move.replace("N", "k");
            move = move.replace("B", "j");
            move = move.replace("Q", "m");
            move = move.replace("K", "n");
            move = move.replace("R", "l");
            move = move.replace("P", "i");
            move = move.replace("p", "i");
            
        }
        move = move.replace("@", "O");
    }
	Vector<String> newRow = new Vector();

	if(moveNum %2 == 1)
	{
		newRow.add("" +(int) (moveNum/2 + 1));
		newRow.add(move);
		newRow.add("");
		gamedata.addRow(newRow);

	}
	else // black insert
	{	gamedata.setValueAt(move, (int) (moveNum/2 ) -1, 2);
		//String whitemove = (String) gamedata.getValueAt((int) (moveNum/2 ) -1 , 1);
		//newRow.add("" + (int) (moveNum/2 + 1));
		//newRow.add(whitemove);
		//newRow.add(move);
		//gamedata.insertRow((int) (moveNum/2) -1, newRow);
	}
}
String getMoves()
{
String theMoves = "";
try {
  Vector temp = gamedata.getDataVector();
for(int a=0; a < temp.size(); a++)
{
int row = a;

String rowString =  temp.get(row).toString();
rowString = rowString.replaceFirst(",", ".");
rowString = rowString.replace("[", "");
rowString = rowString.replace("]", "");
rowString = rowString.replace(",", "");
rowString += " ";
    if(chessFontForMoveList) {
        
            rowString = rowString.replace("N", "Z");
            rowString = rowString.replace("K", "N");
            rowString = rowString.replace("J", "B");
            rowString = rowString.replace("M", "Q");
            rowString = rowString.replace("L", "R");
            rowString = rowString.replace("I", "p");
            rowString = rowString.replace("Z", "K");
            
      
            rowString = rowString.replace("n", "z");
            rowString = rowString.replace("k", "N");
            rowString = rowString.replace("j", "B");
            rowString = rowString.replace("m", "Q");
            rowString = rowString.replace("l", "R");
            rowString = rowString.replace("i", "p");
            rowString = rowString.replace("z", "K");
            
       
    }

theMoves += rowString;
if(row % 7 == 0 && row < temp.size() - 1 && row != 0)
theMoves +="\r\n";

}// end for
} catch(Exception dui){}

return theMoves;
}// end get moves

void removeMoves(int movetop, int num)
{
	//movetop represnts how many moves are in table before removal

	for(int a=0; a<num; a++)
	{
		removeOneMove(movetop);
		movetop--;
	}
}
void removeOneMove(int movetop)
{
	try {
	if(movetop%2 == 0) // remove a black move
	{

		gamedata.setValueAt("", (int) (movetop/2 ) -1, 2);

	}
	else // whites move removed
	{
		int row = (int) ((movetop + 1)/2 - 1);
		gamedata.removeRow(row);
	}
	}catch(Exception d){}
}
class myDefaultTableModel extends DefaultTableModel
{
	boolean done;
	int count;
	public boolean isCellEditable(int row, int collumn)
	{

		return false;

	}
	myDefaultTableModel(Vector<String> data, Vector<String> collumns)
	{
        super(collumns, 0);
         //user below java 9 and up
       //  super(new Object[][] {((Object[]) a1.toArray())},(Object[]) a2.toArray());
		count=0;
		done=false;

	}
	void addTableRow(Vector<String> a1)
	{
        super.addRow(a1);
	}

}
}// end class
