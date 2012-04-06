package ajmas74.experiments;

/**
 * @author andrmas
 */
public class DiscID {

	
  public static class TableOfContents {
    
    public TableOfContents ( int min, int sec, int frame ) {
      this.min = min;
      this.sec = sec;
      this.frame = frame;
    }
    
    int	min;
    int	sec;
    int	frame;
    
    public int getLength () {
      return (min*60)+sec;
    }
    
    public void setLength ( int lenInSecs ) {
      min = lenInSecs / 60;
      sec = lenInSecs % 60;
    }    
  };

  
  private int cddbSum(int n)
  {
    /* For backward compatibility this algorithm must not change */

    int ret = 0;

    while (n > 0) {
      ret = ret + (n % 10);
      n = n / 10;
    }

    return ret;
  }

  public long calculateCDDBDiscId(TableOfContents[] tracks )
  {
    //int	i = 0;
    int t = 0;
    int n = 0;

    /* For backward compatibility this algorithm must not change */

    //i = 0;

    //while (i < tracks.length-1) {
    for ( int i=0; i < tracks.length-1; i++ ) {
      n = n + cddbSum((tracks[i].min * 60) + tracks[i].sec);
      //i++;
    }

   // t = ((tracks[tracks.length-1].min * 60) + tracks[tracks.length-1].sec) -
   //     ((tracks[0].min * 60) + tracks[0].sec);
		//t = (68*60)+56;
    t= (tracks[0].min * 60 * 75) + (tracks[0].sec * 75) + 0 +
    (tracks[tracks.length-1].min * 60 * 75) + (tracks[tracks.length-1].sec * 75) + tracks.length + 1;
    
    long id = ((n % 0xff) << 24 | t << 8 | tracks.length);
    //if ( id < 0 ) {
    System.out.println("a: " +id); 

    id+=(4294967296L);
    System.out.println("b: " +id); 
    //}
    return id;
  }
  
  public static void main(String[] args) {
    TableOfContents[] tracks = new TableOfContents[11];
    tracks[0] = new TableOfContents(6,57,0);
    tracks[1] = new TableOfContents(7,34,1);
    tracks[2] = new TableOfContents(6,37,2);
    tracks[3] = new TableOfContents(7,24,3);
    tracks[4] = new TableOfContents(8,51,4);
    tracks[5] = new TableOfContents(3,35,5);
    tracks[6] = new TableOfContents(7,47,6);
    tracks[7] = new TableOfContents(5,46,7);
    tracks[8] = new TableOfContents(9,21,8);
    tracks[9] = new TableOfContents(5,2,9);
    tracks[10] = new TableOfContents(0,2,10);
    
    tracks[0] = new TableOfContents(6,58,0);
    tracks[1] = new TableOfContents(7,34,1);
    tracks[2] = new TableOfContents(6,37,2);
    tracks[3] = new TableOfContents(7,25,3);
    tracks[4] = new TableOfContents(8,51,4);
    tracks[5] = new TableOfContents(3,37,5);
    tracks[6] = new TableOfContents(7,46,6);
    tracks[7] = new TableOfContents(5,45,7);
    tracks[8] = new TableOfContents(9,21,8);
    tracks[9] = new TableOfContents(4,58,9);
    tracks[10] = new TableOfContents(0,4,10);    
        
    for ( int i=1; i<tracks.length; i++ ) {
      System.out.println(i + " a = " + tracks[i].getLength());
      tracks[i].setLength(tracks[i-1].getLength() + tracks[i].getLength());
      System.out.println(i + " b = " + tracks[i].getLength());      
    }
    System.out.println( (tracks[tracks.length-1].min) + ":" +
     (tracks[tracks.length-1].sec) );
    
    DiscID discId = new DiscID();
    
    long id = discId.calculateCDDBDiscId(tracks);
    System.out.println("disc ID="+id);
    System.out.println("disc ID="+Long.toString(id,16));
    
 
  }
}
