package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.UserCompleteDTO;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Nome: UserService.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */
@RemoteServiceRelativePath("usersService")
public interface UserService extends RemoteService {

    public UserCompleteDTO loadProfile(String user);

    boolean deleteProfile(String user);

    boolean editProfile(String user, UserCompleteDTO new_info_user);

    List<String> findRelatedUsers(String user);

}
