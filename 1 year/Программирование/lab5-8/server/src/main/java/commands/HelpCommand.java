package commands;


import exceptions.WrongArgumentException;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

public class HelpCommand extends AbstractCommand {
    public HelpCommand(Supervisor supervisor) {
        super("help", "Вывести справку по доступным командам", supervisor);
    }

    @Override
    public Response execute(Request request) throws WrongArgumentException {
        if (!request.getArgs().isBlank()) throw new WrongArgumentException("");
        String str = "help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "remove_first : удалить первый элемент из коллекции\n" +
                "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "average_of_heart_count : вывести среднее значение поля heartCount для всех элементов коллекции\n" +
                "min_by_id : вывести любой объект из коллекции, значение поля id которого является минимальным\n" +
                "group_counting_by_name : сгруппировать элементы коллекции по значению поля name, вывести количество элементов в каждой группе";
        return new Response(ResponseStatus.OK, str);
    }

    public void helpCommand(String commandName) {
        try {
            Command command = supervisor.getCommandByName(commandName);
            System.out.println(command.toString());
        } catch (NullPointerException e) {
            System.out.println("Название команды не найдено, впишите команду help, чтобы получить список всех команд");
        }
    }

    public void helpAll() {
        System.out.println("help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "remove_first : удалить первый элемент из коллекции\n" +
                "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "average_of_heart_count : вывести среднее значение поля heartCount для всех элементов коллекции\n" +
                "min_by_id : вывести любой объект из коллекции, значение поля id которого является минимальным\n" +
                "group_counting_by_name : сгруппировать элементы коллекции по значению поля name, вывести количество элементов в каждой группе");
    }
}
