package ajmas74.experimental;

import java.util.List;

import ajmas74.experimental.TimeSlider.Marker;

public class HtmlTimeSlider {

	public static String generateHtmlTimeSlide ( int pageWidth, int dayWidth, long date, String calendarType ) {
		StringBuilder strBuilder = new StringBuilder();
		
		List<Marker> markers = (new TimeSlider()).calculatePositions(dayWidth, pageWidth, date, calendarType);
		
		strBuilder.append("<div style=\"width: " + pageWidth + "\">\n");
		int prevOffset = 0;
		Marker marker = null;
		for ( int i=0; i<markers.size(); i++ ) {
			marker = markers.get(i);
			int offset = marker.getOffset();
			
			if ( marker.getType() == Marker.NEW_YEAR ) {
				strBuilder.append("  <div style=\"left: " + prevOffset + "; width: " + (offset-prevOffset) + "\">\n");
				strBuilder.append("  </div>\n");
				prevOffset = offset;
			}
		}
		
		strBuilder.append("</div>\n");
		
		return strBuilder.toString();
	}
	
	public static void main ( String[] args ) {
		System.out.println(generateHtmlTimeSlide(800,1,System.currentTimeMillis(),"@calendar=gregorian"));
	}
}
