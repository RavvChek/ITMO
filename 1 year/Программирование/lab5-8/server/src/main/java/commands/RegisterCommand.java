package commands;

import exceptions.WrongArgumentException;
import exceptions.WrongValuesException;
import exceptions.UserExistException;
import supervisor.DatabaseManager;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.sql.SQLException;

public class RegisterCommand extends AbstractCommand {
    private DatabaseManager databaseManager;

    public RegisterCommand(Supervisor supervisor, DatabaseManager databaseManager) {
        super("register", "Регистрация пользователя в приложении", supervisor);
        this.databaseManager = databaseManager;
    }

    @Override
    public Response execute(Request request) throws WrongValuesException, WrongArgumentException {
        try {
            databaseManager.addUser(request.getUser());
        } catch (SQLException e) {
            return new Response(ResponseStatus.ERROR, "Произошла ошибка при обращении к базе данных!");
        } catch (UserExistException e) {
            return new Response(ResponseStatus.AUTH_ERROR, "Такой пользователь уже существует.");
        }
        return new Response(ResponseStatus.OK, "Пользователь успешно добавлен!");
    }
}
