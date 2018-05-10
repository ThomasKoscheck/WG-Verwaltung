package de.thomaskoscheck.wgverwaltung;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;

//TODO: Update UI
class GetServerData extends AsyncTask<GetDetails, Void, ServerResponse> {
    private DataProcessedListener dataProcessedListener;

    @Override
    protected ServerResponse doInBackground(GetDetails... params) {
        try {
            Settings settings = params[0].getSettings();
            Socket socket = new Socket(settings.getServer(), settings.getPort());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            //String toWrite = "getServerData";
            String toWrite = "abcdefghijklmnop";
            outputStreamWriter.write(StringHelper.getStringWithZeros(toWrite.length(), settings.getAMOUNTOFCHARACTERS()));
            outputStreamWriter.flush();

            InputStream inputStream = socket.getInputStream();
            String initVector = readStream(inputStream, 16);
            Log.d("TK", "initVector: " + initVector);
            String encrypted = Cryptographics.encryptString(toWrite, "bf9a0b105bf549ffe1fc0cb2a5c47389", initVector);
            outputStreamWriter.write(encrypted);
            outputStreamWriter.flush();


            String serverResponseEncrypted = readStream(inputStream, 100000);
            Log.d("TK", "Serverresponse: "+serverResponseEncrypted);

            outputStreamWriter.close();
            socket.close();
            String serverResponseDecrypted = Cryptographics.decryptString(serverResponseEncrypted, "bf9a0b105bf549ffe1fc0cb2a5c47389", initVector);
            ServerResponse serverResponse = null;
            try {
                if (serverResponseDecrypted != null)
                    serverResponse = JsonHandler.parseJson(serverResponseDecrypted);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return serverResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    public void setDataProcessedListener(DataProcessedListener dataProcessedListener) {
        this.dataProcessedListener = dataProcessedListener;
    }

    @Override
    protected void onPostExecute(ServerResponse serverResponse) {
        super.onPostExecute(serverResponse);
        dataProcessedListener.onDataLoaded(serverResponse);
    }
}
