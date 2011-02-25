/**
 * 
 */
package it.unipd.netmus.client.service;

import java.util.List;
import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author ValterTexasGroup
 *
 */
@RemoteServiceRelativePath("libraryService")
public interface LibraryService extends RemoteService {
	
	public void sendUserNewMusic(String user, List<SongDTO> new_songs);
	
	
	//---------GESTIONE PLAYLISTS-------------//
	
	public boolean addPlaylist(String user, String playlist_name);
	
	public boolean removePlaylist(String user, String playlist_name);
	
	public boolean addSongToPlaylist(String user, String playlist_name, String song_id);
	
	public boolean moveSongInPlaylist(String user, String playlist_name, int from, int to); 
	
	public boolean removeSongFromPlaylist(String user, String playlist_name, String song_id);
	
	
	//---------GESTION STATISTICHE------------//
	
	public List<String> loadPreferredArtists(String user);
	
	public List<String> loadPreferredGenres(String user);
	
	public String loadMostPopularSong(String user);
	

}
