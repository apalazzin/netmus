/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.server.utils.Utils;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.appengine.api.datastore.Transaction;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;


/**
 * @author ValterTexasGroup
 *
 *
 *PROCEDURA SUGGERTA PER INSERIRE O MODIFICARE UNA CANZONE NEL DATABASE 
 *
 * Song s= new Song().changeArtist(song.getArtist()).changeTitle(song.getTitle()).changeAlbum(song.getAlbum());
 * s.set_quello_che_vuoi   (per modificare gli altri attrobuti, 
 *          ATTENZIONE che se la canzone era già presente in database 
 *          potrebbe avere già delle informazioni).
 *
 *
 *
 *UserAccount user = UserAccount.load("nome_utente"); (Caricamente di un utente di Netmus)
 *
 *PROCEDURA PER ASSOCIARLA AD UN UTENTE
 * user.getMusicLibrary().addSong(s, false);       (con "true" aggiorna anche i dati statistici)
 * 
 *PROCEDURA PER RIMUOVERLA DA UN UTENTE
 * user.getMusicLibrary().removeSong(s, false);    (con "true" aggiorna anche i dati statistici)
 *
 *
 */


/**
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * Contiene le informazioni relative ad un brano musicale, in particolare ogni canzone è 
 * identificata da una chiave univoca composta dal titolo, un separatore di default, l'artista 
 * e l'album disposte nell'ordine titolo-sep-artista-sep-album. Dei brani condivisi da più 
 * utenti viene salvata una sola copia per ottimizzare la quantità di spazio utilizzato.
 * La logica di salvataggio delle canzoni implementata prevede che nei tag estratti sia 
 * presente almeno il titolo altrimenti il brano non verrà mantenuto nel Datastore. 
 * Al primo inserimento di una canzone qesta classe si occupa anche di reperire informazioni 
 * aggiuntive dai servizi esterni Youtube e Last.fm quali link per copertine e video in streaming.
 * 
 */


public class Song {
    
    static final String SEPARATOR = "-vt.g-";

    @Id private String id;
    
    @Index private String title;
    
    @Index private String album;
    
    @Index private String artist;
    
    @Index private int num_owners;
    
    @Index private double rating;
    
    @Index private String genre;
    
    private String album_cover;
    
    private String year;
    
    private String composer;
    
    private String track_number;
    
    private String file;
    
    private String youtube_code;
    
    private int num_ratings;
    
    
    public Song() {
        this.id = SEPARATOR;
        this.num_owners = 0;
        this.album = "";
        this.album_cover = "";
        this.artist = "";
        this.composer = "";
        this.file = "";
        this.genre = "";
        this.title = "";
        this.track_number = "";
        this.year = "";
        this.youtube_code = "";
        this.num_ratings = 0;
        this.rating = 0;
    }
    
    public void update() {
        ODF.get().storeOrUpdate(this);
    }
    
    public static Song load(String id) {
        return ODF.get().load().type(Song.class).id(id.toLowerCase()).now();
    }
    
    public static Song loadFromDTO(SongSummaryDTO dto) {
        Transaction tx = ODF.get().beginTransaction();
        try {
            Song tmp = ODF.get().load().type(Song.class).id((dto.getTitle()+SEPARATOR+dto.getArtist()+SEPARATOR+dto.getAlbum()).toLowerCase()).now();
            tx.commit();
            return tmp;
        }
        finally {
            if (tx.isActive())
                tx.rollback();
        }
    }
    
    public SongSummaryDTO toSummaryDTO() {
        return new SongSummaryDTO(this.artist, this.title, this.album);
    }
    
