/**
 * 
 */
package it.unipd.netmus.shared;

import it.unipd.netmus.server.persistent.Song;
import it.unipd.netmus.server.persistent.UserAccount;

import java.util.List;
/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class MusicLibraryDTO implements GenericDTO {
	private UserSummaryDTO owner;
	private List<SongSummaryDTO> songs;
	
	public MusicLibraryDTO(UserAccount user, List<Song> s){
		owner = user.toUserDTO();
		if(!s.isEmpty()){
			for(int i =0; i<s.size(); i++){
				songs.add(s.get(i).toSummaryDTO());
			}
		}
	}
	
	public UserSummaryDTO getOwner(){return owner;}
	public List <SongSummaryDTO> getSongs(){return songs;}
}
