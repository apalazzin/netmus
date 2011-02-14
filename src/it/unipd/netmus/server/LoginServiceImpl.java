/**
 * 
 */
package it.unipd.netmus.server;

import java.util.ArrayList;
import java.util.Iterator;

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
		
		//find user in the database
		UserAccount userAccount = UserAccount.findUser(login.getUser());
		
		if (userAccount == null) {
			//create new user in the database
			userAccount = new UserAccount(login.getUser(),login.getPassword());
			PMF.get().store(userAccount);
			return true;
		}
		else return false;
	}

	
	@Override
	public boolean verifyLogin(LoginDTO login) {
		
		//find user in the database
		UserAccount userAccount = UserAccount.findUser(login.getUser());

		if (userAccount == null) {
			//user not found in the database
			return false;
		}
		else {
			if (login.getPassword() == userAccount.getPassword()) {
				//correct password
				return true;
			}
			//incorrect password
			else return false;
		}
	}

	@Override
	public boolean startLogin(LoginDTO login) {
	/*	if (verifyLogin(login)) {
			UserAccount userAccount = UserAccount.findUser(login.getUser());
			HttpSession session = getThreadLocalRequest().getSession();
			LoginHelper.loginStarts(session, userAccount);
			return true;
		}
		return false;*/
		return true;
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

	/*METODO USATO PER TESTING*/
	@Override
	public ArrayList<UserSummaryDTO> getAllUsers() {
		Iterator<UserAccount> allUsers = PMF.get().find().type(UserAccount.class).returnResultsNow();
		ArrayList<UserAccount> allUsersList = new ArrayList<UserAccount>();
		while (allUsers.hasNext() == true)
			allUsersList.add(allUsers.next());
		ArrayList<UserSummaryDTO> allUsersDTO = new ArrayList<UserSummaryDTO>();
		for (UserAccount tmp:allUsersList)
			allUsersDTO.add(tmp.toUserSummaryDTO());
		return allUsersDTO;
	}

}
