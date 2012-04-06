package ajmas74.experiments;

// 1 dot = 8 bytes
// 1 line = 8 bytes * 40 = 320 bytes
import java.util.*;
import java.text.*;

public class ProgressText {

  private static final int DOT_BYTES = 2048;
  private static final int DOTS_PER_BLOCK = 10;
  private static final int DOTS_PER_LINE = 80;
  private static final int PERCENT_BLOCK_SIZE = 6;

  private int _fileSize = 0;
  private int _amountTransfered = 0;
  private int _dotCount = 0;
  private int _percent = 0;

  private DecimalFormat _numberFormatter;

  private int _dotsPerBlock = DOTS_PER_BLOCK;
  private int _dotsPerLine = DOTS_PER_LINE;
  private int _charsPersLine = 80;
  private int _byesPerDot = DOT_BYTES;

  public ProgressText ( int fileSize ) {
    _fileSize = fileSize;
    _numberFormatter = new DecimalFormat("000");
    calculateDotsPerLine();
  }

  public void setBytesPerDot ( int bytes ) {
    _byesPerDot = bytes;
  }

  public void setBlockSize( int dotsPerBlock ) {
    _dotsPerBlock = dotsPerBlock;
    calculateDotsPerLine();
  }

  public void setCharsPerLine( int charsPersLine ) {
    _charsPersLine = charsPersLine;
    calculateDotsPerLine();
  }

  private void calculateDotsPerLine () {
    int x = _charsPersLine / (_dotsPerBlock+1);
    _dotsPerLine = _charsPersLine - PERCENT_BLOCK_SIZE - x;
    _dotsPerLine = _dotsPerLine - (_dotsPerLine % _dotsPerBlock);
  }

  private void displayPercent ( int percent ) {

    System.out.println(" [" + _numberFormatter.format(percent)+ "%]");
  }

  private void inc() {
    System.out.print(".");
    _amountTransfered += _byesPerDot;
    _dotCount++;
    if ( _dotCount % _dotsPerBlock == 0 ) {
      System.out.print(" ");
    }

    if ( _dotCount == _dotsPerLine ) {
      _percent = (int) (((double)_amountTransfered / (double)_fileSize) * 100);
      displayPercent( _percent );
      _dotCount = 0;
    }
  }

  private void incSpace() {
    System.out.print(" ");
    _dotCount++;
    if ( _dotCount % _dotsPerBlock == 0 ) {
      System.out.print(" ");
    }
  }

  public void setAmountTransfered ( int bytes ) {
    int dotsToAdd = (bytes - _amountTransfered) / _byesPerDot;
    //_amountTransfered = bytes;
    for ( int i=0; i<dotsToAdd; i++ ) {
      inc();
    }
  }

  public void completed () {
    int addSpaces = 0;
    if ( _percent != 100 ) {
      addSpaces = _dotsPerLine - _dotCount;

      for ( int i=0; i<addSpaces; i++ ) {
        incSpace();
      }
      displayPercent(100);
    }

    // ( " + ((((double)_fileSize)/1024)/1024) + "Mb
    System.out.println("Finished. Transfered " + _fileSize + " bytes ");
  }

  public static void main ( String[] args ) {
    //int fileSize = 972;
    int fileSize = 29437583;
    ProgressText pt = new ProgressText ( fileSize );
    pt.setBytesPerDot(4092);
    //pt.setCharsPerLine(80);
   // pt.setBlockSize(13);
    for ( int i=0; i<8; i++ ) {
      System.out.print("123456789 ");
    }
    System.out.println("");

    //int amountTransfered = 0;
    for ( int i=0; i<fileSize;i++ ) {
      pt.setAmountTransfered(i);
    }
    pt.completed();
  }

}