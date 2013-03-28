package ajmas74.experimental.maths;

import java.math.BigInteger;

/**
 * This class provides a collection of methods for converting between bases.
 * 
 * @author Andre-John Mas, 2013-03-24 
 *
 */
public class BaseConversion {

    /**
     * Method that take a numerical value of the base described in the baseInStr
     * and returns a new numerical value of the base baseOutStr. For example,
     * base 10 to base 16 (hex) would be:
     * 
     * baseInStr: "0123456789"
     * baseOutStr: "0123456789ABCDEF"
     * 
     * with some input numerical value;
     * 
     * This is limited to values that can be represented by a long.
     * 
     * Note that any negative signs are maintained in the result. If this does
     * not suit your use-case you should remove the sign. Positive signs are
     * not supported in the input.
     * 
     * @param baseInStr the string representing the input base
     * @param baseOutStr the string representing the output base
     * @param value
     * @return
     */
    public static String baseToBase (String baseInStr, String baseOutStr, String value ) {
    	int baseIn = baseInStr.length();
    	int baseOut = baseOutStr.length();
    	String signStr = "";
    	if (value.startsWith("-")) {
    		signStr = "-";
    		value = value.substring(1);
    	}
    	
    	long base10Value = 0;
    	
    	for (int i=0; i<value.length(); i++) {
    		base10Value = base10Value * baseIn;
    		base10Value += baseInStr.indexOf(value.charAt(i));
    	}
    	    	
    	if (base10Value < 0) {
    		throw new RuntimeException("Value exceeds size of long (too " + (signStr.length()>0?"small":"big") + ").");
    	}
    	
    	StringBuilder strBuilder = new StringBuilder();
    	
    	while (base10Value >= baseOut ) {    		
			strBuilder.append(baseOutStr.charAt((int)(base10Value % baseOut)));
			base10Value = base10Value / baseOut;
    	}    	
    	
    	strBuilder.append(baseOutStr.charAt((int)base10Value));
    	        	
    	return strBuilder.append(signStr).reverse().toString();
    }        
    
    /**
     * This is an implementation of {@link #baseToBase(String, String, String)} implemented
     * to deal with very large values, being backed by BigInteger. It is significantly
     * slower than its counterpart intended for non-big numbers, so this should be used
     * in special cases. 
     * 
     * @param baseInStr the string representing the input base
     * @param baseOutStr the string representing the output base
     * @param value
     * @return
     */
    public static String bigBaseToBigBase (String baseInStr, String baseOutStr, String value ) {
    	BigInteger baseIn = BigInteger.valueOf(baseInStr.length());
    	BigInteger baseOut = BigInteger.valueOf(baseOutStr.length());
    	
    	String signStr = "";
    	if (value.startsWith("-")) {
    		signStr = "-";
    		value = value.substring(1);
    	}
    	
    	BigInteger base10Value = BigInteger.ZERO;
    	    	
    	for (int i=0; i<value.length(); i++) {
    		base10Value = base10Value.multiply(baseIn);
    		base10Value = base10Value.add(BigInteger.valueOf(baseInStr.indexOf(value.charAt(i))));
    	}
    	    	
    	StringBuilder strBuilder = new StringBuilder();
    	
    	while (base10Value.compareTo(baseOut) >= 0) {    					    					
			strBuilder.append(baseOutStr.charAt(base10Value.mod(baseOut).intValue()));
			base10Value = base10Value.divide(baseOut);
    	}
    	
    	strBuilder.append(baseOutStr.charAt(base10Value.intValue()));
    	    	    	
    	return strBuilder.append(signStr).reverse().toString();
    }

    
    /**
     * Implementation of {@link #baseToBase(String, String, String)} that
     * takes a base 10 numerical value and outputs a value using the
     * specified base string.
     * 
     * This also falls back to bigBaseToBigBase for cases where value
     * is equal to Long.MIN_VALUE.
     * 
     * Note, this is slower than {@link Long#toHexString(long)} and other
     * such methods provided by the Long class, so this is best used in
     * cases where it is not possible to use those methods. For example,
     * where the base conversion or base 'alphabet' is not provided by
     * the Log class.
     * 
     * @param baseOutStr
     * @param value
     * @return
     */
    public static String base10ToBase (String baseOutStr, long value) {    	
    	int baseOut = baseOutStr.length();
    	String signStr = "";
    	if (value == Long.MIN_VALUE) {
    		return bigBaseToBigBase("0123456789",baseOutStr,Long.toString(value));
    	} else {
	    	if (Math.signum(value) < 0) {
	    		signStr = "-";
	    		value = Math.abs(value);
	    	}
    	}
    	
    	StringBuilder strBuilder = new StringBuilder();
    	
    	while (value >= baseOut ) {    		
			strBuilder.append(baseOutStr.charAt((int)(value % baseOut)));
			value = value / baseOut;
    	}    	
    	
    	strBuilder.append(baseOutStr.charAt((int)value));
    	        	
    	return strBuilder.append(signStr).reverse().toString();	
    }
    
    /**
     * Converts a value in the given base to a long.
     * 
     * 
     * @param baseInStr
     * @param value
     * @return
     */
    public static long baseToBase10 (String baseInStr, String value) {    	
    	int baseIn = baseInStr.length();
    	int sign = 1;
    	if (value.startsWith("-")) {
    		sign = -1;
    		value = value.substring(1);
    	}
    	
    	long base10Value = 0;
    	
    	for (int i=0; i<value.length(); i++) {
    		base10Value = base10Value * baseIn;
    		base10Value += baseInStr.indexOf(value.charAt(i));
    	}
    	
       	if (base10Value < 0) {
    		throw new RuntimeException("Value exceeds size of long (too " + (sign < 0?"small":"big") + ").");
    	}
    	
       	
    	return sign * base10Value;
    }
    

