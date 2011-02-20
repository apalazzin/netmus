/**
 * 
 */
package it.unipd.netmus.server;

import java.util.List;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.server.persistent.DatastoreUtils;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LibraryServiceImpl extends RemoteServiceServlet implements
      LibraryService {
	public MusicLibraryDTO loadLibrary(String user,String password){
		return null;
	}
	public void addSong(SongDTO newTrack){}
	
	
    @Override
    public void sendUserNewMusic(String user, List<SongDTO> new_songs) {
        
        for (SongDTO song : new_songs){            
            Song.storeOrUpdateFromDTO(song);
        }
        
        List<Song> list = DatastoreUtils.getAllSongsInDatastore();

        for (Song s : list) {
            System.out.println(s.getArtist()+" || "+s.getTitle());
        }
        
    }

}
