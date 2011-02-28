/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.MusicLibrarySummaryDTO;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import java.util.ArrayList;
import java.util.List;

import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Parent;

/**
 * @author ValterTexasGroup
 *
 */
public class MusicLibrary {
	
	@Id @Parent private UserAccount owner;
	
	@Index private List<SongWithRating> song_list;
	
	
    //dati statistici salvati nel database
	
    private int num_songs;
    
    private String preferred_artist;
    
    private String preferred_genre;

    //gestione PLAYLISTS
    
    @Index private List<Playlist> playlists;

    
	public MusicLibrary() {
	    this.song_list = new ArrayList<SongWithRating>();
	    this.playlists = new ArrayList<Playlist>();
	    this.num_songs = 0;
	    this.preferred_artist = "";
	    this.preferred_genre = "";
	}
	
	public MusicLibrary(UserAccount owner) {
	    this.owner = owner;
	    this.song_list = new ArrayList<SongWithRating>();
	    this.playlists = new ArrayList<Playlist>();
	    this.num_songs = 0;
	    this.preferred_artist = "";
	    this.preferred_genre = "";
	}
	
	public MusicLibrarySummaryDTO toMusicLibrarySummaryDTO() {
	    List<SongSummaryDTO> list = new ArrayList<SongSummaryDTO>();
	    for (Song tmp:this.allSongs()) 
	        list.add(tmp.toSummaryDTO());
	    
	    List<String> playlists = this.getPlaylists();
	    return new MusicLibrarySummaryDTO(this.owner.toUserDTO(), list, playlists);
	}
	
	public MusicLibraryDTO toMusicLibraryDTO() {
	    List<SongDTO> list = new ArrayList<SongDTO>();
	    for (Song tmp:this.allSongs()) {
	        if (tmp != null) {
	            SongDTO tmp2 = tmp.toSongDTO();
	            tmp2.setRatingForThisUser(this.getSongRate(tmp));
	            list.add(tmp2);
	        }
	    }
	    
	    List<String> playlists = this.getPlaylists();
	    return new MusicLibraryDTO(this.owner.toUserDTO(), list, playlists);
	}
	
	public void store() {
	    ODF.get().store().instance(this).ensureUniqueKey().now();
	}
	
	public void update() {
	    ODF.get().storeOrUpdate(this);
	}
	
    public UserAccount getOwner() {
        return owner;
    }
    
    public int getNumSongs() {
        return num_songs;
    }

    private void setNumSongs(int num_songs) {
        this.num_songs = num_songs;
        this.update();
    }

    public String getPreferredArtist() {
        return preferred_artist;
    }

    private void setPreferredArtist(String preferred_artist) {
        this.preferred_artist = preferred_artist;
        this.update();
    }

    public String getPreferredGenre() {
        return preferred_genre;
    }

    private void setPreferredGenre(String preferred_genre) {
        this.preferred_genre = preferred_genre;
        this.update();
    }

    public boolean addSong(Song song, boolean update) {
        
        song.update();
        boolean trovato = false;
        for (SongWithRating tmp:this.song_list)
            if (tmp.getSongId().equals(song.getId()) == true) {
                trovato = true;
            }
        if (trovato == false) {
            //add songId to the list
            this.song_list.add(new SongWithRating(song.getId()));
            
            //update song's attributes
            song.newOwner();
            
            //increment the counter
            this.setNumSongs(num_songs+1);
            
            //update statistic fields
            if (update == true) {
                this.updatePreferredArtist();
                this.updatePreferredGenre();
            }
            return true;
        }
        else return false;
    }
        
    
    
