package modules.SQL;

import com.sun.rowset.CachedRowSetImpl;
import modules.FileReaderToArray.FileReaderToArray;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JDBC {

    private static final String DBMS = "jdbc:mysql://";
    private static String
            host,
            port,
            dbName,
            user,
            password;

    public JDBC(String configFile) {
        FileReaderToArray readerToArray = new FileReaderToArray();
        String[] config = readerToArray.readLines(configFile);
        this.host = config[0].split(":")[0];
        this.port = config[0].split(":")[1];
        this.dbName = config[1];
        this.user = config[2];
        this.password = config[3];
    }

    private String getURL() {
        return this.DBMS + this.host + ":" + this.port + "/" + this.dbName + "?serverTimezone=Europe/Moscow";
    }

    /*
     * Для различных команд
     * */
    public void executeCommands(String... commands) {
        try (Connection conn = DriverManager.getConnection(getURL(), this.user, this.password);
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
    public CachedRowSetImpl executeQuery(String command) {
        try (Connection conn = DriverManager.getConnection(getURL(), this.user, this.password);
             Statement statement = conn.createStatement()) {
            CachedRowSetImpl result = new CachedRowSetImpl();
            ResultSet rs = statement.executeQuery(command);
            result.populate(rs);
            return result;
        } catch (SQLException e) {
            System.out.println("Проблемы с запросом/соединением");
            e.printStackTrace();
            return null;
        }
    }

    public CachedRowSetImpl insert(String tablename, String[] coloumns, String[] values, int[] indexesOfStrings) {
        if (coloumns.length != values.length) {
            throw new ArrayIndexOutOfBoundsException("Количество столбцов не равно количеству значений");
        }

        StringBuilder query = new StringBuilder().append("INSERT INTO ").append(this.dbName).append(".").append(tablename).append(" (");
        for (int i = 0; i < coloumns.length - 1; i++) {
            query.append(coloumns[i]).append(", ");
        }
        query.append(coloumns[coloumns.length - 1]).append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            StringBuilder valueToAppend = new StringBuilder().append(values[i]);
            if (ArrayUtils.contains(indexesOfStrings, i)) {
                valueToAppend.insert(0, "'");
                valueToAppend.append("'");
            }

            if (i == values.length - 1) {
                query.append(valueToAppend).append(");");
            } else {
                query.append(valueToAppend).append(", ");
            }

        };
        System.out.println(query);
        try (Connection conn = DriverManager.getConnection(getURL(), this.user, this.password);
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(query.toString(), Statement.RETURN_GENERATED_KEYS);
            CachedRowSetImpl result = new CachedRowSetImpl();
            ResultSet rs = statement.getGeneratedKeys();
            result.populate(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // todo определять, где строки, а где нет
    public void update(String tablename, String whereColoumn, String whereValue, String[] coloumnsToUpdate,
                                   String[] updatedValues, int[] indexesOfStrings) {
        if (coloumnsToUpdate.length != updatedValues.length) {
            throw new ArrayIndexOutOfBoundsException("Количество столбцов не равно количеству значений");
        }

        StringBuilder query = new StringBuilder().append("UPDATE ").append(this.dbName).append(".").append(tablename).append(" SET ");
        for (int i = 0; i < coloumnsToUpdate.length - 1; i++) {
            StringBuilder valueToUpdate = new StringBuilder().append(updatedValues[i]);
            query.append(coloumnsToUpdate[i]).append(" = '").append(updatedValues[i]).append("', ");
        }
        query.append(coloumnsToUpdate[coloumnsToUpdate.length - 1]).append(" = '")
                .append(updatedValues[updatedValues.length - 1]).append("' WHERE ").append(whereColoumn).append(" = ")
                .append(whereValue).append(";");

        try (Connection conn = DriverManager.getConnection(getURL(), this.user, this.password);
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(query.toString(), Statement.RETURN_GENERATED_KEYS);
//            CachedRowSetImpl result = new CachedRowSetImpl();
            /*ResultSet rs = */statement.getGeneratedKeys();
//            result.populate(rs);
//            return result;
        } catch (SQLException e) {
            e.printStackTrace();
//            return null;
        }
    }

    public CachedRowSetImpl selectWithStrings(String tablename, String[] coloumns, String[] values) {
        if (coloumns.length != values.length) {
            throw new ArrayIndexOutOfBoundsException("Количество столбцов не равно количеству значений");
        }

        int hyphenIndex = Arrays.asList(values).indexOf("-");

        if (hyphenIndex > -1) {
            List<String> valuesList = new LinkedList<>(Arrays.asList(values));
            valuesList.remove(hyphenIndex);
            values = valuesList.toArray(new String[valuesList.size()]);
            List<String> coloumnList = new LinkedList<>(Arrays.asList(coloumns));
            coloumnList.remove(hyphenIndex);
            coloumns = coloumnList.toArray(new String[coloumnList.size()]);
        }
        int lastIndex = coloumns.length - 1;

        StringBuilder query = new StringBuilder().append("SELECT * FROM ").append(this.dbName).append(".")
                .append(tablename).append(" WHERE ");
        for (int i = 0; i < lastIndex; i++) {
            query.append(coloumns[i]).append(" LIKE '%").append(values[i]).append("%' AND ");
        }
        query.append(coloumns[lastIndex]).append(" LIKE '%").append(values[lastIndex]).append("%';");

        try {
            CachedRowSetImpl result = new CachedRowSetImpl();
            ResultSet rs = executeQuery(query.toString());
            result.populate(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
