/* Created on 19-Feb-2004 */
package ajmas74.experiments.validation;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class NullStringValidator implements StringValidatorIF {

  /**
   * 
   */
  public NullStringValidator() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * validates to see whether a string is null. if the condition is
   * 'true' then the null string is considered valid, otherwise any
   * other value will consider the null string invalid
   * 
   * @see ajmas74.experiments.validation.StringValidatorIF#validate(java.lang.String, java.lang.String)
   */
  public boolean validate(String str, String condition) throws Exception {
    boolean nullValid = false;
    
    nullValid = condition.equals("true");
    
    if ( nullValid ) {
      return true;
    } else {
      return ( str != null);      
    }


  }

}
