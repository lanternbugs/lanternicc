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

import java.awt.*;
import java.util.ArrayList;


class F9Management
{
 ArrayList list;
 int head;
 int tail;
 int index;
 int max;


 F9Management()
 {
  list = new ArrayList();
 head=tail=index-1;
 max=10;
 }


 String getNameReverse(boolean empty)
 {
  // if input is empty reset iterator and return tail
  // otherwise iterate one and return item
  if(head == -1)
  return "";
  if(empty == true)// no text in input
  index = tail;
  else
  {
   // index is set to 1 more than tail when at initial position
   index ++;
   if(index >tail)
   index=head;
  }
   return (String)list.get(index);
 }// end up


 String getName(boolean empty)
 {
  // if input is empty reset iterator and return tail
  // otherwise iterate one and return item
  if(head == -1)
  return "";
  if(empty == true)// no text in input
  index = tail;
  else
  {
   // index is set to 1 more than tail when at initial position
   index --;
   if(index < 0)
   index=tail;
  }
   return (String)list.get(index);
 }// end up
 void addName(String mes)
 {
   // add to queue, delete if more than 10 last of commands, reset iterator
  if(mes.equals(""))
  return;

  if(tail>-1)
  {
	  String match=(String)list.get(tail);
	  if(match.equals(mes))
	  	return; // this mess was allready last

  }
  String lastguy;
  if(tail > -1)
  {
	  lastguy = (String)list.get(tail);
	  if(mes.equals(lastguy))
	  return;

  }
  list.add(mes);
  if(head == -1)
  {
   head=tail=0;
   index=1;
   return;
  }
  else if(tail < max -1)
 {
   tail++;
   index=tail+1;
 }
 else
 {
  list.remove(0);
  index=tail+1;
 }


   // add to queue, delete if more than 10 last of commands, reset iterator
 }// end add

}// end f9
