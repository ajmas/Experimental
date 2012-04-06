package ajmas74.experimental.audio;

import java.util.List;

public interface MediaPlayer {

	// Current track related stuff
	
	public void play();
	
	public void stop();
	
	public void pause();
	
	public void seek ( int seconds ); 

	public void fastForward ( int seconds ); 

	public void rewind ( int seconds ); 

	public boolean isPlaying();
	
	
	public void setRepeat( boolean repeat );

	public void setLoop( boolean loop );
	
	public void setRandom( boolean random );
	
	// Volume
	
	public void getVolume ();
	
	public void setVolume ( int volume );
	
	public void mute ( boolean mute );
	
	public void increaseVolume ( int steps );
	
	public void decreaseVolume ( int steps );
	
	
	
	// Playlist related stuff
	
	public void next();
	
	public void previous();
		
	public void clearPlaylist();
	
	public void enqueue ( String mediaReference );
	
	public void enqueue ( List<String> mediaReferences );
	
	public String getPlaylist ();
	
	
	// Info
	
	public String getStatus();
	
	public String getTitle();
	
	public long getLength();
	
	public String getInfo();
	
	
	
	
}
