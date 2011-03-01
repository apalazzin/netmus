package it.unipd.netmus.server;

import it.unipd.netmus.server.utils.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Nome: LoginHelper.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 17 Febbraio 2011
 * 
 */

@SuppressWarnings("serial")
public class LoginHelper extends RemoteServiceServlet {

    /**
     * Imposta una nuova sessione per l’utente in input.
     */
    static public String getApplicationURL(HttpServletRequest request) {

        if (isDevelopment(request)) {
            return "http://127.0.0.1:8888/Netmus.html?gwt.codesvr=127.0.0.1:9997";
        } else {
            return ServletUtils.getBaseUrl(request);
        }

    }

    /**
     * Legge dal database le informazioni relative all’utente specificato nella
     * HttpSession e ne restituisce l’user-name.
     */
    static public String getLoggedInUser(HttpSession session) {

        if (session == null) {
            return null; // user not logged in
        }

        String username = (String) session.getAttribute("userLoggedIn");
        if (username == null) {
            return null; // user not logged in
        }

        return username;
    }

    /**
     * Ritorna true solamente se l’applicazione sta eseguendo in development
     * mode altrimenti ritorna false.
     */
    public static boolean isDevelopment(HttpServletRequest request) {

        return SystemProperty.environment.value() != SystemProperty.Environment.Value.Production;

    }

    /**
     * Restituisce l’URL attuale dell’applicazione, anche nel caso sia eseguendo
     * in development mode.
     */
    static public void setSession(HttpSession session, String user) {

        session.setAttribute("userLoggedIn", user);

    }
}