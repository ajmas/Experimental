package ajmas74.experiments;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;


// 00:30:65:d6:b1:64
/**
 * <p>WakeOnLAN - a small Java class to wake up a computer that is asleep
 * and has "Wake On LAN" support activated</p>
 */

public class WakeOnLAN {

  public static int PORT = 80;
  //public static String ADDRESS = "192.168.0.255";
  //public static String ADDRESS = "224.0.0.1";
  public static String ADDRESS = "224.0.0.2";;

  public WakeOnLAN() {
  }

 /** sends wake command to the computer specified by the human readable
   *  MAC address.
   */
  public void sendWake ( String macAddressStr )
    throws Exception {
    byte[] macAddress = new byte[6];
    String separator = "-";
    if ( macAddressStr.indexOf('-') > -1 ) {
      separator = "-";
    } else if ( macAddressStr.indexOf('.') > -1 ) {
      separator = ".";
    } else if ( macAddressStr.indexOf(':') > -1 ) {
      separator = ":";
    }
    StringTokenizer tokenizer = new StringTokenizer(macAddressStr,separator);
    int i=0;
    while ( tokenizer.hasMoreTokens() ) {
      String token = tokenizer.nextToken();
      System.out.print( token + ":" );
      macAddress[i] = (byte) (
        Character.digit( token.charAt(0),16) << 4 |
        Character.digit( token.charAt(1),16)
      );
      i++;
    }
    sendWake(macAddress);
  }

  /** sends wake command to the computer specified by the 6 byte
   *  MAC address.
   */
  public void sendWake ( byte[] macAddress )
    throws Exception {
    byte[] magicPacket = new byte[102];
    for ( int i=0; i<6; i++ ) {
      magicPacket[i] = (byte) 0xFF;
    }
    for ( int i=1; i<17; i++ ) {
      for ( int j=0; j<6; j++ ) {
        magicPacket[(i*6)+j] = macAddress[j];
      }
    }
    InetAddress group = InetAddress.getByName( ADDRESS );
    System.out.println("addr = " + group);
    MulticastSocket mcastSocket = new MulticastSocket ( PORT );
    mcastSocket.setTimeToLive(5);
    mcastSocket.joinGroup(group);

    DatagramPacket packet = new DatagramPacket(magicPacket, magicPacket.length, group, PORT);
    mcastSocket.send(packet);

  }

  public static void main ( String[] args ) {
    try {
     // (new WakeOnLAN()).sendWake("00-01-03-84-FF-E2");
      (new WakeOnLAN()).sendWake("00:30:65:d6:b1:64");
    } catch ( Exception ex ) {
      ex.printStackTrace();
    }
  }

}

/*

Description of WakeOnLAN from a posting by: mike (mike@wakerly.com) on usenet

Anyway, you have to send the 'magic packet' on the wire such that your
target computer will see the sequence. The magic packet can be any
message... be it an ethernet frame, or part of a TCP/IP or UDP payload.
It is merely:

  6 bytes: FF FF FF FF FF FF
  ..followed by...
  16 repetitions of the 6-byte MAC address

Neat, huh? And they got a patent for it! A better description is at:
http://www.amd.com/products/npd/overview/20212.html

*/