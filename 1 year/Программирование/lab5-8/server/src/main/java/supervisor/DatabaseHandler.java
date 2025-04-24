package supervisor;

import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseHandler {
    private final String JDBC_DRIVER = "org.postgresql.Driver";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/studs";
    public static final String DATABASE_URL_HELIOS = "jdbc:postgresql://pg:5432/postgres";
    String dbProps = "db.cfg";
    private Connection connection;
    public boolean isConnect = false;

    public DatabaseHandler() throws SQLException {
        this.connectToDatabase(dbProps);
        if (isConnect) {
            try {
                this.createTables();
            } catch (SQLException sqlException){

            }
        }
    }

    public void connectToDatabase(String propertiesFile) throws SQLException {
        Properties properties = null;
        try {
            properties = new Properties();
            try (FileReader fr = new FileReader(propertiesFile)) {
                properties.load(fr);
            } catch (IOException exception) {
                System.out.println("Ошибка в чтении конфинга базы данных.");
                isConnect = false;
            }
            Class.forName(JDBC_DRIVER);
            //DriverManager.register(new org.postgresql.Driver());
            connection = DriverManager.getConnection(DATABASE_URL, "s367086", "1ivnj0YivnCMVuUk");
            System.out.println("Соединение с базой данных установлено.");
            isConnect = true;
        } catch (SQLException exception) {
            //exception.printStackTrace();
            try {
                connection = DriverManager.getConnection(DATABASE_URL, properties);
            } catch (SQLException e) {
//                e.printStackTrace();
                System.out.println("Произошла ошибка при подключении к базе данных.");
                isConnect = false;
                throw new SQLException();
            }
        } catch (ClassNotFoundException exception) {
            System.out.println("Драйвер управления базой данных не найден.");
            //exception.printStackTrace();
            isConnect = false;
        }
    }

    public void closeConnection() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
            System.out.println("Соединение с базой данных успешно разорвано!");
        } catch (SQLException e) {
            System.out.println("При попытке разрыва соединения с базой данных возникла ошибка!");
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStat) throws SQLException {
        PreparedStatement prepareStatement;
        try {
            if (connection == null) {
                throw new SQLException();
            }
            prepareStatement = connection.prepareStatement(sqlStat);
            return prepareStatement;
        } catch (SQLException e) {
            if (connection == null) {
                System.out.println("Соединение с базой данных не установлено!");
            }
            throw new SQLException();
        }
    }

    public void createTables() throws SQLException {
        connection.prepareStatement(SQLRequests.CREATE_TABLES).execute();
        System.out.println("Таблицы успешно созданы!");
    }
}
