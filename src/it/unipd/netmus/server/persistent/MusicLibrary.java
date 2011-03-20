package it.unipd.netmus.server.persistent;

import it.unipd.netmus.server.utils.cache.CacheSupport;
import it.unipd.netmus.server.utils.cache.Cacheable;
import it.unipd.netmus.shared.FieldVerifier;
import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.MusicLibraryDTO.PlaylistDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Parent;

/**
 * Nome: MusicLibrary.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 * 
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * MusicLibrary contiene tutte le informazioni relative ad un catalogo
 * multimediale, ovvero il proprietario e la lista delle canzoni. La funzione
 * principale però è quella di fornire dei servizi comodi ed efficenti per la
 * gestione del catalogostesso e le entità interne ad esso associate. La lista
 * di brani è mantenutacome lista di codici identificativi (strighe) per una
 * questione di efficenza con twig-persist.
 * 
 */


public class MusicLibrary implements Serializable, Cacheable {

    private static final long serialVersionUID = 6833302154286903936L;

    // ---------------------------------------------------//
    // ----------Classe per gestire PLAILISTS-------------//
    static private class Playlist implements Serializable, Cacheable {

        private static final long serialVersionUID = 8384098475154741660L;

        static void deletePlaylist(Playlist p) {
            ODF.get().storeOrUpdate(p);
            p.removeFromCache();
            ODF.get().delete(p);
        }
        
        static public Playlist load(String id) {
            Playlist playlist = (Playlist) CacheSupport.cacheGet(id);
            
            if (playlist == null) {
                playlist = ODF.get().load().type(Playlist.class).id(id).now();
                if (playlist != null) {
                    playlist.addToCache();
                }
            }
            
            return playlist;
        }

        @Id
        private String id;
        
        private String name;

        private List<String> songs_list;

        @SuppressWarnings("unused")
        Playlist() {
            this.songs_list = new ArrayList<String>();
            this.name = "";
        }

        Playlist(String name, String owner) {
            this.name = name;
            this.songs_list = new ArrayList<String>();
            this.id = FieldVerifier.generatePlaylistId(name, owner);
            this.update();
        }

        boolean addSong(String song_id) {
            if (songs_list.add(song_id)) {
                this.update();
                return true;
            } else
                return false;
        }
        
        String getId() {
            return id;
        }

        String getName() {
            return name;
        }

        List<String> getSongs() {
            return this.songs_list;
        }

        boolean moveSong(int from, int to) {
            if (from >= 0 && from < songs_list.size() && to >= 0
                    && to < songs_list.size() && to != from) {
                String tmp = songs_list.remove(from);
                songs_list.add(to, tmp);
                this.update();
                return true;
            } else
                return false;
        }

        boolean removeSong(String song_id) {
            boolean tmp = songs_list.remove(song_id);
            this.update();
            return tmp;
        }

        void update() {
            ODF.get().storeOrUpdate(this);
            this.addToCache();
        }

        @Override
        public void addToCache() {
            CacheSupport.cachePut(this.id, this);
        }

        @Override
        public void removeFromCache() {
            CacheSupport.cacheRemove(this.id);
        }

    }

    static void deleteMusicLibrary(MusicLibrary ml) {
        for (String tmp : ml.playlists) {
            Playlist playlist = Playlist.load(tmp);
            Playlist.deletePlaylist(playlist);
        }
        ml.removeFromCache();
        ODF.get().delete(ml);
    }

    @Id
    private String id;
    
    @Parent
    private UserAccount owner;
    
    private Map<String, String> song_list;

    @Index
    private String preferred_artist;

    @Index
    private String most_popular_song;
    
    @Index
    private String most_popular_song_for_this_user;
    
    private List<String> playlists;

    public MusicLibrary() {
        this.playlists = new ArrayList<String>();
        this.song_list = new HashMap<String, String>();
        this.preferred_artist = "";
        this.most_popular_song = "";
        this.most_popular_song_for_this_user = "";
        this.id = "";
    }

