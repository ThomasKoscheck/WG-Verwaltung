package de.thomaskoscheck.wgverwaltung.serverCommunication;

import de.thomaskoscheck.wgverwaltung.setting.Settings;

public class SendDetails {
    private Settings settings;
    private RequestDetails requestDetails;

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
