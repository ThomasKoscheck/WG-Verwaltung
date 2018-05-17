package de.thomaskoscheck.wgverwaltung;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

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
    ListAdapter adapter;

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
                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, serverResponse.getExpenses());
                    listView.setAdapter(adapter);
                }
            }
        });
        getServerData.execute(new GetDetails(settings));
    }
}
