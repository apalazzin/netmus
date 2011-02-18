/**
 * 
 */
package it.unipd.netmus.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.SongDTO;

/**
 * @author ValterTexasGroup
 *
 */
public interface LibraryServiceAsync {
	public void loadLibrary(String user,String password, AsyncCallback<MusicLibraryDTO> callback);
	public void addSong(SongDTO newTrack,AsyncCallback<Void> callback);
}
