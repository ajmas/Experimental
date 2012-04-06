package ajmas74.experimental;

import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

public class TimeSlider {

	/** */
	private static final long serialVersionUID = 1L;
	
	public List<Marker> calculatePositions( float dayWidth, float containerWidth, long baseDate, String calendarType) {
		
		List<Marker> markers = new ArrayList<Marker>();
		
		int divisions = (int)(containerWidth / dayWidth);
		
		Calendar calendar = null;
		Calendar currentDayCalendar = null;
		if ( calendarType != null ) {
			calendar = Calendar.getInstance(new ULocale(calendarType));
			currentDayCalendar = Calendar.getInstance(new ULocale(calendarType));
		}
		else {
			calendar = Calendar.getInstance();
			currentDayCalendar = Calendar.getInstance();
		}

		calendar.setTimeInMillis( baseDate );
		currentDayCalendar.setTimeInMillis( System.currentTimeMillis() );
		
		calendar.add( Calendar.DATE, -1);
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		
		for ( int i=0; i < divisions; i++ ) {
			
			calendar.add( Calendar.DATE, 1);
			
			int year2 = calendar.get(Calendar.YEAR);
			int month2 = calendar.get(Calendar.MONTH);
			
			int offset = (int) (dayWidth * i);
			
			// info: see if this is a new year
			if ( year2 != year ) {
				markers.add ( new Marker (offset, Marker.NEW_YEAR, year2,month2,"") ); 
			}
			
			// info: see if this is a new month
			if ( month2 != month ) {
				markers.add ( new Marker (offset, Marker.NEW_MONTH, year2,month2,"") ); 
			}	
			
			if ( calendar.get(Calendar.YEAR) == currentDayCalendar.get(Calendar.YEAR)
				&& calendar.get(Calendar.MONTH) == currentDayCalendar.get(Calendar.MONTH)
				&& calendar.get(Calendar.DAY_OF_MONTH) == currentDayCalendar.get(Calendar.DAY_OF_MONTH)
					) {
				markers.add ( new Marker (offset, Marker.CURRENT_DAY, year2, month2, "") ); 
			}
			
			
			year = year2;
			month = month2;
		}
		
		return markers;
	}			
	
	// --------------------------------------------------------------------------------
	// Private inner classes
	// --------------------------------------------------------------------------------
	
	public static class Marker {
		
		public static final int NEW_YEAR = 0;
		public static final int NEW_MONTH = 1;
		public static final int CURRENT_DAY = 2;
		
		int offset;
		int type;
		int year;
		int month;
		String monthName;
		
		public Marker(int offset, int type, int year, int month, String monthName) {
			this.offset = offset;
			this.type = type;
			this.year = year;
			this.month = month;
			this.monthName = monthName;
		}

		public int getOffset() {
			return offset;
		}

		public int getType() {
			return type;
		}

		public int getYear() {
			return year;
		}

		public int getMonth() {
			return month;
		}

		public String getMonthName() {
			return monthName;
		}
		
	}
}
