/*
 * Created on 07-Oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ajmas74.experimental;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author ajmas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalendarCreator {
    public static final int[] MONTH_LENGTHS = new int[] {
    		31, 28, 31, 30, 31, 30, 31,
		31, 30, 31, 30, 31
    };
    
    public static final String[] DAY_OF_WEEK_ABBREV = new String[] {
    		"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"
    };    
    
    /**
     * 
     * @param year
     * @param month - use Calendar.JANUARY to Calendar.DECEMBER (0 - 11)
     * @param firstDayOfWeek - use Calendar.MONDAY to Calendar.SUNDAY (1 - 7)
     * @return
     */
	public static int[][] createMonth ( int year, int month, int firstDayOfWeek ) {
		GregorianCalendar calendar = new GregorianCalendar(year,month,1);		
		calendar.setFirstDayOfWeek(firstDayOfWeek);
		
		int monthLen = MONTH_LENGTHS[month];
		if ( calendar.isLeapYear(year) && month == Calendar.FEBRUARY) {
			monthLen++;
		} 
			
		SimpleDateFormat dateFormater = new SimpleDateFormat("MMMM");
		dateFormater.format(calendar.getTime());
		int offset = calendar.get(GregorianCalendar.DAY_OF_WEEK) - firstDayOfWeek;
		int day = 1 - offset;
		int[][] monthMatrix = new int[6][7];
		for ( int i=0; i<monthMatrix.length; i++ ) {
			for ( int d=0; d<monthMatrix[i].length; d++ ) {
				if (day < 1 ) {
					monthMatrix[i][d] = 0;
				} else if ( day <= monthLen ) {
					monthMatrix[i][d] = day;
				}
				day++;
			}
			
		}

		return monthMatrix;
	}
	
	private static String[] createWeekHeadings( int firstDayOfWeek ) {
		String[] headings = new String[7];
		int idx = firstDayOfWeek;
		for ( int i=0; i<7; i++ ) {
			if ( idx == 8 ) {
				idx = 1;
			}
			headings[i] = DAY_OF_WEEK_ABBREV[(idx++)-1];  			
		}
		return headings;
	}
	
	public static void main2(String[] args) {
		DecimalFormat formater = new DecimalFormat("00");
		int firstDayOfWeek = Calendar.SUNDAY;
		int[][] month = createMonth(2004,Calendar.OCTOBER,firstDayOfWeek);
		
		SimpleDateFormat dateFormater = new SimpleDateFormat("MMMM");
		System.out.println(dateFormater.format((new GregorianCalendar(2004,Calendar.OCTOBER,1)).getTime()));
		
		String[] headings = createWeekHeadings(firstDayOfWeek);
		for ( int i=0; i<7; i++ ) {
			System.out.print(headings[i] + "  ");
		}
		System.out.println();
		System.out.println("--------------------------");
		for ( int i=0; i<month.length; i++ ) {
			for ( int d=0; d<month[i].length; d++ ) {
				if ( month[i][d] == 0 ) {
					System.out.print("    ");
				} else {
					System.out.print(formater.format(month[i][d]) + "  ");
				}
			}
			System.out.println("");
		}
	}
	
	public static void main(String[] args) {
		DecimalFormat formater = new DecimalFormat("00");
		int firstDayOfWeek = Calendar.SUNDAY;
		int[][] month = createMonth(2004,Calendar.OCTOBER,firstDayOfWeek);
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<table class=\"calborder\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\" width=\"170\">");
		strBuf.append("<tr><td colspan=\"7\" align=\"center\" class=\"sideback\">");
		strBuf.append("<div style=\"height: 16px;\"><b>");
		SimpleDateFormat dateFormater = new SimpleDateFormat("MMMM");
		strBuf.append(dateFormater.format((new GregorianCalendar(2004,Calendar.OCTOBER,1)).getTime()));
		strBuf.append("</b></div>");
		strBuf.append("</td></tr>");
		
		strBuf.append("<tr><td align=\"center\"><table>");
		strBuf.append("<tr>");
		String[] headings = createWeekHeadings(firstDayOfWeek);
		for ( int i=0; i<7; i++ ) {
			strBuf.append("<td><b>");
			strBuf.append(headings[i]);
			strBuf.append("</b></td>");
		}
		strBuf.append("</tr>");
		
		for ( int i=0; i<month.length; i++ ) {
			strBuf.append("<tr>");
			for ( int d=0; d<month[i].length; d++ ) {
				strBuf.append("<td>");
				if ( month[i][d] == 0 ) {
					strBuf.append("&nbsp;");
				} else {
					if ( month[i][d] == (new Date()).getDate() ) {
						strBuf.append("<a class=\"ps2\" href=\"\">"+month[i][d]+"</a>");
					} else {
						strBuf.append("<a class=\"psf\" href=\"\">"+month[i][d]+"</a>");
					}
				}
				strBuf.append("</td>");
			}
			strBuf.append("</tr>");
			System.out.println("");
		}
		strBuf.append("</table></td></tr>");
		strBuf.append("</table>");
		System.out.println(strBuf);
	}
	
}
