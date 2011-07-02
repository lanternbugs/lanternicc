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
import java.applet.*;
import java.awt.event.*;
import java.util.StringTokenizer;

class saveScores {


	String aFile;
	String aFileLinux;


	saveScores(){

			aFile= "multi_mine_settings.txt";



		} // constructor

	void saveNow(channels sharedVariables)
	{

	String set_string = "";
	FileWrite out = new FileWrite();
	int a=0;

	set_string= set_string + "[9]::::";
	for(a=0; a < sharedVariables.mineScores.score9by9.top; a++)
		set_string=set_string + "///" +  sharedVariables.mineScores.score9by9.list.get(a).score + "##" + sharedVariables.mineScores.score9by9.list.get(a).name + "##" + sharedVariables.mineScores.score9by9.list.get(a).date + "##::::";
	set_string = set_string + "[done9]::::";


	set_string= set_string + "[16]::::";
	for(a=0; a < sharedVariables.mineScores.score16by16.top; a++)
		set_string=set_string + "///" +  sharedVariables.mineScores.score16by16.list.get(a).score + "##" + sharedVariables.mineScores.score16by16.list.get(a).name + "##" + sharedVariables.mineScores.score16by16.list.get(a).date + "##::::";
	set_string = set_string + "[done16]::::";


	set_string= set_string + "[30]::::";
	for(a=0; a < sharedVariables.mineScores.score16by30.top; a++)
		set_string=set_string + "///" +  sharedVariables.mineScores.score16by30.list.get(a).score + "##" + sharedVariables.mineScores.score16by30.list.get(a).name + "##" + sharedVariables.mineScores.score16by30.list.get(a).date + "##::::";
	set_string = set_string + "[done30]::::";

	set_string= set_string + "[Custom]::::";
	for(a=0; a < sharedVariables.mineScores.scoreCustom.top; a++)
		set_string=set_string + "///" +  sharedVariables.mineScores.scoreCustom.list.get(a).score + "##" + sharedVariables.mineScores.scoreCustom.list.get(a).name + "##" + sharedVariables.mineScores.scoreCustom.list.get(a).date + "##::::";
	set_string = set_string + "[doneCustom]::::";


	out.write(set_string);

	}

	void readNow(channels sharedVariables)
	{
String dummy="";
		try {
			FileRead in = new  FileRead();

			//StringTokenizer tokens = new StringTokenizer(in.read(), "::::");

			String theDump = in.read();
			String [] theDumpArray = theDump.split("::::");

			String line="";
			boolean go=true;
			int state=0;
			int ii=0;


			for(ii=0; ii<theDumpArray.length; ii++)
			{
				line = theDumpArray[ii];

				dummy +=line + ii;
				if(line == null)
					break;
				if(line.equals(""))
					break;

				if(line.equals("[9]"))
				{
					state=1;
					continue;
				}
				if(line.equals("[done9]"))
				{
					state=0;
					continue;
				}

				if(line.equals("[16]"))
				{
					state=2;
					continue;
				}
				if(line.equals("[done16]"))
				{
					state=0;
					continue;
				}
				if(line.equals("[30]"))
				{
					state=3;
					continue;
				}
				if(line.equals("[done30]"))
				{
					state=0;
					continue;
				}

				if(line.equals("[Custom]"))
				{
					state=4;
					continue;
				}
				if(line.equals("[doneCustom]"))
				{
					state=0;
					continue;
				}

				if(line.startsWith("///"))
				{
				highScoreManagement scoring;
				if(state == 1)
					scoring = sharedVariables.mineScores.score9by9;
				else if(state == 2)
					scoring = sharedVariables.mineScores.score16by16;
				else if(state == 3)
					scoring = sharedVariables.mineScores.score16by30;
				else if(state == 4)
					scoring = sharedVariables.mineScores.scoreCustom;
				else
					 continue;

				int n=line.indexOf("##");
				String name;
				String date;
				String time;
				int scoreTime=0;

				if(n != -1)
				{
					// \\\score##name##date##

					time=line.substring(3, n);
					try { scoreTime = Integer.parseInt(time); } catch(Exception d){}

					int m = line.indexOf("##", n+1);
					if(m != -1)
					{
						name=line.substring(n+2, m);

						int p = line.indexOf("##", m+1);
						if(p != -1)
						{
							date=line.substring(m+2, p);

							scoring.addScore(scoreTime, date, name);
						}// third index true
					}// second index true

				}// first index true



				}


			}

		}
		catch(Exception ddd){}

	/*	JFrame frame = new JFrame();
		JPanel mypanel = new JPanel();
		frame.add(mypanel);
		JLabel lab = new JLabel(dummy);
		mypanel.add(lab);
		frame.setSize(500,500);
		frame.setVisible(true);

	*/
	}// end method readNow


class FileWrite
{

   void write(String s)
   {

    		// Create file
    		try {
					FileWriter fstream = new FileWriter(aFile);
					write2(fstream, s);
				}
			catch(Exception e)
			{

			}// end catch

  }// end method

void write2(FileWriter fstream, String s)
{
	       try {
			   BufferedWriter out = new BufferedWriter(fstream);
	    		out.write(s);
	    		//Close the output stream
	    		out.close();
			}
			catch(Exception e)
			{  }

}

}// end class

class FileRead
{

   String read()
   {String s = "";
		try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      BufferedReader input=null;

      try {
		  	input=  new BufferedReader(new FileReader(aFile));
		  }
      catch(Exception ee)
      {

	input = null;

      }  // endcatch



      try {
        String line = null; //not declared within while loop
        /*
        * readLine is a bit quirky :
        * it returns the content of a line MINUS the newline.
        * it returns null only for the END of the stream.
        * it returns an empty String if two newlines appear in a row.
        */
        while (( line = input.readLine()) != null){
         s+=line;

        }
      }
    catch (IOException ex){     }
    finally
    {
        input.close();
    }// end finally
}// overall try
catch(Exception eeee)
{ }// overall catch

    return s.toString();

  }// end method  read
}// end file read class

}// end class saveScores
