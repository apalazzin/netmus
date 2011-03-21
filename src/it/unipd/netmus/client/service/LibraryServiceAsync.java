package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.SongDTO;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Nome: LibraryServiceAsync.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */

public interface LibraryServiceAsync {

    public void sendUserNewMusic(String user, List<SongDTO> new_songs,
            AsyncCallback<Void> callback);

    void addPlaylist(String user, String playlist_name,
            AsyncCallback<Boolean> callback);

    void addSongToPlaylist(String user, String playlist_name, String title,
            String artist, String album, AsyncCallback<Boolean> callback);

    void generateDoc(String user, AsyncCallback<String> callback);

    void moveSongInPlaylist(String user, String playlist_name, int from,
            int to, AsyncCallback<Boolean> callback);

    void removePlaylist(String user, String playlist_name,
            AsyncCallback<Boolean> callback);

    void removeSongFromPlaylist(String user, String playlist_name,
            String title, String artist, String album,
            AsyncCallback<Boolean> callback);

    void storeStatistics(String user, String preferred_artist,
            String most_popular_song, String most_popular_song_for_this_user,
            AsyncCallback<Void> callback);

}
