/* Created on 22-Aug-2003 */
package ajmas74.experimental.minidb;

import java.io.*;
import java.util.*;

/**
 * @author Andre-John Mas
 *
 */
public class DBIndex {

  int   _entryCount;
  List  _indexEntries;
  Block _dbIndexBlock;
  
  public void writeIndex() {

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