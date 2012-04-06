package ajmas74.experimental;

import java.io.*;
import java.util.*;
import java.text.*;
public class MovieDuration {

	static float getDuration ( File file ) {
	    try {
			
			Process proc = Runtime.getRuntime().exec(
					new String[] { "ffmpeg", "-i", file.getAbsolutePath() });
	
			String s = null;
	
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));
			
			// read any errors from the attempted command
			while ((s = stdOut.readLine()) != null) {
				//System.out.println(s);
				if ( s.trim().startsWith("Duration") ) {
				    float duration = -1f;
				    
				    s = s.trim();
				    s = s.substring( s.indexOf ( ":" )+1 );
				    s = s.substring( 0, s.indexOf ( "," ) ) ;
				    System.out.println("s: " + s);
				    String[] parts = s.trim().split(":");
				    
				    duration = Integer.parseInt(parts[0]) * 60 * 60 ;
				    duration += Integer.parseInt(parts[1]) * 60  ;
				    duration += Float.parseFloat(parts[2])  ;
				    
				    return duration;
				}
				
				
			}	    
			
	    } catch ( Exception ex ) {
	       ex.printStackTrace();
	    }
	    return -1;
	}
	
	static  void writeAttribute( File file, String attrName, String attrValue ) throws IOException {
		
		Process proc = Runtime.getRuntime().exec(
				new String[] { "xattr", "-w", attrName, attrValue, file.getAbsolutePath() });
		
		String s = null;
		
		BufferedReader stdOut = new BufferedReader(new InputStreamReader(
				proc.getErrorStream()));
		
		// read any errors from the attempted command
		while ((s = stdOut.readLine()) != null) {
			System.out.println("err: " + s);
		}
	}
	
	
	
	static Map<String, Object> getMetaData ( File file) throws IOException {
		Process proc = Runtime.getRuntime().exec(
				new String[] { "xattr", "-l", file.getAbsolutePath() });
		
		String s = null;
		
		BufferedReader stdOut = new BufferedReader(new InputStreamReader(
				proc.getErrorStream()));
		
		// read any errors from the attempted command
		while ((s = stdOut.readLine()) != null) {
			System.out.println("err: " + s);
		}
		
		return null;
	}
	
	static String getSuffix ( File file ) {
		String name = file.getName();
		int idx = name.lastIndexOf('.');
		if ( idx < 0 ) {
			return "";
		}
		return name.substring(idx);
	}
	
	public static void main ( String[] args ) {
//		System.out.println( "duration: " + getDuration(new File("/Users/ajmas/Movies/")) );
		
		// -------------------------------------------------
		
		
		DecimalFormat formatter = new DecimalFormat("#.000");
		
		Set<String> supportedTypes = new HashSet<String>();
		supportedTypes.add(".mp4");
		supportedTypes.add(".flv");
		supportedTypes.add(".wmv");
		supportedTypes.add(".m4v");
		supportedTypes.add(".mpg");
		supportedTypes.add(".mpeg");
		supportedTypes.add(".avi");
		supportedTypes.add(".ts");
		supportedTypes.add(".mkv");
		supportedTypes.add(".divx");
		supportedTypes.add(".iso");
		supportedTypes.add(".vob");
		
		List<File> unvistedFolders = new LinkedList<File>();
		
		File baseFolder = new File ("/Volumes/Blue Mana/media/videos/tluda/main");		
		File folder = null;
		
		unvistedFolders.add(baseFolder);
		
		while ( ! unvistedFolders.isEmpty() ) {
			folder = unvistedFolders.remove(0); 
					
			File[] files = folder.listFiles();
			for ( File file : files ) {
				String name = file.getName();
				String suffix = getSuffix(file);
				
				System.out.println("name: " + file.getAbsolutePath());
				System.out.println("suffix: " + suffix);
				System.out.println("file: " + file.isFile());
				
				if ( file.isDirectory() ) {
					unvistedFolders.add(file);
				}
				else if ( supportedTypes.contains(suffix.toLowerCase()) ) {
					
					try {
						String durationStr = (String) MdlsParser.getMetaData(file,"kMDItemDurationSeconds");
						if ( durationStr == null || durationStr.trim().length() == 0 ) {
							float duration = getDuration(file);
							if ( duration > 0 ) {
								System.out.println("writing attr: " + formatter.format(duration));
								writeAttribute(file,"com.apple.metadata:kMDItemDurationSeconds",formatter.format(duration));
							}
							System.out.println("movie duration: " + duration);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("---------------------");
			}
		}
	}
}
