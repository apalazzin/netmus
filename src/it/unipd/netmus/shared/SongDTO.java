package it.unipd.netmus.shared;

/**
 * Nome: SongDTO.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * Come tutte le altri classi di questo package questa classe permette lo
 * scambio di informazioni tra client e server all'interno delle chiamate RPC.
 * Contiene e rappresenta tutti i dati di un brano musicale nel sistema Netmus.
 * 
 * 
 */
@SuppressWarnings("serial")
public class SongDTO extends SongSummaryDTO {

    private String year;

    private String composer;

    private String genre;

    private String track_number;

    private String file;

    private int num_owners;

    private int num_ratings;

    // costruttore di default
    public SongDTO() {
        super();
        this.year = "";
        this.composer = "";
        this.genre = "";
        this.track_number = "";
        this.file = "";
        this.num_owners = 0;
        this.num_ratings = 0;
    }

    public String getComposer() {
        return composer;
    }

    public String getFile() {
        return file;
    }

    public String getGenre() {
        return genre;
    }

    public int getNumOwners() {
        return num_owners;
    }

    public int getNumRatings() {
        return num_ratings;
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

    public void setFile(String file) {
        this.file = file;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setNumOwners(int num_owners) {
        this.num_owners = num_owners;
    }

    public void setNumRatings(int num_ratings) {
        this.num_ratings = num_ratings;
    }

    public void setTrackNumber(String track_number) {
        this.track_number = track_number;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
