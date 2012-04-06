package ajmas74.experiments;

import java.util.Date;
import java.net.InetAddress;

/**
 * <p>Title: </p>
 * <p>Description: This class is used to generate unique IDs. The current
 * implementation does some magic based on the hostname, port number ( if
 * pertinent ), the time, and sequence value Hopefully the resulting value
 * should not repeat itself in a million years ( a non-scientific hope ).
 * Note: The generated IDs should not exceed 20 characters in length
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andre John Mas</a>
 * @version 1.0
 */

public class UniqueIdMaker {

  private final static int MIN_COUNT = 0;
  private final static int MAX_COUNT = 999;
  private final static int RADIX = Character.MAX_RADIX;

  private static int _sequenceIdx = 0;

  /** creates an id taking hostname, port and time as parameters */
  public static synchronized String makeId ( String hostname, int port, long time ) {

    if(_sequenceIdx >= MAX_COUNT){
      _sequenceIdx = MIN_COUNT;
    }

    String y = Integer.toString(hostname.hashCode(),RADIX)
      + Integer.toString(port,RADIX);

    String x = Integer.toString(y.hashCode(),RADIX)
      + Long.toString(time,RADIX)
      + Integer.toString(_sequenceIdx++,RADIX);
     /* + Integer.toString( (int)(100 / Math.random()) ,_radix) */

      if ( x.startsWith("-") ) {
        x = x.substring(1);
      }
      return x;
  }

  /** Creates an id, taking no parameters. */
  public static String makeId () {
    String hostname = "localhost"; // default value
    try {
      InetAddress.getLocalHost().getHostName();
    } catch ( Exception ex ) {
    }
    return makeId ( hostname, 0, System.currentTimeMillis() );
  }

  /** creates an id taking hostname, port and date as parameters */
  public static String makeId ( String hostname, int port, Date date ) {
     return makeId ( hostname, port, date.getTime() );
  }

/*
  public static void main ( String[] args ) {
    for ( int i=0; i < 2000; i++ ) {
      String id = UniqueIdMaker.makeId( "andrmas", 30101, new Date() );
      System.out.println( id + "   ( len="+id.length()+" )");
    }
  }
*/
}