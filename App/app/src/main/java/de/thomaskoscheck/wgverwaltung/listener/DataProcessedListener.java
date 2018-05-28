package de.thomaskoscheck.wgverwaltung.listener;

import de.thomaskoscheck.wgverwaltung.server_communication.ServerResponse;

public interface DataProcessedListener {
    void onDataLoaded(ServerResponse serverResponse);
}
