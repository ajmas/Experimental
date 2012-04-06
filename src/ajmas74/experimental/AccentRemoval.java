package ajmas74.experimental;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class AccentRemoval {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String name1 = "Heiðrún Sigurðardóttir";
		
		String name2 = Normalizer.normalize(name1, Normalizer.Form.NFD);
		
		StringBuilder strBuilder = new StringBuilder();
		
		for ( int i=0; i<name2.length(); i++ )
		{
			if ( Character.isLetter(name2.charAt(i)) )
			{
				strBuilder.append( name2.charAt(i)) ;
			}
		}
		
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        name2 = pattern.matcher(name2).replaceAll("");
		
		System.out.println( name2 );
	}

}
