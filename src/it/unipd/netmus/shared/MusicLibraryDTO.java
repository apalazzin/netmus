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
public class MusicLibraryDTO implements Serializable {
    
	private UserDTO owner;
	
	private List<SongDTO> songs;
	
	private List<String> playlists;
	
	public MusicLibraryDTO() {
        this.owner = new UserDTO();
        this.songs = new ArrayList<SongDTO>();
	}
	
	public MusicLibraryDTO(UserDTO user, List<SongDTO> songs, List<String> playlists){
		setOwner(user);
		setSongs(songs);
		setPlaylists(playlists);
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

    public void setPlaylists(List<String> playlists) {
        this.playlists = playlists;
    }

    public List<String> getPlaylists() {
        return playlists;
    }
    
    public void addPlaylist(String playlist_name) {
        playlists.add(playlist_name);
    }
    
    public void removePlaylist(String playlist_name) {
        if (playlists.contains(playlist_name)) {
            playlists.remove(playlist_name);
        }
    }
}
