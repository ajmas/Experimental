package ajmas74.experimental.graphics2d;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.*;

import java.util.List;
import java.util.*;

public class PerpendicularLine extends Component {

  public static final int POINT_AT_CENTER = 0;
  public static final int POINT_AT_START  = 1;
  public static final int POINT_AT_END    = 2;  
  
  Point p1;
  Point p2;
  Line2D line;
  List  lines;
  
  PerpendicularLine ( Point p1, Point p2 ) {
    this.p1 = p1;
    this.p2 = p2;
    line = createPerpendicularLine(new Line2D.Double(p1,p2),200);    
  }
  
  PerpendicularLine ( List lines ) {
    this.lines = lines;
  }
  
  static final double HALF_PI = Math.PI/2;
  
  public static Line2D createSemiPerpendicularLine (Line2D l1, Line2D l2, int newLineLength ) {
    double angleL1 = lineAngle(l1);
    double angleL2 = lineAngle(l2);
    double angle = (angleL1+angleL2)/2;        
    return createAngledLine(l1.getP2(),angle-HALF_PI,newLineLength,POINT_AT_CENTER);
  }
  
  public static Line2D createPerpendicularLine2 (Line2D l1, int newLineLength ) {
    double angle = lineAngle(l1);    
    return createAngledLine(l1.getP2(),angle-HALF_PI,newLineLength,POINT_AT_CENTER);
  }
  
  
  /**
   * Creates a perpendicular line, relative the to the line passed as
   * parameter. The center of the new line is placed at point p2. This
   * should work for all lines.
   *
   * @param p1 first point in line
   * @param p2 second point in line
   * @param lineLength length of new line
   * @return new perpendicular line
   */
  //public static Line2D createPerpendicularLine (Point2D p1, Point2D p2, int newLineLength ) {
  public static Line2D createPerpendicularLine (Line2D line, int newLineLength ) {
    Point2D p3 = null;
    Point2D p4 = null;
    double x = line.getX2() - line.getX1();
    double y = line.getY2() - line.getY1();
    Point2D p2 = line.getP2();
    // radius of new line
    double radius = newLineLength / 2.0;
    
    if ( x == 0 ) { // we have a vertical line        
      x = p2.getX() - radius;
      y = p2.getY();  
      p3 = new Point2D.Double(x,y);
      x = p2.getX() + radius;
      y = p2.getY();       
      p4 = new Point2D.Double(x,y);
    } else if ( y == 0 ) { // we have a horizontal line
      x = p2.getX();
      y = p2.getY() - radius;   
      p3 = new Point2D.Double(x,y);
      x = p2.getX();
      y = p2.getY() + radius;  
      p4 = new Point2D.Double(x,y);
    } else { // this line is at an angle                  
      double angleB = HALF_PI - Math.atan(x/y);
      if ( y < 0 ) { // adjust for quadrant
        angleB = angleB + Math.PI;
      }      
      x = p2.getX() + radius * Math.cos(angleB+HALF_PI);
      y = p2.getY() + radius * Math.sin(angleB+HALF_PI);
      p3 = new Point2D.Double(x,y);
      x = p2.getX() + radius * Math.cos(angleB-HALF_PI);
      y = p2.getY() + radius * Math.sin(angleB-HALF_PI); 
      p4 = new Point2D.Double(x,y);      
    }
    return new Line2D.Double(p3,p4);
  }
  
