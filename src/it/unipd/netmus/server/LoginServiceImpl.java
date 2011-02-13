/**
 * 
 */
package it.unipd.netmus.server;

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
		
		ObjectDatastore datastore = new AnnotationObjectDatastore(false);
		
		UserAccount userAccount = new UserAccount().findUser(login.getUser());
		if (userAccount.equals(null)) {
			userAccount = new UserAccount(login.getUser(),login.getPassword());
			datastore.store(userAccount);
			return true;
		}
		else return false;
	}

	@Override
	public boolean verifyLogin(LoginDTO login) {
		UserAccount userAccount = UserAccount.findUser(login.getUser());
		if (userAccount.equals(null)) {return false;}
		else {
			if (login.getPassword().equals(userAccount.getPassword())) {
				return true;
			}
			else return false;
		}
	}

	@Override
	public void startLogin(LoginDTO login) {
		UserAccount userAccount = UserAccount.findUser(login.getUser());
		HttpSession session = getThreadLocalRequest().getSession();
		LoginHelper.loginStarts(session, userAccount);
	}

	@Override
	public UserSummaryDTO getLoggedInUserDTO() {
	    UserSummaryDTO userDTO;
	    HttpSession session = getThreadLocalRequest().getSession();

	    UserAccount u = LoginHelper.getLoggedInUser(session, null);
	    if (u == null)
	      return null;
	    userDTO = UserAccount.toDTO(u);
	    return userDTO;
	}

	@Override
	public void logout() {
	    getThreadLocalRequest().getSession().invalidate();
	}

}
