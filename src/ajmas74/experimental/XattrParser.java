package ajmas74.experimental;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XattrParser {


	public static Map<String, Object> getMetaData(File file) throws IOException {

		Process proc = Runtime.getRuntime().exec(
				new String[] { "xattr","-l", file.getAbsolutePath() });

		BufferedReader stdOut = new BufferedReader(new InputStreamReader(
				proc.getInputStream()));

		String s = null;

		List<String> lines = new ArrayList<String>();

		// read any errors from the attempted command
		while ((s = stdOut.readLine()) != null) {
			lines.add(s);
		}
		return parseXattr(lines);
	}

	static Map<String, Object> parseXattr(List<String> lines) {

		boolean split = true;

		Map<String, Object> metaDataMap = new HashMap<String, Object>();

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			
			if (split) {
				System.out.println("line: " + line);
				
				int idx = line.indexOf(": ");
				String key = line.substring(0,idx);
				String value = line.substring(idx+2).trim();

				if (value.equals("(null)")) {
					continue;
				}
				if (value.startsWith("(")) {
					split = false;
					//members = new ArrayList<String>();
					
					// TODO deal with arrays
					metaDataMap.put(key, value);
				} else {
					metaDataMap.put(key, value);
				}

				// System.out.println( key + ": " + value);
			} 

		}

		return metaDataMap;
	}

	static Object getMetaData(File file, String key) throws IOException {

		Process proc = Runtime.getRuntime().exec(
				new String[] { "xattr", "-p", key, file.getAbsolutePath() });

		BufferedReader stdOut = new BufferedReader(new InputStreamReader(
				proc.getInputStream()));

		String s = null;

		List<String> lines = new ArrayList<String>();

		// read any errors from the attempted command
		while ((s = stdOut.readLine()) != null) {
			if ( s.startsWith("xattr: ") ) {
				return null;
			}
			else {
				return s;
			}
			
		}

		return null;
		
	}

	public static void main(String[] args) {
		try {
			System.out.println( getMetaData(new File(
					"/Volumes/Little Black/Pictures/Female Fitness/Larissa Reis/zzzzzzzz.jpg"),"com.apple.metadata:kMDItemDurationSeconds"));
			
			System.out.println(getMetaData(new File(
					"/Volumes/Little Black/Pictures/Female Fitness/Larissa Reis/zzzzzzzz.jpg")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

// ref: http://sage.ucsc.edu/~wgscott/xtal/wiki/index.php/Extended_Attributes
// ref:
// http://www.iptc.org/std/Iptc4xmpCore/1.0/specification/Iptc4xmpCore_1.0-spec-XMPSchema_8.pdf
// ref: http://wiki.creativecommons.org/XMP_Implementations
// ref: http://www.iptc.org/site/Photo_Metadata/IPTC_Core_&_Extension/
/*


*/