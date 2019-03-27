package modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modules.API.API;
import modules.ArrayListFiller.ArrayListFiller;
import modules.Deserializer.Deserializer;
import modules.HSSFWorkbookGenerator.HSSFWorkbookGenerator;
import modules.ITNGenerator.ITNGenerator;
import modules.RandomNumber.RandomNumber;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


class GeneratePeople {
    private final static String MALE = "Муж";
    private final static String FEMALE = "Жен";
    private final static String OUTPUT_PATH = new StringBuilder().append("src").append(File.separator).append("main")
            .append(File.separator).append("output").append(File.separator).toString();

    private final static String[] SHEETS_NAMES = {"Люди"};
    private final static String[] COLUMNS_NAMES = {"Имя", "Фамилия", "Отчество/Среднее имя", "Возраст", "Пол", "Дата рождения", "ИНН",
            "Почтовый индекс", "Страна", "Область", "Город", "Улица", "Дом", "Квартира"};

    private final static HashMap<String, String> API_PARAMS = new HashMap<String, String>() {{
        put("inc", "gender,name,location,nat,dob"); // будем запрашивать с ресурса пол, ФИО, локацию, национальность, дату рождения
        put("noinfo", "");
    }};

    public static void main(String[] args) {

        RandomNumber random = new RandomNumber();
        int rowsCount = random.generateWithStart(1, 31) + 1;

        HSSFWorkbookGenerator people = new HSSFWorkbookGenerator(SHEETS_NAMES);
        people.createRow(COLUMNS_NAMES, 0); // заголовки

        for (int i = 1; i < rowsCount; i++) {
            ArrayList<String> cells;
            try {
                InetAddress.getByName("randomuser.me");
                String resp = new API("https://randomuser.me/api/").getResponse(1, API_PARAMS);
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ArrayList.class, new Deserializer())
                        .create();

                cells = gson.fromJson(resp, ArrayList.class);
                cells.set(4, (cells.get(4).equals("Male")) ? MALE : FEMALE);
                cells.set(6, new ITNGenerator(77).getString());
                cells.add(Integer.toString(random.generateWithStart(1, 1000))); // в данных нет квартир
            } catch (UnknownHostException ex) {
                System.out.println("Сеть отсутствует, генерирую из файлов строку номер " + (i + 1));
//                cells.clear();

                String sex = (i % 2 == 0) ? MALE : FEMALE;

                cells = new ArrayListFiller(sex).fill();

            }
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