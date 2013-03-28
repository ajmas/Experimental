package ajmas74.experimental;

import java.awt.*;
import java.io.*;

/**
 * Due to current limitations, I can't see how I would implement this without
 * the use of native libraries
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class FontViewer extends Frame {

  public FontViewer ( String file ) {
    try {
      FileInputStream finStream = new FileInputStream(file);
    } catch ( Exception ex ) {
      ex.printStackTrace();
    }
  }
  
  public FontViewer ( ) {
    setLayout( new BorderLayout() );
  }
  
  public static void main ( String[] args ) {
 /*   if ( args.length > 0 ) {
      FontViewer fv = new FontViewer( args[0] );
      fv.setVisible(true);
    }
   */
   
   Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
   for ( int i=0; i<fonts.length; i++ ) {
    System.out.println(fonts[i]);
   }
   System.out.println("--------------");
   GraphicsDevice[] graphDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
   for ( int i=0; i<graphDevice.length; i++ ) {
    System.out.println(graphDevice[i]);
   }   
  }
}
