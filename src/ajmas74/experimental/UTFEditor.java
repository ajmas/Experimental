package ajmas74.experimental;

import javax.swing.*;
import java.awt.*;

/**
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class UTFEditor extends JFrame {

	/**
	 * Constructor for UTFEditor.
	 */
	public UTFEditor() {
    JTextArea textArea = null;
    
		getContentPane().setLayout( new BorderLayout() );
    getContentPane().add(new JScrollPane( textArea = new JTextArea() ) );
    textArea.setFont(new Font("Times New Roman",Font.PLAIN,18));
    textArea.setWrapStyleWord(true);
    textArea.setLineWrap(true);
	}

	public static void main(String[] args) {
    UTFEditor editorFrame = new UTFEditor();
    editorFrame.setVisible(true);
	}
}
