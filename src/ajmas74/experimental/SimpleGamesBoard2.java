/* Created on 26-Jun-2003 */
package ajmas74.experimental;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="mailto:ajmas@bigfoot.com">Andr&eacute;-John Mas</a>
 *
 */
public class SimpleGamesBoard2 extends JComponent implements ImageObserver {

	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;	
	
	int idx=0;
	SimpleBoard _board = new SimpleBoard();
	Point       _currentPos = new Point(1,1);
	boolean     _complete = false;
	Image       _overlayImage;
	
	public SimpleGamesBoard2 () {
		//addMouseMotionListener(new MouseMotionListenerImpl());
		addMouseListener(new MouseListenerImpl());
		//_overlayImage = Toolkit.getDefaultToolkit().createImage("datafiles/05-01-shuttle.jpg");
		//_overlayImage = Toolkit.getDefaultToolkit().createImage("datafiles/globe.jpg");
		//_overlayImage = Toolkit.getDefaultToolkit().createImage("datafiles/iccelogo.gif");
    //_overlayImage = Toolkit.getDefaultToolkit().createImage("datafiles/jordan001.jpg");
    try {
      _overlayImage = Toolkit.getDefaultToolkit().createImage(new URL("http://andrmas/applets/magicsquare/jordan001.jpg"));
      _overlayImage.getHeight(new MyImageObserver());
    } catch (MalformedURLException e) {
      // XXX Auto-generated catch block
      e.printStackTrace();
    }
	}
		
	Point calculateOffset ( int width, int height, int relToWidth, int relToHeight ) {
		Point p = new Point(0,0);
		p.x = (int) (( relToWidth / 2.0) - (width/2.0)) * -1;
		p.y = (int) (( relToHeight / 2.0) - (height/2.0)) * -1;
		return p;		
	}
	
  int basex = 0;
  int basey = 0;
	public void paint ( Graphics g ) {
		//System.out.println("abc " + idx++);		

    Rectangle gridRect = null;
		for ( int y=0; y<_board._layout.length; y++ ) {
			for ( int x=0; x<_board._layout[0].length; x++ ) {
				if ( gridRect == null ) {
					gridRect = _board.getBlockRect(x,y);
				} else {
					gridRect = gridRect.union(_board.getBlockRect(x,y));
				}
			}
		}  
		    
    if ( _complete ) {
      g.setColor(Color.white);

      //g.drawRect(0,0,rect.width,rect.height);
      g.fillRect(basex+0,basey+0,gridRect.width,gridRect.height);    
    } 
    
    Point offset = calculateOffset(
      _overlayImage.getWidth(null),
		  _overlayImage.getHeight(null),
		  gridRect.width,
		  gridRect.height);
      
		for ( int y=0; y<_board._layout.length; y++ ) {
			for ( int x=0; x<_board._layout[0].length; x++ ) {
				Rectangle rect = _board.getBlockRect(x,y);
								
//				if ( ! rect.intersects(g.getClipBounds())) {
//					continue;
//				}

				if ( _board._layout[y][x] != 0 ) {		
					//if ( ! _complete ) {		
					//	g.setColor(_board.getBlockColor(x,y));
					//} else {
						g.setColor(Color.white);
					//}
					
					g.fillRect(basex+rect.x,basey+rect.y,rect.width,rect.height);
					
					int value = _board._layout[y][x];
					int iy = (value-1) / 4;
					int ix = (value-1) % 4;
					Rectangle rect2 = _board.getBlockRect(ix,iy);
					g.drawImage(_overlayImage,
            basex+rect.x,
            basey+rect.y,
					  basex+(rect.x+rect.width-1),
            basey+(rect.y+rect.height-1),
					  offset.x+rect2.x,
            offset.y+rect2.y,
            offset.x+rect2.x+rect2.width-1,
            offset.y+rect2.y+rect2.height-1,
            Color.white,null);
					
					
					//g.fillRect(basex+rect.x,basey+rect.y,rect.width,rect.height);
					g.setColor(Color.black);	
					if ( ! _complete ) {							
						g.drawRect(basex+rect.x,basey+rect.y,rect.width-1,rect.height-1);
					}
          g.setXORMode(Color.white);
					g.drawString(""+_board._layout[y][x],basex+rect.x+(rect.width/2),basey+rect.y+(rect.height/2));
          g.setPaintMode();
				} else {
					
					if ( !_complete) {					
						g.setColor(Color.white);
						g.fillRect(basex+rect.x,basey+rect.y,rect.width,rect.height);
					} else {
						int value = 0;
						int iy = 3;
						int ix = 3;
						Rectangle rect2 = _board.getBlockRect(ix,iy);
						g.drawImage(_overlayImage,basex+rect.x,basey+rect.y,
						basex+rect.x+rect.width-1,basey+rect.y+rect.height-1,
					  offset.x+rect2.x,offset.y+rect2.y,offset.x+rect2.x+rect2.width-1,offset.y+rect2.y+rect2.height-1,Color.white,null);
					
					}
								
				}
			}
		}
    
    if ( _complete ) {
      g.setColor(Color.red);
//      Rectangle rect = null; 
//      for ( int y=0; y<_board._layout.length; y++ ) {
//        for ( int x=0; x<_board._layout[0].length; x++ ) {
//          if ( rect == null ) {
//            rect = _board.getBlockRect(x,y);
//          } else {
//            rect = rect.union(_board.getBlockRect(x,y));
//          }
//        }
//      }  
//      //g.drawRect(0,0,rect.width,rect.height);
      //g.drawRect(basex,basey,239,239);    
			g.drawRect(basex+0,basey+0,gridRect.width,gridRect.height); 
    }    
	}
	
