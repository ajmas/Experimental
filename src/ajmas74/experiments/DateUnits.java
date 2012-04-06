/* Created on 25-Nov-2003 */
package ajmas74.experiments;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class DateUnits {

	public final static char SECOND_UNIT  = 's';
  public final static char MINUTE_UNIT  = 'm';
	public final static char HOUR_UNIT    = 'h';
	/** month assumed to be 30 days */
	public final static char MONTH_UNIT   = 'M';
	public final static char DAY_UNIT     = 'D';	
	public final static char WEEK_UNIT    = 'W';
	
	public final static int SECOND_VALUE  = 1;
	public final static int MINUTE_VALUE  = 60;
	public final static int HOUR_VALUE    = 3600;

	public final static int DAY_VALUE     = HOUR_VALUE*24;
	/** month assumed to be 30 days */
	public final static int MONTH_VALUE   = DAY_VALUE*30;
	public final static int WEEK_VALUE    = DAY_VALUE*7;
	
  /**
   * 
   */
  public DateUnits() {
    super();
    // TODO Auto-generated constructor stub
  }

  public static long valueInSeconds ( String numberValue ) {
    int total = 0;
    int marker = 0;
    for ( int i=0; i<numberValue.length(); i++ ) {
      char c = numberValue.charAt(i);
      if ( Character.isLetter(c) ) {
				int multiplier = 0;
        String numberStr = numberValue.substring(marker,i);
        int number = Integer.parseInt(numberStr);
        //System.out.println(numberStr);
				//System.out.println(c);
				switch ( c ) {
				  case SECOND_UNIT:
					  multiplier = SECOND_VALUE;
				    break;
					case MINUTE_UNIT:
						multiplier = MINUTE_VALUE;
						break;	
					case HOUR_UNIT:
						multiplier = HOUR_VALUE;
						break;	
					case DAY_UNIT:
						multiplier = DAY_VALUE;
						break;					
					case WEEK_UNIT:
						multiplier = WEEK_VALUE;
						break;		
					case MONTH_UNIT:
						multiplier = MONTH_VALUE;
						break;								
				  default:
				}
				total+=(number*multiplier);
				marker = i+1;
      }
    }
    return total;
  }

	public static long valueInMilliseconds ( String numberValue ) {
		return valueInSeconds(numberValue) * 1000;
	}
	
  public static void main ( String[] args ) {
    System.out.println(valueInSeconds("30h20m"));
		System.out.println(valueInMilliseconds("30D20m"));
  }
}
