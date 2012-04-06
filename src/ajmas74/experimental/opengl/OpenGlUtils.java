package ajmas74.experimental.opengl;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.vecmath.Point3d;

/**
 * Collected methods useful for OpenGL.
 * 
 * @author ajmas
 * 
 */
public class OpenGlUtils {

    /**
     * This draw a circle outline on the x/y plane, located at the given z
     * position.
     * 
     * @param gl
     * @param radius
     * @param x
     * @param y
     * @param z
     */
    public static void drawCircle(GL gl, float radius, float x, float y, float z) {

        drawEllipse(gl, radius, radius, x, y, z);
    }

    /**
     * This draw a circle outline on the x/y plane, located at the given z
     * position.
     * 
     * @param gl
     * @param xRadius
     * @param yRadius
     * @param x
     * @param y
     * @param z
     */
    public static void drawEllipse(GL gl, double xRadius, double yRadius,
            float x, float y, float z) {

        gl.glBegin(GL.GL_LINE_LOOP);

        for (int i = 0; i < 360; i++) {
            float angle = (float) Math.toRadians(i);
            gl.glVertex3d(x + Math.cos(angle) * xRadius, y + Math.sin(angle)
                    * yRadius, z);
        }

        gl.glEnd();

    }

    /*
     * Create a sphere centered at c, with radius r, and precision n Draw a
     * point for zero radius spheres
     * 
     * from: http://local.wasp.uwa.edu.au/~pbourke/texture_colour/spheremap/
     */
    public static void drawSphere(GL gl, Point3d c, double r, int n) {
        int i, j;
        double theta1, theta2, theta3;
        Point3d e = new Point3d();
        Point3d p = new Point3d();

        if (c == null) {
            c = new Point3d(0, 0, 0);
        }

        double twoPi = Math.PI * 2;
        double piD2 = Math.PI / 2;
        if (r < 0)
            r = -r;
        if (n < 0)
            n = -n;
        if (n < 4 || r <= 0) {
            gl.glBegin(GL.GL_POINTS);
            gl.glVertex3d(c.x, c.y, c.z);
            gl.glEnd();
            return;
        }

        for (j = 0; j < n / 2; j++) {
            theta1 = j * twoPi / n - piD2;
            theta2 = (j + 1) * twoPi / n - piD2;

            gl.glBegin(GL.GL_QUAD_STRIP);
//            gl.glBegin(GL.GL_TRIANGLE_STRIP);
            for (i = 0; i <= n; i++) {
                theta3 = i * twoPi / n;

                e.x = Math.cos(theta2) * Math.cos(theta3);
                e.y = Math.sin(theta2);
                e.z = Math.cos(theta2) * Math.sin(theta3);
                p.x = c.x + r * e.x;
                p.y = c.y + r * e.y;
                p.z = c.z + r * e.z;

                gl.glNormal3d(e.x, e.y, e.z);
                gl.glTexCoord2d(i / (double) n, 2 * (j + 1) / (double) n);
                gl.glVertex3d(p.x, p.y, p.z);

                e.x = Math.cos(theta1) * Math.cos(theta3);
                e.y = Math.sin(theta1);
                e.z = Math.cos(theta1) * Math.sin(theta3);
                p.x = c.x + r * e.x;
                p.y = c.y + r * e.y;
                p.z = c.z + r * e.z;

                gl.glNormal3d(e.x, e.y, e.z);
                gl.glTexCoord2d(i / (double) n, 2 * j / (double) n);
                gl.glVertex3d(p.x, p.y, p.z);
            }
            gl.glEnd();
        }
    }

