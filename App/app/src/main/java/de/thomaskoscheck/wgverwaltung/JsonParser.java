package de.thomaskoscheck.wgverwaltung;

import org.json.*;

public class JsonParser {
    static ServerResponse parseJson(String json) throws JSONException {
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
}
