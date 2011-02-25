/**
 * 
 */
package it.unipd.netmus.client.service;

import java.util.List;

import it.unipd.netmus.shared.UserCompleteDTO;
import it.unipd.netmus.shared.UserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ValterTexasGroup
 *
 */
public interface UserServiceAsync {
    void loadProfile(String user, AsyncCallback<UserCompleteDTO> callback);

    void editProfile(String user, UserCompleteDTO new_info_user,
            AsyncCallback<Boolean> callback);

    void deleteProfile(String user, AsyncCallback<Boolean> callback);

    void findSimilarUser(String user, AsyncCallback<List<UserDTO>> callback);
}
