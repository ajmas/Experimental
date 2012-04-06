package ajmas74.experimental.graphingcalc;

/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Separator {
   static int LEFT = 1;
   static int RIGHT = 2;
   static int GENERAL = 3;
 
   char _val;
   int _type;
 
   Separator( int type, char val ) {
     _type = type;
     _val = val;  
   }
 
   public String toString () {
     //return "Separator:["+_type+","+_val+"]";
   return _val+"";
   }
 }
