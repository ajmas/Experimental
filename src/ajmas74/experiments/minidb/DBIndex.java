/* Created on 22-Aug-2003 */
package ajmas74.experiments.minidb;

import java.io.*;
import java.util.*;

/**
 * @author Andre-John Mas
 *
 */
public class DBIndex {

  public static final int INDEX_ENTRy_SIZE = 
  int   _entryCount;
  List  _indexEntries;
  Block _dbIndexBlock;
  
  public void writeIndex() {
    if ( )
  }
  
//  public void writeIndex(OutputStream out ) throws IOException {
//    DataOutputStream dout = new DataOutputStream(out);
//    dout.write(_entryCount);
//    
//  }
  
  public class IndexEntry {
    int _id;
    int _size;
    int _offset;
    int _flags;
  }
  
}

/*

  The integral types are byte, short, int, and long, whose values are
  8-bit, 16-bit, 32-bit and 64-bit signed two's-complement integers,
  respectively, and char, whose values are 16-bit unsigned integers
  representing Unicode characters.

*/