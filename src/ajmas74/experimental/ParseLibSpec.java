package ajmas74.experimental;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParseLibSpec {

	private static final String FILENAME = "data/gl.spec";
	
	// http://www.opengl.org/registry/
	
	private void readFile ( String fileName ) throws IOException {
	
		Map<String,List<String>> versionMap = new HashMap<String,List<String>>();
		
		InputStream in = null;
		try {
			in = new FileInputStream(fileName);
			
			BufferedReader reader = new BufferedReader( new InputStreamReader( in ));
			
			String line = null;
			boolean inBlock = false;
			
			String function = null;
			String version = null;
			boolean extension = true;
			
			while ( ( line = reader.readLine()) != null ) {
				
				if ( line.startsWith("#") ) {
					// ignore this, it is a comment
					continue;
				}
				else if ( line.startsWith("passthru:") ) {
					continue;
				}
				else if ( line.trim().length() == 0 ) {
					if ( inBlock && !extension) {

						
						List<String> fnList = versionMap.get(version);
						if ( fnList == null ) {
							fnList = new ArrayList<String>();
							versionMap.put(version,fnList);
						}
						fnList.add(function);
						
					}
					inBlock = false;
					extension = true;
					continue;
				}				
				else if ( inBlock ) {
					if ( line.trim().startsWith("version") ) {
						String[] parts = line.trim().split("\t");
						version = parts[ parts.length-1];
					}
					else if ( line.trim().startsWith("category")) {
						String[] parts = line.trim().split("\t");
						if ( parts[ parts.length-1].startsWith("VERSION_") ) {
							extension = false;
						}
						continue;
					}
				}
				else if ( line.indexOf('(') > -1 ) {
					inBlock = true;
					function = line.substring(0, line.indexOf('('));
	
				}
				
			}
			
		} finally {
			if ( in != null ) {
				in.close();
			}
		}
		

					
//		for ( String version : versionList ) {
//			String key = version;
//			System.out.println ( "OpenGL " + key + ": ");
//			
//			List<String> fnList = versionMap.get(key);
//			Collections.sort(fnList);
//			for ( String fn : fnList ) {
//				System.out.println ( "    gl" + fn );
//			}
//			System.out.println ();
//		}
		
		String[] versionList = versionMap.keySet().toArray( new String[0] );
		Arrays.sort(versionList);
		
		System.out.println("<opengl-features>");
		for ( String version : versionList ) {
			String key = version;
			//System.out.println ( "OpenGL " + key + ": ");
			System.out.println ( "  <feature-set category=\"OpenGL "+key+"\">");
			
			List<String> fnList = versionMap.get(key);
			Collections.sort(fnList);
			for ( String fn : fnList ) {
				System.out.println ( "    <fn>gl" + fn + "</fn>");
			}
			System.out.println ( "  </feature-set>");
		}	
		System.out.println("</opengl-features>");
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new ParseLibSpec().readFile(FILENAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
