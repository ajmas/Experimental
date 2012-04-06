package ajmas74.experimental.graphingcalc;

/**
 * @author ajmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Constant {
	
	private String _name;
	
	/**
	 * Constructor for Constants.
	 */
	Constant( String name ) {
		super();
		_name = name;
	}

	public double getValue () {
		return getConstantValue(_name);
	}
	
	public String getConstantSymbol() {
		return _name;
	}
	
	public String toString() {
		return _name;
	}
	
	// --------------------------------------------
	
	public static Constant getConstantFor( String constant ) {
		if ( isConstant(constant) ) {
			return new Constant(constant);
		} else {
			return null;
		}
	}
	
	public static boolean isConstant(char constant) {
		return isConstant(constant + "");
	}
	
	public static boolean isConstant(String constant) {
		return (!Double.isNaN(getConstantValue(constant)));
	}

	public static double getConstantValue(char constant) {
		return getConstantValue(constant + "");
	}
			
	public static double getConstantValue(String constant) {
		if ( constant.equals("\u03C0") || constant.equals("pi") ) {		
			return Math.PI;
		} else {
			return Double.NaN;
		}
	}
	
}
