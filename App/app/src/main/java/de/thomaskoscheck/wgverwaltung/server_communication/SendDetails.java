package de.thomaskoscheck.wgverwaltung.server_communication;

import de.thomaskoscheck.wgverwaltung.setting.Settings;

public class SendDetails {
    private final Settings settings;
    private final RequestDetails requestDetails;

    public SendDetails(Settings settings, RequestDetails requestDetails) {
        this.settings = settings;
        this.requestDetails = requestDetails;
    }

    public Settings getSettings() {
        return settings;
    }

    public RequestDetails getRequestDetails() {
        return requestDetails;
    }
}
