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
public class MusicLibraryDTO implements Serializable {

    // ---------------------------------------------------//
    // ----------Classe per gestire PLAILISTS-------------//
    static public class PlaylistDTO implements Serializable {

        private String name;

        List<String> song_list;

        public PlaylistDTO() {
            this.name = "";
            this.song_list = new ArrayList<String>();
        }

        public PlaylistDTO(String name) {
            this.name = name;
            this.song_list = new ArrayList<String>();
        }

        public PlaylistDTO(String name, List<String> song_list) {
            this.name = name;
            this.song_list = song_list;
        }

        public String getName() {
            return name;
        }

        public List<String> getSongList() {
            return song_list;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSongList(List<String> song_list) {
            this.song_list = song_list;
        }
    }

    private Map<String, SongSummaryDTO> songs;

    private List<PlaylistDTO> playlists;

    private String preferred_artist;

    private String most_popular_song;

    private String most_popular_song_for_this_user;

    public MusicLibraryDTO() {
        this.songs = new HashMap<String, SongSummaryDTO>();
        this.playlists = new ArrayList<PlaylistDTO>();
        this.preferred_artist = "";
        this.most_popular_song = "";
        this.most_popular_song_for_this_user = "";
    }

    public MusicLibraryDTO(Map<String, SongSummaryDTO> m,
            List<PlaylistDTO> playlists) {
        setSongs(m);
        setPlaylists(playlists);
        this.preferred_artist = "";
        this.most_popular_song = "";
        this.most_popular_song_for_this_user = "";
    }

    public void addPlaylist(PlaylistDTO playlist) {
        playlists.add(playlist);
    }

    public void addSongToPlaylist(String playlist_name, String song_id) {
        for (PlaylistDTO tmp : playlists) {
            if (tmp.getName().equals(playlist_name)) {
                tmp.getSongList().add(song_id);
                return;
            }
        }
    }

    public int getLibrarySize() {
        return songs.size();
    }

    public String getMostPopularSong() {
        return most_popular_song;
    }

    public String getMostPopularSongForThisUser() {
        return most_popular_song_for_this_user;
    }

    public List<PlaylistDTO> getPlaylists() {
        return playlists;
    }

    public String getPreferred_artist() {
        return preferred_artist;
    }

    public Map<String, SongSummaryDTO> getSongs() {
        return songs;
    }

    public void removePlaylist(String playlist_name) {
        for (PlaylistDTO tmp : playlists) {
            if (tmp.getName().equals(playlist_name)) {
                playlists.remove(tmp);
                return;
            }
        }
    }

    public void removeSongFromPlaylist(String playlist_name, String song_id) {
        for (PlaylistDTO tmp : playlists) {
            if (tmp.getName().equals(playlist_name)) {
                tmp.getSongList().remove(song_id);
                return;
            }
        }
    }

    public void setMostPopularSong(String most_popular_song) {
        this.most_popular_song = most_popular_song;
    }

    public void setMostPopularSongForThisUser(
            String most_popular_song_for_this_user) {
        this.most_popular_song_for_this_user = most_popular_song_for_this_user;
    }

    public void setPlaylists(List<PlaylistDTO> playlists) {
        this.playlists = playlists;
    }

    public void setPreferred_artist(String preferred_artist) {
        this.preferred_artist = preferred_artist;
    }

    private void setSongs(Map<String, SongSummaryDTO> songs) {
        this.songs = songs;
    }
}
