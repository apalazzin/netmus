/**
 * 
 */
package it.unipd.netmus.shared;

import java.io.Serializable;
import java.util.List;
/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class MusicLibrarySummaryDTO implements Serializable {
    
    private UserSummaryDTO owner;
    
    private List<SongSummaryDTO> songs;
    
    public MusicLibrarySummaryDTO() {
    }
    
    public MusicLibrarySummaryDTO(UserSummaryDTO user, List<SongSummaryDTO> s){
        setOwner(user);
        setSongs(s);
    }

    public void setOwner(UserSummaryDTO owner) {
        this.owner = owner;
    }

    public UserSummaryDTO getOwner() {
        return owner;
    }

    public void setSongs(List<SongSummaryDTO> songs) {
        this.songs = songs;
    }

    public List<SongSummaryDTO> getSongs() {
        return songs;
    }
    
    public int getLibrarySize() {
        return songs.size();
    }
}
