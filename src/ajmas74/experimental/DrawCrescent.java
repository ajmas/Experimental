package ajmas74.experimental;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Useful links: http://www.delphiforfun.org/programs/Delphi_Techniques/Draw%20Moon.htm
 * 
 * @author Andre-John Mas
 *
 */
public class DrawCrescent extends JPanel {

	Image img;
	
	DrawCrescent () 
	{
		img = Toolkit.getDefaultToolkit().createImage("data/full moon x.jpg");
	}
	
	
	/**
	 * This method paint the moon phase. It is based on a modified circle algorithm.
	 * The idea is that a moon's phase can be represented by a percentage of its
	 * width, so applying this to each point vertically creates the wanted image.
     *
	 * @param g Graphics object to draw with
	 * @param radius radius of the moon
	 * @param centerX center point on x axis
	 * @param centerY center point on y axis
	 * @param phase phase of the moon, from 0.0 to 1.0
	 * @param waxing
	 */
	public void paintMoon (Graphics g, int radius, int centerX, int centerY, float phase, boolean waxing ) {
		
		g.drawImage(img,0,0,200,200,null);
		
		// Don't bother drawing the phase, since there is essentially no shadow
		if ( (phase < 0.01) || (phase > 0.99) ) {
			return;
		}
		
		g.setColor( new Color(0,0,0,210) );
		
		// Make the darkness semi-transparent
		if ( g instanceof Graphics2D ) {
			Graphics2D g2d = (Graphics2D) g;
			
			RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,               RenderingHints.VALUE_ANTIALIAS_ON); 
			qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); 
			g2d.setRenderingHints(qualityHints); 
			
			g2d.setComposite( AlphaComposite.SrcOver );			
		}
		
		int[] xPoints = new int[360];
		int[] yPoints = new int[360];
		
		for ( int i=0; i< 180; i++ ) {
						
			double angle = Math.toRadians(i-90);
			int x1 = (int) ( Math.cos( angle ) * radius );			
			int y1 = (int) ( Math.sin( angle ) * radius );
//			System.out.println((i-90) + " --- " + y1 + " -- " + Math.toDegrees( Math.asin(y1/radius) ) );
			int width = x1 * 2;
			int x2 = (int) ( width * phase );
			
			if ( waxing ) {
				x1 = centerX - x1;				
				x2 = x1 + (width - x2);
			} 
			else { // waning
				x1 = centerX + x1;				
				x2 = x1 - (width - x2);				
			}
								
			y1 = centerY + y1;
			
			xPoints[i] = x1;
			yPoints[i] = y1;
			
			xPoints[xPoints.length-(i+1)] = x2;
			yPoints[yPoints.length-(i+1)] = y1;			
	
		}
		g.fillPolygon(xPoints, yPoints, xPoints.length);
						
	}
	
	@Override
	public void paint(Graphics g) {

		boolean waxing = true;
		
		int radius = 90;
		int centerX = 100;
		int centerY = 100;
		float phase = 0.25f;
		
		paintMoon(g,radius,centerX,centerY,phase, waxing);
		
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JFrame jf = new JFrame();
		jf.add( new DrawCrescent() );
		jf.setBounds(100,100,300,300);
		jf.setVisible(true);
		
	}

}
