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

import java.io.*;


/**
 * A commandline utility application which adds text taken from a given file,
 * and adds it to the beginning of all files with the given extension in a given
 * directory. This is useful for adding license text to source code files.
 * The modifying process of a single file includes renaming it to a file with
 * the name of the original file, ".tmp" added to it, then creating a new file
 * with the name of the original file, writing license information into the new
 * file, writing the contents of the ".tmp" file into the new file and only then
 * deleting the ".tmp" file. If at any point an I/O error occurrs, the utility
 * terminates immediately. This ensures that at all times, either the original
 * file or the ".tmp" file exist, preventing possible data loss.
 */

public class ApplyLicense{


  /**
   * The main method, duh.
   */

  public static void main(String [] args){
    if (args.length<3){
      if (args.length!=0)
        System.out.println("Not enough arguments.");
      printUsage();
      System.exit(0);
    }

    boolean recurse = false;
    boolean undo = false;
    String backupDirName = null;

    int argIndex = 0;
    while (argIndex<args.length-3){
      String arg = args[argIndex];
      if (arg.equals("-r"))
        recurse = true;
      else if (arg.equals("-b"))
        backupDirName = args[++argIndex];
      else if (arg.equals("-u"))
        undo = true;

      argIndex++;
    }

    if (argIndex>args.length-3){
      System.out.println("Not enough arguments");
      printUsage();
      System.exit(0);
    }

    String licenseFilename = args[args.length-3];
    String extension = args[args.length-2];
    String dirName = args[args.length-1];

    File backupDir = (backupDirName==null ? null : new File(backupDirName));
    File licenseFile = new File(licenseFilename);
    File dir = new File(dirName);

    if (!licenseFile.exists()){
      System.out.println("No such file: "+licenseFilename);
      System.exit(0);
    }

    if (!dir.exists()){
      System.out.println("No such directory: "+dirName);
      System.exit(0);
    }

    if (!dir.isDirectory()){
      System.out.println(dirName+" is a file, not a directory");
      System.exit(0);
    }

    if (backupDir != null){
      if (!backupDir.exists()){
        System.out.print("Creating backup directory "+backupDirName+" ");
        if (!backupDir.mkdirs()){
          System.out.println("Unable to create backup directory "+backupDirName);
          System.exit(0);
        }
        System.out.println("done");
      }
      try{
        System.out.print("Backing up files in "+backupDir.getCanonicalPath()+" ");
        IOUtilities.copyDir(dir, backupDir, recurse);
        System.out.println("done");
      } catch (IOException e){
          System.out.println("Unable to backup files into "+backupDirName);
          e.printStackTrace();
          System.exit(0);
        }
    }

    try{
      String license = IOUtilities.loadTextFile(licenseFile);
      if (undo)
        removeLicense(license, new ExtensionFilenameFilter("."+extension), dir, recurse);
      else
        applyLicense(license, new ExtensionFilenameFilter("."+extension), dir, recurse);
    } catch (IOException e){
        e.printStackTrace();
        System.exit(0);
      }
  }




  /**
   * Prints usage information to the standard output stream.
   * Here is what this method prints:
   * <PRE>
   * 
   * ApplyLicense Utility
   * Copyright (C) 2001 Alexander Maryanovsky
   *
   * Use: java free.util.ApplyLicense [-r][-b backupDir] licenseFile extension dir
   *
   * -r apply recursively into subdirectories
   * -b backupDir    Backup files into the given directory before applying license
   *
   * Version 1.00 - 05 Oct. 2001
   * </PRE>
   */

  private static void printUsage(){
    System.out.println();
    System.out.println("ApplyLicense Utility");
    System.out.println("Copyright (C) 2001 Alexander Maryanovsky");
    System.out.println();
    System.out.println("Use: java free.util.ApplyLicense [-r][-b backupDir] licenseFile extension dir");
    System.out.println();
    System.out.println("-r              Apply recursively into subdirectories");
    System.out.println("-u              Undo applying the license");
    System.out.println("-b backupDir    Backup files into the given directory before applying license");
    System.out.println();
    System.out.println("Version 1.00 - 05 Oct. 2001");
  }




