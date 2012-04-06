package ajmas74.experimental;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

import org.ibex.crypto.Base64;

import jonelo.sugar.util.Base32;

public class ChecksumFile {

    public final  int KB = 1024;
    public final  int MB = 1024*KB;
    public final  long GB = 1024*MB;

	public  final int BLOCK_SIZE = 1 * MB;

	public ChecksumFile () {
		MyProvider provider = new MyProvider("AJ's Provider",0.1,"pure expirementation");
		provider.put("MessageDigest.tiger", com.limegroup.gnutella.security.Tiger.class.getName() );
		provider.put("MessageDigest.TTH", com.limegroup.gnutella.security.TigerTree.class.getName() );
		
		Security.addProvider(provider);
	}
	
	public  byte[] createChecksum(String filename, MessageDigest messageDigest) throws Exception {
		InputStream fis = new FileInputStream(filename);

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
	public  String getMD5Checksum(String filename) throws Exception {
		return getChecksum(filename,"MD5",16);
	}
	
	public  String getChecksum(String filename, String algorithm, int base) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		return getChecksum(filename,messageDigest,base);
	}
	
	public  String getChecksum(String filename, MessageDigest messageDigest, int base) throws Exception {
		byte[] bytes = createChecksum(filename,messageDigest);
		
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
		private static  final long serialVersionUID = 1L;

		protected MyProvider(String name, double version, String info) {
			super(name, version, info);
		}
		
	}
	
	/**
	 * @param args
	 */
	public  void main(String[] args) {

		// http://docstore.mik.ua/orelly/java-ent/security/ch09_03.htm
		

		ChecksumFile checksumFile = new ChecksumFile();
		
		System.out.println(Arrays.toString(Security.getProviders()));
		
		try {
			//String file = "/zigidoo.png";
			String file = "/Volumes/Blue Mana/media/videos/tluda/fbb/By Name/Lindsey Cope/Cope_Army_Abs_and_Legs_HD1080.wmv";
			
			String checksum = checksumFile.getChecksum(file,"TTH",16);
			System.out.println(checksum);
			
			byte[] bytes = new byte[checksum.length()/2];
			int len = checksum.length();
			for ( int i=0; i<len; i+=2) {
				bytes[i/2] = (byte) Integer.parseInt( checksum.substring(i,i+2) ,16);
			}
			checksum = Base32.encode(bytes);
			System.out.println(checksum);
			
			checksum = checksumFile.getChecksum(file,"TTH",32);
			System.out.println(checksum);
			
			checksum = checksumFile.getChecksum(file,"TTH",64);
			System.out.println(checksum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

//   :filename:target_dir:size:tth:flags:ctime:prio:seq
// +T:Cope_Army_Abs_and_Legs_HD1080.wmv::79702069:XQY7W4LQOLSBNPMBPLAWT6ZBLQMFHUBVRMCDEXA:0:1297473139:3:28111
// +T:Cope_Army_Abs_and_Legs_HD1080.wmv::79724361:HJHVFSZIM5F3VRXK3AJDOEZQU7JBCIHXXF6Z3GI:0:1297462816:3:28111
                                                  

// Cope_Army_Abs_and_Legs_HD1080.wmv:6TCBM3Q2XORRVOHUFLGMUNKEL2IOUSECQQFHXWQ{TTH32}:F4C4166E1ABBA31AB8F42ACCCA35445E90EA4882840A7BDA{TTH16}:E
// Cope_Army_Abs_and_Legs_HD1080.wmv:/Volumes/Blue Mana/media/videos/tluda/fbb/By Name/Lindsey Cope/Cope_Army_Abs_and_Legs_HD1080.wmv:79702069:1291344313000:F4C4166E1ABBA31AB8F42ACCCA35445E90EA4882840A7BDA


// ref: http://sourceforge.net/projects/tigertree/
