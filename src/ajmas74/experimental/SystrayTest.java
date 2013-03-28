package ajmas74.experimental;

import JTray.*;
import java.awt.*;
import java.awt.image.*;

/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SystrayTest {

  JTrayIcon _jTrayIcon;
  BufferedImage _bufferedImage;
  
  SystrayTest () {
  	char degreesSymbol = '\u00B0';
  	init();
    setIconText("37"+degreesSymbol);
  }
  
  private void init () {
    _bufferedImage = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = (Graphics2D) _bufferedImage.getGraphics();
    g2.setColor(Color.white);

    //float alpha = (float) 0.0;
    //AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , alpha);
    //g2.setComposite(ac);  
    
    g2.fillRect(0,0,16,16);
    
    try {
	    _jTrayIcon = new JTrayIcon(_bufferedImage);
      _jTrayIcon.setToolTipText("current temperature for Montreal");
	    _jTrayIcon.setVisible(true);
    } catch (JTrayLibraryLoadFailedException e) {
      e.printStackTrace();
    }    
        	
  }
  
  private void setIconText( String text ) {
//  	if ( _jTrayIcon == null ) {
//  		return;
//  	}
    _bufferedImage = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = (Graphics2D) _bufferedImage.getGraphics();
    g2.setColor(Color.white);
    
    float alpha = (float) 0.0;
    AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , alpha);
    g2.setComposite(ac);
    
    g2.fillRect(0,0,16,16);
    g2.setColor(Color.black);
    
    alpha = (float) 1.0;
    ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , alpha);
    g2.setComposite(ac);      
    
    g2.setFont(new Font("Arial",Font.BOLD,10));
    g2.drawString(text,1,12);


    _jTrayIcon.setIcon(_bufferedImage);
//    _jTrayIcon.setVisible(false);
    
    
//    try {
//    	if ( _jTrayIcon != null ) {
//    	  _jTrayIcon.setVisible(false);
//    	}
//      _jTrayIcon = new JTrayIcon(_bufferedImage);
//      _jTrayIcon.setVisible(true);
//    } catch (JTrayLibraryLoadFailedException e) {
//      e.printStackTrace();
//    }
    
  }
  
  public static void main(String[] args) {
    SystrayTest test = new SystrayTest();
  }
}
