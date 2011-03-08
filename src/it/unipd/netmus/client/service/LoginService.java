package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Nome: LoginService.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */

@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService {

    String getLoggedInUser() throws LoginException;

    LoginDTO insertRegistration(LoginDTO login) throws RegistrationException;

    String logout();

    String restartSession(String user, String session_id) throws LoginException;

    String startLogin(LoginDTO login) throws LoginException;

}