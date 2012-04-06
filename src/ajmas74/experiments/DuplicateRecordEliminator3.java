package ajmas74.experiments;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class DuplicateRecordEliminator3 {

  
	/**
	 * Constructor for DuplicateRecordEliminator.
	 */
	public DuplicateRecordEliminator3( int column, String src, String dest )
    throws IOException{
    

    HashMap mapA = readFile(new File("D:/drop zone/christine/MD Hotels.txt"),1,5);
    HashMap mapB = readFile2(new File("D:/drop zone/christine/sort HIS.txt"),1,5);
    
    HashMap resultMap = new HashMap();
    HashMap nonMatchMap = new HashMap();
    HashMap nonMatchMap2 = new HashMap();
    HashMap possibleMatchMap = new HashMap();
    
    int count = 0;    
    Iterator iter = mapB.keySet().iterator();
    while ( iter.hasNext() ) {
      Object obj = iter.next();
      Object obj2 = mapA.get(obj);
      Object obj3 = mapB.get(obj);
      
      if ( obj2 != null ) {
        if ( resultMap.get(obj) != null ) {
          System.out.println("result map already contains ID " + obj);
        }
        resultMap.put(obj,obj2);
        count++;
      } else {
        nonMatchMap.put(obj,obj3);
      }
      
    }
    
    String[] mapAArray = (String[]) mapA.values().toArray(new String[0]);
    
    iter = nonMatchMap.keySet().iterator();
    wLoop: while ( iter.hasNext() ) { 
      Object key =  iter.next();
      String line = (String) nonMatchMap.get(key);
      String[] parts = split(line,"\t");
      
      String name = parts[1];
      if ( name.indexOf(',') > -1 ) {
        name = name.substring(0,parts[1].indexOf(',')-1);
      }
      for ( int i=0; i<mapAArray.length; i++ ) {
        if (mapAArray[i].indexOf(name)>0 && mapAArray[i].indexOf(parts[5]) > 0 ){
          if ( possibleMatchMap.get(new Integer(i)) != null ) {
            System.out.println("already a possible match:" + mapAArray[i]);
          }
          possibleMatchMap.put(new Integer(i),mapAArray[i]);
          continue wLoop;
        }
      }
      nonMatchMap2.put(key,line);
    }
        
    System.out.println("size="+resultMap.size());
    
    writeFile(new File("D:/drop zone/christine/out5.txt"),resultMap);
    writeFile(new File("D:/drop zone/christine/out5-2.txt"),nonMatchMap2);
    writeFile(new File("D:/drop zone/christine/out5-3.txt"),possibleMatchMap);
    
    System.out.println("Finished, with " + count + " matches");
	}

  private HashMap readFile( File file, int a, int b) throws IOException {
    System.out.println("---- NAME:" + file.getName());
    
    HashMap dups = new HashMap();
    
    HashMap map = new HashMap();
    FileInputStream fin = null;
    try {
      fin = new FileInputStream(file);
      BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
      String line = null;
      while ( ( line = reader.readLine()) != null ) {
        String[] parts = split(line,"\t");
        String id = parts[a] + " - " + parts[b];
        //String id = parts[0];
        if ( map.get(id) != null && dups.get(id) == null ) {
          System.out.println("duplicate ID: " + parts[0] + " - "+ id );
          dups.put(id,"");
          continue;
        }
       // if ( parts[0].equals("x") || parts[0].equals("NULL")) {
       if ( parts[0].equals("NULL")) {
          continue;
        }
        map.put(id,line);
      }
    } finally {
      if (fin != null) {
        fin.close();
      }
    }
    return map;
  }
  
  private HashMap readFile2( File file, int a, int b) throws IOException {
    System.out.println("---- NAME:" + file.getName());
    
    HashMap dups = new HashMap();
    
    HashMap map = new HashMap();
    FileInputStream fin = null;
    try {
      fin = new FileInputStream(file);
      BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
      String line = null;
      while ( ( line = reader.readLine()) != null ) {
        if ( line.trim().length() == 0 ) {
          continue;
        }
        String[] parts = split(line,"\t");
        String id = parts[a] + " - " + parts[b];
        //String id = parts[0];
        if ( map.get(id) != null && dups.get(id) == null ) {
          System.out.println("duplicate ID: " + parts[0] + " - "+ id );
          dups.put(id,"");
          continue;
        }
        if ( !parts[0].equals("x") ) { //|| parts[0].equals("NULL")) {
          continue;
        }
        map.put(id,line);
      }
    } finally {
      if (fin != null) {
        fin.close();
      }
    }
    return map;
  }
    
  private void writeFile ( File file, HashMap map ) throws IOException {
    FileOutputStream fout = null;
    try {
      fout = new FileOutputStream(file);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fout));
      Object[] keys = (Object[]) map.keySet().toArray(new Object[0]);
      Arrays.sort(keys);
      System.out.println("len="+keys.length);
      for ( int i=0; i<keys.length; i++ ) {
        writer.write( (String)map.get(keys[i]));
        //writer.newLine();
        writer.write("\n");
      }
      writer.flush();
    } finally {
      if (fout != null) {
        fout.close();
      }
    }    
  }
  
  public static String[] split(String text, String delim)
  {
    java.util.List parts = new java.util.LinkedList();

    int index;
    while ((index = text.indexOf(delim)) != -1)
    {
      parts.add(text.substring(0, index));
      text = text.substring(index + delim.length());
    }

    parts.add(text);
    return (String[]) parts.toArray(new String[parts.size()]);
  }
  

	public static void main(String[] args) {
    try {
      DuplicateRecordEliminator3 dre 
        = new DuplicateRecordEliminator3 (
          1,
          "d://drop zone/data.txt",
          "d://drop zone/data.txt");
    } catch ( Exception ex ) {
      ex.printStackTrace();
    }
	}
}
