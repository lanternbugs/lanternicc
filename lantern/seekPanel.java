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
import java.util.ArrayList;
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
import java.awt.datatransfer.Clipboard;
import java.lang.reflect.Method;



class seekPanel extends JPanel implements MouseMotionListener, MouseListener
{

int seekHeight;
int blitzSeekW;
int bulletSeekW;
int standardSeekW;
int baseHeightBottom;
int baseHeightTop;
int bulletBaseX;
int blitzBaseX;
int standardBaseX;
int mx;
int my;
Color backColor;
Color seekTextColor;
String seekText;
Font seekTextFont;
int width;
int height;
channels sharedVariables;
ConcurrentLinkedQueue<myoutput> queue;
int displayType;
static int aSeeks = 0;
static int hSeeks = 1;
static int cSeeks = 2;

seekPanel(channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1, int displayType1)
{

sharedVariables=sharedVariables1;
queue=queue1;
displayType=displayType1;
	addMouseMotionListener(this);
	addMouseListener(this);
	mx=0;
	my=0;
	seekText="";
	backColor = new Color(255, 255, 255); //white
	seekTextColor=new Color(0,255,0);
	seekTextFont = new Font("Times New Roman", Font.PLAIN, 20);
	width=height=50;
}

void setDimensions()
{
	bulletBaseX=0;
	blitzBaseX=(int)width/6;
	standardBaseX=(int)width*5/6;

	baseHeightBottom= height -40;
	baseHeightTop=20;
	if(baseHeightBottom<150)
	baseHeightBottom =150;

	if(blitzBaseX < 30)
	{
		blitzBaseX=30;
		standardBaseX=150;
	}

	seekHeight = (int)(baseHeightBottom - baseHeightTop) / sharedVariables.graphData.height;
	bulletSeekW = (int) (blitzBaseX - bulletBaseX) / sharedVariables.graphData.bulletW;
	blitzSeekW = (int) (standardBaseX - blitzBaseX) / sharedVariables.graphData.blitzW;
	standardSeekW = (int) (width - standardBaseX) / sharedVariables.graphData.standardW;
       
        if(bulletSeekW > seekHeight)
        bulletSeekW = seekHeight;
        if(blitzSeekW > seekHeight)
        blitzSeekW = seekHeight;
        if(standardSeekW > seekHeight)
        standardSeekW = seekHeight;


        if(seekHeight >  bulletSeekW && seekHeight >  blitzSeekW && seekHeight >  standardSeekW)
        {
          if(bulletSeekW > blitzSeekW && bulletSeekW > standardSeekW)
          seekHeight = bulletSeekW;
          else if(blitzSeekW > bulletSeekW  && blitzSeekW > standardSeekW)
          seekHeight = blitzSeekW;
          else
          seekHeight = standardSeekW;

        }

}

