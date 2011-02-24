/**
 * 
 */
package it.unipd.netmus.server.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author ValterTexasGroup
 *
 */
@SuppressWarnings("serial")
public class LoginGoogleServlet extends LoginSuperServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String callbackURL = buildCallBackURL(request);
        UserService userService = UserServiceFactory.getUserService();
        String googleLoginUrl = userService.createLoginURL(callbackURL);
        //System.out.println("Going to Google login URL: " + googleLoginUrl);
        response.sendRedirect(googleLoginUrl);
    }
}
