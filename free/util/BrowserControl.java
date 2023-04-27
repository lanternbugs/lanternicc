/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2002 Alexander Maryanovsky.
 * All rights reserved.
 *
 * The utillib library is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * The utillib library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with utillib library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
 
package free.util;

// Originally adapted from
// http://www.javaworld.com/javaworld/javatips/jw-javatip66.html

import java.awt.*;
import java.awt.event.*;
import java.applet.AppletContext;
import java.io.IOException; 
import java.io.InputStream; 
import java.io.InterruptedIOException;
import java.net.URL;
import java.util.Properties;
import java.util.StringTokenizer;

/** 
 * A simple, static class to display a URL in the system browser.
 * <UL>
 *   <LI> Windows - The default browser will be opened via
 *        <code>rundll32 url.dll,FileProtocolHandler url</code>
 *   <LI> Mac OS X - The <code>open</code> command is used to open the url in
 *        the default browser.
 *   <LI> Classic MacOS - Netscape is used.
 *   <LI> All other platforms - The command specified by the BROWSER environment
 *        variable is executed, with <code>%s</code> in it replaced by the URL.
 *        If $BROWSER is not specified, mozilla is tried.
 * </UL>
 */

public class BrowserControl{ 



  /**
   * A Properties hashtable containing the environment variables and their
   * values.
   */

  private static Properties environment = null;
  
  
  
  /**
   * The context of the applet we're running within, if any.
   */
   
  private static AppletContext appletContext = null;



  /** 
   * Display a file in the system browser. If you want to display a 
   * file, you must include the absolute path name. Returns whether successful.
   * 
   * @param url the file's url
   */ 

  public static boolean displayURL(String url){
    try{
      if (appletContext != null){ // Running in an applet.
        appletContext.showDocument(new URL(url), "_blank");
      }
      else if (PlatformUtils.isWindows()){
        if (url.endsWith(".html")||url.endsWith(".htm")){

          // url-encode the last character because windows refuses to display URLs
          // ending with ".html" or ".htm", but works fine
          // for ".htm%6c" or ".ht%6d"
          int lastChar = url.charAt(url.length()  -1);
          url = url.substring(0, url.length() - 1) + "%" + Integer.toHexString(lastChar);
        }
        String cmd = "rundll32 url.dll,FileProtocolHandler "+url;
        Runtime.getRuntime().exec(cmd); 
      }
      else if (PlatformUtils.isMacOSX()){
        String [] commandLine = new String[]{"open", url};
        Runtime.getRuntime().exec(commandLine);
      }
      else if (PlatformUtils.isMacOS()){
        String [] commandLine = new String[]{"netscape", url}; 
        Runtime.getRuntime().exec(commandLine); 
      }
      else{
        synchronized(BrowserControl.class){
          if (environment == null){
            environment = new Properties();
            try{
              Process env = Runtime.getRuntime().exec("env");
              InputStream in = env.getInputStream();
              try{
                environment.load(in);
              } finally{
                  in.close();
                }
            } catch (IOException e){e.printStackTrace();}
          }
        }

        String browsers = environment.getProperty("BROWSER");
        if ((browsers == null) || ("".equals(browsers))){
          return tryMozilla(url);
        }

        StringTokenizer tokenizer = new StringTokenizer(browsers, ":");
        if (!tokenizer.hasMoreTokens())
          return false;
        
        String browser = tokenizer.nextToken();
        int percentPercentIndex;
        while ((percentPercentIndex = browser.indexOf("%%")) != -1)
          browser = browser.substring(0, percentPercentIndex)+"%"+browser.substring(percentPercentIndex+3);
        int urlIndex = browser.indexOf("%s");
        String commandline;
        if (urlIndex != -1)
          commandline = browser.substring(0, urlIndex)+url+browser.substring(urlIndex+2);
        else
          commandline = browser+" "+url;
        Runtime.getRuntime().exec(commandline);
      }
    } catch (IOException e){
        return false;
      }

    return true;
  }
  
  
  
  
  /**
   * Tries to open the specified URL in mozilla.
   */
   
  public static boolean tryMozilla(String url){
    try{
      String [] cmd = new String[]{"mozilla", "-remote",  "openURL("+url+", new-tab)"}; 
      Process p = Runtime.getRuntime().exec(cmd);
      if (p == null)
        return false;
  
      try{ 
        // wait for exit code -- if it's 0, command worked, 
        // otherwise we need to start the browser up. 
        int exitCode = p.waitFor(); 
  
        if (exitCode != 0){
          // Command failed, start up the browser
          cmd = new String[]{"mozilla ", url}; 
          p = Runtime.getRuntime().exec(cmd);
        } 
      } 
      catch(InterruptedException x){ 
        System.err.println("Error bringing up browser, cmd='" + cmd + "'"); 
        System.err.println("Caught: " + x); 
        throw new InterruptedIOException(x.getMessage());
      }
    } catch (IOException e){
        return false;
      }
    
    return true;
  }



