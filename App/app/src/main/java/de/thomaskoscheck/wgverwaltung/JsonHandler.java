package de.thomaskoscheck.wgverwaltung;

import org.json.*;

import de.thomaskoscheck.wgverwaltung.ServerCommunication.Expense;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.SendDetails;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.ServerResponse;
import de.thomaskoscheck.wgverwaltung.Setting.SettingsActivity;

public class JsonHandler {

    private static final String CREDIT ="credit";
    private static final String EXPENSES ="expenses";
    private static final String REQUESTER ="requester";
    private static final String NEWESTAPPVERSION ="newestAppVersion";
    private static final String PRODUCT ="product";
    private static final String PRICE ="price";

    public static ServerResponse parseJson(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        String credit = response.getString(CREDIT);
        String newestAppVersion = response.getString(NEWESTAPPVERSION);
        JSONArray expensesJson = response.getJSONArray(EXPENSES);
        Expense[] expenses = new Expense[expensesJson.length()];

        for (int i = 0; i < expensesJson.length(); i++) {
            String requester = expensesJson.getJSONObject(i).getString(REQUESTER);
            String product = expensesJson.getJSONObject(i).getString(PRODUCT);
            String priceString = expensesJson.getJSONObject(i).getString(PRICE);
            double price = Double.parseDouble(priceString);
            expenses[i] = new Expense(requester, product, price);
        }
        return new ServerResponse(credit, newestAppVersion, expenses);
    }

    public static String generateJsonString(SendDetails sendDetails) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(REQUESTER, sendDetails.getSettings().getRequester());
            jsonRequest.put(PRICE, sendDetails.getRequestDetails().getPrice());
            jsonRequest.put(PRODUCT, sendDetails.getRequestDetails().getProduct());
            return jsonRequest.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}