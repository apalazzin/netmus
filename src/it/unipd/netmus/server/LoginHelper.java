/**
 * 
 */
package it.unipd.netmus.server;

import java.util.Date;
import java.util.logging.Logger;

import it.unipd.netmus.server.persistent.UserAccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginHelper extends RemoteServiceServlet {
	
	private static Logger logger = Logger.getLogger(LoginHelper.class.getName());
	
	static public UserAccount getLoggedInUser(HttpSession session) {

	    if (session == null) {
	    	logger.info("L'utente non è loggato");
	      	return null; // user not logged in
	    }

	    String userId = (String) session.getAttribute("userId");
	    if (userId == null) {
	    	logger.info("L'utente non è loggato");
	    	return null; // user not logged in
	    }

	    UserAccount user = UserAccount.findUser(userId);
	    return user;
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
	        Boolean isLoggedIn = (Boolean) session.getAttribute("loggedin");
	        if(isLoggedIn == null){
	        	logger.info("Non si sa se l'utente sia loggato");
	        	return false;
	        } else if (isLoggedIn){
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
		  ODF.get().associate(user);
		  user.setLastLogin(new Date());
		  ODF.get().update(user);
		  
		  /*session.setAttribute("userId", "");
		  session.setAttribute("loggedin", true);*/
	  }
}