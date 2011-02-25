/**
 * 
 */
package it.unipd.netmus.server.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ValterTexasGroup
 *
 */
public class ServletUtils {
    
    /**
     * NOT UNIT TESTED Returns the base url (e.g, <tt>http://myhost:8080/myapp</tt>) suitable for using in a base tag or building
     * reliable urls.
     */
    public static String getBaseUrl(HttpServletRequest request) {
      if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
        return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
      else
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath();
    }

}
