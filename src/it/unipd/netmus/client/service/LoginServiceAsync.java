package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Nome: LoginServiceAsync.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */
public interface LoginServiceAsync {

    void getLoggedInUser(AsyncCallback<String> callback) throws LoginException;

    void insertRegistration(LoginDTO login, AsyncCallback<LoginDTO> callback)
            throws RegistrationException;

    void logout(AsyncCallback<String> callback);

    void restartSession(String user, String session_id,
            AsyncCallback<String> callback) throws LoginException;

    void startLogin(LoginDTO login, AsyncCallback<String> callback);

}
