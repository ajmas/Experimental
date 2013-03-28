package ajmas74.experimental;

/**
 * @author Andre-John Mas
 * @version 0.2
 * 
 * Generates characters for use in test cases. All values
 * are UTF. It is the conversion into the various character
 * sets that will define the limitations. Information on
 * unicode can be found at http://www.unicode.org.
 * 
 * This first version is not designed to necessarily include
 * all the characters from a character set, rather a sampling
 * that is suitable for tests.
 * 
 * Ability to view characters in the whole Unicode range is
 * dependent on the installed fonts and the text display
 * mechanism. For example under windows you need to install
 * all possible language sets and possibly use a viewer
 * such a Mozilla 1.3 (web browser or BabelPad (text editor)
 * that is capable of displaying the characters. Some terminals
 * are capable of displaying unicode, but in many cases they
 * are not configured to do so.
 * 
 */
public class CharacterGenerator {
  
  private CharacterRange [] UTF8_RANGES = new CharacterRange [] {
    new CharacterRange (0x21,0x7E,"ASCII/Basic Latin"),
    new CharacterRange (0x00c0,0x00f6,"ACCENTS"),
    new CharacterRange (0x0391,0x03CE,"Greek"),
    new CharacterRange (0x0401,0x044f,"Cyrillic"),       
    new CharacterRange (0x05D0,0x05f4,"Hebrew"),
    new CharacterRange (0x0621,0x067F,"Arabic"),
    new CharacterRange (0x3041,0x3096,"Hiragana"),
    new CharacterRange (0xADC0,0xAE8F,"Hangul Syllables") 
  };

  private CharacterRange [] LATIN_RANGES = new CharacterRange [] {
    new CharacterRange (0x21,0x7E,"ASCII/Basic Latin"),
    new CharacterRange (0x00c0,0x00f6,"ACCENTS"),
  };

  private CharacterRange[] ASCII_RANGES = new CharacterRange[] {
    new CharacterRange (33,126,"ASCII/Basic Latin")
  };
    
  // ------------------
  
  public CharacterGenerator () {
  }
  
  /** Generates characters from in the charRange and optionally
   *  displays the description of the character range.
   */        
  public String generateCharacters( CharacterRange[] charRanges,  boolean includeDesc  ) {
    StringBuffer strBuf = new StringBuffer();
    
    for ( int i=0; i<charRanges.length; i++ ) {  
      if ( includeDesc ) {   
        strBuf.append(charRanges[i].description + ":\n");  
      }
      strBuf.append(generateCharacters
        ( charRanges[i].start, charRanges[i].end));
      strBuf.append("\n\n");            
    }      
    return strBuf.toString();
  }
    
  /** Generates characters from start to end*/      
  public String generateCharacters( int start, int end  ) {
    StringBuffer strBuf = new StringBuffer();
    for ( int i=start; i<end; i++ ) {
      strBuf.append((char)i);
    }
    return strBuf.toString();
  }  
  
  /** Generates a UTF-8 sample */    
  public String generateUTF8Sample ( boolean includeDesc ) {
    return generateCharacters(UTF8_RANGES,includeDesc);
  }
  
  /** Generates a Latin sample */  
  public String generateLatinSample ( boolean includeDesc ) {
    return generateCharacters(LATIN_RANGES,includeDesc);
  }
  
  /** Generates an ASCII sample */
  public String generateASCIISample ( boolean includeDesc ) {
    return generateCharacters(ASCII_RANGES,includeDesc);
  }
    
  public static class CharacterRange {
    int start;
    int end;
    String description;
    
    public CharacterRange ( int start, int end, String description ) {
      this.start = start;
      this.end = end;
      this.description = description;
    }
  }
  
  public static void main ( String[] args ) {
    CharacterGenerator cg = new CharacterGenerator();
    System.out.println(cg.generateUTF8Sample(true));
    System.out.println("------------------------");
    System.out.println(cg.generateLatinSample(false));
    System.out.println("------------------------");
    System.out.println(cg.generateASCIISample(true));
    System.out.println("------------------------");   
  }
}
