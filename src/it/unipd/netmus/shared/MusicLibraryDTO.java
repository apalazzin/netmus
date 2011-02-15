/**
 * 
 */
package it.unipd.netmus.shared;

import java.util.List;
/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class MusicLibraryDTO implements GenericDTO {
	private UserSummaryDTO owner;
	private List<SongSummaryDTO> songs;
	
	public MusicLibraryDTO(UserSummaryDTO user, List<SongSummaryDTO> s){
		owner = user;
		songs = s;
	}
	
	public UserSummaryDTO getOwner(){return owner;}
	public List <SongSummaryDTO> getSongs(){return songs;}
}
