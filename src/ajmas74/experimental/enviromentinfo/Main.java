package ajmas74.experimental.enviromentinfo;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println( new BasicEnvironmentInfo().getEnvironmentInfo() );		
		System.out.println( new JoglEnvironmentInfo().getEnvironmentInfo() );
		
	}

}
