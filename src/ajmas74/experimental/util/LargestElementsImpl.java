package ajmas74.experimental.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is designed to provided a sorted list of the largest 'n' items,
 * going from the smallest to the largest. The class sorts on the fly and there
 * is an attempt at some basic optimisation. The elements to be sorted must
 * implement the Comparable interface.
 * 
 * I may modify this to implement the List interface.
 * 
 * @author ajmas
 *
 * @param <T>
 */
public class LargestElementsImpl<T extends Comparable<T>> implements LargestElements<T> {
	int maxCount = 0;
	List<T> elements;
	
	public LargestElementsImpl(int maxCount) {
		this.maxCount = maxCount;
		this.elements = new LinkedList<T>();
	}

	public void add(T element) {
		if (elements.size() == 0 ) {
			elements.add(element);
		} else if (elements.size() < maxCount) {
			if (elements.get(0).compareTo(element) > 0) {
				elements.add(0,element);
			} else if (elements.get(elements.size()-1).compareTo(element) < 0) {
				elements.add(element);
			} else {
				for (int i=(elements.size()-1); i>0; i-- ) {
					int s1 = elements.get(i-1).compareTo(element);
				    if (s1 < 0) {
				    	elements.add(i,element);
				    	break;
				    }
				}					
			}	
			
		} else {
			if (elements.get(elements.size()-1).compareTo(element) < 0) {					
				elements.add(element);
				elements.remove(0);					
			} else if (elements.get(0).compareTo(element) < 0) {
								for (int i=(elements.size()-1); i>0; i-- ) {
					int s1 = elements.get(i-1).compareTo(element);
				    if (s1 < 0 ) {
				    	elements.add(i,element);
						elements.remove(0);
				    	return;
				    }
				}	
			} 
			// There is no 'else', because this means they are too small
		}
	}
	
	
	public List<T> getElements() {
		return this.elements;
	}
	
	
	public static void main(String[] args)  {
		
		for (int x=0; x<1000; x++) {
			LargestElements<Integer> largestIntegers = new LargestElementsImpl<Integer>(5);
			
			Integer[] integers = new Integer[] { 2, 3, 4, 1, 0, 3, 9, 3, 5, 8, 10, 20, 30, 15, 16, 100, 1, 0, 25, 60, 0, 99, 98 };
			
			for (int i=0; i<integers.length; i++) {
				integers[i] = (int)(Math.random() * 300);
			}
			
	//		integers = new Integer[] {101, 15, 186, 110, 158, 272, 287, 172, 53, 69, 253, 233, 15, 107, 295, 50, 73, 243, 106, 178, 276, 48, 201};
	
			System.out.println(Arrays.toString(integers));
			
			for (int i=0; i<integers.length; i++) {
				largestIntegers.add(integers[i]);
			}
			
			List<Integer> elements = largestIntegers.getElements();
			
			System.out.println(elements);
			
			Arrays.sort(integers);
			System.out.println(Arrays.toString(integers));
			
			int elemSize = elements.size();
			for (int i=0; i<elemSize; i++) {
				Integer v1 = elements.get(i);
				Integer v2 =  integers[integers.length-(elemSize-i)];
				if (!v1.equals(v2)) {
					System.err.println("Bad match: " + v1 + " != " + v2 );
					return;
				}
			}
		}		
	}
}
