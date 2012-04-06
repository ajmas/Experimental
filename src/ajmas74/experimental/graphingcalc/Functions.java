package ajmas74.experimental.graphingcalc;

/**
 * @author ajmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Functions {

	/**
	 * Constructor for Functions.
	 */
	public Functions() {
		super();
	}

	public double calculate ( String function, double a ) {
		return UnaryFunctions.calculate(function,a);
	}
	
	public double calculate ( String function, double a, double b ) {
		return BinaryFunctions.calculate(function,a,b);
	}
	
	public double calculate ( String function, double[] params ) {
		if ( params.length == 1 ) {
			return calculate(function, params[0]);
		} else if ( params.length == 2 ) {
			return calculate(function, params[0], params[1]);
		}
		return Double.NaN;
	}
}
