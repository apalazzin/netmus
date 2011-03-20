package it.unipd.netmus.server.persistent;

import java.io.Serializable;

import it.unipd.netmus.server.utils.Utils;
import it.unipd.netmus.server.utils.cache.CacheSupport;
import it.unipd.netmus.server.utils.cache.Cacheable;
import it.unipd.netmus.shared.FieldVerifier;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.code.twig.annotation.Id;

/**
 * Nome: Song.java Autore: VT.G Licenza: GNU GPL v3 Data Creazione: 15 Febbraio
 * 2011
 * 
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * Contiene le informazioni relative ad un brano musicale, in particolare ogni
 * canzone è identificata da una chiave univoca composta dal titolo, un
 * separatore di default, l'artista e l'album disposte nell'ordine
 * titolo-sep-artista-sep-album. Dei brani condivisi da più utenti viene salvata
 * una sola copia per ottimizzare la quantità di spazio utilizzato. La logica di
 * salvataggio delle canzoni implementata prevede che nei tag estratti sia
 * presente almeno il titolo altrimenti il brano non verrà mantenuto nel
 * Datastore. Al primo inserimento di una canzone qesta classe si occupa anche
 * di reperire informazioni aggiuntive dai servizi esterni Youtube e Last.fm
 * quali link per copertine e video in streaming.
 * 
 * 
 */

public class Song implements Serializable, Cacheable {

    private static final long serialVersionUID = -6562913769428174789L;

    public static Song load(String id) {
        
        Song song = (Song) CacheSupport.cacheGet(id);
        
        if (song == null) {
            song = ODF.get().load().type(Song.class).id(id).now();
            if (song != null) {
                song.addToCache();
            }
        }
        
        return song;
    }

    public static Song loadFromDTO(SongSummaryDTO dto) {
        
        String id = FieldVerifier.generateSongId(dto.getTitle(), dto.getArtist(), dto.getAlbum());
        
        Song song = (Song) CacheSupport.cacheGet(id);
        
        if (song == null) {
            song = ODF.get().load().type(Song.class).id(id).now();
            if (song != null) {
                song.addToCache();
            }
        }
        
        return song;
    }

    /**
     * 
     * Recupera le informazioni da un oggetto SongDTO e le salva nel modo più
     * opportuno nel Datastore.
     * 
     */
    public static Song storeOrUpdateFromDTO(SongDTO song) {

        if (song.getTitle().equals(""))
            return null;

        Song s = load(FieldVerifier.generateSongId(song.getTitle(), song.getArtist(), song.getAlbum()));

        if (s == null) {
            // La canzone non è presente nel Datastore
            s = new Song();
            s.setAlbum(song.getAlbum());
            
            Album.storeNewAlbum(song.getAlbum(), song.getArtist());
            
            s.setTitle(song.getTitle());
            s.setArtist(song.getArtist());
            s.setComposer(song.getComposer());
            s.setGenre(song.getGenre());
            s.setTrackNumber(song.getTrackNumber());
            s.setYear(song.getYear());
            s.setId(FieldVerifier.generateSongId(song.getTitle(), song.getArtist(), song.getAlbum()));
            return s;
        }

        else {
            // La canzone è presente nel Datastore.
            // inserimento delle informazioni prese dal DTO
            if (s.getComposer().equals(""))
                s.setComposer(song.getComposer());
            if (s.getGenre().equals(""))
                s.setGenre(song.getGenre());
            if (s.getTrackNumber().equals(""))
                s.setTrackNumber(song.getTrackNumber());
            if (s.getYear().equals(""))
                s.setYear(song.getYear());

            s.update();
            return s;
        }
    }
    
    /*
     * Cerca di recuperare informazioni sui brani incompleti, per poi vedere se è il caso di ignorarli
     */
    @SuppressWarnings("unused")
    static private SongDTO incompleteSong(SongDTO s){
    	//guardo se posso ricavare informazioni dai meta tag.
    	if (!s.getArtist().isEmpty() && !s.getTitle().isEmpty())
    		return Utils.getSongFromIncompleteInfo(s.getArtist() + " " 
    				+ s.getTitle() );
    	
    	//altrimenti, proviamo dal nome del file: se ho artista o titolo nei metatag, cerco di utilizzare
    	//pure quelli se non sono già presenti nel nome del file (eventuali ripetizioni sarebbero dannose).
    	String extraTags = new String();
    	if (!s.getArtist().isEmpty() && 
    			!FieldVerifier.cleanString(s.getFile()).contains(FieldVerifier.cleanString(s.getArtist())))
    		extraTags = s.getArtist();
    	else if (!s.getTitle().isEmpty() && 
    			!FieldVerifier.cleanString(s.getFile()).contains(FieldVerifier.cleanString(s.getTitle())))
    		extraTags = s.getTitle();
    	
    	return Utils.getSongFromFileName(s.getFile() + extraTags);
    }

    static void deleteSong(Song s) {
        ODF.get().storeOrUpdate(s);
        s.removeFromCache();
        ODF.get().delete(s);
    }

    @Id
    private String id;

    private String title;

    private String album;

    private String artist;

    private int num_owners;

    private double rating;

    private String genre;

    private String year;

    private String composer;

    private String track_number;

    private int num_ratings;

    public Song() {
        this.id = FieldVerifier.generateSongId("","","");
        this.num_owners = 0;
        this.album = "";
        this.artist = "";
        this.composer = "";
        this.genre = "";
        this.title = "";
        this.track_number = "";
        this.year = "";
        this.num_ratings = 0;
        this.rating = 0;
    }