  /**
   * Brings up the default mailer with the given address in the "to:" field.
   * Returns whether successful.
   */

  public static boolean displayMailer(String address){
    try{
      if (appletContext == null){
        if (PlatformUtils.isLinux()){
          synchronized(BrowserControl.class){
            if (environment == null){
              try{
                environment = new Properties();
                Process env = Runtime.getRuntime().exec("env");
                environment.load(env.getInputStream());
              } catch (IOException e){}
            }
          }
  
          String mailer = environment.getProperty("MAILER");
          if (mailer != null){
            Runtime.getRuntime().exec(mailer + " " + address);
            return true;
          }
        }
      }
    } catch (IOException e){
        return false;
      }

    return displayURL("mailto:" + address);
  }



  
  /**
   * Displays an error dialog to the user appropriate to when the
   * <code>displayURL</code> call fails. If <code>modal</code> is true, this
   * method will block until the user closes the dialog. The specified parent 
   * component must have a <code>Frame</code> parent.
   */

  public static void showDisplayBrowserFailedDialog(String url, Component parent, boolean modal){
    final Dialog dialog = new Dialog(AWTUtilities.frameForComponent(parent), "Error displaying URL", modal);
    dialog.setLayout(new BorderLayout(5, 5));
    dialog.add(new Label("Unable to show URL - please copy/paste it into your browser:"), BorderLayout.NORTH);
    TextField urlTextField = new TextField(url);
    urlTextField.setEditable(false);
    dialog.add(urlTextField, BorderLayout.CENTER);

    Button okButton = new Button("OK");
    okButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
        dialog.dispose();
      }
    });
    Panel okButtonPanel = new Panel(new FlowLayout());
    okButtonPanel.add(okButton);
    dialog.add(okButtonPanel, BorderLayout.SOUTH);

    AWTUtilities.centerWindow(dialog, parent);
    dialog.setVisible(true);
  }



  /**
   * Displays an error dialog to the user appropriate to when the
   * <code>displayMailer</code> call fails. If <code>modal</code> is true, this
   * method will block until the user closes the dialog. The specified parent 
   * component must have a <code>Frame</code> parent.
   */

  public static void showDisplayMailerFailedDialog(String address, Component parent, boolean modal){
    final Dialog dialog = new Dialog(AWTUtilities.frameForComponent(parent), "Error displaying mailer", modal);
    dialog.setLayout(new BorderLayout(5, 5));
    dialog.add(new Label("Unable to display mailer - please copy/paste the address into your mailer:"), BorderLayout.NORTH);
    TextField addressTextField = new TextField(address);
    addressTextField.setEditable(false);
    dialog.add(addressTextField, BorderLayout.CENTER);

    Button okButton = new Button("OK");
    okButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
        dialog.dispose();
      }
    });
    Panel okButtonPanel = new Panel(new FlowLayout());
    okButtonPanel.add(okButton);
    dialog.add(okButtonPanel, BorderLayout.SOUTH);

    AWTUtilities.centerWindow(dialog, parent);
    dialog.setVisible(true);
  }
  
  
  
  /**
   * Passes the <code>AppletContext</code> to <code>BrowserControl</code>.
   * That <code>AppletContext</code> will then be used for opening URLs.
   */
   
  public static void setAppletContext(AppletContext appletContext){
    BrowserControl.appletContext = appletContext;
  }
  
  
 
  /**
   * Displays a small awt dialog to test URL opening.
   */
  
  public static void main(String [] args){
    Frame f = new Frame("BrowserControl Test");
    f.setLocation(100, 100);
    f.setLayout(new BorderLayout());
    Panel p = new Panel(new FlowLayout());
    p.add(new Label("URL: "));
    final TextField tf = new TextField(50);
    p.add(tf);
    f.add(p, BorderLayout.CENTER);
    Button b = new Button("Open URL");
    ActionListener actionListener = new ActionListener(){
      public void actionPerformed(ActionEvent evt){
        displayURL(tf.getText());
      }
    };
    
    b.addActionListener(actionListener);
    tf.addActionListener(actionListener);
    
    Panel p2 = new Panel(new FlowLayout());
    p2.add(b);
    f.add(p2, BorderLayout.SOUTH);
    
    
    f.pack();
    f.setVisible(true);
    
    f.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent evt){
        System.exit(0);
      }
    });
  }


                            
}
