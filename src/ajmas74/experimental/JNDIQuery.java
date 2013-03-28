package ajmas74.experimental;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import java.util.*;
import javax.rmi.*;

/**
 * *
 */
public class JNDIQuery {

	/**
	 * Constructor for LDAPQuery.
	 */
	public JNDIQuery() throws Exception {
		super();

		Hashtable<String, String> env = new Hashtable<String, String>();
		// env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		// env.put(Context.PROVIDER_URL, "t3://192.168.0.55:17001/" );

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, "t3://127.0.0.1:7001/");

		// env.put(Context.SECURITY_PRINCIPAL, "system");
		// env.put(Context.SECURITY_CREDENTIALS, "12345678");
		System.out.println(env);
		// System.out.println("--A");
		// Create initial context
		DirContext ctx = new InitialDirContext(env);

		exploreNext(0, ctx, "");
	}

	private void exploreNext(int depth, DirContext ctx, String path)
			throws NamingException {
		NamingEnumeration names = ctx.list(path);

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
			JNDIQuery lq = new JNDIQuery();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
