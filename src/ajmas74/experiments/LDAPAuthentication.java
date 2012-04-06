package ajmas74.experiments;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class LDAPAuthentication {

    /**
     * 
     * Constructor for LDAPQuery.
     * 
     */

    public LDAPAuthentication( String username, String password ) throws Exception {

        super();

        Hashtable<String,String> env = new Hashtable<String,String>();

        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389/" );

        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        
//        env.put(Context.INITIAL_CONTEXT_FACTORY,
//                "weblogic.jndi.WLInitialContextFactory");
//
//        env.put(Context.PROVIDER_URL, "t3://bic:17001/");

        // Create initial context
        DirContext ctx = new InitialDirContext(env);

        // String query = "cn=fred*";

        String query = "cn=*";

        NamingEnumeration answer = ctx
                .search("ou=people,dc=ajmas,dc=dyndns,dc=org", query, null);

        while (answer.hasMoreElements()) {
            System.out.println(answer.nextElement());
        }

    }

    public static void main(String[] args) {

        try {
            LDAPAuthentication lq = new LDAPAuthentication("uid=mmas,ou=people,dc=ajmas,dc=dyndns,dc=org","future");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
