/* Created on 21-Jan-2004 */
package ajmas74.experiments;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class PNMServer implements Runnable {

  private static final String NEWLINE = "\r\n";
  int _listenPort = 9901;
  
  int _stage = 0;
  /**
   * 
   */
  public PNMServer() {
    super();
    // TODO Auto-generated constructor stub
  }

  public void run() {
    try {
      int i = 0;
      ServerSocket svrSock = new ServerSocket(_listenPort);
      while ( true) {
        i++;
        Socket sock = svrSock.accept();
        if ( i == 1 ) { 
          System.out.println("New connection at " + (new Date()));
          DataSender sender = new DataSender(sock);
          sender._fileName = ("c:/temp/data-000001B.pna");
          (new Thread(sender,"passthrough-"+i)).start();
        }
        if ( i == 2 ) { 
          System.out.println("New connection at " + (new Date()));
          ControlSender sender = new ControlSender(sock);
          //sender._fileName = ("c:/temp/data-000001B.pna");
          (new Thread(sender,"passthrough-"+i)).start();
        }        
        //sender.setDataFile(true);
        

      }
    } catch ( Exception ex ) {
      System.err.println("Server Exception");
      ex.printStackTrace();
    }
  }
  
  class DataSender implements Runnable {
    
    String _encoding = "ISO-8859-1";
    String _host;
    int    _port;
    
    Socket _requestSocket;
    
    String  _request;
    boolean _sendToScreen;
    boolean _sendToFile;
    String  _fileName;
    
    DataSender ( Socket requestSocket) {
      //_host = host;
      //_port = port;
      _requestSocket = requestSocket;
    }
    
    public void run (){
      OutputStream fout = null;
      Socket sock = null;   
      try {
        sock = _requestSocket;
        
        StringBuffer strBuf = new StringBuffer();
        InputStream sIn = sock.getInputStream();
        byte[] buffer = new byte[4096];
        int len = 0;
        while ( (len = sIn.read(buffer)) > -1 ) {
          strBuf.append(new String(buffer,0,len));
        }
        
        if ( strBuf.indexOf("application/x-pncmd") < 0 ) {
          strBuf = new StringBuffer();
          OutputStream sOut = sock.getOutputStream();
	
          strBuf.append("HTTP/1.0 200 OK" + NEWLINE);
          strBuf.append("Server: RMServer 1.0" + NEWLINE);
          strBuf.append("Expires: Mon, 18 May 1974 00:00:00 GMT" + NEWLINE);
          strBuf.append("Pragma: no-cache" + NEWLINE);
          strBuf.append("x-server-ip-address: 127.0.0.1" + NEWLINE);
          strBuf.append("Content-type: audio/x-pn-realaudio" + NEWLINE);
          strBuf.append(NEWLINE);
          strBuf.append(NEWLINE);
          _stage = 1;
          sOut.write(strBuf.toString().getBytes());
          while ( _stage != 2 ) {
            try {
              Thread.sleep(300);
            } catch ( InterruptedException ex ) {              
            }
          }
          sOut.write(48);
          sOut.write(02);
          sOut.flush();
          _stage = 3;
          while ( _stage != 4 ) {
            try {
              Thread.sleep(300);
            } catch ( InterruptedException ex ) {              
            }
          }          
	        FileInputStream fin = new FileInputStream(_fileName);
	        while ( (len = fin.read(buffer)) > -1 ) {
	          sOut.write(buffer,0,len);
	        }
        }
      } catch ( Exception ex ) {
        ex.printStackTrace();
      } finally {
        try {
          if ( sock != null ) {
            sock.close();
          }        
        } catch ( IOException ex ) {          
        }
      }
      System.out.println("terminated");
    }    
  }
  
  
  class ControlSender implements Runnable {
    
    String _encoding = "ISO-8859-1";
    String _host;
    int    _port;
    
    Socket _requestSocket;
    
    String  _request;
    boolean _sendToScreen;
    boolean _sendToFile;
    String  _fileName;
    
    ControlSender ( Socket requestSocket) {
      //_host = host;
      //_port = port;
      _requestSocket = requestSocket;
    }
    
    public void run (){
      OutputStream fout = null;
      Socket sock = null;   
      try {
        sock = _requestSocket;
        
        StringBuffer strBuf = new StringBuffer();
        InputStream sIn = sock.getInputStream();
        InputStreamReader r = new InputStreamReader(sIn);
        BufferedReader reader = new BufferedReader(r);
        String line = "";
        while ( (line = reader.readLine()) != null ) {
          if ( line.length() == 0 ) {
            break;
          }
        }
        if ( line == null ) {
          return;
        }
        
        line = reader.readLine();
        _stage = 2;
        while ( _stage != 3 ) {
          try {
            Thread.sleep(300);
          } catch ( InterruptedException ex ) {              
          }
        }        
        line = reader.readLine();
        _stage = 4;
        
        byte[] buffer = new byte[4096];
        int len = 0;
        while ( (len = sIn.read(buffer)) > -1 ) {
          strBuf.append(new String(buffer,0,len));
        }
        if ( strBuf.indexOf("application/x-pncmd") < 0 ) {
          strBuf = new StringBuffer();
          OutputStream sOut = sock.getOutputStream();
          
          strBuf.append("HTTP/1.0 200 OK" + NEWLINE);
          strBuf.append("Server: RMServer 1.0" + NEWLINE);
          strBuf.append("Expires: Mon, 18 May 1974 00:00:00 GMT" + NEWLINE);
          strBuf.append("Pragma: no-cache" + NEWLINE);
          strBuf.append("x-server-ip-address: 127.0.0.1" + NEWLINE);

          strBuf.append("Content-type: audio/x-pn-realaudio" + NEWLINE);
          strBuf.append(NEWLINE);
          sOut.write(strBuf.toString().getBytes());
          
          FileInputStream fin = new FileInputStream(_fileName);
          while ( (len = fin.read(buffer)) > -1 ) {
            sOut.write(buffer,0,len);
          }
        }
      } catch ( Exception ex ) {
        ex.printStackTrace();
      } finally {
        try {
          if ( sock != null ) {
            sock.close();
          }        
        } catch ( IOException ex ) {          
        }
      }
      System.out.println("terminated");
    }    
  }
  
  public static void main (String[] args) {
    PNMServer server = new PNMServer();
    (new Thread(server)).start();
  }
}
