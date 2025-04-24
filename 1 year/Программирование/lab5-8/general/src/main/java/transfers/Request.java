package transfers;

import model.SpaceMarine;

import java.io.Serializable;
import java.util.Objects;

public class Request implements Serializable {
    private String commandName;
    private String args = "";
    private SpaceMarine object = null;
    private User user;

    public Request() {

    }

    public Request(ResponseStatus OK, String commandName, SpaceMarine help) {
        this.commandName = commandName.trim();
    }

    public Request(String commandName, String args) {
        this.commandName = commandName.trim();
        this.args = args;
    }

    public Request(String commandName, SpaceMarine object) {
        this.commandName = commandName.trim();
        this.object = object;
    }

    public Request(String commandName, String args, SpaceMarine object) {
        this.commandName = commandName.trim();
        this.args = args.trim();
        this.object = object;
    }

    public Request(String commandName, String args, User user) {
        this.commandName = commandName.trim();
        this.args = args;
        this.user = user;
    }

    public Request(String commandName, String args, SpaceMarine object, User user) {
        this.commandName = commandName.trim();
        this.args = args.trim();
        this.object = object;
        this.user = user;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArgs() {
        return args;
    }

    public SpaceMarine getObject() {
        return object;
    }

    public User getUser() {
        return user;
    }

    public boolean isEmpty() {
        if (commandName == null || args == null) {
            return true;
        } else {
            return commandName.isEmpty() && args.isEmpty() && object == null;
        }
    }

    @Override
    public String toString() {
        return "Request: " + "\n" +
                "commandName = " + commandName + "\n" +
                "args = " + (args.isEmpty()
                ? ""
                : args) + "\n" +
                "object = " + ((object == null)
                ? ""
                : object) + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandName, args, object);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Request request)) return false;
        return Objects.equals(commandName, request.commandName) && Objects.equals(args, request.args) && Objects.equals(object, request.object);
    }
}