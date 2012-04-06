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
public class TCPProxy implements Runnable {

//  int    _listenPort   = 7000;  
//  int    _connectToPort   = 7070;
//  String _connectToServer = "129.170.147.17";
  
    
    //http://webcam4.acc.mnsu.edu/axis-cgi/mjpg/video.cgi
        
  int    _listenPort      = 80;  
  int    _connectToPort   = 80;
  String _connectToServer = "www.remax-quebec.com";//"webcam4.acc.mnsu.edu";
  
  
  /**
   * 
   */
  public TCPProxy() {
    super();
    // TODO Auto-generated constructor stub
  }


  public void run() {
    try {
      int i = 0;
      System.out.println("Server started, listening on " + _listenPort);
      ServerSocket svrSock = new ServerSocket(_listenPort);
      while ( true) {
        i++;
	      Socket sock = svrSock.accept();
	      System.out.println("New connection at " + (new Date()));
	      HTTPSender sender = new HTTPSender(sock,_connectToServer,_connectToPort);
	      sender.setSendToFile(true);
	      sender.setFileName("/tmp/out-000"+i+"");
	      (new Thread(sender,"passthrough-"+i)).start();
      }
    } catch ( Exception ex ) {
      System.err.println("Server Exception");
      ex.printStackTrace();
    }
  }
  
  static class HTTPSender implements Runnable {
    
    String _encoding = "ISO-8859-1";
    String _host;
    int    _port;
    
    Socket _requestSocket;
    
    String  _request;
    boolean _sendToScreen;
    boolean _sendToFile;
    String  _fileName;
    
    HTTPSender ( Socket requestSocket,  String host, int port ) {
      _host = host;
      _port = port;
      _requestSocket = requestSocket;
    }
    
    public void run (){
      //OutputStream fout = null;
      Socket sock = null;   
      FileOutputStream foutA = null;
      FileOutputStream foutB = null;
      try {
        sock = new Socket(_host,_port);
        
        foutA = new FileOutputStream(_fileName+"A");
        InputStream rsIn = _requestSocket.getInputStream();
        OutputStream sOut = sock.getOutputStream();
        
        foutB = new FileOutputStream(_fileName+"B");        
        InputStream sIn = sock.getInputStream();
        OutputStream rsOut = _requestSocket.getOutputStream();
        
        StreamConnection sc1
          = new StreamConnection (rsIn,sOut,foutA);
        
        StreamConnection sc2
          = new StreamConnection (sIn,rsOut,foutB);
        
        Thread th1 = new Thread(sc1);
        th1.start();
        
        Thread th2 = new Thread(sc2);
        th2.start();        
        
        while ( th1.isAlive() && th2.isAlive() ) {
          try {
            Thread.sleep(1000);
          } catch ( InterruptedException ex ) {            
          }
        }
        
      } catch ( Exception ex ) {
        ex.printStackTrace();
      } finally {
        try {
          if ( sock != null ) {
            sock.close();
          }
          if ( foutA != null ) {
            foutA.flush();
            foutA.close();
          }
          if ( foutB != null ) {
            foutB.flush();
            foutB.close();
          }          
        } catch ( IOException ex ) {          
        }
      }
      System.out.println("terminated");
    }
    
    /**
     * @return Returns the fileName.
     */
    public String getFileName() {
      return _fileName;
    }

    /**
     * @param fileName The fileName to set.
     */
    public void setFileName(String fileName) {
      _fileName = fileName;
    }

    /**
     * @return Returns the host.
     */
    public String getHost() {
      return _host;
    }

    /**
     * @param host The host to set.
     */
    public void setHost(String host) {
      _host = host;
    }

    /**
     * @return Returns the port.
     */
    public int getPort() {
      return _port;
    }

    /**
     * @param port The port to set.
     */
    public void setPort(int port) {
      _port = port;
    }

    /**
     * @return Returns the request.
     */
    public String getRequest() {
      return _request;
    }

    /**
     * @param request The request to set.
     */
    public void setRequest(String request) {
      _request = request;
    }

    /**
     * @return Returns the sendToFile.
     */
    public boolean isSendToFile() {
      return _sendToFile;
    }

    /**
     * @param sendToFile The sendToFile to set.
     */
    public void setSendToFile(boolean sendToFile) {
      _sendToFile = sendToFile;
    }

    /**
     * @return Returns the sendToScreen.
     */
    public boolean isSendToScreen() {
      return _sendToScreen;
    }

    /**
     * @param sendToScreen The sendToScreen to set.
     */
    public void setSendToScreen(boolean sendToScreen) {
      _sendToScreen = sendToScreen;
    }
  }
  
  static class StreamConnection implements Runnable {
    InputStream  _in;
    OutputStream _out;
    
    OutputStream _out2;
    
    StreamConnection ( InputStream in, OutputStream out, OutputStream out2) {
      _in   = in;
      _out  = out;
      _out2 = out2;
    }
    
    public void run() {
      try {
        //int b;
        int len = 0;
        byte[] buffer = new byte[4096];
        if ( _out2 != null ) {
	        while ( (len = _in.read(buffer)) > -1  ) {
	          _out.write(buffer,0,len);
	          _out2.write(buffer,0,len);
	        }
        } else {
          while ( (len = _in.read(buffer)) > -1  ) {
            _out.write(buffer,0,len);
          }          
        }
      } catch ( IOException ex ) {
        System.err.println(Thread.currentThread().getName() + ":" + ex);
      }
    }
    
    public void run2() {
      try {
        int b;
        if ( _out2 != null ) {
          while ( ( b = _in.read()) != -1  ) {
            _out.write(b);
            _out2.write(b);
          }
        } else {
          while ( ( b = _in.read()) != -1  ) {
            _out.write(b);
          }          
        }
      } catch ( IOException ex ) {
        System.err.println(Thread.currentThread().getName() + ":" + ex);
      }
    }
    
  }
  
  
  public static void main ( String[] args ) {
    TCPProxy tcpProxy = new TCPProxy();
    (new Thread(tcpProxy)).start();
  }
}
