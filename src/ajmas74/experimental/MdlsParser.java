package ajmas74.experimental;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MdlsParser {

	public static Map<String, Object> getMetaData(File file) throws IOException {

		Process proc = Runtime.getRuntime().exec(
				new String[] { "mdls", file.getAbsolutePath() });

		BufferedReader stdOut = new BufferedReader(new InputStreamReader(
				proc.getInputStream()));

		String s = null;

		List<String> lines = new ArrayList<String>();

		// read any errors from the attempted command
		while ((s = stdOut.readLine()) != null) {
			lines.add(s);
		}
		return parseMdls(lines);
	}

	static Map<String, Object> parseMdls(List<String> lines) {

		boolean split = true;
		String key = null;

		List<String> members = null;

		Map<String, Object> metaDataMap = new HashMap<String, Object>();

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			if (line.startsWith("mdls: ")) {
				break;
			}
			if (split) {
				System.out.println("line: " + line);
				String[] parts = line.split("=");
				key = parts[0].trim();
				String value = parts[1].trim();

				if (value.equals("(null)")) {
					continue;
				}
				if (value.equals("(")) {
					split = false;
					members = new ArrayList<String>();
					continue;
				} else {
					metaDataMap.put(key, value);
				}

				// System.out.println( key + ": " + value);
			} else if (line.startsWith(")")) {
				split = true;

				metaDataMap.put(key, members);
				members = null;
			} else {
				line = line.trim();
				if (line.endsWith(",")) {
					line = line.substring(0, line.length() - 1);
				}
				members.add(line);
			}

		}

		return metaDataMap;
	}

	static Object getMetaData(File file, String key) throws IOException {

		Process proc = Runtime.getRuntime().exec(
				new String[] { "mdls", "-name", key, file.getAbsolutePath() });

		BufferedReader stdOut = new BufferedReader(new InputStreamReader(
				proc.getInputStream()));

		String s = null;

		List<String> lines = new ArrayList<String>();

		// read any errors from the attempted command
		while ((s = stdOut.readLine()) != null) {
			lines.add(s);
		}

		return parseMdls(lines).get(key);
	}

	public static void main(String[] args) {
		try {
			System.out.println(getMetaData(new File(
					"/Users/ajmas/Desktop/sexy-female-abs.jpg")));
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