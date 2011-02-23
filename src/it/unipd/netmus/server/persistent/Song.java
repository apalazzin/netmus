/**
 * 
 */
package it.unipd.netmus.server.persistent;

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
    
    static final String UNKNOWN_ARTIST = "&unknartst";
    
    static final String UNKNOWN_TITLE = "&unknttl";
    
    static final String UNKNOWN_ALBUM = "&unknalbm";

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
    
    private String playme_code;
    
    private int num_ratings;
    
    
    public Song() {
        this.id = SEPARATOR;
        this.numOwners = 0;
        this.album = "";
        this.artist = "";
        this.composer = "";
        this.file = "";
        this.genre = "";
        this.title = "";
        this.trackNumber = "";
        this.year = "";
        this.youtube_code = "";
        this.playme_code = "";
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
        //tmp.setAlbumCover(this.albumCover.toString());
        tmp.setNumOwners(this.numOwners);
        tmp.setYoutubeCode(this.youtube_code);
        tmp.setPlaymeCode(this.playme_code);
        tmp.setNumRatings(this.num_ratings);
        tmp.setRating(this.rating);
        return tmp;
    }
    
    
    //OLD VERSION
    public static Song storeOrUpdateFromDTO(SongDTO song) {
        Song s= new Song().changeArtist(song.getArtist()).changeTitle(song.getTitle()).changeAlbum(song.getAlbum());
        if (song.getAlbum() != null)
            s.setAlbum(song.getAlbum());
        if (song.getComposer() != null)
            s.setComposer(song.getComposer());
        if (song.getGenre() != null)
            s.setGenre(song.getGenre());
        if (song.getTrackNumber() != null)
            s.setTrackNumber(song.getTrackNumber());
        if (song.getYear() != null)
            s.setYear(song.getYear());
        return s;
    }
    
    //NEW VERSION IN COSTRUZIONE
    /*
    public static Song storeOrUpdateFromDTO(SongDTO song) throws Exception {
        //prelievo delle informazioni dal DTO
        Song s = new Song();
        if (song.getTitle() != null)
            s.setTitle(song.getTitle());
        if (song.getArtist() != null)
            s.setArtist(song.getArtist());
        if (song.getAlbum() != null)
            s.setAlbum(song.getAlbum());
        if (song.getComposer() != null)
            s.setComposer(song.getComposer());
        if (song.getFile() != null)
            s.setFile(song.getFile());
        if (song.getGenre() != null)
            s.setGenre(song.getGenre());
        if (song.getTrackNumber() != null)
            s.setTrackNumber(song.getTrackNumber());
        if (song.getYear() != null)
            s.setYear(song.getYear());
        
        
        /////////////////////////////////////////////////////////////////////////////////
        //se le informazioni primarie sono complete procede con la ricerca nel database
        if (s.getArtist() != "" && s.getTitle() != "" && s.getAlbum() != "") {
            
            Song tmp= Song.load(s.getTitle()+SEPARATOR+s.getArtist()+SEPARATOR+s.getAlbum());
            
            if (tmp == null) { 
                //ricerca esterna
                    //da implementare
                    System.out.println("Effettuata ricerca esterna senza risultati.");
                //inserimento nel database
                s.setId(s.getTitle()+SEPARATOR+s.getArtist()+SEPARATOR+s.getAlbum());
                return s;
            }
            else {
                //aggiornamento tag mancanti nel database
                if (tmp.getComposer() == "")
                    tmp.setComposer(s.getComposer());
                if (tmp.getGenre() == "")
                    tmp.setGenre(s.getGenre());
                if (tmp.getTrackNumber() == "")
                    tmp.setTrackNumber(s.getTrackNumber());
                if (tmp.getYear() == "")
                    tmp.setYear(s.getYear());
                return tmp;
            }
        }
        
        
        /////////////////////////////////////////////////////////////////////////////////
        //Le informazioni primarie non sono complete però avendo titolo e album si può risalire all'artista 
        if (s.getTitle() != "" && s.getAlbum() != "") {
            
            List<Song> possible_songs= ODF.get().find().type(Song.class).addFilter("title", FilterOperator.EQUAL, s.getTitle())
            .addFilter("album", FilterOperator.EQUAL, s.getAlbum()).returnAll().now();
            
            if (possible_songs.size()>0) { 
                
                //Assumiamo che non esistano due canzoni di artisti diversi con album e titolo uguali
                Song s2 = possible_songs.get(0);

                //aggiornamento tag mancanti nel database
                if (s2.getComposer() == "")
                    s2.setComposer(s.getComposer());
                if (s2.getGenre() == "")
                    s2.setGenre(s.getGenre());
                if (s2.getTrackNumber() == "")
                    s2.setTrackNumber(s.getTrackNumber());
                if (s2.getYear() == "")
                    s2.setYear(s.getYear());
                return s2;
            }
            else {
                
                //Ricerca esterna di possibili album da associare a questa canzone
                //if (trovato_possibile_artista) {
                    //inserimento nel database
                    //s.setId(s.getTitle()+SEPARATOR+s.getArtist()+SEPARATOR+s.getAlbum());
                    //return s;
                //}
                //else {
                    s.setArtist(UNKNOWN_ARTIST);
                    s.setId(s.getTitle()+SEPARATOR+s.getArtist()+SEPARATOR+s.getAlbum());
                    return s;
                //}
            }
        }
        
        
        /////////////////////////////////////////////////////////////////////////////////
        //Le informazioni primarie non sono complete, sarà lanciata un'apposita eccezione che contiene 
        //le possibilità da sottoporre all'utente
        if (s.getTitle() != "" && s.getArtist() != "") {
            
            List<Song> possible_songs= ODF.get().find().type(Song.class).addFilter("artist", FilterOperator.EQUAL, s.getArtist())
            .addFilter("title", FilterOperator.EQUAL, s.getTitle()).returnAll().now();
            
            if (possible_songs.size()>0) { 
                
                //Sono presenti nel database delle canzoni che potrebbero coincidere con questa, differiscono
                //solamente per il nome dell'album. L'eccezione lanciata contiene i possibili nomi di album.
                
                //Prima di ritornare l'eccezione all'utente salva comunque il brano con alcun sconosciuto nel database
                s.setAlbum(UNKNOWN_ALBUM);
                possible_songs.add(s);
                s.changeAlbum(s.getAlbum());
                throw new Exception();
                
            }
            else {
                
                //Ricerca esterna di possibili album da associare a questa canzone
                //if (trovati_possibili_album) {
                    //s.setAlbum(UNKNOWN_ALBUM);
                    //possible_songs.add(s);
                    //s.changeAlbum(s.getAlbum());
                    //throw new Exception();
                //}
                
            }
        }  
            
        
        /////////////////////////////////////////////////////////////////////////////////
        //Le informazioni primarie non sono complete, sarà lanciata un'apposita eccezione che contiene 
        //le possibilità da sottoporre all'utente
        if (s.getAlbum() != "" && s.getArtist() != "") {
            
            List<Song> possible_songs= ODF.get().find().type(Song.class).addFilter("artist", FilterOperator.EQUAL, s.getArtist())
            .addFilter("album", FilterOperator.EQUAL, s.getAlbum()).returnAll().now();
            
            if (possible_songs.size()>0) { 
                
                //Sono presenti nel database delle canzoni che potrebbero coincidere con questa, differiscono
                //solamente per il nome dell'album. L'eccezione lanciata contiene i possibili nomi di album.
                
                //Prima di ritornare l'eccezione all'utente salva comunque il brano con alcun sconosciuto nel database
                s.setAlbum(UNKNOWN_TITLE);
                possible_songs.add(s);
                s.changeTitle(s.getTitle());
                throw new Exception();
                
            }
            else {
                
                //Ricerca esterna di possibili album da associare a questa canzone
                //if (trovati_possibili_album) {
                    //s.setAlbum(UNKNOWN_TITLE);
                    //possible_songs.add(s);
                    //s.changeTitle(s.getTitle());
                    //throw new Exception();
                //}
                
            }
        }
        return null;
    }
    */
    
    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id.toLowerCase();
        this.update();
    }
    
    public Song changeTitle(String title) {
        if (title != null && title.equals("")==false) {
            Song.deleteSong(this);
            this.setTitle(title);
        
            Song tmp= Song.load(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
            if (tmp == null) { 
                this.setId(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
                return this;
            }
            else {
                return tmp;
            }
        }
        else return this;
    }
    
    private void setTitle(String title) {
        this.title = title;
    }

    public Song changeArtist(String artist) {
        if (artist != null && artist.equals("")==false) {
            Song.deleteSong(this);
            this.setArtist(artist);
        
            Song tmp= Song.load(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
            
            if (tmp == null) { 
                this.setId(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
                return this;
            }
            else {
                return tmp;
            }
        }
        else return this;
    }
        
    private void setArtist(String artist) {
        this.artist = artist;
    }
    
    public Song changeAlbum(String album) {
        if (album != null && album.equals("")==false) {
            Song.deleteSong(this);
            this.setAlbum(album);
        
            Song tmp= Song.load(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
            if (tmp == null) { 
                this.setId(this.title+SEPARATOR+this.artist+SEPARATOR+this.album);
                return this;
            }
            else {
                return tmp;
            }
        }
        else return this;
    }

    private void setAlbum(String album) {
        this.album = album;
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

    public void setPlaymeCode(String playme_code) {
        this.playme_code = playme_code;
    }

    public String getPlaymeCode() {
        return playme_code;
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
