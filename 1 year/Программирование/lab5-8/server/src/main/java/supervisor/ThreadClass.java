package supervisor;

import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ForkJoinPool;

public class ThreadClass extends ForkJoinPool implements Runnable {
    private static ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private Response responseToUser;
    private Request userRequest;
    private RequestHandler requestHandler;
    private ObjectOutputStream clientWriter;
    private ObjectInputStream clientReader;

    public ThreadClass(Response responseToUser, Request userRequest, RequestHandler requestHandler, ObjectInputStream clientReader, ObjectOutputStream clientWriter) {
        this.responseToUser = responseToUser;
        this.clientReader = clientReader;
        this.clientWriter = clientWriter;
        this.requestHandler = requestHandler;
        this.userRequest = userRequest;
    }

    @Override
    public void run() {
        responseToUser = requestHandler.handle(userRequest);
        System.out.println("Запрос '" + userRequest.getCommandName() + "' успешно обработан");
        forkJoinPool.execute(()-> {
                    try {
                        clientWriter.writeObject(responseToUser);
                        clientWriter.flush();
                        clientReader.close();
                        clientWriter.close();
                    } catch (IOException e) {
                        if (userRequest == null) {
                            System.out.println("Разрыв соединения с клиентом");
                        } else {
                            System.out.println("Клиент успешно отключен от сервера");
                        }
                    }
                });
//        forkJoinPool.execute(() -> {
//
//        });
    }
}
