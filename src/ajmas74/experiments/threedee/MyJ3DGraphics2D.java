package ajmas74.experiments.threedee;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class MyJ3DGraphics2D extends Graphics2D {
  
  Graphics2D _graphics;
  
	/**
	 * Constructor for MyJ3DGraphics2D.
	 */
	public MyJ3DGraphics2D( Graphics2D graphics ) {
		//super();
    _graphics = graphics;
	}

	/**
	 * @see java.awt.Graphics2D#draw(Shape)
	 */
	public void draw(Shape s) {
    _graphics.draw(s);
	}

	/**
	 * @see java.awt.Graphics2D#drawImage(Image, AffineTransform, ImageObserver)
	 */
	public boolean drawImage(
		Image img,
		AffineTransform xform,
		ImageObserver obs) {
		return _graphics.drawImage(img,xform,obs);
	}

	/**
	 * @see java.awt.Graphics2D#drawImage(BufferedImage, BufferedImageOp, int, int)
	 */
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
    _graphics.drawImage(img,op,x,y);
	}

	/**
	 * @see java.awt.Graphics2D#drawRenderedImage(RenderedImage, AffineTransform)
	 */
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
    _graphics.drawRenderedImage(img,xform);
	}

	/**
	 * @see java.awt.Graphics2D#drawRenderableImage(RenderableImage, AffineTransform)
	 */
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
    _graphics.drawRenderableImage(img,xform);
	}

	/**
	 * @see java.awt.Graphics#drawString(String, int, int)
	 */
	public void drawString(String str, int x, int y) {
    _graphics.drawString(str,x,y);
	}

	/**
	 * @see java.awt.Graphics2D#drawString(String, float, float)
	 */
	public void drawString(String s, float x, float y) {
    _graphics.drawString(s,x,y);
	}

	/**
	 * @see java.awt.Graphics#drawString(AttributedCharacterIterator, int, int)
	 */
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
    _graphics.drawString(iterator,x,y);
	}

	/**
	 * @see java.awt.Graphics2D#drawString(AttributedCharacterIterator, float, float)
	 */
	public void drawString(
		AttributedCharacterIterator iterator,
		float x,
		float y) {
    _graphics.drawString(iterator,x,y);
	}

	/**
	 * @see java.awt.Graphics2D#drawGlyphVector(GlyphVector, float, float)
	 */
	public void drawGlyphVector(GlyphVector g, float x, float y) {
    _graphics.drawGlyphVector(g,x,y);
	}

	/**
	 * @see java.awt.Graphics2D#fill(Shape)
	 */
	public void fill(Shape s) {
    _graphics.fill(s);
	}

	/**
	 * @see java.awt.Graphics2D#hit(Rectangle, Shape, boolean)
	 */
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return _graphics.hit(rect,s,onStroke);
	}

	/**
	 * @see java.awt.Graphics2D#getDeviceConfiguration()
	 */
	public GraphicsConfiguration getDeviceConfiguration() {
		return _graphics.getDeviceConfiguration();
	}

	/**
	 * @see java.awt.Graphics2D#setComposite(Composite)
	 */
	public void setComposite(Composite comp) {
    _graphics.setComposite(comp);
	}

	/**
	 * @see java.awt.Graphics2D#setPaint(Paint)
	 */
	public void setPaint(Paint paint) {
    _graphics.setPaint(paint);
	}

	/**
	 * @see java.awt.Graphics2D#setStroke(Stroke)
	 */
	public void setStroke(Stroke s) {
    _graphics.setStroke(s);
	}

	/**
	 * @see java.awt.Graphics2D#setRenderingHint(Key, Object)
	 */
	public void setRenderingHint(Key hintKey, Object hintValue) {
    _graphics.setRenderingHint(hintKey,hintValue);
	}

	/**
	 * @see java.awt.Graphics2D#getRenderingHint(Key)
	 */
	public Object getRenderingHint(Key hintKey) {
		return getRenderingHint(hintKey);
	}

	/**
	 * @see java.awt.Graphics2D#setRenderingHints(Map)
	 */
	public void setRenderingHints(Map hints) {
    _graphics.setRenderingHints(hints);
	}

	/**
	 * @see java.awt.Graphics2D#addRenderingHints(Map)
	 */
	public void addRenderingHints(Map hints) {
    _graphics.addRenderingHints(hints);
	}

	/**
	 * @see java.awt.Graphics2D#getRenderingHints()
	 */
	public RenderingHints getRenderingHints() {
		return _graphics.getRenderingHints();
	}

	/**
	 * @see java.awt.Graphics#translate(int, int)
	 */
	public void translate(int x, int y) {
    _graphics.translate(x,y);
	}

	/**
	 * @see java.awt.Graphics2D#translate(double, double)
	 */
	public void translate(double tx, double ty) {
    _graphics.translate(tx,ty);
	}

	/**
	 * @see java.awt.Graphics2D#rotate(double)
	 */
	public void rotate(double theta) {
    _graphics.rotate(theta);
	}

	/**
	 * @see java.awt.Graphics2D#rotate(double, double, double)
	 */
	public void rotate(double theta, double x, double y) {
    _graphics.rotate(theta,x,y);
	}

	/**
	 * @see java.awt.Graphics2D#scale(double, double)
	 */
	public void scale(double sx, double sy) {
    _graphics.scale(sx,sy);
	}

	/**
	 * @see java.awt.Graphics2D#shear(double, double)
	 */
	public void shear(double shx, double shy) {
    _graphics.shear(shx,shy);
	}

	/**
	 * @see java.awt.Graphics2D#transform(AffineTransform)
	 */
	public void transform(AffineTransform Tx) {
    _graphics.transform(Tx);
	}

	/**
	 * @see java.awt.Graphics2D#setTransform(AffineTransform)
	 */
	public void setTransform(AffineTransform Tx) {
    _graphics.setTransform(Tx);
	}

	/**
	 * @see java.awt.Graphics2D#getTransform()
	 */
	public AffineTransform getTransform() {
		return _graphics.getTransform();
	}

	/**
	 * @see java.awt.Graphics2D#getPaint()
	 */
	public Paint getPaint() {
		return _graphics.getPaint();
	}

	/**
	 * @see java.awt.Graphics2D#getComposite()
	 */
	public Composite getComposite() {
		return _graphics.getComposite();
	}

	/**
	 * @see java.awt.Graphics2D#setBackground(Color)
	 */
	public void setBackground(Color color) {
    _graphics.setBackground(color);
	}

	/**
	 * @see java.awt.Graphics2D#getBackground()
	 */
	public Color getBackground() {
		return _graphics.getBackground();
	}

	/**
	 * @see java.awt.Graphics2D#getStroke()
	 */
	public Stroke getStroke() {
		return _graphics.getStroke();
	}

	/**
	 * @see java.awt.Graphics2D#clip(Shape)
	 */
	public void clip(Shape s) {
    _graphics.clip(s);
	}

	/**
	 * @see java.awt.Graphics2D#getFontRenderContext()
	 */
	public FontRenderContext getFontRenderContext() {
		return _graphics.getFontRenderContext();
	}

	/**
	 * @see java.awt.Graphics#create()
	 */
	public Graphics create() {
		return this;
	}

	/**
	 * @see java.awt.Graphics#getColor()
	 */
	public Color getColor() {
		return _graphics.getColor();
	}

	/**
	 * @see java.awt.Graphics#setColor(Color)
	 */
	public void setColor(Color c) {
    _graphics.setColor(c);
	}

	/**
	 * @see java.awt.Graphics#setPaintMode()
	 */
	public void setPaintMode() {
    _graphics.setPaintMode();
	}

	/**
	 * @see java.awt.Graphics#setXORMode(Color)
	 */
	public void setXORMode(Color c1) {
    _graphics.setXORMode(c1);
	}

	/**
	 * @see java.awt.Graphics#getFont()
	 */
	public Font getFont() {
		return _graphics.getFont();
	}

	/**
	 * @see java.awt.Graphics#setFont(Font)
	 */
	public void setFont(Font font) {
    _graphics.setFont(font);
	}

	/**
	 * @see java.awt.Graphics#getFontMetrics(Font)
	 */
	public FontMetrics getFontMetrics(Font f) {
		return _graphics.getFontMetrics(f);
	}

	/**
	 * @see java.awt.Graphics#getClipBounds()
	 */
	public Rectangle getClipBounds() {
		return _graphics.getClipBounds();
	}

	/**
	 * @see java.awt.Graphics#clipRect(int, int, int, int)
	 */
	public void clipRect(int x, int y, int width, int height) {
    _graphics.clipRect(x,y,width,height);
	}

	/**
	 * @see java.awt.Graphics#setClip(int, int, int, int)
	 */
	public void setClip(int x, int y, int width, int height) {
    _graphics.setClip(x,y,width,height);
	}

	/**
	 * @see java.awt.Graphics#getClip()
	 */
	public Shape getClip() {
		return _graphics.getClip();
	}

	/**
	 * @see java.awt.Graphics#setClip(Shape)
	 */
	public void setClip(Shape clip) {
    _graphics.setClip(clip);
	}

	/**
	 * @see java.awt.Graphics#copyArea(int, int, int, int, int, int)
	 */
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
    _graphics.copyArea(x,y,width,height,dx,dy);
	}

	/**
	 * @see java.awt.Graphics#drawLine(int, int, int, int)
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
    _graphics.drawLine(x1,y1,x2,y2);
	}

	/**
	 * @see java.awt.Graphics#fillRect(int, int, int, int)
	 */
	public void fillRect(int x, int y, int width, int height) {
    _graphics.fillRect(x,y,width,height);
	}

	/**
	 * @see java.awt.Graphics#clearRect(int, int, int, int)
	 */
	public void clearRect(int x, int y, int width, int height) {
    _graphics.clearRect(x,y,width,height);
	}

	/**
	 * @see java.awt.Graphics#drawRoundRect(int, int, int, int, int, int)
	 */
	public void drawRoundRect(
		int x,
		int y,
		int width,
		int height,
		int arcWidth,
		int arcHeight) {
    _graphics.drawRoundRect(x,y,width,height,arcWidth,arcHeight);
	}

	/**
	 * @see java.awt.Graphics#fillRoundRect(int, int, int, int, int, int)
	 */
	public void fillRoundRect(
		int x,
		int y,
		int width,
		int height,
		int arcWidth,
		int arcHeight) {
    _graphics.fillRoundRect(x,y,width,height,arcWidth,arcHeight);
	}

	/**
	 * @see java.awt.Graphics#drawOval(int, int, int, int)
	 */
	public void drawOval(int x, int y, int width, int height) {
    _graphics.drawOval(x,y,width,height);
	}

	/**
	 * @see java.awt.Graphics#fillOval(int, int, int, int)
	 */
	public void fillOval(int x, int y, int width, int height) {
    _graphics.fillOval(x,y,width,height);
	}

	/**
	 * @see java.awt.Graphics#drawArc(int, int, int, int, int, int)
	 */
	public void drawArc(
		int x,
		int y,
		int width,
		int height,
		int startAngle,
		int arcAngle) {
    _graphics.drawArc(x,y,width,height,startAngle,arcAngle);
	}

	/**
	 * @see java.awt.Graphics#fillArc(int, int, int, int, int, int)
	 */
	public void fillArc(
		int x,
		int y,
		int width,
		int height,
		int startAngle,
		int arcAngle) {
    _graphics.fillArc(x,y,width,height,startAngle,arcAngle);
	}

	/**
	 * @see java.awt.Graphics#drawPolyline(int[], int[], int)
	 */
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
    _graphics.drawPolyline(xPoints,yPoints,nPoints);
	}

	/**
	 * @see java.awt.Graphics#drawPolygon(int[], int[], int)
	 */
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
    _graphics.drawPolygon(xPoints,yPoints,nPoints);
	}

	/**
	 * @see java.awt.Graphics#fillPolygon(int[], int[], int)
	 */
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
    _graphics.fillPolygon(xPoints,yPoints,nPoints);
	}

	/**
	 * @see java.awt.Graphics#drawImage(Image, int, int, ImageObserver)
	 */
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return false;
	}

	/**
	 * @see java.awt.Graphics#drawImage(Image, int, int, int, int, ImageObserver)
	 */
	public boolean drawImage(
		Image img,
		int x,
		int y,
		int width,
		int height,
		ImageObserver observer) {
		return false;
	}

	/**
	 * @see java.awt.Graphics#drawImage(Image, int, int, Color, ImageObserver)
	 */
	public boolean drawImage(
		Image img,
		int x,
		int y,
		Color bgcolor,
		ImageObserver observer) {
		return false;
	}

	/**
	 * @see java.awt.Graphics#drawImage(Image, int, int, int, int, Color, ImageObserver)
	 */
	public boolean drawImage(
		Image img,
		int x,
		int y,
		int width,
		int height,
		Color bgcolor,
		ImageObserver observer) {
		return false;
	}

	/**
	 * @see java.awt.Graphics#drawImage(Image, int, int, int, int, int, int, int, int, ImageObserver)
	 */
	public boolean drawImage(
		Image img,
		int dx1,
		int dy1,
		int dx2,
		int dy2,
		int sx1,
		int sy1,
		int sx2,
		int sy2,
		ImageObserver observer) {
		return false;
	}

	/**
	 * @see java.awt.Graphics#drawImage(Image, int, int, int, int, int, int, int, int, Color, ImageObserver)
	 */
	public boolean drawImage(
		Image img,
		int dx1,
		int dy1,
		int dx2,
		int dy2,
		int sx1,
		int sy1,
		int sx2,
		int sy2,
		Color bgcolor,
		ImageObserver observer) {
		return false;
	}

	/**
	 * @see java.awt.Graphics#dispose()
	 */
	public void dispose() {
    _graphics.dispose();
	}

}
