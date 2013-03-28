/* Created on 31-Oct-2003 */
package ajmas74.experimental;

import java.io.*;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class XMLHilighter {

  String[] colours = new String[] { "#999900","#3333ff","#00dd00" };
  
  /**
	 *  
	 */
  public XMLHilighter() {
    super();
    // TODO Auto-generated constructor stub
  }

  String getColour( int type ) {
    return colours[type];
  }
  
  public String hilightXML(String xml) {
    StringBuffer strBuf = new StringBuffer();

    strBuf.append("<html>\n");
    strBuf.append("  <head>\n");
    strBuf.append("  </head>\n");
    strBuf.append("  <body>\n");
    strBuf.append("<pre>\n");
    
    int prevState = 0;
    int state = 0;
    int len = xml.length();
    for (int i = 0; i < len; i++) {

      char c = xml.charAt(i);
      switch (state) {
        case 0 :
          if (c == '<') {
            if ( xml.charAt(i+1) == '!' && xml.charAt(i+2) == '-' && xml.charAt(i+3) == '-') {
							state = 3;
							strBuf.append("<font color=\"" + getColour(2) + "\">");
							strBuf.append("&lt;!--");  
							i+=3;
            } else {
	            state = 1;
							strBuf.append("<font color=\"" + getColour(0) + "\">");
	            strBuf.append("&lt;");
            }
          } else {
            strBuf.append(c);
          }
          break;
        case 1 :
          if (c == '>') {
            state = 0;
						strBuf.append("&gt;");
						strBuf.append("</font>");
          } else if (c == '"') {
            state = 2;
						strBuf.append("<font color=\"" + getColour(1) + "\">");
            strBuf.append(c);
          } else {
            strBuf.append(c);
          }
          break;
        case 2 :
          if (c == '"') {
            state = 1;
						strBuf.append(c);
						strBuf.append("</font>");
          }
          break;
				case 3 :
					if ( xml.charAt(i) == '-' && xml.charAt(i+1) == '-' && xml.charAt(i+2) == '>' ) {
						state = 0;
						strBuf.append("--&gt;");
						strBuf.append("</font>");
						i+=2;
					} else if (c == '>') {
						strBuf.append("&gt;");
					} else if (c == '<') {
						strBuf.append("&lt;");
					} else {
						strBuf.append(c);
					}
					break;          
      }

      prevState = state;
    }
    strBuf.append("</pre>\n");
    strBuf.append("  </body>\n");
    strBuf.append("</html>\n");
    
    return strBuf.toString();
  }

  public static void main ( String[] args ) {
    try {
			XMLHilighter xh = new XMLHilighter();
			InputStream in = new FileInputStream("datafiles/settings.xml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			StringBuffer strBuf = new StringBuffer();
			String line = null;
			while ( (line = reader.readLine()) != null ) {
				strBuf.append(line+"\n");
			}
			System.out.println(xh.hilightXML(strBuf.toString()));
    } catch ( IOException ex ) {
      ex.printStackTrace();
    }
  }
}
