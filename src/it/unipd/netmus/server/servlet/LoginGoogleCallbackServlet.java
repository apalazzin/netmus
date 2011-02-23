/**
 * 
 */
package it.unipd.netmus.server.servlet;

import it.unipd.netmus.server.LoginHelper;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.exception.NetmusException;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginGoogleCallbackServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        Principal googleUser = request.getUserPrincipal();
        if (googleUser != null) {
            
            try {
                // REGISTRO IL GOOGLE USER (COME GOOGLE USER) E LO LOGGO
                // controllo se esiste gia` in DB
                UserAccount userAccount = UserAccount.load(googleUser.getName());
                if (userAccount == null) {
                    // creo l'utente google nel db
                    userAccount = new UserAccount(googleUser.getName(), "");
                    userAccount.setGoogleUser(true);
                    
                } else {
                    if (!userAccount.isGoogleUser()) {
                        // non e' lui non e' possibile registrarlo con Google con quel nome
                        throw new NetmusException("UTENTE NETMUS con STESSO USERNAME/MAIL");
                    }
                }
                //loggo , sessione (non uso i cookies per un utente non Netmus)
                
                HttpSession session = request.getSession();
                String session_id = session.getId();
                // set session parameter - userID
                LoginHelper.setSession(session, googleUser.getName());
                // set in DB userAccount the new SessionID
                userAccount.setLastSessionId(session_id);
            }
            catch (NetmusException ne) {
                System.out.println(ne.getMoreInfo());
            }
        }
        // TORNO AL ENTRY POINT E SE E' STATO LOGGATO PASSA IN AUTOMATICO AL PROFILE
        response.sendRedirect(LoginHelper.getApplicationURL(request));
    }
}
