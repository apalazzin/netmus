/**
 * 
 */
package it.unipd.netmus.server;

import javax.servlet.http.HttpSession;

import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.server.utils.BCrypt;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.exception.LoginException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
      LoginService {
   
	@Override
	public LoginDTO insertRegistration(LoginDTO login) throws IllegalStateException {
		
	    String passwordHash = BCrypt.hashpw(login.getPassword(), BCrypt.gensalt());
		
        //create new user in the database
        new UserAccount(login.getUser(), passwordHash);
        
        return login;

	}
	
	private UserAccount verifyLogin(LoginDTO login) throws LoginException {
		
		//find user in the database
	    String username = login.getUser();
	    if (username==null || username.equals(""))
	        throw new LoginException("User empty");
		UserAccount userAccount = UserAccount.load(login.getUser());

		if (userAccount == null) {
			//user not found in the database
			throw new LoginException("User don't exists");
		}
		else {
			if (BCrypt.checkpw(login.getPassword(), userAccount.getPassword())) {
				//correct password
				return userAccount;
			}
			else
			    throw new LoginException("pass inserita: "+login.getPassword()+" - pass salvata: "+userAccount.getPassword());
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
            UserAccount userAccount = UserAccount.load(user);
            if (userAccount == null)
                throw new LoginException();
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