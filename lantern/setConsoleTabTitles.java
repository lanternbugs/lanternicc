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

class setConsoleTabTitles {
    setConsoleTabTitles() { // constructor not used now

    }

    void createConsoleTabTitle(channels sharedVariables, int conNumber,
                               subframe[] consoleSubframes, String Name) {
        String title = "";
        int space = 0;
        for (int a = 0; a < 500; a++) {
            if (sharedVariables.console[conNumber][a] == 1) {
                if (space == 1)
                    title = title + " ";
                else
                    space = 1;
                title = title + a;
            }
        }

        if (sharedVariables.shoutRouter.shoutsConsole == conNumber)
            title = title + " " + "Shouts";

        if (sharedVariables.shoutRouter.sshoutsConsole == conNumber)
            title = title + " " + "S-Shouts";

    /*
    if (title.length() > 7) {
      title=title.substring(0, 6);
      title=title + ".";
    }
    */

        if (Name.length() > 0)
            sharedVariables.consoleTabCustomTitles[conNumber] = Name;
        else
            sharedVariables.consoleTabCustomTitles[conNumber] = "";

        if (title.length() > 0)
            sharedVariables.consoleTabTitles[conNumber] = title;
        else
            sharedVariables.consoleTabTitles[conNumber] = "C" + conNumber;


        for (int a = 0; a < sharedVariables.openConsoleCount; a++) {
            if (consoleSubframes[a] != null) {
                if (Name.length() > 0)
                    consoleSubframes[a].channelTabs[conNumber].setFullText(sharedVariables.consoleTabCustomTitles[conNumber]);
                else
                    consoleSubframes[a].channelTabs[conNumber].setText(sharedVariables.consoleTabTitles[conNumber]);

                if (sharedVariables.looking[a] == conNumber) {
                    consoleSubframes[a].updateComboBox(conNumber);
                    consoleSubframes[a].setTitle(consoleSubframes[a].consoleTitle +
                            sharedVariables.consoleTabTitles[conNumber]);
                }
                //consoleSubframes[a].overall.recreate();
                // not sure why i need this yet. more investigation. tabs
                // wouldnt resize after tab name
            }// end if
        }
    }//end method
}