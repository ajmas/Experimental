package ajmas74.experimental;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;


/**
 * Code to read DX data, based on file format specified at
 * http://isccp.giss.nasa.gov/pub/documents/d-doc.pdf
 * 
 * @author ajmas
 *
 */
public class DXRead {

	static final int RECLEN = 30720;
	static final int DXS1SZ = 5;
	static final int DXS2SZ = 5;
	static final int DXS3SZ = 7;
	static final int DXS4SZ = 9;
	static final int ADD1SZ = 10;
	static final int ADD3SZ = 3;

	class DXdataline {
		char[] dataline = new char[RECLEN];
	};

	class Prefix {
		long Iwest;
		long Ieast;
		long Inorth;
		long Isouth;
		long npix;
		long iout;
	};

	static abstract class Section {

		Section() {
			// System.out.println(getClass());
		}

		// http://stackoverflow.com/questions/10514077/how-to-validate-value-of-bit-in-byte-array-in-java
		boolean isSet(byte[] data, int index) {
			int bitIndex = index % 8;
			int byteIndex = index / 8;
			int bitMask = 1 << bitIndex;
			return (data[byteIndex] & bitMask) > 0;
		}
	}

	static class DxS1 extends Section {
		static final int DATA_LEN = 5;
		byte[] data = new byte[DATA_LEN];
		ByteBuffer byteBuffer;
		
		public DxS1(byte[] data) {
			this.data = data;
		}

		public DxS1(byte[] data, int start) {
			super();
			System.arraycopy(data, start, this.data, 0, DATA_LEN);
			byteBuffer = ByteBuffer.allocate(DATA_LEN);
			byteBuffer.put(this.data);
		}

		public boolean isNoDay() {
			return isSet(data, 0);
		}
		
		public boolean isShoreFlag() {
			return isSet(data, 1);
		}

		public boolean isLand() {
			return isSet(data, 2);
		}
		
		public int getIRRadiance() {
			return getUnsignedByte(3);
		}
		
		public int getFirstIRClearSkyRadiance() {
			return getUnsignedByte(4);
		}
		
		int getUnsignedByte(int offset) {
			return byteBuffer.get(offset) & 0xff;
		}
		
		// unsigned noday : 1;
		// unsigned bxshor : 1;
		// unsigned lndwtr : 1;
		// unsigned hitopo : 1;
		// unsigned snoice : 2;
		// unsigned timspa : 2;
		// unsigned icslog : 5;
		// unsigned bxithr : 3;
		// unsigned mue : 8;
		// unsigned irad : 8;
		// unsigned bxicsr : 8;
	};

	static class DxAdd1 extends Section {
		byte[] data = null;
		

		public DxAdd1(byte[] data) {
			this.data = data;
		}

		public DxAdd1(byte[] data, int start, int fieldCount) {
			super();
			this.data = new byte[fieldCount];
			System.arraycopy(data, 0, this.data, 0, fieldCount);
		}

		// unsigned arad[10];
	};

	static class DxS2 extends Section {
		static final int DATA_LEN = 5;
		byte[] data = new byte[DATA_LEN];

		public DxS2(byte[] data) {
			this.data = data;
		}

		public DxS2(byte[] data, int start) {
			super();
			System.arraycopy(data, 0, this.data, 0, DATA_LEN);
		}
		// unsigned glint : 1;
		// unsigned vcslog : 4;
		// unsigned bxvthr : 3;
		// unsigned mu0 : 8;
		// unsigned phi : 8;
		// unsigned vrad : 8;
		// unsigned bxvcsr : 8;
	};

	static class DxS3 extends Section {
		static final int DATA_LEN = 7;
		byte[] data = new byte[DATA_LEN];

		public DxS3(byte[] data) {
			this.data = data;
		}

		public DxS3(byte[] data, int start) {
			super();
			System.arraycopy(data, 0, this.data, 0, DATA_LEN);
		}
		// unsigned daynit : 1;
		// unsigned ithr : 3;
		// unsigned vthr : 3;
		// unsigned shore : 1;
		// unsigned iret : 4;
		// unsigned icsret : 4;
		// unsigned icsrad : 8;
		// unsigned itmp : 8;
		// unsigned iprs : 8;
		// unsigned icstmp : 8;
		// unsigned icsprs : 8;
	};

