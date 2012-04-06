/* Created on 26-Jun-2003 */
package ajmas74.experiments;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;
import java.awt.image.*;

/**
 * @author <a href="mailto:ajmas@bigfoot.com">Andr&eacute;-John Mas</a>
 *
 */
public class SimpleGamesBoard extends Component {

	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;	
	
	int _idx = 0;
	Point    _clickPoint = null;
	Point    _currentPoint = null;

	Rectangle _selectionRect = null;
	Rectangle _repaintRect = null;
	boolean  _popUpTrigger = false;
	boolean  _newSelection = false;
	
	SimpleBoard _board = new SimpleBoard();
	Point       _currentPos = new Point(1,1);
  Image       _playerImage = null;
  
	public SimpleGamesBoard () {
		//addMouseMotionListener(new MouseMotionListenerImpl());
		//addMouseListener(new MouseListenerImpl());
		//setDoubleBuffered(false);
		//addKeyListener(new KeyListenerImpl());
    _playerImage = Toolkit.getDefaultToolkit().createImage("datafiles/img40x40-002.gif");
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
		for ( int y=0; y<_board._layout.length; y++ ) {
			for ( int x=0; x<_board._layout[y].length; x++ ) {
				Rectangle rect = _board.getBlockRect(x,y);
								
				if ( ! rect.intersects(g.getClipBounds())) {
					continue;
				}
				
				//if ( _board.isWall(x,y) ) {
          Image img = _board.getImageForBlock(x,y);
          if ( img == null ) {
            g.setColor(_board.getBlockColor(x,y));
            g.fillRect(rect.x,rect.y,rect.width,rect.height);
          } else {
            g.drawImage(img,rect.x,rect.y,rect.width,rect.height,null);
          }

				//} else {
				//	g.setColor(_board.getBlockColor(x,y));
				//	g.fillRect(rect.x,rect.y,rect.width,rect.height);					
				//}
				
				rect = _board.getBlockRect(_currentPos.x,_currentPos.y);
				//g.setColor(Color.green);
				//g.fillOval(rect.x,rect.y,rect.width,rect.height);
        g.drawImage(_playerImage,rect.x,rect.y,rect.width,rect.height,null);
			}
		}
		if ( _selectionRect != null ) {
			g.setColor(Color.red);
			g.drawRect(_selectionRect.x,_selectionRect.y,_selectionRect.width,_selectionRect.height);
		}
	}
	
