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

package free.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;


/**
 * Various image related utilities.
 */

public class ImageUtilities{



  /**
   * A constant indicating that the loading of the image completed.
   */

  public static final int COMPLETE = 1;



  /**
   * A constant indicating that the loading of the image errored.
   */

  public static final int ERRORED = 2;



  /**
   * A constant indicating that the loading of the image was aborted.
   */

  public static final int ABORTED = 3;


  
  /**
   * A constant indicating that the loading of the image was interrupted.
   */

  public static final int INTERRUPTED = 4;

  

  /**
   * Starts loading the given image, returns only when it's done loading.
   * Note that it's much more efficient to preload a lot of images at once using
   * the preload(Image []) method instead of this one.
   *
   * @return The result of the image loading, either {@link #COMPLETE},
   * {@link #ERRORED}, {@link #ABORTED} or {@link #INTERRUPTED}.
   *
   * @see #preload(java.awt.Image [], int [])
   */

  public static int preload(Image image){
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    // Check if already loaded
    if ((toolkit.checkImage(image, -1, -1, null) & ImageObserver.ALLBITS) != 0)
      return COMPLETE;
    
    Object lock = new Object();
    synchronized(lock){
      while (true){
        ImageLoadObserver observer = new ImageLoadObserver(lock);
        toolkit.prepareImage(image, -1, -1, observer);
        int result = toolkit.checkImage(image, -1, -1, null);
        if ((result & ImageObserver.ALLBITS) != 0)
          return COMPLETE;
        if ((result & ImageObserver.ERROR) != 0)
          return ERRORED;
        if ((result & ImageObserver.ABORT) != 0)
          return ABORTED;
        
        try{
          lock.wait();
          return observer.getResult();
        } catch (InterruptedException e){
            return INTERRUPTED;
          }
      }
    }
  }



  /**
   * Starts loading the given images, returns only when all the images are done
   * loading. If you just need to preload one Image, use the preload(Image) method
   * instead.
   *
   * @return An array specifying the loading result of each image. Possible values
   * are {@link #COMPLETE}, {@link #ERRORED} and {@link #ABORTED}.
   *
   * @see #preload(Image)
   */

  public static int [] preload(Image [] images, int [] results){
    Object [] locks = new Object[images.length];
    ImageLoadObserver [] loadObservers = new ImageLoadObserver[images.length];
    if ((results == null) || (results.length < images.length))
      results = new int[images.length];
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    for (int i = 0; i < images.length; i++){
      locks[i] = new Object();
      loadObservers[i] = new ImageLoadObserver(locks[i]);
      toolkit.prepareImage(images[i], -1, -1, loadObservers[i]);
    }

    for (int i = 0; i < images.length; i++){
      synchronized(locks[i]){
        int result = toolkit.checkImage(images[i], -1, -1, null);

        if ((result & ImageObserver.ALLBITS) != 0){
          results[i] = COMPLETE;
          continue;
        }
        if ((result & ImageObserver.ERROR) != 0){
          results[i] = ERRORED;
          continue;
        }
        if ((result & ImageObserver.ABORT) != 0){
          results[i] = ABORTED;
          continue;
        }
        
        try{
          locks[i].wait();
          results[i] = loadObservers[i].getResult();
        } catch (InterruptedException e){
            results[i] = INTERRUPTED;
          }
      }
    }
    
    return results;
  }
  
  
  
  /**
   * Returns whether the specified image is already fully loaded.
   */
  
  public static boolean isLoaded(Image image){
    return (Toolkit.getDefaultToolkit().checkImage(image, -1, -1, null) & ImageObserver.ALLBITS) != 0;
  }
  
  
  
  /**
   * This class is an implementation of ImageObserver which notifies a given
   * lock when loading of the Image is done. This can be used to wait until a
   * certain Image has finished loading.
   */

  public static class ImageLoadObserver implements ImageObserver{


    /**
     * The lock.
     */

    private final Object lock;



    /**
     * The loading result.
     */

    private int result = -1;




    /**
     * Creates a new ImageLoadObserver which will notify the given lock when
     * the Image is done loading.
     */

    public ImageLoadObserver(Object lock){
      this.lock = lock;
    }



    /**
     * If infoflags has the ALLBITS flag set, notifies the lock and returns
     * false, otherwise simply returns true.
     */

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height){
      synchronized(lock){
        if ((infoflags & ALLBITS)!=0){
          result = ImageUtilities.COMPLETE;
          lock.notify();
          return false;
        }
        if ((infoflags & ERROR)!=0){
          result = ImageUtilities.ERRORED;
          lock.notify();
          return false;
        }
        if ((infoflags & ABORT)!=0){
          result = ImageUtilities.ABORTED;
          lock.notify();
          return false;
        }
      }
      return true;
    }




    /**
     * Returns the result of the image loading process or -1 if loading hasn't finished yet.
     * Possible values are {@link ImageUtilities#COMPLETE}, {@link ImageUtilities#ERRORED}
     * and {@link ImageUtilities#ABORTED}
     */

    public int getResult(){
      return result;
    }

  }

 
}
