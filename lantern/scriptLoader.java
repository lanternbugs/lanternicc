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
import javax.swing.text.*;
import java.io.*;
import java.net.*;
import java.applet.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class scriptLoader {


scriptLoader()
{


}
/* opens file and loads line by line into array list */
/* connect can run script */
void loadScript(ArrayList<String> myArray, String fileName)
{
      try{

    FileInputStream fstream = new FileInputStream(fileName);
    // Get the object of DataInputStream
    DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String strLine;
    //Read File Line By Line
    while ((strLine = br.readLine()) != null)   {
      myArray.add(strLine.trim());
    }
    //Close the input stream
    in.close();
    }catch (Exception e){// do nothing on exception

    }

}




}