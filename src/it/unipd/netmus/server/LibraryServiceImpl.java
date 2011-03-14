package it.unipd.netmus.server;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.server.utils.GdataManager;
import it.unipd.netmus.server.utils.velocity.VelocityEngineManager;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Nome: LibraryServiceImpl.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
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
    public void storeStatistics(String user, String preferred_artist,
            String most_popular_song, String most_popular_song_for_this_user) {
        UserAccount user_account = UserAccount.load(user);
        
        if (!preferred_artist.equals("")) {
            user_account.getMusicLibrary().setPreferredArtist(preferred_artist);
        }
        
        if (!most_popular_song.equals("")) {
            user_account.getMusicLibrary().setMostPopularSong(most_popular_song);
        }
        
        if (!most_popular_song_for_this_user.equals("")) {
            user_account.getMusicLibrary().setMostPopularSongForThisUser(most_popular_song_for_this_user);
        }
     
    }

    @Override
    public String generatePDF(String input) {
        //generate the document
        VelocityEngineManager.init();
        VelocityContext context = new VelocityContext();
        context.put("name", input);
        Template t = VelocityEngineManager.getTemplate("mytemplate.vm");
        StringWriter writer = new StringWriter();
        try {
            t.merge(context, writer);
        } catch (Exception e) {
            e.printStackTrace();
            return "troubles merging your template " + e.getMessage();
        } 
        
        //create the doc on google docs
        String newDocId = "<error>";
        try {
            String title = "test doc for " + input;
            String content = writer.toString();
            newDocId = GdataManager.manager().createNewDocument(title, content).getResourceId();
        } catch (Exception e) {
            e.printStackTrace();
            return "error interfacing with Google Docs, see your logs";
        }

        
        //return the id
        return "pdf?id=" + newDocId;
    }
}
