package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Nome: LibraryService.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */
@RemoteServiceRelativePath("libraryService")
public interface LibraryService extends RemoteService {

    public boolean addPlaylist(String user, String playlist_name);

    public boolean addSongToPlaylist(String user, String playlist_name,
            String title, String artist, String album);

    public List<SongSummaryDTO> getPlaylist(String user, String playlist_name);

    public boolean moveSongInPlaylist(String user, String playlist_name,
            int from, int to);

    public boolean removePlaylist(String user, String playlist_name);

    public boolean removeSongFromPlaylist(String user, String playlist_name,
            String title, String artist, String album);

    public void sendUserNewMusic(String user, List<SongDTO> new_songs);
    
    public void storeStatistics(String user, String preferred_artist, String most_popular_song, String most_popular_song_for_this_user);
    
    public String generatePDF(String user);

}
