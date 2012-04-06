package ajmas74.experimental.graphingcalc;

import java.util.*;
/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable 'typecomment':
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Parser {

  public static final char[] LEFT_SEPARATORS
    = new char[] {'(','[','{'};
      
  public static final char[] RIGHT_SEPARATORS
    = new char[] {')',']','}'};
      
  public static final char[] SEPARATORS
    = new char[] {'+','-','*',
      '/','&','|','<','>','^','=',';',','};
      
	public Equation parse ( String input ) {

	  StringBuffer part = new StringBuffer();
	  List equations = new Vector();
    equations.add(new Equation());
    //int rmvCount = 1;
    Equation eq = null;
    
	  eqLoop: for ( int i=0; i<input.length();i++) {
	    eq = (Equation) equations.get(equations.size()-1);
	    
	    int separator = 0;
	    char c = input.charAt(i);
	    for ( int ix=0; ix<LEFT_SEPARATORS.length;ix++ ) {
        if ( c == LEFT_SEPARATORS[ix] ) {
          
          Equation newEq = new Equation();
	        
          if ( part.length() > 0 ) {
          	if ( part.toString().equals("-") ) {
          		newEq.negate(); 
          	} else {
              addToEquation(eq,part.toString());
          	}
            part.setLength(0);           
          }   
           
          List l = eq._parts;
          if (l.size() > 0 ) {
            Object obj = l.get(l.size()-1);
            if ( obj instanceof String && UnaryFunctions.isUnaryFunction((String)obj)) {
              l.remove(l.size()-1);
              newEq.setFunctionName((String)obj);
            }
          }
                    
          equations.add(newEq);
          eq.add(newEq);
          
          continue eqLoop;        

        }
      	      
	    }
	    for ( int ix=0; ix<RIGHT_SEPARATORS.length;ix++ ) {
	      if ( c == RIGHT_SEPARATORS[ix] ) {
          if ( part.length() > 0 ) {
            addToEquation(eq,part.toString());
            part.setLength(0);           
          }   	        
          equations.remove(equations.size()-1);
          continue eqLoop;
	      }
	    }
      for ( int ix=0; ix<SEPARATORS.length;ix++ ) {
        if ( c == SEPARATORS[ix] ) {
          separator = Separator.GENERAL;
          break;
        }
      }   
		  if ( separator > 0) {
//		    if ( c == '-' && 
//		      (( eq.getList().size() == 0) 
//		      ||  !( eq.getList().get(eq.getList().size()-1) instanceof Double ))) {
//          part.append(c);
//          continue;
//		    }
		    if ( part.length() > 0 ) {
		      String data = part.toString();
		      addToEquation(eq,part.toString());
          part.setLength(0);  
			  }
		    
		    if ( c == '-' && eq.getList().size() == 0) {
		    	part.append('-');        	
		    } else {
		    	eq.add(new Separator(separator,c));
		    }
		  } else {
	      part.append(c);
      }
	  }
    eq = (Equation) equations.get(0);
	  if ( part.length() > 0 ) {
      addToEquation(eq,part.toString());
	    part.setLength(0);           
	  }     

	  return eq;
	   
  }
 
  /** */
  void addToEquation( Equation eq, String value ) {
  	if ( value instanceof String ) {  		
      try { 
        eq.add(new Double(value));                            	
      } catch ( NumberFormatException ex ) {
      	if ( Constant.isConstant(value) ) {
      		eq.add(Constant.getConstantFor(value));
      	} else {
          eq.add(value);
      	}
      }  		
  	} else {
  		System.err.println("not a string");
  	}
  }
  
     
   public void display(Equation eq) {
   	 Equation eq2 = eq.simplify();
   	 
   	 Object obj = eq2;   	 
   	 if ( eq2.getList().size() == 1 ) {
   	   obj = eq2.getList().get(0);
   	 }
   	 //System.out.println(eq2.getList().size());
     System.out.println(eq + " => " + obj);
   }
   
   
  public void display(List parts) {
    for ( int i=0; i<parts.size(); i++) {
      System.out.print( parts.get(i));
    }
    System.out.println("");
    //System.out.println( " @@@@ ");   
  }
      
   
   public static void main ( String[] args ) {
     try {
      	System.out.println((int)'?');
       //System.out.println(((int)'\u03C0') + "\u03C0");
       System.out.println(((int)'\u00E9') + "\u00E9");
       Parser p = new Parser();
       p.display(p.parse("3*4"));//*(-33^2)");
       p.display(p.parse("3*4").simplify());
       p.display(p.parse("log(3)"));
       p.display(p.parse("log(3)").simplify());
       p.display(p.parse("x*log(3)"));
       p.display(p.parse("x*log(-33^2)"));
       p.display(p.parse("x*log(-3)*2"));

       System.out.println(p.parse("x*log((-33)*2^3)") + " => " + p.parse("x*log((-33)*2^3)").simplify());
 
  		 p.display(p.parse("?*1"));
  		 p.display(p.parse("\u03C0*1"));
  		 p.display(p.parse("pi*1"));
  		 p.display(p.parse("pi*1").simplify());
  		 
       p.display(p.parse("x*3"));       
       p.display(p.parse("x*3"));
       p.display(p.parse("0.5^2"));       
       p.display(p.parse("-0.5+0.5"));   
       p.display(p.parse("(-0.5+0.5)"));

       // This is giving an incorrect result, the answer should be zero
       System.out.println(p.parse("-((0.5^2)+(0.5^2))") + " => " + p.parse("-((0.5^2)+(0.5^2))").simplify());
        System.out.println(p.parse("-(0.5^2)+(0.5^2)") + " => " + p.parse("-((0.5^2)+(0.5^2))").simplify());
        System.out.println(p.parse("(-(0.5^2))+(0.5^2)") + " => " + p.parse("-((0.5^2)+(0.5^2))").simplify()); 
       // this is okay, but it is essentially the same thing!!!!
       System.out.println(p.parse("(-(0.5^2)-(0.5^2))") + " => " + p.parse("((0.5^2)-(0.5^2))").simplify());
       System.out.println(p.parse("(-0.5^2-0.5^2)") + " => " + p.parse("((0.5^2)-(0.5^2))").simplify());
       p.display(p.parse("(-0.5-0.5)"));
       
       p.display(p.parse("(0.5-x-0.5)"));
       p.display(p.parse("bob(1,2,3)"));

      System.out.println(p.parse("((((((0+5))))))").simplify() + " => " );
			p.display(p.parse("1/(0+x)"));

//       HashMap varMap = new HashMap();
//       varMap.put("x",new Double(2.0));
//       Equation eq = p.parse("x*3").simplify();
//       System.out.println("--->"+eq);
//       for ( int i=0; i<30; i++ ) {
//         varMap.put("x",new Double(i));
//         p.display( eq.simplify(varMap));
//         //p.display(eq.simplify(varMap));
//       }
//       
//       p.display(p.parse("x*(3*3)").simplify(varMap));
//  
//  
//       System.out.println("--------------------------");
//       
//       varMap = new HashMap();
//       //varMap.put("x",new Double(2.0));
//       eq = p.parse("x*0.5").simplify();
//       System.out.println("--->"+eq);
//       for ( int i=0; i<30; i++ ) {
//         varMap.put("x",new Double(i));
//         p.display( eq.simplify(varMap));
//         //p.display(eq.simplify(varMap));
//       }
         
     } catch ( Throwable t ) {
       t.printStackTrace();
     }

   }
   
   
}
