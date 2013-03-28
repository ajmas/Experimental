/* Created on 7-Jul-2003 */
package ajmas74.experimental;

import java.net.*;
import java.io.*;


/**
 * @author Andre-John Mas
 *
 */
public class ListFiles {

  public static void main ( String[] args ) {
    File f = new File("D:/datafiles/mp3_library");
    
    File[] files = f.listFiles();
    
    for ( int i=0; i<files.length; i++ ) {
      System.out.println(files[i].getAbsolutePath());
      try {
        System.out.println(URLEncoder.encode(files[i].getName()));
        //System.out.println(URLEncoder.encode(files[i].getName(),"utf8"));
        //System.out.println(files[i].toURI());
        System.out.println(files[i].toURL());
      } catch (Exception e) {
        // XXX Auto-generated catch block
        e.printStackTrace();
      } 
      System.out.println("---------------");
    }
  }
}
