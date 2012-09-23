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
import java.util.ArrayList;

class seekGraphData {
ArrayList<seekInfo> seekList;
seekInfo [] bulletGrid;
seekInfo [] blitzGrid;
seekInfo [] standardGrid;
int bulletW=6;
int blitzW=18; // was 40
int standardW=6;
static int height=30;



seekGraphData()
{
	seekList = new ArrayList();
	bulletGrid = new seekInfo[bulletW + height * bulletW];
	blitzGrid = new seekInfo[blitzW + height * blitzW];
	standardGrid = new seekInfo[standardW + height * standardW];

}

static int getHeightAt(int x)
{
return (int) (x * height / 2750) - 1;
  
}


void addSeek(String sIndex, String sName, String sTitles, String sRating, String sProvisional, String sWild, String sRatingType, String sTime, String sInc, String sRated, String sRange, String sColor, String sFormula, String sManual, listClass snotifyList)
{
	seekInfo newInfo = new  seekInfo(sIndex, sName, sTitles, sRating, sProvisional, sWild, sRatingType, sTime, sInc, sRated, sRange, sColor, sFormula, sManual, snotifyList);
	double etime = newInfo.etime;
	int x=0;
	int y=0;
	try {
int rating = Integer.parseInt(sRating);
if(rating < 0)
rating=0;

y=(int) (rating * height / 2750) - 1;
if(y<1)
y=1;
if(y>=height)
y=height-1;

if(newInfo.eTimeType == 0) // bullet
	{
		 x =(int)( etime * (double)bulletW / 3 - 1);
		if(x<0)
		x=0;
		newInfo.gridSpot=getSpot(x,y, newInfo.eTimeType, rating);
		bulletGrid[newInfo.gridSpot]=newInfo;
	}
	else if(newInfo.eTimeType == 1) // blitz
	{
		 x =(int)( (etime-3) * (double)blitzW / 12 );
		if(x<0)
		x=0;
		newInfo.gridSpot=getSpot(x,y, newInfo.eTimeType, rating);
		blitzGrid[newInfo.gridSpot]=newInfo;

	}
	else // standard
	{
	//	if((etime - 15) / 5  + 1  > standardW)
	//	etime=100;
		x =(int)( (etime-15)/4);
		if(x >= standardW)
		x=standardW -1;
		newInfo.gridSpot=getSpot(x,y, newInfo.eTimeType, rating);
		standardGrid[newInfo.gridSpot]=newInfo;

	}


seekList.add(newInfo);




}catch(Exception dummy){}


}



int getSpot(int x, int y, int type, int rating)
{
int spot=0;
int occupiedRating=0;

if(type == 0) // bullet
{
spot=x + y * bulletW;
if(bulletGrid[spot]!=null)
{
	boolean go=false;
	try { occupiedRating = Integer.parseInt(bulletGrid[spot].rating); } catch(Exception duii){}
	if(occupiedRating < rating)
        for(int a=1; spot + bulletW * a < bulletW * height; a++)
	{
		if(bulletGrid[spot + a * bulletW] == null)
		{
			spot=spot+a*bulletW;
			return spot;
		}
	}

	for(int a=1; spot - bulletW * a >=0; a--)
	{
		if(bulletGrid[spot - a * bulletW] == null)
		{
			spot=spot-a*bulletW;
			return spot;
		}
	}
	for(int a=1; spot + bulletW * a < bulletW * height; a++)
	{
		if(bulletGrid[spot + a * bulletW] == null)
		{
			spot=spot+a*bulletW;
			return spot;
		}
	}

	return 0;

}
return spot;
}// end type ==0


if(type == 1) // blitz
{
spot=x + y * blitzW;
if(blitzGrid[spot]!=null)
{
	boolean go=false;
	try { occupiedRating = Integer.parseInt(blitzGrid[spot].rating); } catch(Exception duii){}
	if(occupiedRating < rating)
	for(int a=1; spot + blitzW * a < blitzW * height; a++)
	{
		if(blitzGrid[spot + a * blitzW] == null)
		{
			spot=spot+a*blitzW;
			return spot;
		}
	}

	for(int a=1; spot - blitzW * a >=0; a--)
	{
		if(blitzGrid[spot - a * blitzW] == null)
		{
			spot=spot-a*blitzW;
			return spot;
		}
	}
	for(int a=1; spot + blitzW * a < blitzW * height; a++)
	{
		if(blitzGrid[spot + a * blitzW] == null)
		{
			spot=spot+a*blitzW;
			return spot;
		}
	}

	return 0;

}

return spot;
}// end type ==0


if(type == 2) // standard
{
spot=x + y * standardW;
if(standardGrid[spot]!=null)
{
	boolean go=false;
	try { occupiedRating = Integer.parseInt(standardGrid[spot].rating); } catch(Exception duii){}
	if(occupiedRating < rating)
	for(int a=1; spot + standardW * a < standardW * height; a++)
	{
		if(standardGrid[spot + a * standardW] == null)
		{
			spot=spot+a*standardW;
			return spot;
		}
	}

	for(int a=1; spot - standardW * a >=0; a--)
	{
		if(standardGrid[spot - a * standardW] == null)
		{
			spot=spot-a*standardW;
			return spot;
		}
	}
	for(int a=1; spot + standardW * a < standardW * height; a++)
	{
		if(standardGrid[spot + a * standardW] == null)
		{
			spot=spot+a*standardW;
			return spot;
		}
	}

	return 0;

}
return spot;
}// end type ==0



return 0;

}

void removeSeek(String index)
{
	try {
	for(int a=0; a<seekList.size(); a++)
	{
		if(seekList.get(a).index.equals(index))
		{

			int eTimeType=seekList.get(a).eTimeType;
			int gridSpot=seekList.get(a).gridSpot;
			seekList.remove(a);
			if(eTimeType==0)
			bulletGrid[gridSpot]=null;
			else if(eTimeType ==1)
			blitzGrid[gridSpot]=null;
			else
			standardGrid[gridSpot]=null;
			break;
		} // end if

	}// end for
}// end try
catch(Exception dumb){}
}// end method remove


} // end class