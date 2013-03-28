package ajmas74.experimental;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import java.util.*;
import javax.rmi.*;
import javax.sql.*;
import java.sql.*;

/**
 * *
 */
public class JNDITomcat {

	/**
	 * Constructor for LDAPQuery.
	 */
	public JNDITomcat() throws Exception {
		super();

		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.naming.java.javaURLContextFactory");
		env.put(Context.PROVIDER_URL, "jndi://nadetrou2:8080/");

		// Obtain our environment naming context

		Context initCtx = new InitialContext(env);

		// Context envCtx = (Context) initCtx.lookup("comp/env");

		// Look up our data source

		// DataSource datasource = (DataSource)
		// initialContext.lookup((String)settings.get(CONNECTION_POOL_NAME));

		DataSource ds = (DataSource) initCtx.lookup("/jdbc/vrdb");

		// Allocate and use a connection from the pool

		Connection conn = ds.getConnection();

		System.out.println(conn);
		// ... use this connection to access the database ...

		conn.close();

		// Hashtable env = new Hashtable();
		// //env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		// //env.put(Context.PROVIDER_URL, "t3://192.168.0.55:17001/" );
		//
		// env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		// env.put(Context.PROVIDER_URL, "3://127.0.0.1:7001/" );
		//
		// // env.put(Context.SECURITY_PRINCIPAL, "system");
		// // env.put(Context.SECURITY_CREDENTIALS, "12345678");
		// System.out.println(env);
		// //System.out.println("--A");
		// // Create initial context
		// DirContext ctx = new InitialDirContext(env);

		// exploreNext(0, envCtx, "");
	}

	void exploreNext(int depth, DirContext ctx, String path)
			throws NamingException {
		NamingEnumeration<NameClassPair> names = ctx.list(path);

		while (names.hasMore()) {
			Object obj = names.next();
			NameClassPair ncp = (NameClassPair) obj;
			if (ncp.getClassName().equals(
					"weblogic.jndi.internal.ServerNamingNode")) {
				System.out.println(createBlanks(depth) + ncp.getName());
				exploreNext(depth + 1, ctx, path + "/" + ncp.getName());
			} else {
				System.out.print(createBlanks(depth) + "[" + ncp.getName());
				System.out.print("] - ");
				try {
					// System.out.println(ctx.lookup(path+"/"+ncp.getName()));
					System.out.println(ncp.getClassName());
				} catch (Exception ex) {
					System.out.println("");
				}
			}
		}
	}

	private String createBlanks(int count) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < count; i++) {
			strBuf.append("   ");
		}
		return strBuf.toString();
	}

	public static void main(String[] args) {
		try {
			new JNDITomcat();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
