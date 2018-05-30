package de.thomaskoscheck.wgverwaltung.server_communication;

import de.thomaskoscheck.wgverwaltung.setting.Settings;

public class GetDetails {
    private final Settings settings;

    public GetDetails(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }
}
