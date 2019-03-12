package main.modules;
import com.google.gson.*;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test {

    public static class Entity {
        volatile int id;
        Map<String, String> arr;
        String name;
        transient long random;

        public Entity(int id, String name, Map<String, String> arr) {
            this.id = id;
            this.name = name;
            this.arr = arr;
        }

        String getName() {
            return this.name;
        }

        String getId() {
            return Integer.toString(this.id);
        }
    }

    public static void main(String[]args) {
//        Map<String, String> arr = new HashMap<String, String>() {{
//            put("date", "1991-07-26");
//        }};
//        Entity entity = new Entity(100, "name", arr);
//        entity.random = 1234;
//
//        Gson gson = new Gson();
//        String stringGson = gson.toJson(entity); // {"id":100,"name":"name"}
//        JsonObject jsonObject = gson.toJsonTree(entity).getAsJsonObject();
//        Entity read = gson.fromJson(stringGson, Entity.class);
//        String date = jsonObject.get("arr").getAsJsonObject().get("date").getAsString();
//        System.out.println(stringGson);
//        System.out.println(date);
//
//        Birthdate bd = new Birthdate("dd-MM-yyy");
//        System.out.println(bd.getFormattedBirthdate(date, "yyyy-MM-dd"));

        System.out.println("4456 frederick ave".split("\\d+ ")[1]);
        System.out.println("karlsrudveien 1965".split("\\d+")[0]);

//        URIBuilder b = null;
//        try {
//            b = new URIBuilder("https://randomuser.me/api/");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        b.addParameter("results", "2");
//        b.addParameter("inc", "gender,name");
//
//        try {
//            URL url = b.build().toURL();
//            System.out.println(url);
//        } catch (URISyntaxException | MalformedURLException e) {
//            e.printStackTrace();
//        }

//        try {
//            API api = new API("https://randomuser.me/api/");
//            HashMap<String, String> params = new HashMap<>();
//            params.put("inc", "gender,name,location,nat,dob,id");
//            System.out.println(api.getResponse(2, params, "noinfo"));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }
}
