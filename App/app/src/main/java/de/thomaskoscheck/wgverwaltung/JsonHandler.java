package de.thomaskoscheck.wgverwaltung;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.thomaskoscheck.wgverwaltung.server_communication.Expense;
import de.thomaskoscheck.wgverwaltung.server_communication.SendDetails;
import de.thomaskoscheck.wgverwaltung.server_communication.ServerResponse;

public class JsonHandler {
    private static final String CREDIT ="credit";
    private static final String EXPENSES ="expenses";
    private static final String REQUESTER ="requester";
    private static final String PRODUCT ="product";
    private static final String DONE = "done";
    private static final String PRICE ="price";
    private static final String ID = "id";

    public static ServerResponse parseServerResponse(String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        String credit = response.getString(CREDIT);
        JSONArray expensesJson = response.getJSONArray(EXPENSES);
        Expense[] expenses = new Expense[expensesJson.length()];

        for (int i = 0; i < expensesJson.length(); i++) {
            String requester = expensesJson.getJSONObject(i).getString(REQUESTER);
            String product = expensesJson.getJSONObject(i).getString(PRODUCT);
            String priceString = expensesJson.getJSONObject(i).getString(PRICE);
            double price = Double.parseDouble(priceString);
            String idString = expensesJson.getJSONObject(i).getString(ID);
            int id = Integer.parseInt(idString);
            expenses[i] = new Expense(requester, product, price, id);
        }
        return new ServerResponse(credit, expenses);
    }

    public static String generateJsonString(SendDetails sendDetails) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(REQUESTER, sendDetails.getSettings().getRequester());
            jsonRequest.put(PRICE, sendDetails.getRequestDetails().getPrice());
            jsonRequest.put(PRODUCT, sendDetails.getRequestDetails().getProduct());
            jsonRequest.put(DONE, sendDetails.getRequestDetails().getDone());
            jsonRequest.put(ID, sendDetails.getRequestDetails().getId());
            return jsonRequest.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}