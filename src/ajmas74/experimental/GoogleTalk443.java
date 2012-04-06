package ajmas74.experimental;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GoogleTalk443 {

    public GoogleTalk443() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static String removeSpaces ( String text )
    {
        if ( text.indexOf(' ') > -1 )
        {
            StringBuilder strBuilder = new StringBuilder();
            for ( int i=0; i<text.length();i++ )
            {
                char c = text.charAt(i);
                if (c != ' ' )
                {
                    strBuilder.append(c);
                }
            }
            text = strBuilder.toString();
        }
        return text;
    }
    
    public static byte[] hexStringToBytes( String hexString ) 
    { 
        hexString = removeSpaces(hexString); 

        byte[] bytes = new byte[hexString.length()/2]; 
        for ( int i=0; i<hexString.length(); i+=2 ) 
        { 
            bytes[i/2]=(byte) Integer.parseInt( hexString.substring(i,i+2), 16); 
        } 
        return bytes; 
    } 

    public static void main( String[] args ) 
    { 
        try 
        { 
            byte[] bytes = new byte[2056]; 

            Socket socket = new Socket("talk.google.com",443); 
            OutputStream out = socket.getOutputStream(); 
            InputStream in = socket.getInputStream(); 

            out.write(hexStringToBytes("80460103 01002D00 00001001 00800300 800700C0 06004002 00800400 80000004 00FEFF00 000A00FE FE000009 00006400 00620000 03000006 1F170CA6 2F0078FC 46552EB1 8339F1EA")); 
            out.flush(); 

            in.read(bytes,0,6); 
            in.read(bytes,0,79); 
            out.write( ("<stream:stream to=\"gmail.com\" version=\"1.0\" xmlns:stream=\"http://etherx.jabber.org/streams\" xmlns=\"jabber:client\">" + 0x0D +  0x0A).getBytes()); 

            // This should be the Jabber XML response from Google 
            char c = 0; 
            while ( ( c = (char) in.read() ) != 0x0D ) 
            { 
                System.out.print(c); 
            } 
            // we are expecting a new line char (0x0A) 
            in.read(); 

        } 
        catch ( Exception e ) 
        { 
            e.printStackTrace(); 
        } 

    }     
}
