package ajmas74.experimental.opengl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.opengl.util.GLUT;

import quicktime.qd3d.math.Point3D;

/**
 * some stuff taken from: http://www.felixgers.de/teaching/jogl/index.html
 * 
 * @author ajmas
 * 
 */
public class Axis3D extends Panel implements GLEventListener, KeyListener, MouseWheelListener {

    /** */
    private static final long serialVersionUID = 1L;

    private GLU glu = new GLU();

    private GLCanvas canvas;

    private float screenY = 0.0f;

    private float screenX = 0.0f;

    private float inc = 3.0f;

    private float zoom = 0.0f;
    
    private int frame = 0;
    
    private long time = 0;
    
    private static final String[] TEXTURE_FILES = new String[] {
//        "data/earthmap.jpg",
        "data/artificialhorizon.jpg",
        "data/starmap.jpg",
        "data/artificialhorizon.jpg",
        //"data/white.jpg"
        
    };
    
    int textureCount = TEXTURE_FILES.length;
    int[] textureIds = new int[textureCount];
    
    GLUquadric quadratic;
    
    final boolean enableMipmapping = true;

//    private static final String IMAGE_FILENAME ="data/earthmap.jpg";
    
    // Hardware accelerated mipmaps (if supported)
    // are auto-created when textures are loaded.
    // If this is enabled gluBuild2DMipmaps should
    // not be called.
    final boolean enableHardwareAcceleratedMipmaps = false;

    // Decide if we have to build mipmapps.
    final boolean buildMipmaps = enableMipmapping
            && !enableHardwareAcceleratedMipmaps;

    public Axis3D() {
        setLayout(new BorderLayout());

        GLCapabilities capabilities = new GLCapabilities();

        this.canvas = new GLCanvas(capabilities);
        this.canvas.addGLEventListener(this);
        this.canvas.addKeyListener(this);
        this.canvas.addMouseWheelListener(this);
        add(this.canvas);
    }

    /**
     * 
     * @param cameraLocation (long,lat, height in meters)
     * @param cameraAngle
     * @return
     */
    private Texture[][] getTexture ( Point3D cameraLocation, double cameraAngle ) {
        return null;
    }
    
