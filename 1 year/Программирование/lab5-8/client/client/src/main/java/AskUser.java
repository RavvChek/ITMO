import forms.Build;
import transfers.User;

import java.util.Scanner;

public class AskUser implements Build {
    private final Scanner scanner;

    AskUser(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public User build() {
        return new User(askLogin(), askPassword());
    }

    public boolean askAuth(String question) {
        String finalQuestion = question + " (y/n):";
        String answer;
        for (; ; ) {
            System.out.println(finalQuestion);
            answer = scanner.nextLine().trim();
            if (answer.equals("y")) return true;
            else if (answer.equals("n")) return false;
            else System.out.println("Ответ должен быть y или n");
        }
    }

    public String askLogin() {
        String login;
        System.out.println("Введите логин: ");
        while (true) {
            login = scanner.nextLine().trim();
            if (login.isEmpty()) {
                System.out.println("Логин не может быть пустым!!!");
            } else {
                return login;
            }
        }
    }

    public String askPassword() {
        String password;
        System.out.println("Введите пароль: ");
        while (true) {
            password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Пароль не может быть пустым!!!");
            } else {
                return password;
            }
        }
    }

}
