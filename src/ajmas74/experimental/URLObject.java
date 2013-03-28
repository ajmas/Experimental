package ajmas74.experimental;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * @author Andre-John Mas &lt;andrejohn.mas@gmail.com%gt;, and Y. Duchesne
 * @version 1.5
 *
 * This version of URLObject now includes support for user and
 * password elements as well as support for the new specification
 * of URL, as specified by amended by RFC 2732. This RFC specifies
 * that IP addresses can be surrounded by square brackets. This is
 * in order to support IPv6 IP addresses, which include colons.
 *
 * RFC 2396 is the original specification.
 *
 */
public class URLObject {

  public static final int UNDEFINED_PORT    = -1;

  private static final int ERROR            = -1;
  private static final int START            = 0;
  private static final int PROTOCOL         = 1;
  private static final int USER             = 2;
  private static final int PASSWORD         = 3;
  private static final int HOST             = 4;
  private static final int PORT             = 5;
  private static final int PATH             = 6;
  private static final int END              = 7;

  private static final char COLON           = ':';
  private static final char SLASH           = '/';
  private static final char QUESTION_MARK   = '?';
  private static final char AMPERSHAND      = '&';
  private static final char LEFT_BRACKET    = '[';
  private static final char RIGHT_BRACKET   = ']';
  private static final char AT   = '@';

  private int _port = UNDEFINED_PORT;
  private String _host, _protocol;
  private String _pathInfo = "/";
  private String _basePath = "/";
  private String _fullURL;
  private String _password;
  private String _user;
  private HashMap _params = new HashMap();
  private boolean _ipv6Address = false;

  public URLObject ( String url ) throws IOException {
    try {
      _fullURL = url;
      syntaxFSM(_fullURL);

    } catch(Throwable t){
        t.printStackTrace();
      throw new IOException("invalid url (" + url + ");" +
        "format must be: 'protocol://host:port?param1=value1&param2=value' - "
        + t.getMessage());
    }
  }

  /** Finite state machine for parsing URL */
  private void syntaxFSM ( String url) throws ParseException {
    StringBuffer urlStrBuf = new StringBuffer(url);
    int state = START;
    int pos = 0;
    int idx = -1;
//  	try {
      while ( !(state == END || state == ERROR)) {
        switch ( state ) {
          case START:
            state = PROTOCOL;
          break;
          case PROTOCOL:
            idx = pos;
            while ( idx < url.length()
              && urlStrBuf.charAt(idx) != COLON ) {
              idx++;
            }
            _protocol = urlStrBuf.substring(pos,idx);
            if ( urlStrBuf.charAt(idx) == COLON ) {
              if ( urlStrBuf.charAt(idx+1) == SLASH
                && urlStrBuf.charAt(idx+2) == SLASH ) {
                if ( urlStrBuf.toString().indexOf(AT) > -1 ) {
                  state = USER;
                } else {
                  state = HOST;
                }
                idx+=2;
              } else {
                state = PATH;
              }
              idx++;
            } else {
              state = ERROR;
            }
            pos = idx;
          break;
          case USER:
            idx = pos;
            while ( idx < url.length()
              && !( urlStrBuf.charAt(idx) == COLON
              || urlStrBuf.charAt(idx) == AT) ) {
              idx++;
            }
            _user = urlStrBuf.substring(pos,idx);
            if ( idx >= url.length() ) {
              state = ERROR;
            } else if ( urlStrBuf.charAt(idx) == COLON ) {
              state = PASSWORD;
            } else if ( urlStrBuf.charAt(idx) == AT ) {
              state = HOST;
            } else {
              state = ERROR;
            }
            idx++;
            pos = idx;
          break;
          case PASSWORD:
            idx = pos;
            while ( idx < url.length()
              && urlStrBuf.charAt(idx) != AT ) {
              idx++;
            }
            _password = urlStrBuf.substring(pos,idx);
            if ( idx >= url.length() ) {
              state = ERROR;
            } else if ( urlStrBuf.charAt(idx) == AT ) {
              state = HOST;
            } else {
              state = ERROR;
            }
            idx++;
            pos = idx;
          break;
          case HOST:
            idx = pos;
            if ( urlStrBuf.charAt(idx) == LEFT_BRACKET ) {
              while ( idx < url.length()
                && urlStrBuf.charAt(idx) != RIGHT_BRACKET ) {
                idx++;
              }
              _host = urlStrBuf.substring(pos+1,idx);
              if ( _host.indexOf(COLON) > -1 ) {
                _ipv6Address = true;
              }
              if ( idx >= url.length() ) {
                state = ERROR;
              } else if ( urlStrBuf.charAt(idx) == RIGHT_BRACKET ) {
                state = HOST;
                idx++;
                if ( urlStrBuf.charAt(idx) == COLON ) {
                  state = PORT;
                  idx++;
                } else if ( urlStrBuf.charAt(idx) == SLASH ) {
                  state = PATH;
                } else {
                  state = PATH;
                }
              }
              pos = idx;
            } else {
              while ( idx < url.length()
                && !( urlStrBuf.charAt(idx) == SLASH
                || urlStrBuf.charAt(idx) == COLON) ) {
                idx++;
              }
             _host = urlStrBuf.substring(pos,idx);
             if ( idx >= url.length() ) {
               state = PATH;
             } else if ( urlStrBuf.charAt(idx) == COLON ) {
               state = PORT;
               idx++;
             } else if ( urlStrBuf.charAt(idx) == SLASH ) {
               state = PATH;
             } else {
               state = ERROR;
             }
             pos = idx;
            }
          break;
          case PORT:
            idx = pos;
            while ( idx < url.length()
              && !( urlStrBuf.charAt(idx) == SLASH
              || urlStrBuf.charAt(idx) == COLON) ) {
              idx++;
            }
            _port = Integer.parseInt(urlStrBuf.substring(pos,idx));
            state = PATH;
            //idx++;
            pos = idx;
          break;
          case PATH:
            _pathInfo = urlStrBuf.substring(pos);
            state = END;
          break;
          case END:
          default:
        }
      }
    if ( state == ERROR ) {
      System.err.println("parse error");
      throw new ParseException("error while parsing url",pos);
    }

    _basePath = _pathInfo;
    if ( _pathInfo.indexOf("?") > -1 ) {
      _basePath = parseBasePath(_pathInfo);
      _params = parseParams(_pathInfo);
    }
//    System.out.println("protocol ...... " + _protocol);
//    System.out.println("host .,,,...... " + _host);
//    System.out.println("IPv6 Address .. " + _ipv6Address);
//    System.out.println("port .,,,...... " + _port);
//    System.out.println("user .,,,...... " + _user);
//    System.out.println("password ...... " + _password);
//    System.out.println("path .,,,...... " + _pathInfo);
  }


