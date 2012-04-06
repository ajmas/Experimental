package ajmas74.experimental;

import java.util.Date;
import java.util.UUID;
import com.fasterxml.uuid.*;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

public class UuidExperiments {

	
	static void displayUuidDetails ( UUID uuid ) {
		System.out.println("version: .... " + uuid.version());
		System.out.println("variant: .... " + uuid.variant());
				
		try {
			System.out.print("node ........ ");
			System.out.print( uuid.node() );

		}
		catch ( Exception ex ) {
			System.out.print("error - Not a time-based UUID");
		}
		System.out.println();
		
		try {
			System.out.print("timestamp ... " );
			System.out.print( uuid.timestamp() + " (" + (new Date(uuid.timestamp())) + ")" );
		}
		catch ( Exception ex ) {
			System.out.print("error - Not a time-based UUID");
		}
		System.out.println();
		
		System.out.println();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		displayUuidDetails(UUID.randomUUID());

		
		TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();
		displayUuidDetails( timeBasedGenerator.generate() );
		
	}

}
