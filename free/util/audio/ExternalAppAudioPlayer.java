/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2004 Alexander Maryanovsky.
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

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import free.util.BlockingQueue;
import free.util.IOUtilities;


/**
 * This is an AudioPlayer implementation which uses some external application
 * to play audio. The player attempts to use a script named "play", which is
 * run from the current directory without any arguments. The script is expected
 * to read audio data from stdin, play it, and then terminate when its stdin is
 * closed. 
 */

public class ExternalAppAudioPlayer implements Runnable, AudioPlayer{

  

  /**
   * The current thread playing the sound.
   */

  private Thread playerThread = null;
  
  
  
  /**
   * A BlockingQueue of queued AudioClips.
   */

  private final BlockingQueue clipQueue = new BlockingQueue();
  


  /**
   * Returns <code>true</code>.
   */

  public boolean isSupported(){
    try{
      Process testPlayer = createPlayer();
      if (testPlayer != null){
        testPlayer.destroy();
        return true;
      }
      else
        return false;
    } catch (SecurityException e){
        return false;
      }
      catch (IOException e){
        return false; 
      }
  }

  
  
  /**
   * Plays the given AudioClip.
   */

  public synchronized void play(AudioClip clip) throws java.io.IOException{
    // Lazily initialize player thread.
    if (playerThread == null){
      playerThread = new Thread(this, "ExternalAppAudioPlayer");
      playerThread.setDaemon(true);
      playerThread.start();
    }

    clipQueue.push(clip);
  }
  
  
  
  /**
   * Creates the process that will play audio.
   */
   
  private Process createPlayer() throws IOException{
    return Runtime.getRuntime().exec("./play");
  }



  /**
   * <code>Runnable</code> implementation. Plays the queued clips.
   */

  public void run(){
    while (true){
      InputStream err = null;
      try{
        Process player = createPlayer();
        err = player.getErrorStream();
        OutputStream out = player.getOutputStream();
      
        AudioClip clip;
        try{
          clip = (AudioClip)clipQueue.pop();
        } catch (InterruptedException e){
            e.printStackTrace();
            return;
          }

        byte [] data = clip.getData();
        out.write(data);
        out.flush();
        out.close();
      } catch (IOException e){
          try{
            IOUtilities.pump(err, System.out);
          } catch (IOException ex){}
          e.printStackTrace();
          synchronized(this){
            playerThread = null;
            return;
          }
        }
    }
  }



}

