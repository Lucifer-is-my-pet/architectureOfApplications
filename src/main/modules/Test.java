package main.modules;
import com.google.gson.*;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class Test {

    public static class Entity {
        volatile int id;
        String name;
        transient long random;

        public Entity(int id, String name) {
            this.id = id;
            this.name = name;
        }

        String getName() {
            return this.name;
        }

        String getId() {
            return Integer.toString(this.id);
        }
    }

    public static void main(String[]args) {
//        Entity entity = new Entity(100, "name");
//        entity.random = 1234;
//
//        Gson gson = new Gson();
//        String json = gson.toJson(entity); // {"id":100,"name":"name"}
//        Entity read = gson.fromJson(json, Entity.class);
//        System.out.println(json);
//        System.out.println(read.getId());

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

        try {
            API api = new API("https://randomuser.me/api/");
            HashMap<String, String> params = new HashMap<>();
            params.put("inc", "gender,name,location,nat,dob");
            System.out.println(api.getResponse(2, params, "noinfo"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
