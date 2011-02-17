/**
 * 
 */
package it.unipd.netmus.client.service;

import java.util.ArrayList;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserSummaryDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public interface LoginServiceAsync {

	void insertRegistration(LoginDTO login, AsyncCallback<LoginDTO> callback) throws RegistrationException;

	void verifyLogin(LoginDTO login, AsyncCallback<Void> callback) throws LoginException;

	void getLoggedInUserDTO(AsyncCallback<String> callback) throws LoginException;

	void logout(AsyncCallback<Void> callback);

	void startLogin(LoginDTO login, AsyncCallback<Void> callback);

	void getAllUsers(AsyncCallback<ArrayList<UserSummaryDTO>> callback);

    void restartSession(String session_id, AsyncCallback<Void> callback);

}
