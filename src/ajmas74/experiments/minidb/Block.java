/* Created on 22-Aug-2003 */
package ajmas74.experiments.minidb;

/**
 * @author Andre-John Mas
 *
 */
public class Block {
  
  private long    _offset;
  
  private long    _size;
  private byte[]  _data;
  private boolean _relocatable;
  
  /**
   * @return
   */
  public byte[] getData() {
    return _data;
  }

  /**
   * @return
   */
  public boolean isRelocatable() {
    return _relocatable;
  }

  /**
   * @return
   */
  public long getSize() {
    return _size;
  }

  /**
   * @param bs
   */
  public void setData(byte[] bs) {
    _data = bs;
  }

  /**
   * @param b
   */
  public void setRelocatable(boolean b) {
    _relocatable = b;
  }

  /**
   * @param l
   */
  public void setSize(long l) {
    _size = l;
  }

}
