import exceptions.WrongArgumentException;
import exceptions.WrongValuesException;

import java.io.File;
import java.io.Writer;
import java.util.Scanner;

public class AppClient {

    private static final int RECONNECTION_TIMEOUT = 5000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port;

    public static void main(String[] args) {
        String[] as = new String[]{"localhost", "8009"};
        if (!initializeConnectionAddress(as)) return;
        Scanner userScanner = new Scanner(System.in);
        Manager userHandler = new Manager(userScanner);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler);
        client.run();
        userScanner.close();
    }

    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongArgumentException("");
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new WrongValuesException("Порт не может быть отрицательным");
            return true;
        } catch (WrongArgumentException e) {
            System.out.println("В аргументы командной строки передайте хост и порт в формате ...");
            String jarName = new File(AppClient.class.getProtectionDomain().getCodeSource().getLocation()
                    .getPath()).getName();
            System.out.println("Использование: 'java -jar' " + jarName + " <host> <port>'");
        } catch (NumberFormatException e) {
            System.out.println("Порт должен быть представлен числом");
        } catch (WrongValuesException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}

