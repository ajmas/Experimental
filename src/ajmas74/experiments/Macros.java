/* Created on 22-Jun-2003 */
package ajmas74.experiments;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Vector;

/**
 * @author <a href="mailto:ajmas@bigfoot.com">Andr&eacute;-John Mas</a>
 *
 */
public class Macros {

	
	private final static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	private final static String[] NON_MACRO_METHODS = new String[] {
		"main","listMacros","execMacro"
	};
	 
	/** returns today's date as a String */
  public static String today() {
    return (today(DEFAULT_DATE_FORMAT)).toString();
  }

	/** returns today's date as a String, with the specified format */
	public static String today( String format ) {
		SimpleDateFormat sdFormat = new SimpleDateFormat(format);
		return sdFormat.format(new Date());		
	}
	
	
	/** returns date, relative to today, as a String, with the specified format */	
	public static String date( String daysRelativeTo ) {
		return date (new Integer(daysRelativeTo),DEFAULT_DATE_FORMAT);
	}
	
	/** returns date, relative to today, as a String, with the specified format */	
	public static String date( Integer daysRelativeTo ) {
		return date (daysRelativeTo,DEFAULT_DATE_FORMAT);
	}

	/** returns date, relative to today, as a String, with the specified format */	
	public static String date( Integer daysRelativeTo, String format ) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR,daysRelativeTo.intValue());
		SimpleDateFormat sdFormat = new SimpleDateFormat(format);
		return sdFormat.format(c.getTime());
	}
		
  public static String execMacro(String name, Object[] params)
    throws
      SecurityException,
      NoSuchMethodException,
      IllegalArgumentException,
      IllegalAccessException,
      InvocationTargetException {

    Vector paramClasses = new Vector();
    for (int i = 0; i < params.length; i++) {   	
      paramClasses.add(params[i].getClass());
    }

    Method m =
      Macros.class.getMethod(
        name,
        (Class[]) paramClasses.toArray(new Class[0]));
    return (String) m.invoke(null, params);
  }
  
  public static String listMacros () {
  	StringBuffer strBuf = new StringBuffer();
  	Method[] method = Macros.class.getDeclaredMethods();
  	
  	// NOTE: l1 is outer-loop
  	l1: for ( int i=0; i<method.length; i++ ) {
  		String mName = method[i].getName();		
  		for ( int j=0; j<NON_MACRO_METHODS.length; j++ ) {
  			if ( mName.equals(NON_MACRO_METHODS[j])) {		
  				continue l1;
  			}
  		}

			strBuf.append(mName);
			strBuf.append(" (");
						
			int len = strBuf.length();
			Class[] params = method[i].getParameterTypes();
			//method[i].			
			for ( int j=0; j<params.length; j++ ) {
				strBuf.append(params[j]);
				strBuf.append(", ");
			}
			if ( len < strBuf.length() ) {
				strBuf.setLength(strBuf.length()-2);
			}
			strBuf.append(")\n");	
								
  	}
  	
  	return strBuf.toString();
  }
  
  public static void main ( String[] args ) {
  	try {
			System.out.println(listMacros());
			
      System.out.println(Macros.execMacro("today", new String[0]));
      
			System.out.println(Macros.execMacro("date", new Object[] { new String("-1")} ));
      System.out.println(Macros.execMacro("today", new Object[] { "Z"} ));                    
			System.out.println(Macros.execMacro("date", new Object[] { new Integer(0)} ));      
			System.out.println(Macros.execMacro("date", new Object[] { new Integer(+1)} ));      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
