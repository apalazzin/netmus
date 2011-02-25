/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author ValterTexasGroup
 *
 */
@RemoteServiceRelativePath("songsService")
public interface SongService extends RemoteService {
    
    double rateSong(String user, SongSummaryDTO song, int rating);
    
    boolean editSong(String user, String artist, String title, String album);
    
    boolean deleteSong(String user, String artist, String title, String album);
}
