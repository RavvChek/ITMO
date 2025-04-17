package org.example;

import org.example.solver.Solver;
import org.example.utils.CsvPlotter;

import java.io.IOException;
import java.util.Scanner;

public class Manager {
    private final Scanner scanner = new Scanner(System.in);
    private final String PATH = "src/main/resources/";

    public void run() throws IOException {
/*
        System.out.println("Добро пожаловать!");
        System.out.println("Укажите стартовое значение X");
        Solver.setStart(scanner.nextDouble());
        System.out.println("Укажите конечное значение X");
        Solver.setEnd(scanner.nextDouble());
        System.out.println("Укажите шаг step");
        Solver.setStep(scanner.nextDouble());
        System.out.println("Укажите точность epsilon");
        Solver.setEpsilon(scanner.nextDouble());
        System.out.println("Укажите разделитель для csv файла");
        char delimiter = scanner.next().0
        Solver.setDelimiter(scanner.nextLine().charAt(0));
        char delimiter = Solver.getDelimiter();
        System.out.println("Выберите выражение: sin, cos, csc, sec, cot, tan, ln, log2, log3, log5");
        String func = scanner.next();
*/
//        Solver.runFunc(PATH + "sin.csv", "sin", -5, 0, 0.01, 0.001, ';');
//        Solver.runFunc(PATH + "tan.csv", "tan", -1.5, 0, 0.01, 0.001, ';');
//        Solver.runFunc(PATH + "cos.csv", "cos", -3.14, 0, 0.01, 0.001, ';');
//        Solver.runFunc(PATH + "csc.csv", "csc", -4, 0, 0.1, 0.001, ';');
//        Solver.runFunc(PATH + "ln.csv", "ln", 1.1, 5, 0.1, 0.001, ';');
//        Solver.runFunc(PATH + "log2.csv", "log2", 1.1, 5, 0.1, 0.001, ';');
//        Solver.runFunc(PATH + "log3.csv", "log3", 1.1, 5, 0.1, 0.001, ';');
//        Solver.runFunc(PATH + "log5.csv", "log5", 1.1, 5, 0.1, 0.001, ';');
//        Solver.runFunc(PATH + "cot.csv", "cot", -1.5, 0, 0.01, 0.001, ';');
//        Solver.runFunc(PATH + "sec.csv", "sec", -3.14, 0, 0.05, 0.001, ';');
        Solver.runFunc(PATH + "system.csv", "system", 0.1, 10, 0.001, 0.0001, ';');

        CsvPlotter csvPlotter = new CsvPlotter(PATH + "system.csv");
        csvPlotter.setVisible(true);
    }
}
