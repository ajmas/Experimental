package ajmas74.experimental.geo;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LandmarkDirectory extends JPanel {

    /** Base search radius in meters (100 metres) */
    private static final int BASE_SEARCH_RADIUS = 152;
    /** Base search radius in meters (10 kilometres) */
    private static final int MAX_SEARCH_RADIUS = 10000;
    private static final int VIEWING_ANGLE = 45;
    
    private Connection connection;
    
    private static int[][] BOUND_ZONE_LIST = new int[][] {
        {1,315},
        {2,45},
        {3,135},
        {4,225},
        {5,270},
        {6,0},
        {7,90},
        {8,180},
        {6,360},
    };
    
    // id, x, y
    int [][] landmarks = new int[][] {
            {0,300,300},
            {1,300,310},
            {2,300,320},
            {3,400,300},
            {4,225,225},
    };
    
    LandmarkDirectory() 
    {
        dbConnect();
    }
    
    Arc2D arc = null;
    
    public List indentifyLandmark ( ObservationInfo observationInfo )
    {       


        Point2D coord = observationInfo.getLocation();
        
        float angle = observationInfo.getHorizontalOrientation();
       // System.out.println("a:"+angle);
        if ( angle >= 360 )
        {
            
            angle = angle % 360;
            
        }
       // System.out.println(" c:"+angle);
        // TODO reduce list based on viewing angle
        arc = new Arc2D.Double(
                new Rectangle2D.Double(coord.getX()-200,coord.getY()-200,400,400),
                (angle-90)-(VIEWING_ANGLE/2.0),
                VIEWING_ANGLE/2.0,
                Arc2D.PIE);
        
        Rectangle2D searchBounds = getSeachBounds(observationInfo);
        List l = listLandmarks(searchBounds);
        
         for ( int i=0; i<l.size();i++ )
         {
            if ( ! arc.contains( ((Landmark)l.get(i)).location)  )
            {
                l.remove(i);
                i--;
            }
         }
         
        return l;
    }

    
    private Rectangle2D getSeachBounds ( ObservationInfo observationInfo )
    {
        Rectangle2D boundsRect = null;
        Point2D coord = observationInfo.getLocation();
        
        int n = BASE_SEARCH_RADIUS;
        double halfN = n / 2.0d;
        
        switch ( selectBoundBox(observationInfo) )
        {
            case 1:
                boundsRect = new Rectangle2D.Double( coord.getX() - n, coord.getY(), n, n );
                break;
            case 2:
                boundsRect = new Rectangle2D.Double( coord.getX(), coord.getY(), n, n );
                break;
            case 3:
                boundsRect = new Rectangle2D.Double( coord.getX(), coord.getY() -n, n, n );
                break;   
            case 4:
                boundsRect = new Rectangle2D.Double( coord.getX() -n, coord.getY() -n, n, n );
                break;   
                
            case 5:
                boundsRect = new Rectangle2D.Double( coord.getX() - n, coord.getY() - halfN , n, n );
                break;
            case 6:
                boundsRect = new Rectangle2D.Double( coord.getX() -halfN, coord.getY(), n, n );
                break;
            case 7:
                boundsRect = new Rectangle2D.Double( coord.getX(), coord.getY() - halfN, n, n );
                break;   
            case 8:
                boundsRect = new Rectangle2D.Double( coord.getX() - halfN, coord.getY() - n, n, n );
                break;                   
        }
        
//        float angle = observationInfo.getHorizontalOrientation();
//        System.out.println("a:"+angle);
//        if ( angle >= 360 )
//        {
//            
//            angle = angle % 360;
//            System.out.println("b:"+angle);
//        }
//        arc = new Arc2D.Double(
//                new Rectangle2D.Double(coord.getX()-200,coord.getY()-200,400,400),
//                (angle-90)-(VIEWING_ANGLE/2.0),
//                VIEWING_ANGLE/2.0,
//                Arc2D.PIE);
        return boundsRect;
    }

    int idx = 0;
    private int selectBoundBox (  ObservationInfo observationInfo )
    {
        float angle = observationInfo.getHorizontalOrientation();
       // System.out.println("a:"+angle);
        if ( angle >= 360 )
        {
            
            angle = angle % 360;
           // System.out.println("b:"+angle);
        }
        for ( int i=0; i<BOUND_ZONE_LIST.length; i++)
        {
            if ( BOUND_ZONE_LIST[i][1]-23f<angle && BOUND_ZONE_LIST[i][1]+23f>angle )
            {
                return BOUND_ZONE_LIST[i][0];
            }
        }
        return -1;
    }
    
    // -----------------------------------------------------
    //   Database Code
    // -----------------------------------------------------    
    

    
    private void dbConnect()
    {

    }
    
    private List listLandmarks ( Rectangle2D area )
    {        

        
        List l = new ArrayList();
        
        for ( int i=0; i< landmarks.length; i++ )
        {
            if ( area.contains(landmarks[i][1],landmarks[i][2]))
            {
                Landmark landmark = new Landmark();
                landmark.id = landmarks[i][0];
                landmark.name = "landmark " + landmark.id;
                landmark.location = new Point2D.Double(landmarks[i][1],landmarks[i][2]);
                l.add(landmark);
            }
        }
        return l;
    }
    
    // -----------------------------------------------------
    //   Singleton Code
    // -----------------------------------------------------
    
    private static LandmarkDirectory instance;
    
    public static LandmarkDirectory getInstance()
    {
        if ( instance == null ) 
        {
            instance = new LandmarkDirectory();
        }
        return instance;
    }
    
    
    
    public void paint ( Graphics g )
    {
        Graphics2D g2 = (Graphics2D) g;
        ObservationInfo observationInfo = new ObservationInfo();
        observationInfo.setLocation(new Point2D.Double(250,200));
        observationInfo.setHorizontalOrientation(70+idx++);
        setBackground(Color.BLACK);
        super.paint(g);
        g2.setColor(Color.YELLOW);
        g.drawOval(250-5,200-5,10,10);
        
        g2.setColor(Color.BLUE);

        g2.draw(getSeachBounds(observationInfo));
        
        for ( int i=0; i<landmarks.length; i++ ) 
        {
            g2.setColor(Color.GREEN);
            g.drawOval(landmarks[i][1]-5,landmarks[i][2]-5,10,10);
        }
        
        List l = indentifyLandmark(observationInfo);
        for ( int i=0; i<l.size(); i++ ) 
        {
            System.out.println("   --- " + l.get(i));
        }
        
        g2.setColor(Color.CYAN);
        g2.draw(arc);
   
    }
    
    public static void main ( String[] args )
    {
        LandmarkDirectory lmDir = new LandmarkDirectory();
        
        JFrame frame = new JFrame();
        frame.getContentPane().add(lmDir);
        frame.setBounds( 100,100,500,400);
        frame.setVisible(true);
    }
    
}
