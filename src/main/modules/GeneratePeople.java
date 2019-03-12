package main.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


class GeneratePeople {

    public static void main(String[] args) {
        final String MALE = "Муж";
        final String FEMALE = "Жен";
        // "C:\\Users\\guralnik\\Documents\\GitHub\\architectureOfApplications\\src\\main\\resources\\"
        final String RESOURCES_PATH = new StringBuilder().append(".").append(File.separator).append("src")
                .append(File.separator).append("main").append(File.separator).append("resources").append(File.separator).toString();
        // "C:\\Users\\guralnik\\Documents\\GitHub\\architectureOfApplications\\src\\main\\output\\"
        final String OUTPUT_PATH = new StringBuilder().append("src").append(File.separator).append("main")
                .append(File.separator).append("output").append(File.separator).toString();

        String[] countries = null;
        String[] districts = null;
        String[] cities = null;
        String[] streets = null;
        Map<String, String[]> names = new HashMap<>();
        Map<String, String[]> surnames = new HashMap<>();
        Map<String, String[]> patronNames = new HashMap<>();

        FileReaderToArray readerToArray = new FileReaderToArray();

        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] sheetsNames = {"Люди"};
        String[] columnNames = {"Имя", "Фамилия", "Отчество", "Возраст", "Пол", "Дата рождения", "ИНН",
                "Почтовый индекс", "Страна", "Область", "Город", "Улица", "Дом", "Квартира"};

        int rowsCount = new RandomNumber(1, 31).get();

        HashMap<String, String> params = new HashMap<String, String>() {{
            put("inc", "gender,name,location,nat,dob,id");
        }};

        ArrayList<String> cells = new ArrayList<>();

        try {
            HSSFWorkbookGenerator people = new HSSFWorkbookGenerator(sheetsNames);
            people.createRow(columnNames, 0); // заголовки

            InetAddress inetAddress = InetAddress.getByName("randomuser.me");

            for (int i = 1; i < rowsCount + 1; i++) {
                if (inetAddress.isReachable(700)) {
                    String resp = new API("https://randomuser.me/api/").getResponse(1, params, "noinfo");
                    Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(ArrayList.class, new Deserializer())
                            .create();
                    cells = gson.fromJson(resp, ArrayList.class);
                } else {
                    System.out.println("Сеть отсутствует");

                    String sex = (i % 2 == 0) ? MALE : FEMALE;
                    Birthdate birthdate = new Birthdate("dd-MM-yyyy");
                    birthdate.generateBirthdate();

                    cells.add(names.get(sex)[new RandomNumber(names.get(sex).length).get()]);
                    cells.add(surnames.get(sex)[new RandomNumber(surnames.get(sex).length).get()]);
                    cells.add(patronNames.get(sex)[new RandomNumber(patronNames.get(sex).length).get()]);
                    cells.add(Long.toString(birthdate.getAge()));
                    cells.add(sex);
                    cells.add(birthdate.get());
                    cells.add(new ITNGenerator(77).getString());
                    cells.add(new RandomNumber(100000, 200001).getString());
                    cells.add(countries[new RandomNumber(countries.length).get()]);
                    cells.add(districts[new RandomNumber(districts.length).get()]);
                    cells.add(cities[new RandomNumber(cities.length).get()]);
                    cells.add(streets[new RandomNumber(streets.length).get()]);
                    cells.add(new RandomNumber(1, 301).getString());
                    cells.add(new RandomNumber(1, 1000).getString());

                }

            }

            people.createRow(cells, 0);

            File filename = new File(OUTPUT_PATH + "Люди.xls");
            FileOutputStream fileOut = new FileOutputStream(filename);
            people.write(fileOut);
            fileOut.close();
            people.close();
            System.out.println("Файл создан. Путь: " + filename.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}