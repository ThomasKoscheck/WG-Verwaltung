package de.thomaskoscheck.wgverwaltung.Setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.thomaskoscheck.wgverwaltung.R;

public class SettingsActivity extends AppCompatActivity {
    EditText passwordField;
    EditText requesterField;
    EditText serverField;
    EditText portField;
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        passwordField = findViewById(R.id.password);
        requesterField = findViewById(R.id.requester);
        serverField = findViewById(R.id.server);
        portField = findViewById(R.id.port);

        portField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    save();
                    handled = true;
                }
                return handled;
            }
        });

        settings = SettingsStore.load(this);
        passwordField.setText(settings.getPassword());
        requesterField.setText(settings.getRequester());
        serverField.setText(settings.getServer());
        if(settings.getPort()!=0) {
            portField.setText(String.valueOf(settings.getPort()));
        }
    }

    public void save(View view) {
        save();
    }

    private void save(){
        String requester = requesterField.getText().toString();
        String password = passwordField.getText().toString();
        String server = serverField.getText().toString();
        int port = Integer.parseInt(portField.getText().toString());

        SettingsStore.addValue(getString(R.string.requesterKey), requester, this);
        SettingsStore.addValue(getString(R.string.passwordKey), password, this);
        SettingsStore.addValue(getString(R.string.serverKey), server, this);
        SettingsStore.addValue(getString(R.string.portKey), port, this);

        settings.setRequester(requester);
        settings.setPassword(password);
        settings.setServer(server);
        settings.setPort(port);
        Toast settingsSaved = Toast.makeText(this, R.string.settingsSaved, Toast.LENGTH_LONG);
        settingsSaved.show();
    }
}