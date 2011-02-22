/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.MusicLibrarySummaryDTO;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;
import it.unipd.netmus.shared.exception.DatastoreException;

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
	
	@Index private List<SongWithRating> songList;
	
	//---------------------------------------------------
    //gestione RATINGS
    static private class SongWithRating {

        private String songId;
        
        private int rating;
        
        @SuppressWarnings("unused")
        public SongWithRating() {
            this.songId = "";
            this.rating = -1;
        }
        
        public SongWithRating(String songId) {
            this.songId = songId;
            this.rating = -1;
            this.update();
        }
        
        void update() {
            ODF.get().storeOrUpdate(this);
        }

        public String getSongId() {
            return songId;
        }

        public void setRating(int rating) {
            this.rating = rating;
            this.update();
        }

        public int getRating() {
            return rating;
        }
    }
	//---------------------------------------------------
    //dati statistici salvati nel database
    private int numSongs;
    
    private String preferredArtist;
    
    private String preferredGenre;
    
    private int numPlaylists;
	//---------------------------------------------------
    //gestione PLAYLISTS
    @Index private List<Playlist> playlists;
    //---------------------------------------------------
    
	public MusicLibrary() {
	    this.songList = new ArrayList<SongWithRating>();
	    this.playlists = new ArrayList<Playlist>();
	    this.numPlaylists = 0;
	    this.numSongs = 0;
	    this.preferredArtist = "";
	    this.preferredGenre = "";
	}
	
	public MusicLibrary(UserAccount owner) {
	    this.owner = owner;
	    this.songList = new ArrayList<SongWithRating>();
	    this.playlists = new ArrayList<Playlist>();
	    this.numPlaylists = 0;
	    this.numSongs = 0;
	    this.preferredArtist = "";
	    this.preferredGenre = "";
	}
	
	public MusicLibrarySummaryDTO toMusicLibrarySummaryDTO() {
	    List<SongSummaryDTO> list = new ArrayList<SongSummaryDTO>();
	    for (Song tmp:this.allSongs()) 
	        list.add(tmp.toSummaryDTO());
	    return new MusicLibrarySummaryDTO(this.owner.toUserSummaryDTO(), list);
	}
	
	public MusicLibraryDTO toMusicLibraryDTO() {
	    List<SongDTO> list = new ArrayList<SongDTO>();
	    for (Song tmp:this.allSongs()) {
	        SongDTO tmp2 = tmp.toSongDTO();
	        tmp2.setRatingForThisUser(this.getSongRate(tmp));
	        list.add(tmp2);
	    }
	    return new MusicLibraryDTO(this.owner.toUserSummaryDTO(), list);
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
        return numSongs;
    }

    private void setNumSongs(int numSongs) {
        this.numSongs = numSongs;
        this.update();
    }

    public String getPreferredArtist() {
        return preferredArtist;
    }

    private void setPreferredArtist(String preferredArtist) {
        this.preferredArtist = preferredArtist;
        this.update();
    }

    public String getPreferredGenre() {
        return preferredGenre;
    }

    private void setPreferredGenre(String preferredGenre) {
        this.preferredGenre = preferredGenre;
        this.update();
    }

    public int getNumPlaylists() {
        return numPlaylists;
    }

    public boolean addSong(Song song, boolean update) {
        
        song.update();
        boolean trovato = false;
        for (SongWithRating tmp:this.songList)
            if (tmp.getSongId().equals(song.getId())) {
                trovato = true;
            }
        if (song.getTitle() != null && song.getArtist() != null && trovato == false) {
            //add songId to the list
            this.songList.add(new SongWithRating(song.getId()));
            
            //update song's attributes
            song.newOwner();
            
            //increment the counter
            this.setNumSongs(numSongs+1);
            
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
        for (SongWithRating tmp:this.songList)
            if (tmp.getSongId().equals(song.getId())) {
                trovato = true;
                saved_song = tmp;
            }
        if (trovato == true) {
            //remove songId to the list
            this.songList.remove(saved_song);
            
            //update song's attributes and delete it form database if necessary
            song.deleteOwner();
            
            //remove song from playlists
            for (Playlist tmp:this.playlists) {
                tmp.removeSong(song.getId());
            }
            
            //increment the counter
            this.setNumSongs(numSongs-1);
            
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
        for (SongWithRating tmp:this.songList)
            if (tmp.getSongId().equals(song.getId())) {
                tmp.setRating(rating);
                song.addRate(rating);
            }
    }
    
    public int getSongRate(Song song) {
        song.update();
        for (SongWithRating tmp:this.songList)
            if (tmp.getSongId().equals(song.getId()))
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
            if (tmp.getGenre() != null) {
                count = 1;
                toBeRemoved.clear();
                for (Song it:allSongs)
                    if (it.getGenre() != null && !it.equals(tmp) && it.getGenre().equals(tmp.getGenre())) {
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
            if (tmp.getArtist() != null) {
                count = 1;
                toBeRemoved.clear();
                for (Song it:allSongs)
                    if (it.getArtist() != null && !it.equals(tmp) && it.getArtist().equals(tmp.getArtist())) {
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
        for (SongWithRating tmp:this.songList) {
            lista.add(ODF.get().load().type(Song.class).id(tmp.getSongId()).now());
        }
        return lista;
    }

    static void deleteMusicLibrary(MusicLibrary ml) {
        ODF.get().storeOrUpdate(ml);
        ODF.get().delete(ml);
    }
    
    
    
    //Playlist's methods
    public void addPlaylist(String playlistName) throws DatastoreException {
        if (this.getPlaylist(playlistName) == null) {
            Playlist tmp = new Playlist(playlistName);
            this.playlists.add(tmp);
            this.numPlaylists++;
            this.update();
        }
        else throw new DatastoreException();
    }
    
    public void removePlaylist(String playlistName) throws DatastoreException {
        Playlist playlist = this.getPlaylist(playlistName);
        if (playlist != null) {
            this.playlists.remove(playlist);
            Playlist.deletePlaylist(playlist);
            this.numPlaylists--;
            this.update();
        }
        else throw new DatastoreException();
    }

    private Playlist getPlaylist(String playlistName) {
        for (int i=0; i<this.playlists.size(); i++) {
            Playlist tmp = this.playlists.get(i);
            if (tmp.getName().equalsIgnoreCase(playlistName))
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
    public List<Song> getPlaylistSongs(String playlistName) {
        for (Playlist tmp:this.playlists) {
            if (tmp.getName().equalsIgnoreCase(playlistName)) {
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
    
    public List<String> getPlaylistSongNames(String playlistName) {
        for (Playlist tmp:this.playlists) {
            if (tmp.getName().equalsIgnoreCase(playlistName))
                return tmp.getSongs();
        }
        return null;
    }
    
    public boolean addSongToPlaylist(String playlistName, String songId) {
        Playlist tmp = this.getPlaylist(playlistName);
        if (tmp != null) {
            boolean trovato = false;
            for (SongWithRating tmp2:this.songList)
                if (tmp2.getSongId().equals(songId)) {
                    trovato = true;
                }
            if (trovato == true) {
                return tmp.addSong(songId);
            }
            else return false;
        }
        else return false;
    }
    
    public boolean moveSongInPlaylist(String playlistName, int from, int to) {
        Playlist tmp = this.getPlaylist(playlistName);
        if (tmp != null) {
            return tmp.moveSong(from, to);
        }
        else return false;
    }
    
    public boolean removeSongFromPlaylist(String playlistName, String songId) {
        Playlist tmp = this.getPlaylist(playlistName);
        if (tmp != null) {
            return tmp.removeSong(songId);
        }
        else return false;
    }
    
    
    //NESTED CLASS: PLAYLIST, IT'S NECESSARY FOR IMPLEMENTATION WITH TWIG-PERIST  
    static private class Playlist {
        
        @Id private String name;
        
        private List<String> songsList;

        @SuppressWarnings("unused")
        Playlist() {
            this.songsList = new ArrayList<String>();
            this.name = "";
        }
        
        Playlist(String name) {
            this.name = name;
            this.songsList = new ArrayList<String>();
        }
        
        void update() {
            ODF.get().storeOrUpdate(this);
        }
        
        String getName() {
            return name;
        }
        
        List<String> getSongs() {
            return this.songsList;
        }
        
        boolean addSong(String songId) {
            if (songsList.indexOf(songId)<0) {
                songsList.add(songId);
                this.update();
                return true;
            }
            else return false;
        }
        
        boolean removeSong(String songId) {
            boolean tmp = songsList.remove(songId);
            this.update();
            return tmp;
        }
        
        boolean moveSong(int from, int to) {
            if (from >=0 && from<songsList.size() && to>=0 && to<songsList.size() && to != from) {
                String tmp = songsList.remove(from);
                songsList.add(to, tmp);
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

}
