/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2002, 2003 Alexander Maryanovsky.
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

import java.awt.*;
import java.lang.reflect.*;


/**
 * A collection of AWT related utilities.
 */

public class AWTUtilities{



  /**
   * Packs and centers the given window relative to the given component. The
   * specified component may be <code>null</code>, in which case the window will
   * be centered on the screen. The method also makes sure that the target
   * window is fully visible by calling <code>forceToScreen</code>.
   */

  public static void centerWindow(Window target, Component parent){
    target.pack();

    Dimension size = target.getSize();
    Rectangle parentBounds = parent == null || !parent.isShowing() ? 
      getUsableScreenBounds() :
      new Rectangle(parent.getLocationOnScreen(), parent.getSize());

    target.setLocation(parentBounds.x + (parentBounds.width - size.width)/2, parentBounds.y + (parentBounds.height - size.height)/2);
    
    forceToScreen(target);
  }
  
  
  
  /**
   * Reposition the specified window so that it necessarily fits on the screen,
   * while trying to minimize changes.
   */
   
  public static void forceToScreen(Window window){
    Dimension screenSize = window.getToolkit().getScreenSize();
    Rectangle bounds = window.getBounds();
    
    bounds.width = Math.min(bounds.width, screenSize.width);
    bounds.height = Math.min(bounds.height, screenSize.height);
    bounds.x = Math.min(Math.max(bounds.x, 0), screenSize.width - bounds.width);    
    bounds.y = Math.min(Math.max(bounds.y, 0), screenSize.height - bounds.height);

    window.setBounds(bounds);    
  }




  /**
   * Returns the parent Frame of the specified <code>Component</code> or
   * <code>null</code> if none exists.
   */

  public static Frame frameForComponent(Component component){
    while (component != null){
      if (component instanceof Frame)
        return (Frame)component;
      component = component.getParent();
    }

    return null;
  }




  /**
   * Returns a list of available font names. Under JDK1.1 it uses
   * <code>Toolkit.getFontList()</code> while under JDK1.2 (via reflection),
   * <code>GraphicsEnvironment.getAvailableFontFamilyNames()</code>
   */

  public static String [] getAvailableFontNames(){
    if (PlatformUtils.isJavaBetterThan("1.2")){
      try{
        // The equivalent of "return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();"
        Class geClass = Class.forName("java.awt.GraphicsEnvironment");
        Method getLocalGraphicsEnvironmentMethod = geClass.getMethod("getLocalGraphicsEnvironment", new Class[0]);
        Object localGE = getLocalGraphicsEnvironmentMethod.invoke(null, new Object[0]);
        Method getAvailableFontFamilyNamesMethod = geClass.getMethod("getAvailableFontFamilyNames", new Class[0]);
        String [] fontNames = (String [])getAvailableFontFamilyNamesMethod.invoke(localGE, new Object[0]);
        return fontNames;
      } catch (ClassNotFoundException e){e.printStackTrace();}
        catch (NoSuchMethodException e){e.printStackTrace();}
        catch (IllegalAccessException e){e.printStackTrace();}
        catch (InvocationTargetException e){e.printStackTrace();}
      return null;
    }
    else
      return Toolkit.getDefaultToolkit().getFontList();
  }
  
  
  
  /**
   * Returns the state of the specified frame, as specified by
   * <code>Frame.getExtendedState()</code> if running under JDK 1.4 or later,
   * otherwise returns 0. The call to <code>Frame.getExtendedState()</code> is
   * done via reflection to avoid runtime errors.
   */
   
  public static int getExtendedFrameState(Frame frame){
    if (PlatformUtils.isJavaBetterThan("1.4")){
      try{
        Class frameClass = Class.forName("java.awt.Frame");
        Method getExtendedStateMethod = frameClass.getMethod("getExtendedState", new Class[0]);
        Integer state = (Integer)getExtendedStateMethod.invoke(frame, new Object[0]);
        return state.intValue();
      } catch (ClassNotFoundException e){e.printStackTrace();}
        catch (NoSuchMethodException e){e.printStackTrace();}
        catch (IllegalAccessException e){e.printStackTrace();}
        catch (InvocationTargetException e){e.printStackTrace();}
    }
    
    return 0;
  }
  
  
  
