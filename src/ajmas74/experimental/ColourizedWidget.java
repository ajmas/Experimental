package ajmas74.experimental;

import java.awt.*;
import javax.swing.*;

/**
 * @author ajmas
 *
 * works best when black is used as a fixed value and it alpha is
 * modified, as opposed to a fixed alpha and varying gray
 * 
 */
public class ColourizedWidget extends JComponent {

	Image _img;
	
	ColourizedWidget () {
		_img = Toolkit.getDefaultToolkit().getImage("datafiles/box.jpg");
	}
	
	public void paint ( Graphics g ) {
		Graphics2D g2 = (Graphics2D) g;
		Dimension size = getSize();
		int y=0;
		int gray=0;
		
		g.setColor(Color.yellow);
		g.fillRect(0,0,size.width,size.height);		
		
		float alpha = (float) 0.7;//((double)i)/size.height;
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , alpha);
		g2.setComposite(ac);
		
		g2.drawImage(_img,0,0,null);
//		for ( int i=0; i<size.height; i++ ) {
//			gray = (int)(255*(((double)i)/size.height));
//				
//					
//			float alpha2 = (float) (1.0 - ((double)gray/255));//((double)i)/size.height;
//			AlphaComposite ac2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , alpha2);
//			g2.setComposite(ac2);
//			
//			Color color = new Color(gray,gray,gray);
//			g.setColor(color);
//			g.drawLine(0,i,size.width,i);
//		}

		

	}
	
	public static void main ( String[] args ) {
		JFrame jf = new JFrame();
		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(new ColourizedWidget());
		jf.setBounds(100,100,100,100);
		jf.setVisible(true);
	}
	
}
