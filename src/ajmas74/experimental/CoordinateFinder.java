package ajmas74.experimental;

import java.awt.Frame;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
/**
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class CoordinateFinder {

  Frame _theFrame;
  MyComponent _displayArea;
  Point[] _points;
  java.util.List _hilitePoints;
	/**
	 * Constructor for CoordinateFinder.
	 */
	public CoordinateFinder() {
		super();
    _hilitePoints = new Vector();
    generatePoints();
    findPointsAroundPoint(new Point(300,300), 100);
    createDisplay();
	}

  void createDisplay () {
    _displayArea = new MyComponent();
    _theFrame = new Frame();
    _theFrame.setLayout( new BorderLayout() );
    _theFrame.add(_displayArea);
    _theFrame.setBounds(100,100,700,500);
    _theFrame.setVisible(true);
  }
  
  void generatePoints () {
    _points = new Point[10000];
    for ( int i=0; i<_points.length; i++ ) {
      _points[i] = new Point();
      _points[i].x = (int) ( Math.random() * 1000 );
      _points[i].y = (int) ( Math.random() * 1000 );
    }
  }
  
  void findPointsAroundPoint( Point center, int circumference ) {
    //v1 - using a square
/*    Point topLeft = new Point ( center.x - circumference,
                                center.y - circumference);
    Point bottomRight = new Point ( center.x + circumference,
                                center.y + circumference);  
                                                              
    for ( int i=0; i<_points.length; i++ ) {
      Point p = _points[i];
      if ( p.x > topLeft.x && p.y > topLeft.y
        && p.x < bottomRight.x && p.y < bottomRight.y ) {
        _hilitePoints.add(p);
       // System.out.println(p);
      }
    }
*/    
    // v2 - using arc
    
    Point topLeft = new Point ( center.x - circumference,
                                center.y - circumference);
    Point bottomRight = new Point ( center.x + circumference,
                                center.y + circumference);  
                                    
    Arc2D theArc = new Arc2D.Float(topLeft.x,topLeft.y,
      bottomRight.x - topLeft.x, bottomRight.y - topLeft.y,
      0,360,Arc2D.CHORD);
      
    for ( int i=0; i<_points.length; i++ ) {
      Point p = _points[i];
      if ( theArc.contains(p) ) {
        _hilitePoints.add(p);
       // System.out.println(p);
      }
    }     
  }
  
  class MyComponent extends Component {
    
    public MyComponent () {
      setBackground(Color.black);
    }
      
    public void paint ( Graphics g ) {
      super.paint(g);
      g.setColor(Color.black);
      Rectangle r = g.getClipBounds();

      g.fillRect(r.x,r.y,r.width,r.height);
      g.setColor(Color.yellow);
      for ( int i=0; i<_points.length; i++ ) {
        g.drawLine(
          _points[i].x,
          _points[i].y,
          _points[i].x,
          _points[i].y
          );          
      }  
      
      g.setColor(Color.red);
      for ( int i=0; i<_hilitePoints.size(); i++ ) {
        Point p = (Point) _hilitePoints.get(i);
        g.drawLine( p.x, p.y, p.x, p.y );          
      }          
    }
  }
  
  public static void main ( String[] args ) {
    CoordinateFinder cf = new CoordinateFinder();
  }
}
