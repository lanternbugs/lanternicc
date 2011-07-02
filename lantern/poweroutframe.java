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

import javax.swing.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


import java.awt.*;
import java.awt.geom.*;
import java.awt.Color;
import java.awt.Dimension;
//import java.awt.event.MouseMotionListener;
//import java.awt.event.MouseEvent;
import java.awt.event.*;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;
 import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
//import java.awt.image.*;
import java.util.Random;
import java.io.*;
import java.net.*;
import java.applet.*;
import java.util.concurrent.locks.*;


class poweroutframe extends JInternalFrame  implements ActionListener
{
URL [] poweroutSounds;
	public poweroutframe(URL [] poweroutSounds1)
	{


	 super("Powerout Console" ,
	          true, //resizable
	          true, //closable
	          true, //maximizable
          true);//iconifiable

	//setTitle("powerout");
	 colors = 1;
setDefaultCloseOperation(DISPOSE_ON_CLOSE);
poweroutSounds=poweroutSounds1;
topY = 460;
py=(int) topY;

paddlewidth = 40;

pheight = 7;
px=300;

ballwidth=9;
mybricks = new bricks();
inplay=0;
normalspeed=1;
gravity=0;
sounds = 0;
JComponent newContentPane = new poweroutpanel();
       newContentPane.setOpaque(true); //content panes must be opaque
       setContentPane(newContentPane);


//poweroutpanel panel = new poweroutpanel();
//		add(panel);
		//panel.kill=1;

JMenuItem currentversion;
JMenuItem level1;
JMenuItem level2;
JMenuItem level3;
JMenuItem level4;

JMenuItem level5;
JMenuItem level6;
JMenuItem level7;
JMenuItem level8;
JMenuItem level9;
JMenuItem level10;

bricks1color= new Color(255,255,0);
bricks2color = new Color(160, 32, 240);

JMenuBar menu = new JMenuBar();
 JMenu file = new JMenu("Paddle");
 colors1 = new JCheckBoxMenuItem("white paddle");
 file.add(colors1);
 colors2 = new JCheckBoxMenuItem("pinkish paddle");
 file.add(colors2);
 menu.add(file);
//setJMenuBar(menu);
 colors2.setSelected(true);

JMenu filespeed = new JMenu("Speed");
 normal = new JCheckBoxMenuItem("normal");
 filespeed.add(normal);
 fast = new JCheckBoxMenuItem ("fast");
 filespeed.add(fast);
 lightning = new JCheckBoxMenuItem ("lightning");
 filespeed.add(lightning);

 menu.add(filespeed);



JMenu brickscolor = new JMenu("Bricks");
 bricks1 = new JCheckBoxMenuItem ("red/green");
 brickscolor.add(bricks1);
 bricks2 = new JCheckBoxMenuItem("yellow/purple");
 brickscolor.add(bricks2);
bricks3 = new JCheckBoxMenuItem ("white/blue");
 brickscolor.add(bricks3);

 menu.add(brickscolor);
bricks2.setSelected(true);




JMenu soundmenu = new JMenu("Sound");
 soundon = new JCheckBoxMenuItem ("on");
 soundmenu.add(soundon);
 soundoff = new JCheckBoxMenuItem("off");
 soundmenu.add(soundoff);

 menu.add(soundmenu);
soundoff.setSelected(true);




JMenu gravityspeed = new JMenu("Gravity");
 gravity1 = new JCheckBoxMenuItem ("straight");
 gravityspeed.add(gravity1);
 gravity2 = new JCheckBoxMenuItem("bent");
 gravityspeed.add(gravity2);
gravity3 = new JCheckBoxMenuItem ("paddle bent");
 gravityspeed.add(gravity3);

 menu.add(gravityspeed);
gravity1.setSelected(true);

normal.setSelected(true);
JMenu file3 = new JMenu("Levels");
 level1 = new JMenuItem("level 1");
 file3.add(level1);
 level2 = new JMenuItem("level 2");
 file3.add(level2);
 level3 = new JMenuItem("level 3");
 file3.add(level3);
 level4 = new JMenuItem("level 4");
 file3.add(level4);


level5 = new JMenuItem("level 5");
 file3.add(level5);
 level6 = new JMenuItem("level 6");
 file3.add(level6);
 level7 = new JMenuItem("level 7");
 file3.add(level7);
 level8 = new JMenuItem("level 8");
 file3.add(level8);
  level9 = new JMenuItem("level 9");
 file3.add(level9);
  level10 = new JMenuItem("level 10");
 file3.add(level10);



menu.add(file3);

 JMenu file2 = new JMenu("Version");
 currentversion = new JMenuItem("1.4.7");
 file2.add(currentversion);
 menu.add(file2);








setJMenuBar(menu);

soundon.addActionListener(this);
soundoff.addActionListener(this);


colors1.addActionListener(this);
colors2.addActionListener(this);
normal.addActionListener(this);
fast.addActionListener(this);
lightning.addActionListener(this);

level1.addActionListener(this);
level2.addActionListener(this);
level3.addActionListener(this);
level4.addActionListener(this);
level5.addActionListener(this);
level6.addActionListener(this);
level7.addActionListener(this);
level8.addActionListener(this);
level9.addActionListener(this);
level10.addActionListener(this);


gravity1.addActionListener(this);
gravity2.addActionListener(this);
gravity3.addActionListener(this);

bricks1.addActionListener(this);
bricks2.addActionListener(this);
bricks3.addActionListener(this);


	}

	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 600;

public bricks mybricks;
public int inplay;
public double normalspeed;
public int sounds;
JCheckBoxMenuItem  normal;
JCheckBoxMenuItem  fast;
JCheckBoxMenuItem  lightning;
JCheckBoxMenuItem  soundon;
JCheckBoxMenuItem  soundoff;


JCheckBoxMenuItem colors1;
JCheckBoxMenuItem colors2;
JCheckBoxMenuItem gravity1;
JCheckBoxMenuItem gravity2;
JCheckBoxMenuItem gravity3;
Color bricks1color;
Color bricks2color;

JCheckBoxMenuItem bricks1;
JCheckBoxMenuItem bricks2;
JCheckBoxMenuItem bricks3;

int px;
int py;
int ballwidth;

int paddlewidth;
double pheight;
public double topY;

int gravity;

int colors;
public void actionPerformed(ActionEvent event){
//Object source = event.getSource();
//handle action event here



if(event.getActionCommand().equals("on"))
{	sounds=1;
	soundon.setSelected(true);
	soundoff.setSelected(false);

}
if(event.getActionCommand().equals("off"))
{	sounds=0;
	soundoff.setSelected(true);
	soundon.setSelected(false);


}


 if(event.getActionCommand().equals("straight"))
{	gravity=0;
	gravity1.setSelected(true);
	gravity2.setSelected(false);
	gravity3.setSelected(false);
	repaint();
}
if(event.getActionCommand().equals("bent"))
{	gravity=1;
	gravity2.setSelected(true);
	gravity1.setSelected(false);

	gravity3.setSelected(false);
	repaint();
}
if(event.getActionCommand().equals("paddle bent"))
{	gravity=2;
	gravity3.setSelected(true);
	gravity1.setSelected(false);
	gravity2.setSelected(false);

	repaint();
}



 if(event.getActionCommand().equals("red/green"))
{	bricks1color= new Color(255,51,51);
	bricks2color = new Color(0, 200, 20);
	bricks1.setSelected(true);
	bricks2.setSelected(false);
	bricks3.setSelected(false);
	repaint();
}
if(event.getActionCommand().equals("yellow/purple"))
{	bricks1color= new Color(255,255,0);
	bricks2color = new Color(160, 32, 240);
	bricks2.setSelected(true);
	bricks1.setSelected(false);

	bricks3.setSelected(false);
	repaint();
}
if(event.getActionCommand().equals("white/blue"))
{
	bricks1color= new Color(255, 255,255);
	bricks2color = new Color(50, 50, 255);
	bricks3.setSelected(true);
	bricks1.setSelected(false);
	bricks2.setSelected(false);

	repaint();
}


 if(event.getActionCommand().equals("white paddle"))
{	colors=1;
	colors1.setSelected(true);
	colors2.setSelected(false);
	repaint();
}
if(event.getActionCommand().equals("pinkish paddle"))
{	colors=2;
	colors1.setSelected(false);
	colors2.setSelected(true);
	repaint();
}
 if(event.getActionCommand().equals("normal"))
{	normalspeed=1;

	normal.setSelected(true);
	fast.setSelected(false);
	lightning.setSelected(false);

       repaint();
}
if(event.getActionCommand().equals("fast"))
{	normalspeed=2;
	normal.setSelected(false);
	fast.setSelected(true);
	lightning.setSelected(false);

	repaint();
}
if(event.getActionCommand().equals("lightning"))
{	normalspeed=3;
	normal.setSelected(false);
	fast.setSelected(false);
	lightning.setSelected(true);

	repaint();
}



 if(event.getActionCommand().equals("level 1"))// level 1 is all ones and from file level 1 is 9
{
for(int b=0; b<mybricks.numy; b++)
for(int a=1; a <= mybricks.numx; a++)
if(b== 2 || b == 3 || b == 7 || b == 8)
mybricks.grid[a + b * mybricks.numx]=1;
else if(b== 4 || b == 9)
mybricks.grid[a + b * mybricks.numx]=4;
else
mybricks.grid[a + b * mybricks.numx]=0;
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.gridhits[a]=0;

inplay=0;
repaint();
}
 if(event.getActionCommand().equals("level 2"))
{
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.grid[a]=mybricks.totalgrid[a+200];
inplay=0;
repaint();
}
 if(event.getActionCommand().equals("level 3"))
{
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.grid[a]=mybricks.totalgrid[a+400];
inplay=0;
repaint();
}
 if(event.getActionCommand().equals("level 4"))
{
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.grid[a]=mybricks.totalgrid[a+600];
inplay=0;
repaint();
}


 if(event.getActionCommand().equals("level 5"))
{
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.grid[a]=mybricks.totalgrid[a+800];
inplay=0;
repaint();
}
 if(event.getActionCommand().equals("level 6"))
{
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.grid[a]=mybricks.totalgrid[a+1000];
inplay=0;
repaint();
}
 if(event.getActionCommand().equals("level 7"))
{
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.grid[a]=mybricks.totalgrid[a+1200];
inplay=0;
repaint();
}
 if(event.getActionCommand().equals("level 8"))
{
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
{if(mybricks.totalgrid[a+1400] == 1)
mybricks.grid[a]=4;
else
mybricks.grid[a]=0;
}
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.gridhits[a]=0;

inplay=0;
repaint();
}
 if(event.getActionCommand().equals("level 9"))// level 1 is now 9
{
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.grid[a]=mybricks.totalgrid[a];
inplay=0;
repaint();
}
 if(event.getActionCommand().equals("level 10"))
{
for(int a=1; a <= mybricks.numx * mybricks.numy; a++)
mybricks.grid[a]=mybricks.totalgrid[a+1800];
inplay=0;
repaint();
}

}



