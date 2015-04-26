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
 * Used for data available here:
 * ftp://eclipse.ncdc.noaa.gov/pub/isccp/dx/
 * 
 * Code based on documention, working with a friend and code found
 * here: http://isccp.giss.nasa.gov/products/software.html
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
		}

		// http://stackoverflow.com/questions/10514077/how-to-validate-value-of-bit-in-byte-array-in-java
		boolean isSet(byte[] data, int index) {
			int bitIndex = index % 8;
			int byteIndex = index / 8;
			int bitMask = 1 << bitIndex;
			return (data[byteIndex] & bitMask) > 0;
		}
		
		int getUnsignedByte(ByteBuffer byteBuffer, int offset) {
			return byteBuffer.get(offset) & 0xff;
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
			return getUnsignedByte(byteBuffer, 3);
		}
		
		public int getFirstIRRadiance() {
			return getUnsignedByte(byteBuffer, 4);
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
		ByteBuffer byteBuffer;
		
		public DxS2(byte[] data) {
			this.data = data;
		}

		public DxS2(byte[] data, int start) {
			super();
			System.arraycopy(data, start, this.data, 0, DATA_LEN);
			byteBuffer = ByteBuffer.allocate(DATA_LEN);
			byteBuffer.put(this.data);
		}
		
		public int getVISRadiance() {
			return getUnsignedByte(byteBuffer, 3);
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

	static class Header {
		int year = -1;
		int month = -1;
		int day = -1;
		int utc = -1;
		
		int satelliteId = -1;
		int channelCount = -1;
		boolean nightImage = false;
		
		boolean polarSatellite = false;
		
		public String toString() {
			return year + "-" + month + "-" + day + ", " + 
			satelliteId + ", " + 
			channelCount + ", " + nightImage + ", " + polarSatellite;			
		}
	}
		
	
	public Header readHeader(byte[] buffer) {

		Header header = new Header();
		
		String line = new String(buffer, 0, 80, Charset.forName("ascii"));
		//System.out.print(line);
		int j = 0;
		header.year = Integer.parseInt(line.substring(j, j + 10).trim());
		j += 10;
		header.month = Integer.parseInt(line.substring(j, j + 10).trim());
		j += 10;
		header.day = Integer.parseInt(line.substring(j, j + 10).trim());
		j += 10;
		header.utc = Integer.parseInt(line.substring(j, j + 10).trim());
		j += 10;
		header.satelliteId = Integer.parseInt(line.substring(j, j + 10)
				.trim());
		j += 10;
		header.polarSatellite = (Integer.parseInt(line
				.substring(j, j + 10).trim()) > 0);
		j += 10;
		header.channelCount = Integer.parseInt(line.substring(j, j + 10)
				.trim());
		
		System.out.println("polar: " + header.polarSatellite);
		
		for (int i = 2; i < 384; i++) {
			int offset = (i - 1) * 80;
			line = new String(buffer, offset, 80,
					Charset.forName("ascii"));
			
			if (line.trim().length() > 0) {
				System.out.println(line);
			}
		}

		return header;
	}
	
	@SuppressWarnings("unused")
	public void readRecord(Header header, byte[] buffer, BufferedImage image) throws IOException {
				
		DataInputStream bIn = new DataInputStream(
				new ByteArrayInputStream(buffer,0,RECLEN));
		
		// Prefix area
						
		int iwest = bIn.readInt();
		int ieast = bIn.readInt();
		int inorth = bIn.readInt();
		int isouth = bIn.readInt();		
		int npix = bIn.readInt();
		int iout = bIn.readInt();
		
		int[] longitudes = new int[npix];
		int[] latitudes = new int[npix];
		int[] xPositions = new int[npix];
		int[] yPositions = new int[npix];

		DxDataRec[] dataRec = new DxDataRec[npix];
		for (int i=0; i<npix; i++ ) {
			longitudes[i] = bIn.readShort();
			latitudes[i] = bIn.readShort();
			xPositions[i] = bIn.readShort();
			yPositions[i] = bIn.readShort();

			dataRec[i] = new DxDataRec(longitudes[i], latitudes[i], xPositions[i], yPositions[i]);
		}

		// Data area
		
		for (int i = 0; i < npix; i++) {									
			DxS1 dxs1 = null;

			bIn.read(buffer, 0, DxS1.DATA_LEN);
			dxs1 = new DxS1(buffer, 0);
		
			int radiance = dxs1.getIRRadiance();

			
//			if (radiance > 0 && radiance < 200) {
//				radiance *= 2;
//				radiance = 254;
//			}
//			else if (radiance > 100 && radiance < 200) {
//				radiance *= 1.8;
//			}
			if (image != null) {
				int height = image.getHeight();
				int r = radiance;
	            int g = radiance;
	            int b = radiance;
	            int col = (r << 16) | (g << 8) | b;
	            
	            if (col != 0) {
	            	image.setRGB(dataRec[i].xBuf, height - dataRec[i].yBuf, col);
	            }
			}
			
			// ADD1
			bIn.read(buffer, 0, header.channelCount - 2);
			new DxAdd1(buffer, 0, header.channelCount - 2);

			if (!header.polarSatellite) {
				if (!dxs1.isNoDay()) {
					// S3
					bIn.read(buffer, 0, DxS3.DATA_LEN);
					new DxS3(buffer, 0);
				} else {
					// S2
					bIn.read(buffer, 0, DxS2.DATA_LEN);
					DxS2 dxs2 = new DxS2(buffer, 0);					
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
					DxS2 dxs2 = new DxS2(buffer, 0);					
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
	
	public void readFile(File inputFile, File outputFile, boolean mergeImage) throws IOException {

		byte[] buffer = new byte[RECLEN];
		BufferedImage image = null;
		FileInputStream fIn = null;
		DataInputStream in = null;
		
		if (!inputFile.exists()) {
			System.out.println("File does not exist: " + inputFile);
			return;
		}
		try {
			
			fIn = new FileInputStream(inputFile);			
			in = new DataInputStream(fIn);
			
			int recordCount = (int) (inputFile.length() / RECLEN);
			
			if (outputFile != null) {
				int width = 1440;
				int height = 550;
				
				if (outputFile.exists() && mergeImage) {
					image = ImageIO.read(outputFile);
				} else {
					image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				}
			}
			
			in.read(buffer,0,RECLEN);
			Header header = readHeader(buffer);
			
			System.out.println(header);
			
			for (int recIdx=1; recIdx < recordCount; recIdx++) {
				in.read(buffer, 0, RECLEN);
				readRecord(header, buffer, image);
			}
			
			if (image != null) {
				ImageIO.write(image, "PNG", outputFile);
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	public void read(String[] args) throws IOException {
		File inputFile = new File(args[0]);
		File outputFile = new File("output.png"); 
		new DXRead().readFile(inputFile, outputFile, true);
	}
	
	public static void main(String[] args) throws IOException {
		args = new String[1];
		for (int i=0; i<=21; i+=3) {
			String val = "" + i;
			if (val.length() < 2) {
				val = "0" + val;
			}
			args[0] = "data/2004/MET-7/ISCCP.DX.0.NOA-17S.2004.01.01." + val + "00.NOM";
			args[0] = "data/2004/MET-7/ISCCP.DX.0.MET-7.2004.01.01." + val + "00.EUM";

			new DXRead().read(args);
		}
		
//		args[0] = "data/ISCCP.DX.0.NOA-10A.1991.01.01.0000.NOM";
//		new DXRead().read(args);

	}
	
}
