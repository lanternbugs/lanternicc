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
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

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
// for events table

    String thisListType = "";
    public myDefaultTableModel eventsTable;
    public myDefaultTableModel topGamesTable;
    ImageIcon joinIcon;
    ImageIcon watchIcon;
    ImageIcon infoIcon;


    listClass(String type) {
        eventList = new ArrayList<String>();
        eventList.add(type);
        eventListData = new ArrayList<String>();
        eventListData.add(type);
        thisListType = type;
        type1 = "none";
        type2 = "none";


        model = new DefaultListModel();
        modeldata = new DefaultListModel();
        modeljoin = new DefaultListModel();
        modelinfo = new DefaultListModel();
        modelwatch = new DefaultListModel();


        int i;
        for (i = 0; i < eventList.size(); i++) {
            model.add(i, eventList.get(i));
            modeldata.add(i, eventListData.get(i));
            modeljoin.add(i, eventList.get(i));
            modelinfo.add(i, eventList.get(i));
            modelwatch.add(i, eventList.get(i));

        }

// we are storing events in both a table and list

        if (type.equals("Events") || type.equals("Tournaments")) {
            joinIcon = new ImageIcon(channels.eventsImages.get(0));
            watchIcon = new ImageIcon(channels.eventsImages.get(1));
            infoIcon = new ImageIcon(channels.eventsImages.get(2));

            Object[][] tableData = {{joinIcon, watchIcon, infoIcon, "Click the icon on events below for Join, Watch and Info"}};
            String[] tableCollumns = {"Join", "Watch", "Info", "Listing"};

            eventsTable = new myDefaultTableModel(tableCollumns, tableData);

            Object[][] tableData2 = {{"", watchIcon, "", "Click the eyeball to watch a top game."}};

            topGamesTable = new myDefaultTableModel(tableCollumns, tableData2);
        }
/* for(int ii=i; ii<200; ii++)
 model.add(ii, "" + ii);
*/
    }// end constructor


    void addToList(String entry, String number) {

        model.add(model.size(), entry);
        modeldata.add(modeldata.size(), number);

    }

    void removeFromList(String index) {
        try {

            for (int i = 0; i < model.size(); i++)
                if (modeldata.elementAt(i).equals(index)) {
                    model.remove(i);
                    modeldata.remove(i);
                }
        } catch (Exception e) {
        }

    }

    void addToEvents(String entry, String number, String join, String watch, String info) {
        CharSequence cs_tourn = "Tournament";
        CharSequence cs_manager = "manager";
        if (thisListType.equals("Tournaments") && (!entry.contains(cs_tourn) || !entry.contains(cs_manager))) {
            return;
        }
        int add = 1;

        int spot = -1;
        if (join.equals("") && info.equals("") && (watch.toLowerCase().startsWith("observe ") && !entry.startsWith("LIVE")))
            spot = 1;  // we place high rated games to watch at top

        if (join.equals(""))
            join = "!!!";

        if (watch.equals(""))
            watch = "!!!";

        //        if(entry.contains("[VIDEO]") && info.equals(""))
        //        return;

        if (info.equals(""))
            info = "!!!";

        for (int i = 0; i < model.size(); i++) {
            String tempentry = (String) model.elementAt(i);
            if (modeldata.elementAt(i).equals(number) || (tempentry.equals("-") && spot == 1)) {
                model.set(i, entry);
                modeljoin.set(i, join);
                modelinfo.set(i, info);
                modelwatch.set(i, watch);
                if (join.equals("!!!")) {
                    eventsTable.setValueAt(null, i, 0);
                    if (spot == 1)
                        topGamesTable.setValueAt(null, i, 0);
                } else
                    eventsTable.setValueAt(joinIcon, i, 0);

                if (watch.equals("!!!"))
                    eventsTable.setValueAt(null, i, 1);
                else {
                    eventsTable.setValueAt(watchIcon, i, 1);
                    if (spot == 1)
                        topGamesTable.setValueAt(watchIcon, i, 1);
                }
                if (info.equals("!!!")) {
                    eventsTable.setValueAt(null, i, 2);
                    if (spot == 1)
                        topGamesTable.setValueAt(null, i, 2);
                } else
                    eventsTable.setValueAt(infoIcon, i, 2);

                eventsTable.setValueAt(entry, i, 3);
                if (spot == 1)
                    topGamesTable.setValueAt(entry, i, 3);


                return;
            }
        }// end for

        // we will use this but for now below model.add(model.size(), entry);
        if (spot == 1) {
            model.add(spot, entry);
            modeljoin.add(spot, join);
            modelinfo.add(spot, info);
            modelwatch.add(spot, watch);
            modeldata.add(spot, number);
            Object[] tempo = new Object[4];
            if (join.equals("!!!"))
                tempo[0] = null;
            else
                tempo[0] = joinIcon;

            if (watch.equals("!!!"))
                tempo[1] = null;
            else
                tempo[1] = watchIcon;

            if (info.equals("!!!"))
                tempo[2] = null;
            else
                tempo[2] = infoIcon;

            tempo[3] = entry;

            eventsTable.insertRow(spot, tempo);
            topGamesTable.insertRow(spot, tempo);

        } else {
            if (entry.equals(""))
                return;

            model.add(model.size(), entry);
            modeljoin.add(modeljoin.size(), join);
            modelinfo.add(modelinfo.size(), info);
            modelwatch.add(modelwatch.size(), watch);
            modeldata.add(modeldata.size(), number);


            Object[] tempo = new Object[4];
            if (join.equals("!!!"))
                tempo[0] = null;
            else
                tempo[0] = joinIcon;

            if (watch.equals("!!!"))
                tempo[1] = null;
            else
                tempo[1] = watchIcon;

            if (info.equals("!!!"))
                tempo[2] = null;
            else
                tempo[2] = infoIcon;

            tempo[3] = entry;

            eventsTable.insertRow(eventsTable.getRowCount(), tempo);
        }// end else
    }

    void removeFromEvents(String index) {

        if (thisListType.equals("Tournaments")) {
            boolean go = false;
            for (int i = 0; i < model.size(); i++) {
                if (modeldata.elementAt(i).equals(index)) {

                    go = true;
                }
            }

            if (!go) {
                return;
            }

        }


        for (int i = 0; i < model.size(); i++)
            if (modeldata.elementAt(i).equals(index)) {
                String tempwatch = (String) modelwatch.elementAt(i);
                String tempinfo = (String) modelinfo.elementAt(i);
                String tempjoin = (String) modeljoin.elementAt(i);
                if (tempinfo.equals("!!!") && tempjoin.equals("!!!") && tempwatch.toLowerCase().startsWith("observe")) {
                    model.set(i, "-");// entry
                    modeljoin.set(i, "!!!");
                    modelinfo.set(i, "!!!");
                    modelwatch.set(i, "!!!");
                    // setValueAt(object, row, column);
                    eventsTable.setValueAt(null, i, 0);
                    eventsTable.setValueAt(null, i, 1);
                    eventsTable.setValueAt(null, i, 2);
                    eventsTable.setValueAt("-", i, 3);
                    topGamesTable.setValueAt(null, i, 0);
                    topGamesTable.setValueAt(null, i, 1);
                    topGamesTable.setValueAt(null, i, 2);
                    topGamesTable.setValueAt("-", i, 3);


                    break;

                }
                try {

                    model.remove(i);
                    modelinfo.remove(i);
                    modeljoin.remove(i);
                    modelwatch.remove(i);
                    modeldata.remove(i);

                    eventsTable.removeRow(i);
                    eventsTable.rowsRemoved(new TableModelEvent(eventsTable, i, i, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
                    topGamesTable.removeRow(i);
                    topGamesTable.rowsRemoved(new TableModelEvent(eventsTable, i, i, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));

                } catch (Exception noentry) {
                    System.out.println(noentry.getMessage());
                }


            }


    }

    void resetList() {
        int bottom = 0;
        if (thisListType.toLowerCase().startsWith("notify")) {
            bottom = 1;
        }
        try {
            for (int i = model.size() - 1; i > bottom; i--) {
                model.remove(i);
            }

            for (int i = modeldata.size() - 1; i > bottom; i--) {
                modeldata.remove(i);
            }
            for (int i = modeljoin.size() - 1; i > 0; i--) {
                modeljoin.remove(i);

            }
            for (int i = modelinfo.size() - 1; i > 0; i--) {
                modelinfo.remove(i);

            }

            for (int i = modelwatch.size() - 1; i > 0; i--) {
                modelwatch.remove(i);

            }

        } catch (Exception e) {
        }

        if (eventsTable != null) {
            try {
                for (int i = eventsTable.getRowCount() - 1; i > 0; i--) {
                    eventsTable.removeRow(i);

                }

            } catch (Exception duiii) {
            }

        }// if events
        if (topGamesTable != null) {
            try {
                for (int i = topGamesTable.getRowCount() - 1; i > 0; i--) {
                    topGamesTable.removeRow(i);

                }

            } catch (Exception duiii) {
            }

        }// if events
    }

    void notifyStateChanged(String name, String state) {
        int add = 1;
        String notify_string = name;
        if (state.equals("P"))
            notify_string = name + " (Playing)";
        if (state.equals("E"))
            notify_string = name + " (Examining)";
        if (state.equals("S"))
            notify_string = name + " (In Simul)";

        for (int i = 0; i < model.size(); i++)
            if (modeldata.elementAt(i).equals(name)) {
                model.set(i, notify_string);
                return;
            }
        if (name.toLowerCase().startsWith("double click")) {
            model.add(1, notify_string);
            modeldata.add(1, name);
        } else {
            model.add(2, notify_string);
            modeldata.add(2, name);
        }


    }

    String getOfferNumber(int index) {
        String offer = "-1";
        try {
            offer = (String) modeldata.elementAt(index);
        } catch (Exception D) {
        }
        return offer;

    }

    String getJoinCommand(int index) {
        String command = "-1";
        try {
            command = (String) modeljoin.elementAt(index);
        } catch (Exception D) {
        }
        return command;

    }

    String getInfoCommand(int index) {
        String command = "-1";
        try {
            command = (String) modelinfo.elementAt(index);
        } catch (Exception D) {
        }
        return command;

    }

    String getWatchCommand(int index) {
        String command = "-1";
        try {
            command = (String) modelwatch.elementAt(index);
        } catch (Exception D) {
        }
        return command;

    }

    String getEventListing(int index) {
        String command = "-1";
        try {
            command = (String) model.elementAt(index);
        } catch (Exception D) {
        }
        return command;

    }

    class myDefaultTableModel extends DefaultTableModel {
        boolean done;
        int count;

        public boolean isCellEditable(int row, int collumn) {

            return false;

        }

        myDefaultTableModel(String[] a1, Object[][] a2) {
            super(a2, a1);
            count = 0;
            done = false;

        }

        void addTableRow(Object[] a1) {
            super.addRow(a1);
        }

    }// end my default table model


}// end class
