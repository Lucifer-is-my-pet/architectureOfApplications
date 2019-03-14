package main.modules;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

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

    private String getWords(String wordsAndDigits) {
        if (Pattern.matches(".*\\d.*", wordsAndDigits)) {
            String[] splitted = wordsAndDigits.split("\\d+");
            return StringUtils.join(splitted).trim();
        } else {
            return wordsAndDigits;
        }
    }

    @Override
    public ArrayList<String> deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();
        ArrayList<String> result = new ArrayList<>();

        result.add(StringUtils.capitalize(jsonObject.get("name").getAsJsonObject().get("first").getAsString()));
        result.add(StringUtils.capitalize(jsonObject.get("name").getAsJsonObject().get("last").getAsString()));
        result.add("");
        result.add(jsonObject.get("dob").getAsJsonObject().get("age").getAsString());
        result.add(StringUtils.capitalize(jsonObject.get("gender").getAsString()));

        Birthdate bd = new Birthdate("dd-MM-yyy"); // конвертируем в требуемый формат
        String actualBd = jsonObject.get("dob").getAsJsonObject().get("date").getAsString().split("T")[0];
        result.add(bd.getFormattedBirthdate(actualBd, "yyyy-MM-dd"));

        result.add(""); // ИНН
        result.add(jsonObject.get("location").getAsJsonObject().get("postcode").getAsString());
        result.add(this.countries.get(jsonObject.get("nat").getAsString()));
        result.add(StringUtils.capitalize(jsonObject.get("location").getAsJsonObject().get("state").getAsString()));
        result.add(StringUtils.capitalize(jsonObject.get("location").getAsJsonObject().get("city").getAsString()));
        result.add(StringUtils.capitalize(getWords(jsonObject.get("location").getAsJsonObject().get("street").getAsString())));
        result.add(jsonObject.get("location").getAsJsonObject().get("street").getAsString().split(" ")[0]);
        return result;
    }

}
