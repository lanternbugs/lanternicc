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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA
 */

package free.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;



/**
 * A simple localization utility, backed up by a <code>Properties</code> object
 * loaded from a <code>localization.properties</code> resource.
 */

public class Localization{
  
  
  
  /**
   * The locale used for the application.
   */
  
  public static volatile Locale appLocale = Locale.getDefault();
  
  
  
  /**
   * The <code>Property</code> object holding the translations.
   */
  
  private final Properties props;
  
  
  
  /**
   * The name of the class this <code>Localization</code> is for. 
   */
  
  private final String className;
  
  
  
  /**
   * Creates a new <code>Localization</code> backed up by the specified
   * <code>Properties</code> object for the specified class.
   * The keys in the properties resource are prefixed by the class name.
   */
  
  private Localization(Properties props, String className){
    this.props = props;
    this.className = className;
  }
  
  
  
  /**
   * Sets the locale for this application to the specified locale.
   */
  
  public static void setAppLocale(Locale locale){
    if (locale == null)
      throw new IllegalArgumentException("locale may not be null");
    
    Localization.appLocale = locale;
  }
  
  
  
  /**
   * Loads and returns a <code>Localization</code> object for the specified
   * class and the application's locale. See {@link #load(Class, Locale)} and
   * {@link #setAppLocale(Locale)} for more details.
   */
  
  public static Localization load(Class c){
    return load(c, appLocale);
  }
  
  
  
  /**
   * Loads and returns a <code>Localization</code> object for the specified
   * class and locale. The <code>Localization</code> object is loaded from
   * property resources located in the package of the specified class. The
   * resources are looked up in the following order, with each one being the
   * parent of the following one:
   * <ol>
   *   <li><code>localization.properties</code>
   *   <li><code>localization_[language].properties</code>
   *   <li><code>localization_[language]_[country].properties</code>.
   *   <li><code>localization_[language]_[country]_[variant].properties</code>.
   * </ol>
   * where <code>[language]</code>, <code>[country]</code> and
   * <code>[variant]</code> are taken from the locale (if the locale provides
   * them).
   * Returns <code>null</code> if all of the resources are missing. 
   */
  
  public static Localization load(Class c, Locale locale){
    Properties props = loadTranslationProperties(c, locale);
    return props == null ? null : new Localization(props, Utilities.getClassName(c));
  }
  
  
  
  /**
   * Loads and returns the translation <code>Properties</code> for the specified class and locale.
   */
  
  private static Properties loadTranslationProperties(Class c, Locale locale){
    String propsFilenamePrefix = "localization";
    String language = locale.getLanguage();
    String country = locale.getCountry();
    String variant = locale.getVariant();
    
    Properties props = null;
    
    props = loadProps(c, props, propsFilenamePrefix + ".properties");
    
    if ((language != null) && !"".equals(language))
      props = loadProps(c, props, propsFilenamePrefix + "_" + language + ".properties");
    if ((country != null) && !"".equals(country))
      props = loadProps(c, props, propsFilenamePrefix + "_" + language + "_" + country + ".properties");
    if ((variant != null) && !"".equals(variant))
      props = loadProps(c, props, propsFilenamePrefix + "_" + language + "_" + country + "_" + variant + ".properties");
    
    return props;
  }
  
  
  
  /**
   * Loads and returns a <code>Properties</code> object from the resource with the specified name.
   * The specified <code>Properties</code> object is used as the default delegate for the returned one.
   */
  
  private static Properties loadProps(Class c, Properties props, String resourceName){
    InputStream in = c.getResourceAsStream(resourceName);
    
    if (in != null){
      props = new Properties(props);
      try{
        props.load(in);
      } catch (IOException e){
          throw new MissingResourceException("IOException while loading " + resourceName, c.getName(), "");
        }
    }
    
    return props;
  }
  
  
  
  /**
   * Returns the translation for the specified key.
   */
  
  public String getString(String key){
    return props.getProperty(className + "." + key);
  }
  
  
  
  /**
   * Returns a new <code>Localization</code> object backed up by the same
   * resources as this one, but for the specified class. This is useful if
   * several classes (in the same package) share a localization properties
   * resource.
   */
  
  public Localization getForClass(Class c){
    return new Localization(props, Utilities.getClassName(c));
  }
  
  
  
}
