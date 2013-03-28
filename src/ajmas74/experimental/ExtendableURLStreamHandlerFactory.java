package ajmas74.experimental;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;

/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ExtendableURLStreamHandlerFactory
  implements URLStreamHandlerFactory {

	HashMap _streamHandlerMap;
	
  public ExtendableURLStreamHandlerFactory () {
    _streamHandlerMap = new HashMap();
  }
  
  public void addHandler( String protocol, Class handlerClass ) {
    _streamHandlerMap.put(protocol,handlerClass);
  }
  
  /**
   * @see java.net.URLStreamHandlerFactory#createURLStreamHandler(java.lang.String)
   */
  public URLStreamHandler createURLStreamHandler(String protocol) {
    Class c = (Class) _streamHandlerMap.get(protocol);
    if ( c != null ) {
    	try {
        return (URLStreamHandler) c.newInstance();
      } catch (InstantiationException e) {
      } catch (IllegalAccessException e) {
      }
    }
    return null;    
  }

	public static void main ( String[] args ) {
    //GenericURLStreamHandler handler = new GenericURLStreamHandler();
    ExtendableURLStreamHandlerFactory factory = new ExtendableURLStreamHandlerFactory();
    factory.addHandler("dict",GenericURLStreamHandler.class);
    URL.setURLStreamHandlerFactory(factory);
    try {
      System.out.println(new URL("dict://bob/"));
      System.out.println(new URL("dicxt://bob/"));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
	}
}
