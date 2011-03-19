package it.unipd.netmus.server.utils;

import it.unipd.netmus.server.youtube.YouTubeManager;
import it.unipd.netmus.shared.SongDTO;

import java.util.Collection;
import java.util.Iterator;

import de.umass.lastfm.Album;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;

/**
 * Nome: Utils.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 17 Febbraio 2011
 */
public final class Utils {
    
    /**
     * Questo metodo attiva la cache di Last.fm per AppEngine ed esegue una
     * ricerca esterna, con keyword artista e album, per recuperare l’url della
     * copertina dell’album relativo ad un brano, in formato JPG.
     */
    public static String getCoverImage(String artist, String album) {
        
        try {

            Album search = Album.getInfo(artist, album, "33d9ef520018d87db5dff9ef74cc4904");

            if (search != null) {
                String album_image = search.getImageURL(ImageSize.EXTRALARGE);
                if (album_image != null) {
                    return album_image;
                }
                else {
                    return "";
                }
            }
            else return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Con questo metodo andremo a provare a recuperare informazioni ulteriori
     * di un brano che non ha info nel tag, usando come keyword il nome del file
     * stesso, più eventuali paroe chiave. Se verrà trovato qualcosa si proverà a 
     * proporlo all’utente. In uscita restituirà un SongDTO.
     * 
     * Per un corretto utilizzo, il nome del file deve sempre essere al primo posto,
     * seguito dalle altre chiavi di ricerca.
     */
    public static SongDTO getSongFromFileName(String filename) {
    	
    	//pulisco la stringa da percorsi e estensione, rimango solo con il nome del file.
    	filename = filename.replaceAll("(^.*(\\\\|/))|\\.MP3|\\.mp3|\\.Mp3|\\.mP3", "");
        return getSongFromIncompleteInfo(filename);
    }
    
    /*
     * Richiama last.fm per vedere se si riesce a trovare una canzone con le poche informazioni a disposizione.
     */
    public static SongDTO getSongFromIncompleteInfo(String info){
    	try {
    		System.out.println("Ricerca info: " + info);
            Collection<Track> search = Track.search(info,
                    "33d9ef520018d87db5dff9ef74cc4904");

            Iterator<Track> it = search.iterator();
            Track t;
            if (it.hasNext())
                t = it.next();
            else
                t = null;

            if (t == null)
                return null;
            else {
                SongDTO song = new SongDTO();
                song.setTitle(t.getName());
                song.setArtist(t.getArtist());
                if (t.getAlbum() != null)
                	song.setAlbum(t.getAlbum());
                song.setAlbumCover(t.getImageURL(ImageSize.EXTRALARGE));
                return song;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Grazie a YouTubeManager restituisce la prima occorrenza di una ricerca su
     * YouTube per pertinenza alla keyword in ingresso (autore titolo). Se la
     * ricerca non produce risultati restituisce stringa vuota.
     */
    public static String getYouTubeCode(String keywords, String ip) {
        
        return getYouTubeCode(keywords, ip, 3);
    }
    
    private static String getYouTubeCode(String keywords, String ip, int retries) {
        
        if (retries > 0) {
            try {
                return YouTubeManager.getSearchResult(keywords, ip);
            }
            catch (Exception e) {
                return getYouTubeCode(keywords, ip, retries-1);
            }

        } else {
            return "";
        }        
    }

}
