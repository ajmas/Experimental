/* Created on 9-Oct-2003 */
package ajmas74.experimental;

/**
 * Unicode Byte Order Mark utility. This class
 * is designed to help identifying the text encoding
 * of an input stream.
 *  
 * @author Andre-John Mas
 */
public class UnicodeBOMUtils {

	public static final int NO_BOM = -1; 
	public static final int UTF8  = 0;
	public static final int UTF16BE = 1;
	public static final int UTF16LE = 2;	
	// not not supported by java InputStreamReader
	public static final int UTF32BE = 3;
	public static final int UTF32LE = 4;	

	/** */
	private static final BomIdentifier[] BOM_IDENTIFIERS = new BomIdentifier[] {
		new BomIdentifier(new int[]{0xEF,0xBB,0xBF},"UTF-8",UTF8),
		new BomIdentifier(new int[]{0xFE,0xFF},"UTF-16BE",UTF8),
		new BomIdentifier(new int[]{0xFF,0xFE},"UTF-16LE",UTF16LE),
		new BomIdentifier(new int[]{0x00,0x00,0xFE,0xFF},"UTF-32BE",UTF32BE),
		new BomIdentifier(new int[]{0xFF,0xFE,0x00,0x00},"UTF-32LE",UTF32BE)	
	};
	
	/** */	
	public int getUnicodeBOMType ( byte[] bytes ) {
		for ( int i=0; i<BOM_IDENTIFIERS.length; i++ ) {
			BomIdentifier bomIdent = BOM_IDENTIFIERS[i];
			int[] bom = bomIdent.getByteOrderMark();
			boolean match = true;
			if ( bytes.length < bom.length ) {
				throw new RuntimeException("not enough bytes for BOM test");
			}
			for ( int j=0; j<bom.length;j++){
				if ( bytes[j] != bom[j] ) {
					match = false;
					break;
				}
			}
			if ( match ) {
				return bomIdent.getId();
			}
		}
		return NO_BOM;
	}
		
	/** */
  public int getBOMLength( int bomType ) {
		for ( int i=0; i<BOM_IDENTIFIERS.length; i++ ) {
			if (BOM_IDENTIFIERS[i].getId() == bomType ) {
				return BOM_IDENTIFIERS[i].getByteOrderMark().length;
			}			
		}
		return 0;
  }
  
  /** */
	public String getCharsetname ( byte[] bytes ) {
		return getCharsetname(getUnicodeBOMType(bytes));
	}
	
	/** */
	public String getCharsetname ( int bomType ) {
		for ( int i=0; i<BOM_IDENTIFIERS.length; i++ ) {
			if (BOM_IDENTIFIERS[i].getId() == bomType ) {
				return BOM_IDENTIFIERS[i].getCharset();
			}
		}		
		return System.getProperty("file.encoding");		
	}	
	
	/** */	
	private static class BomIdentifier {
		int[] _byteOrderMark;
		String _charset;
		int    _id;
		
		BomIdentifier ( int[] byteOrderMark, String charset, int id ) {
			_byteOrderMark = byteOrderMark;
			_charset = charset;
			_id = id;
		}
				
    /**
     * @return
     */
    public int[] getByteOrderMark() {
      return _byteOrderMark;
    }

    /**
     * @return
     */
    public String getCharset() {
      return _charset;
    }

    /**
     * @return
     */
    public int getId() {
      return _id;
    }

	}
}
