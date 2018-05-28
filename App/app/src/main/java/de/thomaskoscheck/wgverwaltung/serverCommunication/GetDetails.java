package de.thomaskoscheck.wgverwaltung.serverCommunication;

import de.thomaskoscheck.wgverwaltung.setting.Settings;

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
