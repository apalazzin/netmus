/**
 * 
 */
package it.unipd.netmus.server.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public abstract class LoginSuperServlet extends HttpServlet {
   
   public LoginSuperServlet() {
      super();
    }

    protected String buildCallBackURL(HttpServletRequest request) {
      StringBuffer requestURL = request.getRequestURL();
      String callbackURL = requestURL.toString();
      callbackURL += "callback";
      // System.out.println("callback url: " + callbackURL);
      return callbackURL;
    }

}
