package by.epamtc.stanislavmelnikov.view.output;

import java.util.List;

public class ConsoleOutput {
    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printLoginRequest() {
        System.out.println("Enter login: ");
    }

    public void printPasswordRequest() {
        System.out.println("Enter password: ");
    }

    public void printMenu(List<String> menu) {
        menu.forEach(System.out::println);
    }
}
