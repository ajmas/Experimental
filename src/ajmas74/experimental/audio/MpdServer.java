package ajmas74.experimental.audio;

public class MpdServer {

	
	static class MpdCommand {
		String commandName;
		String[] parameterNames;
		
		public boolean invoke ( MpdCommand command, String[] parameters ) {
			return false;
		}
		
		public String toString() {
			return null;
		}
	}
}
