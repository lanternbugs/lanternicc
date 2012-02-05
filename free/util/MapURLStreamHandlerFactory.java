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

import java.util.Hashtable;
import java.net.URLStreamHandlerFactory;
import java.net.URLStreamHandler;


/**
 * An implementation of <code>URLStreamHandlerFactory</code> which maps protocol
 * names to instances of <code>URLStreamHandlerFactory</code> with a simple
 * hashtable.
 */

public class MapURLStreamHandlerFactory implements URLStreamHandlerFactory{



  /**
   * A hashtable mapping protocol names to instances of
   * <code>URLStreamHandler</code> which handle that protocol.
   */

  private final Hashtable handlers = new Hashtable();



  /**
   * Creates a new, empty, <code>MapURLStreamHandlerFactory</code>.
   */

  public MapURLStreamHandlerFactory(){

  }



  /**
   * Sets the specified protocol to be handled by the specified
   * <code>URLStreamHandler</code>. The protocol name is case insensitive.
   */

  public void setHandler(String protocol, URLStreamHandler handler){
    handlers.put(protocol.toLowerCase(), handler);
  }



  /**
   * Returns the <code>URLStreamHandler</code> for the specified protocol.
   */

  public URLStreamHandler getHandler(String protocol){
    return (URLStreamHandler)handlers.get(protocol.toLowerCase());
  }



  /**
   * Returns the <code>URLStreamHandler</code> for the specified protocol by
   * delegating to <code>getHandler(String protocol)</code>.
   */

  public URLStreamHandler createURLStreamHandler(String protocol){
    return getHandler(protocol);
  }



}
