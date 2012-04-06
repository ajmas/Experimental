package ajmas74.experimental.iodevice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import gnu.io.*;

public class Spaceball5000 implements Runnable {

    private static final String NIBBLES = "0AB3D56GH9:K<MN?";
    
    private static final int MAX_DATA_SIZE = 128;
    
    public final static char KEY_PRESS = 'k';
    public final static char DIRECTIONAL_INPUT = 'd';
    
    public final static int BUTTON_A = 0x42;
    public final static int BUTTON_B = 0x44;
    public final static int BUTTON_C = 0x48;
    public final static int BUTTON_1 = 0x410000;
    public final static int BUTTON_2 = 0x420000;
    public final static int BUTTON_3 = 0x440000;
    public final static int BUTTON_4 = 0x480000;
    public final static int BUTTON_5 = 0x4100;
    public final static int BUTTON_6 = 0x4200;
    public final static int BUTTON_7 = 0x4400;
    public final static int BUTTON_8 = 0x4800;
    public final static int BUTTON_9 = 0x41;
    
    private static final Object[][] keyPressNameArray = new Object [][] {
        { BUTTON_A, 'A' }, { BUTTON_B, 'B' }, { BUTTON_C, 'C' },
        { BUTTON_1, '1' }, { BUTTON_2, '2' }, { BUTTON_3, '3' },
        { BUTTON_4, '4' }, { BUTTON_5, '5' }, { BUTTON_6, '6' },
        { BUTTON_7, '7' }, { BUTTON_8, '8' }, { BUTTON_9, '9' },
    };

    
//    Set<Character> charSet = new HashSet<Character>();
    
    InputStream in;
    OutputStream out;
    SerialPort serialPort;
    boolean connected;
    
    private int baud = 9600;
    private int stopBits = SerialPort.DATABITS_8;
    private int dataBits= SerialPort.STOPBITS_1;
    private int parity = SerialPort.PARITY_NONE;
    private int timeout = 2000;
    
    private static class Packet {                
        char type;
        int length;
        byte[] data = new byte[MAX_DATA_SIZE];
        
        @Override
        public String toString() {
            return "SpaceBallPacket: [" +type + "] " + arrayToString(data,length);
        }
    }
    
