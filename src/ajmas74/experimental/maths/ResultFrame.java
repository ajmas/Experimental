package ajmas74.experimental.maths;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ResultFrame extends JPanel {

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        
        for ( int i=0; i<=360; i+=10 ) {
            //int n = 360-i;
            Point2D p1 = new Point2D.Double(200,200);
            Point2D p2 = MathsUtils.findPointOnCircle(p1,150,Math.toRadians(i*1f));
            p2.setLocation(Math.round(p2.getX()), Math.round(p2.getY()));
            System.out.println ( "angle:" + i + ", slope: " + MathsUtils.findSlope(p1,p2) + "  (" + p1 + "," + p2 +")");
            
            float brightness = 1.0f;
            if ( i == 90 ) {
                brightness = 0.0f;
            }
            g2.setColor( Color.getHSBColor(i/360f, 0.5f, brightness));
            
            g2.drawLine((int)p1.getX(), (int)p1.getY(),(int) p2.getX(),(int) p2.getY());
            
        }
    }

    
    public static void main ( String[] args ) {
        
        Frame jf = new Frame();
        jf.setBounds(100,100,500, 500);
        jf.add( new ResultFrame() );
        jf.setVisible(true);
    }
    
}
