/**
 * 
 */
package it.unipd.netmus.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserSummaryDTO;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
      LoginService {

	private static Logger logger = Logger.getLogger(LoginHelper.class.getName());
	
	@Override
	public boolean insertRegistration(LoginDTO login) {
		
		ObjectDatastore datastore = new AnnotationObjectDatastore(false);
		
		UserAccount userAccount = UserAccount.findUser(login.getUser());
		if (userAccount.equals(null)) {
			logger.info("Inserimento nel database del nuovo utente: " + login.getUser());
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
				logger.info("Account verificata con successo: " + login.getUser());
				return true;
			}
			else return false;
		}
	}

	@Override
	public boolean startLogin(LoginDTO login) {
		/*if (verifyLogin(login)) {
			UserAccount userAccount = UserAccount.findUser(login.getUser());
			HttpSession session = getThreadLocalRequest().getSession();
			logger.info("Inizio sessione: " + login.getUser());
			LoginHelper.loginStarts(session, userAccount);
			return true;
		}
		logger.info("Account non presente nel database: " + login.getUser());*/
		if (login.getUser().equals("lol"))
			return true;
		else return false;
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

}