    public static void main ( String[] args ) {        
        
    	System.out.println( Math.abs(Long.MIN_VALUE+1) );
    	
    	System.out.println( base10ToBase("0123456789", 1000) );
    	System.out.println( base10ToBase("0123456789ABCDEF", 255) );
    	System.out.println( base10ToBase("01", 255) );
    	
    	System.out.println("A **");
    	
    	// Base 10 to Base 10
    	System.out.println( baseToBase("0123456789", "0123456789", "0") );
        System.out.println( baseToBase("0123456789", "0123456789", "12345") );        
        System.out.println( baseToBase("0123456789", "0123456789", "-12345") );
        
        System.out.println("B **");
        
        // Base 10 to Base 16 (Hex)
        System.out.println( baseToBase("0123456789", "0123456789ABCDEF", "15") );
        System.out.println( baseToBase("0123456789", "0123456789ABCDEF", "16") );
        System.out.println( baseToBase("0123456789", "0123456789ABCDEF", "255") );

        System.out.println("C **");
        
        // Base 10 to Base 16 (Hex)
        System.out.println( baseToBase("0123456789", "012345678", "16") );
        
        // Base 8 (Octal) to Base 16 (Hex)
        System.out.println( baseToBase("01234567", "0123456789ABCDEF", "16") );        
        System.out.println( baseToBase("01234567", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "16") );
        
        System.out.println("D **");
        
        // Big Integer
        
    	// Base 10 to Base 10        
        System.out.println( bigBaseToBigBase("0123456789", "0123456789", Long.toString(Long.MAX_VALUE) + "0") );
        System.out.println( bigBaseToBigBase("0123456789", "0123456789ABCDEF", "0") );
        System.out.println( bigBaseToBigBase("0123456789", "0123456789ABCDEF", Long.toString(Long.MAX_VALUE) + "0") );
        
        System.out.println("E **");
        
        System.out.println( bigBaseToBigBase("0123456789", "01234567", Long.toString(Long.MAX_VALUE) + "0") );        
        System.out.println( bigBaseToBigBase("0123456789", "0123456789ABCDEF", "-" + Long.toString(Long.MAX_VALUE) + "0") );

        System.out.println("F **");
        
        System.out.println( bigBaseToBigBase("0123456789", "01", Long.toString(Long.MAX_VALUE)) );        
    	System.out.println( Long.toBinaryString(Long.MAX_VALUE));
    	
    	System.out.println("G **");
    	
        System.out.println( bigBaseToBigBase("0123456789", "01", Long.toString(Math.abs(Long.MIN_VALUE))) );        
    	System.out.println( Long.toBinaryString(Long.MIN_VALUE));
    	
    	System.out.println("H **");
    	
    	System.out.println( baseToBase10("0123456789BCDFGHJKLMNPQRSTVWXZ", "HBV6R" ) );
    	System.out.println( baseToBase10("0123456789BCDFGHJKLMNPQRSTVWXZ", "RG77T" ) );
    	
    	System.out.println("I **");

    	System.out.println( Long.toHexString(Long.MIN_VALUE));
    	System.out.println( bigBaseToBigBase("0123456789", "0123456789ABCDEF", Long.toString(Long.MIN_VALUE) ) );
    	
    	System.out.println( base10ToBase("0123456789ABCDEF", Long.MIN_VALUE ) );

    	System.out.println("J **");
    	
    	// Handle size exceptions and fall back to bigBaseToBigBase()
    	try {
        	System.out.println( "A1: " + baseToBase("0123456789", "0123456789ABCDEF", Long.toString(Long.MIN_VALUE) ) );
    	} catch (Exception ex) {
    		//ex.printStackTrace();
        	System.out.println( "A2: " + bigBaseToBigBase("0123456789", "0123456789ABCDEF", Long.toString(Long.MIN_VALUE) ) );    		
    	}
    	
    	try {
    		System.out.println( "B1: " + baseToBase("0123456789", "0123456789ABCDEF", Long.toString(Long.MAX_VALUE) + "1" ) );
    	} catch (Exception ex) {
    		//ex.printStackTrace();
        	System.out.println( "B2: " + bigBaseToBigBase("0123456789", "0123456789ABCDEF", Long.toString(Long.MAX_VALUE) + "1" ) );

    	}
    	
       	try {
        	System.out.println( "C1: " + baseToBase10("0123456789", Long.toString(Long.MAX_VALUE) + "1" ) );
    	} catch (Exception ex) {
    		//ex.printStackTrace();
        	System.out.println( "C2: " + bigBaseToBigBase("0123456789", "0123456789", Long.toString(Long.MAX_VALUE) + "1" ) );
    	}
    	
    	// -----
    	
//    	int  loopFor = 20000;
//    	long t = System.currentTimeMillis();
//    	int loopCount = 0;
//        
//    	while ( System.currentTimeMillis() - t < loopFor ) {
//        	baseToBase("0123456789","0123456789ABCDEF", "255");
//        	loopCount++;
//        }
//        
//    	System.out.println("loopCount: " + loopCount);    	
    	
    }
}