    public boolean removeSong(Song song, boolean update) {
        
        song.update();
        boolean trovato = false;
        SongWithRating saved_song = null;
        for (SongWithRating tmp:this.song_list)
            if (tmp.getSongId().equals(song.getId()) == true) {
                trovato = true;
                saved_song = tmp;
            }
        if (trovato == true) {
            //remove songId to the list
            this.song_list.remove(saved_song);
            
            //update song's attributes and delete it form database if necessary
            song.deleteOwner();
            
            //remove song from playlists
            for (Playlist tmp:this.playlists) {
                tmp.removeSong(song.getId());
            }
            
            //increment the counter
            this.setNumSongs(num_songs-1);
            
            //update statistic fields
            if (update == true) {
                this.updatePreferredArtist();
                this.updatePreferredGenre();
            }
            
            return true;
        }
        else return false;
    }
     
    public void rateSong(Song song, int rating) {
        song.update();
        for (SongWithRating tmp:this.song_list)
            if (tmp.getSongId().equals(song.getId()) == true) {
                if (tmp.getRating()>0) {
                    int old_rating = tmp.getRating();
                    tmp.setRating(rating);
                    song.changeRate(old_rating, rating);
                }
                else {
                    tmp.setRating(rating);
                    song.addRate(rating);
                }
            }
    }
    
    public int getSongRate(Song song) {
        song.update();
        for (SongWithRating tmp:this.song_list)
            if (tmp.getSongId().equals(song.getId()) == true)
                return tmp.getRating();
        return -1;
    }
    
    private void updatePreferredGenre() {
        String topGenre = "";
        int max = 0;
        int count;
        List<Song> toBeRemoved = new ArrayList<Song>();
        List<Song> allSongs = this.allSongs();
        while (allSongs.size()>max) {
            Song tmp = allSongs.get(0);
            if (tmp.getGenre() != "") {
                count = 1;
                toBeRemoved.clear();
                for (Song it:allSongs)
                    if (it.getGenre() != "" && it.equals(tmp)==false && it.getGenre().equals(tmp.getGenre())==true ) {
                        count++;
                        toBeRemoved.add(it);
                    }
                if (count > max) {
                    max = count;
                    topGenre = tmp.getGenre();
                }
                allSongs.removeAll(toBeRemoved);
                allSongs.remove(tmp);
            }
            else allSongs.remove(tmp);
        }
        this.setPreferredGenre(topGenre);
    }
    
    private void updatePreferredArtist() {
        String topArtist = "";
        int max = 0;
        int count;
        List<Song> toBeRemoved = new ArrayList<Song>();
        List<Song> allSongs = this.allSongs();
        while (allSongs.size()>max) {
            Song tmp = allSongs.get(0);
            if (tmp.getArtist() != "") {
                count = 1;
                toBeRemoved.clear();
                for (Song it:allSongs)
                    if (it.getArtist() != "" && it.equals(tmp)==false && it.getArtist().equals(tmp.getArtist())==true ) {
                        count++;
                        toBeRemoved.add(it);
                    }
                if (count > max) {
                    max = count;
                    topArtist = tmp.getArtist();
                }
                allSongs.removeAll(toBeRemoved);
                allSongs.remove(tmp);
            }
            else allSongs.remove(tmp);
        }
        this.setPreferredArtist(topArtist);
    }
    
    public List<Song> allSongs() {
        ArrayList<Song> lista = new ArrayList<Song>();
        for (SongWithRating tmp:this.song_list) {
            lista.add(ODF.get().load().type(Song.class).id(tmp.getSongId()).now());
        }
        return lista;
    }

    static void deleteMusicLibrary(MusicLibrary ml) {
        ODF.get().storeOrUpdate(ml);
        for (SongWithRating tmp:ml.song_list)
            SongWithRating.deleteSongWithRating(tmp);
        for (Playlist tmp:ml.playlists)
            Playlist.deletePlaylist(tmp);
        ODF.get().delete(ml);
    }
    
    
    
    //Playlist's methods
    public boolean addPlaylist(String playlist_name) {
        if (this.getPlaylist(playlist_name) == null) {
            Playlist tmp = new Playlist(playlist_name);
            this.playlists.add(tmp);
            this.update();
            return true;
        }
        else return false;
    }
    
