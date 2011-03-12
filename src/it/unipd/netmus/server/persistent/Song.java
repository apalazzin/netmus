package it.unipd.netmus.server.persistent;

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
 * 
 * PROCEDURA SUGGERTA PER INSERIRE O MODIFICARE UNA CANZONE NEL DATABASE
 * 
 * Song s= new
 * Song().changeArtist(song.getArtist()).changeTitle(song.getTitle())
 * .changeAlbum(song.getAlbum()); s.set_quello_che_vuoi (per modificare gli
 * altri attrobuti, ATTENZIONE che se la canzone era già presente in database
 * potrebbe avere già delle informazioni).
 * 
 * 
 * 
 * UserAccount user = UserAccount.load("nome_utente"); (Caricamente di un utente
 * di Netmus)
 * 
 * PROCEDURA PER ASSOCIARLA AD UN UTENTE user.getMusicLibrary().addSong(s,
 * false); (con "true" aggiorna anche i dati statistici)
 * 
 * PROCEDURA PER RIMUOVERLA DA UN UTENTE user.getMusicLibrary().removeSong(s,
 * false); (con "true" aggiorna anche i dati statistici)
 * 
 * 
 */

public class Song {

    private static final String SEPARATOR = "-vtg-";
    
    static String generateSongId(String title, String artist, String album) {
        String song_id = (title + Song.SEPARATOR + artist + Song.SEPARATOR + album).toLowerCase();
        if (song_id != Song.SEPARATOR+Song.SEPARATOR) {
            song_id = song_id.replace('.', ' ');
            song_id = song_id.replace('\"', ' ');
            song_id = song_id.replace('\'', ' ');
            song_id = song_id.replace(':', ' ');
            song_id = song_id.replace('/', ' ');
            song_id = song_id.replace('\\', ' ');
            song_id = song_id.replaceAll(" ", "");
        }
        return song_id;
    }

    public static Song load(String id) {
        return ODF.get().load().type(Song.class).id(id).now();
    }

    public static Song loadFromDTO(SongSummaryDTO dto) {
        Song tmp = ODF
                .get()
                .load()
                .type(Song.class)
                .id(generateSongId(dto.getTitle(), dto.getArtist(), dto.getAlbum())).now();
        return tmp;
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

        Song s = load(generateSongId(song.getTitle(), song.getArtist(), song.getAlbum()));

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
            s.setId(generateSongId(song.getTitle(), song.getArtist(), song.getAlbum()));
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

    static void deleteSong(Song s) {
        ODF.get().storeOrUpdate(s);
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
        this.id = generateSongId("","","");
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

        Song tmp = Song.load(generateSongId(this.title, this.artist, this.album));
        if (tmp == null) {
            // La canzone non è presente nel Datastore.
            this.setId(generateSongId(this.title, this.artist, this.album));
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

        Song tmp = Song.load(generateSongId(this.title, this.artist, this.album));

        if (tmp == null) {
            // La canzone non è presente nel Datastore.
            this.setId(generateSongId(this.title, this.artist, this.album));
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

        Song tmp = Song.load(generateSongId(this.title, this.artist, this.album));
        if (tmp == null) {
            // La canzone non è presente nel Datastore.
            this.setId(generateSongId(this.title, this.artist, this.album));
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

}
