
package ajmas74.experiments;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * This class provides a mechanism for executing a Java class
 * in a new process.
 * <p>
 * Known limitations:
 * <ul>
 * <li>Library path not passed to sub process
 * </ul>
 * @author Andre-John Mas
 */
public class JavaExec {
  static int id = 0;
  
  private static final String SUN_BOOT_LIB_PATH = "sun.boot.library.path";
  
  /**
   * @param classpath the classpath to be included (may be null)
   * @param javaProps the properties to be passed (may be null)
   * @param mainClass the main class
   * @param params parameters to be passed to the main method (may be null)
   * @param env the application environment (may be null)
   * @param userDir the directory from which the application is to be started (may be null)
   * @return Process
   * @throws IOException
   */
	public static Process exec ( String[] classpath, Properties javaProps,
	  String mainClass, String[] params, String[] env, File userDir ) throws IOException {
 			id++;
 			// create the classpath environment variable		
      StringBuffer classpathStrBuf = new StringBuffer("CLASSPATH=");
      if ( classpath != null ) {
	      String separator = System.getProperty("path.separator");
	      for ( int i=0; i<classpath.length; i++ ) {
	        classpathStrBuf.append((String)classpath[i]);
	        classpathStrBuf.append(separator);
	      }
	      if ( classpathStrBuf.length() > 0 ) {
	        classpathStrBuf.setLength(classpathStrBuf.length()-1);
	      }
      } else {
        classpathStrBuf.append(System.getProperty("java.class.path"));
      }
      
   		// ensure env is not null
			if ( env == null ) {
        env =  new String[] { };
			}
			
			//add the classpath environment variable to the enviroment
			String[] lEnv = new String[env.length+1];
      for ( int i=0; i<env.length; i++ ) {
        lEnv[i]=env[i];
      }
      lEnv[lEnv.length-1] = classpathStrBuf.toString();
      System.out.println(classpathStrBuf.toString());
      
      //ensure userDir is not null
      if ( userDir == null ) {
        userDir = new File(".").getAbsoluteFile();
      }
      
      // create the command line
      Vector commandElements = new Vector();
      
      commandElements.add(System.getProperty(SUN_BOOT_LIB_PATH) + "/java");
      //commandElements.add("-Djava.library.path=\""+System.getProperty("java.library.path")+"\"");
      
    	if ( javaProps != null ) {
        Enumeration enum = javaProps.keys();
        while ( enum.hasMoreElements() ) {
          String name = (String) enum.nextElement();
          commandElements.add("-D"+name+"="+javaProps.get(name));
        }    	  
    	}
      
      commandElements.add(mainClass);
   		
   		if ( params != null ) {
	      for ( int i=0; i<params.length; i++ ) {
	        commandElements.add(params[i]);
	      }
   		}
                 
      String[] command = (String[]) commandElements.toArray(new String[0]);
        
      // execute all this
      return Runtime.getRuntime().exec(command,lEnv,userDir);
	}

/* UNCOMMENT TO TEST CLASS */	
//	public static void main ( String[] args ) {
//	  try {
//	    if ( args.length > 0 && args[0].equals("xxTESTxx") ) {
//        Process p = exec(null,null,"ajmas74.experiments.JavaExec",null,null,null);
//        ProcessOutputHandler outHandler = new ProcessOutputHandler(System.out,p.getInputStream(),id);
//        ProcessOutputHandler errHandler = new ProcessOutputHandler(System.err,p.getErrorStream(),id);
//        p.waitFor();
//        System.out.println("----- " + id + " -----");
//        p = exec(null,null,"ajmas74.experiments.JavaExec",null,null,new File("c:/"));
//        outHandler = new ProcessOutputHandler(System.out,p.getInputStream(),id);
//        errHandler = new ProcessOutputHandler(System.err,p.getErrorStream(),id);
//        p.waitFor();
//        System.out.println("----- " + id + " -----");
//        p = exec(null,null,"ajmas74.experiments.JavaExec",
//          new String[]{ "hello" },null,new File("c:/"));
//        outHandler = new ProcessOutputHandler(System.out,p.getInputStream(),id);
//        errHandler = new ProcessOutputHandler(System.err,p.getErrorStream(),id);
//        p.waitFor();
//        System.out.println("----- " + id + " -----");
//        Properties props = new Properties();
//        props.put("abc.def","123.456");
//        p = exec(null,props,"ajmas74.experiments.JavaExec",null,null,new File("c:/"));
//        outHandler = new ProcessOutputHandler(System.out,p.getInputStream(),id);
//        errHandler = new ProcessOutputHandler(System.err,p.getErrorStream(),id);
//        p.waitFor();        
//	    } else {
//        	      
//        System.out.println("args:");
//        for ( int i=0; i<args.length; i++ ) {
//          System.out.println("  " + args[i]);
//        }
//        System.out.println("properties:");
//        Properties props = System.getProperties();
//        Enumeration enum = props.keys();
//        while ( enum.hasMoreElements() ) {
//          String key = (String) enum.nextElement();
//          System.out.println("  "+key+"="+System.getProperty(key));
//        }  	      
//	    }
//	  } catch ( Exception ex ) {	
//	    ex.printStackTrace();
//		}
//	}
	
}