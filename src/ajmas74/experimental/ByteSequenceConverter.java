/*
 * Created on 14-May-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ajmas74.experimental;

import java.util.*;
import java.io.*;

/**
 * @author andrmas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ByteSequenceConverter {

  public static int[] UTF8_BOM = new int[] {0xEF, 0xBB, 0xBF};
  
  ByteSequenceConverter () {
  }
    
  public static int[] convert ( File inputFile ) {
    inputFile = new File("K:\\development\\Experimental\\datafiles\\byte_sequence.txt");
    try {
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      FileInputStream in = new FileInputStream(inputFile);
      
      StringBuffer strBuf = new StringBuffer();
      int theByte = 0;
      while ( (theByte = in.read()) > -1 ) {
        strBuf.append((char)theByte);
      }
      in.close();      
      
      // set values to -1
      int[] bytes = new int[strBuf.length()];
      for ( int i=0; i<bytes.length;i++ ) {
        bytes[i] = -1;
      }
      
      StringTokenizer tokenizer = new StringTokenizer(strBuf.toString()," ");
//      int x=3;
//      bytes[0] = UTF8_BOM[0];
//      bytes[1] = UTF8_BOM[1];
//      bytes[2] = UTF8_BOM[2];
      int x=0;
      while ( tokenizer.hasMoreElements() ) {
        bytes[x] = Integer.valueOf(tokenizer.nextToken(),16).intValue();
        x++;
      }
      
      return bytes;
            
    } catch (IOException e) {
      e.printStackTrace();
    }    
    return new int[0];
  }
  
  static void convert ( File inputFile, File outputFile ) {
    try {
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      FileInputStream in = new FileInputStream(inputFile);
      
      StringBuffer strBuf = new StringBuffer();
      int theByte = 0;
      while ( (theByte = in.read()) > -1 ) {
        strBuf.append((char)theByte);
      }
      in.close();      
      
      // set values to -1
      int[] bytes = new int[strBuf.length()];
      for ( int i=0; i<bytes.length;i++ ) {
        bytes[i] = -1;
      }
      
      StringTokenizer tokenizer = new StringTokenizer(strBuf.toString()," ");
//      int x=3;
//      bytes[0] = UTF8_BOM[0];
//      bytes[1] = UTF8_BOM[1];
//      bytes[2] = UTF8_BOM[2];
      int x=0;
      while ( tokenizer.hasMoreElements() ) {
        bytes[x] = Integer.valueOf(tokenizer.nextToken(),16).intValue();
        x++;
      }
      
      FileOutputStream out = new FileOutputStream(outputFile);
      for ( int i=0; i<bytes.length && bytes[i] != -1;i++ ) {
        out.write((byte)bytes[i]);        
      }
      out.close();
      
    } catch (IOException e) {
      e.printStackTrace();
    }    
  }
  
  public static void main (String[] args) {
    convert(new File("K:\\development\\Experimental\\datafiles\\byte_sequence.txt"),new File("K:\\development\\Experimental\\datafiles\\text.txt"));
  }
  
}
