package supervisor;

import exceptions.WrongArgumentException;
import exceptions.WrongCommandException;
import exceptions.WrongValuesException;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.util.Collections;

public class RequestHandler {
    private Supervisor supervisor;

    public RequestHandler(Supervisor commandManager) {
        this.supervisor = commandManager;
    }

    public Response handle(Request request) {
        try {
            Collections.sort(supervisor.getCollection());
            return supervisor.start(request);
        } catch (WrongArgumentException e) {
            return new Response(ResponseStatus.ERROR, "Введены неправильные аргументы команды.");
        } catch (WrongValuesException e) {
            return new Response(ResponseStatus.ERROR, "Ошибка при исполнении программы");
        } catch (WrongCommandException e) {
            return new Response(ResponseStatus.ERROR, "Такой команды не существует.");
        }
    }
}