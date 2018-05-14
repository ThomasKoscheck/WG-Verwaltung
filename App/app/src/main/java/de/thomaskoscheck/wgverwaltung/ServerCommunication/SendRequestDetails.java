package de.thomaskoscheck.wgverwaltung.ServerCommunication;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;

import de.thomaskoscheck.wgverwaltung.Cryptographics;
import de.thomaskoscheck.wgverwaltung.JsonHandler;
import de.thomaskoscheck.wgverwaltung.Listener.DataSentListener;
import de.thomaskoscheck.wgverwaltung.Setting.Settings;
import de.thomaskoscheck.wgverwaltung.StringHelper;

public class SendRequestDetails extends AsyncTask<SendDetails, Void, Boolean> {
    private DataSentListener dataSentListener;
    private Settings settings;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private byte[] passphraseHex;
    private String initVector;

    @Override
    protected Boolean doInBackground(SendDetails... params) {
        try {
            settings= params[0].getSettings();
            socket = new Socket(settings.getServer(), settings.getPort());
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            passphraseHex = Cryptographics.generateHexPassphrase(settings.getPassword());
            initVector = getInitVector();

            String rawJsonString = JsonHandler.generateJsonString(params[0]);
            String encryptedJsonString = Cryptographics.encryptString(rawJsonString, passphraseHex, initVector);

            writeEncryptedData(encryptedJsonString);

            inputStream.close();
            outputStream.close();
            socket.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getInitVector() throws IOException {
        return readStream(settings.getInitVectorLength());
    }

    private void writeEncryptedData(String data) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        String filledWithZeroes = StringHelper.getStringWithZeros(data.length(), settings.getAmountOfCharacters());
        outputStreamWriter.write(filledWithZeroes);
        outputStreamWriter.flush();

        outputStreamWriter.write(data);
        outputStreamWriter.flush();
    }

    private String readStream(int maxReadSize) throws IOException {
        Reader reader;
        reader = new InputStreamReader(inputStream, "UTF-8");
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

    @Override
    protected void onPostExecute(Boolean succeeded) {
        super.onPostExecute(succeeded);
    }

    public void setDataSentListener(DataSentListener dataSentListener) {
        this.dataSentListener = dataSentListener;
    }
}