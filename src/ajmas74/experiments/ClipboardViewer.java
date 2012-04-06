/*
 * Created on 15-Oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ajmas74.experiments;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.io.IOException;

/**
 * @author ajmas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClipboardViewer implements Transferable, ClipboardOwner {

  private DataFlavor[] _dataFlavours = new DataFlavor[1];
  private InputStream _in;
  
  ClipboardViewer () {
    try {
      _dataFlavours[0] = new DataFlavor("image/x-pict");
      
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /* (non-Javadoc)
   * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
   */
  public void lostOwnership(Clipboard clipboard, Transferable contents) {
    // TODO Auto-generated method stub
    System.out.println("ownership lost");
  }
  
  /* (non-Javadoc)
   * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
   */
  public DataFlavor[] getTransferDataFlavors() {
    // TODO Auto-generated method stub
    return _dataFlavours;
  }

  /* (non-Javadoc)
   * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
   */
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    return _dataFlavours[0].equals(flavor);
  }

  /* (non-Javadoc)
   * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
   */
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if ( isDataFlavorSupported(flavor) ) {
      _in = new FileInputStream("/test.pict");
      return _in;
    } else {
      throw new UnsupportedFlavorException(flavor);
    }
  }
  
  public void addToClipboard() {
    try {
      String mimeType = "image/x-pict";
      byte[] data = null;
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      
      DataFlavor dataFlavor = new DataFlavor(mimeType);
      
      StringSelection contents = new StringSelection( "x" );
      
      ClipboardOwner owner = this;
      
      clipboard.setContents(this,owner);
    } catch ( Exception ex ) {
      ex.printStackTrace();
    }
  }
  
  public static void main(String[] args) {
    //(new ClipboardViewer()).addToClipboard();
    
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable transfer = clipboard.getContents(null);
    DataFlavor[] flavours = transfer.getTransferDataFlavors();
    for ( int i=0; i<flavours.length; i++ ) {
      System.out.println(i);
      System.out.println("  MIME Type ............ " + flavours[i].getMimeType());
      System.out.println("  Displayable name ..... " + flavours[i].getHumanPresentableName());

      Object data = null;
      if ( flavours[i].getMimeType().startsWith("image/x-pict") ) {
        try {
          data = transfer.getTransferData(flavours[i]);
          System.out.println("writing");

          FileOutputStream fOut = new FileOutputStream("/out.pict");
          InputStream in = (InputStream) data;
          
          byte[] buffer = new byte[1024];
          int len = -1;
          while ( (len = in.read(buffer)) > -1 ) {
            fOut.write(buffer,0,len);
          }
          fOut.close();
        } catch ( Exception ex ) {
          ex.printStackTrace();
        }
      } else if ( flavours[i].getMimeType().startsWith("application/x-pdf") ) {
        try {
          data = transfer.getTransferData(flavours[i]);
          System.out.println("writing");

          FileOutputStream fOut = new FileOutputStream("/out.pdf");
          InputStream in = (InputStream) data;
          
          byte[] buffer = new byte[1024];
          int len = -1;
          while ( (len = in.read(buffer)) > -1 ) {
            fOut.write(buffer,0,len);
          }
          fOut.close();
        } catch ( Exception ex ) {
          ex.printStackTrace();
        }
      }
    }
  }



}
