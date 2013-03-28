package ajmas74.experimental;

/**
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class PhotoAlbumHTML {

//  public final static String LEFT_BORDER_SUFFIX = "_left.jpg";
//  public final static String RIGHT_BORDER_SUFFIX = "_right.jpg";
//  public final static String TOP_BORDER_SUFFIX = "_top.jpg";
//  public final static String BOTTOM_BORDER_SUFFIX = "_bot.jpg";
        
  public final static String LEFT_BORDER_SUFFIX = "_left.gif";
  public final static String RIGHT_BORDER_SUFFIX = "_right.gif";
  public final static String TOP_BORDER_SUFFIX = "_top.gif";
  public final static String BOTTOM_BORDER_SUFFIX = "_bot.gif";
          
//  private String _borderBaseFolder = "./borders/";
  private String _borderBaseFolder = "";
  private String _separator = "";
  
	/**
	 * Constructor for PhotoAlbumHTML.
	 */
	public PhotoAlbumHTML() {
		super();
	}

  private String createPhotoFrame ( String photoURL, String largePhotoURL, String caption, String borderName ) {
    StringBuffer strBuf = new StringBuffer();
    strBuf.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
        
    strBuf.append("<tr>");

    strBuf.append("<td rowspan=\"3\"><img src=\"");
    strBuf.append( _borderBaseFolder + borderName + _separator + LEFT_BORDER_SUFFIX );
    strBuf.append("\"></td>");
        
    strBuf.append("<td><img src=\"");
    strBuf.append( _borderBaseFolder + borderName + _separator + TOP_BORDER_SUFFIX );
    strBuf.append("\"></td>");
    
    strBuf.append("<td rowspan=\"3\"><img src=\"");
    strBuf.append( _borderBaseFolder + borderName + _separator + RIGHT_BORDER_SUFFIX );
    strBuf.append("\"></td>");
        
    strBuf.append("</tr>");
    
    strBuf.append("<tr><td align=\"center\" valign=\"center\" height=\"141\">"); 
    strBuf.append("<a href=\"");  
    strBuf.append(largePhotoURL);
    strBuf.append("\"><img src=\"");
    strBuf.append( photoURL );
    strBuf.append("\" alt=\"gallery photo\" border=\"0\">");
    strBuf.append("</td></tr>");
    
    strBuf.append("<tr>");    
    strBuf.append("<td><img src=\"");
    strBuf.append( _borderBaseFolder + borderName + _separator + BOTTOM_BORDER_SUFFIX );
    strBuf.append("\"></td>");
    strBuf.append("</tr>");
       
    strBuf.append("<tr><td colspan=\"3\" align=\"center\">");  
    strBuf.append(caption);
    strBuf.append("</td></tr>");         
    strBuf.append("</table>");
    return strBuf.toString();
  }
  
	public static void main(String[] args) {
    System.out.println("<table><tr><td>");
    System.out.println((new PhotoAlbumHTML()).createPhotoFrame(
      "http://homepage.mac.com/kangol/.cv/kangol/Sites/.Pictures/FH000005.JPG-thumb_106_141.jpg",
      "http://homepage.mac.com/kangol/.Pictures/FH000005.JPG",
      "Fridge",
      "file:///k:/development/Experimental/datafiles/borders/2/portrait/Modern_frame")
    );
    System.out.println("</td><td>");
    System.out.println((new PhotoAlbumHTML()).createPhotoFrame(
      "http://homepage.mac.com/kangol/.cv/kangol/Sites/.Pictures/FH000005.JPG-thumb_106_141.jpg",
      "http://homepage.mac.com/kangol/.Pictures/FH000005.JPG",
      "Fridge",
      "file:///k:/development/Experimental/datafiles/borders/2/portrait/Modern_frame")
    );
    System.out.println("</td></tr></table>");    
	}
}
