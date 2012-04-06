/* Created on 21-Jan-2004 */
package ajmas74.experiments;

import java.io.*;
import java.net.*;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class PNMRecorder {

  private static final String NEWLINE = "\r\n";
  /**
   * 
   */
  public PNMRecorder() {
    super();
    
    System.out.println("Starting ... ");
    
    String host = "localhost";
    int port = 7070;
    
    // TODO Auto-generated constructor stub
    HTTPSender c1 = new HTTPSender(host,port);
    c1.setSendToFile(true);
    //c1.setSendToScreen(true);
    c1.setFileName("c:/temp/abc.db");
    StringBuffer strBuf = new StringBuffer();
    strBuf.append("GET /SmpDsBhgRl08b8bba9-0c0a-4c0d-a77c-5465839ad6e6 HTTP/1.0" + NEWLINE);
    strBuf.append("User-Agent: RealPlayer G2" + NEWLINE);
    strBuf.append("Expires: Mon, 18 May 1974 00:00:00 GMT" + NEWLINE);
    strBuf.append("Pragma: no-cache" + NEWLINE);
    strBuf.append("Accept: application/x-rtsp-tunnelled, */*" + NEWLINE);     
    strBuf.append(NEWLINE);
    c1.setRequest(strBuf.toString());
    
    HTTPSender c2 = new HTTPSender(host,port);
    strBuf = new StringBuffer();
    strBuf.append("POST /SmpDsBhgRl HTTP/1.0" + NEWLINE);
    strBuf.append("User-Agent: RealPlayer G2" + NEWLINE);
    strBuf.append("Pragma: no-cache" + NEWLINE);
    strBuf.append("Expires: Mon, 18 May 1974 00:00:00 GMT" + NEWLINE);
    strBuf.append("Accept: application/x-rtsp-tunnelled, */*" + NEWLINE);
    strBuf.append("Content-type: application/x-pncmd" + NEWLINE);
    strBuf.append("Content-length: 32767" + NEWLINE);
    strBuf.append(NEWLINE);
    strBuf.append("08b8bba9-0c0a-4c0d-a77c-5465839ad6e6" + NEWLINE);
    strBuf.append("UE5BAAoAFAACAAEABAAgZWUyMGRkOTBhOGU2ZDY5YTkxMTJhZGU2NzIxYzI4YjcAAwB+B4pwbnJ2AJBwbnJ2AGRkbmV0AEZwbnJ2ADJkbmV0ACtwbnJ2AChkbmV0ACRwbnJ2ABlkbmV0ABhwbnJ2ABRzaXByABRkbmV0ACQyOF84ABJwbnJ2AA9kbmV0AApzaXByAApkbmV0AAhzaXByAAZzaXByABJscGNKAAcwNV82AAoAAAAMAAAADQAAABYAAgABABcAHFsyMS8wMS8yMDA0OjEzOjIxOjAzIC0wNTowMF0ABQAEABePQAAIAAAADgAAAA8AAAARAAAAEAAAABUAAAASAAAAEwAkMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwABgAEB1Kse5EHRQk+ZsLga7I0KgAACft6JxjADBXaW5OVF81LjBfNi4wLjExLjg2OF9SZWFsUGxheWVyX1IxUDIzRF9lbi1VU182ODZsAABSABZtYXRjL2NhbGN1bHVzc2VyaWVzLnJteQ==");
    c2.setRequest(strBuf.toString());
    c2.setSendToScreen(true);
    
    (new Thread(c1,"C1")).start();
    System.out.println("Thread C1 started");
    
    try {
      Thread.sleep(300);
    } catch ( InterruptedException ex ) {
      
    }
    (new Thread(c2,"C2")).start();
    System.out.println("Thread C2 started");
  }

  static class HTTPSender implements Runnable {
    
    String _encoding = "ISO-8859-1";
    String _host;
    int    _port;
    
    
    String  _request;
    boolean _sendToScreen;
    boolean _sendToFile;
    String  _fileName;
    
    HTTPSender ( String host, int port ) {
      _host = host;
      _port = port;
    }
    
    public void run (){
      OutputStream fout = null;
      Socket sock = null;     
      try {
        if ( _sendToFile ) {
          fout = new FileOutputStream(_fileName);
        }
        sock = new Socket(_host,_port);
        OutputStream out = sock.getOutputStream();
        if ( _sendToScreen ) {
          System.out.println("------ REQUEST -------");
          System.out.println(_request);
        }
        out.write(_request.getBytes());
        out.flush();
        
        if ( _sendToScreen ) {
          System.out.println("------ RESPONSE -------");         
        }
        
        InputStream in = sock.getInputStream();        
        byte[] buffer = new byte[4096];
        int len = 0;
        int total = 0;
        
        while ( (len = in.read(buffer)) > -1 ) {
          total += len;
          if ( _sendToScreen ) {
            System.out.print(new String(buffer,0,len));
          } else {
            if ( total % 300 == 0 ) {
              System.out.println("read: " +total);
            }
          }
          if ( _sendToFile ) {
            fout.write(buffer,0,len);
            fout.flush();
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
  
  public static void main ( String[] args ) {
    PNMRecorder rec = new PNMRecorder();
  }
}
