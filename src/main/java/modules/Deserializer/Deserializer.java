package modules.Deserializer;

import com.google.gson.*;
import modules.Birthdate.Birthdate;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

public class Deserializer implements JsonDeserializer<ArrayList> {

    private final Map<String, String> COUNTRIES = new HashMap<String, String>() {{
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

    private boolean hasDigits(String string) {
        return Pattern.matches(".*\\d.*", string);
    }

    private String getWords(String wordsAndDigits) {
        String[] splitted = wordsAndDigits.split("\\d+");
        return StringUtils.join(splitted).trim();
    }

    private String getDigits(String wordsAndDigits) {
        String[] splitted = wordsAndDigits.split("\\D+");
        return StringUtils.join(splitted).trim();
    }

    @Override
    public ArrayList<String> deserialize(JsonElement json, Type typeofT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();
        ArrayList<String> result = new ArrayList<>();

        result.add(StringUtils.capitalize(jsonObject.get("name").getAsJsonObject().get("first").getAsString()));
        result.add(StringUtils.capitalize(jsonObject.get("name").getAsJsonObject().get("last").getAsString()));

        if (jsonObject.has("middle")) {
            result.add(StringUtils.capitalize(jsonObject.get("name").getAsJsonObject().get("middle").getAsString()));
        } else {
            result.add("-");
        }
        result.add(jsonObject.get("dob").getAsJsonObject().get("age").getAsString());
        result.add(StringUtils.capitalize(jsonObject.get("gender").getAsString()));

        Birthdate bd = new Birthdate("dd-MM-yyyy"); // конвертируем в требуемый формат
        String actualBd = jsonObject.get("dob").getAsJsonObject().get("date").getAsString().split("T")[0];
        result.add(bd.getFormattedBirthdate(actualBd, "yyyy-MM-dd"));

        result.add(""); // ИНН
        result.add(jsonObject.get("location").getAsJsonObject().get("postcode").getAsString());
        result.add(this.COUNTRIES.get(jsonObject.get("nat").getAsString()));
        result.add(StringUtils.capitalize(jsonObject.get("location").getAsJsonObject().get("state").getAsString()));
        result.add(StringUtils.capitalize(jsonObject.get("location").getAsJsonObject().get("city").getAsString()));

        String street = jsonObject.get("location").getAsJsonObject().get("street").getAsString();

        if (hasDigits(street)) {
            result.add(StringUtils.capitalize(getWords(street)));
            result.add(StringUtils.capitalize(getDigits(street)));
        } else {
            result.add(StringUtils.capitalize(street));
            result.add("-");
        }

        return result;
    }
}
