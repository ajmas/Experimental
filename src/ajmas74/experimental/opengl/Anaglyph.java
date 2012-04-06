package ajmas74.experimental.opengl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.vecmath.Point3d;

import com.sun.opengl.util.GLUT;

/**
 * Anaglyphs implemented in OpenGL without stereo buffer support.
 * Note:<br/>
 * 1. All objects must be drawn as gray scale!<br/>
 * 2. This is written for illustrative purposes...not efficiency!<br/>
 * 3. Example of correct stereo projections
 * </p><p>
 * Ported from C code available at:
 * <ul>
 *  <li>http://local.wasp.uwa.edu.au/~pbourke/texture_colour/anaglyph/</li>
 *  <li>http://local.wasp.uwa.edu.au/~pbourke/texture_colour/anaglyph/daniel/</li>
 * </ul>
 * </p><p> 
 * Not everything has been ported at this point, but enough to display the anaglyph.
 * The elements that have not been implemented are essentially the menus and the ability
 * to save to file.
 * </p><p>
 * This code depends on JOGL, which you will need to download and install
 * </p>
 * @author Paul Bourke (original C version)
 * @author Daniel van Vugt (removed all accumulation buffer code)
 * @author Andre-John Mas (Java Port)
 *
 */
public class Anaglyph extends GLCanvas implements GLEventListener, Runnable, MouseMotionListener,
        KeyListener {

    /** */
    private static final long serialVersionUID = 1L;
    /** Radians per degree */
    private static final double DTOR = 0.0174532925;
    /** 2 * PI */
    private static final double TWOPI = Math.PI * 2;
    /** PI / 2 */
    private static final double PID2 = Math.PI / 2.0;

    /* Glasses filter types */
    public static final int REDBLUE = 1;
    public static final int REDGREEN = 2;
    public static final int REDCYAN = 3;
    public static final int BLUERED = 4;
    public static final int GREENRED = 5;
    public static final int CYANRED = 6;

    /* Model types */
    public static final int MESH = 1;
    public static final int BOX = 2;
    public static final int SPHERE = 3;
    public static final int PULSAR = 4;
    public static final int KNOT = 5;
    public static final int TRITORUS = 6;
    public static final int LORENZ = 7;

    int modeltype = SPHERE;

    private int glassestype = REDBLUE;

    private Camera camera = new Camera();

    private Point3d origin = new Point3d();

    private boolean debug = false;

    /** Each object can auto-rotate */
    private double rotatespeed = 0.5;

    private int xlast=-1,ylast=-1;
    
    /** Camera rotation angle increment */
    private double dtheta = 1.0;

    private GLU glu;

    private GLUT glut;

    public Anaglyph() {

        addGLEventListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        this.glu = new GLU();
        this.glut = new GLUT();

        cameraHome(0);
        
        new Thread(this).start();
    }

    // ----------------------------------------------------------------------
    // GLEventListener methods
    // ----------------------------------------------------------------------

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        Point3d right = new Point3d();
        Point3d focus = new Point3d();

        /* Determine the focal point */
        normalise(camera.vd);
        focus.x = camera.vp.x + camera.focallength * camera.vd.x;
        focus.y = camera.vp.y + camera.focallength * camera.vd.y;
        focus.z = camera.vp.z + camera.focallength * camera.vd.z;

        /* Derive the the "right" vector */
        crossProduct(camera.vd, camera.vu, right);
        normalise(right);
        right.x *= camera.eyesep / 2.0;
        right.y *= camera.eyesep / 2.0;
        right.z *= camera.eyesep / 2.0;

        /* Set the buffer for writing and reading */
        gl.glDrawBuffer(GL.GL_BACK);
        gl.glReadBuffer(GL.GL_BACK);

        /* Clear things */
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        /* DVV */
        /* glClear(GL.GL_ACCUM_BUFFER_BIT); *//* Not strictly necessary */

        /* Set projection */
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(camera.aperture, camera.screenwidth / (double) camera.screenheight, 0.1,
                10000.0);
        gl.glViewport(0, 0, camera.screenwidth, camera.screenheight);

        /* Left eye filter */
        gl.glColorMask(true, true, true, true);
        switch (glassestype) {
        case REDBLUE:
        case REDGREEN:
        case REDCYAN:
            gl.glColorMask(true, false, false, true);
            break;
        case BLUERED:
            gl.glColorMask(false, false, true, true);
            break;
        case GREENRED:
            gl.glColorMask(false, true, false, true);
            break;
        case CYANRED:
            gl.glColorMask(false, true, true, true);
            break;
        }

        /* Create the model */
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(camera.vp.x - right.x, camera.vp.y - right.y, camera.vp.z - right.z, focus.x,
                focus.y, focus.z, camera.vu.x, camera.vu.y, camera.vu.z);
        createWorld(gl);
        gl.glFlush();
        gl.glColorMask(true, true, true, true);

        /* DVV */
        /* Write over the accumulation buffer */
        /* glAccum(GL.GL_LOAD,1.0); *//* Could also use gl.glAccum(GL.GL_ACCUM,1.0); */

        gl.glDrawBuffer(GL.GL_BACK);
        /*
         * gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
         */
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(camera.aperture, camera.screenwidth / (double) camera.screenheight, 0.1,
                10000.0);
        gl.glViewport(0, 0, camera.screenwidth, camera.screenheight);

        /* Right eye filter */
        gl.glColorMask(true, true, true, true);
        switch (glassestype) {
        case REDBLUE:
            gl.glColorMask(false, false, true, true);
            break;
        case REDGREEN:
            gl.glColorMask(false, true, false, true);
            break;
        case REDCYAN:
            gl.glColorMask(false, true, true, true);
            break;
        case BLUERED:
        case GREENRED:
        case CYANRED:
            gl.glColorMask(true, false, false, true);
            break;
        }

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(camera.vp.x + right.x, camera.vp.y + right.y, camera.vp.z + right.z, focus.x,
                focus.y, focus.z, camera.vu.x, camera.vu.y, camera.vu.z);
        createWorld(gl);
        gl.glFlush();
        gl.glColorMask(true, true, true, true);

        /* Addin the new image and copy the result back */
        /*
         * DVV gl.glAccum(GL.GL_ACCUM,1.0); gl.glAccum(GL.GL_RETURN,1.0);
         */

        // /* Optionally dump image */
        // if (windowdump || movierecord) {
        // WindowDump(camera.screenwidth,camera.screenheight);
        // windowdump = FALSE;
        // }
        gl.glFlush();

        /* Let's look at it */
        // glut.glutSwapBuffers();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        // not implemented
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glViewport(0, 0, width, height);
        camera.screenwidth = width;
        camera.screenheight = height;
    }

    public void init(GLAutoDrawable drawable) {

        int[] num = new int[2];

        GL gl = drawable.getGL();

        // glut.glutInitDisplayMode(GLUT.GLUT_DOUBLE | GLUT.GLUT_RGB | GLUT.GLUT_DEPTH);

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDisable(GL.GL_LINE_SMOOTH);
        gl.glDisable(GL.GL_POINT_SMOOTH);
        gl.glDisable(GL.GL_POLYGON_SMOOTH);
        gl.glDisable(GL.GL_DITHER);
        gl.glDisable(GL.GL_CULL_FACE);
        gl.glDisable(GL.GL_BLEND); /* Not necessary but for bug in PS350 driver */
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glLineWidth(1.0f);
        gl.glPointSize(1.0f);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
        gl.glFrontFace(GL.GL_CW);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearAccum(0.0f, 0.0f, 0.0f, 0.0f); /* The default */
        gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);

        /*
         * In case you want to check what the colour depth of your accumulation buffer is, hopefully
         * it is 16 bits
         */
        if (debug) {
            gl.glGetIntegerv(GL.GL_ACCUM_RED_BITS, num, 0);
            System.err.println("Red bits: " + num[0]);
            gl.glGetIntegerv(GL.GL_ACCUM_GREEN_BITS, num, 0);
            System.err.println("Green bits: " + num[0]);
            gl.glGetIntegerv(GL.GL_ACCUM_BLUE_BITS, num, 0);
            System.err.println("Blue bits: " + num[0]);
            gl.glGetIntegerv(GL.GL_ACCUM_ALPHA_BITS, num, 0);
            System.err.println("Alpha bits: " + num[0]);
        }
    }

    /*
     * Create one of the possible models Handle the rotation of the model, about the y axis
     */
    void createWorld(GL gl) {
        double rotateangle = 0.0;

        gl.glPushMatrix();
        gl.glRotated(rotateangle, 0.0, 1.0, 0.0);
        switch (modeltype) {
        case MESH:
            makeMesh(gl);
            break;
        case BOX:
            makeBox(gl);
            break;
        case SPHERE:
            makeSphere(gl);
            break;
        case KNOT:
            makeKnot(gl);
            break;
        case PULSAR:
            makePulsar(gl);
            break;
        case TRITORUS:
            makeTriTorus(gl);
            break;
        case LORENZ:
            makeLorenz(gl);
            break;
        }
        gl.glPopMatrix();

        rotateangle += rotatespeed;
        makeLighting(gl);
    }

    /*
     * Create the geometry for a wireframe box
     */
    void makeBox(GL gl) {
        Point3d pmin = new Point3d(-3, -3, -3);
        Point3d pmax = new Point3d(3, 3, 3);

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL.GL_LINE_STRIP);
        gl.glVertex3d(pmin.x, pmin.y, pmin.z);
        gl.glVertex3d(pmax.x, pmin.y, pmin.z);
        gl.glVertex3d(pmax.x, pmin.y, pmax.z);
        gl.glVertex3d(pmin.x, pmin.y, pmax.z);
        gl.glVertex3d(pmin.x, pmin.y, pmin.z);
        gl.glVertex3d(pmin.x, pmax.y, pmin.z);
        gl.glVertex3d(pmax.x, pmax.y, pmin.z);
        gl.glVertex3d(pmax.x, pmax.y, pmax.z);
        gl.glVertex3d(pmin.x, pmax.y, pmax.z);
        gl.glVertex3d(pmin.x, pmax.y, pmin.z);
        gl.glEnd();
        gl.glBegin(GL.GL_LINES);
        gl.glVertex3d(pmax.x, pmin.y, pmin.z);
        gl.glVertex3d(pmax.x, pmax.y, pmin.z);
        gl.glEnd();
        gl.glBegin(GL.GL_LINES);
        gl.glVertex3d(pmax.x, pmin.y, pmax.z);
        gl.glVertex3d(pmax.x, pmax.y, pmax.z);
        gl.glEnd();
        gl.glBegin(GL.GL_LINES);
        gl.glVertex3d(pmin.x, pmin.y, pmax.z);
        gl.glVertex3d(pmin.x, pmax.y, pmax.z);
        gl.glEnd();
    }

    /*
     * Create the geometry for a sphere
     */
    void makeSphere(GL gl) {
        int i, j, n = 32;
        double t1, t2, t3, r = 4;

        Point3d e = new Point3d(0, 0, 0);
        Point3d p = new Point3d(0, 0, 0);
        Point3d c = new Point3d(0, 0, 0);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        for (j = 0; j < n / 2; j++) {
            t1 = -PID2 + j * Math.PI / (n / 2);
            t2 = -PID2 + (j + 1) * Math.PI / (n / 2);

            gl.glBegin(GL.GL_QUAD_STRIP);
            for (i = 0; i <= n; i++) {
                t3 = i * TWOPI / n;

                e.x = Math.cos(t1) * Math.cos(t3);
                e.y = Math.sin(t1);
                e.z = Math.cos(t1) * Math.sin(t3);
                p.x = c.x + r * e.x;
                p.y = c.y + r * e.y;
                p.z = c.z + r * e.z;
                gl.glNormal3d(e.x, e.y, e.z);
                gl.glTexCoord2d(i / (double) n, 2 * j / (double) n);
                gl.glVertex3d(p.x, p.y, p.z);

                e.x = Math.cos(t2) * Math.cos(t3);
                e.y = Math.sin(t2);
                e.z = Math.cos(t2) * Math.sin(t3);
                p.x = c.x + r * e.x;
                p.y = c.y + r * e.y;
                p.z = c.z + r * e.z;

                gl.glNormal3d(e.x, e.y, e.z);
                gl.glTexCoord2d(i / (double) n, 2 * (j + 1) / (double) n);
                gl.glVertex3d(p.x, p.y, p.z);

            }
            gl.glEnd();
        }
    }

    /*
     * Create the geometry for the knot
     */
    void makeKnot(GL gl) {
        int i, n = 200;
        double x, y, z;
        double mu;

        gl.glLineWidth(2.0f);
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        gl.glBegin(GL.GL_LINE_STRIP);
        for (i = 0; i <= n; i++) {
            mu = i * TWOPI / (double) n;
            x = 10 * (Math.cos(mu) + Math.cos(3 * mu)) + Math.cos(2 * mu) + Math.cos(4 * mu);
            y = 6 * Math.sin(mu) + 10 * Math.sin(3 * mu);
            z = 4 * Math.sin(3 * mu) * Math.sin(5 * mu / 2) + 4 * Math.sin(4 * mu) - 2
                    * Math.sin(6 * mu);
            gl.glVertex3d(x / 4, y / 4, z / 4);
        }
        gl.glEnd();

        gl.glLineWidth(1.0f);
    }

    /*
     * Create the geometry for the lorenz attractor
     */
    void makeLorenz(GL gl) {
        int i, n = 10000;
        double x0 = 0.1, y0 = 0, z0 = 0, x1, y1, z1;
        double h = 0.005;
        double a = 10.0;
        double b = 28.0;
        double c = 8.0 / 3.0;

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL.GL_POINTS);
        for (i = 0; i <= n; i++) {
            x1 = x0 + h * a * (y0 - x0);
            y1 = y0 + h * (x0 * (b - z0) - y0);
            z1 = z0 + h * (x0 * y0 - c * z0);
            x0 = x1;
            y0 = y1;
            z0 = z1;
            if (i > 100) {
                gl.glVertex3d((x0 - 0.95) / 5, (y0 - 1.78) / 5, (z0 - 26.7) / 5);
            }
        }
        gl.glEnd();
    }

    /*
     * Create the geometry for a tritorus
     */
    void makeTriTorus(GL gl) {
        int m = 51;
        int i, j, k;
        double u, v, u1, v1, delta = 0.001;
        Point3d[] p = createPoint3dArray(4);
        Point3d[] n = createPoint3dArray(4);
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        gl.glBegin(GL.GL_QUADS);
        for (i = 0; i < m; i++) {
            for (j = 0; j < m; j++) {

                u = -Math.PI + i * TWOPI / m;
                u1 = -Math.PI + (i + 1) * TWOPI / m;
                v = -Math.PI + j * TWOPI / m;
                v1 = -Math.PI + (j + 1) * TWOPI / m;

                p[0] = triTorusEval(u, v);
                p1 = triTorusEval(u + delta, v);
                p2 = triTorusEval(u, v + delta);
                n[0] = calculateNormal(p[0], p1, p2);

                p[1] = triTorusEval(u1, v);
                p1 = triTorusEval(u1 + delta, v);
                p2 = triTorusEval(u1, v + delta);
                n[1] = calculateNormal(p[1], p1, p2);

                p[2] = triTorusEval(u1, v1);
                p1 = triTorusEval(u1 + delta, v1);
                p2 = triTorusEval(u1, v1 + delta);
                n[2] = calculateNormal(p[2], p1, p2);

                p[3] = triTorusEval(u, v1);
                p1 = triTorusEval(u + delta, v1);
                p2 = triTorusEval(u, v1 + delta);
                n[3] = calculateNormal(p[3], p1, p2);

                for (k = 0; k < 4; k++) {
                    gl.glNormal3d(n[k].x, n[k].y, n[k].z);
                    gl.glVertex3d(2.5 * p[k].x, 2.5 * p[k].y, 2.5 * p[k].z);
                }
            }
        }
        gl.glEnd();
    }

    Point3d triTorusEval(double u, double v) {
        Point3d p = new Point3d();

        p.x = Math.sin(u) * (1 + Math.cos(v));
        p.y = Math.sin(u + 2 * Math.PI / 3) * (1 + Math.cos(v + 2 * Math.PI / 3));
        p.z = Math.sin(u + 4 * Math.PI / 3) * (1 + Math.cos(v + 4 * Math.PI / 3));

        return (p);
    }

    /*
     * Create the geometry for the mesh
     */
    void makeMesh(GL gl) {
        int i, j, n = 1, w = 4;

        gl.glColor3d(1.0, 1.0, 1.0);
        gl.glBegin(GL.GL_LINES);
        for (i = -w; i <= w; i += n) {
            for (j = -w; j < w; j += n) {
                gl.glVertex3d(0.0, (double) i, (double) j);
                gl.glVertex3d(0.0, (double) i, (double) j + n);
            }
        }
        for (j = -w; j <= w; j += n) {
            for (i = -w; i < w; i += n) {
                gl.glVertex3d(0.0, (double) i, (double) j);
                gl.glVertex3d(0.0, (double) i + n, (double) j);
            }
        }
        gl.glEnd();
    }

    /**
     * Create the geometry for the pulsar
     */
    void makePulsar(GL gl) {
        int i, j, k;
        double cradius = 1; /* Final radius of the cone */
        double clength = 5; /* Cone length */
        double sradius = 1.3; /* Final radius of sphere */
        double r1 = 1.4, r2 = 1.8; /* Min and Max radius of field lines */
        double x, y, z;
        Point3d[] p = createPoint3dArray(4);
        Point3d[] n = createPoint3dArray(4);
        double rotateangle = 0.0;

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);

        /* Top level rotation - spin */
        gl.glPushMatrix();
        gl.glRotatef((float) rotateangle, 0.0f, 1.0f, 0.0f);

        /* Rotation about spin axis */
        gl.glPushMatrix();
        gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);

        /* Light in center */
        glut.glutSolidSphere(0.9f, 16, 8);

        /* Spherical center */
        for (i = 0; i < 360; i += 5) {
            for (j = -80; j < 80; j += 5) {

                p[0].x = sradius * Math.cos(j * DTOR) * Math.cos(i * DTOR);
                p[0].y = sradius * Math.sin(j * DTOR);
                p[0].z = sradius * Math.cos(j * DTOR) * Math.sin(i * DTOR);
                n[0] = p[0];

                p[1].x = sradius * Math.cos((j + 5) * DTOR) * Math.cos(i * DTOR);
                p[1].y = sradius * Math.sin((j + 5) * DTOR);
                p[1].z = sradius * Math.cos((j + 5) * DTOR) * Math.sin(i * DTOR);
                n[1] = p[1];

                p[2].x = sradius * Math.cos((j + 5) * DTOR) * Math.cos((i + 5) * DTOR);
                p[2].y = sradius * Math.sin((j + 5) * DTOR);
                p[2].z = sradius * Math.cos((j + 5) * DTOR) * Math.sin((i + 5) * DTOR);
                n[2] = p[2];

                p[3].x = sradius * Math.cos(j * DTOR) * Math.cos((i + 5) * DTOR);
                p[3].y = sradius * Math.sin(j * DTOR);
                p[3].z = sradius * Math.cos(j * DTOR) * Math.sin((i + 5) * DTOR);
                n[3] = p[3];

                gl.glBegin(GL.GL_POLYGON);
                if (i % 20 == 0)
                    gl.glColor3f(1.0f, 1.0f, 1.0f);
                else
                    gl.glColor3f(0.5f, 0.5f, 0.5f);
                for (k = 0; k < 4; k++) {
                    gl.glNormal3f((float) n[k].x, (float) n[k].y, (float) n[k].z);
                    gl.glVertex3f((float) p[k].x, (float) p[k].y, (float) p[k].z);
                }
                gl.glEnd();
            }
        }

        /* Draw the cones */
        for (j = -1; j <= 1; j += 2) {
            for (i = 0; i < 360; i += 10) {

                p[0] = origin;
                n[0] = p[0];
                n[0].y = -1;

                p[1].x = cradius * Math.cos(i * DTOR);
                p[1].y = j * clength;
                p[1].z = cradius * Math.sin(i * DTOR);
                n[1] = p[1];
                n[1].y = 0;

                p[2].x = cradius * Math.cos((i + 10) * DTOR);
                p[2].y = j * clength;
                p[2].z = cradius * Math.sin((i + 10) * DTOR);
                n[2] = p[2];
                n[2].y = 0;

                gl.glBegin(GL.GL_POLYGON);
                if (i % 30 == 0)
                    gl.glColor3f(0.2f, 0.2f, 0.2f);
                else
                    gl.glColor3f(0.5f, 0.5f, 0.5f);
                for (k = 0; k < 3; k++) {
                    gl.glNormal3f((float) n[k].x, (float) n[k].y, (float) n[k].z);
                    gl.glVertex3f((float) p[k].x, (float) p[k].y, (float) p[k].z);
                }
                gl.glEnd();
            }
        }

        /* Draw the field lines */
        for (j = 0; j < 360; j += 20) {
            gl.glPushMatrix();
            gl.glRotated((double) j, 0.0f, 1.0f, 0.0f);
            gl.glBegin(GL.GL_LINE_STRIP);
            gl.glColor3f(0.7f, 0.7f, 0.7f);
            for (i = -140; i < 140; i++) {
                x = r1 + r1 * Math.cos(i * DTOR);
                y = r2 * Math.sin(i * DTOR);
                z = 0;
                gl.glVertex3d(x, y, z);
            }
            gl.glEnd();
            gl.glPopMatrix();
        }

        gl.glPopMatrix(); /* Pulsar axis rotation */
        gl.glPopMatrix(); /* Pulsar spin */
        rotateangle += rotatespeed;
    }

    /**
     * Set up the lighting environment
     */
    void makeLighting(GL gl) {
        float[] fullambient = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] position = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
        float[] ambient = new float[] { 0.2f, 0.2f, 0.2f, 1.0f };
        float[] diffuse = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] specular = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };

        /* Turn off all the lights */
        gl.glDisable(GL.GL_LIGHT0);
        gl.glDisable(GL.GL_LIGHT1);
        gl.glDisable(GL.GL_LIGHT2);
        gl.glDisable(GL.GL_LIGHT3);
        gl.glDisable(GL.GL_LIGHT4);
        gl.glDisable(GL.GL_LIGHT5);
        gl.glDisable(GL.GL_LIGHT6);
        gl.glDisable(GL.GL_LIGHT7);
        gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, GL.GL_TRUE);
        gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_FALSE);

        /* Turn on the appropriate lights */
        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, fullambient, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0);
        gl.glEnable(GL.GL_LIGHT0);

        /* Sort out the shading algorithm */
        gl.glShadeModel(GL.GL_SMOOTH);

        /* Turn lighting on */
        gl.glEnable(GL.GL_LIGHTING);
    }

    /*
     * Rotate (ix,iy) or roll (iz) the camera about the focal point ix,iy,iz are flags, 0 do
     * nothing, +- 1 rotates in opposite directions Correctly updating all camera attributes
     */
    void rotateCamera(int ix, int iy, int iz) {
        Point3d vp = new Point3d();
        Point3d vu = new Point3d();
        Point3d vd = new Point3d();
        Point3d right = new Point3d();
        Point3d newvp = new Point3d();
        Point3d newr = new Point3d();
        double radius, dd, radians;
        double dx, dy, dz;

        vu = camera.vu;
        normalise(vu);
        vp = camera.vp;
        vd = camera.vd;
        normalise(vd);
        crossProduct(vd, vu, right);
        normalise(right);
        radians = dtheta * Math.PI / 180.0;

        /* Handle the roll */
        if (iz != 0) {
            camera.vu.x += iz * right.x * radians;
            camera.vu.y += iz * right.y * radians;
            camera.vu.z += iz * right.z * radians;
            normalise(this.camera.vu);
            return;
        }

        /* Distance from the rotate point */
        dx = camera.vp.x - camera.pr.x;
        dy = camera.vp.y - camera.pr.y;
        dz = camera.vp.z - camera.pr.z;
        radius = Math.sqrt(dx * dx + dy * dy + dz * dz);

        /* Determine the new view point */
        dd = radius * radians;
        newvp.x = vp.x + dd * ix * right.x + dd * iy * vu.x - camera.pr.x;
        newvp.y = vp.y + dd * ix * right.y + dd * iy * vu.y - camera.pr.y;
        newvp.z = vp.z + dd * ix * right.z + dd * iy * vu.z - camera.pr.z;
        normalise(newvp);
        camera.vp.x = camera.pr.x + radius * newvp.x;
        camera.vp.y = camera.pr.y + radius * newvp.y;
        camera.vp.z = camera.pr.z + radius * newvp.z;

        /* Determine the new right vector */
        newr.x = camera.vp.x + right.x - camera.pr.x;
        newr.y = camera.vp.y + right.y - camera.pr.y;
        newr.z = camera.vp.z + right.z - camera.pr.z;
        normalise(newr);
        newr.x = camera.pr.x + radius * newr.x - camera.vp.x;
        newr.y = camera.pr.y + radius * newr.y - camera.vp.y;
        newr.z = camera.pr.z + radius * newr.z - camera.vp.z;

        camera.vd.x = camera.pr.x - camera.vp.x;
        camera.vd.y = camera.pr.y - camera.vp.y;
        camera.vd.z = camera.pr.z - camera.vp.z;
        normalise(this.camera.vd);

        /* Determine the new up vector */
        crossProduct(newr, camera.vd, camera.vu);
        normalise(this.camera.vu);
    }

    private void crossProduct(Point3d p1, Point3d p2, Point3d p3) {
        p3.x = p1.y * p2.z - p1.z * p2.y;
        p3.y = p1.z * p2.x - p1.x * p2.z;
        p3.z = p1.x * p2.y - p1.y * p2.x;
    }

    /**
     * Translate (pan) the camera view point In response to i,j,k,l keys Also move the camera rotate
     * location in parallel
     */
    private void translateCamera(int ix, int iy) {
//        Point3d vp = new Point3d();
        Point3d vu = new Point3d();
        Point3d vd = new Point3d();
        Point3d right = new Point3d();

        // double radians;
        double delta;

        vu = camera.vu;
        normalise(vu);
//        vp = camera.vp;
        vd = camera.vd;
        normalise(vd);

        crossProduct(vd, vu, right);
        normalise(right);
        // radians = dtheta * Math.PI / 180.0;
        delta = dtheta * camera.focallength / 90.0;

        camera.vp.x += iy * vu.x * delta;
        camera.vp.y += iy * vu.y * delta;
        camera.vp.z += iy * vu.z * delta;
        camera.pr.x += iy * vu.x * delta;
        camera.pr.y += iy * vu.y * delta;
        camera.pr.z += iy * vu.z * delta;

        camera.vp.x += ix * right.x * delta;
        camera.vp.y += ix * right.y * delta;
        camera.vp.z += ix * right.z * delta;
        camera.pr.x += ix * right.x * delta;
        camera.pr.y += ix * right.y * delta;
        camera.pr.z += ix * right.z * delta;
    }

    /*
     * Move the camera to the home position Or to a predefined stereo configuration The model is
     * assumed to be in a 10x10x10 cube Centered at the origin
     */
    private void cameraHome(int mode) {
        camera.aperture = 60;
        camera.pr = origin;

        camera.vd.x = 1;
        camera.vd.y = 0;
        camera.vd.z = 0;

        camera.vu.x = 0;
        camera.vu.y = 1;
        camera.vu.z = 0;

        camera.vp.x = -10;
        camera.vp.y = 0;
        camera.vp.z = 0;

        switch (mode) {
        case 0:
        case 2:
        case 4:
            camera.focallength = 10;
            break;
        case 1:
            camera.focallength = 5;
            break;
        case 3:
            camera.focallength = 15;
            break;
        }

        /* Non stressful stereo setting */
        camera.eyesep = camera.focallength / 30.0;
        if (mode == 4)
            camera.eyesep = 0;
    }

    /**
     * Normalise a vector
     */
    private void normalise(Point3d p) {
        double length;

        length = Math.sqrt(p.x * p.x + p.y * p.y + p.z * p.z);
        if (length != 0) {
            p.x /= length;
            p.y /= length;
            p.z /= length;
        } else {
            p.x = 0;
            p.y = 0;
            p.z = 0;
        }
    }

    /**
     * Calculate the unit normal at p given two other points p1,p2 on the surface. The normal points
     * in the direction of p1 crossproduct p2
     * 
     * @param p
     * @param p1
     * @param p2
     * @return
     */
    private Point3d calculateNormal(Point3d p, Point3d p1, Point3d p2) {
        Point3d n = new Point3d();
        Point3d pa = new Point3d();
        Point3d pb = new Point3d();

        pa.x = p1.x - p.x;
        pa.y = p1.y - p.y;
        pa.z = p1.z - p.z;
        pb.x = p2.x - p.x;
        pb.y = p2.y - p.y;
        pb.z = p2.z - p.z;
        normalise(pa);
        normalise(pb);

        n.x = pa.y * pb.z - pa.z * pb.y;
        n.y = pa.z * pb.x - pa.x * pb.z;
        n.z = pa.x * pb.y - pa.y * pb.x;
        normalise(n);

        return (n);
    }

    public void setCameraMode(int mode) {
        cameraHome(mode);
    }

    public void setSpeed(int speed) {
        this.rotatespeed = (speed - 1) / 2.0;
    }

    public void nextGlassesType () {
        this.glassestype++;
        if ( this.glassestype > 6 ) {
            this.glassestype = REDBLUE;
        }
        this.repaint();
    }
    
    public void setGlassesType(int type) {
        this.glassestype = type;
    }

    public void nextModelType () {
        this.modeltype++;
        if ( this.modeltype > 7 ) {
            this.modeltype = MESH;
        }
        this.repaint();
    }
    
    public void setModelType(int type) {
        this.modeltype = type;
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    private Point3d[] createPoint3dArray(int size) {
        Point3d[] points = new Point3d[size];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point3d();
        }
        return points;
    }

    // ----------------------------------------------------------------------
    // Runnable methods
    // ----------------------------------------------------------------------

    public void run() {

        this.repaint();
        try {
            Thread.sleep(30);
        } catch (InterruptedException ex) {

        }
    }

    // ----------------------------------------------------------------------
    // KeyListener methods
    // ----------------------------------------------------------------------

    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        switch (key) {
        case 'Q':
        case 'q':
            System.exit(0);
            break;
        case 'h': /* Go home */
        case 'H':
            cameraHome(0);
            break;
        // case 'w': /* Save one image */
        // case 'W':
        // windowdump = TRUE;
        // break;
        case 'r': /* Toggle image recording */
            // case 'R':
            // movierecord = !movierecord;
            // break;
        case '[': /* Roll anti clockwise */
            rotateCamera(0, 0, -1);
            break;
        case ']': /* Roll clockwise */
            rotateCamera(0, 0, 1);
            break;
        case 'i': /* Translate camera up */
        case 'I':
            translateCamera(0, 1);
            break;
        case 'k': /* Translate camera down */
        case 'K':
            translateCamera(0, -1);
            break;
        case 'j': /* Translate camera left */
        case 'J':
            translateCamera(-1, 0);
            break;
        case 'l': /* Translate camera right */
        case 'L':
            translateCamera(1, 0);
            break;
        case 's':
        case 'S':
            nextModelType();
            break;
        case 'c':
        case 'C':
            nextGlassesType();            
        }

        switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
            System.exit(0);
            break;
        case KeyEvent.VK_LEFT:
            rotateCamera(-1, 0, 0);
            break;
        case KeyEvent.VK_RIGHT:
            rotateCamera(1, 0, 0);
            break;
        case KeyEvent.VK_UP:
            rotateCamera(0, 1, 0);
            break;
        case KeyEvent.VK_DOWN:
            rotateCamera(0, -1, 0);
            break;
        }

        this.repaint();
    }

    public void keyReleased(KeyEvent e) {
        // not implemented
    }

    public void keyTyped(KeyEvent e) {
        // not implemented
    }

    // ----------------------------------------------------------------------
    // MouseMotionListener methods
    // ----------------------------------------------------------------------
    
    public void mouseDragged(MouseEvent e) {
        int dx,dy;

        dx = e.getX() - xlast;
        dy = e.getY() - ylast;
        if (dx < 0)      dx = -1;
        else if (dx > 0) dx =  1;
        if (dy < 0)      dy = -1;
        else if (dy > 0) dy =  1;

        if (e.getButton() == MouseEvent.BUTTON1)
           rotateCamera(-dx,dy,0);
        else if (e.getButton() == MouseEvent.BUTTON2)
           rotateCamera(0,0,dx);

        xlast = e.getX();
        ylast = e.getY(); 
        
        this.repaint();
    }

    public void mouseMoved(MouseEvent e) {
        // not implemented
    }     
    
    // ----------------------------------------------------------------------
    // Inner classes
    // ----------------------------------------------------------------------

    private static class Camera {
        Point3d vp = new Point3d(); /* View position */
        Point3d vd = new Point3d(); /* View direction vector */
        Point3d vu = new Point3d(); /* View up direction */
        Point3d pr = new Point3d(); /* Point to rotate about */
        double focallength; /* Focal Length along vd */
        double aperture; /* Camera aperture */
        double eyesep; /* Eye separation */
        int screenheight, screenwidth;
    }

    public static void main(String[] args) {
        
        Anaglyph anaglyph = new Anaglyph();
        JFrame frame = new JFrame("Anaglyph");
        frame.add(anaglyph);

        frame.setBounds(100, 100, 500, 300);
        frame.setVisible(true);

        anaglyph.requestFocus();
                
    }
}


