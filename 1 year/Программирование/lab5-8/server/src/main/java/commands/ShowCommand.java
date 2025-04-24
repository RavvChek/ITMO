package commands;


import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

public class ShowCommand extends AbstractCommand {
    public ShowCommand(Supervisor supervisor) {
        super("show", "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении", supervisor);
    }

    @Override
    public Response execute(Request request) {
        if (supervisor.getCollection().isEmpty()) {
            return new Response(ResponseStatus.OK, "Коллекция пуста!");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            this.supervisor.getCollection().forEach(SpaceMarine -> stringBuilder.append(SpaceMarine.toString()).append("\n"));
            return new Response(ResponseStatus.OK, stringBuilder.toString());
        }
    }
}
