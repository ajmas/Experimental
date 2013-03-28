package ajmas74.experimental.threedee;

import java.awt.*;
import java.awt.geom.*;
/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MathEquations {



	/** The four points represent a real quadrilateral, where p1, p2, p3 and
	 * p4 each represent the for corners, in order of drawing. This uses the
	 * midpoint equation on both opposite corners. If the mid points are the
	 * same then it is a parallelagram.
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return boolean
	 */
  public static boolean isParallelagram( Point p1, Point p2, Point p3, Point p4 ) {
  	// use midpoint equation:
  	Point mp1 = new Point();
  	mp1.x = (p1.x+p3.x)/2;
  	mp1.y = (p1.y+p3.y)/2;
  	
    Point mp2 = new Point();
    mp2.x = (p2.x+p4.x)/2;
    mp2.y = (p2.y+p4.y)/2;
      	
    return ( mp1.x == mp2.x && mp1.y == mp2.y ); 
  }
 	 	
  public static boolean isParallelagram( Point[] points ) {   
    return isParallelagram(points[0],points[1],points[2],points[3]);
  }
   	
	public static void main ( String[] args ) {
		System.out.println(" -> " + isParallelagram(new Point(0,5), new Point(5,5),
	  new Point(5,0), new Point(0,0)));
	}
}
