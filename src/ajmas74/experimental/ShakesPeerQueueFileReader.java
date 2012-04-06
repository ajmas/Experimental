package ajmas74.experimental;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jonelo.sugar.util.Base32;

import ajmas74.experimental.ChecksumFile.MyProvider;

public class ShakesPeerQueueFileReader {

	Map<String,String> localFileChecksumMap = new HashMap<String,String>();
	Map<String,String> reverseLocalFileChecksumMap = new HashMap<String,String>();
	
	void readFile ( String filename ) throws IOException {
		
		File file = new File(filename);
//		if ( !file.exists() ) {
//			throw new FileNotFoundException("File not found: " + filename);
//		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			BufferedReader reader = new BufferedReader( new InputStreamReader(in,"UTF8") );
			String line = null;
			while ( (line = reader.readLine()) != null ) {
				String[] parts = line.split(":");
				if ( "+T".equals(parts[0]) ) {
					
					String filePath = parts[1];

					int idx = filePath.lastIndexOf("/");
					if ( idx == -1 ) {
						idx = 0;
					}
					
//					String fileName = filePath.substring(idx);
					
					String str = localFileChecksumMap.get(parts[4]);
					
					if ( str != null ) {
						System.out.println("A: " + filePath + " is in the queue");
					}
					
					str = reverseLocalFileChecksumMap.get(parts[1]);
					
					if ( str != null ) {
						System.out.println("B: " + filePath + " is in the queue");
					}
					
//					System.out.println("File ......... " + parts[1] );
//					System.out.println("TTH .......... " + parts[4] );
//					System.out.println();
					
					
				}
				else if ( "+S".equals(parts[0]) ) {
					
				}
				else if ( "+D".equals(parts[0]) ) {
					
				}
				else if ( "=R".equals(parts[0]) ) {
					
				}
			}
		}
		finally {
			if ( in != null ) {
				in.close();
			}
		}
	}
	
	
	
	void scanDirectory ( String directoryPath ) throws Exception {
		System.out.println("Scanning dir: " + directoryPath);
		List<File> unscannedFolders = new ArrayList<File>();
		
		unscannedFolders.add(new File(directoryPath));
		
		
		while ( unscannedFolders.size() > 0 ) {
			File folder = unscannedFolders.remove(0);
			File[] files = folder.listFiles();
			for ( File fileOrFolder : files ) {
				if ( fileOrFolder.isDirectory() ) {
					//System.out.println("Found folder: " + fileOrFolder);
					unscannedFolders.add(fileOrFolder);
				}
				else {
					System.out.print(fileOrFolder.getName() + ":");
//					System.out.print(fileOrFolder.getAbsolutePath() + ":");
//					System.out.print(fileOrFolder.length() + ":");
//					System.out.print(fileOrFolder.lastModified() + ":");
//					
					String path = fileOrFolder.getAbsolutePath();
					
					if ( reverseLocalFileChecksumMap.get(path) != null ) {
						String checksum = reverseLocalFileChecksumMap.get(path);
						byte[] bytes = new byte[checksum.length()/2];
						int len = checksum.length();
//						System.out.println("checksum: " + checksum);
						for ( int i=0; i<len; i+=2) {
//							String substring = checksum.substring(i,i+2);
//							System.out.println( ">>" + substring + "<<");
//							System.out.println("len: " + Integer.parseInt(substring,16) + " -- " + substring)	;
							
							bytes[i/2] = (byte) Integer.parseInt( checksum.substring(i,i+2) ,16);
						}
						System.out.print( Base32.encode(bytes) + "{TTH32}:" ) ;
						System.out.println( checksum + "{TTH16}" + ":E");
					}
					else {			
						String checksum = ChecksumFile.getChecksum(path, "TTH",16);
						localFileChecksumMap.put(checksum.toUpperCase(), path);
						System.out.println(checksum.toUpperCase() + ":N");
						
						//writeToChecksumFile(fileOrFolder,checksum);
					}
				}
			}
		}
		
	}
	
	String checksumFilePath = "data/checksums.data";
	
	void writeToChecksumFile ( File file, String checksum ) throws IOException {
		File checksumFile = new File(checksumFilePath);
		//System.out.println("writing to: " + checksumFile.getAbsolutePath() );
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(checksumFile, "rws");

			raf.seek(checksumFile.length());

			PrintWriter writer = new PrintWriter( new OutputStreamWriter( new FileOutputStream ( raf.getFD() ),"UTF-8")); //checksumFilePath ), "UTF-8")); 

			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append( file.getName() + ":" );
			strBuilder.append( file.getAbsolutePath() + ":");
			strBuilder.append( file.length() + ":");
			strBuilder.append( file.lastModified() + ":");
			strBuilder.append( checksum.toUpperCase() );
			
			writer.println( strBuilder );
			writer.flush();
									
		}
		finally {
			if ( raf != null ) {
				raf.close();
			}
		}
	}
	
	void readChecksumFile ( ) throws IOException {
		InputStream in = null;
		try {
			in = new FileInputStream(checksumFilePath);
			BufferedReader reader = new BufferedReader ( new InputStreamReader(in,"UTF-8") );
			String line = null;
			while ( (line = reader.readLine() ) != null ) {
				String[] parts = line.split(":");
//				System.out.println("parts[1]= " + parts[1]);
//				System.out.println("parts[4]= " + parts[4]);
				localFileChecksumMap.put(parts[4], parts[1]);
				reverseLocalFileChecksumMap.put(parts[1], parts[4]);
			}
		}
		finally {
			if ( in != null ) {
				in.close();
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MyProvider provider = new MyProvider("AJ's Provider",0.1,"pure expirementation");
		provider.put("MessageDigest.TTH", "org.alliance.core.file.hash.Tiger" );
		
		Security.addProvider(provider);
		
		ShakesPeerQueueFileReader myReader = new ShakesPeerQueueFileReader();
		
		try {
			myReader.readChecksumFile();
//			myReader.writeToChecksumFile(new File("/zigidoo.png"), "xxxxX");
			
			System.out.println("*** Checksumming local files system...");
			myReader.scanDirectory("/Volumes/Blue Mana/media/videos/tluda/fbb/");
			System.out.println("*** Checking to see if they are in the queue...");
			myReader.readFile("/Users/ajmas/Library/Application Support/ShakesPeer/queue2.db");
			
//			BigInteger.
		}
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

}
