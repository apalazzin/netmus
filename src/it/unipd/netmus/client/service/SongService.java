/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.SongSummaryDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Nome: SongService.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 15 Febbraio 2011
*/
@RemoteServiceRelativePath("songsService")
public interface SongService extends RemoteService {
    
    double rateSong(String user, SongSummaryDTO song, int rating);
    
    boolean editSong(String user, String artist, String title, String album);
    
    boolean deleteSong(String user, String artist, String title, String album);
}
