/*
 * Created on 07-Oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ajmas74.experimental;

/**
 * @author ajmas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SignedBytetoUnsignedByte {

	public static void main(String[] args) {
		byte b = (byte) 254;
		System.out.println(b);
		int i = b & 0xFF;
		System.out.println(i);
	}
}
