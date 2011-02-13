/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.shared.MusicLibraryDTO;

import java.util.List;

/**
 * @author ValterTexasGroup
 *
 */
public class MusicLibrary {
	
	public MusicLibrary(UserAccount user){
		owner = user;
	}
	
	private UserAccount owner;
	private List<Song> songs;
	
	public UserAccount getOwner(){return owner;}
	
	public List<Song> getList(){return songs;}
	public void addSong(Song s){songs.add(s);}
	
	public MusicLibraryDTO toDTO(){
		return new MusicLibraryDTO(owner, songs);
	}
}
