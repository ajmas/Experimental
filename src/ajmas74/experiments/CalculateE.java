package ajmas74.experiments;

import java.math.BigDecimal;

/**
 * <p>
 * This class generates as long a value of the Euler e as
 * possible, with BigInterger. This class has two methods
 * for caclulating e. The first method, called basicCalulateE ().
 * illustrates the basic algorithm with no optimisations, either
 * for precision or speed. The second method, called
 * caclulateE(), is written for precision and speed.
 * </p>
 * <p>
 * I make this class available so it could be a reference
 * to anyone interested.
 * </p>
 * <p>
 * For information on the algorithm, see the notes section
 * at the following link:
 * http://www.2dcurves.com/exponential/exponentiale.html
 * </p>
 * <p>
 * The 'largest' value of e I have managed to create with this class is:
 * 2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613313845830007520449338265602976067371132007093287091274437470472306969772093101416928368190255151086574637721112523897844250569536967707854499699679468644549059879316368892300987931277361782154249992295763514822082698951936680331825288693984964651058209392398294887933203625094431173012381970684161403970198376793206832823764648042953118023287825098194558153017567173613320698112509961818815930416903515988885193458072738667385894228792284998920868058257492796104841984443634632449684875602336248270419786232090021609902353043699418491463140934317381436405462531520961836908887070167683964243781405927145635490613031072085103837505101157477041718986106873969655212671546889570350363
 * </p>
 * @author Andre-John Mas
 *
 */
public class CalculateE {

	static BigDecimal ZERO = new BigDecimal("0");
	static BigDecimal ONE = new BigDecimal("1");
		
	
// ---------------------------------------------------------------
//  This section shows the basic implementation of a solution
//  for calculating e, along with a straight forward implementation
//  of factorial.
// ---------------------------------------------------------------
	
	public static double factorial ( double n ) {		
		if (n == 0) return 1;
		else return n * factorial(n-1);
	}
	
	public static double basicCalculateE ( double x  ) {
		double n = 0;
		for ( int i=0; i<=x; i++ ) {
			n=n+(1/factorial(i));
		}
		return n;
	}
	
// ---------------------------------------------------------------
//  This next section caculates a large value of e, and also
//  is optimised to provide the best performance. Incremental
//  Factorial is implemented to improve factorial calculation
//  speed based on the current use.
// ---------------------------------------------------------------
	
	/**
	 * Version calculating e, using BigDecimal to allow for
	 * a much larger number
	 * 
	 * @param x the number of iterations
	 * @return
	 */
	public static BigDecimal calculateE ( double x  ) {
		IncrementalFactorial factoral = new IncrementalFactorial();
		BigDecimal n = ZERO;
		for ( int i=0; i<=x; i++ ) {
			BigDecimal m = ONE.divide(factoral.next(),1000,BigDecimal.ROUND_HALF_EVEN);
			n = n.add(m);
		}
		return n;
	}
	

	/** This version of factorial is designed to reduce procesing
	 *  time. This is used on this basis that each call to the method
	 *  increases the number index by one. Therefore instead of
	 *  recalculating factorial every time, we simply multiply
	 *  the previous total with the current index.
	 *   1=0, 1=1*1
	 *   2=1*2, 3=2*3
	 *   etc.
	 *   
	 */	
	static class IncrementalFactorial {
		static BigDecimal ONE = new BigDecimal("1");
		
		int idx = 0;
		BigDecimal total = ONE;
		
		public BigDecimal next() {
			if (idx== 0) {
				total = ONE;
			} else {
				total = total.multiply(new BigDecimal(idx));
			}
			idx++;
			return total;			
		}
	}
	
