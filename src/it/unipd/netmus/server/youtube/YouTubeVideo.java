package it.unipd.netmus.server.youtube;

import java.util.List;

/**
 * Nome: YouTubeVideo.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 17 Febbraio 2011
 */
public class YouTubeVideo {

    private List<YouTubeMedia> medias;
    private String video_code;

    public List<YouTubeMedia> getMedias() {
        return medias;
    }

    public String getVideoCode() {
        return video_code;
    }

    public String retrieveHttpLocation() {
        if (medias == null || medias.isEmpty()) {
            return null;
        }
        for (YouTubeMedia media : medias) {
            String location = media.getLocation();
            if (location.startsWith("http")) {
                return location;
            }
        }
        return null;
    }

    public void setMedias(List<YouTubeMedia> medias) {
        this.medias = medias;
    }

    public void setVideoCode(String embeddedWebPlayerUrl) {
        this.video_code = embeddedWebPlayerUrl;
    }
}