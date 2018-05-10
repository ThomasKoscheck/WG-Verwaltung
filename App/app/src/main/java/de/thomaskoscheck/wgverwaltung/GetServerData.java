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
            Settings settings=params[0].getSettings();
            Socket socket = new Socket(settings.getServer(), settings.getPort());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            String toWrite = "getServerData";
            outputStreamWriter.write(StringHelper.getStringWithZeros(toWrite.length(), settings.getAMOUNTOFCHARACTERS()));
            outputStreamWriter.write(toWrite);
            outputStreamWriter.flush();

            InputStream inputStream = socket.getInputStream();
            String serverResponseEncrypted = readStream(inputStream, 100000);

            outputStreamWriter.close();
            //inputStream.close();
            //outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            //outputStreamWriter.write("quit");
            socket.close();
            String serverResponseDecrypted = Cryptographics.decryptString(serverResponseEncrypted);
            ServerResponse serverResponse = null;
            try {
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
