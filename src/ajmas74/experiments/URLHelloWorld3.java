package ajmas74.experiments;
import java.net.*;
import java.io.*;
import java.util.jar.*;
import java.util.*;

public class URLHelloWorld3 {

	URLHelloWorld3 ( String url ) {
		try {
			URL theURL = new URL("http://cvs.sophia.8x8.com/");
			if ( url != null )
				theURL = new URL(url);		
				
			//URL theURL = new URL("file:/d:/ajmas/projects/UB2x/beans/");
			URLConnection uc = theURL.openConnection();
			
			uc.setRequestProperty("USER_AGENT","User-Agent: Mozilla/3.01 (WinNT; I)");
			//uc.setRequestProperty("HTTP_ACCEPT","image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, image/png, */*");
			
/*			System.out.println("<PRE>");
			System.out.println("URL ............ : " + theURL );
			System.out.println("Server Software. : " + uc.getHeaderField("Server"));
			System.out.println("Content-length . : " + uc.getContentLength());
			System.out.println("Content-type ... : " + uc.getContentType());
			System.out.println("");
*/			
		
			if ( uc instanceof JarURLConnection ) {
				JarURLConnection juc = (JarURLConnection) uc;
				Manifest manif = juc.getManifest();

				// that is in the manifest file header
				Attributes attribs = manif.getMainAttributes();
				Set keySet = attribs.keySet();
				Object[] keySetArray = keySet.toArray();
				for (int i=0; i < keySetArray.length; i++) {
					System.out.println(i + " = " + keySetArray[i]);
				}	
			} else if ( uc instanceof HttpURLConnection ) {
				HttpURLConnection huc = (HttpURLConnection) uc;
				//System.out.println("Response-code .. : " + huc.getResponseCode());		
				//System.out.println("Response-mesg .. : " + huc.getResponseMessage());
				//System.out.println("</PRE>");
				//System.out.println("");
				
				Object content = uc.getContent();				
				if ( content instanceof java.io.InputStream ) {
					InputStream is = (InputStream) content;
					//BufferedReader ois = new BufferedReader(new InputStreamReader(is));
					BufferedInputStream ois = new BufferedInputStream(is);
					//while ( ois.ready() ) {
					byte[] buffer = new byte[4096];
					//char[] buffer = new char[2048];
					while ( true )
					{
						int count = ois.read(buffer);//,0,buffer.length-1);
						System.out.write(buffer,0,count);
						//System.out.println(ois.readLine());
					}
				} else {
					System.out.println("content : " + content ) ;
				}
			} else { // This is used when we are referencing a local file
				Object content = uc.getContent();
				if ( content instanceof java.io.InputStream ) {
					InputStream is = (InputStream) content;
					BufferedReader ois = new BufferedReader(new InputStreamReader(is));
					while ( ois.ready() ) {
						System.out.println(ois.readLine());
					}
				}
			}
/*			URL[] urlList = new URL[1];
			urlList[0] = new URL ("jar:http://127.0.0.1/beans/components.jar!/");
			URLClassLoader cl = new URLClassLoader(urlList);
			Class aClass = cl.loadClass("com.uforce.ubf.beans.state.Exit");
			System.out.println("aClass : " + aClass.getName());
*/		} catch ( Exception ex ) {
			System.out.println(ex);
		}
	}
	
	
	public static void main ( String[] args ) {
		if ( args.length > 0 )
		{
			//while ( true )
			//{
				URLHelloWorld3 uhw = new URLHelloWorld3(args[0]);
			//}
		}
		else
		{
			URLHelloWorld2 uhx = new URLHelloWorld2(null);
		}
		
	}

}