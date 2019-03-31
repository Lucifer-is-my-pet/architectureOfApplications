package modules.ArrayListFiller;

import modules.Birthdate.Birthdate;
import modules.FileReaderToArray.FileReaderToArray;
import modules.ITNGenerator.ITNGenerator;
import modules.RandomNumber.RandomNumber;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrayListFiller {

    private final String RESOURCES_PATH = new StringBuilder().append(".").append(File.separator).append("src")
            .append(File.separator).append("main").append(File.separator).append("resources").append(File.separator).toString();

    private ResultSet person;
    private ResultSet address;

    private final String MALE = "Муж";
    private final String FEMALE = "Жен";
    private String currentSex;
    private String[] countries;
    private String[] districts;
    private String[] cities;
    private String[] streets;
    private Map<String, String[]> names = new HashMap<>();
    private Map<String, String[]> surnames = new HashMap<>();
    private Map<String, String[]> patronNames = new HashMap<>();

    public ArrayListFiller(String sex) {
        this.currentSex = sex;

        FileReaderToArray readerToArray = new FileReaderToArray();

        countries = readerToArray.readLines(RESOURCES_PATH + "Countries.txt");
        districts = readerToArray.readLines(RESOURCES_PATH + "Districts.txt");
        cities = readerToArray.readLines(RESOURCES_PATH + "Cities.txt");
        streets = readerToArray.readLines(RESOURCES_PATH + "Streets.txt");

        if (this.currentSex.equals(MALE)) {
            names.put(MALE, readerToArray.readLines(RESOURCES_PATH + "Names_m.txt"));
            surnames.put(MALE, readerToArray.readLines(RESOURCES_PATH + "Surnames_m.txt"));
            patronNames.put(MALE, readerToArray.readLines(RESOURCES_PATH + "Patronymic_m.txt"));
        } else {
            names.put(FEMALE, readerToArray.readLines(RESOURCES_PATH + "Names_f.txt"));
            surnames.put(FEMALE, readerToArray.readLines(RESOURCES_PATH + "Surnames_f.txt"));
            patronNames.put(FEMALE, readerToArray.readLines(RESOURCES_PATH + "Patronymic_f.txt"));
        }
    }

    public ArrayListFiller(ResultSet person, ResultSet address) {
        this.person = person;
        this.address = address;
    }

    public ArrayList<String> fillFromDB() {
        ArrayList<String> result = new ArrayList<>();
        Birthdate birthdate = new Birthdate("dd-MM-yyyy");
        try {
            result.add(this.person.getString("name"));
            result.add(this.person.getString("surname"));
            result.add(this.person.getString("middlename"));

            birthdate.setBirthdate(this.person.getDate("birthday").toString());
            result.add(Long.toString(birthdate.getAge()));

            result.add(this.person.getString("gender").equals("М") ? MALE : FEMALE);
            result.add(birthdate.get());
            result.add(this.person.getString("inn"));
            result.add(this.address.getString("postcode"));
            result.add(this.address.getString("country"));
            result.add(this.address.getString("region"));
            result.add(this.address.getString("city"));
            result.add(this.address.getString("street"));
            result.add(Integer.toString(this.address.getInt("house")));
            result.add(Integer.toString(this.address.getInt("flat")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> fillFromFiles() {
        RandomNumber random = new RandomNumber();
        Birthdate birthdate = new Birthdate("dd-MM-yyyy");
        birthdate.generate();

        ArrayList<String> result = new ArrayList<>();

        result.add(names.get(this.currentSex)[random.generateWithoutStart(names.get(this.currentSex).length)]);
        result.add(surnames.get(this.currentSex)[random.generateWithoutStart(surnames.get(this.currentSex).length)]);
        result.add(patronNames.get(this.currentSex)[random.generateWithoutStart(patronNames.get(this.currentSex).length)]);
        result.add(Long.toString(birthdate.getAge()));
        result.add(this.currentSex);
        result.add(birthdate.get());
        result.add(new ITNGenerator(77).toString());
        result.add(Integer.toString(random.generateWithStart(100000, 200001)));
        result.add(countries[random.generateWithoutStart(countries.length)]);
        result.add(districts[random.generateWithoutStart(districts.length)]);
        result.add(cities[random.generateWithoutStart(cities.length)]);
        result.add(streets[random.generateWithoutStart(streets.length)]);
        result.add(Integer.toString(random.generateWithStart(1, 301)));
        result.add(Integer.toString(random.generateWithStart(1, 1000)));

        return result;
    }
}