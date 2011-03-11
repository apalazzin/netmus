package it.unipd.netmus.shared;

import java.io.Serializable;

/**
 * Nome: SongSummaryDTO.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * Come tutte le altri classi di questo package questa classe permette lo
 * scambio di informazioni tra client e server all'interno delle chiamate RPC.
 * Contiene e rappresenta i dati più comunemente utilizzati di un brano, ovvero
 * quelli che vengono visualizzati in una playlist all'interno di un profilo.
 * 
 * 
 */
@SuppressWarnings("serial")
public class SongSummaryDTO implements Serializable {

    private String artist;
    
    private String title;
    
    private String album;
    
    private double rating;
    
    private int rating_for_this_user;
    
    private String album_cover;
    
    private String youtube_code;

    public SongSummaryDTO() {
        this.artist = "";
        this.title = "";
        this.album = "";
        this.rating = -1;
        this.rating_for_this_user = -1;
        this.album_cover = "";
        this.youtube_code = "";
    }

    public SongSummaryDTO(String artist, String title, String album) {
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.rating = -1;
        this.rating_for_this_user = -1;
        this.album_cover = "";
        this.youtube_code = "";
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }
    
    public double getRating() {
        return rating;
    }

    public int getRatingForThisUser() {
        return rating_for_this_user;
    }

    public String getTitle() {
        return title;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRatingForThisUser(int rating_for_this_user) {
        this.rating_for_this_user = rating_for_this_user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYoutubeCode(String youtube_code) {
        this.youtube_code = youtube_code;
    }

    public String getYoutubeCode() {
        return youtube_code;
    }

    public void setAlbumCover(String album_cover) {
        this.album_cover = album_cover;
    }

    public String getAlbumCover() {
        return album_cover;
    }
}
