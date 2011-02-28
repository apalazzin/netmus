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

	private MusicLibraryDTO music_library;

    public UserCompleteDTO() {
        super();
    }
    
    public MusicLibraryDTO getMusicLibrary() {
        return music_library;
    }

    public void setMusicLibrary(MusicLibraryDTO music_library) {
        this.music_library = music_library;
    }
}
