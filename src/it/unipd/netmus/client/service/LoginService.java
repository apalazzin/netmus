/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author ValterTexasGroup
 *
 */
@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService {
   
	LoginDTO insertRegistration(LoginDTO login) throws RegistrationException;
	
	String startLogin(LoginDTO login) throws LoginException;
	
	String getLoggedInUser() throws LoginException;
	
	String logout();
	
	String restartSession(String user, String session_id) throws LoginException;

}