    public SongDTO toSongDTO() {
        SongDTO tmp = new SongDTO();
        tmp.setArtist(this.artist);
        tmp.setTitle(this.title);
        tmp.setAlbum(this.album);
        tmp.setComposer(this.composer);
        tmp.setFile(this.file);
        tmp.setGenre(this.genre);
        tmp.setTrackNumber(this.track_number);
        tmp.setYear(this.year); 
        tmp.setAlbumCover(this.album_cover);
        tmp.setNumOwners(this.num_owners);
        tmp.setYoutubeCode(this.youtube_code);
        tmp.setNumRatings(this.num_ratings);
        tmp.setRating(this.rating);
        return tmp;
    }
    
    
    /**
     * 
     * Recupera le informazioni da un oggetto SongDTO e le salva nel modo più opportuno nel Datastore.
     * 
     */
    public static Song storeOrUpdateFromDTO(SongDTO song) {

//        if (song != null) {
//            if (song.getTitle() == null)
//                song.setTitle("");
//            if (song.getArtist() == null)
//                song.setArtist("");
//            if (song.getAlbum() == null)
//                song.setAlbum("");
//        }
        
        //Se la canzone non ha almeno il titolo non viene inserita nel database e ritorna un riferimento null
        if (song.getTitle().equals("") && (song.getArtist().equals("") || song.getAlbum().equals("")))
            return null;
        
//        Transaction tx = ODF.get().beginTransaction();
        
//        try { 
            //Ricerca e caricamento della canzone se già presente nel Datastore
            Song s = load(song.getTitle()+SEPARATOR+song.getArtist()+SEPARATOR+song.getAlbum());
            
            if (s == null) {
                //La canzone non è presente nel Datastore
                s = new Song();
                s.setAlbum(song.getAlbum());
                s.setTitle(song.getTitle());
                s.setArtist(song.getArtist());
                s.setId(song.getTitle()+SEPARATOR+song.getArtist()+SEPARATOR+song.getAlbum());
                if (!song.getComposer().equals(""))
                    s.setComposer(song.getComposer());
                if (!song.getGenre().equals(""))
                    s.setGenre(song.getGenre());
                if (!song.getTrackNumber().equals(""))
                    s.setTrackNumber(song.getTrackNumber());
                if (!song.getYear().equals(""))
                    s.setYear(song.getYear()); 
                s.setYoutubeCode(Utils.getYouTubeCode(s.getTitle()+" "+s.getArtist()));
                s.setAlbumCover(Utils.getCoverImage(s.getTitle()+" "+s.getArtist()));
                s.update();
//                tx.commit();
                return s;
            }
            
            else {
                //La canzone è presente nel Datastore.
                //inserimento delle informazioni prese dal DTO
                if (s.getComposer().equals("") && song.getComposer() != null)
                    s.setComposer(song.getComposer());
                if (s.getGenre().equals("") && song.getGenre() != null)
                    s.setGenre(song.getGenre());
                if (s.getTrackNumber().equals("") && song.getTrackNumber() != null)
                    s.setTrackNumber(song.getTrackNumber());
                if (s.getYear().equals("") && song.getYear() != null)
                    s.setYear(song.getYear());   
                
                //prelievo delle informazioni da servizi esterni
                if (s.getYoutubeCode().equals("")) {
                    s.setYoutubeCode(Utils.getYouTubeCode(s.getTitle()+" "+s.getArtist()));
                }  
                if (s.getAlbumCover().equals("")) {
                    s.setAlbumCover(Utils.getCoverImage(s.getTitle()+" "+s.getArtist()));
                }
//                tx.commit();
                return s;
            }
//        }
//        finally {
//            if (tx.isActive())
//                tx.rollback();
//        }
    }
    
    
    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id.toLowerCase();
        this.update();
    }
    
    public Song changeTitle(String title) {
        
        //Se la canzone precedente alla modifica non ha altri utenti che la possiedono viene
        //cancellata dal Datastore perchè probabilmente contiene informazioni errate o incomplete.
        ODF.get().update(this);
        if (this.num_owners == 0)
            Song.deleteSong(this);
        else {
            this.deleteOwner();
        }
    
        if (title != null && title.equals("")==false)
            this.title = title;
        else
            this.title = "";
        
        Song tmp= Song.load(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
        if (tmp == null) { 
            //La canzone non è presente nel Datastore.
            this.setId(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
            return this;
        }
        else {
            //La canzone è presente nel Datastore.
            return tmp;
        }

    }

    public Song changeArtist(String artist) {
        
        //Se la canzone precedente alla modifica non ha altri utenti che la possiedono viene
        //cancellata dal Datastore perchè probabilmente contiene informazioni errate o incomplete.
        ODF.get().update(this);
        if (this.num_owners == 0)
            Song.deleteSong(this);
        else {
            this.deleteOwner();
        }
    
        if (artist != null && artist.equals("")==false)
            this.artist = artist;
        else
            this.artist = "";
    
        Song tmp= Song.load(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
        
        if (tmp == null) { 
            //La canzone non è presente nel Datastore.
            this.setId(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
            return this;
        }
        else {
            //La canzone è presente nel Datastore.
            return tmp;
        }

    }
    
    public Song changeAlbum(String album) {
        
        //Se la canzone precedente alla modifica non ha altri utenti che la possiedono viene
        //cancellata dal Datastore perchè probabilmente contiene informazioni errate o incomplete.
        ODF.get().storeOrUpdate(this);
        if (this.num_owners == 0)
            Song.deleteSong(this);
        else {
            this.deleteOwner();
        }

        if (album != null && album.equals("")==false)
            this.album = album;
        else
            this.album = "";

        Song tmp= Song.load(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
        if (tmp == null) { 
            //La canzone non è presente nel Datastore.
            this.setId(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
            return this;
        }
        else {
            //La canzone è presente nel Datastore.
            return tmp;
        }
            
    }
    
    private void setTitle(String title) {
        this.title = title;
    }

    private void setAlbum(String album) {
        this.album = album;
    }

    private void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    void newOwner() {
        this.num_owners += 1;
        this.update();
    }
    
    void deleteOwner() {
        this.num_owners -= 1;
        this.update();
    }

    public int getNumOwners() {
        return num_owners;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumCover() {
        return album_cover;
    }

    public void setAlbumCover(String album_cover) {
        this.album_cover = album_cover;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getTrackNumber() {
        return track_number;
    }

    public void setTrackNumber(String track_number) {
        this.track_number = track_number;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    
    public void setYoutubeCode(String youtube_code) {
        this.youtube_code = youtube_code;
    }

    public String getYoutubeCode() {
        return youtube_code;
    }

    public int getRatingInt() {
        return (int) rating;
    }
    
    public double getRatingDouble() {
        return rating;
    }

    
    /**
     * 
     * Aggiunge la votazione data in input alla canzone aggiornando nel modo appropriato gli 
     * attributi rating e num_ratings. Questo metodo, poichè gli stessi dati possono essere 
     * acceduti concorrentemente, viene eseguito all'interno di una tranzazione.
     * 
     */
    public double addRate(int rating) {
        
        Transaction tx = ODF.get().beginTransaction();
        
        try {
            this.num_ratings++;
            this.rating = (this.rating + rating) / this.num_ratings;
            this.update();
            tx.commit();
            return this.rating;
        }
        finally {
            if (tx.isActive())
                tx.rollback();
        }
        
    }
    
    
    /**
     * 
     * Permette ad ogni utente di modificare la propria votazione su questo brano dando
     * in input il voto precendete e quello nuovo. Questo metodo, poiché gli stessi dati
     * possono essere acceduti concorrentemente, viene eseguito all'interno di una tranzazione.
     * 
     */
    public double changeRate(int old_rating, int rating) {
        
        Transaction tx = ODF.get().beginTransaction();
        
        try {
            this.rating = ((this.rating * this.num_ratings) + rating - old_rating) / this.num_ratings;
            this.update();
            tx.commit();
            return this.rating;
        }
        finally {
            if (tx.isActive())
                tx.rollback();
        }
    }

    public int getNumRatings() {
        return num_ratings;
    }

    static void deleteSong(Song s) {
        ODF.get().storeOrUpdate(s);
        ODF.get().delete(s);
    }

}
