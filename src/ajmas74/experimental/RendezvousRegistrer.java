package ajmas74.experimental;

import javax.jmdns.*;

import java.io.IOException;
import java.net.*;

import javax.jmdns.JmDNS;
public class RendezvousRegistrer {

	JmDNS _rendezvous;
	
  RendezvousRegistrer ( String[] args ) {
    try {
      String serviceType = "_http._tcp.local.";
      InetAddress address = InetAddress.getLocalHost();
      int port = 80;
            
      String name = "andrmas";
      
//      String name = "webserver " + address.getHostName();
//      if ( port != 80 ) {
//        name += " (" + port + ")";
//      }
//      if ( args.length > 0 ) {
//        name = args[0];
//      }
			
      _rendezvous = new JmDNS();
      //ServiceInfo serviceInfo =  new ServiceInfo(serviceType, name + "." + serviceType, address, port, 1,1, "");
      ServiceInfo serviceInfo = new ServiceInfo("_http._tcp.local.", "andrmas' webserver._http._tcp.local.", InetAddress.getLocalHost(), 8080, 1,1, "");
      _rendezvous.unregisterService( serviceInfo );
      System.out.println("serviceinfo - " + serviceInfo);
//      rendezvous.registerService(
//        new ServiceInfo("_http._tcp.local.", "andrmas' webserver (8080)._http._tcp.local.", InetAddress.getLocalHost(), 8080, 1,1, "index.html")
//      );      
      System.out.println(name + " registered");
      while ( true ) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
      }
    } catch (UnknownHostException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }    
  }
  
  public void run() {
    System.out.println("unregistering services");
    if ( _rendezvous != null ) {
      _rendezvous.unregisterAllServices();
    }
  }
  
  public static void main ( String[] args ) {
    RendezvousRegistrer rvr = new RendezvousRegistrer(args);
  }

}