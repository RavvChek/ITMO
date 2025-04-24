package forms;


import exceptions.WrongValuesException;
import model.Coordinates;

import java.util.Scanner;

public class CoordinatesBuild implements Build {
    private Scanner scanner;

    public CoordinatesBuild(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Coordinates build() {
        return new Coordinates(buildX(), buildY());
    }

    public float buildX() {
        while (true) {
            String x;
            float X;
            System.out.println("Введите координату X");
            x = scanner.nextLine().trim();
            try {
                if (x.equals("")) {
                    return Float.NaN;
                } else {
                    X = Float.parseFloat(x);
                    Coordinates.CoordinatesValidation.validateX(X);
                    return Float.parseFloat(x);
                }
            } catch (NumberFormatException e) {
                System.out.println("Поле x принимает значения типа float");
            } catch (WrongValuesException e) {
                System.out.println("Поле X не может быть таким большим");
            }
        }
    }

    public Double buildY() {
        while (true) {
            String y;
            Double Y;
            System.out.println("Введите координату Y (Значение поля должно быть больше -903, Поле не может быть null)");
            y = scanner.nextLine().trim();
            try {
                if (y.length() > 32) {
                    throw new NumberFormatException();
                }
                Y = Double.valueOf(y);
                Coordinates.CoordinatesValidation.validateY(Y);
                return Double.valueOf(y);
            } catch (NumberFormatException e) {
                System.out.println("Поле y принимает double значения (после запятой не может быть так много цифр)");
            } catch (WrongValuesException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
