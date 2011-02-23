/**
 * 
 */
package it.unipd.netmus.server.utils;

import java.util.Collection;
import java.util.Iterator;

import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;
import it.unipd.netmus.server.youtube.YouTubeManager;
import it.unipd.netmus.shared.SongDTO;

/**
 * @author ValterTexasGroup
 *
 */
final public class ExternalService {
    
    // ritorna stringa vuota se non trova nulla, se no ritorna il codice del video
    public static String getYouTubeCode(String keywords) {
        
        return YouTubeManager.getSearchResult(keywords);
    }
    
    public static String getCoverImage(String keywords) {
        
        Collection<Track> search = Track.search(keywords,
        "33d9ef520018d87db5dff9ef74cc4904");

        Iterator<Track> it = search.iterator();
        Track t;
        if (it.hasNext())
            t = (Track) it.next();
        else t = null;
        
        if (t==null)
            return "";
        else
            return t.getImageURL(ImageSize.EXTRALARGE);
    }
    
    public static SongDTO getSongFromFileName(String filename) {
        
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
    }
}
