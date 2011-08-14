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
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class listClass {
JList theList;
List eventList;
DefaultListModel model;

List eventListData;
DefaultListModel modeldata;
DefaultListModel modeljoin;
DefaultListModel modelinfo;
DefaultListModel modelwatch;

// type used in game list, type1 is history search etc. type2 is argument
String type1;
String type2;

listClass(String type){
eventList = new ArrayList<String>();
    eventList.add(type);
eventListData = new ArrayList<String>();
    eventListData.add(type);

type1="none";
type2="none";


model = new DefaultListModel();
modeldata = new DefaultListModel();
modeljoin = new DefaultListModel();
modelinfo = new DefaultListModel();
modelwatch = new DefaultListModel();


 int i;
 for (i=0; i<eventList.size(); i++)
 { model.add(i, eventList.get(i));
  modeldata.add(i, eventListData.get(i));
  modeljoin.add(i, eventList.get(i));
  modelinfo.add(i, eventList.get(i));
  modelwatch.add(i, eventList.get(i));

  }



/* for(int ii=i; ii<200; ii++)
 model.add(ii, "" + ii);
*/
}// end constructor


void addToList(String entry, String number)
{
	model.add(model.size(), entry);
	modeldata.add(modeldata.size(), number);
}

void removeFromList(String index)
{
	try {

	for(int i=0; i < model.size(); i++)
	if(modeldata.elementAt(i).equals(index))
	{model.remove(i);
	 modeldata.remove(i);
 	}
}catch(Exception e){}

}
void addToEvents(String entry, String number, String join, String watch, String info)
{
	int add=1;
	if(join.equals(""))
	join="!!!";

	if(watch.equals(""))
	watch = "!!!";
 
 //        if(entry.contains("[VIDEO]") && info.equals(""))
 //        return;

	if(info.equals(""))
	info = "!!!";

	for(int i=0; i < model.size(); i++)
	if(modeldata.elementAt(i).equals(number))
	{ model.set(i,  entry);
	  modeljoin.set(i,  join);
	  modelinfo.set(i,  info);
	  modelwatch.set(i,  watch);
		return;
	}

	// we will use this but for now below model.add(model.size(), entry);
	model.add(model.size(), entry);
	modeljoin.add(modeljoin.size(), join);
	modelinfo.add(modelinfo.size(), info);
	modelwatch.add(modelwatch.size(), watch);
	modeldata.add(modeldata.size(), number);

}
void removeFromEvents(String index)
{
	for(int i=0; i < model.size(); i++)
	if(modeldata.elementAt(i).equals(index))
	{model.remove(i);
	modelinfo.remove(i);
	modeljoin.remove(i);
	modelwatch.remove(i);
	 modeldata.remove(i);
 	}


}
void resetList()
{
	try {
		for(int i=model.size()-1; i >0; i--)
	{model.remove(i);
	}

	for(int i=modeldata.size()-1; i >0; i--)
	{
	 modeldata.remove(i);
 	}
	for(int i=modeljoin.size()-1; i >0; i--)

	{modeljoin.remove(i);

 	}
	for(int i=modelinfo.size()-1; i >0; i--)
	{modelinfo.remove(i);

 	}

	for(int i=modelwatch.size()-1; i >0; i--)
	{modelwatch.remove(i);

 	}

}
catch(Exception e){}


}

void notifyStateChanged(String name, String state)
{
	int add=1;
	String notify_string=name;
	if(state.equals("P"))
	notify_string=name + " (Playing)";
	if(state.equals("E"))
	notify_string=name + " (Examining)";
	if(state.equals("S"))
	notify_string=name + " (In Simul)";

	for(int i=0; i < model.size(); i++)
	if(modeldata.elementAt(i).equals(name))
	{ model.set(i,  notify_string);
		return;
	}

	model.add(model.size(), notify_string);
	modeldata.add(modeldata.size(), name);



}

String getOfferNumber(int index)
{
	String offer="-1";
	try{
		offer = (String)modeldata.elementAt(index);
	}
	catch(Exception D){}
	return offer;

}

String getJoinCommand(int index)
{
	String command="-1";
	try{
		command = (String)modeljoin.elementAt(index);
	}
	catch(Exception D){}
	return command;

}
String getInfoCommand(int index)
{
	String command="-1";
	try{
		command = (String)modelinfo.elementAt(index);
	}
	catch(Exception D){}
	return command;

}
String getWatchCommand(int index)
{
	String command="-1";
	try{
		command = (String)modelwatch.elementAt(index);
	}
	catch(Exception D){}
	return command;

}

String getEventListing(int index)
{
	String command="-1";
	try{
		command = (String)model.elementAt(index);
	}
	catch(Exception D){}
	return command;

}



}// end class