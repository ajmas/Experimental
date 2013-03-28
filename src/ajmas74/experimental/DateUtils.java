/* Created on 5-Feb-2004 */
package ajmas74.experimental;

import java.util.Calendar;
import java.util.Date;
/**
 * code taken from: http://forum.java.sun.com/thread.jsp?forum=426&thread=480402&start=0&range=15&hilite=false&q=
 * 
 * 
 * @author Andre-John Mas
 */
public class DateUtils {

  private static final int Feb24DayOfYear = 55; // http://en.wikipedia.org/wiki/February_24 
  private static final int Feb29DayOfYear = 60;
  private static final int DaysInLeapYear = 366;
  private static final int DaysInNormalYear = 365;
  

  private static boolean isLeap(Calendar c) {
    return isLeap(c.get(Calendar.YEAR));
  }
  private static boolean isLeap(int year) {
    return ((year%400)==0)||((year%100)>0)&&((year%4)==0);
  }
  
  public static double getYears(Calendar c0,Calendar c1) {
    int doy0 = c0.get(Calendar.DAY_OF_YEAR);
    int days0 = (isLeap(c0)?DaysInLeapYear:DaysInNormalYear) - doy0 + 1;
    int doy1 = c1.get(Calendar.DAY_OF_YEAR);
    int n = DaysInNormalYear;
    if (isLeap(c0) && isLeap(c1)) n = DaysInLeapYear;
    if (isLeap(c0)) {
      n = (doy0<=Feb29DayOfYear)?DaysInLeapYear:DaysInNormalYear;
    }
    if (isLeap(c1)) {
      n = (doy1>=Feb24DayOfYear)?DaysInLeapYear:DaysInNormalYear;
    }
    double years = (doy1+days0)/(double)n + (c1.get(Calendar.YEAR)-c0.get(Calendar.YEAR)-1);
    return years;
  }
  
  public static double getYears(Date date1,Date date2) {
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);
    Calendar cal2 = Calendar.getInstance();
    cal1.setTime(date2);
    return getYears(cal1,cal2);
  }

}