 class poweroutpanel extends JPanel implements MouseMotionListener, MouseListener
{

poweroutpanel()
{addMouseMotionListener(this);
addMouseListener(this);
//System.out.println("constructing panel");
myball = new ball();
myball2 = new ball();
myball2.alive=0;
ballnumber=1;
sticky=0;
kill=0;

mouseclicks=0;

addKeyListener(new KeyListener() {public void keyPressed(KeyEvent e)
{
        int a=e.getKeyCode();
		double movement = 2*paddlewidth/5;
    	int oldpx=px;
         if(a == 39  ) // shift + right arrow
         {

	px=px + (int ) movement;
      if(px <= 40)
	px = 40;
      if(px + paddlewidth > 640)
	px = 640 - paddlewidth;
	//py=e.getY() ;
	adjustCaughtBalls(oldpx);
	repaint(20, (int) (topY -30), 700, (int) (topY + 30));

         }
         if(a == 37  ) // shift + left arrow
         {



	px=px - (int ) movement;
      if(px <= 40)
	px = 40;
      if(px + paddlewidth > 640)
	px = 640 - paddlewidth;
	adjustCaughtBalls(oldpx);
	repaint(20, (int) (topY -30), 700, (int) (topY + 30));

          }

         if(a==32)
         {

			 	mousePressedEvent();
			 	repaint(20, (int) (topY -30), 700, (int) (topY + 30));

	  	}

}

void adjustCaughtBalls(int oldpx)
	  	{
			 	if(sticky == 1 && inplay == 1)
			 	{
			 	if(myball.alive == 1 && myball.caught == 1)
			 	{
			 		myball.bx=myball.bx + px - oldpx;
			 		myball.b= myball.by - myball.m * myball.bx;
			 		myball.dx= (int) myball.bx;
			 	}
			 	if(myball2.alive == 1 && myball2.caught == 1)
			 	{
			 		myball2.bx=myball2.bx + px - oldpx;
			 		myball2.b= myball2.by - myball2.m * myball2.bx;
			 		myball2.dx=(int) myball2.bx;
			 	}

			 	}

		}


   public void keyTyped(KeyEvent e) {;

    }

    /** Handle the key-released event from the text field. */
    public void keyReleased(KeyEvent e) {;

    }

}

);

	timer = new Timer (  ) ;
    timer.scheduleAtFixedRate( new ToDoTask (  ) , 50 ,50) ;
//timer.schedule( new ToDoTask (  ) ,50) ;

}
	public void paintComponent(Graphics g)
	{
 	Color mycolor, mycolor2, mycolor3;
	mycolor = new Color(5,0,0);
	setBackground(mycolor);
	if(kill==0)
	{
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	double leftX = px;


	if(colors == 1)
	mycolor= new Color(255,255,255);
	else
	mycolor= new Color(225,100,100);


	g2.setColor(mycolor);
	for(int a=0; a<pheight; a++)
		g2.draw(new Line2D.Double(leftX, topY+a, leftX+paddlewidth, topY+a));

// draw ball
	mycolor= new Color(255,255, 255);
	g2.setColor(mycolor);
	//if(inplay == 1)
	//for(int a=0; a<myball.height; a++)
	//	g2.draw(new Line2D.Double(myball.bx, myball.by+a, myball.bx+myball.width, myball.by+a));




/*	if(inplay == 1)
	g2.draw(new Rectangle2D.Double(myball.dx, myball.dy,
                               myball.width,
                               myball.height));

*/

if(inplay == 1)
{
 //   g2.setPaint(mycolor);
   // g2.fill(new Ellipse2D.Double(myball.dx, myball.dy, myball.width, myball.height));




BufferedImage img = new BufferedImage((int) myball.width,(int) myball.height ,BufferedImage.TYPE_INT_RGB);


Graphics2D g2d = img.createGraphics();
g2d.setColor(new Color(255,255,255));
g2d.fillOval(0, 0, (int) myball.width, (int) myball.height);
if(myball.alive == 1)
g2.drawImage(img, null, (int) myball.dx, (int) myball.dy);
if(myball2.alive == 1)
g2.drawImage(img, null, (int) myball2.dx, (int) myball2.dy);


 }






/*	mycolor= new Color(200,0,20);
	mycolor2 = new Color(0, 200, 20);
	mycolor3 = new Color(0, 20, 200);
*/
	for(int a=1; a<= mybricks.numx; a++)
	for(int b=1; b<=mybricks.numy; b++)
	if(mybricks.grid[a + (b-1)* mybricks.numx]>0)
	{

	if(mybricks.grid[a + (b-1)* mybricks.numx]==1)
	g2.setColor(bricks1color);
	if(mybricks.grid[a + (b-1)* mybricks.numx]==2)
	g2.setColor(bricks2color);
	if(mybricks.grid[a + (b-1)* mybricks.numx]==3)
	g2.setColor(bricks1color);
	if(mybricks.grid[a + (b-1)* mybricks.numx]==4)
	{
	Color bricks4color = new Color(175, 89, 67);
	if(mybricks.gridhits[a + (b-1)* mybricks.numx] > 0)
	bricks4color = new Color(125, 59, 47);

	g2.setColor(bricks4color);
	}



	for(int c=1; c<mybricks.height-1; c++)
		g2.draw(new Line2D.Double((mybricks.left + 1 + (a-1) * mybricks.width), (mybricks.top + (b - 1) * mybricks.height + c), (mybricks.left - 1 + (a) * mybricks.width), (mybricks.top + (b - 1) * mybricks.height + c)));


	}

	// g.drawString("ihitx is  " + mybricks.ihit.x + " and yhit is " + mybricks.ihit.y, 150, 10);

//draw left right bars
mycolor= new Color(160, 32, 240);
	g2.setColor(mycolor);
	for(int a=0; a < myball.top + 20; a++)
		g2.draw(new Line2D.Double(myball.left - 20, a, myball.left - 1, a));
	for(int a=0; a<myball.top + 20; a++)
		g2.draw(new Line2D.Double(myball.right + myball.width + 1, a, myball.right + myball.width + 20, a));

// paint bricks falling
for(int c=0; c<= mybricks.topf; c++)
{
Color mycolorb;
if(mybricks.bricksfalling[c].t==0)
mycolorb= new Color(250,0,0);
else if(mybricks.bricksfalling[c].t == 2)
mycolorb = new Color(0,250,0);
else
mycolorb= new Color(0,0,250);


mycolor = new Color(230,230,230); // overall brick color

g2.setColor(mycolor);
if(mybricks.bricksfalling[c].x>0)
{g2.fill(new Rectangle2D.Double(mybricks.bricksfalling[c].x + 1, mybricks.bricksfalling[c].y + 1, (int) (mybricks.width - 1), (int) (mybricks.height - 1)));
// internal color of rectangle
g2.setColor(mycolorb);
g2.fill(new Rectangle2D.Double((int) (mybricks.bricksfalling[c].x + mybricks.width/2 - 5), mybricks.bricksfalling[c].y + mybricks.height/2 -3, 10, 6));
} // x > 0
}//end for

}// end kill ==0

}

