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

import java.io.File;
import java.io.FilenameFilter;


/**
 * An implementation of the <code>java.io.FilenameFilter</code> which accepts
 * files whose name ends with a certain string. This makes it useful for
 * accepting files with a certain extension.
 */


public class ExtensionFilenameFilter implements FilenameFilter{


  /**
   * The strings with one of which a file's name must end in order to pass the 
   * filter.
   */

  private final String [] endStrings;




  /**
   * Creates a new ExtensionFilenameFilter with the given string. Only files
   * ending with that string will be accepted. Note that in order to use this
   * class to accept only files with a certain extension, you must also provide
   * the '.' character before the extension. For example, to accept only 'txt'
   * files, you must pass ".txt".
   */

  public ExtensionFilenameFilter(String endString){
    this(new String[]{endString});
  }




  /**
   * Creates a new ExtensionFilenameFilter with the given string array. Only
   * files ending with one of the strings in the given string array will be
   * accepted by the created ExtensionFilenameFilter.  Note that in order to use
   * this class to accept only files with a certain extension, you must also 
   * provide the '.' character before the extension. For example, to accept 
   * only 'txt' files, you must pass ".txt".
   */

  public ExtensionFilenameFilter(String [] endStrings){
    this.endStrings = new String[endStrings.length];
    for (int i=0;i<endStrings.length;i++){
      this.endStrings[i] = endStrings[i];
    }
  }




  /**
   * Tests whether the specified file passes the filter. Returns true if the
   * file's name ends with one of the string specified in the constructor.
   */

  public boolean accept(File dir, String filename){
    for (int i=0;i<endStrings.length;i++){
      if (filename.endsWith(endStrings[i]))
        return true;
    }
    return false;
  }


}
