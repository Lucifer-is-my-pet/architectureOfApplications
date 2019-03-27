package modules.API;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class API {

    private String url;

    public API(String url) {
        this.url = url;
    }

    public String getResponse(int results, HashMap<String, String> params, String additionalParam) {
        HttpRequest request = Unirest.get(this.url)
                .queryString("results", results);
        HttpResponse jsonResponse = null;

        for (Map.Entry<String, String> pair : params.entrySet()) {
            request = request.queryString(pair.getKey(), pair.getValue());
        }

        request = request.queryString(additionalParam, "");

//        System.out.println(request.getUrl());

        try {
            jsonResponse = request.asString(); // https://github.com/Kong/unirest-java/issues/198#issuecomment-297657504 зависимости
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return jsonResponse.getBody().toString();
    }
}
