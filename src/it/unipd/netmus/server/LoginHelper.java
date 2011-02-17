/**
 * 
 */
package it.unipd.netmus.server;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

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
	    	logger.info("Invalid Session");
	      	return null; // user not logged in
	    }

	    String username = (String) session.getAttribute("userLoggedIn");
	    if (username == null) {
	    	logger.info("User not logged");
	    	return null; // user not logged in
	    }

	    return username;
	  }

	  static public void setSession(HttpSession session, String user) {
	      
	      session.setAttribute("userLoggedIn", user);
	  }
}