package de.thomaskoscheck.wgverwaltung;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText product;
    EditText price;
    Settings settings;
    TextView leftCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        product = findViewById(R.id.product);
        price = findViewById(R.id.price);
        settings = SettingsStore.load(this);
        leftCredit = findViewById(R.id.leftCredit);
        sendRequest("yoyo", 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clear(View view) {
        clearInput();
    }

    private void clearInput() {
        product.setText("");
        price.setText("");
    }

    public void send(View view) {
        final String productString = product.getText().toString();
        final String priceString = price.getText().toString();
        if (priceString.equals("") && productString.equals("")) {
            Toast errorNoPrice = Toast.makeText(this, R.string.errorNoPriceAndProductSet, Toast.LENGTH_LONG);
            errorNoPrice.show();
        } else if (priceString.equals("")) {
            Toast errorNoPrice = Toast.makeText(this, R.string.errorNoPriceSet, Toast.LENGTH_LONG);
            errorNoPrice.show();
        } else if (productString.equals("")) {
            Toast errorNoPrice = Toast.makeText(this, R.string.errorNoProductSet, Toast.LENGTH_LONG);
            errorNoPrice.show();
        } else {
            buildAlertDialog(priceString, productString);
        }
    }

    private void buildAlertDialog(final String priceString, final String productString){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle(R.string.SendConfirmation);
        builder.setMessage(R.string.SendConfirmationText);

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                double price = Double.parseDouble(priceString);
                sendRequest(productString, price);
            }
        });

        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setIcon(android.R.drawable.ic_input_add);
        builder.show();
    }

    private void sendRequest(String description, double price) {
        RequestDetails requestDetails = new RequestDetails(description, price);
        SendRequestDetails sendRequestDetails = new SendRequestDetails(leftCredit);
        sendRequestDetails.execute(getGETString(requestDetails));
    }


    private String getGETString(RequestDetails requestDetails) {
        Settings settings = SettingsStore.load(this);
        String GETData = "?";
        GETData += "product=" + requestDetails.getProduct() + "&";
        GETData += "price=" + requestDetails.getPrice() + "&";
        GETData += "requester=" + settings.getRequester() + "&";
        GETData += "password=" + settings.getPassword();
        Log.d("TK", GETData);
        return GETData;
    }
}