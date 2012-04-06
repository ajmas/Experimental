/*
 * Created on Mar 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ajmas74.experiments;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ajmas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class LotteryChecker {
	/**
	 * 
	 */
	public LotteryChecker() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private MatchDetails analyseMatch( WinningNumbers winningNumbers, int[] sequence ) {
		MatchDetails matchDetails = new MatchDetails();
		matchDetails.setNumbers(sequence);
		List details = new ArrayList();
		int[] numbers = winningNumbers.getNumbers();
		for ( int j=0; j<sequence.length; j++ ) {		
			for ( int i=0; i<numbers.length; i++ ) {
				if ( numbers[i] == sequence[j] ) {
					details.add(new Integer(numbers[i]));
					break;
				}
			}
			if ( sequence[j] == winningNumbers.getBonusNumber()) {
				matchDetails.setBonusMatch(true);
			}			
		}
		numbers = new int[details.size()];
		for ( int i=0; i<numbers.length; i++ ) {
			numbers[i] = ((Integer)details.get(i)).intValue();
		}
		matchDetails.setMatches(numbers);
		return matchDetails;
	}
	
	
	public static void main(String[] args) {
		WinningNumbers wn = new WinningNumbers();
		wn.setNumbers(new int[] { 12, 17, 25, 26, 43, 44 });
		wn.setBonusNumber(2);
		
		LotteryChecker lotteryChecker = new LotteryChecker();
		System.out.println(lotteryChecker.analyseMatch(wn,new int[] { 33, 44, 22, 11, 34, 8, 32} ));
		System.out.println(lotteryChecker.analyseMatch(wn,new int[] { 33, 44, 22, 11, 34, 8, 1} ));
		System.out.println(lotteryChecker.analyseMatch(wn,new int[] { 2, 44, 22, 11, 34, 8, 1} ));
		System.out.println(lotteryChecker.analyseMatch(wn,new int[] { 2, 44, 4, 11, 34, 8, 1} ));

		System.out.println(lotteryChecker.analyseMatch(wn,new int[] { 1, 5, 32, 35, 36, 49 } ));
		System.out.println(lotteryChecker.analyseMatch(wn,new int[] { 1, 9, 12, 15, 16, 45,} ));
	}
	
	// --------------------------------------------
	// --------------------------------------------
	
	static class  WinningNumbers {
		int[] _numbers;
		int   _bonusNumber;
		
		/**
		 * @return Returns the bonusNumber.
		 */
		public int getBonusNumber() {
			return _bonusNumber;
		}
		/**
		 * @param bonusNumber The bonusNumber to set.
		 */
		public void setBonusNumber(int bonusNumber) {
			_bonusNumber = bonusNumber;
		}
		/**
		 * @return Returns the numbers.
		 */
		public int[] getNumbers() {
			return _numbers;
		}
		/**
		 * @param numbers The numbers to set.
		 */
		public void setNumbers(int[] numbers) {
			_numbers = numbers;
		}
	}
	
	static class MatchDetails {
		int[]   _numbers;
		int[]   _matches;
		boolean _bonusMatch;
		/**
		 * @return Returns the matches.
		 */
		public int[] getMatches() {
			return _matches;
		}
		/**
		 * @param matches The matches to set.
		 */
		public void setMatches(int[] matches) {
			_matches = matches;
		}
		/**
		 * @return Returns the numbers.
		 */
		public int[] getNumbers() {
			return _numbers;
		}
		/**
		 * @param numbers The numbers to set.
		 */
		public void setNumbers(int[] numbers) {
			_numbers = numbers;
		}
		/**
		 * @return Returns the bonusMatch.
		 */
		public boolean isBonusMatch() {
			return _bonusMatch;
		}
		/**
		 * @param bonusMatch The bonusMatch to set.
		 */
		public void setBonusMatch(boolean bonusMatch) {
			_bonusMatch = bonusMatch;
		}
		
		public String toString() {
			StringBuffer strBuf = new StringBuffer();
			if ( _matches != null ) {
				for ( int i=0; i<_matches.length; i++ ) {
					strBuf.append(_matches[i]);
					strBuf.append(',');
				}
			}
			if ( _bonusMatch ) {
				strBuf.append("bonus-match");
			}
			return strBuf.toString();
		}
	}
	
	
}
