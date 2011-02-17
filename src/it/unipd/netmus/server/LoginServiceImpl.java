/**
 * 
 */
package it.unipd.netmus.server;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.server.utils.BCrypt;
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
	public LoginDTO insertRegistration(LoginDTO login) throws RegistrationException {
		
	    String passwordHash = BCrypt.hashpw(login.getPassword(), BCrypt.gensalt());
		
		//create new user in the databas
		UserAccount userAccount = new UserAccount(login.getUser(), passwordHash);
		
		//persist the new user
		try {
			ODF.get().store().instance(userAccount).ensureUniqueKey().returnKeyNow();
			return login;
		} catch (IllegalStateException e) {
			throw new RegistrationException();
		}
	}
	
	private UserAccount verifyLogin(LoginDTO login) throws LoginException {
		
		//find user in the database
		UserAccount userAccount = UserAccount.loadUserWithoutLibrary(login.getUser());

		if (userAccount == null) {
			//user not found in the database
			throw new WrongLoginException("User don't exists");
		}
		else {
			if (BCrypt.checkpw(login.getPassword(), userAccount.getPassword())) {
				//correct password
				return userAccount;
			}
			else
			    throw new WrongLoginException("pass inserita: "+login.getPassword()+" - pass salvata: "+userAccount.getPassword());
		}
	}

	@Override
	public String startLogin(LoginDTO login) throws LoginException {
	    
		//find user in the database
		UserAccount userAccount = verifyLogin(login);
		
		HttpSession session = getThreadLocalRequest().getSession();
		String session_id = session.getId();

		// set session parameter - userID
		LoginHelper.setSession(session, login.getUser());
		// set in DB userAccount the new SessionID
		userAccount.setLastSessionId(session_id);
		
		return session_id;
	}

	@Override
	public String getLoggedInUser() throws LoginException {
	    
	    HttpSession session = getThreadLocalRequest().getSession();
	    String user = LoginHelper.getLoggedInUser(session);
	    if (user == null) {
	        throw new LoginException();
	    }
	    return user;
	    
	}

	@Override
	public String logout() {
	    HttpSession session = getThreadLocalRequest().getSession();
	    String user = (String) session.getAttribute("userLoggedIn");
	    session.invalidate();
	    return user;
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

    @Override
    public String restartSession(String user, String session_id) throws LoginException {
        
        HttpSession session = getThreadLocalRequest().getSession();
        String session_id_new = session.getId();
        String userLoggedIn = (String) session.getAttribute("userLoggedIn");
        
        if (userLoggedIn != null)
            // deve aggionare i cookie pero' (refresh)
            return session_id_new;
        else {
            if (user==null || session_id == null)
                // non deve fare niente sui cookie, non esistono
                throw new LoginException();
            
            // restart old session by Cookies (se soddisfa)
            UserAccount userAccount = UserAccount.loadUserWithoutLibrary(user);
            String session_id_old = userAccount.getLastSessionId();
            
            if (session_id_old.equals(session_id)) {
                // caricare attributi in nuova session
                session.setAttribute("userLoggedIn", user);
                
                // aggiornare il campo lastSessionId
                userAccount.setLastSessionId(session_id_new);
                
                // deve aggionare i cookie pero' (refresh)
                return session_id_new;
                
            } else {
                // non c'e' sessione, ci sono cookie, ma son estranei (altra macchina)
                throw new LoginException();
            }
        }
    }
}