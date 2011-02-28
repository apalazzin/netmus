/**
 * 
 */
package it.unipd.netmus.shared;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class UserCompleteDTO extends UserDTO {

	private MusicLibraryDTO musicLibrary;

    public UserCompleteDTO() {
        super();
    }
    
    public MusicLibraryDTO getMusicLibrary() {
        return musicLibrary;
    }

    public void setMusicLibrary(MusicLibraryDTO musicLibrary) {
        this.musicLibrary = musicLibrary;
    }
}
