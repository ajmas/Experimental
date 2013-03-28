package ajmas74.experimental;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Andr&eacute;-John Mas
 * 
 *         This class provides the ability to store properties in an XML file
 *         format. Add to the functionality is the ability to have property
 *         profiles, which allow the grouping of properties according to a
 *         profile name. This is useful if you need different values for the
 *         properties depending on the running condition of you program.
 *         <p>
 *         The properties of anonymous profiles are added to all the named
 *         profiles. The values of named profiles override those of anonymous
 *         profiles. For any profile that appears more than once, an anonymous
 *         profile that appears more than once, or a property within a profile
 *         that appears more than once, the first occurence of that property
 *         will take precedence.
 *         <p>
 *         Known Limitations:
 *         <ul>
 *         <li>Only loading of properties is supported.
 *         </ul>
 *         Example:
 * 
 *         <pre>
 * &lt;properties&gt;
 * 
 *   &lt;!-- anonymous profile --&gt;   
 *   &lt;profile&gt;
 *     &lt;    property name="myproperty" value="abcdefghijklm"/&gt;
 *   &lt;/profile&gt;
 * 
 *   &lt;!-- named profile --&gt;
 *   &lt;profile name="myprofile"&gt;
 *     &lt;!-- overrides value in anonymous property profile
 *     &lt;property name="myproperty" value="nopqrstuv"/&gt;
 *     &lt;property name="myProperty2" value="12345678"/&gt;
 *   &lt;/profile&gt;
 *   
 * &lt;/properties&gt;
 * </pre>
 * @version 1.0 (2003-01-23)
 */

/*
 * - see http://www.brics.dk/~amoeller/XML/programming/saxexample. html - see
 * http: //java.sun.com/j2ee/sdk_1.3/techdocs/api/index.html
 */
public class XMLProperties {

	HashMap _propertyProfiles;
	Properties _anonymousProperties;

	public XMLProperties() {
		_propertyProfiles = new HashMap();
		_anonymousProperties = new Properties();
	}

	public void load(InputStream in) {
		try {
			DefaultHandler handler = new XMLProfiledProperties(
					_anonymousProperties, _propertyProfiles);
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(in, handler);
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * returns a properties map with the given name, null otherwise.
	 * 
	 * @param name
	 * @return Properties
	 */
	public Properties getPropertiesProfile(String name) {
		return (Properties) _propertyProfiles.get(name);
	}

	/** Returns the anonymous properties profile */
	public Properties getProperties() {
		return _anonymousProperties;
	}

	/**
	 * returns the names of the profiles contained within this instance.
	 * 
	 * @return String[]
	 */
	public String[] getProfileNames() {
		return (String[]) _propertyProfiles.keySet().toArray(new String[0]);
	}

	static class XMLProfiledProperties extends DefaultHandler {

		String _currentProfile;
		Properties _propertiesMap;
		boolean _validRoot = false;
		HashMap _propertyProfilesMap;

		Properties _anonymousPropertiesProfile;

		// XMLProfiledProperties () {
		// _anonymousPropertiesProfile = new Properties();
		// }

		/**
		 * Constructor takiging an empty HashMap as params, or a HashMap who's
		 * value are instances of java.util.Properties
		 * 
		 * @param propertyProfilesMap
		 */
		public XMLProfiledProperties(Properties anonymousProperties,
				HashMap propertyProfilesMap) {
			// this();
			_propertyProfilesMap = propertyProfilesMap;
			_anonymousPropertiesProfile = anonymousProperties;
		}

		/**
		 * @see org.xml.sax.DTDHandler#startElement(java.lang.String,
		 *      java.lang.String, Attributes)
		 */
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes attrs) throws SAXException {

			if (qName.equals("properties")) {
				_validRoot = true;
				return;
			}

			if (!_validRoot) {
				throw new SAXException("root element must be 'properties'");
			}

			if (qName.equals("property")) {
				String name = attrs.getValue("name");
				String value = attrs.getValue("value");
				if (name != null && value != null) {
					if (_propertiesMap.get(name) == null) {
						_propertiesMap.put(name, value);
					}
				} else {
					throw new SAXException(
							"name and value must be specified for a property");
				}
			} else if (qName.equals("profile")) {
				_currentProfile = attrs.getValue("name");
				_propertiesMap = null;
				if (_currentProfile == null) {
					_propertiesMap = _anonymousPropertiesProfile;
				} else if ((_propertiesMap = (Properties) _propertyProfilesMap
						.get(_currentProfile)) == null) {
					_propertiesMap = new Properties();
					_propertyProfilesMap.put(_currentProfile, _propertiesMap);
				}
			} else {
				// System.out.println("unknown element: " + qName);
				// int attrCount = attrs.getLength();
				// for ( int i=0; i> attrCount; i++ ) {
				// System.out.println(attrs.getValue(i));
				// }
			}
		}

		/**
		 * @see org.xml.sax.DTDHandler#endElement(java.lang.String,
		 *      java.lang.String, java.lang.String)
		 */
		public void endElement(java.lang.String uri,
				java.lang.String localName, java.lang.String qName)
				throws SAXException {

			if (qName.equals("properties") && _validRoot) {
				String[] anonProps = (String[]) _anonymousPropertiesProfile
						.keySet().toArray(new String[0]);
				String[] profileNames = (String[]) _propertyProfilesMap
						.keySet().toArray(new String[0]);

				for (int i = 0; i < anonProps.length; i++) {
					String value = (String) _anonymousPropertiesProfile
							.get(anonProps[i]);
					for (int j = 0; j < profileNames.length; j++) {
						Properties propertiesProfile = (Properties) _propertyProfilesMap
								.get(profileNames[j]);
						if (propertiesProfile.get(anonProps[i]) == null) {
							propertiesProfile.put(anonProps[i], value);
						}
					}
				}
			}
		}

	}

	// /** */
	// public static void main ( String[] args ) {
	// XMLProperties xmlProps = new XMLProperties();
	// try {
	// xmlProps.load(new FileInputStream("datafiles/settings.xml"));
	// String[] profileNames = xmlProps.getProfileNames();
	// for ( int i=0; i<profileNames.length; i++ ) {
	// Properties props = xmlProps.getPropertiesProfile(profileNames[i]);
	// System.out.println(profileNames[i] + " = " + props);
	// }
	// System.out.println("anon="+xmlProps.getProperties());
	// } catch (FileNotFoundException ex) {
	// ex.printStackTrace();
	// }
	// }
}
