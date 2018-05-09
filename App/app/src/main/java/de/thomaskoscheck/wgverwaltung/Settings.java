package de.thomaskoscheck.wgverwaltung;

class Settings {
    private String password;
    private String requester;

    Settings(String requester, String password) {
        this.password = password;
        this.requester = requester;
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
}