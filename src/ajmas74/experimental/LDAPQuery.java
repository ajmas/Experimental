package ajmas74.experimental;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import java.util.*;

/** *
 */
public class LDAPQuery {

	/**
	 * Constructor for LDAPQuery.
	 */
	public LDAPQuery() throws Exception {
		super();
    
    Hashtable env = new Hashtable();
//     env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
//     env.put(Context.PROVIDER_URL, "ldap://localhost:389/" );

    env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    env.put(Context.PROVIDER_URL, "t3://bic:17001/" );
    
    // Create initial context
    DirContext ctx = new InitialDirContext(env);

    //String query = "cn=fred*";
    String query = "cn=*";
    NamingEnumeration answer = ctx.search( "ou=people, dc=myco", query,  null);

    while ( answer.hasMoreElements() ) {
      System.out.println(answer.nextElement());
    }
        
	}

	public static void main(String[] args) {
    try {
      LDAPQuery lq = new LDAPQuery();
    } catch ( Exception ex ) {
      ex.printStackTrace();
    }
	}
}