    // Read texture form image from file with
    // formats like bmp, png, jpg, etc.
    private Texture readTextureFile(String filename) {
        Texture texture = null;
        try {
            texture = TextureReader.readTexture(filename);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        // printDebug( filename, texture );//ddd
        return texture;
    }

    // Load texture data into openGL texturememory.
    private void loadTextureIntoOpenGL(GL gl, GLU glu, Texture texture, int target) {
        
        if (buildMipmaps) {
            // Create texture with all mipmap levels.
            glu.gluBuild2DMipmaps(target, GL.GL_RGB8, // Internal Texel Format,
                    texture.getWidth(), texture.getHeight(), GL.GL_RGB, // External
                                                                        // format
                                                                        // from
                                                                        // image,
                    GL.GL_UNSIGNED_BYTE, texture.getPixels() // Imagedata
                    );
        } else {
            gl.glTexImage2D(target, 1, // Mipmap level.
                    GL.GL_RGBA,// GL.GL_RGBA, // Internal Texel Format,
                    texture.getWidth(), texture.getHeight(), 0, // Border
                    GL.GL_RGB, // External format from image,
                    GL.GL_UNSIGNED_BYTE, texture.getPixels() // Imagedata
                    );
        }
    }

  
    
    /* ---------------------------------------------------- */
    /* GLEventListener methods */
    /* ---------------------------------------------------- */

    public void display(GLAutoDrawable drawable) {
        
//        long currentTime = System.currentTimeMillis();
//        
//        if ( currentTime - this.time > 1000 ) {
//            this.time = currentTime;
//            System.out.println("fps:" + this.frame);
//            this.frame = 0;
//        } else {
//            this.frame++;
//        }
//        
        GL gl = drawable.getGL();
        
        
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // Clear
                                                                        // window.

        gl.glLoadIdentity();

        OpenGlUtils.drawRectangle(gl,-5,5,-5,-5,textureIds[0]);
        
//         gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);

        gl.glBindTexture(GL.GL_TEXTURE_2D, textureIds[0]);
        
        gl.glPushMatrix();
        
        float baseZoom = -10.0f;
        if ( (-10.0f + this.zoom) >= -2.1 ) {
            baseZoom = -2.1f;
        } else {
            baseZoom = baseZoom + this.zoom;
        }
        System.out.println ( this.zoom + " ||| " + baseZoom );
        
        gl.glTranslatef(0.0f, 0.0f, baseZoom);

        gl.glRotatef(screenY, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(screenX, 1.0f, 0.0f, 0.0f);

        gl.glColor3f(1.0f, 1.0f, 1.0f);

        
        displayAxisXYZ( gl, 4 );
        
//        OpenGlUtils.drawSphere(gl, 2.0, 30, 30); // glutSolidSphere(1.0, 10, 10);

//        gl.glPolygonMode(GL.GL_FRONT, GL.GL_FILL);
        
        gl.glFlush();
    }

    private void displayAxisXYZ (  GL gl, float length ) {
        
        float rotationAngle = -90;

        GLUT glut = new GLUT();

        gl.glPushMatrix();

        // info: y-axis

        gl.glColor3f ( 0, 1, 0 );

        displayAxis (gl, glut, length);

        // info: x-axis

        gl.glRotatef(rotationAngle, 0.0f, 0.0f, 1.0f);

        gl.glColor3f ( 1, 0, 0 );

        displayAxis (gl, glut, length);

        // info: z-axis

        gl.glRotatef(-rotationAngle, 1.0f, 0.0f, 0.0f);

        gl.glColor3f ( 0, 0, 1 );

        displayAxis (gl, glut, length);

        gl.glPopMatrix();



      }

      private void displayAxis ( GL gl, GLUT glut, float length ) {

        gl.glPushMatrix();
          
        // mayb use a cylinder (GlutCylinder)
        
        gl.glBegin(GL.GL_LINES);

        gl.glVertex3f(0,0,0);
      
        gl.glVertex3f(0,length,0);

        gl.glEnd();
         
        // consider scaling cone between a min and max value

        
        
        gl.glTranslatef(0, length, 0);
        gl.glRotated(-90, 1, 0, 0);
//        gl.glRotated(180, 0, 1, 0);
        glut.glutSolidCone ( 0.2f, 0.5f, 10, 10 );

        gl.glPopMatrix();
        
      }

      
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
            boolean deviceChanged) {
        // TODO Auto-generated method stub

    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        quadratic = glu.gluNewQuadric();  
        
        int method = 2;

        if (method == 0) {
            // This will clear the ackground color to black
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            // Enables Clearing Of The Depth Buffer
            gl.glClearDepth(1.0);
            // Enables Depth Testing
            gl.glEnable(GL.GL_DEPTH_TEST);
            // The Type Of Depth Test To Do
            gl.glDepthFunc(GL.GL_LEQUAL);
            // Really Nice Perspective Calculations
            gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
            // Full Brightness. 50% Alpha (new )
            gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        } else if (method == 1){

            float sun_direction[] = { 0.0f, 2.0f, -1.0f, 1.0f };
//            float sun_intensity[] = { 0.7f, 0.7f, 0.7f, 1.0f };
            float sun_intensity[] = { 1.7f, 1.7f, 1.7f, 1.0f };
//            float ambient_intensity[] = { 0.3f, 0.3f, 0.3f, 1.0f };
            float ambient_intensity[] = { 1.5f, 1.5f, 1.5f, 1.0f };
            
            // gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Set window color to
            // white.
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Set window color to
                                                        // white.

            gl.glClearDepth(1.0);
            // Enables Depth Testing
            gl.glEnable(GL.GL_DEPTH_TEST);
            // The Type Of Depth Test To Do
            gl.glDepthFunc(GL.GL_LEQUAL);
            // Really Nice Perspective Calculations
            gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

            // -- compute location (START)

            // double userTheta = 0;
            // double userHeight = 0;
            //            
            // double x = 2 * Math.cos(userTheta); // my x-, y-, and
            // z-coordinates
            // double y = 2 * Math.sin(userTheta);
            // double z = userHeight;
            // double d = Math.sqrt(x * x + y * y + z * z); // distance to
            // origin
            //               
            // gl.glMatrixMode(GL.GL_PROJECTION); // Set projection parameters.
            // gl.glLoadIdentity();
            // gl.glFrustum(-d * 0.5, d * 0.5, -d * 0.5, d * 0.5, d - 1.1, d +
            // 1.1);
            // glu.gluLookAt(x, y, z, 0, 0, 0, 0, 0, 1);

            // -- compute location (END)
            gl.glEnable(GL.GL_DEPTH_TEST); // Draw only closest surfaces

            gl.glEnable(GL.GL_LIGHTING); // Set up ambient light.
            gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, ambient_intensity, 0);

            gl.glEnable(GL.GL_LIGHT0); // Set up sunlight.
            gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, sun_direction, 0);
            gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, sun_intensity, 0);

