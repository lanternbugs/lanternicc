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

import java.io.BufferedWriter;
import java.io.FileWriter;

class savesettings {


    gameboard myboards[];

    subframe[] consoleSubframes;


    savesettings() {
    } // constructor we are not using now

    void saveNow(gameboard boards[], subframe frames[], channels sharedVariables) {
        myboards = boards;
        consoleSubframes = frames;

        // sharedVariables.console[aChannelNumber 1-500] will tell me what console a channel number is in, if not setto a sub console number it goes in main, i.e its 0
        // note the only persistent feature really is the document associated with a console.  if it goes to main ( console 0 i think) that means it goes to teh document associated with console 0 originally.
        // any console if they change tabs can look at any document, but in a sense the consoles 1-10 enjoy persistence in their docuemnts 1-10
        // next two lines below an example of colorizing a channel to show how we store colors
        //	sharedVariables.channelOn[num1]=1; // channelOn accepts numbers up to 500 and indicates it is colorized, not a default color
        //    sharedVariables.channelColor[num1]=newColor; // channelColor accepts channel numbers up to 500 and gives their color. you wouldnt need to look at this unless channelOn for that channel number was set to 1


        // format for saving
	/*
	// \n between lines. we can make substrings by looking for the \n if we want. \n after last line.
	connum=#  // i.e. consnum=0
	visible=t or f // i.e. visible=t
	window=[window cordinates if visiable is true]
	channels=1 5 7 9 etc
	colored=0 1 1 0 etc
	colors=R G B, R G B
	// repeat for as many consoles as we have
	*/

        //  number = color.getRGB()

        String set_string = "";
        set_string = set_string + "[color] ";
        //  light square
        set_string = set_string + "light_color ";
        set_string = set_string + sharedVariables.lightcolor.getRGB() + " ";

        // dark square
        set_string = set_string + "dark_color ";
        set_string = set_string + sharedVariables.darkcolor.getRGB() + " ";


        // console foreground
        set_string = set_string + "ForColor ";
        set_string = set_string + sharedVariables.ForColor.getRGB() + " ";


        // console background
        set_string = set_string + "BackColor ";
        set_string = set_string + sharedVariables.BackColor.getRGB() + " ";

        //  application background
        set_string = set_string + "MainBackColor ";
        set_string = set_string + sharedVariables.MainBackColor.getRGB() + " ";

        // shout color
        set_string = set_string + "shoutcolor ";
        set_string = set_string + sharedVariables.shoutcolor.getRGB() + " ";

        // tell  color
        set_string = set_string + "tellcolor ";
        set_string = set_string + sharedVariables.tellcolor.getRGB() + " ";

        // tabs not implmented now


        // channels

        for (int a = 0; a < 500; a++)
            if (sharedVariables.channelOn[a] == 1) {
                set_string = set_string + "c" + a + " ";
                set_string = set_string + sharedVariables.channelColor[a].getRGB() + " ";
            }

        // now add closing signal
        set_string = set_string + "[donecolor] ";
        FileWrite out = new FileWrite();
        out.write(set_string);

    }//  end  method


    void readNow(gameboard boards[], subframe frames[], channels sharedVariables) {

    }

    class FileWrite {

        void write(String s) {
            try {
                // Create file
                FileWriter fstream = new FileWriter("\\multiframe\\multi_settings.txt");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(s);
                //Close the output stream
                out.close();
            } catch (Exception e) {//Catch exception if any

            }
        }
    }


}