/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public interface LoginServiceAsync {

	void insertRegistration(LoginDTO login, AsyncCallback<LoginDTO> callback) throws RegistrationException;

	void getLoggedInUser(AsyncCallback<String> callback) throws LoginException;

	void logout(AsyncCallback<String> callback);

	void startLogin(LoginDTO login, AsyncCallback<String> callback);

    void restartSession(String user, String session_id, AsyncCallback<String> callback)  throws LoginException;

}
