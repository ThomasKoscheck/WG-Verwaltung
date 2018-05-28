package de.thomaskoscheck.wgverwaltung.listener;

import de.thomaskoscheck.wgverwaltung.serverCommunication.ServerResponse;

public interface DataProcessedListener {
    void onDataLoaded(ServerResponse serverResponse);
}