    /**
     * 
     * Aggiunge la votazione data in input alla canzone aggiornando nel modo
     * appropriato gli attributi rating e num_ratings. Questo metodo, poichè gli
     * stessi dati possono essere acceduti concorrentemente, viene eseguito
     * all'interno di una tranzazione.
     * 
     */
    public double addRate(int rating) {
        this.num_ratings++;
        this.rating = (this.rating + rating) / this.num_ratings;
        this.update();
        return this.rating;
    }

    public Song changeAlbum(String album) {

        // Se la canzone precedente alla modifica non ha altri utenti che la
        // possiedono viene
        // cancellata dal Datastore perchè probabilmente contiene informazioni
        // errate o incomplete.
        ODF.get().storeOrUpdate(this);
        if (this.num_owners == 0)
            Song.deleteSong(this);
        else {
            this.deleteOwner();
        }

        if (album != null && album.equals(""))
            this.album = album;
        else
            this.album = "";

        Song tmp = Song.load(FieldVerifier.generateSongId(this.title, this.artist, this.album));
        if (tmp == null) {
            // La canzone non è presente nel Datastore.
            this.setId(FieldVerifier.generateSongId(this.title, this.artist, this.album));
            return this;
        } else {
            // La canzone è presente nel Datastore.
            return tmp;
        }

    }

    public Song changeArtist(String artist) {

        // Se la canzone precedente alla modifica non ha altri utenti che la
        // possiedono viene
        // cancellata dal Datastore perchè probabilmente contiene informazioni
        // errate o incomplete.
        ODF.get().update(this);
        if (this.num_owners == 0)
            Song.deleteSong(this);
        else {
            this.deleteOwner();
        }

        if (artist != null && artist.equals(""))
            this.artist = artist;
        else
            this.artist = "";

        Song tmp = Song.load(FieldVerifier.generateSongId(this.title, this.artist, this.album));

        if (tmp == null) {
            // La canzone non è presente nel Datastore.
            this.setId(FieldVerifier.generateSongId(this.title, this.artist, this.album));
            return this;
        } else {
            // La canzone è presente nel Datastore.
            return tmp;
        }

    }

    /**
     * 
     * Permette ad ogni utente di modificare la propria votazione su questo
     * brano dando in input il voto precendete e quello nuovo. Questo metodo,
     * poiché gli stessi dati possono essere acceduti concorrentemente, viene
     * eseguito all'interno di una tranzazione.
     * 
     */
    public double changeRate(int old_rating, int rating) {

        this.rating = ((this.rating * this.num_ratings) + rating - old_rating)
                / this.num_ratings;
        this.update();
        return this.rating;
    }

    public Song changeTitle(String title) {

        // Se la canzone precedente alla modifica non ha altri utenti che la
        // possiedono viene
        // cancellata dal Datastore perchè probabilmente contiene informazioni
        // errate o incomplete.
        ODF.get().update(this);
        if (this.num_owners == 0)
            Song.deleteSong(this);
        else {
            this.deleteOwner();
        }

        if (title != null && title.equals(""))
            this.title = title;
        else
            this.title = "";

        Song tmp = Song.load(FieldVerifier.generateSongId(this.title, this.artist, this.album));
        if (tmp == null) {
            // La canzone non è presente nel Datastore.
            this.setId(FieldVerifier.generateSongId(this.title, this.artist, this.album));
            return this;
        } else {
            // La canzone è presente nel Datastore.
            return tmp;
        }

    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getComposer() {
        return composer;
    }

    public String getGenre() {
        return genre;
    }

    public String getId() {
        return id;
    }

    public int getNumOwners() {
        return num_owners;
    }

    public int getNumRatings() {
        return num_ratings;
    }

    public double getRatingDouble() {
        return rating;
    }

    public int getRatingInt() {
        return (int) rating;
    }

    public String getTitle() {
        return title;
    }

    public String getTrackNumber() {
        return track_number;
    }

    public String getYear() {
        return year;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setTrackNumber(String track_number) {
        this.track_number = track_number;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public SongDTO toSongDTO() {
        SongDTO tmp = new SongDTO();
        tmp.setArtist(this.artist);
        tmp.setTitle(this.title);
        tmp.setAlbum(this.album);
        tmp.setComposer(this.composer);
        tmp.setGenre(this.genre);
        tmp.setTrackNumber(this.track_number);
        tmp.setYear(this.year);
        tmp.setNumOwners(this.num_owners);
        tmp.setNumRatings(this.num_ratings);
        tmp.setRating(this.rating);
        return tmp;
    }

    public SongSummaryDTO toSongSummaryDTO() {
        SongSummaryDTO tmp = new SongSummaryDTO(this.artist, this.title, this.album);
        tmp.setRating(this.rating);
        return tmp;
    }

    public void update() {
        ODF.get().storeOrUpdate(this);
        this.addToCache();
    }

    void deleteOwner() {
        this.num_owners -= 1;
        this.update();
    }

    void newOwner() {
        this.num_owners += 1;
        this.update();
    }

    private void setAlbum(String album) {
        this.album = album;
    }

    private void setArtist(String artist) {
        this.artist = artist;
    }

    private void setId(String id) {
        this.id = id.toLowerCase();
        this.update();
    }

    private void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void addToCache() {
        CacheSupport.cachePut(this.id, this);
    }

    @Override
    public void removeFromCache() {
        CacheSupport.cacheRemove(this.id);
    }

}