	private void movePlayer ( int direction ) {
		switch ( direction ) {
			case UP:
			  if ( _currentPos.y > 1 && !_board.isWall(_currentPos.x,_currentPos.y-1)) {
			  	_currentPos.y--;
			  	Rectangle rect = _board.getBlockRect(_currentPos.x,_currentPos.y+1);
					repaint(rect.x,rect.y,rect.width,rect.height);
					rect = _board.getBlockRect(_currentPos.x,_currentPos.y);
					repaint(rect.x,rect.y,rect.width,rect.height);					
			  }
			  break;
			case DOWN:
				if ( _currentPos.y <_board._layout.length  && !_board.isWall(_currentPos.x,_currentPos.y+1)) {
					_currentPos.y++;
					Rectangle rect = _board.getBlockRect(_currentPos.x,_currentPos.y-1);
					repaint(rect.x,rect.y,rect.width,rect.height);
					rect = _board.getBlockRect(_currentPos.x,_currentPos.y);
					repaint(rect.x,rect.y,rect.width,rect.height);					
				}
				break;			
			case LEFT:
				if ( _currentPos.x > 1  && !_board.isWall(_currentPos.x-1,_currentPos.y)) {
					_currentPos.x--;
					Rectangle rect = _board.getBlockRect(_currentPos.x+1,_currentPos.y);
					repaint(rect.x,rect.y,rect.width,rect.height);
					rect = _board.getBlockRect(_currentPos.x,_currentPos.y);
					repaint(rect.x,rect.y,rect.width,rect.height);					
				}
				break;				
			case RIGHT:
				if ( _currentPos.x < _board._layout[0].length  && !_board.isWall(_currentPos.x+1,_currentPos.y)) {
					_currentPos.x++;
					Rectangle rect = _board.getBlockRect(_currentPos.x-1,_currentPos.y);
					repaint(rect.x,rect.y,rect.width,rect.height);
					rect = _board.getBlockRect(_currentPos.x,_currentPos.y);
					repaint(rect.x,rect.y,rect.width,rect.height);					
				}
				break;			
		}
	}
	
	
	class KeyListenerImpl implements KeyListener {
		
	    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			if ( e.getKeyCode() == KeyEvent.VK_UP ) {
				movePlayer(UP);
			} else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {			
				movePlayer(DOWN);
			} else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
				movePlayer(LEFT);
			} else if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
				movePlayer(RIGHT);
			} else {
				System.out.println(e);
			}
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
      // TODO Auto-generated method stub
			System.out.println("y");
    }

  }
  
  
	static class SimpleBoard implements ImageObserver {
		
    Image   _wallImage = null;
    Image   _corridorImage = null;
    boolean _imageLoaded = false;
		int     _blockSize = 32;
		String  _boardName = "Test";
		
		int[][] _layout = new int[][]
			{{2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2},
			 {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1},
			 {1,0,1,0,1,1,0,1,0,1,1,0,0,0,0,0,0,0,0,1},
			 {1,0,0,0,0,0,0,0,0,1,1,0,1,0,1,1,0,1,0,1},
			 {1,0,1,0,1,1,0,1,0,0,0,0,1,0,1,1,0,1,0,1},		
			 {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1},
			 {1,0,1,0,1,1,0,1,0,1,1,0,0,0,0,0,0,0,0,1},
			 {1,0,0,0,0,0,0,0,0,1,1,0,1,0,1,1,0,1,0,1},
			 {1,0,1,0,1,1,0,1,0,0,0,0,1,0,1,1,0,1,0,1},		
			 {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1},			 
			 {2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2}	 
			};
		  
    SimpleBoard () {
      _wallImage = Toolkit.getDefaultToolkit().createImage("datafiles/img40x40-001.jpg");
      _corridorImage = Toolkit.getDefaultToolkit().createImage("datafiles/img40x40-003.gif");
    }
    
		public boolean isWall ( int x, int y ) {
			return (_layout[y][x] != 0 );
		}
		
		public Rectangle getBlockRect( int x, int y ) {
			return new Rectangle(x*_blockSize,y*_blockSize,_blockSize,_blockSize);
		}
		
		public Color getBlockColor( int x, int y ) {
			switch ( _layout[y][x] ) {
				case 0: return Color.white;
				case 1: return Color.blue;
				case 2: return Color.red;		
				default:
				return Color.black;		
			}
		}
    
    public Image getImageForBlock( int x, int y ) {
      if ( _layout[y][x] == 1 ) {

//        while ( ! _imageLoaded ) {
//          _wallImage.getHeight(this);
//          try {
//            Thread.sleep(400);
//          } catch ( InterruptedException ex ) {
//          }
//        }
        return _wallImage;
      } else if ( _layout[y][x] == 0 ) {
        return _corridorImage;
      }
      return null;
    }
    
    /* (non-Javadoc)
     * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int, int)
     */
    public boolean imageUpdate(
      Image img,
      int infoflags,
      int x,
      int y,
      int width,
      int height) {
      // XXX Auto-generated method stub
      _imageLoaded = ( (infoflags & ImageObserver.ALLBITS) == 0 );
      return _imageLoaded;
    }

	}
	
	public static void main (String[] args) {
		SimpleGamesBoard sgb = new SimpleGamesBoard();
		JFrame frame = new JFrame();
		frame.setBounds(50,50,500,400);
		frame.getContentPane().add(sgb);
		frame.setVisible(true);
		frame.addKeyListener(sgb.new KeyListenerImpl());
	}
	
  /**
   * @see java.awt.Component#update(java.awt.Graphics)
   */
  public void update(Graphics g) {
    // TODO Auto-generated method stub
    paint(g);
  }

}
