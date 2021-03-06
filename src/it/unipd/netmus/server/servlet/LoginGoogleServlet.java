package it.unipd.netmus.server.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Nome: LoginGoogleServlet.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 17 Febbraio 2011
 */
@SuppressWarnings("serial")
public class LoginGoogleServlet extends LoginSuperServlet {

    /**
     * Crea ed invia una richiesta al servizio di login Google per fargli
     * mostrare la pagina di login predefinita per i Google Account.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String callbackURL = buildCallBackURL(request);
        UserService userService = UserServiceFactory.getUserService();
        String googleLoginUrl = userService.createLoginURL(callbackURL);
        response.sendRedirect(googleLoginUrl);
    }
}
