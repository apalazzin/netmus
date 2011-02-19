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
	
	@Index private List<String> songList;
	
    //dati statistici salvati nel database
    private int numSongs;
    
    private String preferredArtist;
    
    private String preferredGenre;
	//---------------------------------------------------
    
	public MusicLibrary() {
	    songList = new ArrayList<String>();
	}
	
	public MusicLibrary(UserAccount owner) {
	    this.owner = owner;
	    songList = new ArrayList<String>();
	}
	
	public MusicLibrarySummaryDTO toMusicLibrarySummaryDTO() {
	    List<SongSummaryDTO> list = new ArrayList<SongSummaryDTO>();
	    for (Song tmp:this.allSongs()) {
	        list.add(tmp.toSummaryDTO());
	    }
	    return new MusicLibrarySummaryDTO(this.owner.toUserSummaryDTO(), list);
	}
	
	public MusicLibraryDTO toMusicLibraryDTO() {
	    List<SongDTO> list = new ArrayList<SongDTO>();
	    for (Song tmp:this.allSongs()) {
	        list.add(tmp.toSongDTO());
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
    
    public boolean addSong(Song song, boolean update) {
        
        song.update();
        if (!songList.isEmpty() && songList.get(0) == null) {
            for (String tmp:songList) {
                ODF.get().refresh(tmp);
            }
        }
        if (this.songList.indexOf(song.getId())<0) {
            //add songId to the list
            this.songList.add(song.getId());
            
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
        if (!songList.isEmpty() && songList.get(0) == null) {
            for (String tmp:songList) {
                ODF.get().refresh(tmp);
            }
        }
        if (this.songList.indexOf(song.getId())>=0) {
            //add songId to the list
            this.songList.remove(song.getId());
            
            //update song's attributes and delete it form database if necessary
            song.deleteOwner();
            if (song.getNumOwners() == 0)
                Song.deleteSong(song);
            
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
        
        if (!songList.isEmpty() && songList.get(0) == null) {
            for (String tmp:songList) {
                ODF.get().refresh(tmp);
            }
        }
        ArrayList<Song> lista = new ArrayList<Song>();
        for (String tmp:this.songList) {
            lista.add(ODF.get().load().type(Song.class).id(tmp).now());
        }
        return lista;
    }


}
