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

class messageStyles {
   // if no data blocks[0] is tell.length() and top is 1. we presume spot 0 is end spot starting from 0
  int [] blocks = new int[100];
  int top;
  int [] boldSpots = new int[100];
  int [] italicSpots = new int[100];
  int [] underlineSpots = new int[100];
  Color [] colors = new Color[100];
  
  int start;
  int stop;
  
}



