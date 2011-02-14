/**
 * 
 */
package it.unipd.netmus.client.service;

import java.util.ArrayList;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserSummaryDTO;
import it.unipd.netmus.shared.exception.WrongLoginException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author ValterTexasGroup
 *
 */
@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService {
   
	void insertRegistration(LoginDTO login) throws IllegalStateException;
	
	void verifyLogin(LoginDTO login) throws WrongLoginException;
	
	boolean startLogin(LoginDTO login);
	
	UserSummaryDTO getLoggedInUserDTO();
	
	void logout();
	
	/*METODO USATO PER TESTING*/
	ArrayList<UserSummaryDTO> getAllUsers();

}