	public void paintComponent(Graphics g)
		{

		try {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		setBackground(backColor);
		width=getWidth();
		height=getHeight();
		setDimensions();
Color lineColor=new Color(0,0,0);
		// draw your lines no seeks yet
g2.setColor(lineColor);
// x y width height
g2.fill(new Rectangle2D.Double(0, (double) baseHeightBottom, (double) getWidth(), (double) 2));
g2.fill(new Rectangle2D.Double((double) blitzBaseX,  0, 2, (double) baseHeightBottom));
g2.fill(new Rectangle2D.Double((double) standardBaseX,  0, 2, (double) baseHeightBottom));

// draw bullet seeks
int a=0;
for(a=0; a<sharedVariables.graphData.height * sharedVariables.graphData.bulletW; a++)
{
if(sharedVariables.graphData.bulletGrid[a]!=null)
{
	int x= a - ((int) a/sharedVariables.graphData.bulletW) * sharedVariables.graphData.bulletW;
	int y=((int) a/sharedVariables.graphData.bulletW) ;
	if(x<0)
	x=0;
	if(y<0)
	y=0;
	drawSeek(g2, sharedVariables.graphData.bulletGrid[a].col, sharedVariables.graphData.bulletGrid[a].compCol, bulletBaseX, x, y, bulletSeekW, seekHeight, sharedVariables.graphData.bulletGrid[a].rated, sharedVariables.graphData.bulletGrid[a].computer);
}
}// end draw bullet

// draw blitz seeks
a=0;
for(a=0; a<sharedVariables.graphData.height * sharedVariables.graphData.blitzW; a++)
{
if(sharedVariables.graphData.blitzGrid[a]!=null)
{
	int x= a - ((int) a/sharedVariables.graphData.blitzW) * sharedVariables.graphData.blitzW;
	int y=((int) a/sharedVariables.graphData.blitzW) ;
	if(x<0)
	x=0;
	if(y<0)
	y=0;
	drawSeek(g2, sharedVariables.graphData.blitzGrid[a].col, sharedVariables.graphData.blitzGrid[a].compCol, blitzBaseX, x, y, blitzSeekW, seekHeight, sharedVariables.graphData.blitzGrid[a].rated, sharedVariables.graphData.blitzGrid[a].computer);
}
}// end draw blitz seeks

// draw standard seeks
a=0;
for(a=0; a<sharedVariables.graphData.height * sharedVariables.graphData.standardW; a++)
{
if(sharedVariables.graphData.standardGrid[a]!=null)
{
	int x= a - ((int) a/sharedVariables.graphData.standardW) * sharedVariables.graphData.standardW;
	int y=((int) a/sharedVariables.graphData.standardW) ;
	if(x<0)
	x=0;
	if(y<0)
	y=0;
	drawSeek(g2, sharedVariables.graphData.standardGrid[a].col, sharedVariables.graphData.standardGrid[a].compCol, standardBaseX, x, y, standardSeekW, seekHeight, sharedVariables.graphData.standardGrid[a].rated, sharedVariables.graphData.standardGrid[a].computer);
}
}// end draw standard
g2.setColor(seekTextColor);
g2.setFont(seekTextFont);
int local = (int)width/ 2 - 100;
if(local < 0)
local=0;
g.drawString(seekText, local, baseHeightBottom+30);
		}// end try
		catch(Exception badPaint){}
}// end method paint components


void drawSeek(Graphics2D g2, Color col, Color compColor, int originX, int x, int y, int width, int height, String rated, boolean computer)
{


try {
if(computer == true && displayType != cSeeks && displayType != aSeeks)
return;
if(computer == false && displayType != hSeeks && displayType != aSeeks)
return;

	g2.setColor(col);
// x y width height
if(compColor == null)
{ if(computer == true)
   g2.fill(new Rectangle2D.Double(originX + (x * width), (double) baseHeightBottom - y * height, (double) width-1, (double) height-1));
	else
g2.fill(new Ellipse2D.Double(originX + (x * width), (double) baseHeightBottom - y * height, (double) width-1, (double) height-1));
}
else // 2 halves
{
g2.fill(new Rectangle2D.Double(originX + (x * width), (double) baseHeightBottom - y * height, (double) width-1, (double) height/2));
g2.setColor(compColor);
g2.fill(new Rectangle2D.Double(originX + (x * width), (double) baseHeightBottom - y * height + height/2, (double) width-1, (double) height/2-1));


}

if(rated.equals("u") && compColor == null)
{g2.setColor(backColor);//  background
if(computer == true)
g2.fill(new Rectangle2D.Double(originX + (x * width)+2, (double) baseHeightBottom - y * height + 2, (double) width-5, (double) height-5));
else
g2.fill(new Ellipse2D.Double(originX + (x * width)+2, (double) baseHeightBottom - y * height + 2, (double) width-5, (double) height-5));
}
else if(rated.equals("u") && compColor != null)
{g2.setColor(backColor);//  background
g2.fill(new Rectangle2D.Double(originX + (x * width)+2, (double) baseHeightBottom - y * height + 2, (double) width-5, (double) height-6));
}


}
catch(Exception baddraw){}
}


seekInfo getHoverOver()
{
if(my > baseHeightBottom + seekHeight)
return null;


try{
if(mx<blitzBaseX)
{
	int x=(int)mx/bulletSeekW;
	int y=(int)(baseHeightBottom-my)/seekHeight + 1;
	if(my > baseHeightBottom)
	y=0;
	return sharedVariables.graphData.bulletGrid[x + y * sharedVariables.graphData.bulletW];
}
else if((mx>blitzBaseX) && mx < (standardBaseX))
{
	int x=(int)(mx-(blitzBaseX))/blitzSeekW;
	if(x<0)
	x=0;
	int y=(int)(baseHeightBottom-my)/seekHeight + 1;
	if(my > baseHeightBottom)
	y=0;
	return sharedVariables.graphData.blitzGrid[x + y * sharedVariables.graphData.blitzW];
}
else
{
	int x=(int)(mx-(standardBaseX))/standardSeekW;
	if(x<0)
	x=0;
	int y=(int)(baseHeightBottom-my)/seekHeight + 1;
	if(my > baseHeightBottom)
	y=0;
	return sharedVariables.graphData.standardGrid[x + y * sharedVariables.graphData.standardW];

}
}
catch(Exception badHover){}


return null;
}

 void eventOutput(String eventDescription, MouseEvent e) {


	      mx=e.getX();
	      my=e.getY();

	      seekInfo over= getHoverOver();
	      
	      if(over!= null) // check if we look at these seeks
	      {
                 if(over.computer == true && displayType != cSeeks && displayType != aSeeks)
                 return;
                 if(over.computer == false && displayType != hSeeks && displayType != aSeeks)
                 return;
                }

	      if(over!=null)
	      {
		/*JFrame fr = new JFrame();
		fr.setSize(150, 50);
		fr.setVisible(true);
		fr.setTitle(over.seekText);
		*/
		seekText=over.seekText;
		if(over.computer == false && over.wild.equals("0"))
			seekTextColor=new Color(0,0,0);
		else
			seekTextColor=over.col;
	  	repaint();
	  	}
		  else if(!seekText.equals(""))
		{
			seekText="";
		repaint();
		}
}

	    public void mouseMoved(MouseEvent e) {
	        eventOutput("Mouse moved", e);
	    }

	    public void mouseDragged(MouseEvent e) {
	        eventOutput("Mouse dragged", e);
	    }
	    public void mousePressed(MouseEvent e) {
		}

	 public void mouseEntered (MouseEvent me) {}

	 public void mouseReleased (MouseEvent me) {
		}

	 public void mouseExited (MouseEvent me) {}
	public void mouseClicked (MouseEvent me) {
			      seekInfo over= getHoverOver();

	      if(over!= null) // check if we look at these seeks
	      {
                 if(over.computer == true && displayType != cSeeks && displayType != aSeeks)
                 return;
                 if(over.computer == false && displayType != hSeeks && displayType != aSeeks)
                 return;
                }

			      if(over!=null)
			      {
				/*JFrame fr = new JFrame();
				fr.setSize(150, 50);
				fr.setVisible(true);
				fr.setTitle(over.seekText);
				*/
					String play = "play " + over.index + "\n";
					myoutput temp =new myoutput();
					temp.data=play;
					temp.consoleNumber=0;
					queue.add(temp);
			  	}


		}
void setDisplayType(int n)
{
 displayType=n;
 repaint();
}
}// end jpanel class


