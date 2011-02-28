/**
 * 
 */
package it.unipd.netmus.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import it.unipd.netmus.shared.SongDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

/**
 * @author ValterTexasGroup
 *
 */
public interface LibraryServiceAsync {
	
    public void sendUserNewMusic(String user, List<SongDTO> new_songs,AsyncCallback<Void> callback);

    void addPlaylist(String user, String playlist_name,
            AsyncCallback<Boolean> callback);

    void removePlaylist(String user, String playlist_name,
            AsyncCallback<Boolean> callback);
    
    void getPlaylist(String user, String playlist_name, AsyncCallback<List<SongSummaryDTO>> callback);

    void addSongToPlaylist(String user, String playlist_name, String song_id,
            AsyncCallback<Boolean> callback);

    void moveSongInPlaylist(String user, String playlist_name, int from,
            int to, AsyncCallback<Boolean> callback);

    void removeSongFromPlaylist(String user, String playlist_name,
            String song_id, AsyncCallback<Boolean> callback);

    void loadPreferredArtists(String user, AsyncCallback<List<String>> callback);

    void loadPreferredGenres(String user, AsyncCallback<List<String>> callback);

    void loadMostPopularSong(String user, AsyncCallback<String> callback);

}
