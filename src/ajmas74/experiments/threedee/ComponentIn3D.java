package ajmas74.experiments.threedee;

import javax.vecmath.*;
import javax.media.j3d.*;
import java.awt.*;
import javax.swing.*;
import com.sun.j3d.utils.universe.*;

/**
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class ComponentIn3D extends Panel {

  SimpleUniverse _simpleU;
  
	/**
	 * Constructor for ComponentIn3D.
	 */
	public ComponentIn3D(  ) {
    setBounds(0,0,500,400);
		setLayout( new BorderLayout() );
    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    
    _simpleU = new SimpleUniverse(canvas3D);
    
	}

  public void displayComponent( Component component ) {
    J3DGraphics2D g2 = _simpleU.getCanvas().getGraphics2D();    
    component.paint(new MyJ3DGraphics2D(g2));
    
    _simpleU.getViewingPlatform().setNominalViewingTransform();  
    repaint();  
  }
  
  public static void main ( String[] args ) {
    ComponentIn3D cin3d = new ComponentIn3D();
    JFrame myFrame = new JFrame();
    myFrame.setBounds(50,50,600,500);
    myFrame.getContentPane().setLayout(new BorderLayout());
    myFrame.getContentPane().add(cin3d);
    myFrame.setVisible(true); 
    
    //JComponent comp = new Label("This is a JLabel");
    JComponent comp = new JPanel();
    comp.setBackground(Color.yellow);
    comp.setOpaque(true);
    comp.setBounds(20,20,500,400);
    cin3d.displayComponent(comp);
    
    
  }
  
}
