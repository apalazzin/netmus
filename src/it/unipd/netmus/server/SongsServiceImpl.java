/**
 * 
 */
package it.unipd.netmus.server;

import it.unipd.netmus.client.service.SongsService;
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
public class SongsServiceImpl extends RemoteServiceServlet implements
      SongsService {

    @Override
    public double rateSong(String user, SongSummaryDTO song, int rating) {
        UserAccount userAccount = UserAccount.load(user);
        MusicLibrary library = userAccount.getMusicLibrary();
        library.rateSong(Song.loadFromDTO(song), rating);
        System.out.println("Rating della canzone: "+Song.loadFromDTO(song).getRatingDouble());
        System.out.println("Rating dato da questo utente alla canzone: "+library.getSongRate(Song.loadFromDTO(song)));
        return Song.loadFromDTO(song).getRatingDouble();
    }

}
