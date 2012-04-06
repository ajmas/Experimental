package ajmas74.experiments;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class DuplicateRecordEliminator2 {

	/**
	 * Constructor for DuplicateRecordEliminator.
	 */
	public DuplicateRecordEliminator2( int column, String src, String dest )
    throws IOException{
    
    HashMap map = new HashMap();
    HashMap weightMap = new HashMap();
    
    //File theFile = new File(filePath);
    //InputStream inStream = new FileInputStream(theFile);
    BufferedReader in
       = new BufferedReader(new FileReader(src));
       
    String line = null;
    
    while ( (line = in.readLine()) != null ) {
      Data dataObj = new Data(line);
      
      HashMap subMap = null;
      
//      StringTokenizer tokenizer = new StringTokenizer(line,"\t");
			if ( line.startsWith("\t") ) {
        line = " " + line;
			}
      String[] parts = StringUtils.splitStr(line,'\t',0);
      //String col1 = null;
      String key = null;
      int idx=0;
      //while ( tokenizer.hasMoreElements()/*MoreTokens()*/ ) {
      for ( int i=0; i<parts.length; i++ ) {
      	
        //String token = (String)tokenizer.nextElement();//nextToken();
        String token = parts[i];
        //System.out.print(token + " , ");
        dataObj._fields.add(token);
        switch ( idx ) {
          case 0:
            dataObj._key1 = token;
          break;
          case 1:
            dataObj._key2 = token;
            if ( weightMap.get(dataObj._key2) == null ) {
              weightMap.put( dataObj._key2, new HashMap());
            }
            subMap = (HashMap) weightMap.get(dataObj._key2);
          break;
          case 2:
            dataObj._value1 = token;
            if ( subMap != null ) {
              Integer count = null;
              if ( ( count = (Integer) subMap.get(token)) == null ) {
                count = new Integer(1);
                subMap.put(token,count);
              } else {
                int intCount = count.intValue();
                intCount++;
                subMap.put(token,new Integer(intCount));
              }
            } else {
              System.err.println("error");
            }
          break;                    
        }
        idx++;
      }
      //System.out.println("");            
      //map.put(dataObj._key1 + ":" + dataObj._key2,dataObj);
    }
    
    Iterator weightIter = weightMap.keySet().iterator();
    Object obj=null;
    while ( weightIter.hasNext() ) {
      obj=weightIter.next();
    //for ( Object obj=null; weightIter.hasNext(); obj=weightIter.next()) {
      HashMap wtMap = (HashMap) weightMap.get(obj);
        
      if ( wtMap.size() > 1 ) {
        System.out.println(obj + " --- " + weightMap.get(obj));
      }
    //}
    }    
//    //System.out.println("Map has " + map.size() + " unique records");
//    BufferedWriter out 
//      = new BufferedWriter(new FileWriter(dest));
//  
//    Iterator iter = map.keySet().iterator();
//    while ( iter.hasNext() ) {
//      String key = (String) iter.next();
//      String x = (String) map.get(key);
//      out.write( x + "\n");//x + "\t" + key + "\n");
//    }
    System.out.println("Finished");
	}

  class Data {

    String _data;
    String _key1;
    String _key2;
    String _value1;
    List   _fields = new Vector();
    
    Data ( String data ) {
      _data = data;
    }    
  }
	public static void main(String[] args) {
    try {
      DuplicateRecordEliminator2 dre 
        = new DuplicateRecordEliminator2 (
          1,
          "d://drop zone/data.txt",
          "d://drop zone/data.txt");
    } catch ( Exception ex ) {
      ex.printStackTrace();
    }
	}
}
