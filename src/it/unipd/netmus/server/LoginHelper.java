/**
 * 
 */
package it.unipd.netmus.server;

import it.unipd.netmus.server.utils.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginHelper extends RemoteServiceServlet {
	
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

	static public void setSession(HttpSession session, String user) {
	      
	    session.setAttribute("userLoggedIn", user);
	    
	}
	  
	static public String getApplicationURL(HttpServletRequest request) {

	    if (isDevelopment(request)) {
	        return "http://127.0.0.1:8888/Netmus.html?gwt.codesvr=127.0.0.1:9997";
	    } else {
	        return ServletUtils.getBaseUrl(request);
	    }

	}
	
	public static boolean isDevelopment(HttpServletRequest request) {
	    
	    return SystemProperty.environment.value() != SystemProperty.Environment.Value.Production;
	    
	}
}