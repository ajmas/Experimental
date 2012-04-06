package ajmas74.experimental;

import java.util.HashMap;
import java.util.Map;

public class LongComparison {

	
	public static void test1 () {
		long l1 = 22L;
		long l2 = 22L;
		Long l3 = 22L;
		Long l4 = 22L;
		
		System.out.println(l1 + "=" + l2 + "? " +  (l1==l2));
		System.out.println(l2 + "=" + l3 + "? " +  (l2==l3));
		System.out.println(l3 + "=" + l4 + "? " +  (l3==l4));
		
		System.out.println(l3 + "equals " + l4 + "? " +  (l3.equals(l4) ));

		System.out.println(l3 + ".hash? " + l3.hashCode() );
		System.out.println(l4 + ".hash? " + l4.hashCode() );
	}
	
	
	public static void test2 () {
		long l1 = Long.MAX_VALUE;
		long l2 = Long.MAX_VALUE;
		Long l3 = Long.MAX_VALUE;
		Long l4 = Long.MAX_VALUE;
		
		System.out.println("A: " + l1 + "=" + l2 + "? " +  (l1==l2));
		System.out.println("B: " + l2 + "=" + l3 + "? " +  (l2==l3));
		System.out.println("C: " + l3 + "=" + l4 + "? " +  (l3==l4));
		System.out.println("D: " + l3 + "=" + l4 + "? " +  (((long)l3)==((long)l4)));
		
		System.out.println("E: " + l3 + "equals " + l4 + "? " +  (l3.equals(l4) ));

		System.out.println("F: " + l3 + ".hash? " + l3.hashCode() );
		System.out.println("G: " + l4 + ".hash? " + l4.hashCode() );
	}
	
	public static void main ( String[] args ) {
		test1();
		System.out.println("");
		test2();
		
		Map<Long,String> myMap = new HashMap<Long,String>();
		
		myMap.put(new Long(Long.MAX_VALUE-1), "abc");
		
		long l1 = Long.MAX_VALUE - 1 ;
		System.out.println(myMap.get(l1));
		
//		long l1 = Long.MAX_VALUE;
//		System.out.println(myMap.get(l1));
	}
}
