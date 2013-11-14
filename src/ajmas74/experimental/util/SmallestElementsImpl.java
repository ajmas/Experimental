package ajmas74.experimental.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class is designed to provided a sorted list of the smallest 'n' items,
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
public class SmallestElementsImpl<T extends Comparable<T>> implements SmallestElements<T> {
	int maxCount = 0;
	List<T> elements;
	int blockSize = 50;

	public SmallestElementsImpl(int maxCount) {
		this.maxCount = maxCount;
		this.elements = new ArrayList<T>(maxCount+1);
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	
	public void add(T element) {
		
		int size = elements.size();
		
		if (size == 0 ) {
			elements.add(element);
		} else if (size < maxCount) {
			if (elements.get(0).compareTo(element) > 0) {
				elements.add(0,element);
			} else if (elements.get(size-1).compareTo(element) <= 0) {
				elements.add(element);
			} else {			
				for (int i=1; i<size; i++) {
					int s1 = elements.get(i).compareTo(element);
				    if (s1 >= 0) {
				    	elements.add(i,element);
				    	break;
				    }
				}
			}				
		} else {
			size = maxCount;
			
			if (elements.get(0).compareTo(element) >= 0) {		
				elements.add(0,element);
				elements.remove((size+1)-1);	
				
			} else if (elements.get(size-1).compareTo(element) > 0) {
				
				int start = -1;
				int end = elements.size();
				int offset = blockSize;
				
				// Establish the block to place the value in
				
				for (; offset<elements.size(); offset+=blockSize) {
					if (elements.get(offset).compareTo(element) > 0) {
						end = offset+1;
						break;
					}
				}
				
				start = offset-blockSize;
				
				// Work out where we should be placing the value and then place it, removing excess
				
				for (int i=start; i<end; i++) {
					int s1 = elements.get(i).compareTo(element);
				    if (s1 >= 0) {
				    	elements.add(i,element);
						elements.remove((size+1)-1);		
						return;
				    }
				}
				
			} 
			// There is no 'else', because this means they are too large
		}
	}
	
	public List<T> getElements() {
		return this.elements;
	}
	
	
	public static void main(String[] args)  {
		
		for (int x=0; x<1000; x++) {
			SmallestElements<Integer> smallestIntegers = new SmallestElementsImpl<Integer>(100);
			
//			Integer[] integers = new Integer[] { 2, 3, 4, 1, 0, 3, 9, 3, 5, 8, 10, 20, 30, 15, 16, 100, 1, 0, 25, 60, 0, 99, 98 };
			
			Integer[] integers = new Integer[200000];
			
			
			for (int i=0; i<integers.length; i++) {
				integers[i] = i;				
			}
			
			for (int i=0; i<integers.length; i++) {
				int idxA = (int)(Math.random() * integers.length);
				int idxB = (int)(Math.random() * integers.length);
				
				Integer tmp = integers[idxB];
				integers[idxB] = integers[idxA];
				integers[idxA] = tmp;
			}
			
//			for (int i=0; i<integers.length; i++) {
//				integers[i] = (int)(Math.random() * 3000000);
//			}
			
//			integers = new Integer[] {101, 15, 186, 110, 158, 272, 287, 172, 53, 69, 253, 233, 15, 107, 295, 50, 73, 243, 106, 178, 276, 48, 201};
	
//			System.out.println(Arrays.toString(integers));
			
			long time = System.currentTimeMillis();
			
			for (int i=0; i<integers.length; i++) {
				smallestIntegers.add(integers[i]);
			}
			
			System.out.print ((System.currentTimeMillis() - time) + " ms");

			
			System.out.print(" vs ");

			PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>();

			time = System.currentTimeMillis();
			for (int i=0; i<integers.length; i++) {
				priorityQueue.add(integers[i]);
			}
			System.out.println((System.currentTimeMillis() - time) + " ms");
			
			
			///////
			
			List<Integer> elements = smallestIntegers.getElements();
			
			
			System.out.println(elements.size());
//			System.out.println(elements);

//			System.out.println(Arrays.toString(integers));

			Arrays.sort(integers);
//			System.out.println(Arrays.toString(integers));
			
			int elemSize = elements.size();
			for (int i=0; i<elemSize; i++) {
				Integer v1 = elements.get(i);
				Integer v2 =  integers[i];
				if (!v1.equals(v2)) {
					System.err.println("");
					System.err.println("Bad match: " + v1 + " != " + v2 );
					return;
				}
			}
		}	
		
			

	}
}
