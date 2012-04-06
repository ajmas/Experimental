package ajmas74.experimental.audio;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bff.javampd.MPD;
import org.bff.javampd.MPDDatabase;
import org.bff.javampd.MPDPlayer;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDPlayerException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.MPDSong;

public class JavaMpdClient {

	private static final int SERVER_PORT = 6600;
	
	private static final String SERVER_NAME = "kusanagi.local";
	
	protected MPD musicPlayerDaemon;
	
	public void connect() {
		MPD mpd = null;
	    try {
	        mpd = new MPD(SERVER_NAME,SERVER_PORT);
	        System.out.println("Version:"+mpd.getVersion());
	        System.out.println("Uptime:"+mpd.getUptime());
	        mpd.close();
	    } catch(MPDConnectionException e) {
	        System.out.println("Error Connecting:"+e.getMessage());
	        e.printStackTrace();
	    } catch(MPDResponseException e) {
	        System.out.println("Response Error:"+e.getMessage());
	        e.printStackTrace();
	    } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    this.musicPlayerDaemon = mpd;
	} 
	
	public void playerExample() {
	    //get the player from the connection
	    MPDPlayer mpdPlayer = this.musicPlayerDaemon.getMPDPlayer();
	    try {
	    	MPDSong song = mpdPlayer.getCurrentSong();
	    	
	    	
	    	if ( song == null ) {
	    		System.out.println("No current track, will abort");
	    		return;
	    	}
	    	
	    	System.out.println("Current Track : " + song );
	    	
	    	
	        mpdPlayer.play();
	        mpdPlayer.getElapsedTime();
	        mpdPlayer.pause();
	        mpdPlayer.setXFade(5);
	        mpdPlayer.setRepeat(true);
	        mpdPlayer.seek(100);
	        mpdPlayer.play();
	        mpdPlayer.stop();
	    } catch(MPDConnectionException e) {
	        System.out.println("Connection error:"+e.getMessage());
	        e.printStackTrace();
	    } catch (MPDPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public void play ( MPDSong track ) throws Exception {
	    MPDPlayer mpdPlayer = this.musicPlayerDaemon.getMPDPlayer();
	    mpdPlayer.playId(track);
	}
	
	public void play ( String trackName ) throws Exception {
		MPDDatabase mpdDatabase = this.musicPlayerDaemon.getMPDDatabase();
		
		Collection<MPDSong> tracks = mpdDatabase.listAllSongs();//findAlbum("Vandread");
		
		System.out.println("length: " + tracks.size());
		
		play(tracks.iterator().next());
	}
	
	public void databaseExample() throws Exception {
	    try {
	        //first get the database from the connection
	        MPDDatabase mpdDatabase = this.musicPlayerDaemon.getMPDDatabase();
	            
	        System.out.println("Finding Songs by Artist Cannible Corpse:");
	        List<MPDSong> list = new ArrayList<MPDSong>(mpdDatabase.findArtist("Cannible Corpse"));
	        for(MPDSong s : list) {
	            System.out.println(s);
	        }
	        
	        System.out.println("Finding Songs in Album Unplugged:");
	        list = new ArrayList<MPDSong>(mpdDatabase.findAlbum("Unplugged"));
	        for(MPDSong s : list) {
	            System.out.println(s);
	        }
	                    
	        System.out.println("Searching for Songs by Artist Clapton, found:");
	        list = new ArrayList<MPDSong>(mpdDatabase.searchArtist("Clapton"));
	        for(MPDSong s : list) {
	            System.out.println(s);
	        }
	            
	        System.out.println("Searching for Songs in Album Unplugged:");
	        list = new ArrayList<MPDSong>(mpdDatabase.searchAlbum("Unplugg"));
	        for(MPDSong s : list) {
	            System.out.println(s);
	        }
	                       
	        System.out.println("Searching Title Lay, found:");
	        list = new ArrayList<MPDSong>(mpdDatabase.searchTitle("Lay"));
	        for(MPDSong s : list) {
	            System.out.println(s);
	        }
	                       
	        System.out.println("Listing All Files:");
	        List<String> stringList = new ArrayList<String>(mpdDatabase.listAllFiles());
	        for(String s : stringList) {
	            System.out.println(s);
	        }
	            
//	        System.out.println("Listing All Albums:");
//	        stringList = new ArrayList<String>(mpdDatabase.listAllAlbums());
//	        for(String s : stringList) {
//	            System.out.println(s);
//	        }
//	                
//	        System.out.println("Listing All Artists:");
//	        stringList = new ArrayList<String>(mpdDatabase.listAllArtists());
//	        for(String s : stringList) {
//	            System.out.println(s);
//	        }
//	            
//	        System.out.println("Listing All Albums by Artist Eric Clapton:");
//	        stringList = new ArrayList<String>(mpdDatabase.listAlbumsByArtist("Eric Clapton"));
//	        for(String s : stringList) {
//	            System.out.println(s);
//	        }
	           
	        System.out.println("Listing All Playlists:");
	        stringList = new ArrayList<String>(mpdDatabase.listPlaylists());
	        for(String s : stringList) {
	            System.out.println(s);
	        }
	            
	    } catch(MPDConnectionException e) {
	        System.out.println("Connection error:"+e.getMessage());
	        e.printStackTrace();
	    }
	} 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			JavaMpdClient client = new JavaMpdClient();
			client.connect();
			//client.playerExample();
			
			client.databaseExample();
			
			client.play("xxx");
			
		}
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

}

//ref: http://www.thejavashop.net/javampd/examples.shtml
