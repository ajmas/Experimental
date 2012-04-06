package ajmas74.experimental;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CrescentDrawer extends JPanel {

    public void paint ( Graphics g ) {
        g.fillArc(20, 20, 200, 200, -90, 180);
//        g.setColor( getBackground() );
        g.setColor( Color.red );
        g.fillArc(20, 20, 200, 200, -90, 70);
    }
    
    public static void main ( String[] args ) {
        JFrame f = new JFrame();
        f.add(new CrescentDrawer());
        f.setBounds(100, 100, 300, 300);
        f.setVisible(true);
    }
}
