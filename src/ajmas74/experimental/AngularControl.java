
package ajmas74.experimental;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

/**
 * @author andrmas
 */
public class AngularControl {
	
	public final static int LINES = 200;
  public final static int BASE_COLUMNS = 4;
  public final static float COL_INCREASE = 0.20f; // 1.0 = 100 percent
  
  
  int[]   pointsPerLine = new int[200];
  int[][] topSurfaceControlPoints = new int[200][1000];
  MyComponent mc;
  
	//Color[][] topSurfaceControlPoints;
	//List _topSurfaceControlPoints;
	
	
  AngularControl () {
    
    int n = topSurfaceControlPoints.length;
    for ( int i=0; i<topSurfaceControlPoints.length; i++ ) {
      int x = (int) (1000 * (((float)i)/n));
      pointsPerLine[i] = x;
      for ( int j=0; j<x; j++ ) {
        topSurfaceControlPoints[i][j]=1;
      }
    }
    mc = new MyComponent();
  }
  
//  AngularControl () {
//    _topSurfaceControlPoints = new Vector();
//    int points = BASE_COLUMNS;
//    for ( int i=0; i<LINES; i++ ) {
//      Vector horizontalControlPoints = new Vector();
//      _topSurfaceControlPoints.add(horizontalControlPoints);
//      
//      
//      for ( int j=0; j<points; j++ ) {
//        horizontalControlPoints.add( new Color(200,200,200));  
//      }
//      
//      points = (int) (points + (points * COL_INCREASE));
//    }
//    System.out.println("xxxx");
//  }
  
  
  class MyComponent extends JComponent {
    List _controlPoints;
    
//    MyComponent ( List controlPoints ) {
//      _controlPoints = controlPoints;
//    }
    
    MyComponent () {      
    }
    
    public void paint( Graphics g ) {
      g.setColor(Color.red);
      //g.fillRect(50,50,200,200);
      int centerX = 150;
      int centerY = 150;
      int maxRadius = 100;
      for ( int i =0; i<topSurfaceControlPoints.length; i++ ) {
        float cps = topSurfaceControlPoints.length;
        float r = maxRadius * (i/cps);
        float vs = pointsPerLine[i]; 
        System.out.println("vs="+pointsPerLine[i]);
        for ( int j =0; j<pointsPerLine[i];j++ ) {
          Color c = Color.black;
          g.setColor(c);          
          float angle = j * (360 / vs);
          int x = (int)( r * Math.cos(angle)) + centerX;
          int y = (int)( r *  Math.sin(angle)) + centerY;
          g.drawLine(x,y,x,y);
        }
      }
    }
  }
  
  static class MyComponentX extends JComponent {
    List _controlPoints;
    
    MyComponentX ( List controlPoints ) {
      _controlPoints = controlPoints;
    }
    
    public void paint( Graphics g ) {
      g.setColor(Color.red);
      //g.fillRect(50,50,200,200);
      int centerX = 150;
      int centerY = 150;
      int maxRadius = 100;
      
      //for ( int i =0; i<360; i++ ) {
      //for(float theta=0;theta < (2*Math.PI);theta+=.01) {
      for ( int i =0; i<360; i++ ) {
        Color c = Color.blue;
        float theta = i; 
        //int angle = i/1000;
        int x = (int)( maxRadius * Math.cos(theta)) + centerX;
        int y = (int)( maxRadius *  Math.sin(theta)) + centerY;
        g.drawLine(x,y,x,y);
        
      }
    }
  }
    
//  static class ControlPoint {
//    Color
//  }
	public static void main ( String[] args ) {
    AngularControl ac = new AngularControl();
    //MyComponent mc = new MyComponent(ac._topSurfaceControlPoints);
    //MyComponentX mc = new MyComponentX(ac._topSurfaceControlPoints);
	  JFrame jf = new JFrame();
	  jf.setBounds(100,100,300,300);
	  jf.getContentPane().add(ac.mc);
	  jf.setVisible(true);
	}
}
