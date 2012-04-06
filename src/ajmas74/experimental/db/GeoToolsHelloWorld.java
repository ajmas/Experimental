package ajmas74.experimental.db;

import java.io.IOException;
import java.util.HashMap;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.factory.BasicFactories;
import org.geotools.jdbc.*;


public class GeoToolsHelloWorld {

    public static void main ( String[] args ) {

        HashMap params = new HashMap();
        params.put( "dbtype", "mysql");
        params.put( "database", "experiments");
        
        java.nio.ByteBuffer byteBuffer = null;
        
        //byteBuffer.
        try {
            DataStore ds = (DataStore) DataStoreFinder.getDataStore( params );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