  /**
   * Adds the given license string to the beginning of every file in the given
   * directory, optionally recursing into subdirectories, which passes the given
   * FilenameFilter.
   *
   * @throws IOException If an I/O error occurs during the processing of the
   * file(s).
   */

  public static void applyLicense(String license, FilenameFilter filter, File dir, boolean recurse) throws IOException{
    if (!dir.exists())
      throw new IllegalArgumentException("The directory "+dir+" does not exist");

    if (!dir.isDirectory())
      throw new IllegalArgumentException(dir.toString()+" is a file, not a directory");

    String [] filenames = dir.list();
    for (int i = 0; i < filenames.length; i++){
      String filename = filenames[i];
      File file = new File(dir, filename);
      if (file.isDirectory()){
        if (recurse)
          applyLicense(license, filter, file, true);
      }
      else if (filter.accept(dir, filename)){
        System.out.print("Prepending license text to: "+file);
        prependLicense(license, file);
        System.out.println(" done");
      }
    }
  }




  /**
   * Prepends the given text to the given file.
   */

  private static void prependLicense(String license, File file) throws IOException{
    File tmpFile = new File(file.getAbsolutePath()+".tmp");
    if (!file.renameTo(tmpFile))
      throw new IOException("Unable to rename "+file+" into "+tmpFile);

    OutputStream out = null;
    InputStream in = null;

    try{
      out = new FileOutputStream(file);
      Writer writer = new BufferedWriter(new OutputStreamWriter(out));
      writer.write(license);
      writer.flush();
      in = new FileInputStream(tmpFile);
      IOUtilities.pump(in, out);
    } finally{
        if (in != null)
          in.close();
        if (out != null)
          out.close();
      }
    if (!tmpFile.delete())
      throw new IOException("Unable to delete "+tmpFile);
  }





  /**
   * Removes the given license string from the beginning of every file in the
   * given directory, optionally recursing into subdirectories, which passes
   * the given FilenameFilter.
   *
   * @throws IOException If an I/O error occurs during the processing of the
   * file(s).
   */

  public static void removeLicense(String license, FilenameFilter filter, File dir, boolean recurse) throws IOException{
    if (!dir.exists())
      throw new IllegalArgumentException("The directory "+dir+" does not exist");

    if (!dir.isDirectory())
      throw new IllegalArgumentException(dir.toString()+" is a file, not a directory");

    String [] filenames = dir.list();
    for (int i = 0; i < filenames.length; i++){
      String filename = filenames[i];
      File file = new File(dir, filename);
      if (file.isDirectory()){
        if (recurse)
          removeLicense(license, filter, file, true);
      }
      else if (filter.accept(dir, filename)){
        System.out.print("Removing license text from: "+file);
        deleteLicense(license, file);
        System.out.println(" done");
      }
    }
  }




  /**
   * Prepends the specified text from the beginning of the given file. Returns
   * <code>true</code> if the file indeed started with the specified text,
   * false otherwise.
   */

  private static boolean deleteLicense(String license, File file) throws IOException{
    File tmpFile = new File(file.getAbsolutePath()+".tmp");
    if (!file.renameTo(tmpFile))
      throw new IOException("Unable to rename "+file+" into "+tmpFile);

    OutputStream out = null;
    InputStream in = null;

    try{
      in = new FileInputStream(tmpFile);
      int i = 0, b;
      while (i < license.length()){
        b = in.read();
        if ((b==-1) || ((byte)b != license.charAt(i++))){
          in.close();
          in = null;
          if (!tmpFile.renameTo(file))
            throw new IOException("Unable to rename "+tmpFile+" into "+file);
          return false;
        }
      }
      out = new FileOutputStream(file); 
      IOUtilities.pump(in, out);
    } finally{
        if (in != null)
          in.close();
        if (out != null)
          out.close();
      }
    if (!tmpFile.delete())
      throw new IOException("Unable to delete "+tmpFile);

    return true;
  }



}
