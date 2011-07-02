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
import java.util.Random;
import java.awt.*;
import java.awt.Window.*;
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
import java.util.Timer;
import java.util.TimerTask;

/*

public class connectFour extends JApplet
{
//public static void main(String[] args)
//{


public void init()

{
fourFrame frame = new fourFrame();
frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

frame.setSize(500,500);
//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
frame.setTitle("Connect Four");
frame.setVisible(true);
frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);



}// end main class


}// end main class
*/

class connectFour extends JInternalFrame implements ActionListener
{

int[] xtops;
int maxX;
int maxY;
int xhit, yhit;
int [] myboard;
int gridXpoint;
int gridYpoint;
int gridWidth;
int gridHeight;
int myX;
int myY;
fourclass fourgame;
int turntype;
myparameters parm;
JMenuItem  newgame;
JCheckBoxMenuItem diff1;
JCheckBoxMenuItem diff2;
JCheckBoxMenuItem diff3;
JCheckBoxMenuItem diff4;

class myparameters
{
public int turn;
public int noDraw;
public int gameover;
public int lastx;
public int lasty;
public myparameters()
{
turn=0;
noDraw=1;
gameover=0;
lastx=-1;
lasty=-1;
}
}


connectFour()
{


super("Connect Four",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable

setSize(500,500);
//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
setTitle("Connect Four");
setVisible(true);
setDefaultCloseOperation(DISPOSE_ON_CLOSE);

parm = new myparameters();

maxX=7;
maxY=6;
parm.turn=0;
turntype=0;
myX=0;
myY=0;
gridXpoint = 80;
gridYpoint = 50;
gridWidth = 40;
gridHeight = 40;
parm.noDraw=1;
myboard = new int[64];// able to handle 8 by 8 and 7 by 6
for( int b=0; b< maxX * maxY; b++)
   myboard[b] =0;


xtops = new int[8];
for(int a = 0; a < maxX; a++)
   xtops[a]=0;
fourgame = new fourclass();


fourPanel panel = new fourPanel();
add(panel);


/************  Menu *************/
 JMenuBar menu = new JMenuBar();
   JMenu file = new JMenu("File");
   newgame = new JMenuItem("New Game");
    file.add(newgame);
    newgame.addActionListener(this);
    menu.add(file);

  JMenu diff = new JMenu("Difficulty");
  diff1 = new JCheckBoxMenuItem("Normal");
     diff.add(diff1);
     diff1.addActionListener(this);


  diff2 = new JCheckBoxMenuItem("Moderate");
     diff.add(diff2);
     diff2.addActionListener(this);


  diff3 = new JCheckBoxMenuItem("Hard");
     diff.add(diff3);
     diff3.addActionListener(this);


  diff4 = new JCheckBoxMenuItem("Difficult");
     diff.add(diff4);
     diff4.addActionListener(this);



    menu.add(diff);


 setJMenuBar(menu);
diff1.setSelected(true);
}// end constructor

public void actionPerformed(ActionEvent event)
{

if(event.getActionCommand().equals("New Game"))
{


			// newgame
			if(parm.noDraw == 1)
			{
				maxX=7;
				maxY=6;
				parm.turn=0;
				for(int a =0; a< 8; a++)
					xtops[a]=0;
				for(int a=0; a< 64; a++)
					myboard[a]=0;
				parm.gameover=0;
				parm.lastx=-1;
				parm.lasty=-1;
				repaint();
				 fourgame.reset();
				fourgame.type=0;

				// now toggle side to move
				if(turntype == 0)
				{
					turntype = 1;

					// we feed them maxX instead of a move so the move is off the playable board
	                    	mythreadobject runthis= new mythreadobject( maxX,  maxY,  xtops, parm, fourgame, myboard, xhit, gridXpoint, gridYpoint, gridHeight, gridWidth);

                                    Thread t1 = new Thread(runthis);
                                    t1.start();
				}
				else
				{
					turntype=0;
				}
			}
}// end if new game
if(event.getActionCommand().equals("Normal"))
{
	setDifficultyCheck(0);
}// end normal

if(event.getActionCommand().equals("Moderate"))
{
	setDifficultyCheck(1);
}

if(event.getActionCommand().equals("Hard"))
{
setDifficultyCheck(2);
}

if(event.getActionCommand().equals("Difficult"))
{
	setDifficultyCheck(3);
}

}
// end action performed method

void setDifficultyCheck(int n)
{
	if(n == 0)
	{
		fourgame.level=1;
		diff1.setSelected(true);
		diff2.setSelected(false);
		diff3.setSelected(false);
		diff4.setSelected(false);

	}


	if(n == 1)
	{
		fourgame.level=2;
		diff1.setSelected(false);
		diff2.setSelected(true);
		diff3.setSelected(false);
		diff4.setSelected(false);

	}
	if(n == 2)
	{
		fourgame.level=3;
		diff1.setSelected(false);
		diff2.setSelected(false);
		diff3.setSelected(true);
		diff4.setSelected(false);

	}
	if(n == 3)
	{
		fourgame.level=4;
		diff1.setSelected(false);
		diff2.setSelected(false);
		diff3.setSelected(false);
		diff4.setSelected(true);

	}

}


class fourPanel extends JPanel implements MouseMotionListener, MouseListener
{

  int myX;
  int myY;
  int oldmx;
  int oldmy;

fourPanel()
{
 addMouseMotionListener(this);
addMouseListener(this);

}

public void paintComponent(Graphics g2)
{     super.paintComponent(g2);
      Color backColor, red, green, blue, orange, yellow, purple, white, black, colorEmpty;
backColor = new Color(255,255,255);
red = new Color(255,0,0);
green = new Color(0,255,0);
blue = new Color(0,0,255);
yellow = new Color(0,255,255);
purple = new Color(255, 255, 0);
orange = new Color(250, 250, 100);
white = new Color(255, 255, 255);
black = new Color(0, 0, 0);
setBackground(backColor);

colorEmpty = new Color(200,200,200);
Graphics2D g = (Graphics2D) g2;

			g.setColor(blue);
			// draw grid
			for(int a=0; a<maxY + 1; a++)
			g.fill(new Rectangle2D.Double((double)(gridXpoint), (double)(gridYpoint + a * gridHeight), (double)(maxX * gridWidth),(double) 2));
			for(int a=0; a<maxX + 1; a++)
				g.fill(new Rectangle2D.Double((double)(gridXpoint + a * gridWidth),(double)( gridYpoint), 2,(double)( maxY * gridHeight)));
			// draw filled squares
			g.setColor(white);
			for(int a=0; a<maxX; a++)
				for( int b=0; b<maxY; b++)
				{
					if(myboard[b * maxX + a] == 1)
					{   g.setColor(red);
                                          g2.fillOval((int)(gridXpoint + 2 + a * gridWidth),(int)( gridYpoint + 2 + b * gridHeight),(int)( gridWidth - 2), (int)(gridHeight - 2));
					}
                                        if(myboard[b * maxX + a] == 2)
					{g.setColor(blue);
                                          g2.fillOval((int)(gridXpoint + 2 + a * gridWidth),(int)( gridYpoint + 2 + b * gridHeight),(int)( gridWidth - 2),(int)( gridHeight - 2));
				        }
                                if(parm.lastx == a && parm.lasty == b)
				{  g2.setColor(black);
                                  g.fill(new Rectangle2D.Double((double)(gridXpoint + ((int) (gridWidth/2)-3) + a * gridWidth),(double)( gridYpoint + ((int) (gridHeight/2)-3) + b * gridHeight), 6, 6));
				}

                                }
			//g.DrawString(parm.turn + " Mouse " + myY,this.Font, mybrush1, 200, 400);
			g.drawString("Max Depth Reached: " + fourgame.levelreached, 80,(int)( maxY * gridHeight + 80));
			if(parm.gameover == 1)
			g.drawString("Game Over, You Win", 270,(int)( maxY * gridHeight + 80));
			if(parm.gameover == 2)
				g.drawString("Game Over, Computer Wins", 270,(int)( maxY * gridHeight + 80));
			if(parm.gameover == 3)
				g.drawString("Game Over, Draw", 270,(int)( maxY * gridHeight + 80));


}

  int findFour(int x, int token)
		{

			// our rows go 0-6 then 7-13 so we add 1 for the mod checks



			//token is 1 or 2

			int win=0;
			int max=4;
			//if(type==2)
			//	max=5;

			// diagonal down

			int count;

			count=1;
			int a;
			for(a=1; a<max; a++) // up +11 down -9
				if(x + (maxX+1) * a < maxX * maxY && (x+1 + (maxX +1) * a)%maxX !=1)
				{
					if(myboard[x + (maxX+1)*a]==token)
						count++;
					else
						a=max;
				}
				else
					break;

			for(a=1; a<max; a++) // up +11 down -9
				if(x - (maxX+1) * a >= 0 && (x+1 - (maxX +1) * a)%maxX !=0)
				{
					if(myboard[x - (maxX+1)*a]==token)
						count++;
					else
						a=max;
				}
				else
					break;

			if(count >= max)
				win=1;

			count=1;

			for(a=1; a<max; a++) // up +11 down -9
				if(x - (maxX-1) * a >= 0 && (x+1 - (maxX - 1) * a) % maxX !=1)
				{
					if(myboard[x - (maxX-1)*a]==token)
						count++;
					else
						a=max;
				}
				else
					break;

			for(a=1; a<max; a++) // up +11 down -9
				if(x + (maxX-1) * a < maxX * maxY && (x +1 + (maxX - 1) * a)%maxX !=0)
				{
					if(myboard[x + (maxX-1)*a]==token)
						count++;
					else
						a=max;
				}
				else
					break;


			if(count >= max)
				win=1;

			count=1;

			for(a=1; a<max; a++) // up +11 down -9
				if(x + maxX * a < maxX * maxY)
				{
					if(myboard[x + maxX*a]==token)
						count++;
					else
						a=max;
				}
				else
					break;

			for(a=1; a<max; a++) // up +11 down -9
				if(x -maxX * a >= 0)
				{
					if(myboard[x - maxX*a]==token)
						count++;
					else
						a=max;
				}
				else
					break;


			if(count >= max)
				win=1;

			count=1;

			for(a=1; a<max; a++) // up +11 down -9
				if(x -1 * a >=0 && (x + 1 - a) % maxX != 0)
				{
					if(myboard[x - 1*a]==token)
						count++;
					else
						a=max;
				}
				else
					break;

			for(a=1; a<max; a++) // up +11 down -9
				if(x + a < maxX * maxY && (x + 1 + a) % maxX != 1)
				{
					if(myboard[x + 1*a]==token)
						count++;
					else
						a=max;
				}
				else
					break;


			if(count >= max)
				win=1;

			return win;

		}


/*****************************    mouse events *****************************************/

	    void eventOutput(String eventDescription, MouseEvent e) {
	     /* oldmx=mx;
	      oldmy=my;

	      mx=e.getX();
	      my=e.getY();
               */
		}

	    public void mouseMoved(MouseEvent e) {
	        eventOutput("Mouse moved", e);
	    }

	    public void mouseDragged(MouseEvent e) {
	        eventOutput("Mouse dragged", e);
	    }



	    public void mousePressed(MouseEvent e) {
		if(parm.gameover >0)
				return;

			myX = e.getX();
			myY = e.getY();
			int squareX=0;
			int squareY=0;
			int aHit=0;
			 xhit=0;
			yhit=0;
			int a=0, b=0;

			for(a=0; a<maxX; a++)
				for(b=0; b<maxY; b++)
				{

						squareX=gridXpoint + 2 + a * gridWidth;
						squareY=gridYpoint + 2 + b * gridHeight;
					if(myX > squareX && myX < squareX + gridWidth)
						if(myY > squareY && myY < squareY + 40)
						{
							aHit=1;
							xhit=a;
							yhit=b;

							break;

						}
				}
			if(aHit == 1 && xtops[xhit]  < maxY && parm.turn %2== turntype)
			{
				yhit=maxY - (xtops[xhit]) -1; // we no longer require they hit the y square just that there is an available square in that column

				myboard[maxX * yhit + xhit] =2;
				xtops[xhit]++;
				parm.turn ++;
				parm.gameover=findFour(yhit*maxX + xhit, 2);
				if(parm.turn == maxX * maxY && parm.gameover == 0)
					parm.gameover=3;

				myX=xhit;
				myY=yhit;
				repaint();
			/*	if(parm.gameover == 0)
				{
					// set new game to disable
					//menuItem2.Enabled = false;

					thread1.Start();
				}
			*/
			          if(parm.gameover == 0)
            			{
							mythreadobject runthis= new mythreadobject( maxX,  maxY,  xtops, parm, fourgame, myboard, xhit, gridXpoint, gridYpoint, gridHeight, gridWidth);

                                    Thread t1 = new Thread(runthis);
                                    t1.start();
						}// end if game not over

        			}


            }






	public void mouseEntered (MouseEvent me) {
		 }



	public void mouseReleased (MouseEvent me) {
		 }

	 public void mouseExited (MouseEvent me) {
		 }

	public void mouseClicked (MouseEvent me) {
		}
}// end class fourpanel

/********************************* end mouse events **************************************/

	class mythreadobject  implements Runnable
		{

int maxX, maxY, xhit, gridXpoint, gridYpoint, gridHeight, gridWidth;
myparameters parm;
int [] myboard;
int [] xtops;
fourclass fourgame;


			public mythreadobject(int maxX1, int maxY1, int [] xtops1, myparameters parm1, fourclass fourgame1, int [] myboard1, int xhit1, int gridXpoint1, int gridYpoint1, int gridWidth1, int gridHeight1)
			{
			maxX=maxX1;
			maxY=maxY1;

			xhit=xhit1;
			xtops = xtops1;
			myboard = myboard1;
			fourgame=fourgame1;
			gridXpoint=gridXpoint1;
			gridYpoint=gridYpoint1;
			gridHeight=gridHeight1;
			gridWidth=gridWidth1;
			parm=parm1;
			//Form1 myform1=new FormatException(myform);


			}
				public void run()
		{
			parm.noDraw=0;
			//	Thread current = Thread.CurrentThread;
				int finalmove=fourgame.makemove(xhit);

				int reversey=((maxY-xtops[finalmove]-1)*maxX);
					if(reversey + finalmove >=0)
					{
							myboard[reversey+ finalmove]=1;
							parm.gameover=findFour(reversey + finalmove, 1);
					if(parm.gameover == 1)
						parm.gameover=2;// 2 is computer wins
						parm.lastx=finalmove;
					parm.lasty=maxY-xtops[finalmove]-1;
					parm.turn++;
						if(parm.turn == maxX * maxY && parm.gameover == 0)
						parm.gameover=3;


					}
					else
					{
						//myX=reversey + finalmove;
						//myY=-1;
					}
				xtops[finalmove]++;

				parm.noDraw=1;
				//super.Invalidate();
				repaint();

				}
			int findFour(int x, int token)
			{

			// our rows go 0-6 then 7-13 so we add 1 for the mod checks



				//token is 1 or 2

				int win=0;
				int max=4;
				//if(type==2)
				//	max=5;

				// diagonal down

				int count;

				count=1;
				int a;
				for(a=1; a<max; a++) // up +11 down -9
					if(x + (maxX+1) * a < maxX * maxY && (x+1 + (maxX +1) * a)%maxX !=1)
					{
						if(myboard[x + (maxX+1)*a]==token)
							count++;
						else
							a=max;
					}
					else
						break;

				for(a=1; a<max; a++) // up +11 down -9
					if(x - (maxX+1) * a >= 0 && (x+1 - (maxX +1) * a)%maxX !=0)
					{
							if(myboard[x - (maxX+1)*a]==token)
						 count++;
					 else
						 a=max;
					}
					else
						break;

				if(count >= max)
					win=1;

				count=1;

				for(a=1; a<max; a++) // up +11 down -9
					if(x - (maxX-1) * a >= 0 && (x+1 - (maxX - 1) * a) % maxX !=1)
					{
						if(myboard[x - (maxX-1)*a]==token)
							count++;
						else
							a=max;
					}
					else
						break;

				for(a=1; a<max; a++) // up +11 down -9
					if(x + (maxX-1) * a < maxX * maxY && (x +1 + (maxX - 1) * a)%maxX !=0)
					{
						if(myboard[x + (maxX-1)*a]==token)
							count++;
						else
							a=max;
					}
					else
						break;


				if(count >= max)
					win=1;

				count=1;

				for(a=1; a<max; a++) // up +11 down -9
					if(x + maxX * a < maxX * maxY)
					{
						if(myboard[x + maxX*a]==token)
							count++;
						else
							a=max;
					}
					else
						break;

				for(a=1; a<max; a++) // up +11 down -9
					if(x -maxX * a >= 0)
					{
						if(myboard[x - maxX*a]==token)
							count++;
						else
							a=max;
					}
					else
						break;


				if(count >= max)
					win=1;

				count=1;

				for(a=1; a<max; a++) // up +11 down -9
					if(x -1 * a >=0 && (x + 1 - a) % maxX != 0)
					{
						if(myboard[x - 1*a]==token)
							count++;
						else
							a=max;
					}
					else
						break;

				for(a=1; a<max; a++) // up +11 down -9
					if(x + a < maxX * maxY && (x + 1 + a) % maxX != 1)
					{
						if(myboard[x + 1*a]==token)
							count++;
						else
							a=max;
					}
					else
						break;


				if(count >= max)
					win=1;

				return win;

			}      // end method

		}  // end class







}// end class fourframe



