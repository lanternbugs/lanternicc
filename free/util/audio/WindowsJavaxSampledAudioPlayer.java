/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2006 Alexander Maryanovsky.
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

import javax.sound.sampled.*;

import free.util.PlatformUtils;


/**
 * A subclass of <code>JavaxSampledAudioPlayer</code> which tries to run perfectly under MS Windows.  
 */

public class WindowsJavaxSampledAudioPlayer extends JavaxSampledAudioPlayer{
  
  
  
  /**
   * Returns whether we're running under MS Windows.
   */

  public boolean isSupported(){
    return super.isSupported() && PlatformUtils.isWindows();
  }
  


  /**
   * <code>Runnable</code> implementation. Plays the queued clips.
   */

  public void run(){
    while (true){
      try{
        AudioClip audioClip = (AudioClip)clipQueue.pop();
        
        byte [] data = audioClip.getData();
        AudioFormat format = getFormatForPlaying(data);
        data = convertAudioData(data, format);
        
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        
        Clip clipLine = (Clip)AudioSystem.getLine(info);
        clipLine.open(format, data, 0, data.length);
        clipLine.addLineListener(new LineListener(){
          public void update(LineEvent evt){
            if (evt.getType() == LineEvent.Type.STOP)
              evt.getLine().close();
          }
        });
        clipLine.loop(0);
      } catch (IOException e){
        e.printStackTrace();
      } catch (UnsupportedAudioFileException e){
        e.printStackTrace();
      } catch (LineUnavailableException e){
        e.printStackTrace();
      } catch (IllegalArgumentException e){
        e.printStackTrace();
      } catch (Throwable t){
        t.printStackTrace();
      }
    }
    
  }
  
  
  
}