    void eventOutput(String eventDescription, MouseEvent e) {
      	int oldpx=px;

	px=e.getX();
	px=px - 20;
      if(px <= 40)
	px = 40;
      if(px + paddlewidth > 640)
	px = 640 - paddlewidth;
	//py=e.getY() ;
	if(sticky == 1 && inplay == 1)
	{
	if(myball.alive == 1 && myball.caught == 1)
	{
		myball.bx=myball.bx + px - oldpx;
		myball.b= myball.by - myball.m * myball.bx;
		myball.dx= (int) myball.bx;
	}
	if(myball2.alive == 1 && myball2.caught == 1)
	{
		myball2.bx=myball2.bx + px - oldpx;
		myball2.b= myball2.by - myball2.m * myball2.bx;
		myball2.dx=(int) myball2.bx;
	}

	}
	repaint(20, (int) (topY -30), 700, (int) (topY + 30));

    }


    public void mouseMoved(MouseEvent e) {
        eventOutput("Mouse moved", e);
    }

    public void mouseDragged(MouseEvent e) {
        eventOutput("Mouse dragged", e);
    }
    public void mousePressed(MouseEvent e) {
 		mousePressedEvent();
 		mouseclicks++;
	}



 public void mouseEntered (MouseEvent me) {mouseclicks++;}
 public void mouseReleased (MouseEvent me) {mouseclicks++;}
 public void mouseExited (MouseEvent me) {mouseclicks++;}
public void mouseClicked (MouseEvent me) {mouseclicks++;}

void mousePressedEvent()
{
      	myball.caught=0;
	myball2.caught=0;


	if(inplay == 0)
	{
	inplay=1;
	mybricks.clear();
	myball.ground=0;
	myball.bx= (double) px + (double) (paddlewidth / 2);
	myball.by= (double) (py - 25);
	myball.m=2;
	myball.dir=-1;
        myball.b=( myball.by - myball.m * myball.bx);
        myball.speed=0;
	myball.inc=4;
	myball.dx=(int) myball.bx;
	myball.dy=(int) myball.by;
	myball.alive=1;
	myball2.alive=0;
	ballnumber=1;
	paddlewidth=40;
	sticky=0;
	repaint();
	mybricks.topf=-1;
}
}
ball myball;
ball myball2;
int sticky;
int ballnumber;

Timer timer;

public int kill;
public int mouseclicks;


class ToDoTask extends TimerTask  {


