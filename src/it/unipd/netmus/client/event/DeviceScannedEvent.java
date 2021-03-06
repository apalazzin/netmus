package it.unipd.netmus.client.event;

import it.unipd.netmus.shared.SongDTO;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Nome: DeviceScannedEvent.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 19 Febbraio 2011
 */
public class DeviceScannedEvent extends GwtEvent<DeviceScannedEventHandler> {
    public static Type<DeviceScannedEventHandler> TYPE = new Type<DeviceScannedEventHandler>();

    private List<SongDTO> new_songs;

    private boolean last_songs = false;

    public DeviceScannedEvent(List<SongDTO> new_songs) {
        this.new_songs = new_songs;
        this.last_songs = false;
    }

    public DeviceScannedEvent(List<SongDTO> new_songs, boolean last_songs) {
        this.new_songs = new_songs;
        this.last_songs = last_songs;
    }

    /**
     * Restituisce il Type associato all'evento.
     */
    @Override
    public Type<DeviceScannedEventHandler> getAssociatedType() {
        return TYPE;
    }

    public List<SongDTO> getNewSongs() {
        return new_songs;
    }

    public boolean isLastSongs() {
        return last_songs;
    }

    /**
     * Metodo interno per gestire l'evento.
     */
    @Override
    protected void dispatch(DeviceScannedEventHandler handler) {
        handler.onScanDevice(this);
    }

}
