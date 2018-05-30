package de.thomaskoscheck.wgverwaltung.ServerCommunication;

import de.thomaskoscheck.wgverwaltung.Setting.Settings;

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
