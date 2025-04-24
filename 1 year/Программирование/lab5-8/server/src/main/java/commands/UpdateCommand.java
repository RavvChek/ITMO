package commands;

import exceptions.WrongValuesException;
import model.SpaceMarine;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.sql.SQLException;
import java.util.Optional;

public class UpdateCommand extends AbstractCommand {
    public UpdateCommand(Supervisor supervisor) {
        super("update", "Обновить значение элемента коллекции, id которого равен заданному", supervisor);
    }

    @Override
    public Response execute(Request request) throws WrongValuesException {
        if (!request.getArgs().matches("-?\\d+")) {
            return new Response(ResponseStatus.ERROR, "id должен быть целочисленным!");
        }
        if (!supervisor.isExistID(Integer.parseInt(request.getArgs()))) {
            return new Response(ResponseStatus.OK, "В коллекции нет объекта с таким ID!");
        }
        if (!supervisor.rightsCheck(Integer.parseInt(request.getArgs()), request.getUser().getLogin())) {
            return new Response(ResponseStatus.ERROR, "У вас недостаточно прав, чтобы обновить этот элемент!");
        }
        Optional<SpaceMarine> spaceMarine = supervisor.getCollection().stream()
                .filter(p -> p.getId() == Integer.parseInt(request.getArgs()))
                .findFirst();
        SpaceMarine sp =  supervisor.getCollection().get(Integer.parseInt(request.getArgs()));
        try {
            if (supervisor.getCollection().isEmpty()) {
                return new Response(ResponseStatus.OK, "Коллекция пуста - нечего обновлять!");
            } else if (spaceMarine.isEmpty()) {
                return new Response(ResponseStatus.OK, "Нет элемента с таким id!");
            } else {
                if (supervisor.getDatabaseManager().updateObject(Integer.parseInt(request.getArgs()), spaceMarine.get(), request.getUser()))
                    supervisor.getDatabase().updateItem(Integer.parseInt(request.getArgs()), request.getObject());
                return new Response(ResponseStatus.OK, "Элемент с id " + request.getArgs() + " успешно обновлён!");
            }
        } catch (SQLException e) {
            return new Response(ResponseStatus.ERROR, "Возникла ошибка при обращении к базе данных!");
        }

    }
}