	boolean isComplete() {
		if ( _board._layout[3][3] == 0 && _board._layout[0][0] == 1) {
			int xp = 0;
			int yp = 0;
			for ( int y=0; y <4; y++ ) {
				for ( int x=0; x <4; x++ ) {
					//System.out.println( _board._layout[y][x] + " - " + _board._layout[yp][xp] + " = " + (_board._layout[y][x] - _board._layout[yp][xp] ));

					//deal with first square
					if ( y == 0 && x == 0 ) {
						continue;
					}
					
					//deal with last square
					if ( y == 3 && x == 3 && _board._layout[y][x] == 0) {
						return true;
					}
					
					//System.out.println( " --- " + _board._layout[y][x] + " - " + _board._layout[yp][xp] + " = " + (_board._layout[y][x] - _board._layout[yp][xp] ));

					
					if ( _board._layout[y][x] - _board._layout[yp][xp] != 1 ) {
						//System.out.println( "xxx");
						xp = x; yp = y;
						return false;
					}
					xp = x; yp = y;
					
					//_board._layout[y][x] - _board._layout[yp][xp] != 0 &&
					//( y != 3 && y!=3) ) {
					//	return false;
					//}
					//xp = x; yp = y;
				}				
			}
			return false;
		}
		else {
			return false;
		}
	}
	


	class MouseListenerImpl implements MouseListener {

		public void mousePressed(MouseEvent e) {
			//System.out.println("mousePressed");
						
      if ( _complete ) {
        return;
      }
                  
			Point mousePoint = e.getPoint();
			int x = -1;
			int y = -1;
			for ( int x1=0; x1<4; x1++ ) {
				for ( int y1=0; y1<4; y1++ ) {
					Rectangle rect = _board.getBlockRect(x1,y1);
					if ( rect.contains(mousePoint) ) {
						x = x1;
						y = y1;
						break;
					}
				}				
			}

			if ( x == -1 || y == -1 ) {
				//System.out.println("out of bounds");
				return;
			}
			

			Rectangle rect1 = null;
			Rectangle rect2 = null;
			if ( _board._layout[y][x] != 0 ) {
 				if ( y > 0 && _board._layout[y-1][x] == 0 ) {
					_board._layout[y-1][x] = _board._layout[y][x];
					_board._layout[y][x] = 0;
					rect1 = _board.getBlockRect(x,y-1);
					rect2 = _board.getBlockRect(x,y);			
 				} else if ( y < _board._layout.length-1 && _board._layout[y+1][x] == 0 ) {
					_board._layout[y+1][x] = _board._layout[y][x];
					_board._layout[y][x] = 0;
					rect1 = _board.getBlockRect(x,y+1);
					rect2 = _board.getBlockRect(x,y);					
				} else if ( x > 0 && _board._layout[y][x-1] == 0 ) {
					_board._layout[y][x-1] = _board._layout[y][x];
					_board._layout[y][x] = 0;
					rect1 = _board.getBlockRect(x-1,y);
					rect2 = _board.getBlockRect(x,y);				
				} else if ( x < _board._layout[0].length-1 && _board._layout[y][x+1] == 0 ) {
					_board._layout[y][x+1] = _board._layout[y][x];
					_board._layout[y][x] = 0;
					rect1 = _board.getBlockRect(x+1,y);
					rect2 = _board.getBlockRect(x,y);					
				} else {
					//System.out.println("not in moveable square");
					return;
				}
				//System.out.println(rect1);
				rect1 = rect1.union(rect2);
				//repaint(0,0,240,240);
				repaint(rect1);
				//System.out.println(rect1);
				//System.out.println(rect2);				
				//repaint(rect2.x,rect2.y,rect2.width,rect2.height);		
			} else {
				//System.out.println("in hole");
			}
			
			_complete = isComplete();
			if ( _complete ) {
				//System.out.println("board is complete");
				repaint(0,0,240,240);
			}
		}

