package ajmas74.experiments;

/**
 * The goal of this class is to be able to take an expression and
 * convert to executable byte code on the fly.
 * 
 * @author <a href="mailto:andrejohn.mas@gmail.com">Andr&eacute;-John Mas</a>
 *
 */
public class ConditionToCode {

  public ConditionToCode ( String condition ) {
    int it = 5;
    
  }
  
  public static void main ( String[] args ) {
    ConditionToCode ctc = new ConditionToCode ( "it > 30" );
  }
  
}
