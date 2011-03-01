package it.unipd.netmus.server.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Nome: LoginSuperServlet.java
 * Autore:  VT.G
 * Licenza: GNU GPL v3
 * Data Creazione: 17 Febbraio 2011
 *
 */
@SuppressWarnings("serial")
public abstract class LoginSuperServlet extends HttpServlet {
   
   public LoginSuperServlet() {
      super();
    }

   /**
    * Crea l’indirizzo richiedente, per login.
    */
    protected String buildCallBackURL(HttpServletRequest request) {
      StringBuffer requestURL = request.getRequestURL();
      String callbackURL = requestURL.toString();
      callbackURL += "callback";
      return callbackURL;
    }

}
