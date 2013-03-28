package ajmas74.experimental.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import ajmas74.experimental.ChecksumFile;
import ajmas74.experimental.ChecksumFile2;
import ajmas74.experimental.metadata.MetadataExtractionService;

public class GWMSiteScraper {

	static HttpClient httpclient = new DefaultHttpClient();
	static File baseFolder = new File("/Volumes/Little Black/gwm-auto/");///Users/ajmas/Pictures/gwm-auto");
	static File indexFile = new File(baseFolder, "index");
	static int maxPage;
	static MetadataExtractionService metadataExtractionService = new MetadataExtractionService();
	
	static void writeMeta(File outputFolder, String fileName,
			Map<String,Object> metadataMap) throws IOException {
		
		Properties properties = new Properties();
		properties.putAll(metadataMap);
		
		OutputStream out = null;
		try {
			out = new FileOutputStream(
					new File(outputFolder, fileName + ".xml"));

			properties.storeToXML(out, null, "UTF-8");
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}

	}

	static  void writeAttribute( File file, String attrName, String attrValue ) throws IOException {
		
		Process proc = Runtime.getRuntime().exec(
				new String[] { "xattr", "-w", attrName, attrValue, file.getAbsolutePath() });
		
	}
	
	// static void markVisited ( String href ) {
	//
	// }
	//

	static boolean exists(String href) {
		return ((new File(baseFolder, href)).exists());
	}

	static File retrieveImage(File outputFolder, String urlStr)
			throws IOException {
		URL url = new URL(urlStr);
		InputStream in = null;
		OutputStream out = null;

		String fileName = url.getFile().substring(url.getFile().lastIndexOf("/"));
		File imageFile = new File(outputFolder, fileName);

		System.out.println("url...." + url);
		
		try {
			if ( ! imageFile.exists() ) {		
			
				try {
					URLConnection connection = url.openConnection();
					in = connection.getInputStream();
		
		
					out = new FileOutputStream(imageFile);
		
					byte[] buffer = new byte[256];
					int len = -1;
					while ((len = in.read(buffer)) > -1) {
						out.write(buffer, 0, len);
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
		} catch ( IOException ex ) {
			ex.printStackTrace();
			new File(outputFolder,"ERROR").mkdir();
		}
		
		return imageFile;
	}

	public static void handleImageDetailsPage(String basePath, String url)
			throws IOException {

		String name = null;
		String tags = null;
		String originalFileName = null;
		String imageUrl = null;

		Map<String,Object> properties = new HashMap<String, Object>();

		properties.put("source-page", url);

		try {
			Document doc = parseDocument(url);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();

			XPathExpression expr = xpath
					.compile("//span[@id='identitytext']/a/b/text()");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				name = nodes.item(i).getNodeValue();
				properties.put("name", name);
			}

			expr = xpath.compile("//span[@id='tagtext']/b/text()");
			result = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				tags = nodes.item(i).getNodeValue();
				properties.put("tags", tags);
			}

			expr = xpath.compile("//span[@id='filename']/text()");
			result = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				originalFileName = nodes.item(i).getNodeValue();
				properties.put("originalFileName", originalFileName);
			}

			expr = xpath.compile("//img[starts-with(@src,'/images/')]/@src");
			result = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				imageUrl = nodes.item(i).getNodeValue();
				properties.put("source", basePath + imageUrl);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("page =" + url);
		System.out.println("   name ........ " + name);
		System.out.println("   tags ........ " + tags);
		System.out.println("   file name ... " + originalFileName);
		System.out.println("   image ....... " + imageUrl);

		if (imageUrl != null) {
			String path = url.substring(basePath.length());

			File outputFolder = new File(baseFolder, path);
			outputFolder.mkdirs();

			File file = retrieveImage(outputFolder, basePath + imageUrl);

			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {

			}
			
			metadataExtractionService.extractMetaData(file, properties);

			URL fileUrl = new URL(basePath + imageUrl);
			String fileName = fileUrl.getFile().substring(
					fileUrl.getFile().lastIndexOf("/"));

			writeMeta(outputFolder, fileName, properties);
		}

	}

	public static void handleIndexPage(String basePath, String url, int idx)
			throws IOException {
		try {
			Document doc = parseDocument(url);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//a");

			Object result = expr.evaluate(doc, XPathConstants.NODESET);

			NodeList nodes = (NodeList) result;

			System.out.println(nodes.getLength());
			for (int i = 0; i < nodes.getLength(); i++) {
				String href = nodes.item(i).getAttributes()
						.getNamedItem("href").getNodeValue();
				if (href.matches("/[0-9]+/")) {
					System.out.println(href);
					if (!exists(href)) {
						handleImageDetailsPage(basePath, basePath + href);
					}
				} else if (href.startsWith("/images/")) {
					// String index
					// if ( Integer.parseInt(href.las))
					// System.out.println("next: " + href);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Document parseDocument(String urlStr) throws IOException {

		HttpGet httpget = new HttpGet(urlStr);
		httpget.setHeader(
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

	public static void main(String[] args) {

		//System.out.println("---" + System.getProperty("testvar"));
		
		System.out.println("** Starting");

		String basePath = "http://myhost";
		String startPage = basePath + "/images/";

		int maxDownloads = 10000;
		int downloadCount = 0;
		
		try {
			for (int pageNum = 6; pageNum < 400; pageNum++) {

				Document doc = parseDocument(startPage + pageNum);
				XPathFactory factory = XPathFactory.newInstance();
				XPath xpath = factory.newXPath();
				XPathExpression expr = xpath.compile("//a");

				Object result = expr.evaluate(doc, XPathConstants.NODESET);

				NodeList nodes = (NodeList) result;

				System.out.println(nodes.getLength());
				for (int i = 0; i < nodes.getLength(); i++) {
					String href = nodes.item(i).getAttributes()
							.getNamedItem("href").getNodeValue();
					if (href.matches("/[0-9]+/")) {
						System.out.println(href);
						if (!exists(href)) {
							if ( downloadCount >= maxDownloads ) {
								System.out.println("***** Reached maximum downloads for session");
								return;
							}
							downloadCount++;
							handleImageDetailsPage(basePath, basePath + href);
							try {
								Thread.sleep(2000);
							} catch (InterruptedException ex) {
								// ignored
							}
						}

					} else if (href.startsWith("/images/")) {
						System.out.println("next: " + href);
					}
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					// ignored
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}


		System.out.println("** Finished");
	}

}

//ref: http://www.sno.phy.queensu.ca/~phil/exiftool/


