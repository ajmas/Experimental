package ajmas74.experimental;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author ajmas
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ProcessOutputHandler implements Runnable {

  OutputStream _procOut;
	InputStream _in;
	boolean run = true;
	/**
	 * Constructor for ProcessOutputHandler.
	 */
	public ProcessOutputHandler( OutputStream procOut, InputStream subprocOut, int procID ) {
		_in = subprocOut;
    _procOut = procOut;
		(new Thread(this,"output.handler."+procID)).start();
	}
	
	public void close () {
		run = false;
	}
		
	public void run() {
		try {
			int i = 0;
			while ( run && (i=_in.read()) != -1 ) {
        _procOut.write(i);
			}
		} catch (IOException e) {
		  //e.printStackTrace(procOut.err);
		}
	}

}
