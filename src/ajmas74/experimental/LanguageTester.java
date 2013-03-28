/*
 * Created on 06-Sep-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ajmas74.experimental;

import java.util.*;

/**
 * @author ajmas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LanguageTester {

	public static void main(String[] args) {
      Properties prop = System.getProperties();
      String[] propNames = (String[]) prop.keySet().toArray(new String[0]);
      Arrays.sort(propNames);
      for ( int i=0;i<propNames.length;i++) {
      	System.out.println(propNames[i]+"="+System.getProperty(propNames[i]));
      }
      System.out.println("--END--");
      //prop.list(System.out); 		
	}
}
