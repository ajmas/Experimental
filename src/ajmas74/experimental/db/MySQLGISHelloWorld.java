package ajmas74.experimental.db;

import java.nio.ByteBuffer;
import java.sql.*;

public class MySQLGISHelloWorld {

    private static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    
    MySQLGISHelloWorld () throws SQLException {
        
        String databaseUrl   = "jdbc:mysql://localhost/experiments";

        String query = "SELECT address, asBinary(address_loc) FROM address ";

        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            Class.forName(MYSQL_DRIVER_CLASS_NAME);
            Class.forName  ("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection (
                    databaseUrl, "ajmas", null );

                stmt = con.createStatement ();

                rs = stmt.executeQuery (query);
                
                System.out.println(rs.getClass());
                while ( rs.next() ) {
                    System.out.print( rs.getObject(1) );
                    System.out.print( ", " );
                    
                    byte[] bytes = (byte[]) rs.getObject(2);
                    
                    
                    System.out.print( (int)bytes[0] );
                    System.out.print( ", " );
                    System.out.println( bytes.length );
                    
                    
       
                    
                }

                //com.mysql.jdbc.JDBC4ResultSet rs
                //printResultSet ( resp, rs );

                rs.close();
                stmt.close();
                con.close();

          } catch (Exception e) {
            e.printStackTrace();
          }
          finally {
              if ( rs != null ) { rs.close(); }
              if ( stmt != null ) { stmt.close(); }
              if ( con != null ) { con.close(); }
          }
        
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        
        
//        try {
//            new MySQLGISHelloWorld();
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        byte[] bytes = "Hello".getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.limit(bytes.length);
        byteBuffer.rewind();
        byteBuffer.put(bytes);
        byteBuffer.rewind();
        System.out.println((char) byteBuffer.get(0) );  
        
        bytes = "Goodbye".getBytes();
        byteBuffer.limit(bytes.length);
        byteBuffer.rewind();
        byteBuffer.put(bytes);       
        byteBuffer.rewind();
        System.out.println((char) byteBuffer.get(0) );        
        
        
    }

}