  /**
   * Sets the state of the specified frame, as specified by
   * <code>Frame.setExtendedState</code> if running in JDK 1.4 or later,
   * otherwise does nothing. The call to <code>Frame.setExtendedState()</code>
   * is done via reflection to avoid runtime errors. 
   */
   
  public static void setExtendedFrameState(Frame frame, int state){
    if (PlatformUtils.isJavaBetterThan("1.4")){
      try{
        Class frameClass = Class.forName("java.awt.Frame");
        Method setExtendedStateMethod = frameClass.getMethod("setExtendedState", new Class[]{int.class});
        setExtendedStateMethod.invoke(frame, new Object[]{new Integer(state)});
      } catch (ClassNotFoundException e){e.printStackTrace();}
        catch (NoSuchMethodException e){e.printStackTrace();}
        catch (IllegalAccessException e){e.printStackTrace();}
        catch (InvocationTargetException e){e.printStackTrace();}
    }
  }
  
  
  
  /**
   * Attempts to determine the usable screen bounds of the default screen
   * device. If the require java.awt API is not available under the JVM we're
   * running in, this will simply return the screen bounds obtained via
   * <code>Toolkit.getScreenSize()</code>.
   */
  
  public static Rectangle getUsableScreenBounds(){
    if (PlatformUtils.isJavaBetterThan("1.4")){
      try{
        Class graphicsEnvironmentClass = Class.forName("java.awt.GraphicsEnvironment");
        Class graphicsDeviceClass = Class.forName("java.awt.GraphicsDevice");
        Class graphicsConfigurationClass = Class.forName("java.awt.GraphicsConfiguration");
        
        Class [] emptyClassArr = new Class[0];
        Method getLocalGraphicsEnvironmentMethod = 
          graphicsEnvironmentClass.getMethod("getLocalGraphicsEnvironment", emptyClassArr);
        Method getDefaultScreenDeviceMethod = 
          graphicsEnvironmentClass.getMethod("getDefaultScreenDevice", emptyClassArr);
        Method getDefaultConfigurationMethod =
          graphicsDeviceClass.getMethod("getDefaultConfiguration", emptyClassArr);
        Method getBoundsMethod = 
          graphicsConfigurationClass.getMethod("getBounds", emptyClassArr);
        Method getScreenInsetsMethod = 
          Toolkit.class.getMethod("getScreenInsets", new Class[]{graphicsConfigurationClass});
        
        Object [] emptyObjArr = new Object[0];
        Object graphicsEnvironment = getLocalGraphicsEnvironmentMethod.invoke(null, emptyObjArr);
        Object defaultScreenDevice = getDefaultScreenDeviceMethod.invoke(graphicsEnvironment, emptyObjArr);
        Object defaultConfiguration = getDefaultConfigurationMethod.invoke(defaultScreenDevice, emptyObjArr);
        Rectangle bounds = (Rectangle)getBoundsMethod.invoke(defaultConfiguration, emptyObjArr);
        Insets insets = 
          (Insets)getScreenInsetsMethod.invoke(Toolkit.getDefaultToolkit(), new Object[]{defaultConfiguration});
        
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= insets.left + insets.right;
        bounds.height -= insets.top + insets.bottom;
        
        return bounds;
      } catch (ClassNotFoundException e){e.printStackTrace();}
        catch (SecurityException e){e.printStackTrace();}
        catch (NoSuchMethodException e){e.printStackTrace();}
        catch (IllegalArgumentException e){e.printStackTrace();}
        catch (IllegalAccessException e){e.printStackTrace();}
        catch (InvocationTargetException e){e.printStackTrace();}
    }

    return new Rectangle(new Point(0, 0), Toolkit.getDefaultToolkit().getScreenSize());
  }
  
  
  
  
  /**
   * Enables or disables all the components within the specified container.
   * 
   * This is a rather hacky method - it doesn't work well if there are both
   * enabled and disabled components in the container.
   */

  public static void setContainerEnabled(Container container, boolean enabled){
    Component [] children = container.getComponents();
    for (int i = 0; i < children.length; i++){
      Component child = children[i];
      child.setEnabled(enabled);
      if (child instanceof Container)
        setContainerEnabled((Container)child, enabled);
    }
  }
  


}
