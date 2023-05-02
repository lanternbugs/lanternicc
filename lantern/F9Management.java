package lantern;

/*
 *  Copyright (C) 2012 Michael Ronald Adams, Andrey Gorlin.
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

import java.util.ArrayList;
import java.util.List;


public class F9Management {
    private List<String> list;
    //private int head;
    //private int tail;
    private int index;
    //private int max;


    /**
     * Constructor
     **/
    public F9Management() {
        list = new ArrayList<String>();
        //head = -1;
        //tail = -1;
        index = 0;
        //max = 100;
    }

    public String getNameReverse(boolean empty) {
        int len = list.size();
        // if input is empty reset iterator and return tail
        // otherwise iterate one and return item
        if (len == 0)
            return "";
    /*
    if (empty) {// no text in input
      index = tail;
    } else {
      // index is set to 1 more than tail when at initial position
      index++;
      if (index > tail)
        index = 0;
    }
    */
        index = (empty ? len - 1 : (index + 1) % len);

        return list.get(index);
    }

    public String getName(boolean empty) {
        int len = list.size();
        // if input is empty reset iterator and return tail
        // otherwise iterate one and return item
        if (len == 0)
            return "";
   /*
   if (empty) {// no text in input
     index = tail;
   } else {
     // index is set to 1 more than tail when at initial position
     index--;
     if (index < 0)
       index = tail;
   }
   */
        index = (empty ? len - 1 : (index + (len - 1)) % len);

        return list.get(index);
    }

    public void addName(String mes) {
        // add to queue, delete if more than 10 last of commands, reset iterator
        if (mes.equals(""))
            return;

        list.remove(mes);
        list.add(mes);
        index = 0;
   /*
   if (tail>-1) {
     String match = list.get(tail);
     if (match.equals(mes))
       return; // this mess was allready last
   }
   
   String lastguy;
   if (tail > -1) {
     lastguy = list.get(tail);
     if (mes.equals(lastguy))
       return;
   }
   
   list.add(mes);
   if (head == -1) {
     head = 0;
     tail = 0;
     index = 1;
     return;

   } else if (tail < max -1) {
     tail++;
     index = tail+1;

   } else {
     list.remove(0);
     index = tail+1;
   }
   */
    }
}// end f9
