/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;
import it.unipd.netmus.shared.exception.DatastoreException;

import com.google.appengine.api.datastore.Blob;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Type;
import com.reveregroup.gwt.imagepreloader.FitImage;

/**
 * @author ValterTexasGroup
 *
 *
 *PROCEDURA SUGGERTA PER INSERIRE O MODIFICARE UNA CANZONE NEL DATABASE 
 *
 * Song s = new Song().changeArtist("artist").changeTitle("title");
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
public class Song {
    
    static final String SEPARATOR = "-vt.g-";

    @Id private String id;
    
    @Index private String title;
    
    @Index private String genre;
    
    @Index private String artist;
    
    @Index private int numOwners;
    
    private String album;
    
    @Type(Blob.class) private FitImage albumCover;
    
    private String year;
    
    private String composer;
    
    private String trackNumber;
    
    private String file;
    
    
    public Song() {
        this.id = SEPARATOR;
        this.numOwners = 0;
    }
    
    public void store() throws DatastoreException {
        if (this.title == null && this.artist == null)
            throw new DatastoreException();
        else
            ODF.get().store().instance(this).ensureUniqueKey().now();
    }
    
    public void update() {
        ODF.get().storeOrUpdate(this);
    }
    
    public static Song load(String id) {
        return ODF.get().load().type(Song.class).id(id.toLowerCase()).now();
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
        tmp.setTrackNumber(this.trackNumber);
        tmp.setYear(this.year); 
        //tmp.setAlbumCover(this.albumCover.toString());
        tmp.setNumOwners(this.numOwners);
        return tmp;
    }
    
    public static Song storeOrUpdateFromDTO(SongDTO song) {
        Song s= new Song().changeArtist(song.getArtist()).changeTitle(song.getTitle());
        if (song.getAlbum() != null)
            s.setAlbum(song.getAlbum());
        if (song.getAlbumCover() != null)
            s.setAlbumCover(s.getAlbumCover());
        if (song.getComposer() != null)
            s.setComposer(s.getComposer());
        if (song.getFile() != null)
            s.setFile(s.getFile());
        if (song.getGenre() != null)
            s.setGenre(s.getGenre());
        if (song.getTrackNumber() != null)
            s.setTrackNumber(s.getTrackNumber());
        if (song.getYear() != null)
            s.setYear(s.getYear());
        return s;
    }
    
    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id.toLowerCase();
        this.update();
    }

    public Song changeArtist(String artist) {
        if (artist != null && !artist.equals("")) {
            Song.deleteSong(this);
            this.artist = artist;
        
            Song tmp= Song.load(this.title+SEPARATOR+this.artist);
            
            if (tmp == null) { 
                this.setId(this.title+SEPARATOR+this.artist);
                return this;
            }
            else {
                return tmp;
            }
        }
        else return this;
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
        System.out.println("Add owner to "+this.getTitle()+": "+this.getNumOwners());
        this.numOwners += 1;
        this.update();
    }
    
    void deleteOwner() {
        System.out.println("Delete owner to "+this.getTitle()+": "+this.getNumOwners());
        this.numOwners -= 1;
        this.update();
    }

    public int getNumOwners() {
        return numOwners;
    }

    public Song changeTitle(String title) {
        if (artist != null && !artist.equals("")) {
            Song.deleteSong(this);
            this.title = title;
        
            Song tmp= Song.load(this.title+SEPARATOR+this.artist);
            if (tmp == null) { 
                this.setId(this.title+SEPARATOR+this.artist);
                return this;
            }
            else {
                return tmp;
            }
        }
        else return this;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public FitImage getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(FitImage albumCover) {
        this.albumCover = albumCover;
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
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    
    static void deleteSong(Song s) {
        ODF.get().storeOrUpdate(s);
        ODF.get().delete(s);
    }

}
