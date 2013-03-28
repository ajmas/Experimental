/* Created on 3-Dec-2003 */
package ajmas74.experimental.graphics2d;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public interface Graph2D {

  
  public Axis getXAxis();
  
  public Axis getYAxis();
  
  public void addDataset( DataSetIF dataset );
}
