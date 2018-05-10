package de.thomaskoscheck.wgverwaltung;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    EditText passwordField;
    EditText requesterField;
    EditText serverField;
    EditText portField;
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        passwordField = findViewById(R.id.password);
        requesterField = findViewById(R.id.requester);
        serverField = findViewById(R.id.server);
        portField = findViewById(R.id.port);
        settings = SettingsStore.load(this);
        Log.d("TK", "password: " + settings.getPassword() + " requester: " + settings.getRequester());
        passwordField.setText(settings.getPassword());
        requesterField.setText(settings.getRequester());
    }

    public void save(View view) {
        Log.d("TK", "Saving");
        String password = passwordField.getText().toString();
        String requester = requesterField.getText().toString();
        String server = serverField.getText().toString();
        int port = Integer.parseInt(portField.getText().toString());

        SettingsStore.addValue(getString(R.string.requesterKey), requester, this);
        SettingsStore.addValue(getString(R.string.passwordKey), password, this);
        SettingsStore.addValue(getString(R.string.serverKey), server, this);
        SettingsStore.addValue(getString(R.string.portKey), port, this);
    }
}