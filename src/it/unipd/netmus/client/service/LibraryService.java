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
            String song_id);

    public List<SongSummaryDTO> getPlaylist(String user, String playlist_name);

    public String loadMostPopularSong(String user);

    public String loadPreferredArtist(String user);

    public String loadPreferredGenre(String user);

    public boolean moveSongInPlaylist(String user, String playlist_name,
            int from, int to);

    public boolean removePlaylist(String user, String playlist_name);

    public boolean removeSongFromPlaylist(String user, String playlist_name,
            String song_id);

    public List<SongSummaryDTO> sendUserNewMusic(String user, List<SongDTO> new_songs);

    void completeSongs(List<SongSummaryDTO> incomplete);
    
    void updateStatisticFields(String user);

}
