/* Created on 19-Feb-2004 */
package ajmas74.experiments.validation;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class StringLengthValidator implements StringValidatorIF {

  /**
   * 
   */
  public StringLengthValidator() {
    super();
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see ajmas74.experiments.validation.StringValidatorIF#validate(java.lang.String, java.lang.String)
   */
  public boolean validate(String str, String condition) throws Exception {
    int minLen = 0;
    int maxLen = -1;
    boolean trimmed = false;
    
    if ( trimmed ) {
      str = str.trim();
    }
    if ( minLen > -1 ) {
      if ( str.length() < minLen ) {
        return false; // throw Exception
      }
    }
    if ( maxLen > -1 ) {
      if ( str.length() > maxLen ) {
        return false; // throw Exception
      }
    }    
    // TODO Auto-generated method stub
    return false;
  }

}
