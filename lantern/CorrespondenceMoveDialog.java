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
import javax.swing.table.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


class CorrespondenceMoveDialog extends JDialog
{
	channels sharedVariables;
	ConcurrentLinkedQueue<myoutput> queue;JFrame homeFrame;
    JTextArea textView;
    JLabel textLabel;
    JButton cancel;
    JButton send;
    JButton help;
    JTextField input;
    String gameNumber;
    

	
    CorrespondenceMoveDialog(JFrame master, channels sharedVariables1, ConcurrentLinkedQueue<myoutput> queue1, String gNumber)
{
    super(master, false);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
sharedVariables=sharedVariables1;
queue=queue1;
    gameNumber = gNumber;
    initComponents();
}// end constructor


void initComponents(){
   
    setBackground(Color.white);
    textView = new JTextArea();
    textView.setColumns(20);
    textView.setRows(5);
    textView.setEditable(false);
    textView.setLineWrap(true);
    textView.setWrapStyleWord(true);
    textView.setFont(sharedVariables.myFont);
    textView.setText("Type the move in standard notation such as e4 or Nf3. A confirmation board will popup up showing the candiate move and a dialog to choose yes make move or no." );
    input = new JTextField();
    input.setFont(sharedVariables.inputFont);
    setKeyListener();
    cancel = new JButton();
    send = new JButton();
    help = new JButton();
    cancel.setText("Cancel");
    send.setText("Send");
    help.setText("Help Notation");
    cancel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dispose();
        }
      } );
    help.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            myoutput output = new myoutput();
              output.data = "multi help notation\n";
            output.consoleNumber = 0;
            queue.add(output);
        }
      } );
    send.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          sendMove();
        }
      } );
    
    textLabel = new JLabel("Type a move. There is a  chance to confirm.");
    textLabel.setFont(sharedVariables.myFont);

   

setLayout();

}// end inti components

  
void setLayout()
{

    JPanel pane = new JPanel();
    GroupLayout layout = new GroupLayout(pane);
    add(pane);

    pane.setLayout(layout);
      //Create a parallel group for the horizontal axis
      ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
      ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING, true);
    SequentialGroup hRowTop = layout.createSequentialGroup();
    SequentialGroup hRowBottom = layout.createSequentialGroup();
    hRowTop.addComponent(cancel);
    hRowTop.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    hRowTop.addComponent(help);
    hRowBottom.addComponent(input, GroupLayout.PREFERRED_SIZE,
                            GroupLayout.DEFAULT_SIZE, 250);
    hRowBottom.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    hRowBottom.addComponent(send, GroupLayout.PREFERRED_SIZE,
                            GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
    h1.addGroup(hRowTop);
    h1.addComponent(textView, GroupLayout.PREFERRED_SIZE,
                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
    h1.addComponent(textLabel, GroupLayout.PREFERRED_SIZE,
                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
    h1.addGroup(hRowBottom);
      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);// was trailing
      //Create the horizontal group
      layout.setHorizontalGroup(hGroup);


      //Create a parallel group for the vertical axis
      SequentialGroup vGroup = layout.createSequentialGroup();
    ParallelGroup vRowTop = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
    ParallelGroup vRowBottom = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
    vRowTop.addComponent(cancel);
    vRowTop.addComponent(help);
    vRowBottom.addComponent(input, GroupLayout.PREFERRED_SIZE,
                            GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
    vRowBottom.addComponent(send, GroupLayout.PREFERRED_SIZE,
                            GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
    
    vGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    vGroup.addGroup(vRowTop);
    vGroup.addComponent(textView, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
    vGroup.addComponent(textLabel, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
    vGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    vGroup.addGroup(vRowBottom);

      layout.setVerticalGroup(vGroup);


}// end set layout
    
    void setKeyListener()
    {
        input.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
              int a = e.getKeyCode();
              int gme = e.getModifiersEx();
            
              //if (a == 27) {
              if (a == KeyEvent.VK_ESCAPE) {
                input.setText("");
              }

              //if (a == 10) {
              if (a == KeyEvent.VK_ENTER) {
                sendMove();
              }// end enter
                }// end key pressed

            public void keyTyped(KeyEvent e) {

            }

            /** Handle the key-released event from the text field. */
            public void keyReleased(KeyEvent e) {

            }
          });
    }
    
    void sendMove() {
        String pre = "multi cc-move #" + gameNumber + " ";
        String mes = pre + input.getText() + "\n";
        myoutput output = new myoutput();
        if (sharedVariables.myServer.equals("ICC") &&
            sharedVariables.myname.length() > 0)
          output.data = "`cormove"  + "`" + mes;
        else
          output.data = mes;

        output.consoleNumber = 0;
        queue.add(output);
        input.setText("");
          dispose();
    }

}//end class
