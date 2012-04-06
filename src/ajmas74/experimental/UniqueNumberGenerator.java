package ajmas74.experimental;

import java.util.HashSet;
import java.util.Set;

public class UniqueNumberGenerator {

	private Set<Integer> generatedNumbers = new HashSet<Integer>();
	
	private int maxValue = 200;
	private int clashes = 0;
	private long timeTaken = 0;
	private int mostClashes = 0;
	
	public UniqueNumberGenerator ( int maxValue ) {
		this.maxValue = maxValue;
	}
	
	public int generateUniqueNumber () {
		long t = System.currentTimeMillis();
		int clash = 0;
		// INFO handle case when we have used up all possible permutations in range
		if ( generatedNumbers.size() == maxValue) {
			return -1;
		}
		
		int value = (int)(Math.random() * maxValue);
		while ( generatedNumbers.contains(value) ) {
			clash++;
			clashes++;
			value = (int)(Math.random() * maxValue);
		}
		generatedNumbers.add(value);
		
		timeTaken = System.currentTimeMillis() - t;
		
		if ( clash > mostClashes ) {
			mostClashes = clash;
		}
		return value;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		UniqueNumberGenerator generator = new UniqueNumberGenerator(500);
		for ( int i=0; i<100; i++ ) {
			System.out.println(generator.generateUniqueNumber() + "  --- t: " + generator.timeTaken);
		}
		System.out.println("clashes: " + generator.clashes);
		System.out.println("most: " + generator.mostClashes);
	}

}
