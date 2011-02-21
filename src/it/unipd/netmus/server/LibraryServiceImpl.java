/**
 * 
 */
package it.unipd.netmus.server;

import java.util.List;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.MusicLibrarySummaryDTO;
import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LibraryServiceImpl extends RemoteServiceServlet implements LibraryService {
    
	public MusicLibraryDTO loadLibrary(String user,String password){
		return null;
	}
	
	public void addSong(SongDTO newTrack){}
	
	
    @Override
    public void sendUserNewMusic(String user, List<SongDTO> new_songs) {
        
        UserAccount useraccount = UserAccount.load(user);
        
        for (SongDTO songDTO : new_songs){            
            Song song = Song.storeOrUpdateFromDTO(songDTO);
            useraccount.getMusicLibrary().addSong(song, false);
        }
    }

    @Override
    public MusicLibrarySummaryDTO getLibrary(String user) {
      
        UserAccount useraccount = UserAccount.load(user);
        return useraccount.getMusicLibrary().toMusicLibrarySummaryDTO();
    }

}
