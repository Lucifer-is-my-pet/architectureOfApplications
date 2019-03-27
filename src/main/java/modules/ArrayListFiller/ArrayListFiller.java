package modules.ArrayListFiller;

import modules.Birthdate.Birthdate;
import modules.FileReaderToArray.FileReaderToArray;
import modules.ITNGenerator.ITNGenerator;
import modules.RandomNumber.RandomNumber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrayListFiller {

    private final String RESOURCES_PATH = new StringBuilder().append(".").append(File.separator).append("src")
            .append(File.separator).append("main").append(File.separator).append("resources").append(File.separator).toString();

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
    
    public ArrayList<String> fill() {
        RandomNumber random = new RandomNumber();
        Birthdate birthdate = new Birthdate("dd-MM-yyyy");
        birthdate.set();

        ArrayList<String> result = new ArrayList<>();

        result.add(names.get(this.currentSex)[random.generateWithoutStart(names.get(this.currentSex).length)]);
        result.add(surnames.get(this.currentSex)[random.generateWithoutStart(surnames.get(this.currentSex).length)]);
        result.add(patronNames.get(this.currentSex)[random.generateWithoutStart(patronNames.get(this.currentSex).length)]);
        result.add(Long.toString(birthdate.getAge()));
        result.add(this.currentSex);
        result.add(birthdate.get());
        result.add(new ITNGenerator(77).getString());
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
