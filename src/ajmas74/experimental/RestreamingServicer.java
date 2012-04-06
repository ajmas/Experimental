package ajmas74.experimental;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

public class RestreamingServicer implements Runnable {

	static final int PORT = 30000;

	public void run() {
		ServerSocket serverSocket = null;

		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		try {

			serverSocket = new ServerSocket(PORT);

			while ((socket = serverSocket.accept()) != null) {

				in = socket.getInputStream();

				int len = -1;
				byte[] buffer = new byte[2056];

				while ((len = in.read(buffer)) > -1) {
					System.out.println("read: " + len);
					System.out.println(new String(buffer,Charset.forName("ISO-8859-1")));
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void runUDP() throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(PORT);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			
			System.out.println("len: " + receivePacket.getLength());
			
			
//			String sentence = new String(receivePacket.getData());
//			System.out.println("RECEIVED: " + sentence);
//			InetAddress IPAddress = receivePacket.getAddress();
//			int port = receivePacket.getPort();
//			String capitalizedSentence = sentence.toUpperCase();
//			sendData = capitalizedSentence.getBytes();
//			DatagramPacket sendPacket = new DatagramPacket(sendData,
//					sendData.length, IPAddress, port);
//			serverSocket.send(sendPacket);
		}
	}

	public static void main(String[] args) {
		RestreamingServicer restreamer = new RestreamingServicer();
		restreamer.run();
//		try {
//			restreamer.runUDP();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
