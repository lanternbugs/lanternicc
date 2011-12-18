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
import javax.swing.text.*;
import java.net.*;
import java.applet.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.io.*;

class credentials
{
DesEncrypter encrypter;  
credentials()
{

// 8-byte Salt
    byte[] salt = {
        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
    };

    // Iteration count
    int iterationCount = 19;
String passPhrase = "***Yjohioherfe999ydyoane9uinoui8";
try {
 KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
 SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
 encrypter = new DesEncrypter(key);
}catch(Exception e){}


}
void resetNamePass()
{
String mess = "\r\n\r\n";
FileWrite writer = new FileWrite();
writer.write(mess, "lantern_credentials.txt");


}
void saveNamePass(String name, String pass)
{
name = encrypter.encrypt(name);
pass = encrypter.encrypt(pass);
String mess = name + "\r\n" + pass + "\r\n";
FileWrite writer = new FileWrite();
writer.write(mess, "lantern_credentials.txt");
}// end method save name and pass





String getName()
{
scriptLoader loadScripts = new  scriptLoader();
 ArrayList<String> namepass = new ArrayList();
loadScripts.loadScript(namepass, "lantern_credentials.txt");
String name = "";

if(namepass.size() >=1)
{
name=namepass.get(0);
if(name.equals(""))
return "";
name=encrypter.decrypt(name);
}

return name;

}// end method get name

String getPass()
{
scriptLoader loadScripts = new  scriptLoader();
 ArrayList<String> namepass = new ArrayList();
loadScripts.loadScript(namepass, "lantern_credentials.txt");
String pass = "";

if(namepass.size() >=2)
{
pass=namepass.get(1);
if(pass.equals(""))
return "";
pass=encrypter.decrypt(pass);
}

return pass;

}// end method get pass

// below class http://exampledepot.com/egs/javax.crypto/DesString.html
class DesEncrypter {
    Cipher ecipher;
    Cipher dcipher;
 // 8-byte Salt
    byte[] salt = {
        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
    };

    // Iteration count
    int iterationCount = 19;

    DesEncrypter(SecretKey key) {
        try {
            ecipher =Cipher.getInstance(key.getAlgorithm()); // Cipher.getInstance("DES");
            dcipher = Cipher.getInstance(key.getAlgorithm()); //Cipher.getInstance("DES");
  // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (Exception E){}
    }

  public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

   public String decrypt(String str) {
        try {
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }
}




}// end class