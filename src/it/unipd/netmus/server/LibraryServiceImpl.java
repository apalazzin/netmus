package it.unipd.netmus.server;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.server.utils.GdataManager;
import it.unipd.netmus.server.utils.velocity.VelocityEngineManager;
import it.unipd.netmus.shared.SongDTO;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Nome: LibraryServiceImpl.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
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
            String title, String artist, String album) {
        UserAccount useraccount = UserAccount.load(user);
        return useraccount.getMusicLibrary().addSongToPlaylist(playlist_name,
                title, artist, album);
    }

    /**
     * Questo metodo si occupa dell'intera generazione del Google Doc
     * dell'utente dato in input. La creazione consiste nel creare il modello
     * HTML del documento generandone al momento i contenuti tramite le libreria
     * velocity e inserendoli poi in un modello standard in template.mv.
     * Successivamente viene invocato il metodo createNewDocument di
     * GdataManager per la generazione del Doc dal modello HTML. Ritorna
     * l'indirizzo al documento.
     */
    @Override
    public String generateDoc(String user) {
        // generate the document
        VelocityEngineManager.init();
        VelocityContext context = new VelocityContext();
        context.put("name", "My music library");
        // create the music list

        // catalog statistics
        int artists = 0;
        int albums = 0;
        int songs = 0;
        List<String> curr_artist = new ArrayList<String>();
        List<String> curr_album = new ArrayList<String>();

        UserAccount useraccount = UserAccount.load(user);
        String content;
        String content_tmp = "<table width=\"100%\">\n"
                + "  <tr style=\"background-color: #993366;\">\n"
                + "    <td><b>Title</b></td>\n"
                + "    <td><b>Artist</b></td>\n"
                + "    <td><b>Album</b></td>\n" + "  </tr>\n";

        List<Song> tmp_list = useraccount.getMusicLibrary().getAllSongs();
        Collections.sort(tmp_list, new Comparator<Song>() {
            @Override
            public int compare(Song arg0, Song arg1) {
                String artist_1 = arg0.getArtist();
                String artist_2 = arg1.getArtist();
                String album_1 = arg0.getAlbum();
                String album_2 = arg1.getAlbum();
                int compare_artists = artist_1.compareToIgnoreCase(artist_2);

                if (compare_artists == 0) {
                    return album_1.compareToIgnoreCase(album_2);
                } else {
                    return compare_artists;
                }
            }
        });

        String prec_artist = "prec_artist";
        String artist = "artist";
        for (Song tmp : tmp_list) {

            artist = tmp.getArtist();
            if (!artist.equalsIgnoreCase(prec_artist)) {

                prec_artist = artist;

                if (artist.equalsIgnoreCase("")) {
                    artist = "Unknown artist";
                }

                content_tmp += "  <tr style=\"background-color: #996666;\">\n"
                        + "    <td height=20><b>" + tmp.getArtist()
                        + "</b></td>\n" + "    <td height=20></td>\n"
                        + "    <td height=20></td>\n" + "  </tr>\n";

            }

            content_tmp += "  <tr>\n" + "    <td height=30>" + tmp.getTitle()
                    + "</td>\n" + "    <td height=30>" + artist + "</td>\n"
                    + "    <td height=30>" + tmp.getAlbum() + "</td>\n"
                    + "  </tr>\n";
            songs++;
            artists += check_list(curr_artist, tmp.getArtist());
            albums += check_list(curr_album, tmp.getAlbum());
        }

        content = "<table width=\"30%\">"
                + "  <tr><td style=\"background-color: #993366;\"><b>Songs</b></td><td>"
                + songs
                + "</td></tr>"
                + "  <tr><td style=\"background-color: #993366;\"><b>Artists</b></td><td>"
                + artists
                + "</td></tr>"
                + "  <tr><td style=\"background-color: #993366;\"><b>Albums</b></td><td>"
                + albums + "</td></tr>" + "</table>\n" + content_tmp;

        context.put("content", content);
        StringWriter writer = new StringWriter();
        Template list = VelocityEngineManager.getTemplate("list.mv");

        try {
            list.merge(context, writer);
        } catch (Exception e) {
            e.printStackTrace();
            return "troubles merging your template " + e.getMessage();
        }
        // create the doc on google docs
        String newDocId = "<error>";
        try {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(new Date());
            String title = "Library of " + user + " (" + date + ")";
            String body = writer.toString();
            newDocId = GdataManager.manager().createNewDocument(title, body)
                    .getDocId();
        } catch (Exception e) {
            throw (RuntimeException) e;
        }

        // return the id
        return "https://docs.google.com/document/d/" + newDocId
                + "/edit?hl=en#";
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

        UserAccount user_account = UserAccount.load(user);

        return user_account.getMusicLibrary().moveSongInPlaylist(playlist_name,
                from, to);
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
     */
    @Override
    public void sendUserNewMusic(String user, List<SongDTO> new_songs) {

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

    /**
     * Salva nel MusicLibrary persitente le statistiche relative al catalogo
     * calcolate nel client.
     */
    @Override
    public void storeStatistics(String user, String preferred_artist,
            String most_popular_song, String most_popular_song_for_this_user) {
        UserAccount user_account = UserAccount.load(user);

        if (!preferred_artist.equals("")) {
            user_account.getMusicLibrary().setPreferredArtist(preferred_artist);
        }

        if (!most_popular_song.equals("")) {
            user_account.getMusicLibrary()
                    .setMostPopularSong(most_popular_song);
        }

        if (!most_popular_song_for_this_user.equals("")) {
            user_account.getMusicLibrary().setMostPopularSongForThisUser(
                    most_popular_song_for_this_user);
        }

    }

    /**
     * Funzione che aggiunge una stringa ad una lista verificandone l'unicità e
     * ritorna 1 se ha succeso, viene utilizzata all'interno di GenerateDoc.
     */
    private int check_list(List<String> tested, String test) {
        int counter = 0;
        boolean cond = false;
        for (int i = 0; i < tested.size(); i++) {
            if (test.equals(tested.get(i))) {
                cond = true;
            }
        }
        if (cond == false) {
            tested.add(test);
            counter++;
        }
        return counter;
    }

}
