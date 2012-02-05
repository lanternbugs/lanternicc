package free.util;



/**
 * Allows one to attach a name to an object. The name is also returned by the
 * <code>toString</code> method, making this class useful for objects put into
 * Swing container classes (<code>JTables</code> and such).
 */

public class NamedObject{
  
  
  
  /**
   * The object.
   */
  
  private final Object target;
  
  
  
  /**
   * The name.
   */
  
  private final String name;
  
  
  
  /**
   * Creates a new <code>NamedObject</code> with the specified target and name.
   */

  public NamedObject(Object target, String name){
    this.target = target;
    this.name = name;    
  }
  
  
  
  /**
   * Returns the target.
   */
  
  public Object getTarget(){
    return target;
  }
  
  
  
  /**
   * Returns the name.
   */
  
  public String getName(){
    return name;
  }

  
  
  /**
   * Returns the name.
   */
  
  public String toString(){
    return getName();
  }
  
  
  
}