    public boolean removePlaylist(String playlist_name) {
        Playlist playlist = this.getPlaylist(playlist_name);
        if (playlist != null) {
            this.playlists.remove(playlist);
            Playlist.deletePlaylist(playlist);
            this.update();
            return true;
        }
        else return false;
    }

    private Playlist getPlaylist(String playlist_name) {
        for (int i=0; i<this.playlists.size(); i++) {
            Playlist tmp = this.playlists.get(i);
            if (tmp.getName().equalsIgnoreCase(playlist_name))
                return tmp;
        }
        return null;
    }
    
    public List<String> getPlaylists() {
        List<String> playlists = new ArrayList<String>();
        for (Playlist tmp:this.playlists)
            playlists.add(tmp.getName());
        return playlists;
    }

    
    
    //Playlist's songs methods
    public List<Song> getPlaylistSongs(String playlist_name) {
        for (Playlist tmp:this.playlists) {
            if (tmp.getName().equalsIgnoreCase(playlist_name)) {
                List<String> songNames = tmp.getSongs();
                List<Song> songs = new ArrayList<Song>();
                for (String tmp2:songNames) {
                    songs.add(Song.load(tmp2));
                }
                return songs;
            }
        }
        return null;
    }
    
    public List<String> getPlaylistSongNames(String playlist_name) {
        for (Playlist tmp : this.playlists) {
            if (tmp.getName().equalsIgnoreCase(playlist_name))
                return tmp.getSongs();
        }
        return null;
    }
    
    public boolean addSongToPlaylist(String playlist_name, String song_id) {
        Playlist tmp = this.getPlaylist(playlist_name);
        if (tmp != null) {
            boolean trovato = false;
            for (SongWithRating tmp2 : this.song_list)
                if (tmp2.getSongId().equals(song_id)) {
                    trovato = true;
                }
            if (trovato == true) {
                return tmp.addSong(song_id);
            }
            else return false;
        }
        else return false;
    }
    
    public boolean moveSongInPlaylist(String playlist_name, int from, int to) {
        Playlist tmp = this.getPlaylist(playlist_name);
        if (tmp != null) {
            return tmp.moveSong(from, to);
        }
        else return false;
    }
    
    public boolean removeSongFromPlaylist(String playlist_name, String song_id) {
        Playlist tmp = this.getPlaylist(playlist_name);
        if (tmp != null) {
            return tmp.removeSong(song_id);
        }
        else return false;
    }
    
    
    
    //---------------------------------------------------//
    //----------Classe per gestire PLAILISTS-------------//
    static private class Playlist {
        
        @Id private String name;
        
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
        
        void update() {
            ODF.get().storeOrUpdate(this);
        }
        
        String getName() {
            return name;
        }
        
        List<String> getSongs() {
            return this.songs_list;
        }
        
        boolean addSong(String song_id) {
            if (songs_list.add(song_id)) {
                this.update();
                return true;
            }
            else return false;
        }
        
        boolean removeSong(String song_id) {
            boolean tmp = songs_list.remove(song_id);
            this.update();
            return tmp;
        }
        
        boolean moveSong(int from, int to) {
            if (from >=0 && from<songs_list.size() && to>=0 && to<songs_list.size() && to != from) {
                String tmp = songs_list.remove(from);
                songs_list.add(to, tmp);
                this.update();
                return true;
            }
            else return false;
        }
        
        static void deletePlaylist(Playlist p) {
            ODF.get().storeOrUpdate(p);
            ODF.get().delete(p);
        }
    }

    
    
    //---------------------------------------------------//
    //----------classe per gestione RATINGS--------------//
    static private class SongWithRating {

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
        
        void update() {
            ODF.get().storeOrUpdate(this);
        }

        public String getSongId() {
            return song_id;
        }

        public void setRating(int rating) {
            this.rating = rating;
            this.update();
        }

        public int getRating() {
            return rating;
        }
        
        static void deleteSongWithRating(SongWithRating s) {
            ODF.get().storeOrUpdate(s);
            ODF.get().delete(s);
        }
    }
    //---------------------------------------------------
    
}