    public void run (  )   {

	ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	Lock writeLock = rwl.writeLock();

	writeLock.lock();
	try {

	if(kill==0 && inplay == 1)
	{

	if(myball.alive == 1 && myball.caught == 0)
	myball.move();
	if(myball2.alive == 1 && myball2.caught == 0)
	myball2.move();

	if(myball.alive == 1)
	repaint((int) myball.dx - 50, (int) myball.dy - 50, (int) myball.width + 100, (int) myball.height + 100);

	if(myball2.alive == 1)
	repaint((int) myball2.dx - 50, (int) myball2.dy - 50, (int) myball2.width + 100, (int) myball2.height + 100);


	mybricks.incrementfalling();
        int yess=mybricks.isitahit();
	if(yess >= 0)
	{
	if(sounds == 1)
	   playSound(2);
	if(yess == 0)
	sticky=1;
	if(yess == 1 && ballnumber == 1)
	{
	sticky=0;
	myball.caught=0;
	myball2.caught=0;

	ballnumber++;
	if(myball2.alive == 0)
	{
	myball2.initialize();
	myball2.speed=myball.speed;
	}// myball2
	else if(myball.alive == 0)
	{
	myball.initialize();
	myball.speed=myball2.speed;
	}//myball
	}
	if(yess == 2 && paddlewidth == 40)
	{
	paddlewidth = 60;
	sticky=0;
	myball.caught=0;
	myball2.caught=0;
	repaint();
	}

	}// if yesss

	}

	}// end try
	finally { writeLock.unlock();}

	//System.out.println(" z is " + z + " in timer");
     // timer.cancel (  ) ; //Terminate the thread
	try{
	Thread.sleep(10);
	}
	catch(Exception e)
	{;}
// timer.schedule( new ToDoTask (  ) ,50) ;
  }
  }

class ball {

public double bx;
public double by;
public double dx;
public double dy;
public int alive;
int ydown[];
int yup[];
int left;
double right;
double top;
double bottom;
int speed;
int ground;
double m;
double b;
int caught;
double dir;
double inc;
public double height;
public double width;
Toolkit toolkit;
//public Sound song1, song2, song3;

ball()
{
left=40;
//song1 = new Sound("DING.WAV");
//song2 = new Sound("BEEPPURE.wav");
//song1 = new Sound("BEEP_FM.wav");
//song3 = new Sound("BEEPSPAC.wav");
top=topY-10;
bottom=10;
initialize();
ground=0;
b= ( by -  m * bx);
inc=4;
height=8;
width=ballwidth;
speed=0;
right=640-width;
ydown = new int[mybricks.numy];
yup = new int[mybricks.numy];
setyupdown();

}

void setyupdown()
{

for(int a =0; a < mybricks.numy; a++)
{
ydown[a]=(int) (mybricks.top + mybricks.height * a);
yup[a]=(int) (mybricks.top + mybricks.height * (a+1));

}// end for
}// end method

void initialize()
{
alive=1;
dir=-1;
m=2;
bx= (double) px + (double) (paddlewidth / 2);
by= (double) (py - 25);
// y=mx+b
b=by - m * bx;

caught=0;
dx=(int) bx;
dy=(int) dy;
}
double getslope()
{

double center = bx + width / 2;
double paddlebit=paddlewidth/10;
double paddlex=(double) px;

double pix=center - px;

if(paddlewidth == 40)
{
if(pix < 1)
pix=1;
if(pix>40)
pix=40;

if(pix >= 25) // 25 is the imaginary center we dont use
pix+=5;
else
pix+=4;

m=Math.sin(Math.PI/49*pix)/Math.cos(Math.PI/49*pix);
if(m>0 && m < .4)
m=.4;
if(m<0 && m > -.4)
m=-.4;
}
else
{
if(pix < 1)
pix=1;
if(pix>60)
pix=60;

if(pix >= 35) // 35 is the imaginary center we dont use
pix+=5;
else
pix+=4;

m=Math.sin(Math.PI/69*pix)/Math.cos(Math.PI/69*pix);
if(m>0 && m < .4)
m=.4;
if(m<0 && m > -.4)
m=-.4;

}
return m;
// we wont incrment speed on paddle hits now
/*
if(center <px)
m=.6;
else if(center < paddlex + paddlebit)
m=.9;
else if(center < paddlex +  2 * paddlebit)
m=1.3;
else if(center < paddlex +  3 * paddlebit)
m=1.7;
else if(center < paddlex +  4 * paddlebit)
m=2.5;
else if(center < paddlex +  5 * paddlebit)
m=4;
else if(center  < paddlex + 6 * paddlebit)
m=-4;
else if(center < paddlex +  7 * paddlebit)
m=-2.5;
else if(center < paddlex +  8 * paddlebit)
m=-1.7;
else if(center < paddlex +  9 * paddlebit)
m=-1.3;
else if(center < paddlex +  10 * paddlebit)
m=-.9;
else
m=-.6;
speed++;
return m;
*/
}

void hitbottom()
{
by=bottom;
m=m*-1;
dir=dir*-1;
}
void hitbottomcollision()
{

m=m*-1;
dir=dir*-1;
}


void hittopcollision()
{

m=m*-1;
dir=dir*-1;
}


void hitright()
{
bx=right;
m=m*-1;
}

void hitleft()
{
bx=left;
m=m*-1;
}

void move()
{
if(inplay == 0)
return;

//y=mx+b
int hit=0;
inc=5;
if(speed > 30)
inc=7;
if(speed > 75)
inc=9;
/*if(normalspeed == 2)
{
if(inc == 5)
inc=inc+3;
else if(inc== 8)
inc = inc + 4;
else
inc=inc+5;
}
*/


/*if(m < 1 && m > -1)
{
// adjust inc by slope.
// m = y/x  i.e. .7 = y/x .7 x = y so multiply inc by the slope
if(m < 0)
inc = (int) (-1 * m * inc);
else
inc = (int) (m * inc);
}*/

double unit=1;
unit = Math.sqrt(1/(m*m) + 1);
double inc2 = (double) inc;
inc2 = inc2 * 1/unit;
if(normalspeed == 1)
inc = Math.round( inc2 * 1.25);
else if(normalspeed == 2)
inc = Math.round( inc2 * 1.25 * 1.66);
else
inc = Math.round( inc2 * 1.25 * 2.2);


// make horizontal ball move faster

if(m < .6 && m > -.6)
{
double tempm=m;
double mult=1;
tempm=Math.abs(m);
mult=1 + ( 1 - tempm + .4);
inc= Math.round(inc * mult);
}

for(int z =0; z < inc * 4 && hit == 0; z++)
{

by+= .25 * dir;
bx= ((by - b )/ m);

if(bx>right)
{
hitright();
b= ( by - m * bx);
hit=1;
}

if(bx<left)
{
hitleft();
b= ( by - m * bx);
hit=1;
}



if(by<bottom)
{
hitbottom();
b= ( by - m * bx);
hit=1;
}



if(by >= py - height)
{

if(bx >= px - width + 1 && bx <= ( px + paddlewidth + width -1))
{// hit paddle
//by=top;
//m=m*-1;
dir=dir*-1;
// set paddle tilt

m=getslope();
hit=1;
ground=0;
if(sticky == 1)
caught=1;
}
else // miss
{
/*bx=200;
by=400;
m=2;
dir=-1;
*/
if(ballnumber > 1)
{alive=0;
ballnumber--;
hit=1;

repaint((int) bx - 25, (int) by - 25 , 50, 50 );
}
else
{
inplay=0;
paddlewidth=40;
}
ground=0;
}

b= ( by - m * bx);
}

int collisionhit = checkcollision();
if(collisionhit == 1)
{
hit=1;
ground++;
}
dx=(int) bx;
dy=(int) by;

}// end loop

if((hit==0 && gravity > 0) || ground > 30)
{
if(m < 15 && m > -15)
{
if(gravity == 1 || gravity == 0) // gravity == 0 then ground > 20
m=m + m * Math.abs(bx - 340) / 9000;
if(gravity == 2)
m=m + m * Math.abs(bx - (px + paddlewidth / 2)) / 9000;


b= ( by - m * bx);
}
}

}// end move


int checkforhit(int difx, int difx2, int dify, int dify2, int fallb)
{

int hit =0;

int gohit=0;
int ihit1=0; // the location of the last unbreakable hit we scored a ++ so we dont score it twice
int ihit2=0;// we track ihit 3 times for if statements 2-4
int ihit3=0;
// difx and difx 2 on dify
if(difx > 0 && difx <= mybricks.numx && dify > 0 && dify <= mybricks.numy)
if(mybricks.grid[(int) (difx + (dify-1) * mybricks.numx)]>0)
{
if(mybricks.grid[(int) (difx + (dify-1) * mybricks.numx)]!=2)
{

if(mybricks.grid[(int) (difx + (dify-1) * mybricks.numx)] == 1)
gohit = 1;
else
{
mybricks.gridhits[(int) (difx + (dify-1) * mybricks.numx)]++;
ihit1 = (int) (difx + (dify-1) * mybricks.numx);
}

if(mybricks.gridhits[(int) (difx + (dify-1) * mybricks.numx)]==2)
gohit=1;

if(gohit==1)
{
mybricks.grid[difx + (dify-1) * mybricks.numx]=0;
if(fallb == 1)
mybricks.addfalling(difx, dify -1);
gohit=0;
}// end gohit

}
hit=1;

}


if(difx2 > 0 && difx2 <= mybricks.numx && dify > 0 && dify <= mybricks.numy)
if(mybricks.grid[difx2 + (dify-1) * mybricks.numx]>0)
{
if(mybricks.grid[(int) (difx2 + (dify-1) * mybricks.numx)]!=2)
{
if(mybricks.grid[(int) (difx2 + (dify-1) * mybricks.numx)] == 1)
gohit = 1;
else if(ihit1 != (int) (difx2 + (dify-1) * mybricks.numx))
{
mybricks.gridhits[(int) (difx2 + (dify-1) * mybricks.numx)]++;
ihit2=(int) (difx2 + (dify-1) * mybricks.numx);
}

if(mybricks.gridhits[(int) (difx2 + (dify-1) * mybricks.numx)]==2)
gohit=1;

if(gohit==1)
{

mybricks.grid[(int) (difx2 + (dify-1) * mybricks.numx)]=0;
if(fallb == 1)
mybricks.addfalling(difx2, dify -1);
gohit=0;
}// end gohit

}
hit=1;
}

// difx and difx 2 on dify2
if(difx > 0 && difx <= mybricks.numx && dify2 > 0 && dify2 <= mybricks.numy)
if(mybricks.grid[(int) (difx + (dify2-1) * mybricks.numx)]>0)
{
if(mybricks.grid[(int) (difx + (dify2-1) * mybricks.numx)]!=2)
{

int store1=(int) (difx + (dify2-1) * mybricks.numx);
if(mybricks.grid[(int) (difx + (dify2-1) * mybricks.numx)] == 1)
gohit = 1;
else if(ihit1 != store1 && ihit2 != store1)
{
mybricks.gridhits[(int) (difx + (dify2-1) * mybricks.numx)]++;
ihit3 = store1;
}

if(mybricks.gridhits[(int) (difx + (dify2-1) * mybricks.numx)]==2)
gohit=1;

if(gohit==1)
{
mybricks.grid[difx + (dify2-1) * mybricks.numx]=0;
if(fallb == 1)
mybricks.addfalling(difx, dify2 -1);
gohit=0;
}// end gohit



}


hit=2;
}


if(difx2 > 0 && difx2 <= mybricks.numx && dify2 > 0 && dify2 <= mybricks.numy)
if(mybricks.grid[(int) (difx2 + (dify2-1) * mybricks.numx)]>0)
{
if(mybricks.grid[(int) (difx2 + (dify2-1) * mybricks.numx)]!=2)
{
int store1=(int) (difx2 + (dify2-1) * mybricks.numx);
if(mybricks.grid[(int) (difx2 + (dify2-1) * mybricks.numx)] == 1)
gohit = 1;
else if(ihit1 != store1 && ihit2 != store1 && ihit3 != store1)
{
mybricks.gridhits[(int) (difx2 + (dify2-1) * mybricks.numx)]++;
}

if(mybricks.gridhits[(int) (difx2 + (dify2-1) * mybricks.numx)]==2)
gohit=1;

if(gohit==1)
{mybricks.grid[difx2 + (dify2-1) * mybricks.numx]=0;
if(fallb == 1)
mybricks.addfalling(difx2, dify2 -1);
gohit=0;
}// end gohit

}

hit=2;
}




return hit;



}
















int checkcollision()
{
int hit=0;

// we check a supposedly up down collision for
// if it is impossible, brick above or below
// and make it sideways
// exception is corner L it simply backs up i.e. reverse
int reversehit=0;

if(dir > 0 && by + height < mybricks.top)
return 0;
if(dir < 0 && by < mybricks.top)
return 0;

// we generate the chances that a collsion causes a brick to fall
Random generator2 = new Random( (int) (bx + by) );
int hittype=generator2.nextInt();
hittype = Math.abs(hittype);
hittype = hittype % 12;
int fallb = 0;
if(hittype == 7)
fallb = 1;


if(dir > 0)
{
// check up
double brickx=(double) bx - mybricks.left;
int difx= ( int) (brickx/mybricks.width);
difx++;

double brickx2=(double) bx + width - mybricks.left;
int difx2= ( int) (brickx2/mybricks.width);
difx2++;

double bricky=(double) by - mybricks.top;
int dify= ( int) (bricky/mybricks.height);
dify++;

double bricky2=(double) by + height - mybricks.top;
int dify2= ( int) (bricky2/mybricks.height);
dify2++;  // dify2 leads when traveling down

hit=checkforhit(difx, difx2, dify, dify2, fallb);

// define up down always
int vertical =0;
if(hit > 0)
for(int z=0; z<mybricks.numy; z++)
if(by + height == ydown[z])
{
vertical=1;
hit=2;
}
if(vertical == 1)
{
if(m < 0)
reversehit=reverse(difx, dify2);
if(m > 0)
reversehit=reverse(difx2, dify2);
}


if(hit > 0)
{
// determine if its up down or left right
//if(by+height -2 > mybricks.top + (mybricks.numy - dify) * mybricks.height )
//m=-m;
//else

if(hit == 1)
{
//if(by+height  != mybricks.top + ( dify - 1) * mybricks.height )// was dify - 1, that didnt work not sure why
m=-1 * m;
//else
//hitbottomcollision();
}
else if(hit == 2)
{
if( vertical != 1 || reversehit == 1)// was dify - 1, that didnt work not sure why
m=-1 * m;
else
hitbottomcollision();
if(reversehit == 2)
m=m * -1;

}

b= ( by - m * bx);
//by+=dir;
//bx= ((by - b )/ m);
}






}
else // dir < 0
{




// check down
double brickx=(double) bx - mybricks.left;
int difx= ( int) (brickx/mybricks.width);
difx++;

double brickx2=(double) bx + width - mybricks.left;
int difx2= ( int) (brickx2/mybricks.width);
difx2++;


double bricky=(double) by - mybricks.top;
int dify= ( int) (bricky/mybricks.height);
if(dify != bricky/mybricks.height)
dify++; // dify leads wehn traveling up

double bricky2=(double) by + height - mybricks.top;
int dify2= ( int) (bricky2/mybricks.height);
if(dify2 != bricky2/mybricks.height)
dify2++;
hit=0;




hit=checkforhit(difx, difx2, dify, dify2, fallb);


// define up down always
int vertical =0;
if(hit > 0)
for(int z=0; z<mybricks.numy; z++)
if(by  == yup[z])
{
vertical=1;
hit=1;
}
if(vertical == 1)
{
if(m < 0)
reversehit=reverse(difx2, dify);
if(m > 0)
reversehit=reverse(difx, dify);
}

if(hit > 0)
{
//if(by + 2 < mybricks.top + (mybricks.numy - dify + 1) * mybricks.height)
//m=-m;
//else
if(hit == 1)
{
if(vertical != 1 || reversehit == 1)
m=-1 * m;
else
{
hittopcollision();
if(reversehit == 2)
m=m* -1;
}
}
else if(hit == 2)
{
//if(by  != mybricks.top + (dify2) * mybricks.height)
m=-1 * m;
//else
//hittopcollision();

}
b= ( by - m * bx);
//by+=dir;
//bx= ((by - b )/ m);

}


}// else dir < 0

if(hit == 1 && sounds == 1)
playSound(0);
if(hit == 2 && sounds == 1)
playSound(1);

if(hit == 2)
hit--; // normalize to 1


if(hit == 1)
speed++;



return hit;

}// end check collision

int reverse(int x, int y)
{

// not clear on this boundary. was y + 1 < numy changing to y < numy
if(dir < 0 && y < mybricks.numy) // upscreen
{
if(mybricks.grid[y * mybricks.numx + x] > 0) // a brick below it
{
if(m < 0) // left right
{
if(x+1<= mybricks.numx)
if(mybricks.grid[(y -1) * mybricks.numx + x - 1] > 0)
return 2;
}
else if(m > 0)// right left
{
if(x-1 > 0)
if(mybricks.grid[(y -1) * mybricks.numx + x + 1] > 0)
return 2;
}

return 1;
}
}// end if dir > 0
if(dir > 0 && y  > 1)
{
if(mybricks.grid[(y - 2) * mybricks.numx + x] > 0) // a brick below it
{
if(m < 0) // left right
{
if(x-1 > 0)
if(mybricks.grid[(y - 1) * mybricks.numx + x + 1] > 0)
return 2;
}
else if(m > 0)// right left
{
if(x+1<= mybricks.numx)
if(mybricks.grid[(y - 1) * mybricks.numx + x - 1] > 0)
return 2;
}

return 1;
}
}// end if dir > 0
return 0;

}

}// end ball







void playSound(int n)
{

try {
Sound movesound=new Sound(poweroutSounds[n]);
}
catch(Exception dd){}

}





