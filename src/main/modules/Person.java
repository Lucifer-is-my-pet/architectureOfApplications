package main.modules;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class Person {

    private String name; // first
    private String surname; // last
    private String patronymicName;
    private int age;
    private Gender gender;
    private String birthdate; // date
    private String ITN;
    private String postalIndex;
    private String country;
    private String region;
    private String city;
    private String street;
    private int house;
    private String flat;

    private Map<String, String> countries = new HashMap<String, String>() {{
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

    Person(String name, String surname, String patonName, int age, Gender gender, String birthdate, String ITN, String[] address, int house, String flat) {
        this.name = name;
        this.surname = surname;
        this.patronymicName = patonName;
        this.age = age;
        this.gender = gender;
        this.birthdate = birthdate;
        this.ITN = ITN;
        this.postalIndex = address[0];
        this.country = address[1];
        this.region = address[2];
        this.city = address[3];
        this.street = address[4];
        this.house = house;
        this.flat = flat;
    }

    Person(String name, String surname, int age, Gender gender, String birthdate, String[] address, int house) {
        this.name = name;
        this.surname = surname;
        this.patronymicName = "";
        this.age = age;
        this.gender = gender;
        this.birthdate = birthdate;
        this.ITN = "";
        this.postalIndex = address[0];
        this.country = address[1];
        this.region = address[2];
        this.city = address[3];
        this.street = address[4];
        this.house = house;
        this.flat = "";
    }
}

enum Gender {
    MALE,
    FEMALE
}