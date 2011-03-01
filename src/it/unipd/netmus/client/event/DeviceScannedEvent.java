package it.unipd.netmus.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Nome: DeviceScannedEvent.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 Data
 * Creazione: 19 Febbraio 2011
 */
public class DeviceScannedEvent extends GwtEvent<DeviceScannedEventHandler> {
    public static Type<DeviceScannedEventHandler> TYPE = new Type<DeviceScannedEventHandler>();

    /**
     * Restituisce il Type associato all'evento.
     */
    @Override
    public Type<DeviceScannedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Metodo interno per gestire l'evento.
     */
    @Override
    protected void dispatch(DeviceScannedEventHandler handler) {
        handler.onScanDevice(this);
    }

}
