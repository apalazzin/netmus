/**
 * 
 */
package it.unipd.netmus.client.ui;

import it.unipd.netmus.client.ui.LoginView.Presenter.LoginType;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * 
 * 
 * @author ValterTexasGroup
 *
 */
public interface LoginView extends IsWidget {
   
   void setPresenter(Presenter listener);
   void setUser(String user);
   void setPassword(String passwordHash);
   void setError(String error);
   void setLoginType(LoginType loginType);
   //aggiusta il layout all'avvio
   void setLayout();
   
   public interface Presenter
   {
      void goTo(Place place);
      void sendLogin(String login, String password) throws LoginException;
      void sendRegistration(String login, String password, String confirmPassword) throws RegistrationException;
      enum LoginType {NETMUSLOGIN, NETMUSREGISTRATION, GOOGLELOGIN};
   }
}
