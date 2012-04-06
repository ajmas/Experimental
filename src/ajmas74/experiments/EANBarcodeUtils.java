package ajmas74.experiments;

/**
 * Small util class to calculate and check the parity of EAN
 * (European Article Number) bar codes.
 * http://www.ex.ac.uk/cimt/resource/barcode.htm
 * 
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class EANBarcodeUtils {

	/**
	 * Constructor for EANBarcodeUtils.
	 */
	public EANBarcodeUtils() {
		super();
	}

  private static int[] stringToIntArray ( String digitString ) 
    throws NumberFormatException {
    int asciiZero = (int) '0';    
    try {
      Integer.parseInt(digitString);
    } catch ( NumberFormatException ex ) {
      throw ex;
    }
    int len = digitString.length();
    int[] digits = new int[len];
    for ( int i=0; i<len; i++ ) {
      digits[i] = ((int)digitString.charAt(i)) - asciiZero;
    }
    return digits;   
  }
  
  /**
   */
  public static int findCheckDigit( String digitString ){
   
    if ( digitString.length() != 7 ) {
      return -1;
    }
    return findCheckDigit(stringToIntArray(digitString));
  }
  
  /**
   */  
  public static int findCheckDigit( int[] digits ) {
    int a = 3 * ( digits[0] + digits[2] + digits[4] + digits[6]);
    int b = digits[1] + digits[3] + digits[5];
    int c = a + b;
    if ( a % 10 == 0 ) {
      return 0;
    } else {
      for ( int i=0; i<10; i++ ) {
        if ( (i + c) % 10 == 0 ) {
          return i;
        }
      }
    }
    return -3;
  }
  
  public static boolean isParityOK ( String digitString ) {
    if ( digitString.length() != 8 ) {
      return false;
    }
    return isParityOK(stringToIntArray(digitString));    
  }
  
  public static boolean isParityOK ( int[] digits ) {
    int a = 3 * ( digits[0] + digits[2] + digits[4] + digits[6]);
    int b = digits[1] + digits[3] + digits[5] + digits[7];
        
    return ( (a+b) % 10 == 0 );
  }
      
  public static void main ( String[] args ) {
    System.out.println("check digit=" + findCheckDigit("0003454"));
    System.out.println("parity ok=" + isParityOK("00034548"));
  }
}
