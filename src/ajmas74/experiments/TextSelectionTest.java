/* Created on 26-Jun-2003 */
package ajmas74.experiments;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

/**
 * @author <a href="mailto:ajmas@bigfoot.com">Andr&eacute;-John Mas</a>
 *
 */
public class TextSelectionTest extends Component {

	int _idx = 0;
	Point    _clickPoint = null;
	Point    _currentPoint = null;

	Rectangle _selectionRect = null;
	Rectangle _repaintRect = null;
	boolean  _popUpTrigger = false;
	boolean  _newSelection = false;
	
	public TextSelectionTest () {
		addMouseMotionListener(new MouseMotionListenerImpl());
		addMouseListener(new MouseListenerImpl());
		//setDoubleBuffered(false);
	}
	
	
	public void paintLetter ( Graphics g, Rectangle rect, char character, boolean selected) {
		//Color bgColor = Color.white;
		Color bgColor = new Color((int)(Math.random() * 255),(int)(Math.random() * 255),(int)(Math.random() * 255));
		Color fgColor = Color.black;		
		if ( selected ) {
			bgColor = Color.black;
			fgColor = Color.white;
		}
		g.setColor(bgColor);
		g.fillRect(rect.x,rect.y,rect.width,rect.height);
		g.setColor(Color.blue);		
		//g.drawRect(rect.x,rect.y,rect.width,rect.height);
		g.setColor(fgColor);		
		g.drawString(""+character,rect.x+(rect.width/2),rect.y+rect.height);
	}
	
	int width=15;
	int height=15;
	int xOffset = 00;
	int yOffset = 00;
	public void paint ( Graphics g ) {
		System.out.println("abc - "+ _idx++);		
		boolean selected = false;
		for ( int x=0; x<70; x++ ) {
			for ( int y=0; y<70; y++ ) {
				Rectangle rect = new Rectangle(xOffset+x*width,yOffset+y*height,width,height);
				
				if ( ! rect.intersects(g.getClipBounds())) {
					continue;
				}
				
				boolean intersects = false;
				if ( _selectionRect != null ) {
					intersects = ( rect.intersects(_selectionRect) || rect.contains(_clickPoint));
				}
				paintLetter(g, rect,'A',intersects);
			}
		}
		if ( _selectionRect != null ) {
			g.setColor(Color.red);
			g.drawRect(_selectionRect.x,_selectionRect.y,_selectionRect.width,_selectionRect.height);
		}
	}
	
	class MouseListenerImpl implements MouseListener {

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
//    	if ( (e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0) {
//				//_popUpTrigger = true;
//    		return;
//    	}
			if ( _selectionRect != null ) {
				repaint(_selectionRect.x-20,
				_selectionRect.y-20,
				_selectionRect.width+40,
				_selectionRect.height+40);
			}
			_clickPoint = e.getPoint();
			_selectionRect = new Rectangle(_clickPoint.x,_clickPoint.y,0,0);
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
      // TODO Auto-generated method stub
			//_clickPoint = null;
			//_currentPoint = null;
			//_selectionRect = null;
			_popUpTrigger = false;
    }
	}
	
	class MouseMotionListenerImpl extends MouseMotionAdapter {
		
	    /**
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
      // TODO Auto-generated method stub
      super.mouseDragged(e);
			System.out.println("md: " + e);
			System.out.println( e.getModifiers() & MouseEvent.BUTTON1_MASK);
      if ( (e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0) {
      	return;
      }
			_currentPoint = e.getPoint();
			
			repaint(_selectionRect.x-20,
			_selectionRect.y-20,
			_selectionRect.width+40,
			_selectionRect.height+40);
			Rectangle oldRect = _selectionRect;
			if ( _selectionRect == null ) {
				_selectionRect = new Rectangle();
			}
			if ( _clickPoint.x > _currentPoint.x && _clickPoint.y > _currentPoint.y ) {
				_selectionRect.x = _currentPoint.x;
				_selectionRect.y = _currentPoint.y;
				_selectionRect.width = _clickPoint.x - _currentPoint.x;
				_selectionRect.height = _clickPoint.y - _currentPoint.y;																
			} else if ( _clickPoint.x > _currentPoint.x && _clickPoint.y < _currentPoint.y ) {
				_selectionRect.x = _currentPoint.x;
				_selectionRect.y = _clickPoint.y;
				_selectionRect.width = _clickPoint.x - _currentPoint.x;
				_selectionRect.height = _currentPoint.y - _clickPoint.y;
			} else if ( _clickPoint.x < _currentPoint.x && _clickPoint.y > _currentPoint.y ) {
				_selectionRect.x = _clickPoint.x;
				_selectionRect.y = _currentPoint.y;
				_selectionRect.width = _currentPoint.x - _clickPoint.x;
				_selectionRect.height = _clickPoint.y - _currentPoint.y;				
			} else {
				_selectionRect.x = _clickPoint.x;
				_selectionRect.y = _clickPoint.y;				
				_selectionRect.width = _currentPoint.x - _clickPoint.x;
				_selectionRect.height = _currentPoint.y - _clickPoint.y;								
			}
						
			_repaintRect = new Rectangle(
			  Math.min(_selectionRect.x,oldRect.x),
			  Math.min(_selectionRect.y,oldRect.y),
			  Math.max(_selectionRect.width,oldRect.width)+100,
			  Math.max(_selectionRect.height,oldRect.height)+100
			);
			//repaint(0);
			//RepaintManager.currentManager(this).
			repaint(_selectionRect.x-20,
			_selectionRect.y-20,
			_selectionRect.width+40,
			_selectionRect.height+40);
      System.out.println(e.getPoint());
    }

}
	
	public static void main (String[] args) {
		JFrame frame = new JFrame();
		frame.setBounds(50,50,500,400);
		frame.getContentPane().add(new TextSelectionTest());
		frame.setVisible(true);
	}
	
  /**
   * @see java.awt.Component#update(java.awt.Graphics)
   */
  public void update(Graphics g) {
    // TODO Auto-generated method stub
    paint(g);
  }

}
