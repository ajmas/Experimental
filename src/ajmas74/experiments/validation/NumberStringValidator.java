/* Created on 19-Feb-2004 */
package ajmas74.experiments.validation;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class NumberStringValidator implements StringValidatorIF {

  /**
   * 
   */
  public NumberStringValidator() {
    super();
    // TODO Auto-generated constructor stub
  }

  public boolean validate ( String str, String numberType ) throws Exception {
    if ( numberType == null ) {
      numberType = "int";
    }
    if ( numberType.equals("int") ) {
      Integer.parseInt(str);
      return true;
    } else if ( numberType.equals("long") ) {
      Long.parseLong(str);
      return true;	      
    } else if ( numberType.equals("float") ) {
      Float.parseFloat(str);
      return true;	      
    } else if ( numberType.equals("double") ) {
      Double.parseDouble(str);
      return true;	      
    } else {
      // unknown number type
    }
    return true;
  }
  
}
