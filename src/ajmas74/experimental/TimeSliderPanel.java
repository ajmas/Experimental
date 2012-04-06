package ajmas74.experimental;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ajmas74.experimental.TimeSlider.Marker;

public class TimeSliderPanel extends JPanel {

	/** */
	private static final long serialVersionUID = 1L;
	
	TimeSlider timeSlider;
	String calendarType;
	float dayWidth = 1;
	
	TimeSliderPanel ( String calendarType, float dayWidth ) {
		this.timeSlider = new TimeSlider();
		this.calendarType = calendarType;
		this.dayWidth = dayWidth;
		
		setForeground( Color.ORANGE );
	}
	
	public void paint(Graphics g) {
		
		Calendar calX = Calendar.getInstance();
		calX.add(Calendar.MONTH, -2);
		long baseDate = calX.getTimeInMillis();
//		long baseDate = System.currentTimeMillis();
//		int dayWidth = 1;
		
		List<Marker> markers = timeSlider.calculatePositions(dayWidth, getWidth(), baseDate, calendarType);

		int y = 5;
//		g.drawLine(0, y, getWidth(), y);
		
		int prevOffset = -20;
		Marker marker = null;
		for ( int i=0; i<markers.size(); i++ ) {
			marker = markers.get(i);
			int offset = marker.getOffset();
			
//			g.setColor( getForeground() );
			if ( marker.getType() == Marker.NEW_YEAR ) {
				g.setColor(getForeground() );
				g.fillRoundRect(prevOffset, 5, offset-prevOffset, 15,7,7);
				g.setColor(Color.BLACK);
				g.drawRoundRect(prevOffset, 5, offset-prevOffset, 15,7,7);
				
//				g.setColor(Color.CYAN);
//				g.fillRect(prevOffset, 5, offset-prevOffset, 10);
//				g.setColor(Color.BLACK);
//				g.drawRect(prevOffset, 5, offset-prevOffset, 10);
				
				int year = marker.getYear()-1;
				drawYear(g, year, new Rectangle2D.Float(prevOffset, 5, offset-prevOffset, 10) );
				prevOffset = offset;
			}
		}
		
		if (prevOffset!=getWidth()) {
			g.setColor(getForeground());
			g.fillRoundRect(prevOffset, 5, getWidth()-prevOffset + 20, 15,7,7);
			g.setColor(Color.BLACK);
			g.drawRoundRect(prevOffset, 5, getWidth()-prevOffset + 20, 15, 7,7);
			int year = marker.getYear();
//			drawYear(g, year, prevOffset+ ((getWidth()-prevOffset)/2), 15 );
			
			drawYear(g, year, new Rectangle2D.Float(prevOffset, 5, getWidth()-prevOffset + 20, 10) );
		}
		
		prevOffset = -1;
		marker = null;
		for ( int i=0; i<markers.size(); i++ ) {
			marker = markers.get(i);
			int offset = marker.getOffset();
			
			g.setColor( Color.BLACK );
			if ( marker.getType() == Marker.NEW_MONTH || marker.getType() == Marker.NEW_YEAR) {
				g.drawRect(prevOffset, 20, offset-prevOffset, 10);
				prevOffset = offset;
			}
		}
		
		if (prevOffset!=getWidth()) {
			g.drawRect(prevOffset, 20, getWidth()-prevOffset, 10);
		}	
		
		for ( int i=0; i<markers.size(); i++ ) {
			marker = markers.get(i);
			int offset = marker.getOffset();
			if ( marker.getType() == Marker.CURRENT_DAY ) {
				g.setColor( Color.RED );
				g.drawLine(offset, 0, offset, getHeight());
			}
		}
	}
	
//	@Override
	public void paint3(Graphics g) {
		
		Calendar calX = Calendar.getInstance();
		calX.add(Calendar.MONTH, -2);
		long baseDate = calX.getTimeInMillis();//System.currentTimeMillis();
//		int dayWidth = 1;
		
		List<Marker> markers = timeSlider.calculatePositions(dayWidth, getWidth(), baseDate, calendarType);

		int y = 5;
		g.drawLine(0, y, getWidth(), y);
		
		for ( int i=0; i<markers.size(); i++ ) {
			Marker marker = markers.get(i);
			int offset = marker.getOffset();
			
			g.setColor( Color.BLACK );
			if ( marker.getType() == Marker.NEW_YEAR ) {
				g.drawLine(offset, y, offset, 20);
				drawYear(g,marker.getYear(),offset,30);
			}
			else if ( marker.getType() == Marker.NEW_MONTH ) {
				g.drawLine(offset, y, offset, 10);
			}
			if ( marker.getType() == Marker.CURRENT_DAY ) {
				g.setColor( Color.RED );
				g.drawLine(offset, 0, offset, getHeight());
			}
		}
		

//		Marker newYearMarker1 = null;
//		for ( int i=0; i<markers.size(); i++ ) {
//			Marker marker = markers.get(i);
//			if ( marker.getType() == Marker.NEW_YEAR ) {
//				if ( newYearMarker1 == null ) {
//					newYearMarker1 = marker;
//				} else {
//					String str = newYearMarker1.getYear()+"";
//					int centerPoint = (marker.getOffset() - newYearMarker1.getOffset())/2;
//					
//					Rectangle2D bounds = g.getFontMetrics().getStringBounds(str, g);
//					
//					int x = (int)(centerPoint - (bounds.getWidth()/2));
//					g.drawString(str, newYearMarker1.getOffset() + x, 25);
//				}					
//			}
//		}
	}

