package commands;


import exceptions.WrongArgumentException;
import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

public class InfoCommand extends AbstractCommand {
    public InfoCommand(Supervisor supervisor) {
        super("info", "Вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)", supervisor);
    }

    @Override
    public Response execute(Request request) throws WrongArgumentException {
        if (!request.getArgs().isBlank()) throw new WrongArgumentException("");
        String lastInitTime = (supervisor.getDatabase().getDate() == null)
                ? "В сессии коллекция не инициализирована."
                : String.valueOf(supervisor.getDatabase().getData());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Тип коллекции: " + supervisor.getDatabase().getType() + "\n")
                .append("Размер коллекции (кол-во элементов): " + supervisor.getDatabase().getCount() + "\n")
                .append("Дата инициализации: " + supervisor.getDatabase().getDate() + "\n");
        return new Response(ResponseStatus.OK, stringBuilder.toString());
    }
}