	static class DxAdd3 extends Section {
		static final int DATA_LEN = 3;
		byte[] data = new byte[DATA_LEN];

		public DxAdd3(byte[] data) {
			this.data = data;
		}

		public DxAdd3(byte[] data, int start) {
			super();
			System.arraycopy(data, 0, this.data, 0, DATA_LEN);
		}

		// unsigned nref : 8;
		// unsigned nthr : 8;
		// unsigned ncsref : 8;
	};

	static class DxS4 extends Section {
		static final int DATA_LEN = 9;
		byte[] data = new byte[DATA_LEN];

		public DxS4(byte[] data) {
			this.data = data;
		}

		public DxS4(byte[] data, int start) {
			super();
			System.arraycopy(data, 0, this.data, 0, DATA_LEN);
		}

		// unsigned vret : 4;
		// unsigned vcsret : 4;
		// unsigned vcsrad : 8;
		// unsigned valbta : 8;
		// unsigned vcsalb : 8;
		// unsigned vtmp : 8;
		// unsigned vprs : 8;
		// unsigned vtauic : 8;
		// unsigned vtmpic : 8;
		// unsigned vprsic : 8;
	};

	static class DxDataRec {

		// Prefix prefix;
		// short *lonbuf;
		// short *latbuf;
		// short *xbuf;
		// short *ybuf;

		int longitudeBuf;
		int latitudeBuf;
		int xBuf;
		int yBuf;

		DxS1 dxs1;
		DxAdd1 add1;
		DxS2 dxS2;
		DxS3 dxS3;
		DxAdd3 DxAdd3;
		DxS4 dxS4;
		
		public DxDataRec(int longitudeBuf, int latitudeBuf, int xBuf, int yBuf) {
			this.longitudeBuf = longitudeBuf;
			this.latitudeBuf = latitudeBuf;
			this.xBuf = xBuf;
			this.yBuf = yBuf;
		}
	};

	// static int bytes4ToInt(byte[] data, int offset) {
	// return new BigInteger(data,).intValue();
	// }

	public static void main(String[] args) throws IOException {
		args = new String[1];
		for (int i=0; i<=21; i+=3) {
			String val = "" + i;
			if (val.length() < 2) {
				val = "0" + val;
			}
			args[0] = "/Users/ajmas/Development/projects/NOM parser/data/ISCCP.DX.0.NOA-10A.1991.01.03." + val + "00.NOM";
			read(args);
		}
	}
	
	
	public static void readHeader(byte[] buffer) {
		int year = -1;
		int month = -1;
		int day = -1;
		int utc = -1;
		
		int satelliteId = -1;
		int satelliteType = -1;
		int channelCount = -1;
		boolean nightImage = false;
		
		boolean polarSatellite = false;
		
//		len = in.read(buffer, 0, RECLEN);

		String line = new String(buffer, 0, 80, Charset.forName("ascii"));
		int j = 0;
		year = Integer.parseInt(line.substring(j, j + 10).trim());
		//System.out.println(year);
		j += 10;
		month = Integer.parseInt(line.substring(j, j + 10).trim());
		//System.out.println(month);
		j += 10;
		day = Integer.parseInt(line.substring(j, j + 10).trim());
		//System.out.println(day);
		j += 10;
		utc = Integer.parseInt(line.substring(j, j + 10).trim());
		//System.out.println(utc);
		j += 10;
		satelliteId = Integer.parseInt(line.substring(j, j + 10)
				.trim());
		//System.out.println(satelliteId);
		j += 10;
		polarSatellite = (Integer.parseInt(line
				.substring(j, j + 10).trim()) > 0);
		//System.out.println(polarSatellite);
		j += 10;
		channelCount = Integer.parseInt(line.substring(j, j + 10)
				.trim());
		//System.out.println(channelCount);
		j += 10;
		channelCount = Integer.parseInt(line.substring(j, j + 10)
				.trim());
		//System.out.println(channelCount);
		
		for (int i = 2; i < 384; i++) {
			int offset = (i - 1) * 80;
			line = new String(buffer, offset, 80,
					Charset.forName("ascii"));
			
			System.out.println(line);
		}

	}
	
