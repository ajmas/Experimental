package ajmas74.experiments;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class SynchedScrollFrame extends JFrame {

  JScrollPane _scrollPaneA;
  JScrollPane _scrollPaneB;
	/**
	 * Constructor for SynchedScrollFrame.
	 */
	public SynchedScrollFrame() {
		super();
    getContentPane().setLayout(new GridBagLayout());
    
    JTextArea textAreaA = new JTextArea();
    JTextArea textAreaB = new JTextArea();
    
    _scrollPaneA = new JScrollPane();
    _scrollPaneA.getViewport().setView(textAreaA);
    _scrollPaneA.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    
    _scrollPaneB = new JScrollPane();
    _scrollPaneB.getViewport().setView(textAreaB); 
    _scrollPaneB.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);      
    _scrollPaneB.getVerticalScrollBar()
      .addAdjustmentListener(new MyAdjustmentListener());     
    
    GridBagConstraints constraints = new GridBagConstraints(1, GridBagConstraints.RELATIVE, 1, 1, 0.5, 0.5, GridBagConstraints.NORTHEAST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0); 
                
    getContentPane().add(_scrollPaneA,constraints); 
    
    constraints = new GridBagConstraints(2, GridBagConstraints.RELATIVE, 1, 1, 0.5, 0.5, GridBagConstraints.NORTHEAST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0); 
                    
    getContentPane().add(_scrollPaneB,constraints);        
    
    textAreaA.setDocument(new IntegerDocument());
	}

  class MyAdjustmentListener implements AdjustmentListener {
  	/**
		 * @see java.awt.event.AdjustmentListener#adjustmentValueChanged(AdjustmentEvent)
		 */
		public void adjustmentValueChanged(AdjustmentEvent e) {
      _scrollPaneA.getVerticalScrollBar().setValue(
        _scrollPaneB.getVerticalScrollBar().getValue()
      );
		}

  }
  
	public static void main(String[] args) {
    SynchedScrollFrame ssf = new SynchedScrollFrame();
    ssf.setBounds(100,100,500,400);
    ssf.setVisible(true);
	}
}

class IntegerDocument extends PlainDocument {
  public void insertString(int offset, String s,
    AttributeSet attributeSet) throws BadLocationException {
    
    try {
      Integer.parseInt(s);
    } catch ( Exception ex ) {
      Toolkit.getDefaultToolkit().beep();
      return;
    } 
    super.insertString(offset,s,attributeSet);
  }
}