package ajmas74.experimental;

//import java.sql.Date;
import java.text.*;
import java.util.*;
//import java.net.Inet6Address;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Misc {

//  /**
//    * how many day between the two ISODates. If the start date is more recent
//    * than the end date, then the result will  be negative, so:
//    *  <ul>
//    *  <li> +1 = ( start: 2002-12-17, end 2002-12-18)
//    *  <li>  0 = ( start: 2002-12-18, end 2002-12-18)
//    *  <li> -1 = ( start: 2002-12-19, end 2002-12-18)
//    *  </ul>
//    */
//   protected static int daysBetween( ISODate start, ISODate end ) {
//       Calendar calStart = Calendar.getInstance();
//       calStart.setTime(start.toDate());
//
//       Calendar calEnd = Calendar.getInstance();
//       calEnd.setTime(end.toDate());
// 
//       return daysBetween(calStart, calEnd);
//   }
 
   /**
    * how many day between the two Calendar dates. If the start date is more
    * recent than the end date, then the result will  be negative, so:
    *  <ul>
    *  <li> +1 = ( start: 2002-12-17, end 2002-12-18)
    *  <li>  0 = ( start: 2002-12-18, end 2002-12-18)
    *  <li> -1 = ( start: 2002-12-19, end 2002-12-18)
    *  </ul>
    */
   protected static int daysBetween( Calendar start, Calendar end ) {
 
 		 long ONE_HOUR = 60 * 60 * 1000;
     System.out.println(start);
     System.out.println(end);
          
     start = (Calendar) start.clone();
     start.set(Calendar.HOUR,0);
     start.set(Calendar.HOUR_OF_DAY,0);     
     start.set(Calendar.MINUTE,0);
     start.set(Calendar.SECOND,0);
     start.set(Calendar.MILLISECOND,0);
 		 //start.setTimeZone(new SimpleTimeZone(-5,"America/Montreal"));
     start.setTimeZone(TimeZone.getTimeZone("GMT0"));
     start.set(Calendar.DST_OFFSET,0);
     
     end = (Calendar) end.clone();
     end.set(Calendar.HOUR,0);
     end.set(Calendar.HOUR_OF_DAY,0);
     end.set(Calendar.MINUTE,0);
     end.set(Calendar.SECOND,0);
     end.set(Calendar.MILLISECOND,0);
     //end.setTimeZone(new SimpleTimeZone(-5,"America/Montreal")); 
     start.setTimeZone(TimeZone.getTimeZone("GMT0"));        
     end.set(Calendar.DST_OFFSET,0);     
     //end.set(
 
     //long diff = end.getTime().getTime() - start.getTime().getTime();
     long diff = end.getTime().getTime() - start.getTime().getTime();
     // since we hours and seconds are not important, the following
     // will correct the DST error issue:
     diff+=ONE_HOUR;
     //86400000 = day in milliseconds
     int days = (int) (diff / (ONE_HOUR*24));
 
     return days;
   }

  protected static int daysBetween( long start, long end ) {
    float startF = start / 86400000.0f;
    float endF = end / 86400000.0f;
    
    int days = (int)( endF-startF );
	  //long diff = end - start;
	 
	  //86400000 = day in milliseconds
	  //int days = (int) (diff / 86400000);
	 
	  return days;
	}   
	
  public static void displayAdjustedDate() {
    String format = "";
    int gmtOffset = -5;
    Date date = new Date();
    
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		
		int offsetAdjusted = gmtOffset * (3600000);
		
		SimpleTimeZone tz = new SimpleTimeZone(offsetAdjusted,gmtOffset+":00");
		dateFormat.setTimeZone(tz);
		
		System.out.println("date A: " + date);
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
		//cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.setTimeZone(tz);
		
		System.out.println("date B: " + dateFormat.format(cal.getTime()) );
		//return dateFormat.format(cal.getTime());		
  }
  
  private static Calendar zeroNonTime ( Calendar calendar, boolean secondsToZero ) {
    calendar.set(Calendar.YEAR,1970);
    calendar.set(Calendar.MONTH,GregorianCalendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH,1); 
    if (secondsToZero) {
      calendar.set(Calendar.SECOND,0);
      calendar.set(Calendar.MILLISECOND,0);      
    }
    return calendar;
  } 
  

  private static boolean matches ( String text ) {
      return text.matches("2001:4978:15d.*");
  }
	public static void main ( String[] args ) {
	  
	  //displayAdjustedDate();
	  
//		System.out.println((new Date(2002,01,01)).getTime());
		
//		try {
//      InetAddress address = (InetAddress) InetAddress.getLocalHost();
//      System.out.println(address);
//      System.out.println(address.hashCode());
//    } catch (UnknownHostException e) {
//      e.printStackTrace();
//    } 

//  --------------------------------


//		String bookingCode = "N1KAMX";
//		String[] desc = new String[] { "               " + "hello",
//		  "0123456789012345------------------",
//      "*****AAABBB*****------------------"
//		};
//		
//    if ( desc.length > 0 ) {
//      StringBuffer strBuf = new StringBuffer(desc[0]);
//      strBuf.replace(5,10,bookingCode);
//      desc[0] = strBuf.toString();
//    }		
//    for ( int i=0; i<desc.length; i++) {
//      System.out.println(desc[i]);
//    }


//    double dd = 95.2;
//    System.out.println(dd);
//		
//    System.out.println("");		
//		float f = 95.2f;		
//		double d = f;
//		
//    System.out.println(f);
//    System.out.println(d);
//		System.out.println((float)d);
//    
//    System.out.println("");
//    d = (new Float(f)).doubleValue();
//    System.out.println(d);
//    System.out.println((float)d); 
//    
//    d = (new Double(f+"")).doubleValue(); 
//    System.out.println(d); 

// --------------------

//		Properties props = System.getProperties();
//		Enumeration enum = props.keys();
//		while ( enum.hasMoreElements() ) {
//		  String key = (String) enum.nextElement();
//      System.out.println(key+"="+System.getProperty(key));
//		}

//  --------------------


//    String[] availableIDs = TimeZone.getAvailableIDs();
//    Arrays.sort(availableIDs);
//    for ( int i=0; i<availableIDs.length; i++ ) {
//      System.out.println(availableIDs[i]+"\t"+ (TimeZone.getTimeZone(availableIDs[i]).getRawOffset()/(60*60*1000f))  );
//    }
//		for ( int i =0; i<2; i++ ) {
//      Calendar in = Calendar.getInstance();
//      //"2003-04-05" endDate="2003-04-06"
//      in.set(2003,3,4);
//      //in.set(Calendar.MILLISECOND,250);
//      Calendar out = Calendar.getInstance();
//      out.set(2003,3,7);
//      //out.set(Calendar.MILLISECOND,240);
//  	
//  		
//      ISODate isoIn = new ISODate(in.getTime());
//      ISODate isoOut = new ISODate(out.getTime());
//  	
//  		System.out.println(isoIn.toISODateTime());
//      System.out.println(isoOut.toISODateTime());  	
//      	
//      ISODateTimeSpan plDateRange = new ISODateTimeSpan(isoIn,isoOut);
//      System.out.println(daysBetween(plDateRange.getStartInstant(),
//        plDateRange.getEndInstant()));
////      //System.out.println(daysBetween(	isoIn,isoOut));		  
////		}
//      System.out.println(daysBetween(in,out));
//			//System.out.println(daysBetween(1049346000000L-3600000,1049688000000L));
//
//    	
//  	//System.out.println(daysBetween(in,out));	
    
      //System.setProperty("file.encoding","UTF-8");
      //System.out.println("123-\u0067\u0035\u039e\u322F\u5193");
      //System.out.
      //sun.io.
      
//      try {
//        System.out.println( new String( (new sun.misc.BASE64Decoder()).decodeBuffer("YWRtaW46YWRtaW4=")) );
//      } catch (IOException e) {
//        // XXX Auto-generated catch block
//        e.printStackTrace();
//      }
//      
//      NumberFormat nf = new DecimalFormat("00000000");
//      System.out.println(nf.format(1));

    
/*
    String reqMsgName = "OTA_HotelNotifRQ";
    String reqMsgNameShrt = "OTA_HotelNotif";   

    String rspMsgName = "OTA_HotelNotifRS";
               
    String rspMsgNameShrt = rspMsgName.substring(0,rspMsgName.length()-2);
               
    System.out.println("reqMsgNameShrt: "+reqMsgNameShrt);
    System.out.println("rspMsgNameShrt: "+rspMsgNameShrt);
        
    System.out.println("a: " + (reqMsgName.endsWith("RQ") && rspMsgName.endsWith("RS")) );
    System.out.println("b: " + reqMsgNameShrt.equals(rspMsgNameShrt) );
    
    if ( (reqMsgName.endsWith("RQ") && rspMsgName.endsWith("RS")) && 
       reqMsgNameShrt.equals(rspMsgNameShrt) ) {      
      System.out.println("true");
    } else {
      System.out.println("false");      
    }
*/
	  
	  //System.out.println(Byte.parseByte("19"));
      
//       Date d = new Date();
//       Calendar cal = Calendar.getInstance();
//       cal.setTime(d);
//       cal.set(1970,0,1);
//       System.out.println(cal.getTime());
//       System.out.println(cal.getTime().getTime());
//       
//       //cal = Calendar.getInstance();
//       cal.setTime(d);
//       cal = zeroNonTime(cal,false);
//       System.out.println(cal.getTime());
//       System.out.println(cal.getTime().getTime());
       
//       String prefix = "wget http://www.freshteen.biz/teensluts4free/free";
//       String suffix = ".mpg";
//       for ( int i=131; i<186; i++ ) {
//         System.out.println(prefix+i+suffix);
//       }
	    
	    System.out.println ( matches ( "2001:4978:18d::216:cbff:fe9f:dc47") );
	}
	
}

