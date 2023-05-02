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

import javax.swing.*;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class nameListClass {
    List theList;
    DefaultListModel model;
    DefaultListModel model2;

    String channel;

    nameListClass() {


        model = new DefaultListModel();
        model2 = new DefaultListModel();


    }// end constructor


    void addToList(String name) {
        ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        Lock readLock = rwl.readLock();
        try {
            readLock.lock();
            addToModel(name);
            addToModel2(name);
            readLock.unlock();
        } catch (Exception dui) {
        }
    }

    void addToModel2(String name) {


        try {
            if (name.contains(" ")) {


                model2.add(0, name);
                return;
            }
            int num = Integer.parseInt(name);
            model2.add(0, name);

        } catch (Exception d) {
            try {
                model2.add(1, name);
            } catch (Exception z) {
            }
        }
    }

    void addToModel(String name) {

        for (int a = 0; a < model.size(); a++)
            if (model.elementAt(a).toString().compareToIgnoreCase(name) > 0) {
                model.add(a, name);
                return;
            }

        model.add(model.size(), name);

    }

    void removeFromList(String shortName) {
        ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        Lock readLock = rwl.readLock();
        try {
            readLock.lock();
            for (int i = 0; i < model.size(); i++)
                if (model.elementAt(i).equals(shortName)) {
                    model.remove(i);
                    break;
                }
            for (int i = 0; i < model2.size(); i++)
                if (model2.elementAt(i).equals(shortName)) {
                    model2.remove(i);
                    break;
                }
            readLock.unlock();
        } catch (Exception e) {
        }
    }// end method remove


    boolean isOnList(String shortName) {
        try {

            for (int i = 0; i < model.size(); i++) {
                String temp = (String) model.elementAt(i);
                if (temp.toLowerCase().equals(shortName.toLowerCase()))
                    return true;

            }


        } catch (Exception e) {
        }
        return false;
    }// end method is on liast


    void clearList() {
        try {

            for (int i = model.size() - 1; i > 0; i--)
                model.remove(i);
            for (int i = model2.size() - 1; i > 0; i--)
                model2.remove(i);


        } catch (Exception e) {
        }
    }// end method remove


}// end class

