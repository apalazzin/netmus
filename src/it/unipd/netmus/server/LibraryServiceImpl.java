/**
 * 
 */
package it.unipd.netmus.server;

import java.util.List;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.exception.SongAlbumMissingException;
import it.unipd.netmus.shared.exception.SongTitleMissingException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LibraryServiceImpl extends RemoteServiceServlet implements LibraryService {
    
	public MusicLibraryDTO loadLibrary(String user, String password){
		return null;
	}
	
	public void addSong(SongDTO newTrack){}
	
	
    @Override
    public void sendUserNewMusic(String user, List<SongDTO> new_songs) {
        // dovremo tornare all'applet con la lista dei Song (o SummaryDTO per fargli scelgiere)
        
        UserAccount useraccount = UserAccount.load(user);
        for (SongDTO songDTO : new_songs){            
            try {
                Song song = Song.storeOrUpdateFromDTO(songDTO);
                if (song != null)
                    useraccount.getMusicLibrary().addSong(song, false);
            } catch (SongAlbumMissingException e) {
                Song song = Song.load(e.getSong_id());
                if (song != null)
                    useraccount.getMusicLibrary().addSong(song, false);
            } catch (SongTitleMissingException e1) {
                Song song = Song.load(e1.getSong_id());
                if (song != null)
                    useraccount.getMusicLibrary().addSong(song, false);
            }
        }
    }

    @Override
    public MusicLibraryDTO getLibrary(String user) {
      
        UserAccount useraccount = UserAccount.load(user);
        
        /*System.out.println(DatastoreUtils.songsInDatastore()+" canzoni totali salvate nel datastore");
        for (Song tmp:useraccount.getMusicLibrary().allSongs()) {
            System.out.println(tmp.getId());
            System.out.println(tmp.getTitle()+" "+tmp.getArtist()+" "+tmp.getAlbum());
            System.out.println("");
        }*/
        
        return useraccount.getMusicLibrary().toMusicLibraryDTO();
    }

}
