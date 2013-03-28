/* Created on 27-Jun-2003 */
package ajmas74.experimental;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * 
 * Note if you want to be able to deal with entries with unicode character
 * names, then you will need to use JDK 1.4.x to write them. Tests with JDK
 * 1.3.x show that it is unable to create unicode file names and folders. This
 * is at least true for MS-Windows, though I have not tested this for other
 * platforms.
 * 
 * @author Andre-John Mas
 * 
 */
public class ZipReader2 {

	private static void listContents(File theFile) {
		ZipInputStream zin = null;
		try {
			zin = new ZipInputStream(new FileInputStream(theFile));

			ZipEntry entry = null;
			while ((entry = zin.getNextEntry()) != null) {
				System.out.println(entry);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (zin != null) {
					zin.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void extractContents(File theFile, File dest) {
		int fileIndex = 1;
		Map convertedEntriesMap = new HashMap();

		ZipFile zipFile = null;
		try {
			byte[] buffer = new byte[4096];
			int len = -1;

			// fin = new FileInputStream(theFile);
			zipFile = new ZipFile(theFile);
			Enumeration entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				// ZipEntry entry = zipFile.getEntry(entryName);
				InputStream in = zipFile.getInputStream(entry);

				// System.out.println(entry.getName());

				String entryName = entry.getName();
				String folderName = "";
				if (entryName.indexOf("\\") > -1) {
					folderName = entryName
							.substring(0, entryName.indexOf("\\"));
				} else if (entryName.indexOf("/") > -1) {
					folderName = entryName.substring(0, entryName.indexOf("/"));
				}

				boolean containsUTF = false;
				// for ( int i=0; i<entryName.length(); i++ ) {
				// if ( ((int)entryName.charAt(i)) > 256 ) {
				// containsUTF = true;
				// break;
				// }
				// }

				File folder = new File(dest, folderName);
				if (!folder.exists()) {
					folder.mkdir();
				}

				File f = new File(dest, entry.getName());

				FileOutputStream fout = null;
				// for dealing with UTF8 names entries
				// (not an issue with JDK 1.4.x)
				try {
					fout = new FileOutputStream(f);
				} catch (FileNotFoundException ex1) {
					// had originally put 'folder' as first param, but
					// this also had the danger of having UTF8 chars
					// in it
					f = new File(dest, "0000_RENAMED_0000_" + fileIndex++);
					fout = new FileOutputStream(f);
					containsUTF = true;
					convertedEntriesMap.put(entry.getName(),
							f.getAbsolutePath());
				}

				while ((len = in.read(buffer)) > -1) {
					fout.write(buffer, 0, len);
				}
				fout.close();

				System.out.print(" entry '" + entry.getName()
						+ "' written to: " + f);
				if (containsUTF) {
					System.out
							.println(" - converted file name since it contained characters that "
									+ " were either not supported by the file system or by the Java implementation");
				} else {
					System.out.println();
				}

			}
			zipFile.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		List files = new Vector();
		if (args.length >= 2) {
			for (int i = 1; i < args.length; i++) {
				files.add(args[i]);
			}

			if (args[0].equals("-x")) {
				extractContents(new File((String) files.get(0)), new File(
						"d:/temp/out"));
			} else if (args[0].equals("-l")) {
				listContents(new File((String) files.get(0)));
			}

		} else {
			System.out.println("ZipReader [-x|-l] file1 ... fileN");
		}
	}
}
