package de.thomaskoscheck.wgverwaltung;

import org.json.*;

import de.thomaskoscheck.wgverwaltung.ServerCommunication.Expense;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.SendDetails;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.ServerResponse;

public class JsonHandler {

    public static ServerResponse parseJson(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        String credit = response.getString("credit");
        String newestAppVersion = response.getString("newestAppVersion");
        JSONArray expensesJson = response.getJSONArray("expenses");
        Expense[] expenses = new Expense[expensesJson.length()];

        for (int i = 0; i < expensesJson.length(); i++) {
            String requester = expensesJson.getJSONObject(i).getString("requester");
            String product = expensesJson.getJSONObject(i).getString("product");
            String priceString = expensesJson.getJSONObject(i).getString("price");
            double price = Double.parseDouble(priceString);
            expenses[i] = new Expense(requester, product, price);
        }
        return new ServerResponse(credit, newestAppVersion, expenses);
    }

    public static String generateJsonString(SendDetails sendDetails) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("requester", sendDetails.getSettings().getRequester());
            jsonRequest.put("price", sendDetails.getRequestDetails().getPrice());
            jsonRequest.put("product", sendDetails.getRequestDetails().getProduct());
            return jsonRequest.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
