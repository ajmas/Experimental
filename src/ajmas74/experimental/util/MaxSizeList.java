package ajmas74.experimental.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MaxSizeList<E> implements List<E> {

	public static enum AddBehaviour {
		IGNORE, ADD_HEAD, ADD_TAIL
	};

	List<E> elements;
	int maxSize;
	AddBehaviour addBehaviour = AddBehaviour.IGNORE;

	public MaxSizeList(int maxSize) {
		this.maxSize = maxSize;
		this.elements = new ArrayList<E>(maxSize + 1);
	}

	protected MaxSizeList(int maxSize, List<E> backingList) {
		this.maxSize = maxSize;
		if (backingList.size() > 0) {
			throw new RuntimeException("The backing list should be of zero size");
		}
	}

	@Override
	public void clear() {
		this.elements.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.elements.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return this.elements.containsAll(collection);
	}

	@Override
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return this.elements.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return this.elements.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return this.elements.remove(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		throw new UnsupportedOperationException("retainAll(Collection<?>) is not supported");
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public Object[] toArray() {
		return this.elements.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.elements.toArray(a);
	}

	public boolean add(E e) {

		switch (addBehaviour) {
		case ADD_HEAD:
			if (this.elements.size() >= maxSize) {
				elements.remove(elements.size() - 1);
			}
			elements.add(0, e);
			return true;
		case ADD_TAIL:
			if (this.elements.size() >= maxSize) {
				elements.remove(0);
			}
			elements.add(e);
			return true;
		default:
			if (this.elements.size() < maxSize) {
				elements.add(e);
				return true;
			}
		}

		return false;
	}

	public void add(int index, E e) {
		if (index > maxSize) {
			throw new IndexOutOfBoundsException ("index (" + index + ") is greater than max size ("+maxSize+")");
		}
		
		switch (addBehaviour) {
		case ADD_HEAD:
			if (this.elements.size() >= maxSize) {
				elements.remove(elements.size() - 1);
			}
			elements.add(index, e);
		case ADD_TAIL:
			if (this.elements.size() >= maxSize) {
				elements.remove(0);
			}
			elements.add(index,e);
		default:
			if (this.elements.size() < maxSize) {
				elements.add(index,e);
			}
		}		
	}

	/**
	 * While all elements will be processed, those that get added depend on the underlying
	 * implementation of the add() method.
	 * 
	 */
	public boolean addAll(Collection<? extends E> c) {
		boolean added = false;
		for (E entry : c) {
			added |= add(entry);
		}
		return added;
	}

	/**
	 * While all elements will be processed, those that get added depend on the underlying
	 * implementation of the add() method.
	 * 
	 * A RuntimeException is thrown if index is larger than the maxSize
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		if (index > maxSize) {
			throw new RuntimeException("index (" + index + ") is greater than max size ("+maxSize+")");
		}		
		boolean added = false;
		for (E entry : c) {
			added |= add(entry);
		}
		return added;	}

	public boolean equals(Object o) {
		return elements.equals(o);
	}

	public E get(int index) {
		return elements.get(index);
	}

	public int hashCode() {
		return elements.hashCode();
	}

	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return elements.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return elements.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return elements.listIterator(index);
	}

	public E remove(int index) {
		return elements.remove(index);
	}

	public E set(int index, E element) {
		return elements.set(index, element);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	public int getMaxSize() {
		return this.maxSize;
	}
}
