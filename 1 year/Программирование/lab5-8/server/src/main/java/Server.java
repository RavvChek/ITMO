import exceptions.ConnectException;
import exceptions.ServerLaunchException;
import supervisor.*;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private final int port = 8009;
    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private static final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private static final ForkJoinPool forkJoinPool2 = ForkJoinPool.commonPool();
    private Socket socket;
    private ServerSocket server;
    private InputStream is;
    private OutputStream os;
    private boolean isActivate;
    private Supervisor supervisor;
    private RequestHandler requestHandler;
    private int timeout;
    private DatabaseHandler databaseHandler;
    private DatabaseManager databaseManager;


    public Server(Supervisor supervisor, int port, int timeout, RequestHandler requestHandler, DatabaseManager databaseManager, DatabaseHandler databaseHandler) {
        this.supervisor = supervisor;
        this.timeout = timeout;
        this.requestHandler = requestHandler;
        this.databaseHandler = databaseHandler;
        this.databaseManager = databaseManager;

    }

    public void open() throws ServerLaunchException {
        try {
            System.out.println("Запуск сервера... ");
            server = new ServerSocket(port);
            server.setSoTimeout(timeout);
            activateServer();
        } catch (IllegalArgumentException e) {
            System.out.println("Порт " + port + " недоступен!");
            throw new ServerLaunchException("Сервер не смог запуститься!!!");
        } catch (IOException e) {
            System.out.println("Непредвиденная ошибка при использовании порта " + port + "!");
            throw new ServerLaunchException("Сервер не смог запуститься!!!");
        }
    }

    public Socket connect() throws ConnectException {
        try {
            socket = server.accept();
            System.out.println("Соединение с клиентом установлено!");
            return socket;
        } catch (SocketTimeoutException e) {
            System.out.println("Превышено время ожидания подключения!");
            throw new ConnectException("");
        } catch (IOException e) {
            System.out.println("Ошибка при соединении с клиентом!");
            throw new ConnectException("");
        }
    }


    public boolean receiveRequest(Socket socket) {
        fixedThreadPool.submit(() -> {
            Request userRequest = null;
            Response responseToUser = null;
            try{
                ObjectInputStream clientReader = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream clientWriter = new ObjectOutputStream(socket.getOutputStream());
                userRequest = (Request) clientReader.readObject();
                ThreadClass threadClass = new ThreadClass(responseToUser, userRequest, requestHandler, clientReader, clientWriter);
                forkJoinPool.execute(threadClass);
//                threadClass.run();
            } catch (ClassNotFoundException e) {
                System.out.println("Ошибка при чтении полученных данных");
            } catch (InvalidClassException | NotSerializableException ex) {
                System.out.println("Ошибка при отправке данных на клиент");
            } catch (IOException e) {
                if (userRequest == null) {
                    System.out.println("Разрыв соединения с клиентом");
                } else {
                    System.out.println("Клиент успешно отключен от сервера");
                }
            }
            return true;
        });
        return true;
    }

    public void run() {
        try {
            open();
            while (isActivate) {
                try {
                    isActivate = receiveRequest(connect());
                } catch (ConnectException e) {
                    break;
                }
            }
            stop();
        } catch (ServerLaunchException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        deactivateServer();
        try {
            socket.close();
            System.out.println("Работа сервера завершена!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void activateServer() {
        isActivate = true;
        System.out.println("Сервер запущен!");
    }

    public void deactivateServer() {
        isActivate = false;
        System.out.println("Сервер закрыт!");
    }

    public int getPort() {
        return port;
    }

}


