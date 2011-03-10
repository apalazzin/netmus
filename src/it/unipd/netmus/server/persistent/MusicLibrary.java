package it.unipd.netmus.server.persistent;

import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.MusicLibrarySummaryDTO;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

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

public class MusicLibrary {

    // ---------------------------------------------------//
    // ----------Classe per gestire PLAILISTS-------------//
    static private class Playlist {

        static void deletePlaylist(Playlist p) {
            ODF.get().storeOrUpdate(p);
            ODF.get().delete(p);
        }

        @Id
        private String name;

        private List<String> songs_list;

        @SuppressWarnings("unused")
        Playlist() {
            this.songs_list = new ArrayList<String>();
            this.name = "";
        }

        Playlist(String name) {
            this.name = name;
            this.songs_list = new ArrayList<String>();
        }

        boolean addSong(String song_id) {
            if (songs_list.add(song_id)) {
                this.update();
                return true;
            } else
                return false;
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
        }
    }

    // ---------------------------------------------------//
    // ----------classe per gestione RATINGS--------------//
    /*
    static private class SongWithRating {

        static void deleteSongWithRating(SongWithRating s) {
            ODF.get().storeOrUpdate(s);
            ODF.get().delete(s);
        }

        private String song_id;

        private int rating;

        @SuppressWarnings("unused")
        public SongWithRating() {
            this.song_id = Song.SEPARATOR;
            this.rating = -1;
        }

        public SongWithRating(String song_id) {
            this.song_id = song_id;
            this.rating = -1;
            this.update();
        }

        public int getRating() {
            return rating;
        }

        public String getSongId() {
            return song_id;
        }

        public void setRating(int rating) {
            this.rating = rating;
            this.update();
        }

        void update() {
            ODF.get().storeOrUpdate(this);
        }
    }
    // ---------------------------------------------------
    */

    static void deleteMusicLibrary(MusicLibrary ml) {
        ODF.get().storeOrUpdate(ml);
        //for (SongWithRating tmp : ml.song_list)
        //    SongWithRating.deleteSongWithRating(tmp);
        for (Playlist tmp : ml.playlists)
            Playlist.deletePlaylist(tmp);
        ODF.get().delete(ml);
    }

    @Id
    @Parent
    private UserAccount owner;
    
    @Index
    private Map<String, String> song_list;

    @Index
    private String preferred_artist;

    @Index
    private String preferred_genre;
    
    @Index
    private List<Playlist> playlists;

    public MusicLibrary() {
        this.playlists = new ArrayList<Playlist>();
        this.song_list = new HashMap<String, String>();
        this.preferred_artist = "";
        this.preferred_genre = "";
    }

    public MusicLibrary(UserAccount owner) {
        this.owner = owner;
        this.playlists = new ArrayList<Playlist>();
        this.song_list = new HashMap<String, String>();
        this.preferred_artist = "";
        this.preferred_genre = "";
    }

    public boolean addPlaylist(String playlist_name) {
        if (this.getPlaylist(playlist_name) == null) {
            Playlist tmp = new Playlist(playlist_name);
            this.playlists.add(tmp);
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

            this.update();
            
            return true;
            
        } else {
            return false;
        }
    }

