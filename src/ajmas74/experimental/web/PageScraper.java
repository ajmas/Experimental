package ajmas74.experimental.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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

public class PageScraper {

	static HttpClient httpclient = new DefaultHttpClient();
	static File baseFolder = new File("/Volumes/Little Black/images/");
	static File indexFile = new File(baseFolder, "index");
	static int maxPage;

	static void writeMeta(File outputFolder, String fileName,
			Properties properties) throws IOException {
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

	// static void markVisited ( String href ) {
	//
	// }
	//

	static boolean exists(String href) {
		return ((new File(baseFolder, href)).exists());
	}

	static void retrieveImage(File outputFolder, String urlStr)
			throws IOException {
		URL url = new URL(urlStr);
		InputStream in = null;
		OutputStream out = null;

		String fileName = url.getFile().substring(url.getFile().lastIndexOf("/"));
		File imageFile = new File(outputFolder, fileName);

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
	}

//	public static void handleImageDetailsPage(String basePath, String url)
//			throws IOException {
//
//		String name = null;
//		String tags = null;
//		String originalFileName = null;
//		String imageUrl = null;
//
//		Properties properties = new Properties();
//
//		properties.setProperty("source-page", url);
//
//		try {
//			Document doc = parseDocument(url);
//			XPathFactory factory = XPathFactory.newInstance();
//			XPath xpath = factory.newXPath();
//
//			XPathExpression expr = xpath
//					.compile("//span[@id='identitytext']/a/b/text()");
//			Object result = expr.evaluate(doc, XPathConstants.NODESET);
//			NodeList nodes = (NodeList) result;
//			for (int i = 0; i < nodes.getLength(); i++) {
//				name = nodes.item(i).getNodeValue();
//				properties.setProperty("name", name);
//			}
//
//			expr = xpath.compile("//span[@id='tagtext']/b/text()");
//			result = expr.evaluate(doc, XPathConstants.NODESET);
//			nodes = (NodeList) result;
//			for (int i = 0; i < nodes.getLength(); i++) {
//				tags = nodes.item(i).getNodeValue();
//				properties.setProperty("tags", tags);
//			}
//
//			expr = xpath.compile("//span[@id='filename']/text()");
//			result = expr.evaluate(doc, XPathConstants.NODESET);
//			nodes = (NodeList) result;
//			for (int i = 0; i < nodes.getLength(); i++) {
//				originalFileName = nodes.item(i).getNodeValue();
//				properties.setProperty("originalFileName", originalFileName);
//			}
//
//			expr = xpath.compile("//img[contains(@src,'/images/')]/@src");
//			result = expr.evaluate(doc, XPathConstants.NODESET);
//			nodes = (NodeList) result;
//			for (int i = 0; i < nodes.getLength(); i++) {
//				imageUrl = nodes.item(i).getNodeValue();
//				properties.setProperty("source", basePath + imageUrl);
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//		System.out.println("page =" + url);
//		System.out.println("   name ........ " + name);
//		System.out.println("   tags ........ " + tags);
//		System.out.println("   file name ... " + originalFileName);
//		System.out.println("   image ....... " + imageUrl);
//
//		if (imageUrl != null) {
//			String path = url.substring(basePath.length());
//
//			File outputFolder = new File(baseFolder, path);
//			outputFolder.mkdirs();
//
//			retrieveImage(outputFolder, basePath + imageUrl);
//
//			URL fileUrl = new URL(basePath + imageUrl);
//			String fileName = fileUrl.getFile().substring(
//					fileUrl.getFile().lastIndexOf("/"));
//
//			writeMeta(outputFolder, fileName, properties);
//		}
//
//	}


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

	}

	public static void main(String[] args) {

		System.out.println("** Starting");

		String basePath = "http://claus.collected.info/ajax/showfeeds/claus/400//c";
		String startPage = basePath + "";

		int maxDownloads = 10000;
		int downloadCount = 0;
		
		try {
			//for (int pageNum = 977; pageNum < 1185; pageNum++) {

				Document doc = parseDocument(startPage);
				XPathFactory factory = XPathFactory.newInstance();
				XPath xpath = factory.newXPath();
				XPathExpression expr = xpath.compile("//img");

				Object result = expr.evaluate(doc, XPathConstants.NODESET);

				NodeList nodes = (NodeList) result;

				System.out.println(nodes.getLength());
				for (int i = 0; i < nodes.getLength(); i++) {
					
					Properties properties = new Properties();
					
					String imageUrl = nodes.item(i).getAttributes()
							.getNamedItem("src").getNodeValue();
					
					System.out.println("A src: " + imageUrl);
					if (imageUrl != null) {
						String url = imageUrl;
						if ( imageUrl.startsWith("//") ) {
							url = "http:" + imageUrl;
						}
						else if ( imageUrl.startsWith("/") || imageUrl.indexOf("://") < 0 ) {
							url = basePath + "/" + imageUrl;
						}
						System.out.println("B src: " + imageUrl);
						
						properties.setProperty("source", startPage);
						properties.setProperty("orginalUrl", url);
						
//						String path = url.substring(basePath.length());
//
//						File outputFolder = new File(baseFolder, path);
//						outputFolder.mkdirs();

						retrieveImage(baseFolder, url);

						URL fileUrl = new URL(url);
						String fileName = fileUrl.getFile().substring(
								fileUrl.getFile().lastIndexOf("/"));

						writeMeta(baseFolder, fileName, properties);
					}
					
					
//					String href = nodes.item(i).getAttributes()
//							.getNamedItem("href").getNodeValue();
//					if (href.matches("/[0-9]+/")) {
//						System.out.println(href);
//						if (!exists(href)) {
//							if ( downloadCount >= maxDownloads ) {
//								System.out.println("***** Reached maximum downloads for session");
//								return;
//							}
//							downloadCount++;
//							handleImageDetailsPage(basePath, basePath + href);
//							try {
//								Thread.sleep(2000);
//							} catch (InterruptedException ex) {
//								// ignored
//							}
//						}
//
//					} else if (href.startsWith("/images/")) {
//						System.out.println("next: " + href);
//					}
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					// ignored
				}
//			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}


		System.out.println("** Finished");
	}

}
