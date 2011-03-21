package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Nome: SongService.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */
@RemoteServiceRelativePath("songsService")
public interface SongService extends RemoteService {

    boolean deleteSong(String user, String artist, String title, String album);

    boolean editSong(String user, String artist, String title, String album);

    String getCoverImage(SongSummaryDTO song_summary_dto);

    SongDTO getSongDTO(SongSummaryDTO song_summary_dto);

    double rateSong(String user, SongSummaryDTO song, int rating);

}
