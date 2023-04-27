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

import java.util.EventListener;
import java.lang.reflect.Array;


/**
 * A convenient storage place for Listeners. The listeners are stored in the
 * same manner as in javax.swing.event.EventListenerList.
 * <STRONG>Note:</STRONG> This class is not thread safe.
 */

public class EventListenerList{


  /**
   * The array where we keep the listeners and their types.
   */

  private Object [] listenerList = new Object[0];



  /**
   * Adds the given listener as a listener of the given type.
   */

  public void add(Class listenerType, EventListener listener){
    if (listener==null)
      return;
    if (!listenerType.isInstance(listener))
      throw new IllegalArgumentException("The listener is not an instance of the listener class.");

    Object [] tempArr = new Object[listenerList.length+2];
    System.arraycopy(listenerList,0,tempArr,0,listenerList.length);

    tempArr[tempArr.length-2] = listenerType;
    tempArr[tempArr.length-1] = listener;

    listenerList = tempArr;
  }



  
  /**
   * Removes the given listener as a listener of the specified type.
   */

  public void remove(Class listenerType, EventListener listener){
    if (listener == null)
      return;
    if (!listenerType.isInstance(listener))
      throw new IllegalArgumentException("The listener is not an instance of the listener class.");

    for (int i=0;i<listenerList.length;i+=2){
      if ((listenerList[i]==listenerType)&&(listenerList[i+1].equals(listener))){
        Object [] tempArr = new Object[listenerList.length-2];
        System.arraycopy(listenerList,0,tempArr,0,i);
        System.arraycopy(listenerList,i+2,tempArr,i,listenerList.length-i-2);
        listenerList = tempArr;
        return;
      }
    }
  }



  /**
   * Returns the total number of listeners for this listener list.
   */

  public int getListenerCount(){
    return listenerList.length;
  }



  /**
   * Returns the amount of listeners of the specified type in this listener
   * list.
   */

  public int getListenerCount(Class listenerType){
    int count = 0;
    for (int i=0;i<listenerList.length;i+=2){
      if (listenerList[i]==listenerType)
        count++;
    }
    return count;
  }



  /**
   * Returns an array containing all the listeners in this listener list. The
   * array contains listeners and their types in the following format: the
   * element at index 2*N is the type of the listener at index 2*N+1
   * <B>WARNING!!!</B> Absolutely NO modification of the data contained in this array
   * should be made -- if any such manipulation is necessary, it should be done on a
   * copy of the array returned rather than the array itself.
   */

  public Object [] getListenerList(){
    return listenerList;
  }



  /**
   * Returns an array of listeners registered as the listeners of the given
   * type. Note that the returned array is an array of the actual given type
   * so it can be safely casted to that type once instead of casting each of the
   * listener.
   */

  public EventListener [] getListeners(Class listenerType){
    EventListener [] listeners = (EventListener [])Array.newInstance(listenerType,getListenerCount(listenerType));
    int count = 0;
    for (int i=0;i<listenerList.length;i+=2){
      if (listenerType==listenerList[i])
        listeners[count++] = (EventListener)listenerList[i+1];
    }
    return listeners;
  }


}
