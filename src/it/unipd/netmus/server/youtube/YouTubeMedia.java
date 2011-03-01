package it.unipd.netmus.server.youtube;

/**
 * Nome: YouTubeMedia.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 17 Febbraio 2011
 */
public class YouTubeMedia {

    private String location;
    private String type;

    public YouTubeMedia(String location, String type) {
        super();
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }
}