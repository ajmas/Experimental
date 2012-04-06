package ajmas74.experimental.graphingcalc;

/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BinaryFunctions {

  public static double calculate(String function, double valueA, double valueB) {
    if (function.equals("^")) {
      return Math.pow(valueA, valueB);
    } else if (function.equals("*")) {
      return valueA * valueB;
    } else if (function.equals("/")) {
      return valueA / valueB;
    } else if (function.equals("+")) {
      return valueA + valueB;
    } else if (function.equals("-")) {
      return valueA - valueB;
    } 
    return Double.NaN;
  }

	public static void main ( String[] args ) {
	  System.out.println(BinaryFunctions.calculate("-",0,2));
	}
}