    public boolean addSongToPlaylist(String playlist_name, String song_id) {
        Playlist tmp = this.getPlaylist(playlist_name);
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

    public List<String> getPlaylists() {
        List<String> playlists = new ArrayList<String>();
        for (Playlist tmp : this.playlists)
            playlists.add(tmp.getName());
        return playlists;
    }

    public List<String> getPlaylistSongNames(String playlist_name) {
        for (Playlist tmp : this.playlists) {
            if (tmp.getName().equalsIgnoreCase(playlist_name))
                return tmp.getSongs();
        }
        return null;
    }

    public List<Song> getPlaylistSongs(String playlist_name) {
        for (Playlist tmp : this.playlists) {
            if (tmp.getName().equalsIgnoreCase(playlist_name)) {
                List<String> songNames = tmp.getSongs();
                List<Song> songs = new ArrayList<Song>();
                for (String tmp2 : songNames) {
                    songs.add(Song.load(tmp2));
                }
                return songs;
            }
        }
        return null;
    }

    public String getPreferredArtist() {
        return preferred_artist;
    }

    public String getPreferredGenre() {
        return preferred_genre;
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
            } else {
                song_list.put(song.getId(), str_rating);
                song.addRate(rating);
            }
        }
    }

    public boolean removePlaylist(String playlist_name) {
        Playlist playlist = this.getPlaylist(playlist_name);
        if (playlist != null) {
            this.playlists.remove(playlist);
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

        String song_id = (title + Song.SEPARATOR + artist + Song.SEPARATOR + album).toLowerCase();
        
        if (song_list.containsKey(song_id)) {
            
            // remove songId to the list
            this.song_list.remove(song_id);

            // update song's attributes and delete it form database if necessary
            Song song = Song.load(song_id);
            song.deleteOwner();

            // remove song from playlists
            for (Playlist tmp : this.playlists) {
                tmp.removeSong(song_id);
            }
            return true;
        } 
        else {
            return false;
        }
    }

    public boolean removeSongFromPlaylist(String playlist_name, String song_id) {
        Playlist tmp = this.getPlaylist(playlist_name);
        if (tmp != null) {
            return tmp.removeSong(song_id);
        } else
            return false;
    }

    public MusicLibraryDTO toMusicLibraryDTO() {
        List<SongDTO> list = new ArrayList<SongDTO>();
        
        for (String tmp : song_list.keySet()) {
            
            Song song = ODF.get().load().type(Song.class).id(tmp).now();
            
            if (song != null) {
                SongDTO song_dto = song.toSongDTO();
                song_dto.setRatingForThisUser(Integer.parseInt(song_list.get(tmp)));
                list.add(song_dto);
                
            }
            
        }

        List<String> playlists = this.getPlaylists();
        
        MusicLibraryDTO library = new MusicLibraryDTO(list, playlists);
        
        library.setPreferred_artist(getPreferredArtist());
        
        library.setPreferred_genre(getPreferredGenre());
        
        return library;
    }

    public MusicLibrarySummaryDTO toMusicLibrarySummaryDTO() {
        List<SongSummaryDTO> list = new ArrayList<SongSummaryDTO>();

        for (String tmp : song_list.keySet()) {
            
            if (tmp != null && !tmp.equals("")) {
                Song song = ODF.get().load().type(Song.class).id(tmp).now();
                
                if (song != null) {
                    SongSummaryDTO song_dto = song.toSongSummaryDTO();
                    song_dto.setRatingForThisUser(Integer.parseInt(song_list.get(tmp)));
                    list.add(song_dto);
                    
                }
            }
            
        }

        List<String> playlists = this.getPlaylists();
        
        MusicLibrarySummaryDTO library = new MusicLibrarySummaryDTO(list, playlists);
        
        library.setPreferred_artist(getPreferredArtist());
        
        library.setPreferred_genre(getPreferredGenre());
        
        return library;
    }

    public void update() {
        ODF.get().storeOrUpdate(this);
    }

    private Playlist getPlaylist(String playlist_name) {
        for (int i = 0; i < this.playlists.size(); i++) {
            Playlist tmp = this.playlists.get(i);
            if (tmp.getName().equalsIgnoreCase(playlist_name))
                return tmp;
        }
        return null;
    }

    private void setPreferredArtist(String preferred_artist) {
        this.preferred_artist = preferred_artist;
        this.update();
    }

    private void setPreferredGenre(String preferred_genre) {
        this.preferred_genre = preferred_genre;
        this.update();
    }

    /**
     * 
     * Calcola e salva il nome del artista più ricorrente nella libreria. Ha
     * visibilità privata poichè deve essere utilizzato solo quando necessario
     * all'interno del metodo addSong(). --DA TESTARE--
     * 
     */
    public void updatePreferredArtist() {
        
        String topArtist = "";
        int max = 0;
        int count;
        List<Song> toBeRemoved = new ArrayList<Song>();
        List<Song> allSongs = this.getAllSongs();
        
        while (allSongs.size() > max) {
            Song tmp = allSongs.get(0);
            if (!tmp.getArtist().equals("")) {
                count = 1;
                toBeRemoved.clear();
                for (Song it : allSongs)
                    if (!it.getArtist().equals("") && !it.equals(tmp)
                            && it.getArtist().equals(tmp.getArtist())) {
                        count++;
                        toBeRemoved.add(it);
                    }
                if (count > max) {
                    max = count;
                    topArtist = tmp.getArtist();
                }
                allSongs.removeAll(toBeRemoved);
                allSongs.remove(tmp);
            } else
                allSongs.remove(tmp);
        }
        this.setPreferredArtist(topArtist);
    }

    /**
     * 
     * Calcola e salva il nome del genere musicale più ricorrente nella
     * libreria. Ha visibilità privata poichè deve essere utilizzato solo quando
     * necessario all'interno del metodo addSong. --DA TESTARE--
     */
    public void updatePreferredGenre() {
        
        String topGenre = "";
        int max = 0;
        int count;
        List<Song> toBeRemoved = new ArrayList<Song>();
        List<Song> allSongs = this.getAllSongs();
        while (allSongs.size() > max) {
            Song tmp = allSongs.get(0);
            if (!tmp.getGenre().equals("")) {
                count = 1;
                toBeRemoved.clear();
                for (Song it : allSongs)
                    if (!it.getGenre().equals("") && !it.equals(tmp)
                            && it.getGenre().equals(tmp.getGenre())) {
                        count++;
                        toBeRemoved.add(it);
                    }
                if (count > max) {
                    max = count;
                    topGenre = tmp.getGenre();
                }
                allSongs.removeAll(toBeRemoved);
                allSongs.remove(tmp);
            } else
                allSongs.remove(tmp);
        }
        this.setPreferredGenre(topGenre);
        
    }

}
