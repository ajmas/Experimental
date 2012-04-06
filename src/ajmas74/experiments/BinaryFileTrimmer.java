/* Created on 11-Nov-2003 */
package ajmas74.experiments;

import java.io.*;
import java.util.*;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class BinaryFileTrimmer {

  /**
   * 
   */
  public BinaryFileTrimmer( String fileIn, String fileOut ) throws IOException{
    super();
    
		BufferedInputStream bIn = null;
		BufferedOutputStream bOut = null;
    try {
      bIn = new BufferedInputStream(new FileInputStream(fileIn));
			bOut = new BufferedOutputStream(new FileOutputStream(fileOut));
			
      boolean copy = false;
      int b = -1;
      /* read, looking for the first none null byte */
      while ( ( b = bIn.read()) != -1 && !copy ) {
        if ( b != 0 ) {
          copy = true;
        }
      }
      /* if this test evaluates to true, then there is nothing
       * to copy.
       */
      if ( b == -1 || !copy  ) {
        return;
      }
			/* now we can start writing to the new file */
      bOut.write(b);
			while ( ( b = bIn.read()) != -1 ) {
			  bOut.write(b);
			}      
    } finally {
      if ( bIn != null ) {
        bIn.close();
      }
			if ( bOut != null ) {
				bIn.close();
			}
    }

  }

  public static void main ( String[] args ) {
    System.out.println("input ....... : " +args[0] );
		System.out.println("output ...... : " +args[1] );
		try {
      BinaryFileTrimmer bft = new BinaryFileTrimmer(args[0],args[1]);
    } catch (IOException e) {
      e.printStackTrace();
    }		
  }
}