	private void drawYear ( Graphics g, int year, int xOffset, int yOffset ) {
//		String str = marker.getYear()+"";
//		int centerPoint = marker.getOffset();
		String str = year + "";
		int centerPoint = xOffset;
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(str, g);
		
		int x = (int)(centerPoint - (bounds.getWidth()/2));
		g.drawString(str, x, yOffset);		
	}
	
	private void drawYear ( Graphics g, int year, Rectangle2D rect ) {
//		String str = marker.getYear()+"";
//		int centerPoint = marker.getOffset();
		String str = year + "";
//		int centerPoint = xOffset;
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(str, g);
		
		if ( bounds.getWidth() > rect.getWidth() ) {
			return;
		}
		int x = (int)( rect.getCenterX() - bounds.getCenterX()) ;
		int y = (int)( rect.getCenterY() - bounds.getCenterY()) ;
		g.drawString(str, x, (int) (y + rect.getY()) );		
	}
	
	
	public static void main ( String[] args ) {
		int x = 100;
		int width = 1000;
		float dayWidth = 1.5f;
		
		JFrame f = new JFrame("Gregorian");
		f.setBounds(x,200,width,60);
		f.add( new TimeSliderPanel(null,dayWidth) );
		f.setVisible(true);
		
		JFrame f2 = new JFrame("Hebrew");
		f2.setBounds(x,260,width,60);
		f2.add( new TimeSliderPanel("@calendar=hebrew",dayWidth) );
		f2.setVisible(true);	
		
		JFrame f3 = new JFrame("Islamic");
		f3.setBounds(x,320,width,60);
		f3.add( new TimeSliderPanel("@calendar=islamic",dayWidth) );
		f3.setVisible(true);	
		
		JFrame f4 = new JFrame("Japanese");
		f4.setBounds(x,380,width,60);
		f4.add( new TimeSliderPanel("@calendar=japanese",dayWidth) );
		f4.setVisible(true);	
		
		JFrame f5 = new JFrame("Buddhist");
		f5.setBounds(x,440,width,60);
		f5.add( new TimeSliderPanel("@calendar=buddhist",dayWidth) );
		f5.setVisible(true);
		
		JFrame f6 = new JFrame("Coptic");
		f6.setBounds(x,500,width,60);
		f6.add( new TimeSliderPanel("@calendar=coptic",dayWidth) );
		f6.setVisible(true);	
		
		JFrame f7 = new JFrame("Ethiopic");
		f7.setBounds(x,560,width,60);
		f7.add( new TimeSliderPanel("@calendar=ethiopic",dayWidth) );
		f7.setVisible(true);		
	}
}
