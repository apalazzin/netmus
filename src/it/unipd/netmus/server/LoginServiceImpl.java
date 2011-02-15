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
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;
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
	public void insertRegistration(LoginDTO login) throws RegistrationException {
		//create new user in the database
		UserAccount userAccount = new UserAccount(login.getUser(),login.getPassword());
		
		//persist the new user
		try {
			ODF.get().store().instance(userAccount).ensureUniqueKey().returnKeyNow();
		} catch (IllegalStateException e) {
			throw new RegistrationException();
		}
	}

	
	@Override
	public void verifyLogin(LoginDTO login) throws LoginException {
		
		//find user in the database
		UserAccount userAccount = ODF.get().load(UserAccount.class, login.getUser());

		if (userAccount == null) {
			//user not found in the database
			throw new WrongLoginException("L'utente non esiste");
		}
		else {
			if (login.getPassword().contentEquals(userAccount.getPassword())) {
				//correct password
				return;
			}
			else throw new WrongLoginException("pass inserita: "+login.getPassword()+" - pass salvata: "+userAccount.getPassword());
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
		Iterator<UserAccount> allUsers = ODF.get().find().type(UserAccount.class).returnResultsNow();
		ArrayList<UserAccount> allUsersList = new ArrayList<UserAccount>();
		while (allUsers.hasNext() == true)
			allUsersList.add(allUsers.next());
		ArrayList<UserSummaryDTO> allUsersDTO = new ArrayList<UserSummaryDTO>();
		for (UserAccount tmp:allUsersList)
			allUsersDTO.add(tmp.toUserSummaryDTO());
		return allUsersDTO;
	}

}
