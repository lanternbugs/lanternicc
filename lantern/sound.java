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

import java.applet.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import free.util.IOUtilities;
import free.util.audio.AudioClip;


class Sound extends JApplet
    {
 private  java.applet.AudioClip song; // Sound player
 private URL songPath; // Sound path
 String operatingSystem;
 Sound(String filename)
 {
     try
     {
   songPath = new URL(getCodeBase(),filename); // Geturl of sound
   song = Applet.newAudioClip(songPath); // Load
   song.play();
     }
     catch(Exception e){} // Satisfy the catch
 }


 void runSound(URL songPath)
 {

 }
 Sound(URL songPath1)
  {
    
    String os = System.getProperty("os.name").toLowerCase();
if (os.indexOf( "win" ) >= 0)
	operatingSystem = "win";
else if(os.indexOf( "mac" ) >= 0)
	operatingSystem = "mac";
else
	operatingSystem = "unix";

    if(operatingSystem.equals("unix") || !operatingSystem.equals("unix"))
    {
     try {
        free.util.audio.AudioClip unixClip = new  free.util.audio.AudioClip(songPath1);
      // playUnixSound(unixClip);
       unixClip.play();
     }
     catch(Exception dui){}
    }
    else 
    {

      try
      {
		 songPath=songPath1;
		 playNative player = new playNative();
		 Thread t = new Thread(player);
	         t.start();
	//song = Applet.newAudioClip(songPath); // Load
    //song.play();


      }
      catch(Exception e){} // Satisfy the catch

    }// end else
 }              // end method

 public void playURL(URL song)
 {

 }




 class playNative implements Runnable {

	 public void run()
	{
    song = Applet.newAudioClip(songPath); // Load
    song.play();
	}
}// end play class

}// end sound