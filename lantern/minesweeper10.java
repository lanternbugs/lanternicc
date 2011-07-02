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
import java.util.Date;

//import javax.jnlp.*;

/*public class minesweeper10  extends JApplet
//public class minesweeper10
{

//public static void main(String[] args)
public void init()
{
minesweeperframe frame = new minesweeperframe();
frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

frame.setSize(1000,800);
//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
frame.setTitle("Lantern Chess");
frame.setVisible(true);
frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);


}
}// end main class
*/
class minesweeper10 extends JInternalFrame implements ActionListener, InternalFrameListener
{

 minesweeperData myGridData;
  minesweeperpanel panel;
  Timer timer;

channels sharedVariables;
JFrame masterFrame;

 minesweeper10(channels sharedVariables1, JFrame masterFrame1)
 {

super("MineSweeper",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiable
  setSize(1000,800);
  //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

  setVisible(true);
  setDefaultCloseOperation(DISPOSE_ON_CLOSE);
addInternalFrameListener(this);

  sharedVariables=sharedVariables1;
  masterFrame=masterFrame1;

  JMenuBar menu = new JMenuBar();

   JMenu file = new JMenu("File");
    JMenuItem  newgame9by9 = new JMenuItem("New Game 9 by 9");
    file.add(newgame9by9);

 	JMenuItem  newgame16by16 = new JMenuItem("New Game 16 by 16");
    file.add(newgame16by16);

 	JMenuItem  newgame16by30 = new JMenuItem("New Game 16 by 30");
    file.add(newgame16by30);

    newgame9by9.addActionListener(this);
    newgame16by16.addActionListener(this);
    newgame16by30.addActionListener(this);

    menu.add(file);
    JMenu highscores = new JMenu("High Scores");
    JMenuItem s9by9scores = new JMenuItem("High Scores 9 by 9");
    highscores.add(s9by9scores);
    s9by9scores.addActionListener(this);

    JMenuItem s16by16scores = new JMenuItem("High Scores 16 by 16");
    highscores.add(s16by16scores);
    s16by16scores.addActionListener(this);

    JMenuItem s16by30scores = new JMenuItem("High Scores 16 by 30");
    highscores.add(s16by30scores);
    s16by30scores.addActionListener(this);


  menu.add(highscores);

    setJMenuBar(menu);

  myGridData = new minesweeperData();
  myGridData.setRows(9);
  myGridData.setCollumns(9);
  myGridData.setBombs(8);
  myGridData.makeGrid();
  myGridData.setState(myGridData.ONGOING);
  panel = new minesweeperpanel();
  add(panel);

 }

public void actionPerformed(ActionEvent event)
{

if(event.getActionCommand().equals("New Game 9 by 9"))
{
  myGridData = new minesweeperData();
  myGridData.setRows(9);
  myGridData.setCollumns(9);
  myGridData.setBombs(8);
  myGridData.makeGrid();
  myGridData.setState(myGridData.ONGOING);
  panel.repaint();


}// end new game

if(event.getActionCommand().equals("New Game 16 by 16"))
{
  myGridData = new minesweeperData();
  myGridData.setRows(16);
  myGridData.setCollumns(16);
  myGridData.setBombs(25);
  myGridData.makeGrid();
  myGridData.setState(myGridData.ONGOING);
  panel.repaint();

}// end new game


if(event.getActionCommand().equals("New Game 16 by 30"))
{
  myGridData = new minesweeperData();
  myGridData.setRows(16);
  myGridData.setCollumns(30);
  myGridData.setBombs(48);
  myGridData.makeGrid();
  myGridData.setState(myGridData.ONGOING);
  panel.repaint();

}// end new game

if(event.getActionCommand().equals("High Scores 9 by 9"))
{
highScoreDialog dialog = new highScoreDialog(masterFrame, true, sharedVariables.mineScores.score9by9, "High Scores 9 by 9");
}

if(event.getActionCommand().equals("High Scores 16 by 16"))
{
highScoreDialog dialog = new highScoreDialog(masterFrame, true, sharedVariables.mineScores.score16by16, "High Scores 16 by 16");
}

if(event.getActionCommand().equals("High Scores 16 by 30"))
{
highScoreDialog dialog = new highScoreDialog(masterFrame, true, sharedVariables.mineScores.score16by30, "High Scores 16 by 30");
}

} // end action performed





class minesweeperpanel extends JPanel  implements MouseMotionListener, MouseListener
{
        int originX;
	int originY;
	int dimension;
	int mx;
	int my;
	int oldmx;
	int oldmy;
	int BOMB = -10;
	int OFFBOARD = -20;
        minesweeperpanel()
        {
         originX=25;
	 originY=25;
	 dimension =30;
	 addMouseMotionListener(this);
	 addMouseListener(this);


        }
	public void paintComponent(Graphics g)
	{
 	try {


          Color mycolor, colorEmpty, colorBomb, colorFilled, colorNumber, colorClock, colorMarked;
	mycolor = new Color(0,0,0);
	colorEmpty = new Color(150,150,150);
	colorFilled = new Color(100,100,100);
	colorBomb = new Color(150,0,0);
	colorClock = new Color(240,240,240);
	colorMarked = new Color(240,240, 0);

	if(myGridData.win == true)
		colorBomb = new Color(0,150,0);

	colorNumber = new Color(0,0,255);


	setBackground(mycolor);

	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;







        for(int y=1; y<= myGridData.rows; y++)
        for(int x = 1; x <=  myGridData.collumns; x++)
        {
         if(myGridData.grid[x-1][y-1] == myGridData.UNCOVERED)
			 	g2.setColor(colorEmpty);
         else if(myGridData.grid[x-1][y-1] == myGridData.COVERED)
         {
			 if(myGridData.state == myGridData.ONGOING && myGridData.markedGrid[x-1][y-1]  == myGridData.MARKED)
			 	g2.setColor(colorMarked);
			 else
         g2.setColor(colorFilled);
	 	}
       else if(myGridData.grid[x-1][y-1] == myGridData.BOMB)
       {
		   if(myGridData.state == myGridData.ONGOING)
		   {


			 if(myGridData.state == myGridData.ONGOING && myGridData.markedGrid[x-1][y-1]  == myGridData.MARKED)
			 	g2.setColor(colorMarked);
			 else

			   g2.setColor(colorFilled);
			}
		   else
		   g2.setColor(colorBomb);
        }
        else g2.setColor(new Color(0,255,2500));


        for(int a=2; a < dimension-2; a++)
		g2.draw(new Line2D.Double(originX + x * dimension + 2, originY + y * dimension + a,originX + (x+1) * dimension -2, originY + (y) * dimension + a));


		if(myGridData.grid[x-1][y-1] == myGridData.UNCOVERED && myGridData.numbers[x-1][y-1] > 0)
		{
		g2.setColor(colorNumber);
		Font myFont = new Font("TimesRoman", Font.BOLD, 20);
		g2.setFont(myFont);

		g2.drawString( "" + myGridData.numbers[x-1][y-1], originX + x * dimension + 7, originY + (y) * dimension +  (int) ( 5/4 * dimension) - 5);
		g2.setColor(colorClock);
		myFont = new Font("TimesRoman", Font.BOLD, 24);
		g2.setFont(myFont);
		g2.drawString("" + myGridData.time + " seconds", originX * 2,  originY);

		}


        }// end for



  }
  catch(Exception d)
  { }
        }// end paint components


/*****************************    mouse events *****************************************/

	    void eventOutput(String eventDescription, MouseEvent e) {
	      oldmx=mx;
	      oldmy=my;

	      mx=e.getX();
	      my=e.getY();

		}

	    public void mouseMoved(MouseEvent e) {
	        eventOutput("Mouse moved", e);
	    }

	    public void mouseDragged(MouseEvent e) {
	        eventOutput("Mouse dragged", e);
	    }



	    public void mousePressed(MouseEvent e) {

			if(myGridData.state != myGridData.ONGOING)
			return;

			int x = getSquareXY("X");

			if(x == OFFBOARD)
			return;

			int y=getSquareXY("Y");



			if(e.getButton() == MouseEvent.BUTTON3 && myGridData.grid[x][y] != myGridData.UNCOVERED)
			{
				    if(myGridData.markedGrid[x][y] == myGridData.UNMARKED)
				    	myGridData.markedGrid[x][y] = myGridData.MARKED;
					else
				    	myGridData.markedGrid[x][y] = myGridData.UNMARKED;

					repaint();
					return;
			}

			if(myGridData.grid[x][y] == myGridData.BOMB)
			{
				myGridData.state = myGridData.DONE;


				try {
					timer.cancel();
				}
				catch(Exception z){}
				repaint();
				return;
			}



			if(myGridData.grid[x][y] == myGridData.COVERED)
			{
				myGridData.grid[x][y] = myGridData.UNCOVERED;
				// turn off any marked bombs
				myGridData.markedGrid[x][y] = myGridData.UNMARKED;
				if(myGridData.started == false)
				{
					myGridData.started = true;
					myGridData.initialTime = System.currentTimeMillis();
					try { timer.cancel(); } catch(Exception d){}

					timer = new Timer (  ) ;
					timer.scheduleAtFixedRate( new ToDoTask (  ) , 100 ,100) ;


				}
				if(myGridData.numbers[x][y] ==0)
					myGridData.removeSquares(x,y);
				if(myGridData.checkForWin() == true)
				{
					myGridData.win = true;
					myGridData.state = myGridData.DONE;
					addScore();
					try {timer.cancel();}catch(Exception n){}
				}

				repaint();

			}


	    }



	int getSquareXY(String type)
	{
		try {

		int x=mx;
		int y=my;

		x=(mx - originX - dimension) / dimension;
		y=(my - originY - dimension) / dimension;

		if(x < 0 || x >= myGridData.collumns)
		return OFFBOARD;

		if(y < 0 || y >= myGridData.rows)
		return OFFBOARD;

		/*if(myGridData.grid[x][y] == myGridData.BOMB)
		return BOMB;
		*/
		if(type.equals("X"))
		return x;

		if(type.equals("Y"))
		return y;



		// shouldn hit
		return OFFBOARD;


		}catch(Exception e){}

		return OFFBOARD;
	}



	public void mouseEntered (MouseEvent me) {
		 }



	public void mouseReleased (MouseEvent me) {
		 }

	 public void mouseExited (MouseEvent me) {
		 }

	public void mouseClicked (MouseEvent me) {
		}


/********************************* end mouse events **************************************/
void addScore()
{


	Date present = new Date(System.currentTimeMillis());
	String user=sharedVariables.myname;
	if(user.equals(""))
		user="user";

	if(myGridData.rows ==9 && myGridData.collumns == 9)
		sharedVariables.mineScores.score9by9.addScore(myGridData.time, present.toString(), user);
	else if(myGridData.rows ==16 && myGridData.collumns == 16)
		sharedVariables.mineScores.score16by16.addScore(myGridData.time, present.toString(), user);
	else if(myGridData.rows ==16 && myGridData.collumns == 30)
		sharedVariables.mineScores.score16by30.addScore(myGridData.time, present.toString(), user);
	else
		sharedVariables.mineScores.scoreCustom.addScore(myGridData.time, present.toString(), user);


}

class ToDoTask extends TimerTask  {


    public void run (  )   {

try {
	int time = (int) (System.currentTimeMillis() - myGridData.initialTime )/1000;
	if(time > myGridData.time)
	{
		myGridData.time = time;
		repaint();
	}
	}
	catch(Exception d){}

}// end run

}// end todotask class


}// end minesweeper panel class



/************** jinternal frame listener ******************************/


      public void internalFrameClosing(InternalFrameEvent e) {
	try { timer.cancel(); } catch(Exception d){}


    }

    public void internalFrameClosed(InternalFrameEvent e) {

    }

    public void internalFrameOpened(InternalFrameEvent e) {

    }

    public void internalFrameIconified(InternalFrameEvent e) {

    }

    public void internalFrameDeiconified(InternalFrameEvent e) {



	   }

    public void internalFrameActivated(final InternalFrameEvent e) {
     // System.out.println("fame activate");




    }

    public void internalFrameDeactivated(InternalFrameEvent e) {


    }


}// end minesweeper frame class