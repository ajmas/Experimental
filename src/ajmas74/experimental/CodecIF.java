/*
 * Created on 5-Apr-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ajmas74.experimental;

/**
 * @author ajmas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface CodecIF {

	public byte[] encode ( byte[] data );
	
	public byte[] decode ( byte[] data );
	
}
