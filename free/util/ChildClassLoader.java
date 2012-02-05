/**
 * The utillib library.
 * More information is available at http://www.jinchess.com/.
 * Copyright (C) 2003 Alexander Maryanovsky.
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

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import free.util.IOUtilities;


/**
 * A class loader with a parent, to which loading is delegated if the class or
 * resource can't be found. This class isn't needed with JDK1.2 which already
 * has this functionality. As an additional feature, this class uses a Hashtable
 * to look up already loaded classes, so any subclasses needn't do this.
 */

public abstract class ChildClassLoader extends ClassLoader{



  /**
   * The parent classloader. May be null.
   */

  private final ChildClassLoader parent;



  /**
   * Maps class names to already loaded classes.
   */

  private final Hashtable namesToClasses = new Hashtable();



  /**
   * Creates a new <code>ChildClassLoader</code> with the specified parent. The
   * parent may be <code>null</code>.
   */

  public ChildClassLoader(ChildClassLoader parent){
    this.parent = parent;
  }



  /**
   * Creates a new <code>ChildClassLoader</code> with no parent.
   */

  public ChildClassLoader(){
    this(null);
  }



  /**
   * Returns the parent classloader.
   */

  public ChildClassLoader getParentClassLoader(){
    return parent;
  }



  /**
   * Loads and optionally resolves the class with the specified name. If this
   * classloader can't find the specified class, the parent is asked to load it.
   */

  public final synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException{
    Class c = (Class)namesToClasses.get(name);
    if (c != null)
      return c;

    c = loadClassImpl(name, resolve);
    if (c == null){
      if (parent == null)
        throw new ClassNotFoundException(name);
      else
        c = parent.loadClass(name, resolve);
    }

    namesToClasses.put(name, c);
    return c;
  }



  /**
   * Returns an <code>InputStream</code> for reading the resource with the
   * specified name. If this classloader can't find the resource, the parent
   * is asked to find it.
   */

  public final InputStream getResourceAsStream(String name){
    InputStream in = getResourceAsStreamImpl(name);
    return in != null ? in : (parent == null ? null : parent.getResourceAsStream(name));
  }



  /**
   * Returns a <code>URL</code> pointing to the resource with the specified
   * name. If this classloader can't find the resource, the parent is asked to
   * find it.
   */

  public final URL getResource(String name){
    URL url = getResourceImpl(name);
    return url != null ? url : (parent == null ? null : parent.getResource(name));
  }



  /**
   * Loads the class data for the specified class name. The default
   * implementation loads the class data via
   * <code>getResourceAsStreamImpl</code> 
   */

  protected byte [] loadClassData(String name) throws IOException{
    String resourceName = name.replace('.', '/') + ".class";
    InputStream in = getResourceAsStreamImpl(resourceName);
    if (in == null)
      return null;
    
    return IOUtilities.readToEnd(in);
  }



  /**
   * Loads the class with the specified name and optionally resolves it.
   * The default implementation first tries to obtain the class via
   * <code>findSystemClass</code> and if that fails, loads the class via
   * <code>loadClassData</code>.
   */

  protected Class loadClassImpl(String name, boolean resolve){
    try{
      Class c = null;
      try{
        c = findSystemClass(name);
      } catch (ClassNotFoundException e){}

      if (c == null){
        byte [] classData = loadClassData(name);
        if (classData == null)
          return null;
        
        c = defineClass(name, classData, 0, classData.length);
      }

      if (resolve)
        resolveClass(c);

      return c;
    } catch (IOException e){
        return null;
      }
  }



  /**
   * Returns an <code>InputStream</code> for reading the resource with the
   * specified name.
   */

  protected abstract InputStream getResourceAsStreamImpl(String name);



  /**
   * Returns a <code>URL</code> pointing to the resource with the specified
   * name.
   */

  protected abstract URL getResourceImpl(String name);



}
