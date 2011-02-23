/**
 * 
 */
package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.UserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public interface UsersServiceAsync {
    void loadProfile(String user, AsyncCallback<UserCompleteDTO> callback);
}
