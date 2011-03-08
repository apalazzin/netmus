package it.unipd.netmus.server;

import it.unipd.netmus.client.service.SongService;
import it.unipd.netmus.server.persistent.MusicLibrary;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.appengine.api.capabilities.CapabilityServicePb.IsEnabledResponse.SummaryStatus;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Nome: SongServiceImpl.java 
 * Autore: VT.G Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 */

@SuppressWarnings("serial")
public class SongServiceImpl extends RemoteServiceServlet implements
        SongService {

    /**
     * Rimuove la canzone dalla libreria dell’utente. Le canzoni rimosse
     * rimangono nel Datastore poiché potrebbero contenere informazioni utili
     * per inserimenti futuri.
     */
    @Override
    public boolean deleteSong(String user, String artist, String title,
            String album) {

        UserAccount useraccount = UserAccount.load(user);
        String song_id = title + "-vt.g-" + artist + "-vt.g-" + album;
        song_id = song_id.toLowerCase();
        Song song = Song.load(song_id);

        return useraccount.getMusicLibrary().removeSong(song);
    }

    /**
     * Questo metodo permette di assegnare una nuova chiave primaria ad una
     * canzone variando almeno uno tra gli attributi title, artist e album
     * trasformandola di fatto in un altro brano nel Datastore. Sarà possibile
     * invocare questo metodo solamente su canzoni le cui informazioni che
     * formano la chiave primaria sono incomplete.
     */
    @Override
    public boolean editSong(String user, String artist, String title,
            String album) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Assegna la votazione compresa tra 1 e 5 alla canzone specificata dal
     * SongSummaryDTO. Dopo aver aggiornato i campi relativi di Song nel
     * Datastore ritorna il valore double che rappresenta la nuova media tra
     * tutte le votazione effettuate su quella canzone.
     */
    @Override
    public double rateSong(String user, SongSummaryDTO song, int rating) {
        UserAccount userAccount = UserAccount.load(user);
        MusicLibrary library = userAccount.getMusicLibrary();
        library.rateSong(Song.loadFromDTO(song), rating);
        return Song.loadFromDTO(song).getRatingDouble();
    }
    
    @Override
    public synchronized SongDTO getSongDTO(SongSummaryDTO song_summary_dto) {
        return Song.loadFromDTO(song_summary_dto).toSongDTO();
    }

}
