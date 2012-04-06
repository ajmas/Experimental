package ajmas74.experiments;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.image.*;

/**
 * @author Andre-John Mas <ajmas@bigfoot.com> Player to play BlinkenLights
 *         annimations ( www.blinkenlights.de )
 * 
 */
public class BlinkenLightsPlayer extends Applet {

  /**
   * 
   */
  private static final long serialVersionUID = 1553895818452724243L;

  public final static int ROWS = 8;

  public final static int COLUMNS = 18;

  Thread playerThread;

  PlayerComponent playerComp;

  /** */
  BlinkenLightsPlayer(String fileName, boolean standalone) {
    try {
      BlinkenLightsFrame[] frames = loadDataFile(fileName);

      if (standalone) {
        displayAnimInFrame(frames, (new File(fileName)).getAbsolutePath());
      } else {

      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public BlinkenLightsFrame[] loadDataFile(String fileName) throws Exception {
    File f = new File(fileName);
    return loadDataFile(f.toURL());
  }

  /** */
  public BlinkenLightsFrame[] loadDataFile(URL u) throws IOException {
    Vector frames = new Vector();

    // URLConnection connection = u.openStream();
    InputStream fileIn = u.openStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(fileIn));

    BlinkenLightsFrame theFrame = null;
    String theLine = null;
    int currentRow = 0;
    while ((theLine = reader.readLine()) != null) {
      if (theLine.startsWith("#")) {
        continue;
      } else if (theLine.trim().length() == 0) {
        theFrame = new BlinkenLightsFrame();
        theFrame._bitMap = new boolean[ROWS][COLUMNS];
        currentRow = 0;
        frames.add(theFrame);
      } else if (theLine.startsWith("@")) {
        theFrame._displayTime = Integer.parseInt(theLine.substring(1));
      } else {
        for (int i = 0; i < theLine.length(); i++) {
          theFrame._bitMap[currentRow][i] = (theLine.charAt(i) == '1');
        }
        currentRow++;
      }
    }

    return (BlinkenLightsFrame[]) frames.toArray(new BlinkenLightsFrame[0]);
  }

  void displayAnimInApplet(BlinkenLightsFrame[] frames) {
    int blockWidth = this.getWidth() / COLUMNS;
    int blockHeight = this.getWidth() / ROWS;
    if (getWidth() == 0 && getHeight() == 0) {
      blockWidth = 30;
      blockHeight = 30;
    }
    playerComp = new PlayerComponent(frames);
    playerComp.setBounds(0, 0, COLUMNS * blockWidth, (ROWS * blockHeight)
        + blockHeight);
    this.add(playerComp);
  }

  void displayAnimInFrame(BlinkenLightsFrame[] frames, String title) {
    Frame theFrame = new PlayerFrame();
    theFrame.setLocation(100, 100);
    // theFrame.setBounds(100, 100, COLUMNS * 30, (ROWS * 30) + 30);
    theFrame.setLayout(new BorderLayout());
    theFrame.setTitle(title + " - BlinkenLightsPlayer");
    playerComp = new PlayerComponent(frames);
    playerComp.setBounds(0, 0, COLUMNS * 30, (ROWS * 30) + 30);
    theFrame.add(playerComp);
    theFrame.validate();
    theFrame.setVisible(true);
    // (new Thread((Runnable) playerComp)).start();
  }

  public void init() {

  }

  public void start() {
    String fileName = this.getParameter("animationfile");
    if (fileName == null) {
      throw new RuntimeException("no animationfile specified in parameters");
    }
    BlinkenLightsFrame[] frames;
    try {
      URL u = ((Applet) this).getDocumentBase();
      String urlStr = u.toString();
      urlStr = urlStr.substring(0, urlStr.lastIndexOf('/'));
      frames = loadDataFile(new URL(urlStr + fileName));
      //displayAnimInApplet(frames);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }    
    playerThread = new Thread((Runnable) playerComp);
    playerThread.start();
  }

  public void stop() {
    playerComp.stop();
  }

  public static void main(String[] args) {
    // String fileName = "datafiles/winter_in_the_city.blm";
    if (args.length > 0) {
      BlinkenLightsPlayer player = new BlinkenLightsPlayer(args[0], true);
      player.start();
    } else {
      System.out.println("usage: ...BlinkenLightsPlayer <blinkenlights file>");
    }
  }

  static class PlayerFrame extends Frame {
    public void update(Graphics g) {
      paint(g);
    }
  }

  static class BlinkenLightsFrame {
    int _displayTime;

    boolean[][] _bitMap;

    public String toString() {
      StringBuffer strBuf = new StringBuffer();
      for (int row = 0; row < _bitMap.length; row++) {
        for (int col = 0; col < _bitMap[row].length; col++) {
          if (_bitMap[row][col]) {
            strBuf.append("*");
          } else {
            strBuf.append(".");
          }
        }
        strBuf.append("\n");
      }
      return strBuf.toString();
    }
  }

  static class PlayerComponent extends Component implements Runnable {

    BlinkenLightsFrame[] _frames;

    int _lastFrameIdx = -1;

    int _frameIdx = 0;

    boolean _run = true;

    boolean firstTime = true;

    BufferedImage _bufferedImage;

    Graphics2D _biGraphics;

    boolean[][] _diffs;

    PlayerComponent(BlinkenLightsFrame[] frames) {
      _frames = frames;
      _diffs = new boolean[_frames[0]._bitMap.length][_frames[0]._bitMap[0].length];
    }

    void calculateDiffs(int frameIdx, int frameIdx2) {
      BlinkenLightsFrame frame1 = _frames[frameIdx];
      BlinkenLightsFrame frame2 = _frames[frameIdx2];

      for (int row = 0; row < frame1._bitMap.length; row++) {
        for (int col = 0; col < frame1._bitMap[row].length; col++) {
          _diffs[row][col] = (frame1._bitMap[row][col] == frame2._bitMap[row][col]);
        }
      }
    }

    public void stop() {
      _run = false;
    }

    public void run() {

      while (_run) {

        if (_lastFrameIdx != -1) {
          calculateDiffs(_lastFrameIdx, _frameIdx);
        }
        // paintBuffered();
        repaint();

        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() < time
            + _frames[_frameIdx]._displayTime) {
        }

        _lastFrameIdx = _frameIdx;
        if (_frameIdx == _frames.length - 1) {
          _frameIdx = 0;
        } else {
          _frameIdx++;
        }
      }

    }

    public void update(Graphics g) {
      // note you must have 'paint()' otherwise
      // anything else has no effect
      paint(g);
    }

    public void paint(Graphics g) {
      paintV1(g);
    }

    public void paintBuffered() {
      if (firstTime) {
        _bufferedImage = (BufferedImage) createImage(
            BlinkenLightsPlayer.COLUMNS * 30, BlinkenLightsPlayer.ROWS * 30);
        // _biGraphics = _bufferedImage.createGraphics();
        firstTime = false;
      }
      _biGraphics = (Graphics2D) _bufferedImage.getGraphics();

      BlinkenLightsFrame frame = _frames[_frameIdx];
      for (int row = 0; row < frame._bitMap.length; row++) {
        for (int col = 0; col < frame._bitMap[row].length; col++) {
          if (frame._bitMap[row][col]) {
            _biGraphics.setColor(Color.yellow);
          } else {
            _biGraphics.setColor(Color.black);
          }
          _biGraphics.fillRect(30 * col, 30 * row, 30, 30);
        }
      }
    }

    public void paintV1(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      paintBuffered();
      // g2.drawImage(_bufferedImage,0,0,this);
      g2.drawImage(_bufferedImage, 0, 0, null);
    }

    /**
     * this is an attempent to reduce flicker by only drawing what has changed.
     * the only problem is that it makes no difference as this only affects the
     * buffered image and not the copying of the buffered image.
     */
    public void paintV2(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      if (firstTime) {
        _bufferedImage = (BufferedImage) createImage(
            BlinkenLightsPlayer.COLUMNS * 30, BlinkenLightsPlayer.ROWS * 30);
        // _biGraphics = _bufferedImage.createGraphics();
        firstTime = false;
      }
      _biGraphics = (Graphics2D) _bufferedImage.getGraphics();

      BlinkenLightsFrame frame = _frames[_frameIdx];
      for (int row = 0; row < frame._bitMap.length; row++) {
        for (int col = 0; col < frame._bitMap[row].length; col++) {
          if (!_diffs[row][col]) {
            if (frame._bitMap[row][col]) {
              _biGraphics.setColor(Color.yellow);
            } else {
              _biGraphics.setColor(Color.black);
            }
            _biGraphics.fillRect(30 * col, 30 * row, 30, 30);
          }
        }
      }
      g2.drawImage(_bufferedImage, 0, 0, this);
    }

    /**
     * this is an attempent to reduce flicker by only doing the work elsewhere.
     * The only problem is that it makes no difference as this only affects the
     * buffered image and not the copying of the buffered image.
     */
    public void paintV3(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      if (_bufferedImage != null) {
        // g2.drawImage(_bufferedImage,0,0,this);
        g2.drawImage(_bufferedImage, 0, 0, null);
      }
    }
  }

}