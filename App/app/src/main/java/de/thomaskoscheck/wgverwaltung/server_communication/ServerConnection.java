package de.thomaskoscheck.wgverwaltung.server_communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;

import de.thomaskoscheck.wgverwaltung.Cryptographics;
import de.thomaskoscheck.wgverwaltung.StringHelper;
import de.thomaskoscheck.wgverwaltung.setting.Settings;

public class ServerConnection {
    private Settings settings;
    private InputStream inputStream;
    private OutputStream outputStream;
    private byte[] passphraseHex;
    private String initVector;

    ServerConnection(Settings settings) {
        this.settings = settings;
    }

    public String sendData(String data) {
        try {
            Socket socket = new Socket(settings.getServer(), settings.getPort());
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            passphraseHex = Cryptographics.generateHexPassphrase(settings.getPassword());
            initVector = getInitVector();

            String encrypted = Cryptographics.encryptString(data, passphraseHex, initVector);
            writeEncryptedData(encrypted);

            String serverResponseDecrypted = getDecryptedServerData();

            inputStream.close();
            outputStream.close();
            socket.close();
            return serverResponseDecrypted;
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
}
