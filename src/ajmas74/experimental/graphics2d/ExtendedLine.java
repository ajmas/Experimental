package ajmas74.experimental.graphics2d;

import java.awt.*;
import java.awt.geom.Line2D;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class ExtendedLine extends JComponent {

    Point p1;
    Point p2;
    Point p3;
    
    List lines;
    
    
    ExtendedLine ( ) {      
      setBackground(Color.black);
      setOpaque(true);
    }
    
    ExtendedLine ( Point p1, Point p2 ) {
      this();
      this.p1 = p1;
      this.p2 = p2;      
    }
    
    
    ExtendedLine ( List lines ) {
      this();
      this.lines = lines;
    }
    
    /**
     *  <p>
     *  Given a line at any orientation it extends it by the specified
     *  length, at the same angle and direction.
     *  </p>
     *  <p>
     *  This methods extends a line, using the notion of a triangle.
     *  The hypotheneuse is labeled sideA, the horizontal axis sideB
     *  and the vertical axis sideC. The new length of the hypotheneuse is
     *  calculated and the ratio of the new length is used to extend the
     *  x and y values to produce the new point.
     *  </p>
     *  
     * @param p1 first point in the line
     * @param p2 second pint in the line
     * @param extraLength how much to extend the line by
     * @return
     */
    public static Point extendLine ( Point p1, Point p2, int extraLength ) {
      Point p3 = new Point();
      int sideB = p2.x - p1.x;
      int sideC = p2.y - p1.y;
      
      if ( sideB == 0 ) { // we have a vertical line        
        p3.x = p1.x;
        p3.y = p2.y + extraLength;    
      } else if ( sideC == 0 ) { // we have a horizontal line
        p3.y = p1.y;
        p3.x = p2.x + extraLength;    
      } else { // this line is at an angle
        //Pythagoras Thereom
        double sideA  = Math.sqrt(sideB*sideB+sideC*sideC);
        double sideA2 = sideA+extraLength;
        double ratio  = sideA/sideA2;
        p3.x = (int)Math.round((sideB/ratio)+p1.x);
        p3.y = (int)Math.round((sideC/ratio)+p1.y);
      }
       
      return p3;
      
    }
    
    public void extendLine ( int extraLength ) {
      p3 = extendLine(p1,p2,extraLength);
      this.repaint();
    }
    
    public void paint ( Graphics g ) {
      Graphics2D g2 = (Graphics2D) g; 
      g.setColor(Color.red);
      if ( lines != null ) {
        for ( int i=0; i<lines.size(); i++ ) {
          Line2D line = (Line2D) lines.get(i);
          g2.draw(line);
          g2.draw(line);
        }
      } else {
        if ( p3 != null ) {
          g.drawLine(p1.x,p1.y,p3.x,p3.y);
        }
        g.setColor(Color.blue);
        g.drawLine(p1.x,p1.y,p2.x,p2.y);
      }
    }
    
    public static void main ( String[] args ) {
      ExtendedLine el = new ExtendedLine(new Point(700,100),new Point(400,400));
      el.extendLine(300); 
      Frame f = new Frame();
      f.setBackground(Color.black);
      f.add(el);
      f.setBounds (50,50,400,300);
      f.setVisible(true);


    }

}