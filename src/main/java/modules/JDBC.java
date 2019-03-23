package modules;

import org.junit.jupiter.api.DisplayName;

import java.sql.*;
import java.util.ArrayList;

public class JDBC {

    private static final String
            USER = "username",
            PASS = "password",
            dbURL = "jdbc:mysql://localhost/";

    /*
     * Для различных команд
     * */
    void executeCommands(String dbName, String... commands) {
        try (Connection conn = DriverManager.getConnection(dbURL + dbName, USER, PASS);
             Statement statement = conn.createStatement()) {

            for (String command : commands) {
                statement.execute(command);
            }
        } catch (SQLException e) {
            System.out.println("Проблемы с запросом/соединением");
            e.printStackTrace();
        }
    }

    /*
     * Если нужен результат запроса
     * */
    ResultSet executeQuery(String dbName, String command) {
        ResultSet resultSet = null;
        try (Connection conn = DriverManager.getConnection(dbURL + dbName, USER, PASS);
             Statement statement = conn.createStatement()) {
            resultSet = statement.executeQuery(command);
        } catch (SQLException e) {
            System.out.println("Проблемы с запросом/соединением");
            e.printStackTrace();
        }

        return resultSet;
    }

    public ResultSet insert(String dbName, String[] coloumns, String[] values) {
        if (coloumns.length != values.length) {
            throw new ArrayIndexOutOfBoundsException("Количество столбцов не равно количеству значений");
        }
        StringBuilder query = new StringBuilder().append("INSERT INTO ").append(dbName).append(" (");
        try (Connection conn = DriverManager.getConnection(dbURL + dbName, USER, PASS);
             Statement statement = conn.createStatement()) {
            for (int i = 0; i < coloumns.length - 1; i++) {
                query.append(coloumns[i]).append(", ");
            }
            query.append(") VALUES (");
            for (int i = 0; i < values.length - 1; i++) {
                query.append(values[i]).append(", ");
            }
            query.append(");");
            statement.executeUpdate(query.toString(), Statement.RETURN_GENERATED_KEYS);
            return statement.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
