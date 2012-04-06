/* Created on 20-Jun-2003 */
package ajmas74.experiments;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Andre-John Mas
 *
 */
public class ZipCreator {

  public static void main ( String[] args ) {
    FileOutputStream fout = null;
    try {
      fout = new FileOutputStream("d:/testzip.zip");
      ZipOutputStream zipOut = new ZipOutputStream(fout);
      
      /* If you don't include this then files will simply
       *  be archived, i.e. not compressed.
       */
      zipOut.setMethod(ZipOutputStream.DEFLATED);
      
      ZipEntry entry = null;
      byte[] bytes = "Hello World - AAAAAAAAAAAAAAAA".getBytes();
      
      
      entry = new ZipEntry("EntryA/abc");
      
      /* CRC is optional */
      CRC32 crc = new CRC32();
      crc.reset();
      crc.update(bytes);
      entry.setCrc(crc.getValue());
      
      zipOut.putNextEntry(entry);
      zipOut.write(bytes,0,bytes.length);
      zipOut.closeEntry();
      
      entry = new ZipEntry("EntryB");
      zipOut.putNextEntry(entry);           
      zipOut.write(bytes,0,bytes.length);      
      zipOut.closeEntry();
      
      zipOut.finish();           
        
      System.out.println("finished");
    } catch ( Exception ex ) {
      ex.printStackTrace();
    } finally {
      try {
        if ( fout != null ) {
          fout.close();
        }        
      } catch ( IOException ex ) {
      }
    }
  }
}
