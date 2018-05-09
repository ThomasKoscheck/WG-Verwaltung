package de.thomaskoscheck.wgverwaltung;
import org.json.*;

public class JsonParser {


    private void parseJson(String json) {
        JSONObject response = new JSONObject(json);
        String credit = response.getString("pageName");
        String newestAppVersion = response.getString("pageName");
        
        JSONArray arr = response.getJSONArray("posts");
        for (int i = 0; i < arr.length(); i++) {
            String post_id = arr.getJSONObject(i).getString("post_id");
        }
    }
}