	public static void main(String[] args) {
		int maxIterations = 9000;
		System.out.println("1) Math.E,          e = " + Math.E);
		System.out.println("2) basicCalculateE, e = " + basicCalculateE(maxIterations));
		System.out.println("3) CalculateE,      e = " + calculateE(maxIterations));
	}
	

//
//	
//	public static double factorial2 ( double n ) {		
//		if (n == 0) return 1;
//		double x = 1;
//		for ( int i=1; i<=n; i++ ) {
//			x = x*i;
//		}
//		return x;
//	}
//
//	public static BigDecimal factorial3 ( double n ) {		
//		if (n == 0) return ONE;
//		BigDecimal x = ONE;
//		for ( int i=1; i<=n; i++ ) {
//			x = x.multiply(new BigDecimal(i));
//		}
//		return x;
//	}
//
//	
//
//	

//
//	public static BigDecimal calcE3 ( double x  ) {
//		BigDecimal n = ZERO;
//		for ( int i=0; i<=x; i++ ) {
//			BigDecimal m = ONE.divide(factorial3(i),1000,BigDecimal.ROUND_HALF_EVEN);
//			n = n.add(m);
//		}
//		return n;
//	}
//	
//
	
//	public static double calcE ( double x  ) {
//		x=x+1;
//		int s = 1;
//		double c = x;
//		double n = x;
//		for ( int i=0; i<20; i++ ) {
//			s = s * -1;
//			c = c * x; //(i+1);
//			//double m = Math.pow(x,(double)i+1)/(i+1);
//			//m = m * Math.pow( -1.0,(double)i+1);
//			double m = c/(i+1);
//			m = m * s;		
//			n = n + m; 				
//		}
//		return n;
//	}
	
//	public static boolean isPrime ( long value ) {
//		for ( int i=2;i<value;i++) {
//			if (value%i>0) {
//				return false;
//			}
//		}
//		return true;
//	}
//	
//	public static boolean isPrime ( String strValue ) {
//		if ( strValue.endsWith("5") || strValue.endsWith("2") ) {
//			return false;
//		}
//		return (new BigInteger(strValue)).isProbablePrime(1);
//	}
	
//	public static void main(String[] args) {
//		int maxIterations = 9000;
//		System.out.println(calculateE(maxIterations));
		
//		System.out.println(new BigDecimal("0.00002"));
//		//int n = 175;
//		int n = 9000;
//		System.out.println(factorial2(n));
//		System.out.println(factorial(n));
//		double x = n;
//		System.out.println(Math.E);
//		System.out.println("(1) e^"+x+"="+calcE(x));
//		System.out.println("(2) e^"+x+"="+calcE2(x));
//		//System.out.println("(3)  e^"+x+"="+calcE4(x));
//		System.out.println("(3)  e^"+x+"="+calcE3(x));
//		
//		String string = calcE3(x).toString();
//		int len =string.length()-9;
//		for ( int i=2; i<len;i++) {
//			String ss = string.substring(i,i+10);		
//			try {
//				//System.out.println( ss + " is prime: " + isPrime(ss));
//				if ( isPrime(ss) ) {
//					System.out.println("prime: " + ss);
//					break;
//				}
//			} catch (ArithmeticException e) {
//				System.err.println(ss);
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		String[] terms = new String[] { "7182818284","8182845904","8747135266","7427466391","5966290435" };
//		int lastTerm = 0;
//		for ( int i=0; i<terms.length; i++) {
//			int z = string.indexOf(terms[i]);
//			System.out.println(z + " .... " + string.lastIndexOf(terms[i]));
//			System.out.println("diff="+(z-lastTerm));
//			lastTerm = z;
//		}
//		
//		System.out.println("end");
//	}
	
}


/*
 * 2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613263028961408617242705801890461128905195179364092699994240171173017552375196454248278387177693865588812727423982508737988814063806433481873812365980820761906684044940022190376372525339749932050740026693760249182989462170230545837674447638133818239823979435051973292448130155739699456941352643197487747816203620672548210996786282040680052630286468563005789195272559142581343039695430053331834459341472126546663634242062062027434605816837256979628759842383501921812619589749785472564047931111957735658935950669218133869477921952235039165497843150195135261480715336914474159559635126539169930458681298981679278291527522168294313508163118713448836638802483914842960857381623271539054321
 *   1111111111
 *       2222222222
 *                         3333333333
 *                                                                            
 * 
 * 2.71828182845904523536028747135266249775724709369995957496696762772407663035354759457138217852516642742746639193200305992181741359662904357290033429526059563073813232862794349076323382988075319525101901157383418793070215408914993488416750924476146066808226480016847741185374234544243710753907774499206955170276183860626133138458300075204493382656029
 * 2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613313845830007520449338265602976067371132007093287091274437470472306969772093101416928368190255151086574637721112523897844250569536967707854499699679468644549059879316368892300987931277361782154249992295763514822082698951936680331825288693984964651058209392398294887933203625094431173012381970684161403970198376793206832823764648042953118023287825098194558153017567173613320698112509961818815930416903515988885193458072738667385894228792284998920868058257492796104841984443634632449684875602336248270419786232090021609902353043699418491463140934317381436405462531520961836908887070167683964243781405927145635490613031072085103837505101157477041718986106873969655212671546889570350363
   2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613313845830007520449338265602976067371132007093287091274437470472306969772093101416928368190255151086574637721112523897844250569536967707854499699679468644549059879316368892300987931277361782154249992295763514822082698951936680331825288693984964651058209392398294887933203625094431173012381970684161403970198376793206832823764648042953118023287825098194558153017567173613320698112509961818815930416903515988885193458072738667385894228792284998920868058257492796104841984443634632449684875602336248270419786232090021609902353043699418491463140934317381436405462531520961836908887070167683964243781405927145635490613031072085103837505101157477041718986106873969655212671546889570350363

   2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613313845830007520449338265602976067371132007093287091274437470472306969772093101416928368190255151086574637721112523897844250569536967707854499699679468644549059879316368892300987931277361782154249992295763514822082698951936680331825288693984964651058209392398294887933203625094431173012381970684161403970198376793206832823764648042953118023287825098194558153017567173613320698112509961818815930416903515988885193458072738667385894228792284998920868058257492796104841984443634632449684875602336248270419786232090021609902353043699418491463140934317381436405462531520961836908887070167683964243781405927145635490613031072085103837505101157477041718986106873969655212671546889570350363
   2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274274663919320030599218174135966290435729003342952605956307381323286279434907632338298807531952510190115738341879307021540891499348841675092447614606680822648001684774118537423454424371075390777449920695517027618386062613313845830007520449338265602976067371132007093287091274437470472306969772093101416928368190255151086574637721112523897844250569536967707854499699679468644549059879316368892300987931277361782154249992295763514822082698951936680331825288693984964651058209392398294887933203625094431173012381970684161403970198376793206832823764648042953118023287825098194558153017567173613320698112509961818815930416903515988885193458072738667385894228792284998920868058257492796104841984443634632449684875602336248270419786232090021609902353043699418491463140934317381436405462531520961836908887070167683964243781405927145635490613031072085103837505101157477041718986106873969655212671546889570350363

   
 * 
 */