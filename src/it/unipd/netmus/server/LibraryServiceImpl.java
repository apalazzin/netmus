package it.unipd.netmus.server;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Nome: LibraryServiceImpl.java Autore: VT.G Licenza: GNU GPL v3 Data
 * Creazione: 13 Febbraio 2011
 * 
 */

@SuppressWarnings("serial")
public class LibraryServiceImpl extends RemoteServiceServlet implements
        LibraryService {

    /**
     * Aggiunge una nuova playlist vuota al boolean catalogo dell’utente con il
     * nome dato in input. Ritorna true se l’operazione ha successo.
     */
    @Override
    public boolean addPlaylist(String user, String playlist_name) {

        UserAccount useraccount = UserAccount.load(user);

        return useraccount.getMusicLibrary().addPlaylist(playlist_name);

    }

    /**
     * Aggiunge la canzone con l’id dato in input in coda alla playlist specifi-
     * cata.Ritorna true se l’operazione ha successo.
     */
    @Override
    public boolean addSongToPlaylist(String user, String playlist_name, String title, String artist, String album) {
        UserAccount useraccount = UserAccount.load(user);
        return useraccount.getMusicLibrary().addSongToPlaylist(playlist_name, title, artist, album);
    }

    /**
     * Ritorna la lista ordinate delle canzoni in forma di SongSummaryDTO che
     * ap- partengono alla playlist.Ritorna true se l’operazione ha successo.
     */
    @Override
    public List<SongSummaryDTO> getPlaylist(String user, String name) {

        UserAccount useraccount = UserAccount.load(user);

        List<Song> songs = useraccount.getMusicLibrary().getPlaylistSongs(
                name);
        List<SongSummaryDTO> songs_dto = new ArrayList<SongSummaryDTO>();

        for (Song song : songs) {
            songs_dto.add(song.toSongSummaryDTO());
        }

        return songs_dto;
    }

    /**
     * Sposta la canzone all’indice dato in input nel primo attributo integer
     * al- l’indice specificato nel secondo, se questi indici sono validi
     * relativamente alla dimensione della playlist. Ritorna true se
     * l’operazione ha successo.
     */
    @Override
    public boolean moveSongInPlaylist(String user, String playlist_name,
            int from, int to) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Cancella la playlist il cui nome è dato e in input dal catalogo. Ritorna
     * true se l’operazione ha successo.
     */
    @Override
    public boolean removePlaylist(String user, String playlist_name) {

        UserAccount useraccount = UserAccount.load(user);

        return useraccount.getMusicLibrary().removePlaylist(playlist_name);
    }

    /**
     * Rimuove la canzone con l’id dato in input dalla playlist specificata.
     * Ritorna true se l’operazione ha successo.
     */
    @Override
    public boolean removeSongFromPlaylist(String user, String playlist_name,
            String title, String artist, String album) {

        UserAccount useraccount = UserAccount.load(user);
        return useraccount.getMusicLibrary().removeSongFromPlaylist(
                playlist_name, title, artist, album);
    }

    /**
     * Salva o aggiorna nel Datastore tutte le canzoni passate in input e se
     * possiedono sufficienti informazioni le inserisce nella libreria
     * dell’utente.
     * 
     * @return
     */
    @Override
    public void sendUserNewMusic(String user,
            List<SongDTO> new_songs) {

        // Invia al database tutte le canzoni della lista

        UserAccount useraccount = UserAccount.load(user);

        for (SongDTO songDTO : new_songs) {
            Song song = Song.storeOrUpdateFromDTO(songDTO);
            if (song != null) {
                useraccount.getMusicLibrary().addSong(song);
            }
        }
        
        useraccount.getMusicLibrary().update();
    }

    @Override
    public void storeStatistics(String user, String preferred_artist) {
        UserAccount user_account = UserAccount.load(user);
        
        user_account.getMusicLibrary().setPreferredArtist(preferred_artist);
    }

    @Override
    public List<String> getStatistics(String user) {
        UserAccount user_account = UserAccount.load(user);
        
        List<String> tmp = new ArrayList<String>();
        tmp.add(user_account.getMusicLibrary().getPreferredArtist());
        return tmp;
    }

}
