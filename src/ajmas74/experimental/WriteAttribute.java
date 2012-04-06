package ajmas74.experimental;

import java.io.File;
import java.io.IOException;

public class WriteAttribute {

	static  void writeAttribute( File file, String attrName, String attrValue ) throws IOException {
		
		Process proc = Runtime.getRuntime().exec(
				new String[] { "xattr", "-w", attrName, attrValue, file.getAbsolutePath() });
		
	}
	
	public static void main ( String[] args ) {
		
		try {
			writeAttribute ( new File ("/Users/ajmas/Desktop/test.m3u"),"com.apple.metadata:kMDItemWhereFroms","( \"http://myhost/\" )");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
