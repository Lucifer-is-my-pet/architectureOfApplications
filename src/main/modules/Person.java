package main.modules;

import java.util.HashMap;
import java.util.Map;

public class Person {

    private String name;
    private String surname;
    private String patronymicName;
    private String age;
    private String gender;
    private String birthdate;
    private String ITN;
    private String postcode;
    private String country;
    private String region;
    private String city;
    private String street;
    private String house;
    private String flat;

    private final Map<String, String> countries = new HashMap<String, String>() {{
        put("AU", "Австралия");
        put("BR", "Бразилия");
        put("CA", "Канада");
        put("CH", "Швейцария");
        put("DE", "Германия");
        put("DK", "Дания");
        put("ES", "Испания");
        put("FI", "Финляндия");
        put("FR", "Франция");
        put("GB", "Великобритания");
        put("IE", "Ирландия");
        put("IR", "Иран");
        put("NO", "Норвегия");
        put("NL", "Нидерланды");
        put("NZ", "Новая Зеландия");
        put("TR", "Турция");
        put("US", "США");
    }};

    Person(String name, String surname, String patonName, String age, String gender, String birthdate, String ITN, String[] address, String house, String flat) {
        this.name = name;
        this.surname = surname;
        this.patronymicName = patonName;
        this.age = age;
        this.gender = gender;
        this.birthdate = birthdate;
        this.ITN = ITN;
        this.postcode = address[0];
        this.country = address[1];
        this.region = address[2];
        this.city = address[3];
        this.street = address[4];
        this.house = house;
        this.flat = flat;
    }

    Person() {
        this.patronymicName = "";
        this.flat = new RandomNumber(1, 1000).getString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setITN(String ITN) {
        this.ITN = ITN;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCountry(String country, boolean isCode) {
        if (isCode) {
            this.country = this.countries.get(country);
        } else {
            this.country = country;
        }

    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouse(String house) {
        this.house = house;
    }

}