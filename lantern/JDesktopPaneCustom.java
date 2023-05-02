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

import javax.swing.*;
import java.awt.*;

class JDesktopPaneCustom extends JDesktopPane {
    // we  have use of paint components in frame if they have wall paper
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        if (sharedVariables.wallpaperImage != null)
            g.drawImage(sharedVariables.wallpaperImage, 0, 0, width, height, this);
        else
            setBackground(sharedVariables.MainBackColor);
    }

    gameboard[] myboards;
    JFrame myframe;
    subframe[] consoleSubframes;
    channels sharedVariables;

    JDesktopPaneCustom(channels sharedVariables1, gameboard[] myboards1,
                       subframe[] consoleSubframes1, JFrame myframe1) {
        sharedVariables = sharedVariables1;
        myboards = myboards1;
        consoleSubframes = consoleSubframes1;
        myframe = myframe1;
    }

    int findOurWindow() {
        int num = -1;
        for (int a = 0; a < sharedVariables.maxConsoleTabs; a++)
            if (consoleSubframes[a] != null &&
                    consoleSubframes[a].isVisible())
                if (consoleSubframes[a].isSelected())
                    //if (consoleSubframes[a].overall.Input.hasFocus())
                    return a;

        for (int b = 0; b < sharedVariables.maxGameTabs; b++)
            if (myboards[b] != null &&
                    myboards[b].isVisible())
                //if(myboards[b].isSelected())
                if (myboards[b].myconsolepanel.Input.hasFocus())
                    return b + 1000;

        return -1;
    }

    public void switchWindows() {
        try {
            int type = 0;// type = 0 console type =1 board
            int num = findOurWindow();
            if (num == -1) {
                num = findNextConsole(0, sharedVariables.maxConsoleTabs);
                if (num < 0)
                    return;
            }// no board or chat window selected or is visible

            if (num > 999) {
                num = num - 1000;
                type = 1;
            }

            int selection;
            if (type == 1) {
                selection = findNextBoard(num + 1, sharedVariables.maxGameTabs);
                if (selection > -1) {
                    myboards[selection].setSelected(true);
                    return;
                }
                selection = findNextConsole(0, sharedVariables.maxConsoleTabs);
                if (selection > -1) {
                    consoleSubframes[selection].setSelected(true);
                    return;
                }

                selection = findNextBoard(0, num + 1);
                if (selection > -1) {
                    myboards[selection].setSelected(true);
                    return;
                }
                return;

            } else {
                selection = findNextConsole(num + 1, sharedVariables.maxConsoleTabs);
                if (selection > -1) {
                    consoleSubframes[selection].setSelected(true);
                    return;
                }
                selection = findNextBoard(0, sharedVariables.maxGameTabs);
                if (selection > -1) {
                    myboards[selection].setSelected(true);
                    return;
                }
                selection = findNextConsole(0, num);
                if (selection > -1) {
                    consoleSubframes[selection].setSelected(true);
                    return;
                }
                return;
            }// end else
            // end try
        } catch (Exception d) {
        }
    }

    int findNextConsole(int min, int max) {
        for (int a = min; a < max; a++)
            if (consoleSubframes[a] != null &&
                    consoleSubframes[a].isVisible())
                return a;

        return -1;
    }// end method

    int findNextBoard(int min, int max) {
        for (int a = min; a < max; a++)
            if (myboards[a] != null &&
                    myboards[a].isVisible())
                return a;

        return -1;
    }// end method

    public void switchConsoleWindows() {
        try {
            int type = 0;// type = 0 console type =1 board
            int num = findOurWindow();
            if (num == -1) {
                num = findNextConsole(0, sharedVariables.maxConsoleTabs);
                if (num < 0)
                    return;
            }// no board or chat window selected or is visible

            if (num > 999) {
                num = num - 1000;
                type = 1;
            }

            int selection;
            if (type == 1) {
                selection = findNextConsole(0, sharedVariables.maxConsoleTabs);
                if (selection > -1) {
                    consoleSubframes[selection].setSelected(true);
                    return;
                }

                return;

            } else {
                selection = findNextConsole(num + 1, sharedVariables.maxConsoleTabs);
                if (selection > -1) {
                    consoleSubframes[selection].setSelected(true);
                    return;
                }

                selection = findNextConsole(0, num);
                if (selection > -1) {
                    consoleSubframes[selection].setSelected(true);
                    return;
                }
                return;
            }// end else
            // end try
        } catch (Exception d) {
        }
    }

    void changeTellTabForward() {
        int n = sharedVariables.tellconsole;
        int next = n;

        for (int a = n + 1; a < sharedVariables.maxConsoleTabs; a++) {
            if (consoleSubframes[a] != null &&
                    consoleSubframes[a].isVisible()) {
                next = a;
                break;
            }
        }

        if (next == n) {
            for (int a = 0; a < n; a++) {
                if (consoleSubframes[a] != null &&
                        consoleSubframes[a].isVisible()) {
                    next = a;
                    break;
                }
            }
        }

        if (next != n)
            changeTellConsole(n, next);
    }

    void changeTellTabBackward() {
        int n = sharedVariables.tellconsole;
        int next = n;

        for (int a = n - 1; a >= 0; a--) {
            if (consoleSubframes[a] != null &&
                    consoleSubframes[a].isVisible()) {
                next = a;
                break;
            }
        }

        if (next == n) {
            for (int a = sharedVariables.maxConsoleTabs - 1; a > n; a--) {
                if (consoleSubframes[a] != null &&
                        consoleSubframes[a].isVisible()) {
                    next = a;
                    break;
                }
            }
        }

        if (next != n)
            changeTellConsole(n, next);
    }

    void changeTellConsole(int current, int newer) {
        sharedVariables.tellconsole = newer;
        consoleSubframes[current].tellCheckbox.setSelected(false);
        consoleSubframes[newer].tellCheckbox.setSelected(true);
    }
}// end class