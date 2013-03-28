/* Created on 8-Oct-2003 */
package ajmas74.experimental.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

/**
 * This template handles and process template contents in XSL
 * format. It first tries converting a TestCaseIF into XML,
 * if it implements XMLStringConvertableIF.
 * 
 * The XML is then used as parameter to the XSL when it is run.
 * the result is an HTTPMessageAdaptor.
 * 
 * @author Andre-John Mas
 */
public class XSLProcessor {

  Logger _log = Logger.getLogger(this.getClass());
  
	String             _name;
	String             _content;
	TransformerFactory _tFactory;
	//Transformer        _transformer;
	
	public XSLProcessor () {		
	  _tFactory = TransformerFactory.newInstance(); 
	}
	
	public String process ( String xmlDocument, String xslDocument ) throws Exception {
    ByteArrayInputStream bIn = new ByteArrayInputStream(xslDocument.getBytes("utf-8"));
    Transformer transformer = _tFactory.newTransformer(new StreamSource(bIn));
    transformer.setErrorListener(new XSLErrorListener());
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		
		bIn = new ByteArrayInputStream(xmlDocument.getBytes("utf-8"));
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		transformer.transform(new StreamSource(bIn), new StreamResult(bOut));
		return bOut.toString("utf-8");		
	}
	
	public void process ( OutputStream out, String xmlDocument, String xslDocument ) throws Exception {
    ByteArrayInputStream bIn = new ByteArrayInputStream(xslDocument.getBytes("utf-8"));
    Transformer transformer = _tFactory.newTransformer(new StreamSource(bIn));
    transformer.setErrorListener(new XSLErrorListener());
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		
		bIn = new ByteArrayInputStream(xmlDocument.getBytes("utf-8"));
		transformer.transform(new StreamSource(bIn), new StreamResult(out));		
	}

	public void process ( OutputStream out, InputStream xmlDocument, InputStream xslDocument ) throws Exception {
    //ByteArrayInputStream bIn = new ByteArrayInputStream(xslDocument.getBytes("utf-8"));
    Transformer transformer = _tFactory.newTransformer(new StreamSource(xslDocument));
    transformer.setErrorListener(new XSLErrorListener());
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		
		transformer.transform(new StreamSource(xmlDocument), new StreamResult(out));		
	}
	
	public String process ( InputStream xmlDocument, InputStream xslDocument ) throws Exception {
    //ByteArrayInputStream bIn = new ByteArrayInputStream(xslDocument.getBytes("utf-8"));
    Transformer transformer = _tFactory.newTransformer(new StreamSource(xslDocument));
    transformer.setErrorListener(new XSLErrorListener());
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		
		
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		transformer.transform(new StreamSource(xmlDocument), new StreamResult(bOut));
		return bOut.toString("utf-8");			
	}
		
	
	
	private class XSLErrorListener implements ErrorListener {

    /* (non-Javadoc)
     * @see javax.xml.transform.ErrorListener#error(javax.xml.transform.TransformerException)
     */
    public void error(TransformerException ex) throws TransformerException {
      _log.error(ex);
      System.err.println(ex);
    }

    /* (non-Javadoc)
     * @see javax.xml.transform.ErrorListener#fatalError(javax.xml.transform.TransformerException)
     */
    public void fatalError(TransformerException ex) throws TransformerException {
			_log.fatal(ex); 
			System.err.println(ex);
    }

    /* (non-Javadoc)
     * @see javax.xml.transform.ErrorListener#warning(javax.xml.transform.TransformerException)
     */
    public void warning(TransformerException ex) throws TransformerException {
			_log.warn(ex);
			System.err.println(ex);
    }	  
	}
		
	public static void main ( String[] args ) {
	  try {
	    String baseFolder = "F:\\development\\media-i\\notes\\album-xml\\";
	    InputStream xml = null;
	    InputStream xsl = null;
	    
	    //xml = new FileInputStream( baseFolder + "album2.xml" ) ;
	    //xsl = new FileInputStream( baseFolder + "album.xsl" ) ;
	    //xml = (new URL("http://www.slashdot.org/index.rss")).openStream();
	    xml = (new URL("file:///F:/development/rdf-generator/slashdot.rss.xml")).openStream();
	    	    
	    xsl = (new URL("file:///F:/development/rdf-generator/rss.xsl")).openStream();	    	 
	    XSLProcessor xslProcessor = new XSLProcessor();
	    System.out.println(xslProcessor.process(xml,xsl));
	  } catch ( Exception ex ){
	    ex.printStackTrace();
	  }
	}
}
