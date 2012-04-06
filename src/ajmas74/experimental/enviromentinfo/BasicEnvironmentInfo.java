package ajmas74.experimental.enviromentinfo;

public class BasicEnvironmentInfo implements EnvironmentInfo {

	
	private static void getJavaInfo( StringBuilder strBuilder ) {
		append(strBuilder,"Java Vendor", System.getProperty("java.vendor"));
		append(strBuilder,"Java Version", System.getProperty("java.version"));
//		append(strBuilder,"Class path", System.getProperty("java.class.path"));
	}
	
	private static void getSystemInfo( StringBuilder strBuilder ) {
		append(strBuilder,"OS Name", System.getProperty("os.name"));
		append(strBuilder,"OS Version", System.getProperty("os.version"));
		append(strBuilder,"Architecture", System.getProperty("os.arch"));
	}
			
	private static void append ( StringBuilder strBuilder, String feature, Object value ) {
		strBuilder.append(feature + ": " + value + "\n");
	}
	
	public String getEnvironmentInfo() {
		StringBuilder strBuilder = new StringBuilder();
		
		getJavaInfo ( strBuilder );
		strBuilder.append('\n');
		getSystemInfo ( strBuilder );
		
		
		// TODO Auto-generated method stub
		return strBuilder.toString();
	}

}
