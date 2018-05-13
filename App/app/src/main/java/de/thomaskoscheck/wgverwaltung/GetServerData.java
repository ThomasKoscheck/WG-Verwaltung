package de.thomaskoscheck.wgverwaltung;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;

class GetServerData extends AsyncTask<GetDetails, Void, ServerResponse> {
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
            passphraseHex = generateHexPassphrase(settings.getPassword());
            initVector = getInitVector();


            String encrypted = Cryptographics.encryptString(TOWRITE, passphraseHex, initVector);
            writeEncryptedData(encrypted);

            String serverResponseDecrypted = getDecryptedServerData();

            return getServerResponse(serverResponseDecrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] generateHexPassphrase(String passphrase) {
        StringBuilder stringBuilder = new StringBuilder(passphrase);
        int passphraseLength = passphrase.length();
        if (passphraseLength < 16) {
            while (passphraseLength != 16) {
                stringBuilder.append("?");
                passphraseLength++;
            }
        } else if (passphraseLength < 24) {
            while (passphraseLength != 24) {
                stringBuilder.append("?");
                passphraseLength++;
            }
        } else if (passphraseLength < 32){
            while (passphraseLength != 32) {
                stringBuilder.append("?");
                passphraseLength++;
            }
        }
        Log.d("TK", "passphrase: "+stringBuilder.toString());
        return stringBuilder.toString().getBytes();
    }

    private String getInitVector() throws IOException {
        return readStream(16);
    }

    private void writeEncryptedData(String data) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        String filledWithZeroes = StringHelper.getStringWithZeros(data.length(), settings.getAMOUNTOFCHARACTERS());
        outputStreamWriter.write(filledWithZeroes);
        outputStreamWriter.flush();

        Log.d("TK", "encrypted: " + data);
        outputStreamWriter.write(data);
        outputStreamWriter.flush();
        outputStreamWriter.close();
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
