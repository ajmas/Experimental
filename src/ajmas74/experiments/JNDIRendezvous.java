/* Created on 22-Jun-2003 */
package ajmas74.experiments;

import java.util.*;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * @author <a href="mailto:ajmas@bigfoot.com">Andr&eacute;-John Mas</a>
 *
 */
public class JNDIRendezvous implements Context {

	private Hashtable _environment;
	
  /**
   * @see javax.naming.Context#addToEnvironment(java.lang.String, java.lang.Object)
   */
  public Object addToEnvironment(String propName, Object propVal)
    throws NamingException {
		return _environment.put (propName, propVal);
  }

  /**
   * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
   */
  public void bind(Name name, Object obj) throws NamingException {
    // TODO Auto-generated method stub
  }

  /**
   * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
   */
  public void bind(String name, Object obj) throws NamingException {
    // TODO Auto-generated method stub
  }

  /**
   * @see javax.naming.Context#close()
   */
  public void close() throws NamingException {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.naming.Context#composeName(javax.naming.Name, javax.naming.Name)
   */
  public Name composeName(Name name, Name prefix) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
   */
  public String composeName(String name, String prefix)
    throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#createSubcontext(javax.naming.Name)
   */
  public Context createSubcontext(Name name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#createSubcontext(java.lang.String)
   */
  public Context createSubcontext(String name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
   */
  public void destroySubcontext(Name name) throws NamingException {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.naming.Context#destroySubcontext(java.lang.String)
   */
  public void destroySubcontext(String name) throws NamingException {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.naming.Context#getEnvironment()
   */
  public Hashtable getEnvironment() throws NamingException {
    return _environment;
  }

  /**
   * @see javax.naming.Context#getNameInNamespace()
   */
  public String getNameInNamespace() throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#getNameParser(javax.naming.Name)
   */
  public NameParser getNameParser(Name name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#getNameParser(java.lang.String)
   */
  public NameParser getNameParser(String name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#list(javax.naming.Name)
   */
  public NamingEnumeration list(Name name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#list(java.lang.String)
   */
  public NamingEnumeration list(String name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#listBindings(javax.naming.Name)
   */
  public NamingEnumeration listBindings(Name name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#listBindings(java.lang.String)
   */
  public NamingEnumeration listBindings(String name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#lookup(javax.naming.Name)
   */
  public Object lookup(Name name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#lookup(java.lang.String)
   */
  public Object lookup(String name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#lookupLink(javax.naming.Name)
   */
  public Object lookupLink(Name name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#lookupLink(java.lang.String)
   */
  public Object lookupLink(String name) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
   */
  public void rebind(Name name, Object obj) throws NamingException {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
   */
  public void rebind(String name, Object obj) throws NamingException {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
   */
  public Object removeFromEnvironment(String propName) throws NamingException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
   */
  public void rename(Name oldName, Name newName) throws NamingException {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
   */
  public void rename(String oldName, String newName) throws NamingException {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.naming.Context#unbind(javax.naming.Name)
   */
  public void unbind(Name name) throws NamingException {
    // TODO Auto-generated method stub

  }

  /**
   * @see javax.naming.Context#unbind(java.lang.String)
   */
  public void unbind(String name) throws NamingException {
    // TODO Auto-generated method stub

  }

}
