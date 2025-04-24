package commands;


import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

public class ExitCommand extends AbstractCommand {
    public ExitCommand(Supervisor supervisor) {
        super("exit", "Завершить программу (без сохранения в файл)", supervisor);
    }

    @Override
    public Response execute(Request request) {
        return new Response(ResponseStatus.EXIT, "Выход из программы!");
    }
}
