package ajmas74.experimental;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkStuff {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        try {
            Enumeration<NetworkInterface> enumer = NetworkInterface.getNetworkInterfaces() ;
            
            while ( enumer.hasMoreElements() ) {
                NetworkInterface ni =  enumer.nextElement();
                System.out.println( ni.getDisplayName() );
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
