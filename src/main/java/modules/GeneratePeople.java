package modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.rowset.CachedRowSetImpl;
import modules.API.API;
import modules.ArrayListFiller.ArrayListFiller;
import modules.Birthdate.Birthdate;
import modules.Deserializer.Deserializer;
import modules.HSSFWorkbookGenerator.HSSFWorkbookGenerator;
import modules.ITNGenerator.ITNGenerator;
import modules.SQL.JDBC;
import modules.RandomNumber.RandomNumber;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


class GeneratePeople {
    private final static String
            MALE = "Муж",
            FEMALE = "Жен",
            OUTPUT_PATH = new StringBuilder().append("src").append(File.separator).append("main")
                    .append(File.separator).append("output").append(File.separator).toString(),
            RESOURCES_PATH = new StringBuilder().append(".").append(File.separator).append("src")
                    .append(File.separator).append("main").append(File.separator).append("resources").append(File.separator).toString();

    private final static String[]
            SHEETS_NAMES = {"Люди"},
            COLUMNS_NAMES = {"Имя", "Фамилия", "Отчество/Среднее имя", "Возраст", "Пол", "Дата рождения", "ИНН",
                    "Почтовый индекс", "Страна", "Область", "Город", "Улица", "Дом", "Квартира"};
    private final static String
            SQL_ADDRESS = "address",
            SQL_PERSONS = "persons",
            SQL_ADDRESS_COLUMNS = "id int auto_increment not null, postcode varchar(256), " +
                    "country varchar(256), region varchar(256), city varchar(256), street varchar(256), " +
                    "house int, flat int, primary key (id)",
            SQL_PERSONS_COLUMNS = "id int auto_increment not null, surname varchar(256), " +
                    "name varchar(256), middlename varchar(256), birthday date, gender varchar(1), " +
                    "inn varchar(12), address_id int not null, foreign key (address_id) references address(id), primary key (id)";

    private final static HashMap<String, String> API_PARAMS = new HashMap<String, String>() {{
        put("inc", "gender,name,location,nat,dob"); // будем запрашивать с ресурса пол, ФИО, локацию, национальность, дату рождения
        put("noinfo", "");
    }};

    public static void main(String[] args) {
        RandomNumber random = new RandomNumber();
        int rowsCount = random.generateWithStart(1, 31) + 1;

        HSSFWorkbookGenerator people = new HSSFWorkbookGenerator(SHEETS_NAMES);
        people.createRow(COLUMNS_NAMES, 0); // заголовки

        JDBC jdbc = new JDBC(RESOURCES_PATH + "config.txt");
        jdbc.executeCommands("CREATE TABLE IF NOT EXISTS " + SQL_ADDRESS + " (" + SQL_ADDRESS_COLUMNS + ");");
        jdbc.executeCommands("CREATE TABLE IF NOT EXISTS " + SQL_PERSONS + " (" + SQL_PERSONS_COLUMNS + ");");

        for (int i = 1; i < rowsCount; i++) {
            ArrayList<String> cells = new ArrayList<>();
            try {
                InetAddress.getByName("randomuser.me");
                String resp = new API("https://randomuser.me/api/").getResponse(1, API_PARAMS);
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ArrayList.class, new Deserializer())
                        .create();

                cells = gson.fromJson(resp, ArrayList.class);
                cells.set(4, cells.get(4).equals("Male") ? MALE : FEMALE);
                cells.set(6, new ITNGenerator(77).toString());
                cells.add(Integer.toString(random.generateWithStart(1, 1000))); // в randomuser нет квартир

                // проверяем, нет ли человека с такими данными
                CachedRowSetImpl sameNamePerson = jdbc.selectWithStrings(SQL_PERSONS, new String[]{"surname", "name", "middlename"},
                        new String[]{cells.get(1), cells.get(0), cells.get(2)});
                try {
                    if (sameNamePerson.next()) {
                        String address_id = String.valueOf(sameNamePerson.getInt("address_id"));
                        jdbc.update(SQL_ADDRESS,
                                "id", address_id, new String[]{"postcode", "country", "region", "city", "street", "house", "flat"},
                                Arrays.copyOfRange(cells.toArray(), 7, cells.size(), String[].class),
                                new int[]{0, 1, 2, 3, 4});
                        jdbc.update(SQL_PERSONS,
                                "address_id", address_id, new String[]{"birthday", "gender", "inn"},
                                new String[]{new Birthdate("yyyy-MM-dd").getFormattedBirthdate(cells.get(5), "dd-MM-yyyy"),
                                        cells.get(4).equals("Male") ? "М" : "Ж", cells.get(6)},
                                new int[]{0, 1});
                    } else {
                        String lastTd;
                        CachedRowSetImpl insertResult = jdbc.insert(SQL_ADDRESS,
                                new String[]{"postcode", "country", "region", "city", "street", "house", "flat"},
                                Arrays.copyOfRange(cells.toArray(), 7, cells.size(), String[].class),
                                new int[]{0, 1, 2, 3, 4});
                        insertResult.next();
                        lastTd = Integer.toString(insertResult.getInt(1));

                        jdbc.insert(SQL_PERSONS,
                                new String[]{"surname", "name", "middlename", "birthday", "gender", "inn", "address_id"},
                                new String[]{cells.get(1), cells.get(0), cells.get(2),
                                        new Birthdate("yyyy-MM-dd").getFormattedBirthdate(cells.get(5), "dd-MM-yyyy"),
                                        cells.get(4).equals("Male") ? "М" : "Ж", cells.get(6), lastTd},
                                new int[]{0, 1, 2, 3, 4});
                    }
                } catch (SQLException sqle) {
                    System.out.println("Ошибка при проверке людей");
                    sqle.printStackTrace();
                }
            } catch (UnknownHostException uhex) {
                // получаем строку с этим номером
                CachedRowSetImpl personFromDB = jdbc.executeQuery("SELECT * FROM " + SQL_PERSONS + " LIMIT " + (i - 1) + ", 1;");
                try {
                    if (personFromDB.next()) { // если в таблицах достаточно строк
                        System.out.println("Сеть отсутствует, генерирую из БД строку номер " + (i + 1));

                        int address_id = personFromDB.getInt("address_id");
                        CachedRowSetImpl addressFromDB = jdbc.executeQuery("SELECT * FROM " + SQL_ADDRESS + " WHERE id = " + address_id + ";");
                        addressFromDB.next();
                        cells = new ArrayListFiller(personFromDB, addressFromDB).fillFromDB();
                    } else {
                        System.out.println("Сеть отсутствует, в БД не хватает строк, генерирую из файлов строку номер " + (i + 1));
//                cells.clear();

                        String sex = (i % 2 == 0) ? MALE : FEMALE;

                        cells = new ArrayListFiller(sex).fillFromFiles();
                    }
                } catch (SQLException sqlex) {
                    System.out.println("Ошибка при получении из " + SQL_PERSONS + " строки " + (i - 1) +
                            " или при получении строки из " + SQL_ADDRESS);
                    sqlex.printStackTrace();
                }
            }
            people.createRow(cells, 0);
        }

        File filename = new File(OUTPUT_PATH + "Люди.xls");
        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
            people.write(fileOut);
            people.close();

            System.out.println("Файл создан. Путь: " + filename.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}