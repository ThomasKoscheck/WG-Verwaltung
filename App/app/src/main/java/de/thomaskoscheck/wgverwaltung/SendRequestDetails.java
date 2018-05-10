package de.thomaskoscheck.wgverwaltung;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;

public class SendRequestDetails extends AsyncTask<SendDetails, Void, Boolean> {

    @Override
    protected Boolean doInBackground(SendDetails... params) {
        try {
            Settings settings= params[0].getSettings();
            Socket socket = new Socket(settings.getServer(), settings.getPort());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            String rawJsonString = JsonHandler.generateJsonString(params[0]);

            InputStream inputStream = socket.getInputStream();
            String initVector = readStream(inputStream, 32);

            String encryptedJsonString = Cryptographics.encryptString(rawJsonString, settings.getPassword(), initVector);

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

    private String readStream(InputStream stream, int maxReadSize) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] rawBuffer = new char[maxReadSize];
        int readSize;
        StringBuffer buffer = new StringBuffer();
        while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
            if (readSize > maxReadSize) {
                readSize = maxReadSize;
            }
            buffer.append(rawBuffer, 0, readSize);
            maxReadSize -= readSize;
        }
        return buffer.toString();
    }
}