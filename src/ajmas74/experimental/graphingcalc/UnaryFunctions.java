package ajmas74.experimental.graphingcalc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class UnaryFunctions {

	public static final String SIN = "sin";
  public static final String TAN = "tan";
  public static final String COS = "cos";	  	 
  public static final String ASIN = "asin";
  public static final String ATAN = "atan";
  public static final String ACOS = "acos";	   
  public static final String LOG = "log";
  public static final String SQRT = "sqrt";	
  public static final String FLOOR = "floor";	
  
  public static final String[] FUNCTIONS = new String[] {
    "sin","tan","cos","asin","atan","acos",
    "log","sqrt","floor","ceil","exp"
  };
  
  public static boolean isUnaryFunction ( String fnName ) {
    for ( int i=0; i<FUNCTIONS.length;i++) {
	    if ( FUNCTIONS[i].equals(fnName) ) {
	      return true;
	    }
    }
    return false;
  }
  /** the approach here is probably overkill, but it allows
   *  for easy adding of new functions.
   * 
   * @param function
   * @param value
   * @return double
   */ 
 	public static double calculate ( String function, double value ) {
    try {
      boolean validFn = false;
      for ( int i=0; i<FUNCTIONS.length;i++) {
        if ( FUNCTIONS[i].equals(function) ) {
          validFn = true;
          break;
        }
      }
      if ( validFn ) {
      	Method m =Math.class.getMethod(function,new Class[] {double.class});      	      
      	return ((Double) m.invoke(Math.class,new Object[] {new Double(value)})).doubleValue();
      } else {
        System.out.println("unknown function: " + function + " - " + Arrays.binarySearch(FUNCTIONS,function));
      }      
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return 0.0;
 	}
 	 
 	public static void main ( String[] args ) {
    UnaryFunctions fns = new UnaryFunctions();
    double value = 0.1;

		if ( Double.isNaN( fns.calculate(LOG,value)) ) {
		  System.out.println("can't calculate");
		}
		
    System.out.println("result=" + fns.calculate("ceil",value));
    
    System.out.println("result=" + Math.ceil(value));
 	}
}
