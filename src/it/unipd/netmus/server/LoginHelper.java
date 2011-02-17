/**
 * 
 */
package it.unipd.netmus.server;

import java.util.Date;
import java.util.logging.Logger;

import it.unipd.netmus.server.persistent.UserAccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginHelper extends RemoteServiceServlet {
	
	private static Logger logger = Logger.getLogger(LoginHelper.class.getName());
	
	static public String getLoggedInUser(HttpSession session) {

	    if (session == null) {
	    	logger.info("L'utente non è loggato");
	      	return null; // user not logged in
	    }

	    String username = (String) session.getAttribute("userLoggedIn");
	    if (username == null) {
	    	logger.info("L'utente non è loggato (sessione esistente)");
	    	return null; // user not logged in
	    }
	    
	    // non mi restituisce l'utente, che effettivamente esiste (username e' corretto)
	    //UserAccount user = UserAccount.findUser(username);

	    //return user;
	    return username;
	  }

	  static public boolean isLoggedIn(HttpServletRequest req) {

	    if (req == null)
	      return false;
	    else {
	      HttpSession session = req.getSession();
	      if (session == null) {
	    	  logger.info("Nessuna sessione aperta al momento");
	    	  return false;
	      } else {
	        String user = (String) session.getAttribute("userLoggedIn");
	        if (user != null){
	        	logger.info("L'utente è loggato");
	        	return true;
	        } else {
	        	logger.info("L'utente non è loggato");
	        	return false;
	        }
	      }
	    }
	  }

	  static public void loginStarts(HttpSession session, UserAccount user) {
	     try {
	        session.setAttribute("userLoggedIn", user.getUser());
	        
	        // create a cookie for this section
	        String sid = session.getId();
	        final long DURATION = 1000 * 60 * 60 * 24;
	        Date expires = new Date(System.currentTimeMillis() + DURATION);
	        Cookies.setCookie("sid", sid, expires, null, "/", false);
	        
	     } catch (Exception e) {
	        logger.severe("Eccezione loginStarts");
	     }
	     
		  
	  }
}