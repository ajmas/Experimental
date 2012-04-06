package ajmas74.experimental.proxy;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerSide implements Runnable {

	private Map<String,Object> registrationMap = new HashMap<String,Object>();
	
	public ServerSide () {
		new Thread(this).start();
	}
	
	public void register ( String name, Object theClass ) {
		registrationMap.put(name, theClass);
	}
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(9999);
					
			while (true ) {
				Socket socket = serverSocket.accept();			
			
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				
				ObjectOutputStream objOut = new ObjectOutputStream(out);
				ObjectInputStream objIn = new ObjectInputStream(in);
				
				String referenceName = objIn.readUTF();
				
				String methodName = objIn.readUTF();
				Class<?>[] paramTypes = (Class<?>[]) objIn.readObject();
				Object[] params = (Object[]) objIn.readObject();
				
				System.out.println("Calling: " + methodName);
				
				PrintWriter writer = new PrintWriter(out);
				
				Object instance = registrationMap.get(referenceName);
				
				Method method = instance.getClass().getMethod(methodName, paramTypes );
				
				objOut.writeObject(  method.invoke(instance, params )  );
				
				writer.flush();			
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// -------------------------------------------------------------------
	// Main entry point
	// -------------------------------------------------------------------

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ServerSide serverSide = new ServerSide();
		serverSide.register("InterfaceX", new ClassX());
		
		serverSide.register("InterfaceY", new ClassX("Longitude"));
	}	
}
