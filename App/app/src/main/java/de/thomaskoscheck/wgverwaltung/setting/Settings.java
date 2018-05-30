package de.thomaskoscheck.wgverwaltung.setting;

public class Settings {
    private String password;
    private String requester;
    private String server;
    private int port;
    private final int AMOUNTOFCHARACTERS = 6;
    private final int initVectorLength = 16;


    Settings(String requester, String password, String server, int port) {
        this.password = password;
        this.requester = requester;
        this.server = server;
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getAmountOfCharacters() {
        return AMOUNTOFCHARACTERS;
    }

    public int getInitVectorLength() {
        return initVectorLength;
    }
}