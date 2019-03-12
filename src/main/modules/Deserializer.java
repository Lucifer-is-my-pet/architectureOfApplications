package main.modules;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.*;

public class Deserializer implements JsonDeserializer<ArrayList> {

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

    private String getStreet(String streetAndNumber) {
        return null; // todo
    }

//    @Override
//    public Person deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//
//        Person person = new Person();
//        person.setName(jsonObject.get("name").getAsJsonObject().get("first").getAsString());
//        person.setSurname(jsonObject.get("name").getAsJsonObject().get("last").getAsString());
//        person.setAge(jsonObject.get("dob").getAsJsonObject().get("age").getAsString());
//        person.setGender(jsonObject.get("gender").getAsString());
//
//        Birthdate bd = new Birthdate("dd-MM-yyy");
//        String actualBd = jsonObject.get("dob").getAsJsonObject().get("date").getAsString().split("T")[0];
//        person.setBirthdate(bd.getFormattedBirthdate(actualBd, "yyyy-MM-dd"));
//
//        person.setITN(jsonObject.get("id").getAsJsonObject().get("value").getAsString()); // ???
//        person.setPostcode(jsonObject.get("location").getAsJsonObject().get("postcode").getAsString());
//        person.setCountry(jsonObject.get("nat").getAsString(), true);
//        person.setRegion(jsonObject.get("location").getAsJsonObject().get("state").getAsString());
//        person.setCity(jsonObject.get("location").getAsJsonObject().get("city").getAsString());
//        person.setStreet(jsonObject.get("location").getAsJsonObject().get("street").getAsString().split("\\d+ ")[1]); // улицы вида '4456 frederick ave'
//        person.setHouse(jsonObject.get("location").getAsJsonObject().get("street").getAsString().split(" ")[0]);
//
//        return person;
//    }

    @Override
    public ArrayList<String> deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();
        ArrayList<String> result = new ArrayList<>();

        result.add(StringUtils.capitalize(jsonObject.get("name").getAsJsonObject().get("first").getAsString()));
        result.add(StringUtils.capitalize(jsonObject.get("name").getAsJsonObject().get("last").getAsString()));
        result.add("");
        result.add(jsonObject.get("dob").getAsJsonObject().get("age").getAsString());
        result.add(StringUtils.capitalize(jsonObject.get("gender").getAsString()));

        Birthdate bd = new Birthdate("dd-MM-yyy");
        String actualBd = jsonObject.get("dob").getAsJsonObject().get("date").getAsString().split("T")[0];
        result.add(bd.getFormattedBirthdate(actualBd, "yyyy-MM-dd"));

//        System.out.println(jsonObject.get("id"));
//        System.out.println(jsonObject.get("location"));

        if (jsonObject.get("id").getAsJsonObject().get("value").isJsonNull()) {
            result.add("");
        } else {
            result.add(jsonObject.get("id").getAsJsonObject().get("value").getAsString()); //??
        }

        result.add(jsonObject.get("location").getAsJsonObject().get("postcode").getAsString());
        result.add(this.countries.get(jsonObject.get("nat").getAsString()));
        result.add(StringUtils.capitalize(jsonObject.get("location").getAsJsonObject().get("state").getAsString()));
        result.add(StringUtils.capitalize(jsonObject.get("location").getAsJsonObject().get("city").getAsString()));
        try {
            result.add(StringUtils.capitalize(jsonObject.get("location").getAsJsonObject().get("street").getAsString().split("\\d+")[1]));
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println(jsonObject.get("location"));
            result.add(StringUtils.capitalize(jsonObject.get("location").getAsJsonObject().get("street").getAsString()));
        }

        result.add(jsonObject.get("location").getAsJsonObject().get("street").getAsString().split(" ")[0]);
        result.add(new RandomNumber(1, 1000).getString()); // ??

        return result;
    }

}
