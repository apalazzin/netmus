/**
 * 
 */
package it.unipd.netmus.server;

import it.unipd.netmus.client.service.UsersService;
import it.unipd.netmus.shared.UserDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class UsersServiceImpl extends RemoteServiceServlet implements
      UsersService {
	public UserDTO loadProfile(String user,String password){
		return null;
	}

}
