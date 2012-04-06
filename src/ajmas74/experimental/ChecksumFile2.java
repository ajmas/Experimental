package ajmas74.experimental;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Properties;

import org.ibex.crypto.Base64;

import jonelo.sugar.util.Base32;

public class ChecksumFile2 {

	public static final String TIGER = "tiger";
	public static final String MD5 = "MD5";
	public static final String TTH = "tth";
	
    public final static int KB = 1024;
    public final static int MB = 1024*KB;
    public final static long GB = 1024*MB;

	public static final int BLOCK_SIZE = 1 * MB;

	public static byte[] createChecksum(File file, MessageDigest messageDigest) throws Exception {
		InputStream fis = new FileInputStream(file);

		byte[] buffer = new byte[BLOCK_SIZE];
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				messageDigest.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return messageDigest.digest();
	}

	// see this How-to for a faster way to convert
	// a byte array to a HEX string
	public static String getMD5Checksum(String filename) throws Exception {
		return getChecksum(new File(filename),"MD5",16);
	}
	
	public static String getTTHChecksum(String filename) throws Exception {
		return getChecksum(new File(filename),"TTH",16);
	}
	
//	public static String getTigerChecksum(String filename) throws Exception {
//		return getChecksum(new File(filename),"MD5",16);
//	}
	
	
	public static String getTTHChecksum(File file) throws Exception {
		return getChecksum(file,"TTH",16);
	}
	
	public static String getMD5Checksum(File file) throws Exception {
		return getChecksum(file,"MD5",16);
	}
	
