package main.modules;

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

    public static void main(String[]args) {

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

        try {
            String[] sheetsNames = {"Люди"};
            String[] columnNames = {"Имя", "Фамилия", "Отчество", "Возраст", "Пол", "Дата рождения", "ИНН",
                    "Почтовый индекс", "Страна", "Область", "Город", "Улица", "Дом", "Квартира"};

            HSSFWorkbookGenerator people = new HSSFWorkbookGenerator(sheetsNames);
            people.createRow(columnNames, 0); // заголовки

            int rowsCount = new RandomNumber(1, 31).get();
            for (int i = 1; i < rowsCount + 1; i++) {
                String sex = (i % 2 == 0) ? MALE : FEMALE;
                Birthdate birthdate = new Birthdate("dd-MM-yyyy");

                String[] cells = {names.get(sex)[new RandomNumber(names.get(sex).length).get()],
                        surnames.get(sex)[new RandomNumber(surnames.get(sex).length).get()],
                        patronNames.get(sex)[new RandomNumber(patronNames.get(sex).length).get()],
                        Long.toString(birthdate.getAge()),
                        sex,
                        birthdate.get(),
                        new ITNGenerator(77).getString(),
                        new RandomNumber(100000, 200001).getString(),
                        countries[new RandomNumber(countries.length).get()],
                        districts[new RandomNumber(districts.length).get()],
                        cities[new RandomNumber(cities.length).get()],
                        streets[new RandomNumber(streets.length).get()],
                        new RandomNumber(1, 301).getString(),
                        new RandomNumber(1, 1000).getString()};

                people.createRow(cells, 0);
            }

            File filename = new File(OUTPUT_PATH + "Люди.xls");
            FileOutputStream fileOut = new FileOutputStream(filename);
            people.write(fileOut);
            fileOut.close();
            people.close();
            System.out.println("Файл создан. Путь: " + filename.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}