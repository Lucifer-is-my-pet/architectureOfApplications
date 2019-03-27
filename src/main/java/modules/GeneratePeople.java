package modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modules.API.API;
import modules.ArrayListFiller.ArrayListFiller;
import modules.Birthdate.Birthdate;
import modules.Deserializer.Deserializer;
import modules.FileReaderToArray.FileReaderToArray;
import modules.HSSFWorkbookGenerator.HSSFWorkbookGenerator;
import modules.ITNGenerator.ITNGenerator;
import modules.JDBC.JDBC;
import modules.RandomNumber.RandomNumber;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.*;


class GeneratePeople {
    private final static String MALE = "Муж";
    private final static String FEMALE = "Жен";
    private final static String OUTPUT_PATH = new StringBuilder().append("src").append(File.separator).append("main")
            .append(File.separator).append("output").append(File.separator).toString();

    private final static String[] SHEETS_NAMES = {"Люди"};
    private final static String[] COLUMNS_NAMES = {"Имя", "Фамилия", "Отчество/Среднее имя", "Возраст", "Пол", "Дата рождения", "ИНН",
            "Почтовый индекс", "Страна", "Область", "Город", "Улица", "Дом", "Квартира"};
    private final static String SQL_ADDRESS_COLUMNS = "id int auto_increment not null, postcode varchar(256), " +
            "country varchar(256), region varchar(256), city varchar(256), street varchar(256), " +
            "house int, flat int, primary key (id)";
    private final static String SQL_PERSONS_COLUMNS = "id int auto_increment not null, surname varchar(256), " +
            "name varchar(256), middlename varchar(256), birthday date, gender varchar(1), " +
            "inn varchar(12), address_id int not null, foreign key (address_id) references address(id), primary key (id)";

    private final static HashMap<String, String> API_PARAMS = new HashMap<String, String>() {{
        put("inc", "gender,name,location,nat,dob"); // будем запрашивать с ресурса пол, ФИО, локацию, национальность, дату рождения
        put("noinfo", "");
    }};

    public static void main(String[] args) {
        JDBC jdbc = new JDBC();

        RandomNumber random = new RandomNumber();
        int rowsCount = random.generateWithStart(1, 31) + 1;

        HSSFWorkbookGenerator people = new HSSFWorkbookGenerator(SHEETS_NAMES);
        people.createRow(COLUMNS_NAMES, 0); // заголовки

        jdbc.executeCommands("", "CREATE DATABASE fintech;");
        jdbc.executeCommands("", "CREATE USER 'user1'@'localhost' IDENTIFIED BY 'password';");
        jdbc.executeCommands("", "GRANT ALL PRIVILEGES ON fintech.* TO 'user1'@'localhost';");
        jdbc.executeCommands("", "USE fintech;");
        jdbc.executeCommands("", "CREATE TABLE address (" + SQL_ADDRESS_COLUMNS + ");");
        jdbc.executeCommands("", "CREATE TABLE persons (" + SQL_PERSONS_COLUMNS + ");");

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
                cells.set(4, (cells.get(4).equals("Male")) ? MALE : FEMALE);
                cells.set(6, new ITNGenerator(77).toString());
                cells.add(Integer.toString(random.generateWithStart(1, 1000))); // в данных нет квартир

                String lastTd = "0";
                try {
                    lastTd = Integer.toString(jdbc.insert("address", new String[]{"postcode", "country", "region", "city", "street", "house", "flat"},
                            (String[]) Arrays.copyOfRange(cells.toArray(), 7, cells.size())).getInt("id"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                jdbc.insert("persons", new String[]{"surname", "name", "middlename", "birthday", "gender", "inn", "address_id"},
                        new String[]{cells.get(1), cells.get(0), cells.get(2), cells.get(5), cells.get(4), cells.get(6), lastTd});
            } catch (UnknownHostException ex) {
                System.out.println("Сеть отсутствует, генерирую из файлов строку номер " + (i + 1));
//                cells.clear();

                String sex = (i % 2 == 0) ? MALE : FEMALE;

                cells = new ArrayListFiller(sex).fill();
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