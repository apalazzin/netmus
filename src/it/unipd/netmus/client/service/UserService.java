/**
 * 
 */
package it.unipd.netmus.client.service;

import java.util.List;

import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.UserDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Nome: UserService.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 15 Febbraio 2011
*/
@RemoteServiceRelativePath("usersService")
public interface UserService extends RemoteService {
    
	public UserCompleteDTO loadProfile(String user);
	
	boolean editProfile(String user, UserCompleteDTO new_info_user);
	
	boolean deleteProfile(String user);
	
	List<UserDTO> findSimilarUser(String user);
	
}
