package ajmas74.experiments.graphics2d;

import java.awt.Color;
import java.util.Arrays;

public class Rgb2Hsl {

	static double max ( double... n) {
		double max = Double.MIN_VALUE;
		for ( int i=0; i<n.length; i++ ) {
			if ( n[i] > max ) {
				max = n[i];
			}
		}
		return max;
	}
	
	static double min ( double... n) {
		double min = Double.MAX_VALUE;
		for ( int i=0; i<n.length; i++ ) {
			if ( n[i] < min ) {
				min = n[i];
			}
		}
		return min;
	}
	
	
	public static double[] convertRgbToHsl ( int r, int g, int b ) {
		double[] rgb = new double[3];
		double[] hsl = new double[3];
		
		// INFO Convert the RGB values to the range 0-1
		
		rgb[0] = r/255.0;
		rgb[1] = g/255.0;
		rgb[2] = b/255.0;
		
		// INFO Find min and max values of R, B, G
		
		double maxColor = max ( rgb );	
		double minColor = min ( rgb );
	    double delta = maxColor - minColor;
		
		hsl[2] = (maxColor + minColor)/2.0;
		
		if ( maxColor == minColor  ) {
			hsl[0] = 0;
			hsl[1] = 0;
		}
		else {
			if ( hsl[2] < 0.5 ) {
				hsl[1] = delta/(maxColor+minColor);
			}
			else {
				hsl[1] = delta/(2.0-maxColor-minColor);
			}
			
			if ( rgb[0] == maxColor ) {
				hsl[0] = (rgb[1] - rgb[2])/delta;
			}
			else if ( rgb[1] == maxColor ) {
				hsl[0] = 2.0 + (rgb[2] - rgb[0])/delta;
			}
			else if ( rgb[2] == maxColor ) {
				hsl[0] = 4.0 + (rgb[0] - rgb[1])/delta;
			}
		}
		

		
		hsl[0] /= 6;

		return hsl;
	}
	
	public static double[] convertRgbToHsv ( int r, int g, int b ) {
		double[] rgb = new double[3];
		double[] hsv = new double[3];
		
		// INFO Convert the RGB values to the range 0-1
		
		rgb[0] = r/255.0;
		rgb[1] = g/255.0;
		rgb[2] = b/255.0;
		
		// INFO Find min and max values of R, B, G
		
		double maxColor = max ( rgb );	
		double minColor = min ( rgb );
	    double delta = maxColor - minColor;
		
		hsv[2] = maxColor;
		
		if ( maxColor == 0 ) {
			hsv[1] = 0;
		}
		else {
			hsv[1] = delta/maxColor;
		}
		
		if ( maxColor == minColor  ) {
			hsv[0] = 0;
		}
		else {
			if ( hsv[2] < 0.5 ) {
				hsv[1] = delta/(maxColor+minColor);
			}
			else {
				hsv[1] = delta/(2.0-maxColor-minColor);
			}		
		
			if ( rgb[0] == maxColor ) {
				hsv[0] = (rgb[1] - rgb[2])/delta;
			}
			else if ( rgb[1] == maxColor ) {
				hsv[0] = 2.0 + (rgb[2] - rgb[0])/delta;
			}
			else if ( rgb[2] == maxColor ) {
				hsv[0] = 4.0 + (rgb[0] - rgb[1])/delta;
			}
		}
		
		hsv[0] /= 6;

		return hsv;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Color.CYAN);
		Color c = Color.BLUE;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		System.out.println(Arrays.toString(convertRgbToHsl(r,g,b)));
		System.out.println(Arrays.toString(convertRgbToHsv(r,g,b)));
		System.out.println(Arrays.toString(Color.RGBtoHSB(r,g,b, null)));
	}
	
	// ref: http://serennu.com/colour/rgbtohsl.php
	// ref: http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript

}

