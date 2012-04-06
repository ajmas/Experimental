	package ajmas74.experimental;

public class ThreadLocalExpirements implements Runnable {

	int x = 0;
	String myStr;
	ThreadLocal<String> tlStrValue = new ThreadLocal<String>();
	
	ThreadLocalExpirements () {
		String word = "hello";
		tlStrValue.set(word);
		System.out.println( Thread.currentThread().getName() + " -- " + tlStrValue.get() );
		
		for ( int j=0; j<10; j++ ) {
			ThreadGroup threadGroup = new ExtendedThreadGroup("hello world -- " + j,Thread.currentThread().getThreadGroup(),"myGroup");
	
			for ( int i=0; i<10; i++ ) {
				create(threadGroup, word + ": " + x++);
			}
		}
	}
	
	ThreadLocalExpirements ( String word ) {
		myStr = word;
		tlStrValue.set(word);
		System.out.println( Thread.currentThread().getName() + " -- " + tlStrValue.get() );
		
//		for ( int i=0; i<300; i++ ) {
//			create(word + ": " + x++);
//		}
	}	
	
	synchronized Thread create( ThreadGroup threadGroup, String value ) {
		myStr = value;
		Thread thread = new Thread(threadGroup,new ThreadLocalExpirements(value));
		thread.start();
		return thread;
	}
	
	public void run() {
//		Thread.currentThread().getThreadGroup()
		ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		if ( threadGroup instanceof ExtendedThreadGroup ) {
			System.out.println( ((ExtendedThreadGroup)threadGroup).mySharedObjet );
		}
		
		tlStrValue.set(myStr);
		System.out.println( Thread.currentThread().getName() + " -- " + tlStrValue.get() );
	}
	
	// benefits of extending thread group?	
	class ExtendedThreadGroup  extends ThreadGroup {

		String mySharedObjet;
		
		public ExtendedThreadGroup(String value, ThreadGroup parent, String name) {
			super(parent, name);
			mySharedObjet = value;
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ThreadLocalExpirements();//"hello");
	}

}
