package forms;


import exceptions.WrongValuesException;
import model.AstartesCategory;
import model.Chapter;
import model.Coordinates;
import model.SpaceMarine;

import java.time.ZonedDateTime;
import java.util.Scanner;

public class SpaceMarineBuild implements Build {
    private static int id = 0;
    private Scanner scanner;
    private CoordinatesBuild coordinatesBuild;
    private ChapterBuild chapterBuild;


    public SpaceMarineBuild(Scanner scanner) {
        this.scanner = scanner;
        coordinatesBuild = new CoordinatesBuild(scanner);
        chapterBuild = new ChapterBuild(scanner);
    }

    public SpaceMarineBuild() {
    }

    public SpaceMarine build() {
        return new SpaceMarine(1, buildName(), buildCoordinates(), buildZonedDateTime(), buildHealth(), buildHeartCount(), buildAchievements(), buildAstartesCategory(), buildChapter());
    }

    public String buildName() {
        while (true) {
            String name;
            System.out.println("Введите имя десантника (Поле не может быть null, Строка не может быть пустой)");
            name = scanner.nextLine().trim();
            try {
                SpaceMarine.SpaceMarineValidation.validateName(name);
                return name;
            } catch (WrongValuesException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Coordinates buildCoordinates() {
        return coordinatesBuild.build();
    }

    public ZonedDateTime buildZonedDateTime() {
        return ZonedDateTime.now();
    }

    public Integer buildHealth() {
        while (true) {
            String health;
            Integer h;
            System.out.println("Введите количество здоровья (Поле может быть null, Значение поля должно быть больше 0)");
            health = scanner.nextLine().trim();
            try {
                if (health.equals("")) {
                    return null;
                } else {
                    h = Integer.parseInt(health);
                    SpaceMarine.SpaceMarineValidation.validateHealth(h);
                    return Integer.parseInt(health);
                }
            } catch (NumberFormatException e) {
                System.out.println("Поле health должно быть типа Integer");
            } catch (WrongValuesException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public int buildHeartCount() {
        while (true) {
            String heartCount;
            int hrt;
            System.out.println("Введите количество сердец (Значение поля должно быть больше 0, Максимальное значение поля: 3)");
            heartCount = scanner.nextLine().trim();
            try {
                hrt = Integer.parseInt(heartCount);
                SpaceMarine.SpaceMarineValidation.validateHeartCount(hrt);
                return Integer.parseInt(heartCount);
            } catch (NumberFormatException e) {
                System.out.println("Поле heart_count должно быть типа int");
            } catch (WrongValuesException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String buildAchievements() {
        while (true) {
            String achievements;
            System.out.println("Введите достижения (Поле не может быть null)");
            achievements = scanner.nextLine().trim();
            try {
                SpaceMarine.SpaceMarineValidation.validateAchievements(achievements);
                return achievements;
            } catch (WrongValuesException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public AstartesCategory buildAstartesCategory() {
        while (true) {
            String astartesCategory;
            System.out.println("Список всех категорий:");
            System.out.println("1)" + AstartesCategory.APOTHECARY.toString() + "\n2)" + AstartesCategory.ASSAULT.toString() + "\n3)" + AstartesCategory.SCOUT.toString() + "\n4)" + AstartesCategory.SUPPRESSOR.toString());
            System.out.println("Введите категорию десантника из предложенного списка (Поле может быть null)");
            astartesCategory = scanner.nextLine().trim().toLowerCase();
            try {
                return SpaceMarine.SpaceMarineValidation.createValidateAstartresCategory(astartesCategory);
            } catch (WrongValuesException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Chapter buildChapter() {
        return new Chapter(chapterBuild.buildName(), chapterBuild.buildMarinesCount());
    }
}
