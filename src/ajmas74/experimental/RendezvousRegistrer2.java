package ajmas74.experimental;

import com.strangeberry.rendezvous.*;

import java.io.IOException;
import java.net.*;
public class RendezvousRegistrer2 implements Runnable {

	Rendezvous  _rendezvous;
  ServiceInfo _serviceInfo;
  	
  RendezvousRegistrer2 () {
    Runtime.getRuntime().addShutdownHook(new Thread(this));
    try {		  
      String serviceType = "_http._tcp.local.";
      String name = "bob" + "." + serviceType;
      int port = 80;
      InetAddress address = InetAddress.getLocalHost();
      
      _serviceInfo = new ServiceInfo(serviceType, name + "." + serviceType, address, port, 1,1, "");
      _rendezvous = new Rendezvous();
      _rendezvous.registerService( _serviceInfo );
      System.out.println(name + "registered");
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
    System.out.println("unregistering service");
    if ( _rendezvous != null && _serviceInfo != null ) {
      _rendezvous.unregisterService(_serviceInfo);
    }
  }
  
  public static void main ( String[] args ) {
    RendezvousRegistrer2 rvr = new RendezvousRegistrer2();
  }

}