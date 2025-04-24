package commands;


import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.sql.SQLException;

public class RemoveFirstCommand extends AbstractCommand {
    public RemoveFirstCommand(Supervisor supervisor) {
        super("remove_first", "Удалить первый элемент из коллекции", supervisor);
    }

    @Override
    public Response execute(Request request) {
        if (!supervisor.rightsCheck(supervisor.getCollection().getFirst().getId(), request.getUser().getLogin())) {
            return new Response(ResponseStatus.ERROR, "У вас недостаточно прав, чтобы удалить этот элемент!");
        }
        try {
            if (supervisor.getCollection().isEmpty()) {
                return new Response(ResponseStatus.OK, "Коллекция пуста - нельзя ничего удалить!");
            } else {
                if (supervisor.getDatabaseManager().removeObject(supervisor.getCollection().getFirst().getId(), request.getUser())) {
                    supervisor.getDatabase().removeFirstItem();
                    return new Response(ResponseStatus.OK, "Первый элемент коллекции успешно удалён!");
                } else return new Response(ResponseStatus.ERROR, "Элемент не получилось удалить!");
            }
        } catch (SQLException e) {
            return new Response(ResponseStatus.ERROR, "Возникла ошибка при обращении к базе данных!");
        }
    }
}
