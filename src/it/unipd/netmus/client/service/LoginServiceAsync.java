/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserSummaryDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public interface LoginServiceAsync {

	void insertRegistration(LoginDTO login, AsyncCallback<Boolean> callback);

	void verifyLogin(LoginDTO login, AsyncCallback<Boolean> callback);

	void startLogin(LoginDTO login, AsyncCallback<Void> callback);

	void getLoggedInUserDTO(AsyncCallback<UserSummaryDTO> callback);

	void logout(AsyncCallback<Void> callback);

}
