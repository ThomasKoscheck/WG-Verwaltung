package de.thomaskoscheck.wgverwaltung;

import android.os.AsyncTask;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SendRequestDetails extends AsyncTask<SendDetails, Void, Boolean> {

    @Override
    protected Boolean doInBackground(SendDetails... params) {
        try {
            Socket socket = new Socket("thomaskoscheck.de", 9999);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            String rawJsonString = JsonHandler.generateJsonString(params[0]);
            String encryptedJsonString = Cryptographics.encryptString(rawJsonString);
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