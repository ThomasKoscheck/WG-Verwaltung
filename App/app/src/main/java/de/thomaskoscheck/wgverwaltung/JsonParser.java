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
            String product = expensesJson.getJSONObject(i).getString("requester");
            String priceString = expensesJson.getJSONObject(i).getString("requester");
            String doneString = expensesJson.getJSONObject(i).getString("requester");
            double price = Double.parseDouble(priceString);
            boolean done = Boolean.parseBoolean(doneString);
            expenses[i] = new Expense(requester, product, price, done);
        }
        return new ServerResponse(credit, newestAppVersion, expenses);
    }
}
