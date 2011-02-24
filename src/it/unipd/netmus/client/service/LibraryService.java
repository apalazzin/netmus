/**
 * 
 */
package it.unipd.netmus.client.service;

import java.util.List;

import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author ValterTexasGroup
 *
 */
@RemoteServiceRelativePath("libraryService")
public interface LibraryService extends RemoteService {
	public MusicLibraryDTO loadLibrary(String user,String password);
	public void addSong(SongDTO newTrack);
	
	public void sendUserNewMusic(String user, List<SongDTO> new_songs);
    MusicLibraryDTO getLibrary(String user);
}
