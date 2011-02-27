/**
 * 
 */
package it.unipd.netmus.server;

import java.util.List;

import it.unipd.netmus.client.service.UserService;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.server.utils.BCrypt;
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
        
        try {
            UserAccount current_user = UserAccount.load(user);
            
            if (!new_info_user.getNewPassword().equals("")) {
                String passwordHash = BCrypt.hashpw(new_info_user.getNewPassword(), BCrypt.gensalt());
                current_user.setPassword(passwordHash);
            }
            
            current_user.setNickName(new_info_user.getNickName());
            current_user.setFirstName(new_info_user.getFirstName());
            current_user.setLastName(new_info_user.getLastName());
            current_user.setGender(new_info_user.getGender());
            current_user.setNationality(new_info_user.getNationality());
            current_user.setAboutMe(new_info_user.getAboutMe());
            return true;
            
        } catch (Exception e) {
            return false;
        }

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
