package commands;


import model.SpaceMarine;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.util.Comparator;
import java.util.Optional;

public class MinByIdCommand extends AbstractCommand {
    public MinByIdCommand(Supervisor supervisor) {
        super("min_by_id", "Вывести любой объект из коллекции, значение поля id которого является минимальным", supervisor);
    }

    @Override
    public Response execute(Request request) {
        if(supervisor.getCollection().isEmpty()){
            return new Response(ResponseStatus.OK, "Коллекция пуста - нет минимального элемента по id!");
        }
        else{
            Optional<SpaceMarine> minElement = supervisor.getCollection().stream()
                    .min(Comparator.comparing(SpaceMarine::getId));
            return new Response(ResponseStatus.OK, "Минимальный элемент по id коллекции: " + minElement.get());
        }

    }
}
