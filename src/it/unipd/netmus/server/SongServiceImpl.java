/**
 * 
 */
package it.unipd.netmus.server;

import it.unipd.netmus.client.service.SongService;
import it.unipd.netmus.server.persistent.MusicLibrary;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class SongServiceImpl extends RemoteServiceServlet implements
      SongService {

    @Override
    public double rateSong(String user, SongSummaryDTO song, int rating) {
        UserAccount userAccount = UserAccount.load(user);
        MusicLibrary library = userAccount.getMusicLibrary();
        library.rateSong(Song.loadFromDTO(song), rating);
        return Song.loadFromDTO(song).getRatingDouble();
        
        
    }

    @Override
    public boolean editSong(String user, String artist, String title,
            String album) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteSong(String user, String artist, String title,
            String album) {
        // TODO Auto-generated method stub
        return false;
    }

}
