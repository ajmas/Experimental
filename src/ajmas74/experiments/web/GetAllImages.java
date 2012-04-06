package ajmas74.experiments.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

public class GetAllImages {

	static HttpClient httpclient = new DefaultHttpClient();
	static File baseFolder = new File("/Volumes/Little Black/gwm-auto/");///Users/ajmas/Pictures/gwm-auto");
	static File indexFile = new File(baseFolder, "index");
	static int maxPage;

	protected File outputFolder;
	
	static boolean exists(String href) {
		return ((new File(baseFolder, href)).exists());
	}

	static  void writeAttribute( File file, String attrName, String attrValue ) throws IOException {
		
		Process proc = Runtime.getRuntime().exec(
				new String[] { "xattr", "-w", attrName, attrValue, file.getAbsolutePath() });
		
	}
	
	void retrieveImage(File outputFolder, String urlStr, String referrer)
			throws IOException {
		URL url = new URL(urlStr);
		InputStream in = null;
		OutputStream out = null;

		String fileName = url.getFile().substring(url.getFile().lastIndexOf("/"));
		File imageFile = new File(outputFolder, fileName);

		System.out.println("url .... " + url);
		
		try {
			if ( ! imageFile.exists() ) {		
			
				HttpResponse response = null;
				try {
					HttpGet httpget = new HttpGet(urlStr);
					httpget.addHeader("refrerer", referrer);
					httpget.addHeader(
							"User-Agent",
							"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_6; en-gb) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27");
					response = httpclient.execute(httpget);
					
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();

						if (entity != null) {
							in = entity.getContent();
							
							out = new FileOutputStream(imageFile);
							
							byte[] buffer = new byte[256];
							int len = -1;
							while ((len = in.read(buffer)) > -1) {
								out.write(buffer, 0, len);
							}							
						}
					}
					else {
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							in = entity.getContent();
							
							byte[] buffer = new byte[256];
							int len = -1;
							while ((len = in.read(buffer)) > -1) {
								// ignored
							}
						}
					}
					
					writeAttribute ( imageFile, "com.apple.metadata:kMDItemWhereFroms","( \"" + urlStr + "\" )" );
				} finally {
					if (out != null) {
						out.flush();
						out.close();
					}
					if (in != null) {
						in.close();
					}
				}
			
			}
			else {
				System.out.println("skipping " + url);
			}
		} catch ( IOException ex ) {
			ex.printStackTrace();
			new File(outputFolder,"ERROR").mkdir();
		}
	}

	public static Document parseDocument(String urlStr, String referrer) throws IOException {

		HttpGet httpget = new HttpGet(urlStr);
		if ( referrer != null ) {
			httpget.addHeader("refrerer", referrer);
		}
		httpget.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_6; en-gb) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27");
		HttpResponse response = httpclient.execute(httpget);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream in = null;
				try {
					in = entity.getContent();

					final Tidy tidy = new Tidy();
					tidy.setQuiet(true);
					tidy.setShowWarnings(false);
					tidy.setXHTML(true);
					tidy.setForceOutput(true);

					return tidy.parseDOM(in, null);
				} finally {
					if (in != null) {
						in.close();
					}
				}

			} else {
				throw new IOException("No response body for " + urlStr);
			}

		} else {
			
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream in = null;
				try {
					in = entity.getContent();
					
					byte[] buffer = new byte[256];
					int len = -1;
					while ((len = in.read(buffer)) > -1) {
						// ignored					
					}
				} finally {
					if (in != null) {
						in.close();
					}
				}	
			}
			
			throw new IOException("Received status code "
					+ response.getStatusLine() + " for " + urlStr);
		}

		// URL url = new URL(urlStr);
		// InputStream in = null;
		// try {
		// URLConnection connection = url.openConnection();
		// in = connection.getInputStream();
		//
		// final Tidy tidy = new Tidy();
		// tidy.setQuiet(true);
		// tidy.setShowWarnings(false);
		// tidy.setForceOutput(true);
		//
		// return tidy.parseDOM(in, null);
		// } finally {
		// if ( in != null ) {
		// in.close();
		// }
		// }

	}

	void handleImageVenue ( String url ) {
		try {
			Document doc = parseDocument( url, null );
			
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//img[@id='thepic']");
			
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			
			NodeList nodes = (NodeList) result;

			
			for (int i = 0; i < nodes.getLength(); i++) {
				String src = nodes.item(i).getAttributes()
						.getNamedItem("src").getNodeValue();
				
				if ( src.startsWith("http://") ) {
					// INFO do nothing
				}
				else if ( src.startsWith("/") ) {
					URL pageUrl = new URL (url);					
					src = "http://" + pageUrl.getHost() + "" + src;

				}
				else {
					URL pageUrl = new URL (url);					
					src = "http://" + pageUrl.getHost() + "/" + src;					
					
					//System.out.println("WARN - not supported: " + src);
					//continue;
				}
	
				//System.out.println("INFO - retrieving: " + src);
				
				retrieveImage(this.outputFolder, src, url);
				
				try {
					Thread.sleep(100);
				}
				catch ( InterruptedException ex ) {
					
				}
					
				
			}

			
		}
		catch ( Exception ex) {
			ex.printStackTrace();
		}
	}
	
	void handleImageBam( String url, String referrer ) {
		System.out.println("IMAGEBAM ---- " + url);
		
		StringReader strReader = null;
		
		try {
			Document doc = parseDocument( url, referrer );
			
//			System.out.println(convertXMLFileToString(doc));
			
			final Tidy tidy = new Tidy();
			tidy.setQuiet(true);
			tidy.setShowWarnings(false);
			tidy.setForceOutput(true);
			
			String str = convertXMLFileToString(doc);
			
			strReader = new StringReader(str);
			
			doc = tidy.parseDOM(strReader, null); 
						
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//div[@id='imageContainer']//img");
			
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
//			
			NodeList nodes = (NodeList) result;

			for (int i = 0; i < nodes.getLength(); i++) {
				String src = nodes.item(i).getAttributes()
						.getNamedItem("src").getNodeValue();
				
				if ( src.startsWith("http://") ) {
					// INFO do nothing
				}
				else if ( src.startsWith("/") ) {
					URL pageUrl = new URL (url);					
					src = "http://" + pageUrl.getHost() + "" + src;

				}
				else {
					URL pageUrl = new URL (url);					
					src = "http://" + pageUrl.getHost() + "/" + src;					
					
					//System.out.println("WARN - not supported: " + src);
					//continue;
				}
	
				//System.out.println("INFO - retrieving: " + src);
				
				retrieveImage(this.outputFolder, src, url);
				
				try {
					Thread.sleep(100);
				}
				catch ( InterruptedException ex ) {
					
				}
					
				
			}

			
		}
		catch ( Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if ( strReader != null ) {
				strReader.close();
			}
		}
	}
	

	public static String convertXMLFileToString(Document doc)
    {
      try{
        //DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        //InputStream inputStream = new FileInputStream(new File(fileName));
        //org.w3c.dom.Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
        StringWriter stw = new StringWriter();
        Transformer serializer = TransformerFactory.newInstance().newTransformer();
        serializer.transform(new DOMSource(doc), new StreamResult(stw));
        return stw.toString();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
        return null;
    }
	
	public void retrieveContent () {
		

		//System.out.println("---" + System.getProperty("testvar"));
		
		System.out.println("** Starting");

		//File outputFolder = new File ("/Users/ajmas/Desktop/o/")
		String baseUrl = "http://www.extremefitness.com/";
		String basePath = "forum/fitness-babes/61457-kortney-olson-ko.html";
		
		
		for ( int index=1; index<=2; index++ ) {
			//String indexString = "";
			String path = basePath;
			if ( index > 1 ) {
				//indexString = index + "";
				String[] parts = basePath.split("\\.");
				path = parts[0] + "-" + index + "." + parts[1];
			}
			
			try {
				
				URL url = new URL (baseUrl + path);
				
				outputFolder = new File ("/Users/ajmas/Desktop/Kortney Olson - KO");
				//outputFolder = new File ( outputFolder, url.getHost() );
				if ( !outputFolder.exists() ) {
					outputFolder.mkdirs();
				}
				
				System.out.println("Page: " + url );
				
				Document doc = parseDocument( baseUrl + path, null );
				XPathFactory factory = XPathFactory.newInstance();
				XPath xpath = factory.newXPath();
				XPathExpression expr = xpath.compile("//img");
	
				Object result = expr.evaluate(doc, XPathConstants.NODESET);
	
				NodeList nodes = (NodeList) result;
	
				System.out.println(nodes.getLength());
				for (int i = 0; i < nodes.getLength(); i++) {
					String src = nodes.item(i).getAttributes()
							.getNamedItem("src").getNodeValue();
	
					if ( src.startsWith("http://") ) {
						// INFO do nothing
					}
					else if ( src.startsWith("/") ) {
						src = baseUrl + src;
					}
					else {
						//src = baseUrl + path + src;
						System.out.println("WARN - not supported: " + src);
						continue;
					}
					
					System.out.println("INFO - retrieving " + src);
					
					retrieveImage(outputFolder, src, baseUrl + path );
	
					try {
						Thread.sleep(100);
						Thread.yield();
					}
					catch ( InterruptedException ex ) {
						
					}
				}
				
				expr = xpath.compile("//a");
	
				result = expr.evaluate(doc, XPathConstants.NODESET);
	
				nodes = (NodeList) result;
				
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i).getAttributes().getNamedItem("href");
					if ( node == null ) {
						continue;
					}
					
					String href = node.getNodeValue();
					
					if ( !href.startsWith("http://") ) {
						
					}
					else if ( href.indexOf(".imagevenue.com") > -1 ) {
						System.out.println(":::: IMAGEVENUE");
						handleImageVenue( href );
					}
					else if ( href.indexOf(".imagebam.com") > -1 ) {
						System.out.println(":::: IMAGEBAM");
						handleImageBam( href, baseUrl + path );
					}
					else {
						URL urlx = new URL(href);
						System.out.println(":::: IGNORED -  " + urlx.getHost() );
					}
				}
				
				
	
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {
				Thread.sleep(1000);
			}
			catch ( InterruptedException ex ) {
				
			}
			
		}
		System.out.println("** Finished");
	}
	
	public static void main(String[] args) {
		(new GetAllImages()).retrieveContent();
	}

}
