package ajmas74.experimental.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class AbstractGLCanvas extends GLCanvas implements GLEventListener {

    /** */
    private static final long serialVersionUID = 1L;
    
    protected GLU glu;
    
    public AbstractGLCanvas () {
        addGLEventListener(this);
        
        this.glu = new GLU();
    }
    
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        
        // info: clear display
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

        gl.glFlush();        
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
            boolean deviceChanged) {
        // TODO Auto-generated method stub
        
    }

    public void init(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();
        
        float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
        float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};
        
        //gl.glEnable(GL.GL_BLEND);
        //gl.glDisable(GL.GL_DEPTH_TEST);
//        gl.glEnable(GL.GL_LIGHTING);
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

    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        GL gl = drawable.getGL();
       
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

}
