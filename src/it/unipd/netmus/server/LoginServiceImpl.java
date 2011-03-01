package it.unipd.netmus.server;

import javax.servlet.http.HttpSession;

import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.server.utils.BCrypt;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Nome: LoginServiceImpl.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 13 Febbraio 2011
 *
 */

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
      LoginService {
   
	/**
	 * Inserisce i dati di login relativi alla registrazione di un nuovo utente nel
     * database. Può lanciare eccezioni di tipo <= RegistrationException in
     * caso vi siano problemi nell’inserimento.
	 */
	@Override
	public LoginDTO insertRegistration(LoginDTO login) throws RegistrationException {
		
	    String passwordHash = BCrypt.hashpw(login.getPassword(), BCrypt.gensalt());
		
        //create new user in the database
	    try {
	        new UserAccount(login.getUser(), passwordHash);
	    }
	    catch (Exception e) {
	        throw new RegistrationException();
	    }
        
        return login;

	}
	
	/**
	 * Metodo fondamentale per l’autenticazione che confronta i dati in input con quelli 
	 * presenti nel database per verificare se il login è valido. Può lanciare eccezioni 
	 * di tipo <= LoginException in caso il controllo non vada a buon fine.
	 */
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
			    throw new LoginException();
		}
	}

	/**
	 * Racchiude tutte i passi di autenticazione dell’utente. Utilizza verifyLogin e si 
	 * occupa di iniziare la sessione dell’utente che si è autenticato.
	 */
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

	/**
	 * Esegue una ricerca della sessione corrente utilizzando i metodi di
     * LoginHelper e ritorna, se presenti, le informazioni di base (UserSummaryDTO) 
     * dell’utente loggato.
	 */
	@Override
	public String getLoggedInUser() throws LoginException {
	    
	    HttpSession session = getThreadLocalRequest().getSession();
	    String user = LoginHelper.getLoggedInUser(session);
	    if (user == null) {
	        throw new LoginException();
	    }
	    return user;
	    
	}

	/**
	 * Invalida l’autenticazione dell’utente che viene rimandato alla pagina di
     * login.
	 */
	@Override
	public String logout() {
	    HttpSession session = getThreadLocalRequest().getSession();
	    String user = (String) session.getAttribute("userLoggedIn");
	    session.invalidate();
	    return user;
	}

    /**
     * Recupera un eventuale sessione salvata nei cookie controllandone la validità 
     * per l’utente dato in input. Questa funzionalità non è valida per gli utenti
     * Google dei quali non viene gestito un cookie.
     */
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