package ajmas74.experiments;

/**
 * @author andrmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ShutdownhookTest implements Runnable {
  
  ShutdownhookTest () {
    Runtime.getRuntime().addShutdownHook(new Thread(this));
  }
  
  public void run() {
    System.out.println("Shutdown hook called");
  }

	public static void main ( String[] args ) {
    ShutdownhookTest test = new ShutdownhookTest();
    //while (true) {}
	}
}
