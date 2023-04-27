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

import sun.audio.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import free.util.BlockingQueue;


/**
 * An implementation of AudioPlayer which uses the sun.audio classes to play
 * sounds. The sounds are played in a separate thread because there seems to be
 * a bug with sound-playing on win98 which causes the thread calling
 * <code>sun.audio.AudioPlayer.player.start</code> to get stuck waiting on some
 * lock.
 */

public class SunAudioPlayer implements AudioPlayer, Runnable{



  /**
   * The maximum amount of AudioClips we're allowed to queue.
   */

  private static final int MAX_QUEUE_SIZE = 2;



  /**
   * The current thread playing the sound.
   */

  private volatile Thread playerThread = null;



  /**
   * The queue holding AudioClips for the player thread to play.
   */

  private final BlockingQueue clipQueue = new BlockingQueue();




  /**
   * Returns true if the class "sun.audio.AudioPlayer" can be loaded.
   */

  public boolean isSupported(){
    try{
      return Class.forName("sun.audio.AudioPlayer") != null;
    } catch (ClassNotFoundException e){
        return false;
      }
  }




  /**
   * Plays the given AudioClip. Due to
   * bugs in mixing sound in sun.audio.AudioPlayer, calling this method while
   * an AudioClip is already playing will cause the new clip to be queued for
   * playing instead of mixed with the currently playing one as may happen with
   * other AudioPlayer implementations. The queue has an upper bound for its
   * size. Once that many AudioClips are queued, further requests will be
   * silently dropped.
   */

  public synchronized void play(AudioClip clip){
    if (clipQueue.size() == MAX_QUEUE_SIZE){
      System.err.println("Queue full, ignoring play request.");
      return; // Ignore.
    }

    if (playerThread==null){ // Lazily start the thread.
      playerThread = new Thread(this, "SunAudioPlayer");
      playerThread.setDaemon(true);
      playerThread.start();
    }

    clipQueue.push(clip);
  }




  /**
   * Plays the current <code>clipToPlay</code> when notified.
   */

  public void run(){
    try{
//      int timeToWait = 0;
      while (true){
//        if (timeToWait != 0)
//          Thread.sleep(timeToWait);
        AudioClip clip = (AudioClip)clipQueue.pop();
        byte [] data = clip.getData();
//        timeToWait = data.length/8;
        try{
          NativeAudioStream audioStream = new NativeAudioStream(new ByteArrayInputStream(data));
          sun.audio.AudioPlayer.player.start(audioStream);
        } catch (IOException e){
            return;
          }
      }
    } catch (InterruptedException e){}
  }


}
