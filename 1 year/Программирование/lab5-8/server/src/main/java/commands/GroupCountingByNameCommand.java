package commands;


import model.SpaceMarine;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.util.Map;
import java.util.stream.Collectors;

public class GroupCountingByNameCommand extends AbstractCommand {
    public GroupCountingByNameCommand(Supervisor supervisor) {
        super("group_counting_by_name", "Cгруппировать элементы коллекции по значению поля name, вывести количество элементов в каждой группе", supervisor);
    }

    @Override
    public Response execute(Request request) {
        Map<String, Long> groupCount = supervisor.getCollection().stream().collect(Collectors.groupingBy(SpaceMarine::getName, Collectors.counting()));
        StringBuilder stringBuilder = new StringBuilder();
        groupCount.forEach((name, count) -> stringBuilder.append(name).append(": ").append(count).append("\n"));
        return new Response(ResponseStatus.OK, stringBuilder.toString());
    }
}
