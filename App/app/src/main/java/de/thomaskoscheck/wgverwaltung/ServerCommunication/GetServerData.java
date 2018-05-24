package de.thomaskoscheck.wgverwaltung.ServerCommunication;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;

import de.thomaskoscheck.wgverwaltung.Cryptographics;
import de.thomaskoscheck.wgverwaltung.JsonHandler;
import de.thomaskoscheck.wgverwaltung.Listener.DataProcessedListener;
import de.thomaskoscheck.wgverwaltung.Setting.Settings;
import de.thomaskoscheck.wgverwaltung.StringHelper;

public class GetServerData extends AsyncTask<GetDetails, Void, ServerResponse> {
    private DataProcessedListener dataProcessedListener;
    private final String TOWRITE = "getServerData";
    private Settings settings;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private byte[] passphraseHex;
    private String initVector;

    @Override
    protected ServerResponse doInBackground(GetDetails... params) {
        try {
            settings = params[0].getSettings();
            socket = new Socket(settings.getServer(), settings.getPort());
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            passphraseHex = Cryptographics.generateHexPassphrase(settings.getPassword());
            initVector = getInitVector();

            String encrypted = Cryptographics.encryptString(TOWRITE, passphraseHex, initVector);
            writeEncryptedData(encrypted);

            String serverResponseDecrypted = getDecryptedServerData();

            inputStream.close();
            outputStream.close();
            socket.close();

            return getServerResponse(serverResponseDecrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    private String getDecryptedServerData() throws IOException {
        String serverResponseEncrypted = readStream(100000);
        return Cryptographics.decryptString(serverResponseEncrypted, passphraseHex, initVector);
    }

    private ServerResponse getServerResponse(String serverResponseDecrypted) throws JSONException {
        if (serverResponseDecrypted == null)
            throw new NullPointerException();

        return JsonHandler.parseJson(serverResponseDecrypted);
    }

    private String readStream(int maxReadSize) throws IOException {
        Reader reader;
        reader = new InputStreamReader(inputStream, "UTF-8");
        char[] rawBuffer = new char[maxReadSize];
        int readSize;
        StringBuilder stringBuilder = new StringBuilder();
        while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
            if (readSize > maxReadSize) {
                readSize = maxReadSize;
            }
            stringBuilder.append(rawBuffer, 0, readSize);
            maxReadSize -= readSize;
        }
        return stringBuilder.toString();
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
