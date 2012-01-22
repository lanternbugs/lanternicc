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
import javax.swing.text.html.HTML.Attribute.*;
import java.util.StringTokenizer;
import java.lang.reflect.Constructor;
import java.util.Vector;
import free.freechess.*;
import free.util.*;
import java.util.concurrent.locks.*;


class seekInfo {

int eTimeType;// 0 bullet 1 blitz 2 Standard
int gridSpot;
double etime;
int listIndex;
String index;
String name;
String rating;
String wild;
String time;
String inc;
String rated;
String range;
String color;
String formula;
String manual;
Color col;
Color compCol;
String seekText;
boolean computer;
listClass notifyList;
boolean onNotify;

seekInfo(String sIndex, String sName, String sTitles, String sRating, String sProvisional, String sWild, String sRatingType, String sTime, String sInc, String sRated, String sRange, String sColor, String sFormula, String sManual, listClass snotifyList)
{
//col= new Color(255,0,0);
/*
computer: blue
normal: green
wild: red
losers: orange
crazyhouse pink
above are possible values of col
computerCol is null or one of the wild colors if its going to be half and half
*/


computer=false;
try {
	if(sTitles.contains("C"))
computer=true;
}catch(Exception d){}
compCol=null;
notifyList=snotifyList;
try {
if(isNotified(sName))
onNotify=true;
else
onNotify=false;

}
catch(Exception badnot){ onNotify=false;}

if(sWild.equals("0") && computer==true)
col=new Color(0,0,255); // blue
else if(sWild.equals("0") && computer==false)
col=new Color(0,255,0);
else // wild
{
	if(computer == true)
	{
		col=new Color(0,0,255); // blue
		if(sWild.equals("17") || sWild.equals("26"))// losers orange
			compCol= new Color(255,140,0 );
		else if(sWild.equals("23"))// crazyhouse pink
		 	compCol = new Color(255,20,147 );
		 else
		  compCol= new Color(255,0,0);
	}
	else
	{
		if(sWild.equals("17") || sWild.equals("26"))// losers orange
			col= new Color(255,140,0 );
		else if(sWild.equals("23"))// crazyhouse pink
		 	col = new Color(255,20,147 );
		 else
		  col= new Color(255,0,0);

	}
}

String theTitles="";
if(sProvisional.equals("0") || sProvisional.equals("1"))
	sTitles = sTitles + "P";
if(sTitles.length() > 0)
theTitles="("+ sTitles + ")";

String category=sRatingType;
if(sWild.equals("0"))
category=category + " ";
else
category =category + " w" + sWild + " ";


seekText=sName + theTitles + " " + sRating + " " + category + sTime + " " + sInc + " " + sRated + " " + sRange;
if(sManual.equals("m"))
seekText = seekText + " " + sManual;
if(sFormula.equals("f"))
seekText =seekText + " " + sFormula;
if(onNotify == true)
seekText = seekText + " Notified";
index=sIndex;
name=sName;
rating=sRating;
wild=sWild;
time=sTime;
inc=sInc;
rated=sRated;
range=sRange;
color=sColor;
formula=sFormula;
manual=sManual;
listIndex=0;
eTimeType=0;
try {
int iTime = Integer.parseInt(time);
int iInc = Integer.parseInt(inc);
etime = (double) (iTime + (double) 2/3 * iInc);
if(etime >= 3 && etime < 15 )
eTimeType=1;
else if(etime >=15)
eTimeType=2;

}
catch(Exception d){}

}// end constructor



boolean isNotified(String sName)
{

        for(int i=0; i < notifyList.model.size(); i++)
	if(notifyList.modeldata.elementAt(i).equals(sName))
	return true;

	return false;

}//end method is notified
}// end class
