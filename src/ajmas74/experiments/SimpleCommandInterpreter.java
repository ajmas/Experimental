package ajmas74.experiments;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SimpleCommandInterpreter {
	
	private static char[] ESCAPABLE_CHARACTERS = new char[] {
	  '\\','\"','/'
	};
  private static char ESCAPE_CHARACTER = '\\';
  
//  private boolean inQuotes = false;
//  private boolean inSmallQuotes = false;
//  private boolean escaped= false;

	private List _commandHistory;
	private String _errorMessage;
	
	public SimpleCommandInterpreter() {
    _commandHistory = new Vector();
	}
	
	public String getErrorMessage() {
	  return _errorMessage;
	}
	
  /*
   * Need to be able to handle the unterminated escape,
   * a la bash
   */
	public List parse ( String line ) {
	  Vector v = new Vector();
	  
    _errorMessage = null;	  
	  StringBuffer tmpBuffer = new StringBuffer();
	  boolean inQuotes = false;
    boolean inSmallQuotes = false;
    boolean escaped= false;
	  for ( int i=0; i<line.length();i++ ) {
		  char c = line.charAt(i);
		  
		  if ( escaped ) {
        tmpBuffer.append(c);
        escaped = false;
		  } else if ( c == ESCAPE_CHARACTER ) {
        escaped = true;
		  } else if ( c == '\"' && !inSmallQuotes ) {
        inQuotes = !inQuotes;
			}	else if ( c == '\'' && !inQuotes ) {
        inSmallQuotes = !inSmallQuotes;
      } else if ( c == ' ' && (!inQuotes && !inSmallQuotes)) {
        v.add(tmpBuffer.toString());
        tmpBuffer.setLength(0);
      } else if ( c == '\t' && (!inQuotes && !inSmallQuotes)) {
        v.add(tmpBuffer.toString());
        tmpBuffer.setLength(0);
		  } else if ( c == '\07') {
        Toolkit.getDefaultToolkit().beep();
		    		    //System.
			} else {
			  if ( c != '\n' && c != '\r') {
        	tmpBuffer.append(c);
			  }
			}
	  }
	  if (!inQuotes && !inSmallQuotes && tmpBuffer.length() > 0) {
	    
      v.add(tmpBuffer.toString());
      tmpBuffer.setLength(0);
	  }
    
    return v;
	}
	
	boolean isEscapableCharacter( char c ) {
	  for ( int i=0; i<ESCAPABLE_CHARACTERS.length;i++ ) {
	    if (ESCAPABLE_CHARACTERS[i] == c) {
	      return true;
	    }
	  }
	  return false;
	}
	
	public static void main ( String[] args ) {
		//System.out.println(breakUp("ls -l \" \" 'x' \'\"\' \"\'\" *"));
    //System.out.println(breakUp("ls -l \"\\\\\" "));
    //System.out.println(breakUp("ls -l \\ "));
    SimpleCommandInterpreter simpleCLI = new SimpleCommandInterpreter();
    StringBuffer strBuf = new StringBuffer();
    try {
      while ( true ) {
        strBuf.setLength(0);
        System.out.print("> ");
        while ( true ) {
          char c = (char) System.in.read();
          if ( c == '\n' ) {
            break;
          }
          strBuf.append(c);
        }
        System.out.println(simpleCLI.parse(strBuf.toString()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
	}
}
