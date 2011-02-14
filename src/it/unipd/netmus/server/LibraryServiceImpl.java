/**
 * 
 */
package it.unipd.netmus.server;

import it.unipd.netmus.client.service.LibraryService;
import it.unipd.netmus.shared.MusicLibraryDTO;
import it.unipd.netmus.shared.SongDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LibraryServiceImpl extends RemoteServiceServlet implements
      LibraryService {
	public MusicLibraryDTO loadLibrary(String user,String password){
		return null;
	}
	public void addSong(SongDTO newTrack){}

}
