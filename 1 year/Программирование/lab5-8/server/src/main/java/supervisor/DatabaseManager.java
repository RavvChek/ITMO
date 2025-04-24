package supervisor;

import model.AstartesCategory;
import model.Chapter;
import model.Coordinates;
import model.SpaceMarine;
import exceptions.UserExistException;
import exceptions.UserNotfoundException;
import transfers.User;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class DatabaseManager {
    private DatabaseHandler databaseHandler;

    public DatabaseManager(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public LinkedList<SpaceMarine> readAll() throws SQLException {
        PreparedStatement prepareReadAllObjectsStatement = null;
        try {
            LinkedList<SpaceMarine> collection = new LinkedList<>();
            prepareReadAllObjectsStatement = databaseHandler.getPreparedStatement(SQLRequests.GET_ALL_OBJECTS);
            ResultSet resultSet = prepareReadAllObjectsStatement.executeQuery();
            while (resultSet.next()) {
                collection.add(new SpaceMarine(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        new Coordinates(
                                resultSet.getFloat("coordinates_x"),
                                resultSet.getDouble("coordinates_y")),
                        resultSet.getTimestamp("creation_date").toLocalDateTime().atZone(ZoneId.systemDefault()),
                        resultSet.getInt("health"),
                        resultSet.getInt("heart_count"),
                        resultSet.getString("achievements"),
                        AstartesCategory.valueOf(resultSet.getString("category")),
                        new Chapter(resultSet.getString("chapter_name"),
                                resultSet.getInt("chapter_marines_count")),
                        resultSet.getString("owner")));
            }
            return collection;
        } catch (SQLException e) {
            System.out.println("Возникла ошибка при считывании коллекции из базы данных");
        } finally {
            if (prepareReadAllObjectsStatement != null) {
                prepareReadAllObjectsStatement.close();
            }
        }
        return null;
    }

    public void verifyUser(User user) throws UserNotfoundException, SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SQLRequests.GET_USER);
            preparedStatement.setString(1, user.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String pass = PasswordHasher.sha384(user.getPassword());
                String login = user.getLogin();
                if (pass.equals(resultSet.getString("password")) && login.equals(resultSet.getString("login")))
                    System.out.println("Пользователь " + user + "авторизирован.");
                else throw new UserNotfoundException();
            } else throw new UserNotfoundException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при атворизации пользователя!");
            throw e;
        } finally {
            preparedStatement.close();
        }
    }

    public boolean updateObject(int id, SpaceMarine spaceMarine, User user) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SQLRequests.UPDATE_OBJECT);
            preparedStatement.setString(1, spaceMarine.getName());
            preparedStatement.setFloat(2, spaceMarine.getCoordinates().getX());
            preparedStatement.setDouble(3, spaceMarine.getCoordinates().getY());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(spaceMarine.getCreationDate().toString()));
            preparedStatement.setInt(5, spaceMarine.getHealth());
            preparedStatement.setInt(6, spaceMarine.getHeartCount());
            preparedStatement.setString(7, spaceMarine.getAchievements());
            preparedStatement.setObject(8, spaceMarine.getCategory());
            preparedStatement.setObject(9, spaceMarine.getChapter());
            preparedStatement.setInt(10, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Элемент успешно обновлён!");
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при обновлении объекта!");
        } finally {
            preparedStatement.close();
        }
        return false;
    }

    public boolean removeObject(int id, User user) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SQLRequests.DELETE_OBJECT);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            System.out.println("Элемент успешно удалён");
            return true;
        } catch (SQLException e) {
            System.out.println("Элемент не удалён!");
            return false;
        } finally {
            preparedStatement.close();
        }
    }

    public boolean removeAllObjects(List<Integer> list_id, User user) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            for (Integer id : list_id) {
                preparedStatement = databaseHandler.getPreparedStatement(SQLRequests.DELETE_OBJECT);
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
            }
            System.out.println("Все доступные пользователю объекты успешно удалены!");
            return true;
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при удалении объектов");
            //e.printStackTrace();
            return false;
        } finally {
            preparedStatement.close();
        }
    }

    public boolean checkUser(String login) throws SQLException {
        PreparedStatement preparedStatement = null;
        preparedStatement = databaseHandler.getPreparedStatement(SQLRequests.GET_USER);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public SpaceMarine addObject(SpaceMarine spaceMarine, User user) throws SQLException {
        SpaceMarine newSpaceMarine = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SQLRequests.ADD_OBJECT);
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            preparedStatement.setString(1, spaceMarine.getName());
            preparedStatement.setFloat(2, spaceMarine.getCoordinates().getX());
            preparedStatement.setDouble(3, spaceMarine.getCoordinates().getY());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(zonedDateTime.toLocalDateTime()));
            preparedStatement.setInt(5, spaceMarine.getHealth());
            preparedStatement.setInt(6, spaceMarine.getHeartCount());
            preparedStatement.setString(7, spaceMarine.getAchievements());
            preparedStatement.setObject(8, spaceMarine.getCategory(), Types.OTHER);
            preparedStatement.setString(9, spaceMarine.getChapter().getName());
            preparedStatement.setLong(10, spaceMarine.getChapter().getMarinesCount());
            preparedStatement.setObject(11, user.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("Элемент по каким-то причинам не добавлен в коллекцию!");
            } else {
                newSpaceMarine = new SpaceMarine(
                        resultSet.getInt(1),
                        spaceMarine.getName(),
                        spaceMarine.getCoordinates(),
                        spaceMarine.getCreationDate(),
                        spaceMarine.getHealth(),
                        spaceMarine.getHeartCount(),
                        spaceMarine.getAchievements(),
                        spaceMarine.getCategory(),
                        spaceMarine.getChapter(),
                        user.getLogin()
                );
                System.out.println("Элемент успешно добавлен в коллекцию.");
            }
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при добавлении объекта!");
            throw e;
        }
        return newSpaceMarine;
    }

    public void addUser(User user) throws SQLException, UserExistException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getPreparedStatement(SQLRequests.ADD_USER);
            if (this.checkUser(user.getLogin())) {
                throw new UserExistException();
            }
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, PasswordHasher.sha384(user.getPassword()));
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException();
            }
            System.out.println("Пользовтель успешно добавлен!");
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при добавлении пользователя!");
            e.printStackTrace();
            throw e;
        }
    }
}

