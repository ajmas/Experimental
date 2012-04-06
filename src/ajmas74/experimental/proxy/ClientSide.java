package ajmas74.experimental.proxy;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ClientSide {

	static Object bind(String referenceName, Class<?> theClass) {
		InvocationHandler handler = new MyInvocationHandler(referenceName);

		return Proxy.newProxyInstance(theClass.getClassLoader(),
				new Class[] { theClass }, handler);

	}
	
	private static class MyInvocationHandler implements InvocationHandler {

		String referenceName;
		
		MyInvocationHandler ( String referenceName ) {
			this.referenceName = referenceName;
		}
		
		public Object invoke(Object object, Method method, Object[] params)
				throws Throwable {

			Object result = null;
			
			Socket socket = new Socket("localhost",9999);
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			ObjectInputStream objIn = new ObjectInputStream(in);
			
			objOut.writeUTF(this.referenceName);
			objOut.writeUTF(method.getName());
			Class<?>[] paramTypes = method.getParameterTypes();			
			objOut.writeObject(paramTypes);			
			objOut.writeObject(params);
			objOut.flush();	
			
			result = objIn.readObject();
						
			socket.close();
			
			return result;
		}

	}

	// -------------------------------------------------------------------
	// Main entry point
	// -------------------------------------------------------------------
	
	/**
	 * Main entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Object myObj = new Object();
		
		System.out.println ( "is myObj's class a proxy class: " + Proxy.isProxyClass(myObj.getClass()));
		System.out.println ( " ------ " );
		InterfaceX ifx = (InterfaceX) ClientSide.bind("InterfaceX", InterfaceX.class );
		
		ifx.setValue("Your lucky day");
		System.out.println( "result: " + ifx.getValue() );
		
		InterfaceX ify = (InterfaceX) ClientSide.bind("InterfaceY", InterfaceX.class );
				
		System.out.println( "result: " + ify.getValue() );
		
		System.out.println ( " ------ " );
		
		System.out.println ( "is ify's class a proxy class: " + Proxy.isProxyClass(ify.getClass()));
	}

}
