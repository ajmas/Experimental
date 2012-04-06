package ajmas74.experimental;

import java.io.*;
import java.util.Arrays;

public class FileParser {

    public static void main ( String[] args ) {
        
        InputStream in = null;
        try {
            in = new FileInputStream("/Users/ajmas/Documents/Zigidoo/second-cup.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            int i=0;
            while ( (line = reader.readLine()) != null ) {
                if ( i== 3 ) {
                    System.out.println("");
                    i = 0;
                }
                if ( i==0 ) { 
                    String[] parts = line.split(",");
                    
                    System.out.println("address: " + Arrays.toString(parts) );                    
                    System.out.println("  street: " + parts[0] );                    
                    System.out.println("  city: " + parts[parts.length-3] );                    
                    System.out.println("  province: " + parts[parts.length-2] );                    
                    System.out.println("  postal code: " + parts[parts.length-1] );                    

                }
                else if ( i==1 ) {
                    System.out.println("phone: " + line);                    
                }
                else if ( i==2 ) {
                    System.out.println("internet: " + line);                    
                }

                i++;
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
}
