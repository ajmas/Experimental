package ajmas74.experiments.graphics2d;

import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PolarImageToCartesian {

  /**
   * The method converts an image that is in Polar coordindates to one
   * that is in Cartesian coordinates. For example, imagine a picture
   * that is curved around a center point, in order to see it as a flat
   * panormaric you need to convert. The original uses cartesian coordinates
   * and the resulting is in Cartesian.
   * 
   * @param image image to convert
   * @param innerRadius how pixels from center is the start of the useful pixels
   * @param outerRadius how pixels from center is the end of the useful pixels
   * @param newWidth how wide the new image should be
   * @return the converted image
   */
  public static Image convertPolarImageToCartesian ( Image image, int innerRadius, int outerRadius, int newWidth ) {
    BufferedImage convertedImage = null;
    
    int imgWidth = image.getWidth(null);
    int imgHeight = image.getHeight(null);
    
    System.out.println("width: " + imgWidth);
    System.out.println("height: " + imgHeight);
    
    int[] packedPixels = new int[imgWidth*imgHeight];
    PixelGrabber pixelgrabber 
       = new PixelGrabber(image, 0, 0, imgWidth,
           imgHeight, packedPixels, 0, imgWidth);
    try {
      pixelgrabber.grabPixels();
    } catch (InterruptedException e) {
        throw new RuntimeException();
    }    
    
    if ( imgWidth/2 < outerRadius) {
      outerRadius = imgWidth/2;
    }
    
    // bytes per pixel
    int bpp = 4;
    int width = newWidth;
    int height = outerRadius-innerRadius;
    int size = width * height;// * bpp;
    int[] pixelData = new int[size];

    System.out.println("size: " + size);
    
    int xc = imgWidth/2;
    int yc = imgHeight/2;

    for ( int r=innerRadius; r<outerRadius; r++ ) {
      // t = theta in degrees
      for ( int t=0; t<width; t++ ) {
        // theta in radians
        double tr = Math.toRadians((t*1.0)/width*360);
        int x = (int) Math.round(xc + r * Math.cos(tr));
        int y = (int) Math.round(yc + r * Math.sin(tr));     
        
        int rDiff = r - innerRadius;
        int h = width * (height-(rDiff+1));
        pixelData[h+t] = packedPixels[imgWidth*y+x];
        
      }
    }
    
    DataBuffer dataBuf = new DataBufferInt(pixelData, size);
    
    ColorModel colorModel = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff);
    int[] masks= {0x00ff0000, 0x0000ff00, 0x000000ff};
    WritableRaster raster = Raster.createPackedRaster(dataBuf, width, height, width, masks, null);
    
    convertedImage = new BufferedImage(colorModel, raster, false, null);    
    
    return convertedImage;
    
  }
  
  static class MyPanel extends JPanel {
    Image image;
    
    MyPanel ( Image image ) {
      this.image = image;
      setSize(new Dimension( image.getWidth(null), image.getHeight(null)));
      setPreferredSize(getSize());
      //this.set
    }
    
    public void paint (Graphics g) {
      int width = image.getWidth(null);
      int height = image.getHeight(null);
     // System.out.println(width + " x " + height);
      g.drawImage(image,0,0,width,height,null);
    }
  }
  
  public static void main ( String[] args ) {
    try {
      String filename = 
//        "/Users/ajmas/Sites/polar images/gear.jpg";
          "/Users/ajmas/Desktop/20081110b.jpg";
//        "/Users/ajmas/Sites/polar images/PIA04983_br2.jpg";
        //"/Users/ajmas/Sites/polar images/spirit-360_degrees_sol_388_vertical.jpg";
        //"/Users/ajmas/Sites/polar images/spirit-360_degrees_sol_388_polar.jpg";
        //"/Users/ajmas/Pictures/asbtract-logo-001.jpg";
      URL url = ClassLoader.getSystemResource(filename);
      if (url == null) {
        try {
            url = new URL("file", "localhost", filename);
        } catch (Exception urlException) {
        } // ignore
      }
      
      
      Image srcImage = ImageIO.read(url);
      
      //converts
      MyPanel panel = new MyPanel(srcImage); 
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.getViewport().setView(panel);
      Frame f = new Frame();
      f.add(scrollPane);
      f.setBounds(50,50,500,500);
      f.setVisible(true);
      
      Image dstImage = convertPolarImageToCartesian(srcImage,000,500,360);
      MyPanel panel2 = new MyPanel(dstImage); 
      JScrollPane scrollPane2 = new JScrollPane();
      scrollPane2.getViewport().setView(panel2);
      Frame f2 = new Frame();
      f2.add(scrollPane2);
      f2.setBounds(100,100,500,500);
      f2.setVisible(true);  
      f2.validate();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
