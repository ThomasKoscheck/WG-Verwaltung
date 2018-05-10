package de.thomaskoscheck.wgverwaltung;

import android.os.AsyncTask;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SendRequestDetails extends AsyncTask<SendDetails, Void, Boolean> {

    @Override
    protected Boolean doInBackground(SendDetails... params) {
        try {
            Settings settings= params[0].getSettings();
            Socket socket = new Socket(settings.getServer(), settings.getPort());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            String rawJsonString = JsonHandler.generateJsonString(params[0]);
            String encryptedJsonString = Cryptographics.encryptString(rawJsonString);
            outputStreamWriter.write(StringHelper.getStringWithZeros(encryptedJsonString.length(), settings.getAMOUNTOFCHARACTERS()));
            outputStreamWriter.write(encryptedJsonString);
            outputStreamWriter.close();
            socket.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean succeeded) {
        super.onPostExecute(succeeded);
    }
}