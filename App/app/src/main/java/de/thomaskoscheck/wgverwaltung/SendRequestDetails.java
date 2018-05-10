package de.thomaskoscheck.wgverwaltung;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SendRequestDetails extends AsyncTask<String, Void, ServerResponse> {
    private TextView leftCredit;

    SendRequestDetails(TextView textView) {
        this.leftCredit = textView;
    }

    @Override
    protected ServerResponse doInBackground(String... params) {
        InputStream stream;
        String result = "";
        try {
            URL url = new URL("https://thomaskoscheck.de/projekte/wg-verwaltung/index.php" + params[0]);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream, 1000000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("TK", result);
        ServerResponse serverResponse = null;
        try {
            serverResponse = JsonParser.parseJson(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    public String readStream(InputStream stream, int maxReadSize)
            throws IOException {
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

    @Override
    protected void onPostExecute(ServerResponse result) {
        super.onPostExecute(result);
        if(result!=null) {
            leftCredit.setText(result.getCredit());
        }
        else{
            leftCredit.setText(leftCredit.getContext().getText(R.string.errorParsingTheJson));
        }
    }
}