  /**
   * Returns the value corresponding to the given parameter name, or null
   * if no parameter with the given name exists.
   */
  public String getParameter(String paramName){
    if(_params == null){
      return null;
    }
    return (String)_params.get(paramName);
  }

  public Map getParameters(){
    return Collections.unmodifiableMap(_params);
  }

  private static String parseBasePath (String pathInfo){
//    return StringUtils.splitStr(pathInfo,'?',0)[0];
      return pathInfo.split("\\?")[0];
  }

  private static HashMap parseParams(String pathInfo){
//    String[] baseLine = StringUtils.splitStr(pathInfo,'?',0);
      String[] baseLine = pathInfo.split("\\?");

//    String[] nameVals = StringUtils.splitStr(baseLine[1], '&', 0);
    String[] nameVals = baseLine[1].split("\\&");
    String[] nameVal;
    HashMap prmMap = new HashMap();

    for(int i = 0; i < nameVals.length; i++){
      System.out.println(nameVals[i]);
      nameVal = nameVals[i].split("=");
      if(nameVal.length != 2) continue;
      prmMap.put(nameVal[0].trim(), nameVal[1].trim());
    }
    return prmMap;
  }

  /**
   * Returns this object's protocol.
   */
  public String getProtocol(){
    return _protocol;
  }
  /**
   * Returns this object's user, null if none associated.
   */
  public String getUser(){
    return _user;
  }
  /**
   * Returns this object's password, null if none associated.
   */
  public String getPassword(){
    return _password;
  }
  /**
   * Returns this object's host.
   */
  public String getHost(){
    return _host;
  }
  /**
   * Returns this object's port, or URLObject.UNDEFINED_PORT if no port
   * was specified.
   */
  public int getPort(){
    return _port;
  }
  /**
   * Returns the path information (with a starting '/') after the host:port,
   * if any path information was specified; if not, an empty string is returned.
   */
  public String getPathInfo(){
    return _pathInfo;
  }

  /** returns the path without any parameters */
  public String getBasePath() {
    return _basePath;
  }

  public String toString(){
    return _fullURL;
  }

  public static void main ( String[] args ) {

   try {

     URLObject[] urlObjects = new URLObject[] {
       new URLObject("http://192.168.1.100"),
       new URLObject("http://192.168.1.100/"),
       new URLObject("http://192.168.1.100:80/"),
       new URLObject("http://[192.168.1.100]:80/"),
       new URLObject("http://[1080:0:0:0:8:800:200C:417A]/index.html"),
       new URLObject("http://[1080:0:0:0:8:800:200C:417A]:80/index.html"),
       new URLObject("http://[1080::::8:800:200C:417A]:80/index.html"),
       new URLObject("news:comp.lang.java"),
       new URLObject("http://ajmas:blog@myhost:80/"),
       new URLObject("http://myhost:80/abc?a=x&b=y"),
       new URLObject("http://myh ost:80/abc?a=x&b=y"),
       new URLObject("file:///Volumes/Silver-Carrier/"),
       new URLObject("file:///node::device:00000.filename.suffix;1"),
       new URLObject("http://[192.168.1.100]:80/blah#abc"),
       new URLObject("http://[192.168.1.100]:80/?abcd"),
     };

     for ( int i=0; i < urlObjects.length; i++ ) {
       System.out.println("-----------------------------------------");
       System.out.println("url ........... " + urlObjects[i]);
       System.out.println("protocol ...... " + urlObjects[i].getProtocol());
       System.out.println("host .......... " + urlObjects[i].getHost());
       System.out.println("IPv6 Address .. " + urlObjects[i]._ipv6Address);
       System.out.println("port .......... " + urlObjects[i].getPort());
       System.out.println("user .......... " + urlObjects[i].getUser());
       System.out.println("password ...... " + urlObjects[i].getPassword());
       System.out.println("path .......... " + urlObjects[i].getPathInfo());
       System.out.println("base path ..... " + urlObjects[i].getBasePath());
       System.out.println("params ........ " + urlObjects[i].getParameters());
     }

    } catch ( IOException ex ) {
      ex.printStackTrace();
    }

  }

}
