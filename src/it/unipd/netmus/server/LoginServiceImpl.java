/**
 * 
 */
package it.unipd.netmus.server;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserSummaryDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
      LoginService {
	
	@Override
	public boolean insertRegistration(LoginDTO login) {
		
		UserAccount userAccount = UserAccount.findUser(login.getUser());
		if (userAccount == null) {
			userAccount = new UserAccount(login.getUser(),login.getPassword());
			PMF.get().store(userAccount);
			return true;
		}
		else return false;
	}

	@Override
	public boolean verifyLogin(LoginDTO login) {
		UserAccount userAccount = UserAccount.findUser(login.getUser());

		if (userAccount == null) {return false;}
		else {
			if (login.getPassword() == userAccount.getPassword()) {
				return true;
			}
			else return false;
		}
	}

	@Override
	public boolean startLogin(LoginDTO login) {
		if (verifyLogin(login)) {
			UserAccount userAccount = UserAccount.findUser(login.getUser());
			HttpSession session = getThreadLocalRequest().getSession();
			LoginHelper.loginStarts(session, userAccount);
			return true;
		}
		return false;
	}

	@Override
	public UserSummaryDTO getLoggedInUserDTO() {
	    UserSummaryDTO userDTO;
	    HttpSession session = getThreadLocalRequest().getSession();

	    UserAccount u = LoginHelper.getLoggedInUser(session);
	    if (u == null)
	      return null;
	    userDTO = u.toUserSummaryDTO();
	    return userDTO;
	}

	@Override
	public void logout() {
	    getThreadLocalRequest().getSession().invalidate();
	}

	@Override
	public ArrayList<UserSummaryDTO> getAllUsers() {
		ArrayList<UserAccount> allUsers = (ArrayList<UserAccount>) PMF.get().find().type(UserAccount.class).returnAll().now();
		ArrayList<UserSummaryDTO> allUsersDTO = new ArrayList<UserSummaryDTO>();
		for (UserAccount tmp:allUsers)
			allUsersDTO.add(tmp.toUserSummaryDTO());
		return allUsersDTO;
	}

}
