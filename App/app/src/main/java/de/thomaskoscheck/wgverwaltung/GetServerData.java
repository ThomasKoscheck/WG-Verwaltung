package de.thomaskoscheck.wgverwaltung;

import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;

//TODO: Update UI
class GetServerData extends AsyncTask<GetDetails, Void, Boolean> {

    @Override
    protected Boolean doInBackground(GetDetails... params) {
        try {
            Socket socket = new Socket(params[0].getSettings().getServer(), params[0].getSettings().getPort());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            outputStreamWriter.write("getServerData");

            InputStream inputStream = socket.getInputStream();
            String serverResponseEncrypted = readStream(inputStream, 100000);
            String serverResponseDecrypted = Cryptographics.decryptString(serverResponseEncrypted);

            outputStreamWriter.close();
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
}