    public void connect( String port ) throws IOException
    {        
        try
        {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);
            CommPort commPort = portIdentifier.open(this.getClass().getName(),this.timeout);
            if ( commPort instanceof SerialPort )
            {
                this.serialPort = (SerialPort) commPort;
                //this.serialPort.setSerialPortParams(this.baud,this.dataBits,this.stopBits,this.parity);
                this.in = this.serialPort.getInputStream();
                this.out = this.serialPort.getOutputStream();
                
                if ( in == null ) {
                    throw new IOException("Inputstream is null");
                }
            }
            else
            {
                throw new IOException(commPort.getName() + " is not a serial port");
            }
            
            
        }
        catch ( NoSuchPortException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            disconnect();           
            return;            
        }
        catch ( PortInUseException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            disconnect();           
            return;            
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            disconnect();           
            throw new RuntimeException(e);
        }

        
        this.connected = true;        
    }
    
    
    public void disconnect() throws IOException
    {
        if ( this.serialPort != null )
        {
            this.serialPort.close();
        }
        this.connected = false;        
    }
    
    public void init() {

    }
    
    
    /**
     * This method filters out the high bits, since the important data
     * is in the lower first four bits. Before doing the conversion the
     * byte is matched against a lookup table to see if it is a recognised
     * value.
     * 
     * @param data
     * @param count
     * @return
     */
    private static boolean ignoreHighNibble ( byte[] data, int count ) {
        for ( int i=0; i<count; i++ ) {
            if ( NIBBLES.indexOf( data[i] ) > -1  ) {
                data[i] = (byte) (data[i] & 0x0F);
            }
            else {
                return false;
            }
        }
        return true;
    }

    
    /**
     * This decodes the key data
     * 
     * @param packet
     */
    private void decodeKeyData ( Packet packet ) {

        byte[] data = packet.data;
                
        if ( ! ignoreHighNibble(data, 3) ) {
            return;
        }
        
        int keyPressBitMap = (data[0] << 16 | data[1] <<  8 | data[2]);
        
        for ( int i=0; i< keyPressNameArray.length; i++ ) {
            if ( (keyPressBitMap & ((Integer)keyPressNameArray[i][0])) != 0) {
                System.out.println ( "Key: " + keyPressNameArray[i][1] );
            }
        }
    }

    /**
     * This decodes the ball data
     * 
     * @param packet
     */
    private void decodeDirectionalData ( Packet packet ) {
        
        byte[] data= packet.data;
         
        if ( ! ignoreHighNibble(packet.data,24) ) {
            return;
        }
        
        for ( int i=0; i<6; i++) {
            int l = ( (data[(i * 4) + 0] << 12 | data[(i * 4) + 1] << 8 |
                     data[(i * 4) + 2] <<  4 | data[(i * 4) + 3]) - 32768 );
            System.out.print(l+",");
        }
        System.out.println();
    }
    
    public void decodeEvent ( Packet packet, int length ) {
         switch ( packet.type ) {
            case KEY_PRESS:
                decodeKeyData(packet);
                break;
            case DIRECTIONAL_INPUT:
                decodeDirectionalData(packet);
                break;                    
        }       
    }
    
    public void mainLoop(InputStream in, OutputStream out) throws IOException {

        try {
            int length = 0;
            
            Packet packet = new Packet();            
                      
            write ( out, "vQ" );
            read ( in, packet );
            System.out.println(packet);
            
            write ( out, "vQ" );
            read ( in, packet );
            System.out.println(packet);
            
            write ( out, "m3" );
            write ( out, "q00" );
            write ( out, "pAA" );
            write ( out, "n?" );
            
            while ( (length = read ( in, packet ))  > -1 ) {
                System.out.println(packet);
                decodeEvent(packet, length);
                Thread.yield();
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    int read ( InputStream in, Packet packet)  throws IOException {        
        int b;
        
        packet.length = 0;
        
        b = in.read();
                
        if ( b == -1 ) {
            return -1;
        }
        
        packet.type = (char) b;
        
        while ( (b = in.read()) > -1 && b != 13) {
            packet.data[packet.length++] = (byte) b;
        }
         
        packet.length = packet.length;
        
        if ( b == -1 ) {
            return -1;
        }

        return packet.length;
    }
    
    
    static String hexValue ( int c ) {
        String str = Integer.toHexString(c);
        if ( str.length() == 2 ) {
            return str;
        }
        else if ( str.length() < 2 ) {
            return "0" + str; 
        }
        return "00";
    } 
    
    void write ( OutputStream out, String data ) throws IOException {
        data += ((char)13);
        System.out.println("out: " + data);
        out.write(data.getBytes());
    }
    
    public void run() {

 
        try {
            connect( "/dev/tty.usbserial" );

            mainLoop(in, out);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    
    static String arrayToString ( byte[] bytes, int len ) {
        StringBuffer strBuf = new StringBuffer();
        for ( int i=0; i<len; i++ ) {
            strBuf.append((char)bytes[i]);
        }
        
        strBuf.append("      ");
        for ( int i=0; i<len; i++ ) {
            strBuf.append( hexValue(bytes[i]) + " ");
        }
        
        return strBuf.toString();
    }
    
    
    private static String toBinaryString ( int n ) {
        String str = Integer.toBinaryString(n);
        if ( str.length() < 8 ) {
            for ( int i=8-(8-str.length()); i < 8; i++ ) {
                str = "0" + str;
            }
        }
        return str;
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {        
        (new Thread(new Spaceball5000())).start();
    }


}
