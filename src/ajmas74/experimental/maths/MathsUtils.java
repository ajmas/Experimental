package ajmas74.experimental.maths;

import java.awt.geom.Point2D;

import javax.swing.JFrame;

public class MathsUtils {

    public static Point2D findPointOnCircle ( Point2D center, double radius, double theta ) {        
        return findPointOnCircle(center.getX(), center.getY(), radius, theta);
    }
    
    /**
     * Returns a point on the circumference of a circle, given the specified
     * parameters.
     * 
     * @param x1
     * @param y1
     * @param radius
     * @param theta - angle in radians
     * @return
     */
    public static Point2D findPointOnCircle ( double x1, double y1, double radius, double theta ) {                
        double x = x1 + Math.cos(theta) * radius;
        double y = y1 + Math.sin(theta) * radius;
        
        return new Point2D.Double(x,y);
    }    
    
    public static double findSlope ( Point2D p1, Point2D p2 ) {
        return (p2.getY()-p1.getY()) / (p2.getX()-p1.getX());
    }
    
    public static double findSlope ( double x1, double y1, double x2, double y2 ) {
        return (y2-y1) / (x2-x1);
    }
    
    public static void main ( String[] args ) {
        
        
        for ( int i=0; i<=360; i+=10 ) {
            Point2D p1 = new Point2D.Double(0,0);
            Point2D p2 = findPointOnCircle(p1,60,Math.toRadians(i));
            System.out.println ( "angle:" + i + ", slope: " + findSlope(p1,p2) + "  (" + p1 + "," + p2 +")");
        }
    }
}
