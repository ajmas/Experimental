package ajmas74.experimental;

import java.util.*;


/* Status: currently a basic implementation, so there may still be bugs */

/** This implementation of HashMap orders they keys according
 *  to the order that the objects were first added to the hash
 *  map. Removing an object from the hash map will remove it
 *  from the ordering.
 *
 *  @author Andre-John Mas
 */
public class OrderedHashMap extends HashMap {
  private List _keyOrder = new Vector();
  private int _maxElements = -1;

  public OrderedHashMap() {
    super();
  }

  public OrderedHashMap(int maxElements) {
    super();
    _maxElements = maxElements;
  }

  /** when constructed using this manner the order will
   *  be according to that specified by the proved map,
   *  and then from the order of future adds, if the
   *  objects are new.
   */
  public OrderedHashMap(Map t) {
    super(t);
    Iterator iter = t.keySet().iterator();
    while (iter.hasNext()) {
      _keyOrder.add(iter.next());
    }
  }

  /** see java.util.Map#put() */
  public Object put(Object key, Object value) {
    if (get(key) == null) {
      _keyOrder.add(key);
    }

    // limit the nb of elements in the hashMap if required by removing the FIRST one added
    if (_maxElements > -1) {
      if (_keyOrder.size() > _maxElements) {
        remove(_keyOrder.get(0));
      }
    }
    return super.put(key, value);
  }

  public Object remove(Object key) {
    _keyOrder.remove(key);

    return super.remove(key);
  }

  public void clear() {
    _keyOrder.clear();
    super.clear();
  }

  public Set getKeySet() {
    return new OrderedSet(_keyOrder);
  }

  public Set keySet() {
    return new OrderedSet(_keyOrder);
  }
  
  public Set getInvertedKeySet() {
    return new InvertedOrderedSet(_keyOrder);
  }

  class OrderedSet extends HashSet {
    List col = null;

    public OrderedSet(List t) {
      col = t;
    }

    public Iterator iterator() {
      return new OrderedSetIterator(col);
    }
  }

  class InvertedOrderedSet extends HashSet {
    List col = null;

    public InvertedOrderedSet(List t) {
      try {
        ArrayList invertedList = new ArrayList();

        for (int i = t.size(); i > 0; i--) {
          invertedList.add(t.get(i-1));
        }
        col = invertedList;
      } catch (Exception e) {
        System.out.println("Exception: " + e);
      }
    }

    public Iterator iterator() {
      return new OrderedSetIterator(col);
    }
  }

  class OrderedSetIterator implements Iterator {
    int idx = -1;
    List col = null;

    public OrderedSetIterator(List t) {
      col = t;
    }

    public boolean hasNext() {
      return (idx < col.size() - 1);
    }

    public Object next() {
      idx++;
      return col.get(idx);
    }

    public void remove() {
      throw new RuntimeException("Not Implemented");
    }
  }

}