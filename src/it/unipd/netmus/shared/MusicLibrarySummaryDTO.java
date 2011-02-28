/**
 * 
 */
package it.unipd.netmus.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class MusicLibrarySummaryDTO implements Serializable {
    
    private UserDTO owner;
    
    private List<SongSummaryDTO> songs;
    
    private List<String> playlists;
    
    public MusicLibrarySummaryDTO() {
        this.owner = new UserDTO();
        this.songs = new ArrayList<SongSummaryDTO>();
    }
    
    public MusicLibrarySummaryDTO(UserDTO user, List<SongSummaryDTO> s, List<String> playlists){
        setOwner(user);
        setSongs(s);
        setPlaylists(playlists);
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public UserDTO getOwner() {
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

    public void setPlaylists(List<String> playlists) {
        this.playlists = playlists;
    }

    public List<String> getPlaylists() {
        return playlists;
    }
}
