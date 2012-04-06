/****************************************************************/
/** This file is part of the Coral AHRS Utility,               **/
/** Copyright 2005 by Autonomous Reconnaissance Systems, Inc.  **/
/**                                                            **/
/** The Coral AHRS Utility is free software; you can           **/
/** redistribute it and/or modify it under the terms of the    **/
/** GNU General Public License as published by the             **/
/** Free Software Foundation; either version 2 of the License  **/
/** or (at your option) any later version.                     **/
/**                                                            **/
/** The Coral AHRS Utility is distributed in the hope that it  **/
/** will be useful, but WITHOUT ANY WARRANTY; without even the **/
/** implied warranty of MERCHANTABILITY or FITNESS FOR A       **/
/** PARTICULAR PURPOSE. See the GNU General Public License for **/
/** more details.                                              **/
/**                                                            **/
/** You should have received a copy of the GNU General Public  **/
/** License along with the Coral AHRS Utility; if not, write   **/
/** to the Free Software Foundation, Inc., 59 Temple Place,    **/
/** Suite 330, Boston, MA  02111-1307 USA                      **/
/****************************************************************/


package ajmas74.experimental.opengl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.JPanel;

//import ajmas74.rsrchandler.XmlRsrcParser;
//import ajmas74.rsrchandler.XmlRsrcRenderer;
//import ajmas74.rsrchandler.XmlRsrcRenderer2;
//import ajmas74.steamparser.RsrcParserException;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;


/**
 * This code is a Java port of the Coral AHRS(tm) Utility Software.
 * 
 * This port was done by Andre-John Mas. Currently text is not being rendered.
 *
 */
public class ArtificialHorizon extends JPanel implements GLEventListener, MouseListener, MouseMotionListener, Runnable {

    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private TextRenderer textRenderer;
    
    private GLCanvas canvas;
    
    private int viewmode = 2;
        
    private double rota;
    private double rotx;
    private double roty;
    private double rotz;
    
    private double roll;
    private double pitch;
    
    private double heading;
    private double chead;
    
    private int dragx;
    
    private double dragia;
    private int dragsign = 1 ;
    
    private double[] quat = new double[4];
    
    private int width;
    private int height;
    
    public ArtificialHorizon () 
    {
        setLayout(new BorderLayout());

        GLCapabilities capabilities = new GLCapabilities();

        this.canvas = new GLCanvas(capabilities);
        this.canvas.addGLEventListener(this);
        this.canvas.addMouseListener(this);
        this.canvas.addMouseMotionListener(this);
        add(this.canvas);
        
        this.rotx = 1;
        this.roty = 0;
        this.rotz = 0;
        this.rota = 0;

        this.chead = 0;

//        this.viewmode = 2;

        this.roll = this.pitch = this.heading = 0;                
        
        setOpaque( true );
        setBackground(Color.BLUE);
        
        new Thread( this).start();
    }
    
    public void run() {
        while ( true ) {
            this.roll -= 0.1;
//            this.heading += 0.1;
            
            this.canvas.repaint();
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void init(GLAutoDrawable drawable)
    {
        GL gl = drawable.getGL();
        
        gl.glShadeModel(GL.GL_SMOOTH);

        gl.glClearColor(.9f, .9f, .9f, .9f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        float[] lightdir = new float[] {5f,10f,-5f,0f};
        float[] lightclr = new float[] {1f,1f,1f,1f};
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightdir,0);
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,lightclr,0);
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,lightclr,0);
        gl.glEnable(GL.GL_LIGHT0);

        textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 12),false);
