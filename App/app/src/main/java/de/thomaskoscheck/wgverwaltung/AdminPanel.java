package de.thomaskoscheck.wgverwaltung;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import de.thomaskoscheck.wgverwaltung.Listener.DataProcessedListener;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.Expense;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.GetDetails;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.GetServerData;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.ServerResponse;
import de.thomaskoscheck.wgverwaltung.Setting.Settings;
import de.thomaskoscheck.wgverwaltung.Setting.SettingsStore;

public class AdminPanel extends AppCompatActivity {
    private ListView listView;
    private Settings settings;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.entries);
    }

    protected void onResume() {
        super.onResume();
        settings = SettingsStore.load(this);
        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        GetServerData getServerData = new GetServerData();
        getServerData.setDataProcessedListener(new DataProcessedListener() {
            @Override
            public void onDataLoaded(ServerResponse serverResponse) {
                if (serverResponse != null) {
                    ArrayList<HashMap<String, String>> list = new ArrayList<>();
                    Expense[] expenses = serverResponse.getExpenses();
                    HashMap<String, String> item;
                    for (Expense expense : expenses) {
                        Currency currency = Currency.getInstance(Locale.getDefault());
                        item = new HashMap<>();
                        item.put("product", expense.getProduct());
                        item.put("price", String.valueOf(expense.getPrice() + currency.getSymbol()));
                        item.put("requester", getString(R.string.requester) + ": " + expense.getRequester());
                        list.add(item);
                    }
                    simpleAdapter = new SimpleAdapter(getApplicationContext(),
                            list,
                            R.layout.list_item,
                            new String[]{"product", "requester", "price"},
                            new int[]{R.id.product, R.id.requester, R.id.price});
                    listView.setAdapter(simpleAdapter);
                }
            }
        });
        getServerData.execute(new GetDetails(settings));
    }
}