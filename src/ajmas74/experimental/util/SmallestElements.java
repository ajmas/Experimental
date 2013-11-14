package ajmas74.experimental.util;

import java.util.List;

public interface SmallestElements<T extends Comparable<T>> {

	public void add(T element);
	
	public List<T> getElements() ;
}