	public static void readRecord(DataInputStream in) {
		
	}
	
	public static void readFile(DataInputStream in) {
		
	}
	
	public static void read(String[] args) throws IOException {
		int headerLength = RECLEN;
		
		String outputImageFile = "MyFile.png";
		
		File f = new File("MyFile.png");

		boolean polarSatellite = false;

		int year = -1;
		int month = -1;
		int day = -1;
		int utc = -1;

		int satelliteId = -1;
		int satelliteType = -1;
		int channelCount = -1;
		boolean nightImage = false;

		FileInputStream fIn = null;
		DataInputStream in = null;
		byte[] buffer = new byte[headerLength];
		int len = -1;
		try {

			File file = new File(args[0]);//"/Users/ajmas/Development/projects/NOM parser/data/ISCCP.DX.0.NOA-10A.1991.01.02.0000.NOM");
			if (!file.exists()) {
				System.out.println("no such file: " + file);
				return;
			}
			fIn = new FileInputStream(file);
			
			in = new DataInputStream(fIn);

			// Read the header

			len = in.read(buffer, 0, headerLength);

			String line = new String(buffer, 0, 80, Charset.forName("ascii"));
			int j = 0;
			year = Integer.parseInt(line.substring(j, j + 10).trim());
			//System.out.println(year);
			j += 10;
			month = Integer.parseInt(line.substring(j, j + 10).trim());
			//System.out.println(month);
			j += 10;
			day = Integer.parseInt(line.substring(j, j + 10).trim());
			//System.out.println(day);
			j += 10;
			utc = Integer.parseInt(line.substring(j, j + 10).trim());
			//System.out.println(utc);
			j += 10;
			satelliteId = Integer.parseInt(line.substring(j, j + 10)
					.trim());
			//System.out.println(satelliteId);
			j += 10;
			polarSatellite = (Integer.parseInt(line
					.substring(j, j + 10).trim()) > 0);
			//System.out.println(polarSatellite);
			j += 10;
			channelCount = Integer.parseInt(line.substring(j, j + 10)
					.trim());
			//System.out.println(channelCount);
			j += 10;
			channelCount = Integer.parseInt(line.substring(j, j + 10)
					.trim());
			//System.out.println(channelCount);
			
			for (int i = 2; i < 384; i++) {
				int offset = (i - 1) * 80;
				line = new String(buffer, offset, 80,
						Charset.forName("ascii"));
				
				System.out.println(line);
			}

			int recordCount = (int) (file.length() / RECLEN);
			System.out.println("Record Count: " + recordCount);
			
			int width = 1440;
			int height = 550;
			BufferedImage image = null;
			if (f.exists()) {
				image = ImageIO.read(f);
			} else {
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			}
			recordCount--;
			for (int recIdx=0; recIdx < recordCount; recIdx++) {
				System.out.println("\nidx: " + 	recIdx);
				
				in.read(buffer,0,RECLEN);
				
				DataInputStream bIn = new DataInputStream(
						new ByteArrayInputStream(buffer,0,RECLEN));
				
				// Prefix area
								
				int iwest = bIn.readInt();
				System.out.println("iwest: " +iwest);

				int ieast = bIn.readInt();
				System.out.println("ieast: " +ieast);
				System.out.println("ieast-iwest: " + (ieast-iwest));
				
				int inorth = bIn.readInt();
				System.out.println("inorth: " + inorth);
				
				int isouth = bIn.readInt();
				System.out.println("isouth: " + isouth);
				System.out.println("inorth-isouth: " + (inorth-isouth));
				
				int npix = bIn.readInt();
				System.out.println("npix: " +npix);

				int iout = bIn.readInt();
				System.out.println("iout: " +iout);

				//npix = npix/2;

				int[] longitudes = new int[npix];
				int[] latitudes = new int[npix];
				int[] xPositions = new int[npix];
				int[] yPositions = new int[npix];
				
//				for (int i=0; i<npix; i++ ) {
//					longitudes[i] = bIn.readShort();
//				}
//				
//				for (int i=0; i<npix; i++ ) {
//					latitudes[i] = bIn.readShort();
//				}
//				
//				for (int i=0; i<npix; i++ ) {
//					xPositions[i] = bIn.readShort();
//				}
//				
//				for (int i=0; i<npix; i++ ) {
//					yPositions[i] = bIn.readShort();
//				}
		
				DxDataRec[] dataRec = new DxDataRec[npix];
				for (int i=0; i<npix; i++ ) {
					longitudes[i] = bIn.readShort();
					latitudes[i] = bIn.readShort();
					xPositions[i] = bIn.readShort();
					yPositions[i] = bIn.readShort();

//					System.out.println("pos: " + xPositions[i] + "," + yPositions[i]);
					dataRec[i] = new DxDataRec(longitudes[i], latitudes[i], xPositions[i], yPositions[i]);
				}
				System.out.println("npix: " + npix);
		
				//bIn.read(buffer, 0, iout);
				
//				DataInputStream bIn = new DataInputStream(
//						new ByteArrayInputStream(buffer,0,iout));
				
				System.out.println("iout: " + iout);
				System.out.println("avilable: " + bIn.available());
				
				//npix = npix * 2;
				for (int i = 0; i < npix; i++) {									
					DxS1 dxs1 = null;
	
					// S1
					bIn.read(buffer, 0, DxS1.DATA_LEN);
					dxs1 = new DxS1(buffer, 0);
					//System.out.println("IRRadiance: " + dxs1.getIRRadiance());
					//System.out.println("FirstIRClearSkyRadiance: " + dxs1.getFirstIRClearSkyRadiance());
					
					int radiance = dxs1.getIRRadiance();
					int r = radiance;
		            int g = radiance;
		            int b = radiance;
		            int col = (r << 16) | (g << 8) | b;
		            
		            //System.out.println(i + " ... " + dataRec[i].xBuf + "," + dataRec[i].yBuf);
		            if (col != 0) {
		            	image.setRGB(dataRec[i].xBuf, height - dataRec[i].yBuf, col);
		            }
					
					// ADD1
					bIn.read(buffer, 0, channelCount - 2);
					new DxAdd1(buffer, 0, channelCount - 2);
	
					if (!polarSatellite) {
						if (!dxs1.isNoDay()) {
							// S3
							bIn.read(buffer, 0, DxS3.DATA_LEN);
							new DxS3(buffer, 0);
						} else {
							// S2
							bIn.read(buffer, 0, DxS2.DATA_LEN);
							new DxS2(buffer, 0);
							// S3
							bIn.read(buffer, 0, DxS4.DATA_LEN);
							new DxS3(buffer, 0);
							// S4
							bIn.read(buffer, 0, DxS4.DATA_LEN);
							new DxS4(buffer, 0);
						}
					} else {
						if (!dxs1.isNoDay()) {
							// S3
							bIn.read(buffer, 0, DxS3.DATA_LEN);
							new DxS3(buffer, 0);
							// Add3
							bIn.read(buffer, 0, DxAdd3.DATA_LEN);
							new DxAdd3(buffer, 0);
						} else {
							// S2
							bIn.read(buffer, 0, DxS2.DATA_LEN);
							new DxS2(buffer, 0);
							// S3
							bIn.read(buffer, 0, DxS3.DATA_LEN);
							new DxS3(buffer, 0);
							// Add3
							bIn.read(buffer, 0, DxAdd3.DATA_LEN);
							new DxAdd3(buffer, 0);
							// S4
							bIn.read(buffer, 0, DxS4.DATA_LEN);
							new DxS4(buffer, 0);
						}
					}
				}
			}
			

			
			ImageIO.write(image, "PNG", f);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}

	}
}
