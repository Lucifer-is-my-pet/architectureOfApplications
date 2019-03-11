package main.modules;

public class Person {

    private String name;
    private String surname;
    private String patronymicName;
    private int age;
    private Sex sex;
    private String birthdate;
    private String ITN;
    private String postalIndex;
    private String country;
    private String region;
    private String city;
    private String street;
    private int house;
    private String flat;

    Person(String[] names, int age, Sex sex, String birthdate, String ITN, String[] address, int house, String flat) {
        this.name = names[0];
        this.surname = names[1];
        this.patronymicName = names[2];
        this.age = age;
        this.sex = sex;
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
}

enum Sex {
    MALE,
    FEMALE
}