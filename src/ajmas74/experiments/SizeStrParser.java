package ajmas74.experiments;

import java.text.DecimalFormat;

public class SizeStrParser {
  
  private static final int BYTE = 1;
  private static final int KiB = 1024;
  private static final int MiB = KiB*1024;
  private static final int GiB = MiB*1024;
  private static final int TiB = GiB*1024;
  
  DecimalFormat format = new DecimalFormat("#.##");
  
  SizeStrParser () {
    
  }
  
  /**
   * take a string containing a numerical value, followed by
   * a byte unit and returns the corresponding numerical value.
   * For example:
   *   1 byte = 1
   *   1 KB = 1024;
   *   1 MB = 1024*1204;
   *   and so on.
   * @param value
   * @return
   */
  long parse ( String value ) {
    String[] parts = value.split(" ");
    if ( parts.length > 0 ) {
      long size = Long.parseLong(parts[0]);
      String unit = parts[1];
      if ( unit.equals("byte") ) {  }
      else if ( unit.equals("kB") ) { size = size*KiB; }
      else if ( unit.equals("KB") ) { size = size*KiB; }
      else if ( unit.equals("MB") ) { size = size*MiB; }
      else if ( unit.equals("GB") ) { size = size*GiB; }
      else if ( unit.equals("TB") ) { size = size*TiB; }
      return size;
    }
    return -1;    
  }
  
  String format ( long sizeInBytes, boolean includeDecimal ) {
    int unitValue = 1;
    String unitName  = "bytes";
    if ( sizeInBytes < KiB )  {
      //unitValue = KiB;
      //unitName  = "KB";
    } else if ( sizeInBytes < MiB )  {
      unitValue = KiB;
      unitName  = "KB";
    } else if ( sizeInBytes < GiB )  {
      unitValue = MiB;
      unitName  = "MB";
    } else if ( sizeInBytes < TiB )  {
      unitValue = GiB;
      unitName  = "GB";
    } else  {
      unitValue = TiB;
      unitName  = "TB";
    }
    StringBuffer strBuf = new StringBuffer();
    if ( includeDecimal ) {
      double value = sizeInBytes / (unitValue*1.0);      
      strBuf.append( format.format(value) );
    } else {
      strBuf.append( sizeInBytes / (unitValue) );
    }
    strBuf.append(' ');
    strBuf.append(unitName);
    return strBuf.toString();
    //return sizeInBytes + " bytes";
  }
  
  public static void main ( String[] args ) {
    SizeStrParser ssp = new SizeStrParser();
    System.out.println(ssp.parse("999 MB"));
    
    String sizeStr = "99999999999999999 bytes";
    System.out.println("** original: " + sizeStr);
    System.out.println(ssp.format(ssp.parse(sizeStr),true));
    System.out.println(ssp.format(ssp.parse(sizeStr),false));
    
    sizeStr = "99999999999999999 MB";
    System.out.println("** original: " + sizeStr);
    System.out.println(ssp.format(ssp.parse(sizeStr),true));
    System.out.println(ssp.format(ssp.parse(sizeStr),false));    
  }
  
}
