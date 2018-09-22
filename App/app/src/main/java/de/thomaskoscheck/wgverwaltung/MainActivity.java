package de.thomaskoscheck.wgverwaltung;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Currency;
import java.util.Locale;

import de.thomaskoscheck.wgverwaltung.listener.AlertDialogAnswerSelectedListener;
import de.thomaskoscheck.wgverwaltung.listener.DataProcessedListener;
import de.thomaskoscheck.wgverwaltung.listener.DataSentListener;
import de.thomaskoscheck.wgverwaltung.server_communication.GetDetails;
import de.thomaskoscheck.wgverwaltung.server_communication.GetServerData;
import de.thomaskoscheck.wgverwaltung.server_communication.RequestDetails;
import de.thomaskoscheck.wgverwaltung.server_communication.SendDetails;
import de.thomaskoscheck.wgverwaltung.server_communication.SendRequestDetails;
import de.thomaskoscheck.wgverwaltung.server_communication.ServerResponse;
import de.thomaskoscheck.wgverwaltung.setting.Settings;
import de.thomaskoscheck.wgverwaltung.setting.SettingsActivity;
import de.thomaskoscheck.wgverwaltung.setting.SettingsStore;

public class MainActivity extends AppCompatActivity {
  private EditText product;
  private EditText price;
  private Settings settings;
  private TextView leftCredit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    product = findViewById(R.id.product);
    price = findViewById(R.id.price);
    leftCredit = findViewById(R.id.leftCredit);
    price.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        text = text.replace(getCurrencySymbol(), "");
        if (text.endsWith("+")) {
          text = text.substring(0, text.length() - 1);
          int position = text.lastIndexOf("+");
          if (position != -1) {
            double first = Double.parseDouble(text.substring(0, position));
            if (position + 1 != text.length()) {
              double second = Double.parseDouble(text.substring(position + 1, text.length()));
              price.setText(String.format("%s%s", first + second, getCurrencySymbol()));
              price.setSelection(price.getText().length());
            }
          }
        }
      }
    });
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


  protected void onResume() {
    super.onResume();
    settings = SettingsStore.load(this);
    fetchDataFromServer();
  }


  public void clear(View view) {
    clearInput();
  }

  private void clearInput() {
    product.setText("");
    price.setText("");
  }

  public void send(View view) {
    final String descriptionString = product.getText().toString();
    final String priceString = price.getText().toString();
    if ("".equals(priceString) && "".equals(descriptionString)) {
      Toast errorNoPrice = Toast.makeText(this, R.string.errorNoPriceAndProductSet, Toast.LENGTH_LONG);
      errorNoPrice.show();
    } else if ("".equals(priceString)) {
      Toast errorNoPrice = Toast.makeText(this, R.string.errorNoPriceSet, Toast.LENGTH_LONG);
      errorNoPrice.show();
    } else if ("".equals(descriptionString)) {
      Toast errorNoPrice = Toast.makeText(this, R.string.errorNoProductSet, Toast.LENGTH_LONG);
      errorNoPrice.show();
    } else {
      buildAlertDialog(R.string.SendConfirmation, R.string.SendConfirmationText, android.R.string.yes, android.R.string.no, android.R.drawable.ic_input_add, new AlertDialogAnswerSelectedListener() {
        @Override
        public void onAnswerSelected(boolean answer) {
          if (answer) {
            sendRequest(descriptionString, Double.parseDouble(priceString));
            fetchDataFromServer();
          }
        }
      });
    }
  }

  public void refreshLeftCredit(View view) {
    Toast refreshing = Toast.makeText(this, R.string.refreshing, Toast.LENGTH_SHORT);
    refreshing.show();
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

  private void sendRequest(String description, double price) {
    RequestDetails requestDetails = new RequestDetails(description, price);
    SendRequestDetails sendRequestDetails = new SendRequestDetails();
    sendRequestDetails.setDataSentListener(new DataSentListener() {
      @Override
      public void onDataSent(boolean succeeded) {
        fetchDataFromServer();
        clearInput();
      }
    });
    sendRequestDetails.execute(new SendDetails(settings, requestDetails));
  }

  private void fetchDataFromServer() {
    GetServerData getServerData = new GetServerData();
    getServerData.setDataProcessedListener(new DataProcessedListener() {
      @Override
      public void onDataLoaded(ServerResponse serverResponse) {
        if (serverResponse != null) {
          leftCredit.setText(String.format("%s%s", serverResponse.getCredit(), getCurrencySymbol()));
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