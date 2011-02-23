/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public interface SongsServiceAsync {

    void rateSong(String user, SongSummaryDTO song, int rating,
            AsyncCallback<MusicLibraryDTO> callback);
}