//        update();
    }


    public void display(GLAutoDrawable drawable) {
    
        GL gl = drawable.getGL();
        
        if (viewmode == 1)
        {
            drawAHRS(drawable);
        }
        else if (viewmode == 2)
        {
            drawHorizon(drawable);
        }
    }

    /**
     * Draws the artificial horizon
     * 
     * @param drawable
     */
    void drawHorizon( GLAutoDrawable drawable )
    {
        int i;
        double t;

        GL gl = drawable.getGL();
        
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-167, 167, -100, 100);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);    // Clear Screen And Depth Buffer
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glDisable(GL.GL_LIGHTING);
        gl.glDisable(GL.GL_CULL_FACE);
        gl.glDisable(GL.GL_DEPTH_TEST);
        
        gl.glRotated(roll * 180.0 / 3.1415926,  0,0,1);
        
        gl.glBegin(GL.GL_QUADS);
            gl.glColor4d(.5, .25, 0,1);
            gl.glVertex2d(-250, -250);
            gl.glVertex2d(-250, -pitch / (45 * 3.1415926/180.0) * 100);
            gl.glVertex2d(250, -pitch / (45 * 3.1415926/180.0) * 100);
            gl.glVertex2d(250, -250);

            gl.glColor4d(0, 0, .6,1);
            gl.glVertex2d(-250, 250);
            gl.glVertex2d(-250, -pitch / (45 * 3.1415926/180.0) * 100);
            gl.glVertex2d(250, -pitch / (45 * 3.1415926/180.0) * 100);
            gl.glVertex2d(250, 250);

        gl.glEnd();
        
        gl.glLineWidth(2);


        for (i=-90;i<=90;i+=10)
        {
            gl.glColor4d(1,1,1,1);
            gl.glBegin(GL.GL_LINES);
                gl.glVertex2d(-20, -(pitch + i * 3.1415926 / 180.0) / (45 * 3.1415926/180.0) * 100);
                gl.glVertex2d(-10, -(pitch +  i * 3.1415926 / 180.0) / (45 * 3.1415926/180.0) * 100);

                gl.glVertex2d(10, -(pitch + i * 3.1415926 / 180.0) / (45 * 3.1415926/180.0) * 100);
                gl.glVertex2d(20, -(pitch +  i * 3.1415926 / 180.0) / (45 * 3.1415926/180.0) * 100);
            gl.glEnd();

            String pstr = "" + -i;
            displayText(drawable, 23, -(pitch +  i * 3.1415926 / 180.0) / (45 * 3.1415926/180.0) * 100 - 2.5, 5, pstr, false);
        }




        gl.glLineWidth(2);
        gl.glColor4d(.8,.8,.8,1);
        gl.glBegin(GL.GL_LINES);
        for (i=-30;i<=30;i+=5)
        {
            if (i == 0)
            {
                gl.glVertex2d(-20, 0);
                gl.glVertex2d(20, 0);
            }
            else if ((i % 10) == 0)
            {
                gl.glVertex2d(-10, (double)i / 45.0 * 100.0);
                gl.glVertex2d(10, (double)i / 45.0 * 100.0);
            }
            else
            {
                gl.glVertex2d(-8, (double)i / 45.0 * 100.0);
                gl.glVertex2d(8, (double)i / 45.0 * 100.0);     
            }

        }
        gl.glEnd();

        gl.glLineWidth(1);
        gl.glBegin(GL.GL_LINES);
            gl.glVertex2d(0, -30.0 / 45.0 * 100);
            gl.glVertex2d(0, 30.0 / 45.0 * 100);

        gl.glEnd();


        gl.glBegin(GL.GL_TRIANGLES);
            gl.glVertex2d(0,45);
            gl.glVertex2d(4,38);
            gl.glVertex2d(-4,38);
        gl.glEnd();



        gl.glLoadIdentity();
        gl.glColor4d(1,1,1,1);

        gl.glBegin(GL.GL_TRIANGLES);
            gl.glVertex2d(0,86);
            gl.glVertex2d(4,79);
            gl.glVertex2d(-4,79);
        gl.glEnd();

        gl.glColor4d(1,1,1,1);
        gl.glLineWidth(3);

        gl.glBegin(GL.GL_LINES);
            gl.glVertex2d(-15,0);
            gl.glVertex2d(-5, 0);
            gl.glVertex2d(15,0);
            gl.glVertex2d(5, 0);
            gl.glVertex2d(0,15);
            gl.glVertex2d(0,5);
        gl.glEnd();



        gl.glBegin(GL.GL_LINE_STRIP);
            for (t=.1;t<3.1415926*2;t+=.1)
            {
                gl.glVertex2d(Math.cos(t) * 5, Math.sin(t) * 5);

            }
        gl.glEnd();

        gl.glLineWidth(2);
        gl.glBegin(GL.GL_LINE_STRIP);
            for (t=30; t<=150;t+=10)
            {
                gl.glVertex2d(Math.cos(t*3.1415926/180.0) * 45, Math.sin(t*3.1415926/180.0) * 45);

            }
        gl.glEnd();
        
        

        gl.glBegin(GL.GL_LINES);
            for (t=30; t<=150;t+=10)
            {
                gl.glVertex2d(Math.cos(t*3.1415926/180.0) * 45, Math.sin(t*3.1415926/180.0) * 45);

                if (t == 90)
                {
                    gl.glVertex2d(Math.cos(t*3.1415926/180.0) * 35, Math.sin(t*3.1415926/180.0) * 35);
                }
                else
                {
                    gl.glVertex2d(Math.cos(t*3.1415926/180.0) * 40, Math.sin(t*3.1415926/180.0) * 40);
                }

            }
            for (t=150; t<390;t+=30)
            {
                gl.glVertex2d(Math.cos(t*3.1415926/180.0) * 45, Math.sin(t*3.1415926/180.0) * 45);

                if (t == 90)
                {
                    gl.glVertex2d(Math.cos(t*3.1415926/180.0) * 35, Math.sin(t*3.1415926/180.0) * 35);
                }
                else
                {
                    gl.glVertex2d(Math.cos(t*3.1415926/180.0) * 40, Math.sin(t*3.1415926/180.0) * 40);
                }

            }
        gl.glEnd();


        int hd;
        for (i=-60;i<60;i++)
        {
            hd = ((int)(heading * 180.0 / 3.1415926) + i) % 360;

            if (hd < 0)
                hd += 360;
        

            if ((hd % 10) == 0)
            {
                gl.glBegin(GL.GL_LINES);
                    gl.glVertex2d(i * 167/60.0, 91);
                    gl.glVertex2d(i * 167/60.0, 86);
                gl.glEnd();
                
                String hdstr = "" + hd;

                // adjusted y value (originally 77.5), since it was being rendered in the
                // wrong place, with regards to the C/C++ implementation. x value adjusted by 
                displayText(drawable, (i * 167/60.0 - hdstr.length() * 3)+1, 77.5, 5, hdstr, true);
            }
            else if ((hd % 5) == 0)
            {
                gl.glBegin(GL.GL_LINES);
                    gl.glVertex2d(i * 167/60.0, 91);
                    gl.glVertex2d(i * 167/60.0, 89);
                gl.glEnd();

            }
        }

    }

    void drawAHRS(GLAutoDrawable drawable)
    {
        GL gl = drawable.getGL();
        
        gl.glMatrixMode(GL.GL_PROJECTION);      // Select The Projection Matrix
        gl.glLoadIdentity();                    // Reset The Projection Matrix
        glu.gluPerspective(40, (float)width/(float)height, 10, 100);


        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);    // Clear Screen And Depth Buffer
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();                                    // Reset The Current Modelview Matrix

        glu.gluLookAt(-30,15,0, 0, 0, 0, .7071, .7071, 0);

        gl.glRotated(chead, 0, 1, 0);

        gl.glDisable(GL.GL_LIGHTING);
        gl.glDisable(GL.GL_CULL_FACE);
        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glColor3d(.2,.2,.2);

        gl.glLineWidth(3);
        gl.glBegin(GL.GL_LINE_LOOP);

        for (double i=0;i<3.1415926 * 2;i+=.05)
        {
            gl.glVertex3d(Math.cos(i) * 10, 0, Math.sin(i) * 10);
        }


        gl.glEnd();


        gl.glBegin(GL.GL_LINES);

        gl.glVertex3d(0, 0, 10);
        gl.glVertex3d(0, 0, 9);

        gl.glVertex3d(0, 0, -10);
        gl.glVertex3d(0, 0, -9);

        
        gl.glVertex3d(-10, 0, 0);
        gl.glVertex3d(-9, 0, 0);

        gl.glVertex3d(10, 0, 0);
        gl.glVertex3d(9, 0, 0);



        gl.glVertex3d(10 * Math.cos(45.0 * 3.1415926 / 180.0), 0, 10 * Math.sin(45 * 3.1415926 / 180.0));
        gl.glVertex3d(9.25 * Math.cos(45.0 * 3.1415926 / 180.0), 0, 9.25 * Math.sin(45 * 3.1415926 / 180.0));

        gl.glVertex3d(10 * Math.cos(135.0 * 3.1415926 / 180.0), 0, 10 * Math.sin(135 * 3.1415926 / 180.0));
        gl.glVertex3d(9.25 * Math.cos(135.0 * 3.1415926 / 180.0), 0, 9.25 * Math.sin(135 * 3.1415926 / 180.0));

        gl.glVertex3d(10 * Math.cos(225.0 * 3.1415926 / 180.0), 0, 10 * Math.sin(225 * 3.1415926 / 180.0));
        gl.glVertex3d(9.25 * Math.cos(225.0 * 3.1415926 / 180.0), 0, 9.25 * Math.sin(225 * 3.1415926 / 180.0));

        gl.glVertex3d(10 * Math.cos(315.0 * 3.1415926 / 180.0), 0, 10 * Math.sin(315 * 3.1415926 / 180.0));
        gl.glVertex3d(9.25 * Math.cos(315.0 * 3.1415926 / 180.0), 0, 9.25 * Math.sin(315 * 3.1415926 / 180.0));

        gl.glEnd();


        gl.glBegin(GL.GL_LINE_STRIP);


        gl.glVertex3d(7.25, 0, -.6);
        gl.glVertex3d(8.5, 0,  -.6);
        gl.glVertex3d(7.25,  0, .6);
        gl.glVertex3d(8.5, 0,  .6);

        gl.glEnd();


        gl.glColor3d(.7,0,0);

        gl.glBegin(GL.GL_LINES);

        gl.glVertex3d(0,0,0);
        gl.glVertex3d(10,0,0);

        gl.glEnd();

        gl.glColor3d(0,.7,0);

        gl.glBegin(GL.GL_LINES);

        gl.glVertex3d(0,0,0);
        gl.glVertex3d(0,0,10);

        gl.glEnd();

        gl.glColor3d(0,0,.7);

        gl.glBegin(GL.GL_LINES);

        gl.glVertex3d(0,0,0);
        gl.glVertex3d(0,-10,0);

        gl.glEnd();
        
        

        gl.glLineWidth(5);
        gl.glDisable(GL.GL_LIGHTING);


        gl.glRotated(rota, rotx, -rotz, roty);

        float[] mat1 =new float[] {0.4f, 0.4f, 0.4f, 1};

        gl.glBegin(GL.GL_LINES);
            gl.glColor3d(1,0,0);
            gl.glVertex3d(0,0,0);
            gl.glVertex3d(5, 0, 0);

            gl.glColor3d(0,1,0);
            gl.glVertex3d(0,0,0);
            gl.glVertex3d(0, 0, 5);

            gl.glColor3d(0,0,1);
            gl.glVertex3d(0,0,0);
            gl.glVertex3d(0, -5, 0);
        gl.glEnd();

        
        gl.glEnable(GL.GL_LIGHTING);



        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, mat1, 0);
        


        gl.glBegin(GL.GL_QUADS);                                    // Draw A Quad
            gl.glNormal3d(0,0,1);
            gl.glVertex3d(-1.8855, .8181, 1.5);                 // Top Left 
            gl.glVertex3d( 1.8855, .8181, 1.5);                 // Top Right
            gl.glVertex3d( 1.8855,-.8181, 1.5);                 // Bottom Right
            gl.glVertex3d(-1.8855,-.8181, 1.5);                 // Bottom Left

            gl.glNormal3d(0,0,-1);
            gl.glVertex3d(-1.8855, .8181, -1.5);                    // Top Left 
            gl.glVertex3d( 1.8855, .8181, -1.5);                    // Top Right
            gl.glVertex3d( 1.8855,-.8181, -1.5);                    // Bottom Right
            gl.glVertex3d(-1.8855,-.8181, -1.5);                    // Bottom Left

            gl.glNormal3d(-1,0,0);
            gl.glVertex3d(-1.8855, .8181, -1.5);                    // Top Left 
            gl.glVertex3d(-1.8855, .8181, 1.5);                 // Top Right
            gl.glVertex3d(-1.8855,-.8181, 1.5);                 // Bottom Right
            gl.glVertex3d(-1.8855,-.8181, -1.5);                    // Bottom Left

            gl.glNormal3d(1,0,0);
            gl.glVertex3d(1.8855, .8181, -1.5);                 // Top Left 
            gl.glVertex3d(1.8855, .8181, 1.5);                  // Top Right
            gl.glVertex3d(1.8855,-.8181, 1.5);                  // Bottom Right
            gl.glVertex3d(1.8855,-.8181, -1.5);                 // Bottom Left
        
            gl.glNormal3d(0,-1,0);
            gl.glVertex3d(-1.8855, -.8181, 1.5);                    // Top Left 
            gl.glVertex3d( 1.8855, -.8181, 1.5);                    // Top Right
            gl.glVertex3d( 1.8855, -.8181, -1.5);                   // Bottom Right
            gl.glVertex3d(-1.8855, -.8181, -1.5);                   // Bottom Left

            gl.glNormal3d(0,1,0);
            gl.glVertex3d(-1.8855, .8181, 1.5);                 // Top Left 
            gl.glVertex3d( 1.8855, .8181, 1.5);                 // Top Right
            gl.glVertex3d( 1.8855, .8181, -1.5);                    // Bottom Right
            gl.glVertex3d(-1.8855, .8181, -1.5);                    // Bottom Left

        gl.glEnd();                                         // Done Drawing The Quad

        float[] mat2 = new float[] {0f,0f,0.3f,1f};
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, mat2,0);

        gl.glBegin(GL.GL_QUADS);

            gl.glNormal3d(1,0,0);
            gl.glVertex3d(2.32, .35, -.6);
            gl.glVertex3d(2.32, .35, .6);
            gl.glVertex3d(2.32, -.35, .45);
            gl.glVertex3d(2.32, -.35, -.45);

            gl.glNormal3d(0,1,0);
            gl.glVertex3d(2.32, .35, -.6);
            gl.glVertex3d(2.32, .35, .6);
            gl.glVertex3d(1.8855, .35, .6);
            gl.glVertex3d(1.8855, .35, -.6);

            gl.glNormal3d(0,-1,0);
            gl.glVertex3d(2.32, -.35, -.45);
            gl.glVertex3d(2.32, -.35, .45);
            gl.glVertex3d(1.8855, -.35, .45);
            gl.glVertex3d(1.8855, -.35, -.45);

            gl.glNormal3d(0, .2095, .9778);
            gl.glVertex3d(2.32, -.35, -.45);
            gl.glVertex3d(2.32, .35, -.6);
            gl.glVertex3d(1.8855, .35, -.6);
            gl.glVertex3d(1.8855, -.35, -.45);

            gl.glNormal3d(0, .2095, -.9778);
            gl.glVertex3d(2.32, -.35, .45);
            gl.glVertex3d(2.32, .35, .6);
            gl.glVertex3d(1.8855, .35, .6);
            gl.glVertex3d(1.8855, -.35, .45);


        gl.glEnd();
        
    }

    public void setOrientation(double[] q, double[] euler)
    {
        double[] quat = new double[4];
        double qm;
        qm = Math.sqrt(q[0] * q[0] + q[1] * q[1] + q[2] * q[2] + q[3] * q[3]);

        quat[0] = q[0] / qm;
        quat[1] = q[1] / qm;
        quat[2] = q[2] / qm;
        quat[3] = q[3] / qm;


        this.roll = euler[0] * 3.1415926 / 180.0;
        this.pitch = euler[1] * 3.1415926 / 180.0;
        this.heading = euler[2] * 3.1415926 / 180.0;

        double cos_a = quat[0];

        if (cos_a > 1)
        {
            this.rota = 0;
            this.rotx = 1;
            this.roty = 0;
            this.rotz = 0;
        }
        else if (cos_a < -1)
        {
            this.rota = 0;
            this.rotx = 1;
            this.roty = 0;
            this.rotz = 0;
        }
        else
        {
            this.rota = Math.acos(cos_a) * 2 * 180.0 / 3.1415926;
            double sin_a = Math.sqrt(1.0 - cos_a * cos_a);

            if (Math.abs(sin_a) < .0005) {
                sin_a = 1;
            }
            
            this.rotx = quat[1] / sin_a;
            this.roty = quat[2] / sin_a;
            this.rotz = quat[3] / sin_a;

        }
        this.quat[0] = quat[0];
        this.quat[1] = quat[1];
        this.quat[2] = quat[2];
        this.quat[3] = quat[3];

        this.canvas.repaint();
    

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
    {
        
        GL gl = drawable.getGL();
        
        if (height==0)                                      // Prevent A Divide By Zero By
        {
            height=1;                                       // Making Height Equal One
        }

        gl.glViewport(0,0,Math.abs(width),Math.abs(height));  // Reset The Current Viewport


        // Calculate The Aspect Ratio Of The Window
        //gl.gluPerspective(60.0f,(GL.GLfloat)width/(GL.GLfloat)height,0.1f,100.0f);

        gl.glMatrixMode(GL.GL_MODELVIEW);                           // Select The Modelview Matrix
        gl.glLoadIdentity();                                    // Reset The Modelview Matrix

        this.width = width;
        this.height = height;
    }
 
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
            boolean deviceChanged) {
        // TODO Auto-generated method stub
        
    }
    
    void displayText(GLAutoDrawable drawable, double x, double y, double size, String str, boolean ident)
    {
        GL gl = drawable.getGL();
        
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
    
        if (ident) {
            gl.glLoadIdentity();
        }
        
        gl.glColor3d(1,1,1);        
        
        size *= 0.1f;
        y += 1;
        
//        FontMetrics fm = this.getFontMetrics(textRenderer.getFont());
        float len = 0;//fm.stringWidth(str.trim());
        
//        len = len / 2.0f;
//        len += 1;
        
//        len *= 0.05f;
        
//        Font
        textRenderer.begin3DRendering();
        textRenderer.draw3D(str, (float)x - len, (float)y, 0, (float)size);
        textRenderer.end3DRendering();
 
        gl.glPopMatrix();
        
    }
    
    void setCompassRotation(double r)
    {
        this.chead = r;

        this.canvas.repaint();
    }    

    public void setViewMode(int viewMode)
    {
        this.viewmode = viewMode;
    }
    
    /* ---------------------------------------------------- */
    /* MouseListener methods                                */
    /* ---------------------------------------------------- */
    
    public void mouseClicked(MouseEvent e) {
        // info: ignored    
    }

    public void mouseEntered(MouseEvent e) {
        // info: ignored    
    }

    public void mouseExited(MouseEvent e) {
        // info: ignored    
    }

    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        //dragging = true;
        this.dragx = e.getX();
        this.dragia = this.chead;

        if (e.getY() < 147)
        {
            this.dragsign = -1;
        }
        else
        {
            this.dragsign = 1;
        }        
    }

    public void mouseReleased(MouseEvent e) {
       // info: ignored        
    }
    
    /* ---------------------------------------------------- */
    /* MouseMotionListener methods                          */
    /* ---------------------------------------------------- */
    
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getY() < 147 && dragsign == 1)
        {
            dragx = e.getX();
            dragia = chead;
            dragsign = -1;
        }
        else if (e.getY() > 147 && dragsign == -1)
        {
            dragx = e.getX();
            dragia = chead;
            dragsign = 1;
        }
        
        double newh = dragsign * .5 * (e.getX() - dragx) + dragia;
        setCompassRotation(newh);
    }

    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }    
    
    /* ---------------------------------------------------- */
    /* Application Enty Point (main) */
    /* ---------------------------------------------------- */    

    public static void main ( String[] args ) {
        JFrame glFrame = new JFrame("Artificial Horizton");
        glFrame.setBounds( new Rectangle(453, 50, 400, 400) );

        ArtificialHorizon artificialHorizton = new ArtificialHorizon();
        glFrame.add(artificialHorizton);

        glFrame.setVisible(true);
        
//        try {
//            (new XmlRsrcRenderer2()).render((new XmlRsrcParser())
//                    .parseResourceFile("data/AHWindow.res.xml", null));
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (RsrcParserException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }



}