            gl.glEnable(GL.GL_COLOR_MATERIAL); // Configure glColor().
            gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);

        } else {
            
            float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
            float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
            float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};
            
            //gl.glEnable(GL.GL_BLEND);
            //gl.glDisable(GL.GL_DEPTH_TEST);
//            gl.glEnable(GL.GL_LIGHTING);
            gl.glEnable(GL.GL_TEXTURE_2D);                                  // Enable Texture Mapping
            gl.glShadeModel(GL.GL_SMOOTH);                                  //Enables Smooth Color Shading
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);                        //This Will Clear The Background Color To Black
            gl.glClearDepth(1.0);                                           //Enables Clearing Of The Depth Buffer
            gl.glEnable(GL.GL_DEPTH_TEST);                                  //Enables Depth Testing
            gl.glDepthFunc(GL.GL_LEQUAL);                                   //The Type Of Depth Test To Do
            gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);     // Really Nice Perspective Calculations
            gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightAmbient,0);      // Setup The Ambient Light
            gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lightDiffuse,0);      // Setup The Diffuse Light
            gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPosition,0);    // Position The Light
            gl.glEnable(GL.GL_LIGHT1);                                      // Enable Light One

            gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);                           // Full Brightness.  50% Alpha (new )
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);              
        }
        
//        initTexture ( gl );
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        GL gl = drawable.getGL();
        ;

        if (height == 0) {
            height = 1;
        }

        // Reset The Current Viewport and perspective transformation
        gl.glViewport(0, 0, width, height);
        // Select The Projection Matrix
        gl.glMatrixMode(GL.GL_PROJECTION);
        // Reset The Projection Matrix
        gl.glLoadIdentity();

        // Calculate the aspect ratio of the window
        glu.gluPerspective(45.0f, width / (height * 1.0f), 0.1f, 100.0f);

        // Select The Modelview Matrix
        gl.glMatrixMode(GL.GL_MODELVIEW);

        gl.glLoadIdentity();

    }

    /* ---------------------------------------------------- */
    /* KeyListener methods */
    /* ---------------------------------------------------- */

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                this.screenY -= inc;
                this.canvas.repaint(0);
                break;
            case KeyEvent.VK_RIGHT:
                this.screenY += inc;
                this.canvas.repaint(0);
                break;
            case KeyEvent.VK_UP:
                if ( (e.getModifiers() & KeyEvent.ALT_MASK) != 0 ) {
                    this.zoom += inc;
                } else {
                    this.screenX -= inc;
                }
                this.canvas.repaint(0);
                break;
            case KeyEvent.VK_DOWN:
                if ( (e.getModifiers() & KeyEvent.ALT_MASK) != 0 ) {
                    this.zoom -= inc;
                } else {
                    this.screenX += inc;
                }
                this.canvas.repaint(0);
                break;
            case KeyEvent.VK_SPACE:
                this.screenX = 0.0f;
                this.screenY = 0.0f;
                this.canvas.repaint(0);
                break;

        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    
    /* ---------------------------------------------------- */
    /* MouseWheelListener                                    */
    /* ---------------------------------------------------- */
    
    public void mouseWheelMoved(MouseWheelEvent e) {
        if ( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
//            e.get
            this.zoom += ( e.getWheelRotation() * 0.05 );
            this.canvas.repaint(0);
            System.out.println(e.getScrollAmount());
        }
        
    }
    
    /* ---------------------------------------------------- */
    /* Application Enty Point (main) */
    /* ---------------------------------------------------- */

    public static void main(String[] args) {

        JFrame wiimote3dFrame = new JFrame();
        wiimote3dFrame.setBounds(50, 50, 400, 400);

        Axis3D wm3d = new Axis3D();
        wiimote3dFrame.add(wm3d);

        // JPanel graphPanel = new JPanel();
        // graphPanel.setMinimumSize(new Dimension(200,100));
        // graphPanel.setPreferredSize(new Dimension(200,100));
        // wiimote3dFrame.add(graphPanel,BorderLayout.NORTH);

        JLabel statusPanel = new JLabel();
        statusPanel.setText("  No IR Data");
        statusPanel.setMinimumSize(new Dimension(200, 20));
        statusPanel.setMaximumSize(new Dimension(200, 20));
        statusPanel.setPreferredSize(new Dimension(200, 20));
        wiimote3dFrame.add(statusPanel, BorderLayout.SOUTH);

        wiimote3dFrame.setVisible(true);

        wm3d.canvas.requestFocus();
    }



}
