package supervisor;


import exceptions.WrongArgumentException;
import exceptions.WrongCommandException;
import exceptions.WrongValuesException;
import model.SpaceMarine;
import commands.*;
import reader.DataReader;
import transfers.Request;
import transfers.Response;
import transfers.User;

import java.nio.file.Path;
import java.util.*;


public class Supervisor implements Supervising {
    private static Scanner scanner;
    private static List<Path> stack = new LinkedList<>();
    private final LinkedListCollection database;
    private final HashMap<String, Command> commandMap;
    private boolean active;
    private DatabaseManager databaseManager;
    private DatabaseHandler databaseHandler;


    public Supervisor(DatabaseHandler databaseHandler, DatabaseManager databaseManager) throws Exception {
        this.databaseHandler = databaseHandler;
        this.databaseManager = databaseManager;
        this.database = new LinkedListCollection(databaseManager);
        scanner = new Scanner(System.in);
        this.commandMap = new HashMap<>();
        commandMap.put("add", new AddCommand(this));
        commandMap.put("add_if_max", new AddIfMaxCommand(this));
        commandMap.put("add_if_min", new AddIfMinCommand(this));
        commandMap.put("average_of_heart_count", new AverageOfHeartCountCommand(this));
        commandMap.put("clear", new ClearCommand(this));
        commandMap.put("execute_script", new ExecuteScriptCommand(this));
        commandMap.put("exit", new ExitCommand(this));
        commandMap.put("group_counting_by_name", new GroupCountingByNameCommand(this));
        commandMap.put("help", new HelpCommand(this));
        commandMap.put("info", new InfoCommand(this));
        commandMap.put("min_by_id", new MinByIdCommand(this));
        commandMap.put("remove_by_id", new RemoveByIdCommand(this));
        commandMap.put("remove_first", new RemoveFirstCommand(this));
        commandMap.put("show", new ShowCommand(this));
        commandMap.put("update", new UpdateCommand(this));
        commandMap.put("register", new RegisterCommand(this, this.databaseManager));
        commandMap.put("login", new AuthorizationCommand(this, this.databaseManager));
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scan) {
        scanner = scan;
    }

    public LinkedList<SpaceMarine> getCollection() {
        return database.getData();
    }

    public LinkedListCollection getDatabase() {
        return database;
    }

    //@Override
    public void run() throws Exception {
        this.active = true;
        System.out.println("Добро пожаловать, Ярослав Кулинич!");
        while (active) {
            //waitCommand();
        }
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public HashMap<String, Command> getHashMapCommands() {
        return commandMap;
    }

    public Command getCommandByName(String name) {
        return commandMap.get(name);
    }

    @Override
    public void stop() {
        this.active = false;
    }


    public synchronized Response start(Request request) throws WrongCommandException, WrongValuesException, WrongArgumentException {
        Command command = commandMap.get(request.getCommandName());
        System.out.println(command);
        if (command == null) throw new WrongCommandException();
        return command.execute(request);
    }

    public boolean rightsCheck(int id, String login) {
        SpaceMarine sp = this.getDatabase().getById(id);
        if (!Objects.equals(sp.getOwner(), login)) {
            return false;
        }
        return true;
    }

    public boolean isExistID(Integer id) {
        for (SpaceMarine sp :
                database.getData()) {
            if (sp.getId() == id) {
                return true;
            }
        }
        return false;
    }
}