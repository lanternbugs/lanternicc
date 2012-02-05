/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2002 Alexander Maryanovsky.
 * All rights reserved.
 *
 * The utillib library is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * The utillib library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with utillib library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package free.util.audio;

import java.util.Hashtable;
import free.util.PlatformUtils;
import free.util.BlockingQueue;


/**
 * This is an AudioPlayer implementation which uses the Applet.newAudioClip(URL)
 * API introduced in JDK1.2
 */

public class AppletAudioPlayer implements AudioPlayer, Runnable{
  
  
  
  /**
   * The current thread playing the sound.
   */

  private Thread playerThread = null;



  /**
   * Maps free.util.audio.AudioClip instances to java.applet.AudioClip
   * instances.
   */

  private final Hashtable audioClips = new Hashtable();
  
  
  
  
  /**
   * A BlockingQueue of AudioClips queued for playing.
   */

  private final BlockingQueue clipQueue = new BlockingQueue();




  /**
   * Returns true if we're running under Java 1.2 or later.
   */

  public boolean isSupported(){
    return PlatformUtils.isJavaBetterThan("1.2");
  }




  /**
   * Plays the given AudioClip.
   */

  public void play(AudioClip clip){
    if (playerThread == null){
      playerThread = new Thread(this, "AppletAudioPlayer");
      playerThread.setDaemon(true);
      playerThread.start();
    }
    
    clipQueue.push(clip);
  }
  
  
  
  /**
   * An infinite loop playing clips pushed to the clip queue.
   */
   
  public void run(){
    while (true){
      AudioClip audioClip;
      try{
        audioClip = (AudioClip)clipQueue.pop();
      } catch (InterruptedException e){
        e.printStackTrace();
        return;
      }
    
      java.applet.AudioClip newAudioClip = (java.applet.AudioClip)audioClips.get(audioClip);
      if (newAudioClip == null){
        newAudioClip = java.applet.Applet.newAudioClip(audioClip.getURL());
        audioClips.put(audioClip, newAudioClip);
      }
      
      newAudioClip.play();
    }
  }


}

