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

class parseString {

String text1;
String text2;
String text3;
int start1;
int stop1;
int start2;
int stop2;
int start3;
int stop3;
int start;
int stop;
// we track the absolute characters indexes of each part of the tell
int itsLink;
int spaceLink;
parseString(int startPass, int stopPass)
{
	text1="";
	text2="";
	text3="";
	itsLink=0;
	spaceLink=0;
	start1=start2=start3=stop1=stop2=stop3=0;
        start=startPass;
        stop=stopPass;
}

int getHyperIndex(String s)
{

	boolean multispace=false;

	s = s.toLowerCase();
	int index=-1;

	index=s.indexOf("http://");
	if(index > -1)
	return index;

	index=s.indexOf("https://");
	if(index > -1)
	return index;

	index=s.indexOf("www.");
	if(index > -1)
	{
          int index2=s.indexOf("www..");
          if(index2!=index)
          return index;
        }



/*	index=s.indexOf("\"observe");

	if(index > -1)
	{
		int j=s.indexOf("\"", index + 1);
		int k=s.indexOf(" ", index + 1);

		int go=0;
		if(j < k || (k==-1 && j !=-1))
		{
			if(j!=-1)
			go=1;
		}
		if(go==1)
		return index;
	}

*/

/*	index=s.indexOf("\"observe");
	if(index > -1)
	{
		int j=s.indexOf("\"", index + 1);
		int k=s.indexOf(" ", index + 1);

		int go=0;
		if(j > k && k!=-1)
		{
			if(k+1 < s.length())
			{int m=s.indexOf(" ", k+1);
			if(m>j || m == -1)
			go=1;
			}
			else
			go=1;

		}
		if(go==1)
		{
			spaceLink=1;
			return index;

		}
	}

	index=s.indexOf("'observe");




	if(index > -1)
	{
		int j=s.indexOf("'", index + 1);
		int k=s.indexOf(" ", index + 1);

		int go=0;
		if(j > k && k!=-1)
		{
			if(k+1 < s.length())
			{int m=s.indexOf(" ", k+1);
			if(m>j || m == -1)
			go=1;
			}
			else
			go=1;

		}
		if(go==1)
		{
			spaceLink=1;
			return index;

		}
	}
*/
	index=s.indexOf("\"finger ");
	if(index != -1)// we have finger
		multispace = true;

	if(index == -1)
	{index=s.indexOf("\"examine ");
	if(index > -1)
        multispace=true;
	}
	if(index == -1)
	index=s.indexOf("\"observe ");
	if(index == -1)
	index=s.indexOf("\"help ");
	if(index == -1)
	index=s.indexOf("\"play ");
	if(index == -1)
	index=s.indexOf("\"follow ");
	if(index == -1)
	index=s.indexOf("\"decline ");
	if(index == -1)
	index=s.indexOf("\"accept ");
	if(index == -1)
	index=s.indexOf("\"match ");
	if(index == -1)
	index=s.indexOf("\"liblist ");

	if(index == -1)
	{index=s.indexOf("\"/finger ");
	if(index != -1)// we have finger
		multispace = true;
	}
	if(index == -1)
	{index=s.indexOf("\"/examine ");
	if( index > -1)
        multispace=true;
	}
	if(index == -1)
	index=s.indexOf("\"/observe ");
	if(index == -1)
	index=s.indexOf("\"/help ");
	if(index == -1)
	index=s.indexOf("\"/play ");
	if(index == -1)
	index=s.indexOf("\"/follow ");
	if(index == -1)
	index=s.indexOf("\"/decline ");
	if(index == -1)
	index=s.indexOf("\"/accept ");
	if(index == -1)
	index=s.indexOf("\"/match ");
	if(index == -1)
	index=s.indexOf("\"/liblist ");





	if(index > -1)
	{
		int j=s.indexOf("\"", index + 1);
		int k=s.indexOf(" ", index + 1);
                if(k==index+1)
                k++;

                int kk =0;
                while(kk != -1)// travel through consecutive spaces
                {
                  if(k+1 < s.length())
                  { kk=s.indexOf(" ", k + 1);
                        if(kk==k+1)
                        k++;
                        else
                        kk=-1;
                  }
                  else
                  kk=-1;

                }   // end while
		int go=0;
		if(j > k && k!=-1)
		{
			if(multispace == true)
			go=1;

			if(k+1 < s.length())
			{int m=s.indexOf(" ", k+1);
			if(m>j || m == -1)
			go=1;
			}
			else
			go=1;

		}
		if(go==1)
		{
			spaceLink=1;
			return index;

		}
	}

	index=s.indexOf("'finger ");
	if(index > -1)
		multispace = true;
	if(index == -1)
	{
		index=s.indexOf("'examine ");
		if(index > -1)
                multispace = true;
	}
	if(index == -1)
	index=s.indexOf("'observe ");
	if(index == -1)
	index=s.indexOf("'help ");
	if(index == -1)
	index=s.indexOf("'play ");
	if(index == -1)
	index=s.indexOf("'follow ");
	if(index == -1)
	index=s.indexOf("'decline ");
	if(index == -1)
	index=s.indexOf("'accept ");
	if(index == -1)
	index=s.indexOf("'match ");
	if(index == -1)
	index=s.indexOf("'liblist ");

	if(index == -1)
	{index=s.indexOf("'/finger ");
	if(index > -1)
		multispace = true;
	}
	if(index == -1)
	{
		index=s.indexOf("'/examine ");
		if(index > -1)
                multispace = true;
	}
	if(index == -1)
	index=s.indexOf("'/observe ");
	if(index == -1)
	index=s.indexOf("'/help ");
	if(index == -1)
	index=s.indexOf("'/play ");
	if(index == -1)
	index=s.indexOf("'/follow ");
	if(index == -1)
	index=s.indexOf("'/decline ");
	if(index == -1)
	index=s.indexOf("'/accept ");
	if(index == -1)
	index=s.indexOf("'/match ");
	if(index == -1)
	index=s.indexOf("'/liblist ");




	if(index > -1)
	{
		int j=s.indexOf("'", index + 1);
		int k=s.indexOf(" ", index + 1);
                 if(k==index+1)
                k++;

                int kk =0;
                while(kk != -1)// travel through consecutive spaces
                {
                  if(k+1 < s.length())
                  { kk=s.indexOf(" ", k + 1);
                        if(kk==k+1)
                        k++;
                        else
                        kk=-1;
                  }
                  else
                  kk=-1;

                }   // end while

		int go=0;
		if(j > k && k!=-1)
		{
			if(multispace == true)
			go=1;

			if(k+1 < s.length())
			{int m=s.indexOf(" ", k+1);
			if(m>j || m == -1)
			go=1;
			}
			else
			go=1;

		}
		if(go==1)
		{
			spaceLink=1;
			return index;

		}
	}

	return -1;

}// end method


void parse(String s)
{
	int i=getHyperIndex(s);
	int j=0;

	if(i== -1)
	{
		text1=s;
		itsLink=0;

		return;
	}

	itsLink=1;
	if(i==0)// hyperlink is at start of string/// this case doesnt happen in tells
	{
		j=s.indexOf(" ");

		//  we'll stop  at ) if it comes  before ' '
		int k=s.indexOf(")");
		if(k < j && k != -1)
		j=k;
		// parsing out nonsense at end
		if(j > 0)
		{
			if(s.charAt(j-1) == '.' || s.charAt(j-1) == '"' || s.charAt(j-1) == ',' || s.charAt(j-1) == '\'')
			j--;

		}
		if(j > -1)
		{
			text1="";
			text2=s.substring(0, j);
			text3=s.substring(j, s.length());
			start2=0 + start;
			stop2=j+start;
			start3=j+start;
			stop3=s.length()+start;
			return;
		}// end if
	}// end if

// whats left? hyperlink must be in middle or end
text1=s.substring(0, i);
j=s.indexOf(" ", i + 1);
if(spaceLink == 0) // we check for  hyperlinks if we can stop at ) before  ' '
{
	int k = s.indexOf(")",  i+1);
	if((k < j && k != -1) || (k >  j && j == -1))
	j=k;


	if(j > 0)
		{
			if(s.charAt(j-1) == '.' || s.charAt(j-1) == '"' || s.charAt(j-1) == ',' || s.charAt(j-1) == '\'')
			j--;

		}

}

if(spaceLink == 1)
{
	int j1=s.indexOf("\"", j+1);
	int j2=s.indexOf("'", j+1);

	if(s.charAt(i) == '"')
	j=j1;
	else if(s.charAt(i) == '\'')
	j=j2;
	else
	j=s.indexOf(" ", j+1);


}

// if its a space link its an icc command


if(j == -1) // its at end
{
	if(spaceLink == 0) // its an icc command
	{

		int end = s.length()-1;
		if(end > 0)
		{
			if(s.charAt(end-1) == '.' || s.charAt(end-1) == '"' || s.charAt(end-1) == ',' || s.charAt(end-1) == '\'')
			end--;

		}

	text2=s.substring(i, end);// data has an enter at end
	text3=s.substring(end, s.length()-1) +"\n";
	start1=start;
	stop1=i+start;
	start2=i+start;
	stop2=end+start;
	start3=end+start;
	stop3=s.length()-1 + 1+start;
    }
    else
    {
text1=s.substring(0, i+1);
start1=0 + start;
stop1=i+1 + start;


int end = s.length()-2;
		if(end > 0)
		{
			if(s.charAt(end-1) == '.' || s.charAt(end-1) == '"' || s.charAt(end-1) == ',' || s.charAt(end-1) == '\'')
			end--;

		}

text2=s.substring(i+1, end);
	text3=s.substring(end, s.length()-1) +"\n";
start2=i+1 + start;
stop2=end + start;
start3=end+start;
stop3=s.length()-1 + 1+start;
	}
	return;
}

// its in middle
if(spaceLink == 0)
{

text2=s.substring(i,j);
text3=s.substring(j, s.length());
start1=0+start;
stop1=i+start;
start2=i+start;
stop2=j+start;
start3=j+start;
stop3=s.length()+ start;
}
else // icc command
{
text1=s.substring(0, i+1);
text2=s.substring(i+1,j);
text3=s.substring(j, s.length());
start1=0+start;
stop1=i+1 + start;
start2=i+1 + start;
stop2=j+start;
start3=j+start;
stop3=s.length() + start;

}



}// end method


}// end parse string class