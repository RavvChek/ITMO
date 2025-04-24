package commands;


import exceptions.WrongArgumentException;
import model.SpaceMarine;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.sql.SQLException;
import java.util.List;

public class ClearCommand extends AbstractCommand {

    public ClearCommand(Supervisor supervisor) {
        super("clear", "Очистить коллекцию", supervisor);
    }

    @Override
    public Response execute(Request request) throws WrongArgumentException {
        if (!request.getArgs().isBlank()) throw new WrongArgumentException("");
        try {
            List<Integer> deleteIds = supervisor.getCollection().stream()
                    .filter(spaceMarine -> spaceMarine.getOwner() == null || request.getUser().getLogin().equals(spaceMarine.getOwner()))
                    .map(SpaceMarine::getId)
                    .toList();
            if (deleteIds.isEmpty()) {
                return new Response(ResponseStatus.OK, "У пользователя пока нет своих объектов!");
            }
            if (supervisor.getDatabaseManager().removeAllObjects(deleteIds, request.getUser())) {
                supervisor.getDatabase().clearData(deleteIds);
                return new Response(ResponseStatus.OK, "Коллекция очищена!");
            } else return new Response(ResponseStatus.ERROR, "Не удалось удалить элементы из коллекции!");
        } catch (SQLException e) {
            return new Response(ResponseStatus.ERROR, "Возникла ошибка при взаимодействии с базой данных!");
        }
    }
}
