/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.server.utils.Utils;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

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
public class Song {
    
    static final String SEPARATOR = "-vt.g-";

    @Id private String id;
    
    @Index private String title;
    
    @Index private String album;
    
    @Index private String artist;
    
    @Index private int numOwners;
    
    @Index private double rating;
    
    @Index private String genre;
    
    private String albumCover;
    
    private String year;
    
    private String composer;
    
    private String trackNumber;
    
    private String file;
    
    private String youtube_code;
    
    private int num_ratings;
    
    
    public Song() {
        this.id = SEPARATOR;
        this.numOwners = 0;
        this.album = "";
        this.albumCover = "";
        this.artist = "";
        this.composer = "";
        this.file = "";
        this.genre = "";
        this.title = "";
        this.trackNumber = "";
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
        return ODF.get().load().type(Song.class).id((dto.getTitle()+SEPARATOR+dto.getArtist()+SEPARATOR+dto.getAlbum()).toLowerCase()).now();
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
        tmp.setAlbumCover(this.albumCover);
        tmp.setNumOwners(this.numOwners);
        tmp.setYoutubeCode(this.youtube_code);
        tmp.setNumRatings(this.num_ratings);
        tmp.setRating(this.rating);
        return tmp;
    }
    
    public static Song storeOrUpdateFromDTO(SongDTO song) {
        
        //prelievo delle informazioni dal DTO
        Song s = new Song().changeAlbum(song.getAlbum()).changeArtist(song.getArtist()).changeTitle(song.getTitle());
        if (s.getComposer() == "" && song.getComposer() != null)
            s.setComposer(song.getComposer());
        if (s.getGenre() == "" && song.getGenre() != null)
            s.setGenre(song.getGenre());
        if (s.getTrackNumber() == "" && song.getTrackNumber() != null)
            s.setTrackNumber(song.getTrackNumber());
        if (s.getYear() == "" && song.getYear() != null)
            s.setYear(song.getYear());      
    
        /////////////////////////////////////////////////////////////////////////////////
        //INFORMAZIONI PRIMARIE COMPLETE
        if (song.getArtist() != "" && song.getTitle() != "" && song.getAlbum() != "") {
            
            //prelievo delle informazioni da servizi esterni
            if (s.getAlbumCover().equals("")) {
                s.setAlbumCover(Utils.getCoverImage(s.getTitle()+" "+s.getArtist()));
            }
            if (s.getYoutubeCode().equals("")) {
                s.setYoutubeCode(Utils.getYouTubeCode(s.getTitle()+" "+s.getArtist()));
            }  
            
            return s;
        }
        
        /////////////////////////////////////////////////////////////////////////////////
        //MANCA L'ARTISTA 
        if (s.getTitle() != "" && s.getAlbum() != "") {
            
            //prelievo delle informazioni da servizi esterni
            if (s.getAlbumCover().equals("")) {
                s.setAlbumCover(Utils.getCoverImage(s.getTitle()+" "+s.getArtist()));
            }
            if (s.getYoutubeCode().equals("")) {
                s.setYoutubeCode(Utils.getYouTubeCode(s.getTitle()+" "+s.getArtist()));
            }  
            
            return s;
        }
        
        
        /////////////////////////////////////////////////////////////////////////////////
        //MANCA L'ALBUM
        if (s.getTitle() != "" && s.getArtist() != "") {
            
            //prelievo delle informazioni da servizi esterni
            if (s.getAlbumCover().equals("")) {
                s.setAlbumCover(Utils.getCoverImage(s.getTitle()+" "+s.getArtist()));
            }
            if (s.getYoutubeCode().equals("")) {
                s.setYoutubeCode(Utils.getYouTubeCode(s.getTitle()+" "+s.getArtist()));
            }  
            
            return s;
        } 
            
        
        /////////////////////////////////////////////////////////////////////////////////
        //MANCA IL TITOLO
        if (s.getAlbum() != "" && s.getArtist() != "") {
            
            //prelievo delle informazioni da servizi esterni
            if (s.getAlbumCover().equals("")) {
                s.setAlbumCover(Utils.getCoverImage(s.getTitle()+" "+s.getArtist()));
            } 
            
            return s;
        }
        
        
        /////////////////////////////////////////////////////////////////////////////////
        //MANCANO SIA L'ARTISTA CHE L'ALBUM
        if (s.getTitle() != "") {
            
            //prelievo delle informazioni da servizi esterni
            if (s.getAlbumCover().equals("")) {
                s.setAlbumCover(Utils.getCoverImage(s.getTitle()+" "+s.getArtist()));
            }
            if (s.getYoutubeCode().equals("")) {
                s.setYoutubeCode(Utils.getYouTubeCode(s.getTitle()+" "+s.getArtist()));
            }  
            
            return s;
        }
        
        /////////////////////////////////////////////////////////////////////////////////
        //MANCANO TUTTE LE INFORMAZIONI PRIMARIE
        return null;
        
    }
    
    
    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id.toLowerCase();
        this.update();
    }
    
    public Song changeTitle(String title) {
        
        ODF.get().update(this);
        if (this.numOwners == 0)
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
            this.setId(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
            return this;
        }
        else {
            return tmp;
        }
    }

    public Song changeArtist(String artist) {
        
        ODF.get().update(this);
        if (this.numOwners == 0)
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
            this.setId(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
            return this;
        }
        else {
            return tmp;
        }
    }
    
    public Song changeAlbum(String album) {
        
        ODF.get().storeOrUpdate(this);
        if (this.numOwners == 0)
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
                this.setId(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
                return this;
            }
            else {
                return tmp;
            }
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
        this.numOwners += 1;
        this.update();
    }
    
    void deleteOwner() {
        this.numOwners -= 1;
        this.update();
    }

    public int getNumOwners() {
        return numOwners;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
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

    public double addRate(int rating) {
        this.num_ratings++;
        this.rating = (this.rating + rating) / this.num_ratings;
        this.update();
        return this.rating;
    }
    
    public double changeRate(int old_rating, int rating) {
        this.rating = ((this.rating * this.num_ratings) + rating - old_rating) / this.num_ratings;
        this.update();
        return this.rating;
    }

    public int getNumRatings() {
        return num_ratings;
    }

    static void deleteSong(Song s) {
        ODF.get().storeOrUpdate(s);
        ODF.get().delete(s);
    }

}
