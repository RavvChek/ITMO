package commands;


import model.SpaceMarine;
import supervisor.Supervisor;

public abstract class  AbstractCommand implements Command {

    protected final Supervisor supervisor;
    protected final String name;
    protected final String description;

    AbstractCommand(String name, String description, Supervisor supervisor) {
        this.supervisor = supervisor;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }

    public boolean haveId(int id) {
        for (SpaceMarine sp : supervisor.getCollection()) {
            if (sp.getId() == id) {
                return true;
            }
        }
        return false;
    }
    public String getName(){
        return name;
    }
}
