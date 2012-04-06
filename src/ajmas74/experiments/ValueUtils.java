package ajmas74.experiments;

import java.util.*;

/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ValueUtils {

	private static final long DAY_IN_MILLIS = 86400000;
	
	private static final String[] MONTHS = new String[] {
		"JAN","FEB","MAR","APR","MAY","JUN",
    "JUL","AUG","SEP","OCT","NOV","DEC"
	};
	
	public static String doubleToString ( double value, String separator ) {
	  int wn = (int) value;
	  int fr = (int) ((value - wn) * 100);
	  
	  return wn+separator+fr;
	}
	
  public static String toAmadeusDate ( Date date ) {
  	Calendar cal = Calendar.getInstance();
  	cal.setTime(date);
  	
  	int dayInMonth  = cal.get(Calendar.DAY_OF_MONTH);
    int month = cal.get(Calendar.MONTH);
    int year  = cal.get(Calendar.YEAR);
    
    String dateStr = dayInMonth + "";
    if ( dayInMonth < 10 ) {
      dateStr = "0" + dateStr;
    }
        
    //int yr = year - ((year/100) * 100);

    return dateStr+MONTHS[month];//+yr;
  }


  /** 
   * how many day between the two Calendar dates. If the start date is more
   * recent than the end date, then the result will  be negative, so:
   *  <ul>
   *  <li> +1 = ( start: 2002-12-17, end 2002-12-18)
   *  <li>  0 = ( start: 2002-12-18, end 2002-12-18)
   *  <li> -1 = ( start: 2002-12-19, end 2002-12-18)
   *  </ul>
   */
	public static int daysBetween( Calendar start, Calendar end ) {
	  long diff
	    = (( end.getTime().getTime() / DAY_IN_MILLIS) * DAY_IN_MILLIS)
	    - (( start.getTime().getTime()/ DAY_IN_MILLIS) * DAY_IN_MILLIS);
	  int days = (int) (diff / DAY_IN_MILLIS);
	  return days; 
	}
		
  public static int daysBetween2( Calendar start, Calendar end ) {
    start.set(Calendar.HOUR,0);
    start.set(Calendar.MINUTE,0);
    start.set(Calendar.SECOND,0);

    end.set(Calendar.HOUR,0);
    end.set(Calendar.MINUTE,0);
    end.set(Calendar.SECOND,0);
        
    long diff
      = end.getTime().getTime()
      - start.getTime().getTime();
    int days = (int) (diff / 86400000); //86400000 = day in milliseconds
    return days;
  }
  		
	public static void main ( String[] args ) {		
		System.out.println(doubleToString(30132.23f,""));	  
	  System.out.println(toAmadeusDate(new Date()));
	  
	  Calendar end = Calendar.getInstance();
	  end.set(2002,12,19,1,00);
	  Calendar start = Calendar.getInstance();	  
	  System.out.println(start.getClass());
	  
	  start.set(2002,12,18,23,0);
	  System.out.println( "A- " + daysBetween(start,end) );
	  start.set(2002,12,18,1,0);	  
	  System.out.println( "A- " + daysBetween(start,end) );	  
	  
	  start.set(2002,12,18,23,0);
	  System.out.println( "B- " + daysBetween2(start,end) );
	  start.set(2002,12,18,1,0);
	  System.out.println( "B- " + daysBetween2(start,end) );
	  	  
	  start.set(2002,12,19,1,5);
	  System.out.println( daysBetween(start,end) );	  
	  
	  start.set(2002,11,19,23,0);
	  System.out.println( daysBetween(start,end) );
	  
	  start.set(2002,12,25);
	  System.out.println( daysBetween(start,end) );
	  	  
	}
}
