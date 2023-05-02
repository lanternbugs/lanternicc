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

class FocusOwner {

    boolean console;
    boolean board;
    int number;

    FocusOwner(channels sharedVariables, subframe[] consoleSubframes, gameboard[] myboards) {

        console = false;/// if it doesnt set one of these to true nobody gets it
        number = 0;
        board = false;

        try {


            for (int a = 0; a < sharedVariables.openConsoleCount; a++) {
                if (consoleSubframes[a].overall.Input.hasFocus()) {
                    console = true;
                    number = a;
                    //writeToConsole("console is true and number is " + a + "\n");
                    return;
                }
            }// end for

            for (int a = 0; a < sharedVariables.openBoardCount; a++) {
                if (myboards[a].myconsolepanel.Input.hasFocus()) {
                    board = true;
                    number = a;
                    return;

                }
            }

        }// end try
        catch (Exception e) {
        }

    }// end constructor


}// end class focus owner.
