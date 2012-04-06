package ajmas74.experiments;

import java.net.*;
import java.util.*;
/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NetworkInterfaces {

	public static void main (String[] args) {
    try {
     Enumeration enum = NetworkInterface.getNetworkInterfaces(); 
     while ( enum.hasMoreElements() ) {
       NetworkInterface netIface = (NetworkInterface) enum.nextElement();
       System.out.println(netIface);
     }
    } catch (Exception e) {
      e.printStackTrace();
    }
	}
}
