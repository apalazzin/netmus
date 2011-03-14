package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Nome: SongServiceAsync.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */
public interface SongServiceAsync {

    void deleteSong(String user, String artist, String title, String album,
            AsyncCallback<Boolean> callback);

    void editSong(String user, String artist, String title, String album,
            AsyncCallback<Boolean> callback);

    void rateSong(String user, SongSummaryDTO song, int rating,
            AsyncCallback<Double> callback);

    void getSongDTO(SongSummaryDTO song_summary_dto,
            AsyncCallback<SongDTO> callback);

}
