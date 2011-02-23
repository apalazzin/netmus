/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.UserDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author ValterTexasGroup
 *
 */
@RemoteServiceRelativePath("usersService")
public interface UsersService extends RemoteService {
    
	public UserCompleteDTO loadProfile(String user);
	
}
