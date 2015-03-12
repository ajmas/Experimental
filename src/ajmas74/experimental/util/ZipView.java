package ajmas74.experimental.util;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


/**
 * Be sure to sure to change environment to have
 * LC_CTYPE=UTF-8, otherwise non-latin names will
 * be question marks.
 * 
 * @author ajmas
 *
 */
public class ZipView {

	
	
	static String parentPath (String filePath) {
		String[] parts = filePath.split("/");
		String path = "";
		
		for (int i=0; i<parts.length-1; i++) {
			path += parts[i] + "/";
		}
		
		return path;		
	}
	
	public static void main(String[] args) throws IOException {
		
		// Planned params - keep similar to jar command
		//  - encoding
		//  - list -l
		//  - extract -x
		//  - filename -f
		//  - verbose
		//  - extract basedir
			
		
		System.out.println("file.encoding: " + System.getProperty("file.encoding"));
		
		String filePath = "..."; 
		
		Charset charset = Charset.forName("Shift_JIS");
		File outputFolder = new File("/tmp/def/");
		
		ZipFile zipFile = null;
		boolean listOnly = true;
		try {
			zipFile = new ZipFile(filePath, charset);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String filename = entry.getName();
				
				
				// deal with the case of some windows sources putting
				// paths in using backslashes
				
				String normalisedPath = filename;				
				boolean directory = entry.isDirectory();
				if (filename.indexOf("\\") > -1) {
					normalisedPath = normalisedPath.replace("\\", "/");
					if (normalisedPath.endsWith("/")) {
						directory = true;
					}
				}
								
				File fsObject = new File(outputFolder, normalisedPath);
				if (directory) {
					System.out.println("[DIR ] " + normalisedPath);
					if (!listOnly) {
						if (!fsObject.exists()) {
							fsObject.mkdirs();
						}
					}
				} else {
					System.out.println("[FILE] " + normalisedPath);
					if (!listOnly) {
						InputStream stream = zipFile.getInputStream(entry);
						
						FileOutputStream fOut = new FileOutputStream(fsObject);
						
						byte[] buffer = new byte[2048];
						int len = -1;
						while ((len = stream.read(buffer)) > -1) {
							fOut.write(buffer, 0, len);
						}
						
						stream.close();
						fOut.flush();
						fOut.close();
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {			
			zipFile.close();
		}
		
		
	}

}
