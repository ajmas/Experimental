package ajmas74.experimental;

import java.io.*;

/**
 * BrainStormer is a simple java application that generates
 * random word pairs, using the Unix 'words' file for words
 * source. Hopefully the absurd combination of certain words
 * will create a spark in someone's mind for creating some
 * great idea.
 * 
 * @author Andre-John Mas - ajmas
 */
public class BrainStormer {

	public static final String WORDS_LIST = "datafiles/words";
	
	private long _fileLength;
	private RandomAccessFile _wordsFile;
	
	
	public BrainStormer () 
	  throws java.io.IOException {
		_wordsFile = new RandomAccessFile(WORDS_LIST,"r");
		_fileLength = _wordsFile.length();
	}
	
	public String createWordPair()  throws java.io.IOException {
		return getRandomWord() + "-" + getRandomWord();
	}
	
	public String createWordTuple()  throws java.io.IOException {
		return getRandomWord() + "-" + getRandomWord()
		 + "-" + getRandomWord();
	}	
	
	private String getRandomWord()
	  throws java.io.IOException {
		long idx = (long) (Math.random() * _fileLength);
		_wordsFile.seek(idx);
		while ( _wordsFile.read() != ((int)'\n') ) {
		}
		return _wordsFile.readLine();
	}
	
	public static void main(String[] args) {
		try {
			BrainStormer brainStormer = new BrainStormer();
			System.out.println(brainStormer.createWordPair());
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}
}
