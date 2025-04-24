package commands;


import exceptions.WrongArgumentException;
import exceptions.WrongValuesException;
import model.SpaceMarine;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.sql.SQLException;
import java.util.Objects;

public class AddCommand extends AbstractCommand {
    public AddCommand(Supervisor supervisor) {
        super("add", "Добавить новый элемент в коллекцию", supervisor);
    }

    @Override
    public Response execute(Request request) throws WrongValuesException, WrongArgumentException {
        if (!request.getArgs().isBlank()) throw new WrongArgumentException("");
        try {
            if (Objects.isNull(request.getObject())) {
                return new Response(ResponseStatus.ERROR, "Для команды " + this.getName() + " требуется объект.");
            } else {
                request.getObject().setId(buildId());
                supervisor.getDatabase().addItem(supervisor.getDatabaseManager().addObject(request.getObject(), request.getUser()));
                return new Response(ResponseStatus.OK, "Элемент успешно добавлен");
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            return new Response(ResponseStatus.ERROR, "Произошла ошибка при обращении к базе данных");
        }

    }

    public int buildId() {
        int id = 0;
        boolean flag;
        while (true) {
            flag = true;
            id++;
            for (SpaceMarine sp : supervisor.getCollection()) {
                if (id == sp.getId()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return id;
            }
        }
    }
}

