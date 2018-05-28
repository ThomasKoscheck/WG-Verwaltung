package de.thomaskoscheck.wgverwaltung;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import de.thomaskoscheck.wgverwaltung.Listener.AlertDialogAnswerSelectedListener;
import de.thomaskoscheck.wgverwaltung.Listener.DataProcessedListener;
import de.thomaskoscheck.wgverwaltung.Listener.DataSentListener;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.Expense;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.GetDetails;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.GetServerData;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.RequestDetails;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.SendDetails;
import de.thomaskoscheck.wgverwaltung.ServerCommunication.SendRequestDetails;
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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView productTV = view.findViewById(R.id.product);
                final String product = productTV.getText().toString();

                TextView priceTV = view.findViewById(R.id.price);
                String priceString = priceTV.getText().toString();
                priceString = priceString.replace(getCurrencySymbol(), "");
                final double price = Double.parseDouble(priceString);

                TextView requesterTV = view.findViewById(R.id.requester);
                String requesterRaw = requesterTV.getText().toString();
                final String requester = requesterRaw.replace(getString(R.string.requester) + ": ", "");

                buildAlertDialog(R.string.markDoneConfirmation, R.string.markDoneConfirmationText, android.R.string.yes, android.R.string.no, android.R.drawable.stat_sys_warning, new AlertDialogAnswerSelectedListener() {
                    @Override
                    public void onAnswerSelected(boolean answer) {
                        markEntryAsDone(product, requester, price);
                    }
                });
                return false;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        settings = SettingsStore.load(this);
        fetchDataFromServer();
    }


    private void buildAlertDialog(int titleId, int messageId, int positiveButtonId, int negativeButtonId, int iconId, final AlertDialogAnswerSelectedListener alertDialogAnswerSelectedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle(titleId);
        builder.setMessage(messageId);

        builder.setPositiveButton(positiveButtonId, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialogAnswerSelectedListener.onAnswerSelected(true);
            }
        });

        builder.setNegativeButton(negativeButtonId, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialogAnswerSelectedListener.onAnswerSelected(false);
            }
        });
        builder.setIcon(iconId);
        builder.show();
    }

    private void markEntryAsDone(String description, String requester, double price) {
        RequestDetails requestDetails = new RequestDetails(description, requester, price, true);
        SendRequestDetails sendDone = new SendRequestDetails();
        sendDone.setDataSentListener(new DataSentListener() {
            @Override
            public void onDataSent(boolean succeeded) {
                fetchDataFromServer();
            }
        });
        sendDone.execute(new SendDetails(settings, requestDetails));
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
                        item = new HashMap<>();
                        item.put("product", expense.getProduct());
                        item.put("price", String.valueOf(expense.getPrice() + getCurrencySymbol()));
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

    private String getCurrencySymbol() {
        Currency currency = Currency.getInstance(Locale.getDefault());
        return currency.getSymbol();
    }
}