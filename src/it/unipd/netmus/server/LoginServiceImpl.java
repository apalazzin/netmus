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
import it.unipd.netmus.shared.exception.WrongLoginException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
      LoginService {
	
	@Override
	public void insertRegistration(LoginDTO login) throws IllegalStateException {
		//create new user in the database
		UserAccount userAccount = new UserAccount(login.getUser(),login.getPassword());
		PMF.get().store().instance(userAccount).ensureUniqueKey().returnKeyNow();
	}

	
	@Override
	public void verifyLogin(LoginDTO login) throws WrongLoginException {
		
		//find user in the database
		UserAccount userAccount = PMF.get().load(UserAccount.class, login.getUser());

		if (userAccount == null) {
			//user not found in the database
			throw new WrongLoginException();
		}
		else {
			if (login.getPassword() == userAccount.getPassword()) {
				//correct password
				return;
			}
			else throw new WrongLoginException();
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
