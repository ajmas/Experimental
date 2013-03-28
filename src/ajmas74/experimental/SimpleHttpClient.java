/*
 * Created on 14-May-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ajmas74.experimental;

import java.io.*;
import java.net.*;

/**
 * @author andrmas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SimpleHttpClient {

  //int[] SAMPLE = new int[] { 0xEF, 0xBB, 0xBF, 0xC3, 0xA8, 0xC3, 0xA9,};
  int[] SAMPLE = new int[] { 0xC3, 0xA8, 0xC3, 0xA9,};
  
  SimpleHttpClient () {
  }
  
  public String sendx(URL destinationUrl, int timeout, String message)
    throws Exception
  {
    //try
    URLConnection connection = destinationUrl.openConnection();
    connection.setRequestProperty("Content-type","text/xml; charset=UTF-8");
    connection.setRequestProperty("user-agent", "Magnor");
  
    connection.setDoInput(true);
    connection.setDoOutput(true);
          
    OutputStream out = connection.getOutputStream()  ;            
    FileInputStream fin = new FileInputStream("K:\\development\\Experimental\\datafiles\\text.txt");
    int val =0;
    while ( (val = fin.read()) > -1 ) {
      System.out.print((char)val);
      out.write((byte)val);    
    }
    System.out.println("");
    System.out.println("--------------------------");
    System.out.println("--------------------------");
    System.out.println("--------------------------");
    System.out.println("");    
//    int[] data = ByteSequenceConverter.convert(null);
//    for ( int i=0; i<data.length&&data[i]!=-1;i++) {
//   
//    }
    out.flush();
          
    InputStream in = connection.getInputStream();
    InputStreamReader inr = new InputStreamReader(in,"utf-8");
    BufferedReader br = new BufferedReader(inr);
      
    StringBuffer strBuf = new StringBuffer();
    String line = null;
    while ( (line = br.readLine()) != null ) {
      strBuf.append(line);
      strBuf.append('\n');
    }
    return strBuf.toString();    
  }


  public static void main ( String[] args ) {
    SimpleHttpClient htc = new SimpleHttpClient();
    try {
      //System.out.println(htc.sendx(new URL("http://andrmas:8081/providerspoofer/soap"),120,""));
      System.out.println(htc.sendx(new URL("http://192.168.0.23:18001/polaris_receiver"),120,""));
    } catch (MalformedURLException e) {
      // XXX Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // XXX Auto-generated catch block
      e.printStackTrace();
    }
  }
  
}