    /*
     * Create a sphere centered at c, with radius r, and precision n Draw a
     * point for zero radius spheres Use CCW facet ordering "method" is 0 for
     * quads, 1 for triangles (quads look nicer in wireframe mode) Partial
     * spheres can be created using theta1->theta2, phi1->phi2 in radians 0 <
     * theta < 2pi, -pi/2 < phi < pi/2
     * 
     * from: http://local.wasp.uwa.edu.au/~pbourke/texture_colour/spheremap/
     */
    public static void drawSphere(GL gl, Point3d c, double r, int n, int method,
            double theta1, double theta2, double phi1, double phi2) {
        int i, j;
        double t1, t2, t3;
        Point3d e = new Point3d();
        Point3d p = new Point3d();

        if (c == null) {
            c = new Point3d(0, 0, 0);
        }

        /* Handle special cases */
        if (r < 0)
            r = -r;
        if (n < 0)
            n = -n;
        if (n < 4 || r <= 0) {
            gl.glBegin(GL.GL_POINTS);
            gl.glVertex3d(c.x, c.y, c.z);
            gl.glEnd();
            return;
        }

        for (j = 0; j < n / 2; j++) {
            t1 = phi1 + j * (phi2 - phi1) / (n / 2);
            t2 = phi1 + (j + 1) * (phi2 - phi1) / (n / 2);

            if (method == 0)
                gl.glBegin(GL.GL_QUAD_STRIP);
            else
                gl.glBegin(GL.GL_TRIANGLE_STRIP);

            for (i = 0; i <= n; i++) {
                t3 = theta1 + i * (theta2 - theta1) / n;

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

    /**
     * Method to draw a sphere in OpenGL.
     * 
     * Source taken from: http://ozark.hendrix.edu/~burch/cs/490/sched/feb8/
     * 
     * @param gl
     * @param radius
     * @param lats
     *            number of sub-divisions along the latitude
     * @param longs
     *            number of sub-divisions along the longitude
     */
    static public void drawSphere(GL gl, double radius, int lats, int longs) {
        /*
         * This algorithm moves along the z-axis, for PI radians and then at
         * each point rotates around the z-axis drawing a QUAD_STRIP.
         * 
         * If you you start i at a non-zero value then you will see an open end
         * along the z-axis. If you start j at a none zero-axis then you will
         * see segements taken out.
         */

        for (int i = 0; i <= lats; i++) {
            double lat0 = Math.PI * (-0.5 + (double) (i - 1) / lats);
            double z0 = Math.sin(lat0) * radius;
            double zr0 = Math.cos(lat0) * radius;

            double lat1 = Math.PI * (-0.5 + (double) i / lats);
            double z1 = Math.sin(lat1) * radius;
            double zr1 = Math.cos(lat1) * radius;

            gl.glBegin(GL.GL_QUAD_STRIP);
            for (int j = 0; j <= longs; j++) {
                float pc1 = j / longs;
                float pc2 = j + 1 / longs;

                double lng = 2 * Math.PI * (double) (j - 1) / longs;
                double x = Math.cos(lng) * radius;
                double y = Math.sin(lng) * radius;

                gl.glNormal3d(x * zr0, y * zr0, z0);
                gl.glVertex3d(x * zr0, y * zr0, z0);

                gl.glNormal3d(x * zr1, y * zr1, z1);
                gl.glVertex3d(x * zr1, y * zr1, z1);
            }
            gl.glEnd();
        }
    }

    public static void drawHoop(GL gl, double hoopRadius, double tubeRadius) {

        int segmentsA = 100;
        int segmentsB = 100;

        for (int i = 50; i <= segmentsA; i++) {
            double angleA1 = 2 * Math.PI
                    * (-0.5 + (double) (i - 1) / segmentsA);
            double x0 = Math.sin(angleA1) * hoopRadius;
            double y0 = Math.cos(angleA1) * hoopRadius;

            double angleA2 = 2 * Math.PI * (-0.5 + (double) i / segmentsA);
            double x1 = Math.sin(angleA2) * hoopRadius;
            double y1 = Math.cos(angleA2) * hoopRadius;

            gl.glBegin(GL.GL_QUAD_STRIP);
            for (int j = 0; j <= segmentsB; j++) {
                double angleB = 2 * Math.PI * (double) (j - 1) / segmentsB;
                double x = Math.cos(angleB) * tubeRadius;
                double y = Math.sin(angleB) * tubeRadius;

                gl.glNormal3d(x0 + x, y0, y0 * y);
                gl.glVertex3d(x0 + x, y0, y0 * y);
                gl.glNormal3d(x1 + x, y1, y1 * y);
                gl.glVertex3d(x1 + x, y1, y1 * y);
            }
            gl.glEnd();
        }
    }

    /**
     * Method to draw a sphere in OpenGL.
     * 
     * Source taken from: http://ozark.hendrix.edu/~burch/cs/490/sched/feb8/
     * 
     * @param gl
     * @param radius
     * @param lats
     * @param longs
     */
    static public void drawEllipsoid(GL gl, double radiusH, double radiusV,
            float lats, float longs) {
        for (int i = 0; i <= lats; i++) {
            double lat0 = Math.PI * (-0.5 + (double) (i - 1) / lats);
            double z0 = Math.sin(lat0) * radiusH;
            double zr0 = Math.cos(lat0) * radiusV;

            double lat1 = Math.PI * (-0.5 + (double) i / lats);
            double z1 = Math.sin(lat1) * radiusH;
            double zr1 = Math.cos(lat1) * radiusV;

            gl.glBegin(GL.GL_QUAD_STRIP);
            for (int j = 0; j <= longs; j++) {
                double lng = 2 * Math.PI * (double) (j - 1) / longs;
                double x = Math.cos(lng) * radiusH;
                double y = Math.sin(lng) * radiusV;

                gl.glNormal3d(x * zr0, y * zr0, z0);
                gl.glVertex3d(x * zr0, y * zr0, z0);
                gl.glNormal3d(x * zr1, y * zr1, z1);
                gl.glVertex3d(x * zr1, y * zr1, z1);
            }
            gl.glEnd();
        }
    }

    public static void applyColor ( GL gl, Color c ) {
//        gl.glColor3i(c.getRed(), c.getGreen(), c.getBlue());//, c.getAlpha());
//        gl.glColor3f(1.0f,1.0f,1.0f);
        gl.glColor4f(c.getRed()/255.0f, c.getGreen()/255.0f, c.getBlue()/255.0f,c.getAlpha()/255.0f);
    }
    
    public static void drawRectangle(GL gl, float x, float y, float width, float height, int textureId) {
        
        float front = 0.0f;
        
        if ( textureId > -1 ) {
            gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
        }
        
        gl.glBegin(GL.GL_QUADS);
        
        gl.glNormal3f(0.0f, 0.0f, -1.0f);

        gl.glTexCoord2d(0.0,0.0);
        gl.glVertex3f(x, y, front);
        
        gl.glTexCoord2d(1.0,0.0);
        gl.glVertex3f(x+width, y, front);
        
        gl.glTexCoord2d(1.0,1.0);
        gl.glVertex3f(x+width, y+height, front);
        
        gl.glTexCoord2d(0.0,1.0);
        gl.glVertex3f(x, y+height, front);
        
        gl.glEnd();
    }
    
    /**
     * This draws a cube, with extents being defined by the paramter values.
     * Note that the front is considered to be the face that is facing towards
     * the negative z values. This is important to note, as it will affect the
     * normals.
     * 
     * The normals are all facing the outside of the cube.
     * 
     * @param gl
     * @param width
     * @param height
     * @param length
     * @param colors an array of 6 elements representing the colours of the sides
     */
    public static void drawCube(GL gl, float left, float right, float top,
            float bottom, float front, float back, Color[] colors) {

        // Front

        gl.glBegin(GL.GL_QUADS);

        applyColor(gl,colors[0]);

        gl.glNormal3f(0.0f, 0.0f, -1.0f);

        gl.glVertex3f(left, bottom, front);
        gl.glVertex3f(right, bottom, front);
        gl.glVertex3f(right, top, front);
        gl.glVertex3f(left, top, front);

        // Back

        applyColor(gl,colors[1]);
        
        gl.glNormal3f(0.0f, 0.0f, 1.0f);

        gl.glVertex3f(left, bottom, back);
        gl.glVertex3f(right, bottom, back);
        gl.glVertex3f(right, top, back);
        gl.glVertex3f(left, top, back);

        // Left

        applyColor(gl,colors[2]);
        
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);

        gl.glVertex3f(left, top, front);
        gl.glVertex3f(left, top, back);
        gl.glVertex3f(left, bottom, back);
        gl.glVertex3f(left, bottom, front);

        // Right

        applyColor(gl,colors[3]);
        
        gl.glNormal3f(1.0f, 0.0f, -0.0f);

        gl.glVertex3f(right, top, front);
        gl.glVertex3f(right, top, back);
        gl.glVertex3f(right, bottom, back);
        gl.glVertex3f(right, bottom, front);

        // Top

        applyColor(gl,colors[4]);
        
        gl.glNormal3f(0.0f, 1.0f, -0.0f);

        gl.glVertex3f(left, top, front);
        gl.glVertex3f(right, top, front);
        gl.glVertex3f(right, top, back);
        gl.glVertex3f(left, top, back);

        // Bottom

        applyColor(gl,colors[5]);
        
        gl.glNormal3f(0.0f, -1.0f, 0.0f);

        gl.glVertex3f(left, bottom, front);
        gl.glVertex3f(right, bottom, front);
        gl.glVertex3f(right, bottom, back);
        gl.glVertex3f(left, bottom, back);

        gl.glEnd();

    }
    
    /**
     * This draws a cube, with extents being defined by the paramter values.
     * Note that the front is considered to be the face that is facing towards
     * the negative z values. This is important to note, as it will affect the
     * normals.
     * 
     * The normals are all facing the outside of the cube.
     * 
     * @param gl
     * @param width
     * @param height
     * @param length
     */
    public static void drawCube(GL gl, float left, float right, float top,
            float bottom, float front, float back) {

        // Front

        gl.glBegin(GL.GL_QUADS);

        gl.glColor3f(0.5f, 0.2f, 0.2f);

        gl.glNormal3f(0.0f, 0.0f, -1.0f);

        gl.glVertex3f(left, bottom, front);
        gl.glVertex3f(right, bottom, front);
        gl.glVertex3f(right, top, front);
        gl.glVertex3f(left, top, front);

        // Back

        gl.glNormal3f(0.0f, 0.0f, 1.0f);

        gl.glVertex3f(left, bottom, back);
        gl.glVertex3f(right, bottom, back);
        gl.glVertex3f(right, top, back);
        gl.glVertex3f(left, top, back);

        // Left

        gl.glNormal3f(-1.0f, 0.0f, 0.0f);

        gl.glVertex3f(left, top, front);
        gl.glVertex3f(left, top, back);
        gl.glVertex3f(left, bottom, back);
        gl.glVertex3f(left, bottom, front);

        // Right

        gl.glNormal3f(1.0f, 0.0f, -0.0f);

        gl.glVertex3f(right, top, front);
        gl.glVertex3f(right, top, back);
        gl.glVertex3f(right, bottom, back);
        gl.glVertex3f(right, bottom, front);

        // Top

        gl.glNormal3f(0.0f, 1.0f, -0.0f);

        gl.glVertex3f(left, top, front);
        gl.glVertex3f(right, top, front);
        gl.glVertex3f(right, top, back);
        gl.glVertex3f(left, top, back);

        // Bottom

        gl.glNormal3f(0.0f, -1.0f, 0.0f);

        gl.glVertex3f(left, bottom, front);
        gl.glVertex3f(right, bottom, front);
        gl.glVertex3f(right, bottom, back);
        gl.glVertex3f(left, bottom, back);

        gl.glEnd();

    }

    /**
     * This draws a cube of the given dimensions, centered around the point
     * (0,0,0). The normals are all facing the outside of the cube.
     * 
     * @param gl
     * @param width
     * @param height
     * @param length
     */
    public static void drawCube(GL gl, float width, float height, float length) {

        float left = (width / 2.0f) * -1;
        float right = (width / 2.0f);

        float top = (height / 2.0f);
        float bottom = (height / 2.0f) * -1;

        float front = (length / 2.0f) * -1;
        float back = (length / 2.0f);

        drawCube(gl, left, right, top, bottom, front, back);
    }

    /*
     * ---------------------------------------------------------------------------------------
     * Methods used for drawing torus Orginally from:
     * http://math.ucsd.edu/~sbuss/MathCG/OpenGLsoft/WrapTorus/WrapTorus.html
     * ---------------------------------------------------------------------------------------
     */

    /**
     * Issue vertex command for segment number j of wrap number i
     */
    private static void putVertex(GL gl, double majorRadius,
            double minorRadius, int numWraps, int numPerWrap, double i, double j) {
        double wrapFrac = j / (float) numPerWrap;
        double phi = Math.PI * 2.0 * wrapFrac;
        double theta = Math.PI * 2.0 * (i + wrapFrac) / (double) numWraps;

        double sinphi = Math.sin(phi);
        double cosphi = Math.cos(phi);
        double sintheta = Math.sin(theta);
        double costheta = Math.cos(theta);

        float y = (float) (minorRadius * (float) Math.sin(phi));
        double r = majorRadius + minorRadius * Math.cos(phi);
        float x = (float) (Math.sin(theta) * r);
        float z = (float) (Math.cos(theta) * r);

        gl.glNormal3d(sintheta * cosphi, sinphi, costheta * cosphi);
        gl.glVertex3f(x, y, z);

    }

    /**
     * Draw a torus, using a helix as the guiding point. This uses
     * GL_QUAD_STRIP, numWraps=20 and numPerWrap=8
     * 
     * @see OpenGlUtils#drawTorus(GL, double, double, int, int, boolean)
     * 
     * @param gl
     * @param majorRadius
     *            the major radius
     * @param minorRadius
     *            the minor radius
     */
    public static void drawTorus(GL gl, double majorRadius, double minorRadius) {
        boolean quadMode = true;

        int numWraps = 20;
        int numPerWrap = 8;

        drawTorus(gl, majorRadius, minorRadius, numWraps, numPerWrap, quadMode);
    }

    /**
     * Draw a torus, using a helix as the guiding point.
     * 
     * @param gl
     * @param majorRadius
     *            the major radius
     * @param minorRadius
     *            the minor radius
     * @param numWraps
     *            frequency of rotation
     * @param numPerWrap
     *            segments in 'ribbon'
     * @param quadMode
     *            whether to use GL_QUAD_STRIP (true) or GL_TRIANGLE_STRIP
     *            (false)
     */
    public static void drawTorus(GL gl, double majorRadius, double minorRadius,
            int numWraps, int numPerWrap, boolean quadMode) {

        if (quadMode) {
            gl.glBegin(GL.GL_QUAD_STRIP);
        } else {
            gl.glBegin(GL.GL_TRIANGLE_STRIP);
        }

        for (int i = 0; i < numWraps; i++) {
            for (int j = 0; j < numPerWrap; j++) {
                putVertex(gl, majorRadius, minorRadius, numWraps, numPerWrap,
                        i, j);
                putVertex(gl, majorRadius, minorRadius, numWraps, numPerWrap,
                        (i + 1), j);
            }
        }
        putVertex(gl, majorRadius, minorRadius, numWraps, numPerWrap, 0.0, 0.0);
        putVertex(gl, majorRadius, minorRadius, numWraps, numPerWrap, 1.0, 0.0);
        gl.glEnd();

    }

}