 /*class Sound // Holds one audio file
    {
 private AudioClip song; // Sound player
 private URL songPath; // Sound path
 Sound(String filename)
 {
     try
     {
   songPath = new URL(getCodeBase(),filename); // Get the Sound URL
   song = Applet.newAudioClip(songPath); // Load the Sound
     }
     catch(Exception e){} // Satisfy the catch
 }
 public void playSound()
 {
     song.loop(); // Play
 }
 public void stopSound()
 {
     song.stop(); // Stop
 }
 public void playSoundOnce()
 {
     song.play(); // Play only once
 }
    }

*/












}// end panel class

class bricks
{
double top;
double left;
double width;
double height;
public int numx;
public int numy;
public int grid[];
public int totalgrid[];
public falling bricksfalling[];
public int gridhits[];
int topf;
public falling ihit;
class falling {

public int x;
public int y;
public int t;
falling()
{
x=0;
y=0;
t=0;
}

}



void addfalling(int xpos, int ypos)
{


topf++;
//ihit.x=xpos;
//ihit.y=ypos;
if(topf > -1)
{//bricksfalling[topf].y= (int) (top + ypos * height + 5);
//bricksfalling[topf].x=(int) (left + xpos * width);
falling mine = new falling();
mine.x=(int) (left + xpos * width);
mine.y=(int) (top + ypos * height + 5);

Random generator2 = new Random( (int) (xpos + px) );
int hittype=generator2.nextInt();
hittype = hittype % 3;
hittype = Math.abs(hittype);
mine.t=hittype;

bricksfalling[topf]=mine;
}

}
void remove(int xpos, int ypos)
{
for(int c=0; c< numx * numy +1; c++)
if( bricksfalling[c].y == ypos && bricksfalling[c].x == xpos)
{
bricksfalling[c].x=0;
bricksfalling[c].y=0;
}
}

void clear()
{
// call clear to initialize top and bottom as well
// even on new game
topf=-1;
ihit.x=0;
ihit.y=0;

}

int isitahit()
{
int hitting=-1;

for(int c = 0; c <= topf; c++)
{
if(bricksfalling[c].y > py - 12 && bricksfalling[c].y < py + 2)// allows a fall of 10;
{
if(bricksfalling[c].x >= px - width && bricksfalling[c].x <= px + paddlewidth && hitting == -1)
{
ihit.x=bricksfalling[c].x;
ihit.y=bricksfalling[c].y;
bricksfalling[c].x=bricksfalling[c].y=0;

repaint(bricksfalling[c].x - 2, bricksfalling[c].y- 50,  (int) width + 10, 100);


hitting=bricksfalling[c].t; // only one hit at a time even if two fall.
}
else if(bricksfalling[c].x > 0)
{
bricksfalling[c].x=bricksfalling[c].y=0;
repaint(bricksfalling[c].x - 2, bricksfalling[c].y- 50,  (int) width + 10, 100);


}
} // end if y bound
}// end for
return hitting;
}// end if



void incrementfalling()
{
for(int c = 0; c <= topf; c++)
{
if(bricksfalling[c].x != 0)
{bricksfalling[c].y+=5;
//repaint(bricksfalling[c].x - 2, bricksfalling[c].y- 50,  (int) width + 10, 100);
repaint();
}
}// end for
}












bricks()
{
numx=20;
numy=10;
top=75;
left=40;
width=30;
height=18;
bricksfalling = new falling [1000];
ihit = new falling();
topf=-1;

//grid = new int[numx * numy + 1];
//for(int a=1; a <= numx * numy; a++)
//grid[a]=1;


totalgrid = new int[] {
0,
0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0,
0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0,
0, 0, 0, 2, 2, 1, 2, 2, 0, 0, 0, 0, 2, 2, 1, 2, 2, 0, 0, 0,
0, 2, 2, 2, 1, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 1, 2, 2, 2, 0,
0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 2, 2, 2, 1, 1, 1, 2, 2, 0, 0, 2, 2, 1, 1, 1, 2, 2, 2, 0,
0, 0, 0, 2, 1, 1, 1, 2, 0, 0, 0, 0, 2, 1, 1, 1, 2, 0, 0, 0,
0, 0, 0, 2, 2, 1, 2, 2, 0, 0, 0, 0, 2, 2, 1, 2, 2, 0, 0, 0,
0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 2, 2, 2, 2, 1, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 1, 2, 1, 1, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 0, 0,
0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0,
0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0,
0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0,
0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0,
0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0,
0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 2, 1, 2, 0, 0, 2, 1, 2, 0, 0, 2, 1, 2, 0, 0, 2, 1, 2, 0,
0, 1, 2, 1, 0, 0, 1, 2, 1, 0, 0, 1, 2, 1, 0, 0, 1, 2, 1, 0,
0, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 2, 1, 2, 0, 0, 2, 1, 2, 0, 0, 2, 1, 2, 0, 0, 2, 1, 2, 0,
0, 2, 2, 1, 0, 0, 1, 2, 1, 0, 0, 1, 2, 1, 0, 0, 1, 2, 2, 0,
0, 2, 1, 2, 0, 0, 2, 1, 2, 0, 0, 2, 1, 2, 0, 0, 2, 1, 2, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 2, 2, 1, 1, 2, 2, 0, 0, 0, 0, 2, 2, 1, 1, 2, 2, 0, 0,
0, 0, 2, 1, 1, 1, 1, 2, 0, 0, 0, 0, 2, 1, 1, 1, 1, 2, 0, 0,
0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0,
0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0,
0, 0, 2, 1, 1, 1, 1, 2, 0, 0, 0, 0, 2, 1, 1, 1, 1, 2, 0, 0,
0, 0, 2, 2, 1, 1, 2, 2, 0, 0, 0, 0, 2, 2, 1, 1, 2, 2, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0,
0, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 0,
0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 0,
1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1,
0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0,
0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0,
0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0,
0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0,
0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0,
2, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0,
0, 0, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 0,
0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0,
2, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0,
0, 0, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 0,
0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0,
2, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0,
0, 0, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 0,
0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0,
2, 1, 2, 2, 1, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2, 1, 2, 2, 1, 2,
2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2,
2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2
};

grid = new int[numx * numy + 1];
for(int b=0; b<numy; b++)
for(int a=1; a <= numx; a++)
if(b== 2 || b == 3 || b == 7 || b == 8)
grid[a + b * numx]=1;
else if(b== 4 || b == 9)
grid[a + b * numx]=4;
else
grid[a + b * numx]=0;



gridhits = new int[numx * numy + 1];
for(int a=1; a <= numx * numy; a++)
gridhits[a]=0;


}

}// end class bricks


}// end frame class

