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

import java.applet.AppletContext;
import free.util.BlockingQueue;


/**
 * An <code>AudioPlayer</code> which uses an <code>AppletContext</code> (given
 * to it by external code, obviously) to play audio clips.
 */
 
public class AppletContextAudioPlayer implements AudioPlayer, Runnable{
  
  
  
  /**
   * The <code>AppletContext</code>.
   */
   
  private static AppletContext appletContext = null;
  
  
  
  /**
   * Our clip playing thread.
   */
   
  private Thread playerThread = null;
  

  
  /**
   * A BlockingQueue of AudioClips queued for playing.
   */

  private final BlockingQueue clipQueue = new BlockingQueue();
  
  
  
  
  /**
   * Returns <code>true</code> if our applet context has been set. 
   */
   
  public boolean isSupported(){
    return getAppletContext() != null;
  }
  
  
  
  /**
   * Sets the <code>AppletContext</code> we use to play audio clips.
   */
   
  public synchronized static void setAppletContext(AppletContext appletContext){
    if (appletContext == null)
      throw new IllegalArgumentException("The specified AppletContext may not be null");
    
    AppletContextAudioPlayer.appletContext = appletContext;
  }
  
  
  
  /**
   * Returns the <code>AppletContext</code> we use to play audio clips.
   */
   
  public synchronized static AppletContext getAppletContext(){
    return appletContext;
  }
  
  
  
  /**
   * Plays the specified <code>AudioClip</code>. 
   */
  
  public synchronized void play(AudioClip clip){
    // Lazily initialize the player thread.
    if (playerThread == null){
      playerThread = new Thread(this, "AppletContextAudioPlayer");
      playerThread.setDaemon(true);
      playerThread.start();
    }

    clipQueue.push(clip);
  }
  
  
  
  /**
   * Runs in an infinite loop, fetching audio clips from the queue and plays
   * them.
   */
   
  public void run(){
    while (true){
      try{
        AudioClip audioClip = (AudioClip)clipQueue.pop();
        getAppletContext().getAudioClip(audioClip.getURL()).play();
      } catch (InterruptedException e){
        e.printStackTrace();
        return;
      }
    }
  }
  
  
   
}