package ajmas74.experiments;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GenericURLStreamHandler extends URLStreamHandler {

  /**
   * Constructor for GenericURLStreamHandler.
   */
  public GenericURLStreamHandler() {
    super();
  }

  /**
   * @see java.net.URLStreamHandler#openConnection(java.net.URL)
   */
  protected URLConnection openConnection(URL u) throws IOException {
    return null;
  }

}
