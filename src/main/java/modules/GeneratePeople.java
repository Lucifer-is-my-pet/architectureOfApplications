package modules;

import java.io.*;
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

    public static void main(String[] args) {

        FileReaderToArray readerToArray = new FileReaderToArray();

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

        HSSFWorkbookGenerator people = new HSSFWorkbookGenerator(SHEETS_NAMES);
        people.createRow(COLUMNS_NAMES, 0); // заголовки

        RandomNumber random = new RandomNumber();
        int rowsCount = random.generateWithStart(1, 31);
        for (int i = 1; i < rowsCount + 1; i++) {
            String sex = (i % 2 == 0) ? MALE : FEMALE;
            Birthdate birthdate = new Birthdate("dd-MM-yyyy");

            String[] cells = {names.get(sex)[random.generateWithoutStart(names.get(sex).length)],
                    surnames.get(sex)[random.generateWithoutStart(surnames.get(sex).length)],
                    patronNames.get(sex)[random.generateWithoutStart(patronNames.get(sex).length)],
                    Long.toString(birthdate.getAge()),
                    sex,
                    birthdate.get(),
                    new ITNGenerator(77).getString(),
                    Integer.toString(random.generateWithStart(100000, 200001)),
                    countries[random.generateWithoutStart(countries.length)],
                    districts[random.generateWithoutStart(districts.length)],
                    cities[random.generateWithoutStart(cities.length)],
                    streets[random.generateWithoutStart(streets.length)],
                    Integer.toString(random.generateWithStart(1, 301)),
                    Integer.toString(random.generateWithStart(1, 1000))};

            people.createRow(cells, 0);
        }

        File filename = new File(OUTPUT_PATH + "Люди.xls");
        try(FileOutputStream fileOut = new FileOutputStream(filename)) {
            people.write(fileOut);
            people.close();

            System.out.println("Файл создан. Путь: " + filename.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}