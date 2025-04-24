package commands;


import supervisor.Supervisor;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

public class ExecuteScriptCommand extends AbstractCommand {
    public ExecuteScriptCommand(Supervisor supervisor) {
        super("execute_script", "Считать и исполнить скрипт из указанного файла.", supervisor);
    }

    @Override
    public Response execute(Request request) {
        return new Response(ResponseStatus.OK, "!");
    }
}
