/* Created on 19-Feb-2004 */
package ajmas74.experiments.validation;

/**
 * This String validator checks to see whether the string
 * contains only the characters int he specified character
 * set. Note that a character set is not the same thing as
 * character encoding. A character encoding is used to refer
 * to a named character set, such as 'UTF8', 'ASCII', etc, while
 * a character set is a string containing a user specifed
 * subset. For example, where the subset is the string 'ABC123'
 * the string 'AB12' is valid, while 'AB14' is not.
 * 
 * @author Andre-John Mas
 */
public class CharsetStringValidator implements StringValidatorIF {

  /**
   * 
   */
  public CharsetStringValidator() {
    super();
    // TODO Auto-generated constructor stub
  }

  public boolean validate ( String str, String validChars ) throws Exception {
    int len = str.length();
    for ( int i=0; i<len; i++ ) {
      char c = str.charAt(i);
      for ( int ix=0; ix<len; ix++ ) {
        if ( validChars.indexOf(c) < 0 ) {
          return false; 
        }
      }
    }
    return true;
  }
  
}
