package ajmas74.experimental;

public class Maths {

  static class Point3D {
    
    double x,y,z;
    
    Point3D ( double x, double y, double z ) {
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }
  
  static class Line3D {
    Point3D p1,p2;
    
    Line3D ( Point3D p1, Point3D p2 ) {
      this.p1 = p1;
      this.p2 = p2;
    }
  }
  
  static class Angle3D {
    double horizontal;
    double vertical;

  }
  
  public static void lineAngle ( Line3D line ) {
    Point3D p1 = line.p1;
    Point3D p2 = line.p2;
    
    double x = p2.x - p1.x;
    double y = p2.y - p1.y;
    double z = p2.z - p1.z;
    
    double n = Math.sqrt(x*x+y*y+z*z);
    
    if(n==0.0) {
      System.out.println("0.0");
      return ;
    }
    x/=n;
    y/=n;
    z/=n;

    double nzx=Math.sqrt(x*x+z*z);
    double rx=Math.asin(y);
    double ry=0;
    if(nzx!=0.0) {
      ry=-Math.acos(z/nzx);
    } else {
      ry=0;
    }

    if(x<0) {
      ry=-ry;
    }

    System.out.println("rx="+Math.toDegrees(rx));
    System.out.println("ry="+Math.toDegrees(ry));    
  }
  
  public static double sin ( double x ) {
    int sign = 1;
    if ( x < 0 ) {
      sign = -1;
      x = -x;
    }
    double pim2 = Math.PI * 2;
    x = x % pim2;
    double pid2 = Math.PI / 2; 
    if ( x > Math.PI ) {
      x = x - Math.PI;
      sign = -sign;
    }
    if ( x > pid2 ) {
      x = Math.PI - x;
    }
    
    double term = x;
    double xsup2 = x * x;
    double sum = x;

    for ( double j=3;;j+=2) {
    
      term = -term * xsup2 /(j*(j-1));
      double newSum = sum + term;
      if ( newSum == sum ) {
        return sign * sum;
      }
      sum = newSum;
    }
  }
  
  
  public static double cos ( double x ) {
    int sign = 1;
    x = Math.abs(x);
    if ( x < 0 ) {
      sign = -1;
      x = -x;
    }
    double pim2 = Math.PI * 2;
    x = x % pim2;
    double pid2 = Math.PI / 2; 
    if ( x > Math.PI ) {
      x = x - Math.PI;
      sign = -sign;
    }
    if ( x > pid2 ) {
      x = Math.PI - x;
    }
    
    double term = 1;
    double xsup2 = x * x;
    double sum = 1;

    for ( double j=2;;j+=2) {
    
      term = -term * xsup2 /(j*(j-1));
      double newSum = sum + term;
      if ( newSum == sum ) {
        return sign * sum;
      }
      sum = newSum;
    }
  }
  
  /**
   * Rounds the value up to its nearest power of 2.
   * @param value the value to round up.
   * @return returns a value wich is greater or equal to value and is a power of 2.
   */
  public static int roundUpPow2(int value) {
      int result = 1;
      while (result < value) { 
          result *= 2;
      }
      
      return result;  
  }
  
  private static int nextPowerOf2 ( int value ) {
    double powerOf2d = 0.0d;
    int powerOf2i = 0;
    
    powerOf2d =  Math.log((double)value) / Math.log(2.0);
    
    powerOf2i = (int) powerOf2d;
    if ( powerOf2d % 1 > 0 ) {
      powerOf2i++;
    }
      
    int size = 1 << powerOf2i;
    
    return size;
  }
  
  private static int nextPowerOf2 ( int value, int maxValue ) {
    double powerOf2d = 0.0d;
    int powerOf2i = 0;
    
    if (value <= maxValue) {
      powerOf2d =  Math.log((double)value) / Math.log(2.0);
    } else {
      powerOf2d = Math.log((double)maxValue) / Math.log(2.0);
    }
    
    powerOf2i = (int) powerOf2d;
    if ( powerOf2d % 1 > 0 ) {
      powerOf2i++;
    }
      
    int size = 1 << powerOf2i;//powerOf2;
    
    return size;
  }

  public static void main2 ( String[] args ) {
    System.out.println(Maths.sin(0.5));
    System.out.println(sin(0.5));
    System.out.println("------------");
    System.out.println(Maths.cos(-1.0));
    System.out.println(cos(-1.0));   
    System.out.println("------------");
    
    lineAngle(new Line3D(new Point3D(1,1,1),new Point3D(1,-5,1)));
  }
  
  public static void main ( String[] args ) {
    long t = 0;
    t =System.currentTimeMillis();
    for ( int i=0; i<50000;i++) {
      nextPowerOf2(340);
    }
    System.out.println(""+(System.currentTimeMillis()-t));
    System.out.println(nextPowerOf2(340));

    
    t =System.currentTimeMillis();
    for ( int i=0; i<50000;i++) {
      roundUpPow2(340);
    }
    System.out.println(""+(System.currentTimeMillis()-t));    
    System.out.println(roundUpPow2(340));

  }
}
