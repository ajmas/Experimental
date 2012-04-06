package org.ibex.crypto;

public class Base36 {
    
	public static String encode(long l) {
        StringBuffer ret = new StringBuffer();
        while (l > 0) {
            if ((l % 36) < 10) ret.append((char)(((int)'0') + (int)(l % 36)));
            else ret.append((char)(((int)'A') + (int)((l % 36) - 10)));
            l /= 36;
        }
        return ret.toString();
    }
	
}

//ref: http://www.koders.com/java/fidFF1089324C426E53009218C34E89E63FEDAB61A0.aspx?s=cdef%3Amd5