  /**
   * 
   * @param point
   * @param angle
   * @param length
   * @return
   */
  public static Line2D createAngledLine ( Point2D point, double angle, int length, int pointPos) {
    //angle = Math.toRadians(angle);
    Point2D p1 = null;
    Point2D p2 = null;
    double x = 0.0;
    double y = 0.0;
    switch ( pointPos ) {
      case POINT_AT_CENTER: {
        double radius = length/2.0;
        x = point.getX() + radius * Math.cos(angle-Math.PI);
        y = point.getY() + radius * Math.sin(angle-Math.PI);  
        p1 = new Point2D.Double(x,y);
        x = point.getX() + radius * Math.cos(angle);
        y = point.getY() + radius * Math.sin(angle);  
        p2 = new Point2D.Double(x,y);  
        break;
      }
      case POINT_AT_START: {
        double radius = length;
        x = point.getX() + radius * Math.cos(angle);
        y = point.getY() + radius * Math.sin(angle);  
        p1 = new Point2D.Double(point.getX(),point.getY());
        p2 = new Point2D.Double(x,y);
        break;
      }
      case POINT_AT_END: {
        double radius = length;
        x = point.getX() + radius * Math.cos(angle-Math.PI);
        y = point.getY() + radius * Math.sin(angle-Math.PI);  
        p1 = new Point2D.Double(x,y);        
        p2 = new Point2D.Double(point.getX(),point.getY());
        break;        
      }
    }
    return new Line2D.Double(p1,p2);
  }

  /**
   * 
   * @param line
   * @return
   */
  public static double lineAngle ( Line2D line ) {
    /*
     * I finally got some paper out and realised that the answer was
     * straightforward arctan(y/x) gives a value from -90 to +90, with
     * negative values being in quadrants 2 & 4. Add 180 to the
     * negative values to provide answers in the range of 0-180.
     * But this is not enough since that would give me two halves
     * of 0-180. I also have to test if y<0 and then add 180 if
     * it is less than 0. This now gives me 0-360.
     * 
     */
    double x = line.getX2() - line.getX1();
    double y = line.getY2() - line.getY1();   

    double v = y/x;
    double angle = Math.atan(v);
    if ( v < 0)
      angle = Math.PI + angle;
    if ( y < 0 ) 
      angle = angle + Math.PI;
    return angle;
  }
  
