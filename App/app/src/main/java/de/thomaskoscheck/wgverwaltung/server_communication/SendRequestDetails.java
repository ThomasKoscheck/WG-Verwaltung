package de.thomaskoscheck.wgverwaltung.server_communication;

import android.os.AsyncTask;

import de.thomaskoscheck.wgverwaltung.JsonHandler;
import de.thomaskoscheck.wgverwaltung.listener.DataSentListener;
import de.thomaskoscheck.wgverwaltung.setting.Settings;

public class SendRequestDetails extends AsyncTask<SendDetails, Void, Boolean> {
    private DataSentListener dataSentListener;
    private Settings settings;

    @Override
    protected Boolean doInBackground(SendDetails... params) {
        settings = params[0].getSettings();

        String jsonString = JsonHandler.generateJsonString(params[0]);

        ServerConnection serverConnection = new ServerConnection(settings);
        serverConnection.sendData(jsonString);

        return true;
    }

    public void setDataSentListener(DataSentListener dataSentListener) {
        this.dataSentListener = dataSentListener;
    }

    @Override
    protected void onPostExecute(Boolean succeeded) {
        super.onPostExecute(succeeded);
        dataSentListener.onDataSent(succeeded);
    }
}