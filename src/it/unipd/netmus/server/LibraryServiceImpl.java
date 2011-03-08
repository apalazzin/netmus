package it.unipd.netmus.server;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.server.persistent.MusicLibrary;
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
    public boolean addSongToPlaylist(String user, String playlist_name,
            String song_id) {

        UserAccount useraccount = UserAccount.load(user);
        return useraccount.getMusicLibrary().addSongToPlaylist(playlist_name,
                song_id);
    }

    /**
     * Ritorna la lista ordinate delle canzoni in forma di SongSummaryDTO che
     * ap- partengono alla playlist.Ritorna true se l’operazione ha successo.
     */
    @Override
    public List<SongSummaryDTO> getPlaylist(String user, String titolo) {

        UserAccount useraccount = UserAccount.load(user);

        List<Song> songs = useraccount.getMusicLibrary().getPlaylistSongs(
                titolo);
        List<SongSummaryDTO> songs_dto = new ArrayList<SongSummaryDTO>();

        for (Song song : songs) {
            songs_dto.add(song.toSongSummaryDTO());
        }

        return songs_dto;
    }

    /**
     * Ritorna l’id della canzone condivisa dal maggior numero di utenti che fa
     * parte del catalogo.
     */
    @Override
    public String loadMostPopularSong(String user) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Calcola e ritorna l’artista più ricorrente all’interno delle canzoni del
     * catalogo dell’utente specificato.
     */
    @Override
    public List<String> loadPreferredArtists(String user) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Calcola e ritorna il genere più ricorrente all’interno delle canzoni del
     * catalogo dell’utente specificato.
     */
    @Override
    public List<String> loadPreferredGenres(String user) {
        // TODO Auto-generated method stub
        return null;
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
            String song_id) {

        UserAccount useraccount = UserAccount.load(user);
        return useraccount.getMusicLibrary().removeSongFromPlaylist(
                playlist_name, song_id);
    }

    /**
     * Salva o aggiorna nel Datastore tutte le canzoni passate in input e se
     * possiedono sufficienti informazioni le inserisce nella libreria
     * dell’utente.
     * 
     * @return
     */
    @Override
    public List<SongSummaryDTO> sendUserNewMusic(String user,
            List<SongDTO> new_songs) {

        // Invia al database tutte le canzoni della lista, ogni canzone ritorna
        // dopo le modifche
        // dovute alla gestione della persistenza.

        UserAccount useraccount = UserAccount.load(user);
        List<SongSummaryDTO> incomplete = new ArrayList<SongSummaryDTO>();

        for (SongDTO songDTO : new_songs) {
            Song song = Song.storeOrUpdateFromDTO(songDTO);
            if (song != null) {
                useraccount.getMusicLibrary().addSong(song);
                if (song.getAlbumCover().equals("")
                        || song.getYoutubeCode().equals("")) {
                    incomplete.add(song.toSongSummaryDTO());
                }
            }
        }

        return incomplete;
    }

    @Override
    public void updateStatisticFields(String user) {
        MusicLibrary library = UserAccount.load(user).getMusicLibrary();
        library.updatePreferredArtist();
        library.updatePreferredGenre();
    }
    
    @Override
    public void completeSongs(List<SongSummaryDTO> incomplete) {

        String cache_album = "no_album";
        String cache_album_cover = "no_album_cover";
        
        for (SongSummaryDTO song_dto : incomplete) {
            Song song = Song.loadFromDTO(song_dto);

            if (song != null) {
                if (song.getAlbum().equalsIgnoreCase(cache_album)) {
                    song.setAlbumCover(cache_album_cover);
                }
                song.completeSong();
                cache_album = song.getAlbum();
                if (!song.getAlbumCover().equals("no_album_cover")) {
                    if (song.getAlbumCover().equals("")) {
                        cache_album_cover = "no_album_cover";
                    }
                    else {
                        cache_album_cover = song.getAlbumCover();
                    }
                }
                else {
                    cache_album_cover = song.getAlbumCover();
                    song.setAlbumCover("");
                    song.update();
                }
            }
        }
        
        //update statistic fields
        
        
    }

}
