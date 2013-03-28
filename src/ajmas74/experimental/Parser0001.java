/* Created on 25-Jun-2003 */
package ajmas74.experimental;

import java.util.*;
import java.text.ParseException;

/**
 * @author Andre-John Mas
 *
 */
public class Parser0001 {

  private static final String RESERVED_CHARS = "(),\"\n";
  private static char ESCAPE_CHARACTER = '\\';
  

  /* I could probably change it so that the states handle less
   * work, but this seems to work for me, as is. The real work
   * is in seeing whether the parser is accepting strings that
   * should not be accepted.
   * 
   */
  /** Parses a macro who's syntax is as follows:
   *  <pre>
   *  name
   *  name()
   *  name(var)
   *  name(var,var);
   * </pre>
   * variable may be quoted or unquoted.
   * <p>
   *   
   * @param line
   * @return
   * @throws ParseException
   */
  public List parse ( String line ) throws ParseException{
      String macroName = "";
      
      Vector v = new Vector();
    
      StringBuffer tmpBuffer = new StringBuffer();

      boolean escaped= false;
      int state = 0;   
      int errorValue = -1;   
      boolean inQuotes = false;
      int lastSeparatorIdx = -1; 
      boolean gotVar = false;
      int idx = 0;
      forLoop: for ( idx=0; idx<line.length();idx++ ) {
        char c = line.charAt(idx);
        switch ( state ) {
          case -1: // deals with the error state
            break forLoop;
          case 0: // deals with everything before the brackets
            if ( c == '(' ) { // looks like we have parameters
              state = 1;
              lastSeparatorIdx = idx;
              v.add(tmpBuffer.toString());
              tmpBuffer.setLength(0);
              gotVar = false;
            } else if ( RESERVED_CHARS.indexOf(c) < 0){
              tmpBuffer.append(c);
              gotVar = true;
            } else {  //  got a reserved char out of place
              state = -1;
              errorValue = 1;              
            }
            break;
          case 1: // deals with everything within the brackets
            if ( escaped ) {
              tmpBuffer.append(c);
              escaped = false;  
            } else if ( c == '\\' && inQuotes ) { // looks like we have an escaped char
              escaped = true;     
            } else if ( c == ',' && !inQuotes) { // looks like we another parameter
              if ( tmpBuffer.length() == 0 ) {              
                state = -1;
                errorValue = 3; 
                break;               
              }
              lastSeparatorIdx = idx;
              v.add(tmpBuffer.toString());
              tmpBuffer.setLength(0);
              gotVar = false;
            } else if ( c == ')' && !inQuotes) {  // we should now be finished
              state = 2;
              lastSeparatorIdx = idx;              
            } else if ( c == '"' ) { //  either going start or finishing a string
              inQuotes = !inQuotes;
              gotVar = true;
            } else if ( RESERVED_CHARS.indexOf(c) < 0) {
              tmpBuffer.append(c);              
            } else { //  got a reserved char out of place
              state = -1;
              errorValue = 2;
            }
            break;
          case 2: // should never get here, unless junk after closing bracket
            state = -1;
            errorValue = 2;
            break;
        }
        
      }
      
      
      if ( state != 2 && state != 0 ) { // we weren't in a finishing state, something went wrong
        if  (errorValue == 1) {
          throw new ParseException("encountered a reserved character in macro name (offset " + (idx-1) + ")", (idx-1));                          
        } else if (errorValue == 2) {
          throw new ParseException("syntax error (offset " + (idx-1) + ")", (idx-1));
        } else if (errorValue == 3) {
          throw new ParseException("no parameter specified before comma (offset " + (idx-1) + ")", (idx-1));                          
        } else {
          throw new ParseException("syntax error (offset " + (idx-1) + ")", (idx-1));          
        }
      } else if (gotVar || tmpBuffer.length() > 0){        
        v.add(tmpBuffer.toString());
         tmpBuffer.setLength(0);            
      }

      return v;
    }
    

  
  /** tests */
  public static void main ( String[] args ) {
    try {
      System.out.println((new Parser0001()).parse("abcd(\"xx\",\"yy\")"));
      // should we tolerate this?
      System.out.println((new Parser0001()).parse("abcd(+1,\"xx\",-1)"));
      System.out.println((new Parser0001()).parse("abcd()"));
      System.out.println((new Parser0001()).parse("abcd(\"\\\")"));
            
      System.out.println((new Parser0001()).parse("abcd"));  
      System.out.println((new Parser0001()).parse("abcd(\"\")"));  
            
      System.out.println((new Parser0001()).parse("abcd(,\"yy\")"));         
      //System.out.println((new Parser0001()).parse("abcd(,\"yy)"));
      //System.out.println((new Parser0001()).parse("abcd)"));
      System.out.println((new Parser0001()).parse("abcd(,)"));      
    } catch (ParseException e) {
      // XXX Auto-generated catch block
      e.printStackTrace();
    }
  }
}
