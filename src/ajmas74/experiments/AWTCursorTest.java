/* Created on 28-Oct-2003 */
package ajmas74.experiments;

import java.awt.*;
/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class AWTCursorTest extends Frame{

  /**
   * 
   */
  public AWTCursorTest() {
    super();
    
    Toolkit t = Toolkit.getDefaultToolkit();
    Image i = t.getImage("datafiles/element.gif");
    Cursor c = t.createCustomCursor(i,new Point(1,1),"test");
    //t.getBestCursorSize()
    setCursor(c);
  }

  public static void main ( String[] args ) {
    
		AWTCursorTest test = new AWTCursorTest();
		test.setVisible(true);
  }
}
