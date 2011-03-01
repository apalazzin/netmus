package it.unipd.netmus.shared;

import java.io.Serializable;

/**
 * Nome: LoginDTO.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 * 
 * 
 * Tipo, obiettivo e funzione del componente:
 * 
 * Come tutte le altri classi di questo package questa classe permette lo
 * scambio di informazioni tra client e server all'interno delle chiamate RPC.
 * Contiene e rappresenta i dati di login di un utente.
 * 
 * 
 */

@SuppressWarnings("serial")
public class LoginDTO implements Serializable {

    private String user;
    private String password_hash;
    private String last_session_id;

    public LoginDTO() {

    }

    public LoginDTO(String user, String password_hash) {
        setUser(user);
        setPassword(password_hash);
    }

    public String getLastSessionId() {
        return last_session_id;
    }

    public String getPassword() {
        return password_hash;
    }

    public String getUser() {
        return user;
    }

    public void setLastSessionId(String last_session_id) {
        this.last_session_id = last_session_id;
    }

    public void setPassword(String password_hash) {
        this.password_hash = password_hash;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
