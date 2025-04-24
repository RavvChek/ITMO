package commands;


import exceptions.WrongValuesException;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.sql.SQLException;

public class RemoveByIdCommand extends AbstractCommand {
    public RemoveByIdCommand(Supervisor supervisor) {
        super("remove", "Удалить элемент из коллекции по его id", supervisor);
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
            return new Response(ResponseStatus.ERROR, "У вас недостаточно прав, чтобы удалить этот элемент!");
        }
        try {
            if (supervisor.getCollection().isEmpty()) {
                return new Response(ResponseStatus.OK, "Коллекция пуста - нельзя ничего удалить!");
            } else {
                if (supervisor.getDatabaseManager().removeObject(Integer.parseInt(request.getArgs()), request.getUser())) {
                    supervisor.getDatabase().removeByIdItem(Integer.parseInt(request.getArgs()));
                    return new Response(ResponseStatus.OK, "Элемент коллекции с id - " + request.getArgs() + " успешно удалён!");
                } else return new Response(ResponseStatus.ERROR, "Элемент не получилось удалить!");
            }
        } catch (SQLException e) {
            return new Response(ResponseStatus.ERROR, "Возникла ошибка при обращении к базе данных!");
        }
    }
}
