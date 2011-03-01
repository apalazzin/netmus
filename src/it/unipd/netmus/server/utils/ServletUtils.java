package it.unipd.netmus.server.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Nome: ServletUtils.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 17 Febbraio 2011
 */

public class ServletUtils {

    /**
     * Questo metodo deve tornare l’indirizzo di base dell’applicazione, a
     * seconda di dove è hostato il progetto e da che porta viene acceduto.
     */
    public static String getBaseUrl(HttpServletRequest request) {
        if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
            return request.getScheme() + "://" + request.getServerName()
                    + request.getContextPath();
        else
            return request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath();
    }

}
