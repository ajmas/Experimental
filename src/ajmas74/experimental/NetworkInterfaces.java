package ajmas74.experimental;

import java.net.*;
import java.util.*;

/**
 * @author andrmas
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class NetworkInterfaces {

	public static void main(String[] args) {
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface netIface = (NetworkInterface) netInterfaces
						.nextElement();
				System.out.println(netIface);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
