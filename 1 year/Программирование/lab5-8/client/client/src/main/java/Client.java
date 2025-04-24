import exceptions.ConnectionException;
import exceptions.WrongValuesException;
import transfers.Request;
import transfers.Response;
import transfers.ResponseStatus;
import transfers.User;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Client {
    private final String host;
    private final int port;
    private final int reconnectionTimeout;
    private final int maxReconnectionAttempts;
    private final Manager manager;
    private int reconnectionAttempts;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private User user;
    private final String loginCommand = "login";
    private final String registerCommand = "register";

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, Manager manager) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.manager = manager;
    }

    public void run() {
        try {
            boolean processingStatus = true;
            while (processingStatus) {
                try {
                    connect();
                    processingStatus = processRequestToServer();
                } catch (ConnectionException e) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        System.out.println("Превышено количество попыток подключения");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutEx) {
                        System.out.println("Время ожидания подключения '" + reconnectionTimeout +
                                "' находится за пределом допустимого значения");
                        System.out.println("Производиться повторное подключение");
                    } catch (Exception timeoutEx) {
                        System.out.println("Произошла ошибка при попытке ожидания подключения");
                        System.out.println("Производиться повторное подключение");
                    }
                }
                reconnectionAttempts++;
            }
            if (socketChannel != null) socketChannel.close();
            System.out.println("Работа клиента успешно завершена");
        } catch (WrongValuesException e) {
            System.out.println("Клиент не может быть запущен");
        } catch (IOException e) {
            System.out.println("Произошла ошибка при попытке завершить соединение с сервером");
        }
    }

    private void connect() throws ConnectionException, WrongValuesException {
        try {
//            if (reconnectionAttempts >= 1) System.out.println("Повторное соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
//            System.out.println("Соединение с сервером успешно установлено.");
//            System.out.println("Ожидание разрешения на обмен данными.");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
//            System.out.println("Разрешение на обмен данными получено.");
//            System.out.println("Добро пожаловать!");
        } catch (IllegalArgumentException e) {
            System.out.println("Адрес сервера введен некорректно");
            throw new WrongValuesException("");
        } catch (IOException e) {
            System.out.println("Произошла ошибка соединения с сервером.");
            throw new ConnectionException();
        }

    }

    private boolean processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                if (Objects.isNull(user)) {
                    Response responseToUser = null;
                    AskUser askUser = new AskUser(manager.getUserScanner());
                    String command = askUser.askAuth("У вас уже есть учётная запись?")
                            ? loginCommand
                            : registerCommand;
                    user = new AskUser(manager.getUserScanner()).build();
                    requestToServer = new Request(command, "", user);
                    if (requestToServer.isEmpty()) continue;
                    serverWriter.writeObject(requestToServer);
                    responseToUser = (Response) serverReader.readObject();
                    System.out.println(responseToUser.getResponseBody());

                    serverReader.close();
                    serverWriter.close();
                    return true;
                }
                requestToServer = serverResponse != null ? manager.handle(serverResponse.getStatus(), user) :
                        manager.handle(null, user);
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                System.out.println(serverResponse.getResponseBody());

                serverReader.close();
                serverWriter.close();
                return true;
            } catch (InvalidClassException | NotSerializableException e) {
                System.out.println("Произошла ошибка при отправке данных на сервер");
            } catch (ClassNotFoundException e) {
                System.out.println("Произошла ошибка при чтении полученных данных");
            } catch (IOException e) {
                System.out.println("Соединение с сервером разорвано");
                try {
                    reconnectionAttempts++;
                    connect();
                } catch (ConnectionException | WrongValuesException reconnectionEx) {
                    if (requestToServer.getCommandName().equals("exit"))
                        System.out.println("Команда не зарегистрирована на сервере");
                    else System.out.println("Попробуйте повторить команду позже");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return false;
    }

    /*public void userProcess() throws IOException, ConnectionException, WrongValuesException {
        if (Objects.isNull(user)) {
            Response responsetoUser = null;
            do {
                AskUser askUser = new AskUser(manager.getUserScanner());
                String command = askUser.askAuth("У вас уже есть учётная запись?")
                        ? loginCommand
                        : registerCommand;
                user = new AskUser(manager.getUserScanner()).build();
                responsetoUser = this.processRequestToServer(new Request(command, "", user));
            } while (responsetoUser.getStatus() != ResponseStatus.OK);
        }
    }

    private boolean processRequestToServer(Request request) {
        Response serverResponse = null;
        do {
            try {
                if (request.isEmpty()) continue;
                serverWriter.writeObject(request);
                serverResponse = (Response) serverReader.readObject();
                System.out.println(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException e) {
                System.out.println("Произошла ошибка при отправке данных на сервер");
            } catch (ClassNotFoundException e) {
                System.out.println("Произошла ошибка при чтении полученных данных");
            } catch (IOException e) {
                System.out.println("Соединение с сервером разорвано");
                try {
                    reconnectionAttempts++;
                    connect();
                } catch (ConnectionException | WrongValuesException reconnectionEx) {
                    if (request.getCommandName().equals("exit"))
                        System.out.println("Команда не зарегистрирована на сервере");
                    else System.out.println("Попробуйте повторить команду позже");
                }
            }
        } while (!request.getCommandName().equals("exit"));
        return false;
    }*/

}