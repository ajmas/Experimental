package ajmas74.experimental;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:andrejohn.mas@gmail.comm">Andr&eacute;-John Mas</a>
 *
 */
public class DuplicateRecordEliminator {

	/**
	 * Constructor for DuplicateRecordEliminator.
	 */
	public DuplicateRecordEliminator( int column, String src, String dest )
    throws IOException{
    
    System.out.println("column="+column);
    
    HashMap map = new HashMap();
    
    //File theFile = new File(filePath);
    //InputStream inStream = new FileInputStream(theFile);
    BufferedReader in
       = new BufferedReader(new FileReader(src));
       
    String line = null;
    
    int i=0;
    while ( (line = in.readLine()) != null ) {
      //StringTokenizer tokenizer = new StringTokenizer(line,"\t");
      String[] parts = split(line,",");
      //StringTokenizer tokenizer = new StringTokenizer(line,",");
      //String col1 = null;
      String key = parts[column];
      //System.out.println("key="+key);
      
//      String key = null;
//      //int idx=0;
//      //while ( tokenizer.hasMoreTokens() ) {
//      for ( int idx=0; tokenizer.hasMoreTokens(); idx++ ) {  
//        key = tokenizer.nextToken();
//        //System.out.println(idx);   
//        if ( idx==column ) {
//          //key = tokenizer.nextToken();
//          break;
//       // } else if ( idx==1 ) {
//       //   col2 = tokenizer.nextToken();
//        }
//        //idx++;
//      }
      //System.out.println(i + ": " + key + "  :  " + line);
      if ( map.get(key) == null ) {
        map.put(key,line);
      }
      i++;
    }
    System.out.println("Map has " + map.size() + " unique records");
    BufferedWriter out 
      = new BufferedWriter(new FileWriter(dest));
  
    Iterator iter = map.keySet().iterator();
    while ( iter.hasNext() ) {
      String key = (String) iter.next();
      String x = (String) map.get(key);
      out.write( x + "\n");//x + "\t" + key + "\n");
    }
    System.out.println("Finished");
	}

  public static String[] split(String text, String delim) {
    java.util.List parts = new java.util.LinkedList();

    int index;
    while ((index = text.indexOf(delim)) != -1) {
      parts.add(text.substring(0, index));
      text = text.substring(index + delim.length());
    }

    parts.add(text);
    return (String[]) parts.toArray(new String[parts.size()]);
  }
  
	public static void main(String[] args) {
    try {
      DuplicateRecordEliminator dre 
        = new DuplicateRecordEliminator (
          1,
          "d://drop zone/text.csv",
          "d://drop zone/text_out2.csv");
    } catch ( Exception ex ) {
      ex.printStackTrace();
    }
	}
}
