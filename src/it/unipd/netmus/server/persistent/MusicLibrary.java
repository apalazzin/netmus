/**
 * 
 */
package it.unipd.netmus.server.persistent;

import it.unipd.netmus.server.PMF;
import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.SongSummaryDTO;
import it.unipd.netmus.shared.UserSummaryDTO;

import java.util.List;
import java.util.ArrayList;

import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author ValterTexasGroup
 *
 */
public class MusicLibrary {
	
	public MusicLibrary(UserAccount user){
		owner = user;
		songs = new ArrayList<Song>();
	}
	
	private UserAccount owner;
	private List<Song> songs;
	
	public UserAccount getOwner(){return owner;}
	
	public List<Song> getList(){return songs;}
	public void addSong(Song s){songs.add(s);}
	
	   // STATICO
	   public static MusicLibrary findLibrary(String owner) {
	      List<MusicLibrary> found = PMF.get().find().type(MusicLibrary.class).addFilter("owner", FilterOperator.EQUAL, owner).returnAll().now();
	      if(found.size()>0)
	    	  return found.get(0);
	      return null;
	   }
	
	
	public MusicLibraryDTO toDTO(){
		List<SongSummaryDTO> tmp = new ArrayList<SongSummaryDTO>();
		if(!songs.isEmpty()){
			for(int i=0;i<songs.size();i++){
				tmp.add(songs.get(i).toSummaryDTO());
			}
		}
		return new MusicLibraryDTO(owner.toUserSummaryDTO(), tmp);
	}
}
