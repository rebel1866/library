package by.epamtc.stanislavmelnikov.view.input;

import java.util.Scanner;

public class DataScanner {
    public String inputString() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }

    public String inputMultipleStrings() {
        StringBuilder result = new StringBuilder();
        for (; ; ) {
            String input = inputString();
            if (input.equalsIgnoreCase("E")) break;
            result.append("~" + input);
        }
        return result.toString();
    }
}
