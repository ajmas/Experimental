/* Created on 6-Feb-2004 */
package ajmas74.experiments.fontchooser;

import java.awt.*;
import java.awt.font.*;
import javax.swing.*;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class FontPreviewPane extends JComponent {

  Font _font;
  
  /**
   * 
   */
  public FontPreviewPane() {
    super();
    // TODO Auto-generated constructor stub
  }

 
  public void paint ( Graphics g ) {
    super.paint(g);
    LineMetrics _metrics = _font.
  }
  
  /**
   * @return Returns the font.
   */
  public Font getFont() {
    return _font;
  }

  /**
   * @param font The font to set.
   */
  public void setFont(Font font) {
    _font = font;
  }

}
