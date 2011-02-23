/**
 * 
 */
package it.unipd.netmus.server;

import it.unipd.netmus.client.service.UsersService;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.UserDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class UsersServiceImpl extends RemoteServiceServlet implements
      UsersService {
    
    @Override
	public UserCompleteDTO loadProfile(String user){
		return UserAccount.load(user).toUserCompleteDTO();
	}

}
