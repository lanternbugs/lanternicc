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
import java.util.StringTokenizer;
import java.util.concurrent.locks.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.event.ChangeEvent.*;



public class JPaintedGameLabel extends JLabel
{



	protected void paintBorder(Graphics g){
		try {
			super.paintComponent(g);
			int width = getWidth();

		int height = getHeight();
		Graphics2D g2 = (Graphics2D) g;


		super.setBackground(mybackground);

		if(fontType == 1)
		super.setFont(sharedVariables.myTabFont);
		else
		super.setFont(sharedVariables.myFont);

	    g2.setColor(sharedVariables.tabBorderColor);
	    // paint 4 lines
	    g2.draw(new Line2D.Double(0, 0, (double) width, 0)); // across
	    g2.draw(new Line2D.Double(0, (double)height-1, (double) width-1, (double) height-1)); // across bottom
	    g2.draw(new Line2D.Double(0, 0, 0, (double) height)); // down
	    g2.draw(new Line2D.Double((double) width-1, 0, (double) width-1, (double) height-1)); // down right side
}
catch(Exception e)
{}

	  }

	JPaintedGameLabel()
	{
		super();

	}
	JPaintedGameLabel(String title, channels sharedVariables1)
	{
		super(title, (int) CENTER_ALIGNMENT);
		mybackground = new Color(0,0,0);
		sharedVariables=sharedVariables1;
		fontInUse=0;
		fontType=1;
		setFullText(title);
	}
	public void setForeground(Color c)
	{
		try { super.setForeground(c);}
		catch(Exception e){}
	}
	public void setBackground(Color c)
	{
		try {mybackground = c;
		repaint();}
		catch(Exception e){}
	}
	public void setFullText(String s)
	{
		try {super.setText(s);}
		catch(Exception e){}



	}
	public void setText(String s, int num)
	{
                    num=sharedVariables.tabLooking[num];
		try {

			if(num < 0) // 0-4 tabs get icons // now < 0 not < 5 so disabled the setting or using of icons on game tabs now
			{
			if(s.startsWith("O"))
		setIcon(sharedVariables.observeIcon);
			if(s.startsWith("W"))
		setIcon(sharedVariables.wasIcon);
			if(s.startsWith("P"))
		setIcon(sharedVariables.playingIcon);
			if(s.startsWith("E"))
		setIcon(sharedVariables.examiningIcon);
			if(s.startsWith("S"))
		setIcon(sharedVariables.sposIcon);
			if(s.startsWith("G"))
		setIcon(sharedVariables.gameIcon);
			}

		//setIcon(null);


	try {

			if(s.length() < 11)
			setFullText(s);
			else
			super.setText(s.substring(0, 11));

			}// end try
		catch(Exception e)
		{
			try {super.setText(s);}
			catch(Exception e2){}

		}

}// end try
		catch(Exception dd){}


	}
	public void setVisible(boolean statement)
	{
		try {super.setVisible(statement);}
		catch(Exception e){}
	}

	public void setOpaque(boolean statement)
	{
		try {super.setOpaque(statement);}
		catch(Exception e){}
	}



	Color mybackground;

	channels sharedVariables;

	int fontInUse;
	int fontType;
}