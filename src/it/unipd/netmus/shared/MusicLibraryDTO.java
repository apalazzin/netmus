package it.unipd.netmus.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Nome: MusicLibraryDTO.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * Come tutte le altri classi di questo package questa classe permette lo
 * scambio di informazioni tra client e server all'interno delle chiamate RPC.
 * Contiene le informazioni relative ad un catalogo multimediale compresa la
 * lista di canzoni di cui è composto. Le canzoni presenti nella lista sono
 * incapsulate in oggetti di tipo SongDTO.
 * 
 * 
 */
@SuppressWarnings("serial")
public class MusicLibraryDTO implements Serializable {

    private UserDTO owner;

    private List<SongDTO> songs;

    private List<String> playlists;
    
    private String preferred_artist;

    private String preferred_genre;

    public MusicLibraryDTO() {
        this.owner = new UserDTO();
        this.songs = new ArrayList<SongDTO>();
        this.preferred_artist = "";
        this.preferred_genre = "";
    }

    public MusicLibraryDTO(UserDTO user, List<SongDTO> songs,
            List<String> playlists) {
        setOwner(user);
        setSongs(songs);
        setPlaylists(playlists);
        this.preferred_artist = "";
        this.preferred_genre = "";
    }

    public void addPlaylist(String playlist_name) {
        playlists.add(playlist_name);
    }

    public UserDTO getOwner() {
        return owner;
    }

    public List<String> getPlaylists() {
        return playlists;
    }

    public List<SongDTO> getSongs() {
        return songs;
    }

    public void removePlaylist(String playlist_name) {
        if (playlists.contains(playlist_name)) {
            playlists.remove(playlist_name);
        }
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public void setPlaylists(List<String> playlists) {
        this.playlists = playlists;
    }

    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }

    public void setPreferred_artist(String preferred_artist) {
        this.preferred_artist = preferred_artist;
    }

    public String getPreferred_artist() {
        return preferred_artist;
    }

    public void setPreferred_genre(String preferred_genre) {
        this.preferred_genre = preferred_genre;
    }

    public String getPreferred_genre() {
        return preferred_genre;
    }
}
