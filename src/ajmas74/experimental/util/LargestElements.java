package ajmas74.experimental.util;

import java.util.List;

public interface LargestElements<T extends Comparable<T>> {

	public void add(T element);
	
	public List<T> getElements() ;
}
