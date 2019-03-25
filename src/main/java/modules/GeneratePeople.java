package modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.*;


class GeneratePeople {
    private final static String MALE = "Муж";
    private final static String FEMALE = "Жен";
    private final static String RESOURCES_PATH = new StringBuilder().append(".").append(File.separator).append("src")
            .append(File.separator).append("main").append(File.separator).append("resources").append(File.separator).toString();
    private final static String OUTPUT_PATH = new StringBuilder().append("src").append(File.separator).append("main")
            .append(File.separator).append("output").append(File.separator).toString();

    private static String[] countries = null;
    private static String[] districts = null;
    private static String[] cities = null;
    private static String[] streets = null;
    private static Map<String, String[]> names = new HashMap<>();
    private static Map<String, String[]> surnames = new HashMap<>();
    private static Map<String, String[]> patronNames = new HashMap<>();

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
    }};

    public static void main(String[] args) {
        FileReaderToArray readerToArray = new FileReaderToArray();
        JDBC jdbc = new JDBC();

        countries = readerToArray.readLines(RESOURCES_PATH + "Countries.txt");
        districts = readerToArray.readLines(RESOURCES_PATH + "Districts.txt");
        cities = readerToArray.readLines(RESOURCES_PATH + "Cities.txt");
        streets = readerToArray.readLines(RESOURCES_PATH + "Streets.txt");
        names.put(MALE, readerToArray.readLines(RESOURCES_PATH + "Names_m.txt"));
        names.put(FEMALE, readerToArray.readLines(RESOURCES_PATH + "Names_f.txt"));
        surnames.put(MALE, readerToArray.readLines(RESOURCES_PATH + "Surnames_m.txt"));
        surnames.put(FEMALE, readerToArray.readLines(RESOURCES_PATH + "Surnames_f.txt"));
        patronNames.put(MALE, readerToArray.readLines(RESOURCES_PATH + "Patronymic_m.txt"));
        patronNames.put(FEMALE, readerToArray.readLines(RESOURCES_PATH + "Patronymic_f.txt"));

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
                String resp = new API("https://randomuser.me/api/").getResponse(1, API_PARAMS, "noinfo");
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ArrayList.class, new Deserializer())
                        .create();

                cells = gson.fromJson(resp, ArrayList.class);
                cells.set(4, (cells.get(4).equals("Male")) ? MALE : FEMALE);
                cells.set(6, new ITNGenerator(77).getString());
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
                Birthdate birthdate = new Birthdate("dd-MM-yyyy");
                birthdate.set();

                cells.add(names.get(sex)[random.generateWithoutStart(names.get(sex).length)]);
                cells.add(surnames.get(sex)[random.generateWithoutStart(surnames.get(sex).length)]);
                cells.add(patronNames.get(sex)[random.generateWithoutStart(patronNames.get(sex).length)]);
                cells.add(Long.toString(birthdate.getAge()));
                cells.add(sex);
                cells.add(birthdate.get());
                cells.add(new ITNGenerator(77).getString());
                cells.add(Integer.toString(random.generateWithStart(100000, 200001)));
                cells.add(countries[random.generateWithoutStart(countries.length)]);
                cells.add(districts[random.generateWithoutStart(districts.length)]);
                cells.add(cities[random.generateWithoutStart(cities.length)]);
                cells.add(streets[random.generateWithoutStart(streets.length)]);
                cells.add(Integer.toString(random.generateWithStart(1, 301)));
                cells.add(Integer.toString(random.generateWithStart(1, 1000)));
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