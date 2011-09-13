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
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JDialog;
import java.io.*;
import java.net.*;
import java.lang.Thread.*;
import java.applet.*;
import javax.swing.GroupLayout.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import java.lang.Integer;
import javax.swing.text.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.applet.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.datatransfer.Clipboard;
import java.lang.reflect.Method;



class textSearcher
{

void find(String word, JTextPane pane)
{
int offset = -1;
    Highlighter highlighter = pane.getHighlighter();

    // Remove any existing highlights for last search
    Highlighter.Highlight[] highlights = highlighter.getHighlights();
    for (int a = 0; a < highlights.length; a++) {
      Highlighter.Highlight h = highlights[a];
      try  {
        highlighter.removeHighlight(h);
      }
      catch(Exception dui){}
    }

    if (word == null || word.equals("")) {
      return;
    }

String text = "";
    try {
      StyledDocument doc = pane.getStyledDocument();
      text = doc.getText(0, doc.getLength()).toLowerCase();
    } catch (Exception e) {

      return;
    }

    word = word.toLowerCase();
    int lastIndex = 0;
    int wordSize = word.length();

    while ((lastIndex = text.indexOf(word, lastIndex)) > -1) {
      int endIndex = lastIndex + wordSize;
      try {


      highlighter.addHighlight(lastIndex, endIndex, DefaultHighlighter.DefaultPainter);


      } catch (BadLocationException e) {
        // Nothing to do
      }
      if (offset == -1) {
        offset = lastIndex;
      }
      lastIndex = endIndex;
    }



}// end method


}// end class


