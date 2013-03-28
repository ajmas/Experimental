package ajmas74.experimental;

import java.io.*;

public class TruncateFile {

  public TruncateFile ( String filename, int startIdx, int endIdx ) {
    
  }
  
  static void truncateFile ( String filename, int startIdx, int endIdx ) throws IOException {
    BufferedInputStream in = null;
    BufferedOutputStream out = null;
    try {
      File f = new File(filename);
      long length = f.length();
      
//      long copyBytes = endIdx-startIdx;
//      if ( endIdx == -1 ) {
//        copyBytes = length - startIdx;
//      }
      in = new BufferedInputStream(new FileInputStream(filename));      
      out = new BufferedOutputStream( new FileOutputStream(filename+".out"));
    
      byte[] buffer = new byte[2048];
      int size = -1;
      if ( startIdx > 0 ) {
        in.skip(startIdx);
      }
      while ( (size = in.read(buffer)) > -1 ) {
        out.write(buffer,0,size); 
      }
      
    
    } finally {
      if ( in != null ) {
        in.close();
      }
      if ( out != null ) {
        out.close();
      }
    }
  }
  
  public static void main ( String[] args ) {
    try {
      TruncateFile.truncateFile("/Users/ajmas/Desktop/from VPC/FFIMC.exe",2966193,-1);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
}
