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
import java.util.Calendar;
import java.util.StringTokenizer;
import java.math.BigInteger;
import java.util.ArrayList;

class FocusOwner {

boolean console;
boolean board;
int number;

FocusOwner(channels sharedVariables, subframe [] consoleSubframes, gameboard [] myboards)
{

    console=false;/// if it doesnt set one of these to true nobody gets it
    number=0;
    board=false;

    try {


    for(int a=0; a<sharedVariables.openConsoleCount; a++)
    {
        if(consoleSubframes[a].overall.Input.hasFocus())
        {
            console=true;
            number=a;
            //writeToConsole("console is true and number is " + a + "\n");
            return;
        }
    }// end for

    for(int a=0; a<sharedVariables.openBoardCount; a++)
    {
        if(myboards[a].myconsolepanel.Input.hasFocus())
        {
            board=true;
            number=a;
            return;

        }
    }

}// end try
catch(Exception e){}

}// end constructor


}// end class focus owner.
