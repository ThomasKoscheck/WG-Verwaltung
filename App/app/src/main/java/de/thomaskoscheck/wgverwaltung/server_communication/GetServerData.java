package de.thomaskoscheck.wgverwaltung.server_communication;

import android.os.AsyncTask;

import de.thomaskoscheck.wgverwaltung.JsonHandler;
import de.thomaskoscheck.wgverwaltung.listener.DataProcessedListener;
import de.thomaskoscheck.wgverwaltung.setting.Settings;

public class GetServerData extends AsyncTask<GetDetails, Void, ServerResponse> {
    private DataProcessedListener dataProcessedListener;
    private final String TOWRITE = "getServerData";

    @Override
    protected ServerResponse doInBackground(GetDetails... params) {
        try {
            Settings settings = params[0].getSettings();
            ServerConnection serverConnection = new ServerConnection(settings);
            String serverResponse = serverConnection.sendData(TOWRITE);
            return JsonHandler.parseServerResponse(serverResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDataProcessedListener(DataProcessedListener dataProcessedListener) {
        this.dataProcessedListener = dataProcessedListener;
    }

    @Override
    protected void onPostExecute(ServerResponse serverResponse) {
        super.onPostExecute(serverResponse);
        dataProcessedListener.onDataLoaded(serverResponse);
    }
}
