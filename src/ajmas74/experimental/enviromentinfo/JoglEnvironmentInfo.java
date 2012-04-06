package ajmas74.experimental.enviromentinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.media.opengl.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JoglEnvironmentInfo implements EnvironmentInfo, GLEventListener {

        private static final String SPEC_FILE = "glspec.xml";

        private boolean ready = false;
        private Map<String,List<String>> functionSupportMap = new HashMap<String,List<String>>();
        private Map<String,List<String>> extensionSupportMap = new HashMap<String,List<String>>();
        
        private StringBuilder strBuilder = new StringBuilder();

        public JoglEnvironmentInfo() {
                loadOpenGlSpecs();
        }

        public String getOpenGlInfo() {

                GLCanvas canvas = new GLCanvas();
                canvas.addGLEventListener(this);
                JFrame f = new JFrame();
                f.add(canvas);
                f.setBounds(50,50,200,200);
                f.setVisible(true);
                f.repaint();

                while (!ready) {
                  try {
                     Thread.sleep(100);
                  } catch ( InterruptedException ex ) {
                  }
                }

                f.setVisible(false);

                return strBuilder.toString();
        }

        private void loadOpenGlSpecs () {
                try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (
                this.getClass().getResourceAsStream(SPEC_FILE)
                );

            // normalize text representation
            doc.getDocumentElement ().normalize ();

            NodeList featureSets = doc.getElementsByTagName("feature-set");

            for(int i=0; i<featureSets.getLength() ; i++){
                Node featureSet = featureSets.item(i);
                NamedNodeMap attributes = featureSet.getAttributes();
                String category = (String) attributes.getNamedItem("category").getNodeValue();

                List<String> functionList = new ArrayList<String>();
                this.functionSupportMap.put( category, functionList);

                List<String> extensionList = new ArrayList<String>();
                this.extensionSupportMap.put( category, extensionList);
                
                NodeList children = featureSet.getChildNodes();

                for ( int j=0; j<children.getLength(); j++ ) {
                        Node featureNode = children.item(j);
                        if ( featureNode.getNodeName().equals("fn") ) {
                                //System.out.println
                                if (featureNode.getChildNodes().getLength() > 0 ) {
                                	functionList.add(featureNode.getFirstChild().getNodeValue());
                                }
                        }
                        else if ( featureNode.getNodeName().equals("ext") ) {
                            if (featureNode.getChildNodes().getLength() > 0 ) {
                            	extensionList.add(featureNode.getFirstChild().getNodeValue());
                            }                        	
                        }
                }
            }

                } catch ( Exception ex ) {
                        ex.printStackTrace();
                }

//              System.out.println(versionMap);

//              String[] versionList = versionMap.keySet().toArray( new String[0] );
//              Arrays.sort(versionList);
//
//              System.out.println("<opengl-features>");
//              for ( String version : versionList ) {
//                      String key = version;
//                      //System.out.println ( "OpenGL " + key + ": ");
//                      System.out.println ( "  <feature-set category=\"OpenGL "+key+"\">");
//
//                      List<String> fnList = versionMap.get(key);
//                      Collections.sort(fnList);
//                      for ( String fn : fnList ) {
//                              System.out.println ( "    <fn>" + fn + "</fn>");
//                      }
//                      System.out.println ( "  </feature-set>");
//              }
//              System.out.println("</opengl-features>");
//
        }



        public String getEnvironmentInfo() {
                StringBuilder strBuilder = new StringBuilder();

                boolean joglAvailable = false;
                try {
                        Thread.currentThread().getContextClassLoader().loadClass("javax.media.opengl.GL");
                        joglAvailable = true;
                } catch (ClassNotFoundException e) {
                        //
                }
                append(strBuilder,"JOGL Available", joglAvailable);

                if ( joglAvailable ) {
                        strBuilder.append( getOpenGlInfo() );
                }

                return strBuilder.toString();
        }

        private void append ( StringBuilder strBuilder, String feature, Object value ) {
                strBuilder.append(feature + ": " + value + "\n");
        }


   public void init(GLAutoDrawable drawable) {

                this.strBuilder = new StringBuilder();

                GL gl = drawable.getGL();

                append( strBuilder, "OpenGL version", gl.glGetString(GL.GL_VERSION) );
                append( strBuilder, "OpenGL vendor", gl.glGetString(GL.GL_VENDOR) );
                append( strBuilder, "Renderer", gl.glGetString(GL.GL_RENDERER) );
                append( strBuilder, "Shading Language version", gl.glGetString(GL.GL_SHADING_LANGUAGE_VERSION) );
                append( strBuilder, "OpenGL Extensions", gl.glGetString(GL.GL_EXTENSIONS) );


                String[] versionList = functionSupportMap.keySet().toArray( new String[0] );
                Arrays.sort(versionList);

                strBuilder.append("Core Function Support Info:\n\n");
                for ( String version : versionList ) {
                        String key = version;

                        strBuilder.append("  " + key + "\n");

                        List<String> fnList = functionSupportMap.get(key);
                        Collections.sort(fnList);
                        for ( String fn : fnList ) {
                                String message = "Error";
                                try {
                                     boolean available = gl.isFunctionAvailable(fn);
                                     message = "" + available;
                                } catch ( Exception ex ) {
                                }
                                append( strBuilder, "     " + fn, message);
                        }
                }

                versionList = extensionSupportMap.keySet().toArray( new String[0] );
                Arrays.sort(versionList);
                
                strBuilder.append("Core Extension Support Info:\n\n");
                for ( String version : versionList ) {
                        String key = version;

                        strBuilder.append("  " + key + "\n");

                        List<String> fnList = extensionSupportMap.get(key);
                        Collections.sort(fnList);
                        for ( String fn : fnList ) {
                                String message = "Error";
                                try {
                                     boolean available = gl.isExtensionAvailable(fn);
                                     message = "" + available;
                                } catch ( Exception ex ) {
                                }
                                append( strBuilder, "     " + fn, message);
                        }
                }
                
                this.strBuilder.toString();

       this.ready = true;
   }

   public void display(GLAutoDrawable drawable) {
   }

   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
   }

   public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
   }

}