  Shape flip ( Shape s, int boxHeight ) {
    if ( s instanceof Line2D ) {
      Line2D line = (Line2D)s;
      double x1 = line.getX1();
      double y1 = boxHeight - line.getY1();
      double x2 = line.getX2();
      double y2 = boxHeight - line.getY2();      
      return new Line2D.Double(x1,y1,x2,y2);
    } else {
      return null;
    }
  }
  int xx = 0;
  public void paint (Graphics g) {   
    Graphics2D g2 = (Graphics2D) g;
    if ( lines != null ) {
      int boxHeight = getHeight();
      for ( int i=0; i<lines.size(); i++ ) {  
        g.setColor(Color.red);
        if ( xx % 2 == 0 ) {
          g.setColor(Color.blue);
        }
        xx++;
        Line2D line = (Line2D) lines.get(i);
        line = (Line2D) flip(line,boxHeight);
        //g.setColor(Color.blue);
        g2.draw(line);
        //g.setColor(Color.red);
        if ( i < lines.size()-1 ) {
          Line2D line2 = (Line2D) lines.get(i+1);
          line2 = (Line2D) flip(line2,boxHeight);
          g2.draw(createPerpendicularLine2(line,30));
        }
        //g2.draw(createPerpendicularLine(line,20));
      }
    } else {
      g.setColor(Color.blue);
      if ( line != null ) {
        g.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());
      }
      g.setColor(Color.blue);
      g.drawLine(p1.x,p1.y,p2.x,p2.y);
    }
  }

  
  /**
   * @param args
   */
  public static void main(String[] args) {
    Frame f = new Frame();
    //PerpendicularLine pl = new PerpendicularLine(new Point(100,100),new Point(400,100));
    PerpendicularLine pl = null;
    
    ArrayList lines = new ArrayList();
    lines.add(
    createAngledLine(new Point2D.Double(100,200),Math.toRadians(45),200,POINT_AT_START)
    );
    
//    ArrayList lines = new ArrayList();
//    for ( int i=0; i<=360; i+=10 ) {
//      double angle = Math.toRadians(i*1.0);
//      System.out.println("angle("+i+"):"+ Math.toDegrees(
//          lineAngle(createAngledLine(new Point2D.Double(200,200),angle,200,POINT_AT_END))
//          ));  
//      lines.add(
//          createAngledLine(new Point2D.Double(500,500),angle,200,POINT_AT_END)
//          );
//    }
    
//    
//    ArrayList lines = new ArrayList();
//    //lines.add(createAngledLine(new Point2D.Double(200,200),90,200,POINT_AT_END));
//    //lines.add(createAngledLine(new Point2D.Double(400,400),180,200,POINT_AT_CENTER));
//    lines.add(new Line2D.Double(0,0,100,100));
//    lines.add(new Line2D.Double(100,100,200,100));
//    lines.add(new Line2D.Double(200,100,200,300));
//    
//    lines.add(new Line2D.Double(200,300,250,200));
//    
//    
//    
//    Line2D l =  new Line2D.Double(200,300,250,200);
//    Line2D l2 =  new Line2D.Double(250,200,400,400);
//    int n=1000000;
//    long t = System.currentTimeMillis();
//    for ( long i =0; i<n; i++ ) {
//      l2 = createPerpendicularLine(l,200);
//    }
//    System.out.println("A:took: " + ((System.currentTimeMillis()-t)/1000.0) );
//    System.out.println("    "+l2.getP1()+","+l2.getP2());
//    
//    t = System.currentTimeMillis();
//    for ( long i =0; i<n; i++ ) {
//      //createSemiPerpendicularLine(l,l2,200);
//      l2 = createPerpendicularLine2(l,200);
//    }
//    System.out.println("B:took: " + ((System.currentTimeMillis()-t)/1000.0) );    
//    System.out.println("    "+l2.getP1()+","+l2.getP2());
//    
//    t = System.currentTimeMillis();
//    for ( long i =0; i<n; i++ ) {
//      l2 = createPerpendicularLine(l,200);
//    }
//    System.out.println("A:took: " + ((System.currentTimeMillis()-t)/1000.0) );
//    System.out.println("    "+l2.getP1()+","+l2.getP2());
//    
//    t = System.currentTimeMillis();
//    for ( long i =0; i<n; i++ ) {
//      l2 = createPerpendicularLine(l,200);
//    }
//    System.out.println("A:took: " + ((System.currentTimeMillis()-t)/1000.0) );
//    System.out.println("    "+l2.getP1()+","+l2.getP2());
//    
//    t = System.currentTimeMillis();
//    for ( long i =0; i<n; i++ ) {
//      //createSemiPerpendicularLine(l,l2,200);
//      l2 = createPerpendicularLine2(l,200);
//    }
//    System.out.println("B:took: " + ((System.currentTimeMillis()-t)/1000.0) );    
//    System.out.println("    "+l2.getP1()+","+l2.getP2());
//    
    int angle = 1111;
    switch (angle) {
      case 0:
        pl = new PerpendicularLine(new Point(100,200),new Point(100,100));
        break;
      case 45:
        pl = new PerpendicularLine(new Point(100,100),new Point(200,200));
        break;           
      case 75:
        pl = new PerpendicularLine(new Point(100,100),new Point(500,200));
        break;
      case 90:
        pl = new PerpendicularLine(new Point(100,100),new Point(100,200));
        break;           
      case 135:
        pl = new PerpendicularLine(new Point(200,200),new Point(100,100));
        break;
      case 215: // ----
        pl = new PerpendicularLine(new Point(200,100),new Point(100,200));
        break;
      case 230: // ----
        pl = new PerpendicularLine(new Point(200,100),new Point(100,300));
        break;        
      case 275:
        pl = new PerpendicularLine(new Point(200,100),new Point(100,100));
        break;          
      case 288:
        pl = new PerpendicularLine(new Point(200,100),new Point(100,400));
        break;         
      case 1111:
        pl = new PerpendicularLine(lines);
        break;
    }
    
     
    f.add(pl);
    f.setBackground(Color.black);
    f.setBounds(50,50,400,400);
    f.setVisible(true);

 }

}
