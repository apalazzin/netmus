/**
 * 
 */
package it.unipd.netmus.server;

import java.util.List;

import it.unipd.netmus.client.service.UserService;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.UserDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements
      UserService {
    
    @Override
	public UserCompleteDTO loadProfile(String user){
		return UserAccount.load(user).toUserCompleteDTO();
	}

    @Override
    public boolean editProfile(String user, UserCompleteDTO new_info_user) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteProfile(String user) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<UserDTO> findSimilarUser(String user) {
        // TODO Auto-generated method stub
        return null;
    }

}
