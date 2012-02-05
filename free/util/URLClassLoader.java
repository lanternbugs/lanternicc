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

import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.IOException;


/**
 * A class loader which loads classes and resources from a specified URL.
 */
 
public class URLClassLoader extends ChildClassLoader{
  
  
  
  /**
   * The URL at which we look for classes and resources.
   */

  private final URL url;



  /**
   * Creates a new <code>URLClassLoader</code> which loads classes and resources
   * from the specified URL and the specified parent
   * <code>ChildClassLoader</code> to which loading is delegated if a class or a
   * resource can't be found.
   */

  public URLClassLoader(URL url, ChildClassLoader parent){
    super(parent);
    
    if (url == null)
      throw new IllegalArgumentException("The specified URL may not be null");
    
    this.url = url;
  }


  /**
   * Creates a new <code>URLClassLoader</code> which loads classes and resources
   * from the specified URL. 
   */

  public URLClassLoader(URL url){
    if (url == null)
      throw new IllegalArgumentException("The specified URL may not be null");
    
    this.url = url;
  }



  /**
   * Returns an <code>InputStream</code> for reading the resource with the
   * specified name.
   */

  protected InputStream getResourceAsStreamImpl(String name){
    try{
      URL resourceURL = new URL(url, name);
      return resourceURL.openStream();
    } catch (IOException e){
        return null;
      }
  }



  /**
   * Returns a <code>URL</code> pointing to the resource with the specified
   * name.
   */

  protected URL getResourceImpl(String name){
    try{
      return new URL(url, name);
    } catch (MalformedURLException e){return null;}
  }

  
   
} 