		public void mouseEntered(MouseEvent e) {
      //System.out.println("mouseEntered");
      }

		public void mouseExited(MouseEvent e) {
      //System.out.println("mouseExited");
      }

		public void mouseClicked(MouseEvent e) {
      //System.out.println("mouseClicked");
      }

		/**
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {}
	}
	
	static class SimpleBoard {
		
		int     _blockSize = 47;
		String  _boardName = "Test";
		
		int[][] _layout = new int[][]
			{{1,2,3,4},
			 {5,6,7,8}, 
			 {9,10,11,12}, 
			 {13,14,15,0} 
			};
			
	 Color[] _blockColor = new Color[16];
	 
	 SimpleBoard () {
     //randomize order
//     for ( int i=0; i<16; i++ ) {
//       int idx1 = (int)(16 * Math.random());
//       int idx2 = (int)(16 * Math.random());
//       
//       int y1 = idx1 / 4;
//       int x1 = idx1 % 4;  
//         
//       int y2 = idx2 / 4;
//       int x2 = idx2 % 4;
//       
//       if (_layout[y1][x1] == 0 || _layout[y2][x2] == 0 ) {
//       	continue;
//       }
//       
//       int a = _layout[y1][x1];
//       _layout[y1][x1] = _layout[y2][x2];
//       _layout[y2][x2] = a;            
//     }
     
		 for ( int a=0; a<16; a++ ) {
			   _blockColor[a] = new Color((int)(Math.random() * 255),(int)(Math.random() * 255),(int)(Math.random() * 255));							
		 }	 	
	 }

		public Rectangle getBlockRect( int x, int y ) {
			return new Rectangle(x*_blockSize,y*_blockSize,_blockSize,_blockSize);
		}
		
		public Color getBlockColor( int x, int y ) {
			return _blockColor[_layout[y][x]];
		}
		
		public Image getImageForSquare( int x, int y) {
			int value = _layout[y][x];
			return null;
		}
	}
	
  class MyImageObserver implements ImageObserver {
    public boolean imageUpdate(Image theimg, int infoflags,
                               int x, int y, int w, int h) {
  //      if ((infoflags & (ERROR)) != 0) {
  //          errored = true;
  //      }
  //      if ((infoflags & (WIDTH | HEIGHT)) != 0) {
  //          positionImages();
  //      }
        boolean done = ((infoflags & (ERROR | FRAMEBITS | ALLBITS)) != 0);
        // Repaint immediately if we are done, otherwise batch up
        // repaint requests every 100 milliseconds
        repaint(done ? 0 : 100); 
        return !done; //If done, no further updates required.
    }
  }
    
	public static void main (String[] args) {
		SimpleGamesBoard2 sgb = new SimpleGamesBoard2();
		JFrame frame = new JFrame();
		frame.setBounds(50,50,500,400);
		frame.getContentPane().add(sgb);
		frame.setVisible(true);
	}
	
//  /**
//   * @see java.awt.Component#update(java.awt.Graphics)
//   */
//  public void update(Graphics g) {
//    // TODO Auto-generated method stub
//    paint(g);
//  }

}
