package ajmas74.experimental;

import java.awt.geom.Point2D;

import ajmas74.experimental.maths.BaseConversion;

/**
 * http://en.wikipedia.org/wiki/Natural_Area_Code
 * 
 * @author Andre-John Mas, 2013-03-24
 */
public class NaturalAreaCoding {

	static final String NAC_BASE_STR = "0123456789BCDFGHJKLMNPQRSTVWXZ";
	
	/**
	 * Converts NAC (Natural Area Coding) value into lon/lat value.
	 * @param nac
	 * @return
	 */
	public static Point2D nacToLonLat (String nac) {
		String[] parts = nac.split(" ");

		double lon = nacToDouble(parts[0], 360);
		double lat = nacToDouble(parts[1], 180);
		
		return new Point2D.Double(lon,lat);
	}
	
	private static double nacToDouble(String value, int range) {
		double a = range;
		double b = 0;
		for (int i=0; i<value.length(); i++) {
			b += (a * BaseConversion.baseToBase10(NAC_BASE_STR, value.charAt(i) + "") / 30);
			a /= 30;
		}		
		return b - (range / 2);
	}	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		System.out.println(nacToLonLat("HBV6R RG77T"));
		System.out.println(nacToLonLat("8KDB PGFD"));

	}

}
