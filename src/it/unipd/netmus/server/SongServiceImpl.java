package it.unipd.netmus.server;

import it.unipd.netmus.client.service.SongService;
import it.unipd.netmus.server.persistent.Album;
import it.unipd.netmus.server.persistent.MusicLibrary;
import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.server.utils.Utils;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

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
        return useraccount.getMusicLibrary().removeSong(artist, title, album);
        
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
    
    /**
     * Metodo chiamato quando si vogliono avere tutte le info di una certa canzone.
     * Cerco il video su youtube e lo metto nel DTO... ogni volta faccio una ricerca su youtube (per Copyright legati al Country)
     */
    @Override
    public synchronized SongDTO getSongDTO(SongSummaryDTO song_summary_dto) {
        
        Song song = Song.loadFromDTO(song_summary_dto);
        
        SongDTO song_dto = null;
        if (song != null) {
            song_dto = song.toSongDTO();
        }
        else {
            return new SongDTO();
        }

        String ip = getThreadLocalRequest().getRemoteAddr();
        if (ip.equals("127.0.0.1")) ip="";
        
        song_dto.setAlbumCover(Album.getAlbumCoverLastFm(song_dto.getAlbum(), song_dto.getArtist()));
        song_dto.setYoutubeCode(Utils.getYouTubeCode(song.getTitle() + " " + song.getArtist(),ip));
        
        return song_dto;
    }
    
    /**
     * Restiruisce il link alla copertina dell'album della canzone data in input
     */
    @Override
    public synchronized String getCoverImage(SongSummaryDTO song_summary_dto) {
 
        return Album.getAlbumCoverLastFm(song_summary_dto.getAlbum(), song_summary_dto.getArtist());

    }

}
