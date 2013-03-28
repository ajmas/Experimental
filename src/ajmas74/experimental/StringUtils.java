/* Created on 4-Mar-2004 */
package ajmas74.experimental;


/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class StringUtils {

  //-------------------------------------------------------------------------------------------------
  /**
   * from: com.omniscium.utils - http://sourceforge.net/projects/jcmd
   * orginal code licensed under GPL
   * 
   * Match function for actually performing the filter function.
   *
   * @param s The string to match.
   * @param m The filter.
   * @return true if matched, false otherwise.
   */
  //-------------------------------------------------------------------------------------------------
  public static boolean match (String s, String m) 
  {
      if ((s == null) || (m == null))
      {
          throw new IllegalArgumentException ("Cannot pass in null arguments.");
      }

      final int sl = s.length();
      final int ml = m.length();
      int pos = 0;

      System.out.println("...................");
      System.out.println("s="+s);
      System.out.println("m="+m);
      for (pos = 0; pos < sl && pos < ml && (m.charAt (pos)== '?' || Character.toUpperCase (m.charAt (pos)) == Character.toUpperCase (s.charAt (pos))); ++pos)
      {
        System.out.println("pos="+(pos)+",c1="+m.charAt (pos)+",c2="+s.charAt (pos));
      }
  
      return pos == ml ? pos == sl : m.charAt (pos) == '*' && (match (s.substring (pos), m.substring (pos + 1)) || (pos < sl && match (s.substring (pos + 1), m.substring (pos))));
  }
  
  public static boolean match2 (String s, String m) 
  {
      if ((s == null) || (m == null))
      {
          throw new IllegalArgumentException ("Cannot pass in null arguments.");
      }

      final int sl = s.length();
      final int ml = m.length();
      int pos = 0;

      System.out.println("...................");
      System.out.println("s="+s);
      System.out.println("m="+m);
      for (pos = 0; pos < sl && pos < ml && (m.charAt (pos)== '?' || Character.toUpperCase (m.charAt (pos)) == Character.toUpperCase (s.charAt (pos))); ++pos)
      {
        System.out.println("pos="+(pos)+",c1="+m.charAt (pos)+",c2="+s.charAt (pos));
      }
  
      if ( pos == ml ) {
        return pos == sl;
      } else {
        return m.charAt (pos) == '*' && (match (s.substring (pos), m.substring (pos + 1)) || (pos < sl && match (s.substring (pos + 1), m.substring (pos))));
      }
      //return pos == ml ? pos == sl : m.charAt (pos) == '*' && (match (s.substring (pos), m.substring (pos + 1)) || (pos < sl && match (s.substring (pos + 1), m.substring (pos))));
  }
  
  public static void  main( String[] args ) {
    System.out.println("should match: ");   
    System.out.println(" match: " + match ("abcdef","a*f"));
    System.out.println(" match: " + match ("abcdef","a**f"));
    System.out.println(" match: " + match ("abcdef","a**f"));  
    System.out.println(" match: " + match ("abcdef","a*c*f"));
    System.out.println(" match: " + match ("abcdefghijklm","a*cd*f*g*"));
    System.out.println(" match: " + match ("abcdefghijklm","a*cd*f*g*"));    
    
    System.out.println("--------------------------------------");
    System.out.println("should not match: ");      
    System.out.println(" match: " + match ("abcdef","a*g"));
    System.out.println(" match: " + match ("abcdef","a*f*f"));
    System.out.println(" match: " + match ("abcdef","ab**g"));  
    
  }
}
