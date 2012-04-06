package ajmas74.experimental.graphingcalc;

import java.util.*;

/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Equation {

  List _parts = new Vector();
  String _fnName;
  boolean _negate = false;
  
  public Equation () {
  }
  
  public Equation ( List parts ) {
    //System.out.println("xxx parts ====> " + parts); 
    _parts = parts;
  }
     
  public void add( Object element ) {
    _parts.add(element);
  }
  
  public void setFunctionName (String functionName) {
    _fnName = functionName;
  }
  
  public String getFunctionName () {
    return _fnName;
  }
  
  public Equation simplify() {
    return simplify(null);
  }
  
  public Equation simplify( Map variableMap ) {
    Equation eq = new Equation(calculate(new Vector(_parts),variableMap));
    
    List l = eq.getList();
    for ( int i=0; i<l.size();i++ ) {
    	if ( l.get(i) instanceof Equation ) {
    		List l2 = ((Equation) l.get(i)).getList();
    		int size = l2.size();
    		if ( size == 0 ) {
    			l.remove(i);
    			l.add(i,new Double(0));
    		} else if ( size == 1 ) {
    			Object obj = l2.get(0);
    			if ( obj instanceof Double ) {
    				if ( ((Equation) l.get(i))._negate ) {
    					obj = new Double(-1 * ((Double)obj).doubleValue());
    				}
    			}
    			l.remove(i);
    			l.add(i,obj);
    		}
    	}
    }
    return eq;
    
  }
    
  //private Str
  public String toString () {
  	StringBuffer strBuf = new StringBuffer();
    if ( _fnName != null ) {
    	strBuf.append(_fnName);
    }	
  	strBuf.append("(");
  	for ( int i=0; i<_parts.size(); i++ ) {
  		strBuf.append(_parts.get(i));
  	}
  	strBuf.append(")");
  	return strBuf.toString();
  }
  
  List getList () {
    return _parts;
  }

	void negate () {
		_negate = !_negate;
	}
	
  private static List calculate ( List parts ) {
    return calculate(parts,null);
  }
  
  private static List calculate ( List parts, Map variableMap ) {    
    if ( variableMap == null ){
      variableMap = new HashMap();
    }
    
    String[] fnOrder = new String[] { "^","*","/","+","-" };     
    String[] variables = (String[]) variableMap.keySet().toArray(new String[0]);
    
    for ( int i=0; i<variables.length;i++) {
      for ( int ix=0; ix<parts.size();ix++) {
        if ( parts.get(ix) instanceof String 
          && parts.get(ix).equals(variables[i]) ) {
            parts.remove(ix);
            parts.add(ix,variableMap.get(variables[i]));  
        }
      }
    }
    
    for ( int i=0; i<parts.size();i++) {
    	Object o = parts.get(i);
    	if ( o instanceof Constant ) {
    		parts.remove(i);
    		parts.add(i,new Double(((Constant)o).getValue()));
    	}
    }
    
     for ( int i=0; i<fnOrder.length;i++) {
       int startIdx = 0;
       for ( int ix=startIdx; ix<parts.size();ix++) {
         Object objx = parts.get(ix);
         if ( objx instanceof Separator ) {
           if ( fnOrder[i].charAt(0) == ((Separator)objx)._val)  {
             if ( ix > 0 && parts.get(ix-1) instanceof Double
               && parts.get(ix+1) instanceof Double) {   
                           
               double a = ((Double)parts.get(ix-1)).doubleValue();
               double b = ((Double)parts.get(ix+1)).doubleValue();
               if ( !Double.isNaN(a) && !Double.isNaN(b) ) {
	               parts.remove(ix-1);                   
	               parts.remove(ix-1);
	               parts.remove(ix-1);  
	               parts.add(ix-1,new Double(BinaryFunctions.calculate(fnOrder[i],a,b)));
	               ix--; 
               }           
             } else if ( parts.get(ix+1) instanceof Double
               && ((Separator)objx)._val == '-' ) { 
               double a = -1 ;
               double b = ((Double)parts.get(ix+1)).doubleValue();
               if ( !Double.isNaN(a) && !Double.isNaN(b) ) {
	               parts.remove(ix);                   
	               parts.remove(ix);
	               parts.add(ix,new Double(BinaryFunctions.calculate("*",a,b)));
               }       
             } 
           }         
         } else if ( objx instanceof Equation ) {
           Equation eqx = (Equation)objx;
           List l = Equation.calculate(eqx._parts,variableMap);
         	 if ( l.size() == 1 ) {
             parts.remove(ix);
             if ( eqx.getFunctionName() != null ) {
               double result = UnaryFunctions.calculate( eqx.getFunctionName(),
                 ((Double)eqx.getList().get(0)).doubleValue() );
               parts.add(ix,new Double(result));
             } else {
               parts.add(ix,l.get(0));
             }
                          
         	 }
         } 
       }
     }
     return parts;
   }

}
