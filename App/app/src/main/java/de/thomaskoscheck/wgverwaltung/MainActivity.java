package de.thomaskoscheck.wgverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        product = findViewById(R.id.product);
        price = findViewById(R.id.price);
        settings = SettingsStore.load(this);
        leftCredit = findViewById(R.id.leftCredit);
        sendRequest("", 0);
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
        String product = this.product.getText().toString();
        String priceString = price.getText().toString();
        if (priceString.equals("")&&product.equals("")) {
            Toast errorNoPrice = Toast.makeText(this, R.string.errorNoPriceAndProductSet, Toast.LENGTH_LONG);
            errorNoPrice.show();
        }
        else if(priceString.equals("")){
            Toast errorNoPrice = Toast.makeText(this, R.string.errorNoPriceSet, Toast.LENGTH_LONG);
            errorNoPrice.show();
        }
        else if(product.equals("")){
            Toast errorNoPrice = Toast.makeText(this, R.string.errorNoProductSet, Toast.LENGTH_LONG);
            errorNoPrice.show();
        }

        double price = Double.parseDouble(priceString);
        sendRequest(product, price);
    }

    private void sendRequest(String description, double price) {

        String postData = "?";
        postData += "product=" + description + "&";
        postData += "price=" + price + "&";
        postData += "requester=" + settings.getRequester() + "&";
        postData += "password=" + settings.getPassword();

        SendRequestDetails sendRequestDetails = new SendRequestDetails(leftCredit);
        sendRequestDetails.execute("", postData);
    }
}