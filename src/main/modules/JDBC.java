package main.modules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JDBC {

    private static final String
            USER = "username",
            PASS = "password",
            dbURL = "jdbc:mysql://localhost/";

    void executeCommands(Statement statement, String... commands) {
        try /*(Connection conn = DriverManager.getConnection(dbURL + dbName, USER, PASS);
             Statement statement = conn.createStatement())*/ {

            for (String command : commands) {
                statement.execute(command);
            }
        } catch (SQLException e) {
            System.out.println("Проблемы с запросом/соединением");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (statement.getConnection() != null) {
                    statement.getConnection().close();
                }
            } catch (SQLException se) {
            }
        }
    }

    Statement establishConnection(String dbName) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(dbURL + dbName, USER, PASS);
        return conn.createStatement();
    }

    public void insert(String dbName, ArrayList<String> coloumns, ArrayList<String> values) {
        if (coloumns.size() != values.size()) {
            throw new ArrayIndexOutOfBoundsException("Количество столбцов не равно количеству значений");
        }
        Statement statement = null;
        try {
            establishConnection(dbURL + dbName);
            statement.executeUpdate("CREATE DATABASE " + dbName);
        } catch (SQLException e) {

        } catch (ClassNotFoundException e) {
            System.out.println("Проблемы с драйвером");
            e.printStackTrace();
        }
    }
}
