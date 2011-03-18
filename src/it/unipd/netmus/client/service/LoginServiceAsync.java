package it.unipd.netmus.client.service;

import it.unipd.netmus.shared.LoginDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Nome: LoginServiceAsync.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */
public interface LoginServiceAsync {

    void getLoggedInUser(AsyncCallback<String> callback);

    void insertRegistration(LoginDTO login, AsyncCallback<LoginDTO> callback);

    void logout(AsyncCallback<String> callback);

    void restartSession(String user, String session_id,
            AsyncCallback<String> callback);

    void startLogin(LoginDTO login, AsyncCallback<String> callback);

}
