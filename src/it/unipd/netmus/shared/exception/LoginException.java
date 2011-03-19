package it.unipd.netmus.shared.exception;

/**
 * Nome: LoginException.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 */
@SuppressWarnings("serial")
public class LoginException extends NetmusException {

    public LoginException() {
        super();
    }

    public LoginException(String more_info) {
        super(more_info);
    }

}
