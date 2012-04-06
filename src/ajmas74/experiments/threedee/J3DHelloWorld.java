package ajmas74.experiments.threedee;

import javax.vecmath.*;
import javax.media.j3d.*;

import java.applet.Applet;
import java.awt.*;
import javax.swing.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;

/**
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class J3DHelloWorld extends Applet {

  public J3DHelloWorld() {
    setLayout(new BorderLayout());
    GraphicsConfiguration config =
      SimpleUniverse.getPreferredConfiguration();
    
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    
    BranchGroup scene = createSceneGraph();
    
    // SimpleUniverse is a Convenience Utility class
    SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
    
    // This will move the ViewPlatform back a bit so the
    // objects in the scene can be viewed.
    simpleU.getViewingPlatform().setNominalViewingTransform();
    
    simpleU.addBranchGraph(scene);
  } // end of HelloJava3Da (constructor)

  public BranchGroup createSceneGraph() {
    // Create the root of the branch graph
    BranchGroup objRoot = new BranchGroup();
  
    //objRoot.addChild(new ColorCube(0.4));
  
    Transform3D rotate = new Transform3D();
    rotate.rotX(Math.PI/4.0d);    
    rotate.rotY(Math.PI/4.0d);    
    TransformGroup objRotate = new TransformGroup(rotate);
           
    objRotate.addChild(new ColorCube(0.4));
    objRoot.addChild(objRotate);
    
    return objRoot;
  } // end of CreateSceneGraph method of HelloJava3Da

 
    //  The following allows this to be run as an application
    //  as well as an applet

  public static void main(String[] args) {
    MainFrame frame = new MainFrame(new J3DHelloWorld(), 256, 256);
//    J3DHelloWorld frame = new J3DHelloWorld();
//    frame.setBounds(200,200,256,256);
//    frame.setVisible(true);
  } // end of main (method of HelloJava3Da)

}
