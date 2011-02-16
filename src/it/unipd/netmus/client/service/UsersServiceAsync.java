/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.UserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public interface UsersServiceAsync {
	public void loadProfile(String user,String password, AsyncCallback<UserDTO> callback);
}
