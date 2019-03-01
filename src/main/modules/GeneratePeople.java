package main.modules;

import java.io.*;
import java.util.*;


class GeneratePeople {

    private static String[] readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }

    public static void main(String[]args) {
        final String MALE = "Муж";
        final String FEMALE = "Жен";
        final String PATH = "";
        
        String[] countries = null;
        String[] districts = null;
        String[] cities = null;
        String[] streets = null;
        Map<String, String[]> names = new HashMap<>();
        Map<String, String[]> surnames = new HashMap<>();
        Map<String, String[]> patronNames = new HashMap<>();

        try {
            countries = readLines("./src/main/resources/Countries.txt");
            districts = readLines("./src/main/resources/Districts.txt");
            cities = readLines("./src/main/resources/Cities.txt");
            streets = readLines("./src/main/resources/Streets.txt");
            names.put(MALE, readLines("./src/main/resources/Names_m.txt"));
            names.put(FEMALE, readLines("./src/main/resources/Names_f.txt"));
            surnames.put(MALE, readLines("./src/main/resources/Surnames_m.txt"));
            surnames.put(FEMALE, readLines("./src/main/resources/Surnames_f.txt"));
            patronNames.put(MALE, readLines("./src/main/resources/Patronymic_m.txt"));
            patronNames.put(FEMALE, readLines("./src/main/resources/Patronymic_f.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

            File filename = new File("src/main/resources/Люди.xls");
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
