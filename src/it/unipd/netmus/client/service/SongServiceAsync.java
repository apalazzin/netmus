/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public interface SongServiceAsync {

    void rateSong(String user, SongSummaryDTO song, int rating,
            AsyncCallback<Double> callback);

    void editSong(String user, String artist, String title, String album,
            AsyncCallback<Boolean> callback);

    void deleteSong(String user, String artist, String title, String album,
            AsyncCallback<Boolean> callback);
}
