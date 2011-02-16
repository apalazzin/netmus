/**
 * 
 */
package it.unipd.netmus.client.service;

import java.util.ArrayList;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserSummaryDTO;
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
   
	void insertRegistration(LoginDTO login) throws RegistrationException;
	
	void verifyLogin(LoginDTO login) throws LoginException;
	
	void startLogin(LoginDTO login) throws LoginException;
	
	UserSummaryDTO getLoggedInUserDTO();
	
	void logout();
	
	/*METODO USATO PER TESTING*/
	ArrayList<UserSummaryDTO> getAllUsers();

}