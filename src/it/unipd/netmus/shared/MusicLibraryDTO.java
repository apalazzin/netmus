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
    
	private UserDTO owner;
	
	private List<SongDTO> songs;
	
	public MusicLibraryDTO() {
	}
	
	public MusicLibraryDTO(UserDTO user, List<SongDTO> s){
		setOwner(user);
		setSongs(s);
	}

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }

    public List<SongDTO> getSongs() {
        return songs;
    }
}
