package it.unipd.netmus.server.utils;

import it.unipd.netmus.server.youtube.YouTubeManager;
import it.unipd.netmus.shared.SongDTO;

import java.util.Collection;
import java.util.Iterator;

import de.umass.lastfm.Caller;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;

/**
 * Nome: Utils.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 17 Febbraio 2011
 */
public final class Utils {

    /**
     * Grazie a YouTubeManager restituisce la prima occorrenza di una ricerca su
     * YouTube per pertinenza alla keyword in ingresso (autore titolo). Se la 
     * ricerca non produce risultati restituisce stringa vuota.
     */
    public static String getYouTubeCode(String keywords) {
        
        return YouTubeManager.getSearchResult(keywords);
    }
    
    /**
     * Questo metodo attiva la cache di Last.fm per AppEngine ed esegue una
     * ricerca esterna, con keyword artista e album, per recuperare l’url della 
     * copertina dell’album relativo ad un brano, in formato JPG.
     */
    public static String getCoverImage(String keywords) {
        
        try {
         // attivo il nuovo gestore di cache (x LAST FM)
            Caller.getInstance().setCache(new AppEngineCache());
            
            Collection<Track> search = Track.search(keywords,
            "33d9ef520018d87db5dff9ef74cc4904");

            Iterator<Track> it = search.iterator();
            Track t;
            if (it.hasNext())
                t = (Track) it.next();
            else t = null;
            
            if (t==null)
                return "";
            else {
                if (t.getImageURL(ImageSize.EXTRALARGE) != null)
                    return t.getImageURL(ImageSize.EXTRALARGE);
                else return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Con questo metodo andremo a provare a recuperare informazioni ulteriori di
     * un brano che non ha info nel tag, usando come keyword il nome del file stesso. 
     * Se verrà trovato qualcosa si proverà a proporlo all’utente. In uscita restituirà un SongDTO.
     */
    public static SongDTO getSongFromFileName(String filename) {
        
        try {
         // attivo il nuovo gestore di cache (x LAST FM)
            Caller.getInstance().setCache(new AppEngineCache());
            
            Collection<Track> search = Track.search(filename,
            "33d9ef520018d87db5dff9ef74cc4904");
            
            Iterator<Track> it = search.iterator();
            Track t;
            if (it.hasNext())
                t = (Track) it.next();
            else t = null;
            
            
            if (t==null)
                return null;
            else {
                SongDTO song = new SongDTO();
                song.setTitle(t.getName());
                song.setArtist(t.getArtist());
                song.setAlbum(t.getAlbum());
                song.setAlbumCover(t.getImageURL(ImageSize.EXTRALARGE));
                
                if (song.getTitle() != null && song.getArtist() != null) {
                    String keywords = song.getTitle()+ " " +song.getArtist();
                    
                    song.setYoutubeCode(getYouTubeCode(keywords));
                }
                
                return song;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
}