    public MusicLibrary(UserAccount owner) {
        this.owner = owner;
        this.playlists = new ArrayList<String>();
        this.song_list = new HashMap<String, String>();
        this.preferred_artist = "";
        this.most_popular_song = "";
        this.most_popular_song_for_this_user = "";
        this.id = owner.getUser()+"-library";
    }

    public boolean addPlaylist(String playlist_name) {
        if (this.getPlaylist(playlist_name) == null) {
            Playlist tmp = new Playlist(playlist_name, owner.getUser());
            this.playlists.add(tmp.getId());
            this.update();
            return true;
        } else
            return false;
    }

    /**
     * 
     * Associa una canzone data in input alla libreria aggiungendola in coda.
     * Ritorna true se l'inserimento ha avuto successo, false altrimenti.
     * 
     */
    public boolean addSong(Song song) {
        
        // find sond in the library
        if (!song_list.containsKey(song.getId())) {
            
            // add songId to the list
            this.song_list.put(song.getId(), "-1");

            // update song's attributes
            song.newOwner();
            
            return true;
            
        } else {
            return false;
        }
    }

    public boolean addSongToPlaylist(String playlist_name, String title, String artist, String album) {
        Playlist tmp = this.getPlaylist(playlist_name);
        String song_id = FieldVerifier.generateSongId(title, artist, album);
        
        if (tmp != null) {
            if (song_list.containsKey(song_id)) {
                return tmp.addSong(song_id);
            } else
                return false;
        } else
            return false;
    }

    public List<Song> getAllSongs() {
    
        ArrayList<Song> lista = new ArrayList<Song>();
        
        for (String tmp : song_list.keySet()) {
            Song song = ODF.get().load().type(Song.class).id(tmp).now();
            if (song != null) {
                lista.add(song);
            }
            
        }
        
        return lista;
    }

    public UserAccount getOwner() {
        return owner;
    }

    public List<String> getPlaylistsNames() {
        List <String> list_names = new ArrayList<String>();
        
        for (String id : this.playlists) {
            Playlist playlist = getPlaylistFromId(id);
            if (playlist != null) {
                list_names.add(playlist.getName());
            }
        }
        
        return list_names;
    }

    public List<Song> getPlaylistSongs(String playlist_name) {
        Playlist playlist = getPlaylist(playlist_name);
        if (playlist != null) {
            List<Song> songs = new ArrayList<Song>();
            for (String tmp2 : playlist.getSongs()) {
                songs.add(Song.load(tmp2));
            }
            return songs;
        }
        else {
            return null;
        }
    }

    public String getPreferredArtist() {
        return preferred_artist;
    }

    public int getSongRateForThisUser(Song song) {
        
        if (song_list.containsKey(song.getId())) {
            return Integer.parseInt(song_list.get(song.getId()));
        }
        else  {
            return -1;
        }
    }

    public boolean moveSongInPlaylist(String playlist_name, int from, int to) {
        Playlist tmp = this.getPlaylist(playlist_name);
        if (tmp != null) {
            return tmp.moveSong(from, to);
        } else
            return false;
    }

    /**
     * 
     * Assegna il voto alla canzone dati in input. Il voto è personale
     * dell'utente che possiede la libreria ed è unico, quindi sovrascrive la
     * votazione precendete. Oltre ad aggiornare il voto del'utente in
     * MusicLibrary questo metodo aggiorna anche la media totale dei voti in
     * Song.
     * 
     */
    public void rateSong(Song song, int rating) {
        String str_rating = String.valueOf(rating);

        if (song_list.containsKey(song.getId())) {
            int old_rating = Integer.parseInt(song_list.get(song.getId()));
            if (old_rating > 0) {
                song_list.put(song.getId(), str_rating);
                song.changeRate(old_rating, rating);
                this.update();
            } else {
                song_list.put(song.getId(), str_rating);
                song.addRate(rating);
                this.update();
            }
        }
    }

