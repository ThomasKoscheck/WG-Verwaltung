package de.thomaskoscheck.wgverwaltung.Listener;

import de.thomaskoscheck.wgverwaltung.ServerCommunication.ServerResponse;

public interface DataProcessedListener {
    void onDataLoaded(ServerResponse serverResponse);
}
