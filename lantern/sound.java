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
import  sun.audio.*;    //import the sun.audio package

class Sound extends JApplet
    {
 private AudioClip song; // Sound player
 private URL songPath; // Sound path
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
      try
      {
		 songPath=songPath1;
		 /*play player = new play();
		 Thread t = new Thread(player);
	t.start(); */
	
	AudioStream as = new AudioStream (songPath.openStream());
        AudioPlayer.player.start(as);

	//song = Applet.newAudioClip(songPath); // Load
    //song.play();


      }
      catch(Exception e){} // Satisfy the catch
 }

 public void playURL(URL song)
 {

 }

 public void play()
 {
     song.play(); // Play once
     /* use song.loop(); to loop
     and song.stop(); to stop */

 }




 class play implements Runnable {

	 public void run()
	{
    song = Applet.newAudioClip(songPath); // Load
    song.play();
	}
}// end play class

}// end sound