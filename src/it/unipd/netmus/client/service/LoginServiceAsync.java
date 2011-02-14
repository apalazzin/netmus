/**
 * 
 */
package it.unipd.netmus.client.service;

import java.util.ArrayList;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserSummaryDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public interface LoginServiceAsync {

	void insertRegistration(LoginDTO login, AsyncCallback<Void> callback) throws IllegalStateException;

	void verifyLogin(LoginDTO login, AsyncCallback<Boolean> callback);

	void getLoggedInUserDTO(AsyncCallback<UserSummaryDTO> callback);

	void logout(AsyncCallback<Void> callback);

	void startLogin(LoginDTO login, AsyncCallback<Boolean> callback);

	void getAllUsers(AsyncCallback<ArrayList<UserSummaryDTO>> callback);

}
