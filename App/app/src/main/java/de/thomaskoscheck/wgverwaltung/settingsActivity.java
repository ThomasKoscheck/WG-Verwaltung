package de.thomaskoscheck.wgverwaltung;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class settingsActivity extends AppCompatActivity {
    EditText passwordField;
    EditText requesterField;
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        passwordField = findViewById(R.id.password);
        requesterField = findViewById(R.id.requester);
        settings = SettingsStore.load(this);
        Log.d("TK", "password: " + settings.getPassword() + " requester: " + settings.getRequester());
        passwordField.setText(settings.getPassword());
        requesterField.setText(settings.getRequester());
    }

    public void save(View view) {
        Log.d("TK", "Saving");
        String password = passwordField.getText().toString();
        String requester = requesterField.getText().toString();
        Log.d("TK", "password: " + password + " requester: " + requester);
        SettingsStore.addValue(getString(R.string.requesterKey), requester, this);
        SettingsStore.addValue(getString(R.string.passwordKey), password, this);
    }
}
