package it.unipd.netmus.server;

import java.util.ArrayList;
import java.util.List;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Nome: LibraryServiceImpl.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 13 Febbraio 2011
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
            if (song != null)
                useraccount.getMusicLibrary().addSong(song, false);
        }
    }

    @Override
    public boolean addPlaylist(String user, String playlist_name) {

        UserAccount useraccount = UserAccount.load(user);

        return useraccount.getMusicLibrary().addPlaylist(playlist_name);

    }

    @Override
    public boolean removePlaylist(String user, String playlist_name) {
        
        UserAccount useraccount = UserAccount.load(user);
        
        return useraccount.getMusicLibrary().removePlaylist(playlist_name);
    }
    
    @Override
    public List<SongSummaryDTO> getPlaylist(String user, String titolo) {
        
        UserAccount useraccount = UserAccount.load(user);
        
        List<Song> songs = useraccount.getMusicLibrary().getPlaylistSongs(titolo);
        List<SongSummaryDTO> songs_dto = new ArrayList<SongSummaryDTO>();
        
        for (Song song : songs) {
            songs_dto.add(song.toSummaryDTO());
        }
        
        return songs_dto;
    }

    @Override
    public boolean addSongToPlaylist(String user, String playlist_name,
            String song_id) {
        
        UserAccount useraccount = UserAccount.load(user);
        return useraccount.getMusicLibrary().addSongToPlaylist(playlist_name, song_id);
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
        
        UserAccount useraccount = UserAccount.load(user);
        return useraccount.getMusicLibrary().removeSongFromPlaylist(playlist_name, song_id);
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
