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


import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

class myprintoutput {

    StyledDocument doc;
    int end;
    String mystring;
    SimpleAttributeSet attrs;

    void patchedInsertString(StyledDocument doc1, int end1, String mystring1, SimpleAttributeSet attrs1) {
        doc = doc1;
        end = end1;
        mystring = mystring1;
        attrs = attrs1;


    }

}// end class