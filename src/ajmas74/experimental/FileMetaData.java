package ajmas74.experimental;

import java.io.*;
import java.nio.channels.*;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class FileMetaData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try{
			File baseDir = new File ("/Users/ajmas/Desktop");
			File[] files = baseDir.listFiles();
					
			for ( File file : files ) {
				try {
					Path path = file.toPath();
					
					System.out.println( file.getAbsolutePath() );
					System.out.println( path );
					
					BasicFileAttributes attr = Files.readAttributes(path,BasicFileAttributes.class);
					System.out.println(attr.lastAccessTime());
					System.out.println(attr.lastModifiedTime());
					
					System.out.println("--------------------------------------------------------");
					
					UserDefinedFileAttributeView userAttrs = Files.getFileAttributeView(path,UserDefinedFileAttributeView.class);
					if ( userAttrs != null ) {
						for ( String name : userAttrs.list() ) {
							System.out.println(name);
						}
					}
				}
				catch ( java.nio.file.NoSuchFileException ex ) {
					System.err.println(ex.getMessage());
				}
			}
		}
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

}
