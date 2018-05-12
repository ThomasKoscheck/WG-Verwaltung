package de.thomaskoscheck.wgverwaltung;

public class GetDetails {
    private Settings settings;
    private ServerResponse serverResponse;

    public GetDetails(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }
}
