package commands;

import exceptions.WrongArgumentException;
import exceptions.WrongValuesException;
import transfers.Request;
import transfers.Response;

public interface Command {
    public Response execute(Request request) throws WrongValuesException, WrongArgumentException;
}