	public static String getSHAChecksum(File file) throws Exception {
		return getChecksum(file,"SHA",16);
	}
	
	
	public static String getChecksum(File file, String algorithm, int base) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		return getChecksum(file,messageDigest,base);
	}
	
	public static String getChecksum(File file, MessageDigest messageDigest, int base) throws Exception {
		byte[] bytes = createChecksum(file,messageDigest);
		
		String result = "";
		
		if ( base == 16 ) {
			result = new BigInteger(1,bytes).toString(16);
		} 
		else if ( base == 32 ) {
			result = Base32.encode(bytes);
		}
		else if ( base == 64 ) {
			result = new String(Base64.encode(bytes),Charset.forName("ASCII"));
		}
		return result;
	}

	static class MyProvider extends Provider {

		/**  */
		private static final long serialVersionUID = 1L;

		protected MyProvider(String name, double version, String info) {
			super(name, version, info);
		}
		
	}
	
	static class MetaFilenameFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return filename.endsWith(".xml");
		}
		
	}
	
	static Properties loadProperties ( File file ) throws IOException {
		InputStream in = null;
		Properties properties = null;

		if ( file.exists() ) {
			try {
				in = new FileInputStream(file);
				properties= new Properties();
				properties.loadFromXML(in);
			}
			finally {
				if ( in != null ) {
					in.close();
				}
			}
		} else {
			properties = new Properties();
		}
		return properties;
	}
	
	static void writeMeta(File file, Properties properties) throws IOException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);

			properties.storeToXML(out, null, "UTF-8");
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}

	}
	
	static {
		MyProvider provider = new MyProvider("AJ's Provider",0.1,"pure expirementation");
		provider.put("MessageDigest.tiger", com.limegroup.gnutella.security.Tiger.class.getName() );
		provider.put("MessageDigest.TTH", com.limegroup.gnutella.security.TigerTree.class.getName() );
		
		Security.addProvider(provider);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		MyProvider provider = new MyProvider("AJ's Provider",0.1,"pure expirementation");
//		provider.put("MessageDigest.tiger", com.limegroup.gnutella.security.Tiger.class.getName() );
//		provider.put("MessageDigest.TTH", com.limegroup.gnutella.security.TigerTree.class.getName() );
//		
//		Security.addProvider(provider);
		
		try {
			FilenameFilter filter = new MetaFilenameFilter();
			File baseFolder = new File ("/Users/ajmas/Pictures/gwm-auto");
			File[] files = baseFolder.listFiles();
			for ( File file : files ) {
				if ( file.isDirectory() ) {
					//System.out.println("dir: ")
					File[] filesB = file.listFiles(filter);
					for ( File fileB : filesB ) {
						
						System.out.print( file.getName() );
						
						String name = fileB.getName();
						name = name.substring(0, name.indexOf(".xml"));
						
						File dataFile = new File (fileB.getParentFile(),name );
						
						if ( dataFile.exists() ) {
							
							Properties properties = loadProperties(fileB);
								
							boolean doChecksum = false;
							
							if ( properties.getProperty("checksum.md5") == null
									|| properties.getProperty("checksum.md5") == null ) {
								doChecksum = true;	
								System.out.print( " -- no checksum, will calculate");
							}
							
							if ( properties.getProperty("file.size") == null
									|| Integer.parseInt(properties.getProperty("file.size")) != dataFile.length() ) {
								doChecksum = true;	
								System.out.print( " --  different or missing checksum file size");
							}
							
							if ( doChecksum ) {								
								System.out.print( " -- calculating checksum");
								
								String md5Checksum = getMD5Checksum( dataFile );
								String tthChecksum = getChecksum( dataFile, "TTH", 16 );
								
								properties.setProperty("checksum.md5", md5Checksum);
								properties.setProperty("checksum.tth", tthChecksum);
								properties.setProperty("file.size", dataFile.length() + "");
								
								writeMeta(fileB, properties);
								
							}
							Thread.yield();
							
						}
						else {
							System.out.print( " -- !!! data file is missing" );
						}
						
						
						System.out.println("");
						
					}
					
					
				}
				
			}
		} 
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
//		
//		// http://docstore.mik.ua/orelly/java-ent/security/ch09_03.htm
//		
//		MyProvider provider = new MyProvider("AJ's Provider",0.1,"pure expirementation");
//		provider.put("MessageDigest.tiger", com.limegroup.gnutella.security.Tiger.class.getName() );
//		provider.put("MessageDigest.TTH", com.limegroup.gnutella.security.TigerTree.class.getName() );
//		
//		Security.addProvider(provider);
//		
//		System.out.println(Arrays.toString(Security.getProviders()));
//		
//		try {
//			//String file = "/zigidoo.png";
//			String file = "/Volumes/Blue Mana/media/videos/tluda/fbb/By Name/Lindsey Cope/Cope_Army_Abs_and_Legs_HD1080.wmv";
//			
//			String checksum = getChecksum(file,"TTH",16);
//			System.out.println(checksum);
//			
//			byte[] bytes = new byte[checksum.length()/2];
//			int len = checksum.length();
//			for ( int i=0; i<len; i+=2) {
//				bytes[i/2] = (byte) Integer.parseInt( checksum.substring(i,i+2) ,16);
//			}
//			checksum = Base32.encode(bytes);
//			System.out.println(checksum);
//			
//			checksum = getChecksum(file,"TTH",32);
//			System.out.println(checksum);
//			
//			checksum = getChecksum(file,"TTH",64);
//			System.out.println(checksum);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}

//   :filename:target_dir:size:tth:flags:ctime:prio:seq
// +T:Cope_Army_Abs_and_Legs_HD1080.wmv::79702069:XQY7W4LQOLSBNPMBPLAWT6ZBLQMFHUBVRMCDEXA:0:1297473139:3:28111
// +T:Cope_Army_Abs_and_Legs_HD1080.wmv::79724361:HJHVFSZIM5F3VRXK3AJDOEZQU7JBCIHXXF6Z3GI:0:1297462816:3:28111
                                                  

// Cope_Army_Abs_and_Legs_HD1080.wmv:6TCBM3Q2XORRVOHUFLGMUNKEL2IOUSECQQFHXWQ{TTH32}:F4C4166E1ABBA31AB8F42ACCCA35445E90EA4882840A7BDA{TTH16}:E
// Cope_Army_Abs_and_Legs_HD1080.wmv:/Volumes/Blue Mana/media/videos/tluda/fbb/By Name/Lindsey Cope/Cope_Army_Abs_and_Legs_HD1080.wmv:79702069:1291344313000:F4C4166E1ABBA31AB8F42ACCCA35445E90EA4882840A7BDA


// ref: http://sourceforge.net/projects/tigertree/