    public boolean removePlaylist(String playlist_name) {
        Playlist playlist = this.getPlaylist(playlist_name);
        if (playlist != null) {
            this.playlists.remove(playlist.getId());
            Playlist.deletePlaylist(playlist);
            this.update();
            return true;
        } else
            return false;
    }

    /**
     * 
     * Rimuove una canzone data in input dalla libreria, la canzone rimossa
     * rimane in database anche se non posseduta da alcun utente. Ritorna true
     * se la rimozione ha avuto successo, false altriementi.
     */
    public boolean removeSong(String artist, String title,
            String album) {

        String song_id = FieldVerifier.generateSongId(title, artist, album);
        
        if (song_list.containsKey(song_id)) {
            
            // remove songId to the list
            this.song_list.remove(song_id);
            
            // remove song from Cache
            CacheSupport.cacheRemove(song_id);

            // update song's attributes
            Song song = Song.load(song_id);
            song.deleteOwner();

            // remove song from playlists
            for (String tmp : this.playlists) {
                Playlist playlist = getPlaylistFromId(tmp); 
                playlist.removeSong(song_id);
            }
            
            // save changes to library
            this.update();
            
            return true;
        } 
        else {
            return false;
        }
    }

    public boolean removeSongFromPlaylist(String playlist_name, String title, String artist, String album) {
        Playlist tmp = this.getPlaylist(playlist_name);
        
        String song_id = FieldVerifier.generateSongId(title, artist, album);
        
        if (tmp != null) {
            return tmp.removeSong(song_id);
        } else
            return false;
    }

    public MusicLibraryDTO toMusicLibrarySummaryDTO() {
        Map<String, SongSummaryDTO> map = new HashMap<String, SongSummaryDTO>();

        for (String tmp : song_list.keySet()) {
            
            if (tmp != null && !tmp.equals("")) {
                Song song = ODF.get().load().type(Song.class).id(tmp).now();
                
                if (song != null) {
                    SongSummaryDTO song_dto = song.toSongSummaryDTO();
                    song_dto.setRatingForThisUser(Integer.parseInt(song_list.get(tmp)));
                    map.put(tmp, song_dto);
           
                }
            }
            
        }

        List<PlaylistDTO> playlists = new ArrayList<PlaylistDTO>();
        for (String tmp : this.playlists) {
            Playlist p = getPlaylistFromId(tmp);
            playlists.add(new PlaylistDTO(p.getName(),p.getSongs()));
        }
        
        MusicLibraryDTO library = new MusicLibraryDTO(map, playlists);
        
        library.setPreferred_artist(getPreferredArtist());
        library.setMostPopularSong(this.most_popular_song);
        library.setMostPopularSongForThisUser(this.most_popular_song_for_this_user);
        
        return library;
    }

    public void update() {
        ODF.get().storeOrUpdate(this);
        this.addToCache();
    }

    private Playlist getPlaylist(String playlist_name) {
        return Playlist.load(FieldVerifier.generatePlaylistId(playlist_name, this.owner.getUser()));
    }
    
    private Playlist getPlaylistFromId(String playlist_id) {
        return Playlist.load(playlist_id);
    }

    public void setPreferredArtist(String preferred_artist) {
        this.preferred_artist = preferred_artist;
        this.update();
    }

    @Override
    public void addToCache() {
        CacheSupport.cachePut(this.id, this);
    }

    @Override
    public void removeFromCache() {
        CacheSupport.cacheRemove(this.id);
    }

    public void setMostPopularSong(String most_popular_song) {
        this.most_popular_song = most_popular_song;
        this.update();
    }

    public String getMostPopularSong() {
        return most_popular_song;
    }

    public void setMostPopularSongForThisUser(
            String most_popular_song_for_this_user) {
        this.most_popular_song_for_this_user = most_popular_song_for_this_user;
        this.update();
    }

    public String getMostPopularSongForThisUser() {
        return most_popular_song_for_this_user;
    }

}
