package it.unipd.netmus.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Nome: MusicLibrarySummaryDTO.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * Come tutte le altri classi di questo package questa classe permette lo
 * scambio di informazioni tra client e server all'interno delle chiamate RPC.
 * Contiene le informazioni relative ad un catalogo multimediale compresa la
 * lista di canzoni di cui è composto. Le canzoni presenti nella lista sono
 * incapsulate in oggetti di tipo SongSummaryDTO e contengono solamente le
 * infomrazioni basilari quali artista e titolo.
 * 
 */
@SuppressWarnings("serial")
public class MusicLibrarySummaryDTO implements Serializable {

    private Map<String, SongSummaryDTO> songs;

    private List<String> playlists;
    
    private String preferred_artist;

    private String most_popular_song;
    
    private String most_popular_song_for_this_user;

    public MusicLibrarySummaryDTO() {
        this.songs = new HashMap<String, SongSummaryDTO>();
        this.playlists = new ArrayList<String>();
        this.preferred_artist = "";
        this.most_popular_song = "";
        this.most_popular_song_for_this_user = "";
    }

    public MusicLibrarySummaryDTO(Map<String, SongSummaryDTO> m,
            List<String> playlists) {
        setSongs(m);
        setPlaylists(playlists);
        this.preferred_artist = "";
        this.most_popular_song = "";
        this.most_popular_song_for_this_user = "";
    }
    
    public Map<String, SongSummaryDTO> getSongs() {
        return songs;
    }

    private void setSongs(Map<String, SongSummaryDTO> songs) {
        this.songs = songs;
    }
    
    public void addPlaylist(String playlist_name) {
        playlists.add(playlist_name);
    }

    public int getLibrarySize() {
        return songs.size();
    }

    public List<String> getPlaylists() {
        return playlists;
    }
    
    public void removePlaylist(String playlist_name) {
        if (playlists.contains(playlist_name)) {
            playlists.remove(playlist_name);
        }
    }

    public void setPlaylists(List<String> playlists) {
        this.playlists = playlists;
    }

    public void setPreferred_artist(String preferred_artist) {
        this.preferred_artist = preferred_artist;
    }

    public String getPreferred_artist() {
        return preferred_artist;
    }

    public void setMostPopularSong(String most_popular_song) {
        this.most_popular_song = most_popular_song;
    }

    public String getMostPopularSong() {
        return most_popular_song;
    }

    public void setMostPopularSongForThisUser(
            String most_popular_song_for_this_user) {
        this.most_popular_song_for_this_user = most_popular_song_for_this_user;
    }

    public String getMostPopularSongForThisUser() {
        return most_popular_song_for_this_user;
    }
}
