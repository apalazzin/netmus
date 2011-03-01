package it.unipd.netmus.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Nome: DeviceScannedEventHandler.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 Data
 * Creazione: 19 Febbraio 2011
 */
public interface DeviceScannedEventHandler extends EventHandler {
    /**
     * Gestisce l'evento della fine della scansione di un dispositivo da parte
     * dell'applet.
     * 
     * @param event
     */
    void onScanDevice(DeviceScannedEvent event);
}
