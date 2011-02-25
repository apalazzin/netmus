/**
 * 
 */
package it.unipd.netmus.server;

import java.util.List;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LibraryServiceImpl extends RemoteServiceServlet implements LibraryService {
  
    @Override
    public void sendUserNewMusic(String user, List<SongDTO> new_songs) {
        
        //Invia al database tutte le canzonu della lista, ogni canzone ritorna dopo le modifche
        //dovute alla gestione della persistenza.
        
        UserAccount useraccount = UserAccount.load(user);
        
        for (SongDTO songDTO : new_songs) {            
            Song song = Song.storeOrUpdateFromDTO(songDTO);
            if (song.getAlbum() != "" || song.getArtist() != "" || song.getTitle() != "")
                useraccount.getMusicLibrary().addSong(song, false);
        }
    }

    @Override
    public boolean addPlaylist(String user, String playlist_name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removePlaylist(String user, String playlist_name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addSongToPlaylist(String user, String playlist_name,
            String song_id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean moveSongInPlaylist(String user, String playlist_name,
            int from, int to) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeSongFromPlaylist(String user, String playlist_name,
            String song_id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<String> loadPreferredArtists(String user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> loadPreferredGenres(String user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String loadMostPopularSong(String user) {
        // TODO Auto-generated method stub
        return null;
    }

}
