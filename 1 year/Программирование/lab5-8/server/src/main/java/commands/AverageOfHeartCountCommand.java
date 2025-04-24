package commands;


import model.SpaceMarine;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

public class AverageOfHeartCountCommand extends AbstractCommand {
    public AverageOfHeartCountCommand(Supervisor supervisor) {
        super("average_of_heart_count", "Вывести среднее значение поля heartCount для всех элементов коллекции", supervisor);
    }

    @Override
    public Response execute(Request request) {
        double avg =  supervisor.getCollection().stream().mapToInt(SpaceMarine::getHeartCount).average().orElse(Double.NaN);
        return new Response(ResponseStatus.OK, "Среднее значение поля heartCount для всех элементов коллекции: " + avg);
    }
}
