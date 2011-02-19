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
public class MusicLibraryDTO implements Serializable {
    
	private UserSummaryDTO owner;
	
	private List<SongDTO> songs;
	
	public MusicLibraryDTO() {
	}
	
	public MusicLibraryDTO(UserSummaryDTO user, List<SongDTO> s){
		setOwner(user);
		setSongs(s);
	}

    public void setOwner(UserSummaryDTO owner) {
        this.owner = owner;
    }

    public UserSummaryDTO getOwner() {
        return owner;
    }

    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }

    public List<SongDTO> getSongs() {
        return songs;
    }
}
