package ajmas74.experimental;

import java.util.ArrayList;
import java.util.List;

public class LottoChecker {

    static class WinningNumbers {
        Integer[] numbers;
        int bonus;

        WinningNumbers(Integer[] numbers, int bonus) {
            this.numbers = numbers;
            this.bonus = bonus;
        }
    }

    static class SelectedNumbers {
        Integer[] numbers;
        Integer[] matches;
        boolean bonusMatch = false;
        boolean win = false;

        SelectedNumbers( String numbersStr) {
            String[] parts = numbersStr.split(" ");

            this.numbers = new Integer[parts.length];
            for ( int i=0; i<parts.length; i++ ) {
                numbers[i] = Integer.parseInt(parts[i]);
            }

        }
        
        SelectedNumbers(Integer[] numbers) {
            this.numbers = numbers;
        }
    }

    private static boolean contains(Integer[] list, int number) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] == number) {
                return true;
            }
        }
        return false;
    }

    public static List<SelectedNumbers> getWinningNumbers(WinningNumbers draw,
            List<SelectedNumbers> selectedNumbersList) {

        for (SelectedNumbers selection : selectedNumbersList) {

            List<Integer> matches = new ArrayList<Integer>();
            Integer[] numbers = selection.numbers;

            for (int i = 0; i < numbers.length; i++) {
                if (contains(draw.numbers, numbers[i])) {
                    matches.add(numbers[i]);
                }
            }

            selection.matches = matches.toArray(new Integer[0]);

            selection.bonusMatch = contains(numbers, draw.bonus);
        }

        return selectedNumbersList;
    }

    private static void displayArray(Object[] array) {
        // System.out.println( Arrays.toString(array) );
        for (Object obj : array) {
            System.out.print(obj + " ");
        }
    }

    public static void main(String[] args) {
        WinningNumbers winningNumbers = new WinningNumbers(
                new Integer[] { 8, 17, 21, 23, 24, 28, 39 }, 31);

        List<SelectedNumbers> selectedNumbersList = new ArrayList<SelectedNumbers>();

        SelectedNumbers selectedNumbers = null;

        selectedNumbersList.add( new SelectedNumbers("08 13 14 33 36 37 39") );
        selectedNumbersList.add( new SelectedNumbers("01 07 11 12 29 31 43") );
        selectedNumbersList.add( new SelectedNumbers("05 28 29 33 41 48 49") );

        selectedNumbersList.add( new SelectedNumbers("05 06 14 16 21 48 49") );
        selectedNumbersList.add( new SelectedNumbers("02 15 18 20 28 37 47") );
        selectedNumbersList.add( new SelectedNumbers("06 21 45 46 47 48 49") );

        selectedNumbersList = getWinningNumbers(winningNumbers, selectedNumbersList);

        System.out.print("Winning Numbers ..... : ");
        displayArray(winningNumbers.numbers);
        System.out.println();
        System.out.println("Bonus ............... : " + winningNumbers.bonus);
        System.out.println();

        for (SelectedNumbers selection : selectedNumbersList) {
            System.out.println("Selection ........... ");
            System.out.print("    matches ......... : ");
            displayArray(selection.matches);
            System.out.println();
            System.out.println("    match count ..... : " + selection.matches.length);

            String bonusMatchStr = "ignored";
            if (selection.matches.length == 3 || selection.matches.length == 6) {
                bonusMatchStr = (selection.bonusMatch ? "yes" : "no");
            }
            System.out.println("    bonus match ..... : " + bonusMatchStr);
            // for ( int number : selection.matches ) {
            // System.out.print( number + " " );
            // }
            System.out.println();
        }

    }
}
