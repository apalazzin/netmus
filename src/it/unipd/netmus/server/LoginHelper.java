/**
 * 
 */
package it.unipd.netmus.server;

import it.unipd.netmus.server.persistent.UserAccount;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginHelper extends RemoteServiceServlet {
	
	private static final int NUM_RETRIES = 5;

	static public UserAccount getLoggedInUser(HttpSession session, PersistenceManager pm) {

	    if (session == null)
	      return null; // user not logged in

	    String userId = (String) session.getAttribute("userId");
	    if (userId == null)
	      return null; // user not logged in

	    //cercare nel database l'userID e ritornare il corrispondente UserAccount
	    
	    return null;
	  }

	  static public boolean isLoggedIn(HttpServletRequest req) {

	    if (req == null)
	      return false;
	    else {
	      HttpSession session = req.getSession();
	      if (session == null) {
	        return false;
	      } else {
	        Boolean isLoggedIn = (Boolean) session.getAttribute("loggedin");
	        if(isLoggedIn == null){
	          return false;
	        } else if (isLoggedIn){
	          return true;
	        } else {
	          return false;
	        }
	      }
	    }
	  }

	  public UserAccount loginStarts(HttpSession session, UserAccount user) {

	    // update user info under transactional control
	    try {
	      for (int i = 0; i < NUM_RETRIES; i++) {
	        try {
	          // update session if successful
	          session.setAttribute("userId", "");
	          session.setAttribute("loggedin", true);
	          break;
	        }
	        catch (JDOCanRetryException e1) {
	          if (i == (NUM_RETRIES - 1)) { 
	            throw e1;
	          }
	        }
	      } // end for
	    } 
	    catch (JDOException e) {
	      e.printStackTrace();
	      return null;
	    } 
	    finally {
	      //if transaction alredy active... rollback
	      //close PM
	    }

	    return null;
	  }
}
