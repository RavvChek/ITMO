import exceptions.RecursionScriptException;
import exceptions.ScriptErrorException;
import exceptions.WrongArgumentException;
import model.SpaceMarine;
import forms.SpaceMarineBuild;
import transfers.Request;
import transfers.ResponseStatus;
import transfers.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class Manager {

    private final Stack<File> scriptStack = new Stack<>();
    private final Stack<Scanner> scannerStack = new Stack<>();
    private Scanner userScanner;

    public Manager(Scanner userConsole) {
        this.userScanner = userConsole;
    }

    public Request handle(ResponseStatus serverResponseCode, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseStatus.ERROR ||
                            serverResponseCode == ResponseStatus.EXIT))
                        throw new ScriptErrorException();
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        scriptStack.pop();
                    }
                    if (fileMode()) {
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            System.out.println(userInput);
                        }
                    } else {
                        userInput = userScanner.nextLine();
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException e) {
                    System.out.println("Произошла ошибка при вводе команды.");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    int maxRewriteAttempts = 1;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        System.out.println("Превышено количество попыток ввода.");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                if (fileMode() && (serverResponseCode == ResponseStatus.ERROR || processingCode == ProcessingCode.ERROR))
                    throw new ScriptErrorException();
                switch (processingCode) {
                    case OBJECT, UPDATE_OBJECT -> {
                        SpaceMarineBuild spaceMarineBuild = new SpaceMarineBuild(userScanner);
                        SpaceMarine sp = spaceMarineBuild.build();
                        return new Request(userCommand[0], userCommand[1], sp, user);
                    }
                    case SCRIPT -> {
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new RecursionScriptException("Скрипт вызывается рекурсивно");
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        System.out.println("Скрипт '" + scriptFile.getName() + "'выполняется...");
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Файл со скриптом не найден");
            } catch (RecursionScriptException e) {
                System.out.println(e.getMessage());
                throw new ScriptErrorException();
            } catch (NumberFormatException e) {
            } catch (Exception e) {
                System.out.println("Выполнение команды прервано");
            }
        } catch (ScriptErrorException e) {
            System.out.println("Выполнение скрипта прервано");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request();
        }
        return new Request(userCommand[0], userCommand[1], user);
    }

    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            System.out.print(">>> ");
            switch (command) {
                case "" -> {
                    return ProcessingCode.ERROR;
                }
                case "help" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                case "info" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                case "show" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                case "add", "add_if_max", "add_if_min" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                    return ProcessingCode.OBJECT;
                }
                case "update" -> {
                    if (commandArgument.isEmpty() || Integer.parseInt(commandArgument) <= 0)
                        throw new WrongArgumentException("Номер элемента и элемент");
                    return ProcessingCode.UPDATE_OBJECT;
                }
                case "remove_by_id" -> {
                    if (commandArgument.isEmpty()) throw new WrongArgumentException("Номер элемента");
                }
                case "min_by_id" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                case "clear" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                case "group_counting_by_name" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                case "average_of_heart_count" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                case "remove_first" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                case "execute_script" -> {
                    if (commandArgument.isEmpty()) throw new WrongArgumentException("Название файла");
                    return ProcessingCode.SCRIPT;
                }
                case "exit" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                case "server_exit" -> {
                    if (!commandArgument.isEmpty()) throw new WrongArgumentException();
                }
                default -> {
                    System.out.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessingCode.ERROR;
                }
            }
        } catch (WrongArgumentException e) {
            if (e.getMessage() != null) command += " " + e.getMessage();
            System.out.println("использование: '" + command + "'");
            return ProcessingCode.ERROR;
        } catch (NumberFormatException e) {
            System.out.println("Формат аргумента не соответствует целочисленному");
        }
        return ProcessingCode.OK;
    }


    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }

    public Scanner getUserScanner() {
        return userScanner;
    }
}