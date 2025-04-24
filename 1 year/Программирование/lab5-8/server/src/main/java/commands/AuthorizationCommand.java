package commands;

import exceptions.WrongArgumentException;
import exceptions.WrongValuesException;
import exceptions.UserNotfoundException;
import supervisor.DatabaseManager;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;


import java.sql.SQLException;

public class AuthorizationCommand extends AbstractCommand {
    private DatabaseManager databaseManager;

    public AuthorizationCommand(Supervisor supervisor, DatabaseManager databaseManager) {
        super("authorization", "Авторизация пользователя в приложении", supervisor);
        this.databaseManager = databaseManager;
    }

    @Override
    public Response execute(Request request) throws WrongValuesException, WrongArgumentException {
        try {
            databaseManager.verifyUser(request.getUser());
        } catch (SQLException exception) {
            return new Response(ResponseStatus.ERROR, "Произошла ошибка при обращении к базе данных.");
        } catch (UserNotfoundException e) {
            return new Response(ResponseStatus.AUTH_ERROR, "Неправильные имя пользователя или пароль.");
        }
        return new Response(ResponseStatus.OK, "Пользователь авторизирован.");
    }
}
