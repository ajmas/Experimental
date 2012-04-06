/* Created on 19-Feb-2004 */
package ajmas74.experiments.validation;

import java.util.*;

/**
 * This class is designed to allow a collection of validations
 * to be performed in one go on a string.
 * 
 * @author Andre-John Mas
 */
public class StringValidation {

  boolean _throwException = false;
  Map _stringValidatorMap;
  
  /**
   * 
   */
  public StringValidation() {
    super();
    _stringValidatorMap = new HashMap();
    
    //eventually this will be moved to a resource file or some
    //other location allowing for extending the validators
    //available at runtime
    _stringValidatorMap.put("charset",CharsetStringValidator.class);
    _stringValidatorMap.put("encoding",EncodingStringValidator.class);
    _stringValidatorMap.put("number",NumberStringValidator.class);
    _stringValidatorMap.put("null",NullStringValidator.class);
    _stringValidatorMap.put("length",StringLengthValidator.class);        
    // length
    // empty
    // format
    // null
  }

  
  public boolean isValid( String str, String[] conditions, boolean throwException ) 
  throws Exception {
    if ( str == null ) {
      return false;
    }
    int len = conditions.length;
    try {
      boolean result = false;
	    for ( int i=0; i<len; i++ ) {
	      String[] condition = splitCondition(conditions[i]);
	      
	      StringValidatorIF validator
	        = (StringValidatorIF) _stringValidatorMap.get(condition[0]);
	      if ( validator != null ) {
	        if ( !validator.validate(str,condition[1]) ) {
	          return false;
	        }
	      } else {
	        	//should we throw an Exception or Log the error?
	      }
	    }
	    return true;
    } catch ( Exception ex ) {
      if ( throwException ) {
        throw ex;
      } else {
        //_log.
      }
    }
    return false;
  }
  
  /** Note I could have used String.split(), but in
   *  in order to ensure compatibility with Java 1.3
   *  it is not used.
   * @param str
   * @param validChars
   * @return
   */
  private String[] splitCondition ( String condition ) {
    String[] parts = null;
    int idx = 0;
    if ( (idx=condition.indexOf('=')) > -1 ) {
      parts = new String[2];
      parts[0] = condition.substring(0,idx-1);
      parts[1] = condition.substring(idx);
    } else {
      parts = new String[2];
      parts[0] = condition;
      parts[1] = "";
    }
    return parts;
  }

  public static void main ( String[] args ) {
    try {
      String testStr = null; //"HelloThere";
      String[] conditions = new String[0];
      
      StringValidation sv = new StringValidation();
      System.out.println("valid="+sv.isValid(testStr,conditions,false));
      
    } catch ( Exception ex ) {
      ex.printStackTrace();
    }
  }
}
